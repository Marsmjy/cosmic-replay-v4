# hbp_hismodellog — HR历史模型日志

**表单编码**: `hbp_hismodellog`  
**表单ID**: `4US3XK7X85VJ`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_hismodellog（HR历史模型日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 操作时间 | DateTimeField | — |  |  |
| modifycontent | 修改内容 | TextField | fmodifycontent |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| opobjid | 操作对象id | BigIntField | fopobjid |  |  |
| userid | 操作人id | BigIntField | fuserid |  |  |
| eventid | 事件id | BigIntField | feventid |  |  |
| boid | boid | BigIntField | fboid |  |  |
| optype | 操作类型 | ComboField | foptype |  |  |

