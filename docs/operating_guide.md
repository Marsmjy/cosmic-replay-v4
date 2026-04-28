# 鎿嶄綔鎸囧紩 - 浠庨浂鍒扮涓€鏉＄豢鐢ㄤ緥

杩欎唤鏂囨。**鎸夋椂闂撮『搴?*璧颁竴閬嶅畬鏁存搷浣溿€備綘鎸夋楠ゅ仛灏辫兘鐢紝涓嶇敤璺宠鍏朵粬鏂囨。銆?
> 鍏朵粬鏂囨。锛?> - 鎵╁睍鏂板満鏅殑缁嗚妭锛歚extending.md`
> - 澶辫触鎬庝箞淇細`troubleshooting.md`
> - 鎵撳寘鍙戠粰鍒汉锛歚packaging.md`

---

## 0. 鍓嶇疆妫€鏌ワ紙1 鍒嗛挓锛?
```bash
# 1. 杩?skill 鐩綍
cd .claude/skills/cosmic-replay

# 2. 纭渚濊禆
python -c "import requests, urllib3; print('ok')"
# 濡傛灉鎶ラ敊锛歱ip install requests urllib3

# 3. 纭 cosmic-login skill 鍦ㄥ悓绾?ls ../cosmic-login/cosmic_login.py
# 濡傛灉娌℃湁锛氬鍒?cosmic-login 鐩綍杩囨潵
```

## 1. 閰嶇疆鍑瘉锛? 鍒嗛挓锛屼竴娆℃€э級

鐢ㄧ幆澧冨彉閲忥紝**涓嶈纭紪鐮佸埌 YAML**锛?
**Windows PowerShell**锛?```powershell
$env:COSMIC_USERNAME = "<your-username>"
$env:COSMIC_PASSWORD = "<your-password>"
$env:COSMIC_DATACENTER_ID = "<your-datacenter-id>"
```

**Linux/Mac bash**锛?```bash
export COSMIC_USERNAME=<your-username>
export COSMIC_PASSWORD='<your-password>'
export COSMIC_DATACENTER_ID=<your-datacenter-id>
```

鍐欏埌 `~/.bashrc` / `~/.zshrc` 鎴?PowerShell profile 閲屽氨涓嶇敤姣忔鏁层€?
## 2. 璺戝弬鑰冪敤渚嬮獙璇佺幆澧冿紙1 鍒嗛挓锛?
```bash
python -m lib.runner run cases/admin_org_new.yaml
```

**姝ｅ父杈撳嚭**锛?- 鐧诲綍鎴愬姛 鈫?8 姝ュ叏閮?`[ok]` 鈫?鏈€鍚?`[FAIL]` + 淇寤鸿

**杩欎釜 FAIL 鏄鏈熺殑**锛圫IT 璐﹀彿娌℃湁缁勭粐鏁版嵁锛夛紝**鑳界湅鍒?淇寤鸿"鍧楀氨璇佹槑 skill 宸ヤ綔姝ｅ父**銆?
**濡傛灉鐪嬪埌鐨勬槸杩欎簺**锛屾寜瀵瑰簲澶勭悊锛?
| 鐪嬪埌 | 鍘熷洜 | 澶勭悊 |
|---|---|---|
| `鎵句笉鍒?cosmic-login skill` | skill 璺緞閿?| 鎶?cosmic-login 澶嶅埗鍒板悓绾х洰褰?|
| `Login failed: 502 Bad Gateway` | SIT 缃戝叧鎶栧姩 | 绛?1 鍒嗛挓閲嶈瘯锛堝凡鑷姩閲嶈瘯 3 娆★級 |
| `ModuleNotFoundError: requests` | 娌¤渚濊禆 | `pip install requests urllib3` |
| 鏃犱慨澶嶅缓璁潡 | advisor 娌″伐浣?| 妫€鏌?lib/advisor.py 鏄惁瀹屾暣 |

