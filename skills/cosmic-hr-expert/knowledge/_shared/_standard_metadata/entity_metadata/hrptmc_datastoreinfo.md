# hrptmc_datastoreinfo — 数据落地信息查询

**表单编码**: `hrptmc_datastoreinfo`  
**表单ID**: `4NWJPQL45WDB`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_datastoreinfo（数据落地信息查询） [BaseEntity]

- **数据库表**: `t_hrptmc_datastoreinfo`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| datatype | 数据类型 | ComboField | fdatatype |  |  |
| biztype | 落地类型 | ComboField | fbiztype |  |  |
| cycle | 落地周期 | ComboField | fcycle |  |  |
| storemode | 存储方式 | ComboField | fstoremode |  |  |
| storekey | 存储位置 | TextField | fstorekey |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |

