"""获取失败用例的详细信息"""
import requests
import json

BASE = "http://127.0.0.1:8768"

# 本次执行的run_ids
runs = {
    "HR基础服务云新增一条用工关系基础资料": ("0d74958b7251", True),
    "业务模型添加一个基础资料附表": ("d1fc4ecb46a3", True),
    "入职申请到确认入职": ("0179fc16435a", False),
    "新增一条行政组织": ("c7d54ed622bb", False),
    "新增入职0512测试": ("ebf18668f108", False),
}

# 获取每个run的case_done详细数据
print("="*80)
print("本次执行结果详情")
print("="*80)

all_data = {}
for name, (run_id, passed) in runs.items():
    r = requests.get(f"{BASE}/api/run_history/{run_id}")
    if r.status_code != 200:
        print(f"\n{name}: 获取失败 (HTTP {r.status_code})")
        continue
    
    events = r.json().get("events", [])
    case_done = [e for e in events if e.get("type") == "case_done"]
    if case_done:
        data = case_done[0].get("data", {})
        all_data[name] = data
    else:
        all_data[name] = {"passed": passed, "error": "no case_done event"}

# 汇总表格
print(f"\n{'用例名称':<42} {'结果':<8} {'步骤(ok/total)':<16} {'耗时':<10} {'断言'}")
print("-"*100)
for name, data in all_data.items():
    passed_flag = "PASSED" if data.get("passed") else "FAILED"
    step_ok = data.get("step_ok", "?")
    step_count = data.get("step_count", "?")
    step_fail = data.get("step_fail", 0)
    duration = data.get("duration_s", "?")
    dur_str = f"{duration:.1f}s" if isinstance(duration, (int, float)) else str(duration)
    assert_ok = data.get("assertion_ok", 0)
    assert_fail = data.get("assertion_fail", 0)
    assert_str = f"ok={assert_ok} fail={assert_fail}"
    print(f"{name:<42} {passed_flag:<8} {step_ok}/{step_count} (fail={step_fail}){'':<3} {dur_str:<10} {assert_str}")

# 失败用例详情
print("\n\n" + "="*80)
print("失败用例详细错误")
print("="*80)

for name, (run_id, passed) in runs.items():
    if passed:
        continue
    
    print(f"\n{'─'*80}")
    print(f"用例: {name}")
    print(f"run_id: {run_id}")
    print(f"{'─'*80}")
    
    r = requests.get(f"{BASE}/api/run_history/{run_id}")
    if r.status_code != 200:
        print(f"  HTTP {r.status_code}")
        continue
    
    events = r.json().get("events", [])
    
    # 找step_fail事件
    step_fails = [e for e in events if e.get("type") == "step_fail"]
    if step_fails:
        for sf in step_fails[:3]:
            d = sf.get("data", {})
            print(f"  失败步骤: {d.get('step_name', '?')}")
            print(f"  错误信息: {json.dumps(d.get('errors', d.get('error', '?')), ensure_ascii=False)[:400]}")
            print()
    
    # 也找assertion相关
    assert_events = [e for e in events if "assert" in e.get("type", "").lower() and "fail" in e.get("type", "").lower()]
    if assert_events:
        for ae in assert_events[:2]:
            d = ae.get("data", {})
            print(f"  断言失败: {json.dumps(d, ensure_ascii=False)[:300]}")

print("\n\n总计: 2/5 通过, 3/5 失败")
