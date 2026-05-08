---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 33EUPRZ1Q202
app_number: hcss
app_name: 员工薪酬服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcss_incomeproofscheme — 收入证明方案

**表单编码**: `hcss_incomeproofscheme`  
**表单ID**: `3WCE=GCL1J0S`  
**归属**: 薪酬福利云 / 员工薪酬服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcss_incomeproofscheme（收入证明方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcss_incomeprooftpl` | 主表 · 29 列 |
| `t_hcss_tplcfgentry` | 分录表 · 4 列 |
| `t_hcss_receiveentry` | 分录表 · 3 列 |
| `t_hcss_incomeprooftpl_l` | 多语言表 · 4 列 |
| `t_hcss_tplcfgentry_l` | 多语言表 · 1 列 |
| `t_hcss_receiveentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcss_incomeprooftpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcss_incomeprooftpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcss_incomeprooftpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcss_incomeprooftpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcss_incomeprooftpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcss_incomeprooftpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcss_incomeprooftpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcss_incomeprooftpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcss_incomeprooftpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcss_incomeprooftpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcss_incomeprooftpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hcss_incomeprooftpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcss_incomeprooftpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcss_incomeprooftpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcss_incomeprooftpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcss_incomeprooftpl.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hcss_incomeprooftpl.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcss_incomeprooftpl.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hcss_incomeprooftpl.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcss_incomeprooftpl.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcss_incomeprooftpl.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcss_incomeprooftpl.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcss_incomeprooftpl.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcss_incomeprooftpl.fhisversion |  |  |
| org | 薪酬管理组织 | OrgField | t_hcss_incomeprooftpl.forgid | ✓ | bos_org |
| reason | 员工可选证明用途 | MulBasedataField | — | ✓ |  |
| msgtpl | 证明领取通知模板 | BasedataField | t_hcss_incomeprooftpl.fmsgtplid | ✓ | msg_template |
| ispageissue | 纸质证明 | CheckBoxField | — |  |  |
| iselecissue | 电子证明 | CheckBoxField | — |  |  |
| issuetype | 开具形式 | MulComboField | t_hcss_incomeprooftpl.fissuetype | ✓ |  |

## 实体: entryentity（打印模板配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| printtpl | 打印模板 | BasedataField | t_hcss_tplcfgentry.fprinttplid | ✓ | bos_print_meta |
| mulreasonentry | 适用证明用途 | MulBasedataField | — | ✓ |  |
| tpldesc | 模板说明 | MuliLangTextField | t_hcss_tplcfgentry_l.ftpldesc |  |  |
| econtpl | 电子签署配置 | BasedataField | t_hcss_tplcfgentry.fecontplid |  | hrcs_econtemplate |
| entryboid | 分录boId | BigIntField | t_hcss_receiveentry.fentryboid |  |  |
| isuploadtpl | 允许员工上传模板 | CheckBoxField | t_hcss_incomeprooftpl.fisuploadtpl |  |  |
| customtpldesc | 自定义模板说明 | MuliLangTextField | t_hcss_incomeprooftpl_l.fcustomtpldesc |  |  |

## 实体: receivewayentry（纸质证明领取方式配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| receiveway | 领取方式 | BasedataField | t_hcss_receiveentry.freceivewayid | ✓ | hcss_receiveway |
| receivewaydesc | 领取方式说明 | MuliLangTextField | — |  |  |
| entryboid2 | 分录boId | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcss_incomeprooftpl（主表） | 25 |
| t_hcss_incomeprooftpl_l | 4 |
| t_hcss_receiveentry | 2 |
| t_hcss_tplcfgentry | 2 |
| t_hcss_tplcfgentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
