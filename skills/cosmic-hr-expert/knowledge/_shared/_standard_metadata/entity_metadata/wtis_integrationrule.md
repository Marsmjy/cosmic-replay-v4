---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2MKKI+46MWNZ
app_number: wtis
app_name: 工时假勤集成服务
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtis_integrationrule — 考勤数据推送规则

**表单编码**: `wtis_integrationrule`  
**表单ID**: `2MN0IRB92QNJ`  
**归属**: 工时假勤云 / 工时假勤集成服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtis_integrationrule（考勤数据推送规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtis_integrationrule` | 主表 · 32 列 |
| `t_wtis_attitem_resg` | 分录表 · 5 列 |
| `t_wtis_integrationrule_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtis_integrationrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtis_integrationrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtis_integrationrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtis_integrationrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtis_integrationrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtis_integrationrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtis_integrationrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtis_integrationrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtis_integrationrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtis_integrationrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtis_integrationrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtis_integrationrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtis_integrationrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtis_integrationrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtis_integrationrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtis_integrationrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtis_integrationrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtis_integrationrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtis_integrationrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtis_integrationrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtis_integrationrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtis_integrationrule.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtis_integrationrule.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtis_integrationrule_l.foriname |  |  |
| businessareaid | 业务领域 | BasedataField | t_wtis_integrationrule.fbusinessareaid |  | wtis_businessarea |
| checkbatch | 同一考勤期间分段推送 | CheckBoxField | — |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitemld | 编码 | BasedataField | t_wtis_attitem_resg.fattitemld |  | wtbd_attitem |
| attitemname | 名称 | BasedataPropField | t_wtis_attitem_resg.fattitemname |  |  |
| attitemtype | 数据类型 | BasedataPropField | t_wtis_attitem_resg.fattitemtype |  |  |
| attitemunit | 单位 | BasedataPropField | t_wtis_attitem_resg.fattitemunit |  |  |
| attitemcy | 显示精度 | BasedataPropField | t_wtis_attitem_resg.fattitemcy |  |  |
| itemtype | 项目类型 | BasedataPropField | — |  |  |
| specialset | 特殊设置 | MulComboField | t_wtis_integrationrule.fspecialset |  |  |
| attitem | 考勤项目 | BasedataField | — |  | wtbd_attitem |
| datasource | 数据来源 | ComboField | t_wtis_integrationrule.fdatasource | ✓ |  |
| issumcomp | 汇总计算 | CheckBoxField | t_wtis_integrationrule.fissumcomp |  |  |
| issendqtdata | 推送定额数据 | CheckBoxField | t_wtis_integrationrule.fissendqtdata |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtis_integrationrule（主表） | 25 |
| t_wtis_attitem_resg | 5 |
| t_wtis_integrationrule_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
