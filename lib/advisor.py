"""advisor - 用例失败时给出可操作的修复建议

核心职责：
1. 把服务端中文错误反向解析成字段 key / 值 / 约束
2. 结合会话历史的响应（特别是 addnew 预填），推测该字段应该填什么
3. 输出可直接粘贴到 YAML 的修复补丁片段

输入：
  errors: list[str]            服务端报错文本（来自 diagnoser.extract_save_errors）
  history: list[Any]            本轮所有 invoke 响应（runner 累积）
  case: dict                    当前 YAML 用例

输出：
  list[Fix]  每条含 diagnosis / suggested_patch（字符串）
"""
from __future__ import annotations

import json
import re
from dataclasses import dataclass, field
from typing import Any


# =============================================================
# 字段中文名 → 技术 key 映射（内置常用，可被响应里 fieldCaption 动态补充）
# =============================================================
# key 名暗示字段类型（响应里拿不到样例值时兜底）
KEY_TYPE_HINTS = {
    # 基础资料类
    "org": "basedata",
    "parentorg": "basedata",
    "adminorg": "basedata",
    "adminorgtype": "basedata",
    "changetype": "basedata",
    "changescene": "basedata",
    "changereason": "basedata",
    "otclassify": "basedata",
    "structproject": "basedata",
    "position": "basedata",
    "country": "basedata",
    "province": "basedata",
    "city": "basedata",
    # 多语言文本
    "name": "multilang_text",
    "simplename": "multilang_text",
    "fullname": "multilang_text",
    "description": "multilang_text",
    "detailaddress": "multilang_text",
    # 日期
    "establishmentdate": "date",
    "bsed": "date",
    "disabledate": "date",
    "startdate": "date",
    "enddate": "date",
    "effectdate": "date",
}


STATIC_FIELD_MAP = {
    # HR 常见基础字段
    "编码": "number",
    "编号": "number",
    "名称": "name",
    "简称": "simplename",
    "描述": "description",
    "成立日期": "establishmentdate",
    "生效日期": "bsed",
    "失效日期": "disabledate",
    "启用状态": "enable",
    "启用": "enable",
    # 组织相关
    "组织体系管理组织": "org",
    "组织体系": "structproject",
    "父组织": "parentorg",
    "上级组织": "parentorg",
    "组织类型": "adminorgtype",
    "行政组织类型": "adminorgtype",
    "分类": "otclassify",
    "变动场景": "changescene",
    "变动类型": "changetype",
    "变动原因": "changereason",
    # 员工/人员常见
    "姓名": "name",
    "工号": "number",
    "入职日期": "startdate",
    "部门": "adminorg",
    "职位": "position",
    "岗位": "position",
}


# 错误模式：正则 → 错误类型
ERROR_PATTERNS = [
    # "请填写"XXX"" / "请选择"YYY""
    (re.compile(r'请(?:填写|输入|录入|选择)\s*["""""]?([^"""""，。；\s]+)["""""]?'),
     "missing_required"),
    # "xxx 不能为空"
    (re.compile(r'["""""]?([^"""""]+?)["""""]?\s*不能为空'),
     "missing_required"),
    # "xxx 已存在" / "xxx 重复"
    (re.compile(r'["""""]?([^"""""]+?)["""""]?\s*(?:已存在|重复)'),
     "duplicate"),
    # "xxx 不允许 yyy" / "xxx 不允许有 yyy"
    (re.compile(r'["""""]?([^"""""]+?)["""""]?\s*不允许(?:有)?\s*(.+?)(?:[，。]|$)'),
     "invalid_value"),
    # "xxx 格式不正确"
    (re.compile(r'["""""]?([^"""""]+?)["""""]?\s*格式(?:不正确|错误)'),
     "invalid_format"),
    # "xxx 超出范围/长度"
    (re.compile(r'["""""]?([^"""""]+?)["""""]?\s*超出'),
     "out_of_range"),
]


