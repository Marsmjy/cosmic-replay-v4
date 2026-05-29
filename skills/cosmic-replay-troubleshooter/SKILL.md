---
name: cosmic-replay-troubleshooter
description: Cosmic Replay 执行故障排查与诊断专家。Use when an AI Agent needs to diagnose Cosmic Replay HAR import, YAML generation, pageId chain, target_forms, pick_fields, save/submit failure, PASS but not written to DB, AI evidence package, variable parsing, environment field override, retry safety net, or Kingdee Cosmic replay execution issues.
---

# Cosmic Replay 故障排查诊断

快速定位 Cosmic Replay 用例执行失败的根因，面向 AI Agent 精确到代码位置与修复步骤。

## 先读原则

外部顾问、Qoder Work、Codex、Kiro、WorkBuddy 或任何新 AI Agent 接手本项目时，先阅读：

- `references/external-consultant-handoff.md`：外发交接、支持边界、已验证场景、禁止动作。
- `references/pageid-chain-debugging.md`：pageId 链路优先排障原则。
- `references/assertion-blindspots.md`：PASS 但入库未验证、断言盲区排查。

---

## 一、整体防护架构

### 三层防护总览

```
步骤准备 → [第1层: 预验证] → [第2层: auto-open 补偿] → invoke 调用 → [第3层: 安全网重试]
               │                      │                                    │
               ├─ pageId缺失→open+load  ├─ form_id不在page_ids→open_form     ├─ 可重试错误→pop+open+load+重试
               ├─ L2误用→降级为L3        └─ 排除: open_form/sleep             ├─ 不可重试(业务)→中止
               └─ 未load→预热loadData                                        └─ 超限(2次)→输出原始错误
```

### 第1层：预验证 (`_validate_pageid_before_invoke`)

**位置**: `lib/runner.py` 行 336-400

**触发时机**: 每个 form_id 首次被 invoke 使用（通过 `ctx["_validated_forms"]` 集合去重）

**排除操作**（不做预验证，避免递归/无意义校验）:
- `loadData` / `open_form` / `close_form` / `startupflow` / `doconfirm` / `afterConfirm`
- `method == "itemClick"`（toolbar 操作）

**四种场景处理**:

| 场景 | 触发条件 | 恢复动作 |
|------|----------|----------|
| 1: pageId 缺失 | `form_id not in replay.page_ids` | `open_form(lazy=False)` + `load_data()` |
| 2: L2 pageId 误用于非 toolbar 操作 | `_is_l2_pageid(pid) and not _is_toolbar` | 优先从 `_pending_by_app[app_id]` 取 L3；不可用则 `open_form` 获取新 L3 |
| 3: 有 pageId 但未 loadData | `form_id not in replay._loaded_forms` | 预热 `load_data()` |
| 4: 预验证异常 | open/load 抛异常 | 记 warning，由安全网兜底 |

### 第2层：auto-open 补偿（主循环内）

**位置**: `lib/runner.py` 行 923-939

**逻辑**: 执行每个步骤前，若 `_target_form not in replay.page_ids` 且步骤非 `open_form`/`sleep`，自动调 `replay.open_form(form_id, app_id, lazy=False)` 补偿。

**与第1层区别**: 第1层仅对首次使用的 form_id 触发一次；此层每步都检查，覆盖中途 pageId 被 pop 的情况。

### 第3层：安全网重试 (`invoke-retry`)

**位置**: `lib/runner.py` 行 942-1003

**可重试错误模式**（字符串子串匹配）:
```python
_RETRYABLE_ERRORS = (
    "页面未初始化或者已经过期",
    "获取缓存连接客户端失败",
    "请求超时",
    "NullPointerException",
)
```

**恢复流程**:
1. `replay.page_ids.pop(form_id, None)` — 清除过期 pageId
2. `replay.open_form(form_id, app_id, lazy=False)` — 申请新 pageId
3. `replay.load_data(form_id, app_id)` — 初始化表单数据
4. 重新执行原 handler

**约束参数**:
- 最大重试: `_INVOKE_MAX_RETRIES = 2`
- 退避策略: `min(2^retry_count, 4)` 秒 → 2s, 4s
- open_form 失败 → `break`（防死循环）
- 业务逻辑错误（不匹配上述4模式）→ 不重试，直接跳出

**SSE 事件**: 重试时推 `retry` 事件 `{step_id, attempt, error}`

---

## 二、PageId 四层跃迁模型

| 层级 | 格式 | 来源 | 生命周期 |
|------|------|------|---------|
| L0 | `root{32hex}` | `init_root()` → `sess.root_page_id` | 整个会话 |
| L1 | `{32hex}` | `open_portal()` | 门户切换前 |
| L2 | `{数字menuId}root{32hex}` | menuItemClick 后计算 | 菜单导航切换前 |
| L3 | `{32hex}` (纯32字符hex) | `open_form()`/getConfig | save/submit 后失效 |

