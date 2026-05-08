---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_taxtemplate — 导入导出方案配置

**表单编码**: `sitbs_taxtemplate`  
**表单ID**: `1GRZT=LY0P8K`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_taxtemplate（导入导出方案配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_taxtemplate` | 主表 · 25 列 |
| `t_sitbs_taxtemplateentry` | 分录表 · 8 列 |
| `t_sitbs_taxtemplate_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_taxtemplate.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_taxtemplate_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_taxtemplate.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_taxtemplate.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_taxtemplate.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_taxtemplate.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_taxtemplate.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_taxtemplate.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_taxtemplate.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_taxtemplate_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_taxtemplate_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_sitbs_taxtemplate.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_taxtemplate.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_taxtemplate.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_taxtemplate.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_taxtemplate.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_taxtemplate.fcountryid |  | bd_country |
| templatetype | 方案类型 | ComboField | t_sitbs_taxtemplate.ftemplatetype | ✓ |  |
| scenetype | 场景类型 | ComboField | t_sitbs_taxtemplate.fscenetype | ✓ |  |
| taxcategory | 适用场景 | BasedataField | t_sitbs_taxtemplate.ftaxcategoryid |  | sitbs_taxcategory |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isexport | 是否导出字段 | CheckBoxField | t_sitbs_taxtemplateentry.fisexport |  |  |
| ismustinput | 表头添加必填标识 | CheckBoxField | t_sitbs_taxtemplateentry.fismustinput |  |  |
| isonly | 是否数据唯一性判断 | ComboField | t_sitbs_taxtemplateentry.fisonly |  |  |
| taxitem | 个税项目 | BasedataField | t_sitbs_taxtemplateentry.ftaxitemid |  | sitbs_taxitem |
| importfield | 从本地导入的字段 | TextField | t_sitbs_taxtemplateentry.fimportfield |  |  |
| taxdisplayname | 对应的系统字段 | TextField | — |  |  |
| exportfield | 导出到本地的字段 | TextField | t_sitbs_taxtemplateentry.fexportfield |  |  |
| fieldkey | 系统字段 | TextField | t_sitbs_taxtemplateentry.ffieldkey |  |  |
| taxitemnumber | 个税项目编码 | BasedataPropField | — |  |  |
| notes | 备注 | TextField | t_sitbs_taxtemplateentry.fnotes |  |  |
| curpage | 当前页 | IntegerField | — |  |  |
| taxgroup | 适用场景 | BasedataField | t_sitbs_taxtemplate.ftaxgroupid |  | sitbs_taxgroup |
| scene | 适用场景 | ComboField | t_sitbs_taxtemplate.fscene |  |  |
| asteriskposition | 导出必填标识位置 | RadioGroupField | t_sitbs_taxtemplate.fasteriskposition |  |  |
| scenesearch | 适用场景 | TextField | t_sitbs_taxtemplate.fscenesearch |  |  |
| headerlinenumber | 表头行号 | ComboField | t_sitbs_taxtemplate.fheaderlinenumber | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_taxtemplate（主表） | 22 |
| t_sitbs_taxtemplate_l | 3 |
| t_sitbs_taxtemplateentry | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
