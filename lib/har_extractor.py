"""HAR → YAML 用例起步稿（智能化版）

设计目标：产出的 YAML 质量足够好，用户只需几分钟轻度清理即可运行。

升级点（vs 原版）：
1. 业务语义命名：`fill_name` / `pick_adminorgtype` 而不是 `step_28_setItemByIdFromClient`
2. 自动抽 vars：测试编号/日期/时间戳类值自动变占位符
3. update_fields 合并：连续的 updateValue 合成一条 update_fields
4. pick_basedata 降级：setItemByIdFromClient 转成更清晰的 pick_basedata
5. open_form 去重：去掉 HAR 里多次打开同一表单
6. optional 分级：noise（几乎可删）/ ui_reaction（选留）/ core（必留）
7. 产出带注释：关键决策、清理建议都写在 YAML 注释里

用法：
    python -m lib.har_extractor extract input.har -o case.yaml
"""
from __future__ import annotations

import argparse
import json
import logging
import re
import sys
import urllib.parse
from collections import OrderedDict
from pathlib import Path
from typing import Any

log = logging.getLogger(__name__)

from lib.kb_loader import field_meta as _kb_field_meta
from lib.kb_loader import get_field_label as _kb_get_field_label


# ---------- 常量 ----------

STATIC_SUFFIX = (".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".svg",
                 ".ico", ".woff", ".woff2", ".ttf", ".eot", ".map")

# ac 分级（影响生成的 YAML 里是否标 optional、以及 optional 的类型）
AC_TIER = {
    # noise：纯 UI 装饰，完全可去
    "clientCallBack":          "noise",
    "queryExceedMaxCount":     "noise",
    "customEvent":             "noise",
    "changeYear":              "core",
    "clientPosInvokeMethod":   "noise",
    # ui_reaction：UI 联动类下拉联动 / 城市带出 / 树子节点查询
    "getCityInfo":             "ui_reaction",
    "getTelViaList":           "ui_reaction",
    "getCountrys":             "ui_reaction",
    "getProvincesByCountryId": "ui_reaction",
    "getLookUpList":           "ui_reaction",
    "queryTreeNodeChildren":   "ui_reaction",
    # core：业务必留（包括菜单点击、tab 切换等建立业务上下文的动作）
    "menuItemClick":           "core",   # ⚠ 进入应用的关键入口
    "appItemClick":            "core",   # ⚠ 门户点击应用，建立应用 session
    "treeMenuClick":           "core",   # ⚠ 左侧树菜单点击，注册 L2 pageId（规则6）
    "postExpandNodes":         "core",   # 树节点展开（与 treeMenuClick 配合）
    "getMenuData":             "core",   # 菜单数据
    "getFrequentData":         "core",   # 高频菜单
    "selectTab":               "core",   # tab 切换（可能触发数据加载）
    "loadData":                "core",
    "addnew":                  "core",
    "save":                    "core",
    "saveandeffect":           "core",   # ⚠ 保存并生效，必须保留
    "submit":                  "core",
    "submitandeffect":         "core",   # 提交并生效
    "audit":                   "core",   # 审核
    "unaudit":                 "core",   # 反审核
    "delete":                  "core",
    "modify":                  "core",
    "close":                   "core",
    "updateValue":             "core",
    "setItemByIdFromClient":   "core",
    "setItemValueByIdFromClient": "core",
    "itemClick":               "core",
}

# ⭐ 规则6补充：toolbar 上的 itemClick 按钮一般都是业务操作，不应标 optional
# 即使 ac 不在 AC_TIER 中，只要是 toolbarap/tbmain 上的 itemClick，也视为 core
_CORE_TOOLBAR_KEYS = {"toolbarap", "tbmain", "toolbar"}

# ⭐ 规则6补充：btnsave 类按钮点击 → 视为 core（不被标 optional）
# 这类 save 按钮的 ac 是 click 而非 saveandeffect，但属于业务核心操作
_SAVE_BUTTON_KEYS = {"btnsave", "btnsaveandnew", "btnsaveaddnew", "btnsavenew"}

# click 但属于业务流程入口的按钮。若被 optional 吞掉，后续可能出现
# “执行 PASS 但只保存主单/部分明细”的半成功。
_CORE_CLICK_KEYS = {"newentry", "bizitemgroup", "adminorg", "khr_cost_org"}

# ⭐ 无门户导航时，用于连接“列表/树 → 卡片/表单”的上下文步骤。
# 这类步骤如果被裁掉，新增场景可能丢失默认上下文（如 createorg / tree focus）。
_CONTEXT_BRIDGE_ACS = {
    "refresh",
    "entryRowClick",
    "hyperLinkClick",
    "loadData",
    "selectTab",
    "getHintScroll",
    "postExpandNodes",
    "queryTreeNodeChildren",
    "commonSearch",
    "donothing_newbill",
    "release",
}

_CONTEXT_BRIDGE_CLICK_KEYS = {
    "ok",
    "btnok",
    "btn_ok",
    "bizitemgroup",
    "adminorg",
    "khr_cost_org",
    "newentry",
}


def _is_context_bridge_step(step: dict) -> bool:
    """Return whether a step belongs to a no-menu list/template/F7 bridge."""
    step_type = step.get("type", "")
    ac = step.get("ac", "") or ""
    key = str(step.get("key") or "").lower()
    if step_type == "open_form":
        return True
    if step_type == "pick_basedata":
        return True
    if step_type != "invoke":
        return False
    if ac in _CONTEXT_BRIDGE_ACS:
        return True
    return ac == "click" and key in _CONTEXT_BRIDGE_CLICK_KEYS

# ⭐ 多语言文本字段的语言 key。值即使看起来像数字，也必须按字符串输出。
_MULTILANG_KEYS = {"zh_CN", "zh_TW", "en_US", "GLang"}

# ⭐ 离线上下文补偿提示：
# 某些表单的关键字段并不会在 HAR 中显式 setItem，而是由新增上下文隐式带出。
# 当 API 回放缺失这层客户端上下文时，需要按 form_id 补偿生成步骤。
_CONTEXT_FIELD_HINTS = {
    "hbpm_positionhr": {
        "adminorg": "pick_basedata",
    },
    "hbss_enterprise": {
        "createorg": "update_fields",
    },
}

# ⭐ 非业务主链路导航表单：
# apphome/快捷卡片/后台任务侧栏用于浏览器端布局和入口导航，环境缺少对应 AppIdName
# 时不应阻断已经能直接打开的业务主表单（如 haos_adminorgdetail、hbpm_positionhr）。
_NAVIGATION_FORM_IDS = {
    "bos_card_quicklaunch",
    "gbs_flowcard",
    "gbs_bgtasklistsidebar",
    "gbs_bgtaskdetailsidebar",
    "hom_wbcalendar",
    "hom_wbwaitin",
    "hom_wbwarning",
    "hom_activityoverview",
    "hbp_reviselogpage",
    "khr_hrobs_announcement",
    "nbj_user_selfhelp_sc",
}

_PORTAL_SIDE_EFFECT_FORM_IDS = {
    "gbs_flowcard",
    "gbs_bgtasklistsidebar",
    "gbs_bgtaskdetailsidebar",
    "khr_hrobs_announcement",
    "nbj_user_selfhelp_sc",
}

_AUTO_RESOLVE_FIELD_HINTS = {
    "adminorg",
    "adminorglayer",
    "adminorgtype",
    "ba_e_enterprise",
    "ba_po_adminorg",
    "ba_po_position",
    "changedesc",
    "changereason",
    "changescene",
    "city",
    "companyarea",
    "country",
    "countryregion",
    "enterprise",
    "job",
    "jobgradescm",
    "joblevelscm",
    "khr_homs_orgform",
    "khr_homs_orgloc",
    "lawentity",
    "org",
    "otclassify",
    "parentorg",
    "position",
    "positiontype",
    "province",
    "structproject",
    "workplace",
}

_AUTO_RESOLVE_CODE_FIELD_HINTS = {
    "adminorglayer",
    "bizitemgroup",
    "changescene",
    "city",
    "companyarea",
    "khr_homs_orgform",
    "khr_homs_orgloc",
    "org",
    "otclassify",
    "parentorg",
}


# 值类型识别（从 HAR 里的值反推是什么形式）
_RX_DATE = re.compile(r"^\d{4}-\d{2}-\d{2}$")
_RX_DATETIME = re.compile(r"^\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}")
_RX_TEST_NUMBER = re.compile(r"^[A-Z]{2,5}\d{3,8}$")   # TEST1234 / QA12345 等
_RX_INTEGER = re.compile(r"^\d+$")

# ⭐ 规则2：识别 session-specific pageId 模式（嵌入 root_base_id 的 32位hex）
_RX_ROOT_BASE_ID = re.compile(r"root([a-f0-9]{32})")
_RX_L2_PAGE_ID = re.compile(r"^\d+root[0-9a-f]{32}$")
# 匹配纯随机 32hex pageId（不含 "root" 前缀的独立 pageId）
_RX_RANDOM_PAGE_ID = re.compile(r"^[a-f0-9]{32}$")

# ⭐ 规则5：从原始测试值中提取前缀（去掉末尾数字/随机部分）
_RX_TRAILING_DIGITS = re.compile(r"^(.*?[^0-9])\d{2,}$")

_DEFAULT_CONTEXT_FIELD_KEYS = {"parentorg"}
_RECORDED_DEFAULT_PICK_FIELDS_BY_FORM = {
    "haos_adminorgdetail": (
        "parentorg",
        "changescene",
        "city",
        "companyarea",
        "org",
        "otclassify",
    ),
    "hpdi_bizdatabillchoicetpl": (
        "org",
    ),
}
_RECORDED_DEFAULT_SCALAR_FIELDS_BY_FORM = {
    "haos_orgchangereason": (
        "createorg",
        "ctrlstrategy",
    ),
}


def _recorded_default_value_id(field_key: str, value_code: str, value_name: str = "") -> str:
    """Return the value id to replay for recorded server defaults.

    Some Kingdee "basedata" fields behave like enum-backed data in the UI:
    the display/business code is `1010_S`, while setItemByIdFromClient expects
    the compact id `1010`. Keep `value_code` for the user-facing panel, but
    replay the compact id when this stable pattern is present.
    """
    key = str(field_key or "").strip().lower()
    code = str(value_code or "").strip()
    if key == "changescene":
        m = re.match(r"^(\d+)_S$", code)
        if m:
            return m.group(1)
    return code or str(value_name or "").strip()


def _display_pick_value_id(value_id: Any, value_code: Any) -> str:
    """Prefer business code in user-facing pick_fields when HAR stored an internal id."""
    raw = str(value_id or "").strip()
    code = str(value_code or "").strip()
    if code and _looks_like_internal_id(raw):
        return code
    return raw


def _clean_display_label(label: Any) -> str:
    """Return a user-facing label while keeping technical field_key untouched.

    Some live Kingdee metadata labels include implementation suffixes such as
    "_废弃" even when the current page renders the field without that suffix.
    The field key remains the execution anchor; only the UI-facing label is
    softened here.
    """
    text = str(label or "").strip()
    if not text:
        return ""
    for pattern in (
        r"[_\-\s]*(?:废弃|已废弃)$",
        r"[（(]\s*(?:废弃|已废弃)\s*[）)]$",
    ):
        cleaned = re.sub(pattern, "", text).strip()
        if cleaned != text and cleaned:
            return cleaned
    return text


def _extract_value_prefix(val: str) -> str:
    """从测试值中提取前缀部分。如 'kdtest_hbss_marstest001' → 'kdtest_hbss_'。"""
    m = _RX_TRAILING_DIGITS.match(val)
    if m:
        return m.group(1)
    # 测试编号模式：TEST12345 → TEST
    m2 = re.match(r"^([A-Za-z_]+)", val)
    if m2:
        return m2.group(1)
    return "QA"


# ---------- HAR 解析 ----------

def load_har(path: Path) -> dict:
    with path.open(encoding="utf-8") as f:
        return json.load(f)


def is_business_request(url: str) -> bool:
    if any(url.endswith(s) for s in STATIC_SUFFIX):
        return False
    if "/form/" not in url:
        return False
    return True


# ---------- 业务步骤命名 ----------

# ⭐ 业务描述映射（HAR导入时自动生成description字段）
_FORM_ID_LABELS = {
    'hom_onbrdinfo': '入职信息表',
    'hom_persononbrdhandlebody': '入职处理页',
    'hom_apphome': '人力首页',
    'hom_wbwaitin': '待入职人员',
    'hom_wbcalendar': '日历',
    'hom_wbwarning': '预警',
    'haos_adminorgdetail': '行政组织详情',
    'haos_adminorg': '行政组织',
    'haos_apphome': '行政组织首页',
    'homs_apphome': '人力共享首页',
    'bos_portal_myapp_new': '门户首页',
    'bos_card_quicklaunch': '快捷卡片',
    'gbs_bgtasklistsidebar': '后台任务栏',
    'gbs_bgtaskdetailsidebar': '后台任务详情',
    'home_page': '主页',
    # HR 基础服务常用
    'hbss_appgridhome': 'HR基础服务首页',
    'hbss_basedatalist': '基础资料列表',
    'hbss_lawentity': '法律实体',
    'hbss_hrbuca': 'HR业务管理视图',
    'hbss_laborreltype': '用工关系类型',
    'hbss_laborrel': '用工关系',
    'hbss_employeetype': '员工类型',
    'hbss_dutyworkrole': '岗位职务',
    # 业务模型 / 基础数据建模
    'bos_devportal_bizmodel_basedata': '业务模型-基础资料',
    'bos_devportal_bizmodel_detail': '业务模型设计器',
    'bos_devportal_bizmodel': '业务模型',
    'hrbm_logicentity_display': '业务模型-实体显示设置',
    'hrbm_logicentity': '业务模型-逻辑实体',
    'logicentity_display': '业务模型-实体显示设置',
    'logicentity': '业务模型-逻辑实体',
    'bos_list': '数据列表',
    'bos_form': '业务表单',
    # 开发平台常见
    'bdmanagement': '基础资料管理',
    'apphome': '应用首页',
    # HR其他常见
    'hrcs_appconfig': 'HR应用配置',
}

# ⭐ 按钮 key → 业务动作中文名（用于 itemClick/click 时生成友好描述）
_BUTTON_LABELS = {
    # 保存类
    'btnsave':            '保存',
    'btn_save':           '保存',
    'bar_save':           '保存',
    'barsave':            '保存',
    'btnsaveandeffect':   '保存并生效',
    'btn_saveandeffect':  '保存并生效',
    'btnsubmit':          '提交',
    'btn_submit':         '提交',
    'bar_submit':         '提交',
    'barsubmit':          '提交',
    # 确认/启动类
    'btn_confirm':        '确认',
    'btnconfirm':         '确认',
    'bar_confirm':        '确认',
    'barconfirm':         '确认',
    'bar_start':          '启动流程',
    'barstart':           '启动流程',
    'btn_ok':             '确定',
    'btnok':              '确定',
    # 新增/编辑
    'btn_add':            '新增',
    'btnadd':             '新增',
    'btn_new':            '新增',
    'btnnew':             '新增',
    'btn_edit':           '编辑',
    'btnedit':            '编辑',
    'btn_delete':         '删除',
    'btndelete':          '删除',
    # 审核类
    'btn_audit':          '审核',
    'btnaudit':           '审核',
    'btn_unaudit':        '反审核',
    # 关闭/取消
    'btn_close':          '关闭',
    'btnclose':           '关闭',
    'btn_cancel':         '取消',
    'btncancel':          '取消',
}

_FIELD_LABELS = {
    'ba_em_name': '员工姓名',
    'name': '名称',
    'number': '编码',
    'certificatenumber': '证件号码',
    'certificatetype': '证件类型',
    'gender': '性别',
    'phone': '手机号',
    'ba_em_empnumber': '员工编号',
    'ba_e_laborrelstatus': '用工状态',
    'ba_e_enterprise': '企业',
    'ba_po_adminorg': '行政组织',
    'ba_po_position': '职位',
    'effectdatebak': '生效日期',
    'simple': '简化名称',
    'longname': '长名称',
    # 常见通用字段补充
    'description':  '备注',
    'status':       '状态',
    'enable':       '启用状态',
    'cusstatus':    '业务状态',
    'createtime':   '创建时间',
    'modifytime':   '修改时间',
    'creator':      '创建人',
    'modifier':     '修改人',
    'parent':       '上级',
    'org':          '组织体系管理组织',
    'country':      '国家',
    'companyarea':  '国家/地区',
    'province':     '省份',
    'city':         '城市',
    'address':      '地址',
    'email':        '邮箱',
    'birthday':     '生日',
    'startdate':    '开始日期',
    'enddate':      '结束日期',
    # ⭐ 核心 4 HAR 涉及的 pick_basedata 业务字段
    'adminorgtype':       '行政组织类型',
    'adminorglayer':      '行政组织层级',
    'changescene':        '组织变动场景',
    'otclassify':         '组织分类',
    'laborreltypecls':    '用工关系分类',
    'basedatafield':      '基础资料字段',
    'menulocal':          '菜单位置',
    'bsed':               '生效日期',
    'bsled':              '失效日期',
    'effectdate':         '生效日期',
    'loseeffectdate':     '失效日期',
    'bizdate':            '业务归属日期',
    'kd311':              '工作加班小时',
    'kd305':              '周末加班小时',
    'kd306':              '法定加班小时',
    'khr_homs_condes':    '保密描述',
    'parentorg':          '上级行政组织',
    'orgpattern':         '组织形态',
    'khr_homs_orgform':   '组织形态',
    'khr_homs_orgloc':    '行政组织定位',
    'biztype':            '业务类型',
    'useorg':             '使用组织',
    'createorg':          '创建组织',
    'ctrlstrategy':        '控制策略',
    'entity':             '实体',
    'entityobjecttype':   '实体对象类型',
    'objecttype':         '对象类型',
    'fieldtype':          '字段类型',
    'tabletype':          '表类型',
    # HR 入职/员工相关字段
    'nationality':        '国籍',
    'ba_po_workplace':    '工作地点',
    'workplace':          '工作地点',
    'onbrdtcitybak':      '入职城市',
    'onbrdtcity':         '入职城市',
    'ba_a_country':       '国家/地区',
    'handler':            '经办人',
    'probationperiod':    '试用期',
    'probationenddate':   '试用期结束日期',
    'contractstartdate':  '合同开始日期',
    'contractenddate':    '合同结束日期',
    'laborreltype':       '用工关系类型',
    'empgroup':           '员工组',
    'emptype':            '员工类型',
    'workcity':           '工作城市',
    'nation':             '民族',
    'maritalstatus':      '婚姻状况',
    'politicalstatus':    '政治面貌',
    'education':          '学历',
    'degree':             '学位',
    'major':              '专业',
    'school':             '毕业院校',
    'entrydate':          '入职日期',
    'regulardate':        '转正日期',
    'dimissiondate':      '离职日期',
}


def _resolve_field_label(field_key: str, entity_id: str = None, meta_resolver=None) -> str:
    """动态解析字段中文标签，优先查询实时元数据。

    优先级链：
    1. MetadataResolver 实时查询（在线时）
    2. kb_loader.get_field_label() (知识库动态查询)
    3. _FIELD_LABELS.get() (静态字典兜底)
    4. field_key 原始值 (最终fallback)
    """
    # 新增：实时元数据查询
    if meta_resolver and entity_id:
        label = meta_resolver.get_field_label(entity_id, field_key)
        if label:
            return _clean_display_label(label)
    key_lower = field_key.lower()
    if entity_id:
        meta = _kb_field_meta(entity_id, key_lower)
        label = (meta or {}).get("label")
        if label:
            return _clean_display_label(label)
    # 优先查询知识库
    kb_label = _kb_get_field_label(key_lower)
    if kb_label:
        return _clean_display_label(kb_label)
    # 静态字典兜底
    return _clean_display_label(_FIELD_LABELS.get(key_lower, field_key))


_AC_LABELS = {
    'loadData': '加载数据',
    'loadMeta': '加载元数据',
    'treeMenuClick': '点击树形菜单',
    'menuItemClick': '点击菜单',
    'appItemClick': '点击应用',
    'selectTab': '切换Tab',
    'startupflow': '启动流程',
    'itemClick': '点击按钮',
    'click': '点击',
    'afterConfirm': '确认提交',
    'doconfirm': '执行确认',
    'query': '查询',
    'updateValue': '更新值',
    'setItemByIdFromClient': '选择基础资料',
    'sendDynamicFormAction': '表单动作',
    'addnew': '新增',
    'save': '保存',
    'saveandeffect': '保存并生效',
    'submit': '提交',
    'submitandeffect': '提交并生效',
    'saveandaudit': '保存并审核',
    'audit': '审核',
    'unaudit': '反审核',
    'close': '关闭',
    'entryRowClick': '点击分录行',
    'rowDblClick': '双击行',
    'pickValue': '取值',
    'selectRow': '选中行',
}


# ⭐ 懒加载 cosmic-hr-expert 知识库中的 formNumber→业务名映射
# 这样可以把 HR基础服务/组织开发/薪酬/工时 等云的几千条 form 映射全部拉进来，
# 显著提升执行日志的可读性。
_KNOWLEDGE_FORM_LABELS: dict[str, str] | None = None


def _load_knowledge_form_labels() -> dict[str, str]:
    """从 skills/cosmic-hr-expert/knowledge/ 下加载 formNumber → 业务名字典。

    格式：{formNumber: "{app中文名}-{场景中文名}"}，例如：
        hbss_basedatalist → "HR基础服务-基础资料"

    失败静默（skill 可能不存在），返回空 dict 不影响基础功能。
    结果在进程内缓存一次，后续调用零开销。
    """
    global _KNOWLEDGE_FORM_LABELS
    if _KNOWLEDGE_FORM_LABELS is not None:
        return _KNOWLEDGE_FORM_LABELS

    result: dict[str, str] = {}
    skill_knowledge = (Path(__file__).resolve().parent.parent
                       / "skills" / "cosmic-hr-expert" / "knowledge")
    files = [
        "_hr_hrmp_app_map.json",
        "_org_dev_app_map.json",
        "_swc_app_map.json",
        "_wtc_app_map.json",
    ]
    for fname in files:
        p = skill_knowledge / fname
        if not p.exists():
            continue
        try:
            data = json.loads(p.read_text(encoding="utf-8"))
            apps = data.get("apps", {}) or {}
            for _app_id, app_info in apps.items():
                app_name = (app_info or {}).get("name", "")
                scenes = (app_info or {}).get("scenes", []) or []
                for s in scenes:
                    num = (s or {}).get("formNumber", "")
                    nm = (s or {}).get("name", "")
                    if num and nm and num not in result:
                        result[num] = f"{app_name}-{nm}" if app_name else nm
        except Exception:
            # 知识库个别文件损坏不影响其他
            continue

    _KNOWLEDGE_FORM_LABELS = result
    return result


def _resolve_form_name(form_id: str) -> str:
    """统一的 form_id → 中文业务名查找。
    优先级：内置字典 > scenarios 场景名 > app_map > 启发式
    """
    if not form_id:
        return ''
    # 1) 内置小字典（高频表单定制化命名，优先级最高）
    name = _FORM_ID_LABELS.get(form_id)
    if name:
        return name
    # 2) scenarios/<form>/scenario.json#name（558 个场景，最精确）
    try:
        from lib import kb_loader as _kb
        kb_name = _kb.resolve_form_name(form_id)
        if kb_name:
            return kb_name
    except Exception:
        pass
    # 3) app_map（"{app}-{scene}" 复合命名，兼容旧路径）
    name = _load_knowledge_form_labels().get(form_id)
    if name:
        return name
    # 4) 启发式：从 form_id 末段推断
    short = _form_short(form_id)
    return short.replace('_', ' ') if short else form_id


