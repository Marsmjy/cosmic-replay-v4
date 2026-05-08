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

# wtp_otbaseset — 加班基础配置

**表单编码**: `wtp_otbaseset`  
**表单ID**: `2DD0X1LISM8R`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_otbaseset（加班基础配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_otbaseset` | 主表 · 49 列 |
| `t_wtp_otbaseset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_otbaseset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_otbaseset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_otbaseset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_otbaseset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_otbaseset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_otbaseset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_otbaseset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_otbaseset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_otbaseset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_otbaseset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_otbaseset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_otbaseset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_otbaseset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_otbaseset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_otbaseset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_otbaseset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_otbaseset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_otbaseset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_otbaseset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_otbaseset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_otbaseset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_otbaseset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_otbaseset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_otbaseset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_otbaseset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_otbaseset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_otbaseset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_otbaseset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_otbaseset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_otbaseset.fhisversion |  |  |
| calunit | 计量单位 | ComboField | t_wtp_otbaseset.fcalunit | ✓ |  |
| minottime | 最小加班时长 | IntegerField | t_wtp_otbaseset.fminottime |  |  |
| isadvancecrl | 最早预提限制 | CheckBoxField | t_wtp_otbaseset.fisadvancecrl |  |  |
| advanceunit | 需提前周期 | ComboField | t_wtp_otbaseset.fadvanceunit |  |  |
| advancenum | 期限 | IntegerField | t_wtp_otbaseset.fadvancenum |  |  |
| isaftercrl | 补提限制 | CheckBoxField | t_wtp_otbaseset.fisaftercrl |  |  |
| afterunit | 可补提周期 | ComboField | t_wtp_otbaseset.fafterunit |  |  |
| afternum | 期限 | IntegerField | t_wtp_otbaseset.fafternum |  |  |
| isshowattach | 附件必填 | CheckBoxField | t_wtp_otbaseset.fisshowattach |  |  |
| isresoninput | 加班原因必填 | CheckBoxField | t_wtp_otbaseset.fisresoninput |  |  |
| isflxot | 允许弹性班班后加班 | CheckBoxField | t_wtp_otbaseset.fisflxot |  |  |
| ismaxadvancecrl | 最晚预提限制 | CheckBoxField | t_wtp_otbaseset.fismaxadvancecrl |  |  |
| maxadvanceunit | 可提前周期 | ComboField | t_wtp_otbaseset.fmaxadvanceunit |  |  |
| maxadvancenum | 期限 | IntegerField | t_wtp_otbaseset.fmaxadvancenum |  |  |
| daterangecondition | 日期范围 | TextAreaField | t_wtp_otbaseset.fdaterangecondition |  |  |
| ruledate | 规则控件日期 | DateField | — |  |  |
| applyspan | 单位时长 | ComboField | t_wtp_otbaseset.fapplyspan |  |  |
| isapplyspan | 控制单位时长 | CheckBoxField | t_wtp_otbaseset.fisapplyspan |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_otbaseset（主表） | 44 |
| t_wtp_otbaseset_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
