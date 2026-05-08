# hrptmc_colcustomsort — 自定义排序（废弃）

**表单编码**: `hrptmc_colcustomsort`  
**表单ID**: `354F5LK126QB`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_colcustomsort（自定义排序（废弃）） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_colcustomsort` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_colcustomsort（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_colcustomsort.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_colcustomsort.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_colcustomsort.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_colcustomsort.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_colcustomsort.finitdatasource |  |  |
| id | 列字段 | BasedataField | t_hrptmc_colcustomsort.fid |  | hrptmc_colfield |
| bizindex | 序号 | IntegerField | t_hrptmc_colcustomsort.fbizindex |  |  |
| entitynumber | 基础资料实体编码 | TextField | t_hrptmc_colcustomsort.fentitynumber |  |  |
| field | 枚举属性 | TextField | t_hrptmc_colcustomsort.ffield |  |  |
| value | 基础资料ID/枚举值 | TextField | t_hrptmc_colcustomsort.fvalue |  |  |
| fieldtype | 字段类型 | ComboField | t_hrptmc_colcustomsort.ffieldtype |  |  |
| entryentity | 单据体 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_colcustomsort（主表） | 11 |
| （单据体） | 2 |
| 无数据库列 | 1 |

