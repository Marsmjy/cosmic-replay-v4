# hrcs_warnexeclog — 预警执行日志

**表单编码**: `hrcs_warnexeclog`  
**表单ID**: `5K4MYV8VH63/`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnexeclog（预警执行日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 操作时间 | DateTimeField | — |  |  |
| schemename | 预警方案名称 | TextField | fschemename |  |  |
| scheme | 预警方案ID | BigIntField | fschemeid | ✓ |  |
| warnscene | 预警场景ID | BigIntField | fwarnsceneid | ✓ |  |
| exectype | 执行类型 | ComboField | fexectype |  |  |
| execstatus | 状态 | ComboField | fexecstatus |  |  |
| starttime | 开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 结束时间 | DateTimeField | fendtime |  |  |
| exectime | 执行时长 | TextField | fexectime |  |  |
| user | 用户 | BigIntField | fuserid |  |  |
| concurrentid | 限流ID | TextField | fconcurrentid |  |  |
| limitstatus | 限流状态 | ComboField | flimitstatus |  |  |
| schemenumber | 预警方案编码 | TextField | fschemenumber |  |  |
| execresult | 执行结果 | TextField | fexecresult |  |  |

