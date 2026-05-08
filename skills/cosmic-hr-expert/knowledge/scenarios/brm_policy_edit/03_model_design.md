# brm_policy_edit · 模型设计

> **form**：`brm_policy_edit` · 策略管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**48**
- 必填字段：**7**
- 引用基础资料字段：**9**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bizappid` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `scene` | 所属场景 | `BasedataField` | `brm_scene` |
| `entitybu` | 组织基础资料 | `BasedataField` | `bos_org` |
| `rulebizapp` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `rulescene` | 所属场景 | `BasedataField` | `brm_scene` |
| `createbu` | 创建组织 | `OrgField` | `bos_org` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 四、跨云被引用拓扑

本 form 实体被其他云引用 **1** 处。详见 `11_upstream_downstream_logic.md` 注入的下游消费者段。
