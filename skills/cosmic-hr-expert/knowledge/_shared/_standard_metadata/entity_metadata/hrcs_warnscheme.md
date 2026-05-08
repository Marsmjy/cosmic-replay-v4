# hrcs_warnscheme — 预警方案

**表单编码**: `hrcs_warnscheme`  
**表单ID**: `40H1/OK3YH1U`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnscheme（预警方案） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnplan` | BaseEntity | 主表 |
| `t_hrcs_rcrelation` | EntryEntity | 人员关系单据体 |
| `t_hrcs_rcfix` | EntryEntity | 指定接收人单据体 |
| `t_hrcs_rcrole` | EntryEntity | 角色单据体 |
| `t_hrcs_rcadminorg` | EntryEntity | 行政组织单据体 |
| `t_hrcs_rcposition` | EntryEntity | 岗位单据体 |
| `t_hrcs_rcplugin` | EntryEntity | 插件单据体 |
| `（虚拟分录）` | EntryEntity | 消息配置 |
| `（虚拟分录）` | EntryEntity | 消息表格 |

### 字段列表 — t_hrcs_warnplan（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 方案编码 | TextField | t_hrcs_warnplan.fnumber |  |  |
| name | 方案名称 | MuliLangTextField | t_hrcs_warnplan.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_warnplan.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_warnplan.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_warnplan.fmodifierid |  | bos_user |
| enable | 状态 | BillStatusField | t_hrcs_warnplan.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnplan.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnplan.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_warnplan.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_warnplan.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_warnplan.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_warnplan.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_warnplan.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_warnplan.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_warnplan.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnplan.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_warnplan.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_warnplan.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_warnplan.foriname |  |  |
| basecondition | 基本条件 | TextField | t_hrcs_warnplan.fbasecondition |  |  |
| repeatperiod | 重复周期 | ComboField | t_hrcs_warnplan.frepeatperiod | ✓ |  |
| weekday | 指定时间 | MulComboField | t_hrcs_warnplan.fweekday | ✓ |  |
| monthday | 指定时间（日期） | MulComboField | t_hrcs_warnplan.fmonthday | ✓ |  |
| yearday | 重复周期(年) | TextField | t_hrcs_warnplan.fyearday |  |  |
| jobid | 调度作业ID | TextField | t_hrcs_warnplan.fjobid |  |  |
| planid | 调度任务ID | TextField | t_hrcs_warnplan.fplanid |  |  |
| pushchannel | 推送渠道 | MulComboField | t_hrcs_warnplan.fpushchannel | ✓ |  |
| jumphandle | 跳转处理类 | TextField | t_hrcs_warnplan.fjumphandle |  |  |
| warnscene | 预警场景 | BasedataField | t_hrcs_warnplan.fwarnsceneid | ✓ | hrcs_warnscene |
| bizapp | 所属应用 | BasedataField | t_hrcs_warnplan.fbizappid |  | bos_devportal_bizapp |
| createorg | 创建组织 | OrgField | t_hrcs_warnplan.fcreateorgid | ✓ | bos_org |
| timezone | 时区 | BasedataField | t_hrcs_warnplan.ftimezone | ✓ | inte_timezone |
| daterangefield | 预警日期范围 | DateRangeField | — | ✓ |  |
| org | 所属HR管理组织 | OrgField | t_hrcs_warnplan.forg |  | bos_org |
| adminorg | 所属行政组织 | HRAdminOrgField | t_hrcs_warnplan.fadminorg |  | haos_adminorghrf7 |
| warntype | 预警对象类型 | ComboField | t_hrcs_warnplan.fwarntype | ✓ |  |
| warnbizobj | 业务对象 | BasedataField | t_hrcs_warnplan.fwarnbizobj | ✓ | hbp_entityobject |
| datafilter | 数据过滤规则 | TextField | t_hrcs_warnplan.fdatafilter |  |  |
| ruledate | 触发日期操作的控件（勿删） | DateField | — |  |  |
| belongto | 所属预警场景/业务对象 | TextField | — |  |  |
| monitortime | 监控时间（按小时） | MulComboField | t_hrcs_warnplan.fmonitortime | ✓ |  |
| monthweek | 指定时间（星期） | MulComboField | t_hrcs_warnplan.fmonthweek | ✓ |  |
| monthweekday | 指定星期 | MulComboField | t_hrcs_warnplan.fmonthweekday | ✓ |  |
| customcron | cron表达式 | TextField | t_hrcs_warnplan.fcustomcron | ✓ |  |
| mustconditiondata | 必要条件(废弃) | TextField | t_hrcs_warnplan.fmustconditiondata |  |  |
| msgreceivertype | 消息接收人类型 | CheckBoxGroupField | t_hrcs_warnplan.fmsgreceivertype | ✓ |  |
| localeid | 通知语言 | MulComboField | t_hrcs_warnplan.flocaleid | ✓ |  |
| rangetype | 周期设定 | ComboField | t_hrcs_warnplan.frangetype | ✓ |  |
| monitorstartdate | 开始日期 | DateField | t_hrcs_warnplan.fmonitorstartdate | ✓ |  |
| rcrelationentryentity | 人员关系单据体 | EntryEntity | → t_hrcs_rcrelation |  |  |
| rcfixentryentity | 指定接收人单据体 | EntryEntity | → t_hrcs_rcfix |  |  |
| rcroleentryentity | 角色单据体 | EntryEntity | → t_hrcs_rcrole |  |  |
| adminorgentry | 行政组织单据体 | EntryEntity | → t_hrcs_rcadminorg |  |  |
| positionentry | 岗位单据体 | EntryEntity | → t_hrcs_rcposition |  |  |
| pluginentry | 插件单据体 | EntryEntity | → t_hrcs_rcplugin |  |  |
| entrymsgconf | 消息配置 | EntryEntity | → （虚拟分录） |  |  |
| entrymsgtable | 消息表格 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — t_hrcs_rcrelation（人员关系单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rcuser | 人员 | TextField | t_hrcs_rcrelation.fuser |  |  |
| rcuserdisplay | 人员 | TextField | — |  |  |
| rcrelationship | 人员关系 | MulComboField | t_hrcs_rcrelation.frcrelation | ✓ |  |
| rcsourcetype | 来源类型 | ComboField | t_hrcs_rcrelation.fsourcetype |  |  |

### 字段列表 — t_hrcs_rcfix（指定接收人单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rcfixuser | 人员姓名 | BasedataField | t_hrcs_rcfix.fuser |  | bos_user |
| rcfixuserorg | 部门 | TextField | t_hrcs_rcfix.frcfixuserorg |  |  |
| rcfixperm | 按人员数据权限控制预警数据范围 | CheckBoxField | t_hrcs_rcfix.fperm |  |  |

### 字段列表 — t_hrcs_rcrole（角色单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rcroleentry | 角色编码 | BasedataField | t_hrcs_rcrole.fpermrole |  | perm_role |
| roleperm | 按人员数据权限控制预警数据范围 | CheckBoxField | t_hrcs_rcrole.fperm |  |  |

### 字段列表 — t_hrcs_rcadminorg（行政组织单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorgfield | 行政组织 | HRAdminOrgField | t_hrcs_rcadminorg.fadminorgfield |  | haos_adminorghrf7 |
| containssub | 包含下级 | CheckBoxField | t_hrcs_rcadminorg.fcontainssub |  |  |
| adminorgperm | 按人员数据权限控制预警数据范围 | CheckBoxField | t_hrcs_rcadminorg.fadminorgperm |  |  |

### 字段列表 — t_hrcs_rcposition（岗位单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| position | 岗位 | HRPositionField | t_hrcs_rcposition.fposition |  | hbpm_positionhrf7 |
| positionperm | 按人员数据权限控制预警数据范围 | CheckBoxField | t_hrcs_rcposition.fpositionperm |  |  |

### 字段列表 — t_hrcs_rcplugin（插件单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| serviceclass | 服务类 | TextField | t_hrcs_rcplugin.fserviceclass | ✓ |  |
| plugindesc | 描述 | MuliLangTextField | t_hrcs_rcplugin.fplugindesc | ✓ |  |

### 字段列表 — （消息配置·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （消息表格·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnplan（主表） | 49 |
| t_hrcs_rcrelation（人员关系单据体） | 4 |
| t_hrcs_rcfix（指定接收人单据体） | 3 |
| t_hrcs_rcrole（角色单据体） | 2 |
| t_hrcs_rcadminorg（行政组织单据体） | 3 |
| t_hrcs_rcposition（岗位单据体） | 2 |
| t_hrcs_rcplugin（插件单据体） | 2 |
| （消息配置） | 11 |
| （消息表格） | 11 |
| 无数据库列 | 4 |

