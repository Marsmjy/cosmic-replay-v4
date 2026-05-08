# 编制管理 (headcount_management) · 扩展点（Customization Points）

> 53 个标准扩展点·按客户需求定制·**不改资产骨架**
> 数据源：deep_scan_class_tree.md（每类 → 1 EP）+ _asset_meta.yaml

## 扩展点总览

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 DateTimeCommon 行为 | DateTimeCommon.java | ⭐⭐ | (按需) |
| EP-02 | 改 HisAttachmentTool 行为 | HisAttachmentTool.java | ⭐⭐ | (按需) |
| EP-03 | 改 PlanCollectFromPlugin 行为 | PlanCollectFromPlugin.java | ⭐⭐ | (按需) |
| EP-04 | 改 PlanmonthBillBzFormPlugin 行为 | PlanmonthBillBzFormPlugin.java | ⭐⭐ | (按需) |
| EP-05 | 改 PlanmonthBillFormPlugin 行为 | PlanmonthBillFormPlugin.java | ⭐⭐ | (按需) |
| EP-06 | 改 PlanMonthFormPlugin 行为 | PlanMonthFormPlugin.java | ⭐⭐ | (按需) |
| EP-07 | 改 PlanPersonFormPlugin 行为 | PlanPersonFormPlugin.java | ⭐⭐ | (按需) |
| EP-08 | 改 PlanPersonsFromPlugin 行为 | PlanPersonsFromPlugin.java | ⭐⭐ | (按需) |
| EP-09 | 改 PlanyearBillFormPlugin 行为 | PlanyearBillFormPlugin.java | ⭐⭐ | (按需) |
| EP-10 | 改 PlanyearDetailFormPlugin 行为 | PlanyearDetailFormPlugin.java | ⭐⭐ | (按需) |
| EP-11 | 改 PlanyearDetailPCFormPlugin 行为 | PlanyearDetailPCFormPlugin.java | ⭐⭐ | (按需) |
| EP-12 | 改 PlanYearFormPlugin 行为 | PlanYearFormPlugin.java | ⭐⭐ | (按需) |
| EP-13 | 改 PlanyearSynPersonFormPlugin 行为 | PlanyearSynPersonFormPlugin.java | ⭐⭐ | (按需) |
| EP-14 | 改 CompilationPlanListPlugin 行为 | CompilationPlanListPlugin.java | ⭐⭐ | (按需) |
| EP-15 | 改 CompilationPlanMonthListPlugin 行为 | CompilationPlanMonthListPlugin.java | ⭐⭐ | (按需) |
| EP-16 | 改 PlanMonthAdminOrgTreeList 行为 | PlanMonthAdminOrgTreeList.java | ⭐⭐ | (按需) |
| EP-17 | 改 PlanMonthListPlugin 行为 | PlanMonthListPlugin.java | ⭐⭐ | (按需) |
| EP-18 | 改 PlanYearAdminOrgTreeList 行为 | PlanYearAdminOrgTreeList.java | ⭐⭐ | (按需) |
| EP-19 | 改 PlanyearDetailListPlugin 行为 | PlanyearDetailListPlugin.java | ⭐⭐ | (按需) |
| EP-20 | 改 AffirmPersonOp 行为 | AffirmPersonOp.java | ⭐⭐ | (按需) |
| EP-21 | 改 CompilationPlanMonthSaveOp 行为 | CompilationPlanMonthSaveOp.java | ⭐⭐ | (按需) |
| EP-22 | 改 CompilationPlanReviseOp 行为 | CompilationPlanReviseOp.java | ⭐⭐ | (按需) |
| EP-23 | 改 CompilationPlanSaveAndEnableOp 行为 | CompilationPlanSaveAndEnableOp.java | ⭐⭐ | (按需) |
| EP-24 | 改 CompilationPlanSaveOp 行为 | CompilationPlanSaveOp.java | ⭐⭐ | (按需) |
| EP-25 | 改 CompilationPlanUrgeOp 行为 | CompilationPlanUrgeOp.java | ⭐⭐ | (按需) |
| EP-26 | 改 DeletePlanDetailOp 行为 | DeletePlanDetailOp.java | ⭐⭐ | (按需) |
| EP-27 | 改 DeletePlanOp 行为 | DeletePlanOp.java | ⭐⭐ | (按需) |
| EP-28 | 改 LockPersonOp 行为 | LockPersonOp.java | ⭐⭐ | (按需) |
| EP-29 | 改 MonthBillCheckOp 行为 | MonthBillCheckOp.java | ⭐⭐ | (按需) |
| EP-30 | 改 MonthBillSaveOp 行为 | MonthBillSaveOp.java | ⭐⭐ | (按需) |
| EP-31 | 改 PlanMonthBzSaveOp 行为 | PlanMonthBzSaveOp.java | ⭐⭐ | (按需) |
| EP-32 | 改 PlanYearOp 行为 | PlanYearOp.java | ⭐⭐ | (按需) |
| EP-33 | 改 PlanYearStartOp 行为 | PlanYearStartOp.java | ⭐⭐ | (按需) |
| EP-34 | 改 PositionHrChangeOpPlugin 行为 | PositionHrChangeOpPlugin.java | ⭐⭐ | (按需) |
| EP-35 | 改 PositionValidator 行为 | PositionValidator.java | ⭐⭐ | (按需) |
| EP-36 | 改 SavePlanOp 行为 | SavePlanOp.java | ⭐⭐ | (按需) |
| EP-37 | 改 SubmitEffectOp 行为 | SubmitEffectOp.java | ⭐⭐ | (按需) |
| EP-38 | 改 SycnOnPersonOp 行为 | SycnOnPersonOp.java | ⭐⭐ | (按需) |
| EP-39 | 改 MonthReportExportTask 行为 | MonthReportExportTask.java | ⭐⭐ | (按需) |
| EP-40 | 改 PlanMonthBillCreateTask 行为 | PlanMonthBillCreateTask.java | ⭐⭐ | (按需) |
| EP-41 | 改 YearReportExportClickTask 行为 | YearReportExportClickTask.java | ⭐⭐ | (按需) |
| EP-42 | 改 YearReportExportTask 行为 | YearReportExportTask.java | ⭐⭐ | (按需) |
| EP-43 | 改 DeletePlanDetailValidator 行为 | DeletePlanDetailValidator.java | ⭐⭐ | (按需) |
| EP-44 | 改 DeletePlanValidator 行为 | DeletePlanValidator.java | ⭐⭐ | (按需) |
| EP-45 | 改 MonthBillCheckValidator 行为 | MonthBillCheckValidator.java | ⭐⭐ | (按需) |
| EP-46 | 改 MonthBillSaveValidator 行为 | MonthBillSaveValidator.java | ⭐⭐ | (按需) |
| EP-47 | 改 PlanYearSaveValidator 行为 | PlanYearSaveValidator.java | ⭐⭐ | (按需) |
| EP-48 | 改 PlanYearStartValidator 行为 | PlanYearStartValidator.java | ⭐⭐ | (按需) |
| EP-49 | 改 PlanPersonImportOp 行为 | PlanPersonImportOp.java | ⭐⭐ | (按需) |
| EP-50 | 改 BatchYearImportEdit 行为 | BatchYearImportEdit.java | ⭐⭐ | (按需) |
| EP-51 | 改 PlanImportFormPlugin 行为 | PlanImportFormPlugin.java | ⭐⭐ | (按需) |
| EP-52 | 改 PlanMonthBillBzFormPlugin 行为 | PlanMonthBillBzFormPlugin.java | ⭐⭐ | (按需) |
| EP-53 | 改 PlanMonthBillBzImportPlugin 行为 | PlanMonthBillBzImportPlugin.java | ⭐⭐ | (按需) |

