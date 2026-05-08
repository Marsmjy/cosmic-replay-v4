# hbss_headcount_management · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

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

## 数据读取流向

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
