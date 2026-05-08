# hrcs_rolebu — 角色业务单元范围

**表单编码**: `hrcs_rolebu`  
**表单ID**: `0SFJW2=VM=30`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_rolebu（角色业务单元范围） [BaseEntity]

- **数据库表**: `t_hbss_hrrolebu`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| org | 组织 | BasedataField | forgid |  | bos_org |
| containssub | 是否包含下级 | CheckBoxField | fcontainssub |  |  |
| role | 角色 | TextField | fid |  |  |
| hrbucafunc | 业务职能Id | BigIntField | fbucafuncid |  |  |
| hrbuca | 管控架构ID | BigIntField | fhrbucaid |  |  |

