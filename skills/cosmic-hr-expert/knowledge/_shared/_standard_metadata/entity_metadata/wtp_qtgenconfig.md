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

# wtp_qtgenconfig — 定额生成配置

**表单编码**: `wtp_qtgenconfig`  
**表单ID**: `3420NS8Y=NWJ`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtgenconfig（定额生成配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtgenconfig` | 主表 · 48 列 |
| `t_wtp_qtgenconfigentity` | 分录表 · 12 列 |
| `t_wtp_qtgenconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtgenconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtgenconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtgenconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtgenconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtgenconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtgenconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtgenconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtgenconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtgenconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtgenconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtgenconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtgenconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtgenconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtgenconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtgenconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtgenconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtgenconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtgenconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtgenconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtgenconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtgenconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtgenconfig.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtgenconfig.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtgenconfig.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtgenconfig.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtgenconfig.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtgenconfig.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtgenconfig.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtgenconfig.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtgenconfig.fhisversion |  |  |

## 实体: entryentity（条件信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| conditionfiltershow | 场景条件 | TextField | — |  |  |
| roundrule | 舍入规则 | BasedataField | t_wtp_qtgenconfigentity.froundruleid |  | wtbd_roundrule |
| resultattitem | 结果项目 | BasedataField | t_wtp_qtgenconfigentity.fresultattitemid | ✓ | wtbd_attitem |
| entryboid | 分录BOID | BigIntField | t_wtp_qtgenconfigentity.fentryboid |  |  |
| conditionfilter | 过滤条件 | TextField | t_wtp_qtgenconfigentity.fconditionfilter |  |  |
| attitemshow | 配置项目 | TextField | — |  |  |
| percent | 比例 | DecimalField | t_wtp_qtgenconfigentity.fpercent |  |  |
| roundpriority | 舍入优先级 | ComboField | t_wtp_qtgenconfigentity.froundpriority |  |  |
| attitem | 考勤项目 | BasedataField | t_wtp_qtgenconfigentity.fattitemid |  | wtbd_attitem |
| maxattitem | 超出最大值考勤项目 | BasedataField | t_wtp_qtgenconfigentity.fmaxattitemid |  | wtbd_attitem |
| enjoyattitem | 定额享有时长的考勤项目 | BasedataField | t_wtp_qtgenconfigentity.fenjoyattitemid |  | wtbd_attitem |
| usableattitem | 可用时长考勤项目 | BasedataField | t_wtp_qtgenconfigentity.fusableattitemid |  | wtbd_attitem |
| resultshow | 结果 | TextField | — |  |  |
| qualification | 限定条件折算 | BasedataField | t_wtp_qtgenconfigentity.fqualificationid |  | wtp_qtqualification |
| itemvalue | 项目值 | DecimalField | t_wtp_qtgenconfigentity.fitemvalue |  |  |
| qttype | 定额类型 | BasedataField | t_wtp_qtgenconfig.fqttypeid | ✓ | wtp_qttype |
| convert | 跨挂靠行政组织折算方式 | ComboField | t_wtp_qtgenconfig.fconvert | ✓ |  |
| cycset | 生成周期 | BasedataField | t_wtp_qtgenconfig.fcycsetid | ✓ | wtbd_cycset |
| conditionconvert | 跨阶折算方式 | ComboField | t_wtp_qtgenconfig.fconditionconvert | ✓ |  |
| transferorg | 挂靠行政组织调动时折算额度 | ComboField | t_wtp_qtgenconfig.ftransferorg | ✓ |  |
| gencondition | 按资格条件跨阶折算 | ComboField | t_wtp_qtgenconfig.fgencondition | ✓ |  |
| convertime | 开始日期 | ComboField | t_wtp_qtgenconfig.fconvertime | ✓ |  |
| transfermangorg | 跨考勤管理组织折算方式 | ComboField | t_wtp_qtgenconfig.ftransfermangorg | ✓ |  |
| isgendetails | 允许命中多条件信息 | CheckBoxField | t_wtp_qtgenconfig.fisgendetails |  |  |
| nogenbeforedate | 特定日期前不生成 | ComboField | t_wtp_qtgenconfig.fnogenbeforedate |  |  |
| upversion | 升级版本 | ComboField | t_wtp_qtgenconfig.fupversion |  |  |
| settlement | 结算额度方式 | ComboField | t_wtp_qtgenconfig.fsettlement | ✓ |  |
| crossstartday | 开始日期 | ComboField | t_wtp_qtgenconfig.fcrossstartday | ✓ |  |
| installmentissue | 发放方式 | ComboField | t_wtp_qtgenconfig.finstallmentissue |  |  |
| installmentissuedate | 发放日期 | ComboField | t_wtp_qtgenconfig.finstallmentissuedate | ✓ |  |
| installmentfullissue | 满额发放 | DecimalField | t_wtp_qtgenconfig.finstallmentfullissue |  |  |
| installmentfullissuev | 满额发放 | DecimalField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtgenconfig（主表） | 43 |
| t_wtp_qtgenconfig_l | 3 |
| t_wtp_qtgenconfigentity | 12 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
