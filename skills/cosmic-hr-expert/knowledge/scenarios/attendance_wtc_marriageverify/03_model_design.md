# attendance_wtc_marriageverify · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hrpi_employee` | MarriageDateDealUtil.java:41, MarriageDateDealUtil.java:59 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 10 | `wtabm_batchvaapp_ext`, `wtabm_batchvainf_ext`, `wtabm_batchvaupd_ext`, `wtabm_vaapplymob__ext`, `wtabm_vaapplysel_ext`, `wtabm_vaapply_ext`, `wtabm_vainfosing_ext`, `wtabm_vainfo_ext`, `wtabm_vaupdatese_ext`, `wtabm_vaupdate_ext` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 0 | — |
| cld/cldx (i18n) | 0 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

AbstractBillPlugIn
  ↑ MarriageVerifyPlugin  [169 LOC implements ItemClickListener, ClickListener]

AbstractMobBillPlugIn
  ↑ MobilleMarriageVerifyPlugin  [89 LOC]

AbstractValidator
  ↑ BatchMarriageDateValidator  [104 LOC]
  ↑ MarriageDateValidator  [105 LOC]

(无继承·工具/独立类)
  ↑ MarriageDateDealUtil  [157 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
