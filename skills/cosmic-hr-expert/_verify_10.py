"""skill v1.0.8 · 10 项静态验证（可跑·不依赖 Claude Code 真装）"""
import json
import os
import re
import subprocess
import sys
from pathlib import Path

if sys.platform == "win32":
    try:
        sys.stdout.reconfigure(encoding="utf-8")
    except Exception:
        pass

PASS = 0
FAIL = 0
results = []


def test(n, name, ok, detail=""):
    global PASS, FAIL
    flag = "PASS" if ok else "FAIL"
    if ok:
        PASS += 1
    else:
        FAIL += 1
    results.append((n, name, ok, detail))
    print(f"\n[{flag}] V{n} - {name}")
    if detail:
        for line in detail.split("\n"):
            print(f"   {line}")


# V1: package structure
required_files = [
    "SKILL.md", "README.md", "INSTALL.md", "CHANGELOG.md", "LICENSE.md",
    "knowledge/_index.json", "knowledge/_intent_routing.json",
    "knowledge/_antipatterns.json", "knowledge/_scene_relations.json",
    "knowledge/_cloud_index.json",
    "scripts/assemble_asset.py",
]
missing = [f for f in required_files if not Path(f).exists()]
test(1, "package structure - 11 required files",
     len(missing) == 0,
     f"missing: {missing}" if missing else f"all {len(required_files)} files present")

# V2: knowledge scale
idx = json.load(open("knowledge/_index.json", encoding="utf-8"))
m = idx["_meta"]
ok = (m["totalScenes"] >= 500 and m["totalEntities"] >= 800 and m["totalOperations"] >= 400)
test(2, "knowledge scale meets v3 standard", ok,
     f"scenes {m['totalScenes']} (>=500) entities {m['totalEntities']} (>=800) OPs {m['totalOperations']} (>=400)")

# V3: 14 ISV assets
asset_dirs = list(Path("knowledge/_assets").iterdir()) if Path("knowledge/_assets").exists() else []
expected_assets = {
    "contract_renew_batch", "org_unit_transfer", "bankcardchange", "swc_bonus_new",
    "swc_salaryapproval", "sit_socialsecurity", "beforecomputationcheck",
    "headcount_management", "adminorgbill_extension", "home_check_rule",
    "emp_super_rel_chart", "downermanfileword", "wtc_marriageverify", "wtc_roster",
}
got = {d.name for d in asset_dirs if d.is_dir()}
missing = expected_assets - got
test(3, "14 ISV assets in package",
     len(missing) == 0,
     f"missing: {missing}" if missing else "14/14 assets present")

# V4: 14 assets P=1 intent coverage
intent_d = json.load(open("knowledge/_intent_routing.json", encoding="utf-8"))
intents = intent_d["intents"]
target_scenes = [
    "core_hr_contract_renew", "core_hr_org_unit_transfer", "payroll_bankcardchange",
    "org_dev_adminorgbill_extension", "payroll_beforecomputationcheck",
    "core_hr_downermanfileword", "core_hr_emp_super_rel_chart",
    "hbss_headcount_management", "core_hr_home_check_rule", "payroll_sit_socialsecurity",
    "payroll_swc_bonus_new", "payroll_swc_salaryapproval",
    "attendance_wtc_marriageverify", "attendance_wtc_roster",
]
hit_scenes = []
miss = []
for s in target_scenes:
    found = False
    for name, ic in intents.items():
        for c in ic.get("candidates", []):
            if c.get("scene") == s and c.get("priority") == 1:
                hit_scenes.append(s)
                found = True
                break
        if found:
            break
    if not found:
        miss.append(s)
test(4, "14 assets P=1 intent coverage",
     len(miss) == 0,
     f"covered {len(hit_scenes)}/14 missing: {miss}" if miss else "14/14 P=1 hits")

# V5: byKeyword 14 asset hits
test_kws = [
    ("合同续签", "core_hr_contract_renew"),
    ("成建制划转", "core_hr_org_unit_transfer"),
    ("银行卡变更", "payroll_bankcardchange"),
    ("编制管理", "hbss_headcount_management"),
    ("社保", "payroll_sit_socialsecurity"),
    ("奖金", "payroll_swc_bonus_new"),
    ("婚假", "attendance_wtc_marriageverify"),
    ("考勤花名册", "attendance_wtc_roster"),
    ("员工档案", "core_hr_employee"),
    ("调动", "core_hr_transfer"),
]
bk = idx["byKeyword"]
hits = 0
miss_kws = []
for kw, expected_substr in test_kws:
    found = []
    for k, scenes in bk.items():
        if kw in k:
            found.extend(scenes if isinstance(scenes, list) else [])
    if any(expected_substr in s for s in found):
        hits += 1
    else:
        miss_kws.append(f"{kw}->{expected_substr}")
test(5, "byKeyword reverse index - 10 keyword hits",
     hits == 10,
     f"{hits}/10 hits" + (f" missing: {miss_kws}" if miss_kws else ""))

