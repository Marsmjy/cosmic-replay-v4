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
- 对 loadData 响应中由 pageId 服务端模型默认带出的必填基础资料，按 `form_id` 记录 `response_values_by_form`；已知模板/新增表单可转为显式 `pick_basedata` 步骤，避免回放时丢失默认组织、上级组织等上下文字段。

### 模板选择页默认上下文（2026-05-22）

典型症状：录制时选择模板正常，回放在选择模板步骤报 `请选择“算发薪管理组织”`，或后续 `open_form(...) got list without pageId: 当前业务数据模板数据缺失，请重新选择模板并创建提报单。`

诊断顺序：
1. 先确认不是 pageId 预打开错误：模板驱动的详情页不能在菜单/list 流程前直接 `open_form`，必须等 `menuItemClick/loadData/addnew/showForm` 建立上下文。
2. 再看 HAR 中模板选择表单的 `loadData` 响应是否带出了默认字段，例如 `hpdi_bizdatabillchoicetpl.org = JDGJJT / 金蝶国际软件集团有限公司`。
3. 如果该字段没有显式 setItem 请求，但后续 pick 依赖它，应让 har_extractor 从同一 `form_id` 的响应默认值生成上下文 `pick_basedata`，并在环境字段面板展示可维护值。
4. 不要把这种问题通过补 `save.post_data` 解决；保存时看似缺字段，实际根因通常是模板/新增页的 pageId 模型上下文没有完整重建。

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
