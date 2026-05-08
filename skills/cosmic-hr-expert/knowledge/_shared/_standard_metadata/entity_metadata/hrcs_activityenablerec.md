# hrcs_activityenablerec — 活动启动记录

**表单编码**: `hrcs_activityenablerec`  
**表单ID**: `22NLPNALP07P`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activityenablerec（活动启动记录） [BillEntity]

- **数据库表**: `t_hrcs_activityenablerec`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| activity | 基础资料 | BasedataField | factivityid |  | hrcs_activity |
| createtime | 创建日期 | CreateDateField | fcreatetime |  |  |

