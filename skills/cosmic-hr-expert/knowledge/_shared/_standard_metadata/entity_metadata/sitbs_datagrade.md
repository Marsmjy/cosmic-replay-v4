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

# sitbs_datagrade — 个税税率表

**表单编码**: `sitbs_datagrade`  
**表单ID**: `29RBEJU5CSD4`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_datagrade（个税税率表） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_datagrade` | 主表 · 39 列 |
| `t_sitbs_datagrade_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_datagrade.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_datagrade_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_datagrade.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_datagrade.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_datagrade.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_datagrade.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_datagrade.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_datagrade.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_datagrade.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_datagrade.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_datagrade.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_datagrade.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_datagrade.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_datagrade.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_datagrade.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_sitbs_datagrade.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_datagrade_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_datagrade_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_datagrade.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_datagrade.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_datagrade.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_datagrade.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_sitbs_datagrade.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_datagrade.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_sitbs_datagrade.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_datagrade.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_datagrade.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_datagrade.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_datagrade.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_datagrade.fhisversion |  |  |
| matchingmethod | 匹配方法 | ComboField | t_sitbs_datagrade.fmatchingmethod |  |  |
| scalerule | 数据精度尾差处理 | ComboField | t_sitbs_datagrade.fscalerule |  |  |
| datagradeparam | 数据分级表 | TextField | t_sitbs_datagrade.fdatagradeparam |  |  |
| businessfield | 业务领域 | BasedataField | t_sitbs_datagrade.fbusinessfield |  | hbss_hrbusinessfield |
| uniquecode | 唯一编码 | TextField | t_sitbs_datagrade.funiquecode |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_datagrade.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_datagrade.fcountryid |  | bd_country |
| taxcategories | 个税种类 | MulBasedataField | — | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_datagrade（主表） | 34 |
| t_sitbs_datagrade_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
