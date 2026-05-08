# core_hr_employee · 模型设计

> **form**：`hrpi_employee` · 员工
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**52**
- 必填字段：**3**
- 引用基础资料字段：**15**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `globalperson` | 全球员工 | `BasedataField` | `hrpi_globalperson` |
| `primaryemployee` | 主员工 | `BasedataField` | `hrpi_employee` |
| `procreatstatus` | 生育状况 | `BasedataField` | `hbss_procreatstatus` |
| `marriagestatus` | 婚姻状况 | `BasedataField` | `hbss_marriagestatus` |
| `healthstatus` | 健康状况 | `BasedataField` | `hbss_healthstatus` |
| `gender` | 性别 | `BasedataField` | `hbss_sex` |
| `nationality` | 国籍 | `BasedataField` | `hbss_nationality` |
| `folk` | 民族 | `BasedataField` | `hbss_flok` |
| `symbolicanimals` | 生肖 | `BasedataField` | `hbss_zodiac` |
| `constellation` | 星座 | `BasedataField` | `hbss_constellation` |
| `nbloodtype` | 血型 | `BasedataField` | `hbss_bloodtype` |
| `oldemployee` | 前员工 | `BasedataField` | `hrpi_employee` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentDeleteOp` | `HRDataBaseOp` | OP |
| `AssignmentSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmployeeDeleteOp` | `HRDataBaseOp` | OP |
| `EmployeeNumberCodeRuleDeleteOp` | `CodeRuleDeleteOp` | OP |
| `EmployeeNumberCodeRuleOp` | `CodeRuleOp` | OP |
| `EmployeeNumberCodeRulePlugin` | `CodeRulePlugin` | Edit/Form |
| `EmployeePlugin` | `HRDataBaseEdit` | Edit/Form |
| `EmployeeSaveOp` | `HRDataBaseOp` | OP |
| `InfoCollectStartOp` | `HRDataBaseOp` | OP |
| `OperateLogOp` | `AbstractOperationServicePlugIn` | OP |

## 四、跨云被引用拓扑

本 form 实体被其他云引用 **3** 处。详见 `11_upstream_downstream_logic.md` 注入的下游消费者段。
