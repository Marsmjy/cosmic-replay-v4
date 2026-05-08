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

# hsas_retrocfg — 薪资回溯结转配置

**表单编码**: `hsas_retrocfg`  
**表单ID**: `48LWM5H53=4C`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_retrocfg（薪资回溯结转配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_retrocfg` | 主表 · 25 列 |
| `t_hsas_retrocfgent` | 分录表 · 3 列 |
| `t_hsas_retrocfg_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_retrocfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_retrocfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_retrocfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_retrocfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_retrocfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_retrocfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_retrocfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_retrocfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_retrocfg.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_retrocfg.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_retrocfg.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_retrocfg.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_retrocfg.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_retrocfg.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_retrocfg.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_retrocfg.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_retrocfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_retrocfg_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_retrocfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_retrocfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_retrocfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_retrocfg.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_retrocfg.fcountryid |  | bd_country |

## 实体: itementry（回溯差值计算范围及结转配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitem | 薪酬项目 | BasedataField | t_hsas_retrocfgent.fsalaryitemid | ✓ | hsbs_salaryitem |
| targettype | 结转类型 | ComboField | t_hsas_retrocfgent.ftargettype | ✓ |  |
| targetitem | 目标薪酬项目 | BasedataField | t_hsas_retrocfgent.ftargetitemid | ✓ | hsbs_salaryitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_retrocfg（主表） | 20 |
| t_hsas_retrocfg_l | 3 |
| t_hsas_retrocfgent | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
