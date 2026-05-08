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

# hsas_callistrule — 核算名单规则

**表单编码**: `hsas_callistrule`  
**表单ID**: `2/F4HJ0XCKJ/`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_callistrule（核算名单规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_callistrule` | 主表 · 30 列 |
| `t_hsas_callistrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_callistrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_callistrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_callistrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_callistrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_callistrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_callistrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_callistrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_callistrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_callistrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_callistrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_callistrule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_callistrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_callistrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_callistrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_callistrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_callistrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_callistrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_callistrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_callistrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_callistrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_callistrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_callistrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_callistrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_callistrule.fhisversion |  |  |
| rulecontent | 规则内容 | TextAreaField | t_hsas_callistrule.frulecontent |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_callistrule.forgid | ✓ | bos_org |
| generalenname | 通用英文名 | TextField | t_hsas_callistrule.fgeneralenname |  |  |
| rulecontentshow | 规则内容 | TextAreaField | t_hsas_callistrule.frulecontentshow |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_callistrule.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_callistrule.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_callistrule（主表） | 27 |
| t_hsas_callistrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
