"""执行失败自动归因。

把 run_case 中分散的 step_fail/assertion_fail 转成稳定分类，便于前端展示、
批量报告聚合，以及后续自动修复。
"""
from __future__ import annotations

import re
from typing import Any


_TRANSIENT_PATTERNS = (
    "HTTP 502",
    "HTTP 503",
    "HTTP 504",
    "Bad Gateway",
    "Gateway Timeout",
    "Read timed out",
    "Connection aborted",
    "请求超时",
)

_PAGE_ID_PATTERNS = (
    "页面未初始化或者已经过期",
    "获取缓存连接客户端失败",
    "pageId",
    "pageid",
    "表单会话超时",
)

_OPEN_FORM_CONTEXT_PATTERNS = (
    "got list without pageId",
    "without pageId",
    "open_form(",
)

_TEMPLATE_CONTEXT_PATTERNS = (
    "业务数据模板数据缺失",
    "重新选择模板",
    "业务数据模板",
)

_SERVICE_PATTERNS = (
    "未发现AppIdName",
    "服务或访问服务网络异常",
    "FormService",
    "错误码:1002",
)

_DATABASE_SCHEMA_PATTERNS = (
    "column \"",
    "does not exist",
    "relation \"",
    "数据库表",
    "数据库字段",
)

_SERVER_STACK_PATTERNS = (
    "TraceId",
    "调用堆栈",
    "java.lang",
    "NullPointerException",
    "RequestContext:",
)

_MISSING_PATTERNS = (
    "请填写",
    "请选择",
    "不能为空",
    "必填",
)

_RULE_GROUP_FILTER_PATTERNS = (
    "规则分组",
    "常用筛选",
)

_ENV_CONTEXT_FIELD_PATTERNS = (
    "创建组织",
    "createorg",
    "控制策略",
    "ctrlstrategy",
    "默认组织",
    "所属组织",
    "组织上下文",
)

_F7_LOOKUP_PATTERNS = (
    "getLookUpList",
    "F7",
    "entryRowClick",
    "select_f7",
    "setItemByIdFromClient",
    "候选",
    "基础资料选择",
    "选人",
    "选择人员",
    "lookup",
)

_DIALOG_DETAIL_PATTERNS = (
    "newentry",
    "明细",
    "子窗口",
    "子窗",
    "弹窗",
    "btnok",
    "entryentity",
    "groupcontent",
    "分录",
)

_READBACK_ASSERTION_PATTERNS = (
    "入库回查未找到",
    "readback_by_business_key",
    "只读 commonSearch",
    "响应未包含 grid 行或业务键文本",
)

_WORKFLOW_TASK_WAIT_PATTERNS = (
    "grid_row_exists",
    "runtime_billno_wait_timeout",
    "wait_runtime_billno",
    "待办",
    "wf_task",
)

_DUPLICATE_PATTERNS = (
    "已存在",
    "重复",
    "唯一",
)

_FORMAT_PATTERNS = (
    "格式不正确",
    "格式错误",
    "不合法",
    "不允许",
    "超出",
)

_LOCKED_FIELD_PATTERNS = (
    "无法修改锁定字段",
    "锁定字段",
)

_INVALID_REQUEST_PATTERNS = (
    "无效请求",
    "非法请求",
    "invalid request",
    "csrf",
    "signature",
    "未登录",
    "登录超时",
)

_RECORDED_TEMP_ATTACHMENT_PATTERNS = (
    "临时附件已超时",
    "重新上传以下文件",
    "附件上传中",
    "tempfile.mock",
    "tempfile/download.do",
)