@dataclass
class Fix:
    """一条修复建议"""
    diagnosis: str                    # 诊断说明（一句话）
    error_type: str                   # missing_required / duplicate / invalid_value / ...
    field_caption: str = ""           # 从错误中提取的字段中文名
    field_key: str = ""               # 推断出的技术 key（可能为空）
    suggested_value: Any = None       # 建议填入的值（可能为 None）
    patch_yaml: str = ""              # 可直接粘贴的 YAML 补丁片段
    confidence: str = "medium"        # high / medium / low

    def format(self, idx: int) -> str:
        lines = [f"❐ [{idx}] {self.diagnosis}"]
        if self.field_caption:
            key_info = f" key={self.field_key}" if self.field_key else " key=?（未在预填里找到）"
            lines.append(f"   字段: '{self.field_caption}'{key_info}")
        if self.suggested_value is not None:
            val = json.dumps(self.suggested_value, ensure_ascii=False)
            if len(val) > 100:
                val = val[:100] + "..."
            lines.append(f"   建议值: {val}")
        if self.patch_yaml:
            lines.append(f"   建议补丁（插入到 save 步骤之前）:")
            for line in self.patch_yaml.splitlines():
                lines.append(f"     {line}")
        lines.append(f"   置信度: {self.confidence}")
        return "\n".join(lines)


# =============================================================
# 从响应历史提取字段元信息
# =============================================================
def build_field_index(history: list[Any]) -> dict[str, dict]:
    """遍历所有响应，建立 key → {caption, last_value, field_type} 索引"""
    index: dict[str, dict] = {}

    def record(key: str, value: Any = None, caption: str = ""):
        if not key:
            return
        entry = index.setdefault(key, {"captions": set(), "last_value": None, "field_type": None})
        if value is not None:
            entry["last_value"] = value
            entry["field_type"] = _guess_field_type(value)
        if caption:
            entry["captions"].add(caption)

    def walk(obj):
        if isinstance(obj, dict):
            # u action: [{k, v, today, ...}]
            if "k" in obj and ("v" in obj or "today" in obj):
                record(obj.get("k"), obj.get("v"))
            # updateValue 的 field definition 里可能带 fieldCaption
            if obj.get("fieldName") or obj.get("key"):
                k = obj.get("fieldName") or obj.get("key")
                cap = obj.get("fieldCaption") or obj.get("caption")
                if isinstance(cap, dict):
                    cap = cap.get("zh_CN", "")
                if isinstance(k, str) and isinstance(cap, str):
                    record(k, caption=cap)
            for v in obj.values():
                walk(v)
        elif isinstance(obj, list):
            for x in obj:
                walk(x)

    for resp in history:
        walk(resp)
    return index


def _guess_field_type(value: Any) -> str:
    """从样例值推断字段类型"""
    if value is None:
        return "unknown"
    if isinstance(value, bool):
        return "boolean"
    if isinstance(value, (int, float)):
        return "number"
    if isinstance(value, str):
        if re.match(r"^\d{4}-\d{2}-\d{2}", value):
            return "date"
        return "text"
    if isinstance(value, dict):
        if "zh_CN" in value or "GLang" in value:
            return "multilang_text"
        return "object"
    if isinstance(value, list):
        if len(value) >= 2 and isinstance(value[0], str):
            # ["id", "display_name", ...] 这种是基础资料
            return "basedata"
        return "array"
    return "unknown"


def build_caption_to_key_map(field_index: dict[str, dict]) -> dict[str, str]:
    """反转：中文 caption → key"""
    cap_map: dict[str, str] = {}
    for key, info in field_index.items():
        for cap in info.get("captions", set()):
            if cap and cap not in cap_map:
                cap_map[cap] = key
    return cap_map


