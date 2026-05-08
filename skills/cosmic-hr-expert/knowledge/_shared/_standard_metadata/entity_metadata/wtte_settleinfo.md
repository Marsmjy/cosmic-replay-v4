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

# wtte_settleinfo — 结算信息

**表单编码**: `wtte_settleinfo`  
**表单ID**: `35V8U3HCGLPZ`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_settleinfo（结算信息） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_settleinfo` | 主表 · 23 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_settleinfo.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_settleinfo.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_settleinfo.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_settleinfo.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| employee | 员工 | BasedataField | t_wtte_settleinfo.femployeeid |  | hrpi_employee |
| attfile | 考勤档案版本 | BasedataField | t_wtte_settleinfo.fattfileid |  | wtp_attfilebase |
| attperiod | 考勤期间 | BasedataField | t_wtte_settleinfo.fattperiodid |  | wtp_attperiodentry |
| frozenstatus | 冻结状态 | ComboField | t_wtte_settleinfo.ffrozenstatus |  |  |
| frozendate | 冻结时间 | DateTimeField | t_wtte_settleinfo.ffrozendate |  |  |
| frozenuser | 冻结人 | UserField | t_wtte_settleinfo.ffrozenuser |  | bos_user |
| lockstatus | 锁定状态 | ComboField | t_wtte_settleinfo.flockstatus |  |  |
| lockdate | 锁定时间 | DateTimeField | t_wtte_settleinfo.flockdate |  |  |
| lockuser | 锁定人 | UserField | t_wtte_settleinfo.flockuser |  | bos_user |
| storagestatus | 封存状态 | ComboField | t_wtte_settleinfo.fstoragestatus |  |  |
| storagedate | 封存时间 | DateTimeField | t_wtte_settleinfo.fstoragedate |  |  |
| storageuser | 封存人 | UserField | t_wtte_settleinfo.fstorageuser |  | bos_user |
| perattperiod | 人员考勤期间 | TextField | t_wtte_settleinfo.fperattperiod |  |  |
| taskdetail | 任务明细 | BigIntField | — |  |  |
| busstatus | 业务状态 | ComboField | t_wtte_settleinfo.fbusstatus |  |  |
| attendstatus | 考勤状态 | ComboField | — |  |  |
| looptype | 循环类型 | ComboField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtte_settleinfo.forgid |  | bos_org |
| attperattperiodpk | 人员考勤期间 | BasedataField | t_wtte_settleinfo.fattperattperiodpk |  | wtp_perattperiod |
| startdate | 开始日期 | DateField | t_wtte_settleinfo.fstartdate |  |  |
| enddate | 结束日期 | DateField | t_wtte_settleinfo.fenddate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_settleinfo（主表） | 22 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
