# haos_staffruleconfig — 编制计划设置

**表单编码**: `haos_staffruleconfig`  
**表单ID**: `2OM+E+Y=NQ7F`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_staffruleconfig（编制计划设置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_haos_staffruleconfig` | BaseEntity | 主表 |
| `t_haos_staffruleentry` | EntryEntity | 单据体 |
| `t_haos_stafforgpermentry` | EntryEntity | 单据体 |
| `t_haos_staffruleorg` | MulEmployeeField子表 | 编制计划组织 |

### 字段列表 — t_haos_staffruleconfig（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_haos_staffruleconfig.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_haos_staffruleconfig.fname |  |  |
| status | 数据状态 | BillStatusField | t_haos_staffruleconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_haos_staffruleconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_haos_staffruleconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_haos_staffruleconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_haos_staffruleconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_haos_staffruleconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_haos_staffruleconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_haos_staffruleconfig.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_haos_staffruleconfig.fdescription |  |  |
| index | 排序号 | IntegerField | t_haos_staffruleconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_haos_staffruleconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_haos_staffruleconfig.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_haos_staffruleconfig.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_haos_staffruleconfig.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_haos_staffruleconfig.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_haos_staffruleconfig.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_haos_staffruleconfig.foriname |  |  |
| year | 编制年份 | DateTimeField | t_haos_staffruleconfig.fyear | ✓ |  |
| org | 组织体系管理组织 | OrgField | t_haos_staffruleconfig.forgid | ✓ | bos_org |
| staffcycle | 填报期间 | BasedataField | t_haos_staffruleconfig.fstaffcycleid | ✓ | haos_staffcycle |
| entryentity | 单据体 | EntryEntity | → t_haos_staffruleentry |  |  |
| orgpermentryentity | 单据体 | EntryEntity | → t_haos_stafforgpermentry |  |  |

### 字段列表 — t_haos_staffruleentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| includesub | 包含下级 | CheckBoxField | t_haos_staffruleentry.fincludesub |  |  |
| staffproject | 默认控编规则 | BasedataField | t_haos_staffruleentry.fstaffprojectid | ✓ | haos_staffproject |
| refstaff | 关联编制信息 | BasedataField | t_haos_staffruleentry.frefstaffid |  | haos_staff |
| staffruleorg | 编制计划组织 | HRMulAdminOrgField | t_haos_staffruleorg（子表） | ✓ |  |

### 字段列表 — t_haos_stafforgpermentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| staffruleorgperm | 编制计划组织 | HRAdminOrgField | t_haos_stafforgpermentry.fstaffruleorgpermid |  | haos_adminorghrf7 |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_staffruleconfig（主表） | 22 |
| t_haos_staffruleentry（单据体） | 4 |
| t_haos_stafforgpermentry（单据体） | 1 |
| t_haos_staffruleorg（MulEmployeeField子表） | 1 |