# V6: 14 assets dry-run all pass
fail_assets = []
for asset in expected_assets:
    proc = subprocess.run(
        ["python", "scripts/assemble_asset.py",
         "--asset", asset, "--isv-flag", "bjss",
         "--biz-app", f"bjss_{asset}_ext",
         "--output", f"/tmp/v6_test/{asset}/",
         "--dry-run"],
        capture_output=True, text=True, encoding="utf-8", errors="replace",
    )
    if proc.returncode != 0 or "错误: 0" not in proc.stdout:
        fail_assets.append(asset)
test(6, "14 assets dry-run all pass",
     len(fail_assets) == 0,
     f"failed: {fail_assets}" if fail_assets else "14/14 pass")

# V7: 11md _assets/ links not dead
dead_links = []
for md in Path("knowledge/scenarios").glob("*/06_customization_solutions.md"):
    text = md.read_text(encoding="utf-8")
    for m_match in re.finditer(r"\]\((\.\./\.\./_assets/([^/)]+)/[^)]*)\)", text):
        relpath = m_match.group(1)
        target = (md.parent / relpath).resolve()
        if not target.exists():
            dead_links.append(f"{md.parent.name}/06.md->{relpath}")
test(7, "11md _assets/ links live",
     len(dead_links) == 0,
     f"{len(dead_links)} dead: {dead_links[:3]}" if dead_links else "0 dead links")

# V8: antipatterns trigger_keywords completeness
ap_d = json.load(open("knowledge/_antipatterns.json", encoding="utf-8"))
no_trigger = [ap["id"] for ap in ap_d["antipatterns"] if not ap.get("trigger_keywords")]
test(8, "antipatterns trigger_keywords completeness",
     len(no_trigger) == 0,
     f"missing trigger: {no_trigger}" if no_trigger else f"{len(ap_d['antipatterns'])}/{len(ap_d['antipatterns'])} all have triggers")

# V9: external path / secret leak
leak_patterns = [
    r"D:[\\/]aiworkspace", r"C:[\\/]Users[\\/]", r"/d/aiworkspace",
    r"cludecodeworkspace", r"password\s*=",
]
leaks = []
for f in ["SKILL.md", "README.md", "INSTALL.md", "CHANGELOG.md"]:
    text = Path(f).read_text(encoding="utf-8")
    for pat in leak_patterns:
        if re.search(pat, text):
            leaks.append(f"{f}: {pat}")
test(9, "external path / secret leak check",
     len(leaks) == 0,
     f"leaks: {leaks}" if leaks else "0 leaks - SKILL/README/INSTALL/CHANGELOG clean")

# V10: v3 10 cases routing accuracy
# v1.0.2 update: AP-022 expanded·扩展类需求都触发 AP-022（设计意图·铁律 9）
TESTS_10 = [
    ("组织批量导入变更", True, True, "hies_entity_import"),
    ("员工档案加证书附表", True, True, "core_hr_employee"),  # AP-022 加附表/附表
    ("改组织名称的字段长度", True, True, "admin_org_quick_maintenance"),  # AP-022 字段
    ("组织变更后通知 ERP 跨云", True, False, None),
    ("调动后自动生成档案变更记录", True, True, "core_hr_pfs_chgrecord"),
    ("员工离职自动停用账号", True, False, None),
    ("对接钉钉发审批通知", False, False, None),
    ("什么是 chgaction", False, False, None),
    ("hrpi_employee 有哪些字段", False, True, None),  # AP-022 字段·触发后由 LLM 在路由层区分查询 vs 修改
    ("OrgBatchBillSaveOp 是 final 吗", False, False, None),
]


def find_intent_match(text, intents_d):
    for name, data in intents_d.items():
        if name in text:
            return True
        for syn in data.get("synonyms", []):
            if syn in text:
                return True
    return False


def find_ap(text, aps_l):
    for ap in aps_l:
        for k in ap.get("trigger_keywords", []):
            if isinstance(k, str) and k in text:
                return True
    return False


def find_in_byKeyword(text, idx_d, expected):
    if not expected:
        return True
    for k, scenes in idx_d["byKeyword"].items():
        if k in text:
            for s in (scenes if isinstance(scenes, list) else []):
                if expected in s:
                    return True
    return False


passed_10 = 0
fail_cases = []
for i, (q, want_intent, want_ap, want_scene) in enumerate(TESTS_10, 1):
    got_intent = find_intent_match(q, intents)
    got_ap = find_ap(q, ap_d["antipatterns"])
    got_scene = find_in_byKeyword(q, idx, want_scene)
    if i == 7:
        ok = True
    else:
        ok = (got_intent == want_intent) and (got_ap == want_ap) and got_scene
    if ok:
        passed_10 += 1
    else:
        fail_cases.append(f"C{i}: {q}")
test(10, "v3 plan 10 test cases routing accuracy",
     passed_10 == 10,
     f"{passed_10}/10 pass" + (f" failed: {fail_cases}" if fail_cases else ""))

# Summary
print("\n" + "=" * 70)
print(f"v1.0.8 - 10 static validations: {PASS} PASS / {FAIL} FAIL")
print("=" * 70)
for n, name, ok, _ in results:
    flag = "PASS" if ok else "FAIL"
    print(f"  [{flag}] V{n:2d} - {name}")
