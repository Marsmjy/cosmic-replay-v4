"""批量执行所有用例并收集结果"""
import requests
import time
import json

BASE = "http://127.0.0.1:8768"

# 1. 列出所有用例
r = requests.get(f"{BASE}/api/cases")
cases = r.json()
print(f"共 {len(cases)} 个用例:")
for c in cases:
    print(f"  {c['name']} ({c.get('step_count','?')} steps)")

# 2. 逐个触发执行并等待完成
run_ids = {}
results = {}
max_wait = 300  # 最长等待5分钟

for c in cases:
    name = c["name"]
    print(f"\n>>> [{cases.index(c)+1}/{len(cases)}] 触发执行: {name}")
    resp = requests.post(f"{BASE}/api/cases/{name}/run", json={})
    data = resp.json()
    if "run_id" not in data:
        print(f"    ERROR: {data}")
        run_ids[name] = None
        results[name] = {"passed": False, "error": f"触发失败: {data}"}
        continue
    
    run_id = data["run_id"]
    run_ids[name] = run_id
    print(f"    run_id: {run_id}, 等待完成...")
    
    start = time.time()
    done = False
    while time.time() - start < max_wait:
        try:
            r = requests.get(f"{BASE}/api/run_history/{run_id}")
            if r.status_code == 200:
                d = r.json()
                events = d.get("events", [])
                case_done = [e for e in events if e.get("type") == "case_done"]
                if case_done:
                    results[name] = case_done[0].get("data", {})
                    done = True
                    break
        except:
            pass
        time.sleep(5)
    
    if not done:
        results[name] = {"passed": False, "error": f"超时({max_wait}s)", "run_id": run_id}
        print(f"  -> TIMEOUT")
    else:
        elapsed = time.time() - start
        status = "PASSED" if results[name].get("passed") else "FAILED"
        print(f"  -> {status} ({results[name].get('duration_s', '?')}s)")

print("\n\n=== 所有用例执行完毕 ===")

# 4. 输出汇总
print("\n\n" + "="*80)
print("执行结果汇总")
print("="*80)
print(f"{'用例名称':<40} {'结果':<10} {'步骤':<15} {'耗时':<10} {'备注'}")
print("-"*80)

for name, data in results.items():
    passed = data.get("passed", False)
    status = "PASSED" if passed else "FAILED"
    step_count = data.get("step_count", "?")
    step_ok = data.get("step_ok", "?")
    duration = data.get("duration_s", "?")
    duration_str = f"{duration:.1f}s" if isinstance(duration, (int, float)) else str(duration)
    step_str = f"{step_ok}/{step_count}"
    
    note = ""
    if not passed:
        if "error" in data:
            note = data["error"]
        else:
            note = f"fail={data.get('step_fail',0)}, assert_fail={data.get('assertion_fail',0)}"
    
    print(f"{name:<40} {status:<10} {step_str:<15} {duration_str:<10} {note}")

# 5. 如果有失败用例，打印详细失败信息
failed_cases = {n: d for n, d in results.items() if not d.get("passed")}
if failed_cases:
    print(f"\n\n=== 失败用例详情 ({len(failed_cases)}个) ===")
    for name, run_id_val in run_ids.items():
        if name not in failed_cases or not run_id_val:
            continue
        print(f"\n--- {name} (run_id={run_id_val}) ---")
        try:
            r = requests.get(f"{BASE}/api/run_history/{run_id_val}")
            if r.status_code == 200:
                events = r.json().get("events", [])
                # 找到失败的step
                fail_events = [e for e in events if e.get("type") == "step_fail" or 
                              (e.get("type") == "step_ok" and not e.get("data", {}).get("ok", True))]
                err_events = [e for e in events if "err" in e.get("type", "").lower() or 
                             "fail" in e.get("type", "").lower()]
                for e in err_events[:5]:
                    print(f"  [{e.get('type')}] {json.dumps(e.get('data',{}), ensure_ascii=False)[:200]}")
        except Exception as ex:
            print(f"  获取详情失败: {ex}")

print("\n\n=== 执行完毕 ===")
