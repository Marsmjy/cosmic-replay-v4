"""本地验证 _resolve_form_name 对若干 form_id 的返回值。"""
import sys
sys.path.insert(0, ".")
from lib.har_extractor import _resolve_form_name, _FORM_ID_LABELS

cases = [
    "hrbm_logicentity_display",
    "hrbm_logicentity",
    "hbss_laborreltype",
    "hbss_basedatalist",
    "hbss_appgridhome",
    "logicentity_display",
]
print(f"_FORM_ID_LABELS 条数: {len(_FORM_ID_LABELS)}")
print(f"含 hrbm_logicentity_display? {_FORM_ID_LABELS.get('hrbm_logicentity_display')!r}")
print()
for c in cases:
    print(f"  {c:40s} → {_resolve_form_name(c)!r}")
