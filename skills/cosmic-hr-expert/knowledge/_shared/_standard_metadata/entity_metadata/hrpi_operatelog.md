# hrpi_operatelog — 员工信息日志

**表单编码**: `hrpi_operatelog`  
**表单ID**: `4Z/EIN=LITIM`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_operatelog（员工信息日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 操作时间 | DateTimeField | — |  |  |
| userid | 操作人id | BigIntField | fuserid |  |  |
| eventid | 事件id | BigIntField | feventid |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| opobjid | 操作对象id | BigIntField | fopobjid |  |  |
| modifycontent | 修改内容 | TextField | fmodifycontent |  |  |
| bizcustomval | 业务参数 | TextField | fbizcustomval |  |  |

