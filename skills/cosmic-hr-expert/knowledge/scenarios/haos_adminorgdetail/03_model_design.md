# haos_adminorgdetail · 模型设计

> **form**：`haos_adminorgdetail` · 组织快速维护
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**85**
- 必填字段：**9**
- 引用基础资料字段：**25**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `industrytype` | 行业类别 | `BasedataField` | `hbss_industrytype` |
| `adminorgtype` | 行政组织类型 | `BasedataField` | `haos_adminorgtype` |
| `parentorg` | 上级行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `corporateorg` | 法律实体 | `BasedataField` | `hbss_lawentity` |
| `adminorgfunction` | 行政组织职能 | `BasedataField` | `haos_adminorgfunction` |
| `companyarea` | 国家/地区 | `BasedataField` | `bd_country` |
| `city` | 所在城市 | `BasedataField` | `bd_admindivision` |
| `belongdept` | 所属部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `belongcompany` | 所属公司 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `workplace` | 工作地 | `BasedataField` | `hbss_workplace` |
| `adminorglayer` | 管理层级 | `BasedataField` | `haos_adminorglayer` |
| `org` | 组织体系管理组织 | `OrgField` | `bos_org` |
| `otclassify` | 组织分类 | `BasedataField` | `haos_otclassify` |
| `project` | 所属项目 | `BasedataField` | `bd_project` |
| `belongadminorg` | 所属行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `changescene` | 变动场景 | `BasedataField` | `haos_changescene` |
| `changereason` | 变动原因 | `BasedataField` | `haos_orgchangereason` |
| `changetype` | 变动类型 | `BasedataField` | `haos_orgchangetype` |
| `coopreltype` | 协作类型 | `BasedataField` | `haos_teamcoopreltype` |
| `cooporgteam` | 协作组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `struct_project` | 矩阵架构 | `BasedataField` | `haos_structproject` |
| `struct_parent_org` | 上级组织 | `BasedataField` | `haos_virtualorg_f7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
