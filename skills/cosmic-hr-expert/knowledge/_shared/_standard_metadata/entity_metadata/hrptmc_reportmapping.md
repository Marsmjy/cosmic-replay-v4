# hrptmc_reportmapping — 报表抽取映射

**表单编码**: `hrptmc_reportmapping`  
**表单ID**: `4/F55M7/8YJ/`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportmapping（报表抽取映射） [BaseEntity]

- **数据库表**: `t_hrptmc_reportmapping`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| numberalias | 报表对应字段的标识 | TextField | fnumberalias |  |  |
| metadatafieldkey | 新元数据字段标识 | TextField | fmetadatafieldkey |  |  |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |
| metadatacontrolkey | 新元数据控件类型 | TextField | fmetadatacontrolkey |  |  |
| tablefieldtype | 新表字段类型 | TextField | ftablefieldtype |  |  |
| privatefieldstatus | 隐私字段状态 | ComboField | fprivatefieldstatus |  |  |
| tablefieldlength | 新表字段长度 | IntegerField | ftablefieldlength |  |  |
| fieldalias | 报表所属分析对象对应字段的标识 | TextField | ffieldalias |  |  |

