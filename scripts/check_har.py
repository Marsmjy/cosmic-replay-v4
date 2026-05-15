import json

har_path = r'c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads\preview_1778748678_新增入职0512测试.har'
with open(har_path, 'r', encoding='utf-8') as f:
    har = json.load(f)

entries = har['log']['entries']
for i in range(55, 68):
    e = entries[i]
    url = e['request']['url']
    qs = url.split('?')[1][:120] if '?' in url else ''
    body = e['request'].get('postData', {}).get('text', '')
    print(f"=== Entry {i}: {qs}")
    if body:
        try:
            bd = json.loads(body)
            # Show first action's key and args
            if isinstance(bd, list) and len(bd) > 0:
                for item in bd[:2]:
                    if isinstance(item, dict):
                        print(f"  key={item.get('key','?')}, method={item.get('methodName','?')}, args={json.dumps(item.get('args',''),ensure_ascii=False)[:200]}")
                    elif isinstance(item, list):
                        for sub in item[:2]:
                            if isinstance(sub, dict):
                                print(f"  key={sub.get('key','?')}, method={sub.get('methodName','?')}, args={json.dumps(sub.get('args',''),ensure_ascii=False)[:200]}")
        except:
            print(f"  body={body[:200]}")
    print()
