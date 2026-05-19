import sys
from pathlib import Path

import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.field_resolver import EnvFieldCache, FieldResolver, ResolveResult
from lib.har_extractor import build_yaml_case, preview_har
from lib.runner import _apply_pick_fields, _auto_resolve_pick_basedata_step
from lib.webui.server import _apply_pick_field_manual_update


class FakeReplay:
    def invoke(self, form_id, app_id, ac, actions, page_id=None):
        assert ac == "getLookUpList"
        return [{
            "rows": [
                ["old-id", "OLD", "旧组织"],
                ["new-id", "NEW", "目标组织"],
            ],
            "dataindex": {"id": 0, "number": 1, "name": 2},
        }]


def test_field_resolver_parses_lookup_candidates_and_exact_match():
    resp = [{
        "rows": [
            ["100000", "HQ", "环宇国际集团有限公司"],
            ["200000", "SUB", "子公司"],
        ],
        "dataindex": {"id": 0, "number": 1, "name": 2},
    }]

    candidates = FieldResolver._parse_lookup_candidates(resp)
    best, confidence, status, message = FieldResolver._select_candidate(
        candidates, "环宇国际集团有限公司"
    )

    assert len(candidates) == 2
    assert best.value_id == "100000"
    assert confidence == "high"
    assert status == "resolved"
    assert "精确" in message


def test_field_resolver_parses_set_lookup_list_value_shape():
    resp = [{
        "a": "InvokeControlMethod",
        "p": [{
            "key": "adminorg",
            "methodname": "setLookUpListValue",
            "args": [{
                "data": [["100000", "00", "环宇国际集团有限公司"]],
                "columns": [
                    {"id": "boid", "caption": "业务ID"},
                    {"id": "number", "caption": "组织编码"},
                    {"id": "name", "caption": "组织名称"},
                ],
            }],
        }],
    }]

    candidates = FieldResolver._parse_lookup_candidates(resp)
    best, confidence, status, _ = FieldResolver._select_candidate(
        candidates, "环宇国际集团有限公司"
    )

    assert len(candidates) == 1
    assert best.value_id == "100000"
    assert best.number == "00"
    assert confidence == "high"
    assert status == "resolved"


def test_field_resolver_prefers_business_id_when_dataindex_points_to_code():
    resp = [{
        "rows": [
            ["2266069031129946112", "tmcompany", "天美公司"],
        ],
        "dataindex": {"id": 1, "number": 1, "name": 2},
    }]

    candidates = FieldResolver._parse_lookup_candidates(resp)
    best, confidence, status, _ = FieldResolver._select_candidate(
        candidates, "天美公司"
    )

    assert best.value_id == "2266069031129946112"
    assert best.number == "tmcompany"
    assert confidence == "high"
    assert status == "resolved"


def test_auto_resolve_pick_basedata_step_overrides_value_id_and_emits_status():
    step = {
        "id": "pick_adminorg",
        "type": "pick_basedata",
        "form_id": "demo_form",
        "app_id": "demo",
        "field_key": "adminorg",
        "value_id": "stale-id",
        "value_name": "目标组织",
        "auto_resolve": True,
        "_env_field_id": "pick_adminorg_id",
        "_env_field_meta": {
            "label": "行政组织",
            "env_sensitive": "medium",
            "value_name": "目标组织",
        },
    }
    events = []

    _auto_resolve_pick_basedata_step(
        step,
        FakeReplay(),
        {"env_resolution": {}, "run_event": lambda t, p: events.append((t, p))},
    )

    assert step["value_id"] == "new-id"
    assert events[0][0] == "env_fields_resolved"
    field = events[0][1]["fields"][0]
    assert field["step_id"] == "pick_adminorg_id"
    assert field["resolve_status"] == "resolved"
    assert field["resolved_value_id"] == "new-id"


def test_manual_pick_field_value_id_is_injected_without_value_name():
    case = {
        "pick_fields": {
            "pick_adminorgtype_id": {
                "field_key": "adminorgtype",
                "value_id": "1010",
                "value_name": "",
                "auto_resolve": False,
                "resolve_status": "manual",
            }
        },
        "steps": [
            {
                "id": "pick_adminorgtype",
                "type": "pick_basedata",
                "field_key": "adminorgtype",
                "value_id": "1020",
                "value_name": "公司",
            }
        ],
    }

    _apply_pick_fields(case)

    step = case["steps"][0]
    assert step["value_id"] == "1010"
    assert step["value_name"] == ""
    assert step["auto_resolve"] is False


