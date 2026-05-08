---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R4TU4TF8YZ
app_number: wtabm
app_name: 假期管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtabm_vaapplyself — 休假申请

**表单编码**: `wtabm_vaapplyself`  
**表单ID**: `2XBBLT9XD+ED`  
**归属**: 工时假勤云 / 假期管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtabm_vaapplyself（休假申请） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtabm_vaapply` | 主表 · 40 列 |
| `t_wtabm_vaapplyentry` | 分录表 · 19 列 |
| `t_wtabm_vaapplydetail` | 分录表 · 8 列 |
| `t_wtabm_aifileentity` | 分录表 · 4 列 |

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
| org | 考勤管理组织 | OrgField | t_wtabm_vaapply.forgid |  | bos_org |
| barcode | 条形码 | TextField | t_wtabm_vaapply.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtabm_vaapply.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtabm_vaapply.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtabm_vaapply.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtabm_vaapply.fissubmit |  |  |

## 实体: entryentity（休假信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryparentid | 父单据EntryID | BigIntField | t_wtabm_vaapplyentry.fentryparentid |  |  |
| seqtext | 移动端序列号 | IntegerField | — |  |  |
| entryvacationtype | 休假类型 | BasedataField | — | ✓ | wtbd_vacationtype |
| entrystarttimetext | 开始时间 | TextField | — |  |  |
| entryendtimetext | 结束时间 | TextField | — |  |  |
| entryapplytime | 申请时长 | DecimalField | — |  |  |
| entryunit | 单位 | ComboField | t_wtabm_vaapplyentry.fentryunit |  |  |
| entryvatime | 有效时长 | DecimalField | — |  |  |
| entrystartdate | 开始日期 | DateField | — | ✓ |  |
| entryreason | 休假原因 | TextField | — |  |  |
| entryenddate | 结束日期 | DateField | — | ✓ |  |
| owndate | 自选时段归属日期 | DateField | t_wtabm_vaapplyentry.fowndate |  |  |
| entrystartmethod | 休假开始方式 | ComboField | — | ✓ |  |
| entryendmethod | 休假结束方式 | ComboField | — | ✓ |  |
| billentryname | 分录名称 | TextField | — |  |  |
| vareasoncheck | 休假原因必录(移动端) | CheckBoxField | — |  |  |
| specialvatype | 特殊假类型 | ComboField | t_wtabm_vaapplyentry.fspecialvatype |  |  |
| specialvamethod | 哺乳假休假方式 | BasedataField | t_wtabm_vaapplyentry.fspecialvamethodid |  | wtbd_breastdaytype |
| specialextjson | 特殊假信息JSON | TextField | t_wtabm_vaapplyentry.fspecialextjson |  |  |
| timeframeshow | 移动端特殊假时段控件展示 | TextField | — |  |  |
| isdisposable | 是否一次性假 | CheckBoxField | t_wtabm_vaapplyentry.fisdisposable |  |  |
| cancelstatus | 是否取消申请 | CheckBoxField | t_wtabm_vaapplyentry.fcancelstatus |  |  |
| offtimeperday | 每日申请时长 | TextField | — |  |  |
| vaentryseq | 编辑框序号 | IntegerField | t_wtabm_vaapplydetail.fvaentryseq |  |  |
| vaentrydate | 休假日期 | DateField | — |  |  |
| vaentrystarttime | 明细开始时间 | DateTimeField | — |  |  |
| vaentryendtime | 明细结束时间 | DateTimeField | — |  |  |
| vaentrydatetype | 日期类型基础资料 | BasedataField | — |  | wtbd_datetype |
| vaentryapplytimehour | 单次申请时长（小时) | DecimalField | — |  |  |
| vaentryunit | 单位 | ComboField | — |  |  |
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wtabm_vaapply.fisattfilediscard |  |  |
| mid | 批量单据ID | BigIntField | t_wtabm_vaapply.fmid |  |  |
| employee | 员工 | BasedataField | t_wtabm_vaapply.femployeeid |  | hrpi_employee |
| attfile | 考勤档案 | BasedataField | t_wtabm_vaapply.fattfileid |  | wtp_attfilebase |
| attfilebasef7 | 考勤档案版本 | BasedataField | — |  | wtp_attfilebase |
| billstyle | 样式类型 | ComboField | t_wtabm_vaapply.fbillstyle |  |  |
| submitter | 提交人 | UserField | t_wtabm_vaapply.fsubmitter |  | bos_user |
| submitdate | 提交时间 | DateTimeField | t_wtabm_vaapply.fsubmitdate |  |  |
| applytyperadio | 申请类型 | RadioGroupField | t_wtabm_vaapply.fapplytyperadio |  |  |
| selfradio | 本人申请 | RadioField | — |  |  |
| otherradio | 代申请 | RadioField | — |  |  |
| parentid | 父单据ID | BigIntField | t_wtabm_vaapply.fparentid |  |  |
| ischange | 是否变更 | CheckBoxField | t_wtabm_vaapply.fischange |  |  |
| ishavechange | 是否存在变更单 | CheckBoxField | t_wtabm_vaapply.fishavechange |  |  |
| vacationtypelist | 休假类型多选基础资料 | MulBasedataField | — |  |  |
| unit | 单位 | ComboField | t_wtabm_vaapplydetail.funit |  |  |
| vaplan | 休假方案VID | BasedataField | t_wtabm_vaapply.fvaplanid |  | wtp_vacationplan |
| applytime | 休假申请时长 | DecimalField | t_wtabm_vaapply.fapplytime |  |  |
| vatime | 有效时长 | DecimalField | t_wtabm_vaapply.fvatime |  |  |
| startdatestr | 休假开始时间文本 | TextField | t_wtabm_vaapply.fstartdatestr |  |  |
| enddatestr | 休假结束时间文本 | TextField | t_wtabm_vaapply.fenddatestr |  |  |
| startdate | 开始时间 | DateTimeField | t_wtabm_vaapplyentry.fstartdate |  |  |
| enddate | 结束时间 | DateTimeField | t_wtabm_vaapplyentry.fenddate |  |  |
| edit | 操作 | TextField | t_wtabm_vaapply.fedit |  |  |
| vadate | 休假日期 | DateField | t_wtabm_vaapplydetail.fvadate |  |  |
| originalid | 原单ID | BigIntField | t_wtabm_vaapply.foriginalid |  |  |
| changenum | 变更信息 | TextField | — |  |  |
| isneedhand | 需要工作交接 | CheckBoxField | t_wtabm_vaapply.fisneedhand |  |  |
| handperson | 交接人 | TextField | t_wtabm_vaapply.fhandperson |  |  |
| handreason | 交接说明 | TextField | t_wtabm_vaapply.fhandreason |  |  |

## 实体: datesplitentry（每日申请时长明细） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| vaentryid | 休假分录ID | BigIntField | — |  |  |
| splitdate | 拆分排班日期 | DateField | — |  |  |
| splittimehour | 小时数 | DecimalField | — |  |  |
| splittimeday | 天数 | DecimalField | — |  |  |

## 实体: aifileentity（附件识别结果） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fileuid | 文件uid | TextField | t_wtabm_aifileentity.ffileuid |  |  |
| status | 识别状态 | CheckBoxField | t_wtabm_aifileentity.fstatus |  |  |
| msg | 识别结果 | LargeTextField | t_wtabm_aifileentity.fmsg |  |  |
| origindata | 原始数据 | LargeTextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtabm_vaapply（主表） | 35 |
| t_wtabm_aifileentity | 3 |
| t_wtabm_vaapplydetail | 3 |
| t_wtabm_vaapplyentry | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 32 |
