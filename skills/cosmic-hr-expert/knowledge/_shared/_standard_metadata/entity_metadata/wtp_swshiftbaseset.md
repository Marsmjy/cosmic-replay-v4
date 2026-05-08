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

# wtp_swshiftbaseset — 调班基础配置

**表单编码**: `wtp_swshiftbaseset`  
**表单ID**: `3ZJIZA4FO2+X`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_swshiftbaseset（调班基础配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_swshiftbaseset` | 主表 · 49 列 |
| `t_wtp_swshiftbaseset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_swshiftbaseset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_swshiftbaseset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_swshiftbaseset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_swshiftbaseset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_swshiftbaseset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_swshiftbaseset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_swshiftbaseset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_swshiftbaseset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_swshiftbaseset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_swshiftbaseset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_swshiftbaseset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_swshiftbaseset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_swshiftbaseset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_swshiftbaseset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_swshiftbaseset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_swshiftbaseset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_swshiftbaseset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_swshiftbaseset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_swshiftbaseset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_swshiftbaseset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_swshiftbaseset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_swshiftbaseset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_swshiftbaseset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_swshiftbaseset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_swshiftbaseset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_swshiftbaseset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_swshiftbaseset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_swshiftbaseset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_swshiftbaseset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_swshiftbaseset.fhisversion |  |  |
| isrepair | 补提限制 | CheckBoxField | t_wtp_swshiftbaseset.fisrepair |  |  |
| maxrepairtime | 补提限制-值 | IntegerField | t_wtp_swshiftbaseset.fmaxrepairtime |  |  |
| repairperiod | 可补提周期 | ComboField | t_wtp_swshiftbaseset.frepairperiod | ✓ |  |
| isaheadmax | 最晚预提限制 | CheckBoxField | t_wtp_swshiftbaseset.fisaheadmax |  |  |
| aheadmaxval | 最晚预提限制-值 | IntegerField | t_wtp_swshiftbaseset.faheadmaxval |  |  |
| aheadmaxunit | 可提前周期 | ComboField | t_wtp_swshiftbaseset.faheadmaxunit | ✓ |  |
| isahead | 最早预提限制 | CheckBoxField | t_wtp_swshiftbaseset.fisahead |  |  |
| maxaheadtime | 最早预提限制-值 | IntegerField | t_wtp_swshiftbaseset.fmaxaheadtime |  |  |
| aheadperiod | 需提前周期 | ComboField | t_wtp_swshiftbaseset.faheadperiod | ✓ |  |
| reasonmust | 调班原因必填 | CheckBoxField | t_wtp_swshiftbaseset.freasonmust |  |  |
| attachmentmust | 附件必填 | CheckBoxField | t_wtp_swshiftbaseset.fattachmentmust |  |  |
| allowdatetypeadj | 允许调整日期类型 | CheckBoxField | t_wtp_swshiftbaseset.fallowdatetypeadj |  |  |
| allowplan | 计划排班 | CheckBoxField | t_wtp_swshiftbaseset.fallowplan |  |  |
| allowactual | 实际排班 | CheckBoxField | t_wtp_swshiftbaseset.fallowactual |  |  |
| modifyroster | 修改排班 | CheckBoxField | t_wtp_swshiftbaseset.fmodifyroster |  |  |
| otherexchange | 与他人对调 | CheckBoxField | t_wtp_swshiftbaseset.fotherexchange |  |  |
| selfexchange | 本人对调 | CheckBoxField | t_wtp_swshiftbaseset.fselfexchange |  |  |
| maxaheadtime_s | 期限 | IntegerField | — |  |  |
| aheadmaxval_s | 期限 | IntegerField | — |  |  |
| maxrepairtime_s | 期限 | IntegerField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_swshiftbaseset（主表） | 44 |
| t_wtp_swshiftbaseset_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
