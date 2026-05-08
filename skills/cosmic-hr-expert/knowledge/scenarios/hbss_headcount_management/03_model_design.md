# hbss_headcount_management · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

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

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 25 | `homs_mplantreelist`, `homs_yplantreelist`, `plancollect`, `plancollect2`, `plancollect3`, `plancollect4`, `planmonth`, `planmonth_bill`, `planmonth_bill_base`, `planmonth_bz`... |
| schdata (调度) | 1 | `schedule_PlanMonthBillCreateTask_1766712390685.schdata` |
| SQL (预置数据) | 4 | — |
| cld/cldx (i18n) | 0 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑ DateTimeCommon  [248 LOC]
  ↑ HisAttachmentTool  [76 LOC]

AbstractFormPlugin
  ↑ PlanCollectFromPlugin  [411 LOC]
  ↑ PlanmonthBillBzFormPlugin  [558 LOC]
  ↑ PlanmonthBillFormPlugin  [586 LOC]
  ↑ PlanPersonFormPlugin  [64 LOC]
  ↑ PlanyearBillFormPlugin  [228 LOC]
  ↑ PlanyearSynPersonFormPlugin  [332 LOC]
  ↑ BatchYearImportEdit  [99 LOC]
  ↑ PlanImportFormPlugin  [95 LOC]
  ↑ PlanMonthBillBzFormPlugin  [93 LOC]

AbstractBillPlugIn
  ↑ PlanMonthFormPlugin  [112 LOC]
  ↑ PlanPersonsFromPlugin  [184 LOC]
  ↑ PlanyearDetailFormPlugin  [1195 LOC implements HyperLinkClickListener, TabSelectListener]
  ↑ PlanyearDetailPCFormPlugin  [23 LOC]
  ↑ PlanYearFormPlugin  [94 LOC]
  ↑ CompilationPlanMonthListPlugin  [70 LOC]

AbstractListPlugin
  ↑ CompilationPlanListPlugin  [352 LOC]
  ↑ PlanMonthListPlugin  [365 LOC]
  ↑ PlanyearDetailListPlugin  [58 LOC]

AdminOrgTreeListTemplate
  ↑ PlanMonthAdminOrgTreeList  [113 LOC]
  ↑ PlanYearAdminOrgTreeList  [97 LOC]

AbstractOperationServicePlugIn
  ↑ AffirmPersonOp  [378 LOC]
  ↑ CompilationPlanMonthSaveOp  [119 LOC]
  ↑ CompilationPlanReviseOp  [50 LOC]
  ↑ CompilationPlanSaveAndEnableOp  [124 LOC]
  ↑ CompilationPlanSaveOp  [34 LOC]
  ↑ CompilationPlanUrgeOp  [1754 LOC]
  ↑ DeletePlanDetailOp  [36 LOC]
  ↑ DeletePlanOp  [28 LOC]
  ↑ LockPersonOp  [135 LOC]
  ↑ MonthBillCheckOp  [35 LOC]
  ↑ MonthBillSaveOp  [30 LOC]
  ↑ PlanMonthBzSaveOp  [232 LOC]
  ↑ PlanYearOp  [29 LOC]
  ↑ PlanYearStartOp  [30 LOC]
  ↑ PositionHrChangeOpPlugin  [247 LOC]
  ↑ SavePlanOp  [83 LOC]
  ↑ SubmitEffectOp  [352 LOC]
  ↑ SycnOnPersonOp  [1157 LOC]
  ↑ PlanPersonImportOp  [65 LOC]

AbstractValidator
  ↑ PositionValidator  [30 LOC]
  ↑ DeletePlanDetailValidator  [77 LOC]
  ↑ DeletePlanValidator  [82 LOC]
  ↑ MonthBillCheckValidator  [208 LOC]
  ↑ MonthBillSaveValidator  [250 LOC]
  ↑ PlanYearSaveValidator  [64 LOC]
  ↑ PlanYearStartValidator  [65 LOC]

AbstractTask
  ↑ MonthReportExportTask  [99 LOC]
  ↑ PlanMonthBillCreateTask  [571 LOC]
  ↑ YearReportExportTask  [726 LOC]

AbstractTaskClick
  ↑ YearReportExportClickTask  [39 LOC]

BatchImportPlugin
  ↑ PlanMonthBillBzImportPlugin  [37 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
