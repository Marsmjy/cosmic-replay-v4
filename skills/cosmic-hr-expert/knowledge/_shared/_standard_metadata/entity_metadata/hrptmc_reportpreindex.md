# hrptmc_reportpreindex — 报表关联预置指标

**表单编码**: `hrptmc_reportpreindex`  
**表单ID**: `2Z6OXAU04Q46`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportpreindex（报表关联预置指标） [BaseEntity]

- **数据库表**: `t_hrptmc_reportpreindex`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| preindex | 预置指标 | BasedataField | fpreindexid |  | hrptmc_preindex |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |

