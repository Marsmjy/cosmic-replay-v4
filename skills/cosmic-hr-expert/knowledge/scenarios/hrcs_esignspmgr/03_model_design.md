# hrcs_esignspmgr · 模型设计

> **form**：`hrcs_esignspmgr` · 电子签服务商管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**27**
- 必填字段：**0**
- 引用基础资料字段：**4**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bdesignappcfg` | 集成应用配置 | `BasedataField` | `hrcs_esignappcfg` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ESignSPMgrCloseEdit` | `HRCloseCheckFormPlugin` | Edit/Form |
| `ESignSPMgrEdit` | `HRDataBaseEdit` | Edit/Form |
| `ESignSPMgrListPlugin` | `HRDataBaseList` | List |
| `ESignSPMgrOp` | `HRDataBaseOp` | OP |
