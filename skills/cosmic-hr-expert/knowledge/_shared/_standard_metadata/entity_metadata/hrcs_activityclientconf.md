# hrcs_activityclientconf — 活动编排-业务方MQ配置

**表单编码**: `hrcs_activityclientconf`  
**表单ID**: `21D2BCJ9/Q9Z`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityclientconf（活动编排-业务方MQ配置） [BaseEntity]

- **数据库表**: `t_hrcs_activityclient`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bizobj | 业务对象 | BasedataField | fbizobjid | ✓ | bos_entityobject |
| cloud | 业务云 | BasedataField | fcloudid | ✓ | bos_devportal_bizcloud |
| queuename | MQ队列 | TextField | fqueuename |  |  |
| transtatus | 事务状态 | ComboField | ftranstatus |  |  |

