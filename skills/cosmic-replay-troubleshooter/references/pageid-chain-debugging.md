# pageId 链路排查指南

## 核心认知

Cosmic 表单的 pageId 有三种来源，按照优先级从高到低：

1. **32hex 表单级 pageId** — 从 `showForm` 或 `addVirtualTab` 下发的 32 位 hex 值，是最精确的 pageId
2. **L2 pageId** — 从 `menuItemClick` 响应中通过 `addVirtualTab` 下发的 `{menuId}root{baseId}` 格式（51+ 字符）
3. **root_pageId** — 从 `getConfig.do` 获取的会话根 pageId（兜底）

重要原则：pageId 不只是请求参数，也是服务端表单模型上下文。很多默认值、联动字段、树节点状态和锁定字段状态保存在 pageId 对应的服务端模型里。排障时优先确认 HAR 原始 pageId 链路与回放链路一致，再看变量解析和字段补偿；不要先硬补 `save` 请求体。

## pageId 错误类型

### 类型 1：pageId 缺失（404 / No pageId error）
**症状**：`ProtocolError: no pageId in resp` 或 `HTTP 404`
**原因**：`open_form()` 或 `menuItemClick` 未正确执行
**修复**：确保 YAML 从 `menuItemClick` 开始

### 类型 2：pageId 过期（空响应，不报错）
**症状**：save 返回 `[]`，PASS 但数据未入库
**原因**：`saveandeffect` 后 pageId 失效但未重新获取
**修复**：runner 已自动处理（行 368-370），检查 `keep_page` 设置

### 类型 3：pageId 链路断裂（最隐蔽）
**症状**：`entryRowClick` / `hyperLinkClick` 响应中的 `addVirtualTab` 下发的 pageId 未传递到后续步骤
**原因**：`_pending_by_app` 机制缺失/未集成
**修复**：`replay.py` 三处修复（见下文）

### 类型 4：L2 pageId 屏蔽 `_pending_by_app`（2026-04-30 发现）
**症状**：全部修复后仍返回空 `[]`，`page_ids[form_id]` 是 L2 pageId（`/J9YH7GL2XOVroot...`）
**原因**：`runner.py` 的 `target_form` 绑定设置了 L2 pageId → `_pending_by_app` 后备永不触发
**修复**：pageId 查找时 `_pending_by_app` 优先于 L2 pageId（但不覆盖 32hex）

### 类型 5：L2/L3 过早替换（2026-05-20 发现）
**症状**：录制时正常，回放保存时出现默认字段丢失、业务必填缺失、锁定字段被修改，或保存响应缺少入库证据。
**原因**：HAR 中列表/树/工具栏桥接步骤使用 L2 pageId，但 runner 过早替换成 L3/open_form pageId，导致服务端模型上下文丢失。
**修复**：HAR 导入对原始 L2 步骤生成 `preserve_l2_page: true`；runner 的 `_step_allows_l2_pageid()` 对 `loadData`、`treeNodeClick`、`addnew` 前置桥接、`itemClick` 等步骤保留 L2，只在真实编辑态字段更新/保存/提交时切到 L3。

### 类型 6：showForm 的 formId / billFormId 别名丢失（2026-05-22 发现）
**症状**：用例 PASS 或保存有响应，但只入库主单/部分明细；弹出的 F7 选择列表、确认窗口或后续子表单步骤响应为空 `[]`，或拿到门户首页响应；`pageid_trace` 里这些步骤的回放 pageId 缺失或不属于 HAR 录制的弹窗。
**原因**：苍穹某些 `showForm` 响应会同时下发通用 `formId` 和真实请求使用的 `billFormId`。例如响应里 `formId=hsbs_employeequerylistf7`，但后续 HAR 请求使用 `f=hsbs_empposf7querylist`。如果 runner 只把 pageId 绑定到 `formId`，后续 `billFormId` 请求会找不到正确 pageId。
**修复**：`_harvest_page_ids()` 在处理 `showForm` 时必须把 `formId` 和 `billFormId` 都绑定到同一个 32hex pageId。排障时若看到弹窗列表 loadData/entryRowClick/确定按钮返回空或门户响应，优先检查这个别名绑定，不要误判为字段缺失。

## 修复清单

### `lib/replay.py` 四层修复

