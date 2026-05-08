---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_countmsgrecord — 核算消息记录

**表单编码**: `wtte_countmsgrecord`  
**表单ID**: `1RX2AQH9LXN=`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_countmsgrecord（核算消息记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_countmsgrecord` | 主表 · 20 列 |
| `t_wtte_countmsgrecord_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_countmsgrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_countmsgrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_countmsgrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_countmsgrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| content | 消息内容 | MuliLangTextField | t_wtte_countmsgrecord_l.fcontent |  |  |
| countnumber | 任务号 | TextField | t_wtte_countmsgrecord.fcountnumber |  |  |
| assigndate | 归属日期 | DateField | t_wtte_countmsgrecord.fassigndate |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_countmsgrecord.forgid |  | bos_org |
| number | 编码 | TextField | t_wtte_countmsgrecord.fnumber |  |  |
| attfilevid | 考勤档案版本 | BasedataField | t_wtte_countmsgrecord.fattfilevid |  | wtp_attfilebase |
| attfileid | 考勤档案 | BasedataField | t_wtte_countmsgrecord.fattfileid |  | wtp_attfilebase |
| employee | 员工 | BasedataField | t_wtte_countmsgrecord.femployeeid |  | hrpi_employee |
| assignment | 组织分配 | BasedataField | t_wtte_countmsgrecord.fassignmentid |  | hrpi_assignment |
| empposorgrel | 任职经历 | BasedataField | t_wtte_countmsgrecord.fempposorgrelid |  | hrpi_empposorgrel |
| isprimary | 是否主任职 | CheckBoxField | t_wtte_countmsgrecord.fisprimary |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_countmsgrecord（主表） | 14 |
| t_wtte_countmsgrecord_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
