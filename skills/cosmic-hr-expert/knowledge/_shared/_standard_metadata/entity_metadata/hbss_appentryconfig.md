# hbss_appentryconfig — 应用入口配置

**表单编码**: `hbss_appentryconfig`  
**表单ID**: `2CRS8ROUMR=/`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_appentryconfig（应用入口配置） [BaseEntity]

- **数据库表**: `t_hbss_appentryconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 应用编码 | TextField | fnumber |  |  |
| name | 应用名称 | MuliLangTextField | fname |  |  |
| subtitle | 副标题 | MuliLangTextField | fsubtitle |  |  |
| metanumber | 元数据标识 | TextField | fmetanumber |  |  |
| appicon | 应用图标 | PictureField | fappicon |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| metatype | 元数据类型 | ComboField | fmetatype |  |  |

