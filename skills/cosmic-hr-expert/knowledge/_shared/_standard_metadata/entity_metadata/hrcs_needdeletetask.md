# hrcs_needdeletetask — 需要清除的定时任务白名单

**表单编码**: `hrcs_needdeletetask`  
**表单ID**: `4BF6LOT2TAQW`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_needdeletetask（需要清除的定时任务白名单） [BaseEntity]

- **数据库表**: `t_hrcs_needdeletetask`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| schedule | 待清除调度计划 | BasedataField | fschedule |  | sch_schedule |
| deletejob | 是否清理关联的调度作业 | CheckBoxField | fdeletejob |  |  |
| updatestatus | 更新状态 | ComboField | fupdatestatus |  |  |
| errormsg | 错误信息 | TextField | ferrormsg |  |  |

