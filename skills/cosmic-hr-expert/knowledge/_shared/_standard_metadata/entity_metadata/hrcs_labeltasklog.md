# hrcs_labeltasklog — 标签打标任务日志

**表单编码**: `hrcs_labeltasklog`  
**表单ID**: `2YS/YV/LN199`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labeltasklog（标签打标任务日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 操作时间 | DateTimeField | — |  |  |
| logid | 日志ID | TextField | — |  |  |
| processid | 日志节点ID | TextField | — |  |  |
| processtype | 日志节点类型 | TextField | — |  |  |
| parentid | 日志父节点 | TextField | — |  |  |
| loglevel | 日志级别 | TextField | — |  |  |
| classname | 日志类名 | TextField | — |  |  |
| userid | 操作人ID | BigIntField | — |  |  |
| bizobjid | 源业务对象ID | TextField | — |  |  |
| logmessage | 日志内容 | LargeTextField | — |  |  |

