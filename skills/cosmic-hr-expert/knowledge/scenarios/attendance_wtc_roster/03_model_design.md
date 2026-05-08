# attendance_wtc_roster · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `tdkw_config_properties` | CommonPropertiesQueryUtil.java:62 | — |
| `wtp_attstateinfo` | BatchBusiTripBillSubmitPlugin.java:47, RosterValidatorStorage.java:54, ArchivalInformationServiceImpl.java:17 等 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 0 |  |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 0 | — |
| cld/cldx (i18n) | 0 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑ Constant  [161 LOC]
  ↑ DateScope  [40 LOC]
  ↑ Constant  [118 LOC]
  ↑ CommonPropertiesQueryUtil  [138 LOC]
  ↑   [908 LOC]
  ↑ RosterValidatorStorage  [89 LOC implements RosterValidatorExtPlugin]
  ↑   [85 LOC]
  ↑   [14 LOC]
  ↑ ArchivalInformationServiceImpl  [28 LOC implements ArchivalInformationService]

AbstractValidator
  ↑ BatchBusiTripBillSubmitPlugin  [79 LOC]
  ↑ BusiTripBillSubmitPlugin  [88 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
