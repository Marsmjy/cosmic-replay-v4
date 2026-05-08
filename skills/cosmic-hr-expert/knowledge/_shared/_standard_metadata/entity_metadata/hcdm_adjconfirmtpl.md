---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_adjconfirmtpl — 定调薪确认模板配置

**表单编码**: `hcdm_adjconfirmtpl`  
**表单ID**: `28WDHJ5D6+E1`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_adjconfirmtpl（定调薪确认模板配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_adjconfirmtpl` | 主表 · 28 列 |
| `t_hcdm_adjconfirmtpl_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_adjconfirmtpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_adjconfirmtpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_adjconfirmtpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_adjconfirmtpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_adjconfirmtpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_adjconfirmtpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_adjconfirmtpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_adjconfirmtpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_adjconfirmtpl.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_adjconfirmtpl.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_adjconfirmtpl.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_adjconfirmtpl.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_adjconfirmtpl.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_adjconfirmtpl.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_adjconfirmtpl.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_adjconfirmtpl.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_adjconfirmtpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_adjconfirmtpl_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_adjconfirmtpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_adjconfirmtpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_adjconfirmtpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| buttonselect | 定调薪确认页面显示按钮 | MulComboField | t_hcdm_adjconfirmtpl.fbuttonselect | ✓ |  |
| country | 国家/地区 | BasedataField | t_hcdm_adjconfirmtpl.fcountryid | ✓ | bd_country |
| msgtemplate | 消息通知模板 | BasedataField | t_hcdm_adjconfirmtpl.fmsgtemplateid | ✓ | msg_template |
| msgchannel | 消息通知方式 | BasedataPropField | — |  |  |
| content | 内容 | TextField | t_hcdm_adjconfirmtpl.fcontent |  |  |
| tempcontent | 临时内容 | LargeTextField | — |  |  |
| applyscope | 定调薪确认应用于 | ComboField | t_hcdm_adjconfirmtpl.fapplyscope | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_adjconfirmtpl（主表） | 23 |
| t_hcdm_adjconfirmtpl_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
