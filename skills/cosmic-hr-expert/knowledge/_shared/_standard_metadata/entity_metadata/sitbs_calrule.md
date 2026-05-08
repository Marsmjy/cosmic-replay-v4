---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_calrule — 计算规则

**表单编码**: `sitbs_calrule`  
**表单ID**: `25S1V/FSEKUT`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_calrule（计算规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_calrule` | 主表 · 36 列 |
| `t_sitbs_calruleentry` | 分录表 · 8 列 |
| `t_sitbs_calrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_calrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_calrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_calrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_calrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_calrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_calrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_calrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_calrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_calrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_calrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_calrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_calrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_calrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_calrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_calrule.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_sitbs_calrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_calrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_calrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_calrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_calrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_calrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_sitbs_calrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_sitbs_calrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_sitbs_calrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_sitbs_calrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_sitbs_calrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_sitbs_calrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_sitbs_calrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_sitbs_calrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_sitbs_calrule.fhisversion |  |  |

## 实体: calruleitementry（计算公式分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| taxitemnum | 个税项目编码 | BasedataPropField | t_sitbs_calruleentry.ftaxitemnum |  |  |
| taxitemname | 个税项目名称 | BasedataPropField | t_sitbs_calruleentry.ftaxitemname |  |  |
| formula | 公式 | HisModelBasedataField | — |  | sitbs_taxcalformula |
| formulanum | 公式编码 | BasedataPropField | t_sitbs_calruleentry.fformulanum |  |  |
| formulaname | 公式名称 | BasedataPropField | t_sitbs_calruleentry.fformulaname |  |  |
| datatype | 数据类型 | BasedataPropField | t_sitbs_calruleentry.fdatatype |  |  |
| taxitem | 个税项目 | BasedataField | t_sitbs_calruleentry.ftaxitemid |  | sitbs_taxcalresult |
| entryboid | 分录id | BigIntField | t_sitbs_calruleentry.fentryboid |  |  |
| calindex | 计算顺序 | IntegerField | — |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_calrule.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_calrule.fcountryid |  | bd_country |
| currency | 币种 | CurrencyField | — | ✓ | bd_currency |
| taxcategory | 个税种类 | BasedataField | t_sitbs_calrule.ftaxcategoryid | ✓ | sitbs_taxcategory |
| tracker | 跟踪人 | MulBasedataField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_calrule（主表） | 30 |
| t_sitbs_calrule_l | 3 |
| t_sitbs_calruleentry | 7 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