# =============================================================
# 主分析入口
# =============================================================
def analyze_errors(errors: list[str],
                   history: list[Any] | None = None) -> list[Fix]:
    """把错误列表转换成修复建议列表"""
    history = history or []
    field_index = build_field_index(history)
    dynamic_cap_map = build_caption_to_key_map(field_index)

    # 合并：动态（来自会话响应）优先于静态
    cap_map = dict(STATIC_FIELD_MAP)
    cap_map.update(dynamic_cap_map)

    fixes: list[Fix] = []
    seen_by_key: set[str] = set()   # 按 (error_type, field_key) 去重

    for err in errors:
        err = err.strip()
        if not err:
            continue
        # 跳过汇总信息
        if "成功数量" in err or "失败数量" in err:
            continue

        fix = _analyze_one(err, cap_map, field_index)
        if not fix:
            continue

        # 去重键：按 (error_type, field_key 或 caption) 合并同类错误
        dedup_key = (fix.error_type, fix.field_key or fix.field_caption)
        if dedup_key in seen_by_key:
            continue
        seen_by_key.add(dedup_key)
        fixes.append(fix)

    return fixes


def _analyze_one(err: str, cap_map: dict[str, str],
                 field_index: dict[str, dict]) -> Fix | None:
    """分析单条错误"""
    # 剥掉中英文引号
    QUOTE_CHARS = '"\'""''「」『』'
    for pattern, err_type in ERROR_PATTERNS:
        m = pattern.search(err)
        if not m:
            continue
        caption = m.group(1).strip().strip(QUOTE_CHARS).strip()
        detail = m.group(2).strip() if m.lastindex and m.lastindex >= 2 else ""

        field_key = cap_map.get(caption, "")
        # caption 里可能带"组织"等冗余词，尝试模糊匹配
        if not field_key:
            for cap, k in cap_map.items():
                if caption in cap or cap in caption:
                    field_key = k
                    break

        info = field_index.get(field_key, {}) if field_key else {}
        field_type = info.get("field_type", "unknown")
        last_value = info.get("last_value")
        # 类型兜底：如果会话里没拿到样例值，按 key 名推断
        if field_type in ("unknown", None) and field_key in KEY_TYPE_HINTS:
            field_type = KEY_TYPE_HINTS[field_key]

        fix = Fix(
            diagnosis=err,
            error_type=err_type,
            field_caption=caption,
            field_key=field_key,
        )

        if err_type == "missing_required":
            _build_missing_fix(fix, field_type, last_value)
        elif err_type == "duplicate":
            _build_duplicate_fix(fix)
        elif err_type == "invalid_value":
            _build_invalid_fix(fix, detail)
        elif err_type == "invalid_format":
            _build_format_fix(fix, field_type)

        return fix

    # 不匹配任何模式的兜底
    return Fix(
        diagnosis=err,
        error_type="unknown",
        confidence="low",
    )