def generate_step_description(step: dict) -> str:
    """根据步骤信息生成中文业务描述，用于HAR导入时自动填充description字段。

    策略分层（从精细到兜底）：
      1) 优先识别按钮语义（itemClick/click + key 命中 _BUTTON_LABELS）
      2) 写库类 ac（save/saveandeffect/startupflow/afterConfirm/doconfirm）
      3) 菜单/Tab/加载/打开 → 带业务域前缀（依赖 _resolve_form_name）
      4) 字段填写：多字段列出主要字段名
      5) 基础资料选择：显示字段中文名
      6) 最终兜底：ac 中文名
    """
    step_type = step.get('type', '')
    form_id = step.get('form_id', '')
    ac = step.get('ac', '')
    key = step.get('key', '')
    field_key = step.get('field_key', '')
    fields = step.get('fields', {})

    form_name = _resolve_form_name(form_id)

    # ---------- open_form ----------
    if step_type == 'open_form':
        return f"打开「{form_name or form_id}」"

    # ---------- invoke ----------
    if step_type == 'invoke':
        ac_label = _AC_LABELS.get(ac, ac)
        key_l = (key or '').lower()

        # 1) 按钮优先：itemClick / click + key 命中按钮字典
        if ac in ('itemClick', 'click'):
            btn_name = _BUTTON_LABELS.get(key_l)
            # 按钮 args 里有可能是用户可见文本（中文），优先用它
            args = step.get('args') or []
            btn_from_args = ''
            if args:
                first = args[0]
                if isinstance(first, str) and first:
                    btn_from_args = first

            label = btn_name or btn_from_args
            if label:
                # 高亮写库按钮类，让日志一眼看到
                if label in ('保存', '保存并生效', '提交', '提交并生效',
                             '确认', '确定', '启动流程'):
                    return f"💾 点击【{label}】按钮"
                return f"点击【{label}】按钮"
            # 都没命中，尽量带 form_name
            if form_name:
                return f"在「{form_name}」上点击按钮"
            return "点击按钮"

        # 2) 写库类 ac，明确标识
        if ac == 'startupflow':
            ctx = form_name or '流程'
            return f"🚀 启动【{ctx}】流程"
        if ac == 'afterConfirm':
            ctx = form_name or '表单'
            return f"✅ 【{ctx}】确认提交"
        if ac == 'doconfirm':
            ctx = form_name or '流程'
            return f"✅ 【{ctx}】执行确认"
        if ac in ('save', 'submit'):
            ctx = form_name or ''
            prefix = '💾' if ac == 'save' else '📤'
            return f"{prefix} 保存【{ctx}】" if ctx else f"{prefix} {ac_label}"
        if ac in ('saveandeffect', 'submitandeffect', 'saveandaudit'):
            ctx = form_name or ''
            return f"✅ {ac_label}【{ctx}】" if ctx else f"✅ {ac_label}"
        if ac in ('audit', 'unaudit'):
            ctx = form_name or ''
            return f"🔍 {ac_label}【{ctx}】" if ctx else f"🔍 {ac_label}"

        # 3) 菜单 / Tab / 加载 / 查询
        if ac == 'loadData':
            return f"加载「{form_name or '数据'}」"
        if ac == 'loadMeta':
            return f"加载元数据「{form_name or ''}」".rstrip('「」')
        if ac in ('treeMenuClick', 'menuItemClick', 'appItemClick'):
            return f"点击菜单进入「{form_name or '页面'}」"
        if ac == 'selectTab':
            return f"切换到「{form_name or 'Tab'}」"
        if ac == 'query':
            return f"查询「{form_name or ''}」".rstrip('「」')

        # 4) 值更新 / 选基础资料
        if ac == 'updateValue':
            # 尝试从 args/post_data 解出具体字段
            return "更新字段值"
        if ac == 'setItemByIdFromClient':
            if key:
                fname = _resolve_field_label(key)
                return f"选择「{fname}」基础资料"
            return "选择基础资料"
        if ac == 'pickValue':
            if key:
                fname = _resolve_field_label(key)
                return f"取值「{fname}」"
            return "取值"

        # 5) 分录行操作
        if ac == 'entryRowClick':
            return f"点击分录行" + (f"（{form_name}）" if form_name else "")

        # 6) addnew / close
        if ac == 'addnew':
            return f"📝 新增【{form_name}】" if form_name else "📝 新增记录"
        if ac == 'close':
            return f"关闭「{form_name}」" if form_name else "关闭表单"

        # 兜底：带业务域的 ac 名（用「」包裹而非 []，避免被 YAML 视作 flow-sequence 加引号）
        if form_name:
            return f"「{form_name}」{ac_label}"
        return ac_label

    # ---------- update_fields ----------
    if step_type == 'update_fields':
        if fields and isinstance(fields, dict):
            keys = list(fields.keys())
            names = []
            for k in keys[:3]:  # 最多展示 3 个字段
                names.append(_resolve_field_label(k))
            names_str = '/'.join(names)
            if len(keys) == 1:
                return f"填写「{names_str}」"
            if len(keys) <= 3:
                return f"填写「{names_str}」"
            return f"填写「{names_str}」等 {len(keys)} 个字段"
        return "填写字段"

    # ---------- pick_basedata ----------
    if step_type == 'pick_basedata':
        fname = _resolve_field_label(field_key) if field_key else '基础资料'
        return f"选择「{fname}」"

    # ---------- validate / wait ----------
    if step_type == 'validate':
        return "⚡ 验证断言"
    if step_type == 'wait':
        ms = step.get('ms') or step.get('timeout', '')
        return f"⏱ 等待 {ms}ms" if ms else "⏱ 等待"

    return f"{step_type}"


def _build_case_description(*, har_path: Path, main_form: str, yaml_steps: list,
                             core_count: int, ui_count: int,
                             noise_count: int) -> str:
    """生成顶层 case.description，把业务含义而非统计数字放首位。

    示例：
      "新增【HR基础服务-基础资料】主流程（16 个核心步骤 · 写库动作: 💾 点击【保存】按钮 ·
        来源 preview_xxx.har · ui 联动 3 · 噪声 6）"
    """
    subject = _resolve_form_name(main_form) or main_form or '未知业务'

    # ⭐ 接入知识库上下文：domain / cloud / 菜单尾段
    kb_domain = ''
    kb_cloud = ''
    kb_menu_tail = ''
    try:
        from lib import kb_loader as _kb
        scene = _kb.resolve_scene(main_form)
        if scene:
            kb_domain = scene.get('domain', '') or ''
            mp = scene.get('menu_paths') or []
            if mp and isinstance(mp[0], dict):
                path = mp[0].get('path') or []
                if path:
                    # 取最后 2 级菜单，如 "行政组织维护 > 组织快速维护"
                    kb_menu_tail = ' > '.join(path[-2:])
        kb_cloud = _kb.resolve_cloud(main_form) or ''
    except Exception:
        pass

    # 找写库锚点：优先 saveandeffect / saveandaudit / save / submit
    _write_acs = {"saveandeffect", "submitandeffect", "saveandaudit",
                  "save", "submit", "doconfirm", "afterconfirm", "startupflow"}
    _write_keys = {"btnsave", "btn_save", "bar_save", "barsave",
                   "btn_confirm", "btnconfirm", "bar_confirm", "barconfirm",
                   "btnok", "btn_ok", "bar_submit", "barsubmit",
                   "barstart", "bar_start",
                   "btn_saveandeffect", "btnsaveandeffect"}
    write_ac = ""
    write_key = ""
    write_desc = ""
    for s in yaml_steps:
        ac_l = str(s.get('ac') or '').lower()
        key_l = str(s.get('key') or '').lower()
        if ac_l in _write_acs:
            write_ac = s.get('ac') or ''
            write_desc = s.get('description') or ''
            break
        if key_l in _write_keys:
            write_key = s.get('key') or ''
            write_desc = s.get('description') or ''
            break

    # 推断主语动词
    if write_ac in ('saveandeffect', 'submitandeffect', 'saveandaudit'):
        action_cn = '新增/维护并生效'
    elif write_ac in ('startupflow',):
        action_cn = '发起流程'
    elif write_ac in ('afterconfirm', 'doconfirm'):
        action_cn = '确认业务'
    elif write_ac in ('save', 'submit') or write_key:
        action_cn = '新增/维护'
    else:
        action_cn = '查询浏览'

    write_hint = write_desc or (write_ac or write_key or '无显式写库动作')

    # 注意：用中文全角冒号「：」而非半角「: 」，避免 YAML dump 把整串值包双引号
    parts = [f"{action_cn}【{subject}】"]
    # ⭐ 知识库上下文（有则补在业务含义后面，便于一眼识别业务归属）
    if kb_cloud:
        parts.append(f"云：{kb_cloud}")
    if kb_domain:
        parts.append(f"域：{kb_domain}")
    if kb_menu_tail:
        parts.append(f"菜单：{kb_menu_tail}")
    parts.append(f"{len(yaml_steps)} 步")
    parts.append(f"写库动作：{write_hint}")
    parts.append(f"来源 {har_path.name}")
    stats = f"原始 core={core_count}，ui={ui_count}，noise={noise_count}"
    parts.append(stats)
    return " · ".join(parts)


def smart_name(action: dict, ac: str, ordinal: int) -> str:
    """基于 action 语义生成可读 step id"""
    method = action.get("methodName", "")
    key = action.get("key", "")
    args = action.get("args", [])
    post_data = action.get("postData", [{}, []])

    # 优先级：看具体含义

    # itemClick on toolbar → 按钮名
    if key in ("toolbarap", "tbmain") and method == "itemClick" and args:
        btn = args[0] if isinstance(args[0], str) else ""
        clean = _sanitize(btn)
        return f"click_{clean}" if clean else f"click_button_{ordinal}"

    # updateValue → fill_<字段名>
    if method == "updateValue":
        fields = _extract_update_fields(post_data)
        if fields:
            primary = list(fields.keys())[0]
            if len(fields) == 1:
                return f"fill_{primary}"
            return f"fill_{primary}_etc"

    # setItemByIdFromClient → pick_<字段名>
    if method == "setItemByIdFromClient":
        if key:
            return f"pick_{_sanitize(key)}"

    # loadData
    if ac == "loadData":
        form_hint = _form_short(action.get("_form_id", ""))
        return f"load_{form_hint}" if form_hint else f"load_{ordinal}"

    # addnew
    if ac == "addnew":
        return "addnew"

    # save / submit
    if ac in ("save", "submit"):
        return ac

    # close
    if ac == "close":
        return "close"

    # 其他：ac 名
    return f"{ac}_{ordinal}"


def _sanitize(s: str) -> str:
    """把中文/特殊字符剥成可做 id 的"""
    if not s:
        return ""
    out = re.sub(r"[^a-zA-Z0-9_]", "_", s)
    out = re.sub(r"_+", "_", out).strip("_")
    return out.lower()


def _form_short(form_id: str) -> str:
    """从 form_id 抽出简短标签，如 haos_adminorgdetail → adminorg"""
    if not form_id:
        return ""
    parts = form_id.split("_", 1)
    if len(parts) == 2:
        tail = parts[1]
        # 砍掉常见后缀
        for suffix in ("detail", "edit", "info", "form"):
            if tail.endswith(suffix) and len(tail) > len(suffix) + 2:
                tail = tail[: -len(suffix)]
        return tail
    return form_id


def _extract_update_fields(post_data: list) -> dict[str, Any]:
    """从 updateValue 的 postData 抽 {field_key: value}"""
    fields: dict[str, Any] = {}
    if not (isinstance(post_data, list) and len(post_data) >= 2):
        return fields
    entries = post_data[1]
    if not isinstance(entries, list):
        return fields
    for e in entries:
        if isinstance(e, dict) and "k" in e and "v" in e:
            fields[e["k"]] = e["v"]
    return fields


def _looks_like_internal_id(value: Any) -> bool:
    text = str(value or "").strip()
    return text.isdigit() and len(text) >= 12


def _extract_basedata_parts(value: Any) -> dict[str, str]:
    """从苍穹基础资料显示值中拆出业务编码/名称/编号。"""
    row = value
    if isinstance(row, list) and row and isinstance(row[0], list):
        row = row[0]
    if not isinstance(row, list) or len(row) < 2:
        return {}
    value_code = str(row[0] or "").strip()
    value_name = str(row[1] or "").strip()
    value_number = str(row[2] or "").strip() if len(row) >= 3 else value_code
    if not value_code and not value_name:
        return {}
    return {
        "value_code": value_code,
        "value_name": value_name,
        "value_number": value_number,
    }


def _is_l2_page_id(value: Any) -> bool:
    return bool(_RX_L2_PAGE_ID.match(str(value or "").strip()))


def _iter_har_response_actions(har: dict):
    for idx, entry in enumerate(har.get("log", {}).get("entries", [])):
        req = entry.get("request", {}) or {}
        url = req.get("url", "") or ""
        parsed = urllib.parse.urlparse(url)
        qs = urllib.parse.parse_qs(parsed.query)
        form_id = qs.get("f", [""])[0]
        app_id = qs.get("appId", [""])[0]
        resp_text = (entry.get("response") or {}).get("content", {}).get("text", "") or ""
        if not resp_text:
            continue
        try:
            resp_json = json.loads(resp_text)
        except Exception:
            continue
        def walk(obj: Any):
            if isinstance(obj, dict):
                if obj.get("a"):
                    yield obj
                for child in obj.values():
                    if isinstance(child, (dict, list)):
                        yield from walk(child)
            elif isinstance(obj, list):
                for child in obj:
                    yield from walk(child)

        yield from ((idx, form_id, app_id, cmd) for cmd in walk(resp_json))


def _collect_har_field_observations(har: dict) -> dict[str, Any]:
    """收集响应中的字段锁定状态和值，用于生成更贴近浏览器录制的 YAML。"""
    locked_events: dict[int, set[str]] = {}
    response_values: dict[str, dict[str, str]] = {}
    response_values_by_form: dict[str, dict[str, dict[str, str]]] = {}
    response_raw_values_by_form: dict[str, dict[str, Any]] = {}
    response_internal_ids_by_form: dict[str, dict[str, str]] = {}
    combo_options_by_form: dict[str, dict[str, dict[str, str]]] = {}
    labels_by_form: dict[str, dict[str, str]] = {}

    def mark_locked(har_index: int, field_key: Any) -> None:
        key = str(field_key or "").strip().lower()
        if key:
            locked_events.setdefault(har_index, set()).add(key)

    def _is_recorded_scalar_default(form_id: str, key: str) -> bool:
        return key in {
            str(item).lower()
            for item in _RECORDED_DEFAULT_SCALAR_FIELDS_BY_FORM.get(form_id, ())
        }

    def remember_value(field_key: Any, value: Any, form_id: str = "") -> None:
        key = str(field_key or "").strip().lower()
        if not key:
            return
        parts = _extract_basedata_parts(value)
        if parts:
            response_values[key] = parts
            if form_id:
                response_raw_values_by_form.setdefault(form_id, {})[key] = value
                response_values_by_form.setdefault(form_id, {})[key] = parts
            return
        if value not in (None, "") and (
            key in _DEFAULT_CONTEXT_FIELD_KEYS
            or _is_recorded_scalar_default(form_id, key)
        ):
            fallback = {
                "value_code": str(value),
                "value_name": str(value),
                "value_number": str(value),
            }
            response_values.setdefault(key, fallback)
            if form_id:
                response_raw_values_by_form.setdefault(form_id, {})[key] = value
                response_values_by_form.setdefault(form_id, {}).setdefault(key, fallback)

    def remember_control_meta(form_id: str, meta: dict) -> None:
        def visit(obj: Any) -> None:
            if isinstance(obj, dict):
                field_key = str(obj.get("dataIndex") or obj.get("id") or "").strip().lower()
                if field_key:
                    meta_form_id = str(obj.get("entity") or form_id or "").strip()
                    header = obj.get("header")
                    if isinstance(header, dict):
                        label = str(header.get("zh_CN") or header.get("name") or "").strip()
                        if label and meta_form_id:
                            labels_by_form.setdefault(meta_form_id, {})[field_key] = _clean_display_label(label)
                    editor = obj.get("editor")
                    if isinstance(editor, dict) and editor.get("type") == "combo":
                        options: dict[str, str] = {}
                        for row in editor.get("st") or []:
                            if not (isinstance(row, list) and len(row) >= 2):
                                continue
                            value = str(row[0] or "").strip()
                            name_obj = row[1]
                            if isinstance(name_obj, dict):
                                name = str(name_obj.get("zh_CN") or name_obj.get("name") or "").strip()
                            else:
                                name = str(name_obj or "").strip()
                            if value and name and value != "******" and name != "******":
                                options[value] = _clean_display_label(name)
                        if options and meta_form_id:
                            combo_options_by_form.setdefault(meta_form_id, {})[field_key] = options
                for child in obj.values():
                    if isinstance(child, (dict, list)):
                        visit(child)
            elif isinstance(obj, list):
                for child in obj:
                    visit(child)

        visit(meta)

    def remember_list_row_ids(form_id: str, node: Any) -> None:
        if isinstance(node, dict):
            dataindex = node.get("dataindex")
            rows = node.get("rows")
            if isinstance(dataindex, dict) and isinstance(rows, list):
                for base_key in ("createorg", "useorg", "org", "parentorg"):
                    id_key = f"{base_key}_id"
                    name_key = f"{base_key}_name"
                    if id_key not in dataindex:
                        continue
                    id_idx = dataindex.get(id_key)
                    name_idx = dataindex.get(name_key)
                    if not isinstance(id_idx, int):
                        continue
                    for row in rows:
                        if not isinstance(row, list) or id_idx >= len(row):
                            continue
                        internal_id = str(row[id_idx] or "").strip()
                        if not internal_id:
                            continue
                        display_name = ""
                        if isinstance(name_idx, int) and name_idx < len(row):
                            display_name = str(row[name_idx] or "").strip()
                        response_internal_ids_by_form.setdefault(form_id, {})[base_key] = internal_id
                        parts = response_values_by_form.get(form_id, {}).get(base_key)
                        if isinstance(parts, dict):
                            parts["value_number"] = internal_id
                            if display_name and not parts.get("value_name"):
                                parts["value_name"] = display_name
                        top_parts = response_values.get(base_key)
                        if isinstance(top_parts, dict):
                            top_parts["value_number"] = internal_id
                        break
            for child in node.values():
                if isinstance(child, (dict, list)):
                    remember_list_row_ids(form_id, child)
        elif isinstance(node, list):
            for child in node:
                remember_list_row_ids(form_id, child)

    for har_index, form_id, _app_id, cmd in _iter_har_response_actions(har):
        action = cmd.get("a")
        params = cmd.get("p") or []
        if isinstance(cmd, dict):
            remember_control_meta(form_id, cmd)
            remember_list_row_ids(form_id, cmd)
        if action == "updateControlMetadata" and isinstance(params, list):
            for i in range(0, len(params) - 1, 2):
                meta = params[i + 1]
                if not isinstance(meta, dict):
                    continue
                remember_control_meta(form_id, meta)
                item_meta = meta.get("item") if isinstance(meta.get("item"), dict) else {}
                if meta.get("mi") is True or item_meta.get("mi") is True:
                    mark_locked(har_index, params[i])
        elif action == "u" and isinstance(params, list):
            for item in params:
                if isinstance(item, dict) and "k" in item:
                    remember_value(item.get("k"), item.get("v"), form_id)
        elif action == "showForm" and isinstance(params, list):
            for item in params:
                if isinstance(item, dict):
                    remember_control_meta(form_id, item)

    for form_id, fields in response_values_by_form.items():
        options_for_form = combo_options_by_form.get(form_id) or {}
        for field_key, parts in fields.items():
            options = options_for_form.get(field_key) or {}
            value_code = str(parts.get("value_code") or "").strip()
            option_label = options.get(value_code, "")
            if option_label:
                parts["value_name"] = option_label
                if field_key in response_values:
                    response_values[field_key]["value_name"] = option_label

    return {
        "locked_events": locked_events,
        "response_values": response_values,
        "response_values_by_form": response_values_by_form,
        "response_raw_values_by_form": response_raw_values_by_form,
        "response_internal_ids_by_form": response_internal_ids_by_form,
        "combo_options_by_form": combo_options_by_form,
        "labels_by_form": labels_by_form,
    }


def _is_locked_before(observations: dict[str, Any], field_key: Any, har_index: Any) -> bool:
    key = str(field_key or "").strip().lower()
    if not key:
        return False
    try:
        idx = int(har_index)
    except (TypeError, ValueError):
        return False
    for event_idx, fields in (observations.get("locked_events") or {}).items():
        # 苍穹 loadData 里常会先下发只读/锁定元数据，后续用户仍可能通过业务流程
        # 进入可编辑态。只采用紧邻当前 updateValue 的锁定证据，避免跨页面/跨阶段误杀。
        if 0 < idx - event_idx <= 2 and key in fields:
            return True
    return False


def _drop_locked_update_fields(steps: list[dict], observations: dict[str, Any]) -> list[dict]:
    """移除当前 HAR 中已被服务端标记锁定的字段写入。"""
    if not observations:
        return steps
    change_year_unlocks: set[tuple[int, str]] = set()
    for step in steps:
        if not (
            step.get("type") == "invoke"
            and (step.get("ac") == "changeYear" or step.get("method") == "changeYear")
        ):
            continue
        try:
            idx = int(step.get("_har_index"))
        except (TypeError, ValueError):
            continue
        field_key = str(step.get("key") or "").strip().lower()
        if field_key:
            change_year_unlocks.add((idx, field_key))

    def _has_recent_change_year(field_key: str, har_index: Any) -> bool:
        try:
            idx = int(har_index)
        except (TypeError, ValueError):
            return False
        return any(
            key == field_key and 0 < idx - change_idx <= 1
            for change_idx, key in change_year_unlocks
        )

    out: list[dict] = []
    for step in steps:
        if step.get("type") != "update_fields":
            out.append(step)
            continue
        fields = step.get("fields")
        if not isinstance(fields, dict):
            out.append(step)
            continue
        kept = OrderedDict()
        dropped: list[str] = []
        for field_key, value in fields.items():
            field_key_s = str(field_key or "").strip().lower()
            if (
                _is_locked_before(observations, field_key, step.get("_har_index"))
                and not _has_recent_change_year(field_key_s, step.get("_har_index"))
            ):
                dropped.append(str(field_key))
                continue
            kept[field_key] = value
        if not dropped:
            out.append(step)
        elif kept:
            new_step = dict(step)
            new_step["fields"] = kept
            new_step["_dropped_locked_fields"] = dropped
            out.append(new_step)
    return out


def _extract_row_index(post_data: list) -> int:
    """从 updateValue 的 postData 中提取 entry row_index。

    HAR 中 entry 字段的 updateValue postData 格式：
        [{}, [{"k": "ename", "v": "aaa", "r": 3}]]
    其中 r 是 entry 行号。r=-1 表示主表单字段（非 entry）。
    返回第一个 r>=0 的值，若无则返回 -1。
    """
    if not (isinstance(post_data, list) and len(post_data) >= 2):
        return -1
    entries = post_data[1]
    if not isinstance(entries, list):
        return -1
    for e in entries:
        if isinstance(e, dict) and "r" in e:
            r = e["r"]
            if isinstance(r, int) and r >= 0:
                return r
    return -1


# ---------- 值→占位符 ----------

_UNIQUE_KEY_HINTS = {"number", "code", "simplename", "name", "fullname",
                     "billno", "orderno", "email", "peremail"}
_NUMBER_KEYS = {"number", "code", "billno", "orderno"}
_NAME_KEYS = {"name", "simplename", "fullname"}
_CLASSIFY_KEY_EXCLUSIONS = {"ename", "classtypeid"}
_HR_UNIQUE_SUFFIXES = {"empnumber", "certificatenumber", "phone"}
_HR_NAME_FIELDS = {"ba_em_name", "em_name", "staffname"}
_HR_PHONE_FIELDS = {"phone", "tel", "mobile", "cellphone", "contactphone"}
_HR_EMAIL_FIELDS = {"email", "peremail", "workemail", "personalemail"}
_TEXT_VARIABLE_KEYS = {
    "description",
    "desc",
    "remark",
    "remarks",
    "memo",
    "note",
    "comment",
    "comments",
    "changedescription",
    "changedesc",
    "khr_homs_condes",
}

_BUSINESS_INPUT_VARIABLE_KEYS = {
    "bizdate": ("business_belong_date", "业务归属日期"),
    "kd311": ("workday_overtime_hours", "工作加班小时"),
    "kd305": ("weekend_overtime_hours", "周末加班小时"),
    "kd306": ("holiday_overtime_hours", "法定加班小时"),
}

_F7_SELECTOR_FORM_LABELS = {
    "hsbs_empposf7querylist": ("employee_position", "计薪人员任职经历", "employee"),
    "hsbs_employeequerylistf7": ("employee_position", "计薪人员任职经历", "employee"),
}


def _classify_key(key_hint: str) -> str | None:
    """将字段 key 分类为 number/name/phone/cert/unique 或 None。"""
    return _classify_key_heuristic(key_hint)


def _classify_key_heuristic(key_hint: str) -> str | None:
    kl = (key_hint or "").lower()
    if not kl:
        return None
    if kl in _UNIQUE_KEY_HINTS:
        if kl in _NUMBER_KEYS:
            return "number"
        if kl in _NAME_KEYS:
            return "name"
        if kl in _HR_EMAIL_FIELDS:
            return "email"
        return "unique"
    if kl in _HR_NAME_FIELDS:
        return "name"
    if kl in _HR_PHONE_FIELDS:
        return "phone"
    if kl in _HR_EMAIL_FIELDS:
        return "email"
    if kl in _TEXT_VARIABLE_KEYS or kl.endswith("description") or kl.endswith("remark"):
        return "text"
    for suffix in _HR_UNIQUE_SUFFIXES:
        if kl.endswith(suffix):
            if "number" in suffix and "certificate" not in kl:
                return "number"
            if "certificate" in kl:
                return "cert"
            if suffix in ("phone", "tel", "mobile"):
                return "phone"
            return "unique"
    for hint in _NUMBER_KEYS:
        if kl.endswith(hint) and len(kl) > len(hint):
            return "number"
    if kl in _CLASSIFY_KEY_EXCLUSIONS:
        return None
    if "email" in kl:
        return "email"
    for hint in _NAME_KEYS:
        if kl.endswith(hint) and len(kl) > len(hint):
            return "name"
    if kl in _TEXT_VARIABLE_KEYS or kl.endswith("description") or kl.endswith("remark"):
        return "text"
    return None


