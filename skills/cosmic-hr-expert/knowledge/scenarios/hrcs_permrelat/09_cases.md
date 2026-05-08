# 参考案例 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 3 反编译类实证 + 01_capability_boundary.md 能力边界 + 场景架构分析
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin 反编译 (2026-04-28)

---

## 一、标品设计案例：关联权限项基本配置

**场景**：HR 管理员将"招聘职位 (hbjm_jobhr)"的 view 权限与"员工档案 (hrpi_pernontsprop)"的 view 权限捆绑关联，使得分配招聘职位权限时自动获得员工档案查看权限。

**操作路径**：
1. 进入菜单：HR通用服务 / 权限管理 / 关联权限项
2. 点【新增】→ 进入详情页
3. 选主业务对象：`hbjm_jobhr` (招聘职位)
4. 选应用：`hbjm` (招聘管理)
5. 选主权限项：`view` (查看)
6. 分录新增行：
   - 业务对象：`hrpi_pernontsprop` (员工档案)
   - 应用：`hrpi` (人事)
   - 权限项：点击弹窗 → 多选 `view` (查看)
7. 点【保存】
8. 系统自动同步 `hrcs_permrelatcfg` + 计算受影响角色 → 弹窗询问是否同步角色

**涉及的技术组件**：
- PermRelateEdit 所有 10 个 lifecycleMethods
- F7 过滤链：entitytype → appcombo → entryentity.entitytypeid → entryentity.app
- 子页面 `hrcs_choose_permitem` 多选权限项
- `PermRelateServiceHelper.addPermRelateConfigs` 同步 hrcs_permrelatcfg
- `PermRtSyncService.calcRtPermRole` 角色差异计算

---

## 二、标品设计案例：实时角色同步（选 1-10 行）

**场景**：HR 管理员修改了一条关联权限项后，需要立即将变更同步到所有受影响角色。

**操作路径**：
1. 在关联权限项列表，选择 1-10 行
2. 点【同步角色】
3. 系统实时计算 `calcRtPermRole` → 返回 `LinkedHashMap<roleId, Set<permId>>`
4. 弹 `hrcs_syncroleperm` 预览页，显示角色信息
5. 用户在子页面确认后落库 `t_perm_role_perm`

**涉及的技术组件**：
- PermRelateList.beforeDoOperation FP_LBDO5
- `PermRtSyncService.getRelatePermInfoPair` → `calcRtPermRole`
- 子页面 `hrcs_syncroleperm` (Modal · 独立事务)
- max 10 行限制 (FP_LBDO4)

---

## 三、标品设计案例：全量角色同步 + 后台任务

**场景**：HR 管理员新增了大量关联权限项（全部 hbjm_jobhr 子模块），需要将所有关联全部同步到角色。由于数据量超过 10 行，触发后台调度任务。

**操作路径**：
1. 在关联权限项列表，不选行（全选状态）
2. 点【同步角色】
3. 系统弹红色警告确认框：`"数据量大·请谨慎操作·确认是否同步？"`（ConfirmTypes.Delete + YesNo）
4. 用户点确认 → 下发 `HRRelatePermTask` 调度任务
5. 任务参数：`{syncAll: 1}`, JobType.REALTIME, 1200秒超时, 不可终止
6. 后台任务扫全量 `hrcs_permrelat` → 批量更新 `t_perm_role_perm`

**涉及的技术组件**：
- PermRelateList.beforeDoOperation FP_LBDO3 (全量确认)
- PermRelateList.confirmCallBack FP_LCFB1 (startJob)
- `HRRelatePermTask.execute` (调度任务)
- `JobForm.dispatch` (任务下发)

---

## 四、标品设计案例：导出 INSERT SQL 脚本（迁移用）

**场景**：测试环境配置了一批关联权限项，需要原样迁移到生产环境（不能用导入导出模板，因为要保留原始 ID）。

**操作路径**：
1. 列表选择要迁移的行
2. 点【导出脚本】
3. 系统临时将选中行的 `issyspreset` 改为 `1`, `issynrole` 改为 `0` → save
4. 用 `PreInsDataScriptBuilder.genInsertSQLScript` 生成三表 INSERT SQL：
   - `T_HRCS_PERMRELAT`
   - `T_HRCS_PERMRELAT_L` (zh_CN)
   - `T_HRCS_PERMRELATENTRY`
5. 将数据改回原值 → save
6. 导出 SQL 文件下载

**涉及的技术组件**：
- PermRelateList.afterDoOperation FP_LADO2 (exportscript 触发)
- `TX.requiresNew()` 独立事务
- `PreInsDataScriptBuilder.genInsertSQLScript` SQL 生成
- `CacheFactory.tempFileCache.saveAsUrl` 文件缓存 (5000秒 TTL)

---

## 五、标品设计案例：auth 跳转细粒度授权

**场景**：HR 管理员配置了一条关联权限项后，需要进入该关联的细粒度授权配置列表，对特定部门/岗位做授权范围限制。

**操作路径**：
1. 列表选一行
2. 点【授权配置】(auth opKey)
3. 跳转到 `hrcs_permrelatcfg` 列表：
   - pageId = `hrcs_permrelatcfg@<parentPageId>`
   - ShowType = MainNewTabPage（不阻塞父页面）
