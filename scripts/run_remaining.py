"""执行剩余3个用例"""
import requests
import time
import json

BASE = "http://127.0.0.1:8768"

remaining = ["入职申请到确认入职", "新增一条行政组织", "新增入职0512测试"]
results = {}
run_ids = {}

for i, name in enumerate(remaining):
    print(f"\n>>> [{i+1}/{len(remaining)}] 触发执行: {name}")
    resp = requests.post(f"{BASE}/api/cases/{name}/run", json={})
    data = resp.json()
    if "run_id" not in data:
        print(f"    ERROR: {data}")
        results[name] = {"passed": False, "error": str(data)}
        continue
    
    run_id = data["run_id"]
    run_ids[name] = run_id
    print(f"    run_id: {run_id}, 等待完成...")
    
    start = time.time()
    done = False
    max_wait = 300
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
        results[name] = {"passed": False, "error": f"超时({max_wait}s)"}
        print(f"  -> TIMEOUT")
    else:
        status = "PASSED" if results[name].get("passed") else "FAILED"
        print(f"  -> {status} ({results[name].get('duration_s', '?')}s)")

# 合并之前已完成的2个
prev_results = {
    "HR基础服务云新增一条用工关系基础资料": {"passed": True, "duration_s": 4.32, "step_count": 19, "step_ok": 19, "step_fail": 0, "assertion_ok": 0, "assertion_fail": 0},
    "业务模型添加一个基础资料附表": {"passed": True, "duration_s": 19.6, "step_count": 209, "step_ok": 209, "step_fail": 0, "assertion_ok": 0, "assertion_fail": 0},
}

# 获取之前run的详细信息
for prev_name, prev_run_id in [("HR基础服务云新增一条用工关系基础资料", "0d74958b7251"), ("业务模型添加一个基础资料附表", "d1fc4ecb46a3")]:
    r = requests.get(f"{BASE}/api/run_history/{prev_run_id}")
    if r.status_code == 200:
        events = r.json().get("events", [])
        case_done = [e for e in events if e.get("type") == "case_done"]
        if case_done:
            prev_results[prev_name] = case_done[0].get("data", {})

all_results = {**prev_results, **results}

# 输出汇总
print("\n\n" + "="*80)
print("执行结果汇总")
print("="*80)
print(f"{'用例名称':<40} {'结果':<10} {'步骤':<15} {'耗时':<10} {'备注'}")
print("-"*80)

for name, data in all_results.items():
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

# 失败用例详情
failed = {n: d for n, d in all_results.items() if not d.get("passed")}
if failed:
    print(f"\n\n=== 失败用例详情 ({len(failed)}个) ===")
    for name in failed:
        rid = run_ids.get(name)
        if not rid:
            continue
        print(f"\n--- {name} (run_id={rid}) ---")
        try:
            r = requests.get(f"{BASE}/api/run_history/{rid}")
            if r.status_code == 200:
                events = r.json().get("events", [])
                err_events = [e for e in events if "fail" in e.get("type", "").lower() or "err" in e.get("type", "").lower()]
                for e in err_events[:5]:
                    print(f"  [{e.get('type')}] {json.dumps(e.get('data',{}), ensure_ascii=False)[:300]}")
        except Exception as ex:
            print(f"  获取详情失败: {ex}")

total = len(all_results)
passed_count = sum(1 for d in all_results.values() if d.get("passed"))
print(f"\n\n总计: {passed_count}/{total} 通过")