```
修复级别 1：初始化
  __init__ 加 self._pending_by_app = {}

修复级别 2：调用
  invoke() 响应处理后调用 self._harvest_virtual_tab_pageids(resp)

修复级别 3：查找后备
  pageId 选择：page_id = _pending_by_app.get(app_id) or root_page_id

修复级别 4：优先级
  pageId 查找：_pending_by_app 优先于 L2 pageId
  条件：只当当前 pageId 是 L2 格式（len > 32 或含 '/'）时才覆盖
  不覆盖：32hex 表单级 pageId
```

补充规则：递归收割 `showForm` 时，若响应参数同时包含 `formId` 与 `billFormId`，必须把两者都登记到 `page_ids`。这类 F7/选择器弹窗常用 `formId` 渲染外壳、用 `billFormId` 作为后续 HAR 请求的 `f=`，漏绑会造成后续选择、确认、明细录入步骤“执行成功但上下文错误”。

### `lib/har_extractor.py`
- `_SAVE_BUTTON_KEYS` 标记 `btnsave` 等按钮为 `tier: core`
- 不改变 `ac`（保持 `click`，不改 `saveandeffect`）
- 对 HAR 原始 L2 pageId 步骤写入 `preserve_l2_page: true`
- pick_fields 展示业务编码，同时保留 `recorded_value_id` 作为跨环境解析兜底
- 对 loadData 响应中由 pageId 服务端模型默认带出的必填基础资料，按 `form_id` 记录 `response_values_by_form`。只有确认 API 回放会丢失且字段可编辑时，才补显式 `pick_basedata`；模板选择页这类默认组织通常应保留在 pageId 服务端模型里，不要把默认值误当成用户 pick。

### 模板选择页默认上下文（2026-05-22）

典型症状：录制时选择模板正常，回放在选择模板步骤报 `请选择“算发薪管理组织”`，或后续 `open_form(...) got list without pageId: 当前业务数据模板数据缺失，请重新选择模板并创建提报单。`

诊断顺序：
1. 先确认不是 pageId 预打开错误：模板驱动的详情页不能在菜单/list 流程前直接 `open_form`，必须等 `menuItemClick/loadData/addnew/showForm` 建立上下文。
2. 再看 HAR 中模板选择表单的 `loadData` 响应是否带出了默认字段，例如 `hpdi_bizdatabillchoicetpl.org = JDGJJT / 金蝶国际软件集团有限公司`。这类默认字段如果没有显式 `setItemByIdFromClient`，通常不要补成 pick，否则可能清空锁定上下文。
3. 检查模板字段本身是否有 `/form/invokeAction.do ... getLookUpList` 预热。若 HAR 是 `getLookUpList → setItemByIdFromClient → ok`，生成 YAML 时应在对应 `pick_basedata` 写入 `prefetch_lookup: true`，runner 必须先走 `invokeAction.do/getLookUpList` 再 setItem。
4. 不要把这种问题通过补 `save.post_data` 解决；保存时看似缺字段，实际根因通常是模板/新增页的 pageId 模型上下文或候选列表预热没有完整重建。

## 子弹窗/明细补录链路（2026-05-22）

典型链路：主单点击 `newentry` → 弹出“计薪人员任职经历”选择器 → 确定 → 弹出“业务数据提报新增” → 维护 `bizdate/kd311/kd305/kd306` → 确定 → 主单保存。

排查要点：
1. `newentry` 这类进入明细补录的 click 不能标 optional；失败应中断，否则会出现“主单保存成功但明细缺失”的半成功。
2. 选择器的 `loadData/entryRowClick/确定` 必须使用 `showForm` 下发的 32hex pageId；如果响应为空或像门户首页，优先查 `billFormId` 别名绑定。
3. F7/列表弹窗的 `entryRowClick.post_data[*].selDatas` 是用户选中的环境对象，应暴露为可维护环境字段：界面展示业务编码，YAML 同时保留 `recorded_value_id` 作为兜底，运行时按用户维护的编码重新解析真实内码。
4. 明细新增弹窗里的业务输入值（如 `bizdate/kd311/kd305/kd306`）应进入智能用例变量，而不是环境字段；用户需要能在预览页和用例详情变量面板维护。
5. 明细新增弹窗的字段更新和确定必须使用真实编辑态 L3 pageId；如果出现 `runtime_l2_used_for_l3_step`，先修 pageId 链路。
6. 最终保存响应里应能看到明细字段回填，例如 `entryentity.rows` 中包含录制字段值；这比单看最终 PASS 更可靠。

## 诊断脚本

