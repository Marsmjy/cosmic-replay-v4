# hrptc_userperm · 模型设计

> **form**：`hrptc_userperm` · 用户授权管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**36**
- 必填字段：**1**
- 引用基础资料字段：**15**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `user` | 用户 | `UserField` | `bos_user` |
| `disablefield` | 失效字段 | `BasedataField` | `hrptmc_anobjqueryfield` |
| `disablefieldreport` | 失效字段所在报表 | `BasedataField` | `hrptmc_reportmanage` |
| `permuser` | 权限用户 | `UserField` | `bos_user` |
| `report` | 权限报表 | `BasedataField` | `hrptmc_reportmanage` |
| `queryfield` | 查询字段 | `BasedataField` | `hrptmc_anobjqueryfield` |
| `reportperm` | 权限报表 | `BasedataField` | `hrptc_rptalocperm` |
| `adminorgstruct` | 行政组织架构 | `BasedataField` | `haos_structproject` |
| `modifyuser` | 管理员 | `UserField` | `bos_user` |
| `selectreport` | 已选报表 | `BasedataField` | `hrptmc_reportmanage` |
| `selectuser` | 已选用户 | `UserField` | `bos_user` |
| `selectadminuser` | 已选管理员 | `UserField` | `bos_user` |
| `selectreportperm` | 权限报表 | `BasedataField` | `hrptc_rptalocperm` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ReportUserPermEdit` | `HRBaseDataCommonEdit` | Edit/Form |
| `ReportUserPermList` | `AbstractListPlugin` | List |
| `ReportUserPermOp` | `HRDataBaseOp` | OP |
