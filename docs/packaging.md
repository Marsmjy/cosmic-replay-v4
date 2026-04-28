# 鎵撳寘涓庡垎鍙戞寚鍗?- 鎶?skill 浜や粯缁欏埆浜?
鏈枃鍛婅瘔浣狅細**鎶婅繖涓?skill 鍒嗗彂缁欏埆鐨勫洟闃?鍒殑椤圭洰鏃?*锛岃鎵撳寘浠€涔堬紝涓嶆墦鍖呬粈涔堬紝鎬庝箞瀹夎銆?
---

## 1. 渚濊禆娓呭崟锛堝叧閿級

### 1.1 Python 杩愯鏃朵緷璧?
| 渚濊禆 | 鏄惁蹇呴渶 | 鐢ㄩ€?|
|---|---|---|
| Python | **蹇呴渶** | 3.8 鎴栦互涓婏紙鐢ㄤ簡绫诲瀷鏍囨敞 `\|` 璇硶绛夛級 |
| `requests` | **蹇呴渶** | HTTP 璋冪敤 |
| `urllib3` | **蹇呴渶** | requests 鑷甫锛孲SL 楠岃瘉鍏抽棴闇€瑕?|
| `pycryptodome` | **蹇呴渶** | cosmic-login 鐨?RSA 鍔犲瘑 |
| `pyyaml` | 鍙€?| 瑁呬簡鏇村揩锛涙病瑁呮椂 runner 鍥為€€鍒板唴缃交閲?YAML 瑙ｆ瀽鍣?|

**涓€琛屽畨瑁?*锛?```bash
pip install requests urllib3 pycryptodome pyyaml
```

### 1.2 澶栭儴 skill 渚濊禆

**`cosmic-login`** - **蹇呴渶**銆傝繖涓?skill 璐熻矗 RSA 鐧诲綍鎷?Cookie銆?
**涓嶈浠庨」鐩噷澶嶅埗鐧诲綍浠ｇ爜鍒版湰 skill**锛屾垜浠晠鎰忚蛋 subprocess 璋冪敤 cosmic-login 鑴氭湰锛岃繖鏍凤細
- cosmic-login 鏇存柊浜嗕笉褰卞搷鏈?skill
- 涓や釜 skill 鍙互鐙珛缁存姢

### 1.3 Claude Code 渚濊禆

**涓嶉渶瑕?Claude Code**銆俿kill 鏈綋鏄?Python 鍖咃紝CLI 鍙互鍦ㄤ换浣?shell 閲岃窇銆?
濡傛灉鐢?Claude Code锛屽彲浠ラ€氳繃 Skill 绯荤粺鍞よ捣锛屼絾**涓嶆槸蹇呴』**銆?
### 1.4 椤圭洰渚濊禆

**闆?*銆傛湰 skill 瀹屽叏涓?import 浣犻」鐩殑浠讳綍浠ｇ爜銆傚凡楠岃瘉锛?```bash
grep -rn "^from aihr\|^from cosmic_\|^import aihr" .claude/skills/cosmic-replay/lib/ --include="*.py"
# 绌鸿緭鍑?= 闆堕」鐩€﹀悎
```

---

## 2. 鎵撳寘缁撴瀯

### 2.1 鏈€灏忓畬鏁村寘锛堟帹鑽愶級

```
cosmic-replay-v1.0/
鈹溾攢鈹€ cosmic-replay/              鈫?澶嶅埗鏁翠釜 skill
鈹?  鈹溾攢鈹€ SKILL.md
鈹?  鈹溾攢鈹€ lib/
鈹?  鈹?  鈹溾攢鈹€ __init__.py
鈹?  鈹?  鈹溾攢鈹€ replay.py
鈹?  鈹?  鈹溾攢鈹€ diagnoser.py
鈹?  鈹?  鈹溾攢鈹€ advisor.py
鈹?  鈹?  鈹溾攢鈹€ field_resolver.py
鈹?  鈹?  鈹溾攢鈹€ har_extractor.py
鈹?  鈹?  鈹斺攢鈹€ runner.py
鈹?  鈹溾攢鈹€ cases/
鈹?  鈹?  鈹斺攢鈹€ admin_org_new.yaml  鈫?鍙傝€冪敤渚嬶紙鍙€変繚鐣欙級
鈹?  鈹斺攢鈹€ docs/
鈹?      鈹溾攢鈹€ operating_guide.md
鈹?      鈹溾攢鈹€ scaling.md
鈹?      鈹溾攢鈹€ extending.md
鈹?      鈹溾攢鈹€ troubleshooting.md
鈹?      鈹斺攢鈹€ packaging.md
鈹溾攢鈹€ cosmic-login/               鈫?蹇呴』鍚屾椂鎵撳寘
鈹?  鈹溾攢鈹€ SKILL.md
鈹?  鈹溾攢鈹€ cosmic_login.py
鈹?  鈹溾攢鈹€ reference.md
鈹?  鈹斺攢鈹€ examples.md
鈹溾攢鈹€ INSTALL.md                  鈫?瀹夎璇存槑锛堣涓嬭妭妯℃澘锛?鈹斺攢鈹€ requirements.txt
```

