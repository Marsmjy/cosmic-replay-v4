"""Persistent field type catalog for HAR imports.

The online metadata resolver can tell us precise Kingdee field property types
such as TextProp, BasedataProp, ComboProp, and EntryProp.  This module stores
those observations in a local knowledge file and exposes a stable taxonomy that
HAR import can use even when the next import runs offline.
"""
from __future__ import annotations

import json
from datetime import datetime
from pathlib import Path
from typing import Any


PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_CATALOG_PATH = (
    PROJECT_ROOT
    / "skills"
    / "cosmic-hr-expert"
    / "knowledge"
    / "_shared"
    / "runtime_field_type_catalog.json"
)

SCHEMA_VERSION = 1


TYPE_CATEGORY: dict[str, str] = {
    # Text
    "TextProp": "text",
    "TextField": "text",
    "TextProperty": "text",
    "TextAreaProp": "large_text",
    "LargeTextProp": "large_text",
    "LargeTextProperty": "large_text",
    "MultiLineTextProperty": "large_text",
    "MuliLangTextProp": "multi_lang_text",
    "MultiLangTextProp": "multi_lang_text",
    "MuliLangTextField": "multi_lang_text",
    "MultiLangTextField": "multi_lang_text",
    "MuliLangTextProperty": "multi_lang_text",
    "MultiLangTextProperty": "multi_lang_text",
    "DynamicLocaleProperty": "multi_lang_text",
    "TelephoneProp": "text",
    # Selectors / enums
    "BasedataProp": "basedata",
    "BasedataField": "basedata",
    "BaseDataProperty": "basedata",
    "HRBaseDataField": "basedata",
    "HRAdminOrgField": "basedata",
    "kd.bos.ext.hr.entity.property.AdminOrgFieldProp": "basedata",
    "MainOrgProp": "basedata",
    "OrgProp": "basedata",
    "OrgField": "basedata",
    "kd.bos.ext.hr.entity.property.PositionFieldProp": "basedata",
    "kd.bos.ext.hr.entity.property.HisModelBasedataProp": "basedata",
    "kd.bos.ext.hr.metadata.prop.QueryProp": "basedata",
    "UserProp": "basedata",
    "UserField": "basedata",
    "AdminDivisionProp": "basedata",
    "CityProp": "basedata",
    "CurrencyProp": "basedata",
    "MulBasedataProp": "multi_basedata",
    "MultiBaseDataProperty": "multi_basedata",
    "kd.bos.ext.hr.entity.property.MulAdminOrgFieldProp": "multi_basedata",
    "kd.bos.ext.hr.metadata.prop.MulQueryProp": "multi_basedata",
    "ComboProp": "combo",
    "ComboField": "combo",
    "ComboProperty": "combo",
    "MulComboProp": "multi_combo",
    "BooleanProp": "boolean",
    "BooleanProperty": "boolean",
    "CheckBoxField": "boolean",
    "CheckProperty": "boolean",
    "RadioProperty": "combo",
    "BillStatusProp": "combo",
    # Date/time
    "DateProp": "date",
    "DateField": "date",
    "DateProperty": "date",
    "DateTimeProp": "datetime",
    "DateTimeField": "datetime",
    "DateTimeProperty": "datetime",
    "TimeProp": "time",
    "TimeField": "time",
    # Numeric
    "IntegerProp": "number",
    "IntegerField": "number",
    "IntegerProperty": "number",
    "BigIntProp": "number",
    "BigIntField": "number",
    "LongProp": "number",
    "LongProperty": "number",
    "DecimalProp": "decimal",
    "DecimalProperty": "decimal",
    "AmountProp": "amount",
    "AmountField": "amount",
    # Structure / platform
    "EntryProp": "entry",
    "EntryField": "entry",
    "TreeEntryProp": "entry",
    "kd.bos.entity.property.PictureProp": "attachment",
    "CreaterProp": "system",
    "CreaterField": "system",
    "ModifierProp": "system",
    "ModifierField": "system",
    "CreateDateProp": "system",
    "CreateDateField": "system",
    "ModifyDateProp": "system",
    "ModifyDateField": "system",
    "MasterIdField": "system",
    "BillStatusField": "system",
    "AuditField": "system",
}

PICK_FIELD_CATEGORIES = {
    "basedata",
    "multi_basedata",
    "combo",
    "multi_combo",
    "boolean",
    "date",
    "time",
    "datetime",
}

VARIABLE_CATEGORIES = {
    "text",
    "large_text",
    "multi_lang_text",
    "code",
    "number",
    "decimal",
    "amount",
}


def canonical_category(raw_type: str | None) -> str:
    """Return the stable taxonomy category for a raw Kingdee metadata type."""
    return TYPE_CATEGORY.get(str(raw_type or ""), "unknown")


def panel_for_category(category: str) -> str:
    if category in PICK_FIELD_CATEGORIES:
        return "pick_fields"
    if category in VARIABLE_CATEGORIES:
        return "vars"
    if category in {"entry", "button", "dialog", "system", "attachment"}:
        return "structural"
    return "unknown"


def _catalog_path(path: Path | None = None) -> Path:
    return path or DEFAULT_CATALOG_PATH


