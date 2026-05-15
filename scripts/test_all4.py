"""重新导入其他4种HAR并执行验证"""
import requests, json, time, pathlib

BASE = "http://127.0.0.1:8768"
HAR_DIR = pathlib.Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads")

# 4种HAR（排除0512）
hars = [
    ("preview_1778726790_HR基础服务云新增一条用工关系基础资料.har", "HR基础服务云新增一条用工关系基础资料"),
    ("preview_1778726861_入职申请到确认入职.har", "入职申请到确认入职"),
    ("preview_1778726914_新增一条行政组织.har", "新增一条行政组织"),
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]

results = []

for har_filename, case_name in hars:
    har_path = HAR_DIR / har_filename
    if not har_path.exists():
        print(f"[SKIP] {har_filename} not found")
        results.append((case_name, "SKIP", 0, 0, 0))
        continue
    
    print(f"\n{'='*60}")
    print(f"导入: {case_name}")
    print(f"{'='*60}")
    
    # Step 1: Upload & Preview
    with open(har_path, "rb") as f:
        content = f.read()
    r = requests.post(
        f"{BASE}/api/har/preview?filename={har_filename}",
        data=content,
        headers={"Content-Type": "application/octet-stream"}
    )
    if r.status_code != 200:
        print(f"  [ERR] Preview failed: {r.status_code}")
        results.append((case_name, "IMPORT_ERR", 0, 0, 0))
        continue
    har_file_name = r.json().get("har_file", "")
    print(f"  Preview OK: {har_file_name}")
    
    # Step 2: Extract
    r = requests.post(f"{BASE}/api/har/extract", json={
        "har_file": har_file_name,
        "case_name": case_name
    })
    if r.status_code != 200:
        print(f"  [ERR] Extract failed: {r.status_code} {r.text[:200]}")
        results.append((case_name, "EXTRACT_ERR", 0, 0, 0))
        continue
    print(f"  Extract OK: {r.json().get('action', '?')}")
    
    # Step 3: Run
    r = requests.post(f"{BASE}/api/cases/{case_name}/run", json={})
    if r.status_code != 200:
        print(f"  [ERR] Run failed: {r.status_code}")
        results.append((case_name, "RUN_ERR", 0, 0, 0))
        continue
    run_id = r.json().get("run_id", "")
    print(f"  Running... (run_id: {run_id})")
    
    # Step 4: Wait for completion
    passed = False
    step_ok = 0
    step_count = 0
    duration = 0
    error_msg = ""
    
    for _ in range(30):  # max 150s
        time.sleep(5)
        r = requests.get(f"{BASE}/api/run_history/{run_id}")
        events = r.json().get("events", [])
        done = [e for e in events if e["type"] == "case_done"]
        if done:
            d = done[0]["data"]
            passed = d.get("passed", False)
            step_ok = d.get("step_ok", 0)
            step_count = d.get("step_count", 0)
            duration = d.get("duration_s", 0)
            if not passed:
                fails = [e for e in events if e["type"] == "step_fail"]
                if fails:
                    fd = fails[0].get("data", {})
                    error_msg = f"{fd.get('id','?')}: {json.dumps(fd.get('errors',[]), ensure_ascii=False)[:150]}"
            break
    else:
        error_msg = "TIMEOUT"
    
    status = "PASSED" if passed else "FAILED"
    print(f"  Result: {status} ({step_ok}/{step_count}, {duration:.1f}s)")
    if error_msg:
        print(f"  Error: {error_msg}")
    results.append((case_name, status, step_ok, step_count, duration))

# Summary
print("\n\n" + "="*80)
print("最终结果汇总")
print("="*80)
print(f"{'用例名称':<40} {'结果':<10} {'步骤':<15} {'耗时'}")
print("-"*80)
all_passed = True
for name, status, ok, total, dur in results:
    print(f"{name:<40} {status:<10} {ok}/{total}{'':<10} {dur:.1f}s")
    if status != "PASSED":
        all_passed = False

print("-"*80)
print(f"总计: {sum(1 for _,s,_,_,_ in results if s=='PASSED')}/{len(results)} 通过")
if all_passed:
    print("\n✓ 全部通过！")
else:
    print("\n✗ 有用例失败")
