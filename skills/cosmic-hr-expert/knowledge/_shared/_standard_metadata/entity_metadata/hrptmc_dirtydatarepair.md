# hrptmc_dirtydatarepair — 抽取脏数据修复

**表单编码**: `hrptmc_dirtydatarepair`  
**表单ID**: `42=6Y70JJ3=1`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_dirtydatarepair（抽取脏数据修复） [BaseEntity]

- **数据库表**: `t_hrptmc_dirtydatarepair`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitynumber | 元数据编码 | TextField | fentitynumber |  |  |
| count | 命中次数 | IntegerField | fcount |  |  |
| table | 表名 | TextField | ftable |  |  |