def detect_var_placeholders(actions_seq: list[dict], meta_resolver=None) -> tuple[list[dict], dict[str, Any], dict[str, str]]:
    """扫 updateValue 的值，识别"看起来像测试数据"的值抽成 vars。
    返回：(修改后的 actions_seq, vars_map, vars_labels)

    ⭐ 规则7（统一变量引用）：
    连续新增多条记录时，所有保存轮次统一引用同一个 test_number / test_name 变量。
    vars 在 session 初始化时只解析一次，后续步骤中 ${vars.test_number} 始终返回
    相同的已解析值，保证所有轮次编码/名称一致，UI 只显示 2 个变量。
    """
    vars_map: dict[str, Any] = {}
    vars_labels: dict[str, str] = {}  # 变量名 → 中文标签
    seen_values: dict[str, str] = {}   # 原始值 → 变量名

    # ── 连续新增计数器 ──
    save_round = 1
    round_number_assigned: dict[int, str] = {}   # round → vname
    _SAVE_ACS = {"saveandeffect", "submitandeffect", "save", "submit"}

    # ⭐ 当前遍历到的 action 所属 form_id（供 _classify_key 走 kb 查询）
    current_form_id = ""

    # ⭐ 尝试装载 kb_loader（失败静默，回落纯启发式）
    try:
        from lib import kb_loader as _kb
    except Exception:
        _kb = None  # type: ignore

    def _classify_key(key_hint: str) -> str | None:
        """将字段 key 分类为 number/name/phone/cert/unique 或 None（不需抽变量）。

        ⭐ 接入知识库：先问 kb_loader.classify_field
          - kb 说 "B"/"ignore"/"C" → 返回 None（一票否决：枚举/基础资料/系统字段/响应回传不变量化）
          - kb 说 "A" → 继续走下面启发式细分具体前缀
          - kb 说 None 或异常 → 完全回落启发式

        支持精确匹配和后缀匹配，覆盖 HR 复合字段名如 ba_em_name、certificatenumber。
        """
        kl = key_hint.lower()

        # ⭐ kb 一票否决前置
        if _kb is not None and current_form_id:
            try:
                kb_cls = _kb.classify_field(current_form_id, key_hint, meta_resolver=meta_resolver)
                if kb_cls in ("B", "ignore", "C"):
                    return None
                # kb_cls == "A" 或 None 时继续走原有启发式，以便细分前缀
            except Exception:
                pass

        return _classify_key_heuristic(key_hint)

    def _text_var_name(key_hint: str) -> str:
        kl = (key_hint or "text").lower()
        mapping = {
            "description": "test_description",
            "desc": "test_description",
            "remark": "test_remark",
            "remarks": "test_remark",
            "memo": "test_memo",
            "note": "test_note",
            "comment": "test_comment",
            "comments": "test_comment",
            "changedescription": "test_change_description",
            "changedesc": "test_change_description",
            "khr_homs_condes": "test_confidential_description",
        }
        if kl in mapping:
            return mapping[kl]
        safe = re.sub(r"[^a-zA-Z0-9_]", "_", kl).strip("_") or "text"
        return f"test_{safe}"

    def maybe_var(val: Any, key_hint: str = "") -> Any:
        key_lower = (key_hint or "").lower()
        if key_lower in _BUSINESS_INPUT_VARIABLE_KEYS and val not in ("", None):
            suffix, label = _BUSINESS_INPUT_VARIABLE_KEYS[key_lower]
            vname = f"test_{suffix}"
            if vname not in vars_map:
                if key_lower == "bizdate" and isinstance(val, str) and (_RX_DATE.match(val) or _RX_DATETIME.match(val)):
                    vars_map[vname] = "${today}"
                else:
                    vars_map[vname] = val
                vars_labels[vname] = label
            return f"${{vars.{vname}}}"

        if not isinstance(val, str) or not val:
            return val
        # 日期 → ${today}
        if _RX_DATE.match(val):
            vars_map["_date_replaced"] = True
            return "${today}"
        # 日期时间 → 粗暴也换 today（时分秒不重要）
        if _RX_DATETIME.match(val):
            vars_map["_date_replaced"] = True
            return "${today}"

        key_class = _classify_key(key_hint)

        # 唯一标识字段：按分类抽 vars
        if key_class:
            # ⭐ 去重逻辑：同一轮内相同值可复用
            dedup_key = (val, save_round) if key_class in ("number", "name") else val
            if dedup_key in seen_values:
                ref = seen_values[dedup_key]
                if ref.startswith("$"):
                    return ref
                return f"${{vars.{ref}}}"

            if key_class == "number":
                vname = "test_number"
                if vname not in vars_map:
                    prefix = _extract_value_prefix(val)
                    rand_digits = max(4, min(6, 20 - len(prefix) - 1))
                    if len(prefix) + rand_digits > 20:
                        prefix = prefix[:20 - rand_digits]
                    vars_map[vname] = f"{prefix}${{rand:{rand_digits}}}"
                round_number_assigned[save_round] = vname
                seen_values[dedup_key] = vname
                return f"${{vars.{vname}}}"

            elif key_class == "name":
                vname = f"test_name"
                if vname not in vars_map:
                    cur_num = round_number_assigned.get(save_round, round_number_assigned.get(1))
                    if cur_num:
                        vars_map[vname] = f"自动化${{vars.{cur_num}}}"
                    else:
                        vars_map[vname] = f"自动化${{rand:4}}"
                seen_values[dedup_key] = vname
                return f"${{vars.{vname}}}"

            elif key_class == "phone":
                vname = "test_phone"
                if vname not in vars_map:
                    # 保留原始电话号码的前缀格式（如 +86-138...）
                    import re as _re
                    phone_m = _re.match(r'^(\+?\d{1,4}[-\s]?)?(\d{3})', val)
                    if phone_m:
                        prefix = (phone_m.group(1) or "") + phone_m.group(2)
                    else:
                        prefix = "+86-138"
                    vars_map[vname] = f"{prefix}${{rand:8}}"
                seen_values[val] = vname
                return f"${{vars.{vname}}}"

            elif key_class == "cert":
                vname = "test_cert_no"
                if vname not in vars_map:
                    vars_map[vname] = f"CERT${{rand:10}}"
                seen_values[val] = vname
                return f"${{vars.{vname}}}"

            elif key_class == "email":
                vname = "test_email"
                if vname not in vars_map:
                    local, at, domain = val.partition("@")
                    safe_local = re.sub(r"[^A-Za-z0-9._-]", "", local) or "qa"
                    safe_local = safe_local[:12]
                    safe_domain = domain or "example.com"
                    vars_map[vname] = f"{safe_local}${{rand:6}}@{safe_domain}"
                seen_values[val] = vname
                return f"${{vars.{vname}}}"

            elif key_class == "text":
                if val in seen_values:
                    ref = seen_values[val]
                    return f"${{vars.{ref}}}" if not ref.startswith("$") else ref
                vname = _text_var_name(key_hint)
                if vname not in vars_map:
                    vars_map[vname] = val
                    label = _resolve_field_label(key_hint, entity_id=current_form_id, meta_resolver=meta_resolver)
                    if label and label != key_hint:
                        vars_labels[vname] = label
                seen_values[val] = vname
                return f"${{vars.{vname}}}"

            else:
                vname = f"test_{key_hint}"
                vars_map[vname] = val
                seen_values[val] = vname
                return f"${{vars.{vname}}}"

        # 测试编号模式（非 UNIQUE_KEY_HINTS 但值看起来像编号）
        if _RX_TEST_NUMBER.match(val):
            if val not in seen_values:
                vname = "test_number"
                prefix_m = re.match(r"^([A-Z]{2,5})\d+$", val)
                prefix = prefix_m.group(1) if prefix_m else "TEST"
                digit_count = len(val) - len(prefix)
                if len(prefix) + digit_count > 20:
                    digit_count = max(4, 20 - len(prefix))
                if vname not in vars_map:
                    vars_map[vname] = f"{prefix}${{rand:{digit_count}}}"
                seen_values[val] = vname
                return f"${{vars.{vname}}}"
            return f"${{vars.{seen_values[val]}}}"
        return val

    def walk_update_fields(postData: list):
        if not (isinstance(postData, list) and len(postData) >= 2):
            return
        entries = postData[1]
        if not isinstance(entries, list):
            return
        for e in entries:
            if not isinstance(e, dict):
                continue
            v = e.get("v")
            k = e.get("k", "")
            if isinstance(v, dict) and "zh_CN" in v:
                # 多语言：替换 zh_CN 本身
                new_zh = maybe_var(v.get("zh_CN"), k)
                if new_zh != v.get("zh_CN"):
                    v = dict(v)
                    v["zh_CN"] = new_zh
                    if "GLang" in v:
                        v["GLang"] = new_zh
                    e["v"] = v
            elif isinstance(v, str):
                new_v = maybe_var(v, k)
                if new_v != v:
                    e["v"] = new_v

    def rewrite_dirty_entry(entry: dict) -> None:
        """变量化 save/click/newentry post_data 中携带的脏字段值。"""
        fk = entry.get("k", "")
        fv = entry.get("v")
        if isinstance(fv, dict) and "zh_CN" in fv:
            new_zh = maybe_var(fv.get("zh_CN"), fk)
            if new_zh != fv.get("zh_CN"):
                fv = dict(fv)
                fv["zh_CN"] = new_zh
                if "GLang" in fv:
                    fv["GLang"] = new_zh
                if "zh_TW" in fv:
                    fv["zh_TW"] = new_zh
                entry["v"] = fv
        elif isinstance(fv, str):
            new_v = maybe_var(fv, fk)
            if new_v != fv:
                entry["v"] = new_v

    # 在原地修改，同时追踪 save 轮次
    for action_wrap in actions_seq:
        # ⭐ 更新当前 form_id，供 _classify_key 走 kb 查询
        current_form_id = action_wrap.get("form_id", "") or ""
        # ⭐ 规则7：遇到 keep_page save 步骤时，先处理其 post_data 中嵌入的脏字段值
        # （用户在编辑某字段时直接点保存，字段值仅出现在 save 的 post_data 中）
        ac = action_wrap.get("ac", "")
        method = action_wrap.get("method", "")
        
        # ⭐ 新增：处理 click 步骤的 post_data（用户编辑后直接点保存按钮的场景）
        # click btnsave 的 post_data 格式和 save 一样：[context, entries]
        # entries 包含 {"k": "number", "v": "xxx", "r": -1} 格式的字段值
        if ac == "click" or method == "click":
            pd = action_wrap.get("post_data") or [{}, []]
            if isinstance(pd, list) and len(pd) >= 2 and isinstance(pd[1], list):
                for entry in pd[1]:
                    if isinstance(entry, dict):
                        rewrite_dirty_entry(entry)

        if ac == "changeYear" or method == "changeYear":
            pd = action_wrap.get("post_data") or [{}, []]
            if isinstance(pd, list) and len(pd) >= 2 and isinstance(pd[1], list):
                for entry in pd[1]:
                    if isinstance(entry, dict):
                        rewrite_dirty_entry(entry)
        
        if ac in _SAVE_ACS:
            pd = action_wrap.get("post_data") or [{}, []]
            if isinstance(pd, list) and len(pd) >= 2 and isinstance(pd[1], list):
                for entry in pd[1]:
                    if isinstance(entry, dict):
                        rewrite_dirty_entry(entry)
            # save 步骤处理完脏字段后，推进轮次
            if action_wrap.get("keep_page"):
                save_round += 1

        if action_wrap.get("type") == "invoke" and action_wrap.get("method") == "updateValue":
            walk_update_fields(action_wrap.get("post_data") or [])
        # ⭐ 规则9：处理 newentry（新增条目行）步骤的 post_data
        # 业务模型的基础资料附表等场景中，新增条目行时 name 等字段值嵌入在 newentry 的 post_data 中
        elif ac == "newentry":
            pd = action_wrap.get("post_data") or [{}, []]
            if isinstance(pd, list) and len(pd) >= 2 and isinstance(pd[1], list):
                for entry in pd[1]:
                    if isinstance(entry, dict):
                        rewrite_dirty_entry(entry)
        # ⭐ 规则5补充：也处理 merge 后的 update_fields 类型
        elif action_wrap.get("type") == "update_fields":
            fields = action_wrap.get("fields")
            if isinstance(fields, dict):
                for k, v in list(fields.items()):
                    if isinstance(v, dict) and "zh_CN" in v:
                        new_zh = maybe_var(v.get("zh_CN"), k)
                        if new_zh != v.get("zh_CN"):
                            v = dict(v)
                            v["zh_CN"] = new_zh
                            if "GLang" in v:
                                v["GLang"] = new_zh
                            if "zh_TW" in v:
                                v["zh_TW"] = new_zh
                            fields[k] = v
                    elif isinstance(v, str):
                        new_v = maybe_var(v, k)
                        if new_v != v:
                            fields[k] = new_v
        
        # ⭐ 新增：处理 pick_basedata 的 value_id（环境相关的基础资料选择）
        elif action_wrap.get("type") == "pick_basedata":
            field_key = action_wrap.get("field_key", "")
            value_id = action_wrap.get("value_id")

            # ⭐ 策略变更（2026-05-08）：kb B 档不再"一票否决 continue 跳过"
            #   原因：B 档表示"枚举/基础资料"，但用户仍需在「变量配置」面板看到
            #   并可选择启用/禁用。过去 continue 导致 changescene/adminorgtype 等
            #   pick 完全漏识别，见 preview_1778231110_新增一条行政组织.har 的
            #   2 个 pick 全被吞。
            #   新策略：kb 分类仅用于优先级提示，不再终止变量化；所有 pick 一律
            #   生成 pick_<field_key>_id 变量。
            _pick_form_id = action_wrap.get("form_id", "") or current_form_id
            _kb_hint = None  # 仅作日志/调试信息，不再作为门禁
            if _kb is not None and _pick_form_id:
                try:
                    _kb_hint = _kb.classify_field(_pick_form_id, field_key)
                except Exception:
                    pass

            # 判断是否需要变量化（环境相关的基础资料）
            # 需要变量化的field_key：企业、组织、职位等环境特定数据
            ENV_RELATED_FIELDS = {
                "ba_e_enterprise": "企业",
                "ba_po_adminorg": "行政组织",
                "ba_po_position": "职位",
                "ba_org": "组织",
                "ba_dept": "部门",
                "ba_company": "公司",
                "enterprise": "企业",
                "adminorg": "行政组织",
                "position": "职位",
                "org": "组织",
                "dept": "部门",
            }

            # 不需要变量化的field_key（系统枚举值）—— 保留兼容但不再用作"禁止"
            # 现改为"仅提示 label"，这些字段的 value_id 也会变量化，
            # 用户可在 UI 禁用不需要的变量
            ENUM_FIELDS = {
                "gender": "性别",
                "certificatetype": "证件类型",
                "ba_e_laborrelstatus": "用工状态",
                "laborreltypecls": "用工关系分类",
                "status": "状态",
                "type": "类型",
            }

            if not value_id:
                pass  # 空 value 不处理
            elif field_key in ENV_RELATED_FIELDS:
                # ① 环境相关白名单：命名沿用 <field_key>_id 约定（和旧用例兼容）
                vname = f"{field_key}_id"
                if vname not in vars_map:
                    vars_map[vname] = str(value_id)
                    vars_labels[vname] = ENV_RELATED_FIELDS[field_key]
                action_wrap["value_id"] = f"${{vars.{vname}}}"
            else:
                # ② 兜底：ENUM_FIELDS / kb-B 档 / 未识别 —— 一律变量化
                # 命名规则：pick_<field_key>_id
                # label 优先级：ENUM_FIELDS > _FIELD_LABELS > field_key 原样
                _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
                if _sanitized:
                    vname = f"pick_{_sanitized}_id"
                    if vname not in vars_map:
                        vars_map[vname] = str(value_id)
                        # 优先从 ENUM_FIELDS 兼容映射取（系统枚举中文），
                        # 否则通过知识库/_FIELD_LABELS 通用字段映射取，
                        # 再否则用 field_key 本身作为 label
                        vars_labels[vname] = (
                            ENUM_FIELDS.get(field_key)
                            or _resolve_field_label(field_key)
                        )
                    action_wrap["value_id"] = f"${{vars.{vname}}}"


    # 生成变量标签（基于字段名和变量类型）
    def _generate_var_label(vname: str, key_hint: str) -> str:
        """根据变量名和字段上下文生成中文标签"""
        # 优先使用字段标签映射
        label = _resolve_field_label(key_hint)
        if label and label != key_hint:
            return label
        
        # 根据变量名推断
        vn = vname.lower()
        if 'name' in vn:
            return '名称'
        if 'number' in vn or 'code' in vn:
            return '编号'
        if 'cert' in vn:
            return '证件号'
        if 'phone' in vn or 'mobile' in vn:
            return '手机号'
        if 'email' in vn:
            return '邮箱'
        if 'date' in vn:
            return '日期'
        if 'id' in vn:
            return 'ID'
        
        # 根据 key_hint 推断
        kl = key_hint.lower()
        if 'name' in kl:
            return '名称'
        if 'number' in kl or 'code' in kl:
            return '编号'
        if 'cert' in kl:
            return '证件号'
        if 'phone' in kl or 'mobile' in kl:
            return '手机号'
        if 'emp' in kl:
            return '员工信息'
        
        return ''
    
    # 为每个变量生成标签
    for vname in vars_map:
        if not vname.startswith('_'):
            # 如果已经有标签，跳过（保留pick_basedata等处理时设置的标签）
            if vname in vars_labels and vars_labels[vname]:
                continue
            # 从seen_values反推key_hint
            key_hint = ''
            for val, name in seen_values.items():
                if name == vname:
                    # 尝试从值推断
                    break
            vars_labels[vname] = _generate_var_label(vname, key_hint)

    return actions_seq, vars_map, vars_labels


# ---------- 步骤提取 ----------

def extract_steps(har: dict) -> list[dict]:
    steps: list[dict] = []
    counter = 0
    pending_lookup: dict[tuple[str, str, str], dict[str, Any]] = {}
    for i, entry in enumerate(har.get("log", {}).get("entries", [])):
        req = entry.get("request", {})
        url = req.get("url", "")
        if not is_business_request(url):
            continue

        parsed = urllib.parse.urlparse(url)
        path = parsed.path
        qs = urllib.parse.parse_qs(parsed.query)
        body_text = (req.get("postData") or {}).get("text", "") or ""
        body_params = urllib.parse.parse_qs(body_text)
        form_id = qs.get("f", [""])[0]
        app_id = qs.get("appId", [""])[0]
        ac = qs.get("ac", [""])[0]

        if "invokeAction" in path and ac == "getLookUpList":
            params_raw = body_params.get("params", [""])[0]
            req_page_id = body_params.get("pageId", [""])[0]
            try:
                actions = json.loads(params_raw) if params_raw else []
            except Exception:
                actions = []
            for action in actions:
                if not isinstance(action, dict):
                    continue
                key = str(action.get("key") or "")
                if not key:
                    continue
                pending_lookup[(form_id, app_id, key)] = {
                    "args": action.get("args") or [["%", "", "%", 0, 20, 0]],
                    "_har_index": i,
                    "_har_page_id": req_page_id,
                }
            continue

        if "batchInvokeAction" in path and ac:
            params_raw = body_params.get("params", [""])[0]
            req_page_id = body_params.get("pageId", [""])[0]
            try:
                actions = json.loads(params_raw) if params_raw else []
            except Exception:
                actions = []
            # ⭐ 捕获响应体（用于提取 setItemByIdFromClient 的 value_name）
            _resp_text = (entry.get("response") or {}).get("content", {}).get("text", "") or ""
            for action in actions:
                if not isinstance(action, dict):
                    continue
                counter += 1
                action["_form_id"] = form_id
                name = smart_name(action, ac, counter)
                tier = AC_TIER.get(ac, "ui_reaction")
                # ⭐ 规则6补充：toolbar 按钮点击一律视为 core
                ctrl_key = action.get("key", "")
                # ⭐ 规则6补充：btnsave 类按钮 → 视为 core（HAR 可能把保存录成 click）
                if ac == "click" and ctrl_key in (_SAVE_BUTTON_KEYS | _CORE_CLICK_KEYS):
                    tier = "core"
                elif tier != "core" and ctrl_key in _CORE_TOOLBAR_KEYS:
                    tier = "core"
                step_dict: dict[str, Any] = {
                    "_har_index": i,
                    "type": "invoke",
                    "id": name,
                    "form_id": form_id,
                    "app_id": app_id,
                    "ac": ac,
                    "key": action.get("key", ""),
                    "method": action.get("methodName", ""),
                    "args": action.get("args", []),
                    "post_data": action.get("postData", [{}, []]),
                    "_har_page_id": req_page_id,   # HAR 原始 pageId
                    "_tier": tier,
                }
                if action.get("methodName") == "setItemByIdFromClient" and action.get("key"):
                    lookup = pending_lookup.get((form_id, app_id, str(action.get("key") or "")))
                    if lookup:
                        step_dict["_prefetch_lookup"] = True
                        step_dict["_prefetch_lookup_args"] = lookup.get("args") or [["%", "", "%", 0, 20, 0]]
                if _is_l2_page_id(req_page_id):
                    step_dict["preserve_l2_page"] = True
                # 保留有限响应体：setItem 用于提取 value_name；通知用于识别录制期业务校验点。
                # 响应体不输出到 YAML，仅在本次解析内消费。
                if _resp_text and (
                    action.get("methodName") == "setItemByIdFromClient"
                    or "ShowNotificationMsg" in _resp_text
                    or "showErrMsg" in _resp_text
                ):
                    step_dict["_resp_text"] = _resp_text
                steps.append(step_dict)
        elif "getConfig" in path:
            params_raw = qs.get("params", [""])[0]
            try:
                params = json.loads(params_raw)
            except Exception:
                params = {}
            fid = params.get("formId", form_id)
            if fid and fid != "home_page":
                counter += 1
                steps.append({
                    "_har_index": i,
                    "type": "open_form",
                    "id": f"open_{_form_short(fid) or counter}",
                    "form_id": fid,
                    "app_id": app_id or "bos",
                    "_tier": "core",
                })
    return steps


def dedup_open_forms(steps: list[dict]) -> list[dict]:
    """去掉重复打开同一 form_id 的 open_form，保留最后一次出现的位置。

    ⭐ 规则9（open_form 去重保留最后一次）：
    HAR 中同一表单可能被多次 dispatchFormLoad（如页面预加载 + 真正使用前的加载）。
    早期的 open_form 拿到的 pageId 在后续导航后会失效，只有最后一次（靠近实际
    使用点）拿到的 pageId 才有效。因此去重时保留最后一次。
    """
    # 先找到每个 form_id 最后出现的位置
    last_idx: dict[str, int] = {}
    for i, s in enumerate(steps):
        if s.get("type") == "open_form":
            fid = s.get("form_id")
            if fid:
                last_idx[fid] = i

    out: list[dict] = []
    for i, s in enumerate(steps):
        if s.get("type") == "open_form":
            fid = s.get("form_id")
            # 只保留该 form_id 最后一次出现的 open_form
            if fid and last_idx.get(fid) != i:
                continue
        out.append(s)
    return out


def relocate_premature_open_forms(steps: list[dict]) -> list[dict]:
    """⭐ 规则10：把过早的 open_form 挪到该表单第一次真正被使用的位置前。

    HAR 中 dispatchFormLoad 可能出现在 HAR 最前面（浏览器预加载/缓存），
    但真正使用（invoke/loadData/update_fields）发生在导航完成后。如果 open_form
    和第一次使用之间隔着其他表单的导航步骤，说明 open_form 过早了——此时拿到的
    pageId 在导航后已失效，必须推迟到使用前再打开。

    判定规则：
    - open_form 后紧跟的步骤就是同一个 form_id → 位置正确，不动
    - open_form 和同 form_id 的第一次 invoke 之间隔了别的表单操作 → 挪到那个 invoke 前
    """
    # 1) 找每个 open_form 的位置
    open_form_info: dict[str, tuple[int, dict]] = {}  # form_id → (index, step)
    for i, s in enumerate(steps):
        if s.get("type") == "open_form":
            fid = s.get("form_id")
            if fid:
                open_form_info[fid] = (i, s)

    # 2) 找每个 form_id 第一次被 invoke/update_fields/pick_basedata/loadData 使用的位置
    USAGE_TYPES = {"invoke", "update_fields", "pick_basedata"}
    first_use: dict[str, int] = {}
    for i, s in enumerate(steps):
        fid = s.get("form_id")
        if fid and s.get("type") in USAGE_TYPES and fid not in first_use:
            first_use[fid] = i

    # 3) 判定哪些 open_form 需要挪
    to_relocate: dict[int, tuple[str, dict, int]] = {}  # orig_idx → (form_id, step, target_idx)
    for fid, (oidx, ostep) in open_form_info.items():
        use_idx = first_use.get(fid)
        if use_idx is None:
            continue  # 没有使用，不管
        if use_idx <= oidx + 1:
            continue  # open_form 紧跟使用，位置正确
        # 检查中间是否有其他表单的操作（如果都是同 form_id 就不算"过早"）
        has_other_form = False
        for j in range(oidx + 1, use_idx):
            if steps[j].get("form_id") != fid:
                has_other_form = True
                break
        if has_other_form:
            to_relocate[oidx] = (fid, ostep, use_idx)

    if not to_relocate:
        return steps

    # 4) 构建新步骤列表
    out: list[dict] = []
    skip_indices = set(to_relocate.keys())
    # 按 target_idx 分组，多个 open_form 可能要插到同一个位置前
    insert_before: dict[int, list[dict]] = {}
    for orig_idx, (fid, ostep, target_idx) in to_relocate.items():
        insert_before.setdefault(target_idx, []).append(ostep)

    for i, s in enumerate(steps):
        if i in skip_indices:
            continue
        if i in insert_before:
            for ins_step in insert_before[i]:
                out.append(ins_step)
        out.append(s)
    return out


