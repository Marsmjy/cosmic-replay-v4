# hrptmc_commonsort — 通用排序

**表单编码**: `hrptmc_commonsort`  
**表单ID**: `35SAL2YTWS4W`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_commonsort（通用排序） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_commonsort` | BaseEntity | 主表 |
| `t_hrptmc_commonsortety` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_commonsort（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_commonsort.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_commonsort.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_commonsort.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_commonsort.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_commonsort.finitdatasource |  |  |
| entitynumber | 基础资料实体编码 | TextField | t_hrptmc_commonsort.fentitynumber |  |  |
| entityfield | 基础资料属性 | TextField | t_hrptmc_commonsort.fentityfield |  |  |
| fieldtype | 字段类型 | ComboField | t_hrptmc_commonsort.ffieldtype |  |  |
| valuetype | 值类型 | ComboField | t_hrptmc_commonsort.fvaluetype |  |  |
| locale | 语言 | TextField | t_hrptmc_commonsort.flocaleid |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrptmc_commonsortety |  |  |

### 字段列表 — t_hrptmc_commonsortety（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| long | 长整数 | BigIntField | t_hrptmc_commonsortety.flong |  |  |
| string | 字符串 | TextField | t_hrptmc_commonsortety.fstring |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_commonsort（主表） | 10 |
| t_hrptmc_commonsortety（单据体） | 2 |