# =============================================================
# 各类错误的补丁生成
# =============================================================
def _build_missing_fix(fix: Fix, field_type: str, last_value: Any):
    """必填缺失"""
    if not fix.field_key:
        fix.confidence = "low"
        fix.patch_yaml = (
            f"# 无法自动推断字段 key，请手动:\n"
            f"# 1) 在 HAR 或 loadData 响应里搜 '{fix.field_caption}'\n"
            f"# 2) 找到对应的 k=xxx，填入下方 field_key"
        )
        return

    # 基础资料：用 pick_basedata
    if field_type == "basedata":
        if isinstance(last_value, list) and len(last_value) >= 2:
            # 有会话样例，用它的 id
            fix.suggested_value = last_value[0]
            display = last_value[1] if len(last_value) > 1 else ""
            fix.patch_yaml = (
                f"- id: fill_{fix.field_key}\n"
                f"  type: pick_basedata\n"
                f"  form_id: <主表单 id>\n"
                f"  app_id: <主表单 app_id>\n"
                f"  field_key: {fix.field_key}\n"
                f"  value_id: \"{last_value[0]}\"   # {display}"
            )
            fix.confidence = "high"
        else:
            # 没会话样例，给占位让用户自己填
            fix.patch_yaml = (
                f"- id: fill_{fix.field_key}\n"
                f"  type: pick_basedata\n"
                f"  form_id: <主表单 id>\n"
                f"  app_id: <主表单 app_id>\n"
                f"  field_key: {fix.field_key}\n"
                f"  value_id: \"<该基础资料的 id，可在浏览器操作时 F12 查>\""
            )
            fix.confidence = "medium"
    # 多语言文本
    elif field_type == "multilang_text":
        fix.suggested_value = {"zh_CN": "请填值"}
        fix.patch_yaml = (
            f"- id: fill_{fix.field_key}\n"
            f"  type: update_fields\n"
            f"  form_id: <主表单 id>\n"
            f"  app_id: <主表单 app_id>\n"
            f"  fields:\n"
            f"    {fix.field_key}: {{\"zh_CN\": \"你的值\"}}"
        )
        fix.confidence = "high"
    # 日期
    elif field_type == "date":
        fix.suggested_value = "${today}"
        fix.patch_yaml = (
            f"- id: fill_{fix.field_key}\n"
            f"  type: update_fields\n"
            f"  form_id: <主表单 id>\n"
            f"  app_id: <主表单 app_id>\n"
            f"  fields:\n"
            f"    {fix.field_key}: ${{today}}"
        )
        fix.confidence = "high"
    # 普通字段
    else:
        fix.suggested_value = last_value if last_value else "你的值"
        v = last_value if last_value not in (None, "") else "填写一个值"
        fix.patch_yaml = (
            f"- id: fill_{fix.field_key}\n"
            f"  type: update_fields\n"
            f"  form_id: <主表单 id>\n"
            f"  app_id: <主表单 app_id>\n"
            f"  fields:\n"
            f"    {fix.field_key}: {json.dumps(v, ensure_ascii=False)}"
        )
        fix.confidence = "medium"


def _build_duplicate_fix(fix: Fix):
    fix.diagnosis = f"{fix.field_caption} 已存在/重复"
    fix.patch_yaml = (
        f"# '{fix.field_caption}' 值已存在\n"
        f"# 建议: 在 vars 里用 ${{rand:5}} 或 ${{timestamp}} 生成唯一值\n"
        f"#   test_number: XX${{rand:6}}"
    )
    fix.confidence = "medium"


def _build_invalid_fix(fix: Fix, detail: str):
    fix.diagnosis = f"{fix.field_caption} 值不合法: {detail}"
    # 下划线问题是 HR 常见坑
    if "分隔符" in detail or "_" in detail:
        fix.patch_yaml = (
            f"# 当前值含不允许字符 ({detail.strip()})\n"
            f"# 建议: 把 vars 里相关值的 '_' 改成 '-'"
        )
        fix.confidence = "high"
    else:
        fix.patch_yaml = (
            f"# 字段 '{fix.field_caption}' 值不合法\n"
            f"# 约束: {detail}\n"
            f"# 请调整 vars 或 update_fields 里的值"
        )
        fix.confidence = "medium"


def _build_format_fix(fix: Fix, field_type: str):
    fix.diagnosis = f"{fix.field_caption} 格式不正确"
    if field_type == "date":
        fix.patch_yaml = f"# '{fix.field_caption}' 日期格式应为 YYYY-MM-DD"
    else:
        fix.patch_yaml = f"# '{fix.field_caption}' 格式约束，请查看该字段类型文档"
    fix.confidence = "medium"


# =============================================================
# 格式化输出
# =============================================================
def format_fixes(fixes: list[Fix]) -> str:
    if not fixes:
        return "（未识别到可自动诊断的错误模式）"
    lines = [
        "",
        "═" * 60,
        f"修复建议 ({len(fixes)} 条)",
        "═" * 60,
    ]
    for i, fix in enumerate(fixes, 1):
        lines.append(fix.format(i))
        lines.append("")
    lines.append("─" * 60)
    lines.append("操作指引:")
    lines.append("  1. 审查每条建议，尤其是 confidence=low 的")
    lines.append("  2. 把建议的 YAML 补丁粘到用例 save 步骤之前")
    lines.append("  3. 把 <主表单 id> / <主表单 app_id> 换成实际值")
    lines.append("  4. 再跑一次 runner run")
    return "\n".join(lines)