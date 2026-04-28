"""动态解析基础资料 id - 让用例跨账号可移植

典型场景：
- HAR 录制时 adminorgtype 用的是 id=1020（"公司"）
- 换账号跑时 id 可能变了，但"公司"这个名字在配置表里一直存在
- YAML 里写 ${resolve:basedata:adminorgtype:公司}，runner 跑时实时查出当前账号下"公司"的真实 id

用法（给 runner 用）：
    resolver = FieldResolver(replay)
    real_id = resolver.resolve_basedata("haos_adminorgdetail", "haos",
                                         "adminorgtype", name="公司")
"""
from __future__ import annotations

import json
from typing import Any, TYPE_CHECKING

if TYPE_CHECKING:
    from .replay import CosmicFormReplay


class FieldResolver:
    """运行时基础资料查询"""

    def __init__(self, replay: "CosmicFormReplay"):
        self.replay = replay
        self._cache: dict[tuple, str] = {}  # (form_id, field_key, name) → id

    def resolve_basedata(self, form_id: str, app_id: str, field_key: str,
                         name: str, page_id: str | None = None) -> str | None:
        """用 getLookUpList 搜索 name 对应的 id。返回 None 表示没找到。"""
        ck = (form_id, field_key, name)
        if ck in self._cache:
            return self._cache[ck]

        # args=[% name % 0 20 0] 是常见模糊搜索签名（见 HAR addrule/adminorgtype 示例）
        resp = self.replay.invoke(
            form_id, app_id, "getLookUpList",
            [{"key": field_key, "methodName": "getLookUpList",
              "args": [["%", name, "%", 0, 20, 0]],
              "postData": [{}, []]}],
            page_id=page_id,
        )
        bid = self._parse_lookup(resp, name)
        if bid:
            self._cache[ck] = bid
        return bid

    @staticmethod
    def _parse_lookup(resp: Any, name: str) -> str | None:
        """从 getLookUpList 响应里找匹配 name 的首条记录 id。"""
        candidates: list[tuple[str, str]] = []  # (id, display)

        def walk(obj):
            if isinstance(obj, dict):
                # 苍穹返回形态多样，常见：
                #   rows: [[id, display, ...], ...] + dataindex: {number: 0, name: 1, ...}
                #   或 list: [{id, name}, ...]
                rows = obj.get("rows")
                di = obj.get("dataindex")
                if isinstance(rows, list) and isinstance(di, dict):
                    number_ix = di.get("number") if "number" in di else di.get("id", 0)
                    name_ix = di.get("name") if "name" in di else 1
                    for row in rows:
                        if isinstance(row, list) and len(row) > max(number_ix or 0, name_ix or 0):
                            rid = str(row[number_ix]) if number_ix is not None else None
                            rnm = row[name_ix] if name_ix is not None else None
                            if isinstance(rnm, dict):
                                rnm = rnm.get("zh_CN") or str(rnm)
                            if rid and rnm:
                                candidates.append((rid, str(rnm)))
                lst = obj.get("list")
                if isinstance(lst, list):
                    for it in lst:
                        if isinstance(it, dict):
                            rid = it.get("id") or it.get("number")
                            rnm = it.get("name") or it.get("text")
                            if isinstance(rnm, dict):
                                rnm = rnm.get("zh_CN") or str(rnm)
                            if rid and rnm:
                                candidates.append((str(rid), str(rnm)))
                for v in obj.values(): walk(v)
            elif isinstance(obj, list):
                for x in obj: walk(x)

        walk(resp)
        # 精确匹配优先
        for rid, rnm in candidates:
            if rnm == name:
                return rid
        for rid, rnm in candidates:
            if name in rnm:
                return rid
        return None