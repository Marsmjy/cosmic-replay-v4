#!/usr/bin/env python3
"""Probe raw HAR pageId/component chains and output value-safe experience."""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_chain_probe import build_experience_catalog, probe_har_chain
from lib.har_regression import load_manifest


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("har_paths", nargs="*", type=Path, help="HAR files to probe")
    parser.add_argument("--manifest", type=Path, help="Probe all samples from HAR regression manifest")
    parser.add_argument("--output", type=Path, default=Path("tmp/har_chain_probe.json"))
    parser.add_argument("--include-fragments", action="store_true", help="Include short pageId fragments in local output")
    parser.add_argument(
        "--full-events",
        action="store_true",
        help="For manifest mode, write full per-event probes instead of compact experience catalog",
    )
    args = parser.parse_args(argv)

    payload: dict
    if args.manifest:
        samples = []
        full = []
        for sample in load_manifest(args.manifest):
            probe = probe_har_chain(sample.har_path, include_fragments=args.include_fragments)
            samples.append({
                "id": sample.id,
                "title": sample.title,
                "business_type": sample.business_type,
                "probe": probe,
            })
            full.append({
                "sample_id": sample.id,
                "title": sample.title,
                "business_type": sample.business_type,
                "probe": probe,
            })
        payload = {"schema_version": 1, "samples": full} if args.full_events else build_experience_catalog(samples)
    else:
        if not args.har_paths:
            parser.error("provide HAR paths or --manifest")
        probes = [
            probe_har_chain(path, include_fragments=args.include_fragments)
            for path in args.har_paths
        ]
        payload = {"schema_version": 1, "probes": probes}

    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    print(args.output.resolve())
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

