# hies_switchregistry · 模型设计

> **form**：`hies_switchregistry` · 新旧导入导出切换管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**27**
- 必填字段：**7**
- 引用基础资料字段：**5**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `entity` | 业务对象 | `BasedataField` | `bos_formmeta` |
| `bizapp` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `SwitchRegistryEdit` | `HRDataBaseEdit` | Edit/Form |
| `SwitchRegistryTreeListPlugin` | `HRF7TreeListPlugin` | List |
