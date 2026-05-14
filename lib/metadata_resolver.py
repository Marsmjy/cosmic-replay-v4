"""metadata_resolver.py — 苍穹实体元数据实时查询

通过 getEntityType.do 接口获取实体的字段定义（标签、类型、约束等），
为 HAR 转换阶段提供精确的字段元数据。

设计原则：
- 渐进增强：查询失败静默降级返回 None，不阻断主流程
- 缓存优先：同一实体只查询一次（含失败缓存）
- 最小提取：从响应中只提取需要的属性，减少内存占用
"""
from __future__ import annotations

import logging
from dataclasses import dataclass, field
from typing import Any

import urllib3
urllib3.disable_warnings()
import requests

__all__ = ["FieldMeta", "MetadataResolver"]

log = logging.getLogger(__name__)


# =============================================================
# 数据结构
# =============================================================
@dataclass
class FieldMeta:
    """单个字段的元数据"""

    name: str  # 字段key（Properties 中的 Name）
    label: str  # DisplayName.zh_CN（中文标签）
    type: str  # _Type_（如 TextProp、BasedataProp、ComboProp）
    required: bool  # MustInput 是否必填
    max_length: int | None = None  # MaxLenth（注意苍穹的拼写）
    base_entity: str | None = None  # BasedataProp 时关联的 BaseEntityId
    combo_items: dict[str, str] | None = None  # ComboItems 的 {Value: Name.zh_CN} 映射


