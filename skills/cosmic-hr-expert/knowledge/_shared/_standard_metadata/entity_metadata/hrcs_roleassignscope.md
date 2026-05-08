# hrcs_roleassignscope — 角色分配范围

**表单编码**: `hrcs_roleassignscope`  
**表单ID**: `2V7750SF2CPA`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleassignscope（角色分配范围） [BaseEntity]

- **数据库表**: `t_hrcs_roleassignscope`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| roleid | 通用角色 | BasedataField | froleid |  | perm_role |
| admingroup | 分配管理员组 | BasedataField | fadmingroupid |  | perm_admingroup |
| ismodifiable | 是否允许修改 | CheckBoxField | fismodifiable |  |  |

