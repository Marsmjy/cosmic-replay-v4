---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FSW4YM0ZO
app_number: wtpm
app_name: 打卡管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtpm_signcardlist — 原始卡申请列表

**表单编码**: `wtpm_signcardlist`  
**表单ID**: `2JTA37KK1WTY`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_signcardlist（原始卡申请列表） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_signcardlist` | 主表 · 23 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtpm_signcardlist.fbillno |  |  |
| billstatus | 单据状态 | BillStatusField | t_wtpm_signcardlist.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_wtpm_signcardlist.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtpm_signcardlist.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtpm_signcardlist.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtpm_signcardlist.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_signcardlist.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtpm_signcardlist.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_signcardlist.forgid |  | bos_org |
| barcode | 条形码 | TextField | t_wtpm_signcardlist.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtpm_signcardlist.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtpm_signcardlist.fisexistsworkflow |  |  |
| auditstatus | 数据状态 | BillStatusField | t_wtpm_signcardlist.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtpm_signcardlist.fissubmit |  |  |

## 实体: entryentity（单据分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| supsigninfo | 打卡信息 | BasedataField | t_wtpm_signcardlist.fsupsigninfoid |  | wtpm_signcardinfo |
| isinworkflow | 是否在流程中 | CheckBoxField | t_wtpm_signcardlist.fisinworkflow |  |  |
| attfile | 考勤档案 | BasedataField | t_wtpm_signcardlist.fattfileid |  | wtp_attfilebase |
| card | 考勤卡号 | TextField | t_wtpm_signcardlist.fcard |  |  |
| suppleworktimeshow | 打卡时间列表显示 | TextField | — |  |  |
| attfilev | 考勤档案版本 | BasedataField | t_wtpm_signcardlist.fattfilevid |  | wtp_attfilebase |
| billid | 申请单据 | BasedataField | t_wtpm_signcardlist.fbillid |  | wtpm_signcardapplybs |
| employee | 员工 | BasedataField | t_wtpm_signcardlist.femployeeid |  | hrpi_employee |
| isvalid | 是否有效 | CheckBoxField | t_wtpm_signcardlist.fisvalid |  |  |
| signdate | 打卡日期 | DateField | t_wtpm_signcardlist.fsigndate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_signcardlist（主表） | 23 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
