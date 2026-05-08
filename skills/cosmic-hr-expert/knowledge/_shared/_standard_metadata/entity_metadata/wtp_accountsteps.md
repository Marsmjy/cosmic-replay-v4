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

# wtp_accountsteps — 核算步骤

**表单编码**: `wtp_accountsteps`  
**表单ID**: `1TRUCZ39LLE2`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_accountsteps（核算步骤） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_accountsteps` | 主表 · 38 列 |
| `t_wtp_accountsteps_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_accountsteps.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_accountsteps_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_accountsteps.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_accountsteps.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_accountsteps.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_accountsteps.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_accountsteps.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_accountsteps.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_accountsteps.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_accountsteps.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_accountsteps.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_accountsteps.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_accountsteps.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_accountsteps.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_accountsteps.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_accountsteps.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_accountsteps_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_accountsteps_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_accountsteps.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_accountsteps.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_accountsteps.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_accountsteps.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_accountsteps.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_accountsteps.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_accountsteps.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_accountsteps.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_accountsteps.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_accountsteps.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_accountsteps.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_accountsteps.fhisversion |  |  |
| resulttype | 结果类型 | ComboField | t_wtp_accountsteps.fresulttype | ✓ |  |
| filterrule | 筛选规则 | ComboField | t_wtp_accountsteps.ffilterrule |  |  |
| filtertrueclass | 筛选实现类 | BasedataField | t_wtp_accountsteps.ffiltertrueclass |  | wtbs_pluginregister |
| calimpclass | 计算实现类 | BasedataField | t_wtp_accountsteps.fcalimpclass | ✓ | wtbs_pluginregister |
| accounttype | 核算类型 | MulComboField | t_wtp_accountsteps.faccounttype | ✓ |  |
| calrule | 计算规则 | ComboField | t_wtp_accountsteps.fcalrule | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_accountsteps（主表） | 33 |
| t_wtp_accountsteps_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
