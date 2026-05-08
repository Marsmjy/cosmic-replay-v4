# hrcs_orgstrategy — 行政组织-组织管理关系策略设置

**表单编码**: `hrcs_orgstrategy`  
**表单ID**: `15I1YPE6ME++`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_orgstrategy（行政组织-组织管理关系策略设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_orgstrategy` | BaseEntity | 主表 |
| `t_hbss_orgstrentry` | EntryEntity |  |

### 字段列表 — t_hbss_orgstrategy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_orgstrategy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_orgstrategy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_orgstrategy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_orgstrategy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_orgstrategy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_orgstrategy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_orgstrategy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_orgstrategy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_orgstrategy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_orgstrategy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_orgstrategy.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbss_orgstrategy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_orgstrategy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_orgstrategy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_orgstrategy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_orgstrategy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_orgstrategy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_orgstrategy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_orgstrategy.foriname |  |  |
| inheritedorg | 参照行政组织 | HRAdminOrgField | t_hbss_orgstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| effdt | 生效日期 | DateField | t_hbss_orgstrategy.feffdt | ✓ |  |
| defstrategytype | 默认策略 | BasedataField | t_hbss_orgstrategy.fstrategyid |  | hrcs_strategy |
| leffdt | 失效日期 | DateField | t_hbss_orgstrategy.fleffdt |  |  |
| sourceorg | 源策略组织 | HRAdminOrgField | t_hbss_orgstrategy.fsourceorgid |  | haos_adminorghrf7 |
| hrbu | 业务单元 | OrgField | — |  | bos_org |
| orgteam | 行政组织 | HRAdminOrgField | t_hbss_orgstrategy.forgteamid |  | haos_adminorghrf7 |
| bussinessfield | 业务类型关系 | BasedataField | t_hbss_orgstrategy.fbussinessfieldid |  | hrcs_bussinesstype |
| entryhrbu | 默认HR管理组织 | OrgField | t_hbss_orgstrategy.fhrbuid | ✓ | bos_org |
| entryinheritedorg | 参照行政组织 | HRAdminOrgField | t_hbss_orgstrategy.finheritedorgid | ✓ | haos_adminorghrf7 |
| entryenable | 业务状态 | ComboField | t_hbss_orgstrategy.fenable |  |  |
| entrydefstrategy | 策略 | BasedataField | t_hbss_orgstrategy.fstrategyid | ✓ | hrcs_strategy |
| entryorgteam | 分录行政组织 | HRAdminOrgField | t_hbss_orgstrategy.forgteamid |  | haos_adminorghrf7 |
| entryeffdt | 生效日期 | DateField | t_hbss_orgstrategy.feffdt | ✓ |  |
| strategyentrytype | 策略来源 | ComboField | t_hbss_orgstrategy.fstrategyentrytype |  |  |
| entryleffdt | 失效日期 | DateField | t_hbss_orgstrategy.fleffdt |  |  |
| entrysourceorg | 源策略组织 | HRAdminOrgField | t_hbss_orgstrategy.fsourceorgid |  | haos_adminorghrf7 |
| sourceentry | 源分录内码 | BigIntField | t_hbss_orgstrategy.fsourceentryid |  |  |
| entrystrategy | 管理关系内码 | BigIntField | t_hbss_orgstrategy.fid |  |  |
| entrychangetype | 变更类型 | ComboField | — |  |  |
| inittype | 初始类型 | ComboField | t_hbss_orgstrategy.finittype |  |  |
| effectivedate | 生效日期 | DateField | — |  |  |
| changetype | 变更类型 | ComboField | — |  |  |
|  |  | EntryEntity | → t_hbss_orgstrentry |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_orgstrategy（主表） | 42 |
| 无数据库列 | 4 |

