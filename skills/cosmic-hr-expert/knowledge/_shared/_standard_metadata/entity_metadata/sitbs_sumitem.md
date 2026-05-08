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

# sitbs_sumitem — 合计项目

**表单编码**: `sitbs_sumitem`  
**表单ID**: `3F9O3EKOC6X4`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_sumitem（合计项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_sumitem` | 主表 · 35 列 |
| `t_sitbs_sumitementry` | 分录表 · 4 列 |
| `t_sitbs_sumitem_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_sumitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_sumitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_sumitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_sumitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_sumitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_sumitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_sumitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_sumitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_sumitem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_sumitem.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_sumitem.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_sumitem.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_sumitem.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_sumitem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_sumitem.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_sitbs_sumitem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_sumitem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_sumitem_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_sumitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_sumitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_sumitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_sumitem.fboid |  |  |
| iscurrentversion | 是否当前版本 | CheckBoxField | t_sitbs_sumitem.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_sumitem.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_sitbs_sumitem.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_sumitem.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_sumitem.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_sumitem.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_sumitem.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_sumitem.fhisversion |  |  |

## 实体: entryentity（合计成员） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| insuranceitem | 险种项目名称 | BasedataField | t_sitbs_sumitementry.finsuranceitemid |  | sitbs_insuranceitem |
| insurance | 险种 | BasedataPropField | t_sitbs_sumitementry.finsurance |  |  |
| insurancetype | 险种属性 | BasedataPropField | t_sitbs_sumitementry.finsurancetype |  |  |
| entryboid | 分录业务id | BigIntField | t_sitbs_sumitementry.fentryboid |  |  |
| generalenname | 通用英文名 | TextField | t_sitbs_sumitem.fgeneralenname |  |  |
| areatype | 国家/地区类型 | ComboField | t_sitbs_sumitem.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_sumitem.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_sumitem（主表） | 30 |
| t_sitbs_sumitem_l | 3 |
| t_sitbs_sumitementry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
