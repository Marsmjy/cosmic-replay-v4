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

# wtbd_retrievalconfig — 取数配置

**表单编码**: `wtbd_retrievalconfig`  
**表单ID**: `4/9R=MYZ6W8W`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_retrievalconfig（取数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_retrievalconfig` | 主表 · 27 列 |
| `t_wtbd_rtvlfieldentry` | 分录表 · 3 列 |
| `t_wtbd_rtvlsortentry` | 分录表 · 2 列 |
| `t_wtbd_rtvlrelationentry` | 分录表 · 9 列 |
| `t_wtbd_retrievalconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_retrievalconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_retrievalconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_retrievalconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_retrievalconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_retrievalconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_retrievalconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_retrievalconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_retrievalconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_retrievalconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_retrievalconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_retrievalconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_retrievalconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_retrievalconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_retrievalconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_retrievalconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_retrievalconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_retrievalconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_retrievalconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_retrievalconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_retrievalconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_retrievalconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: fetchfieldentry（取数字段） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| field | 字段 | TextField | — | ✓ |  |
| fieldname | 字段名称 | TextField | — |  |  |
| datatype | 数据类型 | ComboField | t_wtbd_rtvlfieldentry.fdatatype |  |  |
| fetchitem | 取数项目 | BasedataField | t_wtbd_rtvlfieldentry.ffetchitem | ✓ | wtbd_scenefieldcfg |
| fetchitemttype | 取数项目类型 | BasedataPropField | — |  |  |

## 实体: fetchsortentry（取数排序） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sortfield | 字段 | TextField | t_wtbd_rtvlsortentry.fsortfield | ✓ |  |
| sortfieldname | 字段名称 | TextField | — |  |  |
| sorttype | 排序 | ComboField | t_wtbd_rtvlsortentry.fsorttype | ✓ |  |
| fetchsource | 取数实体 | BasedataField | t_wtbd_retrievalconfig.ffetchsourceid | ✓ | hbp_entityobject |
| grouptype | 取数类别 | ComboField | t_wtbd_retrievalconfig.fgrouptype | ✓ |  |
| fetchmode | 取数来源 | ComboField | t_wtbd_retrievalconfig.ffetchmode | ✓ |  |
| outputmode | 结果输出方式 | ComboField | t_wtbd_retrievalconfig.foutputmode | ✓ |  |

## 实体: relationentry（关联信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| filterfield | 字段 | TextField | t_wtbd_rtvlrelationentry.ffilterfield | ✓ |  |
| filterfieldname | 字段名称 | TextField | — |  |  |
| filterdatatype | 字段类型 | ComboField | t_wtbd_rtvlrelationentry.ffilterdatatype |  |  |
| comparetype | 判断条件 | TextField | t_wtbd_rtvlrelationentry.fcomparetype | ✓ |  |
| condition | 判断条件值 | TextField | t_wtbd_rtvlrelationentry.fcondition |  |  |
| comparevaluetype | 比较值类型 | TextField | t_wtbd_rtvlrelationentry.fcomparevaluetype |  |  |
| valuetype | 比较值类型值 | TextField | t_wtbd_rtvlrelationentry.fvaluetype |  |  |
| comparevaluetext | 比较值 | TextField | t_wtbd_rtvlrelationentry.fcomparevaluetext |  |  |
| comparevalue | 比较值使用值 | TextField | t_wtbd_rtvlrelationentry.fcomparevalue |  |  |
| adjustfunction | 比较值调整 | BasedataField | t_wtbd_rtvlrelationentry.fadjustfunction |  | hrcs_function |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_retrievalconfig（主表） | 22 |
| t_wtbd_retrievalconfig_l | 3 |
| t_wtbd_rtvlfieldentry | 2 |
| t_wtbd_rtvlrelationentry | 9 |
| t_wtbd_rtvlsortentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
