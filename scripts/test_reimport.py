"""端到端测试：重新导入0512 HAR并验证执行"""
import requests, json, time

BASE = "http://127.0.0.1:8768"
HAR_FILE = r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads\preview_1778748678_新增入职0512测试.har"

print("="*60)
print("Step 1: 上传HAR并预览")
print("="*60)

with open(HAR_FILE, "rb") as f:
    content = f.read()

r = requests.post(
    f"{BASE}/api/har/preview?filename=新增入职0512测试_retest.har",
    data=content,
    headers={"Content-Type": "application/octet-stream"}
)
print(f"  Status: {r.status_code}")
preview_data = r.json()
print(f"  OK: {preview_data.get('ok')}")
har_file_name = preview_data.get("har_file", "")
print(f"  HAR file: {har_file_name}")

print("\n" + "="*60)
print("Step 2: 提取生成YAML")
print("="*60)

r = requests.post(f"{BASE}/api/har/extract", json={
    "har_file": har_file_name,
    "case_name": "新增入职0512测试_retest"
})
print(f"  Status: {r.status_code}")
extract_data = r.json()
print(f"  Result: {json.dumps(extract_data, ensure_ascii=False)}")

print("\n" + "="*60)
print("Step 3: 验证生成的YAML包含target_forms")
print("="*60)

import pathlib
yaml_path = pathlib.Path(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\cases\新增入职0512测试_retest.yaml")
if yaml_path.exists():
    content = yaml_path.read_text(encoding="utf-8")
    if "target_forms" in content:
        # Find the line
        for i, line in enumerate(content.split("\n")):
            if "target_forms" in line:
                print(f"  ✓ target_forms found at line {i+1}: {line.strip()}")
                break
    else:
        print("  ✗ target_forms NOT found in generated YAML!")
else:
    print(f"  ✗ YAML file not found: {yaml_path}")

print("\n" + "="*60)
print("Step 4: 执行用例")
print("="*60)

r = requests.post(f"{BASE}/api/cases/新增入职0512测试_retest/run", json={})
print(f"  Status: {r.status_code}")
run_data = r.json()
run_id = run_data.get("run_id", "")
print(f"  Run ID: {run_id}")

print("\n  等待执行完成（最多120秒）...")
for i in range(24):
    time.sleep(5)
    r = requests.get(f"{BASE}/api/run_history/{run_id}")
    events = r.json().get("events", [])
    done = [e for e in events if e["type"] == "case_done"]
    if done:
        d = done[0]["data"]
        print(f"\n{'='*60}")
        print("Step 5: 执行结果")
        print("="*60)
        passed = d.get("passed", False)
        print(f"  结果: {'✓ PASSED' if passed else '✗ FAILED'}")
        print(f"  步骤: {d.get('step_ok')}/{d.get('step_count')} (fail={d.get('step_fail')})")
        print(f"  断言: ok={d.get('assertion_ok')} fail={d.get('assertion_fail')}")
        print(f"  耗时: {d.get('duration_s', 0):.1f}s")
        
        if not passed:
            fails = [e for e in events if e["type"] == "step_fail"]
            for f in fails[:3]:
                fd = f.get("data", {})
                print(f"  ❌ {fd.get('id', '?')}: {json.dumps(fd.get('errors', []), ensure_ascii=False)[:200]}")
        break
else:
    print("  ⚠ 超时！用例仍在执行中")

print("\n" + "="*60)
