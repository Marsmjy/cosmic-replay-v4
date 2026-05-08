# hrptmc_anobjjoinentity — 分析对象关联实体

**表单编码**: `hrptmc_anobjjoinentity`  
**表单ID**: `2VJT18CAI32C`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjjoinentity（分析对象关联实体） [BaseEntity]

- **数据库表**: `t_hrptmc_anobjjoinentity`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| entityalias | 实体别名 | TextField | fentityalias |  |  |
| type | 类型 | ComboField | ftype |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| isv | 开发商标识 | TextField | fisv |  |  |
| version | 版本号 | IntegerField | fversion |  |  |
| longnumber | 长编码 | TextField | flongnumber |  |  |

