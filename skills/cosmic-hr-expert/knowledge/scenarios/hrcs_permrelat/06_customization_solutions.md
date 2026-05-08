# 定制方案 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 01_capability_boundary.md 扩展边界 + 3 反编译类 + 11 条 PR
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin (2026-04-28)

---

## 一、方案总览 (7 个 CS)

本场景提供 7 个 ISV 定制方案，均基于标品实证 + PR 平台规则。所有方案均走"并列挂插件"模式（PR-001），不继承场景专属类。

| CS ID | 方案 | 风险 | 涉及插件类型 | 涉及 PR |
|---|---|---|---|---|
| CS-01 | 加自定义业务字段 | 低 | modifyMeta (ISV 扩展元数据) | PR-001 |
| CS-02 | 加 propertyChanged 联动逻辑 | 低 | FormPlugin 并列挂 | PR-003/PR-004 |
| CS-03 | save 前置 OP Validator | 中 | AbstractValidator (onAddValidators) | PR-010/PR-007 |
| CS-04 | delete 前置下游引用检查 | 中 | 自建 OP 并列挂 | PR-010 |
| CS-05 | save 后发 BEC 事件 | 中 | 自建 OP (afterExecuteOperationTransaction) | PR-011/PR-010 |
| CS-06 | 分录扩展 + ID 生成 | 中 | modifyMeta + 自建 OP | PR-005 |
| CS-07 | 列表过滤定制 | 低 | 自建 ListPlugin 并列挂 | PR-001 |

---

## 二、CS-01：加自定义业务字段

**需求**：给主表或分录加"备注 / 生效日期 / 优先级"等自定义字段。

**前提检查**：
- `entitytype` + `appcombo` + `mainpermitem` 的 `isvCanModify=false`（scene_doc.json 实证），不要改这些
- 分录 `issyspreset` 字段 `isvCanModify=false`，不要改
- 主表 `initdatasource` 字段 `isvCanModify=false`，不要改

**步骤**：
1. `modifyMeta` 走 ISV 扩展元数据（不是直接改 `hrcs_permrelat` 标品元数据）
2. 字段类型选 `TextField` / `ComboField` / `CheckBoxField`（不要用 EmployeeField 或 HRMulPositionField — OpenAPI 不支持）
3. 如果加多语言字段（MuliLangTextField），平台自动建 `_l` 扩展表，ISV 不要手建
4. 加完后 `buildMeta` 建扩展表（不是标品物理表 `t_hrcs_permrelat` 上加列）

**危险**：
- 不要在标品元数据上 `modifyMeta add` 字段 — 违反 ISV 归属红线
- 不要在分录 `entitytypeid` / `app` / `permitem` 三字段上加联动（它们已被 `afterBindData` 的 `issyspreset` 锁定逻辑控制）

---

## 三、CS-02：加 propertyChanged 联动逻辑

**需求**：在字段改值时触发级联（如：选了某个自定义字段后自动填另一个字段）。

**实现**：
```java
// 父类：HRDataBaseEdit（不是 PermRelateEdit！）
public class HrcsPermrelatExtEdit extends HRDataBaseEdit {

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        if ("your_custom_field".equals(e.getProperty().getName())) {
            String newValue = (String) e.getChangeSet()[0].getNewValue();
            // PR-004: 必须 beginInit/endInit 防死循环
            IDataModel model = this.getModel();
            model.beginInit();
            model.setValue("another_field", computeValue(newValue));
            model.endInit();
            this.getView().updateView("another_field");
        }
    }
}
```

**实证参考**：PermRelateEdit.propertyChanged FP_CFB2（L375-L381）展示了 beginInit/endInit 模式。

**注意事项**：
- 不要在 `propertyChanged` 里直接 `getModel().setValue` 不加 beginInit 包裹 — 触发死循环
- 不要把 ISV 插件继承 `PermRelateEdit` — 走并列挂（PR-001）
- 注册时把自己的 FormPlugin 排在标品 PermRelateEdit 之前（PR-002 RowKey 顺序）

