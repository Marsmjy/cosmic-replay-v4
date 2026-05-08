# homs_orgdifftemp · 模型设计

> **form**：`homs_orgdifftemp` · ERP组织同步工具
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**43**
- 必填字段：**7**
- 引用基础资料字段：**13**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `industrytype` | 行业类别 | `BasedataField` | `hbss_industrytype` |
| `parentorg` | 上级行政组织 | `BasedataField` | `homs_parentorgdifftemp` |
| `adminorgtype` | 行政组织类型 | `BasedataField` | `haos_adminorgtype` |
| `corporateorg` | 法律实体 | `BasedataField` | `hbss_lawentity` |
| `adminorglayer` | 管理层级 | `BasedataField` | `haos_adminorglayer` |
| `adminorgfunction` | 行政组织职能 | `BasedataField` | `haos_adminorgfunction` |
| `companyarea` | 国家地区 | `BasedataField` | `bd_country` |
| `city` | 所在城市 | `BasedataField` | `bd_admindivision` |
| `workplace` | 工作地 | `BasedataField` | `hbss_workplace` |
| `org` | 组织体系管理组织 | `BasedataField` | `bos_org` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