### 2.2 `requirements.txt`

```txt
requests>=2.28
urllib3>=1.26
pycryptodome>=3.15
pyyaml>=6.0
```

### 2.3 `INSTALL.md` 妯℃澘

```markdown
# 瀹夎 cosmic-replay

## 1. 鏀剧洰褰?
鎶?`cosmic-replay/` 鍜?`cosmic-login/` 涓や釜鐩綍閮芥斁鍒?`.claude/skills/` 涓嬶細

```
浣犵殑椤圭洰/
鈹斺攢鈹€ .claude/
    鈹斺攢鈹€ skills/
        鈹溾攢鈹€ cosmic-login/
        鈹斺攢鈹€ cosmic-replay/
```

## 2. 瑁呬緷璧?
```bash
pip install -r requirements.txt
```

## 3. 閰嶅嚟璇?
```bash
export COSMIC_USERNAME=浣犵殑璐﹀彿
export COSMIC_PASSWORD=浣犵殑瀵嗙爜
export COSMIC_DATACENTER_ID=浣犵殑鏁版嵁涓績
```

## 4. 楠岃瘉

```bash
cd .claude/skills/cosmic-replay
python -m lib.runner run cases/admin_org_new.yaml
```

鐪嬪埌 `[FAIL]` + 淇寤鸿 = 瀹夎鎴愬姛锛團AIL 鏄洜涓?SIT 鏁版嵁宸紓锛宻kill 鏈韩宸ヤ綔姝ｅ父锛夈€?
## 5. 瀛︿範

璇?`docs/operating_guide.md` 寮€濮嬪綍绗竴鏉＄敤渚嬨€?```

---

## 3. 鎵撳寘鍛戒护锛堢ず渚嬶級

### Windows PowerShell

```powershell
# 鍋囪椤圭洰鏍瑰湪 D:/aiworkspace/cludecodeworkspace
$src = "D:/aiworkspace/cludecodeworkspace/.claude/skills"
$dst = "D:/dist/cosmic-replay-v1.0"

# 寤虹粨鏋?New-Item -ItemType Directory -Path $dst -Force

# 澶嶅埗涓や釜 skill
Copy-Item -Recurse "$src/cosmic-replay" $dst
Copy-Item -Recurse "$src/cosmic-login" $dst

# 娓?__pycache__
Get-ChildItem -Path $dst -Recurse -Include "__pycache__" | Remove-Item -Recurse -Force
Get-ChildItem -Path $dst -Recurse -Include "*.pyc" | Remove-Item -Force

# 鎷?requirements + INSTALL
# 锛堟墜宸ュ垱寤鸿繖涓や釜鏂囦欢锛屽唴瀹硅鍓嶉潰锛?
# 鎵?zip
Compress-Archive -Path "$dst/*" -DestinationPath "$dst.zip"
```

### Linux/Mac bash

```bash
SRC=path/to/.claude/skills
DST=/tmp/cosmic-replay-v1.0

rm -rf $DST
mkdir -p $DST
cp -r $SRC/cosmic-replay $DST/
cp -r $SRC/cosmic-login $DST/

# 娓?pyc
find $DST -type d -name __pycache__ -exec rm -rf {} +
find $DST -type f -name "*.pyc" -delete

# 鎷锋枃妗?cat > $DST/requirements.txt <<EOF
requests>=2.28
urllib3>=1.26
pycryptodome>=3.15
pyyaml>=6.0
EOF

# 鎵撳寘
cd /tmp && tar czf cosmic-replay-v1.0.tar.gz cosmic-replay-v1.0/
```

---

## 4. 鎵撳寘鍓嶇殑娓呯悊娓呭崟锛圕HECKLIST锛?
姣忔鍒嗗彂鍓?*蹇呮煡**锛?
- [ ] **鍒犳墍鏈?`__pycache__/` 鍜?`*.pyc`**
- [ ] **鍒犳墍鏈?`.har` 鏂囦欢**锛堝彲鑳藉惈瀵嗙爜/涓氬姟鏁版嵁锛?- [ ] **鍒?`cases/` 涓嬬殑鏁忔劅鐢ㄤ緥**锛堝彧鐣?`admin_org_new.yaml` 杩欑被閫氱敤鍙傝€冿級
- [ ] **妫€鏌?YAML 閲屾病鏈夌‖缂栫爜鐨勫瘑鐮?/ token**锛?  ```bash
  grep -rE "password:|token:|<your-username>" cosmic-replay/
  # 搴旇鍙湅鍒?${env:...} 褰㈠紡鐨勫紩鐢?  ```