def merge_consecutive_update_values(steps: list[dict]) -> list[dict]:
    """把连续的 updateValue（每次一个字段）合并成一条 update_fields。

    ⭐ 规则11（entry 行号传递）：
    HAR 中 entry 字段的 updateValue postData 带 "r" 行号。
    合并时提取 row_index，写入 update_fields 步骤，runner 发送时携带正确行号。
    """
    out: list[dict] = []
    i = 0
    while i < len(steps):
        s = steps[i]
        is_update = (s.get("type") == "invoke" and s.get("method") == "updateValue"
                     and s.get("key") == "")
        if not is_update:
            out.append(s)
            i += 1
            continue

        # 收集连续的 updateValue
        group: list[dict] = [s]
        j = i + 1
        while j < len(steps):
            nxt = steps[j]
            if (nxt.get("type") == "invoke" and nxt.get("method") == "updateValue"
                    and nxt.get("key") == ""
                    and nxt.get("form_id") == s.get("form_id")):
                group.append(nxt)
                j += 1
            else:
                break

        if len(group) >= 2:
            # 合并字段
            merged_fields: dict[str, Any] = {}
            row_idx = -1
            for g in group:
                pd = g.get("post_data") or []
                merged_fields.update(_extract_update_fields(pd))
                # 取第一个有效的 row_index（同组通常同行）
                if row_idx < 0:
                    row_idx = _extract_row_index(pd)
            # 生成合并后的 step（type 改为 update_fields）
            primary_key = list(merged_fields.keys())[0] if merged_fields else "fields"
            merged_step: dict[str, Any] = {
                "type": "update_fields",
                "id": f"fill_{primary_key}_etc" if len(merged_fields) > 1 else f"fill_{primary_key}",
                "form_id": s["form_id"],
                "app_id": s["app_id"],
                "fields": merged_fields,
                "_tier": "core",
                "_har_index": s.get("_har_index"),
                "_har_page_id": s.get("_har_page_id", ""),  # 保留 pageId 供 keep_page 检测
            }
            if row_idx >= 0:
                merged_step["row_index"] = row_idx
            out.append(merged_step)
            i = j
        else:
            # 单个 updateValue 也转成 update_fields 形式
            pd = s.get("post_data") or []
            merged_fields = _extract_update_fields(pd)
            if merged_fields:
                primary_key = list(merged_fields.keys())[0]
                single_step: dict[str, Any] = {
                    "type": "update_fields",
                    "id": f"fill_{primary_key}",
                    "form_id": s["form_id"],
                    "app_id": s["app_id"],
                    "fields": merged_fields,
                    "_tier": "core",
                    "_har_index": s.get("_har_index"),
                    "_har_page_id": s.get("_har_page_id", ""),
                }
                row_idx = _extract_row_index(pd)
                if row_idx >= 0:
                    single_step["row_index"] = row_idx
                out.append(single_step)
            else:
                out.append(s)
            i += 1
    return out


def lower_set_item_to_pick_basedata(steps: list[dict]) -> list[dict]:
    """把 invoke setItemByIdFromClient 降级成 pick_basedata 语义。
    ⭐ 规则改进：同时提取 postData 中的多语言字段（如 name）生成 update_fields 步骤，
    避免字段丢失。"""
    out: list[dict] = []
    pending_lookup: dict[tuple[str, str, str], dict[str, Any]] = {}
    for s in steps:
        if (
            s.get("type") == "invoke"
            and (s.get("method") == "getLookUpList" or s.get("ac") == "getLookUpList")
            and s.get("key")
        ):
            pending_lookup[(
                str(s.get("form_id") or ""),
                str(s.get("app_id") or ""),
                str(s.get("key") or ""),
            )] = {
                "args": s.get("args") or [["%", "", "%", 0, 20, 0]],
                "_har_index": s.get("_har_index"),
                "_har_page_id": s.get("_har_page_id", ""),
            }
            continue
        if (s.get("type") == "invoke" and s.get("method") == "setItemByIdFromClient"
                and s.get("args")):
            args = s["args"]
            # args 形如 [["id_str", 0]]
            value_id = ""
            if isinstance(args, list) and args and isinstance(args[0], list) and args[0]:
                value_id = str(args[0][0])

            # 从 postData 中提取多语言字段更新（如 name）
            extra_fields: dict[str, Any] = {}
            extra_row_idx = -1
            post_data = s.get("post_data", [{}, []])
            if isinstance(post_data, list) and len(post_data) >= 2:
                entries = post_data[1]
                if isinstance(entries, list):
                    for e in entries:
                        if not isinstance(e, dict):
                            continue
                        k = e.get("k", "")
                        v = e.get("v")
                        # 提取 entry 行号
                        if extra_row_idx < 0 and "r" in e:
                            r = e["r"]
                            if isinstance(r, int) and r >= 0:
                                extra_row_idx = r
                        # 跳过 pick_basedata 本身的字段
                        if k == s.get("key"):
                            continue
                        # 收集多语言或普通字段
                        if isinstance(v, dict) and "zh_CN" in v:
                            extra_fields[k] = v
                        elif isinstance(v, str):
                            extra_fields[k] = v

            # 如果有额外字段，先生成 update_fields 步骤
            if extra_fields:
                primary_key = list(extra_fields.keys())[0]
                fill_step: dict[str, Any] = {
                    "type": "update_fields",
                    "id": f"fill_{_sanitize(primary_key) or 'fields'}",
                    "form_id": s["form_id"],
                    "app_id": s["app_id"],
                    "fields": extra_fields,
                    "_tier": "core",
                    "_har_index": s.get("_har_index"),
                    "_har_page_id": s.get("_har_page_id", ""),
                }
                if extra_row_idx >= 0:
                    fill_step["row_index"] = extra_row_idx
                out.append(fill_step)

            if value_id:
                # ⭐ 从响应体提取业务编码/名称（响应中 "u" action 的 v 数组）
                value_code = ""
                value_name = ""
                value_number = ""
                _resp_text = s.get("_resp_text", "")
                if _resp_text:
                    try:
                        resp_json = json.loads(_resp_text)
                        if isinstance(resp_json, list):
                            field_key = s.get("key", "")
                            for rj in resp_json:
                                if isinstance(rj, dict) and rj.get("a") == "u":
                                    for p_item in (rj.get("p") or []):
                                        if (isinstance(p_item, dict)
                                                and p_item.get("k") == field_key):
                                            v_arr = p_item.get("v")
                                            parts = _extract_basedata_parts(v_arr)
                                            if parts:
                                                value_code = parts.get("value_code", "")
                                                value_name = parts.get("value_name", "")
                                                value_number = parts.get("value_number", "")
                                            break
                                    if value_name:
                                        break
                    except Exception:
                        pass
                pick_step = {
                    "type": "pick_basedata",
                    "id": s["id"],
                    "form_id": s["form_id"],
                    "app_id": s["app_id"],
                    "field_key": s["key"],
                    "value_id": value_id,
                    "value_name": value_name,
                    "_tier": "core",
                    "_har_index": s.get("_har_index"),
                    "_har_page_id": s.get("_har_page_id", ""),
                }
                lookup = pending_lookup.get((
                    str(s.get("form_id") or ""),
                    str(s.get("app_id") or ""),
                    str(s.get("key") or ""),
                ))
                if s.get("_prefetch_lookup"):
                    pick_step["prefetch_lookup"] = True
                    pick_step["prefetch_lookup_args"] = s.get("_prefetch_lookup_args") or [["%", "", "%", 0, 20, 0]]
                elif lookup:
                    pick_step["prefetch_lookup"] = True
                    pick_step["prefetch_lookup_args"] = lookup.get("args") or [["%", "", "%", 0, 20, 0]]
                if value_code:
                    pick_step["value_code"] = value_code
                if value_number:
                    pick_step["value_number"] = value_number
                out.append(pick_step)
                continue
        out.append(s)
    return out


# ---------- ⭐ 规则2：session pageId 动态化 ----------

def _infer_root_base_id(steps: list[dict]) -> str:
    """从 HAR 步骤的 _har_page_id 推断原始会话的 root_base_id（32位hex）。
    策略：找首个 `root{32hex}` 格式的 pageId，抽出 base_id。"""
    for s in steps:
        pid = s.get("_har_page_id", "")
        m = re.match(r"^root([a-f0-9]{32})$", pid)
        if m:
            return m.group(1)
        # 也检查含 root{32hex} 的复合 pageId
        m2 = _RX_ROOT_BASE_ID.search(pid)
        if m2:
            return m2.group(1)
    return ""


def dynamize_session_pageids(steps: list[dict]) -> list[dict]:
    """将 selectTab 等步骤 args 中硬编码的 session pageId 替换为 ${session.root_base_id}。
    原理：苍穹 L2 pageId 格式为 {prefix}root{32hex}，其中 32hex 是当前会话的
    root_base_id，每次登录不同。不动态化会导致操作发到错误的 page 上下文。
    """
    base_id = _infer_root_base_id(steps)
    if not base_id:
        return steps  # 无法推断，不做处理

    out = []
    for s in steps:
        s = dict(s)  # shallow copy
        ac = s.get("ac", "")
        args = s.get("args")

        if ac == "selectTab" and isinstance(args, list):
            new_args = []
            for arg in args:
                if isinstance(arg, str) and base_id in arg:
                    # {prefix}root{base_id} → {prefix}root${session.root_base_id}
                    new_arg = arg.replace(base_id, "${session.root_base_id}")
                    new_args.append(new_arg)
                elif isinstance(arg, str) and _RX_RANDOM_PAGE_ID.match(arg):
                    # 纯随机 32hex pageId（如子 tab 的临时 id），无法动态化 → 标 optional
                    new_args.append(arg)
                    s["optional"] = True
                else:
                    new_args.append(arg)
            s["args"] = new_args

        # treeMenuClick 的 _har_page_id 也可能含 base_id，但 args 通常是菜单 id（纯数字）
        # 不需要替换 args，只要确保 form_id 的 pageId 正确（由 selectTab 动态化保证）

        out.append(s)
    return out


# ---------- ⭐ 规则3：saveandeffect 后检测 keep_page ----------

def detect_keep_page(steps: list[dict]) -> list[dict]:
    """当 HAR 显示 saveandeffect 后，同一 form_id + 同一 _har_page_id 继续被用于
    后续操作（updateValue / setItemByIdFromClient / saveandeffect）时，说明服务端
    保持表单 pageId 不变（"连续新增"模式）。此时为 save 步骤添加 keep_page: true，
    防止 runner 自动清除 pageId 导致后续操作变成 no-op。
    """
    save_acs = {"saveandeffect", "submitandeffect", "save", "submit"}
    continue_acs = {"updateValue", "setItemByIdFromClient", "saveandeffect",
                    "submitandeffect", "save", "submit"}

    out = list(steps)  # work on same list
    for i, s in enumerate(out):
        ac = s.get("ac", "")
        if ac not in save_acs:
            continue

        form_id = s.get("form_id", "")
        page_id = s.get("_har_page_id", "")
        if not form_id or not page_id:
            continue

        # 查看 save 后面的步骤是否继续使用同一 form+pageId
        for j in range(i + 1, min(i + 8, len(out))):
            nxt = out[j]
            nxt_form = nxt.get("form_id", "")
            nxt_pid = nxt.get("_har_page_id", "")
            nxt_ac = nxt.get("ac", "")
            nxt_method = nxt.get("method", "")

            if nxt_form == form_id and nxt_pid == page_id:
                if nxt_ac in continue_acs or nxt_method in ("updateValue", "setItemByIdFromClient"):
                    # 确认是连续新增模式 → 标记 keep_page
                    out[i] = dict(out[i])
                    out[i]["keep_page"] = True
                    break
            elif nxt_form != form_id:
                # 不同表单的操作，可以跳过（如 postExpandNodes 树节点刷新）
                continue
            else:
                break

    return out


def infer_main_form(steps: list[dict]) -> str:
    freq: dict[str, int] = {}
    for s in steps:
        fid = s.get("form_id", "")
        if fid and not fid.startswith(("home_page", "bos_portal")):
            freq[fid] = freq.get(fid, 0) + 1
    return max(freq, key=freq.get) if freq else ""


def _find_context_bridge_start(steps: list[dict], main_form: str) -> int | None:
    """在无 portal/menu 导航时，保留 main_form 前的列表/树上下文桥接步骤。

    典型模式：
    - basedatalist refresh -> entryRowClick -> hyperLinkClick -> main_form loadData
    - apphome treeMenuClick -> main_form loadData

    如果这里直接裁到 main_form，会丢失“从哪个列表/树节点进入”的上下文，导致
    新增时默认组织、树焦点等服务端隐式上下文缺失。
    """
    if not main_form:
        return None

    first_main_idx = next(
        (i for i, s in enumerate(steps) if s.get("form_id") == main_form),
        None,
    )
    if first_main_idx is None:
        return None
    if first_main_idx == 0:
        return 0

    start = first_main_idx
    for j in range(first_main_idx - 1, -1, -1):
        prev = steps[j]
        prev_form = prev.get("form_id", "") or ""

        if prev_form.startswith(("home_page", "bos_portal", "portal_", "gpt_")):
            break

        if _is_context_bridge_step(prev):
            start = j
            continue
        break

    return start


def _has_context_bridge_into_main_form(steps: list[dict], main_form: str) -> bool:
    """判断 main_form 是否由前一步列表/树导航自然带出。

    若是这种场景，再静态插入 open_form(main_form) 会拿到脱离上下文的新 pageId，
    反而破坏原始 HAR 的进入链路。
    """
    if not main_form:
        return False

    for i, step in enumerate(steps):
        if step.get("form_id") != main_form:
            continue
        if i == 0:
            return False
        prev = steps[i - 1]
        if prev.get("form_id") == main_form:
            return False
        if prev.get("type") != "invoke":
            return False

        prev_ac = prev.get("ac", "")
        prev_method = prev.get("method", "")
        prev_key = str(prev.get("key") or "").lower()
        if (
            main_form == "hpdi_bizdatabillnewentry"
            and prev_ac == "click"
            and prev_key in _CONTEXT_BRIDGE_CLICK_KEYS
        ):
            return True
        if prev_ac in {"postExpandNodes", "commonSearch"}:
            return True
        if prev_ac == "entryRowClick" and prev_method in (
            "hyperLinkClick",
            "entryRowClick",
            "entryRowDoubleClick",
        ):
            return True
        return False

    return False


def _extract_treeview_focus(step: dict) -> tuple[str, str]:
    """从 addnew/new 请求中提取树焦点上下文。"""
    post_data = step.get("post_data")
    containers = post_data if isinstance(post_data, list) else [post_data]
    for item in containers:
        if not isinstance(item, dict):
            continue
        treeview = item.get("treeview")
        if not isinstance(treeview, dict):
            continue
        focus = treeview.get("focus")
        if not isinstance(focus, dict):
            continue
        value_id = str(focus.get("id") or "").strip()
        value_name = str(focus.get("text") or focus.get("name") or "").strip()
        if value_id or value_name:
            return value_id, value_name
    return "", ""


def _extract_entry_row_selector(step: dict) -> dict[str, Any] | None:
    """从 F7 entryRowClick 中提取用户选择的对象。

    有些选择器不是 setItemByIdFromClient，而是在列表弹窗里 entryRowClick
    一行后再点确定。若不暴露这个 selDatas，用户无法在预览页维护“选择人”。
    """
    if step.get("type") != "invoke" or step.get("ac") != "entryRowClick":
        return None
    form_id = str(step.get("form_id") or "")
    selector_key, label, field_key = _F7_SELECTOR_FORM_LABELS.get(form_id, ("", "", ""))
    if not selector_key:
        return None

    post_data = step.get("post_data")
    if not (isinstance(post_data, list) and post_data and isinstance(post_data[0], dict)):
        return None

    for control_key, payload in post_data[0].items():
        if not isinstance(payload, dict):
            continue
        rows = payload.get("selDatas")
        if not (isinstance(rows, list) and rows and isinstance(rows[0], list)):
            continue
        row = rows[0]
        value_id = str(row[0] or "") if row else ""
        if not value_id:
            continue
        code_index = next((
            idx for idx, cell in enumerate(row)
            if idx > 0
            and isinstance(cell, str)
            and cell
            and not _looks_like_internal_id(cell)
            and cell not in {"0", "1"}
        ), -1)
        value_code = str(row[code_index]) if code_index >= 0 else ""
        return {
            "selector_key": selector_key,
            "field_key": field_key,
            "label": label,
            "value_id": value_id,
            "value_code": value_code,
            "value_name": value_code,
            "control_key": str(control_key),
            "value_index": 0,
            "code_index": code_index,
        }
    return None


def _extract_common_search_defaults(steps: list[dict], form_id: str) -> dict[str, str]:
    """从 commonSearch 参数中提取环境上下文默认值（如 useorg.id=100000）。"""
    found: dict[str, str] = {}
    for step in steps:
        if step.get("form_id") != form_id or step.get("ac") != "commonSearch":
            continue
        args = step.get("args") or []
        if not isinstance(args, list):
            continue
        for group in args:
            if not isinstance(group, list):
                continue
            for cond in group:
                if not isinstance(cond, dict):
                    continue
                fields = cond.get("FieldName") or []
                values = cond.get("Value") or []
                if not isinstance(fields, list) or not isinstance(values, list):
                    continue
                non_empty = next((str(v).strip() for v in values if str(v).strip()), "")
                if not non_empty:
                    continue
                for fname in fields:
                    fname = str(fname or "").strip().lower()
                    if fname and fname not in found:
                        found[fname] = non_empty
    return found


def _step_sets_field(step: dict, field_key: str) -> bool:
    """判断 step 是否已经显式设置过目标字段。"""
    field_key = field_key.lower()
    if step.get("type") == "pick_basedata":
        return str(step.get("field_key") or "").lower() == field_key
    if step.get("type") == "update_fields":
        fields = step.get("fields") or {}
        if isinstance(fields, dict):
            return any(str(k).lower() == field_key for k in fields)
    return False


def _find_context_insertion_pos(steps: list[dict], main_form: str, new_idx: int) -> int:
    """优先插到新增后的首个 loadData 之后，保证新页面已初始化。"""
    for idx in range(new_idx + 1, len(steps)):
        step = steps[idx]
        if (step.get("form_id") == main_form and step.get("type") == "invoke"
                and step.get("ac") == "loadData"):
            return idx + 1
    return new_idx + 1


def _mark_context_bridge_steps_required(steps: list[dict], main_form: str) -> None:
    """列表/树跳卡片前的桥接步骤不能是 optional。"""
    if not main_form:
        return

    first_main_idx = next(
        (i for i, s in enumerate(steps) if s.get("form_id") == main_form),
        None,
    )
    if first_main_idx is None or first_main_idx <= 0:
        return

    for idx in range(first_main_idx):
        step = steps[idx]
        if step.get("type") != "invoke":
            continue
        if step.get("ac") not in {"refresh", "entryRowClick"}:
            continue
        step.pop("optional", None)


def _is_navigation_form(form_id: str, main_form: str) -> bool:
    """判断表单是否属于非主业务链路的导航/装饰表单。"""
    if not form_id or form_id == main_form:
        return False
    if form_id.endswith("_apphome"):
        return True
    if form_id.startswith(("bos_card_", "gbs_bgtask")):
        return True
    return form_id in _NAVIGATION_FORM_IDS


def _mark_navigation_steps_optional(steps: list[dict], main_form: str) -> None:
    """将非主表单的导航/装饰步骤降级为 optional。

    这些步骤可能依赖浏览器端首页应用服务（如 homs_apphome），在 API 回放中缺失时
    不应阻断后续已具备独立入口的业务主表单执行；主表单自身永远不在此处降级。
    """
    if not main_form:
        return
    for step in steps:
        if _is_navigation_form(str(step.get("form_id") or ""), main_form):
            step["optional"] = True


def _drop_portal_side_effect_steps(steps: list[dict], main_form: str) -> list[dict]:
    """Remove browser portal cards that are not part of the business replay path."""
    if not main_form:
        return steps
    kept: list[dict] = []
    for step in steps:
        form_id = str(step.get("form_id") or "")
        if form_id and form_id != main_form and form_id in _PORTAL_SIDE_EFFECT_FORM_IDS:
            continue
        kept.append(step)
    return kept


def _pick_field_auto_resolve_meta(
    field_key: str,
    value_id: Any,
    value_name: Any,
    env_sensitive: str,
    field_type: str | None = None,
    value_code: Any = "",
) -> dict[str, Any]:
    """生成环境字段自动解析元信息。"""
    field_key_l = str(field_key or "").lower()
    value_id_s = str(value_id or "").strip()
    value_name_s = str(value_name or "").strip()
    value_code_s = str(value_code or "").strip()
    field_type_s = str(field_type or "")
    query_s = value_code_s or value_name_s

    is_basedata = field_type_s in ("BasedataProp", "MulBasedataProp", "OrgProp", "UserProp")
    looks_env = (
        env_sensitive in ("high", "medium")
        or field_key_l in _AUTO_RESOLVE_FIELD_HINTS
        or field_key_l.endswith(("org", "position", "entity", "city", "country"))
    )
    can_resolve = bool(
        query_s
        and value_id_s
        and (query_s != value_id_s or (value_code_s and not _looks_like_internal_id(value_id_s)))
        and not value_id_s.startswith("${")
        and not query_s.startswith("${")
        and (is_basedata or looks_env)
    )

    return {
        "auto_resolve": can_resolve,
        "resolve_by": "value_code" if can_resolve and value_code_s and field_key_l in _AUTO_RESOLVE_CODE_FIELD_HINTS else ("value_name" if can_resolve else ""),
        "resolve_status": "pending" if can_resolve else "manual",
    }


def _make_context_default_pick_field(
    field_key: str,
    parts: dict[str, str],
    *,
    main_form: str,
    app_id: str,
) -> OrderedDict | None:
    value_code = str(parts.get("value_code") or "").strip()
    value_name = str(parts.get("value_name") or "").strip()
    if not (value_code or value_name):
        return None
    label = _resolve_field_label(field_key, entity_id=main_form)
    return OrderedDict([
        ("value_id", value_code or value_name),
        ("value_name", value_name),
        ("value_code", value_code),
        ("value_number", str(parts.get("value_number") or "")),
        ("label", label),
        ("env_sensitive", "high"),
        ("field_key", field_key),
        ("form_id", main_form),
        ("app_id", app_id),
        ("auto_resolve", False),
        ("resolve_by", ""),
        ("resolve_status", "context"),
        ("context_only", True),
        ("readonly", False),
        ("source", "server_default"),
    ])


def _append_context_default_pick_fields(
    pick_fields_map: OrderedDict,
    observations: dict[str, Any],
    *,
    main_form: str,
    app_id: str,
) -> None:
    values = observations.get("response_values") or {}
    for field_key in sorted(_DEFAULT_CONTEXT_FIELD_KEYS):
        step_id = f"pick_{field_key}_id"
        if step_id in pick_fields_map:
            continue
        item = _make_context_default_pick_field(
            field_key,
            values.get(field_key) or {},
            main_form=main_form,
            app_id=app_id,
        )
        if item is not None:
            pick_fields_map[step_id] = item


def _scoped_pick_field_id(
    base_id: str,
    existing: dict,
    *,
    form_id: str = "",
    source_step_id: str = "",
) -> str:
    """Return a stable pick_fields key, adding scope only on real collisions."""
    if base_id not in existing:
        return base_id
    for key, prev in existing.items():
        if key != base_id and not str(key).startswith(f"{base_id}__"):
            continue
        if isinstance(prev, dict) and str(prev.get("form_id") or "") == str(form_id or ""):
            return ""
    raw_scope = str(source_step_id or form_id or len(existing) + 1)
    suffix = re.sub(r"[^a-zA-Z0-9_]", "_", raw_scope).strip("_") or str(len(existing) + 1)
    candidate = f"{base_id}__{suffix}"
    idx = 2
    while candidate in existing:
        candidate = f"{base_id}__{suffix}_{idx}"
        idx += 1
    return candidate


def _append_context_default_steps(
    steps: list[dict],
    observations: dict[str, Any],
    *,
    main_form: str,
    app_id: str,
    pick_field_overrides: dict | None = None,
) -> list[dict]:
    """用户修改服务端默认带出字段时，将其转为真实回放步骤。

    默认带出的字段只展示在环境字段面板；一旦导入预览里被用户手工修改，
    就补一条 pick_basedata，使“预览页改值 → 生成 YAML → 执行”真正生效。
    """
    if not main_form or not pick_field_overrides:
        return steps
    response_values = observations.get("response_values") or {}
    if not response_values:
        return steps
    out = list(steps)
    existing_fields = {
        str(step.get("field_key") or "").lower()
        for step in out
        if step.get("type") == "pick_basedata"
    }
    for step in out:
        if step.get("type") == "update_fields" and isinstance(step.get("fields"), dict):
            existing_fields.update(str(k).lower() for k in step["fields"])

    injected: list[dict[str, Any]] = []
    for field_key in _DEFAULT_CONTEXT_FIELD_KEYS:
        if field_key in existing_fields:
            continue
        parts = response_values.get(field_key)
        if not isinstance(parts, dict):
            continue
        pf_id = f"pick_{field_key}_id"
        override = pick_field_overrides.get(pf_id)
        if not isinstance(override, dict):
            continue
        original_value_id = str(parts.get("value_code") or parts.get("value_name") or "").strip()
        incoming_value_id = str(override.get("value_id", original_value_id) or "").strip()
        manual_override = bool(override.get("manual_override") or override.get("user_overridden"))
        if incoming_value_id != original_value_id and override.get("resolve_status") == "manual":
            manual_override = True
        if not manual_override or not incoming_value_id:
            continue
        injected.append({
            "type": "pick_basedata",
            "id": f"pick_{_sanitize(field_key) or field_key}_ctx",
            "form_id": main_form,
            "app_id": app_id,
            "field_key": field_key,
            "value_id": incoming_value_id,
            "value_name": str(override.get("value_name") or "").strip(),
            "value_code": str(override.get("value_code") or "").strip(),
            "_tier": "core",
            "_is_context_default": True,
        })

    if not injected:
        return steps

    insert_pos = next((
        idx for idx, step in enumerate(out)
        if step.get("form_id") == main_form and step.get("type") in ("update_fields", "pick_basedata")
    ), len(out))
    return out[:insert_pos] + injected + out[insert_pos:]


