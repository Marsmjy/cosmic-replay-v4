import json, urllib.parse

har_path = r'c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads\preview_1778748678_新增入职0512测试.har'
with open(har_path, 'r', encoding='utf-8') as f:
    har = json.load(f)

entries = har['log']['entries']
for i, e in enumerate(entries[:55]):
    url = e['request']['url']
    if 'persononbrdhandlebody' in url or 'onbrdinfo' in url:
        qs = url.split('?')[1] if '?' in url else ''
        # Extract pageId from body
        body = e['request'].get('postData', {}).get('text', '')
        page_id = ''
        if body:
            params = urllib.parse.parse_qs(body)
            page_id = params.get('pageId', [''])[0][:50]
        ac = ''
        if 'ac=' in qs:
            ac = qs.split('ac=')[1].split('&')[0]
        form = ''
        if 'f=' in qs:
            form = qs.split('f=')[1].split('&')[0]
        print(f"Entry {i}: form={form} ac={ac} pageId={page_id}")
