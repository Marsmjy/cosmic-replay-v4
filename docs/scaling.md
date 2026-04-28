# 鍦烘櫙鎵╁睍涓庤妯″寲鎸囧崡

operating_guide.md 鏁欎綘浠?0 鍒?1 璺戦€氫竴鏉＄敤渚嬨€傛湰鏂囨暀浣犱粠 1 鍒?N 缁存姢涓€涓彲鎸佺画鐨勬祴璇曡祫浜с€?
闈㈠悜鐨勫満鏅細
- 绉疮鍒?10+ 鏉＄敤渚嬪悗鎬庝箞绠?- 璺ㄧ幆澧冿紙SIT / UAT / 瀹㈡埛鐜锛夊鐢?- 鍥㈤槦澶氫汉鍗忎綔
- 鍜?CI銆佸厓鏁版嵁鍙樻洿鐨勯厤鍚?
---

## 1. 鐢ㄤ緥鐩綍鐨勭粍缁?
闅忕潃鐢ㄤ緥澧炲锛屽钩閾烘墍鏈?YAML 浼氫贡銆傛帹鑽愭寜棰嗗煙鍒嗗瓙鐩綍锛?
```
cases/
鈹溾攢鈹€ README.md                          # 鐢ㄤ緥鎬绘竻鍗?鈹溾攢鈹€ admin_org/
鈹?  鈹溾攢鈹€ new.yaml                       # 鏂板琛屾斂缁勭粐
鈹?  鈹溾攢鈹€ modify.yaml                    # 淇敼
鈹?  鈹溾攢鈹€ disable.yaml                   # 绂佺敤
鈹?  鈹斺攢鈹€ merge.yaml                     # 鍚堝苟
鈹溾攢鈹€ employee/
鈹?  鈹溾攢鈹€ new.yaml                       # 鍛樺伐鍏ヨ亴
鈹?  鈹溾攢鈹€ transfer.yaml                  # 璋冨矖
鈹?  鈹斺攢鈹€ leave.yaml                     # 绂昏亴
鈹溾攢鈹€ smoke/                             # 鍙戠増鍐掔儫闆嗗悎锛堜粠涓婇潰鎸?5 鏉℃牳蹇冿級
鈹?  鈹斺攢鈹€ README.md                      # 鍒楀嚭鍝簺鏂囦欢鏄啋鐑?鈹斺攢鈹€ _shared/
    鈹溾攢鈹€ default_env.yaml               # 榛樿 env 鐗囨锛堝彲閫夛級
    鈹斺攢鈹€ basedata_ids.yaml              # 鍩虹璧勬枡 id 鏄犲皠
```

