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

# hsas_migrationtpl — 历史数据迁移模板

**表单编码**: `hsas_migrationtpl`  
**表单ID**: `2LUVN9D13+/3`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_migrationtpl（历史数据迁移模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_migrationtpl` | 主表 · 21 列 |
| `t_hsas_migrationtplent` | 分录表 · 9 列 |
| `t_hsas_migrationtpl_l` | 多语言表 · 3 列 |
| `t_hsas_migrationtplent_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_migrationtpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_migrationtpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_migrationtpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_migrationtpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_migrationtpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_migrationtpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_migrationtpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_migrationtpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_migrationtpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_migrationtpl_l.fsimplename |  |  |
| description | 备注 | MuliLangTextField | t_hsas_migrationtpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_migrationtpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_migrationtpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_migrationtpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_migrationtpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_migrationtpl.forgid | ✓ | bos_org |
| isarchiveddata | 是否归档数据 | CheckBoxField | t_hsas_migrationtpl.fisarchiveddata |  |  |
| startline | 数据起始行 | IntegerField | t_hsas_migrationtpl.fstartline | ✓ |  |
| endline | 数据结束行 | IntegerField | t_hsas_migrationtpl.fendline | ✓ |  |

## 实体: hsas_migrationtplent（历史数据迁移模板分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| matchcolumn | 对应列 | TextField | t_hsas_migrationtplent.fmatchcolumn | ✓ |  |
| itemtype | 项目类别 | ComboField | t_hsas_migrationtplent.fitemtype |  |  |
| itemnumber | 项目编码 | TextField | — |  |  |
| datatype | 数据类型 | BasedataField | t_hsas_migrationtplent.fdatatypeid |  | hsbs_datatype |
| itemname | 项目名称 | TextField | — |  |  |
| comment | 备注 | MuliLangTextField | t_hsas_migrationtplent_l.fcomment |  |  |
| uniquecode | 唯一编码 | TextField | — |  |  |
| itemid | 项目ID | BigIntField | t_hsas_migrationtplent.fitemid |  |  |
| salaryitem | 薪酬项目 | BasedataField | t_hsas_migrationtplent.fsalaryitemid |  | hsbs_salaryitem |
| fetchitem | 取数项目 | BasedataField | t_hsas_migrationtplent.ffetchitemid |  | hsbs_fetchitem |
| supportitem | 支持项目 | BasedataField | t_hsas_migrationtplent.fsupportitemid |  | hsbs_supportitem |
| bizitem | 业务项目 | BasedataField | t_hsas_migrationtplent.fbizitemid |  | hsbs_bizitem |
| writetasktype | 写入薪资核算任务类型 | ComboField | t_hsas_migrationtpl.fwritetasktype | ✓ |  |
| ismultimeoneperiod | 一期多次 | CheckBoxField | t_hsas_migrationtpl.fismultimeoneperiod |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_migrationtpl（主表） | 18 |
| t_hsas_migrationtpl_l | 3 |
| t_hsas_migrationtplent | 8 |
| t_hsas_migrationtplent_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
