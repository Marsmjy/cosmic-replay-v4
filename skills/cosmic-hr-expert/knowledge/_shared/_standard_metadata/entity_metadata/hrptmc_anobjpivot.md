# hrptmc_anobjpivot — 分析对象行列转置信息

**表单编码**: `hrptmc_anobjpivot`  
**表单ID**: `3SBTXR8I1REA`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjpivot（分析对象行列转置信息） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjpivot`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| pivotdimval | 转置维度值 | TextField | fpivotdimval |  |  |
| pivotindex | 被转置的指标别名 | TextField | fpivotindex |  |  |
| pivotdim | 转置维度别名 | TextField | fpivotdim |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| pivotindexnum | 转置后的生成的指标编码 | TextField | fpivotindexnum |  |  |

