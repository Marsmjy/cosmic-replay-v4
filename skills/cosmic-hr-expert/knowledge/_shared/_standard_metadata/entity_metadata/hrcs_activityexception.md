# hrcs_activityexception — 异常监控

**表单编码**: `hrcs_activityexception`  
**表单ID**: `2M7O85T8I3J6`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityexception（异常监控） [BillEntity]

- **数据库表**: `t_hrcs_activityexception`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| createtime | 异常创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 最后处理时间 | ModifyDateField | fmodifytime |  |  |
| result | 处理结果 | ComboField | fresult |  |  |
| activityins | 活动任务实例 | BasedataField | factivityinsid |  | hrcs_activityins |
| creator | 发起人 | CreaterField | fcreatorid |  | bos_user |
| exceptiontype | 异常分类 | ComboField | fexceptiontype |  |  |
| modifier | 处理人 | UserField | fmodifierid |  | bos_user |

