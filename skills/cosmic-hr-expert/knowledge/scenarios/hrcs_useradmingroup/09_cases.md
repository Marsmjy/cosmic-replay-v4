# 参考案例 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 06_customization_solutions.md 7 个 CS + rules_chain_all.json + form_lifecycle_rules.json 实证
> **confidence**：verified
> **数据源**：CFR 反编译 5 类实证 + 06_customization_solutions.md CS 场景（2026-04-28）

---

## 一、案例总览（按 CS 编号组织）

本场景 06_customization_solutions.md 定义了 7 个定制方案（CS-01 ~ CS-07），每个都对应一类真实的 ISV 二开需求。以下按需求类型展开。

---

## 二、CS-01 · 给 hrcs_useradmingroup 加自定义字段

**需求摘要**：HR 管理员配置时需要加"管理员标签"字段（多选枚举），用于运营分类统计。

**方案要点**：
- 扩展对象：`hrcs_useradmingroup`（主实体 · DynamicFormModel 关联表）
- 扩展点：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI
- 字段类型：`MulComboField`，ISV 前缀 key（如 `${ISV_FLAG}_admintag`）
- 推荐不传 `fieldName`，让平台按 `f + key.lowercase()` 自动生成列名

**关键注意事项**：
- 本场景非 HisModel，ISV 字段不会随版本复制
- 字段挂在中间表上，一行 = 一对 (user, usergroup)，每对 (user, group) 独立维护扩展属性
- 删管理员组时 ISV 字段随主行一起删（DynamicFormModel 默认 1:1 级联）
- 当前无 `_l` 表（没有 MuliLangTextField），加多语言字段需要注意苍穹版本差异

**关联 PR**：PR-007（预置数据不可改，ISV 字段可改）

---

## 三、CS-02 · ISV 字段联动

**需求摘要**：选管理员标签后，联动显示/隐藏授权等级字段。

**方案要点**：
- ISV 自建 FormPlugin（继承 `AbstractTreeListPlugin`，不是 HRAdminGroupTreeListPlugin）
- 在 `propertyChanged` 事件中做联动
- 必须加 `beginInit` / `endInit` 守护——防止 F7 选择触发的多次 propertyChanged 重复执行联动逻辑

**关键注意事项**：
- `usergroup` 是 BasedataField，F7 选择过程会多次触发 propertyChanged（搜索框输入→选中行→确认），不加 init 守护会导致联动逻辑被执行 3-4 次
- ISV 不能继承 HRAdminGroupTreeListPlugin 做联动（PR-001），必须并列挂载

---

## 四、CS-03 · 防重复加用户 / 防删超管

**需求摘要**：
1. 防重复：同一用户不能加到同一管理员组两次
2. 防删超管：不能移除管理员组中的超管用户

**方案要点**：
- 扩展点：`do_add_user` OP 的 `onAddValidators` 注册 Validator
- 实现 `AbstractValidator`，在 `validate()` 中查 `t_perm_useradmingroup` 是否已存在 (user, usergroup) 对
- 或通过 OperateOption 拿 `focusNodeId` 查当前组是否有该用户

**关键注意事项**：
- 防重复校验放在 OP 层 onAddValidators，不是在 FormPlugin.beforeDoOperation（后者是标品 donothing 拦截，不直接落库）
- `do_remove_user` 的 Validator 要通过 `this.getOption().getVariableValue("focusNodeId")` 拿到当前管理员组，再判断要删的用户是否在白名单内
- 不要依赖前端（FormPlugin）做唯一性校验——用户可能通过 API 绕过前端

---

## 五、CS-04 · ISV 自建表反向引用反查（删组前）

**需求摘要**：ISV 自建了 `${ISV_FLAG}_admingroupext`（扩展管理员组属性），含 `admingroup_id` 引用。删管理员组前需要反查：如果 ISV 表有引用，不允许删组或提示级联删除。

**方案要点**：
- 扩展点：`do_remove_group` OP 的 `onAddValidators` 注册 Validator
- 在 `validate()` 中查 ISV 表 `${ISV_FLAG}_admingroupext WHERE admingroup_id = adminGroupId`
- 发现有引用 → `addWarningMessage("此管理员组已被 XX 引用，无法删除")` → 阻止 OP 执行
- 或：不阻止，在 `beginOperationTransaction` 阶段级联 DeleteServiceHelper.delete(${ISV_FLAG}_admingroupext)