**鎺ㄨ崘**锛歚cases/README.md` 缁存姢涓€寮犳€昏〃锛?```markdown
| 鍦烘櫙 | 鐢ㄤ緥 | 涓婃閫氳繃 | 澶囨敞 |
|---|---|---|---|
| 琛屾斂缁勭粐鏂板 | admin_org/new.yaml | 2026-04-22 | 渚濊禆鐜鏈夌埗缁勭粐鏁版嵁 |
| 鍛樺伐鍏ヨ亴 | employee/new.yaml | 2026-04-22 | |
```

## 2. 鐢ㄤ緥鎵撴爣绛?
鍦?YAML 椤堕儴鍔?`tags`锛?
```yaml
name: admin_org_new
tags: [smoke, core, haos, mvp]
```

璺戝瓙闆嗭紙鏀?runner 鏀寔 tag 杩囨护锛屾湰 skill 褰撳墠鏈唴缃紝鍙敤 shell 鑷繁鎸戯級锛?
```bash
# 璺戞墍鏈?smoke 鏍囩
grep -l "tags:.*smoke" cases/*.yaml | xargs -I{} python -m lib.runner run {}
```

寤鸿鐨勬爣绛句綋绯伙細
- `smoke` - 鍙戠増鍓嶅繀璺戠殑鏍稿績鐢ㄤ緥锛?-10 鏉★級
- `core` - 涓绘祦绋嬶紙20-30 鏉★級
- `edge` - 杈圭晫鍦烘櫙锛堟瀬绔暟鎹€佹潈闄愯竟缂橈級
- `domain:<xxx>` - 涓氬姟鍩燂紙haos / bdm / workflow锛?
## 3. 澶氱幆澧冮厤缃紙SIT / UAT / 瀹㈡埛锛?
### 闂

鍚屼竴浠?YAML 瑕佽窇涓夌鐜锛?- 鍐呴儴 SIT锛歚https://feature.kingdee.com:1026/feature_sit_hrpro`
- 鍐呴儴 UAT锛歚https://uat.internal/ierp`
- 瀹㈡埛 ACME锛歚https://acme.kingdee.com/erp`

鍑瘉銆乥ase_url銆佸熀纭€璧勬枡 id 閮戒笉涓€鏍枫€?
### 瑙ｆ硶锛氱幆澧冨垏鐗囨枃浠?
```
cases/_shared/envs/
鈹溾攢鈹€ sit.yaml           # 鍐呴儴 SIT
鈹溾攢鈹€ uat.yaml           # 鍐呴儴 UAT
鈹斺攢鈹€ client_acme.yaml   # 瀹㈡埛 ACME
```

鍐呭鏍蜂緥锛坄sit.yaml`锛夛細
```yaml
# _shared/envs/sit.yaml
base_url: https://feature.kingdee.com:1026/feature_sit_hrpro
datacenter_id: "<your-datacenter-id>"
# 鍩虹璧勬枡 id 鍦ㄦ鐜涓嬬殑瀹為檯鍊?basedata:
  adminorgtype_company: "1020"
  adminorgtype_dept: "1030"
  changescene_new: "1010_S"
  changetype_new: "1010_S"
  default_root_org_id: "00"
  default_root_org_name: 鐜畤鍥介檯闆嗗洟鏈夐檺鍏徃
```

`client_acme.yaml`锛?```yaml
base_url: https://acme.kingdee.com/erp
datacenter_id: "3001234567890"
basedata:
  adminorgtype_company: "2001"   # ACME 鐜鐨?鍏徃"鏄?2001
  adminorgtype_dept: "2002"
  changescene_new: "2010_S"
  # ...
```

鐢ㄤ緥閲屽紩鐢紙闇€瑕?runner 鏀寔 `${basedata.xxx}`锛屽綋鍓?skill 鍙墿灞曟鍔熻兘锛?*鐭湡涓存椂鏂规**锛氱敤鐜鍙橀噺 `${env:ADMINORGTYPE_COMPANY_ID}`锛夛細

```yaml
# cases/admin_org/new.yaml
steps:
  - type: pick_basedata
    field_key: adminorgtype
    value_id: ${env:ADMINORGTYPE_COMPANY_ID}
```

**鍒囨崲鐜**锛?```bash
# 璺?SIT
source cases/_shared/envs/sit.env   # 杞崲涓?shell 褰㈠紡
python -m lib.runner run cases/admin_org/new.yaml

# 璺戝鎴?ACME
source cases/_shared/envs/client_acme.env
python -m lib.runner run cases/admin_org/new.yaml
```

### 鏇撮暱杩滐細鐢?FieldResolver 鑷姩鏌?id

skill 閲屽凡缁忔湁 `lib/field_resolver.py`锛屼絾 runner 鐩墠娌℃帴鍏ャ€傛墿灞曟柟鍚戯細

```yaml
# 鐢ㄤ緥閲屽啓锛?value_id: "${resolve:basedata:adminorgtype:鍏徃}"
```

runner 浼氳皟 `FieldResolver.resolve_basedata()` 瀹炴椂鏌ュ綋鍓嶈处鍙蜂笅"鍏徃"鐨勭湡瀹?id銆?*鎹㈢幆澧冨畬鍏ㄩ浂淇敼鐢ㄤ緥**銆?
杩欎釜鏄?skill 鐨?涓嬩竴涓噷绋嬬"鍔熻兘銆?
## 4. 瀹㈡埛鐜浣跨敤鐨勬纭Э鍔?
锛堣瑙佸墠闈㈠璇濋噷鐨?瀹㈡埛鐜鑳藉彂鐜颁粈涔堥棶棰?鍒嗘瀽锛岃繖閲岃鎿嶄綔锛?
### 棣栨鍦ㄥ鎴风幆澧冭窇

1. **鍏堝缓瀹㈡埛鐜鐨?env 鍒囩墖**锛堣涓婅妭锛?2. **璺?smoke 闆嗗悎**鍏堢湅鏁翠綋鍋ュ悍搴︼細
   ```bash
   grep -l "tags:.*smoke" cases/**/*.yaml | xargs -I{} python -m lib.runner run {}
   ```
3. **澶辫触鍒嗙被**锛?   - 鏁版嵁婕傜Щ锛坕d 鍙樹簡锛夆啋 鏀?`_shared/envs/瀹㈡埛.yaml`
   - 鐪?bug锛堟爣鍝佸湪瀹㈡埛鐜鍧忎簡锛夆啋 寤?issue 璺熻繘
   - 瀹㈡埛宸茬煡瀹氬埗锛堟瘮濡傚皯浜嗘煇瀛楁锛夆啋 鐢ㄤ緥閲屽姞 `expected_failures`

### 寤?"瀹㈡埛宸茬煡瀹氬埗" 鐧藉悕鍗?
缁欑敤渚嬪姞瀛楁锛?```yaml
name: employee_new

