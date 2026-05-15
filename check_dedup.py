import sys
sys.path.insert(0, '.')
from pathlib import Path
from lib import har_extractor

har_file = Path('har_uploads/preview_1778745713_入职申请到确认入职.har')
if har_file.exists():
    preview = har_extractor.preview_har(har_file)
    
    print('===== Full Analysis =====')
    detected_names = set(v['name'] for v in preview.get('detected_vars', []))
    pick_ids = set(pf['id'] for pf in preview.get('pick_fields', []))
    
    print(f'detected_vars count: {len(detected_names)}')
    print(f'detected_vars: {sorted(detected_names)}')
    print(f'')
    print(f'pick_fields count: {len(pick_ids)}')
    print(f'pick_fields ids: {sorted(pick_ids)}')
    print(f'')
    
    _pick_base_keys = set()
    for pf_id in pick_ids:
        if pf_id.startswith('pick_'):
            _pick_base_keys.add(pf_id[5:])
        elif pf_id.startswith('date_'):
            _pick_base_keys.add(pf_id)
        _pick_base_keys.add(pf_id)
    
    print(f'_pick_base_keys (after dedup logic): {sorted(_pick_base_keys)}')
    print(f'')
    
    overlap = detected_names & _pick_base_keys
    if overlap:
        print(f'ERROR: Found overlaps: {overlap}')
    else:
        print(f'OK: No overlaps found')
