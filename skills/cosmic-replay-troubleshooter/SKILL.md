---
name: cosmic-replay-troubleshooter
description: Cosmic Replay 执行故障排查与诊断专家。Use when an AI Agent needs to diagnose Cosmic Replay HAR import, YAML generation, pageId chain, target_forms, pick_fields, save/submit failure, PASS but not written to DB, AI evidence package, variable parsing, environment field override, retry safety net, or Kingdee Cosmic replay execution issues.
---

# Cosmic Replay 故障排查诊断

快速定位 Cosmic Replay 用例执行失败的根因，面向 AI Agent 精确到代码位置与修复步骤。

## 项目核心目标（最高优先级）

AI Agent 排障时必须永远围绕这 7 件事判断是否真正完成；不能只把当前报错压下去，也不能把最终 PASS 当成入库成功：

1. HAR 解析要识别真正可维护字段：文本、大文本、多语言、日期/时间、复选框/开关、下拉、F7/基础资料、多选基础资料、分录、子弹窗、按钮和模板/上下文字段。
2. 用户在预览页或变量面板维护的值，必须进入最终 YAML 和运行时回放；执行不能继续使用 HAR 旧值。
3. 下拉、F7、基础资料和 selector 字段要用用户维护的编码/名称调用目标环境接口解析真实 id、候选行和 selDatas，再回填模型上下文。
4. 换环境后会话、CSRF、签名、账号权限、菜单入口、组织、人员、模板、业务上下文和系统托管字段都要动态适配。
5. 执行结果不能只看无异常或最终 PASS，必须校验关键接口响应与录制 HAR 的稳定业务语义基本一致，并继续检查保存/提交、错误提示、无效请求和必要的只读入库回查。
6. 修复经验要沉淀为通用解析/执行规则、字段类型知识库、经验库和测试，而不是只硬补当前 YAML。
7. 原始 HAR 批量导入后要比较优化前后解析质量、维护易用性、执行结果和入库证据，并输出可行动报告。

## 先读原则

外部顾问、Qoder Work、Codex、Kiro、WorkBuddy 或任何新 AI Agent 接手本项目时，先阅读：

- `references/external-consultant-handoff.md`：外发交接、支持边界、已验证场景、禁止动作。
- `references/pageid-chain-debugging.md`：pageId 链路优先排障原则。
- `references/assertion-blindspots.md`：PASS 但入库未验证、断言盲区排查。

---

## SAZ/JMX 迁移经验：动态值链路优先

已验证的 `saz-to-jmx` 经验对 Cosmic Replay 同样适用：不能只看单个请求是否 replay 成功，要先画出“响应产生动态值 → 后续请求消费动态值”的链路。

排障时优先查看证据包：

- `run_artifacts.dynamic_value_flow`
- `run_artifacts.ir_summary.dynamic_value_flow`

该图只包含值类型、步骤、来源和风险，不包含真实 pageId、单号、内部 id、URL、token 或 cookie。重点看这些类型：

| 动态值类型 | 典型生产者 | 典型消费者 | 风险 |
|---|---|---|---|
| `page_id` | `menuItemClick/showForm/getConfig/loadData` 响应 | 后续 `invoke` 请求 | 使用已关闭弹窗或旧页面 pageId |
| `billno` | 保存/提交响应或 `u` action | 审批待办 `wf_task/commonSearch` | 继续搜索 HAR 录制旧单据 |
| `confirm_callback` | `showConfirm.callbackValue` | `afterConfirm/doConfirm` | 继续提交旧 pkvalue 或旧单号 |
| `task_row` | 待办列表 `commonSearch` 响应 rows 或 `wait_until(grid_row_exists)` | `entryRowClick.selDatas` | 点击录制旧任务行，或任务异步生成未等待 |
| `upload_url` | 真实文件上传接口响应 | 附件 `upload/beforeUpload` | 复用 HAR 临时附件 URL |
| `poll_percent` | `getpercent/status` 响应 | 轮询请求 | 多条重复轮询会在导入阶段折叠为 `wait_until` |
| `lookup_candidate` | F7/基础资料查询响应 | `setItemByIdFromClient/pick_basedata` | 用户维护编码未按目标环境解析 |

处理原则：

