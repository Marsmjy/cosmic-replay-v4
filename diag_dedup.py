import sys
sys.path.insert(0, '.')
import json
from pathlib import Path
from lib import har_extractor

har_file = Path('har_uploads/preview_1778745713_入职申请到确认入职.har')
if har_file.exists():
    preview = har_extractor.preview_har(har_file)
    print('===== detected_vars sample =====')
    for var in preview.get('detected_vars', [])[:10]:
        print(f'  {var.get("name")}')
    print('===== pick_fields sample =====')
    for pf in preview.get('pick_fields', [])[:10]:
        print(f'  id={pf.get("id")} field_key={pf.get("field_key")}')
