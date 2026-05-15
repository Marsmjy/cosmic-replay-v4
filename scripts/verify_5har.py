"""验证5种HAR类型的导入和执行"""
import requests
import json
import time
import sys

BASE = "http://127.0.0.1:8768"

# 5种HAR（每种取最新）
HAR_FILES = [
    ("行政组织", "preview_1778575127_新增一条行政组织.har"),
    ("入职申请到确认入职", "preview_1778569435_入职申请到确认入职.har"),
    ("新增入职0512测试", "preview_1778575043_新增入职0512测试.har"),
    ("业务模型", "preview_1778558127_业务模型添加一个基础资料附表.har"),
    ("用工关系", "preview_1778557847_HR基础服务云新增一条用工关系基础资料.har"),
]

ENV_ID = "sit2222"
results = []

def wait_for_run(run_id, timeout=300):
    """通过 SSE 等待运行完成，收集结果"""
    url = f"{BASE}/api/runs/{run_id}/events"
    final_result = None
    try:
        resp = requests.get(url, stream=True, timeout=timeout)
        current_event = ""
        for line in resp.iter_lines(decode_unicode=True):
            if line is None:
                continue
            line_str = line.strip() if isinstance(line, str) else line.decode('utf-8', errors='replace').strip()
            # SSE format: event: xxx\ndata: {...}\n\n
            if line_str.startswith("event:"):
                current_event = line_str[6:].strip()
                continue
            if line_str.startswith("data:"):
                data_str = line_str[5:].strip()
                if not data_str:
                    continue
                try:
                    evt_data = json.loads(data_str)
                except json.JSONDecodeError:
                    continue
                if current_event == "case_done":
                    final_result = evt_data
                    break
                elif current_event == "close":
                    break
                elif current_event == "error":
                    final_result = {"passed": False, "error": evt_data.get("message", str(evt_data))}
                    break
                current_event = ""
            # skip keepalive comments and empty lines
    except requests.exceptions.Timeout:
        final_result = {"passed": False, "error": "timeout"}
    except Exception as e:
        final_result = {"passed": False, "error": str(e)}
    return final_result


print("=" * 70)
print("验证5种HAR导入 + 执行")
print("=" * 70)

for idx, (har_type, har_filename) in enumerate(HAR_FILES, 1):
    case_name = f"验证_{har_type}"
    print(f"\n{'─' * 60}")
    print(f"[{idx}/5] {har_type}")
    print(f"  HAR: {har_filename}")
    print(f"  用例名: {case_name}")

    # Step 1: Extract (直接用已有的HAR文件生成YAML)
    print(f"  → 正在导入生成YAML...")
    try:
        extract_resp = requests.post(f"{BASE}/api/har/extract", json={
            "har_file": har_filename,
            "case_name": case_name,
        }, timeout=60)
        extract_data = extract_resp.json()
        if not extract_data.get("ok", True):
            err = extract_data.get("error", "unknown")
            print(f"  ✗ 导入失败: {err}")
            results.append({
                "idx": idx, "type": har_type, "case_name": case_name,
                "steps": "-", "passed": False, "error": f"导入失败: {err}"
            })
            continue
        print(f"  ✓ YAML生成成功")
    except Exception as e:
        print(f"  ✗ 导入异常: {e}")
        results.append({
            "idx": idx, "type": har_type, "case_name": case_name,
            "steps": "-", "passed": False, "error": f"导入异常: {e}"
        })
        continue

    # Step 2: Run
    print(f"  → 正在执行用例...")
    try:
        run_resp = requests.post(f"{BASE}/api/cases/{case_name}/run", json={
            "env_id": ENV_ID,
        }, timeout=30)
        run_data = run_resp.json()
        run_id = run_data.get("run_id")
        if not run_id:
            print(f"  ✗ 启动失败: {run_data}")
            results.append({
                "idx": idx, "type": har_type, "case_name": case_name,
                "steps": "-", "passed": False, "error": f"启动失败: {run_data}"
            })
            continue
        print(f"  run_id: {run_id}")
    except Exception as e:
        print(f"  ✗ 启动异常: {e}")
        results.append({
            "idx": idx, "type": har_type, "case_name": case_name,
            "steps": "-", "passed": False, "error": f"启动异常: {e}"
        })
        continue

    # Step 3: Wait for result
    print(f"  → 等待执行完成...")
    result = wait_for_run(run_id, timeout=300)
    if result is None:
        print(f"  ✗ 未收到结果")
        results.append({
            "idx": idx, "type": har_type, "case_name": case_name,
            "steps": "-", "passed": False, "error": "未收到case_done事件"
        })
    else:
        passed = result.get("passed", False)
        step_ok = result.get("step_ok", 0)
        step_count = result.get("step_count", 0)
        duration = result.get("duration_s", 0)
        error_msg = result.get("error", "")
        if not error_msg and not passed:
            # 尝试从 fail_step 获取错误信息
            fail_step = result.get("fail_step", "")
            fail_msg = result.get("fail_message", "") or result.get("message", "")
            error_msg = f"步骤 '{fail_step}' 失败: {fail_msg}" if fail_step else str(result)

        status = "PASS" if passed else "FAIL"
        print(f"  {'✓' if passed else '✗'} {status} ({step_ok}/{step_count} 步, {duration:.1f}s)")
        if not passed and error_msg:
            print(f"  错误: {error_msg[:200]}")
        results.append({
            "idx": idx, "type": har_type, "case_name": case_name,
            "steps": f"{step_ok}/{step_count}",
            "passed": passed, "error": error_msg if not passed else "",
            "duration": duration,
        })

# Final summary
print(f"\n\n{'═' * 70}")
print("最终结果汇总")
print(f"{'═' * 70}")
print(f"{'序号':<4} | {'HAR类型':<20} | {'步骤':<8} | {'结果':<6} | 失败详情")
print(f"{'─' * 4}-+-{'─' * 20}-+-{'─' * 8}-+-{'─' * 6}-+-{'─' * 30}")
for r in results:
    status = "PASS" if r["passed"] else "FAIL"
    err_short = r.get("error", "")[:50]
    print(f"{r['idx']:<4} | {r['type']:<20} | {r['steps']:<8} | {status:<6} | {err_short}")

total = len(results)
passed = sum(1 for r in results if r["passed"])
print(f"\n总计: {passed}/{total} 通过")