---

## 四、CS-03：save 前置 OP Validator

**需求**：保存前校验自定义字段（如：分录"优先级"必须 > 0）。

**实现**：
```java
// 父类：AbstractValidator
public class PriorityValidator extends AbstractValidator {
    @Override
    public void validate() {
        ExtendedDataEntity[] rows = this.getDataEntities();
        for (ExtendedDataEntity row : rows) {
            DynamicObject dy = row.getDataEntity();
            String priority = dy.getString("your_priority_field");
            if (priority != null && Integer.parseInt(priority) <= 0) {
                this.addErrorMessage(row, "优先级必须为正整数");
            }
        }
    }
}

// 自建 OP，在 onAddValidators 阶段注册
public class HrcsPermrelatExtSaveOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator(new PriorityValidator());
    }
}
```

**注意事项**：
- Validator 父类必须是 `kd.bos.entity.validate.AbstractValidator`（SDK 白名单注解 @SdkPublic）
- 不要在 `beforeExecuteOperationTransaction` 才注册 Validator — 晚了
- `issyspreset=true` 预置行已被标品锁定 entitytypeid/app/permitem 三字段 — ISV Validator 不要重复检查
- 不要继承 `HisUniqueValidateOp`（@SdkInternal · 本场景非 HisModel 不需要）

**调度**：挂到 `save` opKey 的 executionChain，排在 HRDataBaseOp 之前。

---

## 五、CS-04：delete 前置下游引用检查

**需求**：删除关联权限项前，检查是否有其他业务单据引用它。

**实现**：
1. 自建 OP 并列挂到 `delete` opKey 的 executionChain
2. 在 `beforeExecuteOperationTransaction` 阶段查 ISV 自建引用表
3. 发现引用 → `addErrorMessage` 阻止删除

**关键认知**：标品已经做了 `hrcs_permrelatcfg` 下游同步删（delete 后通过 PermRelateServiceHelper.deletePermRelateConfigs），但只覆盖标品下游。ISV 自建的下游引用表需要 ISV 自建检查。

---

## 六、CS-05：save 后发 BEC 事件

**需求**：关联权限项保存后通知下游系统（如：第三方权限系统同步）。

**背景**：grep 实证本场景标品 0 处发 BEC（`triggerEventSubscribe` / `IEventService` / `EventServiceHelper` 反编译三类全 0 命中）。与 `hjm_jobhr` 3 层异步发 BEC 不同（`feedback_bec_3layer_async_publish.md`）。

**实现**：
```java
public class HrcsPermrelatBecOp extends HRDataBaseOp {
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs args) {
        // 事务已提交 — 此时发 BEC 保证数据已落库
        IEventService svc = ...; // 获取 EventService
        KDBizEvent event = new KDBizEvent();
        event.setEventNumber("hrcs_permrelat_saved");
        // event.setEventData(...)
        svc.triggerEventSubscribeJobs(app, num, msg, vars);
    }
}
```

**重要警告**：
- 只能在 `afterExecuteOperationTransaction` 发（事务已提交 · 数据已落库）
- 不要在 `beforeExecuteOperationTransaction` 发（事务未提交 · 下游可能读到脏数据）
- 不要在 `afterExecuteOperationTransaction` 抛 `KDBizException` 期望回滚（事务已提交 · 抛了也回滚不了）
- 不要套用 `hjm_jobhr` 3 层异步发 BEC 模式 — 本场景不是异步任务型

**PR-011 建议**：BEC 是 ISV 自建功能 · 标品未发 · ISV 需要自建 OP 在 afterExecute 阶段调 `IEventService.triggerEventSubscribeJobs`。

---

## 七、CS-06：分录扩展 + ID 生成

**需求**：在分录子表新增自定义字段（如"优先级/截止日期"）+ 新增分录行时生成分布式 ID。

