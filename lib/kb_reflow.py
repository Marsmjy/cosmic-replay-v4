"""知识库回流管道 + 生成后反模式自检（Step 3 新增）

两个关键能力：

1. emit_unknowns(har_path, main_form, yaml_steps):
   - 若 main_form 不在 kb_loader.all_form_ids() → 追加到 _unknown_forms.jsonl
   - 若字段命中不到 kb 元数据 → 追加到 _unknown_fields.jsonl
   - 供后续人工/自动补齐知识库 scenarios/ 目录

2. check_antipatterns(har_path, main_form, yaml_steps) -> list[dict]:
   - 参考 skills/cosmic-replay-troubleshooter 4 层修复经验
   - 返回命中的反模式警告列表，供 build_yaml_case 写入 YAML 头部注释
   - 同时落盘 _antipatterns.jsonl（便于巡检）

存储位置：
- logs/_unknowns/_unknown_forms.jsonl
- logs/_unknowns/_unknown_fields.jsonl
- logs/_unknowns/_antipatterns.jsonl

设计约束：
- 所有失败静默（写盘异常不得影响 YAML 生成）
- 字段采样值截断至 32 字符，避免泄露
"""
from __future__ import annotations

import json
import time
from pathlib import Path
from typing import Any

try:
    from lib import kb_loader as _kb
except Exception:  # pragma: no cover
    _kb = None  # type: ignore


# ---------- 文件路径 ----------

def _unknowns_dir() -> Path:
    root = Path(__file__).resolve().parent.parent
    d = root / "logs" / "_unknowns"
    try:
        d.mkdir(parents=True, exist_ok=True)
    except Exception:
        pass
    return d


def _append_jsonl(fname: str, record: dict) -> None:
    """静默追加一行 JSONL。失败不抛。"""
    try:
        p = _unknowns_dir() / fname
        with p.open("a", encoding="utf-8") as f:
            f.write(json.dumps(record, ensure_ascii=False) + "\n")
    except Exception:
        pass


def _truncate(v: Any, n: int = 32) -> str:
    s = "" if v is None else str(v)
    if len(s) > n:
        return s[:n] + "…"
    return s


# ---------- 回流：未命中 form/field ----------

