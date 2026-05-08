# hrcs_dynascheme — 动态授权方案

**表单编码**: `hrcs_dynascheme`  
**表单ID**: `3V6C9072V4IO`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynascheme（动态授权方案） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynascheme` | BaseEntity | 主表 |
| `t_hrcs_dynaschasgnactent` | EntryEntity | 方案分配操作分录 |
| `t_hrcs_dynaschcclactent` | EntryEntity | 方案取消操作分录 |
| `t_hrcs_dynaschemerole` | EntryEntity | 角色清单 |
| `t_hrcs_dynasearchparam` | MulEmployeeField子表 | 方案搜索规则参数 |
| `t_hrcs_dynasearchadminorg` | MulEmployeeField子表 | 方案搜索行政组织 |
| `t_hrcs_dynasearchpos` | MulEmployeeField子表 | 方案搜索岗位 |
| `t_hrcs_dynasearchjob` | MulEmployeeField子表 | 方案搜索职位 |
| `t_hrcs_dynasearchactiona` | MulEmployeeField子表 | 方案搜索分配变动类型 |
| `t_hrcs_dynasearchactionc` | MulEmployeeField子表 | 方案搜索取消变动类型 |

### 字段列表 — t_hrcs_dynascheme（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_dynascheme.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_dynascheme.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_dynascheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_dynascheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_dynascheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_dynascheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynascheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynascheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_dynascheme.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_dynascheme.fsimplename |  |  |
| description | 规则说明 | MuliLangTextField | t_hrcs_dynascheme.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_dynascheme.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_dynascheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_dynascheme.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_dynascheme.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynascheme.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_dynascheme.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_dynascheme.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_dynascheme.foriname |  |  |
| boid | 业务ID | BigIntField | t_hrcs_dynascheme.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hrcs_dynascheme.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hrcs_dynascheme.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hrcs_dynascheme.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hrcs_dynascheme.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hrcs_dynascheme.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hrcs_dynascheme.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hrcs_dynascheme.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hrcs_dynascheme.fhisversion |  |  |
| admingroup | 所属管理员组 | BasedataField | t_hrcs_dynascheme.fadmingroupid | ✓ | perm_admingroup |
| search_param | 方案搜索规则参数 | MulBasedataField | t_hrcs_dynasearchparam（子表） |  |  |
| search_adminorg | 方案搜索行政组织 | HRMulAdminOrgField | t_hrcs_dynasearchadminorg（子表） |  |  |
| search_pos | 方案搜索岗位 | HRMulPositionField | t_hrcs_dynasearchpos（子表） |  |  |
| search_job | 方案搜索职位 | MulHisModelBasedataField | t_hrcs_dynasearchjob（子表） |  |  |
| condition | 规则条件 | LargeTextField | t_hrcs_dynascheme.fcondition |  |  |
| search_assignaction | 方案搜索分配变动类型 | MulBasedataField | t_hrcs_dynasearchactiona（子表） |  |  |
| search_cancelaction | 方案搜索取消变动类型 | MulBasedataField | t_hrcs_dynasearchactionc（子表） |  |  |
| ruledescription | 规则摘要 | MuliLangTextField | t_hrcs_dynascheme.fruledescription |  |  |
| authaction | 授权动作 | ComboField | t_hrcs_dynascheme.fauthaction | ✓ |  |
| assigndays | 分配天数 | IntegerField | t_hrcs_dynascheme.fassigndays |  |  |
| assigndesc | 分配文案 | MuliLangTextField | t_hrcs_dynascheme.fassigndesc |  |  |
| canceldesc | 取消文案 | MuliLangTextField | t_hrcs_dynascheme.fcanceldesc |  |  |
| assignactionentry | 方案分配操作分录 | EntryEntity | → t_hrcs_dynaschasgnactent |  |  |
| cancelactionentry | 方案取消操作分录 | EntryEntity | → t_hrcs_dynaschcclactent |  |  |
| roleentry | 角色清单 | EntryEntity | → t_hrcs_dynaschemerole |  |  |

### 字段列表 — t_hrcs_dynaschasgnactent（方案分配操作分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboidassign | 版本id | BigIntField | t_hrcs_dynaschasgnactent.fentryboid |  |  |
| assignpersonitem | 人员 | BasedataField | t_hrcs_dynaschasgnactent.fassignpersonitemid |  | hrcs_dynaauthobject |
| assignactype | 变动类型 | BasedataField | t_hrcs_dynaschasgnactent.fassignactypeid |  | hpfs_chgcategory |

### 字段列表 — t_hrcs_dynaschcclactent（方案取消操作分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboidcancel | 版本id | BigIntField | t_hrcs_dynaschcclactent.fentryboid |  |  |
| cancelpersonitem | 人员 | BasedataField | t_hrcs_dynaschcclactent.fcancelpersonitemid |  | hrcs_dynaauthobject |
| cancelactype | 变动类型 | BasedataField | t_hrcs_dynaschcclactent.fcancelactypeid |  | hpfs_chgcategory |

### 字段列表 — t_hrcs_dynaschemerole（角色清单·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| role | 角色名称 | BasedataField | t_hrcs_dynaschemerole.froleid | ✓ | perm_role |
| hrcsrole | 中台角色 | BasedataField | — |  | hrcs_role |
| customenable | 角色成员范围属性 | ComboField | t_hrcs_dynaschemerole.fcustomenable | ✓ |  |
| roleremark | 角色描述 | MuliLangTextField | t_hrcs_dynaschemerole.froleremark |  |  |
| custominfo | 自定义信息 | LargeTextField | — |  |  |
| entryboidrole | 版本id | BigIntField | t_hrcs_dynaschemerole.fentryboid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynascheme（主表） | 41 |
| t_hrcs_dynaschasgnactent（方案分配操作分录） | 3 |
| t_hrcs_dynaschcclactent（方案取消操作分录） | 3 |
| t_hrcs_dynaschemerole（角色清单） | 6 |
| t_hrcs_dynasearchparam（MulEmployeeField子表） | 1 |
| t_hrcs_dynasearchadminorg（MulEmployeeField子表） | 1 |
| t_hrcs_dynasearchpos（MulEmployeeField子表） | 1 |
| t_hrcs_dynasearchjob（MulEmployeeField子表） | 1 |
| t_hrcs_dynasearchactiona（MulEmployeeField子表） | 1 |
| t_hrcs_dynasearchactionc（MulEmployeeField子表） | 1 |
| 无数据库列 | 2 |