---

## EP-01 · 改 DateTimeCommon 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DateTimeCommon.java`

### 改造方案

DateTimeCommon 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-02 · 改 HisAttachmentTool 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`HisAttachmentTool.java`

### 改造方案

HisAttachmentTool 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-03 · 改 PlanCollectFromPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanCollectFromPlugin.java`

### 改造方案

PlanCollectFromPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-04 · 改 PlanmonthBillBzFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanmonthBillBzFormPlugin.java`

### 改造方案

PlanmonthBillBzFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-05 · 改 PlanmonthBillFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanmonthBillFormPlugin.java`

### 改造方案

PlanmonthBillFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-06 · 改 PlanMonthFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthFormPlugin.java`

### 改造方案

PlanMonthFormPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-07 · 改 PlanPersonFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanPersonFormPlugin.java`

### 改造方案

PlanPersonFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-08 · 改 PlanPersonsFromPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanPersonsFromPlugin.java`

### 改造方案

PlanPersonsFromPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-09 · 改 PlanyearBillFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanyearBillFormPlugin.java`

### 改造方案

PlanyearBillFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-10 · 改 PlanyearDetailFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanyearDetailFormPlugin.java`

### 改造方案

PlanyearDetailFormPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-11 · 改 PlanyearDetailPCFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanyearDetailPCFormPlugin.java`

### 改造方案

PlanyearDetailPCFormPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-12 · 改 PlanYearFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearFormPlugin.java`

