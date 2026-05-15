import json, sys, pathlib

RUNS_DIR = pathlib.Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\logs\runs")

run_id = sys.argv[1] if len(sys.argv) > 1 else "6aff65540505"
run_file = RUNS_DIR / f"{run_id}.jsonl"
if not run_file.exists():
    print(f"Run file not found: {run_file}")
    sys.exit(1)

events = [json.loads(line) for line in run_file.read_text(encoding="utf-8").splitlines() if line.strip()]
print(f"Run {run_id}: {len(events)} events total")
for ev in events:
    evt = ev.get("event", "?")
    data = ev.get("data", ev)
    if evt == "step_done":
        ok = data.get("ok", True)
        sid = data.get("id", "?")
        err = data.get("error", "")
        if ok:
            print(f"  ok   [{sid}]")
        else:
            print(f"  FAIL [{sid}]: {err[:200]}")
    elif evt == "case_done":
        passed = data.get("passed", False)
        total = data.get("total_steps", "?")
        dur = data.get("duration_s", "?")
        print(f"  RESULT: {'PASS' if passed else 'FAIL'} ({total} steps, {dur}s)")
    elif evt == "case_error":
        print(f"  CASE_ERROR: {data.get('error', '?')[:200]}")
