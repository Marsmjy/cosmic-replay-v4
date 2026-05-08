# hrcs_ignorepermentity — 忽略验权实体

**表单编码**: `hrcs_ignorepermentity`  
**表单ID**: `18ICWDE3HLTN`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_ignorepermentity（忽略验权实体） [BaseEntity]

- **数据库表**: `t_hrcs_ignorepermentity`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| app | 应用ID | TextField | fappid | ✓ |  |
| entitytype | 实体编码 | TextField | fentitytypeid | ✓ |  |
| isskipdatarule | 是否忽略行权限 | CheckBoxField | fisskipdatarule |  |  |
| isskipfield | 是否忽略列权限 | CheckBoxField | fisskipfield |  |  |

