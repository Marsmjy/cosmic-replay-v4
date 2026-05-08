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

# wtp_timecut — 工时切分配置

**表单编码**: `wtp_timecut`  
**表单ID**: `3U8B/E+63TPE`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_timecut（工时切分配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_timecut` | 主表 · 22 列 |
| `t_wtp_timecutentry` | 分录表 · 8 列 |
| `t_wtp_timecut_l` | 多语言表 · 2 列 |
| `t_wtp_timecutentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_timecut.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_timecut_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_timecut.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_timecut.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_timecut.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_timecut.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_timecut.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_timecut.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_timecut.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_timecut.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_timecut.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_timecut.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_timecut.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_timecut.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_timecut.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_timecut.findex |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_wtp_timecut_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_timecut.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_timecut.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_timecut.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: timecutentry（工时切分配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| configname | 名称 | MuliLangTextField | t_wtp_timecutentry_l.fconfigname | ✓ |  |
| iscutattitem | 单独计算班次内出勤时长 | CheckBoxField | t_wtp_timecutentry.fiscutattitem |  |  |
| cutattitem | 班次内时长 | BasedataField | t_wtp_timecutentry.fcutattitem |  | wtbd_attitem |
| configdes | 描述 | MuliLangTextField | t_wtp_timecutentry_l.fconfigdes |  |  |
| datetypetime | 日期类型时段 | BasedataField | t_wtp_timecutentry.fdatetypetime | ✓ | wtbd_datetypeper |
| conditionstr | 条件 | TextField | — |  |  |
| beforedaycond | 前一日条件 | TextField | t_wtp_timecutentry.fbeforedaycond |  |  |
| daycond | 当日条件 | TextField | t_wtp_timecutentry.fdaycond |  |  |
| afterdaycond | 后一日条件 | TextField | t_wtp_timecutentry.fafterdaycond |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_timecut（主表） | 18 |
| t_wtp_timecut_l | 2 |
| t_wtp_timecutentry | 6 |
| t_wtp_timecutentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
