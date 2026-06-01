from lib.har_preflight import assess_har_preflight, assess_pageid_alignment


def test_pageid_alignment_flags_l3_step_using_l2_pageid():
    steps = [
        {
            "id": "fill_name",
            "type": "update_fields",
            "form_id": "demo_form",
            "fields": {"name": "CRPLY"},
            "_har_page_id": "123root0123456789abcdef0123456789abcdef",
        }
    ]

    result = assess_pageid_alignment(steps)

    assert result["score"] < 90
    assert result["risk_level"] == "high"
    assert result["checks"]["risk_counts"]["har_l2_on_l3_step"] == 1
    assert any(issue["code"] == "har_l2_on_l3_step" for issue in result["issues"])


def test_pageid_alignment_scores_l2_navigation_and_l3_edit_chain_as_stable():
    steps = [
        {
            "id": "open_menu",
            "type": "invoke",
            "form_id": "demo_list",
            "ac": "menuItemClick",
            "method": "menuItemClick",
            "preserve_l2_page": True,
            "_har_page_id": "123root0123456789abcdef0123456789abcdef",
        },
        {
            "id": "fill_name",
            "type": "update_fields",
            "form_id": "demo_form",
            "fields": {"name": "CRPLY"},
            "_har_page_id": "0123456789abcdef0123456789abcdef",
        },
    ]

    result = assess_pageid_alignment(steps)

    assert result["score"] >= 85
    assert result["risk_level"] == "low"
    assert result["checks"]["preserve_l2_step_count"] == 1


def test_preflight_blocks_missing_main_form_and_core_steps():
    result = assess_har_preflight(
        main_form_id="",
        tier_counts={"core": 0, "ui_reaction": 2, "noise": 1},
        steps=[],
        detected_vars=[],
        pick_fields=[],
        component_report={"summary": {"coverage_percent": 100, "unsupported_steps": 0}},
        quality={"score": 20, "issues": []},
        pageid_alignment={"score": 100, "issues": []},
    )

    assert result["decision"] == "blocked"
    assert result["allow_generate"] is False
    assert any(issue["code"] == "main_form_missing" for issue in result["issues"])


def test_preflight_recommends_review_for_medium_pageid_score():
    result = assess_har_preflight(
        main_form_id="demo_form",
        tier_counts={"core": 3, "ui_reaction": 1, "noise": 0},
        steps=[
            {"type": "update_fields", "id": "fill_name", "fields": {"name": "CRPLY"}},
            {"type": "invoke", "id": "save", "ac": "save", "key": "bar_save"},
        ],
        detected_vars=[{"name": "test_name"}],
        pick_fields=[],
        component_report={"summary": {"coverage_percent": 95, "unsupported_steps": 0}},
        quality={"score": 88, "issues": []},
        pageid_alignment={"score": 82, "risk_level": "medium", "issues": []},
    )

    assert result["decision"] == "review"
    assert result["allow_generate"] is True
    assert result["recommend_generate"] is True


def test_preflight_uses_ir_alignment_to_surface_missing_write_coverage():
    result = assess_har_preflight(
        main_form_id="demo_form",
        tier_counts={"core": 3, "ui_reaction": 1, "noise": 0},
        steps=[
            {"type": "invoke", "id": "open", "ac": "loadData"},
        ],
        detected_vars=[],
        pick_fields=[],
        component_report={"summary": {"coverage_percent": 95, "unsupported_steps": 0}},
        quality={"score": 88, "issues": []},
        pageid_alignment={"score": 92, "risk_level": "low", "issues": []},
        ir_alignment={
            "score": 68,
            "risk_level": "high",
            "issues": [
                {
                    "severity": "high",
                    "code": "write_step_not_covered",
                    "message": "IR 发现写入动作，但主解析链路未覆盖保存/提交/确认步骤。",
                    "suggestion": "检查 ac/method/key/args 识别。",
                }
            ],
        },
    )

    assert result["decision"] == "risky"
    assert result["checks"]["ir_alignment_score"] == 68
    assert any(issue["code"] == "ir_write_step_not_covered" for issue in result["issues"])
    assert any("IR 覆盖雷达" in action for action in result["next_actions"])