def _append_recorded_default_pick_steps(
    steps: list[dict],
    observations: dict[str, Any],
    *,
    main_form: str,
    app_id: str,
) -> list[dict]:
    """Replay required defaults that were visible in HAR loadData but never clicked.

    Some Kingdee forms prefill required basedata fields during addnew/loadData.
    Browser replay keeps them in the pageId model context, while API replay can
    lose them before a dependent pick/save. For known forms, convert those
    recorded defaults into explicit pick_basedata steps on the same form.
    """
    if not _RECORDED_DEFAULT_PICK_FIELDS_BY_FORM:
        return steps

    values_by_form = observations.get("response_values_by_form") or {}
    legacy_values = observations.get("response_values") or {}
    out = list(steps)

    def _form_values(form_id: str) -> dict[str, dict[str, str]]:
        scoped = values_by_form.get(form_id) or {}
        if scoped:
            return scoped
        # Backward compatibility for older observation payloads used by tests.
        return legacy_values if form_id == main_form else {}

    def _existing_pick_fields(form_id: str) -> set[str]:
        return {
            str(step.get("field_key") or "").lower()
            for step in out
            if step.get("form_id") == form_id
            and step.get("type") == "pick_basedata"
            and step.get("field_key")
        }

    def _needs_recorded_default_pick(form_id: str, field_key: str) -> bool:
        if form_id != "hpdi_bizdatabillchoicetpl":
            return True
        if str(field_key or "").lower() != "org":
            return True
        return any(
            step.get("form_id") == form_id
            and step.get("type") == "pick_basedata"
            and str(step.get("field_key") or "").lower() == "bizitemgroup"
            for step in out
        )

    def _first_business_input_pos(form_id: str) -> int:
        for idx, step in enumerate(out):
            if step.get("form_id") != form_id:
                continue
            if step.get("type") in ("update_fields", "pick_basedata"):
                return idx
        for idx, step in enumerate(out):
            if step.get("form_id") != form_id:
                continue
            if step.get("type") == "invoke" and step.get("ac") not in {"loadData", "getLookUpList"}:
                return idx
        return next((idx + 1 for idx, step in enumerate(out) if step.get("form_id") == form_id), len(out))

    def _write_anchor_pos() -> int:
        return next((idx for idx, step in enumerate(out) if _is_write_anchor_step(step)), len(out))

    injections_by_form: dict[str, list[dict[str, Any]]] = {}
    for form_id, default_fields in _RECORDED_DEFAULT_PICK_FIELDS_BY_FORM.items():
        if not any(step.get("form_id") == form_id for step in out):
            continue
        values = _form_values(form_id)
        if not values:
            continue
        existing_fields = _existing_pick_fields(form_id)
        form_app_id = next((str(step.get("app_id") or "") for step in out if step.get("form_id") == form_id), app_id)
        for field_key in default_fields:
            field_key_l = str(field_key or "").lower()
            if field_key_l in existing_fields:
                continue
            if not _needs_recorded_default_pick(form_id, field_key_l):
                continue
            parts = values.get(field_key_l)
            if not isinstance(parts, dict):
                continue
            value_code = str(parts.get("value_code") or "").strip()
            value_name = str(parts.get("value_name") or "").strip()
            value_number = str(parts.get("value_number") or "").strip()
            value_id = _recorded_default_value_id(field_key_l, value_code, value_name)
            if not value_id:
                continue
            injections_by_form.setdefault(form_id, []).append({
                "type": "pick_basedata",
                "id": f"pick_{_sanitize(field_key_l) or field_key_l}_ctx",
                "form_id": form_id,
                "app_id": form_app_id,
                "field_key": field_key_l,
                "value_id": value_id,
                "value_name": value_name,
                "value_code": value_code,
                "value_number": value_number,
                "_tier": "core",
                "_is_recorded_default": True,
            })

    if not injections_by_form:
        return steps

    # Keep the long-standing admin-org behavior: org must be replayed before
    # first input, while the remaining defaults are safest right before save.
    admin_injected = injections_by_form.pop("haos_adminorgdetail", [])
    if admin_injected:
        early_fields = {"org"} if main_form == "haos_adminorgdetail" else set()
        early = [
            step for step in admin_injected
            if str(step.get("field_key") or "").lower() in early_fields
        ]
        late = [
            step for step in admin_injected
            if str(step.get("field_key") or "").lower() not in early_fields
        ]
        if early:
            early_pos = _first_business_input_pos("haos_adminorgdetail")
            for offset, step in enumerate(early):
                out.insert(early_pos + offset, step)
        if late:
            insert_pos = _write_anchor_pos()
            for offset, step in enumerate(late):
                out.insert(insert_pos + offset, step)

    for form_id, injected in injections_by_form.items():
        insert_pos = _first_business_input_pos(form_id)
        for offset, step in enumerate(injected):
            out.insert(insert_pos + offset, step)
    return out


def _append_recorded_default_update_steps(
    steps: list[dict],
    observations: dict[str, Any],
    *,
    main_form: str,
    app_id: str,
) -> list[dict]:
    """Replay scalar server defaults captured from addnew/loadData responses.

    Browser-side pageId carries combo/boolean defaults such as ctrlstrategy.
    When API replay cannot reconstruct the exact menu shell, preserve behavior
    by writing those recorded defaults back into the form model before save.
    This is a model-context update, not a hard patch to save.post_data.
    """
    if not steps or not _RECORDED_DEFAULT_SCALAR_FIELDS_BY_FORM:
        return steps

    values_by_form = observations.get("response_values_by_form") or {}
    raw_values_by_form = observations.get("response_raw_values_by_form") or {}
    internal_ids_by_form = observations.get("response_internal_ids_by_form") or {}
    out = list(steps)
    injected_by_form: dict[str, dict[str, Any]] = {}

    for form_id, default_fields in _RECORDED_DEFAULT_SCALAR_FIELDS_BY_FORM.items():
        if not any(step.get("form_id") == form_id for step in out):
            continue
        values = values_by_form.get(form_id) or {}
        raw_values = raw_values_by_form.get(form_id) or {}
        internal_ids = internal_ids_by_form.get(form_id) or {}
        if not values:
            continue
        existing_fields = {
            str(field_key).lower()
            for step in out
            if step.get("form_id") == form_id and step.get("type") == "update_fields"
            for field_key in (step.get("fields") or {}).keys()
        }
        form_app_id = next((str(step.get("app_id") or "") for step in out if step.get("form_id") == form_id), app_id)
        fields: dict[str, Any] = {}
        for field_key in default_fields:
            field_key_l = str(field_key or "").lower()
            if field_key_l in existing_fields:
                continue
            parts = values.get(field_key_l)
            if not isinstance(parts, dict):
                continue
            if field_key_l in internal_ids:
                fields[field_key_l] = internal_ids[field_key_l]
                continue
            if field_key_l in raw_values:
                fields[field_key_l] = raw_values[field_key_l]
                continue
            value_code = str(parts.get("value_code") or "").strip()
            if value_code:
                fields[field_key_l] = value_code
        if fields:
            injected_by_form[form_id] = {
                "type": "update_fields",
                "id": f"fill_{_sanitize(form_id) or 'form'}_recorded_defaults",
                "form_id": form_id,
                "app_id": form_app_id,
                "fields": fields,
                "_tier": "core",
                "_is_recorded_default": True,
            }

    if not injected_by_form:
        return steps

    for form_id, injected in injected_by_form.items():
        insert_pos = _write_anchor_pos_for_form(out, form_id)
        out.insert(insert_pos, injected)
    return out


def _infer_context_field_modes(main_form: str, meta_resolver=None) -> dict[str, str]:
    """推断需要由上下文补偿的字段及其写入方式。"""
    modes: dict[str, str] = {}

    hinted = _CONTEXT_FIELD_HINTS.get(main_form, {})
    for field_key, mode in hinted.items():
        modes[str(field_key).lower()] = mode

    if not meta_resolver or not main_form:
        return modes

    fields = meta_resolver.get_entity_fields(main_form) or {}
    for field_key, meta in fields.items():
        key_lower = str(field_key or "").lower()
        if not key_lower or meta is None:
            continue
        field_type = str(getattr(meta, "type", "") or "")
        required = bool(getattr(meta, "required", False))
        if field_type == "MainOrgProp" or key_lower == "createorg":
            modes.setdefault(key_lower, "update_fields")
            continue
        if (required and (
                key_lower in {"adminorg", "org", "useorg"}
                or "AdminOrgFieldProp" in field_type
        )):
            modes.setdefault(key_lower, "pick_basedata")
    return modes


def _inject_context_field_steps(
    steps: list[dict],
    main_form: str,
    meta_resolver=None,
) -> list[dict]:
    """用 HAR 中可见的上下文线索补全隐式字段。

    目标覆盖两类典型缺口：
    1. treeview.focus 隐式决定的组织字段（如 adminorg）
    2. MainOrgProp 由客户端自动带出的主组织字段（如 createorg）
    """
    if not steps or not main_form:
        return steps

    new_idx = next(
        (i for i, s in enumerate(steps)
         if s.get("form_id") == main_form
         and s.get("type") == "invoke"
         and s.get("ac") in ("new", "addnew")),
        None,
    )
    if new_idx is None:
        return steps

    new_step = steps[new_idx]
    tree_focus_id, tree_focus_name = _extract_treeview_focus(new_step)
    search_defaults = _extract_common_search_defaults(steps, main_form)
    field_modes = _infer_context_field_modes(main_form, meta_resolver=meta_resolver)
    if not field_modes:
        return steps

    existing_fields = {
        str(field_key).lower()
        for step in steps
        for field_key in (
            [step.get("field_key")]
            if step.get("type") == "pick_basedata"
            else list((step.get("fields") or {}).keys()) if step.get("type") == "update_fields"
            else []
        )
        if field_key
    }

    insertion_pos = _find_context_insertion_pos(steps, main_form, new_idx)
    injected: list[dict] = []
    app_id = str(new_step.get("app_id") or "bos")

    for field_key, mode in field_modes.items():
        if field_key in existing_fields:
            continue

        if mode == "pick_basedata":
            value_id = tree_focus_id or search_defaults.get(f"{field_key}.id", "")
            value_name = tree_focus_name
            if not value_id:
                continue
            injected.append({
                "type": "pick_basedata",
                "id": f"pick_{_sanitize(field_key) or field_key}_ctx",
                "form_id": main_form,
                "app_id": app_id,
                "field_key": field_key,
                "value_id": str(value_id),
                "value_name": value_name,
                "_tier": "core",
                "_is_auto_inserted": True,
            })
            continue

        if mode == "update_fields":
            candidate_keys = [f"{field_key}.id"]
            if field_key == "createorg":
                candidate_keys.extend(["useorg.id", "org.id", "adminorg.id"])
            value_id = next((search_defaults.get(k, "") for k in candidate_keys if search_defaults.get(k, "")), "")
            if not value_id:
                value_id = tree_focus_id
            if not value_id:
                continue
            injected.append({
                "type": "update_fields",
                "id": f"fill_{_sanitize(field_key) or field_key}_ctx",
                "form_id": main_form,
                "app_id": app_id,
                "fields": {field_key: str(value_id)},
                "_tier": "core",
                "_is_auto_inserted": True,
            })

    if not injected:
        return steps

    out = list(steps)
    for offset, step in enumerate(injected):
        out.insert(insertion_pos + offset, step)
    return out


# ---------- YAML 输出 ----------

def to_yaml(data: Any, indent: int = 0) -> str:
    pad = "  " * indent
    if isinstance(data, dict):
        if not data:
            return "{}"
        lines = []
        for k, v in data.items():
            ks = _yaml_key(k)
            if isinstance(v, (dict, list)) and v:
                lines.append(f"{pad}{ks}:")
                lines.append(to_yaml(v, indent + 1))
            else:
                lines.append(f"{pad}{ks}: {_yaml_scalar(v, key=k)}")
        return "\n".join(lines)
    if isinstance(data, list):
        if not data:
            return "[]"
        lines = []
        for v in data:
            if isinstance(v, dict):
                inner = to_yaml(v, indent + 1)
                inner_lines = inner.split("\n")
                if inner_lines:
                    first = inner_lines[0].lstrip()
                    lines.append(f"{pad}- {first}")
                    for rest in inner_lines[1:]:
                        lines.append(rest)
            elif isinstance(v, list):
                lines.append(f"{pad}- {json.dumps(v, ensure_ascii=False)}")
            else:
                lines.append(f"{pad}- {_yaml_scalar(v)}")
        return "\n".join(lines)
    return f"{pad}{_yaml_scalar(data)}"


def _yaml_key(k: Any) -> str:
    ks = str(k)
    if any(c in ks for c in " :#{}[]&*!|>'\"%@`"):
        return json.dumps(ks, ensure_ascii=False)
    return ks


def _yaml_scalar(v: Any, key: Any | None = None) -> str:
    if v is None:
        return "null"
    if isinstance(v, bool):
        return "true" if v else "false"
    if isinstance(v, (int, float)):
        return str(v)
    if isinstance(v, (list, dict)):
        return json.dumps(v, ensure_ascii=False)
    s = str(v)
    # 以 ${ 开头的占位符不加引号
    if s.startswith("${") and s.endswith("}"):
        return s
    # 多语言文本里的纯数字必须保持字符串，否则 YAML 反序列化会变成 int，
    # 运行时再写回文本字段时会触发 ClassCastException。
    if key in _MULTILANG_KEYS and s and _RX_INTEGER.match(s):
        return json.dumps(s, ensure_ascii=False)
    if key in {"value_id", "value_code", "value_number", "recorded_value_id", "ctrlstrategy"} and s and _RX_INTEGER.match(s):
        return json.dumps(s, ensure_ascii=False)
    # 业务编码常有前导 0（如国家 001、城市 00407），必须保持字符串。
    if s and re.match(r"^0\d+$", s):
        return json.dumps(s, ensure_ascii=False)
    # ⭐ 规则1：纯数字字符串必须加引号，否则 YAML 解析器会转成整数
    # Java 服务端通过 beanutils 反射调用，需要 String 类型匹配方法签名
    if s and _RX_INTEGER.match(s) and len(s) >= 6:
        return json.dumps(s, ensure_ascii=False)
    # ⭐ 日期格式加引号：防止 YAML 解析器把 2026-04-24 解析为 datetime.date 对象
    if re.match(r"^\d{4}-\d{2}-\d{2}([ T]\d{2}:\d{2}(:\d{2})?)?$", s):
        return json.dumps(s, ensure_ascii=False)
    if s == "" or any(c in s for c in "\n\t:#{}[]&*!|>'\"%@`") or s.startswith("-") \
            or s in ("null", "true", "false", "yes", "no"):
        return json.dumps(s, ensure_ascii=False)
    return s


# ---------- 组装 ----------

def _iter_response_actions(node: Any):
    """Yield action-like dicts from a nested Kingdee response payload."""
    if isinstance(node, dict):
        if "a" in node:
            yield node
        for value in node.values():
            if isinstance(value, (dict, list)):
                yield from _iter_response_actions(value)
    elif isinstance(node, list):
        for item in node:
            yield from _iter_response_actions(item)


def _extract_error_notifications(resp_text: str) -> list[dict[str, Any]]:
    """Extract non-success notification messages recorded in HAR responses."""
    if not resp_text:
        return []
    try:
        resp = json.loads(resp_text)
    except Exception:
        return []

    success_kw = (
        "成功", "已保存", "已提交", "已生效", "已审核", "已完成",
        "操作成功", "已设置", "已清空", "已更新", "已调整", "已同步",
    )
    messages: list[dict[str, Any]] = []
    seen: set[str] = set()
    for cmd in _iter_response_actions(resp):
        if cmd.get("a") != "ShowNotificationMsg":
            continue
        for payload in cmd.get("p", []):
            if not isinstance(payload, dict):
                continue
            content = str(payload.get("content") or "").strip()
            if not content or content in seen:
                continue
            ntype = payload.get("type")
            if ntype == 0 or any(kw in content for kw in success_kw):
                continue
            seen.add(content)
            messages.append({
                "content": content,
                "type": ntype,
                "source": "har_recorded",
            })
    return messages


def _is_write_anchor_step(step: dict) -> bool:
    ac = str(step.get("ac") or "").lower()
    key = str(step.get("key") or "").lower()
    sid = str(step.get("id") or "").lower()
    return (
        ac in {
            "save", "submit", "saveandeffect", "submitandeffect",
            "saveandaudit", "doconfirm", "afterconfirm", "startupflow",
        }
        or key in {
            "btnsave", "btn_save", "bar_save", "barsave",
            "btn_confirm", "btnconfirm", "bar_confirm", "barconfirm",
            "btnok", "btn_ok", "bar_submit", "barsubmit",
            "barstart", "bar_start", "btn_saveandeffect", "btnsaveandeffect",
        }
        or "save" in sid
    )


def _write_anchor_pos_for_form(steps: list[dict], form_id: str) -> int:
    return next((
        idx for idx, step in enumerate(steps)
        if step.get("form_id") == form_id and _is_write_anchor_step(step)
    ), len(steps))


_VAR_REF_RE = re.compile(r"\$\{vars\.([A-Za-z_][A-Za-z0-9_]*)\}")


def _clean_group_action_label(text: Any) -> str:
    raw = str(text or "").strip()
    if not raw:
        return ""
    raw = re.sub(r"^[^\w\u4e00-\u9fff]+", "", raw).strip()
    return raw


def _step_scope_for_index(steps: list[dict], index: int, main_form: str = "") -> dict[str, str]:
    """Map a field/variable step to the nearest business write block.

    The YAML key remains stable; this metadata only helps UI group long HAR
    chains into "form/action blocks" so users can maintain A/B/C forms without
    mentally reverse-engineering step order.
    """
    step = steps[index] if 0 <= index < len(steps) else {}
    form_id = str(step.get("form_id") or main_form or "")
    anchor = None
    for nxt in steps[index:]:
        if form_id and str(nxt.get("form_id") or "") != form_id:
            continue
        if _is_write_anchor_step(nxt):
            anchor = nxt
            break
    if anchor is None:
        for prev in reversed(steps[:index + 1]):
            if form_id and str(prev.get("form_id") or "") != form_id:
                continue
            if _is_write_anchor_step(prev):
                anchor = prev
                break

    form_label = _resolve_form_name(form_id) if form_id else "未识别表单"
    anchor_id = str((anchor or {}).get("id") or "")
    action_label = _clean_group_action_label((anchor or {}).get("description") or "")
    if not action_label and anchor:
        action_label = generate_step_description(anchor)
        action_label = _clean_group_action_label(action_label)
    if not action_label:
        action_label = "表单维护"
    group_key = f"{form_id or 'unknown'}:{anchor_id or 'context'}"
    return {
        "group_key": group_key,
        "group_label": f"{form_label} / {action_label}",
        "form_id": form_id,
        "form_label": form_label,
        "source_step_id": str(step.get("id") or ""),
        "write_step_id": anchor_id,
    }


def _collect_var_refs(value: Any, refs: set[str]) -> None:
    if isinstance(value, str):
        refs.update(_VAR_REF_RE.findall(value))
    elif isinstance(value, dict):
        for item in value.values():
            _collect_var_refs(item, refs)
    elif isinstance(value, list):
        for item in value:
            _collect_var_refs(item, refs)


def _infer_vars_meta_from_steps(steps: list[dict], main_form: str = "") -> OrderedDict:
    """Infer variable source form/field/block metadata from generated steps."""
    meta: OrderedDict[str, OrderedDict] = OrderedDict()

    def remember(vname: str, *, step_idx: int, field_key: str = "") -> None:
        if not vname or vname in meta:
            return
        step = steps[step_idx] if 0 <= step_idx < len(steps) else {}
        scope = _step_scope_for_index(steps, step_idx, main_form)
        label = _resolve_field_label(field_key, entity_id=scope.get("form_id") or main_form) if field_key else ""
        item = OrderedDict([
            ("label", label or field_key or vname),
            ("field_key", field_key),
            ("form_id", scope["form_id"]),
            ("form_label", scope["form_label"]),
            ("group_key", scope["group_key"]),
            ("group_label", scope["group_label"]),
            ("source_step_id", scope["source_step_id"] or str(step.get("id") or "")),
            ("write_step_id", scope["write_step_id"]),
        ])
        meta[vname] = item

    for idx, step in enumerate(steps):
        stype = step.get("type")
        if stype == "update_fields":
            for field_key, value in (step.get("fields") or {}).items():
                refs: set[str] = set()
                _collect_var_refs(value, refs)
                for ref in sorted(refs):
                    remember(ref, step_idx=idx, field_key=str(field_key))
        elif stype == "invoke":
            post_data = step.get("post_data")
            if isinstance(post_data, list) and len(post_data) >= 2 and isinstance(post_data[1], list):
                for entry in post_data[1]:
                    if not isinstance(entry, dict):
                        continue
                    refs: set[str] = set()
                    _collect_var_refs(entry.get("v"), refs)
                    for ref in sorted(refs):
                        remember(ref, step_idx=idx, field_key=str(entry.get("k") or ""))
        elif stype == "pick_basedata":
            # pick_fields own the environment panel; do not duplicate them as
            # smart case variables when old YAML still contains a vars ref.
            continue
    return meta


def _attach_pick_field_scopes(pick_fields_map: OrderedDict, steps: list[dict], main_form: str = "") -> None:
    """Add UI grouping metadata to pick_fields without changing their keys."""
    if not pick_fields_map:
        return

    def find_step_index(pf_id: str, pf_meta: dict) -> int:
        field_key = str((pf_meta or {}).get("field_key") or "").lower()
        source_step_id = str((pf_meta or {}).get("source_step_id") or "")
        target_form_id = str((pf_meta or {}).get("form_id") or "")
        if source_step_id:
            for idx, step in enumerate(steps):
                if str(step.get("id") or "") == source_step_id:
                    return idx

        def form_matches(step: dict) -> bool:
            return not target_form_id or not step.get("form_id") or str(step.get("form_id") or "") == target_form_id

        if pf_id.startswith("env_") and pf_id.endswith("_treeview_focus"):
            step_id = pf_id[4:-15]
            for idx, step in enumerate(steps):
                if step.get("id") == step_id:
                    return idx
        if pf_id.startswith("date_"):
            field_key = field_key or pf_id[5:].lower()
            for idx, step in enumerate(steps):
                if step.get("type") != "update_fields":
                    continue
                if not form_matches(step):
                    continue
                fields = step.get("fields") or {}
                if any(str(k).lower() == field_key for k in fields):
                    return idx
        if field_key:
            for idx, step in enumerate(steps):
                if not form_matches(step):
                    continue
                if step.get("type") == "pick_basedata" and str(step.get("field_key") or "").lower() == field_key:
                    return idx
            for idx, step in enumerate(steps):
                if step.get("type") != "update_fields":
                    continue
                if not form_matches(step):
                    continue
                fields = step.get("fields") or {}
                if any(str(k).lower() == field_key for k in fields):
                    return idx
        return 0

    for pf_id, pf_meta in pick_fields_map.items():
        if not isinstance(pf_meta, dict):
            continue
        idx = find_step_index(str(pf_id), pf_meta)
        scope = _step_scope_for_index(steps, idx, main_form)
        pf_meta.setdefault("form_label", scope["form_label"])
        pf_meta.setdefault("group_key", scope["group_key"])
        pf_meta.setdefault("group_label", scope["group_label"])
        pf_meta.setdefault("source_step_id", scope["source_step_id"])
        pf_meta.setdefault("write_step_id", scope["write_step_id"])


def _annotate_env_field_sources(
    pick_fields_map: OrderedDict,
    observations: dict[str, Any],
    *,
    meta_resolver=None,
) -> None:
    """Explain where each environment field came from without changing values."""
    if not pick_fields_map:
        return
    values_by_form = observations.get("response_values_by_form") or {}
    internal_ids_by_form = observations.get("response_internal_ids_by_form") or {}
    combo_options_by_form = observations.get("combo_options_by_form") or {}
    labels_by_form = observations.get("labels_by_form") or {}
    for _pf_id, meta in pick_fields_map.items():
        if not isinstance(meta, dict):
            continue
        form_id = str(meta.get("form_id") or "")
        field_key = str(meta.get("field_key") or "").lower()
        source = str(meta.get("source") or "")
        sources: list[str] = []
        if source:
            sources.append(source)
        if meta.get("selector_source"):
            sources.append("f7_entry_row")
        if meta.get("context_only"):
            sources.append("server_default_context")
        if form_id and field_key in (values_by_form.get(form_id) or {}):
            sources.append("loadData_response")
        if form_id and field_key in (internal_ids_by_form.get(form_id) or {}):
            sources.append("list_dataindex")
        if form_id and field_key in (combo_options_by_form.get(form_id) or {}):
            sources.append("combo_options")
        if form_id and field_key in (labels_by_form.get(form_id) or {}):
            sources.append("control_metadata")
        if meta.get("auto_resolve"):
            resolve_by = str(meta.get("resolve_by") or "auto_resolve")
            sources.append(f"auto_resolve:{resolve_by}")
        field_type = ""
        if meta_resolver and form_id and field_key:
            try:
                field_type = meta_resolver.get_field_type(form_id, field_key) or ""
            except Exception:
                field_type = ""
        if field_type:
            meta.setdefault("metadata_type", field_type)
            sources.append("metadata")
        meta.setdefault("source_type", _primary_env_source(sources, meta))
        meta.setdefault("source_detail", " + ".join(_dedupe_strings(sources)) or "har_recorded")


def _primary_env_source(sources: list[str], meta: dict[str, Any]) -> str:
    ordered = [
        "f7_entry_row",
        "server_default_context",
        "loadData_response",
        "list_dataindex",
        "combo_options",
        "control_metadata",
        "metadata",
    ]
    for item in ordered:
        if item in sources:
            return item
    if meta.get("auto_resolve"):
        return "auto_resolve"
    if sources:
        return sources[0]
    return "har_recorded"


