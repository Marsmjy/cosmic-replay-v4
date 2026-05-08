# hbss_appbusinesstype · 模型设计

> **form**：`hbss_appbusinesstype` · 应用与业务类型关系配置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**21**
- 必填字段：**3**
- 引用基础资料字段：**5**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `app` | 应用 | `BasedataField` | `bos_devportal_bizapp` |
| `businesstype` | 所属业务类型 | `BasedataField` | `hbss_bussinessfield` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `HRAppBusinessSaveOp` | `HRDataBaseOp` | OP |
| `HRAppBusinessTypeEdit` | `HRDataBaseEdit` | Edit/Form |
| `HRAppBusinessTypeList` | `StandardTreeListPlugin` | List |
