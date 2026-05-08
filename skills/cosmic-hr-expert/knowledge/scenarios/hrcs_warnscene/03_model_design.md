# hrcs_warnscene · 模型设计

> **form**：`hrcs_warnscene` · 预警场景
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**34**
- 必填字段：**2**
- 引用基础资料字段：**8**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bizapp` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `warnobjtpl` | 预警对象(废弃) | `BasedataField` | `hrcs_warnobjtpl` |
| `warnfromentiy` | 处理预警业务功能 | `BasedataField` | `hbp_funcprementity` |
| `warnbizappid` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `warnentityperm` | 控权维度 | `BasedataField` | `hrcs_dimension` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `WarnAdFilterRightTreeEditPlugin` | `HRDataBaseEdit` | Edit/Form |
| `WarnDataPermEdit` | `HRDataBaseEdit` | Edit/Form |
| `WarningSceneDataFilterPlugin` | `HRDataBaseEdit` | Edit/Form |
| `WarningSceneReceiverPermEdit` | `HRDataBaseEdit` | Edit/Form |
| `WarningSceneTreeListPlugin` | `HRF7TreeListPlugin` | List |
| `WarnSceneAdConditionOp` | `WarnAdOp` | OP |
| `WarnSceneAdFilterEditPlugin` | `WarnAdFilterEditPlugin` | Edit/Form |
| `WarnSceneAdFilterLeftTreeEditPlugin` | `WarnAdFilterLeftTreeEditPlugin` | Edit/Form |
| `WarnSceneCalConfigEdit` | `WarnSceneCommonEdit` | Edit/Form |
| `WarnSceneCommonConditionOp` | `HRDataBaseOp` | OP |
| `WarnSceneCommonConditionsEdit` | `WarnSceneCommonEdit` | Edit/Form |
| `WarnSceneEdit` | `WarnSceneCommonEdit` | Edit/Form |
| `WarnSceneOp` | `HRDataBaseOp` | OP |
