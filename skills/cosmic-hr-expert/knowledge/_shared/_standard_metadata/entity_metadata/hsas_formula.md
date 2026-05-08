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

# hsas_formula — 计算公式

**表单编码**: `hsas_formula`  
**表单ID**: `2JFOZYIHTDSZ`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_formula（计算公式） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_formula` | 主表 · 45 列 |
| `t_hsas_formula_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_formula.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_formula_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_formula.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_formula.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_formula.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_formula.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_formula.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_formula.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_formula.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_formula.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_formula.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_formula.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_formula.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_formula.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_formula.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_formula_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_formula_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_formula.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_formula.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_formula.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_formula.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_formula.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_formula.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_formula.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_formula.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_formula.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_formula.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_formula.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_formula.fhisversion |  |  |
| originalexp | 公式体 | TextAreaField | t_hsas_formula.foriginalexp |  |  |
| uniquecodeexp | 关键字表达式 | TextAreaField | t_hsas_formula.funiquecodeexp |  |  |
| executeexp | 可执行表达式 | TextAreaField | t_hsas_formula.fexecuteexp |  |  |
| dependonfunc | 依赖函数 | TextAreaField | t_hsas_formula.fdependonfunc |  |  |
| dependonsitem | 依赖薪酬项目 | TextAreaField | t_hsas_formula.fdependonsitem |  |  |
| isdraft | 是否为草稿 | CheckBoxField | t_hsas_formula.fisdraft |  |  |
| dependonbsitem | 依赖业务项目 | TextAreaField | t_hsas_formula.fdependonbsitem |  |  |
| dependonspitem | 依赖支持项目 | TextAreaField | t_hsas_formula.fdependonspitem |  |  |
| dependondatagrade | 依赖数据分级 | TextAreaField | t_hsas_formula.fdependondatagrade |  |  |
| dependonacc | 依赖累加器 | TextAreaField | t_hsas_formula.fdependonacc |  |  |
| dependonftitem | 依赖取数项目 | TextAreaField | t_hsas_formula.fdependonftitem |  |  |
| salaryitem | 薪酬项目 | BasedataField | t_hsas_formula.fsalaryitemid |  | hsbs_salaryitem |
| areatype | 国家/地区类型 | ComboField | t_hsas_formula.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_formula.fcountryid |  | bd_country |
| usetype | 公式用途 | ComboField | — | ✓ |  |
| bsitemprop | 业务项目属性 | TextAreaField | — |  |  |
| dependonsditem | 依赖定调薪标准项目 | TextAreaField | t_hsas_formula.fdependonsditem |  |  |
| dependonadjproperty | 依赖定调薪属性 | TextAreaField | t_hsas_formula.fdependonadjproperty |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_formula（主表） | 42 |
| t_hsas_formula_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
