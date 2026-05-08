# core_hr_downermanfileword · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hlcm_contractsource` | FileConversionImpl.java:1412 | — |
| `hrpi_assignment` | ErmanFileReusePlugin.java:47, PersonFileDownloadFormPlugin.java:229, PersonFileDownloadRptPlugin.java:63 | — |
| `hrpi_personentityconf` | CustomPersonModelUtil.java:19 | — |
| `tdkw_hspm_wordconversion` | ErmanFileReusePlugin.java:100, PersonFileDownloadFormPlugin.java:268, PersonFileDownloadRptPlugin.java:101 | — |
| `tdkw_hspm_wordtempprin` | ErmanFileReusePlugin.java:51, PersonFileDownloadFormPlugin.java:235, PersonFileDownloadRptPlugin.java:68 | — |

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 5 | `hspm_assignmentl_ext`, `hspm_assignmentq_ext`, `hspm_wordconversion`, `hspm_wordtempprin`, `hrpi_assignment_ext` |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 1 | — |
| cld/cldx (i18n) | 4 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑ CustomFileHeadHelper  [196 LOC]
  ↑   [42 LOC]
  ↑ CustomPersonModelUtil  [125 LOC]
  ↑ CustomValueConvertHelper  [51 LOC]
  ↑   [47 LOC]
  ↑ FileConversionImpl  [1458 LOC implements FileConversionService]

AbstractListPlugin
  ↑ ErmanFileReusePlugin  [143 LOC]

AbstractFormPlugin
  ↑ PersonFileDownloadFormPlugin  [306 LOC]
  ↑ WordPrintSelectTemplatePlugin  [162 LOC]

AbstractReportFormPlugin
  ↑ PersonFileDownloadRptPlugin  [121 LOC]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
