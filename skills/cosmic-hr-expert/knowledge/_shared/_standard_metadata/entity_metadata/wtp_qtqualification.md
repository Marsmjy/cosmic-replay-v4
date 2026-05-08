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

# wtp_qtqualification — 限定条件折算

**表单编码**: `wtp_qtqualification`  
**表单ID**: `338RX2A/XRUK`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtqualification（限定条件折算） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtqualification` | 主表 · 38 列 |
| `t_wtp_qtqualificationinf` | 分录表 · 10 列 |
| `t_wtp_qtqualification_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtqualification.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtqualification_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtqualification.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtqualification.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtqualification.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtqualification.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtqualification.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtqualification.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtqualification.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtqualification.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtqualification.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtqualification.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtqualification.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtqualification.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtqualification.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtqualification.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtqualification_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtqualification_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtqualification.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtqualification.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtqualification.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtqualification.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtqualification.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtqualification.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtqualification.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtqualification.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtqualification.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtqualification.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtqualification.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtqualification.fhisversion |  |  |
| relation | 比较符 | TextField | t_wtp_qtqualificationinf.frelation |  |  |
| inspectrang | 检测范围 | TextField | t_wtp_qtqualificationinf.finspectrang | ✓ |  |
| attitem | 考勤项目限定 | BasedataField | t_wtp_qtqualificationinf.fattitemid |  | wtbd_attitem |
| limitmethod | 限定方式 | TextField | t_wtp_qtqualificationinf.flimitmethod |  |  |
| limitprecentg | 限定百分比 | DecimalField | t_wtp_qtqualificationinf.flimitprecentg |  |  |
| qualitytype | 限定类型 | TextField | t_wtp_qtqualificationinf.fqualitytype |  |  |
| quatity | 数量 | DecimalField | t_wtp_qtqualificationinf.fquatity |  |  |
| limitquatity | 限定数量 | DecimalField | t_wtp_qtqualificationinf.flimitquatity |  |  |
| varcondition | 变量条件限定 | BasedataField | t_wtp_qtqualificationinf.fvarconditionid |  | wtbd_qtvarcondition |
| entryboid | 分录BOID | BigIntField | t_wtp_qtqualificationinf.fentryboid |  |  |
| rulecondition | 规则条件 | TextField | — |  |  |
| result | 结果 | TextField | — |  |  |
| inspectrangm | 检查范围 | ComboField | — | ✓ |  |
| attitemcheckbox | 按考勤项目 | CheckBoxField | — |  |  |
| varcheckbox | 按限定条件 | CheckBoxField | — |  |  |

## 实体: condsubentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| limitno | # | TextField | — |  |  |
| condattitem | 考勤项目 | BasedataField | — |  | wtbd_attitem |
| unit | 单位 | BasedataPropField | — |  |  |
| condrel | 关系 | ComboField | — |  |  |
| condvalue | 值 | DecimalField | — |  |  |
| basedatapropfield1 | 类型 | BasedataPropField | — |  |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| limitquatitym | 数量 | DecimalField | — | ✓ |  |
| limitprecentgm | 数量 | DecimalField | — | ✓ |  |

## 实体: varcondsubentryentity（变量条件单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| limitnov | # | TextField | — |  |  |
| varconditionm | 限定条件 | BasedataField | — |  | wtbd_qtvarcondition |
| varcondrel | 关系 | ComboField | — |  |  |
| varcondvalue | 值 | DecimalField | — |  |  |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| logicstr | 隐藏的条件文本 | TextField | — |  |  |
| conditionstr | 条件 | TextField | t_wtp_qtqualification.fconditionstr | ✓ |  |
| logictype | 条件逻辑 | TextField | — |  |  |
| qualitytypem | 限定类型 | ComboField | — |  |  |
| resultm | 业务语义 | TextAreaField | — |  |  |
| limitmethodm | 定额生成方式 | ComboField | — | ✓ |  |
| limitquatityv | 值 | DecimalField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtqualification（主表） | 28 |
| t_wtp_qtqualification_l | 3 |
| t_wtp_qtqualificationinf | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 31 |
