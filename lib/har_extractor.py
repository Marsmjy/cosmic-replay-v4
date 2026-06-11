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
import copy
import json
import logging
import re
import sys
import urllib.parse
from collections import OrderedDict
from pathlib import Path
from typing import Any, Mapping

log = logging.getLogger(__name__)

from lib.kb_loader import field_meta as _kb_field_meta
from lib.kb_loader import get_field_label as _kb_get_field_label
from lib.response_signature import (
    build_response_signature_from_text,
    is_meaningful_response_signature,
    is_meaningful_response_text,
    specialize_response_signature,
    summarize_response_signature,
)
from lib.request_signature import build_request_signature
from lib.case_contract import attach_case_contract, build_case_contract
from lib.pageid_trace import (
    annotate_recorded_pageid_sources,
    annotate_pageid_recovery_strategies,
    expected_pageid_role,
    extract_response_pageid_producers,
    finalize_recorded_pageid_source_retention,
)


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

# ⭐ 运行时规则软必填：
# 有些字段在 getEntityType 中不是 required=true，但流程启动/保存时会被后端规则要求。
# 录制 HAR 如果没有显式维护这些字段，预览阶段仍应作为高优先环境字段暴露给用户；
# 用户填值后，生成 YAML 时再插入对应 pick_basedata 步骤。
_SOFT_REQUIRED_CONTEXT_FIELDS_BY_FORM = {
    "hom_onbrdinfo": {
        "chgreason": {
            "label": "变动原因",
            "reason": "入职流程启动时后端规则要求维护变动原因",
            "resolve_by": "value_code",
        },
    },
    "hbpm_positionhr": {
        "changedesc": {
            "label": "变动原因",
            "reason": "岗位新增保存时后端规则要求维护变动原因",
            "resolve_by": "value_code",
        },
        "khr_positiontpltype": {
            "label": "岗位模板类型",
            "reason": "岗位模板选择前需要先维护模板类型上下文",
            "resolve_by": "value_code",
        },
        "positiontpl": {
            "label": "岗位模板",
            "reason": "目标环境岗位名称等字段由岗位模板带出，不能直接写入录制值",
            "resolve_by": "value_code",
        },
        "parent": {
            "label": "上级岗位",
            "reason": "岗位新增保存时后端规则要求维护上级岗位",
            "resolve_by": "value_code",
        },
    },
}

_SALARY_APPLY_FORM_ID = "khr_hcdm_fapplybill"
_SALARY_TARGET_FORM_ID = "khr_hcdm_targetsalary"
_SALARY_SCOPE_FIELD_KEY = "khr_scope"
_SALARY_SCOPE_ALL_VALUE = "3"
_SALARY_EFFECTIVE_DATE_FIELD_KEY = "khr_heffectivedate"
_SALARY_SCOPE_ALL_DATE_FIELDS = (
    "khr_hjteffectivedate",
    "khr_hfleffectivedate",
)

# ⭐ 跨环境系统托管字段：
# 这些字段在部分录制环境可写，但其他目标环境可能由系统编码规则托管。HAR 明确
# 录制的写入/选择必须保留，并标记为运行时“锁定则跳过”；不能在导入阶段直接删除，
# 否则回到原录制环境执行时会丢失必填字段。
_SYSTEM_LOCKED_FIELDS_BY_FORM = {
    "haos_adminorgdetail": {"number"},
    "hbpm_positionhr": {
        "city",
        "countryregion",
        "diplomareq",
        "job",
        "jobgradescm",
        "joblevelscm",
        "name",
        "number",
        "positiontype",
        "workplace",
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
    # 工作流消息列表只是审批入口的导航/消息中心副作用；实际审核链路由 wf_task
    # 和目标审批/业务表单承接，目标环境缺少消息列表后端类时不应阻断主链路。
    "wf_msg_message",
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
    "khr_hsalarylevel",
    "khr_hsalarymodel",
    "khr_homs_orgform",
    "khr_homs_orgloc",
    "khr_salarylevel",
    "khr_zcurrencyfield",
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
# 匹配独立表单 pageId（不含 "root" 前缀的 32hex 或 UUID）
_RX_RANDOM_PAGE_ID = re.compile(
    r"^(?:[a-f0-9]{32}|[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})$"
)

# ⭐ 规则5：从原始测试值中提取前缀（去掉末尾数字/随机部分）
_RX_TRAILING_DIGITS = re.compile(r"^(.*?[^0-9])\d{2,}$")


def _contains_recorded_tempfile_reference(value: Any) -> bool:
    """Return true when a HAR action only references an expired tempfile handle."""
    if isinstance(value, str):
        return "tempfile" in value and ("download.do" in value or "tempfile.mock" in value)
    if isinstance(value, dict):
        return any(_contains_recorded_tempfile_reference(v) for v in value.values())
    if isinstance(value, list):
        return any(_contains_recorded_tempfile_reference(v) for v in value)
    return False


def _extract_recorded_upload_file_names(value: Any) -> list[str]:
    names: list[str] = []

    def visit(node: Any) -> None:
        if isinstance(node, dict):
            name = node.get("name") or node.get("fileName") or node.get("filename")
            if isinstance(name, str) and name.strip():
                names.append(name.strip())
            for child in node.values():
                visit(child)
        elif isinstance(node, list):
            for child in node:
                visit(child)

    visit(value)
    result: list[str] = []
    seen: set[str] = set()
    for name in names:
        if name in seen:
            continue
        seen.add(name)
        result.append(name)
    return result[:20]


def _header_value(headers: list[dict], name: str) -> str:
    target = str(name or "").lower()
    for item in headers or []:
        if not isinstance(item, dict):
            continue
        if str(item.get("name") or "").lower() == target:
            return str(item.get("value") or "")
    return ""


def _normalize_upload_endpoint_from_url(url: str) -> str:
    parsed = urllib.parse.urlparse(str(url or ""))
    path = parsed.path or ""
    if not path:
        return ""
    # Prefer app-root-relative endpoints so the same YAML can run on SIT/UAT.
    for marker in ("/tempfile/", "/attachment/", "/file/", "/fileservice/"):
        if marker in path:
            endpoint = path[path.index(marker):]
            return endpoint + (f"?{parsed.query}" if parsed.query else "")
    return path + (f"?{parsed.query}" if parsed.query else "")


def _extract_har_upload_endpoint_hints(har: dict) -> list[dict[str, Any]]:
    """Extract real upload endpoints from HAR multipart requests.

    batchInvokeAction upload actions only contain client-side state. The actual
    file bytes, when recorded, appear as a separate multipart request; capture
    its endpoint and file field so a later replay can upload a user-provided
    local file instead of reusing tempfile URLs.
    """
    hints: list[dict[str, Any]] = []
    for idx, entry in enumerate((har.get("log") or {}).get("entries", [])):
        req = entry.get("request") or {}
        url = str(req.get("url") or "")
        if not url:
            continue
        post_data = req.get("postData") or {}
        mime_type = str(post_data.get("mimeType") or _header_value(req.get("headers") or [], "content-type")).lower()
        params = post_data.get("params") or []
        file_field = ""
        file_names: list[str] = []
        extra_data: dict[str, str] = {}
        if isinstance(params, list):
            for item in params:
                if not isinstance(item, dict):
                    continue
                name = str(item.get("name") or "")
                file_name = str(item.get("fileName") or item.get("filename") or "")
                if file_name:
                    file_field = file_field or name or "file"
                    file_names.append(file_name)
                elif name:
                    value = item.get("value")
                    if value is not None:
                        extra_data[name] = str(value)
        path_l = urllib.parse.urlparse(url).path.lower()
        is_upload = (
            "multipart/form-data" in mime_type
            or bool(file_names)
            or ("upload" in path_l and any(token in path_l for token in ("tempfile", "attach", "file")))
        )
        if not is_upload:
            continue
        endpoint = _normalize_upload_endpoint_from_url(url)
        if not endpoint:
            continue
        hints.append({
            "endpoint": endpoint,
            "file_field": file_field or "file",
            "recorded_file_names": file_names[:20],
            "extra_data": extra_data,
            "_har_index": idx,
        })
    return hints


def _pick_upload_hint_for_action(
    hints: list[dict[str, Any]],
    recorded_file_names: list[str],
) -> dict[str, Any]:
    if not hints:
        return {}
    wanted = {str(name or "").strip() for name in recorded_file_names or [] if str(name or "").strip()}
    if wanted:
        for hint in hints:
            names = {str(name or "").strip() for name in hint.get("recorded_file_names") or [] if str(name or "").strip()}
            if wanted & names:
                return hint
    return hints[0]


def _is_real_upload_action_step(step: dict) -> bool:
    method = str(step.get("method") or step.get("methodName") or "").strip().lower()
    ac = str(step.get("ac") or "").strip().lower()
    if method:
        return method == "upload"
    return method == "upload" or ac == "upload"

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
    "hrbm_schedule_quest": (
        "infotype",
        "timeline",
        "timeconstraintmode",
        "mulline",
    ),
}

_FORM_SCALAR_ENUM_FIELDS_BY_FORM = {
    "khr_hcdm_fapplybill": {
        "khr_scope": "定调薪范围",
        "khr_zcurrency": "是否使用薪资核算币种",
        "khr_salaryproposal": "是否薪酬提案",
    },
    "wf_batchtask_handle": {
        "decision_radio_group": "审批动作",
    },
    "wf_approvalpage_bac": {
        "combo_decision": "审批决策",
    },
    "hrbm_schedule_quest": {
        "infotype": "请选择需要添加的信息类型",
        "timeline": "请选择添加信息集是否需要按照时间轴记录数据",
        "timeconstraintmode": "请选择时间轴的约束信息集",
        "mulline": "默认样式是否多行",
    },
}

_WORKFLOW_APPROVAL_FORM_IDS = {"wf_batchtask_handle"}
_WORKFLOW_DECISION_FIELD_KEY = "decision_radio_group"
_WORKFLOW_COMBO_DECISION_FIELD_KEY = "combo_decision"
_WORKFLOW_OPINION_FIELD_KEY = "msg_approval"
_WORKFLOW_DECISION_OPTIONS = OrderedDict([
    ("Consent", "同意"),
    ("Reject", "驳回"),
])
_WORKFLOW_APPROVAL_FIELD_KEYS = {
    _WORKFLOW_DECISION_FIELD_KEY,
    _WORKFLOW_OPINION_FIELD_KEY,
}
_WORKFLOW_APPROVAL_OPTIONS_TEXT = "|".join(
    f"{value}={label}" for value, label in _WORKFLOW_DECISION_OPTIONS.items()
)


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


def _scalar_enum_field_label(
    form_id: str,
    field_key: str,
    known_enum_fields: Mapping[str, str] | None = None,
    meta_resolver: Any | None = None,
) -> str:
    key = str(field_key or "").strip().lower()
    if not key:
        return ""
    if known_enum_fields and key in known_enum_fields:
        return str(known_enum_fields[key] or key)
    form_key = str(form_id or "").strip()
    form_hints = _FORM_SCALAR_ENUM_FIELDS_BY_FORM.get(form_key) or {}
    if key in form_hints:
        return form_hints[key]
    if meta_resolver and form_key:
        try:
            field_type = meta_resolver.get_field_type(form_key, key)
        except Exception:
            field_type = ""
        if field_type in ("ComboProp", "MulComboProp", "BooleanProp"):
            return _resolve_field_label(key, entity_id=form_key, meta_resolver=meta_resolver)
    return ""


def _response_opens_form_or_tab(resp_text: str) -> bool:
    """Return true when a response is a dynamic navigation producer."""
    if not resp_text:
        return False
    if "showForm" not in resp_text and "addVirtualTab" not in resp_text:
        return False
    try:
        payload = json.loads(resp_text)
    except Exception:
        return '"showForm"' in resp_text or "addVirtualTab" in resp_text

    def walk(obj: Any) -> bool:
        if isinstance(obj, dict):
            if obj.get("a") == "showForm":
                return True
            method = str(obj.get("methodname") or obj.get("methodName") or "")
            if method == "addVirtualTab":
                return True
            return any(walk(value) for value in obj.values())
        if isinstance(obj, list):
            return any(walk(item) for item in obj)
        return False

    return walk(payload)


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


def _recorded_shape_random_template(
    value: str,
    *,
    fallback_prefix: str,
    fallback_digits: int,
    max_length: int = 20,
    minimum_random_digits: int = 0,
) -> str:
    """Keep the recorded identifier shape while randomizing its numeric suffix."""
    text = str(value or "")
    match = _RX_TRAILING_DIGITS.match(text)
    if match:
        prefix = match.group(1)
        digit_count = max(len(text) - len(prefix), int(minimum_random_digits))
        target_length = min(len(text), max_length) if max_length > 0 else len(text)
        if target_length > 0 and len(prefix) + digit_count > target_length:
            prefix = prefix[:max(target_length - digit_count, 0)]
        return f"{prefix}${{rand:{digit_count}}}"

    prefix = _extract_value_prefix(text) or fallback_prefix
    digit_count = max(int(fallback_digits), 1)
    if max_length > 0:
        prefix = prefix[:max(max_length - digit_count, 0)]
    return f"{prefix}${{rand:{digit_count}}}"


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
    'wf_task': '待办任务',
    'wf_batchtask_handle': '批量审批',
    'wf_msg_center': '消息中心',
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
    # 定调薪明细业务录入值
    'khr_monthlycommission':  '调薪前-月度提成',
    'khr_hpostallowance':     '调薪后-岗位津贴',
    'khr_hmonthlyincome':     '调薪后-固定月收入',
    'khr_hperformancem':      '调薪后-月度绩效',
    'khr_hmonthlybonus':      '调薪后-月度奖金',
    'khr_hquarterlybonus':    '调薪后-季度奖金',
    'khr_hhalfyearbonus':     '调薪后-半年奖金',
    'khr_hannualbonus':       '调薪后-年度奖金',
    'khr_salarylevel':        '薪酬水平',
    'khr_hsalarylevel':       '调薪后-薪酬水平',
    'khr_hsalarymodel':       '调薪后-薪酬模式',
    'khr_zcurrencyfield':     '调薪后-币种',
    'khr_saproposal':         '薪酬提案',
    'khr_sapexplanation':     '提案说明',
    'khr_heffectivedate':     '调薪后-生效日期',
    'khr_hjteffectivedate':   '调薪后津贴-生效日期',
    'khr_hfleffectivedate':   '调薪后福利-生效日期',
    'khr_scope':              '定调薪范围',
    'khr_upperson':           '薪酬直接上级',
    'decision_radio_group':    '审批动作',
    'msg_approval':            '审批意见',
}


