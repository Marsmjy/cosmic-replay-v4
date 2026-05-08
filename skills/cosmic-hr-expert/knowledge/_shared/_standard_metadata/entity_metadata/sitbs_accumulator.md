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

# sitbs_accumulator — 累加器

**表单编码**: `sitbs_accumulator`  
**表单ID**: `25R7VH60GQ2L`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_accumulator（累加器） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_accumulator` | 主表 · 31 列 |
| `t_sitbs_cumulatemember` | 分录表 · 7 列 |
| `t_sitbs_accumulator_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_accumulator.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_accumulator_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_accumulator.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_accumulator.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_accumulator.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_accumulator.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_accumulator.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_accumulator.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_accumulator.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_accumulator_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_accumulator_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_sitbs_accumulator.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_accumulator.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_accumulator.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_accumulator.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_accumulator.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_accumulator.fcountryid |  | bd_country |
| taxcategories | 个税种类 | BasedataField | t_sitbs_accumulator.ftaxcategoriesid | ✓ | sitbs_taxcategory |
| datatype | 数据类型 | BasedataField | t_sitbs_accumulator.fdatatypeid | ✓ | sitbs_datatype |
| dataprecision | 数据精度 | BasedataField | t_sitbs_accumulator.fdataprecisionid |  | sitbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_sitbs_accumulator.fdataroundid | ✓ | sitbs_dataround |
| updatestrategy | 累加器更新策略 | ComboField | t_sitbs_accumulator.fupdatestrategy | ✓ |  |
| periodtype | 累加频度 | ComboField | t_sitbs_accumulator.fperiodtype | ✓ |  |
| startdatetype | 开始日期类型 | ComboField | t_sitbs_accumulator.fstartdatetype |  |  |
| startdate | 开始日期 | DateField | t_sitbs_accumulator.fstartdate |  |  |
| startmonthval | 开始月值 | ComboField | t_sitbs_accumulator.fstartmonthval |  |  |
| startday | 开始日值 | IntegerField | t_sitbs_accumulator.fstartday |  |  |
| bsedstrategy | 累加生效策略 | ComboField | t_sitbs_accumulator.fbsedstrategy |  |  |
| accdimension | 累加维度 | ComboField | t_sitbs_accumulator.faccdimension | ✓ |  |

## 实体: entryentity（累加成员） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| uniquecode | 累加成员唯一识别码 | TextField | t_sitbs_cumulatemember.funiquecode |  |  |
| membername | 累加成员名称 | TextField | — |  |  |
| taxitem | 个税项目 | BasedataField | t_sitbs_cumulatemember.ftaxitemid |  | sitbs_taxitem |
| accmenstartdate | 开始日期 | DateField | t_sitbs_cumulatemember.faccmenstartdate | ✓ |  |
| accmemenddate | 结束日期 | DateField | t_sitbs_cumulatemember.faccmemenddate |  |  |
| operator | 运算符号 | ComboField | t_sitbs_cumulatemember.foperator | ✓ |  |
| accpercenttype | 累加百分比类型 | ComboField | t_sitbs_cumulatemember.faccpercenttype | ✓ |  |
| percentfixval | 百分比固定值 | DecimalField | t_sitbs_cumulatemember.fpercentfixval | ✓ |  |
| accuniquecode | 累加器唯一识别码 | TextField | t_sitbs_accumulator.faccuniquecode |  |  |
| reaccploy | 重新开始累加策略 | BasedataField | t_sitbs_accumulator.freaccployid |  | sitbs_reaccploy |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_accumulator（主表） | 28 |
| t_sitbs_accumulator_l | 3 |
| t_sitbs_cumulatemember | 7 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
