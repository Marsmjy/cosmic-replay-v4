# 知识库消化摘要（Step 1 产出）

> 目标：让 `har_extractor.py` 的 yaml 生成链路**以知识库为第一信源**，多领域 HAR 导入**一次生成对**。

---

## 一、知识库真实盘点

### A. cosmic-hr-expert/knowledge/ （领域专家，558 场景）

| 文件 | 大小 | 对 yaml 生成的真实价值 |
|---|---|---|
| `_cloud_index.json` | 10KB | **212 场景 → 5 云**（hr_hrmp/org_dev/core_hr/payroll/attendance），每云 scenes 数组就是 form_id 清单 ⭐ |
| `_cross_cloud_index.json` | 94KB | **283 实体**的 `ownedCloud`/`ownedScene`/被引用次数 ⭐ |
| `_intent_routing.json` | 41KB | 78 业务意图 → 候选场景，主要给 LLM 场景定位用，**对 HAR 解析间接** |
| `_antipatterns.json` | 18KB | 27 条 **ISV 代码反模式**（AP-001~027），**对 HAR 解析无关**（是给 codegen 用的）|
| `_scene_relations.json` | 42KB | 场景关系/资产索引，**与 HAR 解析间接** |
| `scenarios/<form>/scenario.json` | ~1KB×400 | **金矿**：`name` 业务名 + `domain` 业务域 + `menu_paths` 菜单路径 ⭐⭐ |
| `scenarios/<form>/scene_doc_lite.json` | ~5-7KB×400 | **金矿**：`mainEntity` + `physicalTable` + **`fields[]` 字段元数据**（type/req/lk/mf/ref） ⭐⭐⭐ |
| `scenarios/<form>/rules_chain_all.json` | ~100KB | opKeys 执行链 + mines（反模式），**对 HAR 解析价值低** |

### B. cosmic-replay-troubleshooter/ （本项目自己的排故手册）

| 文件 | 对 yaml 生成的真实价值 |
|---|---|
| `SKILL.md` | **权威规则书** ⭐⭐⭐：变量识别三清单（UNIQUE_KEY_HINTS/ENV_RELATED_FIELDS/ENUM_FIELDS）+ `_CLASSIFY_KEY_EXCLUSIONS` + `_SAVE_BUTTON_KEYS` + `_pending_by_app` 链路 4 层修复 |
| `references/assertion-blindspots.md` | **断言选择规则** ⭐：save 用 `no_save_failure`，非 save 用 `no_error_actions` |
| `references/pageid-chain-debugging.md` | **pageId 4 级来源 + 4 层修复** ⭐ |

---

## 二、单场景文件能提供什么（以 `haos_adminorgdetail` 为例）

**`scenario.json`** —— 一次读完直接覆盖主表单命中：
```json
{
  "name": "组织快速维护（行政组织详情）",
  "domain": "组织管理",
  "menu_paths": [{"menuId": "1443450410974114816", "path": ["行政组织维护","...","组织快速维护"]}]
}
```

**`scene_doc_lite.json#fields[]`** —— 字段级元数据驱动变量三档：
```
{n:"number",   t:"TextField",    req:1, lk:1}                  → A档（唯一键·必变量化）
{n:"name",     t:"MuliLangTextField", req:1, lk:1}             → A档（名称·必变量化）
{n:"adminorgtype", t:"BasedataField", ref:"haos_adminorgtype"} → B档（基础资料·字面量保留）
{n:"enable",   t:"BillStatusField", mf:"变更会级联影响下游"}    → B档（枚举·字面量）
{n:"creator",  t:"CreaterField",  lk:1}                        → 忽略（系统字段）
```

---

## 三、知识库驱动的 5 节点映射（覆盖核心目标）

