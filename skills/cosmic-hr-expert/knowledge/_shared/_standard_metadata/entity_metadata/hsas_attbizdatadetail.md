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

# hsas_attbizdatadetail — 单据体

**表单编码**: `hsas_attbizdatadetail`  
**表单ID**: `4760JP5CI+9Q`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: entryentity（单据体） [EntryEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_attbsdetail` | 主表 · 31 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| calpayrolltasknumber | 核算任务编码 | BasedataPropField | — |  |  |
| caltask | 核算任务名称 | BasedataField | — |  | hsas_calpayrolltask |
| calperiod | 期间 | BasedataPropField | — |  |  |
| salaryfile | 薪资档案 | BasedataField | — |  | hsas_salaryfile |
| datatype | 数据类型 | BasedataPropField | — |  |  |
| datalength | 数据长度 | BasedataPropField | — |  |  |
| scalelimit | 小数位数限制 | BasedataPropField | — |  |  |
| minvalue | 最小输入值 | TextField | — |  |  |
| maxvalue | 最大输入值 | TextField | — |  |  |
| bizitemgrpentcur | 可输入币种 | BasedataPropField | — |  |  |
| earliestdate | 最早日期 | BasedataPropField | — |  |  |
| lastdate | 最晚日期 | BasedataPropField | — |  |  |
| consumemethod | 计算取值方式 | BasedataPropField | — |  |  |
| usagecountoption | 使用次数 | ComboField | — |  |  |
| monthandyearoption | 所属年月 | DateField | — |  |  |
| bizuniquecode | 唯一识别码 | TextField | t_hsas_attbsdetail.fbizuniquecode |  |  |
| groupid | 分组id | TextField | t_hsas_attbsdetail.fgroupid |  |  |
| empnumber | 工号 | TextField | t_hsas_attbsdetail.fempnumber |  |  |
| personfield | 人员 | EmployeeField | — |  | hsbs_empposf7query |
| nocounttimes | 非薪资发放消费次数 | IntegerField | t_hsas_attbsdetail.fnocounttimes |  |  |
| basedatapropfield | 工号 | BasedataPropField | — |  |  |
| basedatapropfield1 | 用于名单生成 | BasedataPropField | — |  |  |
| empposorgrel | 任职经历 | BasedataField | t_hsas_attbsdetail.fempposorgrelid |  | hsbs_empposorgrel |
| empposorgrelnumber | 任职经历任职编号 | TextField | — |  |  |
| assignment | 工作分配 | BasedataField | t_hsas_attbsdetail.fassignmentid |  | hsbs_assignment |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| t_hsas_attbsdetail | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 19 |
