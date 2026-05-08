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

# hsbs_bizitemgroup — 业务数据模板

**表单编码**: `hsbs_bizitemgroup`  
**表单ID**: `1QZK+R3GJN27`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_bizitemgroup（业务数据模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_bizitemgroup` | 主表 · 35 列 |
| `t_hsbs_bizitemgroupent` | 分录表 · 6 列 |
| `t_hsbs_bizitemgroup_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_bizitemgroup.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_bizitemgroup_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_bizitemgroup.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_bizitemgroup.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_bizitemgroup.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_bizitemgroup.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_bizitemgroup.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_bizitemgroup.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_bizitemgroup.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_bizitemgroup.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_bizitemgroup.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_bizitemgroup.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_bizitemgroup.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_bizitemgroup.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_bizitemgroup.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_bizitemgroup.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_bizitemgroup_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_bizitemgroup_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_bizitemgroup.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_bizitemgroup.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_bizitemgroup.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_bizitemgroup.fgeneralenname |  |  |

## 实体: entryentity（业务项目构成） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bizitem | 编码 | BasedataField | t_hsbs_bizitemgroupent.fbizitemid | ✓ | hsbs_bizitem |
| bizitemname | 名称 | BasedataPropField | — |  |  |
| datatype | 数据类型 | BasedataPropField | — |  |  |
| itemcategory | 业务项目类别 | BasedataPropField | — |  |  |
| cycle | 循环 | BasedataPropField | — |  |  |
| oneoffconsume | 一次性使用 | BasedataPropField | — |  |  |
| multipleinput | 多次输入 | BasedataPropField | — |  |  |
| consumemethod | 计算取值方式 | BasedataPropField | — |  |  |
| islistgenerate | 用于名单生成 | BasedataPropField | — |  |  |
| earliestdate | 业务项目最早日期 | BasedataPropField | — |  |  |
| lastdate | 业务项目最晚日期 | BasedataPropField | — |  |  |
| currencyname | 可输入币种 | BasedataPropField | — |  |  |
| scalelimit | 小数位数限制 | BasedataPropField | — |  |  |
| maxvalue | 业务项目最大值 | TextField | — |  |  |
| minvalue | 业务项目最小值 | TextField | — |  |  |
| maxinputtime | 最大输入次数 | IntegerField | — |  |  |
| submitstartdate | 提报最早日期 | DateField | t_hsbs_bizitemgroupent.fsubmitstartdate |  |  |
| submitenddate | 提报最晚日期 | DateField | t_hsbs_bizitemgroupent.fsubmitenddate |  |  |
| submitmaxvalue | 提报最大输入值 | DecimalField | t_hsbs_bizitemgroupent.fsubmitmaxvalue |  |  |
| submitminvalue | 提报最小输入值 | DecimalField | t_hsbs_bizitemgroupent.fsubmitminvalue |  |  |
| decimalscale | 数值精度 | IntegerField | — |  |  |
| usestatus | 当前模板使用状态 | ComboField | t_hsbs_bizitemgroupent.fusestatus |  |  |
| bizdataobjrule | 业务数据对象规则 | BasedataField | t_hsbs_bizitemgroup.fbizdataobjruleid |  | hsbs_bizdataobjrule |
| calperiodtype | 期间类型 | BasedataField | t_hsbs_bizitemgroup.fcalperiodtypeid | ✓ | hsbs_calperiodtype |
| transalarymode | 推送算薪方式 | ComboField | t_hsbs_bizitemgroup.ftransalarymode | ✓ |  |
| modeltype | 导入及查看样式 | RadioGroupField | t_hsbs_bizitemgroup.fmodeltype |  |  |
| isaudit | 是否已审核 | ComboField | t_hsbs_bizitemgroup.fisaudit |  |  |
| currencyfilltype | 币种填充方式 | ComboField | t_hsbs_bizitemgroup.fcurrencyfilltype | ✓ |  |
| sysfillcurrency | 系统填充币种 | CurrencyField | — |  | bd_currency |
| submitrangectl | 提报人员范围控制 | ComboField | t_hsbs_bizitemgroup.fsubmitrangectl | ✓ |  |
| cycletype | 模板类型 | ComboField | t_hsbs_bizitemgroup.fcycletype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_bizitemgroup（主表） | 27 |
| t_hsbs_bizitemgroup_l | 3 |
| t_hsbs_bizitemgroupent | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 23 |