**关键注意事项**：
- 标品 AdminGroupDelOp 只级联删 9 张标品表，不自动反查 ISV 表
- ISV 自建 OP 继承 `HRDataBaseOp`（不是 AdminGroupDelOp），并列注册到 `do_remove_group`
- 同事务做级联——保证标品 9 表 + ISV 表的一致性回滚
- 如果在 `beginOperationTransaction` 做级联，与标品 AdminGroupDelOp 的 9 表级联在同一事务内

---

## 六、CS-05 · 跨系统通知（不推荐 BEC）

**需求摘要**：管理员组添加/删除用户后，同步通知到外部 SSO 系统。

**方案要点**：
- 推荐方案：ISV 自建定时任务 / 在 `do_add_user` 或 `do_remove_user` OP 的 `afterExecuteOperationTransaction` 阶段调外部 API
- 不推荐方案：BEC 发布订阅

**为什么不推荐 BEC**：
- 标品 hrcs_useradmingroup **0 处 BEC 发布**（grep 实证 5 反编译类 0 次 triggerEventSubscribeJobs 调用）
- 无现成订阅方，ISV 自建发布+自建订阅无任何生态价值
- 跨场景订阅（如 admin_org 的 hjm 3 层异步发布模式）不能直接套用到 hrcs_useradmingroup
- BEC 双发会产生幂等风暴（如有多个订阅方同时处理同一事件）

**推荐替代**：
- `afterExecuteOperationTransaction` 中调 REST API（事务已提交，失败不影响主业务）
- 不适合同步调用的场景，用定时任务增量同步（每次查 modifytime > lastSync）

---

## 七、CS-06 · 用户 F7 范围扩展（SDK 扩展点）

**需求摘要**：加用户时只允许选择"部门负责人"或"正式员工"，过滤掉实习生/外包等临时身份。

**方案要点**：
- ISV 实现 `IAdminGroupListSubExtPlugin` SDK 接口
- 在 `beforeAddCustomUser(AddCustomUserEventArgs eventArgs)` 中向 LSP 追加 QFilter
- 通过 KingdeeCloudConfig 注册扩展实现

**调用链**：
```
HRAdminGroupTreeListPlugin.showUserF7TreeList()
  → 标品 5 道过滤（已加/自己/同scheme不同type/enable=1/usertype）
  → HRPluginProxy.callAfter(p -> p.beforeAddCustomUser(eventArgs))  ← ISV 扩展点
  → ISV 追加自定义 QFilter（如 emp_type = "FT"）
```

**关键注意事项**：
- ISV 追加的 QFilter 在标品 5 道过滤之后生效——是 AND 关系（不是 OR）
- 不要在这里做 DB 直查——标品已经在前面做了 DB.query，ISV 直接加 QFilter 到 ListShowParameter 即可
- EventArgs 提供 `getLsp()`（ListShowParameter）和 `getQFilter()`（当前已累积的过滤条件）

---

## 八、CS-07 · 批量授权自动化模板

**需求摘要**：批量授权时需要预填授权模板（根据管理员组类型自动选择授权方案）。

**方案要点**：
- 扩展点：挂 ISV FormPlugin 到 `hrcs_amingroupbatchauth` 子页面（不是 hrcs_useradmingroup 主页面）
- 在子页面的 `afterCreateNewData` 或 `propertyChanged(adminGroupType)` 中预填授权字段

**关键注意事项**：
- 真正的批量授权落库逻辑在 hrcs_amingroupbatchauth 子页面，不在 hrcs_useradmingroup 主页面
- ISV 加批量授权后置逻辑应挂子页面的 FormPlugin/OP
- hrcs_amingroupbatchauth 通过 type=batchAuth customParam 区分模式——ISV 要注意不要覆盖此参数

---

## 九、总结：ISV 选择扩展点的决策树

```
要改什么？
├── 加字段/改UI → modifyMeta（CS-01） / ISV FormPlugin（CS-02）
├── 加校验（保存前）→ do_* OP 的 onAddValidators（CS-03 / CS-04）
├── 加级联删（跟着标品一起删）→ do_remove_group OP 的 beginOperationTransaction（CS-04）
├── 扩展F7范围 → IAdminGroupListSubExtPlugin SDK 接口（CS-06）
├── 跨系统通知 → afterExecuteOperationTransaction / 定时任务（CS-05 · 不推荐 BEC）
├── 扩展子页面行为 → 挂子页面的 FormPlugin（CS-07 · hrcs_admingroupdetail / hrcs_amingroupbatchauth）
└── 改标品核心逻辑 → ❌ 红线（PR-001），找替代扩展点
```
