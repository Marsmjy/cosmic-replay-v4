# hbss_capacityitem — 能力素质项

**表单编码**: `hbss_capacityitem`  
**表单ID**: `2GHHBD8K5BMH`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_capacityitem（能力素质项） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_capacityitem` | BaseEntity | 主表 |
| `t_hbss_capitemrankentry` | EntryEntity | 等级描述 |

### 字段列表 — t_hbss_capacityitem（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_capacityitem.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_capacityitem.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_capacityitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_capacityitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_capacityitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_capacityitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_capacityitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_capacityitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_capacityitem.fmasterid |  |  |
| group | 所属维度 | GroupField | — |  | hbss_capacitygroup |
| org | 管理组织 | OrgField | t_hbss_capacityitem.forgid |  | bos_org |
| createorg | 创建组织 | OrgField | t_hbss_capacityitem.fcreateorgid | ✓ | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hbss_capacityitem.fctrlstrategy |  |  |
| sourcedata | 原资料id | BigIntField | t_hbss_capacityitem.fsourcedataid |  |  |
| bitindex | 位图 | IntegerField | t_hbss_capacityitem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | t_hbss_capacityitem.fsourcebitindex |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hbss_capacityitem.fsrccreateorgid |  | bos_org |
| disabler | 禁用人 | UserField | t_hbss_capacityitem.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_capacityitem.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_capacityitem.finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_capacityitem.fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_capacityitem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_capacityitem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_capacityitem.foriname |  |  |
| boid | 业务ID | BigIntField | t_hbss_capacityitem.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hbss_capacityitem.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hbss_capacityitem.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hbss_capacityitem.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hbss_capacityitem.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hbss_capacityitem.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hbss_capacityitem.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hbss_capacityitem.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hbss_capacityitem.fhisversion |  |  |
| caprankscheme | 能力等级方案 | BasedataField | t_hbss_capacityitem.fcaprankschemeid | ✓ | hbss_capacityrankscheme |
| type | 描述方式 | ComboField | t_hbss_capacityitem.ftype | ✓ |  |
| definition | 定义 | MuliLangTextField | t_hbss_capacityitem.fdefinition |  |  |
| description | 描述 | MuliLangTextField | t_hbss_capacityitem.fdescription |  |  |
| rankinfo | 能力等级信息 | TextAreaField | t_hbss_capacityitem.frankinfo |  |  |
| mainkeyword | 关键词 | MuliLangTextField | t_hbss_capacityitem.fmainkeyword |  |  |
| isnagbehavior | 增加负向行为 | CheckBoxField | t_hbss_capacityitem.fisnagbehavior |  |  |
| nagbehavior | 负向行为 | MuliLangTextField | t_hbss_capacityitem.fnagbehavior |  |  |
| leftentryentity | 等级描述 | EntryEntity | → t_hbss_capitemrankentry |  |  |

### 字段列表 — t_hbss_capitemrankentry（等级描述·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| grouprankname | 等级名称 | MuliLangTextField | t_hbss_capitemrankentry.frankname |  |  |
| groupsecname | 等级别称 | MuliLangTextField | t_hbss_capitemrankentry.fsecname |  |  |
| groupkeyword | 关键词 | MuliLangTextField | t_hbss_capitemrankentry.fkeyword |  |  |
| groupsubdefinition | 等级定义 | MuliLangTextField | t_hbss_capitemrankentry.fdefinition |  |  |
| groupiscaphasact | 关键行为描述 | CheckBoxField | t_hbss_capitemrankentry.fiscaphasact |  |  |
| gouupcapacityactionentry | 行为描述 | MulBasedataField | — |  |  |
| rankschemeid | 能力等级方案内码 | BigIntField | — |  |  |
| entryboid_rank | 分录业务id | BigIntField | t_hbss_capitemrankentry.fentryboid |  |  |
| groupsubdescription | 描述 | MuliLangTextField | t_hbss_capitemrankentry.fdescription |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_capacityitem（主表） | 42 |
| t_hbss_capitemrankentry（等级描述） | 9 |
| 无数据库列 | 4 |

