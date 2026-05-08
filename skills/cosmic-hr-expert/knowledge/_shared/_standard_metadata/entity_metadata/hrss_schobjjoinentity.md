# hrss_schobjjoinentity — 搜索对象关联实体

**表单编码**: `hrss_schobjjoinentity`  
**表单ID**: `3TYSRB+Z/88R`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_schobjjoinentity（搜索对象关联实体） [BaseEntity]

- **数据库表**: `t_hrss_schobjentity`  

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
| longnumber | 长编码 | TextField | flongnumber |  |  |
| searchobj | 搜索对象 | BasedataField | fsearchobjid |  | hrss_searchobject |
| searchtarget | 是否搜索标签 | CheckBoxField | fsearchtarget |  |  |
| entityname | 实体名称 | MuliLangTextField | fentityname |  |  |

