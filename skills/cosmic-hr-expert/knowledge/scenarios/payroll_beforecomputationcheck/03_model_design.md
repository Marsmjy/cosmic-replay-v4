# payroll_beforecomputationcheck · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hpdi_bizdatabill` | CalPayPushPlugin.java:76 | — |
| `hpdi_bizdatabillent` | HsasCalpayRolltListPlugin.java:87 | — |
| `hsas_calperson` | HsasCalpayRolltListPlugin.java:74 | — |
| `tdkw_calpaypush` | HsasCalpayRolltListPlugin.java:93 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 3 | `calpaypush`, `hsas_calpayrollt_ext`, `hpdi_bizdatabill_ext` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 0 | — |
| cld/cldx (i18n) | 4 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑ AppflgConstant  [23 LOC]

AbstractFormPlugin
  ↑ CalPayPushPlugin  [177 LOC]

AbstractListPlugin
  ↑ HsasCalpayRolltListPlugin  [104 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