def _dedupe_strings(items: list[str]) -> list[str]:
    seen: set[str] = set()
    out = []
    for item in items:
        if item and item not in seen:
            seen.add(item)
            out.append(item)
    return out


def _build_preview_business_blocks(
    var_items: list[dict[str, Any]],
    pick_fields: list[dict[str, Any]],
) -> list[dict[str, Any]]:
    blocks: OrderedDict[str, dict[str, Any]] = OrderedDict()

    def ensure(item: dict[str, Any], fallback: str) -> dict[str, Any]:
        form_id = str(item.get("form_id") or "")
        group_key = str(item.get("group_key") or (f"{form_id}:context" if form_id else "default:unscoped"))
        group_label = str(item.get("group_label") or item.get("form_label") or fallback)
        if group_key not in blocks:
            blocks[group_key] = {
                "key": group_key,
                "label": group_label,
                "form_id": form_id,
                "form_label": str(item.get("form_label") or ""),
                "write_step_id": str(item.get("write_step_id") or ""),
                "smart_var_count": 0,
                "env_field_count": 0,
                "smart_vars": [],
                "env_fields": [],
            }
        return blocks[group_key]

    for item in var_items or []:
        block = ensure(item, "智能变量")
        block["smart_var_count"] += 1
        block["smart_vars"].append({
            "name": item.get("name", ""),
            "label": item.get("label", ""),
            "field_key": item.get("field_key", ""),
        })
    for item in pick_fields or []:
        block = ensure(item, "环境字段")
        block["env_field_count"] += 1
        block["env_fields"].append({
            "id": item.get("id", ""),
            "label": item.get("label", ""),
            "field_key": item.get("field_key", ""),
            "source_type": item.get("source_type", ""),
        })
    return list(blocks.values())


def _has_expected_notification(step: dict) -> bool:
    return bool(step.get("expected_notifications") or step.get("expected_errors"))


def _mark_recorded_business_validations(steps: list[dict]) -> None:
    """Mark intermediate business validations that were present in the recording.

    Real HARs may contain a load/save/click response that intentionally
    triggers a business notification, followed by the user filling the missing
    field and saving. That notification is a validation checkpoint, not a
    replay failure, so we preserve it as expected and keep running.
    """
    for idx, step in enumerate(steps):
        notifications = _extract_error_notifications(str(step.get("_resp_text") or ""))
        if not notifications:
            continue
        has_later_write = any(_is_write_anchor_step(s) for s in steps[idx + 1:])
        has_later_core_input = any(
            s.get("type") in ("update_fields", "pick_basedata")
            and s.get("form_id") == step.get("form_id")
            for s in steps[idx + 1:]
        )
        if not (has_later_write and has_later_core_input):
            continue
        step["expected_notifications"] = notifications
        step["continue_on_expected_error"] = True


def _build_default_assertions(yaml_steps: list[dict]) -> list:
    """根据 step 列表智能生成默认断言。

    策略（P0-1 优化：覆盖 4 类写库形态）：
      1) 行政组织：ac=save / key=tbmain
      2) 业务模型附表：ac=saveandeffect
      3) HR 用工关系：ac=click + key=btnsave（无 save ac）
      4) 入职申请：ac=startupflow / afterConfirm / doconfirm / click btn_confirm（多阶段）

    识别优先级：写库 ac 白名单 > key 写库白名单 > id 含 save > click/itemClick 兜底
    """
    # 写库 ac 白名单（生效/审核类最强；流程类次之；普通保存/提交兜底）
    _save_acs = {
        "saveandeffect", "submitandeffect", "saveandaudit",
        "save", "submit",
        "doconfirm", "afterconfirm", "startupflow",
    }
    # 写库 key 命名特征（click 按钮写库场景，如 HR 用工关系 key=btnsave）
    _save_keys = {
        "btnsave", "btn_save", "bar_save", "barsave",
        "btn_confirm", "btnconfirm", "bar_confirm", "barconfirm",
        "btnok", "btn_ok", "bar_submit", "barsubmit",
        "barstart", "bar_start",
        "btn_saveandeffect", "btnsaveandeffect",
    }

    def _ac(s):
        return str(s.get("ac") or "").lower()

    def _key(s):
        return str(s.get("key") or "").lower()

    # ——找 save_id（挂 no_save_failure）——
    save_id = None
    expected_assertions = []
    for s in yaml_steps:
        if not _has_expected_notification(s):
            continue
        sid = s.get("id")
        for item in s.get("expected_notifications") or s.get("expected_errors") or []:
            content = item.get("content") if isinstance(item, dict) else str(item)
            if sid and content:
                expected_assertions.append(OrderedDict([
                    ("type", "expected_notification"),
                    ("step", sid),
                    ("contains", str(content)),
                ]))

    write_candidates = [s for s in yaml_steps if not _has_expected_notification(s)]

    # 1. ac 在写库白名单
    for s in write_candidates:
        if _ac(s) in _save_acs:
            save_id = s.get("id")
            break
    # 2. key 在写库 key 白名单（覆盖 click btnsave 这类）
    if not save_id:
        for s in write_candidates:
            if _key(s) in _save_keys:
                save_id = s.get("id")
                break
    # 3. id 含 save（兜底，兼容老生成器产物）
    if not save_id:
        for s in write_candidates:
            sid = s.get("id", "")
            if "save" in sid.lower():
                save_id = sid
                break

    # ——找 confirm_id（挂 no_error_actions，取"最后一个写库锚点"）——
    _key_acs = {"itemClick", "click"} | _save_acs
    confirm_id = None
    # 1. 倒序：ac 在写库白名单（最可靠的写库锚点）
    for s in reversed(write_candidates or yaml_steps):
        if _ac(s) in _save_acs:
            confirm_id = s.get("id")
            break
    # 2. 倒序：key 在写库 key 白名单
    if not confirm_id:
        for s in reversed(write_candidates or yaml_steps):
            if _key(s) in _save_keys:
                confirm_id = s.get("id")
                break
    # 3. 倒序：click/itemClick 且 id 以 click_ 开头
    if not confirm_id:
        for s in reversed(write_candidates or yaml_steps):
            sid = s.get("id", "")
            if _ac(s) in ("click", "itemclick") and sid.startswith("click_"):
                confirm_id = sid
                break
    # 4. 兜底：任何关键动作
    if not confirm_id:
        for s in reversed(write_candidates or yaml_steps):
            if _ac(s) in _key_acs:
                confirm_id = s.get("id")
                break

    assertions = expected_assertions[:]
    if save_id:
        assertions.append(OrderedDict([("type", "no_save_failure"), ("step", save_id)]))
    if confirm_id:
        assertions.append(OrderedDict([("type", "no_error_actions"), ("step", confirm_id)]))
    else:
        assertions.append(OrderedDict([("type", "no_error_actions"), ("last_step", True)]))
    return assertions


def _append_readback_assertions(case: OrderedDict) -> dict:
    """Append optional post-save readback assertions from case business keys.

    This is opt-in for HAR generation. Default output stays unchanged so the
    existing regression baselines remain stable, while Web UI can enable it for
    better write verification.
    """
    try:
        from lib.deep_chain_pipeline import build_readback_plan
    except Exception as e:
        log.warning("readback plan unavailable: %s", e)
        return {"status": "not_ready", "plans": []}

    plan = build_readback_plan(dict(case))
    if plan.get("status") != "ready":
        return plan

    assertions = case.setdefault("assertions", [])
    seen = {
        (
            str(a.get("type") or ""),
            str(a.get("form_id") or ""),
            str(a.get("field_key") or ""),
            str(a.get("value") or a.get("value_ref") or ""),
        )
        for a in assertions
        if isinstance(a, dict)
    }
    for item in plan.get("plans") or []:
        policy = item.get("assertion_policy") or {}
        if policy.get("auto_append") is False:
            continue
        suggested = item.get("suggested_assertion") or {}
        field_key = str(suggested.get("field_key") or "")
        value = str(suggested.get("value") or suggested.get("value_ref") or "")
        if not (field_key and value):
            continue
        sig = (
            "readback_by_business_key",
            str(suggested.get("form_id") or ""),
            field_key,
            value,
        )
        if sig in seen:
            continue
        assertions.append(OrderedDict([
            ("type", "readback_by_business_key"),
            *(
                [("strategy", suggested.get("strategy"))]
                if suggested.get("strategy") else []
            ),
            *(
                [("menu_id", suggested.get("menu_id"))]
                if suggested.get("menu_id") else []
            ),
            ("form_id", suggested.get("form_id", "")),
            ("app_id", suggested.get("app_id", "")),
            ("field_key", field_key),
            ("value", value),
        ]))
        seen.add(sig)
    return plan


def _build_preview_readback_plan(main_form: str, var_items: list[dict]) -> dict:
    try:
        from lib.deep_chain_pipeline import build_readback_plan
    except Exception as e:
        log.warning("preview readback plan unavailable: %s", e)
        return {"status": "not_ready", "plans": []}
    case = {
        "main_form_id": main_form,
        "vars": OrderedDict(),
        "vars_meta": OrderedDict(),
    }
    for item in var_items or []:
        name = str(item.get("name") or "")
        if not name:
            continue
        case["vars"][name] = item.get("template", "")
        case["vars_meta"][name] = OrderedDict([
            ("label", item.get("label", "")),
            ("field_key", item.get("field_key", "")),
            ("form_id", item.get("form_id", "") or main_form),
            ("form_label", item.get("form_label", "")),
            ("group_key", item.get("group_key", "")),
            ("group_label", item.get("group_label", "")),
            ("source_step_id", item.get("source_step_id", "")),
            ("write_step_id", item.get("write_step_id", "")),
        ])
    return build_readback_plan(case)


def insert_loaddata_on_form_change(steps: list) -> list:
    """⭐ 规则14：form_id 变化时自动插入 loadData 保护步骤

    当检测到 form_id 从 A 变为 B 且满足以下条件时，在变化点前插入 loadData：
    1. 当前步骤类型为 invoke（非 open_form/loadData/sleep）
    2. 同一 L2 上下文中（_har_page_id 共享相同前缀或相同值）

    排除条件：
    - open_form 步骤后不重复插入（runner 已隐式 loadData）
    - loadData 步骤本身不触发保护
    - 前一步已是同 form_id 的 loadData 则跳过
    """
    out = []
    prev_form = None
    prev_ac = None

    for i, s in enumerate(steps):
        cur_form = s.get("form_id", "")
        cur_type = s.get("type", "")
        cur_ac = s.get("ac", "")

        # 检测 form_id 变化
        if (cur_form and prev_form and cur_form != prev_form
            and cur_type == "invoke"
            and cur_ac not in ("loadData", "open_form")
            and prev_ac != "open_form"):

            # 检查前一步是否已经是同 form_id 的 loadData
            already_has_load = (out and out[-1].get("form_id") == cur_form
                                and out[-1].get("ac") == "loadData")

            if not already_has_load:
                # 插入 loadData 保护步骤
                protect_step = {
                    "type": "invoke",
                    "id": f"protect_load_{cur_form.replace('.', '_')[:30]}",
                    "form_id": cur_form,
                    "app_id": s.get("app_id", "bos"),
                    "ac": "loadData",
                    "key": "",
                    "method": "loadData",
                    "args": [],
                    "post_data": [{}, []],
                    "_tier": "core",
                    "_is_auto_inserted": True,
                }
                # 如果有 _har_page_id，继承
                if s.get("_har_page_id"):
                    protect_step["_har_page_id"] = s["_har_page_id"]
                out.append(protect_step)
                log.debug(f"[规则14] 自动插入 loadData 保护: {prev_form} → {cur_form}")

        out.append(s)
        prev_form = cur_form if cur_form else prev_form
        prev_ac = cur_ac if cur_type in ("invoke", "open_form") else prev_ac

    return out


