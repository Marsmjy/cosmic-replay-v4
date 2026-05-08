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

# wtbd_takecardconfig — 取卡配置

**表单编码**: `wtbd_takecardconfig`  
**表单ID**: `2+6M/AR6LWB6`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_takecardconfig（取卡配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_tcconfig` | 主表 · 35 列 |
| `t_wtbd_tcconfigentry` | 分录表 · 6 列 |
| `t_wtbd_tcconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_tcconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_tcconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_tcconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_tcconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_tcconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_tcconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_tcconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_tcconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_tcconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_tcconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_tcconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_tcconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_tcconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_tcconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_tcconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_tcconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_tcconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_tcconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_tcconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_tcconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_tcconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_tcconfig.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_tcconfig.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_tcconfig.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_tcconfig.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_tcconfig.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtbd_tcconfig.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_tcconfig.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_tcconfig.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_tcconfig.fhisversion |  |  |

## 实体: entryentity（取卡配置信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isworktimestart | 开始时点打卡 | CheckBoxField | t_wtbd_tcconfigentry.fisworktimestart |  |  |
| isworktimeend | 结束时点打卡 | CheckBoxField | t_wtbd_tcconfigentry.fisworktimeend |  |  |
| isopenvalid | 启用校验卡 | CheckBoxField | t_wtbd_tcconfigentry.fisopenvalid |  |  |
| time | 上班时段 | TextField | t_wtbd_tcconfigentry.ftime |  |  |
| entryboid | 分录BOID | BigIntField | t_wtbd_tcconfigentry.fentryboid |  |  |
| classtimetype | 应出勤时段类型 | ComboField | t_wtbd_tcconfigentry.fclasstimetype |  |  |
| classtimenum | 班次时段数 | ComboField | t_wtbd_tcconfig.fclasstimenum | ✓ |  |
| classgetnum | 取卡数 | ComboField | t_wtbd_tcconfig.fclassgetnum | ✓ |  |
| takecardshape | 取卡图示 | TextAreaField | t_wtbd_tcconfig.ftakecardshape |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_tcconfig（主表） | 30 |
| t_wtbd_tcconfig_l | 3 |
| t_wtbd_tcconfigentry | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
