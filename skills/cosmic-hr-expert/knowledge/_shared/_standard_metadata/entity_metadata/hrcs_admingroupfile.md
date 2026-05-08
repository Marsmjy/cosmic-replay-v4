# hrcs_admingroupfile — 权限档案范围

**表单编码**: `hrcs_admingroupfile`  
**表单ID**: `2AMCMSA9ATSE`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_admingroupfile（权限档案范围） [BaseEntity]

- **数据库表**: `t_hrcs_admingroupfile`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| admingroup | 管理员组 | BasedataField | fadmingroupid |  | perm_admingroup |
| org | 业务单元 | OrgField | forgid |  | bos_org |

