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

# hsas_callistview — 核算名单显示方案

**表单编码**: `hsas_callistview`  
**表单ID**: `2+6SF5YF+/93`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_callistview（核算名单显示方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_callistview` | 主表 · 34 列 |
| `t_hsas_callistviewentry` | 分录表 · 12 列 |
| `t_hsas_callistview_l` | 多语言表 · 3 列 |
| `t_hsas_callistviewentry_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_callistview.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_callistview_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_callistview.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_callistview.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_callistview.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_callistview.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_callistview.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_callistview.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_callistview.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_callistview.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_callistview.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_callistview.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_callistview.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_callistview.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_callistview.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_callistview.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_callistview_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_callistview_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_callistview.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_callistview.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_callistview.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_callistview.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_callistview.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_callistview.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_callistview.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_callistview.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_callistview.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_callistview.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_callistview.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_callistview.fhisversion |  |  |

## 实体: columnentryentity（字段选择） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isfreezecolumn | 冻结列 | CheckBoxField | t_hsas_callistviewentry.fisfreezecolumn |  |  |
| color | 字体显示颜色 | TextField | — |  |  |
| fieldkey | 名称标识 | TextField | t_hsas_callistviewentry.ffieldkey |  |  |
| calcolor | 字体显示颜色 | BasedataField | t_hsas_callistviewentry.fcalcolorid |  | hsas_calcolor |
| sort | 排序 | ComboField | t_hsas_callistviewentry.fsort |  |  |
| sortpriority | 排序优先级 | ComboField | t_hsas_callistviewentry.fsortpriority |  |  |
| isdisplay | 显示 | CheckBoxField | t_hsas_callistviewentry.fisdisplay |  |  |
| sourcekey | 来源标识 | TextField | t_hsas_callistviewentry.fsourcekey |  |  |
| fieldname | 字段名称 | MuliLangTextField | t_hsas_callistviewentry_l.ffieldname |  |  |
| source | 字段来源 | MuliLangTextField | t_hsas_callistviewentry_l.fsource |  |  |
| field | 名称字段映射 | TextField | t_hsas_callistviewentry.ffield |  |  |
| entryboid | 分录BOID | BigIntField | t_hsas_callistviewentry.fentryboid |  |  |
| displayname | 显示名称 | MuliLangTextField | t_hsas_callistviewentry_l.fdisplayname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_callistview.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_callistview.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_callistview（主表） | 29 |
| t_hsas_callistview_l | 3 |
| t_hsas_callistviewentry | 9 |
| t_hsas_callistviewentry_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
