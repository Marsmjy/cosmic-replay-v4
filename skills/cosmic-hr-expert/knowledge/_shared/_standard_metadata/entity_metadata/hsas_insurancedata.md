---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_insurancedata — 社保业务数据

**表单编码**: `hsas_insurancedata`  
**表单ID**: `2OIREBTSA7SK`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_insurancedata（社保业务数据） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_insurancedata` | 主表 · 18 列 |
| `t_hsas_insurancedataent` | 分录表 · 6 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| filenumber | 社保档案编号 | TextField | t_hsas_insurancedata.ffilenumber |  |  |
| sinsurperiodnumber | 社保期间 | TextField | t_hsas_insurancedata.fsinsurperiodnumber |  |  |
| perioddate | 社保所属年月 | DateField | t_hsas_insurancedata.fperioddate |  |  |
| batchid | 推送批次号 | TextField | t_hsas_insurancedata.fbatchid |  |  |
| usestatus | 使用状态 | ComboField | t_hsas_insurancedata.fusestatus |  |  |
| caltask | 薪资核算任务 | BasedataField | t_hsas_insurancedata.fcaltaskid |  | hsas_calpayrolltask |
| salaryfile | 个人发薪档案 | BasedataField | t_hsas_insurancedata.fsalaryfileid |  | hsas_salaryfile |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| numvalue | 数值项目数据 | DecimalField | t_hsas_insurancedataent.fnumvalue |  |  |
| amountvalue | 金额 | AmountField | t_hsas_insurancedataent.famountvalue |  |  |
| isnull | 是否为空 | CheckBoxField | t_hsas_insurancedataent.fisnull |  |  |
| currency | 币种 | CurrencyField | — |  | bd_currency |
| storagetype | 存储类型 | ComboField | t_hsas_insurancedataent.fstoragetype |  |  |
| insuranceitem | 险种项目 | BigIntField | — |  |  |
| calperson | 核算名单ID | BigIntField | — |  |  |
| employee | 计薪人员 | BasedataField | t_hsas_insurancedata.femployeeid |  | hsbs_employee |
| withholddate | 算薪代扣年月 | DateField | t_hsas_insurancedata.fwithholddate |  |  |
| withholdtype | 代扣方式 | ComboField | t_hsas_insurancedata.fwithholdtype |  |  |
| modifier | 修改人 | ModifierField | t_hsas_insurancedata.fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_insurancedata.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_insurancedata.fmodifytime |  |  |
| creator | 创建人 | CreaterField | t_hsas_insurancedata.fcreatorid |  | bos_user |
| payrollgroup | 代扣薪资核算组 | BasedataField | t_hsas_insurancedata.fpayrollgroupid |  | hsas_payrollgrp |
| empposorgrel | 关联任职 | BasedataField | t_hsas_insurancedata.fempposorgrelid |  | hsbs_empposorgrel |
| assignment | 工作分配 | BasedataField | t_hsas_insurancedata.fassignmentid |  | hsbs_assignment |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_insurancedata（主表） | 17 |
| t_hsas_insurancedataent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
