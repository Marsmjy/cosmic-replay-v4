---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2AXKDRPJUQ77
app_number: hcsi
app_name: 中国社保
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcsi_sinsurdclrule — 申报名单规则

**表单编码**: `hcsi_sinsurdclrule`  
**表单ID**: `2U7=342K+QMT`  
**归属**: 薪酬福利云 / 中国社保  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcsi_sinsurdclrule（申报名单规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcsi_sinsurdclrule` | 主表 · 26 列 |
| `t_hcsi_sinsurdclruleent` | 分录表 · 5 列 |
| `t_hcsi_sinsurdclrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcsi_sinsurdclrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcsi_sinsurdclrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcsi_sinsurdclrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcsi_sinsurdclrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcsi_sinsurdclrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcsi_sinsurdclrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcsi_sinsurdclrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcsi_sinsurdclrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcsi_sinsurdclrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcsi_sinsurdclrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcsi_sinsurdclrule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hcsi_sinsurdclrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcsi_sinsurdclrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcsi_sinsurdclrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcsi_sinsurdclrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcsi_sinsurdclrule.fboid |  |  |
| iscurrentversion | 是否当前版本 | CheckBoxField | t_hcsi_sinsurdclrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcsi_sinsurdclrule.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hcsi_sinsurdclrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcsi_sinsurdclrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcsi_sinsurdclrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcsi_sinsurdclrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcsi_sinsurdclrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcsi_sinsurdclrule.fhisversion |  |  |
| generalenname | 通用英文名 | TextField | t_hcsi_sinsurdclrule.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hcsi_sinsurdclrule.fcountryid | ✓ | bd_country |

## 实体: entryentity（申报名单规则） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dclbusinessname | 申报业务名称 | TextField | t_hcsi_sinsurdclruleent.fdclbusinessname | ✓ |  |
| modifytype | 变动类型 | ComboField | t_hcsi_sinsurdclruleent.fmodifytype | ✓ |  |
| haswelfaretype | 是否指定险种 | ComboField | t_hcsi_sinsurdclruleent.fhaswelfaretype | ✓ |  |
| welfaretypemul | 指定险种 | MulBasedataField | — |  |  |
| entryboid | 分录业务id | BigIntField | t_hcsi_sinsurdclruleent.fentryboid |  |  |
| dcldisplayscm | 申报名单显示方案 | BasedataField | t_hcsi_sinsurdclruleent.fdcldisplayscmid | ✓ | hcsi_dcldisplayscm |
| instructions | 规则说明下拉 | ComboField | — |  |  |
| instructionstxt | 规则说明 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcsi_sinsurdclrule（主表） | 23 |
| t_hcsi_sinsurdclrule_l | 3 |
| t_hcsi_sinsurdclruleent | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
