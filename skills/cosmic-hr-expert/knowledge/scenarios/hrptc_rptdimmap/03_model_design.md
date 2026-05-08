# hrptc_rptdimmap · 模型设计

> **form**：`hrptc_rptdimmap` · 报表控权维度配置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**15**
- 必填字段：**1**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `rptmanage` | 报表 | `BasedataField` | `hrptmc_reportmanage` |
| `aoqfield` | 分析对象查询字段 | `BasedataField` | `hrptmc_anobjqueryfield` |
| `parententity` | 父基础资料 | `BasedataField` | `bos_entityobject` |
| `entity` | 基础资料 | `BasedataField` | `bos_entityobject` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `RptDimMapEdit` | `HRDataBaseEdit` | Edit/Form |
| `RptDimMapList` | `HRDataBaseList` | List |
| `RptDimMapOp` | `HRDataBaseOp` | OP |