优先查看证据包中的 `run_artifacts.pageid_trace`，它会按关键 step 输出：
`step_id / form_id / app_id / ac / method / HAR pageId 类型 / 回放 pageId 类型 / preserve_l2_page / risk_codes`。
若 `risk_codes` 出现 `missing_preserve_l2_page`、`runtime_l3_used_for_l2_step` 或 `runtime_l2_used_for_l3_step`，先修 pageId 链路，不要先补 `save` 字段。

## Playwright 只读探索样本（2026-05-27）

用途：当没有现成 HAR，或新菜单 pageId 链路未知时，先用 Playwright Level 0 只读探索采集入口、菜单候选和脱敏 HAR pageId 摘要，再决定是否进入 Level 1/2 录制样本。

命令示例：

```bash
./venv/bin/python scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 --record-har --max-menu-clicks 0
```

已沉淀经验：
1. 金蝶首页左上角“全部应用”是图标入口，非普通文本按钮；探索器应通过应用入口打开“搜索应用/表单”，再搜索目标云应用。
2. 薪酬福利云搜索命中后，`app_tree` 应能输出“薪酬福利云 -> 薪酬管理 / 薪资核算 / 薪资数据集成 / 薪酬成本 / 工资条 / 员工薪酬服务 / 薪酬基础服务 / 中国社保”等近似树。
3. 点击薪酬福利云子应用后会出现 `getMenuData`，其 pageId 多为 32hex 门户/菜单目录态，只能说明“菜单目录已加载”，还不是业务表单 L2/L3 链路。
4. 当前已验证可只读展开的薪酬福利云子应用包括：薪酬管理、招商局DEMO、薪资核算、薪资数据集成、薪酬成本、工资条、员工薪酬服务、薪酬基础服务、中国社保。
5. 只读入口阶段常见请求仍是首页/门户 `loadData`、`clientCallBack`、`getFrequentData`、`getMenuData`；这不是业务表单 L2/L3 链路，不要拿它直接推断 save/submit 失败原因。
6. 已验证低风险业务菜单样本：`薪资数据集成:业务数据提报` 打开后目标列表 `hpdi_bizdatabill` 使用 L2 pageId；`薪资核算:计薪人员` 打开后会出现 `hsbs_employeequerylist` 等列表上下文。这里只能证明 list 链路，不能代表新增/保存链路已经正确。
7. 只有真正打开业务菜单/list 后出现 `menuItemClick/loadData/treeNodeClick/itemClick/addnew`，才进入 L2/L3 链路判断；后续新增、选择器、子弹窗、保存/提交仍必须按 HAR 原始链路比对。
8. 原始 Playwright HAR 只能留在 ignored 目录（如 `tmp/playwright_hars/`），排障和提交只能使用脱敏结构摘要，不得提交 cookie、token、账号、真实业务数据。

## Level 2 写入 Smoke（2026-05-27）

写入阶段原则：Playwright 负责发现菜单、采集入口链路；真正生成测试数据优先复用 HAR/YAML runner，因为 YAML 已经包含 pageId 链路、环境字段、智能变量、保存断言和 AI 证据包上下文。

命令示例：

```bash
./venv/bin/python scripts/write_smoke_run.py \
  --env uat \
  --case cases/UA提报保存.yaml \
  --confirm-write YES_GENERATE_TEST_DATA \
  --var test_description=CRPLY_WRITE_${timestamp}
```

排障时注意：
1. 写入 smoke 必须有显式确认 token，且测试数据应使用 `CRPLY_WRITE` 等前缀，方便环境清理。
2. 执行证据只保存在 `tmp/write_smoke_runs/`，不能提交；若要沉淀经验，只提交流程摘要、菜单/表单 ID、pageId 类型和断言结论。
3. 若写入失败，仍优先检查 HAR 原始 pageId 链路与回放 pageId 是否一致，再看变量、环境字段、业务校验和断言，不要靠硬补 `save.post_data`。
4. 若写入 PASS 但入库未验证，查看保存响应是否有 `pkValue/billId/saveResult/保存成功` 等证据；缺强证据时运行 `scripts/deep_chain_pipeline.py readback-plan --case <yaml>`，按 `number/billno/code/name/description` 等业务键补 `readback_by_business_key` 只读断言或人工确认，不要直接把 PASS 等同于已入库。
5. 2026-05-27 UA 提报保存验证：UAT 环境使用 `prefetch_lookup` 后 44 步写入 Smoke 通过；同一 YAML 跨 SIT 会在模板确认前失败，原因是默认组织/业务数据模板内部值不一致，不应视为 pageId 修复失败。

