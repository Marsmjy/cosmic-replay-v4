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

# hsas_calpayrolltask — 薪资核算任务

**表单编码**: `hsas_calpayrolltask`  
**表单ID**: `02GD+L1MBO31`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calpayrolltask（薪资核算任务） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_calpayrolltask` | 主表 · 48 列 |
| `t_hsas_calpayrolltask_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_calpayrolltask.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_calpayrolltask_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_calpayrolltask.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_calpayrolltask.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_calpayrolltask.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_calpayrolltask.fenable |  |  |
| createtime | 任务创建时间 | CreateDateField | t_hsas_calpayrolltask.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_calpayrolltask.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_calpayrolltask.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_calpayrolltask_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_calpayrolltask_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_calpayrolltask.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_calpayrolltask.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_calpayrolltask.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_calpayrolltask.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| tasktype | 任务类型 | ComboField | t_hsas_calpayrolltask.ftasktype | ✓ |  |
| calcount | 核算次数 | IntegerField | t_hsas_calpayrolltask.fcalcount |  |  |
| payrolldate | 薪资所属年月 | DateField | t_hsas_calpayrolltask.fpayrolldate | ✓ |  |
| daterange | 薪资起止日期 | DateRangeField | — | ✓ |  |
| calpersoncount | 计薪人数 | IntegerField | t_hsas_calpayrolltask.fcalpersoncount |  |  |
| exratedate | 汇率日期 | DateField | t_hsas_calpayrolltask.fexratedate | ✓ |  |
| viewdetailfield | 查看图标 | MulComboField | — |  |  |
| tasknewperson | 任务创建人 | TextField | — |  |  |
| tasknewmethod | 任务创建方式 | ComboField | — |  |  |
| tasknewtplhis | 任务创建模板 | BasedataField | — |  | hsas_caltasknewtpl |
| payrollyearshow | 薪资所属年份 | DateField | — | ✓ |  |
| handlestate | 分段处理状态 | ComboField | — |  |  |
| payrollyear | 薪资所属年 | IntegerField | — |  |  |
| payrollmonth | 薪资所属月 | IntegerField | — |  |  |
| paydate | 预计支付日期 | DateField | — | ✓ |  |
| calversionno | 核算版本号 | TextField | t_hsas_calpayrolltask.fcalversionno |  |  |
| currency | 核算币种 | BasedataPropField | — |  |  |
| exratetable | 汇率表 | BasedataPropField | — |  |  |
| isautoaddperson | 自动添加核算人员 | CheckBoxField | — |  |  |
| period | 期间 | BasedataField | — | ✓ | hsbs_calperiod |
| tracker | 任务跟踪人 | MulBasedataField | — |  |  |
| aftercal | 核算后处理方式 | MulComboField | t_hsas_calpayrolltask.faftercal |  |  |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_calpayrolltask.fpayrollsceneid | ✓ | hsas_payrollscene |
| isautoprorationtask | 自动确认分段任务 | CheckBoxField | t_hsas_calpayrolltask.fisautoprorationtask |  |  |
| periodtype | 期间类型 | BasedataField | — |  | hsbs_calperiodtype |
| payrollscenev | 薪资核算场景版本 | BasedataField | t_hsas_calpayrolltask.fpayrollscenevid | ✓ | hsas_payrollscene |
| calfrequency | 频度 | BasedataField | — |  | hsbs_calfrequency |
| country | 国家/地区 | BasedataField | — |  | bd_country |
| org | 算发薪管理组织 | OrgField | t_hsas_calpayrolltask.forgid | ✓ | bos_org |
| callistviewv | 核算名单显示方案 | HisModelBasedataField | — | ✓ | hsas_callistview |
| calrule | 计算规则 | HisModelBasedataField | — | ✓ | hsas_calrule |
| calrulev | 计算规则版本 | HisModelBasedataField | — | ✓ | hsas_calrule |
| payrollgroup | 薪资核算组 | HisModelBasedataField | — | ✓ | hsas_payrollgrp |
| payrollgroupv | 薪资核算组版本 | HisModelBasedataField | — | ✓ | hsas_payrollgrp |
| taskstatus | 任务状态 | ComboField | — |  |  |
| taxitemschemev | 个税项目映射方案版本 | BasedataField | — |  | hsbs_taxprojscheme |
| ishandleproration | 分段计算 | ComboField | t_hsas_calpayrolltask.fishandleproration | ✓ |  |
| stepicon | 进入向导 | ComboField | — |  |  |
| hide | 是否隐藏 | CheckBoxField | t_hsas_calpayrolltask.fhide |  |  |
| attdaterange | 考勤起止日期 | DateRangeField | — |  |  |
| schemeaftercal | 核算后处理方案 | BasedataPropField | — |  |  |
| islimitadd | 追加核算名单限制 | CheckBoxField | t_hsas_calpayrolltask.fislimitadd |  |  |
| retrodate | 最早回溯日期限制 | DateField | t_hsas_calpayrolltask.fretrodate |  |  |
| isenableretro | 启用回溯计算 | CheckBoxField | t_hsas_calpayrolltask.fisenableretro |  |  |
| isretrotask | 是否回溯任务 | CheckBoxField | t_hsas_calpayrolltask.fisretrotask |  |  |
| calstepcfg | 算发薪向导方案 | BasedataField | t_hsas_calpayrolltask.fcalstepcfgid |  | hsas_calstepcfg |
| calicon | 计算中图标 | ComboField | — |  |  |
| retrotasktype | 回溯任务类型 | ComboField | t_hsas_calpayrolltask.fretrotasktype |  |  |
| taxdaterange | 税款所属期 | DateRangeField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_calpayrolltask（主表） | 31 |
| t_hsas_calpayrolltask_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 34 |
