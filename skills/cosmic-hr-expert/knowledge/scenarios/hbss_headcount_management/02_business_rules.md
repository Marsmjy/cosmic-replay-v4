# hbss_headcount_management · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_actual` | AffirmPersonOp.java:262 等 6 处 | `thisObj.get("tdkw_actual"` | — |
| `${ISV_FLAG}_add` | AffirmPersonOp.java:265 等 4 处 | `thisObj.get("tdkw_add"` | — |
| `${ISV_FLAG}_adminorg` | PlanPersonImportOp.java:54 | `adminorg` | — |
| `${ISV_FLAG}_adminorg1` | PlanyearSynPersonFormPlugin.java:169 等 3 处 | `obj` | — |
| `${ISV_FLAG}_adminorg2` | PlanyearSynPersonFormPlugin.java:171 等 3 处 | `obj` | — |
| `${ISV_FLAG}_adminorg3` | PlanyearSynPersonFormPlugin.java:173 等 3 处 | `obj` | — |
| `${ISV_FLAG}_adminorg4` | PlanyearSynPersonFormPlugin.java:175 等 3 处 | `obj` | — |
| `${ISV_FLAG}_adminorg5` | PlanyearSynPersonFormPlugin.java:177 等 3 处 | `obj` | — |
| `${ISV_FLAG}_adminorg6` | PlanyearSynPersonFormPlugin.java:179 等 3 处 | `obj` | — |
| `${ISV_FLAG}_all_bz1` | PlanMonthBzSaveOp.java:201 | `allBz1` | — |
| `${ISV_FLAG}_all_bz10` | PlanMonthBzSaveOp.java:210 | `allBz10` | — |
| `${ISV_FLAG}_all_bz11` | PlanMonthBzSaveOp.java:211 | `allBz11` | — |
| `${ISV_FLAG}_all_bz12` | PlanMonthBzSaveOp.java:212 | `allBz12` | — |
| `${ISV_FLAG}_all_bz2` | PlanMonthBzSaveOp.java:202 | `allBz2` | — |
| `${ISV_FLAG}_all_bz3` | PlanMonthBzSaveOp.java:203 | `allBz3` | — |
| `${ISV_FLAG}_all_bz4` | PlanMonthBzSaveOp.java:204 | `allBz4` | — |
| `${ISV_FLAG}_all_bz5` | PlanMonthBzSaveOp.java:205 | `allBz5` | — |
| `${ISV_FLAG}_all_bz6` | PlanMonthBzSaveOp.java:206 | `allBz6` | — |
| `${ISV_FLAG}_all_bz7` | PlanMonthBzSaveOp.java:207 | `allBz7` | — |
| `${ISV_FLAG}_all_bz8` | PlanMonthBzSaveOp.java:208 | `allBz8` | — |

## R-02 · 字段读取规则（核心业务读）

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `admin1` | PlanPersonsFromPlugin.java:138 | — |
| `admin2` | PlanPersonsFromPlugin.java:139 | — |
| `admin3` | PlanPersonsFromPlugin.java:140 | — |
| `admin4` | PlanPersonsFromPlugin.java:141 | — |
| `admin5` | PlanPersonsFromPlugin.java:142 | — |
| `admin6` | PlanPersonsFromPlugin.java:143 | — |
| `adminorg` | PlanPersonsFromPlugin.java:86, PlanyearSynPersonFormPlugin.java:88, PlanyearSynPersonFormPlugin.java:294 等 31 处 | — |
| `adminorglayer` | PlanmonthBillBzFormPlugin.java:92, PlanMonthAdminOrgTreeList.java:100, PlanMonthBillCreateTask.java:155 | — |
| `billno` | LockPersonOp.java:110 | — |
| `billstatus` | LockPersonOp.java:74, SavePlanOp.java:62 | — |
| `businessstatus` | PlanImportFormPlugin.java:78 | — |
| `datastatus` | BatchYearImportEdit.java:76, PlanImportFormPlugin.java:77, PlanMonthBillBzFormPlugin.java:76 | — |
| `dispatch` | PlanPersonsFromPlugin.java:174 | — |
| `dpt` | PlanPersonFormPlugin.java:41, PlanPersonFormPlugin.java:42, PlanPersonFormPlugin.java:50 | — |
| `enable` | CompilationPlanListPlugin.java:72, CompilationPlanListPlugin.java:101, CompilationPlanListPlugin.java:263 等 5 处 | — |
| `entryentity` | PlanPersonFormPlugin.java:38 | — |
| `fbasedataid` | PlanmonthBillFormPlugin.java:208, AffirmPersonOp.java:241, AffirmPersonOp.java:253 等 5 处 | — |
| `id` | PlanmonthBillBzFormPlugin.java:158, PlanmonthBillBzFormPlugin.java:250, PlanmonthBillBzFormPlugin.java:266 等 57 处 | — |
| `iscurrentversion` | BatchYearImportEdit.java:75, PlanImportFormPlugin.java:76, PlanMonthBillBzFormPlugin.java:75 | — |
| `ispartjob` | PlanPersonFormPlugin.java:41, PlanPersonFormPlugin.java:48 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

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

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：
- `auditstatus` 可能取值: A
- `billstatus` 可能取值: A, B, C, D, H, I
- `${ISV_FLAG}_bz_isinsert` 可能取值: 01
- `${ISV_FLAG}_is_org` 可能取值: 02
- `${ISV_FLAG}_is_syn` 可能取值: 01, 02
- `${ISV_FLAG}_isfreeze` 可能取值: 01
- `${ISV_FLAG}_islastorg` 可能取值: false, true

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
