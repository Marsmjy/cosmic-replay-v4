"""Rule 14 修复后的端到端验证"""
import requests, time, os, sys

BASE = "http://localhost:8768"
ENV = "sit2222"
HAR_DIR = r"c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads"

HARS = [
    ("preview_1778726790_HR基础服务云新增一条用工关系基础资料.har", "HR基础服务云新增一条用工关系基础资料"),
    ("preview_1778726861_入职申请到确认入职.har", "入职申请到确认入职"),
    ("preview_1778726914_新增一条行政组织.har", "新增一条行政组织"),
    ("preview_1778726968_业务模型添加一个基础资料附表.har", "业务模型添加一个基础资料附表"),
]

# 找0512 HAR
candidates = [f for f in os.listdir(HAR_DIR) if "0512" in f and f.endswith(".har")]
if candidates:
    candidates.sort(key=len)
    HARS.append((candidates[0], "新增入职0512测试"))
    print(f"0512 HAR: {candidates[0]}")

def parse_run_result(run_id, timeout=180):
    """轮询 run_history 直到完成，解析 events 获取结果"""
    deadline = time.time() + timeout
    while time.time() < deadline:
        time.sleep(3)
        try:
            r = requests.get(f"{BASE}/api/run_history/{run_id}")
            if r.status_code == 404:
                continue
            if r.status_code != 200:
                continue
            data = r.json()
            events = data.get("events", [])
            # 查找 case_done 事件
            for ev in events:
                if ev.get("type") == "case_done":
                    d = ev.get("data", {})
                    passed = d.get("passed", False)
                    step_ok = d.get("step_ok", 0)
                    step_fail = d.get("step_fail", 0)
                    step_count = d.get("step_count", 0)
                    duration = d.get("duration_s", 0)
                    return {
                        "status": "passed" if passed else "failed",
                        "step_ok": step_ok,
                        "step_fail": step_fail,
                        "total": step_count,
                        "duration": duration,
                    }
                if ev.get("type") == "case_error":
                    d = ev.get("data", {})
                    return {
                        "status": "error",
                        "step_ok": 0,
                        "step_fail": 0,
                        "total": 0,
                        "error": d.get("error", "unknown"),
                    }
            # 查找 step_fail 事件获取失败信息
        except Exception as e:
            print(f"    poll error: {e}")
    return {"status": "TIMEOUT", "step_ok": 0, "step_fail": 0, "total": 0}

results = []
for har_file, case_name in HARS:
    print(f"\n{'='*50}\n{case_name}\n{'='*50}")
    har_path = os.path.join(HAR_DIR, har_file)
    if not os.path.exists(har_path):
        print(f"  SKIP: 文件不存在")
        results.append((case_name, "SKIP", 0, 0))
        continue
    
    # 上传预览
    with open(har_path, "rb") as f:
        r = requests.post(f"{BASE}/api/har/preview", files={"file": (har_file, f)})
    if r.status_code != 200:
        print(f"  上传失败: {r.status_code} {r.text[:100]}")
        results.append((case_name, "UPLOAD_FAIL", 0, 0))
        continue
    uploaded_har = r.json().get("har_file", har_file)
    
    # 提取生成YAML
    r2 = requests.post(f"{BASE}/api/har/extract", json={"har_file": uploaded_har, "case_name": case_name})
    if r2.status_code != 200:
        print(f"  提取失败: {r2.text[:200]}")
        results.append((case_name, "EXTRACT_FAIL", 0, 0))
        continue
    print(f"  提取成功: {r2.json().get('action', '?')}")
    
    # 执行用例
    r3 = requests.post(f"{BASE}/api/cases/{case_name}/run", json={"env_id": ENV})
    if r3.status_code != 200:
        print(f"  运行失败: {r3.status_code} {r3.text[:100]}")
        results.append((case_name, "RUN_FAIL", 0, 0))
        continue
    run_id = r3.json().get("run_id")
    print(f"  运行中: run_id={run_id}")
    
    # 等待完成
    result = parse_run_result(run_id)
    status = result["status"]
    step_ok = result["step_ok"]
    total = result["total"]
    print(f"  结果: {status} ({step_ok}/{total}, {result.get('duration',0):.1f}s)")
    
    if status != "passed":
        # 获取失败详情
        try:
            rh = requests.get(f"{BASE}/api/run_history/{run_id}")
            events = rh.json().get("events", [])
            fails = [e for e in events if e.get("type") == "step_fail"]
            for f_ev in fails[:3]:
                fd = f_ev.get("data", {})
                errs = fd.get("errors", [])
                err_msg = errs[0] if errs else fd.get("error", "")
                print(f"    失败步骤: {fd.get('id','?')} - {str(err_msg)[:120]}")
        except:
            pass
        if result.get("error"):
            print(f"    错误: {result['error'][:150]}")
    
    results.append((case_name, status, total, step_ok))

# 汇总
print(f"\n\n{'='*50}\n最终结果\n{'='*50}")
pass_count = 0
for name, status, total, step_ok in results:
    icon = "✓" if status == "passed" else "✗"
    print(f"  {icon} {name}: {status} ({step_ok}/{total})")
    if status == "passed":
        pass_count += 1
print(f"\n{pass_count}/{len(results)} 通过")
sys.exit(0 if pass_count == len(results) else 1)
