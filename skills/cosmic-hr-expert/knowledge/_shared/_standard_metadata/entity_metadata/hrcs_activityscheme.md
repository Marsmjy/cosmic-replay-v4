# hrcs_activityscheme — 活动方案

**表单编码**: `hrcs_activityscheme`  
**表单ID**: `2/WZZ=1+H+FW`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityscheme（活动方案） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_actscheme` | BaseEntity | 主表 |
| `t_hrcs_actschemeentry` | EntryEntity | 活动 |
| `（虚拟分录）` | EntryEntity | 单据体 |
| `t_hrcs_groupentry` | EntryEntity | 活动组规则 |
| `（虚拟分录）` | EntryEntity | 活动组无数据分录 |
| `t_hrcs_actgroupentity` | SubEntryEntity | 活动分录 |
| `t_hrcs_actschemeactinfo` | SubEntryEntity | 活动对象信息单据体 |
| `t_hrcs_actschemeparam` | SubEntryEntity | 领域参数属性单据体 |
| `t_hrcs_actinfoservice` | SubEntryEntity | 插件 |
| `t_hrcs_actfieldmapping` | SubEntryEntity | 映射关系 |
| `t_hrcs_actschemeref` | MulEmployeeField子表 | 参照方案 |

### 字段列表 — t_hrcs_actscheme（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 方案编码 | TextField | t_hrcs_actscheme.fnumber |  |  |
| name | 方案名称 | MuliLangTextField | t_hrcs_actscheme.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_actscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_actscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_actscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_actscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_actscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_actscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_actscheme.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hrcs_actscheme.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_actscheme.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_actscheme.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_actscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_actscheme.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_actscheme.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_actscheme.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_actscheme.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_actscheme.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_actscheme.foriname |  |  |
| bizobj | 业务对象 | BasedataField | t_hrcs_actscheme.fbizobjid | ✓ | bos_entityobject |
| app | 所属应用 | BasedataField | t_hrcs_actscheme.fappid | ✓ | bos_devportal_bizapp |
| activityschemeref | 参照方案 | MulBasedataField | t_hrcs_actschemeref（子表） |  |  |
| version | 版本 | TextField | t_hrcs_actscheme.fversion | ✓ |  |
| islatest | 是否最新 | CheckBoxField | t_hrcs_actscheme.fislatest |  |  |
| versiondate | 版本_日期 | IntegerField | t_hrcs_actscheme.fversiondate |  |  |
| versionseq | 版本_流水号 | IntegerField | t_hrcs_actscheme.fversionseq |  |  |
| sequence | 序列 | BigIntField | t_hrcs_actscheme.fsequence |  |  |
| viewstatus | 页面状态 | TextField | — |  |  |
| adminorg | 管理行政组织 | HRAdminOrgField | t_hrcs_actscheme.fadminorgid | ✓ | haos_adminorghrf7 |
| actgroupnameview | 活动组名称 | MuliLangTextField | — |  |  |
| actgroupdescview | 活动组描述 | MuliLangTextField | — |  |  |
| actschemeentry | 活动 | EntryEntity | → t_hrcs_actschemeentry |  |  |
| entryentity | 单据体 | EntryEntity | → （虚拟分录） |  |  |
| groupentry | 活动组规则 | EntryEntity | → t_hrcs_groupentry |  |  |
| groupnodataentry | 活动组无数据分录 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_hrcs_actschemeentry（活动·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| activity | 请选择活动 | BasedataField | t_hrcs_actschemeentry.factivityid |  | hrcs_activity |
| actinfo | 活动对象信息单据体 | SubEntryEntity | → t_hrcs_actschemeactinfo |  |  |
| paramconfig | 领域参数属性单据体 | SubEntryEntity | → t_hrcs_actschemeparam |  |  |
| pluginentry | 插件 | SubEntryEntity | → t_hrcs_actinfoservice |  |  |
| subentryentity | 映射关系 | SubEntryEntity | → t_hrcs_actfieldmapping |  |  |

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — t_hrcs_groupentry（活动组规则·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| actgroupdesc | 活动组描述 | MuliLangTextField | t_hrcs_groupentry.factgroupdesc |  |  |
| condition | 规则 | TextField | t_hrcs_groupentry.fcondition |  |  |
| actgroupentity | 活动分录 | SubEntryEntity | → t_hrcs_actgroupentity |  |  |

### 字段列表 — （活动组无数据分录·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — t_hrcs_actgroupentity（活动分录·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| groupactivity | 活动 | BasedataField | t_hrcs_actgroupentity.fgroupactivityid | ✓ | hrcs_activity |
| activitytype | 活动类型 | ComboField | t_hrcs_actgroupentity.factivitytype | ✓ |  |

### 字段列表 — t_hrcs_actschemeactinfo（活动对象信息单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| actbizobj | 业务对象 | BasedataField | t_hrcs_actschemeactinfo.factbizobjid |  | bos_entityobject |
| taskcreatetype | 任务创建方式 | ComboField | t_hrcs_actschemeactinfo.ftaskcreatetype | ✓ |  |
| tasktheme | 任务主题 | TextField | t_hrcs_actschemeactinfo.ftasktheme | ✓ |  |
| taskassignmenttype | 任务分配方式 | ComboField | t_hrcs_actschemeactinfo.ftaskassignmenttype | ✓ |  |
| sla | SLA（小时） | IntegerField | t_hrcs_actschemeactinfo.fsla |  |  |
| flowparam | 流程流转参数 | ComboField | t_hrcs_actschemeactinfo.fflowparam | ✓ |  |
| bindinglayoutid | 绑定页面 | ComboField | t_hrcs_actschemeactinfo.fbindinglayoutid |  |  |
| checkhandler | 是否需要活动处理人 | ComboField | t_hrcs_actschemeactinfo.fcheckhandler | ✓ |  |
| taskthemedisplay | 任务主题 | TextField | — | ✓ |  |

### 字段列表 — t_hrcs_actschemeparam（领域参数属性单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramname | 参数名 | TextField | t_hrcs_actschemeparam.fparamname |  |  |
| paramnumber | 参数编码 | TextField | t_hrcs_actschemeparam.fparamnumber |  |  |
| paramvalue | 参数值 | TextField | t_hrcs_actschemeparam.fparamvalue |  |  |

### 字段列表 — t_hrcs_actinfoservice（插件·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| plugintype | 插件类型 | ComboField | t_hrcs_actinfoservice.fplugintype |  |  |
| plugindesc | 功能描述 | MuliLangTextField | t_hrcs_actinfoservice.fplugindesc |  |  |
| bizapp | 所属应用 | BasedataField | t_hrcs_actinfoservice.fbizapp |  | hbp_devportal_bizapp |
| service | 服务类 | TextField | t_hrcs_actinfoservice.fservice |  |  |
| method | 服务方法 | TextField | t_hrcs_actinfoservice.fmethod |  |  |

### 字段列表 — t_hrcs_actfieldmapping（映射关系·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| targetfieldnumber | 当前活动业务字段标识 | TextField | t_hrcs_actfieldmapping.ftargetfieldnumber |  |  |
| sourcefield | 字段来源 | TextField | t_hrcs_actfieldmapping.fsourcefield |  |  |
| fieldactivityid | 活动ID | TextField | t_hrcs_actfieldmapping.ffieldactivityid |  |  |
| targetfield | 当前活动业务字段名称 | MuliLangTextField | t_hrcs_actfieldmapping.ftargetfield |  |  |
| soucefieldname | 字段来源名称 | MuliLangTextField | t_hrcs_actfieldmapping.fsoucefieldname |  |  |
| sourceissyspreset | 系统预置 | ComboField | t_hrcs_actfieldmapping.fsourceissyspreset |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_actscheme（主表） | 39 |
| t_hrcs_actschemeentry（活动） | 1 |
| t_hrcs_groupentry（活动组规则） | 2 |
| t_hrcs_actgroupentity（活动分录） | 2 |
| t_hrcs_actschemeactinfo（活动对象信息单据体） | 9 |
| t_hrcs_actschemeparam（领域参数属性单据体） | 3 |
| t_hrcs_actinfoservice（插件） | 5 |
| t_hrcs_actfieldmapping（映射关系） | 6 |
| t_hrcs_actschemeref（MulEmployeeField子表） | 1 |
| 无数据库列 | 12 |

