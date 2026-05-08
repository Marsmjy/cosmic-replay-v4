---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R4TU4TF8YZ
app_number: wtabm
app_name: 假期管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtabm_batchvaapply — 批量休假

**表单编码**: `wtabm_batchvaapply`  
**表单ID**: `475H7I+N5H96`  
**归属**: 工时假勤云 / 假期管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtabm_batchvaapply（批量休假） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtabm_vaapplybatch` | 主表 · 27 列 |
| `t_wtabm_vaapply` | 分录表 · 32 列 |
| `t_wtabm_vaapplyentry` | 分录表 · 15 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtabm_vaapply.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wtabm_vaapply.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_wtabm_vaapply.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtabm_vaapply.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtabm_vaapply.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtabm_vaapply.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtabm_vaapply.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtabm_vaapply.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtabm_vaapply.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_wtabm_vaapplybatch.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtabm_vaapplybatch.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtabm_vaapply.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtabm_vaapplybatch.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtabm_vaapplybatch.fissubmit |  |  |

## 实体: entryentity（休假信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attfile | 考勤档案 | BasedataField | t_wtabm_vaapply.fattfileid |  | wtp_attfilebase |
| attfilebasef7 | 休假人员 | BasedataField | — |  | wtp_attfilebase |
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wtabm_vaapply.fisattfilediscard |  |  |
| entrybillstatus | 单据状态 | BillStatusField | — |  |  |
| applytyperadio | 申请类型 | ComboField | t_wtabm_vaapply.fapplytyperadio |  |  |
| attfilenumber | 工号 | BasedataPropField | — |  |  |
| attfilename | 姓名 | TextField | — | ✓ |  |
| employee | 员工 | BasedataField | t_wtabm_vaapply.femployeeid |  | hrpi_employee |
| isneedhand | 需要工作交接 | CheckBoxField | t_wtabm_vaapply.fisneedhand |  |  |
| handperson | 交接人 | TextField | t_wtabm_vaapply.fhandperson |  |  |
| handreason | 交接说明 | TextField | t_wtabm_vaapply.fhandreason |  |  |
| vacationtypelist | 休假类型 | MulBasedataField | — |  |  |
| applytime | 申请时长 | DecimalField | t_wtabm_vaapply.fapplytime |  |  |
| startdate | 开始时间 | DateTimeField | t_wtabm_vaapplyentry.fstartdate |  |  |
| enddate | 结束时间 | DateTimeField | t_wtabm_vaapplyentry.fenddate |  |  |
| ishavechange | 是否存在变更单 | CheckBoxField | t_wtabm_vaapply.fishavechange |  |  |
| billstyle | 样式类型 | ComboField | t_wtabm_vaapply.fbillstyle |  |  |
| unit | 单位 | ComboField | t_wtabm_vaapply.funit |  |  |
| startdateshowstr | 开始时间 | TextField | — |  |  |
| enddateshowstr | 结束时间 | TextField | — |  |  |
| entryparentid | 父单据entryid | BigIntField | t_wtabm_vaapplyentry.fentryparentid |  |  |
| entryvacationtype | 休假类型 | BasedataField | — |  | wtbd_vacationtype |
| entryunit | 单位 | ComboField | t_wtabm_vaapplyentry.fentryunit |  |  |
| entrystartdate | 开始日期 | DateTimeField | — |  |  |
| entryenddate | 结束日期 | DateTimeField | — |  |  |
| entryreason | 休假原因 | TextField | — |  |  |
| owndate | 自选时段归属日期 | DateField | t_wtabm_vaapplyentry.fowndate |  |  |
| specialvatype | 特殊假类型 | ComboField | t_wtabm_vaapplyentry.fspecialvatype |  |  |
| specialvamethod | 特殊假休假方式 | BasedataField | t_wtabm_vaapplyentry.fspecialvamethodid |  | wtbd_breastdaytype |
| specialextjson | 特殊假信息JSON | TextField | t_wtabm_vaapplyentry.fspecialextjson |  |  |
| isdisposable | 是否一次性假 | CheckBoxField | t_wtabm_vaapplyentry.fisdisposable |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_wtabm_vaapplybatch.fadminorgid | ✓ | haos_adminorghrf7 |
| submitter | 提交人 | UserField | t_wtabm_vaapply.fsubmitter |  | bos_user |
| submittime | 提交时间 | DateTimeField | t_wtabm_vaapplybatch.fsubmittime |  |  |
| countperson | 总人数 | IntegerField | t_wtabm_vaapplybatch.fcountperson |  |  |
| applytype | 申请方式 | ComboField | t_wtabm_vaapplybatch.fapplytype |  |  |
| islatestbill | 是否最新单据 | CheckBoxField | t_wtabm_vaapplybatch.fislatestbill |  |  |
| ischangebill | 是否是变更单 | CheckBoxField | t_wtabm_vaapplybatch.fischangebill |  |  |
| parent | 变更单父单ID | BigIntField | — |  |  |
| originalid | 原单ID | BigIntField | t_wtabm_vaapply.foriginalid |  |  |
| attfilef7 | 只用于弹出考勤档案F7 | BasedataField | — |  | wtp_attfilebase |
| changetype | 变更类型 | RadioGroupField | t_wtabm_vaapplybatch.fchangetype |  |  |
| adjustment | 调整 | RadioField | — |  |  |
| cancelbill | 失效 | RadioField | — |  |  |
| desc | 描述 | TextAreaField | t_wtabm_vaapplybatch.fdesc |  |  |

## 实体: entryentityvatime（休假时长信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| vacationtype | 休假类型 | BasedataField | t_wtabm_vaapplyentry.fvacationtypeid |  | wtbd_vacationtype |
| timeunit | 单位 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtabm_vaapplybatch（主表） | 12 |
| t_wtabm_vaapply | 23 |
| t_wtabm_vaapplyentry | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
