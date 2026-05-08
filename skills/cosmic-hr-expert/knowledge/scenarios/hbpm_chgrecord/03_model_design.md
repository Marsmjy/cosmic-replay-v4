# hbpm_chgrecord · 模型设计

> **form**：`hbpm_chgrecord` · 岗位变动明细
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**25**
- 必填字段：**0**
- 引用基础资料字段：**13**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `searchchangereason` | 变动原因 | `BasedataField` | `hbpm_changereason` |
| `org` | 主管责任单位 | `OrgField` | `bos_org` |
| `targetposition` | 目标岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `sourceposition` | 关联岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `changetype` | 变动类型 | `BasedataField` | `hbpm_changetype` |
| `changescene` | 变动场景 | `BasedataField` | `hbpm_changescene` |
| `changereason` | 变动原因 | `BasedataField` | `hbpm_changereason` |
| `operator` | 操作人 | `UserField` | `bos_user` |
| `hisposition` | 岗位历史版本ID | `HRPositionField` | `hbpm_positionhrf7` |
| `changeoperate` | 变动操作 | `BasedataField` | `hbpm_changeoperate` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