1. 若 `dynamic_consumer_without_prior_producer` 命中 `billno/confirm_callback/task_row/upload_url`，先查是否仍在消费 HAR 录制旧值。
1. 若 pageId 动态链路出现 `pageid_consumer_without_matching_producer` 或 `pageid_role_mismatch`，先按 form/app 作用域和 L2/L3 类型确认“响应产生的 pageId”是否真的供后续步骤消费；不要只看时间顺序最近的 pageId。L3 字段维护/保存仍用 L2，通常是 `showForm/addVirtualTab/pending L3` 没接上；列表/树/工具栏被替换成 L3，则可能丢失菜单 L2 服务端模型。
2. 审批链路必须从运行时保存/提交响应提取新 `billno`，再用只读 `wait_until(grid_row_exists: billno)` 搜索待办并重建 `entryRowClick.selDatas`。
3. 确认弹窗必须用最新 `showConfirm.callbackValue` 覆盖后续 `afterConfirm/doConfirm`，不能复用录制时的 pkvalue。
4. 真实上传必须是“用户文件 → 上传接口 → 提取 URL/id → 后续附件请求回填”。当前导入会识别 `requires_user_file/upload_replay_strategy/recorded_file_names/recorded_tempfile_reference`，没有真实文件时只允许跳过 HAR 临时附件并提示用户提供文件。
5. 连续 `getpercent/status` 轮询不要保留成几十条等价步骤，导入阶段会折叠为 `wait_until` 语义步骤并设置超时；提交后待办异步生成也应抽象为 `wait_until(grid_row_exists)`，不要用固定 sleep。
6. 保存/提交/审批响应中的中文提示可作为断言候选，但只能补充 `no_save_failure/readback`，不能替代入库验证。
7. HAR 预览中的 `business_flow` 是 SAZ 事务分组思想的 Cosmic 版本：按录制顺序展示每段业务链路的步骤数、输入/写入步骤、L2/L3 角色和可维护字段数量。若用户看不懂长 HAR，先用 `business_flow` 判断哪段链路负责字段维护、哪段负责保存/提交/审批，再回到字段面板维护值。
8. `response_signature` 摘要表示录制时关键响应的语义锚点，例如“期望成功响应 / 期望 showForm / 期望字段回填 N 项”。运行时若报 `[ResponseSemantic]`，优先比较录制语义与运行语义差异，不要只看 HTTP 200 或最终 PASS。
9. `scripts/har_regression_report.py compare --fail-on-diff` 的 baseline 已纳入可维护字段数、业务链路数、响应语义锚点、pageId 对齐风险和 IR 风险。批量导入优化后必须跑该报告；若新增字段/链路是有意改进，先更新 baseline，再确认 diff 为 0。
10. 导入阶段会按 SAZ 的精确关联思想，把 HAR 请求中的 pageId 与更早响应里的同值 pageId 匹配。生成 YAML 使用 `recorded_pageid_source_step_id`、`recorded_pageid_source_kind`、`recorded_pageid_source_retained` 保存脱敏来源；预览使用 `recorded_pageid_flow` 展示精确边、外部根、关闭后复用和跨表单计数。这里绝不能保存真实 pageId。
11. `recorded_pageid_source_retained=false` 只是“原生产者在清理后未保留”的诊断信号，不可直接判失败。浏览器 `clientCallBack` 可能无法协议回放：可选卡片应使用 `requires_harvested_l3_page`，核心表单则结合 `open_form/showForm/pending L3` 和真实执行 trace 判断。只有跨表单精确关联、关闭后继续复用旧窗口，或真实编辑/保存最终仍用 L2 时才升级为修复项。
12. 响应契约使用 `critical/business/advisory` 三级语义，不能做完整 JSON 相等：
    - `critical`：保存、提交、审核、确认、子弹窗确定等写入锚点。录制成功而运行失败/中性、录制业务校验未复现、必要目标表单或关键动作缺失时必须失败。
    - `business`：真正被紧邻 F7/下拉/列表选择消费的查询结果，以及稳定字段回填。只比较目标表单、稳定字段、`id/number/name/billno/status` 等必要列和是否存在候选行。
    - `advisory`：optional 导航或 UI 回调，只告警不阻断。
    - 必须忽略 pageId、内部 id、单号、时间戳、随机测试值、动态行号和未被后续步骤消费的扩展列；`closeWindow/closeBrowserPage` 等同义动作按动作族比较。
    - pageId 的正确性由精确 producer→consumer 图、L2/L3 角色和运行 trace 校验，不要再用 `showForm` 恰好出现在哪个相邻 callback 作为硬响应契约。
