from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent


def _index_html() -> str:
    return (PROJECT_ROOT / "lib" / "webui" / "static" / "index.html").read_text(encoding="utf-8")


def test_har_preview_grouping_keeps_original_field_object_references():
    html = _index_html()

    assert "const item = raw;" in html
    assert "const item = {...raw};" not in html


def test_har_preview_env_fields_have_explicit_confirm_action():
    html = _index_html()

    assert "@click=\"savePickFieldValue(pf.id, pickFieldDisplayValue(pf), 'display')\"" in html
    assert "已修改，生成 YAML 后生效" in html
