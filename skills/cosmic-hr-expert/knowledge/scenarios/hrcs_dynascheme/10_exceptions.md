# 异常诊断 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + cosmic_realworld_traps 共性陷阱 + admin_org/hjm 异常对照
> **confidence**: verified
> **数据源**: CFR 反编译 + knowledge/cosmic_realworld_traps/*.md (2026-04-28)

---

## 一、本场景特有异常（dynascheme 独有）

### 1.1 confirmchange 在列表上抛 "not support"

**症状**：列表批量选 N 条已审核方案 · 点【确认变更】 · 系统报错 `"not support"`

**根因**：`DynaAuthSchemeConfirmChangeOp.endOperationTransaction` (L29-L31) 主动抛 KDBizException：
```java
String isListConfirmChange = this.getOption().getVariableValue("list_op", "");
if (isListConfirmChange.equals("1")) {
    throw new KDBizException("not support");
}
```

**为什么**：confirmchange 涉及双写 boid + bgVid · 需要单据态信息（如 changedescription）· 列表批量没法拿全这些字段

**解决**：
- 走单据态打开方案 → 进编辑 → 点【确认变更】（一次一个）
- 如必须批量 · ISV 自建脚本 / 调度任务 · 一条一条调 OperationServiceHelper.executeOperate

---

### 1.2 切换 authaction 时弹"清规则确认"反复弹

**症状**：用户改 authaction 字段 · 弹出"切换授权动作后·规则配置将被清空·确定切换吗？" · 选 No 回滚后 · 再次改还是弹

**根因**：DynaAuthSchemePlugin.propertyChanged (L687-L692) 用 PageCache 标记 `secondConfirmCancel`：
```java
if (HRStringUtils.isNotEmpty(oldValue) && HRStringUtils.isNotEmpty(newValue)
    && !PermRuleValidateUtil.isDecisionSetConditionEmpty(allRuleConfigStr)
    && HRStringUtils.isEmpty(this.getPageCache().get("secondConfirmCancel"))) {
    // 弹确认框
}
```

**解决**：
- 选 No 后 · 标品已设 secondConfirmCancel=true · 下次改 authaction 不弹
- 但 PageCache 是 ServerCache · 跨 session 保留 · 用户重新打开页面会重置
- 行为正常 · 不要绕过

---

### 1.3 admingroup F7 列表为空

**症状**：在表单上点 admingroup F7 · 弹出列表无内容

**根因**：DynaAuthSchemePlugin.beforeF7Select (L345-L350) 双闸过滤：
```java
QFilter idFilter = new QFilter("id", "in", HRRolePermHelper.queryUserAdminGroups(currUserId));
QFilter domainFilter = new QFilter("isdomain", "=", "1").and("domain.number", "=", "hr");
```

**可能原因**：
1. 当前用户没绑任何 HR admingroup（HRRolePermHelper.queryUserAdminGroups 返回空）
2. 系统所有 admingroup 都不是 HR 域（domain.number != "hr"）
3. 当前用户不是 HR 管理员（HRAdminStrictPlugin 拦截）

**解决**：
- 让管理员检查当前用户的 perm_admingroup 配置
- 检查 perm_admingroup 表里有没有 HR 域的记录
- 如果用户不是 HR 管理员 · 应该一开始就被 HRAdminStrictPlugin 拦在 preOpenForm（"您无法访问该功能·因为您不是HR领域管理员"）

---

### 1.4 addrole 后子页面回填 customenable 为空

**症状**：addrole 选完角色 · 跳到 hrcs_dyscassignroledetail 子页面 · 用户没填范围属性直接关闭 · 主表 roleentry 多了一行但 customenable 字段空

**根因**：showRoleDetails (L656-L671) + closedCallBack roleDetails (L619-L654) · 用户没填 dataProperty 时 · 回填的 customenable 也是空

**解决**：
- 标品 checkroledetails 的 beforeDoOperation 已经校验（L482-L485）："请选择角色成员范围属性"
- 但是用户也可以直接 save · 此时 standardly DynaAuthSchemeValidator 应该校验（看反编译）
- ISV 加 CS-03 套路的 Validator 可以加这条规则："customenable 不能空"

---

## 二、HisModel 时序场景共性陷阱

### 2.1 查询时漏 iscurrentversion=true · 拿到多个版本

**症状**：查 dynascheme by name · 拿到多个相同 name 的不同版本

**根因**：HisModel 时序保留所有历史版本 · 不带 iscurrentversion 过滤会全拿到

**解决**：
```java
// ❌ 错误
DynamicObject scheme = QueryServiceHelper.queryOne("hrcs_dynascheme", "id,name", new QFilter[]{new QFilter("name", "=", name)});

// ✅ 正确（PR-008）
DynamicObject scheme = QueryServiceHelper.queryOne("hrcs_dynascheme", "id,name", new QFilter[]{
    new QFilter("name", "=", name),
    new QFilter("iscurrentversion", "=", Boolean.TRUE)
});
```

### 2.2 下游引用用 id 而非 boid · audit/confirmchange 后断链

**症状**：ISV 自建表 `${ISV_FLAG}_xxxlog` 用 `dynaschemeid` 引用 dynascheme.id · audit/confirmchange 后 dynascheme.id 变了 · 自建表的 dynaschemeid 失效

**根因**：dynascheme 是 HisModel · audit/confirmchange 后 id 变 · 但 boid 不变（PR-009）

**解决**：
- ISV 自建关联字段必须存 dynascheme.boid · 不是 id
- 需要时用 boid + iscurrentversion 反查当前版本

---

## 三、苍穹平台共性陷阱（buildmeta_traps · addrule_traps · modifymeta_traps）

### 3.1 buildmeta_traps 节选

参考 `knowledge/cosmic_realworld_traps/buildmeta_traps.md`：

- ❌ EmployeeField 用 OpenAPI buildMeta 不支持 · 兜底成 BasedataField
- ❌ HRMulPositionField / HRMulAdminOrgField 同样 OpenAPI 不支持
- ❌ MulHisModelBasedataField OpenAPI 不支持
- ⚠️ ComboField 漏传 comboOptions · UI 下拉框空白
- ⚠️ fieldName 手填 fk_xxx · 平台再加 f → 列名 ffk_xxx 怪名

**dynascheme 涉及**：
- search_pos / search_adminorg / search_job 用了 HR 专用类型 · ISV 加同类字段 OpenAPI 不支持 · 必须走 IDEA 插件

### 3.2 addrule_traps 节选

参考 `knowledge/cosmic_realworld_traps/addrule_traps.md`：

- ❌ ActionType 写小写（应 PascalCase）
- ❌ preCondition 用 `==''` （应 `is empty` / `is not empty`）
- ❌ 加规则后未清旧规则 · 双规则冲突

**dynascheme 涉及**：
- 当前 listRules 有 2 条 formRule（authaction='1' or '3' / authaction='2' or '3'）· ISV 加新 formRule 时 preCondition 必须用文本比较（authaction 是 ComboField · 字符串） · 不要用 `=` 直接比

### 3.3 modifymeta_traps 节选

参考 `knowledge/cosmic_realworld_traps/modifymeta_traps.md`：

- ❌ formId 写错（如 hrcs_dyna_scheme · 多了下划线）· modifyMeta 抛元数据找不到
- ❌ EmbedFormAp 假成功（OpenAPI 报 errorCode=0 但实际没生效）· 必须 getFormSchema 二次验证
- ❌ ops 不是数组（写成单 op 对象）· 平台 parse 失败

**dynascheme 涉及**：
- 修改 dynascheme 元数据必须用 formId="hrcs_dynascheme"（不是 entity number）
- ISV 改字段必须 getFormSchema 验证

---

## 四、ISV 二开常见异常

### 4.1 NullPointerException at DynaAuthSchemePlugin / DynaAuthSchemeOp

**症状**：ISV 加了 OP 后 · 主流程报 NPE

**根因**：
1. ISV OP 继承了 DynaAuthSchemeOp · super 调用了父类方法 · 父类方法依赖某字段 · ISV onPreparePropertys 没声明
2. ISV 在 OP 里 getModel().setValue（PR-003 违规） · model=null

**解决**：PR-001（不继承）+ PR-003（OP 用 entity）

### 4.2 校验生效晚 · save 已落库才报错

**症状**：ISV Validator 应该 save 前拦截 · 但实际数据已经落库后才报错

**根因**：Validator 注册在 beforeExecuteOperationTransaction 而非 onAddValidators · 注册晚了

**解决**：PR-010 · 必须在 `onAddValidators` 阶段注册 Validator

### 4.3 死循环 stack overflow

**症状**：ISV propertyChanged 联动后 · UI 直接卡住 · 后端报 stack overflow

**根因**：propertyChanged 里 setValue 触发新 propertyChanged · 没用 beginInit/endInit

**解决**：PR-004：
```java
this.getModel().beginInit();
this.getModel().setValue(...);
this.getModel().endInit();
this.getView().updateView(...);
```

### 4.4 BEC 静默丢消息

**症状**：ISV 调 IEventService.triggerEventSubscribeJobs · 返回成功 · 但订阅方收不到

**根因**：eventNumber 没在【开发平台】预配（PR-011 prerequisite）

**解决**：去【开发平台】→ 业务事件管理 → 新增事件号 → 启用订阅

### 4.5 跨表查询 N+1

**症状**：ISV Validator 遍历 100 行方案 · 每行查一次 hrcs_userrolerelat · 卡 30+ 秒

**根因**：用 QueryServiceHelper.queryOne 遍历 · 没批量查

**解决**：用 queryDataSet 一次性查全部 boid · 在内存里做 group/count

```java
// ❌ 错误（N+1）
for (ExtendedDataEntity row : this.getDataEntities()) {
    DynamicObject scheme = row.getDataEntity();
    long boid = scheme.getLong("boid");
    int cnt = QueryServiceHelper.queryOne("hrcs_userrolerelat", "id", new QFilter[]{new QFilter("scheme", "=", boid)});
    // ...
}

// ✅ 正确
Set<Long> allBoids = Arrays.stream(this.getDataEntities()).map(r -> r.getDataEntity().getLong("boid")).collect(toSet());
DataSet ds = QueryServiceHelper.queryDataSet("count-bind", "hrcs_userrolerelat",
    "scheme,count(id) bindCount", new QFilter[]{new QFilter("scheme", "in", allBoids)}, null, "scheme");
Map<Long, Long> boidCount = new HashMap<>();
ds.forEach(r -> boidCount.put(r.getLong("scheme"), r.getLong("bindCount")));
```

---

## 五、ISV 元数据扩展异常

### 5.1 modifyMeta add field 后字段不显示

**症状**：API 返回成功 · 但表单上看不到新字段

**可能原因**：
1. 字段已加但未挂到 UI 布局上 · 必须再 modifyMeta add ap（控件） + 设 fieldKey
2. 浏览器缓存 · 强刷 / 清缓存
3. 字段加错了 parentScope · 加到了不存在的子实体

**解决**：
- 先 getFormSchema 验证字段确实在
- 再 getFormMetadata 看 ApFlat 是否含对应控件 · 不含则补 add ap

### 5.2 ISV 加分录字段 · 标品 confirmchange 后字段值不复制

**症状**：ISV 在 roleentry 加了 `${ISV_FLAG}_role_validdate` 字段 · audit 后查看新版本 · 字段值丢了

**可能原因**：
1. 标品 DynaAuthSchemeAuditOp.endOperationTransaction 调用 `genVersionRoleEntryColl(sourceVid, roleEntryColl)` · helper 内部可能只复制特定字段
2. ISV 字段在 `_l` 表（多语言子表）· 但 dynascheme 当前没 _l 表

**解决**：
- 用 `getFormSchema` 验证字段确实在 `t_hrcs_dynaschemerole` 主表 · 不在 `_l`
- 如果是被 helper 漏复制 · 加 ISV 自定义 OP 在 audit afterExecuteOperationTransaction 阶段单独写 · 用 boid + entryboid 关联

---

## 六、性能与超时

### 6.1 大方案 condition JSON 过大

**症状**：condition 字段是 LargeText · 复杂规则下 JSON 可能 > 100KB · save 时序列化慢

**解决**：
- 业务上拆方案 · 一个方案规则不要太复杂
- 必要时增加 condition 字段最大长度（modifyMeta 改 fieldLength）

### 6.2 反查表灌库慢

**症状**：复杂规则 save 时 · resolveRuleConfigToSearch 灌库 6 张表 · 单次 save 卡 5-10 秒

**根因**：标品同步走 DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch（DynaAuthSchemePlugin.beforeDoOperation L457）

**解决**：
- 标品行为 · ISV 不要试图绕开（绕开会破坏反查索引）
- 业务上接受这个延迟（save 不是高频操作）

### 6.3 confirmchange 双写慢

**症状**：confirmchange 时 saveRoleEntry 调两次（boid + bgVid）· N 行 roleentry 写 2N 次 · 慢

**根因**：标品 DynaAuthSchemeConfirmChangeOp.endOperationTransaction 双写设计

**解决**：
- 业务上控制单方案 roleentry 不超 50 行
- 不能绕过双写（破坏 HisModel 时序）

---

## 七、运维诊断 checklist

```
症状：方案保存失败
  → 看 DynaAuthSchemePlugin.beforeDoOperation 哪个分支报错
    □ 规则校验失败？看 PermRuleValidateUtil.validCondition 输出
    □ authaction=3 规则空？UI 应该提示 "请设置条件规则"
    □ admingroup 空？UI 应该提示必填
  → 看 OP 链：HRBaseDataStatusOp / HisModelOPCommonPlugin / DynaAuthSchemeOp / DynaAuthSchemeSaveSubmitOp 哪个抛了
    □ HisUniqueValidateOp 抛唯一性？看 boid + 时间段冲突
    □ HRBaseDataStatusOp 抛状态错？看 status 字段值是否合规
    □ DynaAuthSchemeValidator 抛业务错？看 hrcs jar 反编译类
  → 看 ISV 自建 Validator 是否 addErrorMessage 阻断

症状：方案审核后下游员工没拿到角色
  → 检查 hrcs_userrolerelat 是否新增了 sourcetype=4 的行
    □ 没新增？看权限重算任务是否执行（标品后台调度）
    □ 新增了但用户没感觉？看缓存 / 用户登录态
  → 检查方案 condition 是否真的命中员工

症状：复制方案后 entryId 重复
  → 看 DynaAuthSchemePlugin.beforeBindData (L246-L264) 复制分支
    □ 是否走了 ORM.genLongIds 重新生成 entryId
    □ sourceSchemeIdUsed PageCache 是否漏了

症状：list 上 confirmchange 抛 not support
  → 标品行为（DynaAuthSchemeConfirmChangeOp.java L29-L31）
  → 解决：单据态打开 confirmchange · 不要列表批量
```

---

## 八、与 admin_org / hjm 共有的异常

| 异常 | 共性原因 | 解决方式 |
|---|---|---|
| HisModel 查询拿到多版本 | 没带 iscurrentversion 过滤 | PR-008 |
| 下游引用用 id 而非 boid | HisModel 概念混淆 | PR-009 |
| Validator 注册晚 | 没在 onAddValidators 阶段 | PR-010 |
| 跨事务写表 · 主事务回滚 ISV 表残留 | endOperation vs afterExecute 选错 | PR-010 |
| 死循环 setValue | 没 beginInit/endInit | PR-004 |
| 继承场景专属类 | 不熟 PR-001 铁律 | PR-001 |

→ HR 域所有 HisModel 时序场景共性 · 详见 `_shared/principles/`。