def build_yaml_case(
    har_path: Path,
    case_name: str | None = None,
    var_overrides: dict | None = None,
    pick_field_overrides: dict | None = None,
    meta_resolver=None,
    include_readback_assertions: bool = False,
) -> str:
    har = load_har(har_path)
    field_observations = _collect_har_field_observations(har)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)
    raw_steps = _drop_locked_update_fields(raw_steps, field_observations)

    # ⭐ 规则2：session pageId 动态化（selectTab args 中的 root{32hex} → ${session.root_base_id}）
    raw_steps = dynamize_session_pageids(raw_steps)

    # ⭐ 规则3：检测连续新增模式，自动标记 keep_page
    raw_steps = detect_keep_page(raw_steps)

    # 推断主表单先用占位，清理后重新推断（清理会移除 release 和截断跨应用步骤，影响计数）
    # 此处做一次粗糙推断仅供裁剪兜底用
    main_form = infer_main_form(raw_steps)

    # ⭐ 规则4：裁剪策略改进 —— 保留门户入口步骤
    # 从"第一次 appItemClick"开始保留，但回溯包含其前面紧挨的 bos_portal_* open_form
    # 这样既去掉了首页装饰，又保留了完整的门户入口链（open_portal + appItemClick）
    trimmed_skipped = 0
    cut_idx = None
    for i, s in enumerate(raw_steps):
        if s.get("ac") in ("menuItemClick", "appItemClick"):
            cut_idx = i
            break
    if cut_idx is not None and cut_idx > 0:
        # 回溯：如果 cut_idx 前面有 bos_portal_* 的 open_form，也保留
        portal_start = cut_idx
        for j in range(cut_idx - 1, max(cut_idx - 5, -1), -1):
            prev = raw_steps[j]
            if (prev.get("type") == "open_form"
                    and prev.get("form_id", "").startswith("bos_portal")):
                portal_start = j
                break
        trimmed_skipped = portal_start
        raw_steps = raw_steps[portal_start:]
    elif cut_idx is None:
        # 没有 menu/app 入口时，不要粗暴裁到 main_form。
        # 要保留 main_form 前的列表/树上下文桥接步骤，否则“列表跳卡片后新增”类 HAR
        # 会丢失服务端隐式上下文，导致必填字段无法自动带出。
        bridge_start = _find_context_bridge_start(raw_steps, main_form)
        if bridge_start is not None and bridge_start > 0:
            trimmed_skipped = bridge_start
            raw_steps = raw_steps[bridge_start:]

    # 过滤 noise 类步骤
    noise_count = sum(1 for s in raw_steps if s.get("_tier") == "noise")
    ui_count = sum(1 for s in raw_steps if s.get("_tier") == "ui_reaction")
    core_count = sum(1 for s in raw_steps if s.get("_tier") == "core")

    # 只保留 core + 一小部分 ui_reaction（标 optional）
    cleaned: list[dict] = []
    for s in raw_steps:
        tier = s.get("_tier")
        if tier == "noise":
            continue
        if tier == "ui_reaction":
            s = dict(s)
            s["optional"] = True
        cleaned.append(s)

    # ⭐ 规则11a：将确认/提交类 click 步骤从 optional 中解放
    # ui_reaction 分类器可能把弹窗上的确认按钮（如 btn_confirm、barconfirm）标为 ui_reaction，
    # 但这些是业务流程的关键步骤，不能 optional。
    _confirm_key_set = {"confirm", "barconfirm", "btn_confirm", "barstart", "barsave",
                        "barsubmit", "ok", "btnok", "btn_ok"}
    for s in cleaned:
        if s.get("optional") and s.get("ac") in ("click", "itemClick"):
            key = (s.get("key") or "").lower()
            if key in _confirm_key_set or "confirm" in key:
                s.pop("optional", None)
        # afterConfirm 是 showForm 的触发器，也必须执行
        if s.get("optional") and s.get("ac") in ("afterConfirm", "doConfirm", "save", "submit"):
            s.pop("optional", None)

    # 列表/树进入卡片的桥接步骤一旦失败，后面的默认组织/上下文都会失真，不能 optional。
    _mark_context_bridge_steps_required(cleaned, main_form)

    # ⭐ 规则14 已禁用 — 静态插入 loadData 缺乏运行时上下文，可能干扰 pageId 状态
    # 等效保护由 runner.py 的安全网重试（invoke_retry）和 pageId 预验证（_validate_pageid_before_invoke）提供
    # cleaned = insert_loaddata_on_form_change(cleaned)

    # ⭐ 规则11b：移除所有 release 步骤
    # 在 API 回放中，release 的目标 pageId 由 _harvest_page_ids() 维护的状态机决定。
    # 当 showForm 返回新 pageId 后，状态机已覆盖旧值，导致 release 实际释放的是
    # 新打开的表单实例，使后续步骤遭遇"表单会话超时"。浏览器端不受此影响因为
    # 它缓存了旧 pageId。安全做法：全部移除，测试结束后服务端会自动回收。
    cleaned = [s for s in cleaned if s.get("ac") != "release"]

    # ⭐ 规则12：截断第二次跨应用门户导航
    # 浏览器可通过 mainView 组件在应用间切换，API 回放无法模拟（mainView is null）。
    # 典型场景：用户在入职完成后切换到另一个应用查看人员列表 —— 属于人工验证，
    # 不应包含在自动化用例的核心流程中。截断到第二个 appItemClick 之前。
    _app_click_indices = [i for i, s in enumerate(cleaned) if s.get("ac") == "appItemClick"]
    if len(_app_click_indices) >= 2:
        _cut_at = _app_click_indices[1]
        # 回退：也去掉截断点前紧邻的 portal open_form / selectTab / getFrequentData 等导航噪声
        while _cut_at > 0 and cleaned[_cut_at - 1].get("ac") in (
                "getFrequentData", "selectTab", "loadData") and cleaned[_cut_at - 1].get("optional"):
            _cut_at -= 1
        cleaned = cleaned[:_cut_at]

    # 推断主表单（在 release 清理 + 门户截断后重新推断，结果更准确）
    main_form = infer_main_form(cleaned)
    _mark_context_bridge_steps_required(cleaned, main_form)

    # ⭐ 规则13：menuItemClick → 自动绑定 target_form + target_forms + 移除冗余 open_form
    # 苍穹菜单导航：menuItemClick 创建 L2 pageId ({menuId}root{baseId})，
    # 这是主表单列表页的正确页面标识。如果之后再有 open_form(getConfig)，
    # 会获取一个独立的、不在菜单上下文中的 pageId，导致后续 addnew/save 发送到
    # 错误的页面。修复：1) 为 menuItemClick 添加 target_form 注解；2) 检测共享 L2 的
    # 子表单写入 target_forms；3) 删除冗余 open_form。
    _menu_target_set = False
    _menu_id_for_target = ""
    for s in cleaned:
        if s.get("ac") == "menuItemClick" and not _menu_target_set:
            args = s.get("args", [])
            if args and isinstance(args[0], dict):
                menu_id = str(args[0].get("menuId", ""))
                if menu_id and main_form:
                    s["target_form"] = main_form
                    s["env_sensitive"] = "high"
                    s["resolve_by"] = "menu_path_or_form"
                    s["navigation_form_id"] = main_form
                    _menu_target_set = True
                    _menu_id_for_target = menu_id
    if _menu_target_set:
        # 找 menuItemClick 的位置
        menu_idx = next((i for i, s in enumerate(cleaned) if s.get("target_form") == main_form), None)
        if menu_idx is not None:
            # ⭐ 规则13b：检测共享 L2 pageId 的其他表单 → target_forms
            # L2 pageId 格式: {menuId}root{32hex}，同一导航上下文中多个表单共享此 pageId
            l2_prefix = f"{_menu_id_for_target}root"
            target_forms_set: set[str] = set()
            # 方案A：通过 _har_page_id 精确匹配共享 L2 的表单
            for i in range(menu_idx + 1, len(cleaned)):
                step = cleaned[i]
                # 遇到下一个导航动作则停止扫描
                if step.get("ac") in ("menuItemClick", "appItemClick"):
                    break
                har_pid = step.get("_har_page_id", "")
                if har_pid and har_pid.startswith(l2_prefix):
                    fid = step.get("form_id", "")
                    if fid and fid != main_form:
                        target_forms_set.add(fid)
            # 方案B兜底：如果 _har_page_id 不可用，用启发式——menuItemClick 后紧跟的
            # loadData 步骤（在下一个 open_form/menuItemClick 之前），form_id != main_form
            if not target_forms_set:
                for i in range(menu_idx + 1, len(cleaned)):
                    step = cleaned[i]
                    if step.get("ac") in ("menuItemClick", "appItemClick"):
                        break
                    if step.get("type") == "open_form":
                        break
                    if step.get("ac") == "loadData":
                        fid = step.get("form_id", "")
                        if fid and fid != main_form:
                            target_forms_set.add(fid)
            if target_forms_set:
                cleaned[menu_idx]["target_forms"] = sorted(target_forms_set)
            # 移除 menuItemClick 之后的第一个 open_form(main_form)
            # （该 open_form 会通过 getConfig 获取错误的独立 pageId）
            for i in range(menu_idx + 1, len(cleaned)):
                if cleaned[i].get("type") == "open_form" and cleaned[i].get("form_id") == main_form:
                    cleaned.pop(i)
                    break

    # Some HARs start after the portal/menu click but their first business
    # request still carries the recorded menu L2 pageId. Reconstruct that L2
    # bridge instead of injecting open_form(main_form), which would create an
    # unrelated page model and lose the addnew -> showForm L3 chain.
    if main_form and not _menu_target_set:
        first_main_l2_idx = next(
            (
                i for i, s in enumerate(cleaned)
                if (
                    s.get("form_id") == main_form
                    and _is_l2_page_id(s.get("_har_page_id"))
                    and (
                        str(s.get("ac") or "").lower() in {"new", "addnew"}
                        or (
                            str(s.get("method") or "") == "itemClick"
                            and "toolbar" in str(s.get("key") or "").lower()
                        )
                    )
                )
            ),
            None,
        )
        if first_main_l2_idx is not None:
            l2_pid = str(cleaned[first_main_l2_idx].get("_har_page_id") or "")
            menu_id = l2_pid.split("root", 1)[0]
            has_prior_main_step = any(
                s.get("form_id") == main_form
                for s in cleaned[:first_main_l2_idx]
            )
            next_main_l3_pid = next(
                (
                    str(s.get("_har_page_id") or "")
                    for s in cleaned[first_main_l2_idx + 1:]
                    if (
                        s.get("form_id") == main_form
                        and s.get("_har_page_id")
                        and not _is_l2_page_id(s.get("_har_page_id"))
                    )
                ),
                "",
            )
            if (
                not has_prior_main_step
                and menu_id
                and next_main_l3_pid
            ):
                app_id = cleaned[first_main_l2_idx].get("app_id") or "bos"
                cleaned[first_main_l2_idx:first_main_l2_idx] = [
                    {
                        "type": "open_form",
                        "id": "open_portal",
                        "form_id": "bos_portal_myapp_new",
                        "app_id": "bos",
                        "_tier": "core",
                    },
                    {
                        "type": "invoke",
                        "id": f"menuItemClick_{_form_short(main_form)}",
                        "form_id": "bos_portal_myapp_new",
                        "app_id": "bos",
                        "ac": "menuItemClick",
                        "key": "appnavigationmenuap",
                        "method": "menuItemClick",
                        "args": [{
                            "menuId": menu_id,
                            "appId": app_id,
                            "cloudId": "undefined",
                        }],
                        "post_data": [{}, []],
                        "target_form": main_form,
                        "target_forms": [main_form],
                        "env_sensitive": "high",
                        "resolve_by": "menu_path_or_form",
                        "navigation_form_id": main_form,
                        "_tier": "core",
                    },
                ]

    # ⭐ 规则13c：treeMenuClick 属于 apphome 树菜单外壳，API 回放通常无法
    # 重建浏览器 app-shell 的服务端模型。为兼容已通过样本，不用它绑定
    # target_form/L2；后续通过 open_form + HAR 记录的默认上下文字段补齐。
    for s in cleaned:
        if s.get("ac") == "treeMenuClick":
            s.setdefault("optional", True)

    # ⭐ 规则4补充：确保 appItemClick / menuItemClick 之前有对应的 open_portal 步骤
    # 如果裁剪后第一个 appItemClick/menuItemClick 引用了 bos_portal_* 但没有对应的 open_form，注入一个
    for i, s in enumerate(cleaned):
        if s.get("ac") in ("appItemClick", "menuItemClick"):
            portal_form = s.get("form_id", "")
            if portal_form.startswith("bos_portal"):
                has_open = any(
                    cs.get("type") == "open_form" and cs.get("form_id") == portal_form
                    for cs in cleaned[:i]
                )
                if not has_open:
                    cleaned.insert(i, {
                        "type": "open_form",
                        "id": "open_portal",
                        "form_id": portal_form,
                        "app_id": s.get("app_id", "bos"),
                        "_tier": "core",
                    })
            break  # 只处理第一个

    # 如果 cleaned 里没有 open_form 到主表单，补一条
    # ⭐ 规则10：open_form 注入到该表单第一次被 invoke/loadData 使用的位置前
    # 这保证 pageId 在导航完成后（紧贴使用点）才获取，不会因导航而失效
    # ⭐ 规则13 例外：如果已有 menuItemClick + target_form 绑定了主表单的 L2 pageId，
    #   不再注入 open_form（open_form 的 getConfig 会获取错误的独立 pageId）
    _has_menu_target = any(s.get("target_form") == main_form for s in cleaned)
    _has_context_bridge = _has_context_bridge_into_main_form(cleaned, main_form)
    if main_form and cleaned and not _has_menu_target and not _has_context_bridge:
        has_open = any(s.get("type") == "open_form" and s.get("form_id") == main_form
                       for s in cleaned)
        if not has_open:
            app_id = next((s.get("app_id") for s in cleaned if s.get("form_id") == main_form), "bos")
            inject_step = {
                "type": "open_form",
                "id": f"open_{_form_short(main_form)}",
                "form_id": main_form,
                "app_id": app_id,
                "_tier": "core",
            }
            # 找该表单第一次被 invoke 使用的位置，在其前面插入
            insert_pos = 0
            for idx, s in enumerate(cleaned):
                if s.get("form_id") == main_form and s.get("type") == "invoke":
                    insert_pos = idx
                    break
            # 兜底：如果没找到 invoke，放在 appItemClick/menuItemClick 之后
            if insert_pos == 0:
                for idx, s in enumerate(cleaned):
                    if s.get("ac") in ("appItemClick", "menuItemClick"):
                        insert_pos = idx + 1
                        break
            cleaned.insert(insert_pos, inject_step)

    # 用 HAR 中已有的上下文线索补足隐式字段，避免浏览器自动带出的值在 API 回放中丢失。
    cleaned = _inject_context_field_steps(
        cleaned,
        main_form,
        meta_resolver=meta_resolver,
    )
    _context_app_id = next((s.get("app_id", "") for s in cleaned if s.get("form_id") == main_form), "")
    cleaned = _append_context_default_steps(
        cleaned,
        field_observations,
        main_form=main_form,
        app_id=_context_app_id,
        pick_field_overrides=pick_field_overrides,
    )
    cleaned = _append_recorded_default_pick_steps(
        cleaned,
        field_observations,
        main_form=main_form,
        app_id=_context_app_id,
    )
    cleaned = _append_recorded_default_update_steps(
        cleaned,
        field_observations,
        main_form=main_form,
        app_id=_context_app_id,
    )
    cleaned = _drop_locked_update_fields(cleaned, field_observations)
    cleaned = _drop_portal_side_effect_steps(cleaned, main_form)
    _mark_navigation_steps_optional(cleaned, main_form)

    # ⭐ step ID 去重：同名 ID 加数字后缀
    _id_counts: dict[str, int] = {}
    for s in cleaned:
        sid = s.get("id", "")
        if not sid:
            continue
        _id_counts[sid] = _id_counts.get(sid, 0) + 1
    _id_seen: dict[str, int] = {}
    for s in cleaned:
        sid = s.get("id", "")
        if sid and _id_counts.get(sid, 0) > 1:
            _id_seen[sid] = _id_seen.get(sid, 0) + 1
            s["id"] = f"{sid}_{_id_seen[sid]}" if _id_seen[sid] > 1 else sid

    _mark_recorded_business_validations(cleaned)

    # 抽 vars
    _, vars_map, vars_labels = detect_var_placeholders(cleaned, meta_resolver=meta_resolver)
    # _date_replaced 是内部标记，不输出
    vars_map.pop("_date_replaced", None)

    # --- 从 cleaned 步骤中提取 pick_fields（环境相关字段） ---
    _PF_ENV_RELATED_FIELDS = {
        "ba_e_enterprise": "企业",
        "ba_po_adminorg": "行政组织",
        "ba_po_position": "职位",
        "ba_org": "组织",
        "ba_dept": "部门",
        "ba_company": "公司",
        "enterprise": "企业",
        "adminorg": "行政组织",
        "position": "职位",
        "org": "组织",
        "dept": "部门",
        "createorg": "创建组织",
        "useorg": "使用组织",
    }
    _PF_ENUM_FIELDS = {
        "gender": "性别",
        "certificatetype": "证件类型",
        "ba_e_laborrelstatus": "用工状态",
        "laborreltypecls": "用工关系分类",
        "ctrlstrategy": "控制策略",
        "status": "状态",
        "type": "类型",
    }
    _PF_ENV_SENSITIVE_KEYWORDS = (
        "effectdate", "effectdatebak", "loseeffectdate",
        "bsed", "bsled", "startdate", "enddate",
    )
    pick_fields_map = OrderedDict()
    for s in cleaned:
        if s.get("type") != "pick_basedata":
            continue
        field_key = s.get("field_key", "")
        if not field_key:
            continue
        # value_id 可能已被 detect_var_placeholders 替换为 ${vars.xxx}，需从 vars_map 获取原始值
        raw_value_id = s.get("value_id", "")
        if isinstance(raw_value_id, str) and raw_value_id.startswith("${vars."):
            vname_ref = raw_value_id[7:-1]  # 提取变量名
            raw_value_id = vars_map.get(vname_ref, raw_value_id)
        step_form_id = s.get("form_id") or main_form
        # 确定 step_id 和 env_sensitive
        if field_key in _PF_ENV_RELATED_FIELDS:
            base_step_id = f"pick_{field_key}_id"
            env_sensitive = "medium"
            label = _PF_ENV_RELATED_FIELDS[field_key]
        elif field_key in _PF_ENUM_FIELDS:
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
            base_step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            env_sensitive = "low"
            label = _PF_ENUM_FIELDS[field_key]
        else:
            # 通用处理：所有 pick_basedata 字段均归入环境相关字段
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
            base_step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            env_sensitive = "low"
            label = _resolve_field_label(field_key, entity_id=s.get("form_id") or main_form, meta_resolver=meta_resolver)
        step_id = _scoped_pick_field_id(
            base_step_id,
            pick_fields_map,
            form_id=step_form_id,
            source_step_id=s.get("id", ""),
        )
        if not step_id:
            continue
        # ⭐ 实时元数据增强 env_sensitive 分级
        field_type = None
        if meta_resolver and step_form_id:
            field_type = meta_resolver.get_field_type(step_form_id, field_key)
            if field_type:
                if field_type in ("BasedataProp", "MulBasedataProp", "OrgProp", "UserProp"):
                    env_sensitive = "medium"
                elif field_type in ("ComboProp", "MulComboProp", "BooleanProp"):
                    env_sensitive = "low"
        value_code = s.get("value_code", "") or ""
        display_value_id = _display_pick_value_id(raw_value_id, value_code)
        auto_meta = _pick_field_auto_resolve_meta(
            field_key,
            display_value_id,
            s.get("value_name", "") or "",
            env_sensitive,
            field_type,
            value_code=value_code,
        )
        if value_code and _looks_like_internal_id(raw_value_id):
            auto_meta = {
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "pending",
            }
        pick_fields_map[step_id] = OrderedDict([
            ("value_id", display_value_id),
            ("value_name", s.get("value_name", "") or ""),
            ("value_code", value_code),
            ("value_number", s.get("value_number", "") or ""),
            ("recorded_value_id", str(raw_value_id)),
            ("label", label),
            ("env_sensitive", env_sensitive),
            ("field_key", field_key),
            ("form_id", step_form_id),
            ("app_id", s.get("app_id", "")),
            ("auto_resolve", auto_meta["auto_resolve"]),
            ("resolve_by", auto_meta["resolve_by"]),
            ("resolve_status", auto_meta["resolve_status"]),
        ])

    _main_app_id = next((s.get("app_id", "") for s in cleaned if s.get("form_id") == main_form), "")
    _append_context_default_pick_fields(
        pick_fields_map,
        field_observations,
        main_form=main_form,
        app_id=_main_app_id,
    )

    # --- 从 addnew/new 步骤中提取 treeview.focus（环境上下文组织） ---
    for s in cleaned:
        if s.get("type") != "invoke" or s.get("ac") not in ("new", "addnew"):
            continue
        focus_id, focus_name = _extract_treeview_focus(s)
        if not focus_id and not focus_name:
            continue
        step_id = f"env_{s.get('id', 'addnew')}_treeview_focus"
        if step_id in pick_fields_map:
            continue
        pick_fields_map[step_id] = OrderedDict([
            ("value_id", str(focus_id)),
            ("value_name", focus_name),
            ("label", "新增上下文组织"),
            ("env_sensitive", "high"),
            ("field_key", "treeview.focus.id"),
            ("form_id", s.get("form_id", "")),
            ("app_id", s.get("app_id", "")),
            ("auto_resolve", False),
            ("resolve_by", ""),
            ("resolve_status", "manual"),
        ])

    # --- 从 F7 选择器 entryRowClick 中提取用户选择对象（如计薪人员任职经历） ---
    for s in cleaned:
        selector = _extract_entry_row_selector(s)
        if not selector:
            continue
        step_id = _scoped_pick_field_id(
            f"selector_{selector['selector_key']}_id",
            pick_fields_map,
            form_id=s.get("form_id") or "",
            source_step_id=s.get("id", ""),
        )
        if not step_id:
            continue
        pick_fields_map[step_id] = OrderedDict([
            ("value_id", selector["value_code"] or selector["value_id"]),
            ("value_name", selector["value_name"]),
            ("value_code", selector["value_code"]),
            ("recorded_value_id", selector["value_id"]),
            ("label", selector["label"]),
            ("env_sensitive", "medium"),
            ("field_key", selector["field_key"]),
            ("form_id", s.get("form_id", "")),
            ("app_id", s.get("app_id", "")),
            ("source_step_id", s.get("id", "")),
            ("auto_resolve", True if selector["value_code"] else False),
            ("resolve_by", "value_code" if selector["value_code"] else ""),
            ("resolve_status", "pending" if selector["value_code"] else "manual"),
            ("selector_control_key", selector["control_key"]),
            ("selector_value_index", selector["value_index"]),
            ("selector_code_index", selector["code_index"]),
            ("selector_source", "entryRowClick"),
        ])

    # --- 从 update_fields 步骤中提取环境相关字段和日期字段 ---
    for s in cleaned:
        if s.get("type") != "update_fields":
            continue
        fields = s.get("fields")
        if not isinstance(fields, dict):
            continue
        step_form_id = s.get("form_id") or main_form
        for fk in fields:
            fk_lower = fk.lower()
            if fk_lower in _PF_ENV_RELATED_FIELDS:
                step_id = _scoped_pick_field_id(
                    f"pick_{fk_lower}_id",
                    pick_fields_map,
                    form_id=step_form_id,
                    source_step_id=s.get("id", ""),
                )
                if not step_id:
                    continue
                fv = fields[fk]
                parts = _extract_basedata_parts(fv)
                if parts:
                    display_val = parts.get("value_code") or parts.get("value_name") or ""
                    display_name = parts.get("value_name") or display_val
                elif isinstance(fv, dict):
                    display_val = fv.get("zh_CN", "") or str(fv)
                    display_name = display_val
                elif isinstance(fv, str):
                    display_val = fv
                    display_name = display_val
                elif fv is not None:
                    display_val = str(fv)
                    display_name = display_val
                else:
                    display_val = ""
                    display_name = ""
                observed_parts = (
                    (field_observations.get("response_values_by_form") or {})
                    .get(step_form_id, {})
                    .get(fk_lower, {})
                )
                if (
                    s.get("_is_recorded_default")
                    and isinstance(observed_parts, dict)
                    and observed_parts.get("value_name")
                ):
                    display_name = str(observed_parts.get("value_name") or display_name)
                pick_fields_map[step_id] = OrderedDict([
                    ("value_id", display_val),
                    ("value_name", display_name),
                    ("value_code", display_val if s.get("_is_recorded_default") and display_val and not _looks_like_internal_id(display_val) else ""),
                    ("value_number", display_val if s.get("_is_recorded_default") and display_val and not _looks_like_internal_id(display_val) else ""),
                    ("label", _PF_ENV_RELATED_FIELDS[fk_lower]),
                    ("env_sensitive", "medium"),
                    ("field_key", fk_lower),
                    ("form_id", step_form_id),
                    ("app_id", s.get("app_id", "")),
                    ("auto_resolve", False),
                    ("resolve_by", ""),
                    ("resolve_status", "context" if s.get("_is_recorded_default") else "manual"),
                    ("context_only", bool(s.get("_is_recorded_default"))),
                    ("source", "server_default" if s.get("_is_recorded_default") else ""),
                ])
                continue
            if fk_lower in _PF_ENUM_FIELDS:
                step_id = _scoped_pick_field_id(
                    f"pick_{fk_lower}_id",
                    pick_fields_map,
                    form_id=step_form_id,
                    source_step_id=s.get("id", ""),
                )
                if not step_id:
                    continue
                fv = fields[fk]
                display_val = str(fv) if fv is not None else ""
                combo_options = (
                    (field_observations.get("combo_options_by_form") or {})
                    .get(step_form_id, {})
                    .get(fk_lower, {})
                )
                display_name = combo_options.get(display_val, display_val)
                pick_fields_map[step_id] = OrderedDict([
                    ("value_id", display_val),
                    ("value_name", display_name),
                    ("value_code", display_val),
                    ("label", _PF_ENUM_FIELDS[fk_lower]),
                    ("env_sensitive", "low"),
                    ("field_key", fk_lower),
                    ("form_id", step_form_id),
                    ("app_id", s.get("app_id", "")),
                    ("auto_resolve", False),
                    ("resolve_by", ""),
                    ("resolve_status", "context" if s.get("_is_recorded_default") else "manual"),
                    ("context_only", bool(s.get("_is_recorded_default"))),
                    ("source", "server_default" if s.get("_is_recorded_default") else ""),
                ])
                continue
            if fk_lower in _PF_ENV_SENSITIVE_KEYWORDS or fk_lower.startswith("date_"):
                step_id = _scoped_pick_field_id(
                    f"date_{fk}",
                    pick_fields_map,
                    form_id=step_form_id,
                    source_step_id=s.get("id", ""),
                )
                if not step_id:
                    continue
                label = _resolve_field_label(fk, entity_id=step_form_id, meta_resolver=meta_resolver)
                fv = fields[fk]
                # 提取显示值（可能是字符串、多语言 dict 或 ${today}）
                if isinstance(fv, dict):
                    display_val = fv.get("zh_CN", "") or str(fv)
                elif isinstance(fv, str):
                    display_val = fv
                else:
                    display_val = str(fv) if fv else ""
                pick_fields_map[step_id] = OrderedDict([
                    ("value_id", display_val),
                    ("value_name", display_val),
                    ("label", label),
                    ("env_sensitive", "medium"),
                    ("field_key", fk),
                    ("form_id", step_form_id),
                    ("app_id", s.get("app_id", "")),
                    ("auto_resolve", False),
                    ("resolve_by", ""),
                    ("resolve_status", "manual"),
                ])
    # 注：detect_var_placeholders 对 update_fields 中的日期字段使用 ${today}
    # 内联替换，不生成 vars 变量，因此日期字段无需从 vars_map 去重

    # --- 去重：从 vars_map 中移除被 pick_fields 覆盖的变量 ---
    # 核心逻辑：pick_fields_map 的 key（id）去掉 "pick_" 前缀后与 vars_map 的 key 匹配
    # 例如 pick_ba_e_enterprise_id → ba_e_enterprise_id，即为重复项
    _pick_base_keys = set()
    for pf_id, pf_val in pick_fields_map.items():
        if pf_id.startswith("pick_"):
            _pick_base_keys.add(pf_id[5:])   # pick_ba_e_enterprise_id → ba_e_enterprise_id
        elif pf_id.startswith("date_"):
            _pick_base_keys.add(pf_id)        # date_effectdatebak 保持原样
        _pick_base_keys.add(pf_id)            # 也精确匹配 id 本身
        # ⭐ 直接从 field_key 构建预期变量名（更可靠的匹配路径）
        pf_field_key = pf_val.get("field_key", "") if isinstance(pf_val, dict) else ""
        if pf_field_key:
            _pick_base_keys.add(f"{pf_field_key}_id")  # ba_e_enterprise → ba_e_enterprise_id
            _pick_base_keys.add(pf_field_key)           # 也匹配 field_key 原样
    # 收集非 pick_basedata 步骤中 value_id 实际引用的 ${vars.xxx} 变量名，避免误删
    # ⭐ 注意：必须排除 pick_basedata 步骤本身的引用，因为那些引用正是被 pick_fields 覆盖的
    _step_referenced_vars = set()
    for _s in cleaned:
        if _s.get("type") == "pick_basedata":
            continue  # ⭐ 跳过 pick_basedata 步骤的引用，否则会阻止去重
        _vid = _s.get("value_id", "")
        if isinstance(_vid, str):
            for _m in re.finditer(r'\$\{vars\.(\w+)\}', _vid):
                _step_referenced_vars.add(_m.group(1))

    log.debug("[build_yaml_case dedup] vars_map keys BEFORE: %s", list(vars_map.keys()))
    log.debug("[build_yaml_case dedup] _pick_base_keys: %s", _pick_base_keys)
    log.debug("[build_yaml_case dedup] _step_referenced_vars: %s", _step_referenced_vars)

    for vname in list(vars_map.keys()):
        if vname in _pick_base_keys and vname not in _step_referenced_vars:
            vars_map.pop(vname)
            vars_labels.pop(vname, None)

    log.debug("[build_yaml_case dedup] vars_map keys AFTER: %s", list(vars_map.keys()))

    # --- 应用 pick_field_overrides ---
    if pick_field_overrides:
        for pf_id, pf_cfg in pick_field_overrides.items():
            if isinstance(pf_cfg, dict) and pf_id in pick_fields_map:
                current_value_id = str(pick_fields_map[pf_id].get("value_id", ""))
                current_value_name = str(pick_fields_map[pf_id].get("value_name", "") or "")
                incoming_value_id = str(pf_cfg.get("value_id", current_value_id))
                manual_override = bool(
                    pf_cfg.get("manual_override")
                    or pf_cfg.get("resolve_status") == "manual"
                )
                if incoming_value_id != current_value_id and pf_cfg.get("resolve_status") == "manual":
                    manual_override = True
                if "value_id" in pf_cfg:
                    pick_fields_map[pf_id]["value_id"] = incoming_value_id
                if "value_name" in pf_cfg:
                    incoming_value_name = str(pf_cfg["value_name"] or "")
                    if manual_override and incoming_value_name == current_value_name and incoming_value_id != current_value_id:
                        incoming_value_name = ""
                    pick_fields_map[pf_id]["value_name"] = incoming_value_name
                if "value_code" in pf_cfg:
                    pick_fields_map[pf_id]["value_code"] = str(pf_cfg["value_code"] or "")
                elif manual_override and incoming_value_id != current_value_id:
                    pick_fields_map[pf_id]["value_code"] = ""
                if "value_number" in pf_cfg:
                    pick_fields_map[pf_id]["value_number"] = str(pf_cfg["value_number"] or "")
                if "resolve_by" in pf_cfg:
                    pick_fields_map[pf_id]["resolve_by"] = str(pf_cfg["resolve_by"] or "")
                if "auto_resolve" in pf_cfg:
                    pick_fields_map[pf_id]["auto_resolve"] = bool(pf_cfg["auto_resolve"])
                if "resolve_status" in pf_cfg and not manual_override:
                    pick_fields_map[pf_id]["resolve_status"] = str(pf_cfg["resolve_status"] or "")
                if pf_cfg.get("user_overridden"):
                    pick_fields_map[pf_id]["user_overridden"] = True
                if manual_override:
                    pick_fields_map[pf_id]["auto_resolve"] = False
                    pick_fields_map[pf_id]["resolve_status"] = "manual"
                    pick_fields_map[pf_id]["manual_override"] = True
                elif (
                    str(pick_fields_map[pf_id].get("value_code") or "").strip()
                    and _looks_like_internal_id(pick_fields_map[pf_id].get("value_id"))
                ):
                    pick_fields_map[pf_id]["recorded_value_id"] = str(
                        pick_fields_map[pf_id].get("recorded_value_id")
                        or pick_fields_map[pf_id].get("value_id")
                        or ""
                    )
                    pick_fields_map[pf_id]["value_id"] = str(pick_fields_map[pf_id].get("value_code") or "")
                    pick_fields_map[pf_id]["auto_resolve"] = True
                    pick_fields_map[pf_id]["resolve_by"] = "value_code"
                    pick_fields_map[pf_id]["resolve_status"] = "pending"

    # ⭐ 应用用户的变量配置覆盖（来自 HAR 向导的变量面板）
    if var_overrides:
        for vname, cfg in var_overrides.items():
            if isinstance(cfg, dict):
                if not cfg.get("enabled", True):
                    vars_map.pop(vname, None)
                elif "template" in cfg:
                    vars_map[vname] = cfg["template"]
            elif isinstance(cfg, str):
                # 直接传模板字符串
                vars_map[vname] = cfg

    _attach_pick_field_scopes(pick_fields_map, cleaned, main_form)

    case_name = case_name or har_path.stem

    # 清理 YAML 输出用的字段（去掉以 _ 开头的内部字段）
    yaml_steps = []
    for s in cleaned:
        entry = OrderedDict()
        for k in ("id", "type", "form_id", "app_id", "ac", "key", "method",
                  "args", "post_data", "fields", "field_key", "value_id",
                  "value_name", "value_code", "value_number",
                  "row_index", "lazy", "keep_page", "invalidate_pages", "optional",
                  "target_form", "target_forms", "env_sensitive", "resolve_by",
                  "navigation_form_id", "expected_notifications",
                  "continue_on_expected_error", "preserve_l2_page", "bind_l2_only",
                  "prefetch_lookup", "prefetch_lookup_args"):
            if k in s:
                entry[k] = s[k]
        # ⭐ 自动生成步骤业务描述
        desc = generate_step_description(s)
        if desc:
            entry["description"] = desc
        yaml_steps.append(entry)

    # 组装 vars
    built_vars = OrderedDict()
    if not vars_map:
        built_vars["_hint"] = "在此声明变量，steps 中用 ${vars.xxx} 引用"
    else:
        built_vars.update(vars_map)

    # 保存变量标签（供前端显示中文标注）
    built_vars_labels = OrderedDict()
    if vars_labels:
        built_vars_labels.update(vars_labels)

    vars_meta_all = _infer_vars_meta_from_steps(yaml_steps, main_form)
    built_vars_meta = OrderedDict(
        (name, vars_meta_all[name])
        for name in built_vars.keys()
        if name in vars_meta_all and not name.startswith("_")
    )

    case = OrderedDict([
        ("name", case_name),
        ("description", _build_case_description(
            har_path=har_path, main_form=main_form, yaml_steps=yaml_steps,
            core_count=core_count, ui_count=ui_count, noise_count=noise_count,
        )),
        ("env", OrderedDict([
            ("base_url", "${env:COSMIC_BASE_URL:https://feature.kingdee.com:1026/feature_sit_hrpro}"),
            ("username", "${env:COSMIC_USERNAME}"),
            ("password", "${env:COSMIC_PASSWORD}"),
            ("datacenter_id", "${env:COSMIC_DATACENTER_ID}"),
        ])),
        ("vars", built_vars),
        ("vars_labels", built_vars_labels),  # 变量中文标签
        ("vars_meta", built_vars_meta),       # 变量来源表单/业务块，供长链路分组维护
        ("pick_fields", pick_fields_map if pick_fields_map else OrderedDict()),
        ("main_form_id", main_form),
        ("steps", yaml_steps),
        ("assertions", _build_default_assertions(yaml_steps)),
    ])
    if include_readback_assertions:
        _append_readback_assertions(case)

    trim_note = (f"# 已裁剪前 {trimmed_skipped} 条首页/门户步骤（与主流程无关）"
                 if trimmed_skipped else "")
    header_lines = [
        f"# 自动生成的回放用例（智能起步稿 v2）",
        f"# 来源: {har_path.name}",
        f"# 主表单: {main_form}",
        f"# ",
        f"# 原始 HAR 包含 {core_count} 个核心动作 + {ui_count} 个 UI 联动 + {noise_count} 个噪声",
    ]
    if trim_note:
        header_lines.append(trim_note)

    # ⭐ Step 3：知识库回流 + 反模式自检
    # 失败静默，不影响生成链路
    try:
        from lib import kb_reflow as _kbr
        _unknown_stats = _kbr.emit_unknowns(har_path, main_form, yaml_steps)
        _warnings = _kbr.check_antipatterns(har_path, main_form, yaml_steps, _unknown_stats)
        header_lines += _kbr.format_warnings_for_yaml(_warnings)
    except Exception:
        pass

    header_lines += [
        f"# 已自动：合并连续 updateValue / 降级 setItemByIdFromClient → pick_basedata /",
        f"#         去重 open_form / 抽取 vars / 过滤 noise / 语义化 step id",
        f"# ",
        f"# 人工审查建议：",
        f"#   1. 检查 vars: 是否都是期望的随机/动态值",
        f"#   2. 检查 pick_basedata 的 value_id: 换环境可能失效（参考 scaling.md 抽到 env 配置）",
        f"#   3. 标 optional 的 step: 如果回归失败可补回正式 step",
        f"#   4. 检查 assertions: 是否需要加 response_contains 断言",
        f"",
    ]
    return "\n".join(header_lines) + to_yaml(case) + "\n"


# ---------- 数据结构供 webui 使用 ----------

def _var_category(vname: str) -> str:
    """变量分类标签（用于 UI 展示）。"""
    if "number" in vname or "code" in vname:
        return "编码"
    if "name" in vname:
        return "名称"
    if "phone" in vname:
        return "电话"
    if "cert" in vname:
        return "证件号"
    return "其他"