## 深链路探索闭环（2026-05-27）

目标：用 Playwright 继续探索薪酬福利云下新增、保存、提交、审核、子弹窗、选人、明细等真实链路，录制新 HAR 后立即反哺 HAR/YAML 解析。

闭环顺序：
1. Playwright 用 `--open-menu-samples 子应用:菜单` 打开目标业务菜单，并用 `--record-har` 采集原始 HAR。
2. 若要继续新增/保存/提交/审核，必须传 `--deep-action-plan`。动作计划只允许 `CRPLY_` 前缀测试数据；写库动作需要 `YES_GENERATE_TEST_DATA`，提交/审核还需要 `YES_SUBMIT_OR_AUDIT_TEST_DATA`。
3. 原始 HAR 进入 `scripts/har_chain_probe.py`，先生成脱敏链路画像：`lookup_prefetches`、`showform_aliases`、`default_contexts`、`write_anchors`、L2/L3 风险。
4. 再导入 HAR 生成 YAML，检查是否保留 `prefetch_lookup`、`billFormId` 别名、明细弹窗 L3、保存断言和环境字段。
5. 用 `scripts/write_smoke_run.py` 执行 YAML；若失败，先用链路画像和 `pageid_trace` 判断是 pageId 链路、候选预热、默认上下文、变量/环境字段，还是业务校验。
6. 稳定、脱敏且有代表性的样本才加入 `tests/fixtures/har_regression/manifest.json` 与 `chain_experience_catalog.json`。

重点原则：
- Playwright 可以点击更深，但不能随机点“删除/反审核/导入/上传/批量”等动作。
- 提交/审核只允许作用于本次由 `CRPLY_` 前缀创建并可识别的测试数据。
- 新 HAR 的经验先沉淀为链路画像，再决定是否修改 parser/runner；不要为了让单个 YAML 变绿而删步骤或硬补 `save.post_data`。

## 深链路场景工厂经验（2026-05-28）

已沉淀位置：
- `tests/fixtures/deep_chain_factory/catalog.json`
- `tests/fixtures/deep_chain_factory/salary_item_category_protocol_save.yaml`
- `tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml`
- `tests/fixtures/deep_chain_factory/salary_period_protocol_save.yaml`
- `tests/fixtures/deep_chain_factory/salary_calc_group_protocol_save.yaml`
- `tests/fixtures/deep_chain_factory/salary_retro_reason_protocol_save.yaml`

薪酬福利云当前代表样本：
1. `薪资数据集成 / 业务数据提报`：UAT 写入 smoke 通过，覆盖主单、选人弹窗、子弹窗明细、lookup 预热和保存。
2. `薪资核算 / 薪酬项目类别`：SIT 写入 smoke 通过，覆盖 `menuItemClick L2 -> new L2 -> showForm L3 -> update_fields/pick_basedata -> btnsave`。
3. `薪资核算 / 薪酬项目`：SIT 写入 smoke 通过，覆盖 `menuItemClick L2 -> new L2 -> showForm/loadData L3 -> update_fields(number/name/ispayoutitem) -> pick_basedata(salaryitemtype) -> bar_save`。
4. `薪资核算 / 薪资期间`：SIT 写入 smoke 通过，覆盖 `menuItemClick L2 -> new L2 -> loadData L3 -> pick_basedata(calfrequency) -> update_fields(halfmonthfirstday/halfmonthsecday) -> newentry(addrow) -> row_index=0 分录字段 -> bar_save`。
5. `薪资核算 / 薪资核算组`：SIT 写入 smoke 通过，覆盖 `menuItemClick L2 -> new L2 -> loadData L3 -> update_fields(number/name/bsed) -> pick_basedata(country/currency/exratetable) -> bar_save`。
6. `薪资核算 / 薪资回溯原因`：SIT 写入 smoke 通过，覆盖 `menuItemClick -> showForm(bos_list,billFormId=hsas_retroreason) -> loadData L2 -> new L2 -> loadData L3 -> update_fields(number/name/description) -> bar_save`。
7. `薪资核算 / 薪资核算场景`：已验证写入闭环，pageId 链路正常，原阻塞点是规则分组“默认规则”下常用筛选至少一行必填。仅补 `callistrule` 不会生成 `entryentity` 常用筛选行；正确链路是 `country -> labelap4 -> hsas_salarycalcstyle F7 -> select_f7_list_row -> btnok`，确认响应会回填 `groupcontent/entryentity`，随后标准 `bar_save` 保存通过。不要硬补 `save.post_data` 或放宽 `no_save_failure`。
8. `中国社保 / 社保体系`：当前账号下未发现新增入口，归类为只读/需人工确认。

