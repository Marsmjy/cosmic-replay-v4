# 异常诊断 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 3 反编译类 + 02_business_rules.md + 05_data_flow.md + 通用陷阱
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin + cosmic_realworld_traps (2026-04-28)

---

## 一、本场景特有陷阱 (5 条 · 基于实证)

### 陷阱 1：主业务对象切换确认框点取消 → 字段没回滚

**现象**：用户改了主业务对象 → 弹确认框"确定要清除主权限项和关联信息吗？"→ 用户点取消 → entitytype 还是新值没回滚到旧值。

**根因**：标品 permrelateEdit.confirmCallBack FP_CFB2 用 PageCache.oldEntity 回滚 · 如果 ISV 插件在 propertyChanged 早于标品执行并改了 entitytype，oldEntity 缓存的是 ISV 改后的值而非原值。

**诊断**：
1. 检查你的 ISV FormPlugin RowKey 是否排在 PermRelateEdit 之前
2. 检查 ISV propertyChanged 是否在 entitytype 事件中执行了 `getModel().setValue("entitytype", ...)`
3. 确认 PageCache 中是否有 "oldEntity" 键被覆盖

**修复**：
- ISV propertyChanged 逻辑中不要再次 setValue("entitytype", ...)
- 如必须联动 entitytype 字段，用 beginInit/endInit 包裹且在标品执行之后再设

### 陷阱 2：分录选完权限项回填空值

**现象**：在分录点击 permitem → 弹 hrcs_choose_permitem 子页面 → 多选了 3 个权限项 → 确认 → 分录 permitem 和 permitemid 都是空。

**根因**：子页面 pk 协议是 `"permId||permName"` 双竖杠分隔，但 ISV 或其他插件在 closedCallBack 中解析出错（用单竖杠 `|` split 会错误拆分带竖杠的权限项名称）。

**诊断**：
1. 检查子页面 returnData 的 pk 值是否为 "123||查看权限" 格式
2. 检查 closedCallBack (actionId="hrcs_choose_permitem") 是否被 ISV 覆盖

**修复**：
- 用 `pk.indexOf("||")` 定位双竖杠位置 · 不使用单竖杠 `|`
- 不要在 closedCallBack 中拦截 actionId="hrcs_choose_permitem" 的事件

### 陷阱 3：save 后 hrcs_permrelatcfg 未同步（孤儿/缺失数据）

**现象**：保存关联权限项成功 → 到 hrcs_permrelatcfg 列表查看 → 找不到对应的细粒度授权配置，或者旧配置没被删掉。

**根因**：FP_ADO4 `afterSaveProcessing` 是 `private` 方法，不在 PermRelateEdit 的 public API 中 · ISV 如果覆盖了 `afterDoOperation` 且没调用 `super.afterDoOperation(evt)`，afterSaveProcessing 不会被触发。

**诊断**：
1. 检查 ISV FormPlugin.afterDoOperation 是否调了 super
2. 检查 PageCache.originEntryInfo 是否正确缓存
3. 用 `PermRelateServiceHelper.queryPermRelates` 查 hrcs_permrelatcfg 表是否有对应记录

**修复**：
- ISV 的 afterDoOperation 中必须保留标品逻辑: `super.afterDoOperation(evt)`（如果继承了 HRDataBaseEdit）
- 如果是并列挂，排在 PermRelateEdit 之后执行,不要拦截 save 的 after 事件
- 如需手动同步，调 `PermRelateServiceHelper.deletePermRelateConfigs / addPermRelateConfigs`

### 陷阱 4：btnsycrole 选 11 行提示"不能超出10行"但用户不理解

**现象**：用户选了 11 行点【同步角色】，提示"不能超出10行·请修改。" → 用户不知道该"改"成多少行。

**诊断**：FP_LBDO4 硬限制 ≤ 10 行 · 不在 UI 显示可选行数提示。

**修复**：
- 告知用户：实时同步限 10 行 · 超过走全量路径（不选行，点【同步角色】→ 红色确认框 → 下发后台任务）
- 不要 ISV 私自放松这个限制（calcRtPermRole 计算量 > 10 行会明显变慢）

### 陷阱 5：F7 apply 选不了应用 → 报"业务对象的应用已设置为不允许授权"

**现象**：分录选了业务对象后，点 app F7 → 弹不出来或列表为空 → 提示"当前业务对象的应用已设置为不允许授权·不允许设置关联权限·请修改。"

**根因**：FP_BF7_5 · 该业务对象下所有应用都没通过 `EntityCtrlServiceHelper.removeForBidApp` 过滤（即全部被标记为"不允许授权"）。

**诊断**：
1. 检查该业务对象在后台的 `forBidAppEntity` 配置
2. 用 `EntityCtrlServiceHelper.queryEntityForBidInfo` 查该业务对象的禁止授权列表
3. 确认应用管理页面中该业务对象的"允许授权"标志是否被勾掉

**修复**：
- 去应用管理中把需要关联的应用解除"不允许授权"
- 如果业务确实不需要任何应用，这个业务对象不应出现在关联权限项配置里
- 🚨 ISV 不要覆盖 `removeForBidApp` 逻辑来绕过 — 这是下游授权安全的保障

---

## 二、通用苍穹陷阱 (来自 cosmic_realworld_traps)

### 陷阱 6：buildMeta 不加 parentId → 兜底到 bos 基础资料模板