def classify_run_failure(
    steps: list[dict],
    assertions: list[dict],
    case: dict | None = None,
) -> dict[str, Any]:
    """归因一次执行失败。"""
    case = case or {}
    case_steps = {
        str(item.get("id") or ""): item
        for item in (case.get("steps") or [])
        if isinstance(item, dict) and item.get("id")
    }
    failures = []
    for step in steps or []:
        if step.get("ok"):
            continue
        if step.get("optional"):
            continue
        case_step = case_steps.get(str(step.get("id") or ""), {})
        raw_step = {**case_step, **step}
        failures.append({
            "source": "step",
            "step_id": step.get("id", ""),
            "step_type": step.get("type", ""),
            "form_id": step.get("form_id") or case_step.get("form_id", ""),
            "error": step.get("error") or "; ".join(step.get("_errors") or []),
            "raw": raw_step,
        })

    for assertion in assertions or []:
        if assertion.get("ok"):
            continue
        failures.append({
            "source": "assertion",
            "step_id": assertion.get("step", ""),
            "step_type": "assertion",
            "form_id": "",
            "error": assertion.get("msg", ""),
            "raw": assertion,
        })

    if not failures:
        return {
            "category": "unknown",
            "severity": "low",
            "retryable": False,
            "confidence": "low",
            "root_cause": "未捕获到明确的失败步骤。",
            "evidence": "",
            "recommended_actions": ["查看完整 run_history，确认是否为断言外异常或日志截断。"],
            "failures": [],
        }

    primary = classify_error(
        failures[0].get("error", ""),
        step=failures[0].get("raw") or {},
        case=case,
    )
    category_counts: dict[str, int] = {}
    classified_failures = []
    for failure in failures:
        item = classify_error(
            failure.get("error", ""),
            step=failure.get("raw") or {},
            case=case,
        )
        category_counts[item["category"]] = category_counts.get(item["category"], 0) + 1
        classified_failures.append({**failure, "analysis": item})

    return {
        **primary,
        "category_counts": category_counts,
        "failures": classified_failures,
    }


