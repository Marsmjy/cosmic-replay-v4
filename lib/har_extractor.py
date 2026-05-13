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
import re
import sys
import urllib.parse
from collections import OrderedDict
from pathlib import Path
from typing import Any


# ---------- 常量 ----------

STATIC_SUFFIX = (".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".svg",
                 ".ico", ".woff", ".woff2", ".ttf", ".eot", ".map")

# ac 分级（影响生成的 YAML 里是否标 optional、以及 optional 的类型）
AC_TIER = {
    # noise：纯 UI 装饰，完全可去
    "clientCallBack":          "noise",
    "queryExceedMaxCount":     "noise",
    "customEvent":             "noise",
    "changeYear":              "noise",
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


# 值类型识别（从 HAR 里的值反推是什么形式）
_RX_DATE = re.compile(r"^\d{4}-\d{2}-\d{2}$")
_RX_DATETIME = re.compile(r"^\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}")
_RX_TEST_NUMBER = re.compile(r"^[A-Z]{2,5}\d{3,8}$")   # TEST1234 / QA12345 等
_RX_INTEGER = re.compile(r"^\d+$")

# ⭐ 规则2：识别 session-specific pageId 模式（嵌入 root_base_id 的 32位hex）
_RX_ROOT_BASE_ID = re.compile(r"root([a-f0-9]{32})")
# 匹配纯随机 32hex pageId（不含 "root" 前缀的独立 pageId）
_RX_RANDOM_PAGE_ID = re.compile(r"^[a-f0-9]{32}$")

# ⭐ 规则5：从原始测试值中提取前缀（去掉末尾数字/随机部分）
_RX_TRAILING_DIGITS = re.compile(r"^(.*?[^0-9])\d{2,}$")


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
    'org':          '所属组织',
    'country':      '国家',
    'province':     '省份',
    'city':         '城市',
    'address':      '地址',
    'email':        '邮箱',
    'birthday':     '生日',
    'startdate':    '开始日期',
    'enddate':      '结束日期',
    # ⭐ 核心 4 HAR 涉及的 pick_basedata 业务字段
    'adminorgtype':       '行政组织类型',
    'changescene':        '变更场景',
    'laborreltypecls':    '用工关系分类',
    'basedatafield':      '基础资料字段',
    'menulocal':          '菜单位置',
    'bsed':               '生效日期',
    'bsled':              '失效日期',
    'effectdate':         '生效日期',
    'loseeffectdate':     '失效日期',
    'parentorg':          '上级行政组织',
    'orgpattern':         '组织形态',
    'biztype':            '业务类型',
    'useorg':             '使用组织',
    'createorg':          '创建组织',
    'entity':             '实体',
    'entityobjecttype':   '实体对象类型',
    'objecttype':         '对象类型',
    'fieldtype':          '字段类型',
    'tabletype':          '表类型',
}


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
                fname = _FIELD_LABELS.get(key_l, key)
                return f"选择「{fname}」基础资料"
            return "选择基础资料"
        if ac == 'pickValue':
            if key:
                fname = _FIELD_LABELS.get(key_l, key)
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
                names.append(_FIELD_LABELS.get(k.lower(), k))
            names_str = '/'.join(names)
            if len(keys) == 1:
                return f"填写「{names_str}」"
            if len(keys) <= 3:
                return f"填写「{names_str}」"
            return f"填写「{names_str}」等 {len(keys)} 个字段"
        return "填写字段"

    # ---------- pick_basedata ----------
    if step_type == 'pick_basedata':
        fname = _FIELD_LABELS.get((field_key or '').lower(),
                                  field_key.replace('_', ' ') if field_key else '基础资料')
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

