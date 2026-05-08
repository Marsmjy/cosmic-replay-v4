# hrcs_projorgstrategy — 项目团队-组织管理关系策略设置

**表单编码**: `hrcs_projorgstrategy`  
**表单ID**: `2H4QA4SYFXCC`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_projorgstrategy（项目团队-组织管理关系策略设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_projorgstrategy` | BaseEntity | 主表 |
| `t_hrcs_projorgstrentry` | EntryEntity |  |

### 字段列表 — t_hrcs_projorgstrategy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_projorgstrategy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_projorgstrategy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_projorgstrategy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_projorgstrategy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_projorgstrategy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_projorgstrategy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_projorgstrategy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_projorgstrategy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_projorgstrategy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_projorgstrategy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_projorgstrategy.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_projorgstrategy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_projorgstrategy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_projorgstrategy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_projorgstrategy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_projorgstrategy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_projorgstrategy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_projorgstrategy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_projorgstrategy.foriname |  |  |
| inheritedorg | 参照行政组织/项目团队 | HRAdminOrgField | t_hrcs_projorgstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| effdt | 生效日期 | DateField | t_hrcs_projorgstrategy.feffdt | ✓ |  |
| defstrategytype | 默认策略 | BasedataField | t_hrcs_projorgstrategy.fstrategyid |  | hrcs_strategy |
| leffdt | 失效日期 | DateField | t_hrcs_projorgstrategy.fleffdt |  |  |
| sourceorg | 源策略组织 | HRAdminOrgField | t_hrcs_projorgstrategy.fsourceorgid |  | haos_adminorghrf7 |
| hrbu | 业务单元 | OrgField | — |  | bos_org |
| orgteam | 组织团队 | HRAdminOrgField | t_hrcs_projorgstrategy.forgteamid |  | haos_adminorghrf7 |
| bussinessfield | 业务类型关系 | BasedataField | t_hrcs_projorgstrategy.fbussinessfieldid |  | hrcs_bussinesstype |
| entryhrbu | 默认HR管理组织 | OrgField | t_hrcs_projorgstrategy.fhrbuid | ✓ | bos_org |
| entryinheritedorg | 参照行政组织/项目团队 | HRAdminOrgField | t_hrcs_projorgstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| entryenable | 业务状态 | ComboField | t_hrcs_projorgstrategy.fenable |  |  |
| entrydefstrategy | 策略 | BasedataField | t_hrcs_projorgstrategy.fstrategyid | ✓ | hrcs_strategy |
| entryorgteam | 分录组织团队 | HRAdminOrgField | t_hrcs_projorgstrategy.forgteamid |  | haos_adminorghrf7 |
| entryeffdt | 生效日期 | DateField | t_hrcs_projorgstrategy.feffdt | ✓ |  |
| strategyentrytype | 策略来源 | ComboField | t_hrcs_projorgstrategy.fstrategyentrytype |  |  |
| entryleffdt | 失效日期 | DateField | t_hrcs_projorgstrategy.fleffdt |  |  |
| entrysourceorg | 源策略组织 | HRAdminOrgField | t_hrcs_projorgstrategy.fsourceorgid |  | haos_adminorghrf7 |
| sourceentry | 源分录内码 | BigIntField | t_hrcs_projorgstrategy.fsourceentryid |  |  |
| entrychangetype | 变更类型 | ComboField | t_hrcs_projorgstrategy.fentrychangetype |  |  |
| changetype | 变更类型 | ComboField | t_hrcs_projorgstrategy.fchangetype |  |  |
| belongadminorg | 所属行政组织 | HRAdminOrgField | t_hrcs_projorgstrategy.fbelongadminorg |  | haos_adminorghrf7 |
|  |  | EntryEntity | → t_hrcs_projorgstrentry |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_projorgstrategy（主表） | 40 |
| 无数据库列 | 1 |

