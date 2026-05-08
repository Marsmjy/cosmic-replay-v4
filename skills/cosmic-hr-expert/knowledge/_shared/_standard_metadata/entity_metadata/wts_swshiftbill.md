---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R5/4TCA97N
app_number: wts
app_name: 排班管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wts_swshiftbill — 为他人申请调班

**表单编码**: `wts_swshiftbill`  
**表单ID**: `3ZX8TWH58JSZ`  
**归属**: 工时假勤云 / 排班管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wts_swshiftbill（为他人申请调班） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wts_swshiftbill` | 主表 · 22 列 |
| `t_wts_swshiftbillentry` | 分录表 · 19 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_wts_swshiftbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_wts_swshiftbill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_wts_swshiftbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wts_swshiftbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_wts_swshiftbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_wts_swshiftbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wts_swshiftbill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wts_swshiftbill.fcreatetime |  |  |
| org | 考勤管理组织 | OrgField | t_wts_swshiftbill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_wts_swshiftbill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_wts_swshiftbill.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_wts_swshiftbill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_wts_swshiftbill.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | — |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_wts_swshiftbill.fissubmit |  |  |

## 实体: entryentity（调班信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| swdate | 调班日期 | DateField | t_wts_swshiftbillentry.fswdate |  |  |
| fromrostertype | 排班类型 | ComboField | t_wts_swshiftbillentry.ffromrostertype |  |  |
| beforeshift | 调前班次 | BasedataField | t_wts_swshiftbillentry.fbeforeshiftid |  | wtbd_shift |
| beforedatetype | 调前日期类型 | BasedataField | t_wts_swshiftbillentry.fbeforedatetypeid |  | wtbd_datetype |
| aftershift | 调后班次 | BasedataField | t_wts_swshiftbillentry.faftershiftid |  | wtbd_shift |
| afterdatetype | 调后日期类型 | BasedataField | t_wts_swshiftbillentry.fafterdatetypeid |  | wtbd_datetype |
| fromattfilebo | 调班人BO | BasedataField | t_wts_swshiftbillentry.ffromattfileboid |  | wtp_attfilebase |
| toattfilebo | 对调人BO | BasedataField | t_wts_swshiftbillentry.ftoattfileboid |  | wtp_attfilebase |
| fromshift | 当天班次 | BasedataField | t_wts_swshiftbillentry.ffromshiftid |  | wtbd_shift |
| fromdatetype | 当天日期类型 | BasedataField | t_wts_swshiftbillentry.ffromdatetypeid |  | wtbd_datetype |
| toattfilebase | 对调人 | BasedataField | t_wts_swshiftbillentry.ftoattfilebaseid |  | wtp_attfilebase |
| todate | 对调日期 | DateField | t_wts_swshiftbillentry.ftodate |  |  |
| torostertype | 对调排班类型 | ComboField | t_wts_swshiftbillentry.ftorostertype |  |  |
| toshift | 对调班次 | BasedataField | t_wts_swshiftbillentry.ftoshiftid |  | wtbd_shift |
| todatetype | 对调日期类型 | BasedataField | t_wts_swshiftbillentry.ftodatetypeid |  | wtbd_datetype |
| swapscope | 对调范围 | MulComboField | t_wts_swshiftbillentry.fswapscope |  |  |
| reason | 调班原因 | TextField | t_wts_swshiftbillentry.freason |  |  |
| swtype | 调班方式 | ComboField | t_wts_swshiftbillentry.fswtype |  |  |
| isattfilediscard | 是否档案废弃 | CheckBoxField | t_wts_swshiftbill.fisattfilediscard |  |  |
| mid | 批量单据ID | BigIntField | — |  |  |
| employee | 员工 | BasedataField | t_wts_swshiftbill.femployeeid |  | hrpi_employee |
| attfile | 调班员工 | BasedataField | t_wts_swshiftbill.fattfileid |  | wtp_attfilebase |
| attfilebasef7 | 调班员工 | BasedataField | — | ✓ | wtp_attfilebase |
| billstyle | 样式类型 | ComboField | — |  |  |
| submitter | 提交人 | UserField | t_wts_swshiftbill.fsubmitter |  | bos_user |
| submitdate | 提交时间 | DateTimeField | t_wts_swshiftbill.fsubmitdate |  |  |
| applytyperadio | 申请类型 | RadioGroupField | t_wts_swshiftbill.fapplytyperadio |  |  |
| selfradio | 本人申请 | RadioField | — |  |  |
| otherradio | 代申请 | RadioField | — |  |  |
| justonefile | 是否只有一个档案(移动端本人申请) | CheckBoxField | — |  |  |
| swshifttype | 调班方式 | RadioGroupField | t_wts_swshiftbill.fswshifttype | ✓ |  |
| radiofield | 修改排班 | RadioField | — |  |  |
| radiofield1 | 本人对调 | RadioField | — |  |  |
| radiofield2 | 与他人对调 | RadioField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wts_swshiftbill（主表） | 21 |
| t_wts_swshiftbillentry | 18 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
