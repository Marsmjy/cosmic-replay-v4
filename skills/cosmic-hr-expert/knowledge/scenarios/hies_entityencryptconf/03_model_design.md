# hies_entityencryptconf · 模型设计

> **form**：`hies_entityencryptconf` · 导出实体加密参数设置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**24**
- 必填字段：**1**
- 引用基础资料字段：**5**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `entity` | 加密实体 | `BasedataField` | `hbp_entityobject` |
| `app` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `EntityEncryptEdit` | `HRDataBaseEdit` | Edit/Form |
| `EntityEncryptTreeListEdit` | `HRTreeListBizAppsPlugin` | List |
