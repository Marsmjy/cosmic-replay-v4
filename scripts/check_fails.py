import requests, json
BASE = "http://localhost:8768"
# Get recent runs
r = requests.get(f"{BASE}/api/run_history?limit=5")
runs = r.json()
for run in runs:
    run_id = run.get("run_id")
    case = run.get("case_name", "?")
    status = run.get("status", "?")
    print(f"\n{case} [{status}] run_id={run_id}")
    if status != "passed":
        # Get details
        r2 = requests.get(f"{BASE}/api/run_history/{run_id}")
        events = r2.json().get("events", [])
        fails = [e for e in events if e.get("type") == "step_fail"]
        for f in fails[:2]:
            d = f.get("data", {})
            errs = d.get("errors", [])
            err_text = errs[0] if errs else d.get("error", "")
            print(f"  FAIL: {d.get('id','?')} -> {str(err_text)[:200]}")
