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

# wtpm_supsignself — 补签申请

**表单编码**: `wtpm_supsignself`  
**表单ID**: `2WTH=4EGE+WQ`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_supsignself（补签申请） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_supsign` | 主表 · 26 列 |
| `t_wtpm_supsigninfo` | 分录表 · 11 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wtpm_supsign.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wtpm_supsign.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_wtpm_supsign.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtpm_supsign.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wtpm_supsign.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wtpm_supsign.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_supsign.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wtpm_supsign.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_supsign.forgid |  | bos_org |
| barcode | 条形码 | TextField | t_wtpm_supsign.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wtpm_supsign.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wtpm_supsign.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wtpm_supsign.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wtpm_supsign.fissubmit |  |  |

## 实体: entryentity（单据分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| signdate | 日期 | DateField | t_wtpm_supsigninfo.fsigndate | ✓ |  |
| suppleworktime | 时间 | TimeField | t_wtpm_supsigninfo.fsuppleworktime | ✓ |  |
| applyreason | 补签原因 | BasedataField | t_wtpm_supsigninfo.fapplyreasonid |  | wtbd_reason |
| timezone | 时区 | BasedataField | t_wtpm_supsigninfo.ftimezoneid | ✓ | inte_timezone |
| accesstag | 进出卡 | ComboField | t_wtpm_supsigninfo.faccesstag |  |  |
| pointtag | 卡点归属 | ComboField | t_wtpm_supsigninfo.fpointtag |  |  |
| source | 打卡来源 | BasedataField | t_wtpm_supsigninfo.fsourceid |  | wtbd_signsource |
| equipment | 打卡设备 | BasedataField | t_wtpm_supsigninfo.fequipmentid |  | wtpm_punchcardequip |
| signpoint | 补签卡点时间 | DateTimeField | t_wtpm_supsigninfo.fsignpoint |  |  |
| remark | 备注 | TextField | t_wtpm_supsigninfo.fremark |  |  |
| billentryname | 分录名称 | TextField | — |  |  |
| signtimestip | 补签剩余次数 | TextField | — |  |  |
| onoffwork | 上下班卡 | TextField | — |  |  |
| shifttimedetail | 时段明细 | TextField | — |  |  |
| attpolicy | 考勤制度 | BigIntField | — |  |  |
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wtpm_supsign.fisattfilediscard |  |  |
| mid | 批量单据ID | BigIntField | — |  |  |
| employee | 员工 | BasedataField | t_wtpm_supsign.femployeeid |  | hrpi_employee |
| attfile | 考勤档案 | BasedataField | t_wtpm_supsign.fattfileid |  | wtp_attfilebase |
| attfilebasef7 | 考勤档案版本 | BasedataField | — |  | wtp_attfilebase |
| billstyle | 样式类型 | ComboField | — |  |  |
| submitter | 提交人 | UserField | t_wtpm_supsign.fsubmitter |  | bos_user |
| submitdate | 提交时间 | DateTimeField | t_wtpm_supsign.fsubmitdate |  |  |
| applytyperadio | 申请类型 | RadioGroupField | — |  |  |
| selfradio | 本人申请 | RadioField | — |  |  |
| otherradio | 代申请 | RadioField | — |  |  |
| ischange | 是否是变更单 | CheckBoxField | t_wtpm_supsign.fischange |  |  |
| isnewbill | 是否最新单据 | CheckBoxField | t_wtpm_supsign.fisnewbill |  |  |
| parent | 变更父ID | BigIntField | — |  |  |
| originalid | 原单ID | BigIntField | t_wtpm_supsign.foriginalid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_supsign（主表） | 22 |
| t_wtpm_supsigninfo | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
