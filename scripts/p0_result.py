import urllib.request, json
r = urllib.request.urlopen('http://127.0.0.1:8768/api/tasks/task_1778291698730_abc86a', timeout=10)
t = json.loads(r.read())
print('=== task keys ===', list(t.keys()))
print('status=', t.get('status'), ' duration=', t.get('duration_s'))
print('pass/fail/total=', t.get('passed_count'), '/', t.get('failed_count'), '/', t.get('total_count'))
print('\n=== results[0] keys ===')
results = t.get('results', [])
if results:
    print(list(results[0].keys()))
print('\n=== results dump ===')
print(json.dumps(results, ensure_ascii=False, indent=2))
