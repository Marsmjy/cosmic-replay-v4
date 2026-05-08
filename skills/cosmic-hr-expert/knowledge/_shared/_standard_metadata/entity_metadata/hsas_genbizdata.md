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

# hsas_genbizdata — 业务数据生成配置

**表单编码**: `hsas_genbizdata`  
**表单ID**: `3ZJEC3LIV=SR`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_genbizdata（业务数据生成配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_genbizdata` | 主表 · 29 列 |
| `t_hsas_itemconfentry` | 分录表 · 4 列 |
| `t_hsas_genbizdata_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_genbizdata.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_genbizdata_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_genbizdata.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_genbizdata.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_genbizdata.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_genbizdata.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_genbizdata.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_genbizdata.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_genbizdata.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_genbizdata.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_genbizdata.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_genbizdata.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_genbizdata.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_genbizdata.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_genbizdata.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_genbizdata.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_genbizdata_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_genbizdata_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_genbizdata.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_genbizdata.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_genbizdata.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_genbizdata.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_genbizdata.fcountryid |  | bd_country |
| isautogenbsed | 自动生成生效日期 | CheckBoxField | t_hsas_genbizdata.fisautogenbsed |  |  |
| basedatetype | 基准日期 | ComboField | t_hsas_genbizdata.fbasedatetype |  |  |
| month | 月份 | ComboField | t_hsas_genbizdata.fmonth |  |  |
| datetype | 日期 | ComboField | t_hsas_genbizdata.fdatetype |  |  |

## 实体: entryentity（业务数据推送配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemtype | 项目类型 | ComboField | t_hsas_itemconfentry.fitemtype | ✓ |  |
| itemname | 项目名称 | TextField | — | ✓ |  |
| itemid | 项目id | BigIntField | t_hsas_itemconfentry.fitemid |  |  |
| refbizitem | 映射业务项目 | BasedataField | t_hsas_itemconfentry.frefbizitemid | ✓ | hsbs_bizitem |
| datatype | 数据类型 | BasedataField | t_hsas_itemconfentry.fdatatypeid | ✓ | hsbs_datatype |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_genbizdata（主表） | 24 |
| t_hsas_genbizdata_l | 3 |
| t_hsas_itemconfentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
