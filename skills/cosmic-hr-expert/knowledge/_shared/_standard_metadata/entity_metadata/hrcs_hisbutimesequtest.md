# hrcs_hisbutimesequtest — 时序受控模板单元测试

**表单编码**: `hrcs_hisbutimesequtest`  
**表单ID**: `2RQLYGBZQ9L5`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_hisbutimesequtest（时序受控模板单元测试） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_hisbutimesequtest` | BaseEntity | 主表 |
| `t_hrcs_hisbutimeutentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_hisbutimesequtest（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_hisbutimesequtest.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_hisbutimesequtest.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_hisbutimesequtest.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_hisbutimesequtest.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_hisbutimesequtest.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_hisbutimesequtest.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_hisbutimesequtest.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_hisbutimesequtest.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_hisbutimesequtest.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hrcs_hisbutimesequtest.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_hisbutimesequtest.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_hisbutimesequtest.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_hisbutimesequtest.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_hisbutimesequtest.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_hisbutimesequtest.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_hisbutimesequtest.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_hisbutimesequtest.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_hisbutimesequtest.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_hisbutimesequtest.foriname |  |  |
| boid | 业务ID | BigIntField | t_hrcs_hisbutimesequtest.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hrcs_hisbutimesequtest.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hrcs_hisbutimesequtest.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hrcs_hisbutimesequtest.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hrcs_hisbutimesequtest.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hrcs_hisbutimesequtest.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hrcs_hisbutimesequtest.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hrcs_hisbutimesequtest.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hrcs_hisbutimesequtest.fhisversion |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_hisbutimeutentry |  |  |

### 字段列表 — t_hrcs_hisbutimeutentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboid | entryboid | BigIntField | t_hrcs_hisbutimeutentry.fentryboid |  |  |
| textfield | 文本 | TextField | t_hrcs_hisbutimeutentry.ftextfield |  |  |
| integerfield | 整数 | IntegerField | t_hrcs_hisbutimeutentry.fintegerfield |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_hisbutimesequtest（主表） | 36 |
| t_hrcs_hisbutimeutentry（单据体） | 3 |
| 无数据库列 | 8 |

