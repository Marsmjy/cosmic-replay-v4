---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_calperiodtype — 薪资期间

**表单编码**: `hsbs_calperiodtype`  
**表单ID**: `2APVHQR9=EV6`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_calperiodtype（薪资期间） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_calperiodtype` | 主表 · 36 列 |
| `t_hsbs_calperiod` | 分录表 · 25 列 |
| `t_hsbs_calperiodtype_l` | 多语言表 · 3 列 |
| `t_hsbs_calperiod_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 期间类型编码 | TextField | t_hsbs_calperiod.fnumber | ✓ |  |
| name | 期间类型名称 | MuliLangTextField | t_hsbs_calperiod_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_calperiod.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_calperiod.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_calperiod.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_calperiod.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_calperiod.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_calperiod.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_calperiod.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_calperiodtype.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_calperiodtype.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_calperiodtype.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_calperiodtype.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_calperiodtype.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_calperiodtype.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_calperiod.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_calperiod_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_calperiod_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_calperiod.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_calperiod.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_calperiod.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: entryentity（期间信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| periodnumber | 期间编码 | TextField | — |  |  |
| periodname | 期间名称 | MuliLangTextField | — |  |  |
| periodcalfrequency | 频度 | BasedataField | — |  | hsbs_calfrequency |
| startdate | 期间开始日期 | DateField | t_hsbs_calperiod.fstartdate | ✓ |  |
| enddate | 期间结束日期 | DateField | t_hsbs_calperiod.fenddate | ✓ |  |
| ratedate | 汇率日期 | DateField | t_hsbs_calperiod.fratedate | ✓ |  |
| paydate | 预计支付日期 | DateField | t_hsbs_calperiod.fpaydate | ✓ |  |
| periodyear | 薪资所属年份 | DateField | t_hsbs_calperiod.fperiodyear | ✓ |  |
| periodmasterid | 期间信息主数据内码 | BigIntField | — |  |  |
| periodenable | 期间信息使用状态 | BillStatusField | — |  |  |
| periodstatus | 期间信息数据状态 | ComboField | — |  |  |
| periodcreator | 创建人 | BigIntField | — |  |  |
| periodcreatetime | 创建时间 | DateTimeField | — |  |  |
| periodmodifytime | 修改时间 | DateTimeField | — |  |  |
| periodmodifier | 修改人 | BigIntField | — |  |  |
| perioddisabler | 禁用人 | BigIntField | — |  |  |
| perioddisabledate | 禁用时间 | DateTimeField | — |  |  |
| periodissyspreset | 是否系统预置 | CheckBoxField | — |  |  |
| periodsimplename | 简称 | MuliLangTextField | — |  |  |
| perioddescription | 描述 | MuliLangTextField | — |  |  |
| periodindex | 期间信息顺序号 | IntegerField | — |  |  |
| perioddate | 薪资所属年月 | DateField | t_hsbs_calperiod.fperioddate | ✓ |  |
| refstatus | 引用状态 | ComboField | — |  |  |
| periodtypename | 期间类型名称 | MuliLangTextField | t_hsbs_calperiod_l.fperiodtypename |  |  |
| taxstartdate | 税款所属期起始日期 | DateField | t_hsbs_calperiod.ftaxstartdate |  |  |
| taxenddate | 税款所属期截止日期 | DateField | t_hsbs_calperiod.ftaxenddate |  |  |
| periodnumberprefix | 编码前缀 | TextField | t_hsbs_calperiodtype.fperiodnumberprefix |  |  |
| periodnumbersubject | 编码主体 | ComboField | t_hsbs_calperiodtype.fperiodnumbersubject | ✓ |  |
| periodnumbersuffix | 编码后缀 | TextField | t_hsbs_calperiodtype.fperiodnumbersuffix |  |  |
| serialnumber | 流水号位数 | IntegerField | t_hsbs_calperiodtype.fserialnumber |  |  |
| periodnumberexample | 生成编码样例 | TextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_calperiodtype.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_calperiodtype.fcountryid |  | bd_country |
| calfrequency | 频度 | BasedataField | t_hsbs_calperiod.fcalfrequencyid | ✓ | hsbs_calfrequency |
| monthday | 起始日为每月 | ComboField | t_hsbs_calperiodtype.fmonthday |  |  |
| halfmonthfirstday | 半月的初选择日 | ComboField | t_hsbs_calperiodtype.fhalfmonthfirstday |  |  |
| halfmonthsecday | 半月的次选择日 | ComboField | t_hsbs_calperiodtype.fhalfmonthsecday |  |  |
| weekday | 起始周 | ComboField | t_hsbs_calperiodtype.fweekday |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_calperiodtype.fareatype | ✓ |  |
| calperiodcount | 期间分录数量 | IntegerField | t_hsbs_calperiodtype.fcalperiodcount |  |  |
| calfrequencytype | 频度类型 | BasedataPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_calperiodtype（主表） | 18 |
| t_hsbs_calperiod | 21 |
| t_hsbs_calperiod_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 25 |