- [ ] **妫€鏌ユ病鐣欒皟璇曚唬鐮?*锛坄print("DEBUG")` / `breakpoint()` 绛夛級
- [ ] **纭 SKILL.md 閲岀殑璺緞鏄浉瀵圭殑**锛堜笉甯?`D:/aiworkspace/`锛?- [ ] **docs/ 閲岀殑绀轰緥璺緞鑳界敤**锛堟病寮曠敤浣犵殑绉佷汉鐩綍锛?
---

## 5. 鎺ユ敹鏂圭殑绗竴娆¤繍琛?
缁欐帴鏀舵柟涓€娈佃瘽锛?
> 瑁呭ソ鍚庡厛璺戣繖涓獙璇侊細
>
> ```bash
> cd .claude/skills/cosmic-replay
> python -m lib.runner run cases/admin_org_new.yaml
> ```
>
> **棰勬湡杈撳嚭**锛氫細鐧诲綍澶辫触鎴栬窇瀹?FAIL锛岄兘姝ｅ父銆?> - 鐧诲綍澶辫触 502 鈫?绛?1 鍒嗛挓閲嶈窇
> - 璺戝畬 FAIL + 鐪嬪埌"淇寤鸿"鍧?鈫?璇存槑瑁呭ソ浜?>
> 鐒跺悗璇?`docs/operating_guide.md` 寮€濮嬪綍鑷繁鐨勭涓€鏉＄敤渚嬨€?
---

## 6. 鐗堟湰绠＄悊

### 缁?skill 鎵撶増鏈?
鍦?`SKILL.md` 鎴栨柊鍔?`lib/VERSION`锛?```
1.0.0
```

### 鍙樻洿鏃ュ織锛坄CHANGELOG.md`锛?
```markdown
# Changelog

## v1.0.0 - 2026-04-22
- 鍒濈増鍙戝竷
- 鐗规€э細鐧诲綍/鍗忚鍥炴斁/璇婃柇/淇寤鸿
- 宸查獙璇侊細SIT 鐜 (feature.kingdee.com:1026/feature_sit_hrpro)

## v1.1.0锛堣鍒掞級
- 鏀寔 ${resolve:basedata:...} 鍔ㄦ€?id 瑙ｆ瀽锛堟帴鍏?field_resolver锛?- 鏀寔 --apply 鑷姩鏀?YAML
```

### 鎺ユ敹鏂瑰崌绾?
```bash
# 澶囦唤浠栦滑鐨?cases/ 鍜?_shared/
mv .claude/skills/cosmic-replay/cases /tmp/backup_cases
mv .claude/skills/cosmic-replay/_shared /tmp/backup_shared 2>/dev/null

# 瑕嗙洊 lib/ 鍜?docs/
rm -rf .claude/skills/cosmic-replay/{lib,docs}
cp -r 鏂扮増鏈?cosmic-replay/{lib,docs} .claude/skills/cosmic-replay/

# 鎭㈠涓氬姟璧勪骇
mv /tmp/backup_cases .claude/skills/cosmic-replay/cases
mv /tmp/backup_shared .claude/skills/cosmic-replay/_shared
```

**鍘熷垯**锛歚lib/` 鍜?`docs/` 鏄?skill 鏈綋锛堝彲瑕嗙洊锛夛紝`cases/` 鏄笟鍔¤祫浜э紙涓嶈兘瑕嗙洊锛夈€?
---

## 7. 鎺掍粬鎬ф竻鍗曪紙**涓嶈**涓€璧锋墦鍖呯殑涓滆タ锛?
| 涓滆タ | 涓轰粈涔?|
|---|---|
| `*.har` 鏂囦欢 | 鍚瘑鐮併€佷笟鍔℃暟鎹?|
| 浣犵殑 cases/ 涓氬姟鐢ㄤ緥 | 鎺ユ敹鏂逛笟鍔′笉鍚?|
| `__pycache__/` | 缂栬瘧缂撳瓨锛岀洰鏍囩幆澧?Python 鐗堟湰鍙兘涓嶅悓 |
| `.env` / `.env.local` | 鍚嚟璇?|
| 鏃ュ織鏂囦欢 | 鏃犵敤涓斿彲鑳藉惈鏁忔劅 |
| `project.ini` | 浣犻」鐩殑閰嶇疆 |
| `aihr/` 鐩綍 | 浣犵殑涓氬姟浠ｇ爜 |
| 浠讳綍鍚綘鐜鍩熷悕纭紪鐮佺殑鏂囦欢 | 鍒汉鐢ㄤ笉涓?|

---

## 8. 娉曞緥/瀹夊叏娉ㄦ剰

- **鍑瘉**锛氭案杩滅敤鐜鍙橀噺锛屼笉瑕佸湪 YAML/浠ｇ爜閲屽啓姝?- **HAR**锛氫笉瑕佸叆 git 鎴栧垎鍙戝寘锛岀瓑鍚屼簬娉勯湶鐧诲綍鎬?- **瀹㈡埛鐜淇℃伅**锛歚_shared/envs/client_xxx.yaml` 鑻ュ惈鐪熷疄瀹㈡埛鏁版嵁锛屾墦鍖呭墠鍒犳帀

