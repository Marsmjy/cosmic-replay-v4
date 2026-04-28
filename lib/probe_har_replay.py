"""鏈€绮剧‘鐨?HAR 鍥炴斁锛氫粠 entry 234(menuItemClick) 鍒?291(save) 閫愭潯澶嶅埗 params 鎵ц

鐩殑锛氭帓闄?YAML/runner/field_resolver 绛変腑闂磋浆鎹㈢殑浠讳綍宸紓锛岀湅 HAR 鐩存挱鍥炴斁鏄惁鑳芥垚鍔熴€?鍏抽敭锛氶渶瑕佹浛鎹?HAR 閲岀殑 menu_id/root_base_id 涓烘垜浠綋鍓?session 鐨勫€笺€?浣嗗叾瀹炶鏀跨粍缁?menu_id 鏄厓鏁版嵁鍥哄畾鐨勶紝涓嶉渶瑕佹崲锛沺ageId 褰㈠紡鏄?{menuId}root{baseId}锛?L3 pageId 鏄湇鍔＄鍔ㄦ€佷笅鍙戠殑锛屾墍浠ワ細
- menuItemClick 澶嶇敤 HAR params
- addnew 澶嶇敤 HAR params
- L3 loadData 澶嶇敤 HAR params锛堜絾 pageId 鐢ㄦ垜浠?harvest 鐨勶級
- save 澶嶇敤 HAR params锛坧ageId 涓€鏍凤級
- updateValue 姝ラ鏀?number/name 涓洪殢鏈哄€硷紝閬垮厤缂栧彿鍐茬獊
"""
from __future__ import annotations

import json
import re
import sys
import urllib.parse
import uuid
from pathlib import Path

HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
from replay import login  # noqa: E402


def load_har_entries(p: Path) -> list:
    return json.loads(p.read_text(encoding="utf-8"))["log"]["entries"]


def find_entry_by_range(entries: list, start: int, end: int) -> list:
    """鍙彇 batchInvokeAction.do 鐨?entries锛屽湪 [start, end] 鑼冨洿"""
    out = []
    for i, e in enumerate(entries):
        if i < start or i > end: continue
        if "batchInvokeAction.do" not in e["request"]["url"]: continue
        out.append((i, e))
    return out


