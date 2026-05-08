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

# wtp_suppletimeset — 时间限制配置

**表单编码**: `wtp_suppletimeset`  
**表单ID**: `20EIUCL+OA=N`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_suppletimeset（时间限制配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_sutimeset` | 主表 · 29 列 |
| `t_wtp_sutimeset_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_sutimeset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_sutimeset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_sutimeset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_sutimeset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_sutimeset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_sutimeset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_sutimeset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_sutimeset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_sutimeset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_sutimeset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_sutimeset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_sutimeset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_sutimeset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_sutimeset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_sutimeset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_sutimeset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_sutimeset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_sutimeset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_sutimeset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_sutimeset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_sutimeset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_sutimeset.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_sutimeset.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_sutimeset_l.foriname |  |  |
| timescope | 时效范围 | ComboField | t_wtp_sutimeset.ftimescope | ✓ |  |
| advdaysco | 可申请过去天数 | IntegerField | t_wtp_sutimeset.fadvdaysco |  |  |
| daytype | 天数类型 | ComboField | t_wtp_sutimeset.fdaytype |  |  |
| advdayscobak | 可申请过去天数 | IntegerField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_sutimeset（主表） | 23 |
| t_wtp_sutimeset_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
