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

# wtp_attperiod — 考勤周期

**表单编码**: `wtp_attperiod`  
**表单ID**: `1BMJEE0HZ49/`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attperiod（考勤周期） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_attperiod` | 主表 · 40 列 |
| `t_wtp_attperiodentry` | 分录表 · 11 列 |
| `t_wtp_attperiod_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_attperiod.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_attperiod_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_attperiod.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_attperiod.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_attperiod.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_attperiod.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_attperiod.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_attperiod.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_attperiod.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_attperiod.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_attperiod.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_attperiod.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_attperiod.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_attperiod.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_attperiod.findex |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_wtp_attperiod_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_attperiod.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_attperiod.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_attperiod.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_attperiod.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_attperiod.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_attperiod_l.foriname |  |  |
| nameprefix | 编码前缀 | TextField | t_wtp_attperiod.fnameprefix |  |  |
| namemain | 编码主体 | ComboField | t_wtp_attperiod.fnamemain | ✓ |  |
| namepostfix | 编码后缀 | TextField | t_wtp_attperiod.fnamepostfix |  |  |
| serialnumber | 流水号位数 | IntegerField | t_wtp_attperiod.fserialnumber |  |  |
| namedemo | 生成编码样例 | TextField | t_wtp_attperiod.fnamedemo |  |  |

## 实体: attperiodentry（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| periodname | 期间名称 | TextField | t_wtp_attperiodentry.fperiodname | ✓ |  |
| begindate | 开始日期 | DateField | t_wtp_attperiodentry.fbegindate | ✓ |  |
| enddate | 结束日期 | DateField | t_wtp_attperiodentry.fenddate | ✓ |  |
| mhsa | 日期归属规则 | BasedataField | t_wtp_attperiodentry.fmhsaid | ✓ | wtp_mhsascription |
| backnumber | 回溯期间 | IntegerField | t_wtp_attperiodentry.fbacknumber | ✓ |  |
| ascriptionyear | 所属年份 | TextField | t_wtp_attperiodentry.fascriptionyear | ✓ |  |
| quotestatus | 锁定状态 | ComboField | t_wtp_attperiodentry.fquotestatus |  |  |
| ascriptionmonth | 所属月份 | ComboField | t_wtp_attperiodentry.fascriptionmonth | ✓ |  |
| ascriptionyearview | 所属年份 | DateField | — |  |  |
| periodcode | 期间编码 | TextField | t_wtp_attperiodentry.fperiodcode | ✓ |  |
| totaldays | 总天数 | IntegerField | t_wtp_attperiodentry.ftotaldays |  |  |
| deadline | 截止时间 | DateTimeField | t_wtp_attperiodentry.fdeadline | ✓ |  |
| periodnumber | 期间数 | IntegerField | t_wtp_attperiod.fperiodnumber |  |  |
| looptype | 循环类型 | ComboField | t_wtp_attperiod.flooptype | ✓ |  |
| perioddate | 期间起始日 | ComboField | t_wtp_attperiod.fperioddate | ✓ |  |
| firstperioddate | 首期间起始日 | ComboField | t_wtp_attperiod.ffirstperioddate | ✓ |  |
| secondperioddate | 次期间起始日 | ComboField | t_wtp_attperiod.fsecondperioddate | ✓ |  |
| loopbegindate | 循环基准起始日期 | DateField | t_wtp_attperiod.floopbegindate | ✓ |  |
| perioddays | 期间天数 | IntegerField | t_wtp_attperiod.fperioddays | ✓ |  |
| defaultmhsa | 默认日期归属规则 | BasedataField | t_wtp_attperiod.fdefaultmhsaid | ✓ | wtp_mhsascription |
| isdeadline | 截止时间限制 | CheckBoxField | t_wtp_attperiod.fisdeadline |  |  |
| floatmethod | 浮动方式 | ComboField | t_wtp_attperiod.ffloatmethod | ✓ |  |
| floatvalue | 浮动数值 | IntegerField | t_wtp_attperiod.ffloatvalue | ✓ |  |
| floatunit | 单位 | ComboField | t_wtp_attperiod.ffloatunit | ✓ |  |
| floatduration | 浮动时长 | TimeField | t_wtp_attperiod.ffloatduration |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_attperiod（主表） | 37 |
| t_wtp_attperiod_l | 3 |
| t_wtp_attperiodentry | 11 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
