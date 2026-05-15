import requests, json

r = requests.get('http://127.0.0.1:8768/api/run_history/993c537996c9')
d = r.json()
events = d.get('events', [])

# Find step_start for click_barstart to see resolved_request (shows pageId used)
for e in events:
    if e['type'] == 'step_start' and e.get('data', {}).get('id') == 'click_barstart':
        print("=== click_barstart step_start ===")
        print(json.dumps(e, ensure_ascii=False, indent=2))
        break

# Also find step_start for load_persononbrdhandlebody 
for e in events:
    if e['type'] == 'step_ok' and e.get('data', {}).get('id') == 'load_persononbrdhandlebody':
        print("\n=== load_persononbrdhandlebody step_ok ===")
        print(json.dumps(e, ensure_ascii=False, indent=2)[:1000])
        break

# Find page_ids harvested
for e in events:
    if e['type'] in ('page_harvest', 'page_id_map', 'pageids'):
        print(f"\n=== {e['type']} ===")
        print(json.dumps(e, ensure_ascii=False, indent=2)[:1000])
