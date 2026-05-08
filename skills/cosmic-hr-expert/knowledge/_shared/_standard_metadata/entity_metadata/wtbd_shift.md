---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_shift — 班次

**表单编码**: `wtbd_shift`  
**表单ID**: `188IBXA=97RU`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_shift（班次） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_shift` | 主表 · 59 列 |
| `t_wtbd_shiftentry` | 分录表 · 12 列 |
| `t_wtbd_shiftworkentry` | 分录表 · 5 列 |
| `t_wtbd_shiftrestentry` | 分录表 · 2 列 |
| `t_wtbd_shift_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_shift.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_shift_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_shift.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_shift.fcreatorid |  | bos_user |
| modifier | 最近修改人 | ModifierField | t_wtbd_shift.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_shift.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_shift.fcreatetime |  |  |
| modifytime | 最近修改时间 | ModifyDateField | t_wtbd_shift.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_shift.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_shift.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_shift.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_shift.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_shift.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_shift.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_shift.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_shift.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_shift_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_shift_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_shift.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_shift.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_shift.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_wtbd_shift.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_shift.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_shift.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_shift.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_shift.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_shift.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtbd_shift.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_shift.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_shift.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_shift.fhisversion |  |  |
| shifttype | 班次类型 | ComboField | t_wtbd_shift.fshifttype | ✓ |  |
| middlepoint | 班次中间分割点 | TimeField | t_wtbd_shift.fmiddlepoint | ✓ |  |

## 实体: entryentity（班次时段） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| outworktype | 出勤类别(业务辅助字段误删) | ComboField | t_wtbd_shiftentry.foutworktype |  |  |
| shiftstartdate | 开始时间 | TimeField | t_wtbd_shiftentry.fshiftstartdate |  |  |
| shiftenddate | 结束时间 | TimeField | t_wtbd_shiftentry.fshiftenddate |  |  |
| isworktimestart | 上班卡 | CheckBoxField | t_wtbd_shiftentry.fisworktimestart |  |  |
| isworktimeend | 下班卡 | CheckBoxField | t_wtbd_shiftentry.fisworktimeend |  |  |
| worktime | 时段时长(小时) | DecimalField | t_wtbd_shiftentry.fworktime |  |  |
| sindex | 序号 | IntegerField | t_wtbd_shiftentry.fsindex |  |  |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| shiftperiod | 班次时段 | BasedataField | t_wtbd_shiftentry.fshiftperiodid |  | wtbd_shiftperiod |
| punchcardpairid | 应打卡对 | BasedataField | t_wtbd_shiftentry.fpunchcardpairid |  | wtbd_punchcardpair |
| workcode | 班次代码 | TextField | t_wtbd_shift.fworkcode |  |  |
| allday | 班次天数 | DecimalField | t_wtbd_shift.fallday |  |  |
| halfday | 半天班次天数 | DecimalField | t_wtbd_shift.fhalfday |  |  |
| halfdayhour | 半天班次时数 | DecimalField | t_wtbd_shift.fhalfdayhour |  |  |

## 实体: workentry（核心时段） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| workstarttime | 核心开始时间 | TimeField | t_wtbd_shiftworkentry.fworkstarttime |  |  |
| workendtime | 核心结束时间 | TimeField | t_wtbd_shiftworkentry.fworkendtime |  |  |
| entryboid | 分录BOID | BigIntField | t_wtbd_shiftrestentry.fentryboid |  |  |

## 实体: restentry（休息时间） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| resttimeid | 休息时间 | BasedataField | t_wtbd_shiftrestentry.fresttimeid |  | wtbd_breaktime |
| reststarttime | 休息开始时间 | TimeField | — |  |  |
| restendtime | 休息结束时间 | TimeField | — |  |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| shiftstart | 最早上班时间 | TimeField | t_wtbd_shift.fshiftstart | ✓ |  |
| shiftend | 最晚下班时间 | TimeField | t_wtbd_shift.fshiftend | ✓ |  |
| effectivetime | 有效时长 | DecimalField | t_wtbd_shift.feffectivetime |  |  |
| isoff | 是否OFF班 | ComboField | t_wtbd_shift.fisoff | ✓ |  |
| offnonplan | OFF班无计划时段 | ComboField | t_wtbd_shift.foffnonplan | ✓ |  |
| alldayhour | 全天班次时数 | DecimalField | t_wtbd_shift.falldayhour | ✓ |  |
| workingsections | 取卡图示 | IntegerField | t_wtbd_shift.fworkingsections |  |  |
| cardsnum | 取卡图示 | IntegerField | t_wtbd_shift.fcardsnum |  |  |
| refmiddlepoint | 分割点参照日 | ComboField | t_wtbd_shift.frefmiddlepoint | ✓ |  |
| clockrule | 取卡规则 | BasedataField | t_wtbd_shift.fclockruleid | ✓ | wtbd_takecardrule |
| onecardtype | 一次卡取卡范围 | ComboField | t_wtbd_shift.fonecardtype |  |  |
| isinot | 班内加班 | CheckBoxField | t_wtbd_shift.fisinot |  |  |
| inotstart | 班内加班开始时间 | TimeField | t_wtbd_shift.finotstart | ✓ |  |
| inotend | 班内加班结束时间 | TimeField | t_wtbd_shift.finotend | ✓ |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_wtbd_shift.fsourcesyskey |  |  |
| shiftcolor | 班次颜色 | ColorPickerField | t_wtbd_shift.fshiftcolor | ✓ |  |
| shiftcolor_expt | 班次颜色_导出 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_shift（主表） | 50 |
| t_wtbd_shift_l | 3 |
| t_wtbd_shiftentry | 9 |
| t_wtbd_shiftrestentry | 2 |
| t_wtbd_shiftworkentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
