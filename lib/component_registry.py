"""Kingdee Cloud HAR component registry.

This module is deliberately diagnostic-first: it classifies preview steps and
reports coverage, but does not rewrite steps. That keeps existing YAML
generation stable while giving new HAR imports a component radar.
"""
from __future__ import annotations

from collections import Counter
from dataclasses import asdict, dataclass
from typing import Any, Callable


Step = dict[str, Any]


@dataclass
class ComponentMatch:
    handler_id: str
    component: str
    category: str
    support_level: str
    risk: str
    reason: str
    suggestion: str = ""
    remediation: str = "none"
    remediation_reason: str = ""

    @property
    def supported(self) -> bool:
        return self.support_level == "supported"

    @property
    def partial(self) -> bool:
        return self.support_level == "partial"


@dataclass
class ComponentHandler:
    handler_id: str
    component: str
    category: str
    support_level: str
    risk: str
    reason: str
    match: Callable[[Step], bool]
    suggestion: str = ""

    def classify(self, step: Step) -> ComponentMatch | None:
        if not self.match(step):
            return None
        return ComponentMatch(
            handler_id=self.handler_id,
            component=self.component,
            category=self.category,
            support_level=self.support_level,
            risk=self.risk,
            reason=self.reason,
            suggestion=self.suggestion,
        )


def analyze_component_coverage(steps: list[Step]) -> dict[str, Any]:
    """Classify steps and return a component coverage report."""
    step_reports: list[dict[str, Any]] = []
    handler_counts: Counter[str] = Counter()
    component_counts: Counter[str] = Counter()
    unsupported: list[dict[str, Any]] = []
    remediation_counts: Counter[str] = Counter()

    for index, step in enumerate(steps or []):
        match = classify_step(step)
        handler_counts[match.handler_id] += 1
        component_counts[match.component] += 1
        remediation_counts[match.remediation] += 1
        report = {
            "index": index,
            "step_id": step.get("id", ""),
            "form_id": step.get("form_id", ""),
            "type": step.get("type", ""),
            "ac": step.get("ac", ""),
            "method": step.get("method", ""),
            **asdict(match),
            "supported": match.supported,
        }
        step_reports.append(report)
        if match.support_level == "unsupported":
            unsupported.append(report)

    total = len(step_reports)
    supported_count = sum(1 for s in step_reports if s["support_level"] == "supported")
    partial_count = sum(1 for s in step_reports if s["support_level"] == "partial")
    unsupported_count = sum(1 for s in step_reports if s["support_level"] == "unsupported")
    weighted = supported_count + partial_count * 0.5
    coverage = round((weighted / total * 100) if total else 100)

    return {
        "summary": {
            "total_steps": total,
            "supported_steps": supported_count,
            "partial_steps": partial_count,
            "unsupported_steps": unsupported_count,
            "coverage_percent": coverage,
            "risk_level": _risk_level(coverage, unsupported_count, partial_count),
        },
        "handlers": [
            {
                "handler_id": handler_id,
                "count": count,
                "component": _handler_name(handler_id),
            }
            for handler_id, count in handler_counts.most_common()
        ],
        "components": [
            {"component": component, "count": count}
            for component, count in component_counts.most_common()
        ],
        "unsupported": unsupported[:20],
        "gap_suggestions": _gap_suggestions(unsupported),
        "remediation_counts": dict(sorted(remediation_counts.items())),
        "steps": step_reports,
    }


def classify_step(step: Step) -> ComponentMatch:
    for handler in HANDLERS:
        match = handler.classify(step)
        if match:
            return match
    ac = str(step.get("ac") or "")
    method = str(step.get("method") or "")
    remediation, risk, suggestion = _unknown_remediation(step)
    return ComponentMatch(
        handler_id="unknown_action",
        component="未知组件/动作",
        category="unknown",
        support_level="unsupported",
        risk=risk,
        reason=f"未登记的动作 ac={ac or '-'} method={method or '-'}",
        suggestion=suggestion,
        remediation=remediation,
        remediation_reason=_remediation_reason(remediation),
    )


def _risk_level(coverage: int, unsupported_count: int, partial_count: int) -> str:
    if unsupported_count > 0 or coverage < 70:
        return "high"
    if partial_count > 0 or coverage < 90:
        return "medium"
    return "low"


