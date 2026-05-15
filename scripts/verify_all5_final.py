"""端到端验证：重新导入5种HAR并执行"""
import requests, time, json, sys, os

BASE = "http://localhost:8768"
ENV = "sit2222"

HARS = [
    ("preview_1778726790_HR基础服务云新增一条用工关系基础资料.har", "HR基础服务云新增一条用工关系基础资料"),
    ("preview_1778726861_入职申请到确认入职.har", "入职申请到确认入职"),
    ("preview_1778726914_新增一条行政组织.har", "新增一条行政组织"),
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]

# 检查是否有0512用例的HAR
har_dir = r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads"
har_0512 = None
for f in os.listdir(har_dir):
    if "0512" in f and f.endswith(".har") and not f.startswith("preview_1778"):
        har_0512 = f
        break

# 如果没有非preview的0512 HAR，找第一个0512相关的
if not har_0512:
    for f in sorted(os.listdir(har_dir)):
        if "0512" in f and f.endswith(".har"):
            har_0512 = f
            break

if har_0512:
    HARS.append((har_0512, "新增入职0512测试"))

results = []

for har_file, case_name in HARS:
    print(f"\n{'='*60}")
    print(f"处理: {case_name}")
    print(f"{'='*60}")

    # Step 1: 上传HAR预览
    har_path = os.path.join(har_dir, har_file)
    if not os.path.exists(har_path):
        print(f"  [SKIP] HAR文件不存在: {har_file}")
        results.append((case_name, "SKIP", "HAR文件不存在"))
        continue

    with open(har_path, "rb") as f:
        resp = requests.post(f"{BASE}/api/har/preview", files={"file": (har_file, f)})

    if resp.status_code != 200:
        print(f"  [FAIL] 上传失败: {resp.status_code}")
        results.append((case_name, "FAIL", f"上传失败: {resp.status_code}"))
        continue

    preview = resp.json()
    saved_har_file = preview.get("har_file", "")
    preview_data = preview.get("preview", {})
    step_count = preview_data.get("step_count", "?") if isinstance(preview_data, dict) else "?"
    print(f"  HAR文件: {saved_har_file}")
    print(f"  步骤数: {step_count}")

    # Step 2: 提取生成YAML（使用har_file + case_name）
    extract_payload = {
        "har_file": saved_har_file,
        "case_name": case_name,
    }
    resp = requests.post(f"{BASE}/api/har/extract", json=extract_payload)
    if resp.status_code != 200:
        print(f"  [FAIL] 提取失败: {resp.status_code} - {resp.text[:200]}")
        results.append((case_name, "FAIL", f"提取失败: {resp.status_code}"))
        continue

    extract_data = resp.json()
    print(f"  提取完成: {extract_data.get('action', '?')}")
    print(f"  输出文件: {extract_data.get('file', '?')}")

    # 检查是否有target_forms（特别关注0512）
    if "0512" in case_name:
        import yaml
        yaml_path = os.path.join(r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\cases", f"{case_name}.yaml")
        if os.path.exists(yaml_path):
            with open(yaml_path, "r", encoding="utf-8") as yf:
                case_data = yaml.safe_load(yf)
            steps = case_data.get("steps", [])
            has_target_forms = any(s.get("target_forms") for s in steps)
            print(f"  target_forms 自动检测: {'OK' if has_target_forms else 'MISSING'}")

    # Step 3: 执行用例
    run_resp = requests.post(f"{BASE}/api/cases/{case_name}/run", json={"env_id": ENV})
    if run_resp.status_code != 200:
        print(f"  [FAIL] 执行启动失败: {run_resp.status_code} - {run_resp.text[:200]}")
        results.append((case_name, "FAIL", f"执行启动失败: {run_resp.status_code}"))
        continue

    run_data = run_resp.json()
    run_id = run_data.get("run_id")
    print(f"  执行ID: {run_id}")

    # Step 4: 等待执行完成 - 先等run结束，再获取结果
    max_wait = 120
    elapsed = 0
    finished = False
    while elapsed < max_wait:
        time.sleep(3)
        elapsed += 3
        try:
            runs_resp = requests.get(f"{BASE}/api/runs")
            if runs_resp.status_code == 200:
                runs_list = runs_resp.json()
                run_info = next((r for r in runs_list if r.get("run_id") == run_id), None)
                if run_info and run_info.get("finished"):
                    finished = True
                    break
                if not run_info:
                    # run已从内存中清除，说明已完成
                    finished = True
                    break
        except:
            pass

    if not finished:
        print(f"  [FAIL] 执行超时 ({max_wait}s)")
        results.append((case_name, "TIMEOUT", f"超时 {max_wait}s"))
        continue

    # 获取执行结果
    time.sleep(1)  # 等待日志写入
    hist_resp = requests.get(f"{BASE}/api/run_history/{run_id}")
    if hist_resp.status_code != 200:
        print(f"  [FAIL] 无法获取执行结果")
        results.append((case_name, "FAIL", "无法获取执行结果"))
        continue

    hist_data = hist_resp.json()
    events = hist_data.get("events", [])

    # 从事件中解析最终状态
    case_done = None
    case_error = None
    step_events = []
    for evt in events:
        evt_type = evt.get("type", "")
        if evt_type == "case_done":
            case_done = evt.get("data", {})
        elif evt_type == "case_error":
            case_error = evt.get("data", {})
        elif evt_type in ("step_ok", "step_fail"):
            step_events.append(evt)

    if case_done:
        is_passed = case_done.get("passed", False)
        step_ok = case_done.get("step_ok", 0)
        step_fail = case_done.get("step_fail", 0)
        duration_s = case_done.get("duration_s", "?")
        total_steps = step_ok + step_fail
        if is_passed:
            print(f"  [PASSED] {step_ok}/{total_steps} 步骤通过, 耗时 {duration_s}s")
            results.append((case_name, "PASSED", f"{step_ok}/{total_steps}, {duration_s}s"))
        else:
            print(f"  [FAILED] {step_ok}/{total_steps} 步骤通过, {step_fail}失败, 耗时 {duration_s}s")
            # 找出失败步骤的错误信息
            fail_msgs = [e.get("data", {}).get("error", "")[:100] for e in step_events if e.get("type") == "step_fail"]
            err_detail = "; ".join(fail_msgs[:3]) if fail_msgs else ""
            results.append((case_name, "FAILED", f"{step_fail}步失败: {err_detail}"))
    elif case_error:
        error_msg = case_error.get("error", "未知错误")[:200]
        print(f"  [ERROR] {error_msg}")
        results.append((case_name, "ERROR", error_msg))
    else:
        print(f"  [FAIL] 无法解析执行结果")
        results.append((case_name, "FAIL", "无case_done事件"))

# 最终汇总
print(f"\n\n{'='*60}")
print("最终结果汇总")
print(f"{'='*60}")
total_cases = len(results)
passed_cases = sum(1 for _, s, _ in results if s == "PASSED")
for name, status, detail in results:
    icon = "OK" if status == "PASSED" else "XX"
    print(f"  [{icon}] {name}: {status} ({detail})")

print(f"\n总计: {passed_cases}/{total_cases} 通过")
if passed_cases == total_cases:
    print("全部通过！架构改进无回归。")
    sys.exit(0)
else:
    print("存在失败用例，需要排查。")
    sys.exit(1)
