---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15=TGRTUNG1B
app_number: wtam
app_name: 日常考勤
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtam_busitripbill — 为他人申请出差

**表单编码**: `wtam_busitripbill`  
**表单ID**: `2GEY1P5DDQB2`  
**归属**: 工时假勤云 / 日常考勤  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtam_busitripbill（为他人申请出差） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtam_tripbill` | 主表 · 31 列 |
| `t_wtam_tripbillentry` | 分录表 · 19 列 |
| `t_wtam_tripbillentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtam_tripbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wtam_tripbill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_wtam_tripbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtam_tripbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtam_tripbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtam_tripbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtam_tripbill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtam_tripbill.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtam_tripbill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_wtam_tripbill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtam_tripbill.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtam_tripbill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtam_tripbill.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtam_tripbill.fissubmit |  |  |

## 实体: entryentity（出差信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| seqtext | 移动端使用分录序号 | IntegerField | — |  |  |
| billentryname | 移动端使用分录名称 | TextField | — |  |  |
| entryparentid | 父单据EntryID | BigIntField | t_wtam_tripbillentry.fentryparentid |  |  |
| changever | 移动端使用变更校验 | TextField | — |  |  |
| startdate | 出差开始时间 | DateTimeField | t_wtam_tripbillentry.fstartdate |  |  |
| busitriptype | 出差类型 | BasedataField | t_wtam_tripbillentry.fbusitriptypeid | ✓ | wtbd_traveltype |
| startdatestr | 开始时间 | TextField | t_wtam_tripbillentry.fstartdatestr |  |  |
| enddate | 出差结束时间 | DateTimeField | t_wtam_tripbillentry.fenddate |  |  |
| enddatestr | 结束时间 | TextField | t_wtam_tripbillentry.fenddatestr |  |  |
| traveltool | 交通工具 | BasedataField | t_wtam_tripbillentry.ftraveltoolid |  | wtbd_traveltool |
| from | 出发地 | CityField | — |  | bd_admindivision |
| enclosuremode | 附件信息 | ComboField | t_wtam_tripbillentry.fenclosuremode |  |  |
| to | 目的地 | CityField | — |  | bd_admindivision |
| triptimestr | 申请时长 | TextField | — |  |  |
| triptime | 申请时长 | DecimalField | t_wtam_tripbillentry.ftriptime |  |  |
| efftctime | 有效时长 | DecimalField | t_wtam_tripbillentry.fefftctime |  |  |
| unit | 单位 | ComboField | t_wtam_tripbillentry.funit |  |  |
| tripresontex | 出差原因 | MuliLangTextField | t_wtam_tripbillentry_l.ftripresontex |  |  |
| strapplytime | 申请时长显示 | MuliLangTextField | t_wtam_tripbillentry_l.fstrapplytime |  |  |
| owndate | 班次归属日期 | DateField | t_wtam_tripbillentry.fowndate |  |  |
| tripreason | 出差原因 | TextField | — |  |  |
| cancelstatus | 是否取消申请 | CheckBoxField | t_wtam_tripbillentry.fcancelstatus |  |  |
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wtam_tripbill.fisattfilediscard |  |  |
| mid | 批量单据ID | BigIntField | — |  |  |
| employee | 员工 | BasedataField | t_wtam_tripbill.femployeeid |  | hrpi_employee |
| attfile | 考勤档案 | BasedataField | t_wtam_tripbill.fattfileid |  | wtp_attfilebase |
| attfilebasef7 | 出差人员 | BasedataField | — | ✓ | wtp_attfilebase |
| billstyle | 样式类型 | ComboField | t_wtam_tripbill.fbillstyle |  |  |
| submitter | 提交人 | UserField | — |  | bos_user |
| submitdate | 提交时间 | DateTimeField | — |  |  |
| applytyperadio | 申请类型 | RadioGroupField | t_wtam_tripbill.fapplytyperadio |  |  |
| selfradio | 本人申请 | RadioField | — |  |  |
| otherradio | 代申请 | RadioField | — |  |  |
| isnewbill | 是否最新单据 | CheckBoxField | t_wtam_tripbill.fisnewbill |  |  |
| edate | 出差结束日期 | DateField | t_wtam_tripbill.fedate |  |  |
| parent | 变更父ID | BigIntField | — |  |  |
| sdate | 出差开始日期 | DateField | t_wtam_tripbill.fsdate |  |  |
| applyperson | 提交人 | UserField | t_wtam_tripbill.fapplypersonid |  | bos_user |
| applydate | 提交时间 | DateTimeField | t_wtam_tripbill.fapplydate |  |  |
| applytype | 提交方式 | ComboField | — |  |  |
| busitriptypes | 多个出差类型 | MulBasedataField | — |  |  |
| sumtriptime | 申请时长合计 | DecimalField | t_wtam_tripbill.fsumtriptime |  |  |
| sumunit | 合计单位 | ComboField | t_wtam_tripbill.fsumunit |  |  |
| ischange | 是否是变更单 | CheckBoxField | t_wtam_tripbill.fischange |  |  |
| originalid | 原单ID | BigIntField | t_wtam_tripbill.foriginalid |  |  |
| treatmethodgroup | 处理方式单选按钮组 | RadioGroupField | — |  |  |
| adjustradio | 假单调整 | RadioField | — |  |  |
| invalidradio | 假单作废 | RadioField | — |  |  |
| btdate | 出差日期 | DateField | — |  |  |
| changenum | 变更次数 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtam_tripbill（主表） | 28 |
| t_wtam_tripbillentry | 13 |
| t_wtam_tripbillentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 22 |
