"""运行剩余2个用例：业务模型、新增入职0512"""
import requests, time, os, sys

BASE = "http://localhost:8768"
ENV = "sit2222"
har_dir = r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads"

# 找0512 HAR
har_0512 = None
for f in sorted(os.listdir(har_dir)):
    if "0512" in f and f.endswith(".har"):
        har_0512 = f
        break

CASES = [
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]
if har_0512:
    CASES.append((har_0512, "新增入职0512测试"))

results = []

for har_file, case_name in CASES:
    print(f"\n{'='*60}")
    print(f"处理: {case_name}")
    print(f"{'='*60}")

    har_path = os.path.join(har_dir, har_file)
    if not os.path.exists(har_path):
        print(f"  [SKIP] HAR文件不存在: {har_file}")
        results.append((case_name, "SKIP", "HAR文件不存在"))
        continue

    # 上传预览
    with open(har_path, "rb") as f:
        resp = requests.post(f"{BASE}/api/har/preview", files={"file": (har_file, f)})
    if resp.status_code != 200:
        print(f"  [FAIL] 上传失败: {resp.status_code}")
        results.append((case_name, "FAIL", f"上传失败: {resp.status_code}"))
        continue

    preview = resp.json()
    saved_har = preview.get("har_file", "")
    print(f"  HAR文件: {saved_har}")

    # 提取YAML
    resp = requests.post(f"{BASE}/api/har/extract", json={"har_file": saved_har, "case_name": case_name})
    if resp.status_code != 200:
        print(f"  [FAIL] 提取失败: {resp.status_code} - {resp.text[:200]}")
        results.append((case_name, "FAIL", f"提取失败: {resp.status_code}"))
        continue
    print(f"  提取完成: {resp.json().get('action', '?')}")

    # 检查0512的target_forms
    if "0512" in case_name:
        import yaml
        yaml_path = os.path.join(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\cases", f"{case_name}.yaml")
        if os.path.exists(yaml_path):
            with open(yaml_path, "r", encoding="utf-8") as yf:
                case_data = yaml.safe_load(yf)
            steps = case_data.get("steps", [])
            has_tf = any(s.get("target_forms") for s in steps)
            print(f"  target_forms: {'OK' if has_tf else 'MISSING'}")

    # 执行用例
    run_resp = requests.post(f"{BASE}/api/cases/{case_name}/run", json={"env_id": ENV})
    if run_resp.status_code != 200:
        print(f"  [FAIL] 执行失败: {run_resp.status_code} - {run_resp.text[:200]}")
        results.append((case_name, "FAIL", f"执行失败: {run_resp.status_code}"))
        continue

    run_id = run_resp.json().get("run_id")
    print(f"  执行ID: {run_id}")

    # 等待完成
    for i in range(60):  # 最多3分钟
        time.sleep(3)
        try:
            runs = requests.get(f"{BASE}/api/runs").json()
            run_info = next((r for r in runs if r.get("run_id") == run_id), None)
            if run_info and run_info.get("finished"):
                break
            if not run_info:
                break
        except:
            pass
    else:
        print(f"  [TIMEOUT]")
        results.append((case_name, "TIMEOUT", "180s"))
        continue

    time.sleep(1)
    hist = requests.get(f"{BASE}/api/run_history/{run_id}").json()
    events = hist.get("events", [])
    case_done = next((e.get("data", {}) for e in events if e.get("type") == "case_done"), None)
    case_error = next((e.get("data", {}) for e in events if e.get("type") == "case_error"), None)

    if case_done:
        ok = case_done.get("step_ok", 0)
        fail = case_done.get("step_fail", 0)
        dur = case_done.get("duration_s", "?")
        passed = case_done.get("passed", False)
        total = ok + fail
        if passed:
            print(f"  [PASSED] {ok}/{total} steps, {dur}s")
            results.append((case_name, "PASSED", f"{ok}/{total}, {dur}s"))
        else:
            print(f"  [FAILED] {ok}/{total} steps, {fail} failed, {dur}s")
            results.append((case_name, "FAILED", f"{fail} failed"))
    elif case_error:
        print(f"  [ERROR] {case_error.get('error', '')[:200]}")
        results.append((case_name, "ERROR", case_error.get('error', '')[:100]))
    else:
        print(f"  [UNKNOWN]")
        results.append((case_name, "UNKNOWN", "no case_done"))

print(f"\n\n{'='*60}")
print("结果汇总 (剩余用例)")
print(f"{'='*60}")
for name, status, detail in results:
    icon = "OK" if status == "PASSED" else "XX"
    print(f"  [{icon}] {name}: {status} ({detail})")

passed_count = sum(1 for _, s, _ in results if s == "PASSED")
print(f"\n通过: {passed_count}/{len(results)}")
