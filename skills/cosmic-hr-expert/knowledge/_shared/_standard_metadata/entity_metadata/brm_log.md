# brm_log — 调用日志

**表单编码**: `brm_log`  
**表单ID**: `526X3MUE/9PI`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_log（调用日志） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| username | 操作人 | TextField | — |  |  |
| opname | 操作名称 | TextField | — |  |  |
| opdesc | 操作描述 | TextField | — |  |  |
| opdate | 调用时间 | DateTimeField | — |  |  |
| execosttime | 耗时 | BigIntField | fexecosttime |  |  |
| traceid | TraceId | TextField | ftraceid |  |  |
| scenename | 场景名称 | TextField | fscenename |  |  |
| isbatch | 调用类型 | ComboField | fisbatch |  |  |
| bizapp | 所属应用 | TextField | fbizapp |  |  |
| responsedesc | 调用状态 | ComboField | fresponsedesc |  |  |
| policyresults | 匹配策略 | LargeTextField | fpolicyresults |  |  |
| responsecode | 返回码 | TextField | fresponsecode |  |  |
| errormsg | 异常信息 | TextField | ferrormsg |  |  |
| batchresult | 批量结果 | TextField | fbatchresult |  |  |

