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

# wtte_exrecord — 异常记录

**表单编码**: `wtte_exrecord`  
**表单ID**: `23W9OC50KXJ/`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_exrecord（异常记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_exrecord` | 主表 · 35 列 |
| `t_wtte_exrecord_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_exrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_exrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_exrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_exrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| shiftid | 班次 | BasedataField | — |  | wtbd_shift |
| exattributeid | 异常类型 | BasedataField | t_wtte_exrecord.fexattributeid |  | wtbd_exattribute |
| attitemid | 结果项目 | BasedataField | t_wtte_exrecord.fattitemid |  | wtbd_attitem |
| attitemvalue | 结果时长 | DecimalField | t_wtte_exrecord.fattitemvalue |  |  |
| confirmstatus | 确认状态 | BillStatusField | t_wtte_exrecord.fconfirmstatus |  |  |
| calversion | 任务号 | TextField | — |  |  |
| recorddate | 归属日期 | DateField | t_wtte_exrecord.frecorddate |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_exrecord.forgid |  | bos_org |
| shiftvid | 班次版本 | BasedataField | t_wtte_exrecord.fshiftvid |  | wtbd_shift |
| attitemvid | 结果项目 | BasedataField | t_wtte_exrecord.fattitemvid |  | wtbd_attitem |
| exprocessid | 异常处理方式 | BasedataField | t_wtte_exrecord.fexprocessid |  | wtp_exprocess |
| attitemorvalue | 原始时长 | DecimalField | t_wtte_exrecord.fattitemorvalue |  |  |
| comment | 备注 | MuliLangTextField | t_wtte_exrecord_l.fcomment |  |  |
| punchcardpair | 应打卡对 | BasedataField | t_wtte_exrecord.fpunchcardpairid |  | wtbd_punchcardpair |
| originitemid | 原始项目 | MulBasedataField | — |  |  |
| originitemvid | 原始项目版本 | MulBasedataField | — |  |  |
| tbposition | 班次时段 | BasedataField | t_wtte_exrecord.ftbpositionid |  | wtbd_shiftperiod |
| timeunit | 原始时长单位 | TextField | — |  |  |
| punchcardpoint | 应打卡点 | TextField | t_wtte_exrecord.fpunchcardpoint |  |  |
| operationone | 操作1 | TextField | — |  |  |
| operationtwo | 操作2 | TextField | — |  |  |
| operationthree | 操作3 | TextField | — |  |  |
| operationfour | 操作4 | TextField | — |  |  |
| datatype | 数据类别 | ComboField | — |  |  |
| exrecordconfirmid | 异常确认 | BasedataField | t_wtte_exrecord.fexrecordconfirmid |  | wtte_exrecordconfirm |
| attfilevid | 考勤档案版本 | BasedataField | t_wtte_exrecord.fattfilevid |  | wtp_attfilebase |
| attfileid | 考勤档案 | BasedataField | t_wtte_exrecord.fattfileid |  | wtp_attfilebase |
| shiftdate | 班次日期 | DateField | t_wtte_exrecord.fshiftdate |  |  |
| filtertype | 异常标识 | ComboField | t_wtte_exrecord.ffiltertype |  |  |
| exurgingrecord | 异常催办记录 | BasedataField | t_wtte_exrecord.fexurgingrecordid |  | wtte_exurgingrecord |
| employee | 员工 | BasedataField | t_wtte_exrecord.femployeeid |  | hrpi_employee |
| assignment | 组织分配 | BasedataField | t_wtte_exrecord.fassignmentid |  | hrpi_assignment |
| empposorgrel | 任职经历 | BasedataField | t_wtte_exrecord.fempposorgrelid |  | hrpi_empposorgrel |
| isprimary | 是否主任职 | CheckBoxField | t_wtte_exrecord.fisprimary |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_exrecord（主表） | 27 |
| t_wtte_exrecord_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
