# hrss_schobjqueryfield — 搜索对象查询字段

**表单编码**: `hrss_schobjqueryfield`  
**表单ID**: `3TYSYWD99YIY`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_schobjqueryfield（搜索对象查询字段） [BaseEntity]

- **数据库表**: `t_hrss_schobjfield`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitynumber | 所属实体编码 | TextField | fentitynumber |  |  |
| fieldname | 字段名称 | MuliLangTextField | ffieldname |  |  |
| fieldalias | 字段别名 | TextField | ffieldalias |  |  |
| fieldpath | 字段全路径编码 | TextField | ffieldpath |  |  |
| valuetype | 字段值类型 | ComboField | fvaluetype |  |  |
| complextype | 复杂类型 | ComboField | fcomplextype |  |  |
| searchobj | 搜索对象 | BasedataField | fsearchobjid |  | hrss_searchobject |
| controltype | 控件类型 | ComboField | fcontroltype |  |  |

