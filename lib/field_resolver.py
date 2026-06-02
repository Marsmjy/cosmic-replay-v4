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
from dataclasses import asdict, dataclass, field
from datetime import datetime
from pathlib import Path
from typing import Any, TYPE_CHECKING

if TYPE_CHECKING:
    from .replay import CosmicFormReplay


@dataclass
class LookupCandidate:
    """基础资料搜索候选项。"""

    value_id: str
    value_name: str
    number: str = ""
    raw: Any = None


@dataclass
class ResolveResult:
    """环境字段解析结果。"""

    status: str
    field_key: str
    query: str
    original_value_id: str = ""
    resolved_value_id: str = ""
    resolved_value_name: str = ""
    confidence: str = "low"
    message: str = ""
    candidates: list[LookupCandidate] = field(default_factory=list)

    def to_dict(self) -> dict[str, Any]:
        data = asdict(self)
        data["candidates"] = [asdict(c) for c in self.candidates[:5]]
        return data


class EnvFieldCache:
    """环境字段解析缓存。

    缓存只作为加速和诊断辅助，解析失败时不会阻断实时查询。
    """

    def __init__(self, path: Path | None = None):
        root = Path(__file__).resolve().parent.parent
        self.path = path or (root / "data" / "env_field_cache.json")

    def get(self, env_id: str, form_id: str, app_id: str, field_key: str, query: str) -> dict | None:
        data = self._read()
        return data.get(self._key(env_id, form_id, app_id, field_key, query))

    def set(
        self,
        env_id: str,
        form_id: str,
        app_id: str,
        field_key: str,
        query: str,
        result: ResolveResult,
    ) -> None:
        if result.status != "resolved" or not result.resolved_value_id:
            return
        data = self._read()
        data[self._key(env_id, form_id, app_id, field_key, query)] = {
            "env_id": env_id,
            "form_id": form_id,
            "app_id": app_id,
            "field_key": field_key,
            "query": query,
            "status": result.status,
            "resolved_value_id": result.resolved_value_id,
            "resolved_value_name": result.resolved_value_name,
            "confidence": result.confidence,
            "message": result.message,
            "updated_at": datetime.now().isoformat(timespec="seconds"),
        }
        self._write(data)

    def _read(self) -> dict[str, dict]:
        if not self.path.exists():
            return {}
        try:
            raw = json.loads(self.path.read_text(encoding="utf-8") or "{}")
            return raw if isinstance(raw, dict) else {}
        except Exception:
            return {}

    def _write(self, data: dict[str, dict]) -> None:
        self.path.parent.mkdir(parents=True, exist_ok=True)
        tmp = self.path.with_suffix(self.path.suffix + ".tmp")
        tmp.write_text(
            json.dumps(data, ensure_ascii=False, indent=2, sort_keys=True),
            encoding="utf-8",
        )
        tmp.replace(self.path)

    @staticmethod
    def _key(env_id: str, form_id: str, app_id: str, field_key: str, query: str) -> str:
        return "|".join([
            str(env_id or "").strip(),
            str(form_id or "").strip(),
            str(app_id or "").strip(),
            str(field_key or "").strip().lower(),
            str(query or "").strip(),
        ])