def detect_var_placeholders(actions_seq: list[dict]) -> tuple[list[dict], dict[str, Any]]:
    """扫 updateValue 的值，识别"看起来像测试数据"的值抽成 vars。
    返回：(修改后的 actions_seq, vars_map)

    ⭐ 规则7（统一变量引用）：
    连续新增多条记录时，所有保存轮次统一引用同一个 test_number / test_name 变量。
    vars 在 session 初始化时只解析一次，后续步骤中 ${vars.test_number} 始终返回
    相同的已解析值，保证所有轮次编码/名称一致，UI 只显示 2 个变量。
    """
    vars_map: dict[str, Any] = {}
    vars_labels: dict[str, str] = {}  # 变量名 → 中文标签
    seen_values: dict[str, str] = {}   # 原始值 → 变量名

    # 字段 key 名暗示"必然唯一"——一定得抽 vars，否则跑第二次必挂"已存在"
    UNIQUE_KEY_HINTS = {"number", "code", "simplename", "name", "fullname",
                        "billno", "orderno"}
    NUMBER_KEYS = {"number", "code", "billno", "orderno"}
    NAME_KEYS = {"name", "simplename", "fullname"}
    # ⭐ 排除列表：后缀命中 NAME_KEYS 但不是实际名称字段的 key
    _CLASSIFY_KEY_EXCLUSIONS = {"ename", "classtypeid"}
    # HR 特定字段：需要随机化以避免重复
    HR_UNIQUE_SUFFIXES = {"empnumber", "certificatenumber", "phone"}
    HR_NAME_FIELDS = {"ba_em_name", "em_name", "staffname"}
    HR_PHONE_FIELDS = {"phone", "tel", "mobile", "cellphone", "contactphone"}

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
                kb_cls = _kb.classify_field(current_form_id, key_hint)
                if kb_cls in ("B", "ignore", "C"):
                    return None
                # kb_cls == "A" 或 None 时继续走原有启发式，以便细分前缀
            except Exception:
                pass

        # 精确匹配
        if kl in UNIQUE_KEY_HINTS:
            if kl in NUMBER_KEYS:
                return "number"
            if kl in NAME_KEYS:
                return "name"
            return "unique"
        # HR 特定字段精确匹配
        if kl in HR_NAME_FIELDS:
            return "name"
        if kl in HR_PHONE_FIELDS:
            return "phone"
        # 后缀匹配：empnumber → number，certificatenumber → cert
        for suffix in HR_UNIQUE_SUFFIXES:
            if kl.endswith(suffix):
                if "number" in suffix and "certificate" not in kl:
                    return "number"
                if "certificate" in kl:
                    return "cert"
                if suffix in ("phone", "tel", "mobile"):
                    return "phone"
                return "unique"
        # 后缀匹配通用 key hints
        for hint in NUMBER_KEYS:
            if kl.endswith(hint) and len(kl) > len(hint):
                return "number"
        # ⭐ 排除非名称类字段（如 ename 属性名称不变量化）
        if kl in _CLASSIFY_KEY_EXCLUSIONS:
            return None
        for hint in NAME_KEYS:
            if kl.endswith(hint) and len(kl) > len(hint):
                return "name"
        return None

    def maybe_var(val: Any, key_hint: str = "") -> Any:
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
                        vars_map[vname] = f"测试员${{vars.{cur_num}}}"
                    else:
                        vars_map[vname] = f"测试员${{rand:4}}"
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
                        fk = entry.get("k", "")
                        fv = entry.get("v")
                        if fk in UNIQUE_KEY_HINTS:
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
        
        if ac in _SAVE_ACS:
            pd = action_wrap.get("post_data") or [{}, []]
            if isinstance(pd, list) and len(pd) >= 2 and isinstance(pd[1], list):
                for entry in pd[1]:
                    if isinstance(entry, dict):
                        fk = entry.get("k", "")
                        fv = entry.get("v")
                        if fk in UNIQUE_KEY_HINTS:
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
                        fk = entry.get("k", "")
                        fv = entry.get("v")
                        if fk in UNIQUE_KEY_HINTS:
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
                        # 否则从 _FIELD_LABELS 通用字段映射取，
                        # 再否则用 field_key 本身作为 label
                        vars_labels[vname] = (
                            ENUM_FIELDS.get(field_key)
                            or _FIELD_LABELS.get(field_key.lower())
                            or field_key
                        )
                    action_wrap["value_id"] = f"${{vars.{vname}}}"


    # 生成变量标签（基于字段名和变量类型）
    def _generate_var_label(vname: str, key_hint: str) -> str:
        """根据变量名和字段上下文生成中文标签"""
        # 优先使用字段标签映射
        label = _FIELD_LABELS.get(key_hint.lower())
        if label:
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
                if ac == "click" and ctrl_key in _SAVE_BUTTON_KEYS:
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
                # 仅对 setItemByIdFromClient 保留响应体（提取 value_name）
                if action.get("methodName") == "setItemByIdFromClient" and _resp_text:
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
    for s in steps:
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
                    "_har_page_id": s.get("_har_page_id", ""),
                }
                if extra_row_idx >= 0:
                    fill_step["row_index"] = extra_row_idx
                out.append(fill_step)

            if value_id:
                # ⭐ 从响应体提取 value_name（响应中 "u" action 的 v[1]）
                value_name = ""
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
                                            if (isinstance(v_arr, list)
                                                    and len(v_arr) >= 2
                                                    and isinstance(v_arr[1], str)):
                                                value_name = v_arr[1]
                                            break
                                    if value_name:
                                        break
                    except Exception:
                        pass
                out.append({
                    "type": "pick_basedata",
                    "id": s["id"],
                    "form_id": s["form_id"],
                    "app_id": s["app_id"],
                    "field_key": s["key"],
                    "value_id": value_id,
                    "value_name": value_name,
                    "_tier": "core",
                    "_har_page_id": s.get("_har_page_id", ""),
                })
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
                lines.append(f"{pad}{ks}: {_yaml_scalar(v)}")
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


