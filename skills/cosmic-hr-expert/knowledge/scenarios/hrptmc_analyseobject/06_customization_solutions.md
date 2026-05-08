# hrptmc_analyseobject · 定制方案（ISV 扩展真实模板）

> **form**：`hrptmc_analyseobject`
> **生成时间**：2026-04-29
> **方法**：基于反编译实物的标品类，给出 ISV 真扩展模板（不是空壳骨架）

## CS-01 · 加 ISV 自定义字段

在开发平台给本 form（`hrptmc_analyseobject`）加 ISV 字段：

```yaml
步骤:
  1. 设计器中打开本 form 元数据
  2. 添加 ISV 字段（建议加前缀如 _ext / _isv 标识私域）
  3. 字段类型避开 Required=true（防止数据迁入失败）
  4. 元数据扩展归属为 ISV · 不破坏标品 form （PR-001）
```

## CS-02 · 加 ISV 校验器（save 前业务校验）

标品 `ReportAnalyseObjectOp` 在 `onAddValidators` 注入了：
- `ReportAnalyseObjectValidator`

ISV 模板（参考 admin_org_quick_maintenance / hrcs_datarule 等已沉淀模式）：

```java
// ISV 自定义 OP 插件 · 通过 @SdkPlugin 并列挂（PR-001）
@SdkPlugin
public class MyAnalyseobjectIsvOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);  // 不破坏标品 Validator 链
        e.addValidator(new MyIsvValidator());
    }
}

@SdkPlugin
public class MyIsvValidator extends HRDataBaseValidator {
    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.dataEntities) {
            DynamicObject obj = row.getDataEntity();
            // ISV 业务校验逻辑
        }
    }
}
```

## CS-03 · 在 save 链加 ISV 业务（after / begin）

⚠️ **重要**：标品 OP 在 `afterExecuteOperationTransaction` 派 sch_task 做异步处理：
- `kd.hr.hrptmc.business.anobj.AnalyseObjectDimCountTask`

> ISV 在 `afterExecuteOperationTransaction` 里跑业务时，标品异步任务可能还没起。建议：
> - 同步业务放 `beginOperationTransaction`（事务内）
> - 强一致依赖标品异步结果时 → 不能放 `afterExecute` · 要订阅 sch_task 完成事件

```java
@SdkPlugin
public class MyAnalyseobjectAfterOp extends HRDataBaseOp {
    @Override
    public void beginOperationTransaction(BeginOperationTransactionArgs e) {
        super.beginOperationTransaction(e);
        // 同步业务（事务内）
    }
    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        // 异步业务（事务已提交 · 数据已落库）
    }
}
```

## CS-04 · 加 ISV FormPlugin（UI 联动）

标品 `AnalyseObjectCalculateConfigEdit` 已实现：

ISV **并列挂**（PR-001 · 不要继承标品 Edit）：

```java
@SdkPlugin
public class MyAnalyseobjectIsvFormPlugin extends HRDataBaseEdit {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // ISV UI 联动
    }
}
```

## CS-05 · 调用本场景业务 Service

本场景反编译类调用的 Service / Helper（5 个 · 仅前 10）：

- `AnalyseObjectService`
- `CalculateFieldService`
- `HRBaseServiceHelper`
- `HRReportPreSQLHelper`
- `ReportAnalyseObjectValidator`

> ⚠️ 直接调这些 Service 前必须确认 SDK 注解（@SdkPublic / @SdkPlugin / @SdkService 三大白名单之一）· 详见 [cosmic_sdk_annotation_whitelist.md](../../../../C:/Users/kingdee/.claude/projects/d--aiworkspace-cludecodeworkspace/memory/cosmic_sdk_annotation_whitelist.md)

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 模板基于：admin_org_quick_maintenance（金标准）+ 反编译实证
- 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段