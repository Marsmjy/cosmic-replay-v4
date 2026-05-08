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

# hsas_payrollgrp — 薪资核算组

**表单编码**: `hsas_payrollgrp`  
**表单ID**: `00LQ4ZZP8A1Q`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_payrollgrp（薪资核算组） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_payrollgrp` | 主表 · 37 列 |
| `t_hsas_payrollgrpsc` | 分录表 · 9 列 |
| `t_hsas_payrollgrp_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_payrollgrp.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_payrollgrp_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_payrollgrp.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_payrollgrp.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_payrollgrp.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_payrollgrp.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_payrollgrp.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_payrollgrp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_payrollgrp.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_payrollgrp_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_payrollgrp_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsas_payrollgrp.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_payrollgrp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_payrollgrp.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_payrollgrp.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_payrollgrp.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_payrollgrp.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_payrollgrp.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_payrollgrp.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_payrollgrp.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_payrollgrp.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_payrollgrp.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_payrollgrp.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_payrollgrp.fhisversion |  |  |

## 实体: payrollsceneentry（薪资核算场景） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| periodtype | 期间类型 | BasedataPropField | — |  |  |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_payrollgrpsc.fpayrollsceneid |  | hsas_payrollscene |
| entryboid | 分录业务ID | BigIntField | t_hsas_payrollgrpsc.fentryboid |  |  |
| payrollscenename | 编码 | BasedataPropField | — |  |  |
| calrule | 计算规则 | BasedataPropField | — |  |  |
| releasesalaryslip | 工资条发布时点 | ComboField | t_hsas_payrollgrpsc.freleasesalaryslip |  |  |
| salaryslipview | 默认工资条显示方案 | BasedataField | t_hsas_payrollgrpsc.fsalaryslipviewid |  | hsbs_salaryslipview |
| callistview | 核算名单显示方案 | BasedataField | t_hsas_payrollgrpsc.fcallistviewid |  | hsas_callistview |
| prorationcal | 分段计算 | ComboField | t_hsas_payrollgrpsc.fprorationcal | ✓ |  |
| prorationgenrule | 分段事件生成规则 | BasedataField | t_hsas_payrollgrpsc.fprorationgenruleid |  | hsas_prorationgenrule |
| lssuepayslip | 工资条发布 | ComboField | t_hsas_payrollgrpsc.flssuepayslip |  |  |
| calstepcfg | 算发薪向导方案 | BasedataField | t_hsas_payrollgrpsc.fcalstepcfgid |  | hsas_calstepcfg |
| costadaption | 人力成本维度方案 | BasedataField | — |  | lcs_costadaption |
| generalenname | 通用英文名 | TextField | t_hsas_payrollgrp.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsas_payrollgrp.fcountryid | ✓ | bd_country |
| org | 算发薪管理组织 | OrgField | t_hsas_payrollgrp.forgid | ✓ | bos_org |
| currency | 核算币种 | BasedataField | t_hsas_payrollgrp.fcurrencyid | ✓ | bd_currency |
| exratetable | 汇率表 | BasedataField | t_hsas_payrollgrp.fexratetableid | ✓ | bd_exratetable |
| islssuepayslip | 是否发布工资条 | CheckBoxField | t_hsas_payrollgrp.fislssuepayslip |  |  |
| workingplan | 公共日历 | QueryField | — |  | hrcs_workingplanquery |
| issalaryslipsum | 工资条汇总 | CheckBoxField | t_hsas_payrollgrp.fissalaryslipsum |  |  |
| payrollgrpmulsum | 工资条汇总方案 | MulBasedataField | — |  |  |
| retrodatemethod | 最早回溯日期控制方式 | ComboField | t_hsas_payrollgrp.fretrodatemethod |  |  |
| retrodate | 最早回溯日期 | DateField | t_hsas_payrollgrp.fretrodate |  |  |
| preperiodcount | 前序期间个数 | IntegerField | t_hsas_payrollgrp.fpreperiodcount |  |  |
| retrocfg | 薪资回溯结转配置 | BasedataField | t_hsas_payrollgrp.fretrocfgid |  | hsas_retrocfg |
| isenableretro | 启用回溯计算 | CheckBoxField | t_hsas_payrollgrp.fisenableretro |  |  |
| defaultpaysubject | 默认支付主体 | BasedataField | t_hsas_payrollgrp.fdefaultpaysubjectid |  | hsbs_paysubject |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_payrollgrp（主表） | 34 |
| t_hsas_payrollgrp_l | 3 |
| t_hsas_payrollgrpsc | 9 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
