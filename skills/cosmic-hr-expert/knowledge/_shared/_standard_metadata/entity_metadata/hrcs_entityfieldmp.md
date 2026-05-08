# hrcs_entityfieldmp — 实体字段取值关系

**表单编码**: `hrcs_entityfieldmp`  
**表单ID**: `3EQGW71YI1JZ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entityfieldmp（实体字段取值关系） [BaseEntity]

- **数据库表**: `t_hrcs_entityfieldmp`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| name | 字段名称 | TextField | fname |  |  |
| number | 字段编码 | TextField | fnumber |  |  |
| entityname | 所属实体 | TextField | fentityname |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| mulilang | 多语言标识 | TextField | — |  |  |