def _yaml_scalar(v: Any) -> str:
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
    # 1. ac 在写库白名单
    for s in yaml_steps:
        if _ac(s) in _save_acs:
            save_id = s.get("id")
            break
    # 2. key 在写库 key 白名单（覆盖 click btnsave 这类）
    if not save_id:
        for s in yaml_steps:
            if _key(s) in _save_keys:
                save_id = s.get("id")
                break
    # 3. id 含 save（兜底，兼容老生成器产物）
    if not save_id:
        for s in yaml_steps:
            sid = s.get("id", "")
            if "save" in sid.lower():
                save_id = sid
                break

    # ——找 confirm_id（挂 no_error_actions，取"最后一个写库锚点"）——
    _key_acs = {"itemClick", "click"} | _save_acs
    confirm_id = None
    # 1. 倒序：ac 在写库白名单（最可靠的写库锚点）
    for s in reversed(yaml_steps):
        if _ac(s) in _save_acs:
            confirm_id = s.get("id")
            break
    # 2. 倒序：key 在写库 key 白名单
    if not confirm_id:
        for s in reversed(yaml_steps):
            if _key(s) in _save_keys:
                confirm_id = s.get("id")
                break
    # 3. 倒序：click/itemClick 且 id 以 click_ 开头
    if not confirm_id:
        for s in reversed(yaml_steps):
            sid = s.get("id", "")
            if _ac(s) in ("click", "itemclick") and sid.startswith("click_"):
                confirm_id = sid
                break
    # 4. 兜底：任何关键动作
    if not confirm_id:
        for s in reversed(yaml_steps):
            if _ac(s) in _key_acs:
                confirm_id = s.get("id")
                break

    assertions = []
    if save_id:
        assertions.append(OrderedDict([("type", "no_save_failure"), ("step", save_id)]))
    if confirm_id:
        assertions.append(OrderedDict([("type", "no_error_actions"), ("step", confirm_id)]))
    else:
        assertions.append(OrderedDict([("type", "no_error_actions"), ("last_step", True)]))
    return assertions


