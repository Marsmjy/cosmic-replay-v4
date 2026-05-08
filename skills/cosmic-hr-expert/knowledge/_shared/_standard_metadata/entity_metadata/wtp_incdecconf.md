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

# wtp_incdecconf — 增减配置

**表单编码**: `wtp_incdecconf`  
**表单ID**: `297G/+PLUSHF`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_incdecconf（增减配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_incdecconf` | 主表 · 35 列 |
| `t_wtp_incdecconfentry` | 分录表 · 20 列 |
| `t_wtp_incdecconfdetail` | 分录表 · 4 列 |
| `t_wtp_incdecresultdetail` | 分录表 · 4 列 |
| `t_wtp_incdecconf_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_incdecconf.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_incdecconf_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_incdecconf.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_incdecconf.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_incdecconf.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_incdecconf.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_incdecconf.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_incdecconf.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_incdecconf.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_incdecconf.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_incdecconf.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_incdecconf.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_incdecconf.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_incdecconf.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_incdecconf.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_incdecconf.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_incdecconf_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_incdecconf_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_incdecconf.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_incdecconf.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_incdecconf.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_incdecconf.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_incdecconf.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_incdecconf.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_incdecconf.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_incdecconf.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_incdecconf.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_incdecconf.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_incdecconf.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_incdecconf.fhisversion |  |  |
| triggertype | 触发方式 | ComboField | t_wtp_incdecconf.ftriggertype | ✓ |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| conditionstr | 条件 | TextField | t_wtp_incdecconfentry.fconditionstr |  |  |
| resultstr | 结果 | TextField | t_wtp_incdecconfentry.fresultstr |  |  |
| serialperiod | 期间范围 | ComboField | t_wtp_incdecconfentry.fserialperiod |  |  |
| curattitem | 连续项目 | BasedataField | t_wtp_incdecconfentry.fcurattitemid |  | wtbd_attitem |
| indecrrel | 连续增减方式 | ComboField | t_wtp_incdecconfentry.findecrrel |  |  |
| indecrvalue | 连续增减值 | DecimalField | t_wtp_incdecconfentry.findecrvalue |  |  |
| logicstr | 隐藏的条件文本 | TextField | t_wtp_incdecconfentry.flogicstr |  |  |
| logictype | 条件逻辑 | TextField | t_wtp_incdecconfentry.flogictype |  |  |
| daytype | 适用日期 | ComboField | t_wtp_incdecconfentry.fdaytype |  |  |
| seriallimit | 连续增减 | CheckBoxField | t_wtp_incdecconfentry.fseriallimit |  |  |
| regulartime | 固定时点 | TimeField | t_wtp_incdecconfentry.fregulartime |  |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |
| shifttype | 适用班次 | ComboField | t_wtp_incdecconfentry.fshifttype |  |  |
| istriggeritem | 设置触发项目 | CheckBoxField | t_wtp_incdecconfentry.fistriggeritem |  |  |
| triggercomp | 触发方式 | ComboField | t_wtp_incdecconfentry.ftriggercomp |  |  |
| triggerval | 触发值 | IntegerField | t_wtp_incdecconfentry.ftriggerval |  |  |
| triggeritem | 触发项目 | BasedataField | t_wtp_incdecconfentry.ftriggeritem |  | wtbd_attitem |
| triggersign | 触发增减方式 | ComboField | t_wtp_incdecconfentry.ftriggersign |  |  |
| triggerresult | 触发增减值 | DecimalField | t_wtp_incdecconfentry.ftriggerresult |  |  |
| condrel | 关系 | TextField | t_wtp_incdecconfdetail.fcondrel |  |  |
| condvalue | 条件值 | DecimalField | t_wtp_incdecconfdetail.fcondvalue |  |  |
| limitno | 限制条件序号 | TextField | t_wtp_incdecconfdetail.flimitno |  |  |
| condattitem | 考勤项目 | MulBasedataField | — |  |  |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| resultitem | 考勤项目 | BasedataField | t_wtp_incdecresultdetail.fresultitemid |  | wtbd_attitem |
| resultrel | 增减关系 | ComboField | t_wtp_incdecresultdetail.fresultrel |  |  |
| resultvalue | 增减值 | DecimalField | t_wtp_incdecresultdetail.fresultvalue |  |  |
| entryboid3 | 分录BOID | BigIntField | — |  |  |
| suittype | 适用类型 | ComboField | t_wtp_incdecconf.fsuittype | ✓ |  |
| triggertypeshow | 触发条件 | ComboField | — |  |  |
| conditionjson | 日期范围JSON | TextField | t_wtp_incdecconf.fconditionjson |  |  |

## 实体: notentryentity（不触发单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| notcurattitemshow | 考勤项目 | BasedataField | — |  | wtbd_attitem |
| notindecrrelshow | 增减方式 | ComboField | — |  |  |
| notindecrvalueshow | 值 | DecimalField | — |  |  |
| notunitshow | 单位 | BasedataPropField | — |  |  |
| entryboid12 | 分录BOID | BigIntField | — |  |  |

## 实体: timeentryentity（固定时点单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| timecurattitemshow | 考勤项目 | BasedataField | — |  | wtbd_attitem |
| regulartimeshow | 有效卡时段包含卡点 | TimeField | — |  |  |
| timeindecrrelshow | 增减方式 | ComboField | — |  |  |
| timeindecrvalueshow | 值 | DecimalField | — |  |  |
| timeunitshow | 单位 | BasedataPropField | — |  |  |
| entryboid11 | 分录BOID | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_incdecconf（主表） | 30 |
| t_wtp_incdecconf_l | 3 |
| t_wtp_incdecconfdetail | 3 |
| t_wtp_incdecconfentry | 18 |
| t_wtp_incdecresultdetail | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 22 |
