---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_paynodescm — 时间窗口

**表单编码**: `hsas_paynodescm`  
**表单ID**: `19VC9NN7S3NM`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_paynodescm（时间窗口） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_paynodescm` | 主表 · 35 列 |
| `t_hsas_paynodescment` | 分录表 · 2 列 |
| `t_hsas_paynodescmdtl` | 分录表 · 4 列 |
| `t_hsas_paynodescm_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_paynodescm.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_paynodescm_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_paynodescm.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_paynodescm.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_paynodescm.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_paynodescm.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_paynodescm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_paynodescm.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_paynodescm.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_paynodescm.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_paynodescm.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_paynodescm.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_paynodescm.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_paynodescm.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_paynodescm.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_paynodescm.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_paynodescm_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_paynodescm_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_paynodescm.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_paynodescm.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_paynodescm.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_paynodescm.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_paynodescm.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_paynodescm.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_paynodescm.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_paynodescm.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_paynodescm.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_paynodescm.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_paynodescm.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_paynodescm.fhisversion |  |  |
| paynodegrp | 时间窗口模板 | BasedataField | t_hsas_paynodescm.fpaynodegrpid | ✓ | hsas_paynodegrp |
| startcalperiod | 开始期间 | BasedataField | — |  | hsbs_calperiod |
| endcalperiod | 截止期间 | BasedataField | — |  | hsbs_calperiod |

## 实体: entryentity（时间窗口分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| calperiod | 期间信息 | BasedataField | t_hsas_paynodescment.fcalperiodid |  | hsbs_calperiod |
| changestatus | 是否允许变更 | ComboField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_paynodescment.fentryboid |  |  |
| paynode | 发薪节点 | BasedataField | t_hsas_paynodescmdtl.fpaynodeid |  | hsas_paynodegrpenthis |
| starttime | 开始时间 | DateTimeField | t_hsas_paynodescmdtl.fstarttime |  |  |
| endtime | 截止时间 | DateTimeField | t_hsas_paynodescmdtl.fendtime |  |  |
| entryboidchildren | 分录业务ID | BigIntField | t_hsas_paynodescmdtl.fentryboidchildren |  |  |
| calperiodtype | 期间类型 | BasedataPropField | — |  |  |
| frequency | 频度 | BasedataField | t_hsas_paynodescm.ffrequencyid |  | hsbs_calfrequency |
| paynodegrphis | 时间窗口模板版本 | HisModelBasedataField | — | ✓ | hsas_paynodegrp |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_paynodescm（主表） | 29 |
| t_hsas_paynodescm_l | 3 |
| t_hsas_paynodescmdtl | 4 |
| t_hsas_paynodescment | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