## 3. 褰曚綘鐨勭涓€涓?HAR锛? 鍒嗛挓锛?
閫変竴涓?*浣犵啛鎮夈€佹湁瀹屾暣鏁版嵁銆佽兘鐪熶繚瀛樻垚鍔?*鐨勭畝鍗曞満鏅€傚缓璁笉瑕佷竴涓婃潵灏辨寫鏈€澶嶆潅鐨勩€?
**褰曞埗姝ラ**锛?
1. Chrome/Edge 鎵撳紑鑻嶇┕鐜锛岀櫥褰?2. 鎸?`F12` 鈫?`Network` 闈㈡澘
3. **鍕鹃€?"Preserve log"** + **"Disable cache"**
4. 鐐圭孩鍦堟寜閽‘淇濆湪褰曞埗涓?5. **鐐?馃毇 娓呯┖褰撳墠鏃ュ織**锛堝共鍑€寮€濮嬶級
6. 瀹屾暣璧颁竴閬嶄綘瑕佹祴鐨勫満鏅細
   - 鐐硅彍鍗?鈫?鎵撳紑鍔熻兘
   - 鐐规柊澧?   - 濉墍鏈夊繀濉瓧娈?   - 鐐逛繚瀛?   - 绛夋祻瑙堝櫒鏄剧ず"淇濆瓨鎴愬姛"
7. Network 闈㈡澘鍙抽敭鏌愭潯璇锋眰 鈫?`Save all as HAR with content`
8. 瀛樺埌 `har/浣犵殑鍦烘櫙鍚?har`锛堜笉瑕?commit锛?
**褰曞埗鍘熷垯**锛?- 涓€娆″綍瀹岋紝涓嶅垏 tab 涓嶅埛鏂?- 鎸?涓€瀹氳兘淇濆瓨鎴愬姛"鐨勬暟鎹紙淇濊瘉鏈嶅姟绔姝ゆ暟鎹弧鎰忥級
- 涓嶈鍦ㄥけ璐ュ悗鍙堥噸璇曪紝浼氳 HAR 閲屽嚭鐜颁竴鍫嗛敊璇姹?
## 4. 鑷姩鐢熸垚 YAML 璧锋绋匡紙30 绉掞級

```bash
python -m lib.har_extractor extract path/to/浣犵殑鍦烘櫙鍚?har \
    -o cases/浣犵殑鍦烘櫙鍚?yaml
```

鎵撳紑鐢熸垚鐨?YAML 鐪嬬湅銆傚吀鍨嬭捣姝ョ 50-80 琛岋紝鍖呭惈锛?- `env`: 鍑瘉寮曠敤锛堝凡濉?`${env:COSMIC_*}`锛?- `vars`: 绌烘ā鏉?- `main_form_id`: 鎺ㄦ柇鐨勪富琛ㄥ崟
- `steps`: 鍏ㄩ儴涓氬姟鍔ㄤ綔锛堝姩鎬佸€肩暀绌猴級
- `assertions`: 榛樿鏂█

## 5. 娓呯悊 YAML锛?0-20 鍒嗛挓锛?
### 5.1 鍒犲櫔澹?step

璧锋绋块噷甯?`optional: true` 鐨勫ぇ澶氬彲浠ュ垹锛?```yaml
# 鍒犺繖绫?鈫?- id: step_3_clientCallBack
  type: invoke
  ac: clientCallBack
  optional: true

# 鎴栬繖绫?- id: step_14_queryExceedMaxCount
  ac: queryExceedMaxCount
  optional: true
```

淇濈暀楠ㄦ灦锛?```yaml
# 蹇呯暀 鈫?- type: open_form               # 鎵撳紑琛ㄥ崟
- ac: loadData                  # 鍒濆鍔犺浇
- ac: addnew / modify / delete  # 瑙﹀彂鍔ㄤ綔
- ac: updateValue               # 濉瓧娈?- ac: setItemByIdFromClient     # 閫夊熀纭€璧勬枡
- ac: save                      # 鏈€缁堟彁浜?```

