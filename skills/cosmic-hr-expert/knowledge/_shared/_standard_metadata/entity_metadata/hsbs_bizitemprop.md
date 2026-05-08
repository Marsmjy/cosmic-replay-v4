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

# hsbs_bizitemprop — 业务项目属性

**表单编码**: `hsbs_bizitemprop`  
**表单ID**: `34EIVXY2RICV`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_bizitemprop（业务项目属性） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_bizitemprop` | 主表 · 34 列 |
| `t_hsbs_bizitemprop_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_bizitemprop.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_bizitemprop_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_bizitemprop.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_bizitemprop.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_bizitemprop.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_bizitemprop.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_bizitemprop.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_bizitemprop.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_bizitemprop.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_bizitemprop.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_bizitemprop.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_bizitemprop.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_bizitemprop.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_bizitemprop.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_bizitemprop.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_bizitemprop.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_bizitemprop_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_bizitemprop_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_bizitemprop.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_bizitemprop.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_bizitemprop.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_bizitemprop.fgeneralenname |  |  |
| datatype | 数据类型 | BasedataField | t_hsbs_bizitemprop.fdatatypeid | ✓ | hsbs_datatype |
| scalelimit | 小数位数限制 | ComboField | t_hsbs_bizitemprop.fscalelimit |  |  |
| inputminval | 最小输入值 | TextField | — |  |  |
| inputmaxval | 最大输入值 | TextField | — |  |  |
| earliestdate | 最早日期 | DateField | t_hsbs_bizitemprop.fearliestdate |  |  |
| lastdate | 最晚日期 | DateField | t_hsbs_bizitemprop.flastdate |  |  |
| datalength | 数据长度 | IntegerField | t_hsbs_bizitemprop.fdatalength |  |  |
| propuniquecode | 属性唯一编码 | TextField | t_hsbs_bizitemprop.fpropuniquecode |  |  |
| minvalue | 最小输入值 | DecimalField | t_hsbs_bizitemprop.fminvalue |  |  |
| maxvalue | 最大输入值 | DecimalField | t_hsbs_bizitemprop.fmaxvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_bizitemprop（主表） | 27 |
| t_hsbs_bizitemprop_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
