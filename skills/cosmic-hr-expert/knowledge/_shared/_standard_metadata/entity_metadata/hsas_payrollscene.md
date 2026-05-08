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

# hsas_payrollscene — 薪资核算场景

**表单编码**: `hsas_payrollscene`  
**表单ID**: `233=U88S3R3Z`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_payrollscene（薪资核算场景） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_payrollscene` | 主表 · 47 列 |
| `t_hsas_calborderentry` | 分录表 · 3 列 |
| `t_hsas_payrollscene_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_payrollscene.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_payrollscene_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_payrollscene.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_payrollscene.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_payrollscene.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_payrollscene.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_payrollscene.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_payrollscene.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_payrollscene.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_payrollscene_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_payrollscene_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_payrollscene.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_payrollscene.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_payrollscene.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_payrollscene.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_payrollscene.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_payrollscene.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_payrollscene.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_payrollscene.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_payrollscene.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_payrollscene.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_payrollscene.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_payrollscene.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_payrollscene.fhisversion |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_payrollscene.forgid | ✓ | bos_org |
| generalenname | 通用英文名 | TextField | t_hsas_payrollscene.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsas_payrollscene.fcountryid | ✓ | bd_country |
| periodtype | 期间类型 | BasedataField | t_hsas_payrollscene.fperiodtypeid | ✓ | hsbs_calperiodtype |
| calfrequency | 期间频度 | BasedataPropField | — |  |  |
| calrule | 计算规则 | BasedataField | t_hsas_payrollscene.fcalruleid | ✓ | hsas_calrule |
| aftercal | 核算后处理方式 | MulComboField | t_hsas_payrollscene.faftercal |  |  |

## 实体: entryentity（核算名单范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salarycalcstyle | 算发薪方式 | BasedataField | t_hsas_calborderentry.fsalarycalcstyleid | ✓ | hsas_salarycalcstyle |
| attachcondition | 附加条件 | ComboField | t_hsas_calborderentry.fattachcondition | ✓ |  |
| calbordermulbd | 业务项目 | MulBasedataField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_calborderentry.fentryboid |  |  |
| callistview | 核算名单显示方案 | HisModelBasedataField | — | ✓ | hsas_callistview |
| callistrule | 核算名单规则 | HisModelBasedataField | — |  | hsas_callistrule |
| releasesalaryslip | 工资条发布时点 | ComboField | t_hsas_payrollscene.freleasesalaryslip | ✓ |  |
| salaryslipview | 默认工资条显示方案 | BasedataField | t_hsas_payrollscene.fsalaryslipviewid | ✓ | hsbs_salaryslipview |
| lssuepayslip | 工资条发布 | CheckBoxField | t_hsas_payrollscene.flssuepayslip |  |  |
| isenableatt | 启用考勤 | CheckBoxField | t_hsas_payrollscene.fisenableatt |  |  |
| matchattperiod | 匹配考勤数据期间 | ComboField | t_hsas_payrollscene.fmatchattperiod |  |  |
| schemeaftercal | 核算后处理方案 | BasedataField | t_hsas_payrollscene.fschemeaftercalid | ✓ | hsas_schemeaftercal |
| genbizdata | 业务数据生成配置 | BasedataField | t_hsas_payrollscene.fgenbizdataid |  | hsas_genbizdata |
| aftercaltype | 核算后处理类型 | ComboField | t_hsas_payrollscene.faftercaltype | ✓ |  |
| islimitadd | 追加核算名单限制 | CheckBoxField | t_hsas_payrollscene.fislimitadd |  |  |
| targetscene | 结转到的薪资核算场景 | BasedataField | t_hsas_payrollscene.ftargetsceneid |  | hsas_payrollscene |
| groupcontent | 核算名单范围 | TextAreaField | t_hsas_payrollscene.fgroupcontent |  |  |
| chooseindex | 选择的序号 | IntegerField | — |  |  |
| isenableretro | 启用回溯计算 | CheckBoxField | t_hsas_payrollscene.fisenableretro |  |  |
| isothersceneretro | 结转到其他场景 | CheckBoxField | t_hsas_payrollscene.fisothersceneretro |  |  |
| calstepcfg | 算发薪向导方案 | BasedataField | t_hsas_payrollscene.fcalstepcfgid |  | hsas_calstepcfg |
| retrotasktype | 回溯任务类型 | ComboField | t_hsas_payrollscene.fretrotasktype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_payrollscene（主表） | 42 |
| t_hsas_calborderentry | 3 |
| t_hsas_payrollscene_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