### 5.2 鍔ㄦ€佸€兼敼鍗犱綅绗?
鎵?YAML 閲岀‖缂栫爜鐨勬祴璇曞€硷細
```yaml
# 鍘熸潵 鈫?fields:
  number: "TEST12345"
  name: {"zh_CN": "娴嬭瘯鍛樺伐"}

# 鏀规垚 鈫?vars:
  test_number: EMP${rand:6}
  test_name: 璐ㄩ噺娴嬭瘯鍛樺伐${test_number}

# steps 閲?fields:
  number: ${vars.test_number}
  name: {"zh_CN": "${vars.test_name}"}
```

**鍗犱綅绗﹀弬鑰?*锛?- `${timestamp}` 鈫?姣鏃堕棿鎴?- `${today}` 鈫?浠婂ぉ鏃ユ湡 `2026-04-22`
- `${rand:N}` 鈫?N 浣嶉殢鏈烘暟瀛?- `${uuid}` 鈫?uuid hex
- `${vars.xxx}` 鈫?vars 閲岀殑鍙橀噺
- `${env:VAR_NAME:default}` 鈫?鐜鍙橀噺

### 5.3 鍔犳柇瑷€

鍦ㄦ枃浠舵湯灏撅細
```yaml
assertions:
  - type: no_save_failure      # 娌℃湁寮规牎楠岄敊璇獥
    step: save                 # 浣犵殑 save step id
  - type: response_contains    # 淇濆瓨鍝嶅簲閲屽寘鍚垜濉殑缂栧彿
    step: save
    needle: ${vars.test_number}
```

## 6. 璺戠敤渚嬶紙3-5 绉掞級

```bash
python -m lib.runner run cases/浣犵殑鍦烘櫙鍚?yaml
```

### 缁撴灉 A锛歚[PASS]`
鍏ュ簱锛?```bash
git add cases/浣犵殑鍦烘櫙鍚?yaml
git commit -m "add replay case: 浣犵殑鍦烘櫙鍚?
```

### 缁撴灉 B锛歚[FAIL]` + 淇寤鸿

璇诲缓璁潡锛?*鍏稿瀷涓夌被**锛?
**绫诲瀷 1**锛歚缃俊搴? high`锛堢洿鎺ョ収鎶勶級
```
鉂?鍊间笉鍚堟硶: 鐗规畩鍒嗛殧绗?_
   寤鸿: 鎶?vars 閲岀浉鍏冲€肩殑 _ 鏀规垚 -
```
鈫?鏀?vars

**绫诲瀷 2**锛歚缃俊搴? medium`锛屽熀纭€璧勬枡 id 瑕佷綘濉?```
鉂?璇峰～鍐?缁勭粐浣撶郴绠＄悊缁勭粐"
   瀛楁: key=org
   寤鸿琛ヤ竵:
     - id: fill_org
       type: pick_basedata
       field_key: org
       value_id: "<璇ュ熀纭€璧勬枡鐨?id>"
```
鈫?涓ょ鍔炴硶鎷?id锛?  - 鐢ㄦ枃鏈紪杈戝櫒鎵撳紑 HAR锛屾悳杩欎釜瀛楁鍚嶏紝闄勮繎 `args: [["xxx", 0]]` 閲岀殑 `xxx` 灏辨槸
  - 鍦ㄦ祻瑙堝櫒閲岄噸鏂版搷浣滀竴娆★紝F12 鐪嬭姹?
