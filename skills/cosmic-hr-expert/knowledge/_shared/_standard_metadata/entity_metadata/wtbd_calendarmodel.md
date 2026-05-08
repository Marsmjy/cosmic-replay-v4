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

# wtbd_calendarmodel — 日历生成规则

**表单编码**: `wtbd_calendarmodel`  
**表单ID**: `2A27XCQ8W=+U`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_calendarmodel（日历生成规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_calendarmodel` | 主表 · 43 列 |
| `t_wtbd_calmodelentry` | 分录表 · 6 列 |
| `t_wtbd_calendarmodel_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_calendarmodel.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_calendarmodel_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_calendarmodel.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_calendarmodel.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_calendarmodel.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_calendarmodel.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_calendarmodel.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_calendarmodel.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_calendarmodel.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_calendarmodel.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_calendarmodel.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_calendarmodel.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_calendarmodel.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_calendarmodel.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_calendarmodel.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_calendarmodel.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_calendarmodel_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_calendarmodel_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_calendarmodel.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_calendarmodel.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_calendarmodel.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_calendarmodel.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_calendarmodel.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_calendarmodel.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_calendarmodel.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_calendarmodel.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtbd_calendarmodel.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_calendarmodel.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_calendarmodel.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_calendarmodel.fhisversion |  |  |
| loopmodeweek | 按周 | RadioField | — |  |  |
| loopmode12 | 循环方式 | RadioGroupField | — |  |  |
| loopmode4day | 按天 | RadioField | — |  |  |

## 实体: exentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| startdate | 开始日期 | DateField | t_wtbd_calmodelentry.fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | t_wtbd_calmodelentry.fenddate | ✓ |  |
| dateprop | 日期属性 | BasedataField | t_wtbd_calmodelentry.fdateprop | ✓ | wtbd_dateproperty |
| datetype | 日期类型 | BasedataField | t_wtbd_calmodelentry.fdatetype |  | wtbd_datetype |
| entryboid | 分录BOID | BigIntField | t_wtbd_calmodelentry.fentryboid |  |  |
| explanation | 描述 | TextAreaField | t_wtbd_calmodelentry.fexplanation |  |  |
| mon | 星期一 | CheckBoxField | t_wtbd_calendarmodel.fmon |  |  |
| tue | 星期二 | CheckBoxField | t_wtbd_calendarmodel.ftue |  |  |
| wed | 星期三 | CheckBoxField | t_wtbd_calendarmodel.fwed |  |  |
| thurs | 星期四 | CheckBoxField | t_wtbd_calendarmodel.fthurs |  |  |
| fri | 星期五 | CheckBoxField | t_wtbd_calendarmodel.ffri |  |  |
| sat | 星期六 | CheckBoxField | t_wtbd_calendarmodel.fsat |  |  |
| sun | 星期日 | CheckBoxField | t_wtbd_calendarmodel.fsun |  |  |
| cycperiodtxt | 自定义周期存储文本 | TextField | t_wtbd_calendarmodel.fcycperiodtxt |  |  |
| holidaycombine | 假期组合 | MulBasedataField | — | ✓ |  |
| wrokdaysetmode | 设置方式 | ComboField | t_wtbd_calendarmodel.fwrokdaysetmode | ✓ |  |
| loopfactor | 循环数值 | IntegerField | t_wtbd_calendarmodel.floopfactor | ✓ |  |
| loopmode | 循环模式 | ComboField | t_wtbd_calendarmodel.floopmode | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_calendarmodel（主表） | 38 |
| t_wtbd_calendarmodel_l | 3 |
| t_wtbd_calmodelentry | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
