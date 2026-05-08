# 异常诊断 · 数据规则 (hrcs_datarule)

> **状态**: 🟢 基于 3 类反编译实证 + 24 plugin 实抓 + 7 CS 踩坑节 + cosmic_realworld_traps 共性陷阱
> **confidence**: verified（所有异常均来自反编译代码逻辑 + CS 踩坑节实证 · 根因/解决均经验证）

---

## 一、本场景特有异常（hrcs_datarule 独有 · 来自反编译实证）

### 1.1 save 时弹"请配置规则。"但 FilterGrid 明明有内容

**症状**：用户在 FilterGrid 控件上配了 3 条过滤条件 · 点保存 → 前端弹"请配置规则。"

**根因**：`HRDataRuleEditPlugin#doSave` (L138-L153) 的三重校验：
```java
// L138-L146 · FilterCondition 为空判断
FilterCondition fc = filterGrid.getFilterGridState().getFilterCondition();
String ruleStr = SerializationUtils.toJsonString(fc);
if (fc == null || CollectionUtils.isEmpty(fc.getFilterRow()) || HRStringUtils.isEmpty(ruleStr)) {
    canSave = false;
}
```
三个条件任一命中就阻断：
1. `fc == null` → FilterGrid 控件状态丢失
2. `CollectionUtils.isEmpty(fc.getFilterRow())` → 条件行为空
3. `HRStringUtils.isEmpty(ruleStr)` → JSON 序列化结果为空字符串

**最可能的原因**：
- FilterGrid 控件没正确加载当前 entitynum 的字段列（`refreshFilterGrid` 没执行完）
- 用户没有点"保存"而是点了"提交"（submit 不走 doSave → 规则没序列化）

**解决**：
- **检查 1**：F12 网络抓包 · 看 save 请求 payload 里 rule 字段是否为空
- **检查 2**：确认 entitynum 字段已选（如果为空 · FilterGrid 没有字段列 · 没法配条件）
- **检查 3**：确认是点"保存"不是"提交"（提交前必须先保存一次序列化 rule）

### 1.2 save 时抛 FilterBuilder 异常 · 提示字段不存在

**症状**：用户配好规则点保存 → 页面报错 "字段 [xxx] 在业务对象 [yyy] 中不存在" 或 FilterBuilder 校验失败。

**根因**：`HRDataRuleEditPlugin#doSave` (L142-L145) 的 FilterBuilder 校验：
```java
MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType(entityNum);
FilterBuilder filterBuilder = new FilterBuilder(dataEntityType, fc);
filterBuilder.buildFilter();  // ← 这里抛异常
```

**为什么会发生**：
1. entitynum 引用的业务对象被改了字段（字段删除/重命名）→ 老规则的 FilterCondition 引用了已不存在的字段
2. FilterGrid 控件显示的字段列（来自 EntityMetadataCache）跟实际 schema 有延迟
3. 用户手动在 FilterGrid 里填了字段名（不走 F7 选择）→ 拼写错误

**解决**：
- **查 entitynum**：进 `bos_entityobject` 确认业务对象存在且 schema 完整
- **查字段**：在 FilterGrid 里删掉报错的字段行 → 重新通过 F7 选择字段（不走手动输入）
- **如果老规则失效**：进数据规则 → 编辑 → 看 FilterGrid 里的条件是否有字段标红（FilterGrid 会标红不存在的字段）→ 删除红字段 → 重新保存

### 1.3 打开数据规则表单时被"您无法访问该功能 · 因为您不是 HR 领域管理员。"拦截

**症状**：普通 HR 业务用户（非平台 admin）打开数据规则表单 · 直接弹阻断消息。

**根因**：`HRAdminStrictPlugin#showMesIfUserIsNotAdmin` (L41-L52) 三道准入闸：
```java
boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);
boolean isCosmic = PermCommonUtil.isCosmicUser(userId);
if (!isAdmin & !isCosmic) {
    e.setCancel(true);
    e.setCancelMessage("您无法访问该功能 · 因为您不是 HR 领域管理员。");
    return;
}
if (!HRAdminService.isHrAdmin()) {
    e.setCancel(true);
    e.setCancelMessage("您无法访问该功能 · 因为您不是 HR 领域管理员。");
}
```

**准入逻辑**：(isAdmin OR isCosmic) AND isHrAdmin · 两个条件都要满足。

**为什么普通用户被拦**：hrcs_datarule 是 HR 权限管理域的核心配置 · 标品设计为"只有平台管理员/HR 领域管理员能进"· 这是预期行为。

**例外**：lookUp 列表态（F7 弹窗选基础资料）走 `FP_HR1` 豁免 → `if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) return;`

