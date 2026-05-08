---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_calrule — 计算规则

**表单编码**: `hsas_calrule`  
**表单ID**: `00HJ=L=I4ZDM`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calrule（计算规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_calrule` | 主表 · 43 列 |
| `t_hsas_calruleitementry` | 分录表 · 12 列 |
| `t_hsas_calrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_calrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_calrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_calrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_calrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_calrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_calrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_calrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_calrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_calrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_calrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_calrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_calrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_calrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_calrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_calrule.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsas_calrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_calrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_calrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_calrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_calrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_calrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_calrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_calrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_calrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_calrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_calrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_calrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_calrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_calrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_calrule.fhisversion |  |  |
| opentaxcal | 中国个税计算模式 | ComboField | t_hsas_calrule.fopentaxcal |  |  |
| isdraft | 是否为草稿 | CheckBoxField | t_hsas_calrule.fisdraft |  |  |
| generalenname | 通用英文名 | TextField | t_hsas_calrule.fgeneralenname |  |  |

## 实体: calruleitementry（计算规则） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| calitemgroup | 计算规则模板名称 | BasedataField | t_hsas_calruleitementry.fcalitemgroupid |  | hsas_calitemgroup |
| calindex | 计算顺序 | IntegerField | — |  |  |
| salaryitem | 薪酬项目 | BasedataField | t_hsas_calruleitementry.fsalaryitemid | ✓ | hsbs_salaryitem |
| datasource | 数据来源 | ComboField | t_hsas_calruleitementry.fdatasource | ✓ |  |
| formula | 计算公式 | BasedataField | t_hsas_calruleitementry.fformulaid |  | hsas_formula |
| ispayoutitem | 实发项目 | ComboField | t_hsas_calruleitementry.fispayoutitem | ✓ |  |
| calitemgroupnumber | 计算规则模板编码 | BasedataPropField | — |  |  |
| allowresultcover | 允许结果覆盖 | ComboField | t_hsas_calruleitementry.fallowresultcover | ✓ |  |
| calblock | 计算区段 | BasedataPropField | — |  |  |
| taxtag | 个税云服务标签 | BasedataPropField | — |  |  |
| salaryitemtype | 薪酬项目系统分类 | BasedataPropField | — |  |  |
| iscostallot | 是否参与成本分摊 | ComboField | t_hsas_calruleitementry.fiscostallot |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_calruleitementry.fentryboid |  |  |
| isconvert | 分段折算策略 | ComboField | t_hsas_calruleitementry.fisconvert | ✓ |  |
| customprorationrule | 分段折算规则 | BasedataField | t_hsas_calruleitementry.fcustomprorationruleid |  | hsas_prorationrule |
| taxcategory | 个税种类 | BasedataPropField | — |  |  |
| bizitem | 业务项目 | BasedataField | t_hsas_calruleitementry.fbizitemid |  | hsbs_bizitem |
| standarditem | 定调薪项目 | BasedataField | t_hsas_calruleitementry.fstandarditemid |  | hsbs_standarditem |
| sourcename | 来源公式/项目 | TextField | — |  |  |
| totalsalary | 总薪资 | BasedataField | t_hsas_calrule.ftotalsalary | ✓ | hsbs_salaryitem |
| netsalary | 净薪资 | BasedataField | t_hsas_calrule.fnetsalary | ✓ | hsbs_salaryitem |
| prorationrule | 分段折算规则 | BasedataField | t_hsas_calrule.fprorationruleid |  | hsas_prorationrule |
| areatype | 国家/地区类型 | ComboField | t_hsas_calrule.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_calrule.fcountryid |  | bd_country |
| aftercaltype | 核算后处理类型 | ComboField | t_hsas_calrule.faftercaltype | ✓ |  |
| istrdtaxcal | 第三方算税 | CheckBoxField | t_hsas_calrule.fistrdtaxcal |  |  |
| trdtaxcalcategories | 第三方算税种类 | MulBasedataField | — | ✓ |  |
| trdlogo | 第三方 | BasedataField | t_hsas_calrule.ftrdlogoid |  | hsbs_trdlogo |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_calrule（主表） | 38 |
| t_hsas_calrule_l | 3 |
| t_hsas_calruleitementry | 12 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 14 |
