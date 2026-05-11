"""P1 描述升级对比脚本

目的：对比"旧 P0验证-xxx.yaml"（机械模板） vs "新 P1验证-xxx.yaml"（业务语义）
只做 yaml 重新生成 + 描述对比，不执行用例。

执行日志 label 的升级需要重启服务（runner.py 改动不会被 /api/har/extract 的 reload 覆盖）。
"""
from __future__ import annotations
import json
import re
import sys
import urllib.request
import urllib.error
from pathlib import Path

BASE = "http://127.0.0.1:8768"
ROOT = Path(__file__).resolve().parent.parent
CASES_DIR = ROOT / "cases"

# 与 p0_verify.py 使用相同的 HAR 源
TARGETS = [
    ("preview_1778155690_preview_1778053644_新增一条行政组织.har",
     "P0验证-新增行政组织", "P1验证-新增行政组织"),
    ("preview_1778155691_preview_1778054267_业务模型添加一个基础资料附表.har",
     "P0验证-业务模型附表", "P1验证-业务模型附表"),
    ("preview_1778155692_preview_1778054469_入职申请到确认入职.har",
     "P0验证-入职申请到确认", "P1验证-入职申请到确认"),
    ("preview_1778155693_preview_1778049824_HR基础服务云新增一条用工关系基础资料.har",
     "P0验证-HR用工关系", "P1验证-HR用工关系"),
]


def http_json(method: str, path: str, body: dict | None = None, timeout: int = 180) -> dict:
    url = BASE + path
    data = json.dumps(body, ensure_ascii=False).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, method=method,
                                 headers={"Content-Type": "application/json"})
    try:
        with urllib.request.urlopen(req, timeout=timeout) as r:
            return json.loads(r.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        body_txt = e.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"HTTP {e.code} on {method} {path}: {body_txt}") from e


def read_case_desc(case_name: str) -> tuple[str, list[str]]:
    """返回 (顶层 description, 前 12 步 step.description)"""
    p = CASES_DIR / f"{case_name}.yaml"
    if not p.exists():
        return (f"<文件不存在: {p.name}>", [])
    text = p.read_text(encoding="utf-8")

    # 顶层 description（出现在 steps: 之前的第一个顶格 description:）
    top_desc = ""
    for ln in text.splitlines():
        if ln.startswith("steps:") or ln.startswith("main_form_id:"):
            break
        m = re.match(r"^description:\s*(.+)$", ln)
        if m:
            top_desc = m.group(1).strip()
            break

    # step.description（缩进 4 空格的 description:）
    step_descs: list[str] = []
    for ln in text.splitlines():
        m = re.match(r"^    description:\s*(.+)$", ln)
        if m:
            step_descs.append(m.group(1).strip())
        if len(step_descs) >= 12:
            break

    return top_desc, step_descs


def main():
    print("=" * 72)
    print("【步骤 1】 调 /api/har/extract 重新生成 4 个 P1 验证 yaml")
    print("=" * 72)
    generated: list[tuple[str, str]] = []  # (old_name, new_name)
    for har_file, old_name, new_name in TARGETS:
        print(f"\n→ HAR: {har_file}")
        print(f"  新 yaml: {new_name}")
        try:
            r = http_json("POST", "/api/har/extract",
                          {"har_file": har_file, "case_name": new_name})
            print(f"  ✓ {r.get('action')} 成功")
            generated.append((old_name, new_name))
        except Exception as e:
            print(f"  ✗ 失败: {e}")

    if not generated:
        print("\n!! 没有用例生成成功，终止")
        sys.exit(1)

    print("\n" + "=" * 72)
    print("【步骤 2】 旧 vs 新 描述对比")
    print("=" * 72)

    for old_name, new_name in generated:
        old_top, old_steps = read_case_desc(old_name)
        new_top, new_steps = read_case_desc(new_name)
        print("\n" + "─" * 72)
        print(f"  {new_name}")
        print("─" * 72)
        print(f"【顶层 description】")
        print(f"  旧: {old_top}")
        print(f"  新: {new_top}")
        print(f"\n【前 {max(len(old_steps), len(new_steps))} 步 step.description】")
        n = max(len(old_steps), len(new_steps))
        for i in range(n):
            o = old_steps[i] if i < len(old_steps) else "<无>"
            n2 = new_steps[i] if i < len(new_steps) else "<无>"
            flag = "  " if o == n2 else "🔄"
            print(f"  {flag} #{i+1}")
            print(f"     旧: {o}")
            print(f"     新: {n2}")

    print("\n" + "=" * 72)
    print("【说明】 runner.py 改动（assertion msg / emit step_label）需要重启服务才能")
    print("         反映到 jsonl 日志。重启后可直接跑 scripts/p0_verify.py 观察执行日志。")
    print("=" * 72)


if __name__ == "__main__":
    main()
