# hrcs_projempstrategy — 项目团队-员工管理关系策略设置

**表单编码**: `hrcs_projempstrategy`  
**表单ID**: `2H4Q8+VDA4I/`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_projempstrategy（项目团队-员工管理关系策略设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_projempstrategy` | BaseEntity | 主表 |
| `t_hrcs_projempstrentry` | EntryEntity |  |

### 字段列表 — t_hrcs_projempstrategy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_projempstrategy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_projempstrategy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_projempstrategy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_projempstrategy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_projempstrategy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_projempstrategy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_projempstrategy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_projempstrategy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_projempstrategy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_projempstrategy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_projempstrategy.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_projempstrategy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_projempstrategy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_projempstrategy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_projempstrategy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_projempstrategy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_projempstrategy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_projempstrategy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_projempstrategy.foriname |  |  |
| inheritedorg | 参照行政组织/项目团队 | HRAdminOrgField | t_hrcs_projempstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| effdt | 生效日期 | DateField | t_hrcs_projempstrategy.feffdt | ✓ |  |
| defstrategytype | 默认策略 | BasedataField | t_hrcs_projempstrategy.fstrategyid |  | hrcs_strategy |
| leffdt | 失效日期 | DateField | t_hrcs_projempstrategy.fleffdt |  |  |
| sourceorg | 源策略组织 | HRAdminOrgField | t_hrcs_projempstrategy.fsourceorgid |  | haos_adminorghrf7 |
| hrbu | 业务单元 | OrgField | — |  | bos_org |
| orgteam | 组织团队 | HRAdminOrgField | t_hrcs_projempstrategy.forgteamid |  | haos_adminorghrf7 |
| bussinessfield | 业务类型关系 | BasedataField | t_hrcs_projempstrategy.fbussinessfieldid |  | hrcs_bussinesstype |
| entryhrbu | 默认HR管理组织 | OrgField | t_hrcs_projempstrategy.fhrbuid | ✓ | bos_org |
| entryinheritedorg | 参照行政组织/项目团队 | HRAdminOrgField | t_hrcs_projempstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| entryenable | 业务状态 | ComboField | t_hrcs_projempstrategy.fenable |  |  |
| entrydefstrategy | 策略 | BasedataField | t_hrcs_projempstrategy.fstrategyid | ✓ | hrcs_strategy |
| entryorgteam | 分录组织团队 | HRAdminOrgField | t_hrcs_projempstrategy.forgteamid |  | haos_adminorghrf7 |
| entryeffdt | 生效日期 | DateField | t_hrcs_projempstrategy.feffdt | ✓ |  |
| strategyentrytype | 策略来源 | ComboField | t_hrcs_projempstrategy.fstrategyentrytype |  |  |
| entryleffdt | 失效日期 | DateField | t_hrcs_projempstrategy.fleffdt |  |  |
| entrysourceorg | 源策略组织 | HRAdminOrgField | t_hrcs_projempstrategy.fsourceorgid |  | haos_adminorghrf7 |
| sourceentry | 源分录内码 | BigIntField | t_hrcs_projempstrategy.fsourceentryid |  |  |
| includerange | 适用范围 | TextField | — |  |  |
| entrychangetype | 变更类型 | ComboField | t_hrcs_projempstrategy.fentrychangetype |  |  |
| changetype | 变更类型 | ComboField | t_hrcs_projempstrategy.fchangetype |  |  |
| belongadminorg | 所属行政组织 | HRAdminOrgField | t_hrcs_projempstrategy.fbelongadminorg |  | haos_adminorghrf7 |
|  |  | EntryEntity | → t_hrcs_projempstrentry |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_projempstrategy（主表） | 41 |
| 无数据库列 | 2 |

