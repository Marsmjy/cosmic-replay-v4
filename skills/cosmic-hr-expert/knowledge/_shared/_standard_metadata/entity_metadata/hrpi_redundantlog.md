# hrpi_redundantlog — 冗余字段更新日志

**表单编码**: `hrpi_redundantlog`  
**表单ID**: `5A6LDPN2N38O`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_redundantlog（冗余字段更新日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 操作时间 | DateTimeField | — |  |  |
| fieldkey | 实体字段 | TextField | ffieldkey |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| beforevalue | 修改前值 | TextField | fbeforevalue |  |  |
| aftervalue | 修改后值 | TextField | faftervalue |  |  |
| dataid | 数据id | BigIntField | fdataid |  |  |

