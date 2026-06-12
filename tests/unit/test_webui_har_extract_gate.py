from pathlib import Path

from fastapi.testclient import TestClient

from lib.webui import server


def test_har_extract_does_not_write_case_when_generation_gate_blocks(tmp_path, monkeypatch):
    upload_dir = tmp_path / "uploads"
    upload_dir.mkdir()
    (upload_dir / "blocked.har").write_text("{}", encoding="utf-8")
    output_path = tmp_path / "cases" / "blocked.yaml"

    monkeypatch.setattr(server, "har_upload_dir", lambda: upload_dir)
    monkeypatch.setattr(server, "case_path_from_name", lambda _name: output_path)
    monkeypatch.setattr(server, "unique_case_name", lambda name: name)
    monkeypatch.setattr(server.importlib if hasattr(server, "importlib") else __import__("importlib"), "reload", lambda module: module)
    monkeypatch.setattr(
        server.har_extractor,
        "build_yaml_case",
        lambda *_args, **_kwargs: (
            "name: blocked\n"
            "generation_gate:\n"
            "  allow_generate: false\n"
            "  issues:\n"
            "  - code: ir_write_anchor_uncovered\n"
            "    message: HAR 写入动作没有进入生成 YAML\n"
            "    blocks_generate: true\n"
        ),
    )

    response = TestClient(server.APP).post(
        "/api/har/extract",
        json={"har_file": "blocked.har", "case_name": "blocked"},
    )

    assert response.status_code == 422
    assert "不能安全生成" in response.text
    assert not output_path.exists()
