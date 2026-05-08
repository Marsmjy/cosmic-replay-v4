# hrptmc_dimcount — 维度数量记录表

**表单编码**: `hrptmc_dimcount`  
**表单ID**: `3/M7JP9QB459`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_dimcount（维度数量记录表） [BaseEntity]

- **数据库表**: `t_hrptmc_dimcount`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| tablename | 表名 | TextField | ftablename |  |  |
| dimcount | 维度数量 | IntegerField | fdimcount |  |  |
| isbigdatatable | 是否大数据量表 | CheckBoxField | fisbigdatatable |  |  |
| fieldname | 字段名 | TextField | ffieldname |  |  |

