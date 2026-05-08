# hrcs_esignspmgr — 电子签服务商管理

**表单编码**: `hrcs_esignspmgr`  
**表单ID**: `3E3457P0G9V3`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esignspmgr（电子签服务商管理） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_esignspmgr` | BaseEntity | 主表 |
| `t_hrcs_esignspmgrentry` | EntryEntity | 服务类注册单据体 |
| `t_hrcs_esignspmgrentry2` | EntryEntity | 集成应用配置单据体 |

### 字段列表 — t_hrcs_esignspmgr（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_esignspmgr.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_esignspmgr.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_esignspmgr.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_esignspmgr.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_esignspmgr.fmodifierid |  | bos_user |
| enable | 状态 | BillStatusField | t_hrcs_esignspmgr.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_esignspmgr.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_esignspmgr.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_esignspmgr.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_esignspmgr.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_esignspmgr.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_esignspmgr.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_esignspmgr.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_esignspmgr.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_esignspmgr.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_esignspmgr.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_esignspmgr.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_esignspmgr.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_esignspmgr.foriname |  |  |
| showapptab | 集成应用配置 | TextField | — |  |  |
| entryentity | 服务类注册单据体 | EntryEntity | → t_hrcs_esignspmgrentry |  |  |
| entryentity1 | 集成应用配置单据体 | EntryEntity | → t_hrcs_esignspmgrentry2 |  |  |

### 字段列表 — t_hrcs_esignspmgrentry（服务类注册单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| interface | 接口路径 | TextField | t_hrcs_esignspmgrentry.finterface |  |  |
| srvname | 接口名称 | MuliLangTextField | t_hrcs_esignspmgrentry.fsrvname |  |  |
| plugin | 实现类 | TextField | t_hrcs_esignspmgrentry.fplugin |  |  |
| mustflag | 是否必须接入 | ComboField | t_hrcs_esignspmgrentry.fmustflag |  |  |
| srvdesc | 接口描述 | MuliLangTextField | t_hrcs_esignspmgrentry.fsrvdesc |  |  |

### 字段列表 — t_hrcs_esignspmgrentry2（集成应用配置单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bdesignappcfg | 集成应用配置 | BasedataField | t_hrcs_esignspmgrentry2.fbdesignappcfg |  | hrcs_esignappcfg |
| enable1 | 使用状态 | BillStatusField | t_hrcs_esignspmgrentry2.fenable1 |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_esignspmgr（主表） | 20 |
| t_hrcs_esignspmgrentry（服务类注册单据体） | 5 |
| t_hrcs_esignspmgrentry2（集成应用配置单据体） | 2 |
| 无数据库列 | 1 |

