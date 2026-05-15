"""诊断脚本：分析 HAR 中 setItemByIdFromClient 请求的参数和响应"""
import json, urllib.parse
from pathlib import Path

har_dir = Path(r'c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\har_uploads')
har_files = sorted(har_dir.glob('*行政组织*'), key=lambda p: p.stat().st_mtime, reverse=True)
if not har_files:
    print("没有找到行政组织 HAR 文件")
    exit(1)

har_path = har_files[0]
print(f"分析 HAR: {har_path.name}")
print(f"文件大小: {har_path.stat().st_size / 1024:.1f} KB")
print("=" * 80)

with open(har_path, encoding='utf-8') as f:
    har = json.load(f)

for i, entry in enumerate(har['log']['entries']):
    url = entry['request']['url']
    qs = urllib.parse.parse_qs(urllib.parse.urlparse(url).query)
    ac = qs.get('ac', [''])[0]
    f_id = qs.get('f', [''])[0]

    if 'setItemByIdFromClient' in ac or 'setItem' in url:
        # 提取请求 postData
        post_text = entry.get('request', {}).get('postData', {}).get('text', '')
        try:
            pd = urllib.parse.parse_qs(post_text)
        except:
            pd = {}

        # 提取 pageId
        req_page_id = pd.get('pageId', ['N/A'])[0] if isinstance(pd, dict) else 'N/A'

        # 提取 params（actions JSON）
        params_raw = pd.get('params', ['[]'])[0] if isinstance(pd, dict) else '[]'
        try:
            actions = json.loads(params_raw)
        except:
            actions = []

        args_val = 'N/A'
        field_key = 'N/A'
        method_name = 'N/A'
        for act in (actions if isinstance(actions, list) else []):
            if isinstance(act, dict):
                args_val = act.get('args', 'N/A')
                field_key = act.get('key', 'N/A')
                method_name = act.get('methodName', 'N/A')

        # 提取响应
        resp_text = entry.get('response', {}).get('content', {}).get('text', '')
        resp_len = len(resp_text) if resp_text else 0

        # 检查响应是否为空
        is_empty = resp_text.strip() in ('[]', '', '{}') if resp_text else True

        pid_short = str(req_page_id)[:40] + '...' if len(str(req_page_id)) > 40 else str(req_page_id)
        print(f"\nEntry {i}: ac={ac}, form={f_id}")
        print(f"  pageId: {pid_short} (len={len(str(req_page_id))})")
        print(f"  field_key: {field_key}")
        print(f"  methodName: {method_name}")
        print(f"  args (value_id): {args_val}")
        print(f"  response_len: {resp_len}, empty: {is_empty}")
        if not is_empty and resp_len < 2000:
            print(f"  response: {resp_text[:500]}")
        elif is_empty:
            print(f"  response: [EMPTY] '{resp_text[:100] if resp_text else ''}'")