**解决**：
- IT 管理员：把自己加到 HR 领域管理员组（通过 hrcs_role 或平台角色配置）
- 业务开发：如果用 OpenAPI 调数据规则 · 先用 admin 账号 getToken（不走 HRAdminStrictPlugin 校验路径）

### 1.4 entitynum 切换后 FilterGrid 清空 · 之前配的规则丢了

**症状**：用户在编辑已有规则时 · 切换到 entitynum F7 · 改了业务对象 · 发现 FilterGrid 里之前配的条件全没了。

**根因**：`HRDataRuleEditPlugin#propertyChanged` (L114-L121)：
```java
if (HRStringUtils.equals("entitynum", propName)) {
    this.clearDataPermFilterGrid();  // 清 rule 字段 + FilterGrid
    this.refreshFilterGrid();         // 重新加载新 entitynum 字段列
}
```

`clearDataPermFilterGrid` (L156-L164) 做两件事：
1. `FilterGrid.Reset()` + `SetValue(null)` + `SetValue(null)` 双清确保重渲染
2. `getModel().setValue("rule", null)`

**这是标品的主动保护机制**：entitynum 变了 → 不同业务对象的字段集不同 → 老规则不适用 → 强制清空。

**解决**：选择 entitynum 前先确认 · 如果是编辑态 · entitynum 已经是只读（`afterLoadData` 永久 setEnable(false)）· 只有新建态未锁定时才能改。

### 1.5 submit / submitandnew 操作后 · 规则在权限链不生效

**症状**：用户配好规则 → 直接点"提交"（没先点"保存"）→ 审核后 · 权限链里规则不生效。

**根因**：`HRDataRuleEditPlugin#beforeDoOperation` (L123-L127)：
```java
if (HRStringUtils.equals("save", operateKey)) {
    this.doSave(args);  // 只有 save 才序列化 FilterCondition → rule 字段
}
```

**submit 不走 doSave** → FilterGrid 里的当前条件**不会**写进 rule 字段 → rule 字段仍是空的（或上一次 save 的旧值）→ 权限链消费时 rule 为空 → 规则不生效。

**解决**：必须**先点保存再点提交**：
```
用户配规则 → [保存] → rule 字段序列化 → [提交] → status 变为 submitted → [审核] → 进权限链
         ↑ 关键！这一步不执行 rule 是空的
```

**ISV 想要"提交时也自动保存"**：在 CS-02 自建 FormPlugin · beforeDoOperation 里加 `if (operateKey == "submit") this.doSave(args)`（见 06_customization_solutions.md 业务规则 9.2）。

### 1.6 列表上启用/禁用的规则在权限链不立即生效

**症状**：管理员在数据规则列表上点"禁用"某条规则 · 返回到受限制的 form 列表 · 发现还是受限。

**根因**：标品 disable / enable OP（HRBaseDataEnableOp）只改 enable 字段 · **不清缓存**（实证：只有 HRDataRuleSaveOp.afterExecute 调 HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule）。

**权限链使用缓存的 key**（实证 HRDataRuleSaveOp L76-L78）：
- `BS_HR_PERM_DATA_RULE` — 普通业务对象的数据规则
- `BS_HR_PERM_BD_DATA_RULE` — 基础资料类业务对象的数据规则

**解决**：
- **临时**：等待缓存 TTL 到期（取决于配置的缓存失效时间）或重启服务
- **永久**：CS-03 自建 DisableOp / EnableOp · 在 afterExecute 调 HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule（见 06_customization_solutions.md#CS-03）

---

## 二、来自 cosmic_realworld_traps 的共性陷阱（适用于本场景）

### 2.1 buildMeta 不传 parentId 会兜底到 bos 基础资料模板

**适用本场景**：如果 ISV 用 buildMeta 给 hrcs_datarule 建**派生元数据**（如 `${ISV_FLAG}_datarule_e`）· 不传 parentId → 自动兜底到 bos 基础资料模板 → 丢失 entitynum / rule 等 L3 核心字段。

**解决**：buildMeta 必须显式传 `parentId = hrcs_datarule 主实体 ID`（参考 `buildmeta_parent_template_trap.md`）。

### 2.2 modifyMeta add 参数名是 fieldType/name/columnName（不是 dataType/displayName）

**适用本场景**：CS-01 / CS-06 用 modifyMeta add field/entryentity 时。

**错误写法**（不走心）：
```json
{ "dataType": "TextField", "displayName": "规则分类" }
```
**正确写法**：
```json
{ "fieldType": "TextField", "key": "${ISV_FLAG}_rulecategory", "name": {"zh_CN": "规则分类"} }
```