4. 在细粒度授权列表里继续配置

**涉及的技术组件**：
- PermRelateList.beforeDoOperation FP_LBDO2
- ListShowParameter 组装 + pageId @parent 格式
- hrcs_permrelatcfg 独立场景

---

## 六、ISV 参考案例：加"关联优先级"字段 + 保存前校验

**场景**：ISV 客户需要在关联权限项分录里加一个"优先级"字段（1-5），保存前校验优先级不能为 0。

**实现路径**（对应 CS-01 + CS-03）：
1. `modifyMeta` 走 ISV 扩展元数据 → 在 `entryentity` 上加 `priority` TextField
2. `buildMeta` 建 ISV 扩展表
3. 写 `AbstractValidator` → validate() 中读取 `dy.getString("your_priority_field")` 校验
4. 自建 OP 在 `onAddValidators` 注册 Validator
5. 挂到 `save` opKey 的 executionChain（排在 HRDataBaseOp 之前）

**实证参考**：
- PR-010: OP 13 生命周期 · 在 `onAddValidators` 阶段注册
- PR-007: 不要校验 `issyspreset=true` 的分录行（标品已保护）
- `scene_doc.json`: 分录字段 `isvCanModify` 哪些可改 / 哪些不可改

---

## 七、ISV 参考案例：关联权限项变更后通知下游

**场景**：ISV 客户有自己的权限中台，需要在关联权限项 save/delete 后通知中台同步权限数据。

**实现路径**（对应 CS-05）：
1. grep 实证标品 0 处发 BEC → ISV 必须自建
2. 自建 OP 挂到 `save` 的 executionChain（排在 HRDataBaseOp 之后）
3. 在 `afterExecuteOperationTransaction` 阶段调 `IEventService.triggerEventSubscribeJobs`
4. 单独自建 OP 挂到 `delete` 的 executionChain
5. 注意：事务已提交时发 BEC，不要抛异常期望回滚

**实证参考**：
- PR-011: BEC 业务事件中心 · 标品没发
- `feedback_bec_mode_per_scene_verify.md`: 同应用不同 form BEC 行为差异巨大
- `feedback_bec_3layer_async_publish.md`: 不要套用 hjm_jobhr 3 层异步模式

---

## 八、ISV 参考案例：列表加 BU 过滤

**场景**：ISV 客户有多 BU（招聘/人事/薪酬），希望 HR 管理员只看到自己管辖 BU 的关联权限项。

**实现路径**（对应 CS-07）：
1. 自建 ListPlugin · 父类 HRDataBaseList · 不继承 PermRelateList (PR-001)
2. 在 `setFilter` 阶段用 `EntityCtrlServiceHelper` 查该管理员管辖的 BU entity 列表
3. 追加 QFilter("entitytype", "in", buEntityIds) 到 args.getFilter()
4. 并列挂 · RowKey 排在 PermRelateList 之前 (PR-002)

---

## 九、常见踩坑案例（避坑指南）

### 9.1 坑：继承 PermRelateEdit 后覆盖 propertyChanged → 联动全坏

**现象**：ISV 写了 `extends PermRelateEdit` + `super.propertyChanged(evt)`，但自己的字段联动逻辑覆盖了 entitytype 切换确认回调，导致用户改主业务对象后，分录不清空。

**根因**：PR-001 违反 · 场景专属类不应继承。

**修复**：改为 `extends HRDataBaseEdit` · 并列挂 · 不在标品 propertyChanged 中插入逻辑。

### 9.2 坑：改了 entitytype 的 isvCanModify → 平台升级覆盖

**现象**：ISV 在标品元数据上 `modifyMeta` 把 entitytype 从必填改为非必填，平台升级后覆盖掉 ISV 改动。

**根因**：`entitytype` 的 `isvCanModify=false`（scene_doc.json 实证）· ISV 不应该在标品元数据上改。

**修复**：不要改标品字段属性 · 如果需要非必填，在 ISV OP 的 Validator 里放宽。

### 9.3 坑：save 后抛 KDBizException 期望回滚 → 事务已提交

**现象**：ISV 在 `afterExecuteOperationTransaction` 里检测到下游同步失败，抛 `KDBizException` 期望回滚主 save，但主数据已落库。

**根因**：`afterExecuteOperationTransaction` 阶段事务已提交 · 抛异常不回滚。

**修复**：用 `addErrorMessage` 告知用户，或把检查前移到 `beforeExecuteOperationTransaction` 阶段。

### 9.4 坑：在 propertyChanged 里直接 setValue → 死循环

**现象**：ISV 在 propertyChanged 里直接 `getModel().setValue("x", value)`，触发 model 的 propertyChanged 事件，再次进入自己的 propertyChanged → 无线递归。

**根因**：PR-004 违反 · 没有 beginInit/endInit 包裹。

**修复**：套用 PermRelateEdit.confirmCallBack FP_CFB2 模式（L375-L381）：
```java
this.getModel().beginInit();
this.getModel().setValue("field", value);
this.getModel().endInit();
this.getView().updateView("field");
```
