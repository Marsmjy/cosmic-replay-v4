# 上下游联动 · wtp_attfilebase

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（chgaction + Service 调用 + 跨云引用 · v2）
> **数据源**: 反编译 OP 类 · `_cross_cloud_index.json` · `cosmic_hr_chgaction_mechanism.md`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品流转链路

## 🟡 同 form 维度的下游联动（待人工补充）

<TODO 人工补> 本场景往下游传播什么数据：

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| 薪酬云 (payroll) | `<TODO>` | | |
| 考勤云 (attendance) | `<TODO>` | | |
| 绩效云 (performance) | `<TODO>` | | |
| 福利云 (welfare) | `<TODO>` | | |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-05-02
> 本 form（`wtp_attfilebase`，所属 考勤云）引用了其他云的 **14** 个底座实体：

### ⬆️ 核心人力云（`core_hr`）3 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `employee` | 员工 | EmployeeField | `hrpi_employeenewf7query` | — |
| `empposorgrel` | 关联任职 | EmployeeField | `hrpi_empposf7query` | — |
| `assignment` | 组织分配 | EmployeeField | `hrpi_assignmentf7query` | — |

### ⬆️ HR 基础服务云（`hr_hrmp`）5 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `workplace` | 考勤地点 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `agreedlocation` | 协议工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `postype` | 任职类型 | BasedataField | `hbss_postype` | [hbss_position_dict](../hbss_position_dict/) |
| `hrworkplace` | 常驻工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `persongroup` | 员工组 | BasedataField | `hbss_employeegroup` | [hbss_labor_rel](../hbss_labor_rel/) |

### ⬆️ 组织发展云（`org_dev`）5 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `adminorg` | 行政组织 | HRAdminOrgField | `haos_adminorghrf7` | — |
| `affiliateadminorg` | 挂靠行政组织 | HRAdminOrgField | `haos_adminorghrf7` | — |
| `company` | 所属公司 | HRAdminOrgField | `haos_adminorghrf7` | — |
| `position` | 岗位 | HRPositionField | `hbpm_positionhrf7` | — |
| `job` | 职位 | BasedataField | `hbjm_jobhr` | [hjm_jobhr_maintenance](../hjm_jobhr_maintenance/) |

### ⬆️ unknown（`unknown`）1 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `empgroup` | 考勤档案分组 | BasedataField | `hbss_empgroup` | — |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

<!-- END cross-cloud-upstream -->