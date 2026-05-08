---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_exrule — 异常规则

**表单编码**: `wtp_exrule`  
**表单ID**: `1HKWXHYIEV5U`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_exrule（异常规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_exrule` | 主表 · 29 列 |
| `t_wtp_exruleentry` | 分录表 · 2 列 |
| `t_wtp_exrulecentry` | 分录表 · 13 列 |
| `t_wtp_exrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_exrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_exrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_exrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_exrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_exrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_exrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_exrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_exrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_exrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_exrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_exrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_exrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_exrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_exrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_exrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_exrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_exrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_exrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_exrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_exrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_exrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_exrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_exrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_exrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_exrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_exrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_exrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_exrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_exrule.fhisversion |  |  |

## 实体: ruleentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| exconfigid | 编码 | BasedataField | t_wtp_exruleentry.fexconfigid | ✓ | wtp_exconfig |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| basedatapropfield1 | 异常类型 | BasedataPropField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_exrulecentry.fentryboid |  |  |

## 实体: entryentity（异常项目转化单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitem | 转化来源 | MulBasedataField | — | ✓ |  |
| rela | 比较符 | ComboField | t_wtp_exrulecentry.frela | ✓ |  |
| value | 起算值 | DecimalField | t_wtp_exrulecentry.fvalue |  |  |
| vunit | 单位 | ComboField | t_wtp_exrulecentry.fvunit | ✓ |  |
| logic | 逻辑 | ComboField | t_wtp_exrulecentry.flogic |  |  |
| relas | 比较符 | ComboField | t_wtp_exrulecentry.frelas | ✓ |  |
| values | 最大值 | DecimalField | t_wtp_exrulecentry.fvalues |  |  |
| vunits | 单位 | ComboField | t_wtp_exrulecentry.fvunits | ✓ |  |
| dealtype | 处理方式 | ComboField | t_wtp_exrulecentry.fdealtype | ✓ |  |
| resattitem | 结果时长 | BasedataField | t_wtp_exrulecentry.fresattitemid | ✓ | wtbd_attitem |
| exattribute | 异常类型 | BasedataField | t_wtp_exrulecentry.fexattributeid | ✓ | wtbd_exattribute |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| ruleshow | 条件 | TextField | — |  |  |
| resultshow | 结果 | TextField | — |  |  |
| appointvalue | 指定时长 | DecimalField | t_wtp_exrulecentry.fappointvalue |  |  |
| appointunit | 指定时长单位 | ComboField | t_wtp_exrulecentry.fappointunit |  |  |
| maxvalues | 最大值 | DecimalField | — |  |  |
| exconfig | 异常配置多选 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_exrule（主表） | 26 |
| t_wtp_exrule_l | 3 |
| t_wtp_exrulecentry | 13 |
| t_wtp_exruleentry | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 15 |