def load_catalog(path: Path | None = None) -> dict[str, Any]:
    path = _catalog_path(path)
    if not path.exists():
        return {
            "schema_version": SCHEMA_VERSION,
            "updated_at": "",
            "type_counts": {},
            "entities": {},
        }
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except Exception:
        return {
            "schema_version": SCHEMA_VERSION,
            "updated_at": "",
            "type_counts": {},
            "entities": {},
        }
    if not isinstance(data, dict):
        data = {}
    data.setdefault("schema_version", SCHEMA_VERSION)
    data.setdefault("updated_at", "")
    data.setdefault("type_counts", {})
    data.setdefault("entities", {})
    return data


def save_catalog(data: dict[str, Any], path: Path | None = None) -> None:
    path = _catalog_path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(
        json.dumps(data, ensure_ascii=False, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def update_catalog_from_resolver(
    meta_resolver: Any,
    entity_ids: set[str] | list[str] | tuple[str, ...],
    *,
    path: Path | None = None,
) -> dict[str, Any]:
    """Persist fields fetched by MetadataResolver for the given entities."""
    path = _catalog_path(path)
    result = {
        "path": str(path),
        "entity_count": 0,
        "field_count": 0,
        "new_field_count": 0,
        "updated_field_count": 0,
        "unknown_type_count": 0,
    }
    if not meta_resolver:
        return result

    catalog = load_catalog(path)
    entities = catalog.setdefault("entities", {})
    type_counts: dict[str, int] = {}
    now = datetime.now().isoformat(timespec="seconds")

    for entity_id in sorted({str(e or "").strip() for e in entity_ids if str(e or "").strip()}):
        try:
            fields = meta_resolver.get_entity_fields(entity_id) or {}
        except Exception:
            continue
        if not fields:
            continue
        entity_node = entities.setdefault(entity_id, {"updated_at": "", "fields": {}})
        entity_fields = entity_node.setdefault("fields", {})
        result["entity_count"] += 1
        for field_key, meta in sorted(fields.items()):
            if not field_key:
                continue
            raw_type = str(getattr(meta, "type", "") or "")
            category = canonical_category(raw_type)
            if category == "unknown":
                result["unknown_type_count"] += 1
            type_counts[raw_type or ""] = type_counts.get(raw_type or "", 0) + 1
            old = entity_fields.get(field_key)
            item = {
                "name": getattr(meta, "name", field_key) or field_key,
                "label": getattr(meta, "label", "") or "",
                "raw_type": raw_type,
                "category": category,
                "panel": panel_for_category(category),
                "required": bool(getattr(meta, "required", False)),
                "max_length": getattr(meta, "max_length", None),
                "base_entity": getattr(meta, "base_entity", None) or "",
                "combo_items": getattr(meta, "combo_items", None) or {},
                "last_seen_at": now,
            }
            entity_fields[field_key] = item
            result["field_count"] += 1
            if old is None:
                result["new_field_count"] += 1
            elif old != item:
                result["updated_field_count"] += 1
        entity_node["updated_at"] = now

    if result["field_count"]:
        merged_counts = catalog.setdefault("type_counts", {})
        for raw_type, count in type_counts.items():
            merged_counts[raw_type] = int(merged_counts.get(raw_type, 0)) + count
        catalog["updated_at"] = now
        save_catalog(catalog, path)
    return result


def get_field_entry(form_id: str, field_key: str, *, path: Path | None = None) -> dict[str, Any] | None:
    path = _catalog_path(path)
    if not form_id or not field_key:
        return None
    catalog = load_catalog(path)
    entity = (catalog.get("entities") or {}).get(form_id) or {}
    fields = entity.get("fields") or {}
    return fields.get(field_key) or fields.get(field_key.lower())


def category_from_value(value: Any, field_key: str = "") -> str:
    key = str(field_key or "").lower()
    if isinstance(value, dict):
        if any(locale in value for locale in ("zh_CN", "zh_TW", "en")):
            return "multi_lang_text"
        return "text"
    if isinstance(value, bool):
        return "boolean"
    if isinstance(value, int) and not isinstance(value, bool):
        return "number"
    if isinstance(value, float):
        return "decimal"
    if isinstance(value, str):
        if key.endswith("code") or key.endswith("number") or key in {"code", "number", "billno", "orderno"}:
            return "code"
        if len(value) > 120:
            return "large_text"
        return "text"
    return "unknown"


def classify_for_import(
    *,
    form_id: str = "",
    field_key: str = "",
    raw_type: str = "",
    value: Any = None,
    path: Path | None = None,
) -> dict[str, Any]:
    """Return a normalized field classification used by HAR import."""
    source = ""
    entry = get_field_entry(form_id, field_key, path=path)
    if raw_type:
        category = canonical_category(raw_type)
        source = "metadata"
    elif entry:
        raw_type = str(entry.get("raw_type") or "")
        category = str(entry.get("category") or canonical_category(raw_type))
        if category == "unknown":
            category = canonical_category(raw_type)
        source = "runtime_catalog"
    else:
        category = category_from_value(value, field_key)
        source = "har_heuristic"
    if category == "unknown" and value is not None:
        category = category_from_value(value, field_key)
        source = source or "har_heuristic"
    return {
        "field_key": field_key,
        "form_id": form_id,
        "raw_type": raw_type,
        "category": category,
        "panel": panel_for_category(category),
        "source": source or "unknown",
        "label": (entry or {}).get("label", ""),
        "required": bool((entry or {}).get("required", False)),
        "base_entity": (entry or {}).get("base_entity", ""),
    }
