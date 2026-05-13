"""知识库懒加载器（Step 2 新增）

入口：
- resolve_scene(form_id) -> dict|None
    返回 {"name","domain","menu_paths","main_entity","physical_table","fields"} 或 None
- classify_field(form_id, field_key) -> "A"|"B"|"C"|"ignore"|None
    A 必须变量化（自由输入唯一键/名称）
    B 保留字面量（基础资料/枚举，可在 WebUI 变量面板展示）
    C 响应回传（跨 step 引用）
    ignore 系统字段（不管）
    None 未覆盖（交回原启发式）
- all_form_ids() -> set[str]

数据源：
- skills/cosmic-hr-expert/knowledge/scenarios/<form_id>/scenario.json
- skills/cosmic-hr-expert/knowledge/scenarios/<form_id>/scene_doc_lite.json
- skills/cosmic-hr-expert/knowledge/_cloud_index.json

设计约束：
- 所有加载失败静默回落 None，不破坏 har_extractor 现有路径
- 单场景数据按需加载 + 进程内缓存
"""
from __future__ import annotations

import json
from pathlib import Path
from typing import Any

# ---------- 内部状态 ----------

_SCENE_ROOT: Path | None = None
_SCENE_INDEX: dict[str, Path] | None = None  # form_id → scenario 目录
_SCENE_CACHE: dict[str, dict[str, Any] | None] = {}  # form_id → 场景摘要 or None
_CLOUD_INDEX: dict[str, str] | None = None  # form_id → cloud 名称
_LOADED = False


def _kb_root() -> Path:
    return (Path(__file__).resolve().parent.parent
            / "skills" / "cosmic-hr-expert" / "knowledge")


def _init_indices() -> None:
    """扫描 scenarios/ 目录构建 form_id → 场景目录的倒排索引。只做一次。"""
    global _SCENE_ROOT, _SCENE_INDEX, _CLOUD_INDEX, _LOADED
    if _LOADED:
        return
    _LOADED = True

    scenarios_dir = _kb_root() / "scenarios"
    _SCENE_INDEX = {}
    if scenarios_dir.is_dir():
        for entry in scenarios_dir.iterdir():
            if entry.is_dir():
                _SCENE_INDEX[entry.name] = entry
        _SCENE_ROOT = scenarios_dir

    # cloud 索引（辅助：判断 form_id 属于哪个云）
    _CLOUD_INDEX = {}
    cloud_file = _kb_root() / "_cloud_index.json"
    if cloud_file.is_file():
        try:
            data = json.loads(cloud_file.read_text(encoding="utf-8"))
            clouds = data.get("clouds", {}) or {}
            for cloud_key, cloud_info in clouds.items():
                cn_name = (cloud_info or {}).get("name", cloud_key)
                for sid in (cloud_info or {}).get("scenes", []) or []:
                    if sid:
                        _CLOUD_INDEX[sid] = cn_name
        except Exception:
            pass


def all_form_ids() -> set[str]:
    """返回所有知识库覆盖的 form_id（供回流检测用）"""
    _init_indices()
    return set(_SCENE_INDEX or {})


# ---------- 场景信息 ----------

def _load_scene(form_id: str) -> dict[str, Any] | None:
    """加载单场景的摘要，含字段元数据。结果缓存。
    未命中返回 None。
    """
    _init_indices()
    if form_id in _SCENE_CACHE:
        return _SCENE_CACHE[form_id]

    if not _SCENE_INDEX or form_id not in _SCENE_INDEX:
        _SCENE_CACHE[form_id] = None
        return None

    scene_dir = _SCENE_INDEX[form_id]
    summary: dict[str, Any] = {
        "name": "",
        "domain": "",
        "menu_paths": [],
        "main_entity": "",
        "physical_table": "",
        "fields": {},   # field_key → {t, req, lk, ref, mf, col}
    }

    # scenario.json
    sj = scene_dir / "scenario.json"
    if sj.is_file():
        try:
            data = json.loads(sj.read_text(encoding="utf-8"))
            summary["name"] = data.get("name", "") or ""
            summary["domain"] = data.get("domain") or data.get("domain_label") or ""
            summary["menu_paths"] = data.get("menu_paths", []) or []
        except Exception:
            pass

    # scene_doc_lite.json
    sdl = scene_dir / "scene_doc_lite.json"
    if sdl.is_file():
        try:
            data = json.loads(sdl.read_text(encoding="utf-8"))
            basic = data.get("basicInfo", {}) or {}
            summary["main_entity"] = basic.get("mainEntity", "") or ""
            summary["physical_table"] = basic.get("physicalTable", "") or ""
            fields: dict[str, dict] = {}
            for f in data.get("fields", []) or []:
                k = (f or {}).get("n")
                if not k:
                    continue
                fields[k] = {
                    "t": f.get("t", ""),
                    "req": f.get("req", 0),
                    "lk": f.get("lk", 0),
                    "ref": f.get("ref", ""),
                    "mf": f.get("mf", ""),
                    "col": f.get("col", ""),
                }
            summary["fields"] = fields
        except Exception:
            pass

    _SCENE_CACHE[form_id] = summary
    return summary


