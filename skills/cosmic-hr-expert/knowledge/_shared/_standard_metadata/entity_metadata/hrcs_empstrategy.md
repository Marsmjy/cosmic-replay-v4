# hrcs_empstrategy — 行政组织-员工管理关系策略设置

**表单编码**: `hrcs_empstrategy`  
**表单ID**: `15I/L56JCAL3`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_empstrategy（行政组织-员工管理关系策略设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_empstrategy` | BaseEntity | 主表 |
| `t_hbss_empstrentry` | EntryEntity |  |

### 字段列表 — t_hbss_empstrategy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_empstrategy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_empstrategy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_empstrategy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_empstrategy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_empstrategy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_empstrategy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_empstrategy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_empstrategy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_empstrategy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_empstrategy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_empstrategy.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbss_empstrategy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_empstrategy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_empstrategy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_empstrategy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_empstrategy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_empstrategy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_empstrategy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_empstrategy.foriname |  |  |
| inheritedorg | 参照行政组织 | HRAdminOrgField | t_hbss_empstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| effdt | 生效日期 | DateField | t_hbss_empstrategy.feffdt | ✓ |  |
| defstrategytype | 默认策略 | BasedataField | t_hbss_empstrategy.fstrategyid |  | hrcs_strategy |
| leffdt | 失效日期 | DateField | t_hbss_empstrategy.fleffdt |  |  |
| sourceorg | 源策略组织 | HRAdminOrgField | t_hbss_empstrategy.fsourceorgid |  | haos_adminorghrf7 |
| hrbu | 业务单元 | OrgField | — |  | bos_org |
| orgteam | 行政组织 | HRAdminOrgField | t_hbss_empstrategy.forgteamid |  | haos_adminorghrf7 |
| bussinessfield | 业务类型关系 | BasedataField | t_hbss_empstrategy.fbussinessfieldid |  | hrcs_bussinesstype |
| entryhrbu | 默认HR管理组织 | OrgField | t_hbss_empstrategy.fhrbuid | ✓ | bos_org |
| entryinheritedorg | 参照行政组织 | HRAdminOrgField | t_hbss_empstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| entryenable | 业务状态 | ComboField | t_hbss_empstrategy.fenable |  |  |
| entrydefstrategy | 策略 | BasedataField | t_hbss_empstrategy.fstrategyid | ✓ | hrcs_strategy |
| entryorgteam | 分录组织团队 | HRAdminOrgField | t_hbss_empstrategy.forgteamid |  | haos_adminorghrf7 |
| entryeffdt | 生效日期 | DateField | t_hbss_empstrategy.feffdt | ✓ |  |
| strategyentrytype | 策略来源 | ComboField | t_hbss_empstrategy.fstrategyentrytype |  |  |
| entryleffdt | 失效日期 | DateField | t_hbss_empstrategy.fleffdt |  |  |
| entrysourceorg | 源策略组织 | HRAdminOrgField | t_hbss_empstrategy.fsourceorgid |  | haos_adminorghrf7 |
| sourceentry | 源分录内码 | BigIntField | t_hbss_empstrategy.fsourceentryid |  |  |
| includerange | 适用范围 | TextField | — |  |  |
| seqindex | 顺序号 | IntegerField | — |  |  |
| entrystrategy | 管理关系内码 | BigIntField | t_hbss_empstrategy.fid |  |  |
| entrychangetype | 变更类型 | ComboField | — |  |  |
| inittype | 初始类型 | ComboField | t_hbss_empstrategy.finittype |  |  |
| effectivedate | 生效日期 | DateField | — |  |  |
| changetype | 变更类型 | ComboField | — |  |  |
|  |  | EntryEntity | → t_hbss_empstrentry |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_empstrategy（主表） | 44 |
| 无数据库列 | 6 |

