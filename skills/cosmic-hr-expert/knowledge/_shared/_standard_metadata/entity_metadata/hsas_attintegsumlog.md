---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_attintegsumlog — 考勤集成日志

**表单编码**: `hsas_attintegsumlog`  
**表单ID**: `2N/96JY+MC81`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_attintegsumlog（考勤集成日志） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_attintegsumlog` | 主表 · 25 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 操作人 | CreaterField | t_hsas_attintegsumlog.fcreatorid |  | bos_user |
| createtime | 开始时间 | CreateDateField | t_hsas_attintegsumlog.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_attintegsumlog.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_attintegsumlog.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |

## 实体: entryentity（集成异常记录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| status | 状态 | ComboField | — |  |  |
| attpersonnumber | 工号 | BasedataPropField | — |  |  |
| attpersonname | 姓名 | BasedataPropField | — |  |  |
| companyname | 部门 | BasedataPropField | — |  |  |
| positionname | 岗位 | BasedataPropField | — |  |  |
| attfilenumber | 考勤档案编号 | TextField | — |  |  |
| attintegmapscmname | 考勤集成映射方案 | TextField | — |  |  |
| empposorgrel | 任职经历 | BasedataField | — |  | hsbs_empposorgrel |
| jobname | 职位 | BasedataPropField | — |  |  |
| operationtype | 类型 | ComboField | t_hsas_attintegsumlog.foperationtype |  |  |
| integrationstatus | 集成状态 | ComboField | t_hsas_attintegsumlog.fintegrationstatus |  |  |
| integrationquantity | 集成数量 | IntegerField | t_hsas_attintegsumlog.fintegrationquantity |  |  |
| successquantity | 成功数量 | IntegerField | t_hsas_attintegsumlog.fsuccessquantity |  |  |
| failquantity | 失败数量 | IntegerField | t_hsas_attintegsumlog.ffailquantity |  |  |
| number | 日志编号 | TextField | t_hsas_attintegsumlog.fnumber |  |  |
| frontendsystem | 前端系统 | ComboField | t_hsas_attintegsumlog.ffrontendsystem |  |  |
| filequantity | 集成总考勤档案数 | IntegerField | t_hsas_attintegsumlog.ffilequantity |  |  |
| processstate | 集成处理状态 | ComboField | t_hsas_attintegsumlog.fprocessstate |  |  |
| writebackstate | 集成回写状态 | ComboField | t_hsas_attintegsumlog.fwritebackstate |  |  |
| timeconsumedisplay | 耗时 | TextField | — |  |  |
| endtime | 结束时间 | DateTimeField | t_hsas_attintegsumlog.fendtime |  |  |
| batchversionid | 批次号 | TextField | t_hsas_attintegsumlog.fbatchversionid |  |  |
| writebackfilequantity | 集成回写完成档案数 | IntegerField | t_hsas_attintegsumlog.fwritebackfilequantity |  |  |
| attdatatype | 考勤数据类型 | ComboField | t_hsas_attintegsumlog.fattdatatype |  |  |
| storepage | 落存页面 | ComboField | t_hsas_attintegsumlog.fstorepage |  |  |
| sessionid | 会话ID | TextField | t_hsas_attintegsumlog.fsessionid |  |  |
| earliestattstartdate | 最早考勤开始日期 | DateField | t_hsas_attintegsumlog.fearliestattstartdate |  |  |
| lastattstartdate | 最晚考勤开始日期 | DateField | t_hsas_attintegsumlog.flastattstartdate |  |  |
| earliestattenddate | 最早考勤结束日期 | DateField | t_hsas_attintegsumlog.fearliestattenddate |  |  |
| lastattenddate | 最晚考勤结束日期 | DateField | t_hsas_attintegsumlog.flastattenddate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_attintegsumlog（主表） | 24 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