def build_yaml_case(har_path: Path, case_name: str | None = None, var_overrides: dict | None = None, pick_field_overrides: dict | None = None) -> str:
    har = load_har(har_path)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)

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
    if cut_idx is None and main_form:
        for i, s in enumerate(raw_steps):
            if s.get("form_id") == main_form:
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

    # ⭐ 规则13：menuItemClick → 自动绑定 target_form + 移除冗余 open_form
    # 苍穹菜单导航：menuItemClick 创建 L2 pageId ({menuId}root{baseId})，
    # 这是主表单列表页的正确页面标识。如果之后再有 open_form(getConfig)，
    # 会获取一个独立的、不在菜单上下文中的 pageId，导致后续 addnew/save 发送到
    # 错误的页面。修复：1) 为 menuItemClick 添加 target_form 注解；2) 删除冗余 open_form。
    _menu_target_set = False
    for s in cleaned:
        if s.get("ac") == "menuItemClick" and not _menu_target_set:
            args = s.get("args", [])
            if args and isinstance(args[0], dict):
                menu_id = str(args[0].get("menuId", ""))
                if menu_id and main_form:
                    s["target_form"] = main_form
                    _menu_target_set = True
    if _menu_target_set:
        # 找 menuItemClick 的位置
        menu_idx = next((i for i, s in enumerate(cleaned) if s.get("target_form") == main_form), None)
        if menu_idx is not None:
            # 移除 menuItemClick 之后的第一个 open_form(main_form)
            # （该 open_form 会通过 getConfig 获取错误的独立 pageId）
            for i in range(menu_idx + 1, len(cleaned)):
                if cleaned[i].get("type") == "open_form" and cleaned[i].get("form_id") == main_form:
                    cleaned.pop(i)
                    break

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
    if main_form and cleaned and not _has_menu_target:
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

    # 抽 vars
    _, vars_map, vars_labels = detect_var_placeholders(cleaned)
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
    }
    _PF_ENUM_FIELDS = {
        "gender": "性别",
        "certificatetype": "证件类型",
        "ba_e_laborrelstatus": "用工状态",
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
        # 确定 step_id 和 env_sensitive
        if field_key in _PF_ENV_RELATED_FIELDS:
            step_id = f"pick_{field_key}"
            env_sensitive = "medium"
            label = _PF_ENV_RELATED_FIELDS[field_key]
        elif field_key in _PF_ENUM_FIELDS:
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
            step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            env_sensitive = "low"
            label = _PF_ENUM_FIELDS[field_key]
        elif field_key.lower() in _PF_ENV_SENSITIVE_KEYWORDS:
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
            step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            env_sensitive = "medium"
            label = _FIELD_LABELS.get(field_key.lower(), field_key)
        else:
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
            step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"
            env_sensitive = "low"
            label = _FIELD_LABELS.get(field_key.lower(), field_key)
        if step_id not in pick_fields_map:
            pick_fields_map[step_id] = OrderedDict([
                ("value_id", str(raw_value_id)),
                ("value_name", s.get("value_name", "") or ""),
                ("label", label),
                ("env_sensitive", env_sensitive),
                ("field_key", field_key),
            ])

    # --- 从 update_fields 步骤中提取日期类环境敏感字段 ---
    # 与 preview_har() 保持一致：update_fields 中的日期字段也应出现在 pick_fields 中
    for s in cleaned:
        if s.get("type") != "update_fields":
            continue
        fields = s.get("fields")
        if not isinstance(fields, dict):
            continue
        for fk in fields:
            fk_lower = fk.lower()
            if fk_lower in _PF_ENV_SENSITIVE_KEYWORDS or fk_lower.startswith("date_"):
                step_id = f"pick_{fk}"
                if step_id in pick_fields_map:
                    continue
                label = _FIELD_LABELS.get(fk_lower) or fk
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
                    ("value_name", ""),
                    ("label", label),
                    ("env_sensitive", "medium"),
                    ("field_key", fk),
                ])
    # 注：detect_var_placeholders 对 update_fields 中的日期字段使用 ${today}
    # 内联替换，不生成 vars 变量，因此日期字段无需从 vars_map 去重

    # --- 去重：从 vars_map 中移除被 pick_fields 覆盖的变量 ---
    _pick_field_var_names = set()
    for pf_id, pf_data in pick_fields_map.items():
        fk = pf_data.get("field_key", "")
        if fk:
            _pick_field_var_names.add(f"{fk}_id")
            _pick_field_var_names.add(fk)
            _pick_field_var_names.add(f"pick_{fk}_id")
            _pick_field_var_names.add(f"pick_{fk}")
            _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", fk).strip("_")
            if _sanitized:
                _pick_field_var_names.add(f"pick_{_sanitized}_id")
        _pick_field_var_names.add(pf_id)
    for vname in list(vars_map.keys()):
        if vname in _pick_field_var_names:
            vars_map.pop(vname)
            vars_labels.pop(vname, None)

    # --- 应用 pick_field_overrides ---
    if pick_field_overrides:
        for pf_id, pf_cfg in pick_field_overrides.items():
            if isinstance(pf_cfg, dict) and pf_id in pick_fields_map:
                if "value_id" in pf_cfg:
                    pick_fields_map[pf_id]["value_id"] = pf_cfg["value_id"]
                if "value_name" in pf_cfg:
                    pick_fields_map[pf_id]["value_name"] = pf_cfg["value_name"]

    # ⭐ 应用用户的变量配置覆盖（来自 HAR 向导的变量面板）
    if var_overrides:
        for vname, cfg in var_overrides.items():
            if isinstance(cfg, dict):
                if not cfg.get("enabled", True):
                    vars_map.pop(vname, None)
                elif "template" in cfg and cfg["template"]:
                    vars_map[vname] = cfg["template"]
            elif isinstance(cfg, str):
                # 直接传模板字符串
                vars_map[vname] = cfg

    case_name = case_name or har_path.stem

    # 清理 YAML 输出用的字段（去掉以 _ 开头的内部字段）
    yaml_steps = []
    for s in cleaned:
        entry = OrderedDict()
        for k in ("id", "type", "form_id", "app_id", "ac", "key", "method",
                  "args", "post_data", "fields", "field_key", "value_id",
                  "row_index", "lazy", "keep_page", "invalidate_pages", "optional",
                  "target_form"):
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
        ("pick_fields", pick_fields_map if pick_fields_map else OrderedDict()),
        ("main_form_id", main_form),
        ("steps", yaml_steps),
        ("assertions", _build_default_assertions(yaml_steps)),
    ])

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
                        label = _FIELD_LABELS.get(str(fk).lower())
                        if label:
                            labels.setdefault(next(iter(sub_refs)), label)

        elif t == "pick_basedata":
            vid = s.get("value_id")
            if not isinstance(vid, str):
                continue
            refs_m = ref_re.findall(vid)
            if not refs_m:
                continue
            vname = refs_m[0]
            # 优先从 description 里取「...」；否则用 field_key 查 _FIELD_LABELS
            if biz_name:
                labels.setdefault(vname, biz_name)
            else:
                fk = str(s.get("field_key") or "").lower()
                lbl = _FIELD_LABELS.get(fk)
                if lbl:
                    labels.setdefault(vname, lbl)

    return labels


