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

# hcdm_stdscm — 薪酬体系

**表单编码**: `hcdm_stdscm`  
**表单ID**: `1C7V1D556ENS`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_stdscm（薪酬体系） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_stdscm` | 主表 · 34 列 |
| `t_hcdm_stdscmentry` | 分录表 · 9 列 |
| `t_hcdm_stdscm_l` | 多语言表 · 3 列 |
| `t_hcdm_stdscmentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_stdscm.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_stdscm_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_stdscm.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_stdscm.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_stdscm.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_stdscm.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_stdscm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_stdscm.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_stdscm.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_stdscm.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_stdscm.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_stdscm.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_stdscm.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_stdscm.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_stdscm.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_stdscm.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_stdscm_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_stdscm_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_stdscm.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_stdscm.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_stdscm.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcdm_stdscm.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hcdm_stdscm.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcdm_stdscm.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hcdm_stdscm.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcdm_stdscm.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcdm_stdscm.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hcdm_stdscm.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcdm_stdscm.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcdm_stdscm.fhisversion |  |  |
| coefficienttab | 薪酬标准系数表 | BasedataField | t_hcdm_stdscm.fcoefficienttabid |  | hcdm_coefficienttab |
| basedatapropfield3 | 创建组织 | BasedataPropField | — |  |  |
| basedatapropfield4 | 系数维度 | BasedataPropField | — |  |  |

## 实体: entryentity（体系设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| basedatapropfield | 创建组织 | BasedataPropField | — |  |  |
| basedatapropfield1 | 频度 | BasedataPropField | — |  |  |
| basedatapropfield2 | 薪酬标准类型 | BasedataPropField | — |  |  |
| salaryadjfilematch | 定调薪档案匹配 | ComboField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hcdm_stdscmentry.fentryboid |  |  |
| salarystandard | 薪酬标准表 | HisModelBasedataField | — |  | hcdm_salarystandard |
| salstructurent | 薪酬结构 | BasedataField | — | ✓ | hcdm_salaystructure |
| standarditem | 定调薪项目 | BasedataField | t_hcdm_stdscmentry.fstandarditemid | ✓ | hsbs_standarditem |
| excesscontrol | 超标准控制 | ComboField | t_hcdm_stdscmentry.fexcesscontrol |  |  |
| remark | 备注 | MuliLangTextField | t_hcdm_stdscmentry_l.fremark |  |  |
| appscope | 应用范围 | MulComboField | t_hcdm_stdscmentry.fappscope | ✓ |  |
| matchtype | 匹配类型 | ComboField | t_hcdm_stdscmentry.fmatchtype |  |  |
| matchstrategyid | 匹配策略 | BasedataField | t_hcdm_stdscmentry.fmatchstrategyid |  | hcdm_stdmatchstrategy |
| stdscmcoetab | 薪酬标准系数表 | MulBasedataField | — |  |  |
| dimname | 系数维度 | TextField | — |  |  |
| salaystructure | 薪酬结构 | MulBasedataField | — | ✓ |  |
| country | 国家/地区 | BasedataField | t_hcdm_stdscm.fcountryid | ✓ | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_stdscm（主表） | 29 |
| t_hcdm_stdscm_l | 3 |
| t_hcdm_stdscmentry | 6 |
| t_hcdm_stdscmentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
