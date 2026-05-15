import yaml
from pathlib import Path

d = yaml.safe_load(Path(r'c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4\cases\final_新增入职.yaml').read_text(encoding='utf-8'))
for i, s in enumerate(d.get('steps', [])):
    sid = s.get('id', '?')
    stype = s.get('type', '?')
    form = s.get('form_id', '')
    desc = (s.get('description', '') or '')[:30]
    print(f"{i:3d} {sid:35s} {stype:15s} form={form:35s} {desc}")
