# hrcs_role — HR通用角色

**表单编码**: `hrcs_role`  
**表单ID**: `0RMF38CCFKWX`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_role（HR通用角色） [BaseEntity]

- **数据库表**: `t_hbss_permrole`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rolegrp | 角色组 | BasedataField | fid |  | hrcs_rolegrp |
| property | 角色属性 | ComboField | fproperty |  |  |
| isintersection | 数据规则是否取交集 | ComboField | fisintersection |  |  |
| name | 名称 | TextField | fname |  |  |
| number | 编码 | TextField | fnumber |  |  |
| createadmingrp | 所属管理员组 | BasedataField | fcreateadmingrp |  | perm_admingroup |
| usescope | 公开状态 | ComboField | fusescope | ✓ |  |