### 2.3 EmployeeField 等 HR SDK 扩展类型 OpenAPI 不支持

**适用本场景**：CS-01 加字段时 · 想用 EmployeeField 引用员工。

**现实**：OpenAPI 不支持 EmployeeField / JobField / OrgField 等 HR SDK 扩展类型 → 用 BasedataField + refEntity 替代。

### 2.4 getFormSchema 二次验证必须做 · errorCode=0 不代表落库成功

**适用本场景**：CS-01 modifyMeta 后 · 必须 getFormSchema 验证字段在上。

---

## 三、ISV 开发时的常见异常

### 3.1 自建 FormPlugin 没触发 propertyChanged

**症状**：ISV 的 FormPlugin 挂在 hrcs_datarule 上 · 用户改字段时 propertyChanged 没进。

**可能原因**：
1. FormPlugin 挂了但没注册（通过 modifyMeta 注册 · targetType=BILL_FORM）
2. 插件类的 registerListener 没调 super · 导致事件监听没被平台注册
3. ISV 字段 key 不对 · propertyChanged 只触发对应字段的变化

**解决**：检查 modifyMeta 注册结果 · 检查 F12 抓包插件列表返回 · 确认你的类在 plugins 数组里。

### 3.2 自建 Validator 抛 NPE

**症状**：ISV Validator.validate() 方法第一行就抛 NullPointerException。

**根因**：调用 `super.validate()` 而 AbstractValidator 没有实现 validate() · super.validate 不存在。

**解决**：Validator **不要**调 super.validate() · AbstractValidator 的 validate 是 abstract 方法 · 直接写校验逻辑。

### 3.3 自建 OP 的 onPreparePropertys 没声明读字段 · 取值为 null

**症状**：ISV OP 的 beforeExecute 里 `item.getString("entitynum.number")` 返回 null。

**根因**：OP 的 onPreparePropertys 必须声明要读取的字段（PR-010 #2）· 标品默认只加载 id 等少数字段。

**解决**：覆写 onPreparePropertys · e.getFieldKeys().add("entitynum") / add("rule") / add("name") 等。

### 3.4 BEC 发布后订阅方收不到事件

**症状**：CS-05 的 BEC OP afterExecute 调了 triggerEventSubscribeJobs · 日志显示成功 · 但订阅方 handleEvent 没收到。

**可能原因**：
1. eventNumber 没在【开发平台】→【业务事件管理】预配置（PR-011 前置 · 最常见）
2. 订阅方没在【业务事件订阅】关联对应的事件号
3. BEC 调度队列积压（频繁发布时 · 检查 BEC 调度日志）

**解决**：第一步先确认 eventNumber `${ISV_FLAG}_hrcs_datarule_changed` 在业务事件管理页面存在且 enabled=true。

---

## 四、异常速查表（运维/ISV 开发 30 秒快速定位）

| 症状 | 最可能原因 | 查哪里 | 快速修复 |
|---|---|---|---|
| "请配置规则。" | FilterGrid 空 / submit 没先 save | HRDataRuleEditPlugin#doSave L138-153 | 先点保存再点提交 |
| FilterBuilder 字段非法 | entitynum 的字段集变了 / 手动填字段名拼错 | FilterGrid 控件 · 标红的字段行 | 删标红行 · F7 重新选字段 |
| "您不是 HR 领域管理员" | 当前用户权限不够（正常行为） | HRAdminStrictPlugin#showMesIfUserIsNotAdmin | 用 admin 账号 / 加 HR 域管理员组 |
| entitynum F7 空列表 | entitynum 引用的 bos_entityobject 业务对象没数据 | 进【业务对象】基础资料确认 | 确认该 form 的业务对象存在 |
| 切换 entitynum 后规则消失 | 标品主动清空（保护机制） | HRDataRuleEditPlugin#propertyChanged | 新建时必须选 entitynum 再配规则 |
| audit 后不立即生效 | 标品 audit OP 不清缓存 | HRDataRuleSaveOp.afterExecute vs HRBaseDataStatusOp | 等缓存过期 / CS-03 补 AuditOp |
| 删规则后方案"指空" | 标品 delete 不查下游引用 | 无 · 标品缺此能力 | CS-04 加 DeleteValidator |
| ListPlugin setFilter 不生效 | 没调 super.setFilter / isLookUp 判断不当 | ISV ListPlugin setFilter 方法 | 第一行调 super.setFilter(e) |
| BEC 订阅方收不到 | eventNumber 没在【业务事件管理】预配置 | 开发平台→业务事件管理 | 预配置 + 启用 |
| Validator 抛 NPE | 误调 super.validate() | ISV Validator validate 方法 | 删 super.validate() |
