"""缁堟瀬楠岃瘉 v2锛氭寜 HAR 鐪熷疄鍗忚璧颁竴閬?- 鐧诲綍 鈫?getConfig myapp_new 鈫?menuItemClick 鈫?L2 loadData 鈫?addnew 鈫?L3 鈫?save"""
from __future__ import annotations

import json
import sys
import urllib.parse
import uuid
from pathlib import Path

HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
from replay import login  # noqa: E402


def find_har_action(har_path: Path, ac: str) -> tuple[str, str, list]:
    """浠?HAR 鎵剧涓€涓尮閰?ac 鐨勮姹?(appId, formId, actions)"""
    data = json.loads(har_path.read_text(encoding="utf-8"))
    for e in data["log"]["entries"]:
        url = e["request"]["url"]
        if "batchInvokeAction.do" not in url:
            continue
        q = {x["name"]: x["value"] for x in e["request"].get("queryString", []) or []}
        if q.get("ac") != ac:
            continue
        body = (e["request"].get("postData") or {}).get("text", "")
        parsed = urllib.parse.parse_qs(body, keep_blank_values=True)
        params = parsed.get("params", [""])[0]
        return q.get("appId", ""), q.get("f", ""), json.loads(params) if params else []
    return "", "", []


def main():
    har_path = Path("D:/aiworkspace/琛屾斂缁勭粐蹇€熺淮鎶ゆ搷浣?har")

    import yaml
    cfg = yaml.safe_load((HERE.parent / "config" / "envs" / "sit.yaml").read_text(encoding="utf-8"))
    base_url = cfg["env"]["base_url"]
    dc_id = cfg["env"]["datacenter_id"]

    session = login(base_url, "<your-username>", "<your-password>", dc_id)
    print(f"Login OK")

    import requests, urllib3
    urllib3.disable_warnings()

    # ---- Step 0: init root pageId ----
    flag = uuid.uuid4().hex[:16]
    f_val = uuid.uuid4().hex[:18]
    r = requests.get(
        f"{base_url}/form/getConfig.do",
        params={"params": json.dumps({"formId": "home_page", "flag": flag, "f": f_val}),
                "random": "0.5"},
        headers=session.base_headers(cqappid="bos"),
        timeout=30, verify=False,
    )
    root_pid = r.json()["pageId"]
    root_base = root_pid.replace("root", "")
    session.root_page_id = root_pid
    session.root_base_id = root_base
    print(f"root_page_id = {root_pid}")

    def post_invoke(pid, app, form, ac, actions, label=""):
        body = urllib.parse.urlencode([
            ("pageId", pid), ("appId", app),
            ("params", json.dumps(actions, ensure_ascii=False, separators=(",", ":"))),
        ])
        headers = session.base_headers(cqappid=app)
        headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        url = f"{base_url}/form/batchInvokeAction.do?appId={app}&f={form}&ac={ac}"
        r = requests.post(url, data=body, headers=headers, timeout=30, verify=False)
        return r

    # ---- Step 1: getConfig bos_portal_myapp_new锛堟敞鎰?rootPageId 鍙傛暟锛?---
    r1 = requests.get(
        f"{base_url}/form/getConfig.do",
        params={"params": json.dumps({
            "formId": "bos_portal_myapp_new",
            "rootPageId": root_pid,
            "flag": uuid.uuid4().hex[:16],
            "f": uuid.uuid4().hex[:18],
        }), "random": "0.5"},
        headers=session.base_headers(cqappid="bos"),
        timeout=30, verify=False,
    )
    myapp_pid = r1.json()["pageId"]
    print(f"myapp_new pageId = {myapp_pid}")

    # ---- Step 2: menuItemClick 鐐瑰嚮"琛屾斂缁勭粐" 搴旂敤锛堜粠 HAR 鎷垮弬鏁帮級----
    app_id, form_id, menu_actions = find_har_action(har_path, "menuItemClick")
    print(f"\nStep 2: menuItemClick ({app_id}/{form_id})")
    print(f"  actions: {menu_actions}")
    r2 = post_invoke(myapp_pid, app_id, form_id, "menuItemClick", menu_actions)
    print(f"  Status: {r2.status_code}, size: {len(r2.text)}")
    resp2 = r2.json()
    # harvest new pageIds
    def harvest_pageids(obj, out):
        if isinstance(obj, dict):
            if "pageId" in obj and "formId" in obj:
                out[obj["formId"]] = obj["pageId"]
            for v in obj.values(): harvest_pageids(v, out)
        elif isinstance(obj, list):
            for x in obj: harvest_pageids(x, out)
    pageids = {}
    harvest_pageids(resp2, pageids)
    print(f"  Harvested from response: {pageids}")

    # ---- Step 3: loadData on L2 (haos_adminorgdetail) ----
    # L2 pageId = {menuId}root{baseId}
    menu_id = "1443450410974114816"
    l2_pid = f"{menu_id}root{root_base}"
    print(f"\nStep 3: L2 loadData  pageId={l2_pid}")
    load_actions = [{"key": "", "methodName": "loadData", "args": [], "postData": [{}, []]}]
    r3 = post_invoke(l2_pid, "haos", "haos_adminorgdetail", "loadData", load_actions)
    print(f"  Status: {r3.status_code}, size: {len(r3.text)}")
    if len(r3.text) < 500:
        print(f"  Body: {r3.text}")

    # ---- Step 4: addnew on L2 ----
    addnew_app, addnew_form, addnew_actions = find_har_action(har_path, "addnew")
    print(f"\nStep 4: addnew on L2")
    print(f"  addnew_actions: {addnew_actions}")
    r4 = post_invoke(l2_pid, addnew_app, addnew_form, "addnew", addnew_actions)
    print(f"  Status: {r4.status_code}, size: {len(r4.text)}")
    try:
        resp4 = r4.json()
    except Exception:
        print(f"  bad json: {r4.text[:300]}")
        return
    # find L3 pageId (32-hex random)
    l3_pid = None
    def find_l3(obj):
        nonlocal l3_pid
        if isinstance(obj, dict):
            pid = obj.get("pageId")
            if (isinstance(pid, str) and len(pid) == 32
                    and "root" not in pid and not l3_pid):
                l3_pid = pid
            for v in obj.values(): find_l3(v)
        elif isinstance(obj, list):
            for x in obj: find_l3(x)
    find_l3(resp4)
    print(f"  L3 pageId: {l3_pid}")
    if not l3_pid:
        print(f"  dump: {json.dumps(resp4, ensure_ascii=False)[:800]}")
        return

    # ---- Step 5: L3 loadData (init new form) ----
    print(f"\nStep 5: L3 loadData")
    r5 = post_invoke(l3_pid, "haos", "haos_adminorgdetail", "loadData", load_actions)
    print(f"  Status: {r5.status_code}, size: {len(r5.text)}")

    # ---- Step 6: save on L3 (na茂ve - see what fields it complains about) ----
    print(f"\nStep 6: save on L3 (no fields filled yet - expect validation errors)")
    save_app, save_form, save_actions = find_har_action(har_path, "save")
    print(f"  save_actions: {save_actions}")
    r6 = post_invoke(l3_pid, save_app, save_form, "save", save_actions)
    print(f"  Status: {r6.status_code}, size: {len(r6.text)}")
    print(f"  Body (first 1500 chars): {r6.text[:1500]}")


if __name__ == "__main__":
    main()