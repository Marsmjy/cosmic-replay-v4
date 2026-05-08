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

# wtp_exconfig — 异常配置

**表单编码**: `wtp_exconfig`  
**表单ID**: `23P2KZ2J8TPH`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_exconfig（异常配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_exconfig` | 主表 · 41 列 |
| `t_wtp_exitemconfig` | 分录表 · 16 列 |
| `t_wtp_exconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_exconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_exconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_exconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_exconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_exconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_exconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_exconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_exconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_exconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_exconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_exconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_exconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_exconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_exconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_exconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_exconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_exconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_exconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_exconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_exconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_exconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_exconfig.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_exconfig.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_exconfig.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_exconfig.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_exconfig.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_exconfig.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_exconfig.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_exconfig.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_exconfig.fhisversion |  |  |
| limitfilter | 过滤条件 | TextField | t_wtp_exconfig.flimitfilter |  |  |
| shifttype | 班次类型 | ComboField | t_wtp_exconfig.fshifttype |  |  |
| attmethod | 考勤方式 | ComboField | t_wtp_exconfig.fattmethod |  |  |

## 实体: entryentity（异常项目设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| conditionfiltershow | 条件 | TextField | — |  |  |
| are | 比较符 | ComboField | t_wtp_exitemconfig.fare | ✓ |  |
| culvalue | 值 | IntegerField | t_wtp_exitemconfig.fculvalue |  |  |
| culunit | 单位 | ComboField | t_wtp_exitemconfig.fculunit | ✓ |  |
| logic | 逻辑 | ComboField | t_wtp_exitemconfig.flogic |  |  |
| bre | 比较符 | ComboField | t_wtp_exitemconfig.fbre | ✓ |  |
| maxvalue | 值 | IntegerField | t_wtp_exitemconfig.fmaxvalue |  |  |
| maxunit | 单位 | ComboField | t_wtp_exitemconfig.fmaxunit | ✓ |  |
| roundrule | 舍入规则 | BasedataField | t_wtp_exitemconfig.froundruleid |  | wtbd_roundrule |
| dealmethod | 时长处理方式 | ComboField | t_wtp_exitemconfig.fdealmethod |  |  |
| originalitem | 原始时长 | BasedataField | t_wtp_exitemconfig.foriginalitemid | ✓ | wtbd_attitem |
| attitems | 结果时长 | MulBasedataField | — | ✓ |  |
| entryboid31 | 分录BOID | BigIntField | — |  |  |
| dealtype | 处理方式 | ComboField | t_wtp_exitemconfig.fdealtype |  |  |
| punchcard | 应打卡对 | BasedataField | t_wtp_exitemconfig.fpunchcardid |  | wtbd_punchcardpair |
| conditionfilter | 过滤条件 | TextField | t_wtp_exitemconfig.fconditionfilter |  |  |
| bruleshow | 条件 | TextField | — |  |  |
| bresultshow | 结果 | TextField | — |  |  |
| aruleshow | 条件 | TextField | — |  |  |
| aresultshow | 结果 | TextField | — |  |  |
| appointvalue | 指定时长 | DecimalField | t_wtp_exitemconfig.fappointvalue |  |  |
| appointunit | 指定时长单位 | ComboField | t_wtp_exitemconfig.fappointunit |  |  |
| maxvalue_show | 值 | IntegerField | — |  |  |
| iscustom | 是否属于自定义异常类型 | CheckBoxField | t_wtp_exconfig.fiscustom |  |  |
| exattribute | 异常类型 | BasedataField | t_wtp_exconfig.fexattributeid | ✓ | wtbd_exattribute |
| dealtypeshow3 | 处理方式 | ComboField | — |  |  |
| punchcardshow | 应打卡对 | BasedataField | — |  | wtbd_punchcardpair |
| periodfilter | 时段过滤插件 | BasedataField | t_wtp_exconfig.fperiodfilterid |  | wtbs_pluginregister |
| isallpunchcard | 所有应打卡对同样设置 | CheckBoxField | t_wtp_exconfig.fisallpunchcard |  |  |
| isflexibleattperiod | 弹性出勤时段不记异常 | CheckBoxField | t_wtp_exconfig.fisflexibleattperiod |  |  |
| isoffshift | OFF班不记异常 | CheckBoxField | t_wtp_exconfig.fisoffshift |  |  |
| originalitemshow | 原始时长 | BasedataField | — |  | wtbd_attitem |
| attitemsshow | 结果时长 | MulBasedataField | — |  |  |
| originalitemshow3 | 原始时长 | BasedataField | — |  | wtbd_attitem |
| ruledate | 规则控件日期 | DateField | — |  |  |

## 实体: absentryentity（旷工单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| punchcard_abs | 应打卡对 | BasedataField | — |  | wtbd_punchcardpair |
| originalitem_abs | 原始时长 | BasedataField | — |  | wtbd_attitem |
| attitems_abs | 结果时长 | MulBasedataField | — |  |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| originalitemshowtwo | 原始时长 | BasedataField | — |  | wtbd_attitem |
| dealtypeshow | 处理方式 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_exconfig（主表） | 36 |
| t_wtp_exconfig_l | 3 |
| t_wtp_exitemconfig | 15 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 26 |
