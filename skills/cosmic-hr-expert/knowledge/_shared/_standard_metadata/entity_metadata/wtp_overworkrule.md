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

# wtp_overworkrule — 加班规则

**表单编码**: `wtp_overworkrule`  
**表单ID**: `2DD+2P0FNT6R`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_overworkrule（加班规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_overworkrule` | 主表 · 32 列 |
| `t_wtp_overworkruleentry` | 分录表 · 11 列 |
| `t_wtp_overworkcaldetail` | 分录表 · 9 列 |
| `t_wtp_overworkrule_l` | 多语言表 · 3 列 |
| `t_wtp_overworkruleentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_overworkrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_overworkrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_overworkrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_overworkrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_overworkrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_overworkrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_overworkrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_overworkrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_overworkrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_overworkrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_overworkrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_overworkrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_overworkrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_overworkrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_overworkrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_overworkrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_overworkrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_overworkrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_overworkrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_overworkrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_overworkrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_overworkrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_overworkrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_overworkrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_overworkrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_overworkrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_overworkrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_overworkrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_overworkrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_overworkrule.fhisversion |  |  |

## 实体: overworkcalentry（加班时长计算规则） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| calculname | 计算日期范围 | MuliLangTextField | t_wtp_overworkruleentry_l.fcalculname | ✓ |  |
| calculdes | 限定条件 | MuliLangTextField | t_wtp_overworkruleentry_l.fcalculdes |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_overworkcaldetail.fentryboid |  |  |
| ottype | 加班类型 | BasedataField | t_wtp_overworkruleentry.fottypeid | ✓ | wtbd_ottype |
| otbaseset | 加班基础配置 | BasedataField | t_wtp_overworkruleentry.fotbasesetid | ✓ | wtp_otbaseset |
| otsystem | 加班制度 | BasedataField | t_wtp_overworkruleentry.fotsystemid |  | wtbd_attpolicy |
| datasource | 加班时数来源 | BasedataField | t_wtp_overworkruleentry.fdatasourceid | ✓ | wtbd_ottimesource |
| limitscope | 限定范围JSON | TextAreaField | t_wtp_overworkruleentry.flimitscope |  |  |
| daterangecondition | 日期范围JSON | TextAreaField | t_wtp_overworkruleentry.fdaterangecondition |  |  |
| isempchoice | 是否员工自选 | CheckBoxField | t_wtp_overworkruleentry.fisempchoice |  |  |
| otchangeset | 加班变更配置 | BasedataField | t_wtp_overworkruleentry.fotchangesetid |  | wtp_otchangeset |
| otcompenmode | 补偿方式 | BasedataField | t_wtp_overworkcaldetail.fotcompenmodeid |  | wtbd_otcompenmode |
| originalprj | 原始时长 | BasedataField | t_wtp_overworkcaldetail.foriginalprj | ✓ | wtbd_attitem |
| originalcalprj | 起算时长 | BasedataField | t_wtp_overworkcaldetail.foriginalcalprj | ✓ | wtbd_attitem |
| overtimesub | 扣减配置 | BasedataField | t_wtp_overworkcaldetail.fovertimesub |  | wtp_otsub |
| subprj | 扣减时长 | BasedataField | t_wtp_overworkcaldetail.fsubprj | ✓ | wtbd_attitem |
| bfroundingrule | 舍入规则 | BasedataField | t_wtp_overworkcaldetail.fbfroundingrule |  | wtbd_roundrule |
| resultprj | 结果时长 | BasedataField | t_wtp_overworkcaldetail.fresultprj | ✓ | wtbd_attitem |
| entryboid1 | 子分录BOID | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_overworkrule（主表） | 27 |
| t_wtp_overworkcaldetail | 8 |
| t_wtp_overworkrule_l | 3 |
| t_wtp_overworkruleentry | 8 |
| t_wtp_overworkruleentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
