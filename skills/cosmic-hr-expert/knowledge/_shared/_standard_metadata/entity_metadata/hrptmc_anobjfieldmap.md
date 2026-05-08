# hrptmc_anobjfieldmap — 分析对象落地字段映射

**表单编码**: `hrptmc_anobjfieldmap`  
**表单ID**: `40=W34YQ=00+`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjfieldmap（分析对象落地字段映射） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjfieldmap`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| fieldalias | 分析对象字段别名 | TextField | ffieldalias |  |  |
| fieldnum | 元数据字段标识 | TextField | ffieldnum |  |  |
| controltype | 元数据字段控件类型 | ComboField | fcontroltype |  |  |
| tablefieldtype | 表字段类型 | ComboField | ftablefieldtype |  |  |
| fieldlength | 文本类型字段长度 | IntegerField | ffieldlength |  |  |
| privacysttus | 隐私字段状态 | ComboField | fprivacysttus |  |  |
| fieldname | 物理表字段名 | TextField | ffieldname |  |  |

