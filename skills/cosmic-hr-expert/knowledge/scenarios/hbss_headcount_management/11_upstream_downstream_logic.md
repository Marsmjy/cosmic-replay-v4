# hbss_headcount_management · 上下游逻辑

> **协议**：ADR-009 跨云穿透架构
> **数据源**：deep_scan_audit.md A/E 段（form 清单 / QFilter 过滤）

## 上游（本场景的数据源）

### 直接上游（来自真扫的 form 引用）

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

### 查询过滤上下文

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `adminorg.id` | `QCP.equals` | PlanyearDetailFormPlugin.java:103, PlanyearDetailFormPlugin.java:105 |
| `adminorg.id` | `QCP.in` | PlanmonthBillFormPlugin.java:457, PlanyearBillFormPlugin.java:121, CompilationPlanListPlugin.java:107 |
| `billno` | `QCP.equals` | PlanmonthBillBzFormPlugin.java:339 |
| `createorg.id` | `QCP.equals` | PlanyearDetailFormPlugin.java:69, PlanyearDetailFormPlugin.java:71, PlanyearDetailFormPlugin.java:89 |
| `ctrlstrategy` | `QCP.equals` | PlanyearDetailFormPlugin.java:90 |
| `datastatus` | `QCP.equals` | PlanmonthBillFormPlugin.java:572, PlanyearBillFormPlugin.java:213, PlanyearSynPersonFormPlugin.java:254 |
| `employee.id` | `QCP.in` | SycnOnPersonOp.java:299, SycnOnPersonOp.java:703, YearReportExportTask.java:193 |
| `enable` | `QCP.equals` | PlanyearSynPersonFormPlugin.java:148, CompilationPlanUrgeOp.java:339, CompilationPlanUrgeOp.java:342 |
| `id` | `QCP.equals` | PlanPersonsFromPlugin.java:113, PlanyearBillFormPlugin.java:152, PlanyearSynPersonFormPlugin.java:85 |
| `id` | `QCP.in` | PlanmonthBillFormPlugin.java:90, PlanMonthListPlugin.java:113, PlanMonthListPlugin.java:125 |
| `id` | `QCP.not_equals` | PlanMonthListPlugin.java:277, PlanMonthListPlugin.java:287, PlanMonthListPlugin.java:297 |
| `iscurrentdata` | `QCP.equals` | PlanyearSynPersonFormPlugin.java:132, CompilationPlanUrgeOp.java:190, CompilationPlanUrgeOp.java:315 |
| `iscurrentversion` | `QCP.equals` | PlanmonthBillBzFormPlugin.java:136, PlanmonthBillBzFormPlugin.java:243, PlanmonthBillBzFormPlugin.java:256 |
| `islatestrecord` | `QCP.equals` | CompilationPlanUrgeOp.java:347, CompilationPlanUrgeOp.java:1030 |
| `joblevelscm.id` | `QCP.equals` | PlanyearDetailFormPlugin.java:75, YearReportExportTask.java:202 |
| `laborreltype.laborreltypecls.id` | `QCP.equals` | CompilationPlanUrgeOp.java:348, CompilationPlanUrgeOp.java:1031, PlanMonthBillCreateTask.java:225 |
| `number` | `QCP.equals` | PlanmonthBillBzFormPlugin.java:242 |
| `number` | `QCP.in` | CompilationPlanUrgeOp.java:1491 |
| `org.id` | `QCP.equals` | PlanmonthBillFormPlugin.java:103, PlanmonthBillFormPlugin.java:108, PlanmonthBillFormPlugin.java:116 |
| `person.number` | `QCP.in` | PlanPersonImportOp.java:34 |
| `role.number` | `QCP.in` | PlanmonthBillFormPlugin.java:80 |
| `structnumber` | `QCP.in` | PlanmonthBillBzFormPlugin.java:135, PlanmonthBillBzFormPlugin.java:256, PlanyearDetailFormPlugin.java:114 |
| `tdkw_adminorg2.number` | `QCP.in` | LockPersonOp.java:52 |
| `tdkw_bz_adminorg1` | `QCP.equals` | PlanMonthListPlugin.java:273 |
| `tdkw_bz_adminorg1` | `QCP.is_notnull` | PlanMonthListPlugin.java:272 |

## 下游（本场景的影响范围）

### 直接下游

补本资产写入哪些字段·影响哪些标品记录

### 跨云影响

补本资产是否触发跨云事件（薪酬/考勤/福利等）

## 跨云穿透矩阵

补具体跨云目标 / 触发链路 / 延迟 / 一致性

## ISV 扩展跨云联动建议

参考 W1 标杆模式（不直接发 BEC·靠标品自动派发）

## 关联资产

- 资产复刻：[`_assets/<ASSET_ID>/`](../../_assets/<ASSET_ID>/)
- 部署 SOP：[`_assets/<ASSET_ID>/deploy_sop.md`](../../_assets/<ASSET_ID>/deploy_sop.md)
- ADR-009：[../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md](../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md)
