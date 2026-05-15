import requests, json

BASE = "http://localhost:8768"
r = requests.post(f"{BASE}/api/har/preview", files={"file": open("har_uploads/preview_1778726914_新增一条行政组织.har", "rb")})
hf = r.json()["har_file"]
print(f"har_file={hf}")

r2 = requests.post(f"{BASE}/api/har/extract", json={"har_file": hf, "case_name": "新增一条行政组织"})
print(f"status={r2.status_code}")
d = r2.json()
print(f"keys={list(d.keys())}")
print(f"step_count={d.get('step_count')}")
print(f"ok={d.get('ok')}")
print(f"case_file={d.get('case_file')}")
if d.get("detail"):
    print(f"detail={d['detail']}")