def resolve_scene(form_id: str) -> dict[str, Any] | None:
    """对外：查场景摘要。失败返回 None。"""
    if not form_id:
        return None
    return _load_scene(form_id)


def resolve_form_name(form_id: str) -> str | None:
    """对外：查 form_id 的业务名（不含云名）。失败返回 None。"""
    summary = resolve_scene(form_id)
    if summary and summary.get("name"):
        return summary["name"]
    return None


def resolve_cloud(form_id: str) -> str | None:
    """对外：查 form_id 所属云中文名。失败返回 None。"""
    _init_indices()
    if _CLOUD_INDEX is None:
        return None
    return _CLOUD_INDEX.get(form_id)


# ---------- 字段类型 → 分类映射 ----------

# 知识库字段类型 → 语义分类
# 类型名来自 scene_doc_lite.json 的 "t" 字段（*Field 命名），
# 同时兼容部分 Property 命名（若 MetadataResolver 返回 Property 后缀）。
FIELD_TYPE_CATEGORY: dict[str, str] = {
    # 文本类 → text（归入 vars / 智能用例变量）
    "TextField": "text",
    "MuliLangTextField": "text",
    "TextProperty": "text",
    "MultiLangTextProperty": "text",
    "LargeTextProperty": "text",
    "MultiLineTextProperty": "text",

    # 基础资料类 → basedata（pick_fields, env_sensitive: high）
    "BasedataField": "basedata",
    "HRAdminOrgField": "basedata",
    "HRBaseDataField": "basedata",
    "OrgField": "basedata",
    "UserField": "basedata",
    "BasedataPropField": "basedata",
    "BaseDataProperty": "basedata",
    "MultiBaseDataProperty": "basedata",

    # 日期类 → date（pick_fields, date_* 前缀）
    "DateField": "date",
    "DateTimeField": "date",
    "DateProperty": "date",
    "DateTimeProperty": "date",

    # 枚举/下拉类 → enum（pick_fields, env_sensitive: low）
    "ComboField": "enum",
    "CheckBoxField": "enum",
    "ComboProperty": "enum",
    "RadioProperty": "enum",
    "CheckProperty": "enum",

    # 数值类 → number（pick_fields, env_sensitive: low）
    "IntegerField": "number",
    "BigIntField": "number",
    "AmountField": "number",
    "IntegerProperty": "number",
    "LongProperty": "number",
    "DecimalProperty": "number",

    # 布尔类 → boolean（pick_fields, env_sensitive: low）
    "BooleanProperty": "boolean",

    # 系统字段 → system（忽略）
    "CreaterField": "system",
    "ModifierField": "system",
    "CreateDateField": "system",
    "ModifyDateField": "system",
    "MasterIdField": "system",
    "BillStatusField": "system",
    "AuditField": "system",
}


def get_field_type(form_id: str, field_key: str) -> str | None:
    """查询知识库中字段的原始类型名（如 'BasedataField'、'TextField'）。

    返回 scene_doc_lite.json 中该字段的 't' 值，未命中返回 None。
    可配合 FIELD_TYPE_CATEGORY 映射得到语义分类。
    """
    if not form_id or not field_key:
        return None
    scene = resolve_scene(form_id)
    fields = (scene or {}).get("fields") or {}
    meta = fields.get(field_key) or fields.get(field_key.lower())
    if meta:
        t = meta.get("t", "") or ""
        return t if t else None
    return None


# ---------- 字段分类 ----------

# A 档：必须变量化（自由输入 + 唯一约束）
_A_UNIQUE_KEY_HINTS = {
    "number", "code", "simplename", "name", "fullname",
    "billno", "orderno",
}
_A_NAME_FIELDS = {"ba_em_name", "em_name", "staffname"}
_A_UNIQUE_SUFFIXES = ("empnumber", "certificatenumber", "phone")

