# hrcs_permfilegrpmember — 权限档案组明细表

**表单编码**: `hrcs_permfilegrpmember`  
**表单ID**: `0RMF0LY4=6F8`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_permfilegrpmember（权限档案组明细表） [BaseEntity]

- **数据库表**: `t_hbss_permfilegrpmember`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| permfile | 权限档案 | BasedataField | fpermfileid |  | hrcs_userpermfile |
| permfilegrp | 权限档案组 | BasedataField | fgroupid |  | hrcs_permfilegrp |