def main():
    har_path = Path("D:/aiworkspace/琛屾斂缁勭粐蹇€熺淮鎶ゆ搷浣?har")
    entries = load_har_entries(har_path)

    import yaml
    cfg = yaml.safe_load((HERE.parent / "config" / "envs" / "sit.yaml").read_text(encoding="utf-8"))
    base_url = cfg["env"]["base_url"]
    dc_id = cfg["env"]["datacenter_id"]
    sess = login(base_url, "<your-username>", "<your-password>", dc_id)

    import requests, urllib3
    urllib3.disable_warnings()

    # 浼氳瘽鏍?+ portal
    r = requests.get(f"{base_url}/form/getConfig.do",
                     params={"params": json.dumps({"formId": "home_page",
                              "flag": uuid.uuid4().hex[:16], "f": uuid.uuid4().hex[:18]}), "random": "0.5"},
                     headers=sess.base_headers(cqappid="bos"),
                     timeout=30, verify=False)
    root_pid = r.json()["pageId"]
    root_base = root_pid.replace("root", "")
    print(f"root_pid = {root_pid}")

    r = requests.get(f"{base_url}/form/getConfig.do",
                     params={"params": json.dumps({"formId": "bos_portal_myapp_new",
                              "rootPageId": root_pid, "flag": uuid.uuid4().hex[:16],
                              "f": uuid.uuid4().hex[:18]}), "random": "0.5"},
                     headers=sess.base_headers(cqappid="bos"),
                     timeout=30, verify=False)
    portal_pid = r.json()["pageId"]
    print(f"portal_pid = {portal_pid}")

    # HAR 閲岀殑 base id 鐢ㄤ簬 pageId 鏇挎崲
    HAR_ROOT_BASE = "6100cc3abf4e412b9dd37038841cba6e"
    # portal pid in HAR
    HAR_PORTAL_PID = "fea090d36c7e4feeab7be3b7f6ea27b6"

    # 璁板綍 HAR L3 pid 鈫?鎴戜滑鐨?L3 pid 鏄犲皠
    har_l3_pid = "8800c3e8468547e3914c41d679f9e376"
    our_l3_pid = None

    # 鐢熸垚鍞竴 number 閬垮厤鎾炲崟
    rand_num = uuid.uuid4().hex[:6].upper()

    # HAR entries we want to replay: 234 ~ 291 (menuItemClick to save)
    # 浣嗘垜浠細璺宠繃涓€浜涚函 UI 鐨?(clientCallBack / selectTab / getConfig)
    # 涓撴敞 batchInvokeAction 閲屽涓绘祦绋嬪叧閿殑锛?    TARGET_INDICES = [234, 240, 267, 270, 282, 284, 285, 286, 287, 288, 290, 291]

    for idx in TARGET_INDICES:
        e = entries[idx]
        q = {x['name']: x['value'] for x in e['request'].get('queryString', []) or []}
        body = (e['request'].get('postData') or {}).get('text', '')
        parsed = urllib.parse.parse_qs(body, keep_blank_values=True)
        orig_pid = parsed.get('pageId', [''])[0]
        orig_params = parsed.get('params', [''])[0]
        app = q.get('appId', '')
        form = q.get('f', '')
        ac = q.get('ac', '')

        # 鏇挎崲 pageId锛欻AR 鐨?baseId 鎹㈡垚鎴戜滑鐨?        pid = orig_pid.replace(HAR_ROOT_BASE, root_base)
        if pid == HAR_PORTAL_PID:
            pid = portal_pid
        if pid == har_l3_pid and our_l3_pid:
            pid = our_l3_pid

        # 鏇挎崲 params 閲岀殑 pageId baseId
        params = orig_params.replace(HAR_ROOT_BASE, root_base)

        # 鏇挎崲 number = 闅忔満锛岄伩鍏嶅啿绐?        if ac == "updateValue" and '"number"' in params:
            params = re.sub(r'"v":"[^"]*","r":-1,?(?="k":"number")', '', params)  # 娓呮棫
            # 鏇寸畝鍗曪細鐩存帴 replace 宸茬煡鐨?HAR number
            params = params.replace('"mmmaaatest0422"', f'"REPLAY{rand_num}"')
            params = params.replace('mmmma娴嬭瘯0422', f'閲嶆斁娴嬭瘯{rand_num}')
            params = params.replace('mmmma娓│0422', f'閲嶆斁娴嬭瘯{rand_num}')

        body_new = urllib.parse.urlencode([
            ("pageId", pid), ("appId", app), ("params", params),
        ])
        h = sess.base_headers(cqappid=app)
        h["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        url = f"{base_url}/form/batchInvokeAction.do?appId={app}&f={form}&ac={ac}"
        r = requests.post(url, data=body_new, headers=h, timeout=30, verify=False)

        # 鎵?L3 pageId
        if ac == "addnew" and not our_l3_pid:
            try:
                rj = r.json()
                def walk(obj):
                    global our_l3_pid
                    if isinstance(obj, dict):
                        pid_c = obj.get("pageId")
                        if isinstance(pid_c, str) and len(pid_c) == 32 and "root" not in pid_c:
                            return pid_c
                        for v in obj.values():
                            got = walk(v)
                            if got: return got
                    elif isinstance(obj, list):
                        for x in obj:
                            got = walk(x)
                            if got: return got
                    return None
                our_l3_pid = walk(rj)
                print(f"  [{idx}] addnew 鈫?L3 pid = {our_l3_pid}")
            except Exception as ex:
                print(f"  {ex}")

        status = r.status_code
        size = len(r.text)
        # 鏌ョ湅鍝嶅簲鏄惁鏈夐敊璇抗璞?        is_save = ac == "save"
        badge = ""
        if "璇峰～鍐? in r.text or "showErrMsg" in r.text:
            badge = " !!! HAS ERROR"
        elif is_save and "formTitle" in r.text:
            badge = " 鈫?save may have succeeded (has formTitle)"
        print(f"  [{idx}] {ac:28s} pid={pid[:38]} status={status} size={size}{badge}")
        if is_save:
            print(f"\n=== SAVE RESPONSE ===")
            print(r.text[:2500])


if __name__ == "__main__":
    main()