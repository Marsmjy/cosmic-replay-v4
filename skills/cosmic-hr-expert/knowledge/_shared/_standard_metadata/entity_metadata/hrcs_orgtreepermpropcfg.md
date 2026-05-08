# hrcs_orgtreepermpropcfg — 行政组织左树控权属性配置

**表单编码**: `hrcs_orgtreepermpropcfg`  
**表单ID**: `3VJ=PF6HOOAD`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_orgtreepermpropcfg（行政组织左树控权属性配置） [BaseEntity]

- **数据库表**: `t_hrcs_orgtreepermpropcfg`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entitytype | 业务对象 | BasedataField | fentitytypeid |  | bos_entityobject |
| propkey | 属性 | TextField | fpropkey |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |

