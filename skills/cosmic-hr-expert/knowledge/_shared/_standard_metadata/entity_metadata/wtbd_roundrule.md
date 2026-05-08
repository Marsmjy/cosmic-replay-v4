---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_roundrule — 舍入规则

**表单编码**: `wtbd_roundrule`  
**表单ID**: `1H814HM8+J5T`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_roundrule（舍入规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_roundrule` | 主表 · 21 列 |
| `t_wtbd_roundruleentry` | 分录表 · 8 列 |
| `t_wtbd_roundrule_l` | 多语言表 · 4 列 |
| `t_wtbd_roundruleentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_roundrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_roundrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_roundrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_roundrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_roundrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_roundrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_roundrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_roundrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_roundrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_roundrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_roundrule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtbd_roundrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_roundrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_roundrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_roundrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_roundrule.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_roundrule.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_roundrule_l.foriname |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| lowerlimitvalue | 下限值 | DecimalField | t_wtbd_roundruleentry.flowerlimitvalue | ✓ |  |
| containlower | 包含下限值 | CheckBoxField | — |  |  |
| upperlimitvalue | 上限值 | DecimalField | t_wtbd_roundruleentry.fupperlimitvalue | ✓ |  |
| containupper | 包含上限值 | CheckBoxField | — |  |  |
| targetvalue | 目标值 | DecimalField | t_wtbd_roundruleentry.ftargetvalue |  |  |
| circulate | 循环 | CheckBoxField | — |  |  |
| remark | 备注 | MuliLangTextField | t_wtbd_roundruleentry_l.fremark |  |  |
| isoriginvalue | 使用原值作为目标值 | CheckBoxField | t_wtbd_roundruleentry.fisoriginvalue |  |  |
| function | 舍入函数 | ComboField | t_wtbd_roundrule.ffunction |  |  |
| accuracy | 精度 | StepperField | t_wtbd_roundrule.faccuracy |  |  |
| settingmode | 设置方式 | RadioGroupField | t_wtbd_roundrule.fsettingmode | ✓ |  |
| radiofield | 规则 | RadioField | — |  |  |
| radiofield1 | 函数 | RadioField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_roundrule（主表） | 17 |
| t_wtbd_roundrule_l | 4 |
| t_wtbd_roundruleentry | 4 |
| t_wtbd_roundruleentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
