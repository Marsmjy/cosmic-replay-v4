# core_hr_emp_super_rel_chart · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hbpm_reportcoreltype` | EmpSuprelChartPlugin.java:255 | — |
| `hbpm_reportreltype` | EmpSuprelChartPlugin.java:279 | — |
| `hrpi_employeenewf7query` | EmpSuprelChartPlugin.java:236, EmpSuprelChartPlugin.java:726 | — |
| `hrpi_empposorgrel` | EmpSuprelChartPlugin.java:710 | — |
| `hrpi_empsuprel` | EmpSuprelChartPlugin.java:674, EmpSuprelChartPlugin.java:685 | — |
| `hrpi_percontact` | EmpSuprelChartPlugin.java:744 | — |
| `hrpi_person` | EmpSuprelChartPlugin.java:295 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 3 | `chart_apphome`, `empsuprel`, `hspm_ermanfile` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 2 | — |
| cld/cldx (i18n) | 2 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

HRDynamicFormBasePlugin
  ↑ EmpSuprelChartPlugin  [765 LOC]

TreeNode
  ↑ EmpSuprelChartTreeNode  [85 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
