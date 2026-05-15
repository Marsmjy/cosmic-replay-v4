"""检查最近的运行历史"""
import requests
import json

BASE = "http://127.0.0.1:8768"

# 查看最近的runs
r = requests.get(f"{BASE}/api/run_history", params={"limit": 20})
runs = r.json()
print(f"Recent {len(runs)} runs:")
for x in runs[:20]:
    print(f"  {x.get('run_id','?')}: {x.get('case_name','?')} passed={x.get('passed','?')} duration={x.get('duration_s','?')}s")
