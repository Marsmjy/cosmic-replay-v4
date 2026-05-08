# hrcs_warnscenejoinentity — 预警场景关联实体

**表单编码**: `hrcs_warnscenejoinentity`  
**表单ID**: `3V/ZOHTVR6ZK`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnscenejoinentity（预警场景关联实体） [BaseEntity]

- **数据库表**: `t_hrcs_warnobjectentity`  

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
| entityname | 实体名称 | MuliLangTextField | fentityname |  |  |
| longnumber | 长编码 | TextField | flongnumber |  |  |
| version | 版本号 | IntegerField | fversion |  |  |
| sourceid | 引用来源id | BigIntField | fsourceid |  |  |
| source | 字段来源 | ComboField | fsource |  |  |
| iscore | 是否核心对象 | CheckBoxField | fiscore |  |  |

