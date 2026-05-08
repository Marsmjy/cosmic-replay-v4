# hbss_headcount_management · 能力边界

> **资产名**：编制管理
> **数据源**：deep_scan_inventory.md / deep_scan_audit.md A/G 段

## 场景定位

| 项 | 值 |
|---|---|
| 所属云 / 应用 | hr_hrmp / hbss |
| 主 form | `hbss_headcount` |
| ISV 包前缀 | `tdkw.odc.homs` |
| Java 业务文件数 | 53 |

## 类清单（继承自苍穹白名单基类）

- `DateTimeCommon` (DateTimeCommon.java) extends `(独立)`
- `HisAttachmentTool` (HisAttachmentTool.java) extends `(独立)`
- `PlanCollectFromPlugin` (PlanCollectFromPlugin.java) extends `AbstractFormPlugin`
- `PlanmonthBillBzFormPlugin` (PlanmonthBillBzFormPlugin.java) extends `AbstractFormPlugin`
- `PlanmonthBillFormPlugin` (PlanmonthBillFormPlugin.java) extends `AbstractFormPlugin`
- `PlanMonthFormPlugin` (PlanMonthFormPlugin.java) extends `AbstractBillPlugIn`
- `PlanPersonFormPlugin` (PlanPersonFormPlugin.java) extends `AbstractFormPlugin`
- `PlanPersonsFromPlugin` (PlanPersonsFromPlugin.java) extends `AbstractBillPlugIn`
- `PlanyearBillFormPlugin` (PlanyearBillFormPlugin.java) extends `AbstractFormPlugin`
- `PlanyearDetailFormPlugin` (PlanyearDetailFormPlugin.java) extends `AbstractBillPlugIn`
- `PlanyearDetailPCFormPlugin` (PlanyearDetailPCFormPlugin.java) extends `AbstractBillPlugIn`
- `PlanYearFormPlugin` (PlanYearFormPlugin.java) extends `AbstractBillPlugIn`
- `PlanyearSynPersonFormPlugin` (PlanyearSynPersonFormPlugin.java) extends `AbstractFormPlugin`
- `CompilationPlanListPlugin` (CompilationPlanListPlugin.java) extends `AbstractListPlugin`
- `CompilationPlanMonthListPlugin` (CompilationPlanMonthListPlugin.java) extends `AbstractBillPlugIn`
- `PlanMonthAdminOrgTreeList` (PlanMonthAdminOrgTreeList.java) extends `AdminOrgTreeListTemplate`
- `PlanMonthListPlugin` (PlanMonthListPlugin.java) extends `AbstractListPlugin`
- `PlanYearAdminOrgTreeList` (PlanYearAdminOrgTreeList.java) extends `AdminOrgTreeListTemplate`
- `PlanyearDetailListPlugin` (PlanyearDetailListPlugin.java) extends `AbstractListPlugin`
- `AffirmPersonOp` (AffirmPersonOp.java) extends `AbstractOperationServicePlugIn`
- `CompilationPlanMonthSaveOp` (CompilationPlanMonthSaveOp.java) extends `AbstractOperationServicePlugIn`
- `CompilationPlanReviseOp` (CompilationPlanReviseOp.java) extends `AbstractOperationServicePlugIn`
- `CompilationPlanSaveAndEnableOp` (CompilationPlanSaveAndEnableOp.java) extends `AbstractOperationServicePlugIn`
- `CompilationPlanSaveOp` (CompilationPlanSaveOp.java) extends `AbstractOperationServicePlugIn`
- `CompilationPlanUrgeOp` (CompilationPlanUrgeOp.java) extends `AbstractOperationServicePlugIn`
- `DeletePlanDetailOp` (DeletePlanDetailOp.java) extends `AbstractOperationServicePlugIn`
- `DeletePlanOp` (DeletePlanOp.java) extends `AbstractOperationServicePlugIn`
- `LockPersonOp` (LockPersonOp.java) extends `AbstractOperationServicePlugIn`
- `MonthBillCheckOp` (MonthBillCheckOp.java) extends `AbstractOperationServicePlugIn`
- `MonthBillSaveOp` (MonthBillSaveOp.java) extends `AbstractOperationServicePlugIn`
- `PlanMonthBzSaveOp` (PlanMonthBzSaveOp.java) extends `AbstractOperationServicePlugIn`
- `PlanYearOp` (PlanYearOp.java) extends `AbstractOperationServicePlugIn`
- `PlanYearStartOp` (PlanYearStartOp.java) extends `AbstractOperationServicePlugIn`
- `PositionHrChangeOpPlugin` (PositionHrChangeOpPlugin.java) extends `AbstractOperationServicePlugIn`
- `PositionValidator` (PositionValidator.java) extends `AbstractValidator`
- `SavePlanOp` (SavePlanOp.java) extends `AbstractOperationServicePlugIn`
- `SubmitEffectOp` (SubmitEffectOp.java) extends `AbstractOperationServicePlugIn`
- `SycnOnPersonOp` (SycnOnPersonOp.java) extends `AbstractOperationServicePlugIn`
- `MonthReportExportTask` (MonthReportExportTask.java) extends `AbstractTask`
- `PlanMonthBillCreateTask` (PlanMonthBillCreateTask.java) extends `AbstractTask`
- `YearReportExportClickTask` (YearReportExportClickTask.java) extends `AbstractTaskClick`
- `YearReportExportTask` (YearReportExportTask.java) extends `AbstractTask`
- `DeletePlanDetailValidator` (DeletePlanDetailValidator.java) extends `AbstractValidator`
- `DeletePlanValidator` (DeletePlanValidator.java) extends `AbstractValidator`
- `MonthBillCheckValidator` (MonthBillCheckValidator.java) extends `AbstractValidator`
- `MonthBillSaveValidator` (MonthBillSaveValidator.java) extends `AbstractValidator`
- `PlanYearSaveValidator` (PlanYearSaveValidator.java) extends `AbstractValidator`
- `PlanYearStartValidator` (PlanYearStartValidator.java) extends `AbstractValidator`
- `PlanPersonImportOp` (PlanPersonImportOp.java) extends `AbstractOperationServicePlugIn`
- `BatchYearImportEdit` (BatchYearImportEdit.java) extends `AbstractFormPlugin`
- `PlanImportFormPlugin` (PlanImportFormPlugin.java) extends `AbstractFormPlugin`
- `PlanMonthBillBzFormPlugin` (PlanMonthBillBzFormPlugin.java) extends `AbstractFormPlugin`
- `PlanMonthBillBzImportPlugin` (PlanMonthBillBzImportPlugin.java) extends `BatchImportPlugin`

