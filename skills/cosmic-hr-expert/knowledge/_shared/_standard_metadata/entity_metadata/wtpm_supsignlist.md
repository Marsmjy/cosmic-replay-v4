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

# wtpm_supsignlist — 批量补签列表

**表单编码**: `wtpm_supsignlist`  
**表单ID**: `2IJVLEBQYCO=`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_supsignlist（批量补签列表） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_supsignlist` | 主表 · 23 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtpm_supsignlist.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wtpm_supsignlist.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_wtpm_supsignlist.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtpm_supsignlist.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtpm_supsignlist.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtpm_supsignlist.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_supsignlist.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtpm_supsignlist.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_supsignlist.forgid |  | bos_org |
| barcode | 条形码 | TextField | — |  |  |
| inputdevicetype | 输入设备 | TextField | — |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtpm_supsignlist.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtpm_supsignlist.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtpm_supsignlist.fissubmit |  |  |

## 实体: entryentity（单据分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isinworkflow | 是否在流程中 | CheckBoxField | t_wtpm_supsignlist.fisinworkflow |  |  |
| supsigninfo | 补签信息 | BasedataField | t_wtpm_supsignlist.fsupsigninfoid |  | wtpm_supsignbatchinfo |
| attfile | 考勤档案 | BasedataField | t_wtpm_supsignlist.fattfileid |  | wtp_attfilebase |
| card | 考勤卡号 | TextField | t_wtpm_supsignlist.fcard |  |  |
| suppleworktimeshow | 补签卡点列表显示 | TextField | — |  |  |
| attfilev | 考勤档案版本 | BasedataField | t_wtpm_supsignlist.fattfilevid |  | wtp_attfilebase |
| billid | 批量申请单据 | BasedataField | t_wtpm_supsignlist.fbillid |  | wtpm_supsignbatchbs |
| employee | 员工 | BasedataField | t_wtpm_supsignlist.femployeeid |  | hrpi_employee |
| isvalid | 是否有效 | CheckBoxField | t_wtpm_supsignlist.fisvalid |  |  |
| ischeckpass | 数据是否校验通过 | CheckBoxField | t_wtpm_supsignlist.fischeckpass |  |  |
| applyreason | 补卡原因 | BasedataField | t_wtpm_supsignlist.fapplyreasonid |  | wtbd_reason |
| signdate | 补签日期 | DateField | t_wtpm_supsignlist.fsigndate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_supsignlist（主表） | 23 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