def classify_error(error: str, step: dict | None = None, case: dict | None = None) -> dict[str, Any]:
    """归因单条错误文本。"""
    step = step or {}
    case = case or {}
    text = str(error or "")
    form_id = str(step.get("form_id") or "")
    step_id = str(step.get("id") or "")
    main_form = str(case.get("main_form_id") or "")

    if _matches_expected_notification(text, step):
        return _result(
            "business_validation_expected",
            "low",
            False,
            "该业务校验提示已存在于录制 HAR 中，应作为验证点继续执行，而不是失败。",
            text,
            [
                "确认 YAML 步骤包含 expected_notifications/continue_on_expected_error。",
                "确认 assertions 中有 expected_notification，且后续补录字段或确认动作仍完整回放。",
                "不要删除该校验步骤；它是录制流程的一部分。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _LOCKED_FIELD_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "locked_field_update",
            "high",
            False,
            f"回放尝试修改服务端已锁定字段{f'：{field}' if field else ''}，通常是 HAR 把默认带出/只读字段误生成为 update_fields。",
            text,
            [
                "检查生成 YAML 中对应 update_fields，锁定字段应由服务端默认带出，不应主动回放。",
                "重新导入 HAR 以应用锁定字段过滤规则；不要删除保存断言绕过问题。",
                "若该字段确实需要人工维护，先确认浏览器端是否可编辑，再补充专门的组件处理器。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="high",
        )

    if _contains(text, _INVALID_REQUEST_PATTERNS):
        return _result(
            "invalid_protocol_request",
            "high",
            False,
            "目标环境拒绝了协议请求，通常是会话/CSRF/签名、账号权限或跨环境菜单入口不匹配。",
            text,
            [
                "先运行跨环境 preflight，确认 login、root page、portal、menuItemClick、main loadData 哪一段失败。",
                "检查当前环境登录是否拿到 kd-csrf-token；UAT 可能比 SIT 更严格。",
                "核对 menuId/appId/cloudId 是否来自当前环境；不要直接复用 SIT HAR 的导航字面值。",
                "确认当前账号在目标环境有菜单和主表单访问权限。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _RECORDED_TEMP_ATTACHMENT_PATTERNS):
        return _result(
            "recorded_temp_attachment_stale",
            "medium",
            False,
            "HAR 录制期的临时附件句柄已过期，回放时不能复用该 upload 结果。",
            text,
            [
                "重新导入 HAR，使 upload/tempfile.mock 步骤标记为 skip_replay。",
                "若业务强制要求附件，需要提供真实本地文件并实现运行期重新上传。",
                "不要复用 HAR 中的 tempfile/download.do 临时 URL，也不要硬补保存请求体。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if "真实附件上传缺少" in text or "upload_file.extra_data/data" in text:
        return _result(
            "upload_file_configuration_missing",
            "medium",
            False,
            "真实附件上传配置不完整，运行时无法从用户本地文件重新生成目标环境附件句柄。",
            text,
            [
                "在 YAML 或变量面板为附件步骤补充 file_path，指向本机真实文件。",
                "从 HAR 的真实上传请求或目标环境接口补充 upload_endpoint/upload_url；不要使用 tempfile/download.do 临时 URL。",
                "确认上传成功响应中的 id/url 会被后续附件字段或确认步骤消费，必要时查看 dynamic_value_flow.upload_url。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _TRANSIENT_PATTERNS):
        return _result(
            "transient_protocol",
            "medium",
            True,
            "协议层瞬态错误，通常与网关、服务短暂抖动或网络超时有关。",
            text,
            [
                "优先重试该用例；runner 已对 HTTP 502/503/504 做自动重试。",
                "如果同一步连续失败，再检查目标表单服务和网络链路。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _SERVICE_PATTERNS):
        if _is_navigation_form(form_id, main_form):
            return _result(
                "navigation_service_unavailable",
                "medium",
                False,
                "非主业务导航表单服务不可达。",
                text,
                [
                    "该类 apphome/侧栏/快捷卡片步骤应保持 optional，不应阻断主表单写库。",
                    "如果失败步骤不是 optional，重新导入 HAR 以应用导航降级规则。",
                ],
                step_id=step_id,
                form_id=form_id,
                confidence="high",
            )
        return _result(
            "environment_service_unavailable",
            "high",
            False,
            "主业务或必需应用服务不可达。",
            text,
            [
                "确认当前环境已启用对应 AppIdName 服务，并且账号有访问权限。",
                "如果这是菜单入口后的目标表单，检查 target_form/target_forms 是否正确生成。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _DATABASE_SCHEMA_PATTERNS):
        return _result(
            "environment_schema_mismatch",
            "high",
            False,
            "目标环境数据库结构或后端版本与当前业务服务不匹配，属于环境侧失败，不应通过修改 HAR/YAML 绕过。",
            text,
            [
                "确认当前 SIT/UAT 环境是否完成对应应用的数据库脚本和元数据升级。",
                "保留失败步骤和 TraceId 给环境/后端排查；不要删除 startupflow/save 断言来掩盖问题。",
                "若同一 YAML 在已验证环境可通过，则优先归为环境差异，而不是 pageId 链路问题。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _SERVER_STACK_PATTERNS):
        return _result(
            "environment_server_exception",
            "high",
            False,
            "目标环境后端服务抛出运行时异常，通常需要结合 TraceId 查看服务日志。",
            text,
            [
                "先按 TraceId/发生时间查询目标环境服务日志，确认是否为环境数据、脚本或服务版本问题。",
                "同时检查证据包 pageid_trace；若 pageId 链路无风险，不要优先硬补 save 或流程字段。",
                "如果错误稳定复现且同环境手工操作也失败，应交给环境/后端处理。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="medium",
        )

    if _contains(text, _TEMPLATE_CONTEXT_PATTERNS):
        return _result(
            "business_template_context_missing",
            "high",
            False,
            "业务数据模板上下文缺失，通常是模板选择、选人、子弹窗前置链路没有完整回放。",
            text,
            [
                "先比对 HAR 原始 pageId 与回放 pageId，确认菜单/列表/新增保持 L2，子窗和编辑态切到 L3。",
                "检查模板选择、选人 F7、entryRowClick、btnok、子弹窗字段维护和确定动作是否都保留。",
                "将模板、人员、业务归属日期等可变上下文字段暴露为环境字段或智能变量。",
                "不要只因主单保存成功就裁剪子弹窗链路，也不要硬补 save.post_data。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _OPEN_FORM_CONTEXT_PATTERNS):
        return _result(
            "open_form_context_blocked",
            "high",
            False,
            "open_form 没有拿到新 pageId，而是收到业务提示列表；根因通常在前置选择、模板或子窗上下文。",
            text,
            [
                "先看 pageid_trace：open_form 前的列表/工具栏步骤是否仍在 L2，showForm 响应是否产生 L3。",
                "再检查 HAR 中 open_form 前是否有 getLookUpList、entryRowClick、btnok 或模板选择动作被解析遗漏。",
                "如果业务提示来自录制中的预期校验，应标为 expected_notification 并继续后续步骤。",
                "不要把该问题简单归为网络异常，也不要硬补保存字段。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _PAGE_ID_PATTERNS):
        return _result(
            "pageid_context",
            "high",
            True,
            "PageId 或表单上下文失效。",
            text,
            [
                "检查 menuItemClick 是否带 target_form/target_forms。",
                "优先比对 HAR 原始 pageId 与回放 pageId：列表/树/工具栏步骤应保留 L2，真实编辑/保存步骤才切到 L3。",
                "检查 HAR 导入是否为列表/树到主表单的上下文桥接步骤生成 preserve_l2_page。",
                "若是保存后继续操作同一页面，确认 keep_page 或重新 open/load 策略。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if "无根组织" in text or "根组织初始化" in text:
        return _result(
            "environment_business_prerequisite",
            "high",
            False,
            "当前环境缺少组织管理根行政组织初始化，新增行政组织被业务规则拦截。",
            text,
            [
                "先在目标环境执行组织管理根行政组织生成调度计划，或选择已完成根组织初始化的数据中心。",
                "不要通过删除 addnew/menuItemClick 或保存断言绕过该问题；根组织未初始化时后续字段会级联缺失。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_env_context_field_missing(text):
        field = _extract_field_caption(text)
        return _result(
            "environment_field_context_missing",
            "high",
            False,
            f"环境上下文字段缺失或未回填{f'：{field}' if field else ''}，常见于创建组织、控制策略、默认组织等服务端模型字段。",
            text,
            [
                "先确认 pageId 链路没有异常，再从 HAR 的 loadData、showForm 元数据、列表 dataindex/rows 提取默认上下文。",
                "将 createorg/ctrlstrategy/默认组织等字段解析为可维护环境字段；显示编码/中文，执行时用内部 id。",
                "这些字段应进入 update_fields 或模型上下文，不要直接硬补 save.post_data。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="high",
        )

    if _is_rule_group_filter_missing(text):
        return _result(
            "component_rule_group_filter_missing",
            "high",
            False,
            "规则分组/常用筛选组件未回放完整，保存时 entryentity 仍为空；这通常不是 pageId 错误，也不能靠硬补 save 字段解决。",
            text,
            [
                "先确认 pageid_trace：菜单/列表/新增工具栏步骤保留 L2，编辑态字段维护和保存切到 L3。",
                "补录或补齐规则分组常用筛选完整链路：先维护 country，再点击 labelap4 打开 hsas_salarycalcstyle F7，使用 select_f7_list_row 按编码/名称选中算发薪方式并点 btnok，让确认响应回填 groupcontent/entryentity。",
                "继续维护 attachcondition，必要时维护 calbordermulbd；这些行级字段应进入环境字段或智能变量。",
                "将常用筛选行里的基础资料、枚举和日期字段暴露为环境字段或智能变量，避免写死长整数内码。",
                "不要删除 no_save_failure，不要硬补 save.post_data，也不要仅选择 callistrule 来替代常用筛选行。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_f7_lookup_chain_missing(text):
        return _result(
            "f7_lookup_chain_missing",
            "high",
            False,
            "F7/基础资料/选人候选链路不完整，导致后续字段或子窗无法回填。",
            text,
            [
                "检查 HAR 中 getLookUpList、entryRowClick/select_f7_list_row、btnok/setItemByIdFromClient 是否被解析并按顺序回放。",
                "候选值应作为环境字段可维护；预览展示编码/名称，执行时解析内部 id。",
                "如果选择结果回填到明细或子窗，必须确认后续 update_fields/save 使用同一个 L3 上下文。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_dialog_detail_chain_missing(text):
        return _result(
            "dialog_detail_chain_incomplete",
            "high",
            False,
            "子弹窗或明细分录链路不完整，主保存可能只入库了一部分数据。",
            text,
            [
                "逐步核对 newentry/addrow → F7/entryRowClick → 子窗字段维护 → btnok/确定 → 主保存。",
                "确认确认响应或主保存响应包含明细字段回填；PASS 但明细缺失时应归为断言盲区或入库验证缺口。",
                "将子窗字段按表单/步骤作用域暴露为智能变量或环境字段，避免用户无法维护。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_readback_assertion_gap(text):
        return _result(
            "readback_assertion_gap",
            "medium",
            False,
            "保存/提交已执行，但后置入库回查断言未找到业务键；常见原因是通用 commonSearch 不适配该表单，而不是保存链路失败。",
            text,
            [
                "先确认保存步骤的 no_save_failure/no_error_actions 是否通过，保存响应是否包含保存成功、主键或回写字段。",
                "若环境中确认已入库，应删除或降级该通用 readback_by_business_key 硬断言，改为表单专用回查策略或人工确认。",
                "后续 HAR 生成只应对已有专用策略的表单自动追加硬回查断言；通用 commonSearch 只能作为建议。",
                "不要修改 save.post_data，也不要删除保存失败断言来绕过问题。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_workflow_task_wait_timeout(text, form_id):
        return _result(
            "workflow_task_wait_timeout",
            "high",
            True,
            "提交后未等到运行时单号对应的审批待办行，通常是目标环境流程未生成待办、账号无待办权限，或消息中心/流程配置异常。",
            text,
            [
                "先在目标环境用运行时单号确认是否生成审批待办，以及当前账号是否有处理权限。",
                "检查提交响应是否成功返回新 billno；后续 wf_task/commonSearch 必须按运行时 billno 查询。",
                "若业务环境里待办稍后才出现，可适当延长 wait_until(grid_row_exists) 超时；不要点击 HAR 录制旧任务行。",
                "若待办一直不生成，优先排查流程配置/审批人/消息中心服务，而不是硬补保存或审核请求体。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _is_async_business_sync_timeout(text, form_id):
        return _result(
            "environment_async_business_sync_timeout",
            "high",
            True,
            "前置业务操作已经成功，但目标环境未在等待时间内生成后续业务记录，属于异步同步/投影链路异常。",
            text,
            [
                "先按本次运行时姓名或编号在目标环境查询后续业务记录，确认不是列表状态过滤造成的遗漏。",
                "核对前置响应是否包含“操作成功/同步中”，并确认回放查询已使用本次运行值而不是 HAR 录制旧值。",
                "若跨状态、按姓名和编号均查不到，排查目标环境异步任务、消息队列和业务投影服务；环境恢复后重跑。",
                "不要删除后续列表点击或入库回查断言，也不要改用 HAR 旧行来伪造通过。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _MISSING_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_missing_required",
            "high",
            False,
            f"业务必填字段缺失{f'：{field}' if field else ''}。",
            text,
            [
                "检查该字段是否被 HAR 解析成 update_fields 或 pick_basedata。",
                "若录制时该字段由树节点/默认值/联动带出，先检查 pageId 链路是否与 HAR 一致，不要直接硬补 save 字段。",
                "若字段由浏览器上下文带出，需补充上下文字段规则或 pick_fields 环境配置。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="medium",
        )

    if _contains(text, _DUPLICATE_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_duplicate",
            "medium",
            False,
            f"唯一字段重复{f'：{field}' if field else ''}。",
            text,
            [
                "检查编号、名称、手机号、邮箱等字段是否已抽成 ${vars.*}。",
                "为重复字段模板加入 ${rand:N} 或 ${timestamp}。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="high",
        )

    if _contains(text, _FORMAT_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_invalid_value",
            "medium",
            False,
            f"字段值不符合业务约束{f'：{field}' if field else ''}。",
            text,
            [
                "检查 vars 模板、pick_fields 环境值和字段格式限制。",
                "如果错误提示包含不允许字符，优先调整随机前缀或固定模板。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="medium",
        )

    if "找不到步骤" in text:
        return _result(
            "assertion_anchor_missing",
            "medium",
            False,
            "断言挂靠的保存/提交步骤未执行到。",
            text,
            [
                "先查看更早的 step_fail，它通常才是真正根因。",
                "如果 YAML 被手工改名，更新 assertions 中的 step。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="medium",
        )

    return _result(
        "unknown",
        "low",
        False,
        "暂未匹配到已知失败模式。",
        text,
        ["保留 run_history 和失败响应，按错误文本补充 failure_analysis 规则。"],
        step_id=step_id,
        form_id=form_id,
        confidence="low",
    )


def _result(
    category: str,
    severity: str,
    retryable: bool,
    root_cause: str,
    evidence: str,
    recommended_actions: list[str],
    *,
    step_id: str = "",
    form_id: str = "",
    field_caption: str = "",
    confidence: str = "medium",
) -> dict[str, Any]:
    return {
        "category": category,
        "severity": severity,
        "retryable": retryable,
        "confidence": confidence,
        "root_cause": root_cause,
        "evidence": evidence[:800],
        "recommended_actions": recommended_actions,
        "diagnosis_priority": _diagnosis_priority_for(category),
        "step_id": step_id,
        "form_id": form_id,
        "field_caption": field_caption,
    }


def _contains(text: str, patterns: tuple[str, ...]) -> bool:
    return any(pattern in text for pattern in patterns)


def _is_rule_group_filter_missing(text: str) -> bool:
    if not _contains(text, _RULE_GROUP_FILTER_PATTERNS):
        return False
    return any(token in text for token in ("不允许为空", "至少填写一行", "不能为空", "请至少填写"))


def _is_env_context_field_missing(text: str) -> bool:
    if not _contains(text, _ENV_CONTEXT_FIELD_PATTERNS):
        return False
    return _contains(text, _MISSING_PATTERNS) or any(token in text for token in ("缺失", "未回填", "不存在", "无效"))


def _is_f7_lookup_chain_missing(text: str) -> bool:
    if not _contains(text, _F7_LOOKUP_PATTERNS):
        return False
    return any(token in text for token in ("缺失", "找不到", "未选中", "未回填", "为空", "不能为空", "请选择", "失败", "无候选"))


def _is_dialog_detail_chain_missing(text: str) -> bool:
    if not _contains(text, _DIALOG_DETAIL_PATTERNS):
        return False
    return any(token in text for token in ("缺失", "未回填", "为空", "不能为空", "请至少", "保存成功但", "只入库"))


def _is_readback_assertion_gap(text: str) -> bool:
    return _contains(text, _READBACK_ASSERTION_PATTERNS)


def _is_workflow_task_wait_timeout(text: str, form_id: str = "") -> bool:
    if "wait_until" not in text and "runtime_billno_wait_timeout" not in text:
        return False
    if "grid_row_exists" not in text and "待办" not in text:
        return False
    if form_id == "wf_task" or "wf_task" in text or "billno" in text:
        return True
    return _contains(text, _WORKFLOW_TASK_WAIT_PATTERNS)


def _is_async_business_sync_timeout(text: str, form_id: str = "") -> bool:
    if "dynamic list row not found after" not in text:
        return False
    if form_id == "wf_task" or "wf_task" in text or "billno" in text:
        return False
    return form_id in {"hspm_assignmentlist"} or "assignmentlist" in text


def _matches_expected_notification(text: str, step: dict) -> bool:
    specs = step.get("expected_notifications") or step.get("expected_errors") or []
    for spec in specs:
        if isinstance(spec, str) and spec and spec in text:
            return True
        if isinstance(spec, dict):
            needle = str(spec.get("content") or spec.get("contains") or spec.get("msg") or "")
            if needle and needle in text:
                return True
    return False


def _is_navigation_form(form_id: str, main_form: str) -> bool:
    if not form_id or form_id == main_form:
        return False
    return (
        form_id.endswith("_apphome")
        or form_id.startswith("bos_card_")
        or form_id.startswith("gbs_bgtask")
        or form_id in {"hom_wbcalendar", "hom_wbwaitin", "hom_wbwarning"}
    )


def _extract_field_caption(text: str) -> str:
    patterns = (
        r'请(?:填写|输入|录入|选择)\s*[""“”「」]?([^""“”「」，。；\s]+)',
        r'[""“”「」]?([^""“”「」]+?)[""“”「」]?\s*不能为空',
        r'[""“”「」]?([^""“”「」]+?)[""“”「」]?\s*(?:已存在|重复)',
        r'锁定字段([^的]+)的值',
    )
    for pattern in patterns:
        m = re.search(pattern, text)
        if m:
            return m.group(1).strip()
    return ""


def _diagnosis_priority_for(category: str) -> list[str]:
    priorities = {
        "pageid_context": [
            "比对 HAR 原始 pageId 与 pageid_trace 回放 pageId",
            "确认 L2/L3 切换和 preserve_l2_page/target_forms",
            "再检查变量、环境字段和异步等待",
        ],
        "open_form_context_blocked": [
            "确认 open_form 前置 L2/L3 链路",
            "检查模板/F7/子弹窗前置选择是否遗漏",
            "判断业务提示是否为录制中的预期校验",
        ],
        "business_template_context_missing": [
            "检查模板选择和选人/F7 链路",
            "确认子弹窗字段维护和确定动作完整",
            "将模板/人员/业务日期暴露为可维护字段",
        ],
        "environment_field_context_missing": [
            "从 HAR loadData/showForm/list rows 提取默认上下文",
            "解析为环境字段并保留内部 id 执行",
            "不要硬补 save.post_data",
        ],
        "f7_lookup_chain_missing": [
            "检查 getLookUpList/entryRowClick/btnok 顺序",
            "确认候选值可维护且能解析内部 id",
            "确认回填发生在正确 L3 表单上下文",
        ],
        "dialog_detail_chain_incomplete": [
            "核对 newentry/F7/子窗字段/确定/主保存全链路",
            "确认明细回填证据和入库回查断言",
            "补表单/步骤作用域变量，不裁剪子窗",
        ],
        "readback_assertion_gap": [
            "先确认保存响应是否已经成功",
            "再确认 commonSearch 回查是否适配该表单",
            "必要时移除通用硬回查断言或补表单专用回查",
        ],
        "workflow_task_wait_timeout": [
            "先在目标环境按运行时单号确认审批待办是否生成",
            "再检查当前账号、审批人和消息中心/流程配置",
            "确认 wait_until 使用运行时 billno，不点击录制旧任务行",
        ],
        "environment_async_business_sync_timeout": [
            "确认前置业务操作响应成功并已进入异步同步",
            "按本次运行时姓名/编号跨状态查询后续业务记录",
            "仍无记录时排查目标环境异步任务、消息队列和投影服务",
        ],
        "component_rule_group_filter_missing": [
            "先确认 pageId 链路无异常",
            "补常用筛选 F7 子窗口回填链",
            "确认 entryentity/groupcontent 已回填再保存",
        ],
        "business_validation_expected": [
            "确认 expected_notification 断言存在",
            "继续执行录制中后续补录/确认步骤",
            "不要把预期业务校验当失败修掉",
        ],
        "business_missing_required": [
            "先查 pageId 链路和默认上下文",
            "再查 HAR 变量/环境字段是否漏识别",
            "最后判断是否真实业务必填未录制",
        ],
        "transient_protocol": [
            "先重试并确认 NO_PROXY/网络",
            "连续失败再查目标服务状态",
        ],
    }
    return priorities.get(category, [
        "先查 pageId 链路",
        "再查变量/环境字段/组件处理器",
        "保留证据后补 failure_analysis 规则",
    ])