**来源**：`buildmeta_parent_template_trap.md`

**影响**：ISV 建分录扩展表时如果 buildMeta 不传 parentId，会兜底到 bos_basetpl 基础资料模板，而不是 BillFormModel 的 entry 子表模板。

**修复**：buildMeta 时必须显式指定父模板 Id（ENTITY_PARENT_ID 或 entry 子表模板 Id）。

### 陷阱 7：modifyMeta 加字段的参数名是 fieldType/name/columnName

**来源**：`modifymeta_param_names_and_hr_sdk_limits.md`

**影响**：ISV 用 `dataType` / `displayName` 给 modifyMeta 传参数 → 静默走到 TextField（而不是期望的 BasedataField）。

**修复**：用正确的参数名：
- `fieldType` = "BasedataField" / "TextField" / etc.
- `name` = 字段 key
- `columnName` = 物理列名

### 陷阱 8：EmployeeField 等 HR SDK 扩展类型 OpenAPI 不支持

**来源**：`modifymeta_param_names_and_hr_sdk_limits.md`

**影响**：ISV 通过 OpenAPI `modifyMeta add` EmployeeField → 静默失败或变为 TextField。

**修复**：不要通过 OpenAPI 加 EmployeeField / HRMulPositionField / HRMulAdminOrgField · 走 IDEA 插件在本地添加。

### 陷阱 9：afterExecuteOperationTransaction 抛 KDBizException 期望回滚 → 无效

**来源**：通用苍穹 13 阶段 OP 生命周期规则

**影响**：事务已提交 · 异常不回滚 · 只体现在 UI 错误提示。

**修复**：
- 校验逻辑前移到 `beforeExecuteOperationTransaction` 阶段
- 或者在 `afterExecute` 阶段用 `addErrorMessage` 告知用户 · 不抛异常

### 陷阱 10：PageCache 线程不安全 · 并发会导致数据错

**来源**：苍穹平台通用认知

**影响**：多个用户在同一个 session 中对同一表单操作，PageCache 键可能互相覆盖。

**修复**：
- PageCache 读写只在单用户会话内 · 不做跨会话场景
- ISV 不要用 PageCache 存全局状态 · 只存当前页面临时数据

---

## 三、运行时报错速查表

| 错误消息 | 触发位置 | 含义 | 解决方向 |
|---|---|---|---|
| "您无法访问该功能·因为您不是HR领域管理员。" | HRAdminStrictPlugin FP_HAS2/FP_HAS3 | 双闸校验不通过 | 确认用户有平台管理员或 HR 域管理员权限 |
| "请选择主业务对象。" | PermRelateEdit FP_BIC1 | 增分录行前必须先选主 entitytype | 先在主表选业务对象 |
| "确定要清除主权限项和关联信息吗？" | PermRelateEdit FP_PC1 | 主业务对象切换确认 | 这是确认框 · 不是错误 |
| "当前主业务对象的职能类型为'X'·不允许关联职能类型为非'X'的业务对象。" | FP_PC5 / FP_BDO2 | BU 一致性校验失败 | 分录业务对象必须同主对象同 BU |
| "预置数据无法删除。" | PermRelateEdit FP_BDO3 | 分录 issyspreset=true 不可删 | 这是系统预置数据 · 不可删 |
| "不能超出10行·请修改。" | PermRelateList FP_LBDO4 | btnsycrole 选 >10 行 | 减少选中行数或走全量路径 |
| "当前业务对象的应用已设置为不允许授权·不允许设置关联权限·请修改。" | FP_BF7_5 | 分录所有应用被禁 | 去应用管理解除限制 |
| "主业务对象的应用已设置为不允许授权·不允许设置关联权限·请修改。" | PermRelateEdit FP_ABD2 | 主对象所有应用被禁 | 去应用管理解除限制 |
| "角色已包含关联权限项·无需同步" | PermRelateList FP_LBDO5 | calcRtPermRole 返回空 · 无差异 | 这是信息提示 · 不是错误 |
| "当前权限项为允许独立授权·不允许同步角色" | PermRelateList FP_LBDO5 | 关联权限项被标为 alone 权限 | 这是业务规则限制 · 不需要同步 |
| "请选择业务对象。" | PermRelateEdit FP_BF7_4 | 分录 app F7 前必须先选 entitytypeid | 先在分录选业务对象 |

---

## 四、数据一致性问题诊断

| 现象 | 可能原因 | 诊断方法 |
|---|---|---|
| hrcs_permrelatcfg 有孤儿数据 | delete 时 afterDoOperation(delete) 没调到 deletePermRelateConfigs | 查 PermRelateList.afterDoOperation 是否被覆盖 |
| hrcs_permrelatcfg 缺数据 | save 时 afterSaveProcessing 没执行 | 查 PageCache.originEntryInfo 是否正确 · 是否调了 super.afterDoOperation |
| exportscript SQL 里 issyspreset 不对 | TX.requiresNew 事务没正确提交 | 查 exportscript generateSql 是否异常被吞 |
| 全量角色同步后角色权限没更新 | HRRelatePermTask 执行失败 | 查 sch_task 任务日志 · 查 CanStop=false 是否导致任务挂起 |
| 修改后列表不刷新 | incPermTips changed 回调没触发 | 查 PermRelateEdit.afterDoOperation(FP_ADO2) 是否 returnDataToParent("changed") |
