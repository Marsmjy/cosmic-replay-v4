"""HAR regression sample snapshots and impact reports.

This module keeps regression baselines intentionally value-safe: it records the
shape of generated YAML, variables, environment fields and component coverage,
but not actual business values from HAR files.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
from dataclasses import dataclass
from pathlib import Path
from typing import Any

import yaml

from lib.har_extractor import build_yaml_case, extract_steps, load_har, preview_har
from lib.pageid_trace import build_pageid_trace, compact_pageid_trace


PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_MANIFEST = PROJECT_ROOT / "tests" / "fixtures" / "har_regression" / "manifest.json"
DEFAULT_BASELINE_DIR = PROJECT_ROOT / "tests" / "fixtures" / "har_regression" / "baselines"
SNAPSHOT_SCHEMA_VERSION = 2


@dataclass(frozen=True)
class RegressionSample:
    id: str
    title: str
    har_path: Path
    case_name: str
    business_type: str = ""


def load_manifest(path: Path = DEFAULT_MANIFEST) -> list[RegressionSample]:
    data = json.loads(path.read_text(encoding="utf-8"))
    samples: list[RegressionSample] = []
    for item in data.get("samples", []):
        har_path = Path(item["har_path"])
        if not har_path.is_absolute():
            har_path = PROJECT_ROOT / har_path
        samples.append(RegressionSample(
            id=item["id"],
            title=item.get("title") or item["id"],
            har_path=har_path,
            case_name=item.get("case_name") or f"regression_{item['id']}",
            business_type=item.get("business_type", ""),
        ))
    return samples


def generate_snapshot(sample: RegressionSample) -> dict[str, Any]:
    """Generate a deterministic, redacted snapshot for one HAR sample."""
    yaml_text = build_yaml_case(sample.har_path, case_name=sample.case_name)
    case = yaml.safe_load(yaml_text)
    preview = preview_har(sample.har_path)

    har_steps = extract_steps(load_har(sample.har_path))
    case_summary = summarize_case(case, har_steps=har_steps)
    preview_summary = summarize_preview(preview)
    snapshot = {
        "schema_version": SNAPSHOT_SCHEMA_VERSION,
        "sample_id": sample.id,
        "title": sample.title,
        "business_type": sample.business_type,
        "source_har": sample.har_path.name,
        "source_sha256": _sha256_file(sample.har_path),
        "case": case_summary,
        "preview": preview_summary,
    }
    snapshot["summary_hash"] = stable_hash({
        "case": case_summary,
        "preview": preview_summary,
    })
    return snapshot


def summarize_case(case: dict[str, Any], har_steps: list[dict[str, Any]] | None = None) -> dict[str, Any]:
    vars_map = case.get("vars") or {}
    vars_labels = case.get("vars_labels") or {}
    pick_fields = case.get("pick_fields") or {}
    steps = case.get("steps") or []
    assertions = case.get("assertions") or []
    field_catalog = case.get("field_catalog") or []
    generation_gate = case.get("generation_gate") or {}
    pageid_source_graph = case.get("pageid_source_graph") or {}
    environment_binding_plan = case.get("environment_binding_plan") or {}

    return {
        "name": case.get("name", ""),
        "main_form_id": case.get("main_form_id", ""),
        "scenario_kind": ((case.get("scenario") or {}).get("kind") or ""),
        "scenario_stages": list((case.get("scenario") or {}).get("stages") or []),
        "step_count": len(steps),
        "assertion_count": len(assertions),
        "target_forms": sorted(case.get("target_forms") or []),
        "vars": [
            {
                "name": name,
                "label": vars_labels.get(name, ""),
                "value_shape": value_shape(value),
            }
            for name, value in sorted(vars_map.items())
        ],
        "pick_fields": [
            summarize_pick_field(name, cfg)
            for name, cfg in sorted(pick_fields.items())
        ],
        "field_catalog": {
            "count": len(field_catalog),
            "signatures": [
                "|".join([
                    str(item.get("order") or ""),
                    str(item.get("field_id") or ""),
                    str(item.get("category") or ""),
                    str(item.get("panel") or ""),
                    ",".join(sorted(item.get("vars") or [])),
                    ",".join(sorted(item.get("pick_fields") or [])),
                ])
                for item in field_catalog
                if isinstance(item, dict)
            ],
        },
        "generation_gate": {
            "decision": generation_gate.get("decision", ""),
            "allow_generate": bool(generation_gate.get("allow_generate", True)),
            "allow_run": bool(generation_gate.get("allow_run", True)),
            "blocker_codes": sorted({
                str(item.get("code") or "")
                for item in generation_gate.get("issues") or []
                if isinstance(item, dict) and item.get("blocks_generate") and item.get("code")
            }),
        },
        "environment_resolvers": [
            "|".join([
                str(item.get("id") or ""),
                str(item.get("resolver_kind") or ""),
                str(item.get("interface") or ""),
                str(item.get("resolve_by") or ""),
                str(item.get("match_policy") or ""),
                str(item.get("grid_key") or ""),
                str(item.get("code_column") or ""),
                str(item.get("name_column") or ""),
                str(item.get("id_column") or ""),
                str(item.get("version_column") or ""),
            ])
            for item in environment_binding_plan.get("fields") or []
            if isinstance(item, dict)
        ],
        "pageid_source_graph": {
            "summary": dict(pageid_source_graph.get("summary") or {}),
            "unsafe_consumer_step_ids": list(
                pageid_source_graph.get("unsafe_consumer_step_ids") or []
            ),
        },
        "steps": [summarize_step(step) for step in steps],
        "assertions": [
            {
                "type": item.get("type", ""),
                "step": item.get("step", ""),
            }
            for item in assertions
        ],
        "pageid_trace": compact_pageid_trace(
            build_pageid_trace(case, har_steps=har_steps or [], include_fragments=False)
        ),
    }


def summarize_preview(preview: dict[str, Any]) -> dict[str, Any]:
    quality = preview.get("quality") or {}
    components = preview.get("components") or {}
    component_summary = components.get("summary") or {}
    pageid_alignment = preview.get("pageid_alignment") or {}
    recorded_pageid_flow = preview.get("recorded_pageid_flow") or {}
    recorded_pageid_summary = recorded_pageid_flow.get("summary") or {}
    ir_alignment = preview.get("ir_alignment") or {}
    ir_preview = preview.get("ir_preview") or {}
    ir_write_bridge = preview.get("ir_write_bridge") or {}
    ir_write_checks = ir_write_bridge.get("checks") or {}
    ir_interaction_bridge = preview.get("ir_interaction_bridge") or {}
    ir_interaction_summary = ir_interaction_bridge.get("summary") or {}
    detected_vars = preview.get("detected_vars") or []
    pick_fields = preview.get("pick_fields") or []
    business_flow = preview.get("business_flow") or []
    response_signature_steps = [
        step for step in (preview.get("steps") or [])
        if (step.get("response_signature") or {}).get("label")
    ]
    response_contract_counts = {
        level: sum(
            1
            for step in response_signature_steps
            if (step.get("response_signature") or {}).get("contract_level") == level
        )
        for level in ("critical", "business", "advisory")
    }
    field_catalog = preview.get("field_catalog") or []
    preflight = preview.get("preflight") or {}
    generation_gate = preview.get("generation_gate") or preflight.get("generation_gate") or {}

    return {
        "main_form_id": preview.get("main_form_id", ""),
        "scenario_kind": ((preview.get("scenario") or {}).get("kind") or ""),
        "scenario_stages": list((preview.get("scenario") or {}).get("stages") or []),
        "quality": {
            "score": quality.get("score"),
            "grade": quality.get("grade", ""),
            "blocking": bool(quality.get("blocking")),
            "issue_codes": sorted({
                str(issue.get("code") or "")
                for issue in quality.get("issues", [])
                if issue.get("code")
            }),
        },
        "components": {
            "total_steps": component_summary.get("total_steps", 0),
            "supported_steps": component_summary.get("supported_steps", 0),
            "partial_steps": component_summary.get("partial_steps", 0),
            "unsupported_steps": component_summary.get("unsupported_steps", 0),
            "coverage_percent": component_summary.get("coverage_percent", 100),
            "risk_level": component_summary.get("risk_level", ""),
            "handler_ids": [
                item.get("handler_id", "")
                for item in components.get("handlers", [])
            ],
        },
        "detected_vars": sorted(
            item.get("name", "")
            for item in detected_vars
            if item.get("name")
        ),
        "pick_field_keys": sorted(
            item.get("field_key", "")
            for item in pick_fields
            if item.get("field_key")
        ),
        "field_catalog": {
            "count": len(field_catalog),
            "field_ids": [
                str(item.get("field_id") or "")
                for item in field_catalog
                if isinstance(item, dict) and item.get("field_id")
            ],
            "unknown_count": sum(
                1
                for item in field_catalog
                if isinstance(item, dict) and item.get("category") in {"", "unknown"}
            ),
        },
        "generation_gate": {
            "preflight_decision": preflight.get("decision", ""),
            "decision": generation_gate.get("decision", ""),
            "allow_generate": bool(generation_gate.get("allow_generate", True)),
            "allow_run": bool(generation_gate.get("allow_run", True)),
            "blocker_codes": sorted({
                str(item.get("code") or "")
                for item in generation_gate.get("issues") or []
                if isinstance(item, dict) and item.get("blocks_generate") and item.get("code")
            }),
        },
        "maintainability": {
            "detected_var_count": len(detected_vars),
            "pick_field_count": len(pick_fields),
            "business_flow_count": len(business_flow),
            "business_flow_step_count": sum(int(item.get("step_count") or 0) for item in business_flow),
            "response_signature_step_count": len(response_signature_steps),
            "response_contract_counts": response_contract_counts,
            "auto_resolve_pick_count": sum(1 for item in pick_fields if item.get("auto_resolve")),
            "high_env_sensitive_count": sum(1 for item in pick_fields if item.get("env_sensitive") == "high"),
            "ir_write_bridge_status": ir_write_bridge.get("status", ""),
            "ir_write_anchor_count": ir_write_checks.get("ir_write_anchor_count", 0),
            "ir_write_anchor_uncovered_count": ir_write_checks.get("uncovered_write_anchor_count", 0),
            "ir_write_contract_missing_count": ir_write_checks.get(
                "critical_response_contract_missing_count",
                0,
            ),
            "ir_interaction_bridge_status": ir_interaction_bridge.get("status", ""),
            "ir_interaction_count": ir_interaction_summary.get("interaction_count", 0),
            "ir_interaction_uncovered_count": ir_interaction_summary.get("uncovered_count", 0),
            "ir_interaction_high_risk_uncovered_count": ir_interaction_summary.get(
                "uncovered_high_risk_count",
                0,
            ),
        },
        "pageid_alignment": {
            "grade": pageid_alignment.get("grade", ""),
            "risk_level": pageid_alignment.get("risk_level", ""),
            "issue_codes": sorted({
                str(issue.get("code") or "")
                for issue in pageid_alignment.get("issues", [])
                if issue.get("code")
            }),
            "preserve_l2_count": (
                pageid_alignment.get("checks") or {}
            ).get("preview_l2_preserve_count", 0),
            "recorded_exact_link_count": recorded_pageid_summary.get("exact_link_count", 0),
            "recorded_external_root_count": recorded_pageid_summary.get("external_root_count", 0),
            "recorded_reuse_after_close_count": recorded_pageid_summary.get("reuse_after_close_count", 0),
            "recorded_cross_form_count": recorded_pageid_summary.get("cross_form_count", 0),
            "recorded_filtered_source_count": recorded_pageid_summary.get("filtered_source_count", 0),
        },
        "ir_alignment": {
            "grade": ir_alignment.get("grade", ""),
            "risk_level": ir_alignment.get("risk_level", ""),
            "issue_codes": sorted({
                str(issue.get("code") or "")
                for issue in ir_alignment.get("issues", [])
                if issue.get("code")
            }),
            "warning_codes": sorted({
                str(item.get("code") or "")
                for item in ir_preview.get("warnings", [])
                if item.get("code")
            }),
        },
    }


def summarize_pick_field(name: str, cfg: dict[str, Any]) -> dict[str, Any]:
    return {
        "name": name,
        "field_key": cfg.get("field_key", ""),
        "label": cfg.get("label", ""),
        "form_id": cfg.get("form_id", ""),
        "app_id": cfg.get("app_id", ""),
        "env_sensitive": cfg.get("env_sensitive", ""),
        "auto_resolve": bool(cfg.get("auto_resolve")),
        "resolve_status": cfg.get("resolve_status", ""),
        "resolve_by": cfg.get("resolve_by", ""),
        "value_id_shape": value_shape(cfg.get("value_id", "")),
        "value_name_shape": value_shape(cfg.get("value_name", "")),
    }


def summarize_step(step: dict[str, Any]) -> dict[str, Any]:
    summary = {
        "id": step.get("id", ""),
        "type": step.get("type", ""),
        "form_id": step.get("form_id", ""),
        "app_id": step.get("app_id", ""),
        "optional": bool(step.get("optional", False)),
    }
    step_type = step.get("type")
    if step_type == "invoke":
        summary.update({
            "ac": step.get("ac", ""),
            "method": step.get("method", ""),
            "key": step.get("key", ""),
            "args_count": len(step.get("args") or []),
            "post_data_fields": summarize_post_data(step.get("post_data")),
        })
    elif step_type == "update_fields":
        fields = step.get("fields") or {}
        summary["fields"] = [
            {"field_key": key, "value_shape": value_shape(value)}
            for key, value in sorted(fields.items())
        ]
    elif step_type == "pick_basedata":
        summary.update({
            "field_key": step.get("field_key", ""),
            "value_id_shape": value_shape(step.get("value_id", "")),
        })
    elif step_type == "open_form":
        summary["target"] = step.get("target", "")
    return summary


def summarize_post_data(post_data: Any) -> list[dict[str, Any]]:
    if not isinstance(post_data, list) or len(post_data) < 2:
        return []
    dirty = post_data[1]
    if not isinstance(dirty, list):
        return []
    fields: list[dict[str, Any]] = []
    for entry in dirty:
        if not isinstance(entry, dict):
            continue
        key = entry.get("k", "")
        if not key:
            continue
        fields.append({
            "field_key": key,
            "value_shape": value_shape(entry.get("v")),
        })
    return sorted(fields, key=lambda item: item["field_key"])


def value_shape(value: Any) -> dict[str, Any]:
    """Return a non-sensitive shape for a YAML value."""
    if value is None:
        return {"kind": "null"}
    if isinstance(value, bool):
        return {"kind": "boolean"}
    if isinstance(value, int):
        return {"kind": "integer"}
    if isinstance(value, float):
        return {"kind": "number"}
    if isinstance(value, list):
        return {"kind": "list", "length": len(value)}
    if isinstance(value, dict):
        if "zh_CN" in value:
            return {
                "kind": "multilang",
                "locales": sorted(str(k) for k in value.keys()),
                "zh_CN": value_shape(value.get("zh_CN")),
            }
        return {"kind": "dict", "keys": sorted(str(k) for k in value.keys())}
    if isinstance(value, str):
        tokens = sorted(set(re.findall(r"\$\{[^}]+}", value)))
        if not value:
            return {"kind": "empty"}
        if tokens:
            return {
                "kind": "template",
                "tokens": [_token_shape(token) for token in tokens],
            }
        return {"kind": "literal", "length": len(value)}
    return {"kind": type(value).__name__}


def compare_snapshots(baseline: dict[str, Any], current: dict[str, Any]) -> dict[str, Any]:
    diffs: list[dict[str, Any]] = []
    _diff_value(baseline, current, "", diffs)
    diffs = [
        diff for diff in diffs
        if diff["path"] not in {"summary_hash"}
    ]
    for diff in diffs:
        diff["severity"] = classify_diff_severity(diff)
    level = "none"
    if any(diff["severity"] == "breaking" for diff in diffs):
        level = "breaking"
    elif any(diff["severity"] == "review" for diff in diffs):
        level = "review"
    elif diffs:
        level = "info"
    return {
        "sample_id": current.get("sample_id") or baseline.get("sample_id"),
        "title": current.get("title") or baseline.get("title"),
        "changed": bool(diffs),
        "impact_level": level,
        "diff_count": len(diffs),
        "diffs": diffs,
    }


def classify_diff_severity(diff: dict[str, Any]) -> str:
    path = diff.get("path", "")
    if path.startswith("case.main_form_id"):
        return "breaking"
    if path.startswith("case.steps"):
        return "breaking"
    if path.startswith("case.assertions"):
        return "review"
    if path.startswith("case.pick_fields") or path.startswith("case.target_forms"):
        return "review"
    if path.startswith("preview.components.unsupported_steps"):
        return "breaking"
    if path.startswith("preview.quality.blocking"):
        return "breaking"
    if path.startswith("preview.pageid_alignment.risk_level"):
        return "review"
    if path.startswith("preview.pageid_alignment.issue_codes"):
        return "review"
    if path.startswith("preview.ir_alignment.issue_codes"):
        return "review"
    if path.startswith("preview.maintainability"):
        return "review"
    if path.startswith("case.vars") or path.startswith("preview.detected_vars"):
        return "review"
    if path.startswith("source_sha256"):
        return "info"
    return "info"


def stable_hash(value: Any) -> str:
    payload = json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))
    return hashlib.sha256(payload.encode("utf-8")).hexdigest()


def baseline_path(sample: RegressionSample, baseline_dir: Path = DEFAULT_BASELINE_DIR) -> Path:
    return baseline_dir / f"{sample.id}.json"


def load_baseline(sample: RegressionSample, baseline_dir: Path = DEFAULT_BASELINE_DIR) -> dict[str, Any]:
    return json.loads(baseline_path(sample, baseline_dir).read_text(encoding="utf-8"))


def save_snapshot(snapshot: dict[str, Any], path: Path) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(
        json.dumps(snapshot, ensure_ascii=False, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def compare_manifest(
    manifest_path: Path = DEFAULT_MANIFEST,
    baseline_dir: Path = DEFAULT_BASELINE_DIR,
) -> dict[str, Any]:
    samples = load_manifest(manifest_path)
    results = []
    for sample in samples:
        baseline = load_baseline(sample, baseline_dir)
        current = generate_snapshot(sample)
        results.append(compare_snapshots(baseline, current))
    changed = [result for result in results if result["changed"]]
    return {
        "sample_count": len(results),
        "changed_count": len(changed),
        "impact_level": _overall_impact(results),
        "results": results,
    }


def markdown_report(report: dict[str, Any]) -> str:
    lines = [
        "# HAR Regression Impact Report",
        "",
        f"- Samples: {report['sample_count']}",
        f"- Changed: {report['changed_count']}",
        f"- Overall impact: {report['impact_level']}",
        "",
    ]
    for result in report["results"]:
        mark = "CHANGED" if result["changed"] else "OK"
        lines.append(f"## {mark} · {result['sample_id']} · {result.get('title', '')}")
        lines.append("")
        lines.append(f"- Impact: {result['impact_level']}")
        lines.append(f"- Diff count: {result['diff_count']}")
        if result["diffs"]:
            lines.append("")
            lines.append("| Severity | Path | Change |")
            lines.append("|---|---|---|")
            for diff in result["diffs"][:20]:
                before = _display_value(diff.get("baseline"))
                after = _display_value(diff.get("current"))
                lines.append(
                    f"| {diff['severity']} | `{diff['path']}` | {before} -> {after} |"
                )
        lines.append("")
    return "\n".join(lines).rstrip() + "\n"


def _sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def _token_shape(token: str) -> str:
    inner = token[2:-1].strip()
    if inner.startswith("vars."):
        return "${vars.*}"
    if inner.startswith("env:"):
        return "${env:*}"
    if inner.startswith("rand:"):
        return "${rand:*}"
    return "${" + inner.split(":", 1)[0] + "}"


def _diff_value(baseline: Any, current: Any, path: str, diffs: list[dict[str, Any]]) -> None:
    if isinstance(baseline, dict) and isinstance(current, dict):
        keys = sorted(set(baseline) | set(current))
        for key in keys:
            child_path = f"{path}.{key}" if path else str(key)
            if key not in baseline:
                diffs.append({"path": child_path, "kind": "added", "baseline": None, "current": current[key]})
            elif key not in current:
                diffs.append({"path": child_path, "kind": "removed", "baseline": baseline[key], "current": None})
            else:
                _diff_value(baseline[key], current[key], child_path, diffs)
        return

    if isinstance(baseline, list) and isinstance(current, list):
        key = _list_identity_key(baseline, current)
        if key:
            bmap = {str(item.get(key)): item for item in baseline}
            cmap = {str(item.get(key)): item for item in current}
            for item_key in sorted(set(bmap) | set(cmap)):
                child_path = f"{path}[{item_key}]"
                if item_key not in bmap:
                    diffs.append({"path": child_path, "kind": "added", "baseline": None, "current": cmap[item_key]})
                elif item_key not in cmap:
                    diffs.append({"path": child_path, "kind": "removed", "baseline": bmap[item_key], "current": None})
                else:
                    _diff_value(bmap[item_key], cmap[item_key], child_path, diffs)
            return
        if baseline != current:
            diffs.append({
                "path": path,
                "kind": "changed",
                "baseline": {"length": len(baseline)},
                "current": {"length": len(current)},
            })
        return

    if baseline != current:
        diffs.append({"path": path, "kind": "changed", "baseline": baseline, "current": current})


def _list_identity_key(left: list[Any], right: list[Any]) -> str | None:
    items = left + right
    if not items or not all(isinstance(item, dict) for item in items):
        return None
    for key in ("id", "name", "field_key", "type"):
        left_values = [item.get(key) for item in left]
        right_values = [item.get(key) for item in right]
        values = left_values + right_values
        if (
            all(value not in (None, "") for value in values)
            and len(set(left_values)) == len(left_values)
            and len(set(right_values)) == len(right_values)
        ):
            return key
    return None


def _overall_impact(results: list[dict[str, Any]]) -> str:
    levels = {result["impact_level"] for result in results}
    if "breaking" in levels:
        return "breaking"
    if "review" in levels:
        return "review"
    if "info" in levels:
        return "info"
    return "none"


def _display_value(value: Any) -> str:
    text = json.dumps(value, ensure_ascii=False, sort_keys=True)
    if len(text) > 80:
        text = text[:77] + "..."
    return text.replace("|", "\\|")


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description="Generate HAR regression snapshots or impact reports.")
    sub = parser.add_subparsers(dest="command", required=True)

    snapshot_cmd = sub.add_parser("snapshot", help="Generate snapshots for all manifest samples.")
    snapshot_cmd.add_argument("--manifest", type=Path, default=DEFAULT_MANIFEST)
    snapshot_cmd.add_argument("--baseline-dir", type=Path, default=DEFAULT_BASELINE_DIR)
    snapshot_cmd.add_argument("--update-baseline", action="store_true")
    snapshot_cmd.add_argument("--output", type=Path)

    compare_cmd = sub.add_parser("compare", help="Compare current generated snapshots with baselines.")
    compare_cmd.add_argument("--manifest", type=Path, default=DEFAULT_MANIFEST)
    compare_cmd.add_argument("--baseline-dir", type=Path, default=DEFAULT_BASELINE_DIR)
    compare_cmd.add_argument("--output", type=Path)
    compare_cmd.add_argument("--format", choices=("json", "markdown"), default="markdown")
    compare_cmd.add_argument("--fail-on-diff", action="store_true")

    args = parser.parse_args(argv)

    if args.command == "snapshot":
        samples = load_manifest(args.manifest)
        snapshots = [generate_snapshot(sample) for sample in samples]
        if args.update_baseline:
            for sample, snapshot in zip(samples, snapshots):
                save_snapshot(snapshot, baseline_path(sample, args.baseline_dir))
        payload = {
            "sample_count": len(snapshots),
            "snapshots": snapshots,
        }
        text = json.dumps(payload, ensure_ascii=False, indent=2, sort_keys=True) + "\n"
        if args.output:
            args.output.write_text(text, encoding="utf-8")
        else:
            print(text, end="")
        return 0

    report = compare_manifest(args.manifest, args.baseline_dir)
    if args.format == "json":
        text = json.dumps(report, ensure_ascii=False, indent=2, sort_keys=True) + "\n"
    else:
        text = markdown_report(report)
    if args.output:
        args.output.write_text(text, encoding="utf-8")
    else:
        print(text, end="")
    if args.fail_on_diff and report["changed_count"] > 0:
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