**查找优先级**: 显式指定 > `_pending_by_app[app_id]` > `page_ids[form_id]` > `root_page_id`

**L2 判定** (`lib/replay.py` 行 43-50):
```python
_L2_PATTERN = re.compile(r'^\d+root[0-9a-f]{32}$')
def _is_l2_pageid(pid: str) -> bool:
    return bool(_L2_PATTERN.match(pid))
```

**过期启发式** (`lib/replay.py` 行 297-309 `_is_pageid_likely_stale`):
- 无 pageId → 必定无效
- L2 pageId 绑定到未 loadData 的表单 (`form_id not in _loaded_forms`) → 可能过期

**关键生命周期规则**:
- save/submit 后 pageId 失效，框架自动 pop（除非 `keep_page=true`）
- menuItemClick 后自动计算 L2: `f"{menuId}root{session.root_base_id}"`
- `_pending_by_app`: addVirtualTab 响应中按 app_id 缓存待消费 pageId
- L2 不是错误本身。列表、树、工具栏和 `addnew` 前置桥接步骤依赖 L2 上下文；进入真实编辑页后才应切换到 L3。
- 很多保存字段保存在 pageId 对应的服务端模型里。排障时先比对 HAR 原始 pageId 链路，再看字段解析和补偿；不要一上来硬补 `save` 请求体。
- 深链路样本经验见 `tests/fixtures/deep_chain_factory/catalog.json`。例如 `薪资核算 / 薪酬项目类别` 的正确闭环是 `menuItemClick` 绑定 L2、`new` 保留 L2、`showForm/loadData` 切 L3、再执行 `update_fields`、`pick_basedata(taglevel)` 和弹窗 `btnsave`。Playwright UI 填框没有触发 `updateValue/save` 时，应改用协议 YAML 验证，不要误判为 parser 失败。
- 如果 `showForm` 返回的是 `bos_list` 但带 `billFormId`（如 `hsas_retroreason`），诊断时要确认 L2 已绑定到业务表单别名；否则列表 load/new 可能拿不到正确 pageId。
- `薪资核算 / 薪酬项目` 已验证闭环：`salaryitemtype` 是必填 lookup，应通过 `getLookUpList` 预热并按名称自动解析；`ispayoutitem` 是 ComboField，应作为 enum 环境字段写入 `update_fields`；保存使用标准 `ac=save/key=tbmain/args=[bar_save, save]`。`createorg/datatype/dataprecision/dataround` 等 loadData 默认值属于 pageId 上下文，不要硬补 save。
- `薪资核算 / 薪资核算场景` 已验证写入闭环：保存提示“规则分组/常用筛选至少一行”时，pageId 链路通常已正常，缺的是 F7 子窗口回填链。正确链路是维护 `country` 后点击 `labelap4` 打开 `hsas_salarycalcstyle` F7，用 `select_f7_list_row` 按编码/名称选中算发薪方式并点击 `btnok`，确认响应回填 `groupcontent/entryentity` 后再保存；仅补选 `callistrule` 不会生成筛选行。
- `基础资料-受控-变动原因` 已验证闭环：原始 HAR 通过 `homs_apphome/treeMenuClick` 建立树菜单 L2，但 API replay 可能无法重建 apphome shell。若保存提示“请按要求填写创建组织”，先检查是否从 HAR 的列表 `createorg_id` 和新增态 `loadData` 提取了 `createorg/ctrlstrategy` 默认上下文；`createorg` 要用内部 Long id 写 `update_fields`，`ctrlstrategy` 要解析 ComboField 编码/中文并暴露为环境字段，不要硬补 `save.post_data`。
- 深链路样本排障完成后，运行 `scripts/deep_chain_pipeline.py scenario-report` 生成脱敏闭环报告，把 HAR 链路画像、YAML smoke、失败分类和入库验证策略沉淀到经验库。若执行 PASS 但只有“保存成功”提示，应先运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>` 生成推荐查询表单和业务键，再把 `suggested_assertion` 补为 `readback_by_business_key` 只读断言；不能把 PASS 直接当作入库已验证。
- 新 HAR 或失败证据包若包含 `experience_matches`，先看命中的已闭环样本和 `reusable_lessons`。也可运行 `scripts/deep_chain_pipeline.py match-experience --case <yaml> --har <har>`，按结构特征匹配成功经验。匹配结果只用于排障优先级，仍必须回到 HAR 原始 pageId 与回放 pageId 比对，不能因为命中样本就硬补 save 字段。
- 若需要把新 HAR 从导入到执行串起来，优先用 `scripts/deep_chain_pipeline.py run-scenario`。默认模式只生成 HAR 画像、YAML、经验匹配、入库回查计划和报告；只有明确带 `--run-smoke --confirm-write YES_GENERATE_TEST_DATA` 才允许写入测试数据。AI 修复时应检查 `pipeline.status`、`baseline_candidate` 和 `next_actions`，不要绕过该流水线直接改已通过样本。
- 若新 HAR 在 Web UI 生成时已勾选“附加入库回查断言”，优先检查 `assertions` 中的 `readback_by_business_key` 是否使用正确 `form_id/app_id/field_key/value`。若断言失败，不要改 save 包体；先确认业务键是否被用户修改、列表查询是否需要额外组织/状态过滤、pageId 是否仍在正确 L2/L3 链路。
- 需要补录复杂 UI 组件 HAR 时，用 Playwright 深层动作计划；`click_selector/press/select_option` 默认视为写操作，必须带 `YES_GENERATE_TEST_DATA`，填写值可用 `${timestamp}`、`${today}`、`${rand:N}`，原始 HAR 仍只能放在 ignored 目录。

---

## 三、target_forms 机制（HAR 导入阶段）

### 问题背景

苍穹菜单导航中，menuItemClick 创建 L2 pageId，多个子表单在同一导航上下文中共享此 L2。若 runner 不知道哪些表单共享 L2，会在 invoke 时为子表单使用错误的 pageId。

### 规则13：自动检测

**位置**: `lib/har_extractor.py` 行 2013-2069

**检测流程**:
1. 找到首个 `ac=menuItemClick` 步骤，提取 `menuId`
2. 绑定 `target_form = main_form`
3. 计算 L2 前缀: `l2_prefix = f"{menuId}root"`
4. **方案A（精确）**: 扫描后续步骤的 `_har_page_id`，前缀匹配 `l2_prefix` 的非主表单 → 加入 `target_forms_set`
5. **方案B（兜底）**: 若方案A无结果，取 menuItemClick 后紧跟的 `loadData` 步骤（`form_id ≠ main_form`，在下一个 `open_form`/`menuItemClick` 之前）
6. 输出: `cleaned[menu_idx]["target_forms"] = sorted(target_forms_set)`

### runner 消费 target_forms

menuItemClick 执行时，runner 为 `target_forms` 列表中所有表单共享 L2 pageId，即 `page_ids[sub_form] = L2_pid`。

### 诊断方法

```bash
# 检查 YAML 是否含 target_forms
grep "target_forms" cases/xxx.yaml
# 应在 menuItemClick 步骤看到
```

若缺失：重新导入 HAR → 检查 HAR 中是否有 `_har_page_id` 前缀匹配。

---

## 四、变量体系与解析

### 三档变量

| 档位 | 含义 | 典型字段 | 处理方式 |
|------|------|----------|----------|
| A档（必变） | 每次必须不同 | number/code/name | 变量化 `${vars.test_number}` |
| B档（基础资料） | 环境相关 | org/position/country | 保留字面量，pick_fields 标记 env_sensitive |
| C档（响应回传） | 跨步引用 | pkValue/processInstId | `${resp.step_id.path}` 引用 |

### 变量解析流程

```
vars_ns 初始化 (runner.py 行 800-804)
  → 每步: step = resolve_vars(raw_step, vars_ns) (行 865)
    → date pick_fields 后置注入 (行 867-888)  ← 防 ${today} 覆盖用户自定义日期
