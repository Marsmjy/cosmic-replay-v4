import requests, json, time

BASE = "http://127.0.0.1:8768"

# Check existing results
print("=== 已完成的用例结果 ===")
for run_id in ["803994e8cfe1", "c4b5bde8c557"]:
    r = requests.get(f"{BASE}/api/run_history/{run_id}")
    events = r.json().get("events", [])
    done = [e for e in events if e["type"] == "case_done"]
    start = [e for e in events if e["type"] == "case_start"]
    name = start[0]["data"]["name"] if start else "?"
    if done:
        d = done[0]["data"]
        print(f"  {name}: {'PASSED' if d['passed'] else 'FAILED'} ({d['step_ok']}/{d['step_count']}, {d['duration_s']:.1f}s)")
    else:
        print(f"  {name}: INCOMPLETE")

# Now import and run remaining 2
import pathlib
HAR_DIR = pathlib.Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads")

remaining = [
    ("preview_1778726914_新增一条行政组织.har", "新增一条行政组织"),
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]

print("\n=== 导入并执行剩余2个用例 ===")
run_ids = []
for har_filename, case_name in remaining:
    har_path = HAR_DIR / har_filename
    with open(har_path, "rb") as f:
        content = f.read()
    
    # Preview
    r = requests.post(f"{BASE}/api/har/preview?filename={har_filename}", data=content,
                      headers={"Content-Type": "application/octet-stream"})
    har_file_name = r.json().get("har_file", "")
    
    # Extract
    r = requests.post(f"{BASE}/api/har/extract", json={"har_file": har_file_name, "case_name": case_name})
    print(f"  {case_name}: extracted ({r.json().get('action','?')})")
    
    # Run
    r = requests.post(f"{BASE}/api/cases/{case_name}/run", json={})
    rid = r.json().get("run_id", "")
    run_ids.append((case_name, rid))
    print(f"    run_id: {rid}")

# Wait for all to complete
print("\n  等待执行完成...")
time.sleep(30)

for case_name, rid in run_ids:
    for _ in range(20):
        r = requests.get(f"{BASE}/api/run_history/{rid}")
        events = r.json().get("events", [])
        done = [e for e in events if e["type"] == "case_done"]
        if done:
            d = done[0]["data"]
            status = "PASSED" if d["passed"] else "FAILED"
            print(f"  {case_name}: {status} ({d['step_ok']}/{d['step_count']}, {d['duration_s']:.1f}s)")
            if not d["passed"]:
                fails = [e for e in events if e["type"] == "step_fail"]
                for f in fails[:2]:
                    fd = f.get("data", {})
                    print(f"    ERR: {fd.get('id','?')}")
            break
        time.sleep(5)
    else:
        print(f"  {case_name}: TIMEOUT")

print("\n=== 完成 ===")
