---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_attbizitem — 考勤业务项目

**表单编码**: `hsbs_attbizitem`  
**表单ID**: `3V3T4RTFH0AY`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_attbizitem（考勤业务项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_bizitem` | 主表 · 40 列 |
| `t_hsbs_bizitem_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_bizitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_bizitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_bizitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_bizitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_bizitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_bizitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_bizitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_bizitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_bizitem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_bizitem.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_bizitem.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_bizitem.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_bizitem.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_bizitem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_bizitem.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_bizitem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_bizitem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_bizitem_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_bizitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_bizitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_bizitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| generalenname | 通用英文名称 | TextField | t_hsbs_bizitem.fgeneralenname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_bizitem.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_bizitem.fcountryid |  | bd_country |
| uniquecode | 唯一编码 | TextField | t_hsbs_bizitem.funiquecode |  |  |
| datatype | 数据类型 | BasedataField | t_hsbs_bizitem.fdatatypeid | ✓ | hsbs_datatype |
| datalength | 数据长度 | IntegerField | t_hsbs_bizitem.fdatalength |  |  |
| currency | 可输入币种 | MulBasedataField | — |  |  |
| scalelimit | 小数位数限制 | ComboField | t_hsbs_bizitem.fscalelimit |  |  |
| minvalue | 最小输入值 | DecimalField | t_hsbs_bizitem.fminvalue |  |  |
| inputminval | 最小输入值 | TextField | — |  |  |
| maxvalue | 最大输入值 | DecimalField | t_hsbs_bizitem.fmaxvalue |  |  |
| inputmaxval | 最大输入值 | TextField | — |  |  |
| earliestdate | 最早日期 | DateField | t_hsbs_bizitem.fearliestdate |  |  |
| lastdate | 最晚日期 | DateField | t_hsbs_bizitem.flastdate |  |  |
| attitemtype | 考勤项目类型 | ComboField | t_hsbs_bizitem.fattitemtype | ✓ |  |
| classification | 分类 | ComboField | t_hsbs_bizitem.fclassification |  |  |
| consumemethod | 计算取值方式 | ComboField | t_hsbs_bizitem.fconsumemethod | ✓ |  |
| islistgenerate | 用于名单生成 | ComboField | t_hsbs_bizitem.fislistgenerate | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_bizitem（主表） | 33 |
| t_hsbs_bizitem_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
