---
source: openapi_runtime
extracted_at: 2026-04-27
extractor: build_standard_metadata_md_from_openapi.py
app_id: 14SP9N=406W2
app_number: hbpm
app_name: HR基础岗位
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hbpm_stposition — 标准岗位

**表单编码**: `hbpm_stposition`  
**表单ID**: `24BOER/9R4/9`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hbpm_stposition（标准岗位） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hbpm_position` | 主表 · 36 列 |
| `t_hbpm_standposentry` | 分录表 · 3 列 |
| `t_hbpm_position_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 标准岗位编码 | TextField | t_hbpm_position.fnumber | ✓ |  |
| name | 标准岗位名称 | MuliLangTextField | t_hbpm_position_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hbpm_position.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbpm_position.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbpm_position.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbpm_position.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbpm_position.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbpm_position.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbpm_position.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbpm_position_l.fsimplename |  |  |
| description | 备注 | MuliLangTextField | t_hbpm_position_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbpm_position.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbpm_position.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbpm_position.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbpm_position.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbpm_position.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hbpm_position.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hbpm_position.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hbpm_position.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hbpm_position.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hbpm_position.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hbpm_position.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hbpm_position.fbsled |  |  |
| changedescription | 变更说明 | TextField | — |  |  |
| hisversion | 版本号 | TextField | t_hbpm_position.fhisversion |  |  |
| org | 组织体系管理组织 | OrgField | t_hbpm_position.forgid | ✓ | bos_org |
| jobscm | 职位体系方案 | BasedataField | t_hbpm_position.fjobscmid |  | hbjm_jobscmhr |
| jobgraderange | 职等范围 | TextField | — |  |  |
| joblevelrange | 职级范围 | TextField | — |  |  |
| joblevelscm | 职级方案 | HisModelBasedataField | — |  | hbjm_joblevelscmhr |
| positiontype | 岗位类型 | BasedataField | — |  | hbpm_positiontype |

## 实体: applicableorgentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| applicableorg | 行政组织名称 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| iscontainsu | 包含下级 | CheckBoxField | — |  |  |
| basedatapropfield | 行政组织编码 | BasedataPropField | — |  |  |
| entryboid | boid | BigIntField | t_hbpm_standposentry.fentryboid |  |  |
| lowjoblevel | 最低职级 | BasedataField | t_hbpm_position.flowjoblevelid |  | hbjm_joblevelhr |
| highjoblevel | 最高职级 | BasedataField | t_hbpm_position.fhighjoblevelid |  | hbjm_joblevelhr |
| lowjobgrade | 最低职等 | BasedataField | t_hbpm_position.flowjobgradeid |  | hbjm_jobgradehr |
| highjobgrade | 最高职等 | BasedataField | t_hbpm_position.fhighjobgradeid |  | hbjm_jobgradehr |
| jobseq | 职位序列 | BasedataPropField | — |  |  |
| jobfamily | 职位族 | BasedataPropField | — |  |  |
| jobclass | 职位类 | BasedataPropField | — |  |  |
| orgdesignbu | 职位体系管理组织 | OrgField | — |  | bos_org |
| job | 职位 | HisModelBasedataField | — |  | hbjm_jobhr |
| jobgradescm | 职等方案 | HisModelBasedataField | — |  | hbjm_jobgradescmhr |
| isstandardpos | 是否标准岗位 | ComboField | t_hbpm_position.fisstandardpos |  |  |
| adminorg | 行政组织 | HRAdminOrgField | t_hbpm_standposentry.fadminorgid | ✓ | haos_adminorghrf7 |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbpm_position（主表） | 28 |
| t_hbpm_position_l | 3 |
| t_hbpm_standposentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
