import requests, json
BASE = "http://127.0.0.1:8768"
runs_to_check = [
    ("803994e8cfe1", "HR基础服务云"),
    ("c4b5bde8c557", "入职申请到确认入职"),
    ("7d1950ca40b8", "新增一条行政组织"),
    ("85c16b8e1b32", "业务模型添加一个基础资料附表"),
]
print("="*70)
for rid, name in runs_to_check:
    r = requests.get(f"{BASE}/api/run_history/{rid}")
    if r.status_code != 200:
        print(f"{name}: HTTP {r.status_code}")
        continue
    events = r.json().get("events", [])
    done = [e for e in events if e["type"] == "case_done"]
    if done:
        d = done[0]["data"]
        s = "PASSED" if d["passed"] else "FAILED"
        print(f"{name}: {s} ({d['step_ok']}/{d['step_count']}, {d['duration_s']:.1f}s)")
    else:
        print(f"{name}: NOT_DONE (events={len(events)})")
print("="*70)
