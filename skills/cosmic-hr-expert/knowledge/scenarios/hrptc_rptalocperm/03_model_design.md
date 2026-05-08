# hrptc_rptalocperm · 模型设计

> **form**：`hrptc_rptalocperm` · 报表授权管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**43**
- 必填字段：**0**
- 引用基础资料字段：**15**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `rptmanage` | 报表 | `BasedataField` | `hrptmc_reportmanage` |
| `user` | 用户 | `UserField` | `bos_user` |
| `report` | 报表 | `BasedataField` | `hrptmc_reportmanage` |
| `queryfield` | 查询字段 | `BasedataField` | `hrptmc_anobjqueryfield` |
| `userperm` | 权限用户 | `BasedataField` | `hrptc_userperm` |
| `adminorgstruct` | 行政组织架构 | `BasedataField` | `haos_structproject` |
| `modifyuser` | 管理员 | `UserField` | `bos_user` |
| `disuser22` | 用户 | `UserField` | `bos_user` |
| `dismodifyuser22` | 管理员 | `UserField` | `bos_user` |
| `disreport` | 已选报表 | `BasedataField` | `hrptmc_reportmanage` |
| `disuser` | 已选用户 | `UserField` | `bos_user` |
| `dismodifyuser` | 管理员 | `UserField` | `bos_user` |
| `disuserperm` | 权限用户 | `UserField` | `bos_user` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `RptAllotPermOp` | `HRDataBaseOp` | OP |
| `RptAlocPermEdit` | `HRBaseDataCommonEdit` | Edit/Form |
| `RptAlocPermList` | `RptGroupTreeList` | List |
