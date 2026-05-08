# brm_scene · 模型设计

> **form**：`brm_scene` · 场景管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**43**
- 必填字段：**1**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bizappid` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `inputobject` | 业务对象/基础资料 | `BasedataField` | `bos_entityobject` |
| `outputobject` | 业务对象/基础资料 | `BasedataField` | `bos_entityobject` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `SceneConfigOp` | `HRDataBaseOp` | OP |
| `SceneConfigPlugin` | `HRDataBaseEdit` | Edit/Form |
| `SceneListPlugin` | `HRF7TreeListPlugin` | List |