**步骤**：
1. `modifyMeta` 走 ISV 扩展元数据 — 在 `entryentity` 上加字段
2. `buildMeta` 建 ISV 扩展物理表
3. 新增行 ID 生成：

```java
// PR-005: ID 生成用 kd.bos.id.ID
long newId = ID.genLongId();
// 或者批量：
Long[] ids = ORM.create().genLongIds("hrcs_permrelat.entryentity", rowCount);
```

**危险**：
- 不要手拼 UUID 或自增 ID — 破坏苍穹分布式 ID 体系
- 不要在分录 `issyspreset` 上做任何 ISV 字段改动 — 系统预置行受 PR-007 保护

---

## 八、CS-07：列表过滤定制

**需求**：在列表页加默认过滤条件（如：只显示特定 BU 的关联权限项）。

**实现**：
```java
// 父类：HRDataBaseList（不是 PermRelateList！）
public class HrcsPermrelatExtList extends HRDataBaseList {
    @Override
    public void setFilter(BeforeSetFilterEventArgs args) {
        // 加 ISV 自定义过滤条件
        QFilter buFilter = new QFilter("entitytype", "in", myBuEntityIds);
        args.getFilter().and(buFilter);
    }
}
```

**注意事项**：
- 不要继承 `PermRelateList` — ISV 并列挂（PR-001）
- 确保你的 ListPlugin 排在标品 PermRelateList 之前（PR-002 RowKey 顺序）
- 不要覆盖 `PermItemProvider` 的替换逻辑 — 它在 `beforeCreateListDataProvider` 阶段被标品设置

---

## 九、不可扩展清单（反模式速查）

| 想做什么 | 为什么不行 | 替代路径 |
|---|---|---|
| 继承 PermRelateEdit | PR-001 违反 | extends HRDataBaseEdit · 并列挂 |
| 继承 PermRelateList | PR-001 违反 | extends HRDataBaseList · 并列挂 |
| 继承 HisModelOPCommonPlugin | @SdkInternal · 本场景不需要 | extends HRDataBaseOp |
| 继承 AbsOrgBaseOp | 非 HR 通用推荐 · 已禁 | extends HRDataBaseOp |
| 改 issyspreset 字段语义 | PR-007 违反 | 自建标志字段 |
| 改 entitytype 的 isvCanModify | scene_doc.json 标 false | 不改 · 用衍生字段 |
| 改 BU 一致性规则（允许跨 BU 关联） | 业务上灾难性 | 拆成多个 hrcs_permrelat 记录 |
| 改 hrcs_choose_permitem 子页面协议 | 私有 pk = "permId\|\|permName" 双竖杠 | 不改 · 重写整套子页面流 |
| 改 HRRelatePermTask 任务参数 | 全量同步影响所有角色 | ISV 自建任务 + ISV 自建 opKey |
| 在 F7 阶段用单竖杠 \| 分隔 permId/permName | 会被 split 误解析 | 用双竖杠 \|\| |
| pageId 不带 @parentPageId | 父子关联丢失 · 子页面无法回 | `<formId>@<parentPageId>` |

---

## 十、PR 引用索引

| PR | 适用 CS | 关键约束 |
|---|---|---|
| PR-001 | CS-01~07 | 并列挂插件 · 不继承场景专属类 |
| PR-002 | CS-02/CS-03/CS-07 | RowKey 执行顺序（ISV 插件排在标品前） |
| PR-003 | CS-02 | FormPlugin getModel().setValue 用法 |
| PR-004 | CS-02 | beginInit/endInit 死循环防护 |
| PR-005 | CS-06 | ID 生成用 kd.bos.id.ID |
| PR-007 | CS-03 | 预置数据 issyspreset 保护 |
| PR-010 | CS-03/CS-04/CS-05/CS-06 | OP 13 生命周期 |
| PR-011 | CS-05 | BEC 自建（标品 0 处） |