class FieldResolver:
    """运行时基础资料查询"""

    def __init__(
        self,
        replay: "CosmicFormReplay",
        *,
        env_id: str = "",
        cache_store: EnvFieldCache | None = None,
        use_persistent_cache: bool = True,
    ):
        self.replay = replay
        self.env_id = env_id
        self.cache_store = cache_store if cache_store is not None else (
            EnvFieldCache() if use_persistent_cache else None
        )
        self._cache: dict[tuple, ResolveResult] = {}  # (form_id, app_id, field_key, name) → result

    def resolve_basedata(self, form_id: str, app_id: str, field_key: str,
                         name: str, page_id: str | None = None) -> str | None:
        """用 getLookUpList 搜索 name 对应的 id。返回 None 表示没找到。"""
        result = self.resolve_basedata_result(
            form_id, app_id, field_key, name, page_id=page_id
        )
        return result.resolved_value_id if result.status == "resolved" else None

    def resolve_basedata_result(
        self,
        form_id: str,
        app_id: str,
        field_key: str,
        name: str,
        *,
        original_value_id: str = "",
        page_id: str | None = None,
    ) -> ResolveResult:
        """搜索基础资料并返回结构化解析结果。"""
        ck = (form_id, app_id, field_key, name)
        if ck in self._cache:
            cached = self._cache[ck]
            data = cached.to_dict()
            data.update(original_value_id=original_value_id, message="命中运行时缓存")
            return ResolveResult(
                status=data["status"],
                field_key=data["field_key"],
                query=data["query"],
                original_value_id=data.get("original_value_id", ""),
                resolved_value_id=data.get("resolved_value_id", ""),
                resolved_value_name=data.get("resolved_value_name", ""),
                confidence=data.get("confidence", "low"),
                message=data.get("message", ""),
                candidates=cached.candidates,
            )

        if not name:
            return ResolveResult(
                status="skipped",
                field_key=field_key,
                query=name,
                original_value_id=original_value_id,
                message="缺少 value_name，无法按名称解析",
            )

        cached = self._get_persistent_cache(form_id, app_id, field_key, name, original_value_id)
        if cached:
            self._cache[ck] = cached
            return cached

        try:
            # args=[% name % 0 20 0] 是常见模糊搜索签名（见 HAR addrule/adminorgtype 示例）
            resp = self.replay.invoke(
                form_id, app_id, "getLookUpList",
                [{"key": field_key, "methodName": "getLookUpList",
                  "args": [["%", name, "%", 0, 20, 0]],
                  "postData": [{}, []]}],
                page_id=page_id,
            )
        except Exception as e:
            return ResolveResult(
                status="error",
                field_key=field_key,
                query=name,
                original_value_id=original_value_id,
                message=f"{type(e).__name__}: {e}",
            )

        candidates = self._parse_lookup_candidates(resp)
        best, confidence, status, message = self._select_candidate(candidates, name)
        if best and status == "resolved":
            result = ResolveResult(
                status=status,
                field_key=field_key,
                query=name,
                original_value_id=original_value_id,
                resolved_value_id=best.value_id,
                resolved_value_name=best.value_name,
                confidence=confidence,
                message=message,
                candidates=candidates,
            )
            self._cache[ck] = result
            self._set_persistent_cache(form_id, app_id, field_key, name, result)
            return result
        result = ResolveResult(
            status=status,
            field_key=field_key,
            query=name,
            original_value_id=original_value_id,
            confidence=confidence,
            message=message,
            candidates=candidates,
        )
        self._cache[ck] = result
        return result

    def _get_persistent_cache(
        self,
        form_id: str,
        app_id: str,
        field_key: str,
        name: str,
        original_value_id: str,
    ) -> ResolveResult | None:
        if not (self.env_id and self.cache_store):
            return None
        item = self.cache_store.get(self.env_id, form_id, app_id, field_key, name)
        if not item or item.get("status") != "resolved" or not item.get("resolved_value_id"):
            return None
        cached_value_id = str(item.get("resolved_value_id") or "")
        if _looks_like_internal_id(original_value_id) and not _looks_like_internal_id(cached_value_id):
            return None
        return ResolveResult(
            status="resolved",
            field_key=field_key,
            query=name,
            original_value_id=original_value_id,
            resolved_value_id=cached_value_id,
            resolved_value_name=str(item.get("resolved_value_name") or name),
            confidence=str(item.get("confidence") or "high"),
            message="命中环境字段缓存",
        )

    def _set_persistent_cache(
        self,
        form_id: str,
        app_id: str,
        field_key: str,
        name: str,
        result: ResolveResult,
    ) -> None:
        if not (self.env_id and self.cache_store):
            return
        try:
            self.cache_store.set(self.env_id, form_id, app_id, field_key, name, result)
        except Exception:
            pass

    @staticmethod
    def _parse_lookup(resp: Any, name: str) -> str | None:
        """从 getLookUpList 响应里找匹配 name 的首条记录 id。"""
        candidates = FieldResolver._parse_lookup_candidates(resp)
        best, _, status, _ = FieldResolver._select_candidate(candidates, name)
        return best.value_id if best and status == "resolved" else None

    @staticmethod
    def _parse_lookup_candidates(resp: Any) -> list[LookupCandidate]:
        """从 getLookUpList 响应中提取候选项。"""
        candidates: list[LookupCandidate] = []
        seen: set[tuple[str, str]] = set()

        def add(value_id: Any, value_name: Any, number: Any = "", raw: Any = None):
            if value_id is None or value_name is None:
                return
            if isinstance(value_name, dict):
                value_name = value_name.get("zh_CN") or value_name.get("name") or str(value_name)
            value_id = str(value_id).strip()
            value_name = str(value_name).strip()
            number = str(number or "").strip()
            if not number:
                number = str(_infer_lookup_number(raw, value_id, value_name) or "").strip()
            if not value_id or not value_name:
                return
            key = (value_id, value_name)
            if key in seen:
                return
            seen.add(key)
            candidates.append(LookupCandidate(value_id, value_name, number=number, raw=raw))

        def walk(obj):
            if isinstance(obj, dict):
                # 苍穹返回形态多样，常见：
                #   rows: [[id, display, ...], ...] + dataindex: {number: 0, name: 1, ...}
                #   或 list: [{id, name}, ...]
                rows = obj.get("rows")
                di = obj.get("dataindex")
                if isinstance(rows, list) and isinstance(di, dict):
                    id_ix = _first_index(di, ("boid", "id", "value", "pkid", "pk"))
                    number_ix = _first_index(di, ("number", "code"))
                    name_ix = _first_index(di, ("name", "text", "displayname"))
                    for row in rows:
                        if not isinstance(row, list):
                            continue
                        rid = _row_value(row, id_ix)
                        number = _row_value(row, number_ix)
                        rnm = _row_value(row, name_ix)
                        number = number or _infer_lookup_number(row, rid, rnm)
                        rid = _prefer_business_id(row, rid, number)
                        add(rid or number, rnm, number=number, raw=row)
                lst = obj.get("list")
                if isinstance(lst, list):
                    for it in lst:
                        if isinstance(it, dict):
                            rid = it.get("id") or it.get("value") or it.get("pkid") or it.get("number")
                            number = it.get("number") or it.get("code") or ""
                            rnm = it.get("name") or it.get("text") or it.get("displayname")
                            add(rid, rnm, number=number, raw=it)
                data_rows = obj.get("data")
                columns = obj.get("columns")
                if isinstance(data_rows, list) and isinstance(columns, list):
                    id_ix = _first_column_index(columns, ("boid", "id", "value", "pkid", "pk"))
                    number_ix = _first_column_index(columns, ("number", "code"))
                    name_ix = _first_column_index(columns, ("name", "text", "displayname"))
                    for row in data_rows:
                        if not isinstance(row, list):
                            continue
                        rid = _row_value(row, id_ix)
                        number = _row_value(row, number_ix)
                        rnm = _row_value(row, name_ix)
                        rid = _prefer_business_id(row, rid, number)
                        add(rid or number, rnm, number=number, raw=row)
                for v in obj.values(): walk(v)
            elif isinstance(obj, list):
                for x in obj: walk(x)

        walk(resp)
        return candidates

    @staticmethod
    def _select_candidate(
        candidates: list[LookupCandidate],
        name: str,
    ) -> tuple[LookupCandidate | None, str, str, str]:
        """选择最可信候选。返回 best, confidence, status, message。"""
        name = str(name or "").strip()
        if not candidates:
            return None, "low", "not_found", "未查询到匹配候选"

        # 精确匹配优先
        exact = [c for c in candidates if c.value_name == name or c.number == name]
        if len(exact) == 1:
            return exact[0], "high", "resolved", "名称/编码精确匹配"
        if len(exact) > 1:
            return None, "medium", "ambiguous", f"精确匹配到 {len(exact)} 个候选"

        contains = [c for c in candidates if name in c.value_name or c.value_name in name]
        if len(contains) == 1:
            return contains[0], "medium", "resolved", "名称模糊匹配"
        if len(contains) > 1:
            return None, "low", "ambiguous", f"模糊匹配到 {len(contains)} 个候选"

        return None, "low", "not_found", "候选项与 value_name 不匹配"