13. `recorded_pageid_source_retained=false` 且消费者需要 L3 时，生成 YAML 应标记 `pageid_recovery_strategy=runtime_form_revalidate` 和 `force_pageid_validation=true`；运行器必须绕过“同一表单只校验一次”的缓存，再次确认当前表单拥有可用 L3。L2 列表步骤使用 `recorded_l2_context`，可选浏览器卡片使用 `harvested_l3_guard`。
14. 入库回查必须是独立只读查询的指定 grid 字段精确命中。HTTP 200、保存成功提示、`u` action 对 number/name 的字段回显、执行 PASS 都只能证明请求链路，不得作为入库成功。录制后置查询若依赖关闭窗口返回的 `responsePageId`，但运行时只能得到 L0/空列表/旧记录，应保持 `write_unverified`，报告必须分别写明“已确认、尚未确认、用户下一步”；只有真实环境验证稳定且不会误查旧数据的表单才进入专用回查策略库。
14. 同一个 L2 pageId 字符串可能在同一 HAR 中被菜单多次重新激活。消费者必须关联录制顺序上最近一次生产响应，不能因为 pageId 值相同就永远绑定第一次菜单；菜单响应即使没有 `showForm`，只要生产了 pageId 也必须保留。
15. `activate/showForm` 响应可能把父级 pageId 与后代节点的 `formId/billFormId` 分开放置。解析和运行 harvest 都要把父 pageId 绑定到后代业务表单别名，否则晚期列表会恢复到错误窗口。
16. 保存/提交后的晚期列表只读检查若遇到原会话过期，可在新登录会话中只重放最近的只读菜单窗口，再按运行时业务键查询并重建 `selDatas`。禁止在新会话中重放写步骤，也禁止回退点击 HAR 旧行。
17. 录制查询后紧跟 `wait_until(grid_row_exists)` 时，首次查询暂时为空可以作为瞬态告警；等待结束后的最终查询仍必须满足非空、列结构和业务键契约。不能把所有空列表都降级。
18. 业务接口返回“操作成功，数据同步中”只证明前置操作被接受，不证明后续业务投影已生成。若按本次运行姓名/编号跨状态查询仍无记录，应归类为 `environment_async_business_sync_timeout`，提示排查异步任务、消息队列和投影服务；不得删除后置点击或入库回查来制造 PASS。
19. 每个 YAML 现在应带用例级契约：`capability`、`ai_assistance`、`environment_binding_plan`、`runtime_value_flow_plan` 和 `execution_contract`。排障时先看该契约判断场景是 `query_only`、`write`、`submit_or_audit`、`delete`、`upload` 还是 `unsupported/partial_supported`；不要把只读查询 HAR 强行要求保存/入库，也不要把审批、删除、上传等目标环境依赖场景说成完全自动稳定。
20. 运行前契约预检只阻断真正不安全的写入用例，例如缺少必需目标环境解析字段、写入用例缺 `no_save_failure`，或场景被明确标记 `unsupported`。动态值链路、审批待办、录制环境内部 ID 等默认先作为告警交给运行时和报告解释；除非有真实失败证据，不要把所有告警升级成阻断。
21. 用户在预览或用例详情勾选的“校验点”属于业务断言开关，只控制是否额外检查某个可维护值/字段值；它不能替代系统断言，也不能关闭系统断言。即使用户不勾选任何业务校验点，写入用例仍必须保留 `no_save_failure`、关键响应契约、动态值消费和入库/人工确认证据。
22. 执行报告中的 `decision_summary` 是给使用者看的下一步建议：`environment_binding` 先确认目标环境数据/权限，`script_or_environment_contract_drift` 先比对录制与回放关键接口，`environment_or_session` 先查环境/会话/后端，`script_chain` 再交给 AI 修 pageId/F7/弹窗/字段解析，`write_unverified` 补只读回查。Agent 不应只根据红色 FAIL 就直接改 YAML。
23. `required_context/source=runtime_rule/resolve_status=missing_required_context` 这类字段表示运行时可由目标环境或前置步骤补齐的软上下文。契约预检应提示用户可能需要确认环境数据，但不能在第 0 步直接阻断；只有真实写入步骤证明字段无法解析或后端明确返回必填缺失时，才升级为解析/回放修复项。
24. 定调薪申请单若 `khr_scope=3`（目标薪酬+津贴补助+福利），后续保存/提交可能要求津贴、福利分录生效日期。录制链路中的基准生效日期通常来自子表单 `khr_hcdm_targetsalary.khr_heffectivedate`，但 `khr_hjteffectivedate`、`khr_hfleffectivedate` 要写回父表单 `khr_hcdm_fapplybill` 的分录行模型，且应在 `hcdm_targetsalary` 关闭后、提交/审核前生成可维护字段和 `update_fields`。若错误写到子表单，接口可能返回空数组、页面看似通过但提交仍报“津贴生效日期/福利生效日期”缺失。
25. 每个可维护字段必须有“面板字段 → YAML 步骤 → 运行时注入”的绑定证据。优先查看 `ir_contract.field_bridge` 和 `maintainable_field_binding_plan`：普通变量应绑定 `update_fields/invoke post_data`，F7/基础资料应绑定 `pick_basedata/select_f7_list_row`，列表 selector 应绑定录制的 `entryRowClick`，日期/下拉/开关应绑定对应字段写入。`ir_sources` 只保存 HAR entry/action 索引，不保存真实值，用于证明合并或降级后没有丢动作来源。
26. 写入用例中，若 `user_overridden/manual_override=true` 但绑定状态为 `unbound`，必须在登录和写库前阻断，并归类为 `maintainable_value_unbound`；不能继续使用 HAR 旧值，也不能让用户先排查环境。只读查询用例不应用写入硬门槛。未修改的 `required_context/runtime_rule` 可标记为 `context` 交给目标环境或前置链路补齐，不能误报为用户值未生效。
27. `scripts/har_execute_regression.py` baseline 记录 IR 字段动作覆盖、字段顺序、维护项绑定和跨环境 selector 指标。用例整体 PASS 时，可选/建议型步骤失败仍保留在 Markdown 的“非阻断诊断”，但不进入 `failed_step_ids` 的阻断基线，避免环境瞬态告警制造假回归；请求/响应契约、维护值命中、写入证据和回查结果仍必须参与对比。
28. 写入动作必须由 IR 的规范类型统一识别：`write_save/write_submit/write_audit/write_approve/write_reject/write_delete/write_confirm/write_workflow_start` 等。优先查看 `ir_contract.write_anchor_bridge` 和 `write_anchor_plan`，确认每个 HAR 写入锚点都精确映射到 YAML 步骤，并同时具有关键请求、关键响应和 `no_save_failure` 保护。`saveSetting` 是设置持久化噪声，不是业务保存；普通 `customEvent` 的参数文本即使包含 `save` 或 `ok` 子串，也不能因此升级成写入锚点。
29. 关键响应校验必须来自录制 HAR 的脱敏语义契约，不做完整 JSON 相等。写入锚点的 `expected_response_signature.contract_level` 必须是 `critical`；运行时必须记录 `response_contract_results`。录制响应缺少稳定业务字段时允许使用最小契约 `outcome=not_failure`，但仍要由 `no_save_failure`、写入响应证据和入库回查共同兜底。若 `ir_write_anchor_uncovered` 或 `ir_write_contract_missing` 出现在 preflight，必须在登录和写库前阻断。
30. “首次成功门槛”只有三种写入结论：`verified` 表示首次执行、全部写入锚点、关键请求/响应契约、用户维护值、系统断言和只读入库回查（或明确人工确认）全部通过；`write_unverified` 表示执行与关键契约通过但缺入库证据；`failed` 表示写入锚点未执行、契约失败、维护值未命中或系统断言失败。只读用例为 `not_applicable`，不得强行要求 save/readback。排障时优先查看 evidence 的 `write_anchor_plan`、`first_success_gate.checks/missing`，不要只看顶层 PASS。
31. pageId 风险必须尊重录制事实：真实编辑/保存通常要求 L3，但如果 HAR 本身把工作流确认回调录制在 L2，并且精确 producer→consumer 来源一致，就不能只因 `preserve_l2_page=true` 判错。只有“录制要求 L3、生成步骤却强制保留 L2”、跨表单来源不一致、窗口关闭后复用或运行 trace 证明上下文过期时，才升级为阻断。
32. 新生成 YAML 必须携带 `generation_gate`。预览、`/api/har/extract` 和 runner 共用该门槛：只有写入锚点遗漏、关键请求/响应契约缺失、用户覆盖值未绑定、目标选择器不安全、pageId 生产者丢失且无恢复策略等确定性问题才阻断；质量评分低或复杂交互启发式差异只能提示复核，不能单独误伤可执行用例。目标环境字段尚待维护时允许生成，但必须在写库前阻断运行。
33. `field_catalog` 是预览、变量面板、YAML 和运行报告的统一字段目录。目录只保存脱敏结构信息，必须包含稳定 `field_id`、HAR 首次录入顺序、表单、字段键、类型、位置和变量/环境字段绑定；详情页不得脱离该目录按 vars/pick_fields 各自重排。同一表单、位置和字段只能有一个稳定 field_id。
34. `pageid_source_graph` 是生成和执行契约，不是仅供查看的诊断图。它不保存真实 pageId，只记录消费者、L2/L3 角色、source_request_index、录制生产者是否保留、恢复策略和窗口重开守卫。`recorded_pageid_source_retained=false` 且没有安全 recovery strategy 的写入消费者必须在生成期阻断。

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
- 首页卡片/导航兄弟表单的 `loadData` 不能因为 `loadData` 通常允许 L2 就盲目复用业务 L2。若 HAR 原请求使用纯 32hex L3，但创建该卡片的浏览器 `clientCallBack` 已被过滤，导入应标记 `requires_harvested_l3_page`；运行时只有同表单真实收获到 L3 才执行，否则跳过该 optional 导航步骤。典型证据是回放中的卡片 `loadData` 使用 L2，响应却重新 `showForm` 主业务表单，随后业务 toolbar 在 pageId 正确但服务端模型已被兄弟表单污染的情况下抛 `NullPointerException`。
- 某字段曾在 UAT/其他环境返回“无法修改锁定字段”，不能据此在导入阶段全局删除 HAR 明确录制的 `updateValue/setItemByIdFromClient`。原录制环境可能仍要求该字段必填。应保留录制动作并标记 `skip_if_locked_fields/skip_if_locked`，运行时逐字段执行；仅当目标环境明确返回“无法修改锁定字段”时跳过该字段，其他业务错误仍必须失败。
- 深链路样本经验见 `tests/fixtures/deep_chain_factory/catalog.json`。例如 `薪资核算 / 薪酬项目类别` 的正确闭环是 `menuItemClick` 绑定 L2、`new` 保留 L2、`showForm/loadData` 切 L3、再执行 `update_fields`、`pick_basedata(taglevel)` 和弹窗 `btnsave`。Playwright UI 填框没有触发 `updateValue/save` 时，应改用协议 YAML 验证，不要误判为 parser 失败。
- 如果 `showForm` 返回的是 `bos_list` 但带 `billFormId`（如 `hsas_retroreason`），诊断时要确认 L2 已绑定到业务表单别名；否则列表 load/new 可能拿不到正确 pageId。
- `薪资核算 / 薪酬项目` 已验证闭环：`salaryitemtype` 是必填 lookup，应通过 `getLookUpList` 预热并按名称自动解析；`ispayoutitem` 是 ComboField，应作为 enum 环境字段写入 `update_fields`；保存使用标准 `ac=save/key=tbmain/args=[bar_save, save]`。`createorg/datatype/dataprecision/dataround` 等 loadData 默认值属于 pageId 上下文，不要硬补 save。
- `薪资核算 / 薪资核算场景` 已验证写入闭环：保存提示“规则分组/常用筛选至少一行”时，pageId 链路通常已正常，缺的是 F7 子窗口回填链。正确链路是维护 `country` 后点击 `labelap4` 打开 `hsas_salarycalcstyle` F7，用 `select_f7_list_row` 按编码/名称选中算发薪方式并点击 `btnok`，确认响应回填 `groupcontent/entryentity` 后再保存；仅补选 `callistrule` 不会生成筛选行。
- F7/子弹窗可能多次打开同一个 `billFormId`。若 `showForm` 返回的 formId/billFormId 已在 `_loaded_forms` 中，但 pageId 与当前缓存不同，应视为弹窗重开并更新 pageId；只有 pageId 相同才当作兄弟表单噪声跳过。否则会沿用已关闭弹窗的旧 pageId，典型表现是 F7 `btnok` 误报业务必填字段（如定调薪人员确定时报“生效日期”）。
- 提交后继续审核的链路不能沿用 HAR 录制的 `billno` 搜索待办。应从提交响应 `u` action 中提取运行时 `billno`，注入后续 `wf_task/commonSearch`，并通过 `wait_until(grid_row_exists, field_key=billno)` 等到目标环境真正生成待办行，再按最新任务列表重建 `entryRowClick.selDatas`；超时应提示“目标环境未生成待办/权限或流程配置异常”，不要点击录制旧单据的任务行。
- `基础资料-受控-变动原因` 已验证闭环：原始 HAR 通过 `homs_apphome/treeMenuClick` 建立树菜单 L2，但 API replay 可能无法重建 apphome shell。若保存提示“请按要求填写创建组织”，先检查是否从 HAR 的列表 `createorg_id` 和新增态 `loadData` 提取了 `createorg/ctrlstrategy` 默认上下文；`createorg` 要用内部 Long id 写 `update_fields`，`ctrlstrategy` 要解析 ComboField 编码/中文并暴露为环境字段，不要硬补 `save.post_data`。
- `基础资料-受控-变动原因` 的环境字段覆盖经验：`createorg/ctrlstrategy` 这类 `context_only` 服务端默认上下文字段，用户在预览页或用例详情变量面板维护后，必须在 YAML `pick_fields` 中留下 `user_overridden: true`，并在运行前由 `_apply_pick_fields()` 注入到对应 `update_fields`。若页面看起来已修改但执行仍用 HAR 原值，优先检查 `user_overridden`、`source_step_id/form_id` 作用域、`value_code/value_number/value_id` 优先级，以及运行前是否保存了最新 YAML；不要误判为 pageId 错。
- `resolve_by=value_code` 的基础资料环境字段覆盖经验：当用户只改业务编码时，YAML 中旧的 `value_id/value_name/value_number` 可能仍是录制值。运行期必须把 `value_code` 当作权威输入，旧 `value_name` 只能作为展示/诊断信息，不能因为名称不匹配就回退到录制 `recorded_value_id`。若 run events 中 `pick_fields_preview.value_code` 已是新值，但 `step_start.detail/resolved_request.value_id` 仍是旧值，优先查 `_apply_pick_fields()` 和 `_auto_resolve_pick_basedata_step()` 的编码优先级，而不是改保存包体。
- `setItemByIdFromClient` 的第二参数不能丢：基础资料/F7 在分录或审核表单中常见 `args=[[internal_id, row_index]]`。如果 HAR 原始参数是 `row_index=1`，但回放硬编码成 `0`，可能出现“执行 PASS、保存/提案提示成功，但业务页面实际字段落错行或显示不一致”。解析降级为 `pick_basedata` 时必须保留 `row_index`，运行器调用 `pick_basedata` 时也必须传回该值；不要先硬补 save/postData。
- `recorded_temp_attachment_stale`：HAR 只记录了附件预上传/上传后的临时状态或 `tempfile/download.do?configKey=tempfile.mock&id=...` 句柄，没有真实文件字节。继续回放 `beforeUpload` 可能让表单停在“附件上传中”，继续回放 `upload` 可能让保存/提交报“临时附件已超时，请重新上传”。导入时应将 `beforeUpload/upload` 标记 `skip_replay` 并在运行结果中说明已跳过；若业务强制要求附件，再要求用户提供可重新上传的本地文件，不要复用 HAR 的临时 URL。
- 真实附件上传的最小安全 schema 是 `type: upload_file`、`file_path`、`upload_endpoint`/`upload_url`、可选 `file_field/extra_data/field_key/upload_id`。导入时应从 HAR 的真实 `multipart/form-data` 上传请求提取端点、文件字段和额外表单字段，并把真正的 `upload` 动作暴露为 `source_type=upload_file` 的可维护字段；`beforeUpload` 只作为预检步骤跳过，不能升级成真实上传。若 HAR 上传步骤已带 `skip_replay/requires_user_file/upload_replay_strategy=user_file_required`，用户后续补了 `file_path/upload_endpoint` 后 runner 会把真实 `upload` 步骤转为 `upload_file` 执行。缺 `file_path` 或缺端点要归类为 `upload_file_configuration_missing`，提示用户补本地文件或从真实上传请求/环境接口补端点；不能把它误判为 pageId 或 save.post_data 问题。
- 附件上传闭环必须检查“上传响应 → 后续附件/保存请求消费”。真实 `upload_file` 成功后，runner 会把响应里的运行时 `url/id` 记录为 `runtime_uploads`，后续请求中若仍出现 HAR 录制期 `tempfile/download.do?...id=expired`，应替换为运行时上传返回值；证据包 `dynamic_value_flow` 应出现 `upload_url` producer→consumer 边。若保存过了但附件没入库，优先排查是否缺少这条消费边，而不是直接硬补 save.post_data。
- 若本次运行发生真实附件上传，runner 会隐式执行 `runtime_upload_consumed` 断言；该断言通过表示“附件上传结果已被后续业务请求消费”，可作为附件链路证据，但不能替代 `readback_by_business_key` 的业务入库验证。若 `upload_file_ok` 通过但 `runtime_upload_consumed` 失败，优先修复上传响应回填/附件确认链路；若二者都通过但业务数据仍不符合预期，再补业务键只读回查或表单专用附件字段回查。
- 附件字段入库验证要区分两层：`runtime_upload_consumed` 只证明“运行时上传结果进入了后续业务请求”；`readback_runtime_upload`/`readback_uploaded_attachment` 才证明“保存后只读回查响应中能看到运行时文件名/id/url”。若已有 `readback_by_business_key` 响应且本次发生真实上传，runner 会追加 advisory 附件回查提示；只有显式严格的附件回查断言通过，才可把附件字段作为入库验证信号。通用列表不返回附件字段时，不要把 advisory 未命中误判为保存失败，应补表单专用附件详情回查。
- 附件链路排查时要确认“上传响应 id/url → 后续附件字段/确认请求”是否被消费。若后续请求仍拿 HAR 的 `tempfile.mock` URL 或旧 upload id，应修复动态值链路或补 `dynamic_value_flow.upload_url` 关联；若上传成功但保存仍报附件缺失，优先检查后续附件字段回填步骤，不要直接改保存包体。
- `基础资料-受控-变动原因` 的入库回查经验：保存响应含“保存成功”且 `no_save_failure/no_error_actions` 通过，但 `readback_by_business_key` 失败时，优先归类为 `readback_assertion_gap`。通用 `commonSearch` 对某些受控基础资料不一定能查到刚保存记录，只能作为建议回查策略，不能自动生成硬断言；需要表单专用回查策略或人工确认入库。
- 深链路样本排障完成后，运行 `scripts/deep_chain_pipeline.py scenario-report` 生成脱敏闭环报告，把 HAR 链路画像、YAML smoke、失败分类、入库验证策略和 `experience_candidate` 沉淀为经验库候选。也可单独运行 `scripts/deep_chain_pipeline.py experience-candidate --scenario-id <id> --case <yaml> --smoke-evidence <json>` 输出可审查的 catalog patch；只有 `status=ready` 且脱敏通过时，才允许人工合入经验库。若执行 PASS 但只有“保存成功”提示，应先运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>` 生成推荐查询表单和业务键，再把 `suggested_assertion` 补为 `readback_by_business_key` 只读断言；不能把 PASS 直接当作入库已验证。
- 新 HAR 或失败证据包若包含 `experience_matches`，先看命中的已闭环样本和 `reusable_lessons`。也可运行 `scripts/deep_chain_pipeline.py match-experience --case <yaml> --har <har>`，按结构特征匹配成功经验。匹配结果只用于排障优先级，仍必须回到 HAR 原始 pageId 与回放 pageId 比对，不能因为命中样本就硬补 save 字段。
- 若需要把新 HAR 从导入到执行串起来，优先用 `scripts/deep_chain_pipeline.py run-scenario`。默认模式只生成 HAR 画像、YAML、经验匹配、入库回查计划和报告；只有明确带 `--run-smoke --confirm-write YES_GENERATE_TEST_DATA` 才允许写入测试数据。AI 修复时应检查 `pipeline.status`、`baseline_candidate` 和 `next_actions`，不要绕过该流水线直接改已通过样本。
- 批量真实环境回归默认使用 `scripts/har_execute_regression.py --env auto`：按 HAR 请求 URL 的 host、port 和首段 base path 匹配 `config/envs/*.yaml`，并在报告/baseline 中同时记录 `recorded_env` 与 `execution_env`。不要把 SIT 录制的 HAR 统一跑到 UAT 后再把跨环境必填、锁定字段或菜单差异误判为解析回归。
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
    → date pick_fields 后置注入 (行 867-888)  ← 防日期维护值被变量展开覆盖
```

### date pick_fields 后置注入（runner.py 行 867-888）

**目的**: 日期字段默认保留 HAR 录入值；用户在 pick_fields 中维护日期后，防止运行期变量展开覆盖该维护值。

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
- F7/列表弹窗 selector 的覆盖不能只改 `selDatas[0][0]`。很多列表第 0 列或 `hcdm_adjfileinfo_id` 是 Long 内部主键，用户维护的 `value_code`（如 `06019-0001`）只能用于查找候选行，不能直接写进主键列，否则会触发 `BillListSelection.convertPkValue` 的 `For input string`。正确做法是先从最近一次同表单 `loadData` 的 `billlistap.dataindex/rows` 按 `field_key/number/name/employee_name/employee_empnumber` 定位整行，再同步 `row/selRows/args[0]/selDatas`；找不到候选时不要静默回退录制旧行。
- 若 selector 是从主表/分录字段打开的 F7（YAML 有 `parent_form_id/parent_field_key`），运行期解析必须优先使用父业务字段调用目标环境接口。例如 `hrpi_employee` 弹窗里的列表字段可能是 `name`，但真正要回填的是父表 `khr_hcdm_fapplybill.khr_upperson`；用户维护 `53478` 时，应按父字段 `khr_upperson` 查到员工真实内码和展示名，再重建 `selDatas=[internal_id, code, name]`。只按弹窗 `name` 解析或把编码写进内码列，会导致 `btnok` 只关闭窗口、不回填父分录，最终保存/提交报必填缺失。
- selector 字段在预览页或用例详情页修改业务编码后，旧 `value_id/value_name/value_number` 可能残留。若 `selector_source=entryRowClick` 且 `resolve_by=value_code`，诊断时以用户覆盖后的 `value_code` 为权威；`resolved_request.post_data.selDatas` 必须出现新编码对应的整行和真实内部主键，而不是旧行或把编码塞入主键列。
- 所有需要前端维护值的下拉/F7/基础资料字段，都应先形成运行期 `env_resolution_plan`：基础资料走 `getLookUpList`，F7/list selector 走对应 `loadData` 的 `dataindex/rows`，普通枚举/日期走 `update_fields` 字面量。诊断时先看 `case_start.env_resolution_plan` 和后续 `env_fields_resolved/env_field_resolved`，确认维护编码是否被接口候选解析成真实 id/row/selDatas；不要直接改保存包体或依赖 HAR 录制旧值。
- 明细表格中的用户录入数值（如定调薪 `khr_hpostallowance/khr_hmonthlyincome/khr_hmonthlybonus`，以及加班明细 `bizdate/kd311/kd305/kd306`）属于智能用例变量，不属于环境字段。若预览面板未展示，应检查 `detect_var_placeholders` 是否支持该字段 key 和非字符串数值；不要把这类金额/小时字段硬放进 `pick_fields`。
- 在线导入 HAR 时会将 `getEntityType.do` 返回的字段原始类型（如 `TextProp/BasedataProp/MulBasedataProp/ComboProp/MulComboProp/BooleanProp/DateProp/DateTimeProp/EntryProp`）沉淀到 runtime field type catalog；离线或后续 HAR 导入应优先复用该 catalog 做统一字段分类，再回退知识库和 HAR 启发式。若字段类型识别异常，先看 `preview.field_catalog`、`vars_meta.field_category`、`pick_fields.*.field_category/metadata_type/field_type_source`。
- 子弹窗里的业务输入值（如 `bizdate/kd311/kd305/kd306`）应进入智能用例变量，不能因为最终保存 PASS 就忽略中间明细字段。
- 验证时不能只看最终 PASS，要检查最终保存响应或前一步确认响应中是否包含明细字段回填，例如 `entryentity.rows` 的 `bizdate/kd311/kd305/kd306`。
- 写库链路或 F7/selector/子弹窗修复完成后，不能只跑单测和 HAR compare。必须用原始 HAR 重新导入生成一个新 case，再在目标环境执行一次；只有执行通过并且保存/提交响应或只读入库回查满足证据标准，才可判定修复完成。若执行失败，继续基于新 run 的 evidence 排查，不能用旧用例执行结果代替确认。

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

**症状**: 字段值变成 `${today}` 字面量 / 未保留 HAR 录入日期 / pick_fields 日期值不生效

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

### 类型D-2：预览页/变量面板维护后执行仍使用旧值

**症状**:
- 用户在“导入 HAR 新建用例”预览页修改了环境相关字段，生成 YAML 后执行仍使用 HAR 原始值。
- 用户在用例详情变量面板维护了智能变量或环境字段，点击执行后仍跑旧值。
- `pick_createorg_id`、`pick_ctrlstrategy_id`、默认组织、控制策略等字段在界面显示已改，但 run events 的 `resolved_request.fields` 仍是原始 `100000/5` 或录制值。
- `pick_fields_preview` 里 `resolve_by=value_code` 且 `value_code` 已是用户新值，但 `step_start.detail` / `resolved_request.value_id` 仍显示旧编码；`env_fields_resolved` 可能出现“候选项与 value_name 不匹配”。

**高发范围**:
- 不是所有 HAR 都会遇到。普通 `vars`、普通 `date_*`、明确 `pick_basedata` 步骤通常可以直接注入。
- 高风险字段是 `context_only: true` 的服务端默认上下文字段：它们来自 `loadData/showForm/列表 rows`，执行时通过补偿 `update_fields` 写入 pageId 模型上下文，而不是保存请求体。
- 同样高风险的是按编码维护的基础资料/F7 字段：用户常只改 `value_code`，而旧 `value_id/value_name` 会残留。此时旧名称不能参与“是否回退旧值”的判断。

**诊断步骤**:
1. 查看 YAML `pick_fields.<field>` 是否存在 `context_only: true`、`source_step_id`、`form_id`。
2. 若用户维护过该字段，必须看到 `user_overridden: true`。没有该标记时，runner 会把它当作 HAR 录制默认值，可能不会覆盖原始 `update_fields`。
3. 检查 `value_code/value_number/value_id/value_name`：上下文字段应优先展示和维护业务编码；运行注入优先级应为 `value_code` / `value_number` / `value_id` / `value_name`。
4. 对 `resolve_by=value_code` 字段，确认 `step_start.detail`、`resolved_request.value_id/value_code` 使用的是新编码；若 `env_fields_resolved.status=not_found` 且 message 是旧 `value_name` 不匹配，不得回退 `recorded_value_id`。
5. 检查 `source_step_id/form_id` 是否能命中真实 `update_fields` 或 `pick_basedata` 步骤；多表单链路中不能用同名字段跨表单误覆盖。
6. 检查 Web UI 执行前是否已保存最新 YAML。若用例详情变量面板存在未保存状态，执行前必须先保存或自动保存。
7. 在 run events 中对比 `resolved_request.fields` 与 YAML `pick_fields`，确认实际执行值是否已替换。

**修复原则**:
- 预览页和用例详情页保存环境字段时，都要写入 `user_overridden: true`。
- `context_only` 字段如果用户未覆盖，应尽量保留 HAR 服务端默认上下文；只有用户覆盖后才注入新值。
- `resolve_by=value_code` 字段如果用户覆盖了编码，runner 应按编码注入并解析；解析失败时保留用户编码，不要静默回退录制旧 id。
- `pick_createorg_id` 这类字段界面优先展示编码，不要把组织名称当作执行值写入 `update_fields`。
- 不要通过修改 `save.post_data` 修复此类问题；它属于执行前模型上下文字段注入问题。
- 修复后增加单测覆盖：预览覆盖、详情面板覆盖、`context_only` 注入、多表单作用域、`resolve_by=value_code` 只改编码但旧名称残留的回归。

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
   - 先判断是否已有明确成功信号，例如 `保存成功`、字段回写、主键、业务号、页面状态变化。
   - 先运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>`，优先用 `number/billno/code/name/description` 等本次运行变量生成只读回查计划。
   - 只有表单已有专用可靠回查策略时，才自动追加硬 `readback_by_business_key` 断言。
   - 通用 `commonSearch` 回查只能作为 advisory 建议。YAML 中 `mode: advisory` 或 `advisory: true` 的回查失败不应让用例 FAIL，也不能作为“入库已验证”信号；若保存响应成功且用户确认已入库，应归类为 `readback_assertion_gap`，不要把用例当成自动化执行失败。
   - 不要直接把此类用例标为成功，也不要因为通用回查失败就修改 `save.post_data`。

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
| `run_artifacts.ir_summary` | 由 YAML/runtime 派生的脱敏 IR 摘要：case_shape、写入步骤、变量/环境字段 shape、断言和 warnings |
| `run_artifacts.pageid_trace` | YAML/HAR/运行事件合并后的 pageId 链路画像 |
| `report_context.acceptance` | 批量验收结论 |
| `skills_to_use` | overview、troubleshooter、pageId、assertion、HR expert 知识入口 |
| `guardrails` | 修复红线 |
| `expected_agent_output` | agent 必须输出的诊断、补丁、测试和回滚计划 |

### Agent 修复流程

1. 先读 `skills_to_use` 中的 overview 与 troubleshooter。
2. 只基于 evidence package 中的 HAR/YAML/run events 诊断，不凭空猜业务字段。
3. 先读取 `run_artifacts.ir_summary`：用 `case_shape` 判断是否存在写入步骤，用 `steps.role/expected_pageid_role/runtime_pageid_type` 找 L2/L3 风险，用 `variables.value_shape` 和 `environment_fields.value_shape` 判断是否是变量或环境字段遗漏，用 `assertions/warnings` 判断是否是断言盲区。
4. 再读取 `problem_summary.failure_analysis`，如果存在 `diagnosis_priority`，按该列表排查；但仍必须先比对 HAR 原始 pageId 与回放 pageId。
5. 判断问题类型：
   - `pageid_context` / `open_form_context_blocked`：优先检查 L2/L3 切换、`preserve_l2_page`、`target_forms`、`showForm` 是否产生 L3。
   - `business_template_context_missing`：检查模板选择、选人 F7、entryRowClick、btnok、子弹窗字段维护和确定动作是否完整。
   - `environment_field_context_missing`：检查 `createorg`、`ctrlstrategy`、默认组织等是否从 HAR `loadData`、`showForm`、列表 rows 解析为环境字段。
   - `f7_lookup_chain_missing`：检查 `getLookUpList` 预热、候选选择、内部 id 解析和回填表单上下文。
   - `dialog_detail_chain_incomplete`：检查 `newentry/addrow → F7/entryRowClick → 子窗字段维护 → btnok/确定 → 主保存` 全链路，不能只看主单 PASS。
   - `readback_assertion_gap`：保存/提交已成功，但通用入库回查没有命中。先确认 `no_save_failure/no_error_actions`、保存响应成功信号和业务系统实际入库；若确认已入库，移除或降级通用硬回查断言，补表单专用回查策略。
   - `business_validation_expected`：录制中出现且后续有补录动作的业务校验应保留为 `expected_notification`，不是失败。
   - `environment_field_override_not_applied`：预览页或用例详情维护环境字段后仍跑旧值。检查 `pick_fields.user_overridden`、`context_only`、`source_step_id/form_id`、`value_code/value_number`、`resolve_by=value_code` 是否被旧 `value_id/value_name` 覆盖，以及运行前自动保存。
   - `workflow_approval_field_not_applied`：审批弹窗（如 `wf_batchtask_handle`）的审批动作/意见未随用户维护值生效。检查 HAR `loadData` 是否下发 `decision_radio_group`（`Consent/Reject`）和 `msg_approval`，预览页维护“同意/驳回”后是否写入 YAML `pick_fields`，是否同步到 `fill_workflow_approval.update_fields`，以及运行期 `_apply_pick_fields()` 是否把中文展示值归一化为服务端码。
   - `proposal_audit_field_missing`：提案审核/二级审核业务表单（如 `khr_hcdm_proposalplan`）已有 `updateValue` 写入但预览和变量面板缺字段。先确认字段是否来自真实审核表单而非消息中心；将可录入的数值/文本字段抽为 vars，基础资料字段保留为 pick_fields。`wf_msg_message/loadData` 这类消息中心导航副作用若报后端类缺失，可以标为非关键导航 optional，但不能把 `wf_task`、审核表单保存/提交、主保存/提交标 optional。
   - `workflow_task_pageid_missing`：提交后按运行时 `billno` 搜索待办但 `wf_task/commonSearch` 返回空，或后续 `wf_approvalbill` 报“任务不存在”。检查是否先 `open/load wf_msg_center`；`wf_msg_center/loadData` 会下发真实 UUID pageId 给 `wf_task/wf_task_list`。不要让 `wf_task` 继续使用 root、菜单 L2 或录制旧任务行；搜索未命中时应走只读 `wait_until(grid_row_exists)`，命中后重建 `entryRowClick.selDatas`，超时才归类为环境未生成待办/权限/流程配置问题。
   - `workflow_task_wait_timeout`：显式 `wait_until(grid_row_exists, field_key=billno)` 已按运行时单号等待，但目标环境没有返回待办行。先去业务环境查该单号是否提交成功、是否生成待办、当前账号/审批人是否有权限，以及消息中心/流程配置是否正常；不要把这种超时改成点击录制旧任务行或硬补审核请求体。
   - `afterconfirm_callback_stale`：`afterConfirm` 后字段仍被锁定、审核表单打开旧单据，或确认后继续报“任务不存在/无法修改锁定字段”。检查 `afterConfirm.args[2]` 是否仍是 HAR 录制时的旧 `billNo/pkvalue/MUTEX_OBJ_ID`；运行期必须用最近一次同 ID `showConfirm.callbackValue` 替换，再执行确认，不能把旧 callback 硬编码进 YAML。
   - HAR 解析变量遗漏、保存断言盲区、环境字段缺失或跨环境 value_id 错误、真实业务校验错误、执行器问题。
6. 输出最小补丁：
   - 优先改当前 YAML。
   - 只有确认是通用规则缺陷时才改 `har_extractor.py` / `runner.py` / `repair_planner.py`。
6. 必跑验证：
   - `./venv/bin/python -m pytest -q tests/unit tests/test_core.py`
   - `./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff`
7. 输出影响说明：
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
8. 不得把通用 `commonSearch` 回查失败直接等同于写库失败；保存成功但回查不适配时，应归类为断言盲区并补专用回查或人工确认。
9. 不得忽略用户在预览页/变量面板维护的环境字段；若执行值与面板值不一致，优先修 `pick_fields` 覆盖链路和保存链路。

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

### IR 动作粒度与导航策略

**结论**: 苍穹 `batchInvokeAction` 必须按 action 粒度进入 IR，不能把一个 HTTP entry 简化成一个 step。

**原因**:
1. 同一个请求内可能同时包含 `updateValue`、`menuItemClick`、`click(save)` 等不同语义动作。
2. 只按 HTTP entry 建模会漏掉编辑/写入角色，也无法与 YAML 的 `_har_index + _har_action_index` 精确对齐。
3. 真实 HAR 的动作载荷既可能位于 `actions`，也可能位于 `params`；两种都必须解析，但 IR 只能保存 `key/method/args_shape/post_data_shape`，不得沉淀原始业务值。

**排查方式**:
- 查看 `ir_preview.source_har.action_count` 是否大于等于 `api_entry_count`。
- 查看 `ir_navigation_policy.matched_yaml_count/unmatched_ir_count`；存在 unmatched 时先确认该动作是否被 noise 过滤、是否是尚未迁移的 `invokeAction.do` 导航动作，不能直接硬补 pageId。
- 查看 `ir_contract.navigation_policy.stage=stage_1_navigation_list`，确认菜单/L2/装饰导航策略已经由 action 级 IR 参与判定。
- 修改 IR 解析或导航策略后，必须重新跑 13 条 HAR baseline；要求执行结果、pageId 链路、维护字段和响应契约无回归。

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

### 修复3：日期字段被 `${today}` 覆盖或未保留 HAR 录入值

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
