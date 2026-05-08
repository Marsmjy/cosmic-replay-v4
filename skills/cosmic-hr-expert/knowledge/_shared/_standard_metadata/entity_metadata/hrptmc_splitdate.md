# hrptmc_splitdate — 日期字段拆分粒度表

**表单编码**: `hrptmc_splitdate`  
**表单ID**: `3N+QQDQVCW6N`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_splitdate（日期字段拆分粒度表） [BaseEntity]

- **数据库表**: `t_hrptmc_splitdate`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 字段类型 | TextField | fnumber |  |  |
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| anobjfield | 分析对象字段 | BasedataField | fanobjfieldid |  | hrptmc_anobjqueryfield |