### 改造方案

PlanYearFormPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-13 · 改 PlanyearSynPersonFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanyearSynPersonFormPlugin.java`

### 改造方案

PlanyearSynPersonFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-14 · 改 CompilationPlanListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanListPlugin.java`

### 改造方案

CompilationPlanListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-15 · 改 CompilationPlanMonthListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanMonthListPlugin.java`

### 改造方案

CompilationPlanMonthListPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-16 · 改 PlanMonthAdminOrgTreeList 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthAdminOrgTreeList.java`

### 改造方案

PlanMonthAdminOrgTreeList 继承 AdminOrgTreeListTemplate·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-17 · 改 PlanMonthListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthListPlugin.java`

### 改造方案

PlanMonthListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-18 · 改 PlanYearAdminOrgTreeList 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearAdminOrgTreeList.java`

### 改造方案

PlanYearAdminOrgTreeList 继承 AdminOrgTreeListTemplate·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-19 · 改 PlanyearDetailListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanyearDetailListPlugin.java`

### 改造方案

PlanyearDetailListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-20 · 改 AffirmPersonOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`AffirmPersonOp.java`

### 改造方案

AffirmPersonOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-21 · 改 CompilationPlanMonthSaveOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanMonthSaveOp.java`

### 改造方案

CompilationPlanMonthSaveOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-22 · 改 CompilationPlanReviseOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanReviseOp.java`

### 改造方案

CompilationPlanReviseOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-23 · 改 CompilationPlanSaveAndEnableOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanSaveAndEnableOp.java`

### 改造方案

CompilationPlanSaveAndEnableOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-24 · 改 CompilationPlanSaveOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanSaveOp.java`

### 改造方案

CompilationPlanSaveOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-25 · 改 CompilationPlanUrgeOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CompilationPlanUrgeOp.java`

### 改造方案

CompilationPlanUrgeOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-26 · 改 DeletePlanDetailOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DeletePlanDetailOp.java`

### 改造方案

DeletePlanDetailOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-27 · 改 DeletePlanOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DeletePlanOp.java`

### 改造方案

DeletePlanOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-28 · 改 LockPersonOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`LockPersonOp.java`

### 改造方案

LockPersonOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-29 · 改 MonthBillCheckOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`MonthBillCheckOp.java`

### 改造方案

MonthBillCheckOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-30 · 改 MonthBillSaveOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`MonthBillSaveOp.java`

### 改造方案

MonthBillSaveOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-31 · 改 PlanMonthBzSaveOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthBzSaveOp.java`

### 改造方案

PlanMonthBzSaveOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-32 · 改 PlanYearOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearOp.java`

