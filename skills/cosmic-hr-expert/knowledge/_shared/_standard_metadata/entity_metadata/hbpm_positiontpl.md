# hbpm_positiontpl — 岗位模板

**表单编码**: `hbpm_positiontpl`  
**表单ID**: `3FWXN363Y23+`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_positiontpl（岗位模板） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbpm_positiontpl` | BaseEntity | 主表 |
| `t_hbpm_applicationscope` | EntryEntity | 适用范围 |
| `t_hbpm_exceptionscope` | EntryEntity | 例外组织 |
| `t_hbpm_postplfieldrange` | MulEmployeeField子表 | 字段范围 |

### 字段列表 — t_hbpm_positiontpl（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbpm_positiontpl.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbpm_positiontpl.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbpm_positiontpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbpm_positiontpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbpm_positiontpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbpm_positiontpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbpm_positiontpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbpm_positiontpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbpm_positiontpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbpm_positiontpl.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbpm_positiontpl.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbpm_positiontpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbpm_positiontpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbpm_positiontpl.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbpm_positiontpl.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbpm_positiontpl.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbpm_positiontpl.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbpm_positiontpl.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbpm_positiontpl.foriname |  |  |
| posttpltype | 岗位模板类型 | BasedataField | t_hbpm_positiontpl.fposttpltypeid |  | hbpm_positiontpltype |
| jobscm | 职位体系方案 | BasedataField | t_hbpm_positiontpl.fjobscmid |  | hbjm_jobscmhr |
| job | 职位 | HisModelBasedataField | t_hbpm_positiontpl.fjobid |  | hbjm_jobhr |
| jobgradescm | 职等方案 | HisModelBasedataField | t_hbpm_positiontpl.fjobgradescmid |  | hbjm_jobgradescmhr |
| jobgraderange | 职等范围 | TextField | — |  |  |
| joblevelscm | 职级方案 | HisModelBasedataField | t_hbpm_positiontpl.fjoblevelscmid |  | hbjm_joblevelscmhr |
| joblevelrange | 职级范围 | TextField | — |  |  |
| lowjoblevel | 最低职级 | BasedataField | t_hbpm_positiontpl.flowjoblevelid |  | hbjm_joblevelhr |
| highjoblevel | 最高职级 | BasedataField | t_hbpm_positiontpl.fhighjoblevelid |  | hbjm_joblevelhr |
| lowjobgrade | 最低职等 | BasedataField | t_hbpm_positiontpl.flowjobgradeid |  | hbjm_jobgradehr |
| highjobgrade | 最高职等 | BasedataField | t_hbpm_positiontpl.fhighjobgradeid |  | hbjm_jobgradehr |
| org | 创建组织 | OrgField | t_hbpm_positiontpl.forgid | ✓ | bos_org |
| positiontype | 岗位类型 | BasedataField | t_hbpm_positiontpl.fpositiontypeid |  | hbpm_positiontype |
| ablemodifyfield | 通过模板创建岗位时，可修改来源于模板字段 | CheckBoxField | t_hbpm_positiontpl.fablemodifyfield |  |  |
| fieldrange | 字段范围 | MulBasedataField | t_hbpm_postplfieldrange（子表） |  |  |
| updatestrategygroup | 单选按钮组 | RadioGroupField | t_hbpm_positiontpl.fupdatestrategy |  |  |
| noralation | 不刷新关联岗位 | RadioField | — |  |  |
| amentdent | 修订刷新关联岗位 | RadioField | — |  |  |
| change | 变更刷新关联岗位 | RadioField | — |  |  |
| issetscope | 是否设置适用范围 | CheckBoxField | t_hbpm_positiontpl.fissetscope |  |  |
| orgdesignbu | 职位体系管理组织 | OrgField | — |  | bos_org |
| applicationscope | 适用范围 | EntryEntity | → t_hbpm_applicationscope |  |  |
| exceptionscope | 例外组织 | EntryEntity | → t_hbpm_exceptionscope |  |  |

### 字段列表 — t_hbpm_applicationscope（适用范围·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorg | 行政组织 | HRAdminOrgField | t_hbpm_applicationscope.fadminorgid |  | haos_adminorghrf7 |
| containssubordinate | 是否包含下级 | CheckBoxField | t_hbpm_applicationscope.fcontainssubordinate |  |  |

### 字段列表 — t_hbpm_exceptionscope（例外组织·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| exceptionadminorg | 行政组织 | HRAdminOrgField | t_hbpm_exceptionscope.fexceptionadminorgid |  | haos_adminorghrf7 |
| excontainssubordinate | 是否包含下级 | CheckBoxField | t_hbpm_exceptionscope.fexcontainssubordinate |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbpm_positiontpl（主表） | 40 |
| t_hbpm_applicationscope（适用范围） | 2 |
| t_hbpm_exceptionscope（例外组织） | 2 |
| t_hbpm_postplfieldrange（MulEmployeeField子表） | 1 |
| 无数据库列 | 6 |

