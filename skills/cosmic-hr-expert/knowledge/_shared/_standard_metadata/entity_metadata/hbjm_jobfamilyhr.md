# hbjm_jobfamilyhr — 职位族

**表单编码**: `hbjm_jobfamilyhr`  
**表单ID**: `/WJJLJBB+6XH`  
**归属**: HR基础服务云 / HR基础职位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbjm_jobfamilyhr（职位族） [BaseEntity]

### 物理表（垂直拆分表）

| 表名 | 说明 |
|------|------|
| `t_hbjm_jobfamily` | 主表 |
| `t_hbjm_jobfamily_i` | 职位族关联华为ID |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbjm_jobfamily.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbjm_jobfamily.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbjm_jobfamily.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbjm_jobfamily.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbjm_jobfamily.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbjm_jobfamily.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbjm_jobfamily.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbjm_jobfamily.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbjm_jobfamily.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbjm_jobfamily.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hbjm_jobfamily.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbjm_jobfamily.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbjm_jobfamily.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbjm_jobfamily.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbjm_jobfamily.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbjm_jobfamily.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbjm_jobfamily.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbjm_jobfamily.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbjm_jobfamily.foriname |  |  |
| boid | 业务ID | BigIntField | t_hbjm_jobfamily.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hbjm_jobfamily.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hbjm_jobfamily.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hbjm_jobfamily.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hbjm_jobfamily.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hbjm_jobfamily.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hbjm_jobfamily.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hbjm_jobfamily.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hbjm_jobfamily.fhisversion |  |  |
| jobseq | 职位序列 | HisModelBasedataField | t_hbjm_jobfamily.fjobseqid | ✓ | hbjm_jobseqhr |
| sourcesyskey | 来源系统唯一标识 | TextField | t_hbjm_jobfamily.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbjm_jobfamily（主表） | 30 |
| 无数据库列（RadioField/废弃/虚拟） | 8 |