| 节点 | 现状 | 改造后（知识库驱动） | 数据源 |
|---|---|---|---|
| **① 主表单 & 业务域识别** | 35 条内置字典 + 490 条 formNumber | 先查 `scenarios/{form_id}/scenario.json#name+domain+menu_paths` → 212 场景直接命中；再查 `_cloud_index.json`；再回落 `_cross_cloud_index.json`；再回落内置字典；再启发式 | scenario.json + _cloud_index + _cross_cloud_index |
| **② 变量三档识别** | 仅抓 test_number / test_name + 几个白名单 | 查 `scene_doc_lite.json#fields[]`：<br>- `TextField` + `req=1` + key∈UNIQUE_KEY_HINTS → **A档**<br>- `BasedataField` + `ref` → **B档**（字面量保留·变量面板展示）<br>- `ComboField`/`BillStatusField` → **B档**（枚举字面量）<br>- `lk=1` 或 `mf` 含"系统字段" → **忽略**<br>- 响应回传字段（processInstId/pkValue/fid）→ **C档** | scene_doc_lite.json + troubleshooter SKILL.md 三清单 |
| **③ 写库锚点识别** | 白名单 saveandeffect/save/submit | 保持现有白名单 + 强化：`ac=click && key∈_SAVE_BUTTON_KEYS → tier=core`（防 btnsave 被降级） | troubleshooter SKILL.md 模式 C |
| **④ 生成反模式自检** | 无 | 生成后扫 yaml：<br>- saveandeffect 是否被 optional？（troubleshooter 模式 C-1）<br>- pageId 链路是否从 menuItemClick 开始？（pageid-chain 类型 1）<br>- 入库类用例缺 pick_basedata 必填字段？（模式 C-2）| troubleshooter SKILL.md 五模式 + pageid-chain-debugging.md |
| **⑤ 断言编排（入库确认）** | 统一 no_error + no_save_failure | 按步骤类型精选：<br>- save 步骤 → `no_save_failure`<br>- 其他 → `no_error_actions`<br>- 入库类用例追加 `response_pkValue_not_empty`<br>- 流程类用例追加 `flow_started`（响应含 processInstId）| assertion-blindspots.md + scene_doc_lite#physicalTable |

---

## 四、新增节点 ⑥：未命中回流（沉淀知识库）

任何节点走启发式兜底时 → 写入候选文件：
- `knowledge/_unknown_forms.jsonl` — form_id 未在 212 场景里
- `knowledge/_unknown_fields.jsonl` — 字段未在 scene_doc_lite.fields[] 里
- `knowledge/_unknown_intents.jsonl` — 主表单无法判定业务意图

下次导入同类 HAR → 人工审核后合入正式场景目录 → 命中率单调上升。

---

## 五、对"核心目标"的映射验证

| 核心目标子项 | 知识库驱动后的落地 |
|---|---|
| 多领域 HAR 一次生成对 | 212 场景直接覆盖 5 大云 form_id；命中率从 1/4 升到 ≥80% |
| 变量识别三档准确 | `scene_doc_lite.fields[].t/req/ref/lk/mf` 元数据驱动，不再拍脑袋 |
| 执行无误 | pageId 链路自检（节点④）先从源头避免空 `[]` 无声失败 |
| 数据真的入库 | 入库类用例自动加 `pkValue_not_empty` + 物理表提示（节点⑤）|
| 人工介入易用 | B 档字面量在变量面板集中展示；未命中项自动沉淀知识库，下次即命中 |

---

## 六、与原 Step 2 计划的差异

| 项 | 原计划 | 修正后 |
|---|---|---|
| 主表单识别数据源 | `_cross_cloud_index.json` | 加上 `scenarios/<form>/scenario.json`（更准）|
| 变量三档数据源 | `_intent_routing.json` | 换成 `scene_doc_lite.json#fields[]`（字段级元数据才是正解）|
| 写库锚点识别 | `_intent_routing.json` 路由 | 换成 troubleshooter SKILL.md 的 `_SAVE_BUTTON_KEYS` 规则 |
| 反模式规避 | `_antipatterns.json`（AP-001~027）| 换成 troubleshooter 模式 A-F（cosmic-hr-expert 的 antipatterns 对 HAR 解析无关）|
| 断言编排 | scenarios 模板 | 换成 `assertion-blindspots.md` 规则 + `scene_doc_lite#physicalTable` |
| 新增 | —— | ⑥ 未命中回流管道（让知识库随导入自演进）|

---

## 七、Step 2 实施清单（可直接开工）

1. **新增 `lib/kb_loader.py`**：一次性加载 scenarios/ 索引（form_id → scenario.json + scene_doc_lite.json 的摘要），懒加载单场景字段清单
2. **改造 `har_extractor.py` 5 处**：
   - `_resolve_form_name()` 接入 scenarios 场景名
   - 新增 `_classify_field_from_kb(form_id, field_key)` 返回 `A/B/C/ignore`
   - `detect_var_placeholders()` 按新分类器走
   - `_build_case_description()` 带上 `domain` + `menu_paths` 首尾
   - 生成完成后调 `_check_yaml_antipatterns(yaml_steps)` 输出风险段
3. **新增 `_write_unknown_*.jsonl` 回流管道**
4. **改造断言生成器**：按步骤类型 + 用例模式选断言

---

**Step 1 完成，请决定是否开工 Step 2。**
