# homs_cardstyle_new · 模型设计

> **form**：`homs_cardstyle_new` · 卡片样式设置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**54**
- 必填字段：**5**
- 引用基础资料字段：**10**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `adminorgid` | 组织名称 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `cardcontentid` | 已选字段 | `BasedataField` | `homs_cardconfig` |
| `adminorg` | 所属组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `carddimension` | 卡片维度 | `BasedataField` | `homs_carddimension` |
| `cardstylestdlibid` | 卡片样式模板 | `BasedataField` | `homs_cardstylestdlib` |
| `policy` | 不可见策略字段 | `BasedataField` | `brm_policy_edit` |
| `colorschemadimension` | 显示维度 | `BasedataField` | `homs_cardconfig` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
