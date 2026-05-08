# hrcs_keywordmapping — 模板变量取值关系配置

**表单编码**: `hrcs_keywordmapping`  
**表单ID**: `2AUM+LQ+SQZX`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_keywordmapping（模板变量取值关系配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_keywordmapping` | BaseEntity | 主表 |
| `t_hrcs_keymappingentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_keywordmapping（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| formnumber | 模板实体编码 | TextField | t_hrcs_keywordmapping.fformnumber |  |  |
| contempid | 业务模板ID | BigIntField | t_hrcs_keywordmapping.fcontempid |  |  |
| upgrad | 数据升级标识 | TextField | t_hrcs_keywordmapping.fupgrad |  |  |
| contempmainid | 模板主ID | BigIntField | t_hrcs_keywordmapping.fcontempmainid |  |  |
| creator | 创建人 | CreaterField | t_hrcs_keywordmapping.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_keywordmapping.fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_keywordmapping.fcreatetime |  |  |
| modifytime | 修改日期 | ModifyDateField | t_hrcs_keywordmapping.fmodifytime |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_keymappingentry |  |  |

### 字段列表 — t_hrcs_keymappingentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entityname | 实体名 | TextField | t_hrcs_keymappingentry.fentityname |  |  |
| entityfield | 实体字段编码 | TextField | t_hrcs_keymappingentry.fentityfield |  |  |
| entitynumber | 实体编码 | TextField | t_hrcs_keymappingentry.fentitynumber |  |  |
| entityfieldname | 实体字段名 | TextField | t_hrcs_keymappingentry.fentityfieldname |  |  |
| wordfield | 模板变量 | TextField | t_hrcs_keymappingentry.fwordfield |  |  |
| isenablekeyword | 格式检查 | ComboField | — |  |  |
| relateentityid | 关联实体ID | BigIntField | t_hrcs_keymappingentry.frelateentityid |  |  |
| filtercondition | 过滤条件 | TextField | t_hrcs_keymappingentry.ffiltercondition |  |  |
| iscustomize | 是否自定义 | TextField | t_hrcs_keymappingentry.fiscustomize |  |  |
| customizekey | 映射Key | TextField | t_hrcs_keymappingentry.fcustomizekey |  |  |
| fieldmapping | 模板变量取值关系 | TextField | — |  |  |
| variableid | 常用变量 | BasedataField | t_hrcs_keymappingentry.fvariableid |  | hrcs_commonvariable |
| multlang | 语种 | TextField | t_hrcs_keymappingentry.fmultlang |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_keywordmapping（主表） | 8 |
| t_hrcs_keymappingentry（单据体） | 13 |
| 无数据库列 | 2 |

