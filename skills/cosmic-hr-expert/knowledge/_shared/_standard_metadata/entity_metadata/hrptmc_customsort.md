# hrptmc_customsort — 自定义排序

**表单编码**: `hrptmc_customsort`  
**表单ID**: `35R4=RKY0BY5`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_customsort（自定义排序） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_customsort` | BaseEntity | 主表 |
| `t_hrptmc_customsortety` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_customsort（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_customsort.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_customsort.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_customsort.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_customsort.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_customsort.finitdatasource |  |  |
| rptmanage | 报表 | BasedataField | t_hrptmc_customsort.frptmanageid |  | hrptmc_reportmanage |
| rptfieldid | 报表字段 | BigIntField | t_hrptmc_customsort.frptfieldid |  |  |
| entitynumber | 基础资料实体编码 | TextField | t_hrptmc_customsort.fentitynumber |  |  |
| entityfield | 基础资料属性 | TextField | t_hrptmc_customsort.fentityfield |  |  |
| fieldtype | 字段类型 | ComboField | t_hrptmc_customsort.ffieldtype |  |  |
| valuetype | 值类型 | ComboField | t_hrptmc_customsort.fvaluetype |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_customsort.fworkrptid |  | hrptmc_workreport |
| entryentity | 单据体 | EntryEntity | → t_hrptmc_customsortety |  |  |

### 字段列表 — t_hrptmc_customsortety（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| long | 长整数值 | BigIntField | t_hrptmc_customsortety.flong |  |  |
| string | 字符串值 | TextField | t_hrptmc_customsortety.fstring |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_customsort（主表） | 12 |
| t_hrptmc_customsortety（单据体） | 2 |

