# hrcs_useradmingroup — HR管理员

**表单编码**: `hrcs_useradmingroup`  
**表单ID**: `2BFJAVQXE6R3`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_useradmingroup（HR管理员） [BaseEntity]

- **数据库表**: `t_perm_useradmingroup`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| user | 用户 | UserField | fuserid |  | bos_user |
| usergroup | 管理员分组 | BasedataField | fadmingroupid |  | perm_admingroup |