# =============================================================
# 解析器
# =============================================================
class MetadataResolver:
    """苍穹实体元数据解析器

    调用 getEntityType.do 接口获取实体字段定义，并缓存结果。
    所有查询异常静默降级返回 None，不抛出。
    """

    def __init__(self, base_url: str, cookie: str, csrf_token: str) -> None:
        """初始化连接信息

        Args:
            base_url: 苍穹服务地址（如 https://xxx.kingdee.com/ierp）
            cookie: 登录后的完整 Cookie 字符串
            csrf_token: kd-csrf-token
        """
        self._base_url = base_url.rstrip("/")
        self._cookie = cookie
        self._csrf_token = csrf_token
        # entity_id → {field_key: FieldMeta} 或 None（表示查询失败，避免重试）
        self._cache: dict[str, dict[str, FieldMeta] | None] = {}

    # ---------------------------------------------------------
    # 公开接口
    # ---------------------------------------------------------
    def get_entity_fields(self, entity_id: str) -> dict[str, FieldMeta] | None:
        """查询实体所有字段，结果缓存

        Args:
            entity_id: 苍穹实体标识（如 hom_onbrdinfo）

        Returns:
            {field_key: FieldMeta} 字典，失败返回 None
        """
        if entity_id in self._cache:
            return self._cache[entity_id]

        result = self._fetch_and_parse(entity_id)
        self._cache[entity_id] = result
        return result

    def get_field_label(self, entity_id: str, field_key: str) -> str | None:
        """获取单个字段中文标签"""
        fields = self.get_entity_fields(entity_id)
        if fields is None:
            return None
        meta = fields.get(field_key.lower())
        return meta.label if meta else None

    def get_field_type(self, entity_id: str, field_key: str) -> str | None:
        """获取字段类型"""
        fields = self.get_entity_fields(entity_id)
        if fields is None:
            return None
        meta = fields.get(field_key.lower())
        return meta.type if meta else None

    def is_required(self, entity_id: str, field_key: str) -> bool | None:
        """字段是否必填"""
        fields = self.get_entity_fields(entity_id)
        if fields is None:
            return None
        meta = fields.get(field_key.lower())
        return meta.required if meta else None

    def get_base_entity(self, entity_id: str, field_key: str) -> str | None:
        """获取基础资料关联实体ID"""
        fields = self.get_entity_fields(entity_id)
        if fields is None:
            return None
        meta = fields.get(field_key.lower())
        return meta.base_entity if meta else None

    # ---------------------------------------------------------
    # 内部实现
    # ---------------------------------------------------------
    def _fetch_and_parse(self, entity_id: str) -> dict[str, FieldMeta] | None:
        """请求 getEntityType.do 并解析响应"""
        try:
            url = f"{self._base_url}/metadata/getEntityType.do?entityId={entity_id}"
            headers = {
                "Cookie": self._cookie,
                "kd-csrf-token": self._csrf_token,
                "X-Requested-With": "XMLHttpRequest",
            }
            resp = requests.get(url, headers=headers, timeout=5, verify=False)

            if resp.status_code != 200:
                log.warning(
                    "[MetadataResolver] HTTP %d for entity=%s", resp.status_code, entity_id
                )
                return None

            data = resp.json()
            properties = data.get("Properties")
            if not isinstance(properties, list):
                log.warning(
                    "[MetadataResolver] No Properties array for entity=%s", entity_id
                )
                return None

            return self._parse_properties(properties)

        except requests.exceptions.Timeout:
            log.warning("[MetadataResolver] Timeout for entity=%s", entity_id)
            return None
        except requests.exceptions.RequestException as e:
            log.warning("[MetadataResolver] Request error for entity=%s: %s", entity_id, e)
            return None
        except (ValueError, KeyError, TypeError) as e:
            log.warning("[MetadataResolver] Parse error for entity=%s: %s", entity_id, e)
            return None
        except Exception as e:
            log.warning("[MetadataResolver] Unexpected error for entity=%s: %s", entity_id, e)
            return None

    def _parse_properties(
        self, properties: list[dict[str, Any]], prefix: str = ""
    ) -> dict[str, FieldMeta]:
        """解析 Properties 数组，支持递归展开 EntryProp 子表

        Args:
            properties: 字段属性列表
            prefix: 嵌套时的字段名前缀（如 "entryentity."）

        Returns:
            {field_key: FieldMeta} 字典
        """
        result: dict[str, FieldMeta] = {}

        for prop in properties:
            if not isinstance(prop, dict):
                continue

            name = prop.get("Name")
            if not name:
                continue

            field_type = prop.get("_Type_", "")
            full_key = f"{prefix}{name}".lower()

            # 提取中文标签
            label = self._extract_label(prop)

            # 提取必填标记
            required = bool(prop.get("MustInput", False))

            # 提取最大长度
            max_length = prop.get("MaxLenth")
            if max_length is not None:
                try:
                    max_length = int(max_length)
                except (ValueError, TypeError):
                    max_length = None

            # 提取基础资料关联实体
            base_entity = prop.get("BaseEntityId") or None

            # 提取 ComboItems / StatusItems
            combo_items = self._extract_combo_items(prop)

            meta = FieldMeta(
                name=full_key,
                label=label,
                type=field_type,
                required=required,
                max_length=max_length,
                base_entity=base_entity,
                combo_items=combo_items,
            )
            result[full_key] = meta

            # 递归处理 EntryProp 内嵌 Properties
            sub_properties = prop.get("Properties")
            if isinstance(sub_properties, list) and sub_properties:
                nested = self._parse_properties(sub_properties, prefix=f"{full_key}.")
                result.update(nested)

        return result

    def _extract_label(self, prop: dict[str, Any]) -> str:
        """从 DisplayName 提取 zh_CN 标签，缺失时返回空字符串"""
        display_name = prop.get("DisplayName")
        if isinstance(display_name, dict):
            return display_name.get("zh_CN", "")
        return ""

    def _extract_combo_items(self, prop: dict[str, Any]) -> dict[str, str] | None:
        """提取 ComboItems 或 StatusItems 为 {Value: zh_CN} 映射"""
        items = prop.get("ComboItems") or prop.get("StatusItems")
        if not isinstance(items, list) or not items:
            return None

        mapping: dict[str, str] = {}
        for item in items:
            if not isinstance(item, dict):
                continue
            value = item.get("Value")
            if value is None:
                continue
            name_obj = item.get("Name")
            if isinstance(name_obj, dict):
                label = name_obj.get("zh_CN", "")
            elif isinstance(name_obj, str):
                label = name_obj
            else:
                label = ""
            mapping[str(value)] = label

        return mapping if mapping else None