## 涉及 form 清单（来自真扫）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `bos_user` | CompilationPlanUrgeOp.java:1490 | — |
| `haos_adminorg` | PlanmonthBillBzFormPlugin.java:133, PlanmonthBillBzFormPlugin.java:244, PlanmonthBillBzFormPlugin.java:257 等 | — |
| `hbjm_joblevelhr` | YearReportExportTask.java:203 | — |
| `hbjm_joblevelscmhr` | PlanyearDetailFormPlugin.java:72 | — |
| `hjm_jobhr` | PositionHrChangeOpPlugin.java:108 | — |
| `homs_position` | PositionHrChangeOpPlugin.java:104, PositionHrChangeOpPlugin.java:114 | — |
| `hrpi_empposorgrel` | PlanyearSynPersonFormPlugin.java:138, CompilationPlanUrgeOp.java:195, CompilationPlanUrgeOp.java:866 | — |
| `perm_userrole` | PlanmonthBillFormPlugin.java:81 | — |
| `tdkw_plan_persons` | PlanyearDetailFormPlugin.java:1085, PlanyearSynPersonFormPlugin.java:216 | — |
| `tdkw_plancollect` | PlanyearDetailFormPlugin.java:445 | — |
| `tdkw_plancollect2` | PlanyearDetailFormPlugin.java:447 | — |
| `tdkw_plancollect3` | PlanyearDetailFormPlugin.java:449 | — |
| `tdkw_plancollect4` | PlanyearDetailFormPlugin.java:451 | — |
| `tdkw_planmonth` | PlanMonthBillCreateTask.java:103 | — |
| `tdkw_planmonth_bill` | PlanmonthBillBzFormPlugin.java:339, PlanmonthBillFormPlugin.java:222, PlanMonthFormPlugin.java:65 等 | — |
| `tdkw_planmonth_bz` | PlanmonthBillBzFormPlugin.java:324, PlanmonthBillBzFormPlugin.java:413, PlanmonthBillFormPlugin.java:415 等 | — |
| `tdkw_planperson` | CompilationPlanUrgeOp.java:1484 | — |
| `tdkw_planyear_bill` | CompilationPlanListPlugin.java:79, CompilationPlanListPlugin.java:82, CompilationPlanUrgeOp.java:111 等 | — |
| `tdkw_planyear_detail` | PlanyearBillFormPlugin.java:79, PlanyearBillFormPlugin.java:92, PlanyearBillFormPlugin.java:152 等 | — |
| `tdkw_syn_person` | PlanyearBillFormPlugin.java:144, PlanyearDetailFormPlugin.java:482 | — |

## 边界（脑补防火墙）

## G · 关键否定型事实（脑补防火墙）

⚡ **待精修**：人工补"代码里没看到的功能"·防 LLM 脑补：

- ❌ "<待人工补>·如：'本资产没用调度任务'"
- ❌ "<待人工补>·如：'本资产没发 BEC 跨云事件'"

## SDK 白名单合规

详见 [`deep_scan_forbidden_check.md`](../../../<skills-dir>/cosmic-hr-expert/dcs_regression/passed/case_010_headcount_management/deep_scan_forbidden_check.md)·走苍穹白名单基类·无内部 API 调用。

## 关联资产 / 模板

- 资产复刻：[`_assets/`](../../_assets/)
- Phase D 真扫：[`dcs_regression/`](../../../dcs_regression/)
