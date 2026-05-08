"""skill v1.0.3 - 20 测试 case 验收
来源：SKILL_FEEDBACK_REPORT 第八节 18 case + v1.0.3 实战补充 P19/P20

每条 case 检查：
1. 命中意图（含 inherits=MODEL_TOOL_FIRST 标记）
2. 命中反模式（应该走模型工具的不能误推荐 ISV 编码）
3. 候选场景定位准确

通过标准：≥18/20 (≥90%)
"""
import json
import re
import sys
from pathlib import Path

if sys.platform == "win32":
    try:
        sys.stdout.reconfigure(encoding="utf-8")
    except Exception:
        pass

idx = json.load(open("knowledge/_index.json", encoding="utf-8"))
ir = json.load(open("knowledge/_intent_routing.json", encoding="utf-8"))
ap_d = json.load(open("knowledge/_antipatterns.json", encoding="utf-8"))

intents = ir["intents"]
aps = ap_d["antipatterns"]

# 18 测试 case
TESTS_18 = [
    # 8.1 core_hr 云
    {"id": "P1", "input": "我要给员工档案加附表，应该看哪个场景？",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_employee", "expect_solution_type": "model_tool_config"},
    {"id": "P2", "input": "我要添加一个健康信息的人员档案附表",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_employee", "expect_solution_type": "model_tool_config"},
    {"id": "P3", "input": "我要在 hrpi_employee 主表加一个证书 entry 分录",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-021", "expect_scene": None,
     "expect_solution_type": "model_tool_config"},
    {"id": "P4", "input": "我要给员工档案加紧急联系人字段",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_employee", "expect_solution_type": "model_tool_config"},
    {"id": "P5", "input": "我要给任职信息加培训记录附表",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_assignment", "expect_solution_type": "model_tool_config"},
    {"id": "P6", "input": "我要给候选人加教育经历附表",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_employee_resume", "expect_solution_type": "model_tool_config"},
    {"id": "P7", "input": "我要给入职办理单加自定义入职须知字段",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_apply_emp", "expect_solution_type": "model_tool_config"},
    {"id": "P8", "input": "我要在调动单上加一个原因明细分录",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_transfer", "expect_solution_type": "model_tool_config"},
    {"id": "P9", "input": "我要给离职单加竞业限制条款分录",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_quit", "expect_solution_type": "model_tool_config"},
    # 8.2 org_dev 云
    {"id": "P10", "input": "我要给行政组织加一个注册地址字段",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "admin_org_quick_maintenance", "expect_solution_type": "model_tool_config"},
    {"id": "P11", "input": "我要给岗位加一个岗位说明书附件字段",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "hbpm_position_maintenance", "expect_solution_type": "model_tool_config"},
    {"id": "P12", "input": "我要给行政组织调整申请单加一个审批意见分录",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "homs_orgbatchchgbill_maintenance", "expect_solution_type": "model_tool_config"},
    {"id": "P13", "input": "我要给职位加一个职位序列字段",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "hbjm_joblevelhr", "expect_solution_type": "model_tool_config"},
    # 8.3 hlcm 合同云
    {"id": "P14", "input": "我要给合同档案加违约金字段并在合同申请单上联动",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_contract", "expect_solution_type": "model_tool_config"},
    # 8.4 反向（验证不误推荐模型工具）
    {"id": "P15", "input": "调动审批通过后要触发薪酬云重算上月差额",
     "expect_intent_inherits_mtf": False, "expect_ap": False,
     "expect_scene": None, "expect_solution_type": None},
    {"id": "P16", "input": "我要在员工档案上集成钉钉单点登录",
     "expect_intent_inherits_mtf": False, "expect_ap": False,
     "expect_scene": None, "expect_solution_type": None},
    # 8.5 模糊边界
    {"id": "P17", "input": "我要在员工档案查询时按部门权限隔离",
     "expect_intent_inherits_mtf": False, "expect_ap": False,
     "expect_scene": None, "expect_solution_type": None},
    {"id": "P18", "input": "我要给入职单加一个年龄自动计算字段（基于身份证号）",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-022",
     "expect_scene": "core_hr_apply_emp", "expect_solution_type": "model_tool_config"},
    # v1.0.3 补·实战会话发现的两个真实问题
    # 问题 1·新建人事单据
    {"id": "P19", "input": "我要做一个退休单",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-023",
     "expect_scene": None, "expect_solution_type": "model_tool_config"},
    {"id": "P20", "input": "我要给组织调整单加批量导入功能",
     "expect_intent_inherits_mtf": True, "expect_ap": True,
     "expect_ap_id": "AP-024",
     "expect_scene": "hies_entity_import", "expect_solution_type": "model_tool_config"},
]


def find_intent(text):
    """返回 (matched_names, has_mtf_inherits, primary_solution_type)"""
    matched = []
    has_mtf = False
    sol_type = None
    for name, data in intents.items():
        hit = False
        if name in text:
            hit = True
        else:
            for syn in data.get("synonyms", []):
                if syn in text:
                    hit = True
                    break
        if hit:
            matched.append(name)
            if data.get("inherits") == "MODEL_TOOL_FIRST":
                has_mtf = True
            if not sol_type and data.get("primary_solution_type"):
                sol_type = data["primary_solution_type"]
    return matched, has_mtf, sol_type


def find_ap(text):
    triggered = []
    for ap in aps:
        for k in ap.get("trigger_keywords", []):
            if isinstance(k, str) and k in text:
                triggered.append(ap["id"])
                break
    return triggered


def find_scene(text, expected):
    if not expected:
        return True
    bk = idx["byKeyword"]
    for kw, scenes in bk.items():
        if kw in text:
            for s in (scenes if isinstance(scenes, list) else []):
                if expected in s:
                    return True
    return False


passed = 0
fail_cases = []
print("=" * 80)
print("v1.0.3 - 20 case validation (SKILL_FEEDBACK_REPORT section 8 + v1.0.3 retire/HIES)")
print("=" * 80)

for t in TESTS_18:
    matched, has_mtf, sol_type = find_intent(t["input"])
    aps_hit = find_ap(t["input"])
    scene_ok = find_scene(t["input"], t["expect_scene"])

    # 检查
    intent_ok = bool(matched)
    mtf_ok = (has_mtf == t["expect_intent_inherits_mtf"])
    ap_ok = bool(aps_hit) == t["expect_ap"]
    if t.get("expect_ap_id"):
        ap_id_ok = t["expect_ap_id"] in aps_hit
    else:
        ap_id_ok = True
    sol_ok = True
    if t.get("expect_solution_type"):
        sol_ok = (sol_type == t["expect_solution_type"])

    case_pass = intent_ok and mtf_ok and ap_ok and ap_id_ok and scene_ok and sol_ok

    if case_pass:
        passed += 1
        flag = "PASS"
    else:
        fail_cases.append(t["id"])
        flag = "FAIL"

    print(f"\n[{flag}] {t['id']}: {t['input'][:50]}")
    print(f"    intents: {matched[:2]} | mtf={has_mtf}(expect {t['expect_intent_inherits_mtf']}) | sol={sol_type}(expect {t.get('expect_solution_type')})")
    print(f"    AP: {aps_hit[:2]} (expect_hit={t['expect_ap']})")
    if t["expect_scene"]:
        print(f"    scene [{t['expect_scene']}]: {'hit' if scene_ok else 'MISS'}")
    if not case_pass:
        details = []
        if not intent_ok: details.append("intent miss")
        if not mtf_ok: details.append(f"mtf {has_mtf}!={t['expect_intent_inherits_mtf']}")
        if not ap_ok: details.append(f"ap {bool(aps_hit)}!={t['expect_ap']}")
        if not ap_id_ok: details.append(f"missing AP-id {t.get('expect_ap_id')}")
        if not scene_ok: details.append(f"scene miss")
        if not sol_ok: details.append(f"sol {sol_type}!={t.get('expect_solution_type')}")
        print(f"    REASONS: {details}")

total = len(TESTS_18)
print("\n" + "=" * 80)
print(f"Total: {passed}/{total} passed (target: >={int(total * 0.9)})")
print("=" * 80)
if fail_cases:
    print(f"Failed cases: {fail_cases}")
print(f"Status: {'PASS' if passed >= int(total * 0.9) else 'NEED FIX'}")
