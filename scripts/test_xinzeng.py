"""Test the '新增入职' case with GitHub runner.py: extract + run."""
import requests, time, json, sys

BASE = "http://127.0.0.1:8768"
HAR_FILE = "preview_1778585116_preview_1778575043_新增入职0512测试.har"
CASE_NAME = "final_新增入职"

# 1. Extract HAR -> YAML (re-generate to ensure consistency)
print("=== Step 1: Re-extract HAR -> YAML ===")
extract = requests.post(f"{BASE}/api/har/extract", json={
    "har_file": HAR_FILE,
    "case_name": CASE_NAME,
}).json()
print(f"Extract result: {json.dumps(extract, ensure_ascii=False)}")
if not extract.get("ok"):
    print("Extract failed!")
    sys.exit(1)

# 2. Run case
print(f"\n=== Step 2: Run case '{CASE_NAME}' ===")
run_resp = requests.post(f"{BASE}/api/cases/{CASE_NAME}/run", json={
    "env_id": "sit2222"
}).json()
run_id = run_resp.get("run_id")
print(f"Run started: run_id={run_id}")
if not run_id:
    print(f"Run failed: {run_resp}")
    sys.exit(1)

# 3. Monitor SSE events
print(f"\n=== Step 3: Monitor SSE ===")
import sseclient
sse_url = f"{BASE}/api/runs/{run_id}/events"
response = requests.get(sse_url, stream=True, timeout=120)
client = sseclient.SSEClient(response)

step_count = 0
passed = None
fail_detail = ""
for event in client.events():
    if event.event == "step_done":
        data = json.loads(event.data)
        ok = data.get("ok", False)
        sid = data.get("id", "?")
        step_count += 1
        status = "OK" if ok else "FAIL"
        if not ok:
            fail_detail = f"[{sid}] {data.get('error', '?')}"
        print(f"  Step {step_count}: {sid} -> {status}")
    elif event.event == "case_done":
        data = json.loads(event.data)
        passed = data.get("passed", False)
        total = data.get("total_steps", "?")
        dur = data.get("duration_s", "?")
        print(f"\n=== RESULT: {'PASS' if passed else 'FAIL'} ({step_count}/{total} steps, {dur}s) ===")
        if not passed:
            print(f"  Failure: {fail_detail}")
        break
    elif event.event == "case_error":
        data = json.loads(event.data)
        print(f"\n=== CASE ERROR: {data.get('error', '?')} ===")
        break
    elif event.event == "close":
        break

if passed is None:
    print(f"\n=== RESULT: UNKNOWN (stream ended without case_done, {step_count} steps seen) ===")
