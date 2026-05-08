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

# hsas_taxtemplate — 个税导入导出方案

**表单编码**: `hsas_taxtemplate`  
**表单ID**: `5GHXABV3UKQK`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_taxtemplate（个税导入导出方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_taxtemplate` | 主表 · 27 列 |
| `t_hsas_taxtemplateentry` | 分录表 · 9 列 |
| `t_hsas_taxtemplate_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_taxtemplate.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_taxtemplate_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_taxtemplate.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_taxtemplate.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_taxtemplate.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_taxtemplate.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_taxtemplate.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_taxtemplate.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_taxtemplate.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_taxtemplate_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_taxtemplate_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_taxtemplate.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_taxtemplate.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_taxtemplate.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_taxtemplate.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hsas_taxtemplate.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hsas_taxtemplate.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hsas_taxtemplate_l.foriname |  |  |
| templatetype | 方案类型 | ComboField | t_hsas_taxtemplate.ftemplatetype | ✓ |  |
| scenetype | 场景类型 | ComboField | t_hsas_taxtemplate.fscenetype | ✓ |  |
| scene | 适用场景 | ComboField | t_hsas_taxtemplate.fscene |  |  |
| headerrownumber | 表头行号 | ComboField | t_hsas_taxtemplate.fheaderrownumber | ✓ |  |
| defaulttemplate | 默认方案 | ComboField | t_hsas_taxtemplate.fdefaulttemplate | ✓ |  |
| taxgroup | 适用场景 | BasedataField | t_hsas_taxtemplate.ftaxgroupid |  | hsbs_taxgroup |
| taxcategory | 适用场景 | BasedataField | t_hsas_taxtemplate.ftaxcategoryid |  | hsbs_taxcategory |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| field | 系统字段 | TextField | t_hsas_taxtemplateentry.ffield |  |  |
| importfield | 从本地导入的字段 | TextField | t_hsas_taxtemplateentry.fimportfield |  |  |
| exportfield | 导出到本地的字段 | TextField | t_hsas_taxtemplateentry.fexportfield |  |  |
| salaryitem | 薪酬项目 | BasedataField | t_hsas_taxtemplateentry.fsalaryitemid |  | hsbs_salaryitem |
| salaryitemnumber | 薪酬项目编码 | BasedataPropField | — |  |  |
| trdtaxfield | 第三方个税字段 | BasedataField | t_hsas_taxtemplateentry.ftrdtaxfieldid |  | hsbs_trdtaxfield |
| trdtaxfieldnumber | 第三方个税字段编码 | BasedataPropField | — |  |  |
| isexport | 是否导出字段 | CheckBoxField | t_hsas_taxtemplateentry.fisexport |  |  |
| ismustinput | 表头添加必填标识 | CheckBoxField | t_hsas_taxtemplateentry.fismustinput |  |  |
| isunique | 是否数据唯一性判断 | ComboField | t_hsas_taxtemplateentry.fisunique |  |  |
| fieldname | 对应的系统字段 | TextField | — |  |  |
| remark | 备注 | TextField | t_hsas_taxtemplateentry.fremark |  |  |
| accitem | 累计项 | BasedataPropField | — |  |  |
| curpage | 当前页 | IntegerField | — |  |  |
| asteriskposition | 导出必填标识位置 | RadioGroupField | t_hsas_taxtemplate.fasteriskposition |  |  |
| scenesearch | 适用场景 | TextField | t_hsas_taxtemplate.fscenesearch |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_taxtemplate（主表） | 23 |
| t_hsas_taxtemplate_l | 4 |
| t_hsas_taxtemplateentry | 9 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
