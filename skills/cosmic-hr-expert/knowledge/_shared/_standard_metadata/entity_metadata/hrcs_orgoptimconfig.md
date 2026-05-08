# hrcs_orgoptimconfig — 组织维度优化配置

**表单编码**: `hrcs_orgoptimconfig`  
**表单ID**: `3KIHKU9FK/OS`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_orgoptimconfig（组织维度优化配置） [BaseEntity]

- **数据库表**: `t_hrcs_orgoptimconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| optimtype | 优化类型 | ComboField | foptimtype |  |  |
| entitytype | 业务对象 | BasedataField | fentitytypeid |  | bos_entityobject |
| propkey | 属性 | TextField | fpropkey |  |  |
| longnumberprop | 长编码属性 | TextField | flongnumberprop |  |  |

