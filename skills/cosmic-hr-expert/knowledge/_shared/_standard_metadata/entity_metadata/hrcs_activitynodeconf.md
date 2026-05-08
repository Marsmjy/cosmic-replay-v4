# hrcs_activitynodeconf — HR节点处理设置

**表单编码**: `hrcs_activitynodeconf`  
**表单ID**: `4CHJ1ML+YC4W`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_activitynodeconf（HR节点处理设置） [BaseEntity]

- **数据库表**: `t_hrcs_activitynodeconf`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| nodenumber | 节点编码 | TextField | fnodenumber |  |  |
| setting | 节点设置 | TextField | fsetting |  |  |

