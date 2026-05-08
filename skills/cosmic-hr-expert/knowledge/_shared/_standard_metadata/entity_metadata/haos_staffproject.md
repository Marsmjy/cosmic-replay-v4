# haos_staffproject — 控编规则设置

**表单编码**: `haos_staffproject`  
**表单ID**: `2JJEJ=HXWUET`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_staffproject（控编规则设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_staffproject` | BaseEntity | 主表 |
| `t_haos_sporgentry` | EntryEntity | 单据体 |
| `t_haos_spdimension` | MulEmployeeField子表 | 编制维度 |

### 字段列表 — t_haos_staffproject（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_haos_staffproject.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_haos_staffproject.fname |  |  |
| status | 数据状态 | BillStatusField | t_haos_staffproject.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_staffproject.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_staffproject.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_haos_staffproject.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_staffproject.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_staffproject.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_staffproject.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_haos_staffproject.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_staffproject.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_staffproject.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_staffproject.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_staffproject.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_haos_staffproject.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_staffproject.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_haos_staffproject.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_haos_staffproject.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_haos_staffproject.foriname |  |  |
| controlstrategy | 控编方式 | ComboField | t_haos_staffproject.fcontrolstrategy | ✓ |  |
| unitycontrolmode | 统一控编方式 | CheckBoxField | t_haos_staffproject.funitycontrolmode |  |  |
| elasticcontrol | 弹性方式 | ComboField | t_haos_staffproject.felasticcontrol |  |  |
| elasticcount | 弹性额度 | IntegerField | t_haos_staffproject.felasticcount |  |  |
| unitystaffdimension | 统一编制维度 | CheckBoxField | t_haos_staffproject.funitystaffdimension |  |  |
| staffdimension | 编制维度 | MulBasedataField | t_haos_spdimension（子表） |  |  |
| orgentity | 单据体 | EntryEntity | → t_haos_sporgentry |  |  |

### 字段列表 — t_haos_sporgentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorg | 组织名称 | HRAdminOrgField | t_haos_sporgentry.fadminorgid |  | haos_adminorghrf7 |
| iscontainslower | 是否包含下级 | CheckBoxField | t_haos_sporgentry.fiscontainslower |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_staffproject（主表） | 33 |
| t_haos_sporgentry（单据体） | 2 |
| t_haos_spdimension（MulEmployeeField子表） | 1 |
| 无数据库列 | 8 |

