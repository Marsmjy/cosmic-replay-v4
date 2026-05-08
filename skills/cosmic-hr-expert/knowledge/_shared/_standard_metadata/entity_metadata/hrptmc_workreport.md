# hrptmc_workreport — 工作表

**表单编码**: `hrptmc_workreport`  
**表单ID**: `408DEN5YLMNT`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_workreport（工作表） [BaseEntity]

- **数据库表**: `t_hrptmc_workreport`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| row | 行 | TextField | frow |  |  |
| column | 列 | TextField | fcolumn |  |  |
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| index | 顺序 | IntegerField | findex |  |  |
| key | 标识 | IntegerField | fkey |  |  |

