"""修复后的端到端验证：重新导入5种HAR并执行"""
import requests, time, json, sys, os

BASE = "http://localhost:8768"
ENV = "sit2222"
HAR_DIR = r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads"

# 精确指定HAR文件（使用已知正确的文件）
HARS = [
    ("preview_1778726790_HR基础服务云新增一条用工关系基础资料.har", "HR基础服务云新增一条用工关系基础资料"),
    ("preview_1778726861_入职申请到确认入职.har", "入职申请到确认入职"),
    ("preview_1778726914_新增一条行政组织.har", "新增一条行政组织"),
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]

# 查找0512 HAR - 优先使用原始的（非嵌套preview的）
har_files = os.listdir(HAR_DIR)
# 优先选 "preview_1778728851_新增入职0512测试.har" 类型（原始的）
candidates_0512 = [f for f in har_files if "0512" in f and f.endswith(".har")]
if candidates_0512:
    # 选最短文件名的（避免嵌套preview文件名）
    candidates_0512.sort(key=len)
    print(f"0512 HAR 候选: {candidates_0512[:5]}")
    har_0512 = candidates_0512[0]
    HARS.append((har_0512, "新增入职0512测试"))
    print(f"选择: {har_0512}")

results = []

for har_file, case_name in HARS:
    print(f"\n{'='*60}")
    print(f"处理: {case_name}")
    print(f"{'='*60}")
    
    # 上传HAR预览
    har_path = os.path.join(HAR_DIR, har_file)
    if not os.path.exists(har_path):
        print(f"  [SKIP] HAR文件不存在: {har_file}")
        results.append((case_name, "SKIP", "HAR文件不存在", 0, 0))
        continue
    
    print(f"  HAR文件: {har_file} ({os.path.getsize(har_path)//1024}KB)")
    
    with open(har_path, "rb") as f:
        resp = requests.post(
            f"{BASE}/api/har/preview?filename={har_file}&env_id={ENV}",
            data=f.read(),
            headers={"Content-Type": "application/octet-stream"},
        )
    
    if resp.status_code != 200:
        print(f"  [FAIL] 上传失败: {resp.status_code} - {resp.text[:200]}")
        results.append((case_name, "FAIL", f"上传失败", 0, 0))
        continue
    
    preview_data = resp.json()
    if not preview_data.get("ok"):
        print(f"  [FAIL] 预览失败: {preview_data.get('error', '')}")
        results.append((case_name, "FAIL", f"预览失败", 0, 0))
        continue
    
    server_har_file = preview_data["har_file"]
    preview_obj = preview_data.get("preview", {})
    step_count_preview = preview_obj.get("step_count", len(preview_obj.get("steps", [])))
    print(f"  服务端HAR: {server_har_file}, 预览步骤数: {step_count_preview}")
    
    # 提取生成YAML
    extract_payload = {"har_file": server_har_file, "case_name": case_name}
    resp = requests.post(f"{BASE}/api/har/extract", json=extract_payload)
    if resp.status_code != 200:
        print(f"  [FAIL] 提取失败: {resp.status_code} - {resp.text[:200]}")
        results.append((case_name, "FAIL", f"提取失败", 0, 0))
        continue
    
    extract_data = resp.json()
    step_count_extract = extract_data.get("step_count", 0)
    print(f"  提取完成: {step_count_extract} 步骤")
    
    # 执行用例
    run_resp = requests.post(f"{BASE}/api/cases/{case_name}/run", json={"env": ENV})
    if run_resp.status_code != 200:
        print(f"  [FAIL] 执行启动失败: {run_resp.status_code} - {run_resp.text[:200]}")
        results.append((case_name, "FAIL", f"执行启动失败", step_count_extract, 0))
        continue
    
    run_data = run_resp.json()
    run_id = run_data.get("run_id")
    print(f"  执行ID: {run_id}")
    
    # 等待执行完成 - 通过SSE事件流解析结果
    max_wait = 180
    elapsed = 0
    final_events = None
    while elapsed < max_wait:
        time.sleep(5)
        elapsed += 5
        try:
            hist_resp = requests.get(f"{BASE}/api/run_history/{run_id}")
            if hist_resp.status_code == 200:
                hist = hist_resp.json()
                events = hist.get("events", [])
                # 检查是否有终止事件 (case_done)
                for ev in events:
                    if ev.get("type") == "case_done":
                        final_events = events
                        break
                if final_events:
                    break
        except:
            pass
    
    if not final_events:
        print(f"  [TIMEOUT] 执行超时 ({max_wait}s)")
        results.append((case_name, "TIMEOUT", f"超时", step_count_extract, 0))
        continue
    
    # 从事件流中提取结果
    step_ok_count = sum(1 for e in final_events if e.get("type") == "step_ok")
    step_fail_count = sum(1 for e in final_events if e.get("type") == "step_fail")
    total_steps = step_ok_count + step_fail_count
    
    # 查找case_done事件获取最终状态
    case_done_ev = next((e for e in final_events if e.get("type") == "case_done"), {})
    case_done_data = case_done_ev.get("data", {})
    status = case_done_data.get("status", "unknown")
    
    # 检查assertion结果
    assertion_fail = next((e for e in final_events if e.get("type") == "assertion_fail"), None)
    assertion_ok = next((e for e in final_events if e.get("type") == "assertion_ok"), None)
    
    if status == "passed" or (assertion_ok and not assertion_fail and step_fail_count == 0):
        print(f"  [PASSED] {step_ok_count}/{total_steps} 步骤通过")
        results.append((case_name, "PASSED", f"{step_ok_count}/{total_steps}", step_count_extract, total_steps))
    else:
        # 获取失败详情
        error_msg = ""
        fail_ev = next((e for e in final_events if e.get("type") == "step_fail"), None)
        if fail_ev:
            fd = fail_ev.get("data", {})
            error_msg = f"{fd.get('id','?')}: {str(fd.get('error',''))[:100]}"
        if not error_msg and assertion_fail:
            afd = assertion_fail.get("data", {})
            error_msg = f"assertion: {str(afd)[:100]}"
        if not error_msg:
            error_msg = status
        print(f"  [FAILED] {step_ok_count}/{total_steps}, 错误: {error_msg}")
        results.append((case_name, "FAILED", error_msg, step_count_extract, total_steps))

# 最终汇总
print(f"\n\n{'='*60}")
print("最终结果汇总")
print(f"{'='*60}")
print(f"{'用例':<30} {'状态':<8} {'提取步骤':<10} {'执行步骤':<10} {'详情'}")
print("-" * 90)
total_cases = len(results)
passed_cases = sum(1 for _, s, _, _, _ in results if s == "PASSED")
for name, status, detail, extract_steps, exec_steps in results:
    icon = "✓" if status == "PASSED" else "✗"
    print(f"  {icon} {name:<28} {status:<8} {extract_steps:<10} {exec_steps:<10} {detail[:50]}")

print(f"\n总计: {passed_cases}/{total_cases} 通过")
if passed_cases == total_cases:
    print("全部通过！")
    sys.exit(0)
else:
    print(f"存在 {total_cases - passed_cases} 个失败用例")
    sys.exit(1)