def emit_unknowns(har_path: Path, main_form: str, yaml_steps: list[dict]) -> dict:
    """扫描 yaml_steps，识别 kb 未覆盖的 form_id 和 field_key，追加到回流文件。

    返回统计 dict：
      {unknown_form: bool, unknown_fields_count: int, unknown_forms_secondary: list[str]}
    供反模式自检二次使用。
    """
    stats = {
        "unknown_form": False,
        "unknown_fields_count": 0,
        "unknown_forms_secondary": [],
    }
    if _kb is None:
        return stats

    ts = int(time.time())
    har_name = har_path.name if har_path else ""

    # 1) 主表单未命中 → _unknown_forms.jsonl
    try:
        covered = _kb.all_form_ids()
    except Exception:
        covered = set()

    # 收集 yaml_steps 中所有涉及的 form_id，用于主表单 record 里带出旁证
    form_key_samples: dict[str, dict[str, Any]] = {}
    for s in yaml_steps:
        fid = s.get("form_id", "") or ""
        if not fid:
            continue
        bucket = form_key_samples.setdefault(fid, {"field_keys": set(), "sample_values": {}})
        # update_fields 字段
        flds = s.get("fields") or {}
        if isinstance(flds, dict):
            for k, v in flds.items():
                bucket["field_keys"].add(k)
                if isinstance(v, (str, int, float)) and k not in bucket["sample_values"]:
                    bucket["sample_values"][k] = _truncate(v)
                elif isinstance(v, dict) and "zh_CN" in v and k not in bucket["sample_values"]:
                    bucket["sample_values"][k] = _truncate(v.get("zh_CN"))
        # pick_basedata field_key
        fk = s.get("field_key") or ""
        if fk:
            bucket["field_keys"].add(fk)
            vid = s.get("value_id")
            if vid is not None and fk not in bucket["sample_values"]:
                bucket["sample_values"][fk] = _truncate(vid)

    # 1.1 主表单回流
    if main_form and main_form not in covered:
        stats["unknown_form"] = True
        mf_bucket = form_key_samples.get(main_form, {"field_keys": set(), "sample_values": {}})
        _append_jsonl("_unknown_forms.jsonl", {
            "ts": ts,
            "har": har_name,
            "form_id": main_form,
            "role": "main_form",
            "field_keys": sorted(mf_bucket["field_keys"])[:30],
            "field_samples": dict(list(mf_bucket["sample_values"].items())[:10]),
        })

    # 1.2 副表单（出现在 steps 里但非主表单，且未命中）
    for fid in form_key_samples:
        if fid == main_form or not fid or fid in covered:
            continue
        # 门户/框架 form 不回流（降噪）
        if (fid.startswith("bos_portal") or fid.startswith("bos_list")
                or fid.startswith("bos_card_") or fid.startswith("bos_devp_")
                or fid.startswith("homs_") or fid in ("home_page",)):
            continue
        # 没有任何字段交互的副 form（只是被 open/close）不回流
        bucket = form_key_samples[fid]
        if not bucket["field_keys"]:
            continue
        stats["unknown_forms_secondary"].append(fid)
        _append_jsonl("_unknown_forms.jsonl", {
            "ts": ts,
            "har": har_name,
            "form_id": fid,
            "role": "secondary",
            "field_keys": sorted(bucket["field_keys"])[:30],
            "field_samples": dict(list(bucket["sample_values"].items())[:10]),
        })

    # 2) 字段未命中 kb 元数据（仅对 kb 已命中的 form 做，否则噪声太大）
    for fid, bucket in form_key_samples.items():
        if not fid or fid not in covered:
            continue
        try:
            scene = _kb.resolve_scene(fid) or {}
            kb_fields = scene.get("fields") or {}
        except Exception:
            continue
        for fk in bucket["field_keys"]:
            if fk in kb_fields or fk.lower() in kb_fields:
                continue
            stats["unknown_fields_count"] += 1
            _append_jsonl("_unknown_fields.jsonl", {
                "ts": ts,
                "har": har_name,
                "form_id": fid,
                "field_key": fk,
                "sample_value": bucket["sample_values"].get(fk, ""),
            })

    return stats


# ---------- 反模式自检 ----------

_WRITE_ACS = {
    "saveandeffect", "submitandeffect", "saveandaudit",
    "save", "submit", "doconfirm", "afterconfirm", "startupflow",
}
_WRITE_KEYS = {
    "btnsave", "btn_save", "bar_save", "barsave",
    "btn_confirm", "btnconfirm", "bar_confirm", "barconfirm",
    "btnok", "btn_ok", "bar_submit", "barsubmit",
    "barstart", "bar_start",
    "btn_saveandeffect", "btnsaveandeffect",
}
_PAGE_ANCHOR_ACS = {"menuItemClick", "appItemClick", "selectTab"}


def _is_write_step(s: dict) -> bool:
    ac = str(s.get("ac") or "").lower()
    if ac in _WRITE_ACS:
        return True
    key = str(s.get("key") or "").lower()
    if ac in ("click", "itemclick") and key in _WRITE_KEYS:
        return True
    return False


