---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: W11R1282DJK
app_number: haos
app_name: HR基础组织
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# haos_adminorgdetail — 组织快速维护

**表单编码**: `haos_adminorgdetail`  
**表单ID**: `21=MGSD53K0/`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: haos_adminorgdetail（组织快速维护） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_haos_adminorg` | 主表 · 55 列 |
| `t_haos_adminorg_l` | 多语言表 · 9 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 行政组织编码 | TextField | t_haos_adminorg.fnumber | ✓ |  |
| name | 行政组织名称 | MuliLangTextField | t_haos_adminorg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_haos_adminorg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_adminorg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_adminorg.fmodifierid |  | bos_user |
| enable | 业务状态 | BillStatusField | t_haos_adminorg.fenable | ✓ |  |
| createtime | 创建时间 | CreateDateField | t_haos_adminorg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_adminorg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_adminorg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_adminorg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_adminorg_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_adminorg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_adminorg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_adminorg.fdisablerid |  | bos_user |
| disabledate | 停用日期 | DateTimeField | t_haos_adminorg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_adminorg.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_haos_adminorg.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_haos_adminorg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_haos_adminorg.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_haos_adminorg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_haos_adminorg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_haos_adminorg.fbsed |  |  |
| bsled | 业务预计失效日期 | DateField | t_haos_adminorg.fbsled |  |  |
| changedescription | 变动说明 | TextField | t_haos_adminorg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_haos_adminorg.fhisversion |  |  |
| industrytype | 行业类别 | BasedataField | t_haos_adminorg.findustrytypeid |  | hbss_industrytype |
| positioning | 定位 | MuliLangTextField | t_haos_adminorg_l.fpositioning |  |  |
| mainduty | 主要职责 | MuliLangTextField | t_haos_adminorg_l.fmainduty |  |  |
| adminorgtype | 行政组织类型 | BasedataField | t_haos_adminorg.fadminorgtypeid | ✓ | haos_adminorgtype |
| establishmentdate | 成立日期 | DateField | t_haos_adminorg.festablishmentdate |  |  |
| parentorg | 上级行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| corporateorg | 法律实体 | BasedataField | t_haos_adminorg.fcorporateorgid |  | hbss_lawentity |
| adminorgfunction | 行政组织职能 | BasedataField | t_haos_adminorg.fadminorgfunctionid |  | haos_adminorgfunction |
| companyarea | 国家/地区 | BasedataField | t_haos_adminorg.fcompanyareaid |  | bd_country |
| city | 所在城市 | BasedataField | t_haos_adminorg.fcityid |  | bd_admindivision |
| detailaddress | 详细地址 | MuliLangTextField | — |  |  |
| belongdept | 所属部门 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| belongcompany | 所属公司 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| structnumber | 组织结构编码 | TextField | t_haos_adminorg.fstructnumber |  |  |
| workplace | 工作地 | BasedataField | t_haos_adminorg.fworkplaceid |  | hbss_workplace |
| adminorglayer | 管理层级 | BasedataField | t_haos_adminorg.fadminorglayerid |  | haos_adminorglayer |
| tobedisableflag | 待停用 | CheckBoxField | t_haos_adminorg.ftobedisableflag |  |  |
| tobedisabledate | 待停用日期 | DateField | t_haos_adminorg.ftobedisabledate |  |  |
| deptlongname | 部门长名称 | TextField | — |  |  |
| companychangetype | 所属公司变化类型 | TextField | t_haos_adminorg.fcompanychangetype |  |  |
| org | 组织体系管理组织 | OrgField | t_haos_adminorg.forgid | ✓ | bos_org |
| isvirtualorg | 是否虚拟组织 | CheckBoxField | t_haos_adminorg.fisvirtualorg |  |  |
| billenable | 业务状态(集成) | BillStatusField | — |  |  |
| sortcode | 排序码 | TextField | t_haos_adminorg.fsortcode |  |  |
| structlongnumber | 组织上下级结构长编码 | TextField | t_haos_adminorg.fstructlongnumber |  |  |
| level | 物理层级 | IntegerField | t_haos_adminorg.flevel |  |  |
| isleaf | 是否叶子 | CheckBoxField | — |  |  |
| otclassify | 组织分类 | BasedataField | t_haos_adminorg.fotclassifyid | ✓ | haos_otclassify |
| disbanddate | 解散时间 | DateField | — |  |  |
| project | 所属项目 | BasedataField | — |  | bd_project |
| projectname | 项目名称 | MuliLangTextField | t_haos_adminorg_l.fprojectname |  |  |
| startdate | 项目团队生效日期 | DateField | — |  |  |
| projectidentify | 项目名称标识码 | TextField | — |  |  |
| belongadminorg | 所属行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| fdiscardcourse | 解散原因 | TextField | — |  |  |
| projectnumber | 项目编号 | TextField | — |  |  |
| orglongname | 组织长名称(废弃) | TextField | — |  |  |
| isdeleted | 是否已删除 | CheckBoxField | t_haos_adminorg.fisdeleted |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_haos_adminorg.fsourcesyskey |  |  |
| fullname | 行政组织全称 | MuliLangTextField | t_haos_adminorg_l.ffullname |  |  |
| structfullname | 结构长名称 | MuliLangTextField | t_haos_adminorg_l.fstructfullname |  |  |
| changescene | 变动场景 | BasedataField | — | ✓ | haos_changescene |
| changereason | 变动原因 | BasedataField | — |  | haos_orgchangereason |
| changetype | 变动类型 | BasedataField | — |  | haos_orgchangetype |

## 实体: cooprelentryentity（协作信息分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| coopreltype | 协作类型 | BasedataField | — | ✓ | haos_teamcoopreltype |
| cooporgteam | 协作组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| cooprelid | 协作关系ID | BigIntField | — |  |  |
| cooprelteamclass | 协作组织分类 | BasedataPropField | — |  |  |
| entryboid_coop | 协作信息分录boid | BigIntField | — |  |  |

## 实体: struct_project_entry（矩阵组织分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| struct_project | 矩阵架构 | BasedataField | — |  | haos_structproject |
| struct_parent_org | 上级组织 | BasedataField | — |  | haos_virtualorg_f7 |
| struct_long_name | 上级组织长名称 | TextField | — |  |  |
| entryboid_struct | 矩阵组织分录boid | BigIntField | — |  |  |
| parentorg_name | 上级行政组织全称 | MuliLangTextField | — |  |  |
| billid | 当前单据id | BigIntField | — |  |  |
| isroot | 是否根组织 | CheckBoxField | — |  |  |
| effectdate | 生效日期 | DateField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_adminorg（主表） | 43 |
| t_haos_adminorg_l | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 34 |
