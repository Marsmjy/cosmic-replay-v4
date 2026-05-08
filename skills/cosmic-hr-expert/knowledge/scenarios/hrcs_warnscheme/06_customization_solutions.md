# hrcs_warnscheme · 定制方案（ISV 扩展真实模板）

> **form**：`hrcs_warnscheme`
> **生成时间**：2026-04-29
> **方法**：基于反编译实物的标品类，给出 ISV 真扩展模板（不是空壳骨架）

## CS-01 · 加 ISV 自定义字段

在开发平台给本 form（`hrcs_warnscheme`）加 ISV 字段：

```yaml
步骤:
  1. 设计器中打开本 form 元数据
  2. 添加 ISV 字段（建议加前缀如 _ext / _isv 标识私域）
  3. 字段类型避开 Required=true（防止数据迁入失败）
  4. 元数据扩展归属为 ISV · 不破坏标品 form （PR-001）
```

## CS-03 · 在 save 链加 ISV 业务（after / begin）

```java
@SdkPlugin
public class MyWarnschemeAfterOp extends HRDataBaseOp {
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

标品 `WarningSceneReceiverEdit` 已实现：
- `registerListener` (L116)
- `afterBindData` (L120)
- `propertyChanged` (L132)
- `beforeDoOperation` (L176)
- `afterDoOperation` (L214)

ISV **并列挂**（PR-001 · 不要继承标品 Edit）：

```java
@SdkPlugin
public class MyWarnschemeIsvFormPlugin extends HRDataBaseEdit {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // ISV UI 联动
    }
}
```

## CS-05 · 调用本场景业务 Service

本场景反编译类调用的 Service / Helper（7 个 · 仅前 10）：

- `FieldDefineService`
- `HRBaseServiceHelper`
- `HRWarnPreSQLHelper`
- `InteService`
- `MsgConfigService`
- `WarnSchemeBaseConditionValidator`
- `WarnSchemeValidator`

> ⚠️ 直接调这些 Service 前必须确认 SDK 注解（@SdkPublic / @SdkPlugin / @SdkService 三大白名单之一）· 详见 [cosmic_sdk_annotation_whitelist.md](../../../../C:/Users/kingdee/.claude/projects/d--aiworkspace-cludecodeworkspace/memory/cosmic_sdk_annotation_whitelist.md)

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 模板基于：admin_org_quick_maintenance（金标准）+ 反编译实证
- 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段