```

### date pick_fields 后置注入（runner.py 行 867-888）

**目的**: 防止 `resolve_vars` 展开 `${today}` 后覆盖用户在 pick_fields 中指定的日期值。

**机制**:
1. 仅对 `type == "update_fields"` 步骤生效
2. 从 `case["pick_fields"]` 读取所有 `date_` 前缀的条目
3. 去掉 `date_` 前缀得到 `field_key`
4. 用 pick_fields 的 value 强制覆盖 resolve_vars 后的字段值
5. 支持多语言 dict (`{zh_CN: ..., en: ...}`) 和纯字符串两种格式

### UNIQUE_KEY_HINTS（变量识别）

**位置**: `lib/har_extractor.py` 行 788-789

```python
UNIQUE_KEY_HINTS = {"number", "code", "simplename", "name", "fullname", "billno", "orderno"}
```

匹配这些字段名的值会被自动变量化，防止第二次运行"数据已存在"。

---

## 五、故障诊断手册

### 快速诊断决策树

```
执行失败
├─ 错误含"页面未初始化或者已经过期"?
│   ├─ 日志有 [invoke-retry]? → 安全网已触发
│   │   ├─ 重试后成功? → 正常，瞬态问题
│   │   └─ 重试后仍失败? → open_form 返回空 → 检查环境连通性
│   └─ 日志无 [invoke-retry]? → 错误未匹配 _RETRYABLE_ERRORS → 检查错误文本精确内容
│
├─ save 返回空 [] 无报错?
│   ├─ page_ids 中的值匹配 ^\d+root[0-9a-f]{32}$? → L2 屏蔽问题 (类型B)
│   ├─ YAML 中 menuItemClick 无 target_forms? → 重新导入 HAR (类型C)
│   └─ saveandeffect 被标 optional? → 改 tier: core
│
├─ 字段值不对?
│   ├─ 日期字段? → 检查 date pick_fields 后置注入 (类型D)
│   ├─ 基础资料 ID? → 检查 config/envs/*.yaml 环境配置
│   ├─ 描述/备注仍是硬编码? → 检查 _TEXT_VARIABLE_KEYS + MetadataResolver + cosmic-hr-expert shared entity_metadata
│   └─ 硬编码值? → 检查 _classify_key_heuristic 是否覆盖该字段名
│
├─ "数据已存在" / "名称重复"?
│   └─ 字段名不在 UNIQUE_KEY_HINTS → har_extractor 未变量化 → 手动添加变量
│
├─ 登录失败?
│   └─ 检查 config/envs/*.yaml → username/password/datacenter_id/base_url
│
└─ 其他业务错误?
    └─ 查 advisor 修复建议 (result.fixes) → 按建议修改 YAML
```

### 类型V：HAR 导入变量遗漏

**症状**：导入 YAML 中字段值仍在 `post_data` 或 `fields` 中硬编码，例如：

```yaml
post_data:
  - description:
      fieldKey: description
  - [{"k": "description", "v": {"zh_CN": "aaaaaa", "zh_TW": "aaaaaa"}, "r": -1}]
```

**正确结果**：

```yaml
vars:
  test_description: aaaaaa
vars_labels:
  test_description: 描述
post_data:
  - description:
      fieldKey: description
  - [{"k": "description", "v": {"zh_CN": "${vars.test_description}", "zh_TW": "${vars.test_description}"}, "r": -1}]
```

**诊断步骤**：
1. 检查 `preview.metadata_status`：`online` 表示已尝试调用 `/metadata/getEntityType.do?entityId=...`。
2. 检查 `lib/har_extractor.py` 的 `detect_var_placeholders(..., meta_resolver=...)` 是否收到 resolver。
3. 检查字段是否命中 `_TEXT_VARIABLE_KEYS` 或 `_classify_key_heuristic()`。
4. 检查 `lib/kb_loader.py resolve_scene(form_id)` 是否能命中 `skills/cosmic-hr-expert/knowledge/_shared/_standard_metadata/entity_metadata/<form_id>.md`。
5. 若元数据命中但仍未变量化，补充对应字段分类测试到 `tests/unit/test_har_extractor.py` 和 HAR 回归测试。

**修复原则**：
- 自由输入文本字段抽到 `vars`，但不强制随机化。
- 唯一字段抽到 `vars`，必须带 `${rand:N}` 或 `${timestamp}`。
- 基础资料/枚举抽到 `pick_fields`，不要混入普通 `vars` 面板。

### 类型A：pageId 过期/缺失

**症状**: `"页面未初始化或者已经过期"` / save 返回空 / ProtocolError

**诊断步骤**:
1. 搜日志中 `[pre-validate]` → 确认预验证是否触发
2. 搜日志中 `[invoke-retry]` → 确认安全网是否触发
3. 搜 `检测到可重试错误` → 确认错误是否匹配 `_RETRYABLE_ERRORS`
4. 若安全网触发但恢复失败 → 检查 open_form 返回值（是否为空/异常）

**修复方案**:

| 场景 | 原因 | 修复 |
|------|------|------|
| 新错误模式未被安全网覆盖 | 错误文本不在 `_RETRYABLE_ERRORS` | 在 `runner.py` 行 944-949 添加新模式 |
| open_form 恢复失败 | 网络/服务端异常 | 检查环境连通性 |
| 预验证未触发 | 操作在排除列表 `_skip_actions` 中 | 确认 ac 是否确实需要排除 |
| save 后 pageId 未 pop | 缺少 `invalidate_pages` 配置 | 在 save 步骤添加 `invalidate_pages: [form_id]` |

### 类型B：L2 pageId 屏蔽问题

**症状**: save 返回空 `[]`，且 `page_ids[form_id]` 匹配 `^\d+root[0-9a-f]{32}$`（L2 格式）

**根因**: menuItemClick 后 L2 pageId 被设入 `page_ids[form_id]`，后续 save/submit 等需要 L3 的操作使用了列表态 L2

**诊断**:
1. 检查 `replay.page_ids[form_id]` 长度是否 > 32 字符
2. 检查 `replay._pending_by_app[app_id]` 是否有可用 L3
3. 确认预验证场景2是否正确降级（搜日志 `L2降级→L3`）

**修复**: 预验证层已处理（runner.py 行 373-391）。若仍发生：
- 检查 `_is_l2_pageid()` 判定是否正确匹配该 pageId
- 确认 `_pending_by_app` 中是否有对应 app_id 的 L3

### 类型B-2：L2/L3 过早替换导致服务端模型丢失

**症状**:
- 录制过程正常，但回放保存时报业务必填缺失、锁定字段被修改、默认字段丢失，或 PASS 但入库未验证。
- HAR 中 `treeNodeClick` / `loadData` / `addnew` 使用的是 L2 pageId，回放日志却在这些步骤前切到了 L3 或重新 open_form。
- 硬补 `save.post_data` 后错误变化但不稳定，例如从锁定字段错误变成必填字段缺失。

**根因**: 金蝶苍穹会把树节点、默认值、联动字段和部分表单状态保存在 pageId 对应的服务端模型中。列表/树上下文步骤如果过早替换成 L3，会丢掉录制时的 L2 服务端模型；随后保存步骤即使字段看起来齐全，也可能不是同一条上下文链。

**诊断顺序**:
1. 从 evidence package 或 HAR 中抽取关键步骤的原始 `_har_page_id`：`menuItemClick → loadData/treeNodeClick → addnew → update_fields → save`。
2. 对照 run events / debug 日志中的实际 pageId，确认 L2 (`^\d+root[0-9a-f]{32}$`) 与 L3 (`^[0-9a-f]{32}$`) 切换点是否一致。
3. 若列表/树/工具栏步骤被替换成 L3，检查 YAML 是否缺少 `preserve_l2_page: true`，以及 `runner.py` 的 `_step_allows_l2_pageid()` 是否覆盖该 `ac/method`。
4. 若保存步骤仍使用 L2，再回到“类型B：L2 pageId 屏蔽问题”，检查 `_pending_by_app` 和预验证降级。
5. 若 pageId 链路正确但中间模板/选择页报必填（例如 `hpdi_bizdatabillchoicetpl` 提示缺“算发薪管理组织”），先检查 HAR 是否存在 `invokeAction.do/getLookUpList → setItemByIdFromClient` 预热链路；模板默认组织没有显式 setItem 时不要误补成 pick，否则可能清空锁定上下文。

**修复原则**:
- L2 应保留给 `menuItemClick`、`loadData`、`treeNodeClick`、`treeMenuClick`、`postExpandNodes`、`queryTreeNodeChildren`、`entryRowClick`、`refresh`、`itemClick` 等列表/树/工具栏动作。
- L3 应用于真实编辑页的字段更新、保存、提交、确认等表单态动作。
- HAR 导入阶段应为原始 L2 步骤写入 `preserve_l2_page: true`，runner 执行阶段根据 `_step_allows_l2_pageid()` 决定是否替换为 pending L3。
- 对 `loadData` 响应默认带出的必填基础资料，优先按 `form_id` 记录为环境上下文；只有确认字段可编辑且 API 回放会丢失时才生成补偿步骤，模板页默认组织这类服务端模型值应优先由 pageId 链路保留。
- 对 HAR 中以 `invokeAction.do/getLookUpList` 预热的选择器，生成 `prefetch_lookup: true`，runner 必须在 `pick_basedata` 前按原端点预热候选。
- 不要通过追加 `save.post_data` 或删除锁定字段断言来掩盖 pageId 链路问题。只有确认 pageId 链路正确后，才进入字段解析、pick_fields 或业务补偿。

### 类型B-3：showForm 的 billFormId 别名漏绑导致半成功

**症状**:
- 主单最终保存 PASS，但子弹窗选择、明细新增或二次确认的数据没有完整入库。
- F7/选择器的 `loadData`、`entryRowClick`、确定按钮返回空 `[]`，或响应像门户首页，而不是选择器列表。
- `pageid_trace` 显示这些子步骤 pageId 缺失、或没有使用 `showForm` 下发的 32hex pageId。

**根因**: 苍穹弹窗 `showForm` 可能返回 `formId` 作为外壳表单，同时返回 `billFormId` 作为后续请求的真实 `f=`。例如响应中 `formId=hsbs_employeequerylistf7`，后续 HAR 请求却使用 `f=hsbs_empposf7querylist`。若 `_harvest_page_ids()` 只登记 `formId`，后续 `billFormId` 请求会丢 pageId，造成“执行成功但上下文错误”。

**修复原则**:
- `_harvest_page_ids()` 处理 `showForm` 时同时绑定 `formId` 和 `billFormId` 到同一个 32hex pageId。
- 进入子明细补录的 `click/newentry` 不能标 optional；失败要中断，避免保存主单后误报成功。
- 高级面板分录“增行”可能不是普通按钮 click。已验证的薪资期间样本使用 `ac=newentry/key=advcontoolbarap/method=itemClick/args=[addrow,newentry]`；若返回“请先维护频度/期间起始规则”，先补前置字段（如 `calfrequency`、`halfmonthfirstday`、`halfmonthsecday`），不要把后续 `row_index` 写入报错误判为 runner 问题。
- 遇到多个必填基础资料缺失（如薪资核算组的 `country/currency/exratetable`），先确认 pageId `loadData` 默认上下文是否已带值；若未带出，应通过 `pick_basedata + prefetch_lookup` 按业务编码/名称解析，禁止直接硬补长整数内码。
- 遇到“规则分组/常用筛选不能为空”这类业务组件校验（已见于薪资核算场景），先确认 pageId 链路没错，再检查 `country -> labelap4 -> hsas_salarycalcstyle F7 -> select_f7_list_row -> btnok -> groupcontent/entryentity` 是否完整；仅补 `callistrule` 不等于补了 `entryentity` 常用筛选行。不要删除 `no_save_failure` 或硬补保存包体。
- `entryRowClick.post_data[*].selDatas` 代表用户在 F7/列表弹窗里选中的环境对象，应进入 `pick_fields`：界面展示业务编码，保留 `recorded_value_id`，运行时按用户维护编码重新解析真实内码。
- 子弹窗里的业务输入值（如 `bizdate/kd311/kd305/kd306`）应进入智能用例变量，不能因为最终保存 PASS 就忽略中间明细字段。
- 验证时不能只看最终 PASS，要检查最终保存响应或前一步确认响应中是否包含明细字段回填，例如 `entryentity.rows` 的 `bizdate/kd311/kd305/kd306`。

### 类型C：多表单 L2 共享（target_forms 缺失）

**症状**: 非主表单的 invoke 使用错误 pageId / 子表单操作报"页面未初始化"

**诊断**:
```bash
grep "target_forms" cases/xxx.yaml
# 若 menuItemClick 步骤无 target_forms，则问题确认
```

**修复**:
1. 重新导入 HAR: `python -m lib.har_extractor extract xxx.har -o cases/xxx.yaml`
2. 确认生成: `grep "target_forms" cases/xxx.yaml`
3. 若 har_extractor 仍未检测到，手动添加:
```yaml
- type: invoke
  ac: menuItemClick
  target_form: main_form_id
  target_forms: [sub_form_a, sub_form_b]
```

**排查 har_extractor 未检测到的原因**:
- HAR 中无 menuItemClick 步骤
- 步骤缺少 `_har_page_id` 字段
- `_har_page_id` 前缀不匹配 `{menuId}root` 格式

### 类型D：变量解析失败

**症状**: 字段值为 `${today}` 字面量 / 日期被覆盖 / pick_fields 值不生效

**诊断**:
1. 确认 pick_fields 中 key 格式为 `date_<field_key>`（必须有 `date_` 前缀）
2. 确认 `value_id` 或 `value_name` 非空
3. 确认目标步骤 `type == "update_fields"` 且 `fields` 包含该 `field_key`
4. 确认 vars 中变量名与引用 `${vars.xxx}` 一致

**修复**: 确保 pick_fields 格式正确：
```yaml
pick_fields:
  date_effectdate:       # 必须 date_ 前缀
    value_id: "2026-01-01"
    label: "生效日期"
```

### 类型E：业务逻辑错误（非框架问题）

**特征**: 安全网不重试（错误不匹配可重试模式）

**区分方法**:
- 框架错误: `页面未初始化`/`NullPointerException`/`请求超时`/`缓存连接失败` → 安全网自动处理
- 业务错误: `数据已存在`/`必填字段为空`/`校验不通过` → 需修改 YAML 数据

**修复**: 查 advisor 输出 (`result.fixes`)，按修复建议调整 YAML 中的字段值或补步骤。

### 类型F：登录/环境问题

**症状**: 登录失败 / 连接超时 / datacenter_id 错误

**诊断**:
1. 检查 `config/envs/*.yaml` 中 base_url/username/password/datacenter_id
2. 确认环境地址可达: 浏览器访问 base_url
3. 确认凭证有效: 手动登录苍穹平台

**修复**: 编辑 `config/envs/*.yaml` 或 Web UI → 配置 → 环境列表。

### 类型G：执行 PASS 但入库未验证（假成功）

**症状**：
- 执行结果是 PASS，但用户在业务系统查不到数据。
- save/click 保存步骤响应为空数组 `[]` 或缺少 `pkValue` / `fid` / `billId` 等写库 token。
- 断言只用了 `no_error_actions`，没有 `no_save_failure` 或入库回查断言。
- 批量报告中 `write_status = unverified`，`next_action = ai_agent`。

**根因候选**：
1. PageId 链路不对，保存请求打到了 L2/root/list 上下文，服务端返回空响应。
2. HAR 解析遗漏了列表→编辑态桥接步骤，导致表单上下文不是录制时的上下文。
3. 保存动作被标记为 optional 或断言盲区未覆盖字段级错误。
4. 数据实际被写到另一个组织/实体上下文，当前唯一字段回查不到。

**诊断步骤**：
1. 打开批量报告 → 查看“入库未验证”和“AI 证据包”。
2. 在 evidence package 中检查：
   - `problem_summary.write_status`
   - `problem_summary.write_evidence.signals`
   - `run_artifacts.failed_events`
   - 保存步骤的 `resolved_request` 与 `response`
3. 若保存响应为 `[]`：
   - 检查 `target_forms` 是否缺失。
   - 检查保存步骤是否使用 L2 pageId。
   - 检查 `_pending_by_app` 是否被 L2 屏蔽。
4. 若保存响应非空但无写库 token：
   - 补充保存后唯一字段回查断言，或增强 `no_save_failure`。
   - 先运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>`，优先用 `number/billno/code/name/description` 等本次运行变量生成只读回查计划。
   - 优先补 `readback_by_business_key` 断言；它只复用回查步骤或发 `commonSearch`，不能改写保存请求。
   - 不要直接把此类用例标为成功。

**修复原则**：
- PASS 不是交付标准，必须有入库证据。
- 优先补 pageId 链路、target_forms、保存断言或回查断言。
- 不允许通过删除保存步骤、删除断言、标 optional 来“修绿”。

---

## 六、AI Agent 修复升级协议

当内置 repair_planner 不能生成安全修复，或出现 `write_status=unverified` 的假成功风险时，系统会生成 AI Agent 证据包：

```text
GET /api/tasks/{task_id}/agent-evidence/{case_name}
```

### 证据包内容

| 字段 | 含义 |
|------|------|
| `problem_summary` | 失败/假成功摘要、入库证据、失败归因、AI 原因 |
| `case_artifacts.yaml` | 当前 YAML 用例全文 |
| `run_artifacts.events` | 最近一次 run 的事件流，最多 300 条 |
| `run_artifacts.failed_events` | step_fail/assertion_fail/case_error |
| `run_artifacts.pageid_trace` | YAML/HAR/运行事件合并后的 pageId 链路画像 |
| `report_context.acceptance` | 批量验收结论 |
| `skills_to_use` | overview、troubleshooter、pageId、assertion、HR expert 知识入口 |
| `guardrails` | 修复红线 |
| `expected_agent_output` | agent 必须输出的诊断、补丁、测试和回滚计划 |

### Agent 修复流程

1. 先读 `skills_to_use` 中的 overview 与 troubleshooter。
2. 只基于 evidence package 中的 HAR/YAML/run events 诊断，不凭空猜业务字段。
3. 判断问题类型：
   - pageId / target_forms 链路错误（优先检查 HAR 原始 pageId 与回放 pageId 是否一致）
   - HAR 解析变量遗漏
   - 保存断言盲区
   - 环境字段缺失或跨环境 value_id 错误
   - 业务校验错误
4. 输出最小补丁：
   - 优先改当前 YAML。
   - 只有确认是通用规则缺陷时才改 `har_extractor.py` / `runner.py` / `repair_planner.py`。
5. 必跑验证：
   - `./venv/bin/python -m pytest -q tests/unit tests/test_core.py`
   - `./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff`
6. 输出影响说明：
   - 是否影响 10 类基准 HAR（8 个 SIT + 2 个 UAT）。
   - 是否需要用户确认环境字段。
   - 是否需要真实环境写库回查。

### Agent 红线

1. 不得删除 `menuItemClick`、`target_forms`、`pick_fields` 或保存断言来绕过问题。
2. 不得把写库步骤标为 optional，除非它明确不是主业务保存。
3. 不得修改已经成功的 YAML 用例来适配新 HAR。
4. 不得更新 HAR baseline 掩盖规则回归。
5. 不得在无入库证据时宣称修复完成。
6. 通用代码修复必须保持向后兼容，并通过 10 类 HAR 回归影响报告。
7. 不得把硬补 `save` 字段作为 pageId 链路问题的替代修复；必须先证明 L2/L3 切换点与 HAR 原始链路一致。

---

## 七、经验教训

### Rule 14 废弃教训

**结论**: 不要在 YAML 生成阶段静态插入 loadData。

**失败原因**:
1. 静态分析无法知道 form 是否已通过 menuItemClick/target_forms 初始化
2. 额外 loadData 会覆盖已有的有效 pageId
3. 与 target_forms 动态 pageId 管理机制冲突

**当前状态**: `insert_loaddata_on_form_change()` 调用已注释（`har_extractor.py` 约行 1986-1988），等效保护由运行时三层防护提供。

**正确替代方案**: 运行时三层防护（预验证 + auto-open + 安全网重试）

---

## 八、关键文件索引

| 文件 | 行号范围 | 函数/内容 | 排查用途 |
|------|----------|-----------|----------|
| `lib/runner.py` | 336-400 | `_validate_pageid_before_invoke()` | 预验证四场景逻辑 |
| `lib/runner.py` | 923-939 | auto-open 补偿 | 主循环 pageId 缺失补偿 |
| `lib/runner.py` | 942-1003 | invoke-retry 安全网 | 可重试错误+恢复+重试循环 |
| `lib/runner.py` | 867-888 | date pick_fields 后置注入 | 防 `${today}` 覆盖用户日期 |
| `lib/runner.py` | 800-804 | vars_ns 初始化 | 变量命名空间构建 |
| `lib/runner.py` | 835 | `_apply_pick_fields(case)` | 环境字段值注入 |
| `lib/replay.py` | 43-50 | `_is_l2_pageid()` / `_L2_PATTERN` | L2 pageId 正则判定 |
| `lib/replay.py` | 297-309 | `_is_pageid_likely_stale()` | pageId 过期启发式 |
| `lib/replay.py` | 311-340 | `open_form()` | 表单 pageId 申请(getConfig) |
| `lib/replay.py` | 585-600 | `_harvest_virtual_tab_pageids()` | addVirtualTab → _pending_by_app |
| `lib/har_extractor.py` | 2013-2069 | 规则13 | menuItemClick target_forms 自动检测 |
| `lib/har_extractor.py` | 1986-1988 | 规则14（已禁用） | 静态 loadData 插入（注释状态） |
| `lib/har_extractor.py` | 788-789 | `UNIQUE_KEY_HINTS` | 唯一标识字段名单 |
| `lib/advisor.py` | - | `analyze_errors()` | 错误分析+修复建议生成 |
| `lib/agent_evidence.py` | - | `build_repair_evidence_package()` | AI Agent 修复证据包 |
| `lib/task_manager.py` | - | `infer_write_status()` / `build_acceptance_summary()` | 批量验收与假成功识别 |

---

## 九、日志分析指南

### 日志存储位置

- **实时执行日志**: `logs/runs/<run_id>.jsonl` — 每行一个 JSON 事件
- **服务器日志**: `logs/server-*.log`
- **反模式警告**: `logs/_unknowns/_antipatterns.jsonl`

### JSONL 事件格式

```json
{"ts": 1715760000.123, "type": "step_start", "data": {"step_id": "save_main", "step_type": "invoke"}}
{"ts": 1715760001.456, "type": "retry", "data": {"step_id": "save_main", "attempt": 1, "error": "页面未初始化..."}}
{"ts": 1715760003.789, "type": "step_ok", "data": {"step_id": "save_main"}}
```

### 关键事件类型

| 事件 | 含义 | 关注字段 |
|------|------|----------|
| `case_start` | 用例开始 | case_name |
| `login_ok` | 登录成功 | user_id |
| `step_start` | 步骤开始 | step_id, step_type |
| `step_ok` | 步骤成功 | step_id |
| `retry` | 安全网重试 | step_id, attempt, error |
| `case_done` | 用例完成 | status, duration |
| `case_error` | 用例异常 | error |

### 快速定位方法

```bash
# 查看某次执行的所有重试
grep "retry" logs/runs/<run_id>.jsonl

# 查看失败步骤
grep "case_error\|step_fail" logs/runs/<run_id>.jsonl

# 查看预验证日志（在 stderr/stdout 中）
# 搜索关键字: [pre-validate] / [invoke-retry] / [auto-open]
```

---

## 十、常见修复方案速查

### 修复1：安全网未覆盖新错误模式

```python
# lib/runner.py 行 944-949，添加新模式
_RETRYABLE_ERRORS = (
    "页面未初始化或者已经过期",
    "获取缓存连接客户端失败",
    "请求超时",
    "NullPointerException",
    # 新增: "你的新错误关键字",
)
```

### 修复2：target_forms 缺失

重新导入 HAR → 确认生成。若仍缺失，手动在 menuItemClick 步骤添加:
```yaml
- type: invoke
  ac: menuItemClick
  target_form: main_form_id
  target_forms: [sub_form_a, sub_form_b]
```

### 修复3：日期字段被 ${today} 覆盖

```yaml
pick_fields:
  date_effectdate:       # 必须 date_ 前缀
    value_id: "2026-01-01"
    label: "生效日期"
```

### 修复4：L2 降级不生效

检查 `_pending_by_app` 是否有值。若为空 → 上游 `_harvest_virtual_tab_pageids()` 未被触发 → 检查 addVirtualTab 响应是否被正确解析。

### 修复5：基础资料跨环境失败

`value_id` 跨环境不同 → 在 pick_fields 中标记 `env_sensitive: true`，使用 `value_name` 替代 `value_id`。

---

## 十一、红线规则

1. **不要静态插入 loadData** → Rule 14 教训：运行时防护已覆盖
2. **不要删 menuItemClick** → 页面上下文起点，L2 pageId 来源
3. **不要硬编码 pageId** → 动态值，由 replay 状态机管理
4. **PASS 不等于数据落库** → 检查 save 步骤的断言用 `no_save_failure` 而非 `no_error_actions`
5. **入库未验证必须升级处理** → `write_status=unverified` 不允许作为成功交付
6. **改代码后必须重启 Web UI** → Python 模块启动时一次性加载
7. **业务错误不要改框架** → 先查 advisor 建议，修改 YAML 数据
8. **ac=click + key=btnsave 不要改成 saveandeffect** → 某些表单保存就是 click
9. **Agent 只能做最小补丁** → 必须提供 evidence、diff、测试和回滚计划
