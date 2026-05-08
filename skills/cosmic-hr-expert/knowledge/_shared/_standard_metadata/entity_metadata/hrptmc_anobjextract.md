# hrptmc_anobjextract — 分析对象数据抽取配置存储

**表单编码**: `hrptmc_anobjextract`  
**表单ID**: `408Y/BLWRR81`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjextract（分析对象数据抽取配置存储） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjextract`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| metadatanum | 落地生成元数据编码 | TextField | fmetadatanum |  |  |
| cronexpr | cron表达式 | TextField | fcronexpr |  |  |
| runstatus | 运行状态 | ComboField | frunstatus |  |  |
| openextract | 是否开启调度 | CheckBoxField | fopenextract |  |  |
| executenow | 保存后立即执行 | CheckBoxField | fexecutenow |  |  |
| scheplan | 调度计划 | BasedataField | fscheplan |  | sch_schedule |
| schejob | 调度任务 | BasedataField | fschejob |  | sch_job |
| cycle | 调度周期 | ComboField | fcycle |  |  |
| scheplanlast | 调度计划（L） | BasedataField | fscheplanlast |  | sch_schedule |

