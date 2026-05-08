# hrpi_sortcoderegister — 排序码注册列表

**表单编码**: `hrpi_sortcoderegister`  
**表单ID**: `5IG71V9JL1RI`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_sortcoderegister（排序码注册列表） [BaseEntity]

- **数据库表**: `t_hrpi_sortcoderegister`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| index | 序号 | IntegerField | findex | ✓ |  |
| classname | 服务类名 | TextField | fclassname | ✓ |  |
| providertype | 类别 | ComboField | fprovidertype | ✓ |  |
| enable | 是否可用 | CheckBoxField | fenable |  |  |
| name | 名称 | TextField | fname | ✓ |  |

