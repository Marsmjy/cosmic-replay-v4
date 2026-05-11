"""快速检查知识库 formNumber 是否覆盖目标 form_id。"""
import json
from pathlib import Path

base = Path(__file__).resolve().parent.parent / "skills" / "cosmic-hr-expert" / "knowledge"

targets = [
    "hbss_laborreltype",
    "bos_devportal_bizmodel_detail",
    "bos_devportal_bizmodel_basedata",
    "logicentity",
    "logicentity_display",
    "hbss_basedatalist",
    "hbss_appgridhome",
]

all_nums: dict[str, str] = {}
for f in base.glob("_*_app_map.json"):
    d = json.loads(f.read_text(encoding="utf-8"))
    for app_id, info in (d.get("apps") or {}).items():
        app_name = (info or {}).get("name", "")
        for s in (info or {}).get("scenes", []) or []:
            n = (s or {}).get("formNumber", "")
            nm = (s or {}).get("name", "")
            if n and nm:
                all_nums[n] = f"{app_name}-{nm}" if app_name else nm

print(f"知识库 formNumber 总条数: {len(all_nums)}")
print()
for t in targets:
    exact = all_nums.get(t)
    if exact:
        print(f"  ✓ 精确命中 {t} → {exact}")
        continue
    substr = [(k, v) for k, v in all_nums.items() if t.lower() in k.lower()]
    if substr:
        print(f"  ~ 模糊命中 {t}:")
        for k, v in substr[:5]:
            print(f"       {k} → {v}")
    else:
        print(f"  ✗ 未命中 {t}")
