# lcs_costadaption · 模型设计

> **form**：`lcs_costadaption` · 人力成本维度方案
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**33**
- 必填字段：**7**
- 引用基础资料字段：**10**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `createorg` | 创建组织 | `OrgField` | `bos_org` |
| `org` | 管理组织 | `OrgField` | `bos_org` |
| `useorg` | 使用组织 | `OrgField` | `bos_org` |
| `srccreateorg` | 原创建组织 | `OrgField` | `bos_org` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `country` | 国家/地区 | `BasedataField` | `bd_country` |
| `coststru` | 人力成本维度组合 | `BasedataField` | `lcs_coststru` |
| `currency` | 成本承担币种 | `BasedataField` | `bd_currency` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `CostAdaptionEdit` | `HRDataBaseEdit` | Edit/Form |
| `CostAdaptionList` | `HRDataBaseList` | List |
| `CostAdaptionSaveOp` | `HRDataBaseOp` | OP |
| `CostAdaptionSubmitOp` | `HRDataBaseOp` | OP |
| `CostAdaptionUnAuditOp` | `HRDataBaseOp` | OP |