def _gap_suggestions(unsupported: list[dict[str, Any]]) -> list[dict[str, Any]]:
    rows = []
    for item in unsupported[:20]:
        rows.append({
            "step_id": item.get("step_id", ""),
            "form_id": item.get("form_id", ""),
            "ac": item.get("ac", ""),
            "method": item.get("method", ""),
            "remediation": item.get("remediation", ""),
            "risk": item.get("risk", ""),
            "suggestion": item.get("suggestion", ""),
        })
    return rows


def _unknown_remediation(step: Step) -> tuple[str, str, str]:
    blob = _id_blob(step)
    ac = str(step.get("ac") or "").lower()
    method = _method(step).lower()
    key = _key(step).lower()
    if any(token in blob for token in ("save", "submit", "audit", "confirm", "btnok", "newentry")):
        return (
            "must_preserve",
            "high",
            "该未知动作像写入、确认或分录关键动作，不能 optional；优先新增专用 handler 或映射到保存/弹窗/分录处理器。",
        )
    if method in {"getitembyidfromclient", "setitembyidfromclient", "setitemvaluebyidfromclient"}:
        return (
            "reuse_existing_handler",
            "medium",
            "该动作接近基础资料选择器，可优先复用 pick_basedata/lookup 处理链。",
        )
    if ac in {"gethints", "gethint", "gethintscroll"} or any(token in blob for token in ("hint", "scroll", "tooltip")):
        return (
            "safe_noise_optional",
            "low",
            "该动作像提示/滚动类 UI 噪声；若不影响写库，可标记 optional 或保持低风险联动。",
        )
    if any(token in blob for token in ("entry", "grid", "row", "table")):
        return (
            "add_handler",
            "medium",
            "该动作像表格/分录组件，建议新增按行/列定位的组件处理器。",
        )
    if key or method:
        return (
            "add_handler",
            "medium",
            "该动作包含明确 key/method，若位于主链路应新增组件处理器；若失败点不在此处再降级 optional。",
        )
    return (
        "review_optional",
        "medium",
        "先确认是否位于主链路；若只是页面装饰或后台联动，可降级 optional，否则新增 handler。",
    )


def _remediation_reason(remediation: str) -> str:
    return {
        "must_preserve": "写入、确认、弹窗或分录关键动作，直接删除会造成假通过。",
        "reuse_existing_handler": "与现有基础资料、lookup 或弹窗处理器相似，应优先复用。",
        "safe_noise_optional": "更像 UI 噪声，通常可降级 optional。",
        "add_handler": "缺少专用组件处理器，失败时需要补 handler。",
        "review_optional": "信息不足，需要结合步骤位置人工判断。",
    }.get(remediation, "")


def _handler_name(handler_id: str) -> str:
    for handler in HANDLERS:
        if handler.handler_id == handler_id:
            return handler.component
    if handler_id == "unknown_action":
        return "未知组件/动作"
    return handler_id


def _is_invoke(step: Step, *acs: str) -> bool:
    return step.get("type") == "invoke" and str(step.get("ac") or "") in set(acs)


def _method(step: Step) -> str:
    return str(step.get("method") or "")


def _key(step: Step) -> str:
    return str(step.get("key") or "")


def _id_blob(step: Step) -> str:
    args = " ".join(str(x).lower() for x in (step.get("args") or []))
    return " ".join([
        str(step.get("id") or "").lower(),
        str(step.get("ac") or "").lower(),
        _method(step).lower(),
        _key(step).lower(),
        args,
    ])


def _is_persistence(step: Step) -> bool:
    ac = str(step.get("ac") or "").lower()
    if ac in {"save", "saveandeffect", "submit", "submitandeffect", "audit", "unaudit"}:
        return True
    blob = _id_blob(step)
    return any(x in blob for x in ("bar_save", "submit", "audit", "startupflow", "doconfirm"))


def _has_tree_focus(step: Step) -> bool:
    post_data = step.get("post_data")
    containers = post_data if isinstance(post_data, list) else [post_data]
    for item in containers:
        if isinstance(item, dict) and isinstance(item.get("treeview"), dict):
            focus = item["treeview"].get("focus")
            if isinstance(focus, dict) and (focus.get("id") or focus.get("text")):
                return True
    return False


