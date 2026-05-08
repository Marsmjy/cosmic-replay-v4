# hrcs_label · 模型设计

> **form**：`hrcs_label` · 标签
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**29**
- 必填字段：**5**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `group` | 标签分类 | `GroupField` | `hrcs_labelgroup` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `labelobject` | 打标对象 | `BasedataField` | `hrcs_labelobject` |
| `brmscene` | 场景 | `BasedataField` | `brm_scene` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `LabelAuditOp` | `HRDataBaseOp` | OP |
| `LabelDeleteOp` | `HRDataBaseOp` | OP |
| `LabelListPlugin` | `AbstractTreeListPlugin` | List |
| `LabelPlugin` | `HRDataBaseEdit` | Edit/Form |
| `LabelSaveOp` | `HRDataBaseOp` | OP |

## 四、跨云被引用拓扑

本 form 实体被其他云引用 **2** 处。详见 `11_upstream_downstream_logic.md` 注入的下游消费者段。
