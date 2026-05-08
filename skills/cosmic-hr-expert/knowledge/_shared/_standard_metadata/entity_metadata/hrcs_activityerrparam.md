# hrcs_activityerrparam — 异常活动调用记录

**表单编码**: `hrcs_activityerrparam`  
**表单ID**: `2OF91Q4YP7H4`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityerrparam（异常活动调用记录） [BillEntity]

- **数据库表**: `t_hrcs_activityerrparam`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creater | 创建人 | CreaterField | fcreater |  | bos_user |
| createdate | 创建日期 | CreateDateField | fcreatedate |  |  |
| modifier | 修改人 | ModifierField | fmodifier |  | bos_user |
| modifydate | 修改日期 | ModifyDateField | fmodifydate |  |  |
| param | 参数 | LargeTextField | fparam |  |  |
| exceptionid | 异常ID | BigIntField | fexceptionid |  |  |

