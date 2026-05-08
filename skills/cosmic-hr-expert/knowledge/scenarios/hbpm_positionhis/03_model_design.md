# hbpm_positionhis · 模型设计

> **form**：`hbpm_positionhis` · 岗位历史查询
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**80**
- 必填字段：**7**
- 引用基础资料字段：**29**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `enabler` | 启用人 | `UserField` | `bos_user` |
| `diplomareq` | 最低学历要求 | `BasedataField` | `hbss_diploma` |
| `changeoperate` | 变动操作 | `BasedataField` | `hbpm_changeoperate` |
| `changescene` | 变动场景 | `BasedataField` | `hbpm_changescene` |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `parent` | 上级岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `job` | 职位 | `BasedataField` | `hbjm_jobhr` |
| `positiontype` | 岗位类型 | `BasedataField` | `hbpm_positiontype` |
| `lowjobgrade` | 最低职等 | `BasedataField` | `hbjm_jobgradehr` |
| `highjobgrade` | 最高职等 | `BasedataField` | `hbjm_jobgradehr` |
| `lowjoblevel` | 最低职级 | `BasedataField` | `hbjm_joblevelhr` |
| `highjoblevel` | 最高职级 | `BasedataField` | `hbjm_joblevelhr` |
| `jobscm` | 职位体系方案 | `BasedataField` | `hbjm_jobscmhr` |
| `changetype` | 变动类型 | `BasedataField` | `hbpm_changetype` |
| `changedesc` | 变动原因 | `BasedataField` | `hbpm_changereason` |
| `countryregion` | 国家地区 | `BasedataField` | `bd_country` |
| `workplace` | 工作地 | `BasedataField` | `hbss_workplace` |
| `city` | 所在城市 | `BasedataField` | `bd_admindivision` |
| `orgdesignbu` | 职位体系管理组织 | `OrgField` | `bos_org` |
| `org` | 组织体系管理组织 | `OrgField` | `bos_org` |
| `positiontpl` | 岗位模板 | `BasedataField` | `hbpm_positiontpl` |
| `jobgradescm` | 职等方案 | `BasedataField` | `hbjm_jobgradescmhr` |
| `joblevelscm` | 职级方案 | `BasedataField` | `hbjm_joblevelscmhr` |
| `reporttype` | 协作类型 | `BasedataField` | `hbpm_reportcoreltype` |
| `targetpos` | 协作岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `applicableorg` | 行政组织名称 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
