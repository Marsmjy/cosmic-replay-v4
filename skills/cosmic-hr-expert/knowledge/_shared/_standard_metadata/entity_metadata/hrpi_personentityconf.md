# hrpi_personentityconf — 人员模型配置信息（废弃）

**表单编码**: `hrpi_personentityconf`  
**表单ID**: `2/9EQRTLMT74`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_personentityconf（人员模型配置信息（废弃）） [BaseEntity]

- **数据库表**: `t_hrpi_personentityconf`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | TextField | fname |  |  |
| entity | 实体 | TextField | fentity |  |  |
| classify | 分类 | ComboField | fclassify |  |  |

