---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_shiftmode — 轮班规则

**表单编码**: `wtbd_shiftmode`  
**表单ID**: `191Y3YTYFD2C`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_shiftmode（轮班规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_shiftmode` | 主表 · 31 列 |
| `t_wtbd_shiftmodeentry` | 分录表 · 4 列 |
| `t_wtbd_smholhandleentry` | 分录表 · 2 列 |
| `t_wtbd_shiftmode_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_shiftmode.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_shiftmode_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_shiftmode.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_shiftmode.fcreatorid |  | bos_user |
| modifier | 最近修改人 | ModifierField | t_wtbd_shiftmode.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_shiftmode.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_shiftmode.fcreatetime |  |  |
| modifytime | 最近修改时间 | ModifyDateField | t_wtbd_shiftmode.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_shiftmode.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_shiftmode.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_shiftmode.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_shiftmode.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_shiftmode.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_shiftmode.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_shiftmode.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_shiftmode.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_shiftmode_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_shiftmode_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_shiftmode.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_shiftmode.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_shiftmode.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_shiftmode.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_shiftmode.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_shiftmode_l.foriname |  |  |
| cyclemode | 循环模式 | RadioGroupField | t_wtbd_shiftmode.fcyclemode | ✓ |  |
| weekradio | 按周: | RadioField | — |  |  |
| dayradio | 按天: | RadioField | — |  |  |
| day | 循环天数 | IntegerField | t_wtbd_shiftmode.fday |  |  |

## 实体: wtbd_shiftmodeentry（轮班模式） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| shiftid | 班次 | BigIntField | t_wtbd_shiftmodeentry.fshiftid |  |  |
| weekday | 周数 | IntegerField | t_wtbd_shiftmodeentry.fweekday |  |  |
| nweek | 所属周次 | IntegerField | t_wtbd_shiftmodeentry.fnweek |  |  |
| nday | 天次 | IntegerField | t_wtbd_shiftmodeentry.fnday |  |  |
| week | 循环周数 | IntegerField | t_wtbd_shiftmode.fweek |  |  |
| datetypeadj | 日类型规则 | BasedataField | t_wtbd_shiftmode.fdatetypeadjid |  | wtbd_datetypeadj |
| shiftmodechange | 轮班模式 | TextField | — |  |  |
| holdatetypes0 | 日期类型 | MulBasedataField | — | ✓ |  |
| holhandlemethod0 | 处理方式 | ComboField | — | ✓ |  |
| assignshift0 | 指定OFF班次 | BasedataField | — | ✓ | wtbd_shift |

## 实体: holhandleentry（节假日处理方式） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| holdatetypes | 节假日处理日期类型 | MulBasedataField | — |  |  |
| holhandlemethod | 节假日处理方式 | ComboField | t_wtbd_smholhandleentry.fholhandlemethod | ✓ |  |
| assignshift | 指定OFF班 | BasedataField | t_wtbd_smholhandleentry.fassignshiftid |  | wtbd_shift |
| handleholiday | 节假日处理 | CheckBoxField | t_wtbd_shiftmode.fhandleholiday |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_shiftmode（主表） | 25 |
| t_wtbd_shiftmode_l | 4 |
| t_wtbd_shiftmodeentry | 4 |
| t_wtbd_smholhandleentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
