# org_dev_adminorgbill_extension · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `haos_adminorgdetail` | OrgBatchBillInfoHelper.java:22, OrgBatchBillInfoHelper.java:29, OrgBatchBillInfoHelper.java:36 | — |
| `homs_batchorgentity` | AdminOrgShowMobBillPlugin.java:60, OrgBatchBillSubmitDealSourceInfoOp.java:35 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 6 | `homs_batchorgent_ext`, `homs_batchorgen_ext1`, `homs_batchorgen_ext2`, `homs_orgbatchchg_ext`, `orgbatchchgmobile`, `haos_adminorgdet_ext` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 0 | — |
| cld/cldx (i18n) | 0 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

AbstractFormPlugin
  ↑ AdminOrgBatchDetailDealSourceInfoFormPlugin  [215 LOC]

AbstractMobBillPlugIn
  ↑ AdminOrgShowMobBillPlugin  [716 LOC]

HRCoreBaseBillOp
  ↑ OrgBatchBillSubmitDealSourceInfoOp  [60 LOC]

(无继承·工具/独立类)
  ↑   [97 LOC]
  ↑ OrgBatchBillInfoHelper  [41 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
