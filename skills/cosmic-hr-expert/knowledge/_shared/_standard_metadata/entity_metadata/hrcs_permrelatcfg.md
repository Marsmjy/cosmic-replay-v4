# hrcs_permrelatcfg — 独立授权

**表单编码**: `hrcs_permrelatcfg`  
**表单ID**: `2KOO0O61IMMJ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_permrelatcfg（独立授权） [BaseEntity]

- **数据库表**: `t_hrcs_permrelatcfg`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| app | 应用 | BasedataField | fappid |  | hbp_devportal_bizapp |
| entitytype | 实体编码 | BasedataField | fentitytypeid |  | bos_entityobject |
| permitem | 权限项 | BasedataField | fpermitemid |  | perm_permitem |
| isassign | 独立分配 | ComboField | fisassign |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |

