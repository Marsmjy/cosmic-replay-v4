# hrptmc_filesourceenum — 文件数据源枚举

**表单编码**: `hrptmc_filesourceenum`  
**表单ID**: `3W38J0S/9KPC`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_filesourceenum（文件数据源枚举） [BaseEntity]

- **数据库表**: `t_hrptmc_fsenum`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| tablename | 物理表名 | TextField | ftablename |  |  |
| fieldname | 字段名 | TextField | ffieldname |  |  |
| enum | 枚举值 | TextField | fenum |  |  |
| index | 排序号(用于自定义排序) | IntegerField | findex |  |  |

