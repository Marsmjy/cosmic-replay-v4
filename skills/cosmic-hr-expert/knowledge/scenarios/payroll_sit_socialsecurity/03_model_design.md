# payroll_sit_socialsecurity · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `bos_org` | SocialSecurityPersonFilterFormPlugin.java:56, PersonOrgUtil.java:18, PersonOrgUtil.java:33 | — |
| `haos_adminorghr` | PersonOrgUtil.java:22, PersonOrgUtil.java:28 | — |
| `hcsi_sibase` | SinsurfileHelper.java:187, SinsurfileHelper.java:254, SinsurfileHelper.java:310 等 | — |
| `hcsi_sinsurfile` | SocialSecurityPaybillListPlugin.java:58, SocialSecurityPayClickNumberPlugin.java:44, SocialSecurityPayFormPlugin.java:475 等 | — |
| `hrpi_empjobrel` | SocialSecurityPayFormPlugin.java:700, SocialSecurityPersonFilterFormPlugin.java:170 | — |
| `hrpi_employee` | SocialSecurityPayFormPlugin.java:715 | — |
| `hrpi_empposorgrel` | SocialSecurityPayFormPlugin.java:78, SocialSecurityPayFormPlugin.java:644 | — |
| `hrpi_percre` | SocialSecurityPayFormPlugin.java:659 | — |
| `hsbs_empentrel` | SocialSecurityPayFormPlugin.java:737 | — |
| `sitbs_insuranceprop` | SinsurfileHelper.java:548 | — |
| `sitbs_sinsurstd` | SocialSecurityPayValidator.java:63 | — |
| `sitbs_welfaretype` | SinsurfileHelper.java:533 | — |
| `tdkw_payment_service_type` | SocialSecurityPaybillListPlugin.java:76 | — |
| `tdkw_social_security` | SocialSecurityPaybillListPlugin.java:67, SocialSecurityPaybillListPlugin.java:149, SocialSecurityPayFormPlugin.java:535 | — |
| `tdkw_social_security_fund` | SocialSecurityPersonFilterFormPlugin.java:178 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 4 | `hcsi_sibase_ext`, `payment_service_type`, `social_security`, `social_security_fund` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 1 | — |
| cld/cldx (i18n) | 0 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑   [24 LOC]
  ↑ SinsurfileHelper  [692 LOC]
  ↑ SITEntityConstants  [610 LOC]
  ↑ SocialSecurityFundCommon  [60 LOC]
  ↑ OperateResultCheck  [39 LOC]
  ↑ PersonOrgUtil  [35 LOC]
  ↑ StringHelper  [172 LOC]
  ↑ TypeConvertUtil  [344 LOC]

AbstractBillPlugIn
  ↑ SocialSecurityFundFormPlugin  [227 LOC implements Plugin, BeforeF7SelectListener]
  ↑ SocialSecurityPayFormPlugin  [932 LOC implements Plugin]
  ↑ SocialSecurityPersonFilterFormPlugin  [319 LOC implements Plugin, BeforeF7SelectListener, AfterF7SelectListener]

AbstractListPlugin
  ↑ SocialSecurityFundListPlugin  [19 LOC implements Plugin]
  ↑ SocialSecurityPaybillListPlugin  [157 LOC implements Plugin]

AbstractFormPlugin
  ↑ SocialSecurityPayClickNumberPlugin  [114 LOC]

AbstractOperationServicePlugIn
  ↑ SocialSecurityPayOperationPlugin  [157 LOC]

AbstractValidator
  ↑ SocialSecurityPayValidator  [224 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
