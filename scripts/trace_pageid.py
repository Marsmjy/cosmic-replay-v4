"""Diagnose: trace hom_onbrdinfo pageId changes during case execution."""
import sys, os, json
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from pathlib import Path
from lib.replay import CosmicFormReplay
from lib import runner

# Monkey-patch _harvest_page_ids to trace hom_onbrdinfo changes
_orig_harvest = CosmicFormReplay._harvest_page_ids
_current_step_id = "?"

_TRACK_FORMS = {"hom_onbrdinfo", "hom_persononbrdhandlebody"}

def _traced_harvest(self, resp):
    old_pids = {f: self.page_ids.get(f) for f in _TRACK_FORMS}
    _orig_harvest(self, resp)
    for f in _TRACK_FORMS:
        old_pid = old_pids[f]
        new_pid = self.page_ids.get(f)
        if new_pid != old_pid:
            print(f"  ** [pageId CHANGE] {f}: {str(old_pid)[:20] if old_pid else 'None'} -> {str(new_pid)[:20] if new_pid else 'None'} (during step: {_current_step_id})")

CosmicFormReplay._harvest_page_ids = _traced_harvest

# Monkey-patch run_case to track current step
from lib.runner import run_case, load_yaml

def _trace_emit(evt_type, data):
    global _current_step_id
    if evt_type == "step_start":
        _current_step_id = data.get("id", "?")
        print(f"[step] {_current_step_id}  type={data.get('type', '?')}  form={data.get('form_id', '?')}")
    elif evt_type == "step_done":
        ok = data.get("ok", False)
        if not ok:
            print(f"  ** FAIL: {data.get('error', '?')[:120]}")
    elif evt_type == "case_done":
        passed = data.get("passed", False)
        total = data.get("total_steps", "?")
        done = data.get("done_steps", "?")
        dur = data.get("duration_s", "?")
        print(f"\n=== RESULT: {'PASS' if passed else 'FAIL'} ({done}/{total} steps, {dur}s) ===")

# Load and run the case
yaml_path = Path("cases/final_新增入职.yaml")
case = load_yaml(yaml_path)

# Merge env
from lib.config import Config
cfg = Config()
env = cfg.get_env("sit2222")
if env:
    case.setdefault("env", {}).update({
        "base_url": env.base_url,
        "username": env.credentials.username,
        "password": env.credentials.password,
        "datacenter_id": env.datacenter_id,
    })
else:
    print("ERROR: env sit2222 not found")
    sys.exit(1)

print("=== Starting traced run ===\n")
run_case(case, on_event=_trace_emit)
