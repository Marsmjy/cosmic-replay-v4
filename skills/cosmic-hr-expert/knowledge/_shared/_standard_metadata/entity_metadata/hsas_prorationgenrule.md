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

# hsas_prorationgenrule — 分段事件生成规则

**表单编码**: `hsas_prorationgenrule`  
**表单ID**: `34KZUP3871UK`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_prorationgenrule（分段事件生成规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_prorationgenrule` | 主表 · 34 列 |
| `t_hsas_prorationgenrent` | 分录表 · 3 列 |
| `t_hsas_prorationgenrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_prorationgenrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_prorationgenrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_prorationgenrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_prorationgenrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_prorationgenrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_prorationgenrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_prorationgenrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_prorationgenrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_prorationgenrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_prorationgenrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_prorationgenrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_prorationgenrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_prorationgenrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_prorationgenrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_prorationgenrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_prorationgenrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_prorationgenrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_prorationgenrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_prorationgenrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_prorationgenrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_prorationgenrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_prorationgenrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_prorationgenrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_prorationgenrule.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_prorationgenrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_prorationgenrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_prorationgenrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_prorationgenrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_prorationgenrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_prorationgenrule.fhisversion |  |  |
| generalenname | 通用英文名 | TextField | t_hsas_prorationgenrule.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsas_prorationgenrule.fcountryid | ✓ | bd_country |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| changereason | 变动原因 | BasedataField | t_hsas_prorationgenrent.fchangereasonid | ✓ | hsbs_changereason |
| prorationtype | 分段类型 | ComboField | t_hsas_prorationgenrent.fprorationtype | ✓ |  |
| prorationitem | 分段项目 | MulBasedataField | — | ✓ |  |
| entryboid | 分录BOID | BigIntField | t_hsas_prorationgenrent.fentryboid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_prorationgenrule（主表） | 29 |
| t_hsas_prorationgenrent | 3 |
| t_hsas_prorationgenrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
