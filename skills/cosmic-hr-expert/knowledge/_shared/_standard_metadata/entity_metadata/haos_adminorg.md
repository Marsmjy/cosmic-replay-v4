# haos_adminorg — 组织基本信息（主）

**表单编码**: `haos_adminorg`  
**表单ID**: `4S6B8I6U+EBN`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_adminorg（组织基本信息（主）） [BaseEntity]

### 物理表（垂直拆分表）

| 表名 | 说明 |
|------|------|
| `t_haos_adminorg` | 主表 |
| `t_haos_adminorg_a` | 行政组织扩展表 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 组织编码 | TextField | t_haos_adminorg.fnumber |  |  |
| name | 组织名称 | MuliLangTextField | t_haos_adminorg.fname |  |  |
| status | 数据状态 | BillStatusField | t_haos_adminorg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_adminorg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_adminorg.fmodifierid |  | bos_user |
| enable | 业务状态 | BillStatusField | t_haos_adminorg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_adminorg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_adminorg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_adminorg.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_adminorg.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_adminorg.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_adminorg.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_adminorg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_adminorg.FDisablerID |  | bos_user |
| disabledate | 停用日期 | DateTimeField | t_haos_adminorg.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_adminorg.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_haos_adminorg.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_haos_adminorg.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_haos_adminorg.foriname |  |  |
| boid | 业务ID | BigIntField | t_haos_adminorg.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_haos_adminorg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_haos_adminorg.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_haos_adminorg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_haos_adminorg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_haos_adminorg.fbsed |  |  |
| bsled | 业务预计失效日期 | DateField | t_haos_adminorg.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_haos_adminorg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_haos_adminorg.fhisversion |  |  |
| industrytype | 行业类别 | BasedataField | t_haos_adminorg.findustrytypeid |  | hbss_industrytype |
| positioning | 定位 | MuliLangTextField | t_haos_adminorg.fpositioning |  |  |
| mainduty | 主要职责 | MuliLangTextField | t_haos_adminorg.fmainduty |  |  |
| adminorgtype | 行政组织类型 | BasedataField | t_haos_adminorg.fadminorgtypeid |  | haos_adminorgtype |
| establishmentdate | 成立日期 | DateField | t_haos_adminorg.festablishmentdate | ✓ |  |
| parentorg | 上级行政组织 | HRAdminOrgField | t_haos_adminorg.fparentid |  | haos_adminorghrf7 |
| corporateorg | 法律实体 | BasedataField | t_haos_adminorg.fcorporateorgid |  | hbss_lawentity |
| adminorgfunction | 行政组织职能 | BasedataField | t_haos_adminorg.fadminorgfunctionid |  | haos_adminorgfunction |
| companyarea | 国家/地区 | BasedataField | t_haos_adminorg.fcompanyareaid |  | bd_country |
| city | 所在城市 | BasedataField | t_haos_adminorg.fcityid |  | bd_admindivision |
| detailaddress | 详细地址 | MuliLangTextField | t_haos_adminorg.fbusinessaddress |  |  |
| belongdept | 所属部门 | HRAdminOrgField | t_haos_adminorg.fdepartmentid |  | haos_adminorghrf7 |
| belongcompany | 所属公司 | HRAdminOrgField | t_haos_adminorg.fcompanyid |  | haos_adminorghrf7 |
| structnumber | 组织结构编码 | TextField | t_haos_adminorg.fstructnumber |  |  |
| workplace | 工作地 | BasedataField | t_haos_adminorg.fworkplaceid |  | hbss_workplace |
| adminorglayer | 管理层级 | BasedataField | t_haos_adminorg.fadminorglayerid |  | haos_adminorglayer |
| tobedisableflag | 待停用 | CheckBoxField | t_haos_adminorg.ftobedisableflag |  |  |
| tobedisabledate | 待停用日期 | DateField | t_haos_adminorg.ftobedisabledate |  |  |
| deptlongname | 部门长名称 | TextField | — |  |  |
| companychangetype | 所属公司变化类型 | TextField | t_haos_adminorg.fcompanychangetype |  |  |
| org | 组织体系管理组织 | OrgField | t_haos_adminorg.forgid |  | bos_org |
| isvirtualorg | 是否虚拟组织 | CheckBoxField | t_haos_adminorg.fisvirtualorg |  |  |
| billenable | 业务状态(集成) | BillStatusField | — |  |  |
| sortcode | 排序码 | TextField | t_haos_adminorg.fsortcode |  |  |
| structlongnumber | 组织上下级结构长编码 | TextField | t_haos_adminorg.fstructlongnumber |  |  |
| level | 物理层级 | IntegerField | t_haos_adminorg.flevel |  |  |
| isleaf | 是否叶子 | CheckBoxField | — |  |  |
| otclassify | 组织分类 | BasedataField | t_haos_adminorg.fotclassifyid |  | haos_otclassify |
| disbanddate | 解散时间 | DateField | t_haos_adminorg_a.fdisbanddate |  |  |
| project | 所属项目 | BasedataField | t_haos_adminorg_a.fprojectid |  | bd_project |
| projectname | 项目名称 | MuliLangTextField | t_haos_adminorg.fprojectname |  |  |
| startdate | 项目团队生效日期 | DateField | t_haos_adminorg_a.fstartdate |  |  |
| projectidentify | 项目名称标识码 | TextField | t_haos_adminorg_a.fprojectidentify |  |  |
| belongadminorg | 所属行政组织 | HRAdminOrgField | t_haos_adminorg_a.fbelongadminorgid |  | haos_adminorghrf7 |
| fdiscardcourse | 解散原因 | TextField | t_haos_adminorg_a.fdiscardcourse |  |  |
| projectnumber | 项目编号 | TextField | t_haos_adminorg_a.fprojectnumber |  |  |
| orglongname | 所属长名称 | TextField | — |  |  |
| isdeleted | 是否已删除 | CheckBoxField | t_haos_adminorg.fisdeleted |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_haos_adminorg.fsourcesyskey |  |  |
| fullname | 行政组织全称 | MuliLangTextField | t_haos_adminorg.ffullname |  |  |
| structfullname | 结构长名称 | MuliLangTextField | t_haos_adminorg.fstructfullname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_adminorg（主表） | 58 |
| t_haos_adminorg_a | 7 |
| 无数据库列（RadioField/废弃/虚拟） | 4 |

