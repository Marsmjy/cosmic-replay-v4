---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_persumconfig — 期间汇总组合

**表单编码**: `wtp_persumconfig`  
**表单ID**: `2IGE7PWBXC3Q`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_persumconfig（期间汇总组合） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_persumconfig` | 主表 · 26 列 |
| `t_wtp_persumconfentry` | 分录表 · 6 列 |
| `t_wtp_persumconfig_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_persumconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_persumconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_persumconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_persumconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_persumconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_persumconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_persumconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_persumconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_persumconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_persumconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_persumconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_persumconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_persumconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_persumconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_persumconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_persumconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_persumconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_persumconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_persumconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_persumconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_persumconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_persumconfig.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_persumconfig.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_persumconfig_l.foriname |  |  |

## 实体: entryentity（期间汇总配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| persumsource | 期间汇总考勤项目 | BasedataField | t_wtp_persumconfentry.fpersumsource | ✓ | wtp_persumsource |
| attitemmethod | 考勤项目处理方式 | BasedataPropField | — |  |  |
| display | 默认显示 | CheckBoxField | t_wtp_persumconfentry.fdisplay |  |  |
| adjustment | 允许员工调整布局 | CheckBoxField | t_wtp_persumconfentry.fadjustment |  |  |
| attitemname | 考勤项目 | TextField | — |  |  |
| terminal | 适用终端 | ComboField | t_wtp_persumconfentry.fterminal | ✓ |  |
| aconfirm | 员工确认 | CheckBoxField | t_wtp_persumconfentry.faconfirm |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_persumconfig（主表） | 20 |
| t_wtp_persumconfentry | 5 |
| t_wtp_persumconfig_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
