# hrptmc_mysubscribe — 我的订阅

**表单编码**: `hrptmc_mysubscribe`  
**表单ID**: `48+DFK4+PK4L`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_mysubscribe（我的订阅） [BaseEntity]

- **数据库表**: `t_hrptmc_mysubscribe`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 生成时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| subscriberecord | 订阅记录 | BasedataField | fsubscriberecord |  | hrptmc_subscriberecord |
| user | 订阅人 | UserField | fuser |  | bos_user |
| starttime | 实际开始时间 | DateTimeField | fstarttime |  |  |
| endtime | 实际结束时间 | DateTimeField | fendtime |  |  |
| esindex | es索引 | BasedataField | fesindexid |  | hrptmc_esindex |
| count | 已同步数量 | IntegerField | fcount |  |  |
| runstatus | 执行状态 | ComboField | frunstatus |  |  |
| traceid | traceId | TextField | ftraceid |  |  |
| errormsg | 错误信息 | TextField | ferrormsg |  |  |
| dataqfilter | 数据权限过滤QFilter | TextField | fdataqfilter |  |  |
| f7qfilter | F7权限过滤QFilter | TextField | ff7qfilter |  |  |
| name | 订阅名称 | MuliLangTextField | fname |  |  |
| cycle | 订阅周期 | ComboField | fcycle |  |  |
| report | 报表id | BigIntField | freportid |  |  |
| admorgf7qfilter | 行政组织F7权限QFitler | TextField | fadmorgf7qfilter |  |  |