# 瀹㈡埛 ACME 涓撳睘锛氫粬浠笉浣跨敤绱ф€ヨ仈绯讳汉瀛楁
expected_failures:
  acme:
    - reason: "瀹㈡埛 ACME 瀹氬埗锛氱Щ闄や簡绱ф€ヨ仈绯讳汉瀛楁"
      step: fill_emergency
      expected_error: "璇峰～鍐欑揣鎬ヨ仈绯讳汉"
```

runner 鎵╁睍鐐癸細鍖归厤 `$env:CLIENT_CODE == acme` 鏃讹紝杩欐潯澶辫触绠?PASS銆?
## 5. 鍜屽厓鏁版嵁鍙樻洿鐨勯厤鍚?
浣犻」鐩殑涓荤嚎锛歀LM 瑙ｆ瀽闇€姹?鈫?鐢熸垚鍙傛暟 鈫?kapi 钀藉厓鏁版嵁銆?*Replay 鏄?kapi 钀藉畬鍏冩暟鎹悗鐨勫洖褰掑畨鍏ㄧ綉**銆?
### 鎺ㄨ崘宸ヤ綔娴?
```
闇€姹?鈫?LLM 鐢熸垚 鈫?kapi 钀藉厓鏁版嵁
              鈫?        璺?Replay 鍏ㄩ儴鐢ㄤ緥
              鈫?     鏈?FAIL锛?       鈹溾攢 鏄?鈫?鐪嬩慨澶嶅缓璁?鈫?淇厓鏁版嵁 or 鏀圭敤渚?鈫?鍐嶈窇
       鈹斺攢 鍚?鈫?鍚堝苟涓婄嚎
