# 异常诊断 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译实证 + cosmic_realworld_traps 共性
> **数据源**: PermfilesListPlugin / HRAdminStrictPlugin · `cosmic_realworld_traps/` · 2026-04-28

---

## 一、平台级共性陷阱（参考 cosmic_realworld_traps）

| 陷阱来源 | 关键内容 | 本场景适配 |
|---|---|---|
| `buildmeta_traps.md` | 74 值字段类型枚举 / EmployeeField 不存在 / 命名规则 | ISV 加字段时 · 不要用 EmployeeField · 用 BasedataField → bos_user |
| `addrule_traps.md` | PascalCase / preCondition 限制 / 坏规则清理 | 本场景元数据 formRule 0 条 · 加规则前用 listRules 看是否冲突 |
| `modifymeta_traps.md` | formId 必须是 form · EmbedFormAp 假成功 · ops 格式 | 改 hrcs_userpermfile 必须传完整 ops · 不能省 baseEntityId |
| `kb_cosmic_modifymeta_traps.md` | 参数名 fieldType / name / columnName | ❌ 不是 dataType / displayName |

---

## 二、本场景特有陷阱（10 条 · 反编译实证）

### 🔴 陷阱 1：树根节点 GUID 写死

```java
// 反编译 L216
private static final String ID_ROOTNODE = "8609760E-EF83-4775-A9FF-CCDEC7C0B689";
```

**症状**：ISV 重新写 ListPlugin 用别的 GUID 当树根 · 实际渲染时跟标品 PermfilesListPlugin 树根冲突 · 数据加载错乱。

**对策**：ISV 永远用 `getTreeModel().getRoot().getId()` 拿当前树根 ID · 不要硬编。

---

### 🔴 陷阱 2：bar_new 阻断了标品 new opKey

```java
// 反编译 L555-L565
showNewForm() {
    ...
    args.setCancel(true);  // ⭐ 阻断
}
```

**症状**：ISV 在 hrcs_permfilelist 上挂 OP 监听 `new` opKey · 永远不触发。

**原因**：`bar_new`（itemKey）跟 `new`（opKey）不是一回事。`bar_new` 是工具栏按钮 key · 触发后 PermfilesListPlugin.beforeDoOperation 调 `args.setCancel(true)` 阻断标品 `new` opKey。真正的保存在新打开的 hrcs_userpermfile 表单上 save opKey 走的。

**对策**：ISV "新增前置校验" 应挂 `hrcs_userpermfile` 上 save opKey · 不挂 hrcs_permfilelist 的 new。

---

### 🔴 陷阱 3：disable 级联失效不可逆

**症状**：管理员 disable 一个档案 · 1 周后 enable 回来 · 用户登录系统报权限不足。

**原因**：disable 触发 `RoleServiceHelper.disablePermfile` 把 `hrcs_userrolerelat` 全部失效。enable 仅改回 `permfileenable=1` · 不会自动恢复角色绑定。

**对策**：
1. 业务流程上明确"disable 是软删除 · 恢复要重新分配角色"
2. 如果客户要"暂时挂起 · 恢复时角色一起回来" · ISV 必须自建"暂存 + 恢复"逻辑（CS-03 类似）

---

### 🔴 陷阱 4：HRPermCacheMgr.clearAllCache 不在事务内

```java
// 反编译 setEnable L767-L781
private int setEnable(String enable, List<Object> idList) {
    // 1. UPDATE permfile (in TX)
    success = serviceHelper.update(...).length;
    if (PERFILE_STATUS_0.equals(enable)) {
        RoleServiceHelper.disablePermfile(idList);  // 2. cascade (in TX)
    }
    HRPermCacheMgr.clearAllCache();  // 3. 不在 TX
    return success;
}
```

**症状**：disable 操作步骤 2 失败回滚 · 但缓存已清 · 短暂时间出现"DB 旧值 + 缓存空"的不一致。

**对策**：标品已知行为 · ISV 不要在 OP 里再次 clearCache（重复无效）。如必要 · 放在 `afterExecuteOperationTransaction`。

---

### 🔴 陷阱 5：syncperm 同时只能跑 1 个任务

