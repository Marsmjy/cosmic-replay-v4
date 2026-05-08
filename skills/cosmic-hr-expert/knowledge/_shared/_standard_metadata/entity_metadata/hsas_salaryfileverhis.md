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

# hsas_salaryfileverhis — 薪资档案历史

**表单编码**: `hsas_salaryfileverhis`  
**表单ID**: `4ENA1OJT/U22`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_salaryfileverhis（薪资档案历史） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_salaryfile` | 主表 · 39 列 |
| `t_hsas_salaryfile_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 档案编号 | TextField | t_hsas_salaryfile.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hsas_salaryfile_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_salaryfile.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_salaryfile.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_salaryfile.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_salaryfile.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_salaryfile.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_salaryfile.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_salaryfile.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_salaryfile_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_salaryfile_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_salaryfile.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_salaryfile.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_salaryfile.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_salaryfile.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| hisversion | 版本号 | TextField | t_hsas_salaryfile.fhisversion |  |  |
| changedescription | 变更说明 | TextField | t_hsas_salaryfile.fchangedescription |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_salaryfile.forgid | ✓ | bos_org |
| payrollregion | 发薪管理属地 | BasedataField | t_hsas_salaryfile.fpayrollregionid | ✓ | bd_country |
| payrollgroup | 薪资核算组 | HisModelBasedataField | — | ✓ | hsas_payrollgrp |
| adminorg | 管理部门 | HRAdminOrgField | t_hsas_salaryfile.fadminorgid | ✓ | haos_adminorghrf7 |
| adminorgvid | 历史行政组织 | HRAdminOrgField | t_hsas_salaryfile.fadminorgvid |  | haos_adminorghrf7 |
| empgroup | 薪资档案分组 | BasedataField | t_hsas_salaryfile.fempgroupid | ✓ | hbss_empgroup |
| isescrowstaff | 代管员工 | CheckBoxField | t_hsas_salaryfile.fisescrowstaff |  |  |
| salarycalcstyle | 算发薪方式 | BasedataField | t_hsas_salaryfile.fsalarycalcstyleid | ✓ | hsas_salarycalcstyle |
| paystatus | 算薪状态 | ComboField | t_hsas_salaryfile.fpaystatus | ✓ |  |
| bsed | 生效日期 | DateField | t_hsas_salaryfile.fbsed | ✓ |  |
| bsled | 失效日期 | DateField | t_hsas_salaryfile.fbsled |  |  |
| changereason | 变动原因 | BasedataField | t_hsas_salaryfile.fchangereasonid |  | hsbs_changereason |
| addtaxfilecheckbox | 同时创建个税档案 | CheckBoxField | t_hsas_salaryfile.faddtaxfilecheckbox | ✓ |  |
| salaryfile | 薪资档案 | BasedataField | t_hsas_salaryfile.fsalaryfileid |  | hsas_salaryfile |
| boid | 业务ID | BigIntField | t_hsas_salaryfile.fboid |  |  |
| iscurrentversion | 是否当前版本 | CheckBoxField | t_hsas_salaryfile.fiscurrentversion |  |  |
| datastatus | 版本状态 | ComboField | t_hsas_salaryfile.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_salaryfile.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_salaryfile.ffirstbsed |  |  |
| employee | 计薪人员 | BasedataField | t_hsas_salaryfile.femployeeid |  | hsbs_employee |
| assignment | 组织分配 | QueryField | — | ✓ | hsbs_assignmentquery |
| empposorgrel | 员工任职 | QueryField | — | ✓ | hsbs_empposf7query |
| empadminorg | 行政组织 | QueryPropField | — |  |  |
| position | 岗位 | QueryPropField | — |  |  |
| job | 职位 | QueryPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_salaryfile（主表） | 33 |
| t_hsas_salaryfile_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
