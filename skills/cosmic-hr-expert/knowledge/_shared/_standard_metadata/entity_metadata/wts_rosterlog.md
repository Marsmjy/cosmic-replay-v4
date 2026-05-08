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

# wts_rosterlog — 排班任务日志

**表单编码**: `wts_rosterlog`  
**表单ID**: `2KBLWGCVC9QE`  
**归属**: 工时假勤云 / 排班管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wts_rosterlog（排班任务日志） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wts_rosterlog` | 主表 · 29 列 |
| `t_wts_rosterlog_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 任务号 | BillNoField | t_wts_rosterlog.fbillno |  |  |
| taskstatus | 任务状态 | ComboField | t_wts_rosterlog.ftaskstatus |  |  |
| operator | 操作人 | UserField | t_wts_rosterlog.foperatorid |  | bos_user |
| allrosternum | 执行档案数 | IntegerField | t_wts_rosterlog.fallrosternum |  |  |
| succrosternum | 成功 | IntegerField | t_wts_rosterlog.fsuccrosternum |  |  |
| failrosternum | 失败 | IntegerField | t_wts_rosterlog.ffailrosternum |  |  |
| operatetype | 操作类型 | ComboField | t_wts_rosterlog.foperatetype |  |  |
| operateinfo | 操作详情 | TextAreaField | t_wts_rosterlog.foperateinfo |  |  |
| creator | 创建人 | CreaterField | t_wts_rosterlog.fcreatorid |  | bos_user |
| startdate | 开始日期 | DateField | t_wts_rosterlog.fstartdate |  |  |
| enddate | 结束日期 | DateField | t_wts_rosterlog.fenddate |  |  |
| modifier | 修改人 | ModifierField | t_wts_rosterlog.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wts_rosterlog.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_wts_rosterlog.fcreatetime |  |  |
| allrosterpersonnum | 执行的考勤人数 | IntegerField | t_wts_rosterlog.fallrosterpersonnum |  |  |
| succrosterpersonnum | 成功考勤人数 | IntegerField | t_wts_rosterlog.fsuccrosterpersonnum |  |  |
| failrosterpersonnum | 失败考勤人数 | IntegerField | t_wts_rosterlog.ffailrosterpersonnum |  |  |
| operatedetailtype | 详细操作类型 | ComboField | t_wts_rosterlog.foperatedetailtype |  |  |
| unexerosternum | 未执行档案数 | IntegerField | t_wts_rosterlog.funexerosternum |  |  |
| unexepersonnum | 未执行人员数 | IntegerField | t_wts_rosterlog.funexepersonnum |  |  |
| org | 考勤管理组织 | OrgField | t_wts_rosterlog.forgid | ✓ | bos_org |
| bizstatus | 排班状态 | ComboField | t_wts_rosterlog.fbizstatus |  |  |
| source | 来源 | ComboField | t_wts_rosterlog.fsource |  |  |
| description | 描述 | MuliLangTextField | t_wts_rosterlog_l.fdescription |  |  |
| excause | 任务异常原因 | MuliLangTextField | t_wts_rosterlog_l.fexcause |  |  |
| taketimes | 耗时 | TextField | — |  |  |
| runrosternum | 执行档案数 | IntegerField | t_wts_rosterlog.frunrosternum |  |  |
| runpersonnum | 执行人数 | IntegerField | t_wts_rosterlog.frunpersonnum |  |  |
| taskstarttime | 任务开始时间 | DateTimeField | t_wts_rosterlog.ftaskstarttime |  |  |
| taskendtime | 任务结束时间 | DateTimeField | t_wts_rosterlog.ftaskendtime |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wts_rosterlog（主表） | 27 |
| t_wts_rosterlog_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
