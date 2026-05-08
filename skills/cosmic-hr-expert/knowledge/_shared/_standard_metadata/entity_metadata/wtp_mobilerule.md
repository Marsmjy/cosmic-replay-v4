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

# wtp_mobilerule — 假勤自助规则

**表单编码**: `wtp_mobilerule`  
**表单ID**: `2IGDXU1GWHW5`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_mobilerule（假勤自助规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_mobileconf` | 主表 · 33 列 |
| `t_wtp_mobileconf_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_mobileconf.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_mobileconf_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_mobileconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_mobileconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_mobileconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_mobileconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_mobileconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_mobileconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_mobileconf.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_mobileconf.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_mobileconf.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_mobileconf.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_mobileconf.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_mobileconf.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_mobileconf.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_mobileconf.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_mobileconf_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_mobileconf_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_mobileconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_mobileconf.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_mobileconf.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_mobileconf.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_mobileconf.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_mobileconf_l.foriname |  |  |
| workspace | 作业空间 | ComboField | t_wtp_mobileconf.fworkspace | ✓ |  |
| persumconfig | 期间汇总组合 | BasedataField | t_wtp_mobileconf.fpersumconfigid | ✓ | wtp_persumconfig |
| dailydetconfig | 日明细组合 | BasedataField | t_wtp_mobileconf.fdailydetconfigid | ✓ | wtp_dailydetconfig |
| showproblem | 显示常见问题 | CheckBoxField | t_wtp_mobileconf.fshowproblem |  |  |
| attcommproblem | 问题 | MulBasedataField | — | ✓ |  |
| a | 休假 | CheckBoxField | — |  |  |
| b | 加班 | CheckBoxField | — |  |  |
| c | 调班 | CheckBoxField | — |  |  |
| d | 出差 | CheckBoxField | — |  |  |
| e | 补签 | CheckBoxField | — |  |  |
| selectedbill | 可选常用功能 | MulComboField | t_wtp_mobileconf.fselectedbill | ✓ |  |
| quotaconfig | 定额项目组合 | BasedataField | t_wtp_mobileconf.fquotaconfigid |  | wtp_quotaconfig |
| showboard | 显示仪表盘 | CheckBoxField | t_wtp_mobileconf.fshowboard |  |  |
| f | 为他人申请休假 | CheckBoxField | — |  |  |
| g | 为他人申请加班 | CheckBoxField | — |  |  |
| h | 为他人申请出差 | CheckBoxField | — |  |  |
| i | 为他人申请补签 | CheckBoxField | — |  |  |
| j | 为他人申请调班 | CheckBoxField | — |  |  |
| k | 我的申请 | CheckBoxField | — |  |  |
| m | 考勤确认 | CheckBoxField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_mobileconf（主表） | 27 |
| t_wtp_mobileconf_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 16 |
