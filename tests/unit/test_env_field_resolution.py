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


class CodeReplay:
    def invoke(self, form_id, app_id, ac, actions, page_id=None):
        assert ac == "getLookUpList"
        assert actions[0]["args"][0][1] == "KD001"
        return [{
            "rows": [
                ["internal-kd001", "KD001", "行政组织"],
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


def test_field_resolver_infers_number_from_id_number_name_rows_without_number_index():
    resp = [{
        "rows": [
            ["2381390676873979991", "00002", "9289684"],
            ["2390600880509446144", "100002", "星瀚入职"],
        ],
        "dataindex": {"id": 0, "name": 2},
    }]

    candidates = FieldResolver._parse_lookup_candidates(resp)
    best, confidence, status, message = FieldResolver._select_candidate(candidates, "00002")

    assert candidates[0].number == "00002"
    assert best.value_id == "2381390676873979991"
    assert best.value_name == "9289684"
    assert confidence == "high"
    assert status == "resolved"
    assert "精确" in message


def test_field_resolver_does_not_infer_number_from_dict_raw_candidate():
    resp = [{
        "list": [{
            "id": "2381390676873979991",
            "name": "9289684",
        }],
    }]

    candidates = FieldResolver._parse_lookup_candidates(resp)

    assert candidates[0].number == ""
    assert candidates[0].value_id == "2381390676873979991"


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


def test_auto_resolve_pick_basedata_step_can_resolve_by_business_code():
    step = {
        "id": "pick_khr_homs_orgform",
        "type": "pick_basedata",
        "form_id": "haos_adminorgdetail",
        "app_id": "haos",
        "field_key": "khr_homs_orgform",
        "value_id": "2336398131039579136",
        "value_name": "行政组织",
        "value_code": "KD001",
        "auto_resolve": True,
        "resolve_by": "value_code",
        "_env_field_id": "pick_khr_homs_orgform_id",
        "_env_field_meta": {
            "label": "组织形态",
            "env_sensitive": "medium",
            "value_name": "行政组织",
            "value_code": "KD001",
            "resolve_by": "value_code",
        },
    }
    events = []

    _auto_resolve_pick_basedata_step(
        step,
        CodeReplay(),
        {"env_resolution": {}, "run_event": lambda t, p: events.append((t, p))},
    )

    assert step["value_id"] == "internal-kd001"
    field = events[0][1]["fields"][0]
    assert field["resolve_status"] == "resolved"
    assert field["resolve_by"] == "value_code"
    assert field["query"] == "KD001"
    assert field["value_code"] == "KD001"
    assert field["resolved_value_id"] == "internal-kd001"


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


def test_pick_field_injection_respects_source_step_scope_in_multi_form_chain():
    case = {
        "pick_fields": {
            "pick_adminorg_id": {
                "field_key": "adminorg",
                "value_id": "A-new",
                "value_name": "",
                "form_id": "form_a",
                "source_step_id": "pick_a_adminorg",
                "auto_resolve": False,
                "resolve_status": "manual",
            }
        },
        "steps": [
            {
                "id": "pick_a_adminorg",
                "type": "pick_basedata",
                "form_id": "form_a",
                "field_key": "adminorg",
                "value_id": "A-old",
                "value_name": "A组织",
            },
            {
                "id": "pick_b_adminorg",
                "type": "pick_basedata",
                "form_id": "form_b",
                "field_key": "adminorg",
                "value_id": "B-old",
                "value_name": "B组织",
            },
            {
                "id": "fill_b_adminorg",
                "type": "update_fields",
                "form_id": "form_b",
                "fields": {"adminorg": "B-old"},
            },
        ],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["value_id"] == "A-new"
    assert case["steps"][1]["value_id"] == "B-old"
    assert case["steps"][2]["fields"]["adminorg"] == "B-old"


def test_pick_field_override_updates_repeated_same_form_update_fields():
    case = {
        "pick_fields": {
            "pick_khr_scope_id": {
                "field_key": "khr_scope",
                "value_id": "1",
                "value_name": "目标薪酬",
                "value_code": "1",
                "value_number": "1",
                "form_id": "khr_hcdm_fapplybill",
                "source_step_id": "fill_khr_scope",
                "user_overridden": True,
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "pending",
            }
        },
        "steps": [
            {
                "id": "fill_khr_scope",
                "type": "update_fields",
                "form_id": "khr_hcdm_fapplybill",
                "fields": {"khr_scope": "1"},
            },
            {
                "id": "fill_khr_zcurrency_etc",
                "type": "update_fields",
                "form_id": "khr_hcdm_fapplybill",
                "fields": {"khr_zcurrency": "1", "khr_scope": "3"},
            },
            {
                "id": "fill_other_form_scope",
                "type": "update_fields",
                "form_id": "other_form",
                "fields": {"khr_scope": "3"},
            },
        ],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["fields"]["khr_scope"] == "1"
    assert case["steps"][1]["fields"]["khr_scope"] == "1"
    assert case["steps"][2]["fields"]["khr_scope"] == "3"


def test_date_pick_field_injection_respects_form_scope():
    case = {
        "pick_fields": {
            "date_effectdate": {
                "field_key": "effectdate",
                "value_id": "2026-05-21",
                "form_id": "form_a",
                "source_step_id": "fill_a_date",
            }
        },
        "steps": [
            {
                "id": "fill_a_date",
                "type": "update_fields",
                "form_id": "form_a",
                "fields": {"effectdate": "2026-05-01"},
            },
            {
                "id": "fill_b_date",
                "type": "update_fields",
                "form_id": "form_b",
                "fields": {"effectdate": "2026-05-01"},
            },
        ],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["fields"]["effectdate"] == "2026-05-21"
    assert case["steps"][1]["fields"]["effectdate"] == "2026-05-01"


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
    assert item["user_overridden"] is True


def test_pick_field_update_by_code_marks_user_override_without_manual_mode():
    item = {
        "value_id": "100000",
        "value_name": "环宇国际集团有限公司",
        "value_code": "100000",
        "auto_resolve": True,
        "resolve_by": "value_code",
        "resolve_status": "pending",
        "context_only": True,
    }

    _apply_pick_field_manual_update(
        item,
        "100000",
        value_code="200000",
        resolve_by="value_code",
        auto_resolve=True,
        resolve_status="pending",
        manual_override=False,
    )

    assert item["value_code"] == "200000"
    assert item["value_id"] == "100000"
    assert item["user_overridden"] is True
    assert item["auto_resolve"] is True
    assert item.get("manual_override") is not True


def test_selector_pick_field_update_by_code_syncs_stale_display_values():
    item = {
        "value_id": "00186-0001",
        "value_name": "00186-0001",
        "value_code": "00186-0001",
        "value_number": "00186-0001",
        "auto_resolve": True,
        "resolve_by": "value_code",
        "resolve_status": "pending",
        "selector_source": "entryRowClick",
    }

    _apply_pick_field_manual_update(
        item,
        "00186-0001",
        value_code="06019-0001",
        resolve_by="value_code",
        auto_resolve=True,
        resolve_status="pending",
        manual_override=False,
    )

    assert item["value_id"] == "06019-0001"
    assert item["value_name"] == "06019-0001"
    assert item["value_code"] == "06019-0001"
    assert item["value_number"] == "06019-0001"
    assert item["user_overridden"] is True
    assert item["auto_resolve"] is True
    assert item.get("manual_override") is not True


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


def test_user_overridden_context_pick_field_updates_recorded_update_fields():
    case = {
        "pick_fields": {
            "pick_createorg_id": {
                "field_key": "createorg",
                "value_id": "100000",
                "value_name": "环宇国际集团有限公司",
                "value_code": "200000",
                "value_number": "200000",
                "form_id": "haos_orgchangereason",
                "source_step_id": "fill_defaults",
                "context_only": True,
                "user_overridden": True,
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "pending",
            },
            "pick_ctrlstrategy_id": {
                "field_key": "ctrlstrategy",
                "value_id": "5",
                "value_name": "全局共享",
                "value_code": "7",
                "form_id": "haos_orgchangereason",
                "source_step_id": "fill_defaults",
                "context_only": True,
                "user_overridden": True,
                "resolve_by": "value_code",
            },
        },
        "steps": [{
            "id": "fill_defaults",
            "type": "update_fields",
            "form_id": "haos_orgchangereason",
            "fields": {"createorg": "100000", "ctrlstrategy": "5"},
        }],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["fields"]["createorg"] == "200000"
    assert case["steps"][0]["fields"]["ctrlstrategy"] == "7"


def test_workflow_approval_decision_override_normalizes_display_value():
    case = {
        "pick_fields": {
            "pick_decision_radio_group_id": {
                "field_key": "decision_radio_group",
                "value_id": "驳回",
                "value_name": "驳回",
                "form_id": "wf_batchtask_handle",
                "source_step_id": "fill_workflow_approval",
                "user_overridden": True,
                "resolve_status": "manual",
            },
        },
        "steps": [{
            "id": "fill_workflow_approval",
            "type": "update_fields",
            "form_id": "wf_batchtask_handle",
            "fields": {
                "decision_radio_group": "Consent",
                "msg_approval": {"GLang": "同意", "zh_CN": "同意"},
            },
        }],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["fields"]["decision_radio_group"] == "Reject"


def test_workflow_combo_decision_override_respects_source_step_scope():
    case = {
        "pick_fields": {
            "pick_combo_decision_id": {
                "field_key": "combo_decision",
                "value_id": "审批不通过_and_Disapproved_and_terminate",
                "value_name": "审批不通过_and_Disapproved_and_terminate",
                "form_id": "wf_approvalpage_bac",
                "source_step_id": "fill_combo_decision_2",
                "user_overridden": True,
                "resolve_status": "manual",
            },
        },
        "steps": [
            {
                "id": "fill_combo_decision",
                "type": "update_fields",
                "form_id": "wf_approvalpage_bac",
                "fields": {"combo_decision": "同意_and_Consent_and_approve"},
            },
            {
                "id": "fill_combo_decision_2",
                "type": "update_fields",
                "form_id": "wf_approvalpage_bac",
                "fields": {"combo_decision": "同意_and_Consent_and_approve"},
            },
        ],
    }

    _apply_pick_fields(case)

    assert case["steps"][0]["fields"]["combo_decision"] == "同意_and_Consent_and_approve"
    assert case["steps"][1]["fields"]["combo_decision"] == "审批不通过_and_Disapproved_and_terminate"


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