def check_antipatterns(har_path: Path, main_form: str, yaml_steps: list[dict],
                       unknown_stats: dict | None = None) -> list[dict]:
    """基于 troubleshooter 4 层修复经验做生成后自检。

    命中规则时产出 warning 记录（type/level/detail/fix_hint），并落盘 jsonl。
    level: "error" > "warn" > "info"
    """
    warnings: list[dict] = []
    har_name = har_path.name if har_path else ""
    ts = int(time.time())

    # 规则 1：写库步骤被标为 optional（troubleshooter ui_reaction 误判）
    for s in yaml_steps:
        if s.get("optional") and _is_write_step(s):
            warnings.append({
                "type": "write_step_optional",
                "level": "error",
                "step_id": s.get("id", ""),
                "detail": f"写库步骤 id={s.get('id')} ac={s.get('ac')} key={s.get('key')} 被标 optional，回放时可能被跳过，导致不入库",
                "fix_hint": "从该 step 移除 optional 标记，或在 cleaned 阶段加入白名单",
            })

    # 规则 2：pageId 链断 — 有写库但没有任何 page 锚点步骤
    has_write = any(_is_write_step(s) for s in yaml_steps)
    has_page_anchor = any(
        s.get("ac") in _PAGE_ANCHOR_ACS
        or (s.get("type") == "open_form" and s.get("form_id") == main_form)
        for s in yaml_steps
    )
    if has_write and not has_page_anchor:
        warnings.append({
            "type": "pageid_chain_break",
            "level": "error",
            "step_id": "",
            "detail": "存在写库步骤但缺少 pageId 锚点（menuItemClick/appItemClick/selectTab/open_form(main)）",
            "fix_hint": "检查裁剪阶段是否误伤入口步骤；必要时在 cleaned 补 menuItemClick",
        })

    # 规则 3：入库类缺业务主体 — 有 save 但 0 个 pick_basedata + 0 个 update_fields
    if has_write:
        body_count = sum(
            1 for s in yaml_steps
            if s.get("type") in ("pick_basedata", "update_fields")
        )
        if body_count == 0:
            warnings.append({
                "type": "write_without_body",
                "level": "warn",
                "step_id": "",
                "detail": "存在写库步骤但没有任何 pick_basedata / update_fields 步骤，可能 HAR 采集不全",
                "fix_hint": "确认 HAR 包含字段填写过程；检查 merge_consecutive_update_values 是否误合并",
            })

    # 规则 4：主表单未在 kb 覆盖 — 表示此场景缺场景摘要，无法做字段精准分类
    if unknown_stats and unknown_stats.get("unknown_form"):
        warnings.append({
            "type": "main_form_not_in_kb",
            "level": "info",
            "step_id": "",
            "detail": f"主表单 {main_form} 未在 skills/.../scenarios/ 知识库中，字段分类走启发式兜底",
            "fix_hint": "拉取该 form 的 scenario.json + scene_doc_lite.json 补入知识库",
        })

    # 规则 5：多个连续 save（keep_page 多轮新增）但未抽 test_number/test_name 变量
    save_count = sum(1 for s in yaml_steps if _is_write_step(s))
    if save_count >= 2:
        # 采样一步看看是否有 ${vars.test_number} 等引用
        dumped = json.dumps(yaml_steps, ensure_ascii=False, default=str)
        if "${vars.test_number}" not in dumped and "${vars.test_name}" not in dumped:
            warnings.append({
                "type": "multi_save_without_vars",
                "level": "warn",
                "step_id": "",
                "detail": f"检测到 {save_count} 个写库步骤但未见 test_number/test_name 变量，重跑第二轮可能'已存在'",
                "fix_hint": "检查 number/name/code 字段是否被变量化（detect_var_placeholders）",
            })

    # 规则 6：form_id 为空的 open_form（注入过程中可能丢了 form_id）
    for s in yaml_steps:
        if s.get("type") == "open_form" and not s.get("form_id"):
            warnings.append({
                "type": "open_form_missing_form_id",
                "level": "error",
                "step_id": s.get("id", ""),
                "detail": f"open_form 步骤 id={s.get('id')} 缺 form_id，回放时无法获取 pageId",
                "fix_hint": "检查 inject_step 逻辑或回到源 HAR 人工修正",
            })

    # 落盘
    if warnings:
        _append_jsonl("_antipatterns.jsonl", {
            "ts": ts,
            "har": har_name,
            "main_form": main_form,
            "warnings": warnings,
        })
    return warnings


def format_warnings_for_yaml(warnings: list[dict]) -> list[str]:
    """把 warnings 格式化为 YAML 头部注释行列表。空则返回 []。"""
    if not warnings:
        return []
    lines = ["# ", "# ⚠ 生成期反模式自检（Step 3 回流）"]
    level_icon = {"error": "❌", "warn": "⚠️", "info": "ℹ️"}
    for w in warnings:
        icon = level_icon.get(w.get("level", "info"), "•")
        detail = str(w.get("detail", "")).replace("\n", " ")
        lines.append(f"#   {icon} [{w.get('type','?')}] {detail}")
        fix = w.get("fix_hint")
        if fix:
            lines.append(f"#       → 建议：{fix}")
    return lines


__all__ = [
    "emit_unknowns",
    "check_antipatterns",
    "format_warnings_for_yaml",
]
