# hrcs_relateentity — 关联实体

**表单编码**: `hrcs_relateentity`  
**表单ID**: `36REFQHN/9DC`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_relateentity（关联实体） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_relateentity` | BaseEntity | 主表 |
| `t_hrcs_relateentityentry` | EntryEntity | 单据体 |
| `t_hrcs_entitymapentry` | EntryEntity | 配置实体关系 |
| `t_hrcs_entitymapsubentry` | SubEntryEntity | 子单据体 |

### 字段列表 — t_hrcs_relateentity（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_relateentity.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_relateentity.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_relateentity.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_relateentity.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_relateentity.finitdatasource |  |  |
| contempid | 业务模板ID | BigIntField | t_hrcs_relateentity.fcontempid |  |  |
| mainentity | 主实体 | BasedataField | t_hrcs_relateentity.fmainentity |  | hbp_entityobject |
| upgrad | 数据升级标识 | TextField | t_hrcs_relateentity.fupgrad |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_relateentityentry |  |  |
| entitymappingconfig | 配置实体关系 | EntryEntity | → t_hrcs_entitymapentry |  |  |

### 字段列表 — t_hrcs_relateentityentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entityname | 实体 | BasedataField | t_hrcs_relateentityentry.fentityname |  | hbp_entityobject |

### 字段列表 — t_hrcs_entitymapentry（配置实体关系·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| parententity | 实体 | BasedataField | t_hrcs_entitymapentry.fparententity |  | hbp_entityobject |
| entitycondition | 条件 | TextField | — |  |  |
| entity | 实体 | BasedataField | t_hrcs_entitymapentry.fentity |  | hbp_entityobject |
| subentryentity | 子单据体 | SubEntryEntity | → t_hrcs_entitymapsubentry |  |  |

### 字段列表 — t_hrcs_entitymapsubentry（子单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| parententityfield | 实体字段 | TextField | t_hrcs_entitymapsubentry.fparententityfield |  |  |
| fieldcondition | 条件 | TextField | — |  |  |
| entityfield | 关联实体字段 | TextField | t_hrcs_entitymapsubentry.fentityfield |  |  |
| parententityfieldname | 实体字段名称 | TextField | — |  |  |
| entityfieldname | 关联实体字段名称 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_relateentity（主表） | 8 |
| t_hrcs_relateentityentry（单据体） | 1 |
| t_hrcs_entitymapentry（配置实体关系） | 3 |
| t_hrcs_entitymapsubentry（子单据体） | 5 |
| 无数据库列 | 4 |