def _first_index(dataindex: dict, keys: tuple[str, ...]) -> int | None:
    lowered = {str(k).lower(): v for k, v in dataindex.items()}
    for key in keys:
        value = lowered.get(key)
        if value is None:
            continue
        try:
            return int(value)
        except (TypeError, ValueError):
            continue
    return None


def _row_value(row: list, index: int | None) -> Any:
    if index is None or index < 0 or index >= len(row):
        return None
    return row[index]


def _looks_like_internal_id(value: Any) -> bool:
    text = str(value or "").strip()
    return text.isdigit() and len(text) >= 12


def _prefer_business_id(row: list, value_id: Any, number: Any) -> Any:
    """Prefer the first long numeric business id when metadata points to number/code."""
    if not row:
        return value_id
    first = row[0]
    if not _looks_like_internal_id(first):
        return value_id
    value_text = str(value_id or "").strip()
    number_text = str(number or "").strip()
    if not value_text or value_text == number_text or not _looks_like_internal_id(value_text):
        return first
    return value_id


def _infer_lookup_number(row: list, value_id: Any, value_name: Any) -> Any:
    """Infer number/code from common [id, number, name] rows when dataindex omits it."""
    if len(row) < 3:
        return None
    first = str(row[0] or "").strip()
    second = str(row[1] or "").strip()
    third = str(row[2] or "").strip()
    if not second:
        return None
    if first != str(value_id or "").strip():
        return None
    if third != str(value_name or "").strip():
        return None
    if _looks_like_internal_id(first) and second != third:
        return second
    return None


def _first_column_index(columns: list, keys: tuple[str, ...]) -> int | None:
    wanted = {k.lower() for k in keys}
    for idx, col in enumerate(columns):
        if not isinstance(col, dict):
            continue
        col_id = str(col.get("id") or col.get("key") or col.get("field") or "").lower()
        if col_id in wanted:
            return idx
    return None