```java
// 反编译 launchSyncPermfileJob L1071-L1081
String taskId = (String)appCache.get("syncPermFilesTaskId", String.class);
TaskInfo taskInfo = ...;
if (null != taskInfo && !taskInfo.isTaskEnd()) {
    showTipNotification("已有后台执行任务，无需再执行。");
    return;
}
```

**症状**：A 浏览器起任务后 · B 浏览器同一登录起任务 · 报"已有后台执行任务"。

**对策**：业务流程上让用户等 A 完成。如客户要并发 · ISV 必须自建任务池 · 不复用标品 SyncPermFilesTask。

---

### 🔴 陷阱 6：搜索框只搜分组名 · 不搜用户姓名

```java
// 反编译 updateSearch L257-L262
search.setSearchEmptyText("请输入权限档案组名称。");
```

**症状**：用户在搜索框输用户姓名 · 没结果。

**对策**：
1. 业务培训：搜索框是按分组名搜
2. 加 ISV 自定义搜索（CS-06）· 在 ListPlugin 里拦截 search 事件改成搜 user.name

---

### 🔴 陷阱 7：列表默认只显示生效档案（permfileenable=1）

```java
// 反编译 filterContainerInit L1009-L1015
if (HRStringUtils.equals(PERMFILEENABLE, fieldName)) {
    commFilter.setDefaultValue("1");
}
```

**症状**：用户 disable 了一行档案 · 列表立即看不到 · 以为被删了。

**对策**：业务培训。或 ISV 改默认值（不推荐 · 改了会显示所有失效档案 · 看着乱）。

---

### 🔴 陷阱 8：复制权限的三重权限校验

```java
// 反编译 checkCanCopy L1166-L1188
1. checkRoleForBidden       — user.isforbidden 或 permfileenable=0
2. getUserRelatesByPermFields — 该档案有可复制角色
3. queryViewableRoles       — 当前操作员能看到这些角色
```

**症状**：HRBP 报"复制权限按钮点了没反应 · 也没报错"。

**原因**：三重校验中任一环节失败 · 都只 showTipNotification（信息提示 · 不是错误）。

**对策**：让用户检查：
1. 源档案是否生效 · user 是否被禁用
2. 源档案是否真的有角色绑定（hrcs_userrolerelat 反查）
3. 当前操作员的角色权限是否包含这些角色（HRRolePermHelper.queryViewableRoles）

---

### 🔴 陷阱 9：删除分组前要先清成员

```java
// 反编译 canDelGroup L902-L918
if (permfileGrpColl.size() > 0) → "不允许删除该分组，因为它包含下级分组。"
if (coll.size() > 0)            → "该分组存在权限档案，不允许删除。"
```

**症状**：HR 想删一个旧分组 · 怎么都删不掉 · 提示"该分组存在权限档案"。

**对策**：
1. 先批量分组（batchgroup）把档案挪到别的分组（hrcs_permfilegrpmember）
2. 再删空分组

⚠️ ISV 可考虑加"级联删除"自定义按钮（CS · 涉及风险评审）。

---

### 🔴 陷阱 10：HRAdminStrictPlugin 双闸 · 项目专员看不到

```java
// 反编译 HRAdminStrictPlugin L42-L52
boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);
boolean isCosmic = PermCommonUtil.isCosmicUser(userId);
if (!isAdmin && !isCosmic) {
    e.setCancel(true);
    e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。");
}
if (!HRAdminStrictPlugin.isHrAdmin()) {  // 双闸
    e.setCancel(true);
}
```

**症状**：ISV 项目里"项目专员"角色 · 跟 HR 域无关 · 但要查权限档案 · 打开报错"您不是 HR 领域管理员"。

**对策**：
- 给项目专员加 admingroup（HR 域 · level 设较低）
- 或开发独立列表（非 hrcs_permfilelist）· 用同样 hrcs_userpermfile 数据源 · 不挂 HRAdminStrictPlugin

---

## 三、ISV 编码常见错误（反编译以外）

### 错误 1：在 OP 里调 `getModel().setValue`

```java
// ❌ 错（OP 没有 model · NPE）
public class IsvOp extends HRDataBaseOp {
    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs args) {
        getModel().setValue("xxx", val);  // NullPointerException
    }
}
```

