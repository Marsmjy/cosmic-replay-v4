---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_salaryitem — 薪酬项目

**表单编码**: `hsbs_salaryitem`  
**表单ID**: `0/MC8E0/PCUW`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_salaryitem（薪酬项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_salaryitem` | 主表 · 47 列 |
| `t_hsbs_salaryitem_l` | 多语言表 · 6 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_salaryitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_salaryitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_salaryitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_salaryitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_salaryitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_salaryitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_salaryitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_salaryitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_salaryitem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_salaryitem.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_salaryitem.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_salaryitem.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_salaryitem.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_salaryitem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_salaryitem.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_salaryitem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_salaryitem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_salaryitem_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_salaryitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_salaryitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_salaryitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hsbs_salaryitem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hsbs_salaryitem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hsbs_salaryitem_l.foriname |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_salaryitem.fgeneralenname |  |  |
| datatype | 数据类型 | BasedataField | t_hsbs_salaryitem.fdatatypeid | ✓ | hsbs_datatype |
| datalength | 数据长度 | IntegerField | t_hsbs_salaryitem.fdatalength |  |  |
| dataprecision | 数据精度 | BasedataField | t_hsbs_salaryitem.fdataprecisionid |  | hsbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_hsbs_salaryitem.fdataroundid |  | hsbs_dataround |
| salaryitemtype | 薪酬项目系统分类 | BasedataField | t_hsbs_salaryitem.fsalaryitemtypeid | ✓ | hsbs_salaryitemtype |
| presetname | 预置名称 | MuliLangTextField | t_hsbs_salaryitem_l.fpresetname |  |  |
| sysinsname | 系统名称 | MuliLangTextField | t_hsbs_salaryitem_l.fsysinsname |  |  |
| uniquecode | 项目唯一标识 | TextField | t_hsbs_salaryitem.funiquecode |  |  |
| ispayoutitem | 实发项目 | ComboField | t_hsbs_salaryitem.fispayoutitem | ✓ |  |
| struinstagtxt | 薪酬构成标签(列表动态加载字段) | TextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_salaryitem.fareatype | ✓ |  |
| proratesummarytype | 分段汇总行取值 | ComboField | t_hsbs_salaryitem.fproratesummarytype |  |  |
| taxtag | 个税云服务标签 | ComboField | t_hsbs_salaryitem.ftaxtag |  |  |
| calblock | 计算区段 | ComboField | t_hsbs_salaryitem.fcalblock |  |  |
| iscostallot | 默认参与费用分摊 | CheckBoxField | t_hsbs_salaryitem.fiscostallot |  |  |
| isfixedcost | 固定成本 | CheckBoxField | t_hsbs_salaryitem.fisfixedcost |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_salaryitem.fcountryid |  | bd_country |
| statisticstag | 薪酬项目类别 | BasedataField | t_hsbs_salaryitem.fstatisticstagid |  | hsbs_statisticstag |
| outputcurrency | 输出结果币种 | BasedataField | t_hsbs_salaryitem.foutputcurrencyid |  | bd_currency |
| taxcategory | 个税种类 | BasedataField | t_hsbs_salaryitem.ftaxcategoryid |  | hsbs_taxcategory |
| isadditionaldecuction | 专项附加扣除及其他 | CheckBoxField | t_hsbs_salaryitem.fisadditionaldecuction |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_salaryitem（主表） | 39 |
| t_hsbs_salaryitem_l | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
