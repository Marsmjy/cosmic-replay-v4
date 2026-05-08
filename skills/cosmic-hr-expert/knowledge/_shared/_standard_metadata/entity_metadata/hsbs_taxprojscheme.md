---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_taxprojscheme — 个税项目映射方案

**表单编码**: `hsbs_taxprojscheme`  
**表单ID**: `1HALL50PC+/9`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_taxprojscheme（个税项目映射方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_taxitemscheme` | 主表 · 25 列 |
| `t_hsbs_salarymapent` | 分录表 · 4 列 |
| `t_hsbs_taxmapent` | 分录表 · 5 列 |
| `t_hsbs_taxitemscheme_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_taxitemscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_taxitemscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_taxitemscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_taxitemscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_taxitemscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_taxitemscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_taxitemscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_taxitemscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_taxitemscheme.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_taxitemscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_taxitemscheme_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_taxitemscheme.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_taxitemscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_taxitemscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_taxitemscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_taxitemscheme.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_taxitemscheme.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_taxitemscheme.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsbs_taxitemscheme.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_taxitemscheme.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_taxitemscheme.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_taxitemscheme.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_taxitemscheme.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_taxitemscheme.fhisversion |  |  |

## 实体: salarymapentry（薪酬项目映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| pretaxitem | 个税项目名称 | BasedataField | — | ✓ | sitbs_taxitem |
| presalaryitem | 薪酬项目名称 | BasedataField | — | ✓ | hsbs_salaryitem |
| presalaryitemnum | 薪酬项目编码 | BasedataPropField | — |  |  |
| pretaxitemnum | 个税项目编码 | BasedataPropField | — |  |  |
| pretaxcategory | 个税种类 | BasedataField | — | ✓ | sitbs_taxcategory |
| pretaxpayertype | 纳税人类型 | BasedataPropField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsbs_salarymapent.fentryboid |  |  |

## 实体: taxmapentry（个税项目映射） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| aftertaxitem | 个税项目名称 | BasedataField | — | ✓ | sitbs_taxitem |
| aftersalaryitem | 薪酬项目名称 | BasedataField | — | ✓ | hsbs_salaryitem |
| aftertaxitemnum | 个税项目编码 | BasedataPropField | — |  |  |
| incaomeitem | 所得项目 | BasedataPropField | — |  |  |
| aftersalaryitemnum | 薪酬项目编码 | BasedataPropField | — |  |  |
| aftertaxcategory | 个税种类 | BasedataField | — | ✓ | sitbs_taxcategory |
| aftertaxpayertype | 纳税人类型 | BasedataPropField | — |  |  |
| entryboidtax | 个税分录业务ID | BigIntField | t_hsbs_taxmapent.fentryboidtax |  |  |
| org | 算发薪管理组织 | OrgField | t_hsbs_taxitemscheme.forgid | ✓ | bos_org |
| country | 国家/地区 | BasedataField | t_hsbs_taxitemscheme.fcountryid | ✓ | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_taxitemscheme（主表） | 23 |
| t_hsbs_salarymapent | 1 |
| t_hsbs_taxitemscheme_l | 3 |
| t_hsbs_taxmapent | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
