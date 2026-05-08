# hrcs_entityforbid — 禁用权限

**表单编码**: `hrcs_entityforbid`  
**表单ID**: `294WL3RKR1YB`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entityforbid（禁用权限） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_entityforbid` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 单据体 |
| `（虚拟分录）` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_entityforbid（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_entityforbid.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_entityforbid.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_entityforbid.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_entityforbid.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_entityforbid.finitdatasource |  |  |
| entitytypeid | 业务对象 | BasedataField | t_hrcs_entityforbid.fentitytypeid |  | bos_entityobject |
| app | 应用 | BasedataField | t_hrcs_entityforbid.fappid |  | bos_devportal_bizapp |
| forbidtype | 禁用类型 | ComboField | t_hrcs_entityforbid.fforbidtype |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_entityforbid.fissyspreset |  |  |
| permitem | 权限项 | BasedataField | t_hrcs_entityforbid.fpermitemid |  | perm_permitem |
| entryentity | 单据体 | EntryEntity | → （虚拟分录） |  |  |
| assignedentryentity | 单据体 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_entityforbid（主表） | 10 |
| （单据体） | 8 |
| （单据体） | 8 |
| 无数据库列 | 8 |

