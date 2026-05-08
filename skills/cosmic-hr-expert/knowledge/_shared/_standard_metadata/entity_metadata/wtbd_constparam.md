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

# wtbd_constparam — 常量参数

**表单编码**: `wtbd_constparam`  
**表单ID**: `48ESKBHD9=DV`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_constparam（常量参数） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_constparam` | 主表 · 38 列 |
| `t_wtbd_constparam_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_constparam.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_constparam_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_constparam.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_constparam.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_constparam.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_constparam.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_constparam.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_constparam.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_constparam.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_constparam.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_constparam.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_constparam.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_constparam.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_constparam.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_constparam.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_constparam.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_constparam_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_constparam_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_constparam.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_constparam.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_constparam.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_constparam.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_constparam.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_constparam.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_constparam.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_constparam.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtbd_constparam.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_constparam.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_constparam.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_constparam.fhisversion |  |  |
| type | 类型 | ComboField | t_wtbd_constparam.ftype | ✓ |  |
| decimalvalue | 数值 | DecimalField | t_wtbd_constparam.fdecimalvalue |  |  |
| stringvalue | 文本值 | TextField | t_wtbd_constparam.fstringvalue |  |  |
| datevalue | 日期值 | DateField | t_wtbd_constparam.fdatevalue |  |  |
| value | 值 | TextField | t_wtbd_constparam.fvalue |  |  |
| booleanvalue | 布尔值 | ComboField | t_wtbd_constparam.fbooleanvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_constparam（主表） | 33 |
| t_wtbd_constparam_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