排障启发：
- UI 自动化填框后如果没有 `updateValue` 或 `save` 请求，先判断是 Playwright 原生输入未触发苍穹组件事件，不要误判为 HAR 解析失败。
- 对这类样本，Playwright 的价值是采集菜单 L2、新增 L3、`treeview.focus`、`showForm` 和 lookup 候选；最终闭环应由协议 YAML + runner 完成。
- 弹窗底部保存可能是 `ac=click/key=btnsave/method=click`，不是主工具栏 `ac=save/key=tbmain/args=[bar_save, save]`。
- 高级面板分录增行可能是 `ac=newentry/key=advcontoolbarap/method=itemClick/args=[addrow,newentry]`。如果响应只提示“请先维护基本信息中的频度/期间起始规则”，说明 row 没创建，后续 `row_index=0` 写入一定会越界；应先补前置规则字段，再增行。
- 主表多个必填基础资料（如 `country/currency/exratetable`）缺失时，先检查 `loadData` 是否已经作为 pageId 默认上下文带出；没有带出时再生成 `pick_basedata + prefetch_lookup`，不要硬补长整数内码。
- 当 `showForm` 的真实 `formId` 是 `bos_list`，但 `billFormId` 指向业务表单（如 `hsas_retroreason`）时，要把 L2 同时绑定给业务 form；否则后续 loadData/new 会找错 pageId。
- 业务组件校验（如“规则分组/常用筛选不能为空”）属于真实业务验证点，排障顺序是：先确认 L2/L3 链路一致，再补组件处理器或用户可维护字段；若提示来自薪资核算场景，要检查 `country -> labelap4 -> hsas_salarycalcstyle F7 -> select_f7_list_row -> btnok -> groupcontent/entryentity` 行级链路，而不是只补 `callistrule`；不要把业务校验误判成保存接口失败。
- 新增树形基础资料时，`treeview.focus` 是环境上下文，必须保留并作为环境字段暴露；不要删除新增步骤的 `post_data`。
- `hsbs_salaryitem` 这类表单里，`loadData` 默认带出的 `createorg/datatype/dataprecision/dataround/areatype/ctrlstrategy` 属于 pageId 服务端上下文；排障时不要一上来把这些默认值硬补到 save。真正用户维护缺口是 `number/name/salaryitemtype/ispayoutitem`，其中 `salaryitemtype` 要 lookup 预热，`ispayoutitem` 是可维护枚举字段。
- `haos_orgchangereason` 这类“受控基础资料”可能从 `homs_apphome/treeMenuClick` 进入，浏览器 HAR 的树菜单 L2 正常，但 API 回放不一定能重建 apphome shell。排障顺序：先确认原始 HAR 是 `treeMenuClick → list loadData(L2) → new(L2) → showForm/loadData(L3) → save(L3)`；若 API 无法真实建立树菜单 L2，不要伪造 L2 覆盖已预开的主表单 pageId，而应从 HAR 的新增态 `loadData` 和列表 `dataindex/rows` 提取服务端默认上下文。
- 受控基础资料默认字段经验：`createorg` 保存时需要内部 Long id（例如从列表 `createorg_id` 解析到 `100000`），不是 `loadData` 显示元组；`ctrlstrategy` 是 ComboField，应从 `editor.st/comboItems` 解析编码与中文（如 `5=全局共享`）并作为可维护环境字段。补偿应生成 `update_fields` 写入模型上下文，不能追加到 `save.post_data`。

```python
# 在 invoke() 方法中加临时调试
def invoke(self, form_id, app_id, ac, actions, page_id=None):
    ...
    page_id = self.page_ids.get(form_id)
    print(f"[DIAG] {form_id}/{ac}:")
    print(f"  page_ids.get = {page_id}")
    print(f"  _pending_by_app = {dict(self._pending_by_app)}")
    pending_pid = self._pending_by_app.get(app_id)
    ...
    resp = self._post(...)
    print(f"  response length = {len(json.dumps(resp.json()))}")
    ...
```