**对策**：OP 用 `entity.set("xxx", val)` · entity 来自 `args.getDataEntities()[i]`（PR-003）。

---

### 错误 2：在 propertyChanged 不用 beginInit/endInit

```java
// ❌ 错（死循环）
public void propertyChanged(PropertyChangedArgs e) {
    if ("user".equals(e.getProperty().getName())) {
        getModel().setValue("org", computedOrg);  // 触发 propertyChanged("org") · 如果 org 也监听 · 死循环
    }
}
```

**对策**：begin/endInit + updateView（PR-004）。

---

### 错误 3：Validator 里发 RPC

```java
// ❌ 错（事务内卡住）
class IsvValidator extends AbstractValidator {
    public void validate() {
        for (ExtendedDataEntity row : getDataEntities()) {
            // 调外部 BPM · 30 秒超时 · 整个事务卡住
            String result = bpmClient.checkApproval(row);
        }
    }
}
```

**对策**：Validator 内只查 DB（事务内一致性）· RPC / BEC 放 `afterExecuteOperationTransaction`。

---

### 错误 4：BEC 里塞完整 DynamicObject

```java
// ❌ 错（PR-011 反模式）
Map<String, Object> vars = new HashMap<>();
vars.put("permfile", permfileDynamicObject);  // 序列化后超大 · 反序列化慢
svc.triggerEventSubscribeJobs(...);
```

**对策**：只塞 ID 和必要业务字段。订阅方按需查。

---

### 错误 5：ISV 字段不设 isvAccessable

```json
// ❌ 错
{ "op": "add", "fieldType": "TextField", "name": "档案备注", "columnName": "2isv_remark" }

// ✅ 对
{ "op": "add", "fieldType": "TextField", "name": "档案备注", "columnName": "2isv_remark", "isvAccessable": true }
```

**症状**：苍穹平台升级时 · 没标 isvAccessable 的字段会被覆盖丢失。

---

### 错误 6：用 HisModel 时序模式（boid）·本场景非时序

```java
// ❌ 错（hrcs_userpermfile 不是时序）
QFilter qf = new QFilter("boid", "=", permfileBoid).and("iscurrentversion", "=", true);
HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_userpermfile");
helper.queryOriginalCollection("id", new QFilter[]{qf});  // 报错：字段 boid / iscurrentversion 不存在
```

**对策**：本场景用 `id` 直接查（PR-008/PR-009 不适用）。但下游 `bos_user.id` 如果 user 是时序资料·需 boid。

---

## 四、诊断 SOP（出问题时按这个走）

```
现象 1：列表打不开
  → 看是不是 HRAdminStrictPlugin 拦了（不是 HR 域管理员）
  → 看是不是 ISV ListPlugin 抛异常（看日志）

现象 2：disable / enable 不生效
  → 看 PermFilesSaveOp 的 OperationResult.errorMessage
  → 看 customDataMap.dealResult_notNeedUpdateTag · 是不是已经是目标态

现象 3：列表显示数据少 / 多
  → 看 setFilter 是否被 ISV 改了
  → 看 admingroup level 是否合理
  → 看树节点过滤器是不是选错了

现象 4：缓存不一致
  → 在工具栏点【清空缓存】（btn_clearcache）
  → 看 HRPermCacheMgr.clearAllCache 是否被外部 RPC 重新填脏

现象 5：BEC 没触发
  → 看 eventNumber 是否在开发平台预配置
  → 看是否在 afterExecute（不在 endOperation）发的
  → 看订阅插件是否注册到 eventNumber 上

现象 6：ISV 字段消失
  → 看是否设 isvAccessable=true
  → 看 modifyMeta 是否真的改成功（看 errorCode="0" + 看元数据 schemaText）
```

---

## 五、参考资料

- `cosmic_realworld_traps/` 三大坑：buildmeta / addrule / modifymeta
- `cosmic_hr_sdk_whitelist_audit.md` —— SDK 白名单
- `_shared/platform_rules.json` —— 11 PR 平台规则
- `cosmic_sdk_annotation_whitelist.md` —— SDK 注解铁律
- `feedback_har_is_ground_truth.md` —— HAR 真相源
