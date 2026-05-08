# hrptmc_reportmark — 报表说明配置

**表单编码**: `hrptmc_reportmark`  
**表单ID**: `3982YW2BF7O2`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportmark（报表说明配置） [BaseEntity]

- **数据库表**: `t_hrptmc_reportmark`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| show | 是否显示说明 | RadioGroupField | fshow | ✓ |  |
| radiofield | 显示 | RadioField | — |  |  |
| radiofield1 | 不显示 | RadioField | — |  |  |
| locale | 语言环境 | ComboField | — | ✓ |  |
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |

