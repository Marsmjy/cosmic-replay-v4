# haos_combdimension — 组合维度

**表单编码**: `haos_combdimension`  
**表单ID**: `4K+=BGU6W87/`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_combdimension（组合维度） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_staffdimension` | BaseEntity | 主表 |
| `t_haos_staffflexpropentry` | EntryEntity | 单据体 |
| `t_haos_staffcombdim` | MulEmployeeField子表 | 维度 |

### 字段列表 — t_haos_staffdimension（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_haos_staffdimension.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_haos_staffdimension.fname |  |  |
| status | 数据状态 | BillStatusField | t_haos_staffdimension.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_staffdimension.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_staffdimension.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_haos_staffdimension.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_staffdimension.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_staffdimension.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_staffdimension.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_staffdimension.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_staffdimension.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_staffdimension.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_staffdimension.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_staffdimension.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_haos_staffdimension.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_staffdimension.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_haos_staffdimension.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_haos_staffdimension.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_haos_staffdimension.foriname |  |  |
| combdim | 维度 | MulBasedataField | t_haos_staffcombdim（子表） | ✓ |  |
| iscombdim | 是否组合维度 | CheckBoxField | t_haos_staffdimension.fiscombdim |  |  |
| staffdimension | 编制维度标识 | TextField | t_haos_staffdimension.fstaffdimension |  |  |
| flexpropentryentity | 单据体 | EntryEntity | → t_haos_staffflexpropentry |  |  |

### 字段列表 — t_haos_staffflexpropentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryflexprop | 编制弹性域属性 | BasedataField | t_haos_staffflexpropentry.fentryflexpropid |  | bos_flex_property |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_staffdimension（主表） | 22 |
| t_haos_staffflexpropentry（单据体） | 1 |
| t_haos_staffcombdim（MulEmployeeField子表） | 1 |

