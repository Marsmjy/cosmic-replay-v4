---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 14SP9N=406W2
app_number: hbpm
app_name: HR基础岗位
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hbpm_positionhis — 岗位历史查询

**表单编码**: `hbpm_positionhis`  
**表单ID**: `4SXF84BACMSW`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hbpm_positionhis（岗位历史查询） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hbpm_position` | 主表 · 57 列 |
| `t_hbpm_standposentry` | 分录表 · 3 列 |
| `t_hbpm_position_l` | 多语言表 · 11 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 岗位编码 | TextField | t_hbpm_position.fnumber | ✓ |  |
| name | 岗位名称 | MuliLangTextField | t_hbpm_position_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hbpm_position.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbpm_position.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbpm_position.fmodifierid |  | bos_user |
| enable | 业务状态 | BillStatusField | t_hbpm_position.fenable |  |  |
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
| bsed | 生效日期 | DateField | t_hbpm_position.fbsed | ✓ |  |
| bsled | 失效日期 | DateField | t_hbpm_position.fbsled |  |  |
| changedescription | 变动说明 | TextField | t_hbpm_position.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hbpm_position.fhisversion |  |  |
| enabler | 启用人 | UserField | t_hbpm_position.fenablerid |  | bos_user |
| enabledate | 启用时间 | DateTimeField | t_hbpm_position.fenabledate |  |  |
| posorientation | 岗位定位 | MuliLangTextField | t_hbpm_position_l.fposorientation |  |  |
| posduty | 岗位职责 | MuliLangTextField | t_hbpm_position_l.fposduty |  |  |
| posstandard | 岗位衡量标准 | MuliLangTextField | t_hbpm_position_l.fposstandard |  |  |
| knowledgereq | 知识要求 | MuliLangTextField | t_hbpm_position_l.fknowledgereq |  |  |
| skillreq | 技能要求 | MuliLangTextField | t_hbpm_position_l.fskillreq |  |  |
| abilityreq | 能力要求 | MuliLangTextField | t_hbpm_position_l.fabilityreq |  |  |
| experiencereq | 经验要求 | MuliLangTextField | t_hbpm_position_l.fexperiencereq |  |  |
| diplomareq | 最低学历要求 | BasedataField | t_hbpm_position.fdiplomareqid |  | hbss_diploma |
| agereq | 年龄要求 | MuliLangTextField | t_hbpm_position_l.fagereq |  |  |
| changeoperate | 变动操作 | BasedataField | — |  | hbpm_changeoperate |
| changescene | 变动场景 | BasedataField | — |  | hbpm_changescene |
| adminorg | 行政组织 | HRAdminOrgField | t_hbpm_standposentry.fadminorgid | ✓ | haos_adminorghrf7 |
| parent | 上级岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| establishmentdate | 设立日期 | DateField | t_hbpm_position.festablishmentdate |  |  |
| job | 职位 | BasedataField | t_hbpm_position.fjobid |  | hbjm_jobhr |
| positiontype | 岗位类型 | BasedataField | — |  | hbpm_positiontype |
| lowjobgrade | 最低职等 | BasedataField | t_hbpm_position.flowjobgradeid |  | hbjm_jobgradehr |
| highjobgrade | 最高职等 | BasedataField | t_hbpm_position.fhighjobgradeid |  | hbjm_jobgradehr |
| lowjoblevel | 最低职级 | BasedataField | t_hbpm_position.flowjoblevelid |  | hbjm_joblevelhr |
| highjoblevel | 最高职级 | BasedataField | t_hbpm_position.fhighjoblevelid |  | hbjm_joblevelhr |
| jobscm | 职位体系方案 | BasedataField | t_hbpm_position.fjobscmid |  | hbjm_jobscmhr |
| changetype | 变动类型 | BasedataField | — |  | hbpm_changetype |
| changedesc | 变动原因 | BasedataField | — |  | hbpm_changereason |
| countryregion | 国家地区 | BasedataField | t_hbpm_position.fcountryregionid |  | bd_country |
| workplace | 工作地 | BasedataField | t_hbpm_position.fworkplaceid |  | hbss_workplace |
| city | 所在城市 | BasedataField | t_hbpm_position.fcityid |  | bd_admindivision |
| orgdesignbu | 职位体系管理组织 | OrgField | — |  | bos_org |
| jobgraderange | 职等范围 | TextField | — |  |  |
| joblevelrange | 职级范围 | TextField | — |  |  |
| org | 组织体系管理组织 | OrgField | t_hbpm_position.forgid |  | bos_org |
| positiontpl | 岗位模板 | BasedataField | t_hbpm_position.fpositiontplid |  | hbpm_positiontpl |
| isstandardpos | 是否标准岗位 | ComboField | t_hbpm_position.fisstandardpos |  |  |
| jobseq | 职位序列 | BasedataPropField | — |  |  |
| jobfamily | 职位族 | BasedataPropField | — |  |  |
| jobclass | 职位类 | BasedataPropField | — |  |  |
| jobgradescm | 职等方案 | BasedataField | t_hbpm_position.fjobgradescmid |  | hbjm_jobgradescmhr |
| joblevelscm | 职级方案 | BasedataField | t_hbpm_position.fjoblevelscmid |  | hbjm_joblevelscmhr |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| reporttype | 协作类型 | BasedataField | — | ✓ | hbpm_reportcoreltype |
| targetpos | 协作岗位 | HRPositionField | — | ✓ | hbpm_positionhrf7 |
| targetposnumber | 协作岗位编码 | BasedataPropField | — |  |  |
| targetposorg | 协作岗位所在行政组织 | BasedataPropField | — |  |  |
| targetposorgnumber | 协作岗位所在行政组织编码 | BasedataPropField | — |  |  |
| entryboidrel | 分录bo | BigIntField | — |  |  |

## 实体: applicableorgentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| applicableorg | 行政组织名称 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| basedatapropfield | 行政组织编码 | BasedataPropField | — |  |  |
| iscontainsu | 是否包含下级 | CheckBoxField | — |  |  |
| entryboid | boid | BigIntField | t_hbpm_standposentry.fentryboid |  |  |
| isleader | 主负责岗 | CheckBoxField | t_hbpm_position.fisleader |  |  |
| isdeleted | 是否已删除 | CheckBoxField | t_hbpm_position.fisdeleted |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_hbpm_position.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbpm_position（主表） | 43 |
| t_hbpm_position_l | 11 |
| t_hbpm_standposentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
