# hrcs_tplvariableconfig — 取值对象配置

**表单编码**: `hrcs_tplvariableconfig`  
**表单ID**: `3CZ7IJRRGRKR`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_tplvariableconfig（取值对象配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_tplvariableconfig` | BaseEntity | 主表 |
| `t_hrcs_tplvarconfigentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_tplvariableconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_tplvariableconfig.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_tplvariableconfig.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_tplvariableconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_tplvariableconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_tplvariableconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_tplvariableconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_tplvariableconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_tplvariableconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_tplvariableconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_tplvariableconfig.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_tplvariableconfig.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_tplvariableconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_tplvariableconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_tplvariableconfig.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_tplvariableconfig.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_tplvariableconfig.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_tplvariableconfig.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_tplvariableconfig.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_tplvariableconfig.foriname |  |  |
| isentry | 是否为分录 | TextField | t_hrcs_tplvariableconfig.fisentry |  |  |
| parententity | 父业务对象 | BasedataField | t_hrcs_tplvariableconfig.fparententity |  | hbp_entityobject |
| entrynumber | 分录标识 | TextField | t_hrcs_tplvariableconfig.fentrynumber |  |  |
| entityupgradflag | 业务对象升级标识 | TextField | t_hrcs_tplvariableconfig.fentityupgradflag |  |  |
| variableupgradflag | 常用变量升级标识 | TextField | t_hrcs_tplvariableconfig.fvariableupgradflag |  |  |
| entryname | 分录名称 | TextField | t_hrcs_tplvariableconfig.fentryname |  |  |
| parentid | 父实体ID | BigIntField | t_hrcs_tplvariableconfig.fparentid |  |  |
| sonentity | 子业务对象 | BasedataField | t_hrcs_tplvariableconfig.fsonentity |  | hbp_entityobject |
| sonentityalias | 子实体别名 | MuliLangTextField | t_hrcs_tplvariableconfig.fsonentityalias |  |  |
| mainentity | 主实体 | BasedataField | t_hrcs_tplvariableconfig.fmainentity |  | hbp_entityobject |
| rule | 子实体过滤条件 | TextField | t_hrcs_tplvariableconfig.frule |  |  |
| createorg | 创建组织 | OrgField | t_hrcs_tplvariableconfig.fcreateorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hrcs_tplvariableconfig.fctrlstrategy |  |  |
| useorg | 使用组织 | OrgField | t_hrcs_tplvariableconfig.fuseorgid |  | bos_org |
| org | 管理组织 | OrgField | t_hrcs_tplvariableconfig.forgid |  | bos_org |
| bitindex | 位图 | IntegerField | t_hrcs_tplvariableconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | t_hrcs_tplvariableconfig.fsourcebitindex |  |  |
| sourcedata | 原资料ID | BigIntField | t_hrcs_tplvariableconfig.fsourcedataid |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_tplvarconfigentry |  |  |

### 字段列表 — t_hrcs_tplvarconfigentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| parentfieldname | 父业务对象字段 | TextField | t_hrcs_tplvarconfigentry.fparentfieldname |  |  |
| fieldcondition | 条件 | TextField | t_hrcs_tplvarconfigentry.ffieldcondition |  |  |
| parentfield | 父实体字段 | TextField | t_hrcs_tplvarconfigentry.fparentfield |  |  |
| sonfieldname | 子业务对象字段 | TextField | t_hrcs_tplvarconfigentry.fsonfieldname |  |  |
| sonfield | 子实体字段 | TextField | t_hrcs_tplvarconfigentry.fsonfield |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_tplvariableconfig（主表） | 37 |
| t_hrcs_tplvarconfigentry（单据体） | 5 |