### 改造方案

PlanYearOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-33 · 改 PlanYearStartOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearStartOp.java`

### 改造方案

PlanYearStartOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-34 · 改 PositionHrChangeOpPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PositionHrChangeOpPlugin.java`

### 改造方案

PositionHrChangeOpPlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-35 · 改 PositionValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PositionValidator.java`

### 改造方案

PositionValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-36 · 改 SavePlanOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`SavePlanOp.java`

### 改造方案

SavePlanOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-37 · 改 SubmitEffectOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`SubmitEffectOp.java`

### 改造方案

SubmitEffectOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-38 · 改 SycnOnPersonOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`SycnOnPersonOp.java`

### 改造方案

SycnOnPersonOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-39 · 改 MonthReportExportTask 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`MonthReportExportTask.java`

### 改造方案

MonthReportExportTask 继承 AbstractTask·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-40 · 改 PlanMonthBillCreateTask 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthBillCreateTask.java`

### 改造方案

PlanMonthBillCreateTask 继承 AbstractTask·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-41 · 改 YearReportExportClickTask 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`YearReportExportClickTask.java`

### 改造方案

YearReportExportClickTask 继承 AbstractTaskClick·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-42 · 改 YearReportExportTask 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`YearReportExportTask.java`

### 改造方案

YearReportExportTask 继承 AbstractTask·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-43 · 改 DeletePlanDetailValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DeletePlanDetailValidator.java`

### 改造方案

DeletePlanDetailValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-44 · 改 DeletePlanValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DeletePlanValidator.java`

### 改造方案

DeletePlanValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-45 · 改 MonthBillCheckValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`MonthBillCheckValidator.java`

### 改造方案

MonthBillCheckValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-46 · 改 MonthBillSaveValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`MonthBillSaveValidator.java`

### 改造方案

MonthBillSaveValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-47 · 改 PlanYearSaveValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearSaveValidator.java`

### 改造方案

PlanYearSaveValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-48 · 改 PlanYearStartValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanYearStartValidator.java`

### 改造方案

PlanYearStartValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-49 · 改 PlanPersonImportOp 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanPersonImportOp.java`

### 改造方案

PlanPersonImportOp 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-50 · 改 BatchYearImportEdit 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BatchYearImportEdit.java`

### 改造方案

BatchYearImportEdit 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-51 · 改 PlanImportFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanImportFormPlugin.java`

### 改造方案

PlanImportFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-52 · 改 PlanMonthBillBzFormPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthBillBzFormPlugin.java`

### 改造方案

PlanMonthBillBzFormPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-53 · 改 PlanMonthBillBzImportPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PlanMonthBillBzImportPlugin.java`

### 改造方案

PlanMonthBillBzImportPlugin 继承 BatchImportPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)


---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 form 加字段 | PR-001·必须走 ext 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin before/after·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品自带 BEC·ISV 不重发·避免重复事件 |
| ❌ 改标品 form 的 billstatus / auditstatus | 标品状态机·ISV 不能动·只能加 `${ISV_FLAG}_*` 自定义字段 |
| ❌ 继承 ISV 自己的插件加 if/else 分支 | 双继承死循环（PR-001）·必须并列挂多 plugin |

详见 [`_antipatterns.json`](../_antipatterns.json)。

## 关联 PR 红线

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext 扩展元数据 ✅
- **PR-008/009** 时序资料·HisModel 查询要带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法
- **PR-011** BEC 事件中心·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)

## 关联文档

- 部署 SOP：[deploy_sop.md](deploy_sop.md)
- 业务规则：[02_business_rules.md](../../scenarios/<scene>/02_business_rules.md)
- 扩展点全景：[07_ext_points.md](../../scenarios/<scene>/07_ext_points.md)
- 定制方案：[06_customization_solutions.md](../../scenarios/<scene>/06_customization_solutions.md)
- W1 标杆：[contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)
