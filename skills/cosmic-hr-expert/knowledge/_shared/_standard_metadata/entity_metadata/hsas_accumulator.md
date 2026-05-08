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

# hsas_accumulator — 累加器

**表单编码**: `hsas_accumulator`  
**表单ID**: `1V058M7EV7JX`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_accumulator（累加器） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_accumulator` | 主表 · 40 列 |
| `t_hsas_dimensionentry` | 分录表 · 2 列 |
| `t_hsas_cumulatemember` | 分录表 · 11 列 |
| `t_hsas_accumulator_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_accumulator.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_accumulator_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_accumulator.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_accumulator.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_accumulator.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_accumulator.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_accumulator.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_accumulator.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_accumulator.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_accumulator.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_accumulator.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_accumulator.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_accumulator.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_accumulator.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_accumulator.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_accumulator.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_accumulator_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_accumulator_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_accumulator.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_accumulator.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_accumulator.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_accumulator.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_accumulator.fcountryid |  | bd_country |
| dataround | 精度尾差处理 | BasedataField | t_hsas_accumulator.fdataroundid | ✓ | hsbs_dataround |
| datatype | 数据类型 | BasedataField | t_hsas_accumulator.fdatatypeid | ✓ | hsbs_datatype |
| dataprecision | 数据精度 | BasedataField | t_hsas_accumulator.fdataprecisionid |  | hsbs_dataprecision |
| updatestrategy | 累加器更新策略 | ComboField | t_hsas_accumulator.fupdatestrategy | ✓ |  |
| periodtype | 累加频度 | ComboField | t_hsas_accumulator.fperiodtype | ✓ |  |
| startdatetype | 开始日期类型 | ComboField | t_hsas_accumulator.fstartdatetype |  |  |
| startdate | 开始日期 | DateField | t_hsas_accumulator.fstartdate |  |  |
| startmonth | 开始月值 | ComboField | t_hsas_accumulator.fstartmonth |  |  |
| startday | 开始日值 | IntegerField | t_hsas_accumulator.fstartday |  |  |
| bsedstrategy | 累加生效策略 | ComboField | t_hsas_accumulator.fbsedstrategy |  |  |
| accdimension | 累加维度 | ComboField | t_hsas_accumulator.faccdimension | ✓ |  |

## 实体: accdimitementry（累加维度） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dimitemtype | 累加子维度项目类型 | ComboField | t_hsas_dimensionentry.fdimitemtype | ✓ |  |
| dimitem | 累加子维度项目名称 | TextField | — |  |  |
| fetchitem | 取数项目 | BasedataField | t_hsas_dimensionentry.ffetchitemid |  | hsbs_fetchitem |

## 实体: accmemberentry（累加成员） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| membertype | 累加成员项目类型 | ComboField | t_hsas_cumulatemember.fmembertype | ✓ |  |
| salaryitem | 薪酬项目 | BasedataField | t_hsas_cumulatemember.fsalaryitemid |  | hsbs_salaryitem |
| bizitem | 业务项目 | BasedataField | t_hsas_cumulatemember.fbizitemid |  | hsbs_bizitem |
| accmemstartdate | 开始日期 | DateField | t_hsas_cumulatemember.faccmemstartdate | ✓ |  |
| accmemenddate | 结束日期 | DateField | t_hsas_cumulatemember.faccmemenddate |  |  |
| operator | 运算符号 | ComboField | t_hsas_cumulatemember.foperator | ✓ |  |
| accpercenttype | 累加百分比类型 | ComboField | t_hsas_cumulatemember.faccpercenttype | ✓ |  |
| percentfixval | 百分比固定值 | DecimalField | t_hsas_cumulatemember.fpercentfixval |  |  |
| accmemberitem | 累加成员项目名称 | TextField | — |  |  |
| memuniquecode | 累加成员序号 | TextField | — |  |  |
| formula | 计算公式 | HisModelBasedataField | — |  | hsas_formula |
| fixparam | 固定参数 | ComboField | t_hsas_cumulatemember.ffixparam |  |  |
| accpercent | 累加百分比 | TextField | — |  |  |
| uniquecode | 唯一编码 | TextField | t_hsas_cumulatemember.funiquecode |  |  |
| startdateitem | 开始日期项目 | BasedataField | t_hsas_accumulator.fstartdateitemid |  | hsbs_fetchitem |
| isorgcontrol | 计算时基于组织进行管控 | CheckBoxField | t_hsas_accumulator.fisorgcontrol |  |  |
| isacccalnoorder | 运行时不校验累加计算顺序 | CheckBoxField | t_hsas_accumulator.fisacccalnoorder |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_accumulator（主表） | 34 |
| t_hsas_accumulator_l | 3 |
| t_hsas_cumulatemember | 10 |
| t_hsas_dimensionentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
