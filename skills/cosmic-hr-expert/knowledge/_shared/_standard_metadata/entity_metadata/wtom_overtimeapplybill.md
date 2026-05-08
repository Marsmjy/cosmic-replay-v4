---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R4WSV5KZ5O
app_number: wtom
app_name: 加班管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtom_overtimeapplybill — 为他人申请加班

**表单编码**: `wtom_overtimeapplybill`  
**表单ID**: `2FPRT=F21FDO`  
**归属**: 工时假勤云 / 加班管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtom_overtimeapplybill（为他人申请加班） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtom_otapply` | 主表 · 32 列 |
| `t_wtom_otapplypentry` | 分录表 · 9 列 |
| `t_wtom_otapplydentry` | 分录表 · 9 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtom_otapply.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wtom_otapply.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_wtom_otapply.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtom_otapply.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtom_otapply.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtom_otapply.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtom_otapply.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtom_otapply.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtom_otapply.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_wtom_otapply.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtom_otapply.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtom_otapply.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtom_otapply.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtom_otapply.fissubmit |  |  |

## 实体: entryentity（单据分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wtom_otapply.fisattfilediscard |  |  |
| mid | 批量单据ID | BigIntField | — |  |  |
| employee | 员工 | BasedataField | t_wtom_otapply.femployeeid |  | hrpi_employee |
| attfile | 考勤档案 | BasedataField | t_wtom_otapply.fattfileid | ✓ | wtp_attfilebase |
| attfilebasef7 | 加班员工 | BasedataField | — | ✓ | wtp_attfilebase |
| billstyle | 样式类型 | ComboField | t_wtom_otapply.fbillstyle |  |  |
| submitter | 提交人 | UserField | t_wtom_otapply.fsubmitter |  | bos_user |
| submitdate | 提交时间 | DateTimeField | t_wtom_otapply.fsubmitdate |  |  |
| applytyperadio | 申请类型 | RadioGroupField | t_wtom_otapply.fapplytyperadio |  |  |
| selfradio | 本人申请 | RadioField | — |  |  |
| otherradio | 代申请 | RadioField | — |  |  |
| originalid | 原单ID | BigIntField | t_wtom_otapply.foriginalid |  |  |
| overworktypes | 多个加班类型 | MulBasedataField | — |  |  |
| startdate | 加班开始日期 | DateField | t_wtom_otapply.fstartdate |  |  |
| enddate | 加班结束日期 | DateField | t_wtom_otapply.fenddate |  |  |
| vatime | 申请时长 | BigIntField | t_wtom_otapply.fvatime |  |  |
| vatimetext | 申请时长文本 | TextField | t_wtom_otapply.fvatimetext |  |  |
| entryotdate | 加班日期 | DateField | — |  |  |
| isnewbill | 是否最新单据 | CheckBoxField | t_wtom_otapply.fisnewbill |  |  |
| parent | 变更父ID | BigIntField | — |  |  |
| ischange | 是否是变更单 | CheckBoxField | t_wtom_otapply.fischange |  |  |
| otapplytype | 加班申请方式 | RadioGroupField | t_wtom_otapply.fotapplytype | ✓ |  |
| radiofield2 | 按时段申请 | RadioField | — |  |  |
| radiofield3 | 按时长申请 | RadioField | — |  |  |

## 实体: scentry（按时长申请） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| scottype | 加班类型 | BasedataField | t_wtom_otapplypentry.fscottype | ✓ | wtbd_ottype |
| otdate | 加班日期 | DateField | t_wtom_otapplypentry.fotdate | ✓ |  |
| otdtime | 申请时长 | TimeField | t_wtom_otapplypentry.fotdtime | ✓ |  |
| otdstarttime | 加班开始时间 | DateTimeField | t_wtom_otapplypentry.fotdstarttime |  |  |
| otdendtime | 加班结束时间 | DateTimeField | t_wtom_otapplypentry.fotdendtime |  |  |
| dutydate | 归属班次日期，取加班日期的值 | DateField | t_wtom_otapplypentry.fdutydate |  |  |
| compentype | 补偿方式 | BasedataField | t_wtom_otapplypentry.fcompentypeid |  | wtbd_otcompenmode |
| otreson | 加班原因 | TextField | t_wtom_otapplydentry.fotreson |  |  |
| pcancelreason | 取消加班原因 | TextField | t_wtom_otapplypentry.fpcancelreason |  |  |
| avoidscdelete | 防止平台自动删行 | TextField | — |  |  |
| billentryname_long | 时长申请分录名称 | TextField | — |  |  |

## 实体: sdentry（加班信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sdottype | 加班类型 | BasedataField | t_wtom_otapplydentry.fsdottype | ✓ | wtbd_ottype |
| otstartdate | 开始时间 | DateTimeField | t_wtom_otapplydentry.fotstartdate | ✓ |  |
| otenddate | 结束时间 | DateTimeField | t_wtom_otapplydentry.fotenddate | ✓ |  |
| otdutydate | 加班日期 | DateField | t_wtom_otapplydentry.fotdutydate |  |  |
| compentyped | 补偿方式 | BasedataField | t_wtom_otapplydentry.fcompentypedid |  | wtbd_otcompenmode |
| intottime | 加班时长整数 | IntegerField | t_wtom_otapplydentry.fintottime |  |  |
| ottime | 申请时长 | TextField | t_wtom_otapplydentry.fottime |  |  |
| otresond | 加班原因 | TextField | — |  |  |
| avoidsddelete | 防止平台自动删行 | TextField | — |  |  |
| billentryname_seg | 时段申请分录名称 | TextField | — |  |  |
| cancelstatus | 是否取消申请 | CheckBoxField | t_wtom_otapplydentry.fcancelstatus |  |  |
| changenum | 变更次数 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtom_otapply（主表） | 29 |
| t_wtom_otapplydentry | 9 |
| t_wtom_otapplypentry | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 16 |
