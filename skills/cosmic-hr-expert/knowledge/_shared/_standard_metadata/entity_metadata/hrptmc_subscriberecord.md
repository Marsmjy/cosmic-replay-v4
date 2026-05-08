# hrptmc_subscriberecord — 订阅记录

**表单编码**: `hrptmc_subscriberecord`  
**表单ID**: `48+4Q61=6JUA`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_subscriberecord（订阅记录） [BaseEntity]

- **数据库表**: `t_hrptmc_subscriberecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| cycle | 订阅周期 | ComboField | fcycle |  |  |
| cron | cron表达式 | TextField | fcron |  |  |
| cronlast | cron表达式（L） | TextField | fcronlast |  |  |
| scheplan | 调度计划 | BasedataField | fscheplanid |  | sch_schedule |
| scheplanlast | 调度计划（L） | BasedataField | fscheplanlastid |  | sch_schedule |
| schejob | 调度作业 | BasedataField | fschejobid |  | sch_job |
| runstatus | 任务执行状态 | ComboField | frunstatus |  |  |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |
| user | 订阅人 | UserField | fuser |  | bos_user |
| reportgroup | 报表中心分组 | GroupField | freportgroup |  | hrptc_reportgroup |

