---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 33EUPRZ1Q202
app_number: hcss
app_name: 员工薪酬服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcss_incomeproofhandle — 收入证明办理

**表单编码**: `hcss_incomeproofhandle`  
**表单ID**: `39OJ5/CPVK2K`  
**归属**: 薪酬福利云 / 员工薪酬服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcss_incomeproofhandle（收入证明办理） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcss_incomeproofbill` | 主表 · 41 列 |
| `t_hcss_incomeproofent` | 分录表 · 4 列 |
| `t_hcss_incomeproofbill_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hcss_incomeproofbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hcss_incomeproofbill.fbillstatus | ✓ |  |
| creator | 申请人 | CreaterField | t_hcss_incomeproofbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcss_incomeproofbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hcss_incomeproofbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hcss_incomeproofbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcss_incomeproofbill.fmodifytime |  |  |
| createtime | 申请时间 | CreateDateField | t_hcss_incomeproofbill.fcreatetime |  |  |
| org | 薪酬管理组织 | OrgField | t_hcss_incomeproofbill.forgid |  | bos_org |
| barcode | 条形码 | TextField | t_hcss_incomeproofbill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_hcss_incomeproofbill.finputdevicetype |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | t_hcss_incomeproofbill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hcss_incomeproofbill.fauditstatus | ✓ |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_hcss_incomeproofbill.feventeffectdate |  |  |
| dealstatus | 办理状态 | ComboField | t_hcss_incomeproofbill.fdealstatus |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| revenuefield | 收入证明项目 | BasedataField | t_hcss_incomeproofent.frevenuefieldid |  | hcss_revenuefield |
| calvalue | 计算值 | TextField | t_hcss_incomeproofent.fcalvalue |  |  |
| checkvalue | 开具值 | TextField | t_hcss_incomeproofent.fcheckvalue | ✓ |  |
| entdescription | 备注 | TextField | — |  |  |
| ischeck | 是否已核定 | CheckBoxField | t_hcss_incomeproofbill.fischeck |  |  |
| activityins | 活动实例 | BasedataField | t_hcss_incomeproofbill.factivityinsid |  | hrcs_activityins |
| headsculpture | 人员头像 | PictureField | — |  |  |
| company | 所属公司 | HRAdminOrgField | t_hcss_incomeproofbill.fcompanyid |  | haos_adminorghrf7 |
| adminorg | 行政组织 | HRAdminOrgField | t_hcss_incomeproofbill.fadminorgid |  | haos_adminorghrf7 |
| job | 职位 | BasedataField | t_hcss_incomeproofbill.fjobid |  | hbjm_jobhr |
| applyreason | 证明用途 | BasedataField | t_hcss_incomeproofbill.fapplyreasonid | ✓ | hcss_appliyreason |
| isuploadtpl | 上传自定义模板 | CheckBoxField | t_hcss_incomeproofbill.fisuploadtpl |  |  |
| incometpl | 收入证明方案 | BasedataField | t_hcss_incomeproofbill.fincometplid |  | hcss_incomeproofscheme |
| printtpl | 打印模板 | BasedataField | t_hcss_incomeproofbill.fprinttplid |  | bos_print_meta |
| description | 备注 | TextAreaField | t_hcss_incomeproofent.fdescription |  |  |
| issuetype | 开具形式 | ComboField | t_hcss_incomeproofbill.fissuetype | ✓ |  |
| issuenum | 开具份数 | IntegerField | t_hcss_incomeproofbill.fissuenum | ✓ |  |
| getdate | 期望领取日期 | DateField | t_hcss_incomeproofbill.fgetdate |  |  |
| printnum | 打印证明次数 | IntegerField | t_hcss_incomeproofbill.fprintnum |  |  |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| receiveway | 领取方式 | BasedataField | t_hcss_incomeproofbill.freceivewayid |  | hcss_receiveway |
| recipient | 收件人 | TextField | t_hcss_incomeproofbill.frecipient |  |  |
| recipientphone | 手机号码 | TelephoneField | t_hcss_incomeproofbill.frecipientphone |  |  |
| recipientaddress | 收件地址 | AddressField | — |  | cts_address |
| lawentity | 证明开具单位 | BasedataField | t_hcss_incomeproofbill.flawentityid |  | hbss_lawentity |
| econtfailreason | 签署失败原因 | MuliLangTextField | t_hcss_incomeproofbill_l.fecontfailreason |  |  |
| econtpl | 电子签署配置 | BasedataField | t_hcss_incomeproofbill.fecontplid |  | hrcs_econtemplate |
| datasource | 数据来源 | ComboField | t_hcss_incomeproofbill.fdatasource |  |  |
| econnumber | 合同编号 | TextField | t_hcss_incomeproofbill.feconnumber |  |  |
| name | 姓名 | QueryPropField | — |  |  |
| number | 工号 | QueryPropField | — |  |  |
| employee | 员工 | EmployeeField | t_hcss_incomeproofbill.femployeeid |  | hsbs_employeequery |
| phone | 手机 | TelephoneField | t_hcss_incomeproofbill.fphone |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcss_incomeproofbill（主表） | 37 |
| t_hcss_incomeproofbill_l | 1 |
| t_hcss_incomeproofent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
