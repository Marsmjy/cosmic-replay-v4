# hrptmc_dimensioncount — 维度计数（废弃）

**表单编码**: `hrptmc_dimensioncount`  
**表单ID**: `3914LAN/K0L+`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_dimensioncount（维度计数（废弃）） [BaseEntity]

- **数据库表**: `t_hrptmc_dimensioncount`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| type | 计数类型 | ComboField | ftype |  |  |
| field | 分析对象查询字段 | BasedataField | ffieldid |  | hrptmc_anobjqueryfield |