def test_manual_pick_field_update_disables_auto_resolve_and_clears_stale_name():
    item = {
        "value_id": "1020",
        "value_name": "公司",
        "auto_resolve": True,
        "resolve_status": "pending",
    }

    _apply_pick_field_manual_update(item, "1010", manual_override=True)

    assert item["value_id"] == "1010"
    assert item["value_name"] == ""
    assert item["auto_resolve"] is False
    assert item["resolve_status"] == "manual"
    assert item["manual_override"] is True


def test_generated_pick_fields_carry_auto_resolve_metadata():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    yaml_text = build_yaml_case(har_path, case_name="env_resolution_position")
    case = yaml.safe_load(yaml_text)
    adminorg = case["pick_fields"]["pick_adminorg_id"]

    assert adminorg["value_name"] == "环宇国际集团有限公司"
    assert adminorg["auto_resolve"] is True
    assert adminorg["resolve_by"] == "value_name"
    assert adminorg["resolve_status"] == "pending"
    assert adminorg["form_id"] == "hbpm_positionhr"
    assert adminorg["app_id"] == "hbpm"


def test_manual_har_pick_field_override_disables_auto_resolve():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(
        har_path,
        case_name="manual_adminorgtype",
        pick_field_overrides={
            "pick_adminorgtype_id": {
                "value_id": "1010",
                "value_name": "公司",
                "auto_resolve": False,
                "resolve_status": "manual",
                "manual_override": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    adminorgtype = case["pick_fields"]["pick_adminorgtype_id"]

    assert str(adminorgtype["value_id"]) == "1010"
    assert adminorgtype["value_name"] == ""
    assert adminorgtype["auto_resolve"] is False
    assert adminorgtype["resolve_status"] == "manual"
    assert adminorgtype["manual_override"] is True


def test_preview_pick_fields_carry_auto_resolve_metadata():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    preview = preview_har(har_path)
    adminorg = next(pf for pf in preview["pick_fields"] if pf["id"] == "pick_adminorg_id")

    assert adminorg["auto_resolve"] is True
    assert adminorg["resolve_status"] == "pending"
    assert adminorg["form_id"] == "hbpm_positionhr"
    assert adminorg["app_id"] == "hbpm"


def test_field_resolver_uses_environment_cache(tmp_path):
    cache = EnvFieldCache(tmp_path / "env_field_cache.json")
    resolver = FieldResolver(FakeReplay(), env_id="sit", cache_store=cache)

    first = resolver.resolve_basedata_result(
        "demo_form",
        "demo",
        "adminorg",
        "目标组织",
        original_value_id="stale-id",
    )
    assert first.status == "resolved"
    assert first.resolved_value_id == "new-id"

    class BrokenReplay:
        def invoke(self, *args, **kwargs):
            raise AssertionError("persistent cache should avoid network lookup")

    cached_resolver = FieldResolver(BrokenReplay(), env_id="sit", cache_store=cache)
    cached = cached_resolver.resolve_basedata_result(
        "demo_form",
        "demo",
        "adminorg",
        "目标组织",
        original_value_id="stale-id",
    )

    assert cached.status == "resolved"
    assert cached.resolved_value_id == "new-id"
    assert "缓存" in cached.message


def test_field_resolver_ignores_suspicious_code_cache_for_internal_id(tmp_path):
    cache = EnvFieldCache(tmp_path / "env_field_cache.json")
    cache.set(
        "sit",
        "hom_onbrdinfo",
        "hom",
        "ba_po_adminorg",
        "天美公司",
        ResolveResult(
            status="resolved",
            field_key="ba_po_adminorg",
            query="天美公司",
            resolved_value_id="tmcompany",
            resolved_value_name="天美公司",
        ),
    )

    class AdminOrgReplay:
        def invoke(self, form_id, app_id, ac, actions, page_id=None):
            return [{
                "rows": [["2266069031129946112", "tmcompany", "天美公司"]],
                "dataindex": {"id": 1, "number": 1, "name": 2},
            }]

    resolver = FieldResolver(AdminOrgReplay(), env_id="sit", cache_store=cache)
    result = resolver.resolve_basedata_result(
        "hom_onbrdinfo",
        "hom",
        "ba_po_adminorg",
        "天美公司",
        original_value_id="2266069031129946112",
    )

    assert result.status == "resolved"
    assert result.resolved_value_id == "2266069031129946112"
    assert "缓存" not in result.message
