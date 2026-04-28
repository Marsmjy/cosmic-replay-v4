"""响应诊断：把苍穹的含蓄错误反馈翻译成人类可读的根因。

核心逻辑：
- 保存/提交类操作失败时，苍穹不报 HTTP 错误，而是弹 bos_operationresult 子表单
- 要拿到错误详情：从 save 响应提取 bos_operationresult 的 pageId，再对它发 loadData
- 响应里有 `text=成功数量：0，失败数量：N` 和 `v=具体错误 1\n具体错误 2`
"""
from __future__ import annotations

import json
from typing import Any, TYPE_CHECKING

from .replay import find_form_in_response, has_error_action  # re-export

if TYPE_CHECKING:
    from .replay import CosmicFormReplay


OPERATION_RESULT_FORMID = "bos_operationresult"


def extract_save_errors(save_resp: Any, replay: "CosmicFormReplay") -> list[str]:
    """从 save 响应中提取所有业务错误文本。

    策略：
    1) 先扫顶层 showErrMsg / showMessage
    2) 再找 bos_operationresult 弹窗（校验错误的典型载体），对它发 loadData 拉详情

    返回：去重错误列表（空列表 = 没发现错误，可能真成功）
    """
    errors = list(has_error_action(save_resp))

    op_form = find_form_in_response(save_resp, OPERATION_RESULT_FORMID)
    if op_form:
        op_pid = op_form.get("pageId")
        if op_pid:
            try:
                op_resp = replay.invoke(
                    OPERATION_RESULT_FORMID, "bos", "loadData",
                    [{"key": "", "methodName": "loadData", "args": [], "postData": []}],
                    page_id=op_pid,
                )
                errors.extend(_scan_operation_result(op_resp))
            except Exception as e:
                errors.append(f"(无法打开操作结果弹窗: {e})")

    return _dedup(errors)


def _scan_operation_result(op_resp: Any) -> list[str]:
    """从 bos_operationresult loadData 响应里提取错误明细。"""
    errors: list[str] = []

    def walk(obj):
        if isinstance(obj, dict):
            v = obj.get("v")
            if isinstance(v, str) and _looks_like_error(v):
                # 一般是多行错误
                for line in v.splitlines():
                    line = line.strip()
                    if line:
                        errors.append(line)
            text = obj.get("text")
            if isinstance(text, str) and ("失败数量" in text or "成功数量" in text):
                errors.append(text.strip())
            for val in obj.values():
                walk(val)
        elif isinstance(obj, list):
            for x in obj:
                walk(x)

    walk(op_resp)
    return errors


def _looks_like_error(s: str) -> bool:
    kws = ("请填写", "请选择", "不允许", "不能为空", "必填", "必须", "不合法",
           "已存在", "重复", "超出", "错误", "失败", "无效")
    return any(k in s for k in kws)


def _dedup(items: list[str]) -> list[str]:
    seen: set[str] = set()
    out: list[str] = []
    for s in items:
        k = s.strip()
        if k and k not in seen:
            seen.add(k)
            out.append(k)
    return out


def summarize_response(resp: Any) -> dict:
    """生成响应摘要（调试用）。返回 {action_count, actions, has_errors}"""
    info = {"action_count": 0, "actions": [], "has_errors": False}
    if isinstance(resp, list):
        info["action_count"] = len(resp)
        info["actions"] = [
            cmd.get("a") for cmd in resp
            if isinstance(cmd, dict) and cmd.get("a")
        ]
        if has_error_action(resp):
            info["has_errors"] = True
    elif isinstance(resp, dict):
        info["action_count"] = 1
        info["error"] = resp.get("msg") or resp.get("message")
        if info["error"]:
            info["has_errors"] = True
    return info


def format_error_report(step_name: str, errors: list[str], resp_summary: dict) -> str:
    """生成人类可读的失败报告"""
    lines = [
        f"═══ 步骤 '{step_name}' 失败 ═══",
        f"响应摘要: {resp_summary['action_count']} 个 action",
    ]
    if resp_summary.get("actions"):
        lines.append(f"  action 类型: {', '.join(resp_summary['actions'])}")
    if errors:
        lines.append(f"服务端报错 {len(errors)} 条:")
        for i, e in enumerate(errors, 1):
            lines.append(f"  [{i}] {e}")
    else:
        lines.append("未找到明确错误消息，可能是预期行为或响应结构异常。")
    return "\n".join(lines)