# ignore：类型永远无需变量化（时间字段由运行时补 / 数值由业务算 / 系统字段由平台维护）
_IGNORE_TYPES = {
    "DateField", "DateTimeField",
    "IntegerField", "BigIntField", "AmountField",
    "CreaterField", "ModifierField",
    "CreateDateField", "ModifyDateField",
    "MasterIdField",
    "BillStatusField",  # 单据状态由平台维护（但保留 enable 的 B 档兜底）
}

# B 档：基础资料/枚举（字面量保留）
_B_BASEDATA_TYPES = {
    "BasedataField", "HRAdminOrgField", "OrgField", "UserField",
    "HRBaseDataField",
}
_B_ENUM_TYPES = {"ComboField", "CheckBoxField"}

# C 档：响应回传（跨 step 引用）
_C_RESPONSE_KEYS = {"processinstid", "pkvalue", "fid", "billid"}

# 排除列表：后缀命中 name/number 但实际不是唯一键
_CLASSIFY_EXCLUSIONS = {"ename", "classtypeid"}


def classify_field(form_id: str, field_key: str) -> str | None:
    """三档字段分类。返回 "A"/"B"/"C"/"ignore"/None。
    
    优先级：
      1) 响应回传特征 key → C
      2) 知识库命中 + 字段元数据精准分类
      3) 知识库未命中 → 按 key 名的启发式回落（A 档补位）
      4) 其他 → None（交回原启发式）
    """
    if not field_key:
        return None
    kl = field_key.lower()

    # C 档：响应回传（与 form 无关，纯 key 名特征）
    if kl in _C_RESPONSE_KEYS:
        return "C"

    # 尝试走知识库字段元数据
    scene = resolve_scene(form_id) if form_id else None
    fields = (scene or {}).get("fields") or {}
    meta = fields.get(field_key) or fields.get(kl)

    if meta:
        ftype = meta.get("t", "")
        lk = meta.get("lk", 0)
        mf = meta.get("mf", "") or ""
        # ignore：系统字段 / 类型固定
        if lk == 1 and not meta.get("req"):
            # lk=1 且非必填 → 几乎肯定是系统字段（创建人/修改时间/版本号）
            return "ignore"
        if ftype in _IGNORE_TYPES and ftype != "BillStatusField":
            return "ignore"
        if "系统/平台维护" in mf:
            return "ignore"
        # A 档：TextField + 唯一键命名
        if ftype in ("TextField", "MuliLangTextField"):
            if kl in _CLASSIFY_EXCLUSIONS:
                return None  # ename 等放回启发式
            if kl in _A_UNIQUE_KEY_HINTS or kl in _A_NAME_FIELDS:
                return "A"
            if any(kl.endswith(suf) for suf in _A_UNIQUE_SUFFIXES):
                return "A"
            return None  # 普通文本字段，不强制
        # B 档：基础资料类
        if ftype in _B_BASEDATA_TYPES:
            return "B"
        # B 档：枚举类
        if ftype in _B_ENUM_TYPES:
            return "B"
        # BillStatusField 单据状态：enable 视为 B，其余 ignore
        if ftype == "BillStatusField":
            if kl in ("enable", "status", "billstatus"):
                return "B"
            return "ignore"
        # 其他类型暂不分类
        return None

    # 知识库未命中 → 启发式回落（仅对 A 档常见命名做补位，避免遗漏随机化）
    if kl in _CLASSIFY_EXCLUSIONS:
        return None
    if kl in _A_UNIQUE_KEY_HINTS or kl in _A_NAME_FIELDS:
        return "A"
    if any(kl.endswith(suf) for suf in _A_UNIQUE_SUFFIXES):
        return "A"
    return None


def field_meta(form_id: str, field_key: str) -> dict | None:
    """返回字段的原始元数据（t/req/lk/ref/mf/col）。"""
    if not form_id or not field_key:
        return None
    scene = resolve_scene(form_id)
    fields = (scene or {}).get("fields") or {}
    return fields.get(field_key) or fields.get(field_key.lower())


__all__ = [
    "resolve_scene",
    "resolve_form_name",
    "resolve_cloud",
    "classify_field",
    "field_meta",
    "get_field_type",
    "FIELD_TYPE_CATEGORY",
    "all_form_ids",
]