---

## 9. 甯歌闂

**Q锛氭帴鏀舵柟娌℃湁 Claude Code 鑳界敤鍚楋紵**  
A锛氳兘銆俿kill 鏈綋鏄?Python 妯″潡锛宍python -m lib.runner run ...` 鍦ㄤ换浣?shell 閲岄兘璺戙€侰laude Code 鍙槸"鍙戠幇 skill"鐨勪竴绉嶆柟寮忋€?
**Q锛氬彲浠ユ墦鍖呮垚 pip 鍖呭彂鍒?PyPI 鍚楋紵**  
A锛?*鍙互**浣?*涓嶆帹鑽?*銆俿kill 鍖呭惈涓氬姟閫昏緫锛堣媿绌瑰崗璁級锛屽彂鍏綉涓嶅悎閫傘€傚缓璁唴缃?pip 婧愭垨鐩存帴鐩綍鍒嗗彂銆?
濡傛灉瑕?pip 鍖栵紝`pyproject.toml` 楠ㄦ灦锛?```toml
[project]
name = "cosmic-replay"
version = "1.0.0"
dependencies = ["requests>=2.28", "urllib3>=1.26",
                 "pycryptodome>=3.15", "pyyaml>=6.0"]

[project.scripts]
cosmic-replay = "cosmic_replay.runner:main"
cosmic-replay-extract = "cosmic_replay.har_extractor:main"
```

**Q锛氬涓」鐩叡鐢ㄨ兘鍚楋紵**  
A锛氳兘銆俙.claude/skills/` 涓嶆槸蹇呴』鐨勶紝鍙互鏀句换浣曞湴鏂癸紝鍙 `COSMIC_LOGIN_SCRIPT` 鐜鍙橀噺鑳芥壘鍒?cosmic_login.py銆?
**Q锛氳兘鍦?Docker 閲岃窇鍚楋紵**  
A锛氳兘銆傚啓涓?Dockerfile锛?```dockerfile
FROM python:3.11-slim
COPY cosmic-replay /app/cosmic-replay
COPY cosmic-login /app/cosmic-login
WORKDIR /app/cosmic-replay
RUN pip install -r /app/requirements.txt
ENV COSMIC_LOGIN_SCRIPT=/app/cosmic-login/cosmic_login.py
CMD ["python", "-m", "lib.runner", "run", "cases/your_case.yaml"]
```

**Q锛氳兘璺戝湪闈?Windows 鐜鍚楋紵**  
A锛氳兘銆俿kill 鏈韩璺ㄥ钩鍙般€傚敮涓€娉ㄦ剰锛歚COSMIC_LOGIN_SCRIPT` 鐨勮矾寰勬牸寮忋€?
---

## 10. 鍒嗗彂妫€鏌ュ崟锛堟渶缁堬級

鍑嗗鍒嗗彂鍓嶏紝鎸夐『搴忓嬀锛?
- [ ] 璺戜竴閬?`cases/admin_org_new.yaml` 纭宸ヤ綔
- [ ] 鎸?鎵撳寘鍓嶆竻鐞嗘竻鍗?杩囦竴閬?- [ ] `requirements.txt` / `INSTALL.md` / `CHANGELOG.md` 瀹屾暣
- [ ] 鍦ㄤ竴鍙板共鍑€鏈哄櫒涓婃寜 `INSTALL.md` 瑁呬竴閬嶏紝楠岃瘉鑳借窇
- [ ] 鎶婂瘑鐮?鏁忔劅鏇挎崲鎴?`YOUR_USERNAME` / `YOUR_PASSWORD` 绫诲崰浣嶇
- [ ] `zip` / `tar.gz` 鎵撳寘锛岀畻 md5 鐧昏鐗堟湰

---

## 涓€鍙ヨ瘽

**鎵撳寘缁欏埆浜?= 涓や釜 skill 鐩綍 + requirements.txt + INSTALL.md**锛屽氨杩欎箞绠€鍗曘€傚洜涓烘湰 skill 闆堕」鐩緷璧栥€