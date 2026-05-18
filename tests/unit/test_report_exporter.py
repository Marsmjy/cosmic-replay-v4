from lib.report_exporter import export_html


def test_export_html_embeds_local_assets_without_cdn():
    html = export_html(
        {
            "task_id": "task_1",
            "task_name": "离线报告",
            "env": "sit",
            "summary": {
                "total_cases": 1,
                "passed_cases": 1,
                "failed_cases": 0,
                "pass_rate": 1,
                "total_steps": 3,
                "total_duration_s": 1.2,
            },
            "case_results": [
                {
                    "name": "case_a",
                    "passed": True,
                    "step_ok": 3,
                    "step_count": 3,
                    "duration_s": 1.2,
                }
            ],
        }
    )

    assert '<script src="https://cdn.jsdelivr.net' not in html
    assert '<script src="https://cdn.tailwindcss.com' not in html
    assert "new Chart(" in html
    assert "离线报告" in html
