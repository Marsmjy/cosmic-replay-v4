# hrcs_lbljoinentity — 标签关联实体

**表单编码**: `hrcs_lbljoinentity`  
**表单ID**: `2VJZ=6Q12PRB`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_lbljoinentity（标签关联实体） [BaseEntity]

- **数据库表**: `t_hrcs_lbljoinentity`  

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
| labelobject | 打标对象 | BasedataField | flabelobjectid |  | hrcs_labelobject |
| ismainbo | 是否主打标对象 | CheckBoxField | fismainbo |  |  |