def _resolve_field_label(field_key: str, entity_id: str = None, meta_resolver=None) -> str:
    """动态解析字段中文标签，优先查询实时元数据。

    优先级链：
    1. MetadataResolver 实时查询（在线时）
    2. kb_loader.get_field_label() (知识库动态查询)
    3. runtime field_type_catalog（在线探测沉淀）
    4. _FIELD_LABELS.get() (静态字典兜底)
    5. field_key 原始值 (最终fallback)
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
        try:
            from lib import field_type_catalog as _ftc
            catalog_item = _ftc.get_field_entry(entity_id, key_lower)
            label = (catalog_item or {}).get("label")
            if label:
                return _clean_display_label(label)
        except Exception:
            pass
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

    # ---------- wait_until ----------
    if step_type == 'wait_until':
        condition = step.get("condition") or {}
        if step.get("wait_source") == "workflow_task_search" or condition.get("kind") == "grid_row_exists":
            return "等待「审批待办」生成"
        count = step.get("source_step_count") or 0
        suffix = f"（折叠 {count} 次轮询）" if count else ""
        return f"等待「{form_name or '进度'}」完成{suffix}"

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
    locked_events_by_form: dict[str, dict[int, set[str]]] = {}
    lock_state_events: list[dict[str, Any]] = []
    response_values: dict[str, dict[str, str]] = {}
    response_values_by_form: dict[str, dict[str, dict[str, str]]] = {}
    response_raw_values_by_form: dict[str, dict[str, Any]] = {}
    response_internal_ids_by_form: dict[str, dict[str, str]] = {}
    combo_options_by_form: dict[str, dict[str, dict[str, str]]] = {}
    labels_by_form: dict[str, dict[str, str]] = {}

    def mark_locked(har_index: int, field_key: Any, form_id: str = "", *, locked: bool = True) -> None:
        key = str(field_key or "").strip().lower()
        form = str(form_id or "").strip()
        if not key:
            return
        lock_state_events.append({
            "har_index": har_index,
            "form_id": form,
            "field_key": key,
            "locked": bool(locked),
        })
        if locked:
            locked_events.setdefault(har_index, set()).add(key)
            if form:
                locked_events_by_form.setdefault(form, {}).setdefault(har_index, set()).add(key)

    def _is_recorded_scalar_default(form_id: str, key: str) -> bool:
        return key in {
            str(item).lower()
            for item in _RECORDED_DEFAULT_SCALAR_FIELDS_BY_FORM.get(form_id, ())
        }

    def _workflow_scalar_value(field_key: str, value: Any) -> str:
        if field_key == _WORKFLOW_OPINION_FIELD_KEY and isinstance(value, dict):
            return str(value.get("zh_CN") or value.get("GLang") or "").strip()
        return str(value or "").strip()

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
            or (form_id in _WORKFLOW_APPROVAL_FORM_IDS and key in _WORKFLOW_APPROVAL_FIELD_KEYS)
        ):
            display_value = _workflow_scalar_value(key, value)
            if not display_value:
                return
            fallback = {
                "value_code": display_value,
                "value_name": display_value,
                "value_number": display_value,
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
                col_id = str(obj.get("colId") or "").strip().lower()
                if col_id:
                    meta_form_id = str(obj.get("entity") or form_id or "").strip()
                    raw_name = obj.get("name")
                    if isinstance(raw_name, dict):
                        label = str(raw_name.get("zh_CN") or raw_name.get("name") or "").strip()
                    else:
                        label = str(raw_name or "").strip()
                    if label:
                        clean_label = _clean_display_label(label)
                        if meta_form_id:
                            labels_by_form.setdefault(meta_form_id, {})[col_id] = clean_label
                        # Grid column configs may describe fields later lowered to
                        # child form ids, so keep a field-key level fallback too.
                        labels_by_form.setdefault("*", {})[col_id] = clean_label
                for child in obj.values():
                    if isinstance(child, (dict, list)):
                        visit(child)
            elif isinstance(obj, list):
                for child in obj:
                    visit(child)

        visit(meta)

    def remember_response_field_values(form_id: str, node: Any) -> None:
        """Capture business code/name values from nested HR response payloads.

        HR often returns basedata selections below entry ``fieldstates`` or
        list/grid ``dataindex`` rows rather than the top-level ``u`` action.
        Keeping those observations lets the variable panels show editable
        business codes instead of opaque internal ids.
        """
        if isinstance(node, dict):
            fieldstates = node.get("fieldstates")
            if isinstance(fieldstates, list):
                for item in fieldstates:
                    if not isinstance(item, dict):
                        continue
                    field_key = item.get("k")
                    if field_key:
                        remember_value(field_key, item.get("v"), form_id)

            dataindex = node.get("dataindex")
            rows = node.get("rows")
            if isinstance(dataindex, dict) and isinstance(rows, list):
                for field_key, idx in dataindex.items():
                    if not isinstance(idx, int):
                        continue
                    for row in rows:
                        if not isinstance(row, list) or idx >= len(row):
                            continue
                        if _extract_basedata_parts(row[idx]):
                            remember_value(field_key, row[idx], form_id)

            for child in node.values():
                if isinstance(child, (dict, list)):
                    remember_response_field_values(form_id, child)
        elif isinstance(node, list):
            for child in node:
                remember_response_field_values(form_id, child)

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
            remember_response_field_values(form_id, cmd)
            remember_list_row_ids(form_id, cmd)
        if action == "updateControlMetadata" and isinstance(params, list):
            for i in range(0, len(params) - 1, 2):
                meta = params[i + 1]
                if not isinstance(meta, dict):
                    continue
                remember_control_meta(form_id, meta)
                item_meta = meta.get("item") if isinstance(meta.get("item"), dict) else {}
                if meta.get("mi") is True or item_meta.get("mi") is True:
                    mark_locked(har_index, params[i], form_id, locked=True)
                elif meta.get("mi") is False or item_meta.get("mi") is False:
                    mark_locked(har_index, params[i], form_id, locked=False)
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
        "locked_events_by_form": locked_events_by_form,
        "lock_state_events": lock_state_events,
        "response_values": response_values,
        "response_values_by_form": response_values_by_form,
        "response_raw_values_by_form": response_raw_values_by_form,
        "response_internal_ids_by_form": response_internal_ids_by_form,
        "combo_options_by_form": combo_options_by_form,
        "labels_by_form": labels_by_form,
    }


def _is_locked_before(
    observations: dict[str, Any],
    field_key: Any,
    har_index: Any,
    *,
    form_id: str = "",
) -> bool:
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


def _observed_field_label(observations: dict[str, Any], form_id: Any, field_key: Any) -> str:
    """Return a HAR-observed UI label for a field, including grid-column fallback."""
    key = str(field_key or "").strip().lower()
    if not key:
        return ""
    form = str(form_id or "").strip()
    labels_by_form = (observations or {}).get("labels_by_form") or {}
    for scope in (form, "*"):
        labels = labels_by_form.get(scope) or {}
        label = str(labels.get(key) or "").strip()
        if label:
            return _clean_display_label(label)
    return ""


def _drop_locked_update_fields(steps: list[dict], observations: dict[str, Any]) -> list[dict]:
    """Drop HAR-observed locked writes and mark cross-env candidates as tolerant."""
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
        if step.get("type") == "pick_basedata":
            field_key_s = str(step.get("field_key") or "").strip().lower()
            form_id_s = str(step.get("form_id") or "").strip()
            is_cross_env_candidate = field_key_s in _SYSTEM_LOCKED_FIELDS_BY_FORM.get(form_id_s, set())
            has_recorded_pick_value = bool(
                str(step.get("value_id") or step.get("value_code") or step.get("value_number") or "").strip()
            )
            if (
                not has_recorded_pick_value
                and _is_locked_before(
                    observations,
                    field_key_s,
                    step.get("_har_index"),
                    form_id=form_id_s,
                )
            ):
                continue
            if is_cross_env_candidate and has_recorded_pick_value:
                new_step = dict(step)
                new_step["skip_if_locked"] = True
                out.append(new_step)
            else:
                out.append(step)
            continue
        if step.get("type") != "update_fields":
            out.append(step)
            continue
        fields = step.get("fields")
        if not isinstance(fields, dict):
            out.append(step)
            continue
        kept = OrderedDict()
        dropped: list[str] = []
        skip_if_locked_fields: list[str] = []
        for field_key, value in fields.items():
            field_key_s = str(field_key or "").strip().lower()
            form_id_s = str(step.get("form_id") or "").strip()
            is_cross_env_candidate = field_key_s in _SYSTEM_LOCKED_FIELDS_BY_FORM.get(form_id_s, set())
            is_observed_locked = _is_locked_before(
                observations,
                field_key,
                step.get("_har_index"),
                form_id=form_id_s,
            )
            if (
                is_observed_locked
                and not _has_recent_change_year(field_key_s, step.get("_har_index"))
            ):
                dropped.append(str(field_key))
                continue
            kept[field_key] = value
            if is_cross_env_candidate:
                skip_if_locked_fields.append(str(field_key))
        if not dropped:
            if skip_if_locked_fields:
                new_step = dict(step)
                new_step["skip_if_locked_fields"] = skip_if_locked_fields
                out.append(new_step)
            else:
                out.append(step)
        elif kept:
            new_step = dict(step)
            new_step["fields"] = kept
            new_step["_dropped_locked_fields"] = dropped
            if skip_if_locked_fields:
                new_step["skip_if_locked_fields"] = skip_if_locked_fields
            out.append(new_step)
    return out


def _workflow_approval_display_value(value: Any) -> str:
    if isinstance(value, dict):
        return str(value.get("zh_CN") or value.get("GLang") or "").strip()
    return str(value or "").strip()


def _workflow_approval_defaults(observations: dict[str, Any], form_id: str, ac: str = "") -> tuple[str, str]:
    values_by_form = observations.get("response_values_by_form") or {}
    raw_by_form = observations.get("response_raw_values_by_form") or {}
    values = values_by_form.get(form_id) or {}
    raw_values = raw_by_form.get(form_id) or {}
    decision = (
        _workflow_approval_display_value(raw_values.get(_WORKFLOW_DECISION_FIELD_KEY))
        or str((values.get(_WORKFLOW_DECISION_FIELD_KEY) or {}).get("value_code") or "").strip()
    )
    if decision not in _WORKFLOW_DECISION_OPTIONS:
        decision = "Reject" if "reject" in str(ac or "").lower() else "Consent"
    default_label = _WORKFLOW_DECISION_OPTIONS.get(decision, "同意")
    opinion = (
        _workflow_approval_display_value(raw_values.get(_WORKFLOW_OPINION_FIELD_KEY))
        or str((values.get(_WORKFLOW_OPINION_FIELD_KEY) or {}).get("value_name") or "").strip()
        or default_label
    )
    return decision, opinion


def _has_workflow_approval_fields(step: dict) -> bool:
    if step.get("type") != "update_fields":
        return False
    fields = step.get("fields") or {}
    if not isinstance(fields, dict):
        return False
    keys = {str(k or "").lower() for k in fields}
    return bool({_WORKFLOW_DECISION_FIELD_KEY, _WORKFLOW_OPINION_FIELD_KEY} & keys)


def _ensure_workflow_approval_update_steps(
    steps: list[dict],
    observations: dict[str, Any],
) -> list[dict]:
    """Expose workflow approval action/opinion as maintainable replay fields.

    Kingdee workflow batch approval often opens ``wf_batchtask_handle`` with
    default ``decision_radio_group`` and ``msg_approval`` values in loadData,
    then the user clicks the final OK button without an explicit updateValue.
    Replay still needs a writable anchor so later user-maintained approval
    action/opinion values can affect execution.
    """
    out: list[dict] = []
    counters: dict[str, int] = {}
    for step in steps:
        form_id = str(step.get("form_id") or "")
        ac = str(step.get("ac") or step.get("method") or "")
        is_workflow_submit = (
            step.get("type") == "invoke"
            and form_id in _WORKFLOW_APPROVAL_FORM_IDS
            and ac in {"btnsubmit", "btnreject"}
        )
        if not is_workflow_submit:
            out.append(step)
            continue

        has_existing = any(
            str(prev.get("form_id") or "") == form_id and _has_workflow_approval_fields(prev)
            for prev in out
        )
        if not has_existing:
            counters[form_id] = counters.get(form_id, 0) + 1
            suffix = "" if counters[form_id] == 1 else f"_{counters[form_id]}"
            decision, opinion = _workflow_approval_defaults(observations, form_id, ac)
            out.append({
                "id": f"fill_workflow_approval{suffix}",
                "type": "update_fields",
                "form_id": form_id,
                "app_id": step.get("app_id", "bos"),
                "fields": {
                    _WORKFLOW_DECISION_FIELD_KEY: decision,
                    _WORKFLOW_OPINION_FIELD_KEY: {
                        "GLang": opinion,
                        "zh_CN": opinion,
                    },
                },
                "_tier": "core",
                "_synthetic_workflow_approval": True,
                "description": "维护「审批意见」",
            })
        out.append(step)
    return out


def _workflow_decision_code(value: Any, meta: Mapping[str, Any] | None = None) -> str:
    candidates = [value]
    if isinstance(meta, Mapping):
        candidates.extend([meta.get("value_code"), meta.get("value_id"), meta.get("value_name")])
    aliases = {
        "consent": "Consent",
        "agree": "Consent",
        "approve": "Consent",
        "approval": "Consent",
        "同意": "Consent",
        "通过": "Consent",
        "审批通过": "Consent",
        "reject": "Reject",
        "rejected": "Reject",
        "dismiss": "Reject",
        "dismissed": "Reject",
        "驳回": "Reject",
        "拒绝": "Reject",
        "审批不通过": "Reject",
    }
    for candidate in candidates:
        text = str(candidate or "").strip()
        if not text:
            continue
        if text in _WORKFLOW_DECISION_OPTIONS:
            return text
        mapped = aliases.get(text.lower()) or aliases.get(text)
        if mapped:
            return mapped
    return str(value or "").strip()


def _pick_field_update_value(field_key: str, meta: Mapping[str, Any]) -> str:
    key = str(field_key or "").lower()
    if key == _WORKFLOW_DECISION_FIELD_KEY:
        return _workflow_decision_code(
            meta.get("value_code") or meta.get("value_id") or meta.get("value_name"),
            meta,
        )
    if str(meta.get("resolve_by") or "") == "value_code" and meta.get("value_code"):
        return str(meta.get("value_code") or "")
    for value_key in ("value_code", "value_number", "value_id", "value_name"):
        value = meta.get(value_key)
        if value not in (None, ""):
            return str(value)
    return ""


def _apply_user_pick_field_values_to_update_steps(
    steps: list[dict],
    pick_fields_map: Mapping[str, Mapping[str, Any]],
) -> None:
    """Reflect preview/user-maintained pick field overrides in generated YAML steps."""
    for _pf_id, meta in (pick_fields_map or {}).items():
        if not isinstance(meta, Mapping):
            continue
        if not (meta.get("user_overridden") or meta.get("manual_override")):
            continue
        field_key = str(meta.get("field_key") or "").strip()
        form_id = str(meta.get("form_id") or "").strip()
        source_step_id = str(meta.get("source_step_id") or "").strip()
        if not field_key:
            continue
        new_value = _pick_field_update_value(field_key, meta)
        if not new_value:
            continue
        for step in steps:
            if step.get("type") != "update_fields":
                continue
            if form_id and str(step.get("form_id") or "") != form_id:
                continue
            if (
                source_step_id
                and (
                    not form_id
                    or field_key.lower() in {_WORKFLOW_DECISION_FIELD_KEY, _WORKFLOW_COMBO_DECISION_FIELD_KEY}
                )
                and str(step.get("id") or "") != source_step_id
            ):
                continue
            fields = step.get("fields") or {}
            if not isinstance(fields, dict):
                continue
            matched_key = next((k for k in fields if str(k).lower() == field_key.lower()), "")
            if not matched_key:
                continue
            current = fields[matched_key]
            if isinstance(current, dict):
                for lang in list(current.keys()):
                    current[lang] = new_value
            else:
                fields[matched_key] = new_value

        for step in steps:
            if step.get("type") != "invoke":
                continue
            if form_id and str(step.get("form_id") or "") != form_id:
                continue
            if (
                source_step_id
                and (
                    not form_id
                    or field_key.lower() in {_WORKFLOW_DECISION_FIELD_KEY, _WORKFLOW_COMBO_DECISION_FIELD_KEY}
                )
                and str(step.get("id") or "") != source_step_id
            ):
                continue
            post_data = step.get("post_data")
            if not (isinstance(post_data, list) and len(post_data) >= 2 and isinstance(post_data[1], list)):
                continue
            for entry in post_data[1]:
                if not isinstance(entry, dict):
                    continue
                if str(entry.get("k") or "").lower() != field_key.lower():
                    continue
                entry["v"] = new_value


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
    "msg_approval",
    "approvalopinion",
    "approval_opinion",
    "changedescription",
    "changedesc",
    "khr_homs_condes",
    "khr_sapexplanation",
}

_BUSINESS_INPUT_VARIABLE_KEYS = {
    "bizdate": ("business_belong_date", "业务归属日期"),
    "kd311": ("workday_overtime_hours", "工作加班小时"),
    "kd305": ("weekend_overtime_hours", "周末加班小时"),
    "kd306": ("holiday_overtime_hours", "法定加班小时"),
    "khr_monthlycommission": ("salary_before_monthly_commission", "调薪前-月度提成"),
    "khr_hpostallowance": ("salary_after_post_allowance", "调薪后-岗位津贴"),
    "khr_hmonthlyincome": ("salary_after_fixed_monthly_income", "调薪后-固定月收入"),
    "khr_hperformancem": ("salary_after_monthly_performance", "调薪后-月度绩效"),
    "khr_hmonthlybonus": ("salary_after_monthly_bonus", "调薪后-月度奖金"),
    "khr_hquarterlybonus": ("salary_after_quarterly_bonus", "调薪后-季度奖金"),
    "khr_hhalfyearbonus": ("salary_after_half_year_bonus", "调薪后-半年奖金"),
    "khr_hannualbonus": ("salary_after_annual_bonus", "调薪后-年度奖金"),
    "khr_saproposal": ("salary_proposal_amount", "薪酬提案"),
}

_SALARY_DETAIL_VALUE_HINTS = (
    "allowance",
    "income",
    "bonus",
    "commission",
    "performancem",
    "subsidy",
    "amount",
    "wage",
)


def _business_input_variable_info(key_lower: str) -> tuple[str, str] | None:
    info = _BUSINESS_INPUT_VARIABLE_KEYS.get(key_lower)
    if info:
        return info
    if key_lower.startswith("khr_") and any(hint in key_lower for hint in _SALARY_DETAIL_VALUE_HINTS):
        suffix = re.sub(r"[^a-zA-Z0-9_]", "_", key_lower[4:]).strip("_") or key_lower
        label = _resolve_field_label(key_lower)
        return f"salary_detail_{suffix}", label
    return None


_DATE_FIELD_KEYWORDS = (
    "effectdate",
    "effectivedate",
    "loseeffectdate",
    "validuntil",
    "bsed",
    "bsled",
    "startdate",
    "enddate",
)


def _is_date_like_field_key(field_key: str) -> bool:
    key_lower = str(field_key or "").lower()
    return bool(
        key_lower.startswith("date_")
        or key_lower in _DATE_FIELD_KEYWORDS
        or any(keyword in key_lower for keyword in _DATE_FIELD_KEYWORDS)
    )

_F7_SELECTOR_FORM_LABELS = {
    "hsbs_empposf7querylist": ("employee_position", "计薪人员任职经历", "employee"),
    "hsbs_employeequerylistf7": ("employee_position", "计薪人员任职经历", "employee"),
    "hcdm_adjfileinfof7": ("salary_adjust_employee", "定调薪人员", "employee_name"),
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
            "msg_approval": "test_workflow_approval_opinion",
            "approvalopinion": "test_workflow_approval_opinion",
            "approval_opinion": "test_workflow_approval_opinion",
            "changedescription": "test_change_description",
            "changedesc": "test_change_description",
            "khr_homs_condes": "test_confidential_description",
            "khr_sapexplanation": "test_salary_proposal_explanation",
        }
        if kl in mapping:
            return mapping[kl]
        safe = re.sub(r"[^a-zA-Z0-9_]", "_", kl).strip("_") or "text"
        return f"test_{safe}"

    def maybe_var(val: Any, key_hint: str = "") -> Any:
        key_lower = (key_hint or "").lower()
        business_input = _business_input_variable_info(key_lower)
        if business_input and val not in ("", None):
            suffix, label = business_input
            vname = f"test_{suffix}"
            if vname not in vars_map:
                vars_map[vname] = val
                vars_labels[vname] = label
            return f"${{vars.{vname}}}"

        if not isinstance(val, str) or not val:
            return val
        # 日期类字段保留 HAR 录入值。录制时明确输入的日期不自动改成 ${today}，
        # 避免换环境或复跑时回放值与用户在预览/变量面板维护的值不一致。
        if _RX_DATE.match(val) or _RX_DATETIME.match(val):
            return val

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
                    vars_map[vname] = _recorded_shape_random_template(
                        val,
                        fallback_prefix="QA",
                        fallback_digits=6,
                        minimum_random_digits=6 if "empnumber" in key_lower else 0,
                    )
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
                    vars_map[vname] = _recorded_shape_random_template(
                        val,
                        fallback_prefix="CERT",
                        fallback_digits=10,
                        minimum_random_digits=6,
                    )
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
            elif isinstance(v, (int, float)) and not isinstance(v, bool):
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
        elif isinstance(fv, (int, float)) and not isinstance(fv, bool):
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
                    elif isinstance(v, (int, float)) and not isinstance(v, bool):
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

    # A recorded save is often followed by a list commonSearch using the same
    # literal number/name.  Once the write field is variableized, keeping that
    # old literal makes the replay query the recorded row instead of the row
    # created by this run.  Propagate only exact values into readonly query
    # arguments; never rewrite ids, context defaults or save payloads here.
    recorded_value_refs: dict[str, str] = {}
    for raw_key, var_name in seen_values.items():
        raw_value = raw_key[0] if isinstance(raw_key, tuple) and raw_key else raw_key
        if not isinstance(raw_value, str) or not raw_value:
            continue
        ref = str(var_name or "")
        if not ref:
            continue
        if not ref.startswith("${"):
            ref = f"${{vars.{ref}}}"
        recorded_value_refs.setdefault(raw_value, ref)
    variable_prefix_refs: list[tuple[str, str]] = []
    for var_name, template in vars_map.items():
        if var_name.startswith("_") or not isinstance(template, str):
            continue
        prefix = template.split("${", 1)[0]
        if len(prefix) >= 3:
            variable_prefix_refs.append((prefix, f"${{vars.{var_name}}}"))

    def rewrite_query_value(value: Any) -> Any:
        if isinstance(value, str):
            exact = recorded_value_refs.get(value)
            if exact:
                return exact
            for prefix, ref in variable_prefix_refs:
                if value == prefix or (len(value) >= 3 and prefix.startswith(value)):
                    return ref
            return value
        if isinstance(value, list):
            return [rewrite_query_value(item) for item in value]
        if isinstance(value, dict):
            return {key: rewrite_query_value(item) for key, item in value.items()}
        return value

    for action_wrap in actions_seq:
        ac = str(action_wrap.get("ac") or "").lower()
        method = str(action_wrap.get("method") or "").lower()
        if ac not in {"commonsearch", "query"} and method not in {"commonsearch", "query"}:
            continue
        if "args" in action_wrap:
            action_wrap["args"] = rewrite_query_value(action_wrap.get("args"))


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
    upload_hints = _extract_har_upload_endpoint_hints(har)
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
            for action_index, action in enumerate(actions):
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
            for action_index, action in enumerate(actions):
                if not isinstance(action, dict):
                    continue
                counter += 1
                action["_form_id"] = form_id
                name = smart_name(action, ac, counter)
                tier = AC_TIER.get(ac, "ui_reaction")
                # ⭐ 规则6补充：toolbar 按钮点击一律视为 core
                ctrl_key = action.get("key", "")
                if ac == "customEvent" and _response_opens_form_or_tab(_resp_text):
                    tier = "core"
                # ⭐ 规则6补充：btnsave 类按钮 → 视为 core（HAR 可能把保存录成 click）
                if ac == "click" and ctrl_key in (_SAVE_BUTTON_KEYS | _CORE_CLICK_KEYS):
                    tier = "core"
                elif tier != "core" and ctrl_key in _CORE_TOOLBAR_KEYS:
                    tier = "core"
                step_dict: dict[str, Any] = {
                    "_har_index": i,
                    "_har_action_index": action_index,
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
                if (
                    action.get("methodName") in {"beforeUpload", "upload"}
                    or ac in {"beforeUpload", "upload"}
                ):
                    step_dict["optional"] = True
                    step_dict["skip_replay"] = True
                    step_dict["requires_user_file"] = True
                    step_dict["upload_replay_strategy"] = "user_file_required"
                    step_dict["recorded_file_names"] = _extract_recorded_upload_file_names(action)
                    step_dict["recorded_tempfile_reference"] = _contains_recorded_tempfile_reference(action)
                    upload_hint = (
                        _pick_upload_hint_for_action(upload_hints, step_dict["recorded_file_names"])
                        if _is_real_upload_action_step(step_dict)
                        else {}
                    )
                    if upload_hint:
                        if upload_hint.get("endpoint"):
                            step_dict["upload_endpoint"] = upload_hint["endpoint"]
                        if upload_hint.get("file_field"):
                            step_dict["file_field"] = upload_hint["file_field"]
                        if upload_hint.get("extra_data"):
                            step_dict["extra_data"] = upload_hint["extra_data"]
                    if step_dict["recorded_tempfile_reference"]:
                        step_dict["skip_reason"] = "HAR 仅包含录制期临时附件句柄，回放时跳过以避免过期附件污染表单"
                    else:
                        step_dict["skip_reason"] = "HAR 附件预上传动作没有真实文件字节，回放时跳过以避免表单停留在上传中状态"
                if action.get("methodName") == "setItemByIdFromClient" and action.get("key"):
                    lookup = pending_lookup.get((form_id, app_id, str(action.get("key") or "")))
                    if lookup:
                        step_dict["_prefetch_lookup"] = True
                        step_dict["_prefetch_lookup_args"] = lookup.get("args") or [["%", "", "%", 0, 20, 0]]
                if _is_l2_page_id(req_page_id):
                    step_dict["preserve_l2_page"] = True
                # 保留有限响应体：setItem 用于提取 value_name；通知用于识别录制期业务校验点；
                # 语义签名用于校验关键接口返回是否仍符合录制效果。
                # 响应体不输出到 YAML，仅在本次解析内消费。
                if _resp_text and (
                    action.get("methodName") == "setItemByIdFromClient"
                    or "ShowNotificationMsg" in _resp_text
                    or "showMessage" in _resp_text
                    or "showErrMsg" in _resp_text
                    or _response_opens_form_or_tab(_resp_text)
                    or bool(extract_response_pageid_producers(_resp_text))
                    or is_meaningful_response_text(_resp_text)
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

    同一连续片段里如果字段重复出现，不能只保留最终值。Combo/Boolean
    开关常依赖中间值触发服务端联动；吞掉 toggle 会导致后续 F7/保存提前
    触发必填校验。
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

        # 收集连续的 updateValue。同字段重复时切断批次，保留 toggle 顺序。
        group: list[dict] = [s]
        seen_keys = set(_extract_update_fields(s.get("post_data") or []).keys())
        j = i + 1
        while j < len(steps):
            nxt = steps[j]
            if (nxt.get("type") == "invoke" and nxt.get("method") == "updateValue"
                    and nxt.get("key") == ""
                    and nxt.get("form_id") == s.get("form_id")):
                nxt_keys = set(_extract_update_fields(nxt.get("post_data") or []).keys())
                if seen_keys.intersection(nxt_keys):
                    break
                group.append(nxt)
                seen_keys.update(nxt_keys)
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
                "_har_action_index": s.get("_har_action_index"),
                "ir_sources": _collect_ir_sources(group),
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
                    "_har_action_index": s.get("_har_action_index"),
                    "ir_sources": _collect_ir_sources([s]),
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
                "_har_action_index": s.get("_har_action_index"),
                "_har_page_id": s.get("_har_page_id", ""),
            }
            continue
        if (s.get("type") == "invoke" and s.get("method") == "setItemByIdFromClient"
                and s.get("args")):
            args = s["args"]
            # args 形如 [["id_str", 0]]
            value_id = ""
            set_item_row_idx: int | None = None
            if isinstance(args, list) and args and isinstance(args[0], list) and args[0]:
                value_id = str(args[0][0])
                if len(args[0]) >= 2:
                    raw_row = args[0][1]
                    if isinstance(raw_row, int):
                        set_item_row_idx = raw_row
                    elif isinstance(raw_row, str) and raw_row.isdigit():
                        set_item_row_idx = int(raw_row)

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
                    "_har_action_index": s.get("_har_action_index"),
                    "ir_sources": _collect_ir_sources([s]),
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
                    "_har_action_index": s.get("_har_action_index"),
                    "ir_sources": _collect_ir_sources([s]),
                    "_har_page_id": s.get("_har_page_id", ""),
                }
                if s.get("_resp_text"):
                    pick_step["_resp_text"] = s.get("_resp_text")
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
                if set_item_row_idx is not None:
                    pick_step["row_index"] = set_item_row_idx
                out.append(pick_step)
                continue
        out.append(s)
    return out


def _collect_ir_sources(steps: list[Mapping[str, Any]]) -> list[dict[str, int]]:
    """Keep value-safe HAR action provenance through merge/lowering transforms."""
    sources: list[dict[str, int]] = []
    seen: set[tuple[int, int]] = set()
    for step in steps:
        inherited = step.get("ir_sources")
        candidates = inherited if isinstance(inherited, list) else [{
            "source_index": step.get("_har_index"),
            "action_index": step.get("_har_action_index"),
        }]
        for item in candidates:
            if not isinstance(item, Mapping):
                continue
            source_index = item.get("source_index")
            action_index = item.get("action_index")
            if not isinstance(source_index, int) or not isinstance(action_index, int):
                continue
            key = (source_index, action_index)
            if key in seen:
                continue
            seen.add(key)
            sources.append({"source_index": source_index, "action_index": action_index})
    return sources


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


def _entry_row_payload(step: dict) -> tuple[str, dict] | tuple[str, None]:
    post_data = step.get("post_data")
    if not (isinstance(post_data, list) and post_data and isinstance(post_data[0], dict)):
        return "", None
    for control_key, payload in post_data[0].items():
        if isinstance(payload, dict):
            return str(control_key), payload
    return "", None


def _entry_row_field_key(step: dict) -> str:
    _control_key, payload = _entry_row_payload(step)
    if not isinstance(payload, dict):
        return ""
    return str(payload.get("fieldKey") or "").strip()


def _entry_row_selected_rows(step: dict) -> list:
    _control_key, payload = _entry_row_payload(step)
    if not isinstance(payload, dict):
        return []
    rows = payload.get("selDatas")
    return rows if isinstance(rows, list) else []


def _response_grid_payload(resp_text: str, grid_key: str) -> tuple[dict[str, int], list[list[Any]]]:
    try:
        payload = json.loads(resp_text)
    except Exception:
        return {}, []

    def walk(node: Any) -> tuple[dict[str, int], list[list[Any]]] | None:
        if isinstance(node, dict):
            data = node.get("data")
            if node.get("k") == grid_key and isinstance(data, dict):
                dataindex = data.get("dataindex")
                rows = data.get("rows")
                if isinstance(dataindex, dict) and isinstance(rows, list):
                    return dataindex, rows
            for value in node.values():
                found = walk(value)
                if found:
                    return found
        elif isinstance(node, list):
            for item in node:
                found = walk(item)
                if found:
                    return found
        return None

    found = walk(payload)
    if not found:
        return {}, []
    dataindex, rows = found
    return (
        {str(key): int(index) for key, index in dataindex.items() if isinstance(index, int)},
        [row for row in rows if isinstance(row, list)],
    )


def _annotate_dynamic_query_row_selections(steps: list[dict]) -> None:
    """Link a recorded list query to the following row click.

    The runtime must rebuild selDatas from the current query result. Otherwise a
    changed business key can return no rows while entryRowClick still opens the
    stale row captured in the HAR.
    """
    for index, step in enumerate(steps):
        if (
            step.get("type") != "invoke"
            or step.get("ac") != "entryRowClick"
            or str(step.get("form_id") or "") == "wf_task"
        ):
            continue
        control_key, payload = _entry_row_payload(step)
        selected_rows = payload.get("selDatas") if isinstance(payload, dict) else None
        if not (
            control_key
            and isinstance(selected_rows, list)
            and selected_rows
            and isinstance(selected_rows[0], list)
        ):
            continue

        source = next(
            (
                candidate
                for candidate in reversed(steps[:index])
                if candidate.get("type") == "invoke"
                and candidate.get("ac") == "commonSearch"
                and str(candidate.get("form_id") or "") == str(step.get("form_id") or "")
                and str(candidate.get("_resp_text") or "")
            ),
            None,
        )
        if not source:
            continue
        dataindex, rows = _response_grid_payload(str(source.get("_resp_text") or ""), control_key)
        if not dataindex or not rows:
            continue

        template_row = selected_rows[0]
        recorded_row = max(
            rows,
            key=lambda row: sum(
                1
                for value in template_row
                if value not in ("", None)
                and any(str(value) == str(cell) for cell in row)
            ),
        )
        field_map: list[str] = []
        used_fields: set[str] = set()
        for value in template_row:
            candidates = [
                field
                for field, position in dataindex.items()
                if position < len(recorded_row)
                and str(recorded_row[position]) == str(value)
                and field not in used_fields
            ]
            chosen = candidates[0] if len(candidates) == 1 else ""
            field_map.append(chosen)
            if chosen:
                used_fields.add(chosen)

        step["dynamic_row_source_step_id"] = str(source.get("id") or "")
        step["dynamic_row_grid_key"] = control_key
        step["dynamic_row_field_map"] = field_map
        step["dynamic_row_retry_until_found"] = True
        step["dynamic_row_max_attempts"] = 40
        step["dynamic_row_interval_seconds"] = 1


def _is_workflow_task_entry_row(step: dict) -> bool:
    """Return true for workflow task list row clicks.

    These rows are runtime navigation anchors produced by wf_task searches. They
    should be rebuilt from the current billno/task row during replay, not exposed
    as user-maintained selector fields.
    """
    if step.get("type") != "invoke" or step.get("ac") != "entryRowClick":
        return False
    form_id = str(step.get("form_id") or "")
    key = str(step.get("key") or "")
    if form_id != "wf_task":
        return False
    control_key, _payload = _entry_row_payload(step)
    return (control_key or key) == "billlistap"


def _infer_entry_row_selector_contexts(steps: list[dict]) -> dict[str, dict[str, str]]:
    """Map generic F7 entryRowClick steps back to the parent field that opened them.

    Some HR fields, such as khr_upperson, open a generic F7 form (hrpi_employee)
    instead of using setItemByIdFromClient. The chosen row is on the child form,
    while the business field label lives on the parent grid click.
    """
    contexts: dict[str, dict[str, str]] = {}
    pending_parent: dict[str, str] | None = None
    active_selector: dict[str, str] | None = None

    for step in steps:
        if step.get("type") != "invoke":
            continue

        form_id = str(step.get("form_id") or "")
        ac = str(step.get("ac") or "")
        key = str(step.get("key") or "")

        if (
            active_selector
            and form_id == active_selector.get("selector_form_id")
            and ac == "entryRowClick"
            and _entry_row_selected_rows(step)
        ):
            contexts[str(step.get("id") or "")] = dict(active_selector)
            continue

        if (
            active_selector
            and form_id == active_selector.get("selector_form_id")
            and ac == "click"
            and key.lower() in {"btnok", "btnconfirm", "bar_confirm"}
        ):
            active_selector = None
            continue

        if ac == "loadData" and pending_parent and form_id and form_id != pending_parent.get("parent_form_id"):
            active_selector = {
                **pending_parent,
                "selector_form_id": form_id,
                "selector_app_id": str(step.get("app_id") or ""),
            }
            pending_parent = None
            continue

        field_key = _entry_row_field_key(step)
        if ac == "entryRowClick" and field_key and not _entry_row_selected_rows(step):
            pending_parent = {
                "parent_form_id": form_id,
                "parent_app_id": str(step.get("app_id") or ""),
                "parent_field_key": field_key,
                "label": _resolve_field_label(field_key, entity_id=form_id),
            }

    return contexts


def _extract_entry_row_selector(step: dict, context: dict[str, str] | None = None) -> dict[str, Any] | None:
    """从 F7 entryRowClick 中提取用户选择的对象。

    有些选择器不是 setItemByIdFromClient，而是在列表弹窗里 entryRowClick
    一行后再点确定。若不暴露这个 selDatas，用户无法在预览页维护“选择人”。
    """
    if step.get("type") != "invoke" or step.get("ac") != "entryRowClick":
        return None
    if _is_workflow_task_entry_row(step):
        return None
    form_id = str(step.get("form_id") or "")
    selector_key, label, field_key = _F7_SELECTOR_FORM_LABELS.get(form_id, ("", "", ""))
    if not selector_key and context:
        field_key = str(context.get("parent_field_key") or "")
        selector_key = field_key
        label = str(context.get("label") or _resolve_field_label(field_key, entity_id=context.get("parent_form_id")))
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
            "parent_form_id": str((context or {}).get("parent_form_id") or ""),
            "parent_field_key": str((context or {}).get("parent_field_key") or ""),
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
            har_page_id = str(step.get("_har_page_id") or "")
            if (
                step.get("type") == "invoke"
                and step.get("ac") == "loadData"
                and har_page_id
                and not _is_l2_page_id(har_page_id)
            ):
                # Portal cards and decorative sibling forms are often created by
                # browser-only clientCallBack batches that are intentionally
                # filtered from replay. Their recorded L3 pageId is therefore
                # usable only if an earlier retained response harvests a fresh
                # L3 for the same form. Falling back to the business L2 can
                # reopen sibling forms and corrupt the active form context.
                step["requires_harvested_l3_page"] = True


def _annotate_repeated_menu_targets(steps: list[dict], main_form: str) -> None:
    """Bind later menu reopens to the L2 forms that immediately consume them."""
    for menu_idx, step in enumerate(steps):
        if step.get("ac") != "menuItemClick" or step.get("target_form"):
            continue
        args = step.get("args") or []
        menu_arg = args[0] if args and isinstance(args[0], dict) else {}
        menu_id = str(menu_arg.get("menuId") or "")
        if not menu_id:
            continue
        l2_prefix = f"{menu_id}root"
        consumed_forms: list[str] = []
        for candidate in steps[menu_idx + 1:]:
            if candidate.get("ac") in {"menuItemClick", "appItemClick"}:
                break
            har_page_id = str(candidate.get("_har_page_id") or "")
            form_id = str(candidate.get("form_id") or "")
            if har_page_id.startswith(l2_prefix) and form_id and form_id not in consumed_forms:
                consumed_forms.append(form_id)
        if not consumed_forms:
            continue
        target_form = main_form if main_form in consumed_forms else consumed_forms[0]
        step["target_form"] = target_form
        remaining = [form_id for form_id in consumed_forms if form_id != target_form]
        if remaining:
            step["target_forms"] = remaining
        step["env_sensitive"] = "high"
        step["resolve_by"] = "menu_path_or_form"
        step["navigation_form_id"] = target_form


def _inject_workflow_message_center_bootstrap(steps: list[dict]) -> list[dict]:
    """Ensure wf_task has the message-center page model before task search.

    Browser navigation opens ``wf_msg_center`` via ``openUrl`` and its loadData
    response creates the real UUID pageId for ``wf_task``/``wf_task_list``. HAR
    often contains only the later wf_task requests, so API replay otherwise
    falls back to root or an unrelated menu L2 pageId and searches an empty list.
    """
    if not any(str(step.get("form_id") or "") == "wf_task" for step in steps):
        return steps

    first_workflow_idx = next(
        (
            idx
            for idx, step in enumerate(steps)
            if str(step.get("form_id") or "") in {"wf_msg_message", "wf_task", "wf_approvalbill"}
        ),
        None,
    )
    if first_workflow_idx is None:
        return steps

    if any(
        str(step.get("form_id") or "") == "wf_msg_center"
        and str(step.get("ac") or "") == "loadData"
        for step in steps[:first_workflow_idx]
    ):
        return steps

    existing_ids = {str(step.get("id") or "") for step in steps}

    def _unique_id(base: str) -> str:
        if base not in existing_ids:
            existing_ids.add(base)
            return base
        idx = 2
        while f"{base}_{idx}" in existing_ids:
            idx += 1
        value = f"{base}_{idx}"
        existing_ids.add(value)
        return value

    bootstrap_steps = [
        {
            "id": _unique_id("open_wf_msg_center"),
            "type": "open_form",
            "form_id": "wf_msg_center",
            "app_id": "bos",
            "lazy": False,
        },
        {
            "id": _unique_id("load_wf_msg_center"),
            "type": "invoke",
            "form_id": "wf_msg_center",
            "app_id": "bos",
            "ac": "loadData",
            "key": "",
            "method": "loadData",
            "args": [],
            "post_data": [],
        },
    ]
    return steps[:first_workflow_idx] + bootstrap_steps + steps[first_workflow_idx:]


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
        or field_key_l in _AUTO_RESOLVE_CODE_FIELD_HINTS
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


def _upload_pick_field_id(step: dict) -> str:
    raw = str(step.get("id") or step.get("key") or step.get("method") or "attachment")
    return f"upload_{_sanitize(raw) or 'attachment'}_file_path"


def _append_upload_file_pick_fields(pick_fields_map: OrderedDict, steps: list[dict]) -> None:
    for step in steps or []:
        if not (
            step.get("requires_user_file")
            and step.get("upload_replay_strategy") == "user_file_required"
            and _is_real_upload_action_step(step)
        ):
            continue
        step_id = _upload_pick_field_id(step)
        if step_id in pick_fields_map:
            continue
        names = [str(name) for name in (step.get("recorded_file_names") or []) if str(name or "").strip()]
        label_suffix = f"（录制：{names[0]}）" if names else ""
        pick_fields_map[step_id] = OrderedDict([
            ("value_id", str(step.get("file_path") or "")),
            ("value_name", names[0] if names else ""),
            ("value_code", ""),
            ("value_number", ""),
            ("recorded_value_id", ""),
            ("label", f"附件文件路径{label_suffix}"),
            ("env_sensitive", "medium"),
            ("field_key", "file_path"),
            ("form_id", step.get("form_id", "")),
            ("app_id", step.get("app_id", "")),
            ("source_step_id", step.get("id", "")),
            ("auto_resolve", False),
            ("resolve_by", ""),
            ("resolve_status", "missing_file" if not step.get("file_path") else "manual"),
            ("source_type", "upload_file"),
            ("upload_endpoint", step.get("upload_endpoint") or step.get("upload_url") or step.get("endpoint") or ""),
            ("file_field", step.get("file_field") or step.get("field_name") or "file"),
            ("recorded_file_names", names),
            ("requires_user_file", True),
        ])


def _apply_upload_pick_fields_to_steps(steps: list[dict], pick_fields_map: OrderedDict) -> None:
    if not pick_fields_map:
        return
    for step in steps or []:
        if not (
            step.get("requires_user_file")
            and step.get("upload_replay_strategy") == "user_file_required"
            and _is_real_upload_action_step(step)
        ):
            continue
        pf = pick_fields_map.get(_upload_pick_field_id(step))
        if not isinstance(pf, dict):
            continue
        file_path = str(pf.get("value_id") or pf.get("value_name") or "").strip()
        # value_name initially stores the recorded file name, not a local path.
        recorded_names = {str(name) for name in (pf.get("recorded_file_names") or [])}
        if file_path and file_path not in recorded_names:
            step["file_path"] = file_path
        endpoint = str(pf.get("upload_endpoint") or "").strip()
        if endpoint:
            step["upload_endpoint"] = endpoint
        file_field = str(pf.get("file_field") or "").strip()
        if file_field:
            step["file_field"] = file_field


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


def _refresh_existing_pick_field(
    existing: dict,
    base_id: str,
    *,
    form_id: str,
    field_key: str,
    value_id: str,
    value_name: str,
    value_code: str,
    source_step_id: str,
    app_id: str = "",
) -> bool:
    """Refresh a repeated same-form pick field with the latest recorded value."""
    item = existing.get(base_id)
    if not isinstance(item, dict):
        return False
    if str(item.get("form_id") or "") != str(form_id or ""):
        return False
    if str(item.get("field_key") or "").lower() != str(field_key or "").lower():
        return False
    item["value_id"] = value_id
    item["value_name"] = value_name
    item["value_code"] = value_code
    item["source_step_id"] = source_step_id
    if app_id:
        item["app_id"] = app_id
    return True


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


def _normalize_scalar_for_rule(value: Any) -> str:
    if isinstance(value, dict):
        for key in ("value_code", "number", "id", "zh_CN", "GLang"):
            if value.get(key) not in (None, ""):
                return str(value.get(key)).strip()
    if isinstance(value, list):
        return ",".join(_normalize_scalar_for_rule(item) for item in value if item not in (None, ""))
    return str(value or "").strip()


def _last_update_field_ref(
    steps: list[dict],
    *,
    form_id: str,
    field_key: str,
) -> tuple[int, dict, str, Any] | None:
    wanted_form = str(form_id or "")
    wanted_field = str(field_key or "").lower()
    found: tuple[int, dict, str, Any] | None = None
    for idx, step in enumerate(steps or []):
        if step.get("type") != "update_fields":
            continue
        if wanted_form and str(step.get("form_id") or "") != wanted_form:
            continue
        fields = step.get("fields") or {}
        if not isinstance(fields, dict):
            continue
        for key, value in fields.items():
            if str(key or "").lower() == wanted_field:
                found = (idx, step, str(key), value)
    return found


def _has_model_field_step(steps: list[dict], *, form_id: str, field_key: str) -> bool:
    wanted_form = str(form_id or "")
    wanted_field = str(field_key or "").lower()
    for step in steps or []:
        if wanted_form and str(step.get("form_id") or "") != wanted_form:
            continue
        if step.get("type") == "update_fields":
            fields = step.get("fields") or {}
            if isinstance(fields, dict) and any(str(key).lower() == wanted_field for key in fields):
                return True
        if step.get("type") == "pick_basedata" and str(step.get("field_key") or "").lower() == wanted_field:
            return True
    return False


def _copy_replay_context_from_step(source: dict) -> dict[str, Any]:
    copied: dict[str, Any] = {}
    for key in (
        "_har_page_id",
        "preserve_l2_page",
        "row_index",
        "recorded_pageid_source",
        "recorded_pageid_source_step_id",
        "recorded_pageid_source_type",
        "recorded_pageid_source_form_match",
        "recorded_pageid_source_kind",
        "recorded_pageid_retained",
        "pageid_recovery_strategy",
        "pageid_recovery_source_step_id",
    ):
        if key in source:
            copied[key] = copy.deepcopy(source.get(key))
    return copied


def _compact_ir_contract_for_yaml(
    har: dict[str, Any],
    *,
    source_name: str,
    yaml_steps: list[dict[str, Any]],
    vars_meta: Mapping[str, Any],
    pick_fields: Mapping[str, Any],
    ir_flow: Mapping[str, Any] | None = None,
    navigation_policy: Mapping[str, Any] | None = None,
) -> OrderedDict:
    """Build a value-safe IR contract section for generated YAML.

    The full normalized flow remains a separate diagnostic artifact. YAML only
    carries coverage/risk signals so replay, reports, and AI repair can tell
    whether the main parser covered what IR observed.
    """
    try:
        from lib.ir import (
            assess_ir_preview_alignment,
            build_ir_field_bridge,
            build_ir_yaml_bridge,
            build_normalized_flow,
            compact_flow_for_preview,
        )

        flow = dict(ir_flow) if isinstance(ir_flow, Mapping) else build_normalized_flow(har, source_name=source_name)
        ir_preview = compact_flow_for_preview(flow)
        alignment = assess_ir_preview_alignment(
            flow,
            preview_steps=yaml_steps,
            detected_vars=[
                {"name": name, **(dict(meta) if isinstance(meta, Mapping) else {})}
                for name, meta in (vars_meta or {}).items()
                if str(name or "").strip() and not str(name).startswith("_")
            ],
            pick_fields=[
                {"id": field_id, **(dict(meta) if isinstance(meta, Mapping) else {})}
                for field_id, meta in (pick_fields or {}).items()
                if str(field_id or "").strip()
            ],
        )
        checks = alignment.get("checks") or {}
        issues = alignment.get("issues") or []
        warning_codes = sorted({
            str(item.get("code") or "")
            for item in ir_preview.get("warnings", [])
            if item.get("code")
        })
        high_issue_count = sum(
            1
            for issue in issues
            if issue.get("severity") in {"critical", "high"}
        )
        risk_level = str(alignment.get("risk_level") or "high")
        status = "ready"
        if risk_level == "high" or high_issue_count:
            status = "needs_review"
        elif warning_codes:
            status = "ready_with_warnings"

        return OrderedDict([
            ("schema_version", 1),
            ("source", "normalized_flow"),
            ("status", status),
            ("ir_schema_version", (ir_preview.get("schema_version") or "0.1")),
            ("alignment", OrderedDict([
                ("score", int(alignment.get("score") or 0)),
                ("grade", str(alignment.get("grade") or "")),
                ("risk_level", risk_level),
                ("summary", str(alignment.get("summary") or "")),
                ("issue_codes", sorted({
                    str(issue.get("code") or "")
                    for issue in issues
                    if issue.get("code")
                })),
                ("high_issue_count", high_issue_count),
            ])),
            ("coverage", OrderedDict([
                ("api_entry_count", int(checks.get("ir_api_entry_count") or 0)),
                ("ir_step_count", int(checks.get("ir_step_count") or 0)),
                ("yaml_step_count", len(yaml_steps or [])),
                ("ir_role_counts", checks.get("ir_role_counts") or {}),
                ("yaml_role_counts", checks.get("preview_role_counts") or {}),
                ("ir_l2_expected_count", int(checks.get("ir_l2_expected_count") or 0)),
                ("ir_l3_expected_count", int(checks.get("ir_l3_expected_count") or 0)),
                ("yaml_l2_preserve_count", int(checks.get("preview_l2_preserve_count") or 0)),
                ("variable_count", int(checks.get("detected_var_count") or 0)),
                ("pick_field_count", int(checks.get("pick_field_count") or 0)),
            ])),
            ("generation_bridge", build_ir_yaml_bridge(
                flow,
                yaml_steps,
                vars_meta=vars_meta,
                pick_fields=pick_fields,
            )),
            ("field_bridge", build_ir_field_bridge(
                flow,
                yaml_steps,
                vars_meta=vars_meta,
                pick_fields=pick_fields,
            )),
            ("navigation_policy", dict(navigation_policy or {})),
            ("warning_codes", warning_codes),
            ("policy", OrderedDict([
                ("store_full_ir_in_yaml", False),
                ("raw_har_committed", False),
                ("blocks_run", False),
                ("repair_hint", "IR 与主解析链路差异较大时，优先修解析规则而不是手写 YAML。"),
            ])),
        ])
    except Exception as exc:
        return OrderedDict([
            ("schema_version", 1),
            ("source", "normalized_flow"),
            ("status", "diagnostic_failed"),
            ("ir_schema_version", "0.1"),
            ("alignment", OrderedDict([
                ("score", 0),
                ("grade", "E"),
                ("risk_level", "high"),
                ("summary", f"IR contract build failed: {type(exc).__name__}"),
                ("issue_codes", ["ir_contract_failed"]),
                ("high_issue_count", 0),
            ])),
            ("coverage", OrderedDict([
                ("api_entry_count", 0),
                ("ir_step_count", 0),
                ("yaml_step_count", len(yaml_steps or [])),
                ("ir_role_counts", {}),
                ("yaml_role_counts", {}),
                ("ir_l2_expected_count", 0),
                ("ir_l3_expected_count", 0),
                ("yaml_l2_preserve_count", 0),
                ("variable_count", len(vars_meta or {})),
                ("pick_field_count", len(pick_fields or {})),
            ])),
            ("warning_codes", ["ir_contract_failed"]),
            ("policy", OrderedDict([
                ("store_full_ir_in_yaml", False),
                ("raw_har_committed", False),
                ("blocks_run", False),
                ("repair_hint", "先检查 HAR 是否可正常脱敏和规范化。"),
            ])),
        ])


def _append_salary_scope_required_date_steps(
    steps: list[dict],
    observations: dict[str, Any],
) -> list[dict]:
    """Derive editable allowance/welfare effective dates when salary scope needs them.

    In HR salary-adjust forms, scope=3 means the recorded business action covered
    target salary, allowance, and welfare. The browser may carry allowance/welfare
    effective dates as model defaults without explicit updateValue calls; API
    replay must make them explicit so preview/YAML/users can maintain them.
    """
    if not steps:
        return steps
    scope_ref = _last_update_field_ref(
        steps,
        form_id=_SALARY_APPLY_FORM_ID,
        field_key=_SALARY_SCOPE_FIELD_KEY,
    )
    if not scope_ref:
        return steps
    _scope_idx, _scope_step, _scope_key, scope_value = scope_ref
    if _normalize_scalar_for_rule(scope_value) != _SALARY_SCOPE_ALL_VALUE:
        return steps
    effective_ref = _last_update_field_ref(
        steps,
        form_id=_SALARY_TARGET_FORM_ID,
        field_key=_SALARY_EFFECTIVE_DATE_FIELD_KEY,
    )
    if not effective_ref:
        return steps
    effective_idx, effective_step, _effective_key, effective_value = effective_ref
    if effective_value in (None, ""):
        return steps

    insert_idx = effective_idx
    for idx in range(effective_idx + 1, len(steps)):
        step = steps[idx]
        if (
            str(step.get("form_id") or "") == _SALARY_TARGET_FORM_ID
            and str(step.get("ac") or step.get("method") or "").lower() == "close"
        ):
            insert_idx = idx
            break
    context_step = _scope_step
    for idx in range(insert_idx, -1, -1):
        step = steps[idx]
        if str(step.get("form_id") or "") == _SALARY_APPLY_FORM_ID:
            context_step = step
            break

    additions: list[dict[str, Any]] = []
    for field_key in _SALARY_SCOPE_ALL_DATE_FIELDS:
        if _has_model_field_step(steps, form_id=_SALARY_APPLY_FORM_ID, field_key=field_key):
            continue
        step = {
            "type": "update_fields",
            "id": f"fill_{field_key}",
            "form_id": _SALARY_APPLY_FORM_ID,
            "app_id": context_step.get("app_id") or effective_step.get("app_id", "hcdm"),
            "fields": {field_key: copy.deepcopy(effective_value)},
            "_tier": "core",
            "_derived_from": _SALARY_EFFECTIVE_DATE_FIELD_KEY,
            "_derived_reason": "khr_scope=3 requires allowance/welfare effective dates",
            "description": f"填写「{_FIELD_LABELS.get(field_key, field_key)}」",
        }
        step.update(_copy_replay_context_from_step(context_step))
        step["row_index"] = int(effective_step.get("row_index", 0) or 0)
        additions.append(step)

    if not additions:
        return steps
    out = list(steps)
    for offset, step in enumerate(additions, start=1):
        out.insert(insert_idx + offset, step)
    return out


def _existing_model_field_keys(steps: list[dict], form_id: str) -> set[str]:
    form = str(form_id or "")
    keys: set[str] = set()
    for step in steps or []:
        if form and str(step.get("form_id") or "") != form:
            continue
        if step.get("type") == "pick_basedata" and step.get("field_key"):
            keys.add(str(step.get("field_key") or "").lower())
        elif step.get("type") == "update_fields":
            for field_key in (step.get("fields") or {}).keys():
                keys.add(str(field_key or "").lower())
    return keys


def _soft_required_context_items(main_form: str, meta_resolver=None) -> list[dict[str, Any]]:
    form = str(main_form or "")
    configured = _SOFT_REQUIRED_CONTEXT_FIELDS_BY_FORM.get(form) or {}
    if not configured:
        return []
    items: list[dict[str, Any]] = []
    for field_key, cfg in configured.items():
        key = str(field_key or "").lower()
        label = str(cfg.get("label") or _resolve_field_label(key, entity_id=form, meta_resolver=meta_resolver) or key)
        base_entity = ""
        try:
            if meta_resolver:
                base_entity = meta_resolver.get_base_entity(form, key) or ""
            if not base_entity:
                from lib import field_type_catalog as _ftc
                entry = _ftc.get_field_entry(form, key) or {}
                base_entity = str(entry.get("base_entity") or "")
                label = str(label or entry.get("label") or key)
        except Exception:
            pass
        items.append({
            "field_key": key,
            "label": _clean_display_label(label),
            "base_entity": base_entity,
            "reason": str(cfg.get("reason") or "运行时规则要求维护该字段"),
            "resolve_by": str(cfg.get("resolve_by") or "value_code"),
        })
    return items


def _missing_required_context_pick_id(field_key: str) -> str:
    return f"pick_{_sanitize(field_key) or field_key}_id"


def _append_missing_required_context_pick_fields(
    pick_fields_map: OrderedDict,
    steps: list[dict],
    *,
    main_form: str,
    app_id: str,
    meta_resolver=None,
) -> None:
    configured_forms = set(_SOFT_REQUIRED_CONTEXT_FIELDS_BY_FORM)
    present_forms = {
        str(step.get("form_id") or "")
        for step in steps or []
        if str(step.get("form_id") or "") in configured_forms
    }
    if main_form in configured_forms:
        present_forms.add(main_form)
    for form_id in sorted(present_forms):
        existing_fields = _existing_model_field_keys(steps, form_id)
        form_app_id = next((str(step.get("app_id") or "") for step in steps or [] if step.get("form_id") == form_id), app_id)
        for item in _soft_required_context_items(form_id, meta_resolver=meta_resolver):
            field_key = item["field_key"]
            existing_pf = None
            for existing_id, existing_item in pick_fields_map.items():
                if not isinstance(existing_item, dict):
                    continue
                if (
                    str(existing_item.get("form_id") or "") == form_id
                    and str(existing_item.get("field_key") or "").lower() == field_key
                ):
                    existing_pf = existing_item
                    break
            if existing_pf is not None:
                existing_pf["env_sensitive"] = "high"
                existing_pf["required_context"] = True
                existing_pf.setdefault("source", "runtime_rule")
                existing_pf.setdefault("reason", item["reason"])
                existing_pf.setdefault("base_entity", item.get("base_entity", ""))
                if not existing_pf.get("resolve_by"):
                    existing_pf["resolve_by"] = item["resolve_by"]
                continue
            if field_key in existing_fields:
                continue
            pf_id = _missing_required_context_pick_id(field_key)
            if pf_id in pick_fields_map:
                continue
            pick_fields_map[pf_id] = OrderedDict([
                ("value_id", ""),
                ("value_name", ""),
                ("value_code", ""),
                ("value_number", ""),
                ("recorded_value_id", ""),
                ("label", item["label"]),
                ("env_sensitive", "high"),
                ("field_key", field_key),
                ("form_id", form_id),
                ("app_id", form_app_id),
                ("auto_resolve", True),
                ("resolve_by", item["resolve_by"]),
                ("resolve_status", "missing_required_context"),
                ("required_context", True),
                ("source", "runtime_rule"),
                ("reason", item["reason"]),
                ("base_entity", item.get("base_entity", "")),
            ])


def _append_missing_required_context_steps(
    steps: list[dict],
    *,
    main_form: str,
    app_id: str,
    pick_field_overrides: dict | None = None,
    meta_resolver=None,
) -> list[dict]:
    """Insert soft-required context pick steps only when the user supplied a value."""
    if not steps or not pick_field_overrides:
        return steps
    additions: list[dict[str, Any]] = []
    configured_forms = set(_SOFT_REQUIRED_CONTEXT_FIELDS_BY_FORM)
    present_forms = {
        str(step.get("form_id") or "")
        for step in steps or []
        if str(step.get("form_id") or "") in configured_forms
    }
    if main_form in configured_forms:
        present_forms.add(main_form)
    for form_id in sorted(present_forms):
        existing_fields = _existing_model_field_keys(steps, form_id)
        form_app_id = next((str(step.get("app_id") or "") for step in steps or [] if step.get("form_id") == form_id), app_id)
        for item in _soft_required_context_items(form_id, meta_resolver=meta_resolver):
            field_key = item["field_key"]
            if field_key in existing_fields:
                continue
            pf_id = _missing_required_context_pick_id(field_key)
            override = pick_field_overrides.get(pf_id) if isinstance(pick_field_overrides, dict) else None
            if not isinstance(override, dict):
                continue
            value_code = str(override.get("value_code") or override.get("value_number") or "").strip()
            value_id = str(override.get("value_id") or "").strip()
            value_name = str(override.get("value_name") or "").strip()
            replay_value = value_code or value_id or value_name
            if not replay_value:
                continue
            additions.append({
                "type": "pick_basedata",
                "id": f"pick_{field_key}_required_context",
                "form_id": form_id,
                "app_id": form_app_id,
                "field_key": field_key,
                "value_id": replay_value,
                "value_name": value_name,
                "value_code": value_code,
                "_tier": "core",
                "_is_required_context": True,
            })
    if not additions:
        return steps
    out = list(steps)
    insert_pos = _write_anchor_pos_for_form(out, main_form)
    for offset, step in enumerate(additions):
        out.insert(insert_pos + offset, step)
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

    def remember(content: Any, ntype: Any) -> None:
        text = str(content or "").strip()
        if not text or text in seen:
            return
        if ntype == 0 or any(kw in text for kw in success_kw):
            return
        seen.add(text)
        messages.append({
            "content": text,
            "type": ntype,
            "source": "har_recorded",
        })

    for cmd in _iter_response_actions(resp):
        action = str(cmd.get("a") or "")
        if action == "ShowNotificationMsg":
            for payload in cmd.get("p", []):
                if isinstance(payload, dict):
                    remember(payload.get("content"), payload.get("type"))
            continue
        if action.lower() == "showmessage":
            for payload in cmd.get("p", []):
                if isinstance(payload, dict):
                    remember(
                        payload.get("msg") or payload.get("message") or payload.get("content"),
                        payload.get("messageType"),
                    )
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


def _maintenance_order_for_step_field(steps: list[dict], index: int, field_key: str = "") -> int:
    """Return a stable UI order that preserves HAR step order and field order."""
    base = max(index, 0) * 1000
    step = steps[index] if 0 <= index < len(steps) else {}
    field = str(field_key or "").lower()
    fields = step.get("fields") or {}
    if field and isinstance(fields, dict):
        for pos, key in enumerate(fields.keys()):
            if str(key).lower() == field:
                return base + pos
    return base


def _dedupe_step_ids(steps: list[dict]) -> None:
    """Add numeric suffixes to duplicate step ids in-place."""
    id_counts: dict[str, int] = {}
    for step in steps or []:
        sid = str(step.get("id") or "")
        if not sid:
            continue
        id_counts[sid] = id_counts.get(sid, 0) + 1
    id_seen: dict[str, int] = {}
    for step in steps or []:
        sid = str(step.get("id") or "")
        if sid and id_counts.get(sid, 0) > 1:
            id_seen[sid] = id_seen.get(sid, 0) + 1
            step["id"] = f"{sid}_{id_seen[sid]}" if id_seen[sid] > 1 else sid


def _collect_var_refs(value: Any, refs: set[str]) -> None:
    if isinstance(value, str):
        refs.update(_VAR_REF_RE.findall(value))
    elif isinstance(value, dict):
        for item in value.values():
            _collect_var_refs(item, refs)
    elif isinstance(value, list):
        for item in value:
            _collect_var_refs(item, refs)


def _infer_vars_meta_from_steps(steps: list[dict], main_form: str = "", meta_resolver=None) -> OrderedDict:
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
            ("order", _maintenance_order_for_step_field(steps, step_idx, field_key)),
            ("source_step_id", scope["source_step_id"] or str(step.get("id") or "")),
            ("write_step_id", scope["write_step_id"]),
        ])
        item.update(_field_type_info(
            scope["form_id"],
            field_key,
            meta_resolver=meta_resolver,
            fallback_category=_fallback_field_category(field_key),
            fallback_panel="vars",
        ))
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
        if target_form_id:
            for idx, step in enumerate(steps):
                if str(step.get("form_id") or "") == target_form_id:
                    return idx
        return 0

    for pf_id, pf_meta in pick_fields_map.items():
        if not isinstance(pf_meta, dict):
            continue
        idx = find_step_index(str(pf_id), pf_meta)
        scope = _step_scope_for_index(steps, idx, main_form)
        field_key = str((pf_meta or {}).get("field_key") or "")
        pf_meta.setdefault("form_label", scope["form_label"])
        pf_meta.setdefault("group_key", scope["group_key"])
        pf_meta.setdefault("group_label", scope["group_label"])
        pf_meta.setdefault("order", _maintenance_order_for_step_field(steps, idx, field_key))
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
        field_type_source = ""
        if meta_resolver and form_id and field_key:
            try:
                field_type = meta_resolver.get_field_type(form_id, field_key) or ""
                if field_type:
                    field_type_source = "metadata"
            except Exception:
                field_type = ""
        if not field_type and form_id and field_key:
            try:
                from lib import field_type_catalog as _ftc
                catalog_item = _ftc.get_field_entry(form_id, field_key)
                if catalog_item:
                    field_type = str(catalog_item.get("raw_type") or "")
                    field_type_source = "runtime_catalog" if field_type else ""
            except Exception:
                pass
        if field_type:
            meta.setdefault("metadata_type", field_type)
            try:
                from lib import field_type_catalog as _ftc
                category = _ftc.canonical_category(field_type)
                meta.setdefault("field_category", category)
                meta.setdefault("field_panel", _ftc.panel_for_category(category))
                meta.setdefault("field_type_source", field_type_source or "metadata")
            except Exception:
                pass
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


def _field_type_info(
    form_id: str,
    field_key: str,
    *,
    value: Any = None,
    meta_resolver=None,
    fallback_category: str = "",
    fallback_panel: str = "",
) -> dict[str, Any]:
    """Return normalized field type metadata for vars/pick_fields/preview catalog."""
    form_id = str(form_id or "")
    field_key = str(field_key or "")
    raw_type = ""
    source = ""
    if meta_resolver and form_id and field_key:
        try:
            raw_type = meta_resolver.get_field_type(form_id, field_key) or ""
            if raw_type:
                source = "metadata"
        except Exception:
            raw_type = ""
    try:
        from lib import field_type_catalog as _ftc
        item = _ftc.classify_for_import(
            form_id=form_id,
            field_key=field_key,
            raw_type=raw_type,
            value=value,
        )
        category = str(item.get("category") or "")
        if fallback_category and (category == "unknown" or str(item.get("source") or "") == "har_heuristic"):
            category = fallback_category
        panel = str(item.get("panel") or "")
        if panel == "unknown" and fallback_panel:
            panel = fallback_panel
        return {
            "metadata_type": raw_type or str(item.get("raw_type") or ""),
            "field_category": category or fallback_category or "unknown",
            "field_panel": panel or fallback_panel or _ftc.panel_for_category(category),
            "field_type_source": source or str(item.get("source") or ""),
            "base_entity": str(item.get("base_entity") or ""),
            "required": bool(item.get("required")),
        }
    except Exception:
        return {
            "metadata_type": raw_type,
            "field_category": fallback_category or "unknown",
            "field_panel": fallback_panel or "unknown",
            "field_type_source": source or "",
            "base_entity": "",
            "required": False,
        }


def _fallback_field_category(field_key: str, value: Any = None) -> str:
    key = str(field_key or "").lower()
    if _is_date_like_field_key(key):
        return "date"
    if key in _BUSINESS_INPUT_VARIABLE_KEYS or any(hint in key for hint in _SALARY_DETAIL_VALUE_HINTS):
        return "amount" if key != "bizdate" else "date"
    key_class = _classify_key_heuristic(key)
    if key_class in {"number", "unique", "cert"}:
        return "code"
    if key_class in {"name", "phone", "email", "text"}:
        return "text"
    try:
        from lib import field_type_catalog as _ftc
        return _ftc.category_from_value(value, key)
    except Exception:
        return "unknown"


def _location_for_step(step: dict, field_key: str = "") -> str:
    if step.get("row_index", -1) not in (-1, None):
        return "entry"
    blob = " ".join([
        str(step.get("id") or "").lower(),
        str(step.get("key") or "").lower(),
        str(step.get("form_id") or "").lower(),
        str(field_key or "").lower(),
    ])
    if "entry" in blob or "entity" in blob or "grid" in blob:
        return "entry"
    if "f7" in blob or "querylist" in blob:
        return "dialog"
    return "form"


def _build_unified_field_catalog(
    steps: list[dict],
    var_items: list[dict],
    pick_fields: list[dict],
    *,
    main_form: str = "",
    meta_resolver=None,
) -> list[dict[str, Any]]:
    """Build HAR-order field/control catalog for preview diagnostics."""
    var_by_scope: dict[tuple[str, str], list[str]] = {}
    for item in var_items or []:
        form_id = str(item.get("form_id") or "")
        field_key = str(item.get("field_key") or "").lower()
        if form_id and field_key:
            var_by_scope.setdefault((form_id, field_key), []).append(str(item.get("name") or ""))

    pick_by_scope: dict[tuple[str, str], list[str]] = {}
    for item in pick_fields or []:
        form_id = str(item.get("form_id") or "")
        field_key = str(item.get("field_key") or "").lower()
        if form_id and field_key:
            pick_by_scope.setdefault((form_id, field_key), []).append(str(item.get("id") or ""))

    seen: set[tuple[str, str, str, str]] = set()
    catalog: list[dict[str, Any]] = []

    def add_field(idx: int, step: dict, field_key: str, value: Any = None, *, kind: str = "field", action: str = "") -> None:
        form_id = str(step.get("form_id") or main_form or "")
        field_key_s = str(field_key or "")
        if not form_id and not field_key_s:
            return
        scope = (form_id, field_key_s.lower())
        info = _field_type_info(
            form_id,
            field_key_s,
            value=value,
            meta_resolver=meta_resolver,
            fallback_category="basedata" if action == "pick_basedata" else _fallback_field_category(field_key_s, value),
            fallback_panel="",
        )
        var_names = var_by_scope.get(scope, [])
        pick_ids = pick_by_scope.get(scope, [])
        panel = "pick_fields" if pick_ids else "vars" if var_names else info.get("field_panel") or "unknown"
        key = (kind, form_id, field_key_s.lower(), str(step.get("id") or idx))
        if key in seen:
            return
        seen.add(key)
        catalog.append({
            "order": len(catalog) + 1,
            "kind": kind,
            "field_key": field_key_s,
            "label": _resolve_field_label(field_key_s, entity_id=form_id, meta_resolver=meta_resolver) if field_key_s else action,
            "form_id": form_id,
            "step_id": str(step.get("id") or ""),
            "action": action or str(step.get("ac") or step.get("type") or ""),
            "location": _location_for_step(step, field_key_s),
            "metadata_type": info.get("metadata_type", ""),
            "category": info.get("field_category", "unknown"),
            "panel": panel,
            "source": info.get("field_type_source", ""),
            "required": bool(info.get("required")),
            "base_entity": info.get("base_entity", ""),
            "vars": [v for v in var_names if v],
            "pick_fields": [p for p in pick_ids if p],
        })

    for idx, step in enumerate(steps or []):
        stype = step.get("type")
        if stype == "update_fields":
            fields = step.get("fields") or {}
            if isinstance(fields, dict):
                for field_key, value in fields.items():
                    add_field(idx, step, str(field_key), value, action="update_fields")
        elif stype == "pick_basedata":
            add_field(idx, step, str(step.get("field_key") or ""), step.get("value_id"), action="pick_basedata")
        elif (
            step.get("requires_user_file")
            and step.get("upload_replay_strategy") == "user_file_required"
            and _is_real_upload_action_step(step)
        ):
            add_field(idx, step, "file_path", step.get("file_path", ""), kind="field", action="upload_file")
        elif stype == "invoke":
            ac = str(step.get("ac") or "")
            key = str(step.get("key") or "")
            method = str(step.get("method") or "")
            blob = f"{ac} {key} {method}".lower()
            if any(token in blob for token in ("save", "submit", "audit", "newentry", "btnok", "ok", "cancel")):
                category = "dialog" if any(token in blob for token in ("btnok", "ok", "cancel")) else "button"
                catalog.append({
                    "order": len(catalog) + 1,
                    "kind": "control",
                    "field_key": key,
                    "label": key or ac or method,
                    "form_id": str(step.get("form_id") or ""),
                    "step_id": str(step.get("id") or ""),
                    "action": ac or method,
                    "location": _location_for_step(step, key),
                    "metadata_type": "",
                    "category": category,
                    "panel": "structural",
                    "source": "har_action",
                    "required": False,
                    "base_entity": "",
                    "vars": [],
                    "pick_fields": [],
                })
    return catalog


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


def _build_preview_business_flow(
    steps: list[dict[str, Any]],
    var_items: list[dict[str, Any]],
    pick_fields: list[dict[str, Any]],
    main_form: str = "",
) -> list[dict[str, Any]]:
    """Build SAZ-like business flow groups for HAR preview diagnostics.

    Unlike ``business_blocks`` which is field-centric, this structure is
    step-centric.  It helps users and repair agents see which recorded requests
    belong to one business operation, what pageId roles that operation expects,
    and how many maintainable fields are attached to it.
    """
    field_counts: dict[str, dict[str, int]] = {}
    for item in var_items or []:
        group_key = str(item.get("group_key") or "")
        if not group_key:
            continue
        field_counts.setdefault(group_key, {"smart_var_count": 0, "env_field_count": 0})
        field_counts[group_key]["smart_var_count"] += 1
    for item in pick_fields or []:
        group_key = str(item.get("group_key") or "")
        if not group_key:
            continue
        field_counts.setdefault(group_key, {"smart_var_count": 0, "env_field_count": 0})
        field_counts[group_key]["env_field_count"] += 1

    groups: OrderedDict[str, dict[str, Any]] = OrderedDict()

    def ensure(scope: dict[str, str], idx: int) -> dict[str, Any]:
        group_key = scope.get("group_key") or "default:unscoped"
        if group_key not in groups:
            counts = field_counts.get(group_key, {})
            groups[group_key] = {
                "key": group_key,
                "label": scope.get("group_label") or "业务链路",
                "form_id": scope.get("form_id") or "",
                "form_label": scope.get("form_label") or "",
                "write_step_id": scope.get("write_step_id") or "",
                "first_step_index": idx,
                "last_step_index": idx,
                "step_count": 0,
                "input_step_count": 0,
                "write_step_count": 0,
                "control_step_count": 0,
                "smart_var_count": counts.get("smart_var_count", 0),
                "env_field_count": counts.get("env_field_count", 0),
                "pageid_roles": {},
                "step_ids": [],
            }
        return groups[group_key]

    for idx, step in enumerate(steps or []):
        if not isinstance(step, dict):
            continue
        scope = _step_scope_for_index(steps, idx, main_form)
        group = ensure(scope, idx)
        group["last_step_index"] = idx
        group["step_count"] += 1
        role = expected_pageid_role(step)
        group["pageid_roles"][role] = group["pageid_roles"].get(role, 0) + 1
        sid = str(step.get("id") or "")
        if sid and len(group["step_ids"]) < 12:
            group["step_ids"].append(sid)
        if step.get("type") in {"update_fields", "pick_basedata", "select_f7_list_row"}:
            group["input_step_count"] += 1
        elif _is_write_anchor_step(step):
            group["write_step_count"] += 1
        elif step.get("type") == "invoke":
            group["control_step_count"] += 1

    return list(groups.values())


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


def _response_signature_anchor_step(step: dict, signature: dict[str, Any]) -> bool:
    """Whether this step should carry recorded response semantics into YAML."""
    if not is_meaningful_response_signature(signature):
        return False
    if _is_write_anchor_step(step):
        return True
    ac = str(step.get("ac") or "").lower()
    key = str(step.get("key") or "").lower()
    method = str(step.get("method") or "").lower()
    if signature.get("required_field_effects"):
        return True
    if signature.get("required_actions"):
        return ac in {
            "click", "itemclick", "customevent", "afterconfirm",
            "doconfirm", "loaddata", "addnew",
        }
    if method in {"setitembyidfromclient", "setitemvaluebyidfromclient"}:
        return True
    if ac in {"save", "submit", "saveandeffect", "submitandeffect", "audit", "unaudit"}:
        return True
    if key in {"btnok", "ok", "btn_confirm", "btnsave", "bar_save", "bar_submit"}:
        return True
    return False


def _response_contract_level(
    step: dict,
    signature: dict[str, Any],
    *,
    pageid_producer_ids: set[str],
    later_grid_consumers: set[tuple[str, str]],
) -> tuple[str, str]:
    if _is_write_anchor_step(step):
        return "critical", "write_anchor"

    method = str(step.get("method") or "").lower()
    if signature.get("_field_effect_candidates") and (
        step.get("type") in {"update_fields", "pick_basedata"}
        or method in {
            "updatevalue",
            "setitembyidfromclient",
            "setitemvaluebyidfromclient",
        }
    ):
        return "business", "field_callback"

    form_id = str(step.get("form_id") or "")
    ac = str(step.get("ac") or "").lower()
    if ac in {"loaddata", "commonsearch", "query", "getlookuplist"} or method in {
        "loaddata",
        "commonsearch",
        "query",
        "getlookuplist",
    }:
        for schema in signature.get("grid_schemas") or []:
            if not isinstance(schema, dict):
                continue
            control = str(schema.get("control") or "")
            if (form_id, control) in later_grid_consumers:
                return "business", "selector_data_source"

    step_id = str(step.get("id") or "")
    if step_id in pageid_producer_ids:
        # showForm can move between adjacent callbacks at runtime. Exact pageId
        # source metadata plus the consumer's L2/L3 trace is the reliable guard.
        return "", ""

    if _response_signature_anchor_step(step, signature):
        return "advisory", "ui_response_anchor"
    return "", ""


def _attach_expected_response_signatures(steps: list[dict]) -> None:
    """Attach compact, tiered recorded response contracts to replay steps."""
    pageid_producer_ids = {
        str(step.get("recorded_pageid_source_step_id") or "")
        for step in steps
        if step.get("recorded_pageid_source_step_id")
    }
    for index, step in enumerate(steps):
        step.pop("expected_response_signature", None)
        resp_text = str(step.get("_resp_text") or "")
        if not resp_text:
            continue
        signature = build_response_signature_from_text(
            resp_text,
            include_candidates=True,
        )
        if not is_meaningful_response_signature(signature):
            continue
        later_grid_consumers: set[tuple[str, str]] = set()
        has_followup_wait = False
        source_form = str(step.get("form_id") or "")
        for candidate in steps[index + 1:index + 7]:
            candidate_form = str(candidate.get("form_id") or "")
            candidate_ac = str(candidate.get("ac") or "")
            if _is_write_anchor_step(candidate):
                break
            if candidate_form == source_form and candidate_ac in {"addnew", "new"}:
                break
            if (
                candidate_form == source_form
                and candidate_ac in {"entryRowClick", "rowDblClick", "selectRow"}
            ):
                later_grid_consumers.add((
                    candidate_form,
                    str(candidate.get("key") or "billlistap"),
                ))
                break
            if (
                candidate.get("type") == "wait_until"
                and candidate_form == source_form
                and candidate_ac == str(step.get("ac") or "")
            ):
                has_followup_wait = True
        contract_level, anchor_reason = _response_contract_level(
            step,
            signature,
            pageid_producer_ids=pageid_producer_ids,
            later_grid_consumers=later_grid_consumers,
        )
        if not contract_level:
            continue
        specialized = specialize_response_signature(
            signature,
            step,
            contract_level=contract_level,
            anchor_reason=anchor_reason,
        )
        if has_followup_wait and specialized.get("required_grid_schemas"):
            specialized["allow_transient_empty"] = True
        step["expected_response_signature"] = specialized


def _attach_expected_request_signatures(steps: list[dict]) -> None:
    """Attach value-safe request contracts before YAML serialization."""
    for step in steps:
        step.pop("expected_request_signature", None)
        response_contract = step.get("expected_response_signature") or {}
        level = str(response_contract.get("contract_level") or "")
        reason = str(response_contract.get("anchor_reason") or "")
        if _is_write_anchor_step(step):
            level = "critical"
            reason = "write_anchor"
        elif step.get("type") in {
            "update_fields",
            "pick_basedata",
            "select_f7_list_row",
        }:
            level = "business"
            reason = "maintainable_field"
        elif level not in {"critical", "business", "advisory"}:
            continue
        step["expected_request_signature"] = build_request_signature(
            step,
            contract_level=level,
            anchor_reason=reason or "recorded_request",
        )


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


def _assertion_signature(assertion: Mapping[str, Any]) -> tuple:
    return (
        str(assertion.get("type") or ""),
        str(assertion.get("step") or ""),
        str(assertion.get("last_step") or ""),
        str(assertion.get("form_id") or ""),
        str(assertion.get("field_key") or ""),
        str(assertion.get("value") or assertion.get("value_ref") or ""),
        str(assertion.get("target_id") or ""),
        str(assertion.get("kind") or ""),
    )


def _validation_point_id(prefix: str, *parts: Any) -> str:
    raw = "_".join(str(p or "") for p in parts if str(p or "").strip())
    return _sanitize_id(f"{prefix}_{raw}")[:96]


def _step_lookup(steps: list[dict]) -> dict[str, dict]:
    return {
        str(step.get("id") or ""): step
        for step in steps or []
        if step.get("id")
    }


def _validation_point_for_assertion(assertion: Mapping[str, Any], steps_by_id: Mapping[str, dict]) -> OrderedDict:
    atype = str(assertion.get("type") or "")
    step_id = str(assertion.get("step") or "")
    step = steps_by_id.get(step_id, {}) if step_id else {}
    step_desc = str(step.get("description") or "")
    if atype == "no_save_failure":
        label = f"{step_desc or step_id or '保存/提交'}：不能出现保存失败"
        category = "system"
        scope = "write"
        required = True
    elif atype == "no_error_actions":
        label = f"{step_desc or step_id or '关键接口'}：不能返回无效请求/错误提示"
        category = "system"
        scope = "query" if assertion.get("last_step") else "write"
        required = True
    elif atype == "expected_notification":
        label = f"{step_desc or step_id or '业务校验'}：应出现录制时的业务提示"
        category = "system"
        scope = "business_validation"
        required = True
    elif atype == "readback_by_business_key":
        label = "保存/提交后按业务键只读回查到记录"
        category = "readback"
        scope = "database"
        required = not _assertion_is_advisory_like(assertion)
    else:
        label = f"{atype or '断言'}"
        category = "system"
        scope = "runtime"
        required = not _assertion_is_advisory_like(assertion)
    return OrderedDict([
        ("id", _validation_point_id("assert", atype, step_id or ("last" if assertion.get("last_step") else ""))),
        ("label", label),
        ("category", category),
        ("scope", scope),
        ("source", "assertion"),
        ("enabled", True),
        ("required", required),
        ("severity", "advisory" if _assertion_is_advisory_like(assertion) else "strict"),
        ("step_id", step_id),
        ("form_id", step.get("form_id", "")),
        ("form_label", ""),
        ("group_key", step.get("form_id", "") or "runtime"),
        ("group_label", step_desc or step.get("form_id", "") or "运行校验"),
        ("order", 0),
        ("assertion", OrderedDict(assertion)),
        ("help", _validation_point_help(atype)),
    ])


def _assertion_is_advisory_like(assertion: Mapping[str, Any]) -> bool:
    mode = str(assertion.get("mode") or assertion.get("severity") or "").strip().lower()
    return bool(assertion.get("advisory")) or mode in {"advisory", "warn", "warning", "soft"}


def _validation_point_help(kind: str) -> str:
    return {
        "no_save_failure": "用于判断保存/提交是否被业务规则拦截。",
        "no_error_actions": "用于判断接口是否返回无效请求、权限、环境或脚本链路错误。",
        "expected_notification": "用于判断录制时预期出现的业务校验提示是否仍然出现。",
        "readback_by_business_key": "用于判断写入后是否能通过只读查询找到业务记录。",
        "maintained_value_applied": "用于判断用户维护的字段值是否进入了本次运行的目标请求。",
    }.get(kind, "系统运行校验点。")


def _validation_point_order(value: Any) -> int:
    try:
        return int(float(value))
    except Exception:
        return 99999


def _validation_point_override_enabled(
    overrides: Mapping[str, Any] | None,
    point_id: str,
    default: bool,
    *,
    kind: str = "",
    target_id: str = "",
    category: str = "",
    scope: str = "",
    assertion_type: str = "",
    step_id: str = "",
) -> bool:
    if not isinstance(overrides, Mapping):
        return default
    exact = overrides.get(point_id)
    if isinstance(exact, Mapping) and "enabled" in exact:
        return bool(exact.get("enabled"))

    field_kind = str(kind or "")
    field_target = str(target_id or "")
    if field_kind and field_target:
        for item in overrides.values():
            if not isinstance(item, Mapping) or "enabled" not in item:
                continue
            if (
                str(item.get("kind") or "") == field_kind
                and str(item.get("target_id") or "") == field_target
            ):
                return bool(item.get("enabled"))

    atype = str(assertion_type or "")
    astep = str(step_id or "")
    acategory = str(category or "")
    ascope = str(scope or "")
    if atype:
        candidates = []
        for item in overrides.values():
            if not isinstance(item, Mapping) or "enabled" not in item:
                continue
            if str(item.get("assertion_type") or "") != atype:
                continue
            item_step = str(item.get("step_id") or "")
            if astep and item_step and item_step != astep:
                continue
            item_category = str(item.get("category") or "")
            if acategory and item_category and item_category != acategory:
                continue
            item_scope = str(item.get("scope") or "")
            if ascope and item_scope and item_scope != ascope:
                continue
            candidates.append(item)
        if len(candidates) == 1:
            return bool(candidates[0].get("enabled"))

    return default


def _build_validation_points(
    case: Mapping[str, Any],
    validation_point_overrides: Mapping[str, Any] | None = None,
) -> list[OrderedDict]:
    """Build user-facing validation points from assertions and maintainable fields.

    ``validation_points`` is intentionally metadata-first: it explains what can
    be checked and whether it is enabled. Enabled field-level points are mirrored
    into ``assertions`` by ``_apply_validation_points_to_assertions``.
    """
    overrides = validation_point_overrides or {}
    steps = list(case.get("steps") or [])
    steps_by_id = _step_lookup(steps)
    points: list[OrderedDict] = []
    seen_ids: set[str] = set()

    for assertion in case.get("assertions") or []:
        if not isinstance(assertion, Mapping):
            continue
        point = _validation_point_for_assertion(assertion, steps_by_id)
        pid = str(point.get("id") or "")
        if not point.get("required"):
            point["enabled"] = _validation_point_override_enabled(
                overrides,
                pid,
                bool(point.get("enabled", True)),
                category=str(point.get("category") or ""),
                scope=str(point.get("scope") or ""),
                assertion_type=str(assertion.get("type") or ""),
                step_id=str(assertion.get("step") or ""),
            )
        points.append(point)
        seen_ids.add(pid)

    for var_name, meta in (case.get("vars_meta") or {}).items():
        if not isinstance(meta, Mapping):
            continue
        source_step_id = str(meta.get("source_step_id") or "")
        if not source_step_id:
            continue
        pid = _validation_point_id("field", "var", var_name)
        if pid in seen_ids:
            continue
        label = str(meta.get("label") or meta.get("field_key") or var_name)
        assertion = OrderedDict([
            ("type", "maintained_value_applied"),
            ("kind", "variable"),
            ("target_id", str(var_name)),
            ("step", source_step_id),
        ])
        points.append(OrderedDict([
            ("id", pid),
            ("label", f"{label}：维护值进入回放请求"),
            ("category", "recommended"),
            ("scope", "maintainable_field"),
            ("source", "vars_meta"),
            ("enabled", _validation_point_override_enabled(
                overrides,
                pid,
                bool(meta.get("user_overridden")),
                kind="variable",
                target_id=str(var_name),
                category="recommended",
                scope="maintainable_field",
                assertion_type="maintained_value_applied",
                step_id=source_step_id,
            )),
            ("required", False),
            ("severity", "strict"),
            ("kind", "variable"),
            ("target_id", str(var_name)),
            ("field_key", str(meta.get("field_key") or "")),
            ("step_id", source_step_id),
            ("form_id", str(meta.get("form_id") or "")),
            ("form_label", str(meta.get("form_label") or "")),
            ("group_key", str(meta.get("group_key") or meta.get("form_id") or "maintainable")),
            ("group_label", str(meta.get("group_label") or meta.get("form_label") or "录入字段")),
            ("order", _validation_point_order(meta.get("order"))),
            ("assertion", assertion),
            ("help", _validation_point_help("maintained_value_applied")),
        ]))
        seen_ids.add(pid)

    for pick_id, meta in (case.get("pick_fields") or {}).items():
        if not isinstance(meta, Mapping):
            continue
        source_step_id = str(meta.get("source_step_id") or "")
        if not source_step_id:
            source_step_id = str(pick_id)
        if not source_step_id:
            continue
        pid = _validation_point_id("field", "pick", pick_id)
        if pid in seen_ids:
            continue
        label = str(meta.get("label") or meta.get("field_key") or pick_id)
        default_enabled = bool(meta.get("user_overridden") or meta.get("manual_override"))
        assertion = OrderedDict([
            ("type", "maintained_value_applied"),
            ("kind", "environment_field"),
            ("target_id", str(pick_id)),
            ("step", source_step_id),
        ])
        points.append(OrderedDict([
            ("id", pid),
            ("label", f"{label}：维护值进入回放请求"),
            ("category", "recommended"),
            ("scope", "maintainable_field"),
            ("source", "pick_fields"),
            ("enabled", _validation_point_override_enabled(
                overrides,
                pid,
                default_enabled,
                kind="environment_field",
                target_id=str(pick_id),
                category="recommended",
                scope="maintainable_field",
                assertion_type="maintained_value_applied",
                step_id=source_step_id,
            )),
            ("required", False),
            ("severity", "strict"),
            ("kind", "environment_field"),
            ("target_id", str(pick_id)),
            ("field_key", str(meta.get("field_key") or "")),
            ("step_id", source_step_id),
            ("form_id", str(meta.get("form_id") or "")),
            ("form_label", str(meta.get("form_label") or "")),
            ("group_key", str(meta.get("group_key") or meta.get("form_id") or "maintainable")),
            ("group_label", str(meta.get("group_label") or meta.get("form_label") or "选择字段")),
            ("order", _validation_point_order(meta.get("order"))),
            ("assertion", assertion),
            ("help", _validation_point_help("maintained_value_applied")),
        ]))
        seen_ids.add(pid)

    return sorted(
        points,
        key=lambda p: (
            0 if p.get("category") == "system" else 1 if p.get("category") == "recommended" else 2,
            _validation_point_order(p.get("order")),
            str(p.get("label") or p.get("id") or ""),
        ),
    )


def _apply_validation_points_to_assertions(case: OrderedDict) -> None:
    assertions = [
        dict(a)
        for a in (case.get("assertions") or [])
        if isinstance(a, Mapping)
    ]
    seen = {_assertion_signature(a) for a in assertions}
    for point in case.get("validation_points") or []:
        if not isinstance(point, Mapping) or not point.get("enabled"):
            continue
        assertion = point.get("assertion")
        if not isinstance(assertion, Mapping):
            continue
        sig = _assertion_signature(assertion)
        if sig in seen:
            continue
        assertions.append(dict(assertion))
        seen.add(sig)
    case["assertions"] = assertions


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
        suggested = item.get("suggested_assertion") or {}
        if suggested.get("strategy") == "fresh_recorded_context":
            query_step_id = str(suggested.get("query_step") or "")
            for step in case.get("steps") or []:
                if str(step.get("id") or "") != query_step_id:
                    continue
                response_contract = step.get("expected_response_signature")
                if isinstance(response_contract, dict):
                    response_contract["contract_level"] = "advisory"
                    response_contract["anchor_reason"] = "readback_context_not_reproducible"
                break
        elif suggested.get("retry_until_found") and suggested.get("step"):
            query_step_id = str(suggested.get("step") or "")
            for step in case.get("steps") or []:
                if str(step.get("id") or "") != query_step_id:
                    continue
                response_contract = step.get("expected_response_signature")
                if isinstance(response_contract, dict):
                    response_contract["contract_level"] = "advisory"
                    response_contract["anchor_reason"] = "eventual_readback"
                break
        if suggested.get("value_from_runtime") and suggested.get("step"):
            target_step_id = str(suggested.get("step") or "")
            for step in case.get("steps") or []:
                if str(step.get("id") or "") == target_step_id:
                    step["refresh_recorded_l2_context"] = True
                    break
        if policy.get("auto_append") is False:
            continue
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
                [("step", suggested.get("step"))]
                if suggested.get("step") else []
            ),
            *(
                [("strategy", suggested.get("strategy"))]
                if suggested.get("strategy") else []
            ),
            *(
                [("query_step", suggested.get("query_step"))]
                if suggested.get("query_step") else []
            ),
            *(
                [("value_from_runtime", suggested.get("value_from_runtime"))]
                if suggested.get("value_from_runtime") else []
            ),
            *(
                [("retry_until_found", True)]
                if suggested.get("retry_until_found") else []
            ),
            *(
                [("menu_id", suggested.get("menu_id"))]
                if suggested.get("menu_id") else []
            ),
            ("form_id", suggested.get("form_id", "")),
            ("app_id", suggested.get("app_id", "")),
            ("field_key", field_key),
            ("value", value),
            *(
                [("grid_key", suggested.get("grid_key"))]
                if suggested.get("grid_key") else []
            ),
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


def collapse_repeated_polling_steps(steps: list[dict], *, min_repeats: int = 3) -> list[dict]:
    """Collapse consecutive readonly polling requests into one wait_until step."""
    out: list[dict] = []
    i = 0
    while i < len(steps):
        step = steps[i]
        if not _is_polling_step(step):
            out.append(step)
            i += 1
            continue
        signature = _polling_signature(step)
        j = i + 1
        while j < len(steps) and _is_polling_step(steps[j]) and _polling_signature(steps[j]) == signature:
            j += 1
        run = steps[i:j]
        if len(run) >= min_repeats:
            out.append(_build_wait_until_step(run))
        else:
            out.extend(run)
        i = j
    return out


def _is_polling_step(step: dict) -> bool:
    if step.get("type") != "invoke":
        return False
    ac = str(step.get("ac") or "").lower()
    method = str(step.get("method") or "").lower()
    key = str(step.get("key") or "").lower()
    text = " ".join([ac, method, key])
    if any(token in text for token in ("getpercent", "getprogress", "getstatus", "progress", "percent")):
        return True
    return False


def _polling_signature(step: dict) -> tuple:
    return (
        step.get("form_id", ""),
        step.get("app_id", ""),
        step.get("ac", ""),
        step.get("key", ""),
        step.get("method", ""),
        json.dumps(step.get("args", []), ensure_ascii=False, sort_keys=True, default=str),
        json.dumps(step.get("post_data", [{}, []]), ensure_ascii=False, sort_keys=True, default=str),
    )


def _build_wait_until_step(run: list[dict]) -> dict:
    first = dict(run[0])
    method = str(first.get("method") or first.get("ac") or "poll")
    form_short = _form_short(str(first.get("form_id") or "")) or "poll"
    har_index = first.get("_har_index", 0)
    source_optional = all(s.get("optional") or s.get("_tier") == "ui_reaction" for s in run)
    wait_step = {
        "_har_index": har_index,
        "type": "wait_until",
        "id": f"wait_{_sanitize_id(method)}_{_sanitize_id(form_short)}_{har_index}",
        "form_id": first.get("form_id", ""),
        "app_id": first.get("app_id", ""),
        "ac": first.get("ac", ""),
        "key": first.get("key", ""),
        "method": first.get("method", ""),
        "args": first.get("args", []),
        "post_data": first.get("post_data", [{}, []]),
        "condition": {
            "kind": "percent_at_least",
            "threshold": 100,
        },
        "interval_seconds": 1,
        "timeout_seconds": max(10, min(120, len(run) * 2)),
        "max_attempts": max(10, len(run)),
        "source_step_count": len(run),
        "_tier": "ui_reaction" if source_optional else "core",
        "optional": bool(source_optional),
        "wait_source": "collapsed_polling",
    }
    if first.get("_har_page_id"):
        wait_step["_har_page_id"] = first["_har_page_id"]
    if first.get("preserve_l2_page"):
        wait_step["preserve_l2_page"] = True
    return wait_step


def insert_workflow_task_wait_steps(steps: list[dict]) -> list[dict]:
    """Insert an explicit wait before clicking an async workflow task row.

    Recorded workflow HARs often contain:
    submit/save -> wf_task/commonSearch(billno=recorded) -> entryRowClick.
    Replay must wait for the runtime billno row to appear before clicking.
    """
    out: list[dict] = []
    for idx, step in enumerate(steps):
        out.append(step)
        billno = _workflow_task_billno_search_value(step)
        if not billno:
            continue
        if _next_step_is_workflow_wait(steps, idx):
            continue
        if not _workflow_task_entry_click_follows(steps, idx):
            continue
        out.append(_build_workflow_task_wait_step(step, billno))
    return out


def _workflow_task_billno_search_value(step: dict) -> str:
    if step.get("type") != "invoke":
        return ""
    if str(step.get("form_id") or "") != "wf_task":
        return ""
    if str(step.get("ac") or "") != "commonSearch":
        return ""
    for criteria_group in step.get("args") or []:
        if not isinstance(criteria_group, list):
            continue
        for criteria in criteria_group:
            if not isinstance(criteria, dict):
                continue
            field_names = criteria.get("FieldName")
            if not isinstance(field_names, list) or "billno" not in field_names:
                continue
            values = criteria.get("Value")
            if isinstance(values, list) and values:
                return str(values[0] or "").strip()
            if values not in (None, ""):
                return str(values).strip()
    return ""


def _next_step_is_workflow_wait(steps: list[dict], idx: int) -> bool:
    if idx + 1 >= len(steps):
        return False
    nxt = steps[idx + 1]
    return (
        nxt.get("type") == "wait_until"
        and str(nxt.get("form_id") or "") == "wf_task"
        and str((nxt.get("condition") or {}).get("kind") or "") == "grid_row_exists"
    )


def _workflow_task_entry_click_follows(steps: list[dict], idx: int) -> bool:
    for nxt in steps[idx + 1: idx + 8]:
        if nxt.get("type") == "wait_until" and str(nxt.get("form_id") or "") == "wf_task":
            return False
        if str(nxt.get("form_id") or "") == "wf_task" and str(nxt.get("ac") or "") == "entryRowClick":
            return True
        if str(nxt.get("form_id") or "").startswith("wf_") and str(nxt.get("ac") or "") in {"save", "submit", "audit"}:
            return False
        if str(nxt.get("form_id") or "") not in {"wf_task", "wf_msg_message", "wf_msg_center", "wf_approvalbill"}:
            if str(nxt.get("ac") or "") in {"save", "submit", "audit"}:
                return False
    return False


def _build_workflow_task_wait_step(search_step: dict, billno: str) -> dict:
    har_index = search_step.get("_har_index", 0)
    wait_step = {
        "_har_index": har_index,
        "type": "wait_until",
        "id": f"wait_wf_task_billno_{har_index}",
        "form_id": "wf_task",
        "app_id": search_step.get("app_id", "bos"),
        "ac": search_step.get("ac", "commonSearch"),
        "key": search_step.get("key", "filtercontainerap"),
        "method": search_step.get("method", "commonSearch"),
        "args": copy.deepcopy(search_step.get("args") or []),
        "post_data": copy.deepcopy(search_step.get("post_data") or [{}, []]),
        "condition": {
            "kind": "grid_row_exists",
            "grid_key": "billlistap",
            "field_key": "billno",
            "value": billno,
            "match_fields": ["billno"],
        },
        "interval_seconds": 1,
        "timeout_seconds": 15,
        "max_attempts": 15,
        "source_step_count": 1,
        "_tier": "core",
        "wait_source": "workflow_task_search",
    }
    if search_step.get("_har_page_id"):
        wait_step["_har_page_id"] = search_step["_har_page_id"]
    if search_step.get("preserve_l2_page"):
        wait_step["preserve_l2_page"] = True
    return wait_step


def _sanitize_id(value: str) -> str:
    return re.sub(r"[^A-Za-z0-9_]+", "_", str(value or "")).strip("_").lower() or "step"


def build_yaml_case(
    har_path: Path,
    case_name: str | None = None,
    var_overrides: dict | None = None,
    pick_field_overrides: dict | None = None,
    validation_point_overrides: dict | None = None,
    meta_resolver=None,
    include_readback_assertions: bool = False,
) -> str:
    har = load_har(har_path)
    try:
        from lib.ir import build_normalized_flow
        ir_flow = build_normalized_flow(har, source_name=har_path.name)
    except Exception as exc:
        log.warning("HAR IR 构建失败，主解析链路继续执行: %s", exc)
        ir_flow = {}
    field_observations = _collect_har_field_observations(har)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)
    raw_steps = collapse_repeated_polling_steps(raw_steps)
    raw_steps = insert_workflow_task_wait_steps(raw_steps)
    raw_steps = _drop_locked_update_fields(raw_steps, field_observations)
    raw_steps = annotate_recorded_pageid_sources(raw_steps)

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
    try:
        from lib.ir import apply_ir_navigation_policy
        navigation_policy = apply_ir_navigation_policy(
            ir_flow,
            cleaned,
            main_form=main_form,
            decorative_form_ids=_NAVIGATION_FORM_IDS,
        )
    except Exception:
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
    try:
        from lib.ir import apply_ir_navigation_policy
        navigation_policy = apply_ir_navigation_policy(
            ir_flow,
            cleaned,
            main_form=main_form,
            decorative_form_ids=_NAVIGATION_FORM_IDS,
        )
    except Exception as exc:
        log.warning("IR 导航策略应用失败，回退到旧规则: %s", exc)
        _mark_context_bridge_steps_required(cleaned, main_form)
        navigation_policy = {
            "schema_version": 1,
            "stage": "stage_1_navigation_list",
            "mode": "fallback",
            "status": "diagnostic_failed",
            "error": type(exc).__name__,
        }

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
            # 方案B兜底：如果 _har_page_id 不可用，用启发式——仅取 menuItemClick 后
            # 紧跟的 loadData 小段（在下一个业务动作/open_form/menuItemClick 之前），
            # form_id != main_form。不要一路扫到后续待办/审批/弹窗表单，否则会把
            # wf_task、proposalplan 等独立页面误绑成菜单 L2。
            if not target_forms_set:
                fallback_started = False
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
                        fallback_started = True
                        continue
                    if fallback_started:
                        break
                    if step.get("optional") and step.get("ac") in ("selectTab", "getFrequentData", "click"):
                        continue
                    break
            if target_forms_set:
                cleaned[menu_idx]["target_forms"] = sorted(target_forms_set)
            # 移除 menuItemClick 之后的第一个 open_form(main_form)
            # （该 open_form 会通过 getConfig 获取错误的独立 pageId）
            for i in range(menu_idx + 1, len(cleaned)):
                if cleaned[i].get("type") == "open_form" and cleaned[i].get("form_id") == main_form:
                    cleaned.pop(i)
                    break
    _annotate_repeated_menu_targets(cleaned, main_form)

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

    cleaned = _inject_workflow_message_center_bootstrap(cleaned)

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
    cleaned = _append_salary_scope_required_date_steps(cleaned, field_observations)
    cleaned = _drop_locked_update_fields(cleaned, field_observations)
    cleaned = _append_missing_required_context_steps(
        cleaned,
        main_form=main_form,
        app_id=_context_app_id,
        pick_field_overrides=pick_field_overrides,
        meta_resolver=meta_resolver,
    )
    cleaned = _drop_portal_side_effect_steps(cleaned, main_form)
    try:
        navigation_policy = apply_ir_navigation_policy(
            ir_flow,
            cleaned,
            main_form=main_form,
            decorative_form_ids=_NAVIGATION_FORM_IDS,
        )
    except Exception:
        _mark_navigation_steps_optional(cleaned, main_form)

    # ⭐ step ID 去重：同名 ID 加数字后缀
    _dedupe_step_ids(cleaned)

    _mark_recorded_business_validations(cleaned)
    cleaned = _ensure_workflow_approval_update_steps(cleaned, field_observations)
    _annotate_dynamic_query_row_selections(cleaned)
    cleaned = annotate_recorded_pageid_sources(cleaned)
    cleaned = finalize_recorded_pageid_source_retention(cleaned)
    cleaned = annotate_pageid_recovery_strategies(cleaned)
    _attach_expected_response_signatures(cleaned)
    _attach_expected_request_signatures(cleaned)

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
        "effectdate", "effectdatebak", "loseeffectdate", "validuntil",
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
        soft_required_cfg = (
            _SOFT_REQUIRED_CONTEXT_FIELDS_BY_FORM
            .get(step_form_id, {})
            .get(str(field_key or "").lower())
        )
        if soft_required_cfg:
            env_sensitive = "high"
            label = str(soft_required_cfg.get("label") or label)
        label = _observed_field_label(field_observations, step_form_id, field_key) or label
        observed_parts = (
            (field_observations.get("response_values_by_form") or {})
            .get(step_form_id, {})
            .get(str(field_key or "").lower(), {})
        )
        value_code = str(
            (observed_parts or {}).get("value_code")
            or s.get("value_code", "")
            or ""
        )
        value_name = str(
            (observed_parts or {}).get("value_name")
            or s.get("value_name", "")
            or ""
        )
        value_number = str(
            (observed_parts or {}).get("value_number")
            or s.get("value_number", "")
            or ""
        )
        display_value_id = _display_pick_value_id(raw_value_id, value_code)
        auto_meta = _pick_field_auto_resolve_meta(
            field_key,
            display_value_id,
            value_name,
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
            ("value_name", value_name),
            ("value_code", value_code),
            ("value_number", value_number),
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
        if soft_required_cfg:
            pick_fields_map[step_id]["required_context"] = True
            pick_fields_map[step_id]["source"] = "runtime_rule"
            pick_fields_map[step_id]["reason"] = str(
                soft_required_cfg.get("reason") or "运行时规则要求维护该字段"
            )

    _main_app_id = next((s.get("app_id", "") for s in cleaned if s.get("form_id") == main_form), "")
    _append_context_default_pick_fields(
        pick_fields_map,
        field_observations,
        main_form=main_form,
        app_id=_main_app_id,
    )
    _append_missing_required_context_pick_fields(
        pick_fields_map,
        cleaned,
        main_form=main_form,
        app_id=_main_app_id,
        meta_resolver=meta_resolver,
    )
    _append_upload_file_pick_fields(pick_fields_map, cleaned)

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

    selector_contexts = _infer_entry_row_selector_contexts(cleaned)

    # --- 从 F7 选择器 entryRowClick 中提取用户选择对象（如计薪人员任职经历） ---
    for s in cleaned:
        selector = _extract_entry_row_selector(s, selector_contexts.get(str(s.get("id") or "")))
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
            ("parent_form_id", selector.get("parent_form_id", "")),
            ("parent_field_key", selector.get("parent_field_key", "")),
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
            enum_label = _scalar_enum_field_label(
                step_form_id,
                fk_lower,
                _PF_ENUM_FIELDS,
                meta_resolver,
            )
            if enum_label:
                fv = fields[fk]
                display_val = str(fv) if fv is not None else ""
                combo_options = (
                    (field_observations.get("combo_options_by_form") or {})
                    .get(step_form_id, {})
                    .get(fk_lower, {})
                )
                if step_form_id in _WORKFLOW_APPROVAL_FORM_IDS and fk_lower == _WORKFLOW_DECISION_FIELD_KEY:
                    combo_options = dict(_WORKFLOW_DECISION_OPTIONS)
                display_name = combo_options.get(display_val, display_val)
                base_step_id = f"pick_{fk_lower}_id"
                step_id = _scoped_pick_field_id(
                    base_step_id,
                    pick_fields_map,
                    form_id=step_form_id,
                    source_step_id=s.get("id", ""),
                )
                if not step_id:
                    _refresh_existing_pick_field(
                        pick_fields_map,
                        base_step_id,
                        form_id=step_form_id,
                        field_key=fk_lower,
                        value_id=display_val,
                        value_name=display_name,
                        value_code=display_val,
                        source_step_id=s.get("id", ""),
                        app_id=s.get("app_id", ""),
                    )
                    continue
                pick_fields_map[step_id] = OrderedDict([
                    ("value_id", display_val),
                    ("value_name", display_name),
                    ("value_code", display_val),
                    ("label", enum_label),
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
                if step_form_id in _WORKFLOW_APPROVAL_FORM_IDS and fk_lower == _WORKFLOW_DECISION_FIELD_KEY:
                    pick_fields_map[step_id]["options_text"] = _WORKFLOW_APPROVAL_OPTIONS_TEXT
                continue
            if _is_date_like_field_key(fk_lower):
                step_id = _scoped_pick_field_id(
                    f"date_{fk}",
                    pick_fields_map,
                    form_id=step_form_id,
                    source_step_id=s.get("id", ""),
                )
                if not step_id:
                    continue
                label = (
                    _observed_field_label(field_observations, step_form_id, fk)
                    or _resolve_field_label(fk, entity_id=step_form_id, meta_resolver=meta_resolver)
                )
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
    # 注：日期字段保留 HAR 录入值，并通过 date_* pick_fields 暴露给预览/变量面板维护。

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
                current_value_code = str(pick_fields_map[pf_id].get("value_code", "") or "")
                incoming_value_code = str(pf_cfg.get("value_code", "") or "")
                incoming_value_number = str(pf_cfg.get("value_number", "") or "")
                incoming_resolve_by = str(pf_cfg.get("resolve_by", "") or "")
                code_override = bool(
                    (pf_cfg.get("user_overridden") or pf_cfg.get("manual_override"))
                    and incoming_resolve_by == "value_code"
                    and (incoming_value_code or incoming_value_number)
                )
                incoming_value_id = str(pf_cfg.get("value_id", current_value_id))
                if code_override:
                    incoming_value_id = incoming_value_code or incoming_value_number
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
                    if (
                        (manual_override or code_override)
                        and incoming_value_name == current_value_name
                        and (
                            incoming_value_id != current_value_id
                            or incoming_value_code != current_value_code
                        )
                    ):
                        incoming_value_name = ""
                    pick_fields_map[pf_id]["value_name"] = incoming_value_name
                if code_override:
                    pick_fields_map[pf_id]["value_code"] = incoming_value_id
                    pick_fields_map[pf_id]["value_number"] = incoming_value_id
                    pick_fields_map[pf_id]["auto_resolve"] = True
                    pick_fields_map[pf_id]["resolve_by"] = "value_code"
                    pick_fields_map[pf_id]["resolve_status"] = "pending"
                elif "value_code" in pf_cfg:
                    pick_fields_map[pf_id]["value_code"] = incoming_value_code
                elif manual_override and incoming_value_id != current_value_id:
                    pick_fields_map[pf_id]["value_code"] = ""
                if "value_number" in pf_cfg and not code_override:
                    pick_fields_map[pf_id]["value_number"] = incoming_value_number
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

    _apply_user_pick_field_values_to_update_steps(cleaned, pick_fields_map)
    _apply_upload_pick_fields_to_steps(cleaned, pick_fields_map)

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
                  "expected_request_signature",
                  "expected_response_signature",
                  "continue_on_expected_error", "preserve_l2_page", "bind_l2_only",
                  "requires_harvested_l3_page",
                  "recorded_pageid_source_step_id",
                  "recorded_pageid_type",
                  "recorded_pageid_source_kind",
                  "recorded_pageid_source_form_match",
                  "recorded_pageid_source_retained",
                  "pageid_recovery_strategy", "force_pageid_validation",
                  "prefetch_lookup", "prefetch_lookup_args",
                  "skip_if_locked", "skip_if_locked_fields",
                  "skip_replay", "skip_reason",
                  "condition", "interval_seconds", "timeout_seconds",
                  "max_attempts", "source_step_count", "wait_source",
                  "dynamic_row_source_step_id", "dynamic_row_grid_key",
                  "dynamic_row_field_map", "dynamic_row_retry_until_found",
                  "dynamic_row_max_attempts", "dynamic_row_interval_seconds",
                  "requires_user_file", "upload_replay_strategy",
                  "recorded_file_names", "recorded_tempfile_reference",
                  "file_path", "upload_endpoint", "upload_url", "endpoint",
                  "file_field", "field_name", "extra_data", "data",
                  "headers", "upload_id", "ir_sources"):
            if k in s:
                entry[k] = s[k]
        if "ir_sources" not in entry:
            ir_sources = _collect_ir_sources([s])
            if ir_sources:
                entry["ir_sources"] = ir_sources
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

    vars_meta_all = _infer_vars_meta_from_steps(yaml_steps, main_form, meta_resolver=meta_resolver)
    built_vars_meta = OrderedDict(
        (name, vars_meta_all[name])
        for name in built_vars.keys()
        if name in vars_meta_all and not name.startswith("_")
    )
    if var_overrides:
        for vname, cfg in var_overrides.items():
            if (
                isinstance(cfg, Mapping)
                and cfg.get("user_overridden")
                and vname in built_vars_meta
                and isinstance(built_vars_meta[vname], dict)
            ):
                built_vars_meta[vname]["user_overridden"] = True

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
        ("ir_contract", _compact_ir_contract_for_yaml(
            har,
            source_name=har_path.name,
            yaml_steps=yaml_steps,
            vars_meta=built_vars_meta,
            pick_fields=pick_fields_map,
            ir_flow=ir_flow,
            navigation_policy=navigation_policy,
        )),
        ("steps", yaml_steps),
        ("assertions", _build_default_assertions(yaml_steps)),
    ])
    if include_readback_assertions:
        _append_readback_assertions(case)
    case["validation_points"] = _build_validation_points(
        case,
        validation_point_overrides=validation_point_overrides,
    )
    _apply_validation_points_to_assertions(case)
    attach_case_contract(case)
    try:
        from lib.yaml_schema import attach_yaml_schema_contract
        attach_yaml_schema_contract(case)
    except Exception:
        # Diagnostic-only linting should never block HAR import.
        pass

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
    if any(token in vname for token in ("salary", "income", "bonus", "allowance", "commission", "amount", "wage")):
        return "金额"
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
    try:
        from lib.ir import build_normalized_flow
        ir_flow = build_normalized_flow(har, source_name=har_path.name)
    except Exception as exc:
        log.warning("HAR IR 构建失败，预览主链路继续执行: %s", exc)
        ir_flow = {}
    field_observations = _collect_har_field_observations(har)
    raw_steps = extract_steps(har)
    raw_steps = dedup_open_forms(raw_steps)
    raw_steps = relocate_premature_open_forms(raw_steps)
    raw_steps = lower_set_item_to_pick_basedata(raw_steps)
    raw_steps = merge_consecutive_update_values(raw_steps)
    raw_steps = collapse_repeated_polling_steps(raw_steps)
    raw_steps = insert_workflow_task_wait_steps(raw_steps)
    raw_steps = _drop_locked_update_fields(raw_steps, field_observations)
    raw_steps = annotate_recorded_pageid_sources(raw_steps)

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
    try:
        from lib.ir import apply_ir_navigation_policy
        ir_navigation_policy = apply_ir_navigation_policy(
            ir_flow,
            preview_steps,
            main_form=main_form,
            decorative_form_ids=_NAVIGATION_FORM_IDS,
        )
    except Exception as exc:
        log.warning("预览 IR 导航策略应用失败，回退到旧规则: %s", exc)
        _mark_navigation_steps_optional(preview_steps, main_form)
        ir_navigation_policy = {
            "schema_version": 1,
            "stage": "stage_1_navigation_list",
            "mode": "fallback",
            "status": "diagnostic_failed",
            "error": type(exc).__name__,
        }
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
    preview_steps = _append_salary_scope_required_date_steps(preview_steps, field_observations)
    _mark_recorded_business_validations(preview_steps)
    _dedupe_step_ids(preview_steps)
    preview_steps = _ensure_workflow_approval_update_steps(preview_steps, field_observations)
    preview_steps = annotate_recorded_pageid_sources(preview_steps)
    preview_steps = finalize_recorded_pageid_source_retention(
        preview_steps,
        excluded_tiers={"noise"},
    )
    preview_steps = annotate_pageid_recovery_strategies(preview_steps)
    _attach_expected_response_signatures(preview_steps)
    _attach_expected_request_signatures(preview_steps)

    # ⭐ 变量预检测：提前运行变量检测逻辑，让用户在导入前可配置
    preview_copy = copy.deepcopy(preview_steps)
    _, detected_vars, detected_labels = detect_var_placeholders(preview_copy, meta_resolver=meta_resolver)
    detected_vars.pop("_date_replaced", None)

    # ⭐ step.description 反查业务名：与左侧"执行步骤"的「...」内文字严格对齐
    step_label_map = _infer_labels_from_preview_steps(preview_copy)
    vars_meta_map = _infer_vars_meta_from_steps(preview_copy, main_form, meta_resolver=meta_resolver)

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
        "effectdate", "effectdatebak", "loseeffectdate", "validuntil",
        "bsed", "bsled", "startdate", "enddate",
    )

    pick_fields: list[dict] = []
    _seen_pick_map: OrderedDict[str, dict] = OrderedDict()
    selector_contexts = _infer_entry_row_selector_contexts(preview_steps)

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
            elif _is_date_like_field_key(field_key):
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
            label = _observed_field_label(field_observations, step_form_id, field_key) or label
            observed_parts = (
                (field_observations.get("response_values_by_form") or {})
                .get(step_form_id, {})
                .get(str(field_key or "").lower(), {})
            )
            value_code = str(
                (observed_parts or {}).get("value_code")
                or s.get("value_code", "")
                or ""
            )
            value_name = str(
                (observed_parts or {}).get("value_name")
                or s.get("value_name", "")
                or ""
            )
            value_number = str(
                (observed_parts or {}).get("value_number")
                or s.get("value_number", "")
                or ""
            )
            display_value_id = _display_pick_value_id(value_id, value_code)
            auto_meta = _pick_field_auto_resolve_meta(
                field_key,
                display_value_id,
                value_name,
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
                "value_name": value_name,
                "value_code": value_code,
                "value_number": value_number,
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
            selector = _extract_entry_row_selector(s, selector_contexts.get(str(s.get("id") or "")))
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
                "parent_form_id": selector.get("parent_form_id", ""),
                "parent_field_key": selector.get("parent_field_key", ""),
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
                enum_label = _scalar_enum_field_label(
                    step_form_id,
                    fk_lower,
                    _ENUM_FIELDS,
                    meta_resolver,
                )
                if enum_label:
                    fv = fields[fk]
                    display_val = str(fv) if fv is not None else ""
                    combo_options = (
                        (field_observations.get("combo_options_by_form") or {})
                        .get(step_form_id, {})
                        .get(fk_lower, {})
                    )
                    if step_form_id in _WORKFLOW_APPROVAL_FORM_IDS and fk_lower == _WORKFLOW_DECISION_FIELD_KEY:
                        combo_options = dict(_WORKFLOW_DECISION_OPTIONS)
                    display_name = combo_options.get(display_val, display_val)
                    base_step_id = f"pick_{fk_lower}_id"
                    step_id = _scoped_pick_field_id(
                        base_step_id,
                        _seen_pick_map,
                        form_id=step_form_id,
                        source_step_id=s.get("id", ""),
                    )
                    if not step_id:
                        if _refresh_existing_pick_field(
                            _seen_pick_map,
                            base_step_id,
                            form_id=step_form_id,
                            field_key=fk_lower,
                            value_id=display_val,
                            value_name=display_name,
                            value_code=display_val,
                            source_step_id=s.get("id", ""),
                            app_id=s.get("app_id", ""),
                        ):
                            for item in pick_fields:
                                if item.get("id") == base_step_id:
                                    item.update(_seen_pick_map[base_step_id])
                                    break
                        continue
                    item = {
                        "id": step_id,
                        "field_key": fk_lower,
                        "label": enum_label,
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
                    if step_form_id in _WORKFLOW_APPROVAL_FORM_IDS and fk_lower == _WORKFLOW_DECISION_FIELD_KEY:
                        item["options_text"] = _WORKFLOW_APPROVAL_OPTIONS_TEXT
                        item["options"] = [
                            {"value_id": value, "value_code": value, "value_name": label}
                            for value, label in _WORKFLOW_DECISION_OPTIONS.items()
                        ]
                    pick_fields.append(item)
                    _seen_pick_map[step_id] = {k: v for k, v in item.items() if k != "id"}
                    continue
                if _is_date_like_field_key(fk_lower):
                    step_id = _scoped_pick_field_id(
                        f"date_{fk}",
                        _seen_pick_map,
                        form_id=step_form_id,
                        source_step_id=s.get("id", ""),
                    )
                    if not step_id:
                        continue

                    label = (
                        _observed_field_label(field_observations, step_form_id, fk)
                        or _resolve_field_label(fk, entity_id=step_form_id, meta_resolver=meta_resolver)
                    )
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
    _append_missing_required_context_pick_fields(
        _preview_pick_map,
        preview_steps,
        main_form=main_form,
        app_id=_preview_app_id,
        meta_resolver=meta_resolver,
    )
    _append_upload_file_pick_fields(_preview_pick_map, preview_steps)
    _attach_pick_field_scopes(_preview_pick_map, preview_steps, main_form)
    _annotate_env_field_sources(
        _preview_pick_map,
        field_observations,
        meta_resolver=meta_resolver,
    )
    pick_fields = [{"id": pf_id, **dict(meta)} for pf_id, meta in _preview_pick_map.items()]
    field_catalog = _build_unified_field_catalog(
        preview_copy,
        var_items,
        pick_fields,
        main_form=main_form,
        meta_resolver=meta_resolver,
    )

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
    recorded_pageid_flow = {
        "summary": {
            "exact_link_count": int((har_probe.get("summary") or {}).get("pageid_exact_link_count") or 0),
            "external_root_count": int((har_probe.get("summary") or {}).get("pageid_external_root_count") or 0),
            "reuse_after_close_count": int((har_probe.get("summary") or {}).get("pageid_reuse_after_close_count") or 0),
            "cross_form_count": int((har_probe.get("summary") or {}).get("pageid_cross_form_count") or 0),
            "filtered_source_count": sum(
                1
                for step in preview_steps
                if step.get("recorded_pageid_source_retained") is False
            ),
        },
        "links": list(((har_probe.get("links") or {}).get("pageid_flows") or [])[:80]),
        "risks": [
            item
            for item in (har_probe.get("risks") or [])
            if str(item.get("code") or "").startswith("pageid_")
        ][:80],
    }
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
        from lib.ir import (
            assess_ir_preview_alignment,
            build_ir_field_bridge,
            build_ir_yaml_bridge,
            compact_flow_for_preview,
        )
        ir_preview = compact_flow_for_preview(ir_flow)
        ir_alignment = assess_ir_preview_alignment(
            ir_flow,
            preview_steps=preview_copy,
            detected_vars=var_items,
            pick_fields=pick_fields,
        )
        ir_generation_bridge = build_ir_yaml_bridge(
            ir_flow,
            preview_steps,
            vars_meta=OrderedDict(
                (item.get("name"), OrderedDict((k, v) for k, v in item.items() if k != "name"))
                for item in var_items
                if item.get("name")
            ),
            pick_fields=OrderedDict(
                (item.get("id"), OrderedDict((k, v) for k, v in item.items() if k != "id"))
                for item in pick_fields
                if item.get("id")
            ),
        )
        ir_field_bridge = build_ir_field_bridge(
            ir_flow,
            preview_steps,
            vars_meta=OrderedDict(
                (item.get("name"), OrderedDict((k, v) for k, v in item.items() if k != "name"))
                for item in var_items
                if item.get("name")
            ),
            pick_fields=OrderedDict(
                (item.get("id"), OrderedDict((k, v) for k, v in item.items() if k != "id"))
                for item in pick_fields
                if item.get("id")
            ),
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
        ir_generation_bridge = {
            "schema_version": "0.1",
            "source": "normalized_flow_to_generated_yaml",
            "status": "diagnostic_failed",
            "mode": "shadow_contract",
            "coverage_score": 0,
            "summary": f"IR bridge 失败: {type(e).__name__}: {e}",
            "checks": {},
            "role_requirements": [],
            "uncovered_roles": [],
            "step_role_map": [],
            "migration_policy": {"current_generator": "har_extractor_main", "bridge_mode": "observe_and_validate"},
        }
        ir_field_bridge = {
            "schema_version": "1.0",
            "status": "diagnostic_failed",
            "raw_values_included": False,
            "coverage_score": 0,
            "summary": f"IR field bridge 失败: {type(e).__name__}: {e}",
            "checks": {},
            "ir_field_map": [],
            "maintainable_field_binding_plan": {
                "status": "diagnostic_failed",
                "fields": [],
                "summary": {},
            },
        }

    readback_plan = _build_preview_readback_plan(main_form, var_items)
    preview_validation_case = OrderedDict([
        ("vars_meta", OrderedDict(
            (item.get("name"), OrderedDict((k, v) for k, v in item.items() if k != "name"))
            for item in var_items
            if item.get("name")
        )),
        ("pick_fields", OrderedDict(
            (item.get("id"), OrderedDict((k, v) for k, v in item.items() if k != "id"))
            for item in pick_fields
            if item.get("id")
        )),
        ("steps", preview_steps),
        ("assertions", _build_default_assertions(preview_steps)),
    ])
    validation_points = _build_validation_points(preview_validation_case)
    preview_contract = build_case_contract(preview_validation_case)
    try:
        from lib.yaml_schema import validate_yaml_schema
        preview_schema_contract = validate_yaml_schema({
            "name": f"preview_{main_form or 'case'}",
            "schema_version": 1,
            "vars_meta": preview_validation_case.get("vars_meta"),
            "pick_fields": preview_validation_case.get("pick_fields"),
            "steps": preview_validation_case.get("steps"),
            "assertions": preview_validation_case.get("assertions"),
            "capability": preview_contract["capability"],
            "ai_assistance": preview_contract["ai_assistance"],
            "environment_binding_plan": preview_contract["environment_binding_plan"],
            "maintainable_field_binding_plan": preview_contract["maintainable_field_binding_plan"],
            "runtime_value_flow_plan": preview_contract["runtime_value_flow_plan"],
            "execution_contract": preview_contract["execution_contract"],
        })
    except Exception as e:
        log.warning("YAML schema contract 预览失败（非致命）: %s", e)
        preview_schema_contract = {
            "schema_version": "1.0",
            "status": "invalid",
            "ok": False,
            "error_codes": ["schema_contract_failed"],
            "warning_codes": [],
            "errors": [{"code": "schema_contract_failed", "path": "$", "message": str(e)}],
            "warnings": [],
            "summary": {},
        }

    preview = {
        "main_form_id": main_form,
        "tier_counts": by_tier,
        "detected_vars": var_items,
        "pick_fields": pick_fields,
        "field_catalog": field_catalog,
        "business_blocks": _build_preview_business_blocks(var_items, pick_fields),
        "business_flow": _build_preview_business_flow(preview_copy, var_items, pick_fields, main_form),
        "readback_plan": readback_plan,
        "validation_points": validation_points,
        "capability": preview_contract["capability"],
        "ai_assistance": preview_contract["ai_assistance"],
        "environment_binding_plan": preview_contract["environment_binding_plan"],
        "maintainable_field_binding_plan": preview_contract["maintainable_field_binding_plan"],
        "runtime_value_flow_plan": preview_contract["runtime_value_flow_plan"],
        "execution_contract": preview_contract["execution_contract"],
        "yaml_schema_contract": preview_schema_contract,
        "components": component_report,
        "pageid_alignment": pageid_alignment,
        "recorded_pageid_flow": recorded_pageid_flow,
        "ir_preview": ir_preview,
        "ir_alignment": ir_alignment,
        "ir_generation_bridge": ir_generation_bridge,
        "ir_field_bridge": ir_field_bridge,
        "ir_navigation_policy": ir_navigation_policy,
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
                "request_signature": {
                    "contract_level": (s.get("expected_request_signature") or {}).get("contract_level", ""),
                    "anchor_reason": (s.get("expected_request_signature") or {}).get("anchor_reason", ""),
                    "action_family": (s.get("expected_request_signature") or {}).get("action_family", ""),
                } if s.get("expected_request_signature") else {},
                "response_signature": summarize_response_signature(s.get("expected_response_signature")),
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
            ir_field_bridge=ir_field_bridge,
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
