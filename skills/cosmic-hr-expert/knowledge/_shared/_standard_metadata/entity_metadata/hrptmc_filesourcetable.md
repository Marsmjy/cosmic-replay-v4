# hrptmc_filesourcetable — 文件数据源物理表信息

**表单编码**: `hrptmc_filesourcetable`  
**表单ID**: `3V6=M+JU75P1`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_filesourcetable（文件数据源物理表信息） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_fstable` | BaseEntity | 主表 |
| `t_hrptmc_fsfield` | EntryEntity | 物理字段信息 |

### 字段列表 — t_hrptmc_fstable（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_fstable.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_fstable.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_fstable.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_fstable.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_fstable.finitdatasource |  |  |
| tablename | 物理表名 | TextField | t_hrptmc_fstable.ftablename |  |  |
| anobj | 分析对象 | BasedataField | t_hrptmc_fstable.fanobjid |  | hrptmc_analyseobject |
| virtualobj | 虚拟对象 | BasedataField | t_hrptmc_fstable.fvirtualobjid |  | hrptmc_virtualentity |
| fields | 物理字段信息 | EntryEntity | → t_hrptmc_fsfield |  |  |

### 字段列表 — t_hrptmc_fsfield（物理字段信息·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 表字段名 | TextField | t_hrptmc_fsfield.ffieldname |  |  |
| displayname | 字段显示名 | TextField | t_hrptmc_fsfield.fdisplayname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_fstable（主表） | 8 |
| t_hrptmc_fsfield（物理字段信息） | 2 |