def preview_har(har_path: Path) -> dict:
    """只预览不落盘。供 webui 展示用。"""
    import copy
    har = load_har(har_path)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)

    by_tier = {"core": 0, "ui_reaction": 0, "noise": 0}
    for s in raw_steps:
        by_tier[s.get("_tier", "ui_reaction")] = by_tier.get(s.get("_tier", "ui_reaction"), 0) + 1

    main_form = infer_main_form(raw_steps)

    # ⭐ 变量预检测：提前运行变量检测逻辑，让用户在导入前可配置
    preview_copy = copy.deepcopy(raw_steps)
    _, detected_vars, detected_labels = detect_var_placeholders(preview_copy)
    detected_vars.pop("_date_replaced", None)

    # ⭐ step.description 反查业务名：与左侧"执行步骤"的「...」内文字严格对齐
    step_label_map = _infer_labels_from_preview_steps(preview_copy)

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
    }
    _ENUM_FIELDS = {
        "gender": "性别",
        "certificatetype": "证件类型",
        "ba_e_laborrelstatus": "用工状态",
        "status": "状态",
        "type": "类型",
    }
    # 环境敏感关键字（日期类字段）
    _ENV_SENSITIVE_KEYWORDS = (
        "effectdate", "effectdatebak", "loseeffectdate",
        "bsed", "bsled", "startdate", "enddate",
    )

    pick_fields: list[dict] = []
    _seen_pick_ids: set = set()

    for s in raw_steps:
        if s.get("type") == "pick_basedata":
            field_key = s.get("field_key", "")
            value_id = s.get("value_id", "")
            if not field_key or not value_id:
                continue

            # 确定 step_id（与 build_yaml_case 命名规则一致）
            if field_key in _ENV_RELATED_FIELDS:
                step_id = f"pick_{field_key}"
            else:
                _sanitized = re.sub(r"[^a-zA-Z0-9_]", "_", field_key).strip("_")
                step_id = f"pick_{_sanitized}_id" if _sanitized else f"pick_{field_key}"

            # 去重
            if step_id in _seen_pick_ids:
                continue
            _seen_pick_ids.add(step_id)

            # 判断 env_sensitive 级别
            if field_key in _ENV_RELATED_FIELDS:
                env_sensitive = "medium"
            elif field_key in _ENUM_FIELDS:
                env_sensitive = "low"
            elif field_key.lower() in _ENV_SENSITIVE_KEYWORDS:
                env_sensitive = "medium"
            else:
                env_sensitive = "low"

            # 标签优先级：ENV_RELATED_FIELDS > ENUM_FIELDS > _FIELD_LABELS > field_key
            label = (
                _ENV_RELATED_FIELDS.get(field_key)
                or _ENUM_FIELDS.get(field_key)
                or _FIELD_LABELS.get(field_key.lower())
                or field_key
            )

            pick_fields.append({
                "id": step_id,
                "field_key": field_key,
                "label": label,
                "env_sensitive": env_sensitive,
                "value_id": str(value_id),
                "value_name": s.get("value_name", "") or "",
            })

        elif s.get("type") == "update_fields":
            # 处理 update_fields 中包含环境敏感日期字段
            fields = s.get("fields")
            if not isinstance(fields, dict):
                continue
            for fk in fields:
                fk_lower = fk.lower()
                if fk_lower in _ENV_SENSITIVE_KEYWORDS or fk_lower.startswith("date_"):
                    step_id = f"pick_{fk}"
                    if step_id in _seen_pick_ids:
                        continue
                    _seen_pick_ids.add(step_id)

                    label = _FIELD_LABELS.get(fk_lower) or fk
                    fv = fields[fk]
                    # 提取值（可能是字符串或多语言 dict）
                    if isinstance(fv, dict):
                        display_val = fv.get("zh_CN", "") or str(fv)
                    elif isinstance(fv, str):
                        display_val = fv
                    else:
                        display_val = str(fv) if fv else ""

                    pick_fields.append({
                        "id": step_id,
                        "field_key": fk,
                        "label": label,
                        "env_sensitive": "medium",
                        "value_id": display_val,
                        "value_name": "",
                    })

    # 按 env_sensitive 排序：high(0) → medium(1) → low(2)
    _sens_order = {"high": 0, "medium": 1, "low": 2}
    pick_fields.sort(key=lambda pf: _sens_order.get(pf["env_sensitive"], 9))

    # ⭐ 去重：把已被 pick_fields 覆盖的所有字段对应变量从 var_items 移除
    _pick_field_var_names: set = set()
    for pf in pick_fields:
        fk = pf.get("field_key", "")
        step_id = pf.get("id", "")
        if fk:
            # 覆盖所有可能的变量命名模式
            _pick_field_var_names.add(f"{fk}_id")          # gender_id
            _pick_field_var_names.add(fk)                   # gender
            _pick_field_var_names.add(f"pick_{fk}_id")      # pick_gender_id
            _pick_field_var_names.add(f"pick_{fk}")         # pick_gender
        if step_id:
            _pick_field_var_names.add(step_id)              # pick_gender_id (step_id 格式)
    if _pick_field_var_names:
        var_items = [v for v in var_items if v["name"] not in _pick_field_var_names]

    preview = {
        "main_form_id": main_form,
        "tier_counts": by_tier,
        "detected_vars": var_items,
        "pick_fields": pick_fields,
        "steps": [
            {
                "id": s.get("id"),
                "type": s.get("type"),
                "tier": s.get("_tier"),
                "form_id": s.get("form_id"),
                "ac": s.get("ac"),
                "brief": _step_brief(s),
            }
            for s in raw_steps
        ],
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

    p_prev = sub.add_parser("preview", help="只预览 HAR 结构，不写文件")
    p_prev.add_argument("har", type=Path)

    args = ap.parse_args()
    if args.cmd == "extract":
        if not args.har.exists():
            print(f"ERROR: HAR not found: {args.har}", file=sys.stderr)
            sys.exit(2)
        yaml = build_yaml_case(args.har, args.name)
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