```

### 鍏稿瀷鍦烘櫙

**鍦烘櫙 A锛氱粰鍛樺伐妗ｆ鍔犲瓧娈?*
- kapi buildMeta 鍔犲畬瀛楁鍚?- 璺?`cases/employee/*.yaml`
- 濡傛灉 FAIL 浜嗭紝澶氬崐鏄柊瀛楁鍙樻垚浜嗗繀濉絾鏍囧搧鐢ㄤ緥娌″～ 鈫?鏀圭敤渚嬫垨鏀瑰厓鏁版嵁 mustInput

**鍦烘櫙 B锛氭敼瀛楁鏍￠獙瑙勫垯**
- kapi addRule 鍔犲畬瑙勫垯鍚?- 璺戠浉鍏崇敤渚?- 鏂拌鍒欎細鎷︽埅 save 鈫?advisor 浼氭竻妤氭姤鍑?XXX 涓嶅悎娉?

**鍦烘櫙 C锛氬垹瀛楁 / 鏀?key**
- kapi modifyMeta 鍒犲瓧娈靛悗
- 璺戠敤渚嬩細 FAIL锛堝瓧娈典笉瀛樺湪锛?- 鎻愰啋浣狅細鎵€鏈夋秹鍙婅瀛楁鐨?YAML 閮借鏀?
## 6. 鐗堟湰绠＄悊涓庡彉鏇?
### 鐢ㄤ緥鍏ュ簱鐨勫師鍒?
```bash
# 濂界殑 commit
git add cases/admin_org/new.yaml cases/_shared/envs/sit.yaml
git commit -m "add admin_org_new case and ACME env config"

# 鍧忕殑 commit
git add cases/admin_org/new.yaml *.har    # HAR 鍒叆搴擄紒澶т笖鍚晱鎰?git add *.log                              # 鏃ュ織鍒叆搴?```

**`.gitignore` 鎺ㄨ崘**锛?```
*.har
*.log
__pycache__/
.env
.env.local
```

### 鐢ㄤ緥澶辨晥鐨勫鐞?
鏌愬ぉ璺?`cases/employee/new.yaml` 绐佺劧涓€鐩?FAIL锛?
1. 鍏堢湅 advisor 鎻愮ず
2. 鎵嬪伐鐐逛竴閬嶆祻瑙堝櫒锛氭槸鏍囧搧鐪熷潖浜嗚繕鏄閮ㄦ暟鎹彉浜嗭紵
3. **鐪?bug** 鈫?寤?issue锛岀敤渚嬩繚鐣欙紙鏍囪 `expected_failures: yes`锛?4. **澶栭儴鏁版嵁鍙樹簡** 鈫?鏀?`_shared/envs/` 鎴栫敤渚?
**绂佹**锛氶殢渚?delete 鐢ㄤ緥銆備竴涓豢鐢ㄤ緥鏄暟鍗佸垎閽熺殑鎶曞叆锛屼笉瑕佷涪銆?
## 7. 鍥㈤槦鍗忎綔

### 鏂囦欢鎵€鏈夋潈

澶у洟闃熸椂锛?```
cases/admin_org/      鈫?@org-team
cases/employee/       鈫?@hr-team
cases/workflow/       鈫?@workflow-team
lib/                  鈫?@replay-skill-maintainer
```

鍔?CODEOWNERS 鏂囦欢锛?```
# .github/CODEOWNERS
/.claude/skills/cosmic-replay/cases/admin_org/ @org-team
/.claude/skills/cosmic-replay/lib/             @replay-skill-maintainer
```

### 鏂颁汉鍏ヨ亴

缁欎粬涓€浠?3 姝ユ寚寮曪細
1. 璇?operating_guide.md
2. 鎷夸粬璐熻矗鐨勯鍩熺殑涓€鏉＄敤渚嬪綋鍙傝€?3. 褰曡嚜宸辩殑绗竴涓?HAR锛岃窇閫?
**骞冲潎 1 灏忔椂鑳藉啓鍑虹涓€鏉＄豢鐢ㄤ緥**銆?
## 8. 鎬ц兘涓庤妯?
褰撳墠 skill 鑳藉姏锛?- 涓€鏉＄敤渚嬶細3-5 绉?- 100 鏉′覆琛岋細5-8 鍒嗛挓
- CI 鍚堢悊

**鎵╁睍鍒?500+ 鐢ㄤ緥**鏃惰€冭檻锛?- 骞惰璺戯紙涓嶅悓 session / 涓嶅悓璐﹀彿锛?- 鐢ㄤ緥鍒嗙粍锛屾寜鍏宠仈搴﹁窇瀛愰泦
- 澶辫触 3 娆¤嚜鍔ㄩ噸璇曪紙缃戠粶鎶栧姩锛?
杩欎簺鏄?runner 鐨勬墿灞曠偣锛屽綋鍓嶇増鏈湭鍐呯疆銆?
## 9. 鍜屽叾浠栧伐鍏风殑閰嶅悎鐭╅樀

| 宸ュ叿 | 瑙掕壊 | 閰嶅悎 |
|---|---|---|
| **kapi** | 鍏冩暟鎹惤鍦帮紙璁捐鏈燂級 | 鍏冩暟鎹敼瀹?鈫?璺?Replay |
| **Replay**锛堟湰宸ュ叿锛?| 杩愯鏈熶笟鍔℃祦鍥炲綊 | 鏍稿績 |
| **Playwright** | UI 瑙嗚鍥炲綊 | 鍙€夛紝鍙戠増鍓?2-3 鏉℃牳蹇冩祦 |
| **鎵嬪伐鐐?* | UAT 楠屾敹 | 鍙戠増鍓嶆渶鍚庡厹搴?|
| **cosmic-dev skill** | 甯綘鏌ュ瓧娈?key銆佺敓鎴愭彃浠?| 鍐欑敤渚嬮亣鍒版湭鐭ュ瓧娈垫椂闂畠 |

## 10. 甯歌璇尯

**鉂?鐢ㄤ緥瓒婂瓒婂ソ**  
鈫?鐪熸鐨勪环鍊煎湪"楂橀鍥炲綊鍦烘櫙"銆?0 鏉＄簿閫?> 50 鏉′綆璐ㄩ噺銆?
**鉂?杩芥眰 100% 閫氳繃鐜?*  
鈫?鏌愪簺 FAIL 鏄鏈熺殑锛堝鎴峰凡鐭ュ畾鍒讹級銆傜敤 `expected_failures` 绠＄悊銆?
**鉂?鐢ㄤ緥渚濊禆涓婁竴鏉＄殑鏁版嵁**  
鈫?姣忔潯鐢ㄤ緥搴旇**鐙珛**銆傛瘡娆￠兘鐢ㄩ殢鏈虹紪鍙枫€佷粠澶村缓鏁版嵁銆傚惁鍒欒窇澶辫触涓€鏉℃暣閾炬柇銆?
**鉂?鍦ㄧ敤渚嬮噷纭紪鐮佸瘑鐮?*  
鈫?姘歌繙鐢?`${env:...}`銆俌AML 鏄細鍏?git 鐨勩€?
**鉂?鎶?HAR 鍏?git**  
鈫?澶ぇ + 鍚晱鎰熴€傜敤渚嬬敓鎴愬畬灏卞垹鎴栧瓨 HAR 褰掓。鐩綍锛坄.gitignore`锛夈€?
**鉂?蹇借 advisor 鐨?low 缃俊搴?*  
鈫?褰?advisor 缁欎笉鍑洪珮缃俊搴︽椂锛屽線寰€鏄湁涓氬姟瑙勫垯瀹冧笉鐭ラ亾銆傝姳 5 鍒嗛挓鏌?HAR 姣旂‖鐚滄晥鐜囬珮銆?
---

## 鎺ㄨ崘鐨勬垚鐔熻矾寰?
**绗?1 鍛?*锛氳窇閫?3-5 鏉℃渶鏍稿績鍦烘櫙
**绗?1 鏈?*锛氱Н绱?15-20 鏉′富娴佺▼鐢ㄤ緥
**绗?2 鏈?*锛氭帴 CI锛屾瘡娆?PR 鑷姩璺?**绗?3 鏈?*锛氳鐩栧鎴风幆澧冿紝寤?`_shared/envs/` 鏄犲皠
**鎸佺画**锛氭瘡娆″彂鐗堝墠璺?smoke锛屾瘡娆″厓鏁版嵁鏀瑰姩璺戠浉鍏崇敤渚