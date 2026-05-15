import requests, json

runs = [
    ("35e60b42286b", "HR基础服务云新增一条用工关系基础资料"),
    ("a5dc94f459f1", "业务模型添加一个基础资料附表"),
    ("5d9a2ac00f7b", "入职申请到确认入职"),
    ("27cddf67b6c1", "新增一条行政组织"),
    ("db74051fcd1f", "新增入职0512测试"),
]

print("=" * 80)
print(f"{'用例名称':<30} {'结果':<10} {'步骤':<15} {'断言':<15} {'耗时'}")
print("=" * 80)

for run_id, name in runs:
    r = requests.get(f'http://127.0.0.1:8768/api/run_history/{run_id}')
    data = r.json()
    events = data.get('events', [])
    
    done = [e for e in events if e['type'] == 'case_done']
    if done:
        d = done[0]['data']
        passed = d.get('passed', False)
        status = "PASSED" if passed else "FAILED"
        step_ok = d.get('step_ok', 0)
        step_count = d.get('step_count', 0)
        step_fail = d.get('step_fail', 0)
        assertion_ok = d.get('assertion_ok', 0)
        assertion_fail = d.get('assertion_fail', 0)
        duration = d.get('duration_s', 0)
        print(f"{name:<30} {status:<10} {step_ok}/{step_count} (fail:{step_fail}){'':<5} ok:{assertion_ok} fail:{assertion_fail}{'':<5} {duration:.1f}s")
        
        if not passed:
            # Find failed steps
            failed_steps = [e for e in events if e['type'] == 'step_fail']
            for fs in failed_steps:
                fd = fs.get('data', {})
                print(f"  ❌ 失败步骤: {fd.get('id', '?')} - {fd.get('error', '?')[:100]}")
    else:
        print(f"{name:<30} {'RUNNING':<10}")

print("=" * 80)