def _infer_labels_from_preview_steps(steps: list[dict]) -> dict[str, str]:
    """从预览步骤的 description / field_key 反查每个变量的业务中文名。

    用于「变量配置」面板，让 label 与左侧"执行步骤"中「...」内的文字严格对齐。

    反查来源（按优先级）：
      1. update_fields.fields 里某字段引用 ${vars.KEY}，且 step.description
         命中 generate_step_description 的 "填写「xxx」" 格式 → 取「」内文字
      2. pick_basedata.value_id 引用 ${vars.KEY}，且 step.description
         命中 "选择「xxx」" → 取「」内文字，再回落 _FIELD_LABELS 映射

    返回：{vname: 业务中文名}。未能反查的变量不写入。
    """
    labels: dict[str, str] = {}
    ref_re = re.compile(r"\$\{vars\.([A-Za-z_][A-Za-z0-9_]*)\}")
    zh_re = re.compile(r"[「【]([^」】]+)[」】]")

    def _extract_refs(obj, acc: set):
        """递归扫描任意结构，抓取 ${vars.KEY} 引用的 KEY 集合。"""
        if isinstance(obj, str):
            for k in ref_re.findall(obj):
                acc.add(k)
        elif isinstance(obj, dict):
            for v in obj.values():
                _extract_refs(v, acc)
        elif isinstance(obj, list):
            for v in obj:
                _extract_refs(v, acc)

    for s in steps or []:
        if not isinstance(s, dict):
            continue
        t = s.get("type")
        # 由于 preview_har 内 steps 尚未生成 description，这里当场生成一次
        try:
            desc = generate_step_description(s)
        except Exception:
            desc = ""
        zh_m = zh_re.search(desc or "")
        biz_name = zh_m.group(1).strip() if zh_m else ""

        if t == "update_fields":
            refs: set = set()
            _extract_refs(s.get("fields"), refs)
            if len(refs) == 1 and biz_name:
                # 单字段场景：直接把业务名归属这个变量
                labels.setdefault(next(iter(refs)), biz_name)
            elif len(refs) > 1:
                # 多字段场景：逐字段根据 field_key → _FIELD_LABELS 映射
                fields = s.get("fields") or {}
                for fk, fv in fields.items():
                    sub_refs: set = set()
                    _extract_refs(fv, sub_refs)
                    if len(sub_refs) == 1:
                        label = _resolve_field_label(str(fk))
                        if label and label != str(fk):
                            labels.setdefault(next(iter(sub_refs)), label)

        elif t == "pick_basedata":
            vid = s.get("value_id")
            if not isinstance(vid, str):
                continue
            refs_m = ref_re.findall(vid)
            if not refs_m:
                continue
            vname = refs_m[0]
            # 优先从 description 里取「...」；否则用 field_key 查知识库/_FIELD_LABELS
            if biz_name:
                labels.setdefault(vname, biz_name)
            else:
                fk = str(s.get("field_key") or "").lower()
                lbl = _resolve_field_label(fk)
                if lbl and lbl != fk:
                    labels.setdefault(vname, lbl)

    return labels


def preview_har(har_path: Path, meta_resolver=None) -> dict:
    """只预览不落盘。供 webui 展示用。"""
    import copy
    har = load_har(har_path)
    field_observations = _collect_har_field_observations(har)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)
    raw_steps = _drop_locked_update_fields(raw_steps, field_observations)

    by_tier = {"core": 0, "ui_reaction": 0, "noise": 0}
    for s in raw_steps:
        by_tier[s.get("_tier", "ui_reaction")] = by_tier.get(s.get("_tier", "ui_reaction"), 0) + 1

    main_form = infer_main_form(raw_steps)
    preview_steps = _inject_context_field_steps(
        list(raw_steps),
        main_form,
        meta_resolver=meta_resolver,
    )
    preview_steps = _drop_locked_update_fields(preview_steps, field_observations)
    _mark_navigation_steps_optional(preview_steps, main_form)
    _preview_app_id = next((s.get("app_id", "") for s in preview_steps if s.get("form_id") == main_form), "")
    preview_steps = _append_recorded_default_pick_steps(
        preview_steps,
        field_observations,
        main_form=main_form,
        app_id=_preview_app_id,
    )
    preview_steps = _append_recorded_default_update_steps(
        preview_steps,
        field_observations,
        main_form=main_form,
        app_id=_preview_app_id,
    )
    _mark_recorded_business_validations(preview_steps)

    # ⭐ 变量预检测：提前运行变量检测逻辑，让用户在导入前可配置
    preview_copy = copy.deepcopy(preview_steps)
    _, detected_vars, detected_labels = detect_var_placeholders(preview_copy, meta_resolver=meta_resolver)
    detected_vars.pop("_date_replaced", None)

    # ⭐ step.description 反查业务名：与左侧"执行步骤"的「...」内文字严格对齐
    step_label_map = _infer_labels_from_preview_steps(preview_copy)
    vars_meta_map = _infer_vars_meta_from_steps(preview_copy, main_form)

    var_items = []
    for vname, template in detected_vars.items():
        # label 优先级（高→低）：
        #   1. step.description 反查  —— 与执行步骤「...」对齐（最精准）
        #   2. detect_var_placeholders 生成的 vars_labels  —— 来自 _FIELD_LABELS
        #   3. _var_category()  —— 兜底粗分类（编码/名称/电话/证件号/其他）
        label = (
            step_label_map.get(vname)
            or (detected_labels or {}).get(vname)
            or _var_category(vname)
        )
        var_items.append({
            "name": vname,
            "template": str(template),
            "enabled": True,
            "category": _var_category(vname),  # 保留兼容，前端做彩色分类
            "label": label,                      # ⭐ 新增：真实业务中文名
            **dict(vars_meta_map.get(vname, {})),
        })

    # ⭐ 环境相关字段 pick_fields 提取
    # 复用 build_yaml_case 中的 ENV_RELATED_FIELDS / ENUM_FIELDS 判断逻辑
    _ENV_RELATED_FIELDS = {
        "ba_e_enterprise": "企业",
        "ba_po_adminorg": "行政组织",
        "ba_po_position": "职位",
        "ba_org": "组织",
        "ba_dept": "部门",
        "ba_company": "公司",
        "enterprise": "企业",
        "adminorg": "行政组织",
        "position": "职位",
        "org": "组织",
        "dept": "部门",
        "createorg": "创建组织",
        "useorg": "使用组织",
    }
    _ENUM_FIELDS = {
        "gender": "性别",
        "certificatetype": "证件类型",
        "ba_e_laborrelstatus": "用工状态",
        "laborreltypecls": "用工关系分类",
        "ctrlstrategy": "控制策略",
        "status": "状态",
        "type": "类型",
    }
    # 环境敏感关键字（日期类字段）
    _ENV_SENSITIVE_KEYWORDS = (
        "effectdate", "effectdatebak", "loseeffectdate",
        "bsed", "bsled", "startdate", "enddate",
    )

    pick_fields: list[dict] = []
    _seen_pick_map: OrderedDict[str, dict] = OrderedDict()

    for s in preview_steps:
        if s.get("type") == "pick_basedata":
            field_key = s.get("field_key", "")
            value_id = s.get("value_id", "")
            if not field_key or not value_id:
                continue

            # 确定 step_id（与 build_yaml_case 命名规则一致）
            if field_key in _ENV_RELATED_FIELDS:
                base_step_id = f"pick_{field_key}_id"
            else:
                _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
                base_step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            step_form_id = s.get("form_id") or main_form
            step_id = _scoped_pick_field_id(
                base_step_id,
                _seen_pick_map,
                form_id=step_form_id,
                source_step_id=s.get("id", ""),
            )
            if not step_id:
                continue

            # 判断 env_sensitive 级别
            if field_key in _ENV_RELATED_FIELDS:
                env_sensitive = "medium"
            elif field_key in _ENUM_FIELDS:
                env_sensitive = "low"
            elif field_key.lower() in _ENV_SENSITIVE_KEYWORDS:
                env_sensitive = "medium"
            else:
                env_sensitive = "low"

            # ⭐ 实时元数据增强 env_sensitive 分级
            field_type = None
            if meta_resolver and step_form_id:
                field_type = meta_resolver.get_field_type(step_form_id, field_key)
                if field_type:
                    if field_type in ("BasedataProp", "MulBasedataProp", "OrgProp", "UserProp"):
                        env_sensitive = "medium"
                    elif field_type in ("ComboProp", "MulComboProp", "BooleanProp"):
                        env_sensitive = "low"

            # 标签优先级：ENV_RELATED_FIELDS > ENUM_FIELDS > 实时元数据/知识库/_FIELD_LABELS > field_key
            label = (
                _ENV_RELATED_FIELDS.get(field_key)
                or _ENUM_FIELDS.get(field_key)
                or _resolve_field_label(field_key, entity_id=step_form_id, meta_resolver=meta_resolver)
            )
            value_code = s.get("value_code", "") or ""
            display_value_id = _display_pick_value_id(value_id, value_code)
            auto_meta = _pick_field_auto_resolve_meta(
                field_key,
                display_value_id,
                s.get("value_name", "") or "",
                env_sensitive,
                field_type,
                value_code=value_code,
            )
            if value_code and _looks_like_internal_id(value_id):
                auto_meta = {
                    "auto_resolve": True,
                    "resolve_by": "value_code",
                    "resolve_status": "pending",
                }

            item = {
                "id": step_id,
                "field_key": field_key,
                "label": label,
                "env_sensitive": env_sensitive,
                "value_id": display_value_id,
                "value_name": s.get("value_name", "") or "",
                "value_code": value_code,
                "value_number": s.get("value_number", "") or "",
                "recorded_value_id": str(value_id),
                "form_id": step_form_id,
                "app_id": s.get("app_id", ""),
                "auto_resolve": auto_meta["auto_resolve"],
                "resolve_by": auto_meta["resolve_by"],
                "resolve_status": auto_meta["resolve_status"],
            }
            pick_fields.append(item)
            _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}

        elif s.get("type") == "invoke" and s.get("ac") in ("new", "addnew"):
            focus_id, focus_name = _extract_treeview_focus(s)
            if not focus_id and not focus_name:
                continue
            step_id = f"env_{s.get('id', 'addnew')}_treeview_focus"
            if step_id in _seen_pick_map:
                continue
            item = {
                "id": step_id,
                "field_key": "treeview.focus.id",
                "label": "新增上下文组织",
                "env_sensitive": "high",
                "value_id": str(focus_id),
                "value_name": focus_name,
                "form_id": s.get("form_id", ""),
                "app_id": s.get("app_id", ""),
                "auto_resolve": False,
                "resolve_by": "",
                "resolve_status": "manual",
            }
            pick_fields.append(item)
            _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}

        elif s.get("type") == "invoke" and s.get("ac") == "entryRowClick":
            selector = _extract_entry_row_selector(s)
            if not selector:
                continue
            step_form_id = s.get("form_id") or ""
            step_id = _scoped_pick_field_id(
                f"selector_{selector['selector_key']}_id",
                _seen_pick_map,
                form_id=step_form_id,
                source_step_id=s.get("id", ""),
            )
            if not step_id:
                continue
            item = {
                "id": step_id,
                "field_key": selector["field_key"],
                "label": selector["label"],
                "env_sensitive": "medium",
                "value_id": selector["value_code"] or selector["value_id"],
                "value_name": selector["value_name"],
                "value_code": selector["value_code"],
                "recorded_value_id": selector["value_id"],
                "form_id": step_form_id,
                "app_id": s.get("app_id", ""),
                "source_step_id": s.get("id", ""),
                "auto_resolve": True if selector["value_code"] else False,
                "resolve_by": "value_code" if selector["value_code"] else "",
                "resolve_status": "pending" if selector["value_code"] else "manual",
                "selector_control_key": selector["control_key"],
                "selector_value_index": selector["value_index"],
                "selector_code_index": selector["code_index"],
                "selector_source": "entryRowClick",
            }
            pick_fields.append(item)
            _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}

        elif s.get("type") == "update_fields":
            # 处理 update_fields 中的环境字段和日期字段
            fields = s.get("fields")
            if not isinstance(fields, dict):
                continue
            step_form_id = s.get("form_id") or main_form
            for fk in fields:
                fk_lower = fk.lower()
                if fk_lower in _ENV_RELATED_FIELDS:
                    step_id = _scoped_pick_field_id(
                        f"pick_{fk_lower}_id",
                        _seen_pick_map,
                        form_id=step_form_id,
                        source_step_id=s.get("id", ""),
                    )
                    if not step_id:
                        continue
                    fv = fields[fk]
                    parts = _extract_basedata_parts(fv)
                    if parts:
                        display_val = parts.get("value_code") or parts.get("value_name") or ""
                        display_name = parts.get("value_name") or display_val
                    elif isinstance(fv, dict):
                        display_val = fv.get("zh_CN", "") or str(fv)
                        display_name = display_val
                    elif isinstance(fv, str):
                        display_val = fv
                        display_name = display_val
                    else:
                        display_val = str(fv) if fv is not None else ""
                        display_name = display_val
                    observed_parts = (
                        (field_observations.get("response_values_by_form") or {})
                        .get(step_form_id, {})
                        .get(fk_lower, {})
                    )
                    if (
                        s.get("_is_recorded_default")
                        and isinstance(observed_parts, dict)
                        and observed_parts.get("value_name")
                    ):
                        display_name = str(observed_parts.get("value_name") or display_name)
                    item = {
                        "id": step_id,
                        "field_key": fk_lower,
                        "label": _ENV_RELATED_FIELDS[fk_lower],
                        "env_sensitive": "medium",
                        "value_id": display_val,
                        "value_name": display_name,
                        "form_id": step_form_id,
                        "app_id": s.get("app_id", ""),
                        "auto_resolve": False,
                        "resolve_by": "",
                        "resolve_status": "context" if s.get("_is_recorded_default") else "manual",
                        "context_only": bool(s.get("_is_recorded_default")),
                        "source": "server_default" if s.get("_is_recorded_default") else "",
                    }
                    pick_fields.append(item)
                    _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}
                    continue
                if fk_lower in _ENUM_FIELDS:
                    step_id = _scoped_pick_field_id(
                        f"pick_{fk_lower}_id",
                        _seen_pick_map,
                        form_id=step_form_id,
                        source_step_id=s.get("id", ""),
                    )
                    if not step_id:
                        continue
                    fv = fields[fk]
                    display_val = str(fv) if fv is not None else ""
                    combo_options = (
                        (field_observations.get("combo_options_by_form") or {})
                        .get(step_form_id, {})
                        .get(fk_lower, {})
                    )
                    item = {
                        "id": step_id,
                        "field_key": fk_lower,
                        "label": _ENUM_FIELDS[fk_lower],
                        "env_sensitive": "low",
                        "value_id": display_val,
                        "value_name": combo_options.get(display_val, display_val),
                        "value_code": display_val,
                        "form_id": step_form_id,
                        "app_id": s.get("app_id", ""),
                        "auto_resolve": False,
                        "resolve_by": "",
                        "resolve_status": "context" if s.get("_is_recorded_default") else "manual",
                        "context_only": bool(s.get("_is_recorded_default")),
                        "source": "server_default" if s.get("_is_recorded_default") else "",
                    }
                    pick_fields.append(item)
                    _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}
                    continue
                if fk_lower in _ENV_SENSITIVE_KEYWORDS or fk_lower.startswith("date_"):
                    step_id = _scoped_pick_field_id(
                        f"date_{fk}",
                        _seen_pick_map,
                        form_id=step_form_id,
                        source_step_id=s.get("id", ""),
                    )
                    if not step_id:
                        continue

                    label = _resolve_field_label(fk, entity_id=step_form_id, meta_resolver=meta_resolver)
                    fv = fields[fk]
                    # 提取值（可能是字符串或多语言 dict）
                    if isinstance(fv, dict):
                        display_val = fv.get("zh_CN", "") or str(fv)
                    elif isinstance(fv, str):
                        display_val = fv
                    else:
                        display_val = str(fv) if fv else ""

                    item = {
                        "id": step_id,
                        "field_key": fk,
                        "label": label,
                        "env_sensitive": "medium",
                        "value_id": display_val,
                        "value_name": display_val,
                        "form_id": step_form_id,
                        "app_id": s.get("app_id", ""),
                        "auto_resolve": False,
                        "resolve_by": "",
                        "resolve_status": "manual",
                    }
                    pick_fields.append(item)
                    _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}

    _preview_app_id = next((s.get("app_id", "") for s in preview_steps if s.get("form_id") == main_form), "")
    for _field_key in sorted(_DEFAULT_CONTEXT_FIELD_KEYS):
        _step_id = f"pick_{_field_key}_id"
        if _step_id in _seen_pick_map:
            continue
        _ctx_item = _make_context_default_pick_field(
            _field_key,
            (field_observations.get("response_values") or {}).get(_field_key) or {},
            main_form=main_form,
            app_id=_preview_app_id,
        )
        if _ctx_item is None:
            continue
        item = {"id": _step_id, **dict(_ctx_item)}
        pick_fields.append(item)
        _seen_pick_map[_step_id] = {k: v for k, v in item.items() if k != "id"}

    _preview_pick_map = OrderedDict()
    for pf in pick_fields:
        pf_id = str(pf.get("id") or "")
        if pf_id:
            item = OrderedDict((k, v) for k, v in pf.items() if k != "id")
            _preview_pick_map[pf_id] = item
    _attach_pick_field_scopes(_preview_pick_map, preview_steps, main_form)
    _annotate_env_field_sources(
        _preview_pick_map,
        field_observations,
        meta_resolver=meta_resolver,
    )
    pick_fields = [{"id": pf_id, **dict(meta)} for pf_id, meta in _preview_pick_map.items()]

    # 按 env_sensitive 排序：high(0) → medium(1) → low(2)
    _sens_order = {"high": 0, "medium": 1, "low": 2}
    pick_fields.sort(key=lambda pf: _sens_order.get(pf["env_sensitive"], 9))

    # ⭐ 去重：把已被 pick_fields 覆盖的变量从 var_items 移除
    # 核心逻辑：pick_fields 的 id 去掉 "pick_" 前缀后，与 detected_vars 的 name 匹配
    # 例如 pick_ba_e_enterprise_id → ba_e_enterprise_id，即为重复项
    _pick_base_keys: set = set()
    for pf in pick_fields:
        pf_id = pf.get("id", "").strip()
        pf_field_key = pf.get("field_key", "").strip()
        if pf_id.startswith("pick_"):
            _pick_base_keys.add(pf_id[5:])   # pick_ba_e_enterprise_id → ba_e_enterprise_id
        elif pf_id.startswith("date_"):
            _pick_base_keys.add(pf_id)        # date_effectdatebak 保持原样
        if pf_id:
            _pick_base_keys.add(pf_id)        # 也精确匹配 id 本身
        # ⭐ 直接从 field_key 构建预期变量名（更可靠的匹配路径）
        if pf_field_key:
            _pick_base_keys.add(f"{pf_field_key}_id")     # ba_e_enterprise → ba_e_enterprise_id
            _pick_base_keys.add(pf_field_key)              # 也匹配 field_key 原样

    log.debug("[preview_har dedup] var_items BEFORE: %s", [v['name'] for v in var_items])
    log.debug("[preview_har dedup] _pick_base_keys: %s", _pick_base_keys)

    if _pick_base_keys:
        var_items = [v for v in var_items if v["name"] not in _pick_base_keys]

    log.debug("[preview_har dedup] var_items AFTER: %s", [v['name'] for v in var_items])

    try:
        from lib.component_registry import analyze_component_coverage
        component_report = analyze_component_coverage(preview_copy)
    except Exception as e:
        log.warning("HAR 组件分析失败（非致命）: %s", e)
        component_report = {
            "summary": {
                "total_steps": len(preview_copy),
                "supported_steps": 0,
                "partial_steps": 0,
                "unsupported_steps": len(preview_copy),
                "coverage_percent": 0,
                "risk_level": "high",
            },
            "handlers": [],
            "components": [],
            "unsupported": [],
            "steps": [],
        }
    component_steps = component_report.get("steps") or []

    try:
        from lib.har_chain_probe import probe_har_chain
        har_probe = probe_har_chain(har_path)
    except Exception as e:
        log.warning("HAR 链路画像失败（非致命）: %s", e)
        har_probe = {}
    try:
        from lib.har_preflight import assess_pageid_alignment
        pageid_alignment = assess_pageid_alignment(preview_copy, har_probe=har_probe)
    except Exception as e:
        log.warning("pageId 链路评分失败（非致命）: %s", e)
        pageid_alignment = {
            "score": 0,
            "grade": "E",
            "risk_level": "high",
            "summary": f"pageId 评分失败: {type(e).__name__}: {e}",
            "issues": [],
            "checks": {},
            "steps": [],
        }

    try:
        from lib.ir import assess_ir_preview_alignment, build_normalized_flow, compact_flow_for_preview
        ir_flow = build_normalized_flow(har, source_name=har_path.name)
        ir_preview = compact_flow_for_preview(ir_flow)
        ir_alignment = assess_ir_preview_alignment(
            ir_flow,
            preview_steps=preview_copy,
            detected_vars=var_items,
            pick_fields=pick_fields,
        )
    except Exception as e:
        log.warning("HAR IR 对齐诊断失败（非致命）: %s", e)
        ir_preview = {
            "schema_version": "0.1",
            "confidence_score": 0,
            "source_har": {"entry_count": 0, "api_entry_count": 0, "redacted": True},
            "step_count": 0,
            "page_count": 0,
            "sensitive_field_count": 0,
            "warnings": [{"code": "ir_preview_failed", "message": f"{type(e).__name__}: {e}"}],
            "steps": [],
            "pages": [],
        }
        ir_alignment = {
            "score": 0,
            "grade": "E",
            "risk_level": "high",
            "summary": f"IR 对齐诊断失败: {type(e).__name__}: {e}",
            "issues": [{"severity": "medium", "code": "ir_alignment_failed", "message": str(e), "suggestion": "查看 HAR 是否可正常脱敏和规范化。"}],
            "checks": {},
        }

    preview = {
        "main_form_id": main_form,
        "tier_counts": by_tier,
        "detected_vars": var_items,
        "pick_fields": pick_fields,
        "business_blocks": _build_preview_business_blocks(var_items, pick_fields),
        "readback_plan": _build_preview_readback_plan(main_form, var_items),
        "components": component_report,
        "pageid_alignment": pageid_alignment,
        "ir_preview": ir_preview,
        "ir_alignment": ir_alignment,
        "steps": [
            {
                "id": s.get("id"),
                "type": s.get("type"),
                "tier": s.get("_tier"),
                "form_id": s.get("form_id"),
                "ac": s.get("ac"),
                "optional": bool(s.get("optional")),
                "component": (component_steps[i] or {}).get("component", "") if i < len(component_steps) else "",
                "component_handler": (component_steps[i] or {}).get("handler_id", "") if i < len(component_steps) else "",
                "component_support": (component_steps[i] or {}).get("support_level", "") if i < len(component_steps) else "",
                "brief": _step_brief(s),
            }
            for i, s in enumerate(preview_steps)
        ],
    }
    try:
        from lib.har_quality import assess_preview_quality
        preview["quality"] = assess_preview_quality(
            main_form_id=main_form,
            tier_counts=by_tier,
            steps=preview_copy,
            detected_vars=var_items,
            pick_fields=pick_fields,
            component_report=component_report,
        )
    except Exception as e:
        log.warning("HAR 质量评估失败（非致命）: %s", e)
        preview["quality"] = {
            "score": 0,
            "grade": "E",
            "blocking": True,
            "summary": f"质量评估失败: {type(e).__name__}: {e}",
            "dimensions": [],
            "issues": [],
            "checks": {},
        }
    try:
        from lib.har_preflight import assess_har_preflight
        preview["preflight"] = assess_har_preflight(
            main_form_id=main_form,
            tier_counts=by_tier,
            steps=preview_copy,
            detected_vars=var_items,
            pick_fields=pick_fields,
            component_report=component_report,
            quality=preview.get("quality"),
            pageid_alignment=pageid_alignment,
            ir_alignment=ir_alignment,
        )
    except Exception as e:
        log.warning("HAR 导入预审失败（非致命）: %s", e)
        preview["preflight"] = {
            "score": 0,
            "grade": "E",
            "decision": "blocked",
            "allow_generate": False,
            "recommend_generate": False,
            "summary": f"导入预审失败: {type(e).__name__}: {e}",
            "issues": [],
            "checks": {},
            "next_actions": ["请先查看高级诊断或重新上传 HAR。"],
        }
    return preview


def _step_brief(s: dict) -> str:
    t = s.get("type")
    if t == "open_form":
        return f"open {s.get('form_id')}"
    if t == "update_fields":
        fs = list(s.get("fields", {}).keys())
        return f"fill fields: {', '.join(fs[:5])}" + ("..." if len(fs) > 5 else "")
    if t == "pick_basedata":
        return f"pick {s.get('field_key')} = {s.get('value_id')}"
    if t == "invoke":
        return f"{s.get('ac')} · key={s.get('key','')} · method={s.get('method','')}"
    return str(t)


# ---------- CLI ----------

def main():
    try:
        sys.stdout.reconfigure(encoding="utf-8", errors="replace")
        sys.stderr.reconfigure(encoding="utf-8", errors="replace")
    except Exception:
        pass
    ap = argparse.ArgumentParser(description="HAR → YAML 用例起步稿（智能化版）")
    sub = ap.add_subparsers(dest="cmd", required=True)

    p_ext = sub.add_parser("extract", help="从 HAR 抽取用例")
    p_ext.add_argument("har", type=Path)
    p_ext.add_argument("-o", "--out", type=Path, required=True)
    p_ext.add_argument("-n", "--name")
    p_ext.add_argument(
        "--with-readback-assertions",
        action="store_true",
        help="按业务键附加入库只读回查断言（默认关闭，保持回归输出稳定）",
    )

    p_prev = sub.add_parser("preview", help="只预览 HAR 结构，不写文件")
    p_prev.add_argument("har", type=Path)

    args = ap.parse_args()
    if args.cmd == "extract":
        if not args.har.exists():
            print(f"ERROR: HAR not found: {args.har}", file=sys.stderr)
            sys.exit(2)
        yaml = build_yaml_case(
            args.har,
            args.name,
            include_readback_assertions=args.with_readback_assertions,
        )
        args.out.parent.mkdir(parents=True, exist_ok=True)
        args.out.write_text(yaml, encoding="utf-8")
        print(f"✓ 已生成: {args.out}")
    elif args.cmd == "preview":
        if not args.har.exists():
            print(f"ERROR: HAR not found: {args.har}", file=sys.stderr)
            sys.exit(2)
        preview = preview_har(args.har)
        print(json.dumps(preview, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