**绫诲瀷 3**锛歚缃俊搴? low`锛坅dvisor 甯笉鍒颁綘锛?鈫?闇€瑕佷汉宸ワ細
  - 鐪嬪師濮?HAR 鎵惧瓧娈?key
  - 闂?cosmic-dev skill锛?haos_employee 鐨勫瓧娈靛垪琛?

### 缁撴灉 C锛氬崗璁敊璇紙闈?advisor 鑼冪暣锛?
```
[ERR] No pageId for haos_employee
```
鈫?鍦ㄥ墠闈㈠姞 `- type: open_form` step

```
[ERR] No such method updateValue on TextEdit
```
鈫?`updateValue` 鐢ㄩ敊浜嗭紝`key` 搴斾负绌轰覆銆傛敼鐢?`type: update_fields`

## 7. 寰幆鏀?璺戠洿鍒?PASS

鍏稿瀷寰幆锛?```
绗?1 杞? 3 鏉″缓璁?鈫?鏀?2 鍒嗛挓 鈫?璺?5 绉?绗?2 杞? 1 鏉″缓璁?鈫?鏀?1 鍒嗛挓 鈫?璺?5 绉?绗?3 杞? PASS
```

**浠€涔堟椂鍊欏仠**锛?- 杩炵画 3 杞悓涓€鏉￠敊璇敼涓嶆帀 鈫?璇存槑 advisor 甯笉涓婏紝闇€瑕佷汉宸ヤ粙鍏?- 鐢ㄦ椂瓒?1 灏忔椂杩樻病缁?鈫?鍙兘鍦烘櫙閫夊緱澶鏉傦紝鎹釜绠€鍗曠殑鍏堢粌鎵?
## 8. 鎺?CI锛堝彲閫夛紝棣栨 10 鍒嗛挓锛?
GitHub Actions 绀轰緥锛?```yaml
# .github/workflows/replay-regression.yml
name: replay regression
on: [pull_request]
jobs:
  replay:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with: { python-version: '3.11' }
      - run: pip install requests urllib3 pyyaml
      - name: run all cases
        env:
          COSMIC_USERNAME: ${{ secrets.COSMIC_USERNAME }}
          COSMIC_PASSWORD: ${{ secrets.COSMIC_PASSWORD }}
          COSMIC_DATACENTER_ID: ${{ secrets.COSMIC_DATACENTER_ID }}
        run: |
          cd .claude/skills/cosmic-replay
          for case in cases/*.yaml; do
            python -m lib.runner run "$case" || exit 1
          done
```

---

## 甯歌闄烽槺閫熸煡

| 鐥囩姸 | 鍘熷洜 | 瑙ｆ硶 |
|---|---|---|
| `YAML 鏍煎紡閿欒` | 缂╄繘娣蜂簡 tab 鍜岀┖鏍?| 缁熶竴鐢?2 绌烘牸缂╄繘 |
| `HTTP 502` on login | SIT 缃戝叧鎶栧姩 | 绛?1-2 鍒嗛挓閲嶈瘯 |
| 鎵€鏈?step 閮?FAIL | cookie 澶辨晥 | 鑷姩閲嶇櫥锛屾鏌ュ瘑鐮佸涓嶅 |
| save 鐪嬬潃鎴愬姛浣?assertion FAIL | no_save_failure 鎵掑埌 bos_operationresult | 鐪嬪缓璁紝鍩烘湰閮芥槸缂哄瓧娈?|
| 鍩虹璧勬枡 id 鎹㈢幆澧冨け鏁?| 纭紪鐮佷簡 id | 鐢?`${env:XXX_ID}` 鎴?_shared/ 鏂囦欢鎶界 |
| advisor 璇嗗埆涓嶅嚭瀛楁 | 涓枃鍚嶄笉鍦ㄦ槧灏勮〃 | 鐪?troubleshooting.md 璐＄尞鏄犲皠 |

---

## 涓€鍙ヨ瘽鎬荤粨

**褰?HAR 鈫?extract 鈫?娓呯悊 鈫?璺?鈫?鎸夊缓璁敼 鈫?鍐嶈窇 鈫?PASS 鈫?鍏ュ簱**銆?
3-5 鏉＄敤渚嬪悗浣犱細鏈夋劅瑙夛紝涔嬪悗姣忔潯鐢ㄤ緥 15 鍒嗛挓鎼炲畾銆