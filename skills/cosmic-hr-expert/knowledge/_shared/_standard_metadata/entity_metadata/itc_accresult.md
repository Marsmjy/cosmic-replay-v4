---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+CT1QBPNP
app_number: itc
app_name: 中国个税
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# itc_accresult — 累加结果

**表单编码**: `itc_accresult`  
**表单ID**: `29E3K=IPHBML`  
**归属**: 薪酬福利云 / 中国个税  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: itc_accresult（累加结果） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_itc_accresult` | 主表 · 16 列 |
| `t_itc_accresultentry` | 分录表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_itc_accresult.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_itc_accresult.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_itc_accresult.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_itc_accresult.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| taxfile | 档案编号 | BasedataField | t_itc_accresult.ftaxfileid | ✓ | itc_taxfile |
| accumulator | 累加器名称 | BasedataField | t_itc_accresult.faccumulatorid | ✓ | sitbs_accumulator |
| instancenum | 累加实例号 | IntegerField | t_itc_accresult.finstancenum | ✓ |  |
| actualenddate | 数据实际结束日期 | DateField | t_itc_accresult.factualenddate |  |  |
| enddate | 累加结束日期 | DateField | t_itc_accresult.fenddate |  |  |
| currency | 币种 | CurrencyField | — |  | bd_currency |
| resultvalue | 累加结果值 | DecimalField | t_itc_accresult.fresultvalue | ✓ |  |

## 实体: accresultentry（累加结果调整分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| changevalue | 调整值 | DecimalField | t_itc_accresultentry.fchangevalue | ✓ |  |
| changereason | 调整原因 | TextField | t_itc_accresultentry.fchangereason | ✓ |  |
| changedatetime | 调整时间 | DateTimeField | t_itc_accresultentry.fchangedatetime |  |  |
| user | 调整人 | UserField | t_itc_accresultentry.fuserid |  | bos_user |
| usernumber | 调整人工号 | BasedataPropField | — |  |  |
| username | 调整人姓名 | BasedataPropField | — |  |  |
| ischanged | 已调整 | ComboField | t_itc_accresult.fischanged |  |  |
| datasource | 累加实例来源 | ComboField | t_itc_accresult.fdatasource |  |  |
| frequency | 累加频度 | BasedataPropField | — |  |  |
| empnumber | 工号 | BasedataPropField | — |  |  |
| empname | 姓名 | BasedataPropField | — |  |  |
| accnumber | 累加器编码 | BasedataPropField | — |  |  |
| startdate | 累加开始日期 | DateField | t_itc_accresult.fstartdate |  |  |
| actualstartdate | 数据实际开始日期 | DateField | t_itc_accresult.factualstartdate |  |  |
| empdate | 任职受雇日期 | DateField | t_itc_accresult.fempdate |  |  |
| lastactualenddate | 上次数据实际结束日期 | DateField | — |  |  |
| lastactualstartdate | 上次数据实际开始日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_itc_accresult（主表） | 15 |
| t_itc_accresultentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
