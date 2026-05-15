import requests, json
r = requests.get('http://127.0.0.1:8768/api/runs')
runs = r.json()
print(f"Total runs: {len(runs)}")
for x in runs[-6:]:
    print(f"  {x['case']}: finished={x['finished']}, run_id={x['run_id']}")
