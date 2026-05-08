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

# wtp_countset — 次数限制配置

**表单编码**: `wtp_countset`  
**表单ID**: `20F15Y2EIY6B`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_countset（次数限制配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_countset` | 主表 · 33 列 |
| `t_wtp_countsetentry` | 分录表 · 7 列 |
| `t_wtp_countset_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_countset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_countset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_countset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_countset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_countset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_countset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_countset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_countset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_countset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_countset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_countset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_countset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_countset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_countset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_countset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_countset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_countset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_countset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_countset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_countset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_countset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_countset.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_countset.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_countset_l.foriname |  |  |
| limittype | 限制方式 | RadioGroupField | t_wtp_countset.flimittype | ✓ |  |
| bycard | 按补签卡 | RadioField | — |  |  |
| byreason | 按补签原因 | RadioField | — |  |  |
| count | 可补签次数 | IntegerField | t_wtp_countset.fcount |  |  |
| attitem | 次数 | BasedataField | t_wtp_countset.fattitemid |  | wtbd_attitem |
| abovecount | 可超额次数 | IntegerField | t_wtp_countset.fabovecount |  |  |
| aboveattitem | 超额次数 | BasedataField | t_wtp_countset.faboveattitemid |  | wtbd_attitem |
| countsco | 时间范围 | ComboField | t_wtp_countset.fcountsco |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| condition | 条件 | TextField | — |  |  |
| result | 结果 | TextField | — |  |  |
| reason | 补签原因 | BasedataField | — |  | wtbd_reason |
| enticountsco | 时间范围 | ComboField | t_wtp_countsetentry.fenticountsco |  |  |
| enticount | 可补签次数 | IntegerField | t_wtp_countsetentry.fenticount |  |  |
| entiattitem | 次数 | BasedataField | t_wtp_countsetentry.fentiattitemid |  | wtbd_attitem |
| entiabovecount | 可超额次数 | IntegerField | t_wtp_countsetentry.fentiabovecount |  |  |
| entiaboveattitem | 超额次数 | BasedataField | t_wtp_countsetentry.fentiaboveattitemid |  | wtbd_attitem |
| entiabovecountbak | 可超额次数 | IntegerField | — |  |  |
| enticountbak | 可补签次数 | IntegerField | — |  |  |
| entiallowabove | 允许超额 | ComboField | t_wtp_countsetentry.fentiallowabove |  |  |
| countbak | 可补签次数 | IntegerField | — |  |  |
| abovecountbak | 可超额次数 | IntegerField | — |  |  |
| allowabove | 允许超额 | ComboField | t_wtp_countset.fallowabove |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_countset（主表） | 27 |
| t_wtp_countset_l | 4 |
| t_wtp_countsetentry | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
