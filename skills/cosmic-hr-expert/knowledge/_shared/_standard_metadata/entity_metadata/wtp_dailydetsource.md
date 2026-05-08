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

# wtp_dailydetsource — 日明细考勤项目

**表单编码**: `wtp_dailydetsource`  
**表单ID**: `2IGE5I2FYP0L`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_dailydetsource（日明细考勤项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_dailydetsource` | 主表 · 30 列 |
| `t_wtp_dailydetsouentry` | 分录表 · 5 列 |
| `t_wtp_dailydetsource_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_dailydetsource.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_dailydetsource_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_dailydetsource.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_dailydetsource.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_dailydetsource.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_dailydetsource.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_dailydetsource.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_dailydetsource.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_dailydetsource.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_dailydetsource.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_dailydetsource.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_dailydetsource.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_dailydetsource.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_dailydetsource.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_dailydetsource.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_dailydetsource.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_dailydetsource_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_dailydetsource_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_dailydetsource.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_dailydetsource.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_dailydetsource.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_dailydetsource.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_dailydetsource.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_dailydetsource_l.foriname |  |  |
| busclassify | 业务分类 | ComboField | t_wtp_dailydetsource.fbusclassify | ✓ |  |
| datatype | 数据类型 | ComboField | t_wtp_dailydetsource.fdatatype |  |  |
| unit | 单位 | ComboField | t_wtp_dailydetsource.funit |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitem | 考勤项目 | BasedataField | t_wtp_dailydetsouentry.fattitemid | ✓ | wtbd_attitem |
| attitemcode | 编码 | BasedataPropField | t_wtp_dailydetsouentry.fattitemcode |  |  |
| attitemitemtype | 项目类型 | BasedataPropField | t_wtp_dailydetsouentry.fattitemitemtype |  |  |
| attitemdatatype | 数据类型 | BasedataPropField | t_wtp_dailydetsouentry.fattitemdatatype |  |  |
| attitemunit | 单位 | BasedataPropField | t_wtp_dailydetsouentry.fattitemunit |  |  |
| a | 迟到 | CheckBoxField | — |  |  |
| b | 早退 | CheckBoxField | — |  |  |
| c | 缺卡 | CheckBoxField | — |  |  |
| d | 旷工 | CheckBoxField | — |  |  |
| e | 早到 | CheckBoxField | — |  |  |
| selectedbill | 异常类型 | MulComboField | t_wtp_dailydetsource.fselectedbill |  |  |
| f | 晚走 | CheckBoxField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_dailydetsource（主表） | 24 |
| t_wtp_dailydetsouentry | 5 |
| t_wtp_dailydetsource_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
