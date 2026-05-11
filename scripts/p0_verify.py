"""P0 验证脚本
1) 用 4 个代表性 HAR 调 /api/har/extract 生成 P0验证-* 用例
2) 读取 assertions 段打印对比
3) 创建并启动批量任务，轮询等待完成
4) 汇总每条用例的通过/失败
"""
from __future__ import annotations
import json
import sys
import time
import urllib.request
import urllib.error
from pathlib import Path

BASE = "http://127.0.0.1:8768"
ROOT = Path(__file__).resolve().parent.parent
CASES_DIR = ROOT / "cases"

# 按 main_form_id 选最新时间戳的 HAR（去重）
TARGETS = [
    ("preview_1778155690_preview_1778053644_新增一条行政组织.har", "P0验证-新增行政组织"),
    ("preview_1778155691_preview_1778054267_业务模型添加一个基础资料附表.har", "P0验证-业务模型附表"),
    ("preview_1778155692_preview_1778054469_入职申请到确认入职.har", "P0验证-入职申请到确认"),
    ("preview_1778155693_preview_1778049824_HR基础服务云新增一条用工关系基础资料.har", "P0验证-HR用工关系"),
]


def http_json(method: str, path: str, body: dict | None = None, timeout: int = 120) -> dict:
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


def read_assertions(case_name: str) -> list[str]:
    p = CASES_DIR / f"{case_name}.yaml"
    if not p.exists():
        return [f"<文件不存在: {p}>"]
    text = p.read_text(encoding="utf-8")
    # 从 assertions: 开始读到文件尾
    lines = text.splitlines()
    out = []
    hit = False
    for ln in lines:
        if ln.startswith("assertions:"):
            hit = True
        if hit:
            out.append(ln)
    return out or ["<未找到 assertions 段>"]


def main():
    print("=" * 70)
    print("【步骤 1】 调 /api/har/extract 重新生成 4 个 P0 验证用例")
    print("=" * 70)
    generated = []
    for har_file, case_name in TARGETS:
        print(f"\n→ HAR: {har_file}")
        print(f"  Case: {case_name}")
        try:
            r = http_json("POST", "/api/har/extract",
                          {"har_file": har_file, "case_name": case_name})
            print(f"  ✓ {r.get('action')} 成功: {r.get('file')}")
            generated.append(case_name)
        except Exception as e:
            print(f"  ✗ 失败: {e}")

    print("\n" + "=" * 70)
    print("【步骤 2】 读取新生成 yaml 的 assertions 段")
    print("=" * 70)
    for case_name in generated:
        print(f"\n--- {case_name} ---")
        for ln in read_assertions(case_name):
            print(ln)

    if not generated:
        print("\n!! 没有用例生成成功，中止批量执行")
        sys.exit(1)

    print("\n" + "=" * 70)
    print("【步骤 3】 创建批量任务（并发=3）")
    print("=" * 70)
    task = http_json("POST", "/api/tasks", {
        "case_names": generated,
        "name": "P0断言修复验证",
        "concurrency": 3,
    })
    task_id = task["task_id"]
    print(f"✓ 任务 ID: {task_id}, 用例数: {task.get('total_count')}")

    print("\n【步骤 4】 启动任务")
    r = http_json("POST", f"/api/tasks/{task_id}/start", {})
    print(f"✓ 启动结果: {r}")

    print("\n【步骤 5】 轮询等待完成（最多 600 秒）")
    start = time.time()
    last_status = None
    while time.time() - start < 600:
        t = http_json("GET", f"/api/tasks/{task_id}")
        status = t.get("status")
        passed = t.get("passed_count", 0)
        failed = t.get("failed_count", 0)
        total = t.get("total_count", 0)
        running = total - passed - failed
        if status != last_status or (passed + failed) > 0:
            print(f"  [{int(time.time()-start):3d}s] status={status} "
                  f"passed={passed} failed={failed} running={running}")
            last_status = status
        if status in ("completed", "failed", "cancelled"):
            break
        time.sleep(5)

    print("\n" + "=" * 70)
    print("【步骤 6】 最终结果")
    print("=" * 70)
    t = http_json("GET", f"/api/tasks/{task_id}")
    print(f"状态: {t.get('status')}")
    print(f"耗时: {t.get('duration_s')}s")
    print(f"通过: {t.get('passed_count')}/{t.get('total_count')}")
    print(f"失败: {t.get('failed_count')}")
    print(f"通过率: {t.get('pass_rate')}%")
    print("\n逐条结果:")
    for res in t.get("results", []):
        icon = "✓" if res.get("status") == "passed" else "✗"
        name = res.get("case_name")
        dur = res.get("duration_s", 0)
        err = res.get("error_message", "") or ""
        steps_ok = res.get("steps_passed", 0)
        steps_total = res.get("steps_total", 0)
        asserts_ok = res.get("assertions_passed", 0)
        asserts_total = res.get("assertions_total", 0)
        print(f"  {icon} {name:40s} {dur:6.1f}s  "
              f"step {steps_ok}/{steps_total}  assert {asserts_ok}/{asserts_total}")
        if err:
            print(f"      错误: {err[:200]}")


if __name__ == "__main__":
    main()
