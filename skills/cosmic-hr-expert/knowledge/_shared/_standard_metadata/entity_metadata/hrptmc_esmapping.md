# hrptmc_esmapping — es映射

**表单编码**: `hrptmc_esmapping`  
**表单ID**: `48P=8GVGSLV1`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_esmapping（es映射） [BaseEntity]

- **数据库表**: `t_hrptmc_esmapping`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| numberalias | 报表字段 | TextField | fnumberalias |  |  |
| fieldalias | 分析对象字段 | TextField | ffieldalias |  |  |
| esfieldkey | es字段 | TextField | fesfieldkey |  |  |
| esindex | es索引 | BasedataField | fesindexid |  | hrptmc_esindex |

