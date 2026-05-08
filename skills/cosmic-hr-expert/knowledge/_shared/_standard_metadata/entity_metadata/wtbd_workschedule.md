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

# wtbd_workschedule — 工作日程表

**表单编码**: `wtbd_workschedule`  
**表单ID**: `1=H59=8+41QY`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_workschedule（工作日程表） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_workschedule` | 主表 · 36 列 |
| `t_wtbd_workscheentry` | 分录表 · 16 列 |
| `t_wtbd_workschedule_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_workschedule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_workschedule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_workschedule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_workschedule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_workschedule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_workschedule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_workschedule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_workschedule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_workschedule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_workschedule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_workschedule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_workschedule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_workschedule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_workschedule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_workschedule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_workschedule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_workschedule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_workschedule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_workschedule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_workschedule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_workschedule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_workschedule.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_workschedule.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_workschedule_l.foriname |  |  |
| shiftmode | 轮班规则 | BasedataField | t_wtbd_workschedule.fshiftmode | ✓ | wtbd_shiftmode |
| calendarmodel | 日历生成规则 | BasedataField | t_wtbd_workschedule.fcalendarmodelid | ✓ | wtbd_calendarmodel |
| holidayportfolios | 假期组合 | MulBasedataField | — |  |  |
| basedate | 基准日期 | DateField | t_wtbd_workschedule.fbasedate | ✓ |  |
| genstarttime | 生成开始日期 | DateField | t_wtbd_workschedule.fgenstarttime | ✓ |  |
| genendtime | 生成结束日期 | DateField | t_wtbd_workschedule.fgenendtime | ✓ |  |
| floorgendate | 最早已生成日期-存库 | DateField | t_wtbd_workschedule.ffloorgendate |  |  |
| ceilinggendate | 最晚已生成日期-存库 | DateField | t_wtbd_workschedule.fceilinggendate |  |  |

## 实体: wtbd_workscheduleentry（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| etimezoneid | 地区/时区 | BigIntField | — |  |  |
| weekday | 星期几 | IntegerField | t_wtbd_workscheentry.fweekday |  |  |
| shiftid | 班次 | BigIntField | t_wtbd_workscheentry.fshiftid |  |  |
| holidayid | 假期ID逗号拼接可空 | TextField | t_wtbd_workscheentry.fholidayid |  |  |
| selfset | 是否单独设置 | TextField | t_wtbd_workscheentry.fselfset |  |  |
| ecreatorid | 创建人 | BigIntField | t_wtbd_workscheentry.fecreatorid |  |  |
| emodifierid | 最近修改人 | BigIntField | t_wtbd_workscheentry.femodifierid |  |  |
| entryboid | 分录BOID | BigIntField | t_wtbd_workscheentry.fentryboid |  |  |
| datetype | 日期类型可空 | BigIntField | — |  |  |
| datepropertyid | 日期属性 | BigIntField | t_wtbd_workscheentry.fdatepropertyid |  |  |
| emodifiername | 最近修改人名称-产品要看 | TextField | t_wtbd_workscheentry.femodifiername |  |  |
| workdate | 安排日程的日期 | DateField | t_wtbd_workscheentry.fworkdate |  |  |
| oridatepropertyid | 原始日期属性 | BigIntField | t_wtbd_workscheentry.foridatepropertyid |  |  |
| oridatetype | 原始日期类型可空 | BigIntField | t_wtbd_workscheentry.foridatetype |  |  |
| ecreatetime | 创建时间 | DateTimeField | t_wtbd_workscheentry.fecreatetime |  |  |
| emodifytime | 最近修改时间 | DateTimeField | t_wtbd_workscheentry.femodifytime |  |  |
| timezoneid | 地区/时区 | BasedataField | t_wtbd_workscheentry.ftimezoneid | ✓ | inte_timezone |
| showmonth | 显示月份 | DateField | t_wtbd_workschedule.fshowmonth |  |  |
| appendmode | 新增模式 | ComboField | — |  |  |
| previewfloorgendate | 最早已生成日期-不存库-预览 | DateField | — |  |  |
| previewceilinggendate | 最晚已生成日期-不存库-预览 | DateField | — |  |  |
| calendarmodelfid | 日历生成规则FID | BigIntField | t_wtbd_workschedule.fcalendarmodelfid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_workschedule（主表） | 29 |
| t_wtbd_workschedule_l | 4 |
| t_wtbd_workscheentry | 15 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
