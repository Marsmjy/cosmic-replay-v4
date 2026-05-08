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

# wtp_attendrule — 出勤规则

**表单编码**: `wtp_attendrule`  
**表单ID**: `2HYP=53MEK6X`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attendrule（出勤规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_attendrule` | 主表 · 37 列 |
| `t_wtp_attendruleentry` | 分录表 · 6 列 |
| `t_wtp_attendrule_l` | 多语言表 · 3 列 |
| `t_wtp_attendruleentry_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_attendrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_attendrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_attendrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_attendrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_attendrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_attendrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_attendrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_attendrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_attendrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_attendrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_attendrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_attendrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_attendrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_attendrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_attendrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_attendrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_attendrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_attendrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_attendrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_attendrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_attendrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_attendrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_attendrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_attendrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_attendrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_attendrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_attendrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_attendrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_attendrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_attendrule.fhisversion |  |  |

## 实体: attendruleentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboid | 分录BOID | BigIntField | t_wtp_attendruleentry.fentryboid |  |  |
| daterangedesc | 计算日期范围 | MuliLangTextField | t_wtp_attendruleentry_l.fdaterangedesc |  |  |
| limitscopedesc | 限定条件 | MuliLangTextField | t_wtp_attendruleentry_l.flimitscopedesc |  |  |
| attcustimes | 时长配置 | MulBasedataField | — |  |  |
| daterangecondition | 日期范围 | TextAreaField | t_wtp_attendruleentry.fdaterangecondition |  |  |
| limitscopecondition | 限定条件 | TextAreaField | t_wtp_attendruleentry.flimitscopecondition |  |  |
| calculatename | 名称 | MuliLangTextField | t_wtp_attendruleentry_l.fcalculatename |  |  |
| attendconfigrule | 应出勤配置 | BasedataField | — | ✓ | wtp_attendconfig |
| attendancetimerule | 应出勤时长 | BasedataField | — | ✓ | wtbd_attitem |
| attendancedayrule | 应出勤天数 | BasedataField | — | ✓ | wtbd_attitem |
| originalattitemrule | 原始出勤时长 | BasedataField | — |  | wtbd_attitem |
| actualattitemrule | 实际出勤时长 | BasedataField | — |  | wtbd_attitem |
| ismultiallow | 允许命中多个时长配置 | CheckBoxField | t_wtp_attendrule.fismultiallow |  |  |
| iscontainovertime | 班内加班时段计入应出勤 | CheckBoxField | t_wtp_attendrule.fiscontainovertime |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_attendrule（主表） | 29 |
| t_wtp_attendrule_l | 3 |
| t_wtp_attendruleentry | 3 |
| t_wtp_attendruleentry_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