def _is_entry_grid(step: Step) -> bool:
    blob = _id_blob(step)
    return (
        "entry" in blob
        or "entity" in _key(step).lower()
        or _method(step) in {
            "createGridColumns",
            "selectRows",
            "setFocus",
            "setSelectedDataInfo",
            "newEntry",
            "deleteEntry",
        }
    )


def _is_rule_group_filter_grid(step: Step) -> bool:
    if str(step.get("form_id") or "") != "hsas_payrollscene":
        return False
    field_keys: set[str] = set()
    fields = step.get("fields")
    if isinstance(fields, dict):
        field_keys.update(str(key).lower() for key in fields.keys())
    for key_name in ("field_key", "key"):
        if step.get(key_name):
            field_keys.add(str(step.get(key_name)).lower())
    blob = _id_blob(step)
    return (
        bool(field_keys & {"salarycalcstyle", "attachcondition", "calbordermulbd", "callistrule"})
        or "entryentity" in blob
        or "rulegroupsap" in blob
        or "addgroup" in blob
    )


HANDLERS: tuple[ComponentHandler, ...] = (
    ComponentHandler(
        "open_form",
        "表单打开",
        "workflow",
        "supported",
        "low",
        "open_form 已由 runner 统一管理 pageId。",
        lambda s: s.get("type") == "open_form",
    ),
    ComponentHandler(
        "form_load",
        "表单加载",
        "workflow",
        "supported",
        "low",
        "loadData 是表单初始化和 pageId 绑定核心动作。",
        lambda s: _is_invoke(s, "loadData"),
    ),
    ComponentHandler(
        "rule_group_filter_grid",
        "规则分组/常用筛选",
        "field",
        "partial",
        "high",
        "薪资核算场景依赖规则分组下的 entryentity 常用筛选行；缺行会导致保存被业务校验拦截。",
        _is_rule_group_filter_grid,
        "已验证前置 country 后点击 labelap4 会打开 hsas_salarycalcstyle F7；使用 select_f7_list_row 选中算发薪方式并点 btnok 后，会回填 groupcontent/entryentity。不要硬补 save.post_data。",
    ),
    ComponentHandler(
        "f7_list_selector",
        "F7 列表选择器",
        "field",
        "supported",
        "low",
        "F7 loadData -> entryRowClick -> btnok 已可用 select_f7_list_row 表达，确认响应应回填父表单上下文。",
        lambda s: s.get("type") == "select_f7_list_row",
        "用于子弹窗基础资料列表选择；优先按业务编码/名称匹配行，保留 pageId 链路，不要硬补最终保存包体。",
    ),
    ComponentHandler(
        "field_update",
        "字段更新",
        "field",
        "supported",
        "low",
        "update_fields/updateValue 已规范化为字段批量写入。",
        lambda s: s.get("type") == "update_fields" or _method(s) == "updateValue" or _is_invoke(s, "updateValue"),
    ),
    ComponentHandler(
        "basedata_selector",
        "基础资料选择器",
        "field",
        "supported",
        "low",
        "基础资料选择已接入 pick_fields 与环境字段自动解析。",
        lambda s: s.get("type") == "pick_basedata" or _method(s) == "setItemByIdFromClient",
    ),
    ComponentHandler(
        "geo_lookup",
        "国家/省市/电话区号级联",
        "field",
        "supported",
        "low",
        "地理级联查询通常不写库，可按响应上下文回放。",
        lambda s: _is_invoke(s, "getCityInfo", "getCountrys", "getProvincesByCountryId", "getTelViaList"),
    ),
    ComponentHandler(
        "lookup_query",
        "基础资料查询",
        "field",
        "supported",
        "low",
        "getLookUpList/commonSearch 已用于候选搜索和上下文补偿。",
        lambda s: _is_invoke(s, "getLookUpList", "commonSearch"),
    ),
    ComponentHandler(
        "tree_context",
        "树上下文",
        "navigation",
        "supported",
        "low",
        "treeview.focus 已暴露为 high 级环境字段。",
        lambda s: _has_tree_focus(s),
    ),
    ComponentHandler(
        "tree_navigation",
        "树导航",
        "navigation",
        "supported",
        "low",
        "树展开/点击用于定位上下文，必要时保留为桥接步骤。",
        lambda s: _is_invoke(s, "treeMenuClick", "postExpandNodes", "queryTreeNodeChildren"),
    ),
    ComponentHandler(
        "list_navigation",
        "列表导航",
        "navigation",
        "supported",
        "low",
        "列表刷新/行点击/超链接可作为进入卡片的桥接链路。",
        lambda s: _is_invoke(s, "refresh", "entryRowClick", "hyperLinkClick", "queryExceedMaxCount"),
    ),
    ComponentHandler(
        "portal_navigation",
        "门户/应用导航",
        "navigation",
        "partial",
        "medium",
        "门户导航依赖首页服务，跨环境失败时应降级 optional。",
        lambda s: _is_invoke(s, "menuItemClick", "appItemClick", "selectTab", "getMenuData", "getFrequentData"),
        "如果失败且不是主业务表单，可标记 optional 或改用目标表单直开。",
    ),
    ComponentHandler(
        "persistence_action",
        "保存/提交/审核",
        "workflow",
        "supported",
        "low",
        "写库动作是主链路锚点，不能随意 optional。",
        _is_persistence,
    ),
    ComponentHandler(
        "create_record",
        "新增/复制/修改态",
        "workflow",
        "supported",
        "low",
        "新增/修改动作会切换 pageId，runner 已处理同表单新态 pageId。",
        lambda s: _is_invoke(s, "addnew", "new", "modify"),
    ),
    ComponentHandler(
        "model_structure_action",
        "业务模型结构操作",
        "workflow",
        "supported",
        "low",
        "业务模型新增子实体等结构动作已可按协议回放。",
        lambda s: _is_invoke(s, "addsonlogicentity"),
    ),
    ComponentHandler(
        "business_entry_bridge",
        "业务入口桥接",
        "workflow",
        "supported",
        "low",
        "donothing_* 常用于打开业务录入链路前的服务端模型桥接，不应被当作未知高风险。",
        lambda s: s.get("type") == "invoke" and str(s.get("ac") or "").startswith("donothing_"),
        "若该桥接失败，优先检查 pageId 链路和上游模板/组织上下文。",
    ),
    ComponentHandler(
        "hint_scroll",
        "提示/帮助读取",
        "compatibility",
        "partial",
        "low",
        "getHintScroll 是提示信息读取类 UI 动作，通常不影响主写库链路。",
        lambda s: _is_invoke(s, "getHintScroll"),
        "若失败且不影响写库，可保留为 optional 或作为低风险 UI 联动处理。",
    ),
    ComponentHandler(
        "dialog_confirm",
        "弹窗/确认回调",
        "workflow",
        "partial",
        "medium",
        "弹窗确认可回放，但不同业务提示可能需要按按钮语义补规则。",
        lambda s: _is_invoke(s, "afterConfirm", "doConfirm", "clientCallBack") or "confirm" in _id_blob(s),
        "若失败点在弹窗确认，需检查按钮值、目标表单和响应中的 operation result。",
    ),
    ComponentHandler(
        "entry_table",
        "分录/表格组件",
        "field",
        "partial",
        "medium",
        "分录表格已能回放常见选择/新增行，复杂行定位仍需专用 handler。",
        _is_entry_grid,
        "复杂单据体建议补充按列名和行条件定位的组件处理器。",
    ),
    ComponentHandler(
        "background_task",
        "后台任务/侧边栏",
        "navigation",
        "partial",
        "medium",
        "后台任务一般不是主写库链路，环境差异大。",
        lambda s: "bgtask" in str(s.get("form_id") or "").lower(),
        "若不影响主流程，可标记 optional 或从生成结果中剔除。",
    ),
    ComponentHandler(
        "user_setting_action",
        "用户偏好/首页设置",
        "navigation",
        "partial",
        "low",
        "saveSetting 等用户偏好动作通常不影响主业务写库。",
        lambda s: _is_invoke(s, "saveSetting"),
        "如果目标环境首页服务不可用，可标记 optional 或剔除。",
    ),
    ComponentHandler(
        "generic_safe_invoke",
        "通用低风险动作",
        "compatibility",
        "partial",
        "low",
        "已知低风险动作暂由通用 invoke 回放。",
        lambda s: _is_invoke(s, "click", "customEvent", "clientPosInvokeMethod", "changeYear", "release", "delete", "close", "itemClick"),
        "如果该动作成为失败点，再提升为专用组件处理器。",
    ),
)
