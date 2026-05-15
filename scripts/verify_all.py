"""Full 5-HAR verification: re-extract + run all, report results."""
import requests, time, json, sys

BASE = "http://127.0.0.1:8768"

HARS = [
    ("行政组织", "preview_1778585085_preview_1778575127_新增一条行政组织.har", "final_行政组织"),
    ("入职申请到确认入职", "preview_1778585097_preview_1778584837_入职申请到确认入职.har", "final_入职申请"),
    ("新增入职0512测试", "preview_1778585116_preview_1778575043_新增入职0512测试.har", "final_新增入职"),
    ("业务模型", "preview_1778585120_preview_1778558127_业务模型添加一个基础资料附表.har", "final_业务模型"),
    ("用工关系", "preview_1778585142_preview_1778557847_HR基础服务云新增一条用工关系基础资料.har", "final_用工关系"),
]

# Find actual HAR filenames from server
def find_har_file(partial_name):
    """Find the actual HAR file on disk matching partial name."""
    from pathlib import Path
    har_dir = Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads")
    candidates = list(har_dir.glob(f"*{partial_name}"))
    if candidates:
        return candidates[-1].name  # Use latest
    # Try without prefix
    candidates = list(har_dir.glob(f"*{partial_name.split('_', 2)[-1] if '_' in partial_name else partial_name}"))
    return candidates[-1].name if candidates else partial_name

results = []
for label, har_file, case_name in HARS:
    print(f"\n{'='*60}")
    print(f"  Testing: {label}")
    print(f"{'='*60}")
    
    # Find actual HAR file
    actual_har = find_har_file(har_file)
    print(f"  HAR: {actual_har}")
    
    # 1. Extract
    print(f"  [1/3] Extracting HAR -> YAML...")
    try:
        extract = requests.post(f"{BASE}/api/har/extract", json={
            "har_file": actual_har,
            "case_name": case_name,
        }, timeout=60).json()
        if not extract.get("ok"):
            print(f"  Extract FAILED: {extract}")
            results.append({"label": label, "status": "EXTRACT_FAIL", "error": str(extract)})
            continue
        print(f"  Extracted: {extract.get('file', '?')}")
    except Exception as e:
        print(f"  Extract error: {e}")
        results.append({"label": label, "status": "EXTRACT_ERROR", "error": str(e)})
        continue
    
    # 2. Run
    print(f"  [2/3] Running case '{case_name}'...")
    try:
        run_resp = requests.post(f"{BASE}/api/cases/{case_name}/run", json={
            "env_id": "sit2222"
        }, timeout=10).json()
        run_id = run_resp.get("run_id")
        if not run_id:
            print(f"  Run FAILED: {run_resp}")
            results.append({"label": label, "status": "RUN_FAIL", "error": str(run_resp)})
            continue
        print(f"  Run started: run_id={run_id}")
    except Exception as e:
        print(f"  Run error: {e}")
        results.append({"label": label, "status": "RUN_ERROR", "error": str(e)})
        continue
    
    # 3. Monitor SSE
    print(f"  [3/3] Monitoring execution...")
    try:
        import sseclient
        sse_url = f"{BASE}/api/runs/{run_id}/events"
        response = requests.get(sse_url, stream=True, timeout=300)
        client = sseclient.SSEClient(response)
        
        step_count = 0
        passed = None
        fail_detail = ""
        total_steps = "?"
        duration = "?"
        
        for event in client.events():
            if event.event == "step_done":
                data = json.loads(event.data)
                ok = data.get("ok", False)
                sid = data.get("id", "?")
                step_count += 1
                if not ok:
                    fail_detail = f"[{sid}] {data.get('error', '?')[:200]}"
                    print(f"    Step {step_count}: {sid} -> FAIL: {data.get('error', '?')[:80]}")
            elif event.event == "case_done":
                data = json.loads(event.data)
                passed = data.get("passed", False)
                total_steps = data.get("total_steps", "?")
                duration = data.get("duration_s", "?")
                break
            elif event.event == "case_error":
                data = json.loads(event.data)
                fail_detail = data.get("error", "?")
                passed = False
                break
            elif event.event == "close":
                break
        
        status = "PASS" if passed else "FAIL"
        result = {
            "label": label,
            "status": status,
            "steps": f"{step_count}/{total_steps}",
            "duration": f"{duration}s",
            "error": fail_detail if not passed else "",
        }
        results.append(result)
        print(f"  Result: {status} ({step_count}/{total_steps} steps, {duration}s)")
        if not passed and fail_detail:
            print(f"  Error: {fail_detail[:200]}")
    except Exception as e:
        print(f"  Monitor error: {e}")
        results.append({"label": label, "status": "MONITOR_ERROR", "error": str(e)})

# Summary
print(f"\n{'='*60}")
print(f"  VERIFICATION REPORT")
print(f"{'='*60}")
pass_count = sum(1 for r in results if r.get("status") == "PASS")
total = len(results)
print(f"  Overall: {pass_count}/{total} PASSED\n")
for r in results:
    icon = "✓" if r["status"] == "PASS" else "✗"
    line = f"  {icon} {r['label']}: {r['status']}"
    if r.get("steps"):
        line += f" ({r['steps']} steps, {r.get('duration', '?')})"
    if r.get("error"):
        line += f"\n    Error: {r['error'][:200]}"
    print(line)
print()
