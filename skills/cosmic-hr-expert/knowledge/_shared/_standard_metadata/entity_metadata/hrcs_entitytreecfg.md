# hrcs_entitytreecfg — 实体权限树配置

**表单编码**: `hrcs_entitytreecfg`  
**表单ID**: `1ZDCXJQ42F9L`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entitytreecfg（实体权限树配置） [BaseEntity]

- **数据库表**: `t_hrcs_entitytreecfg`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| index | 顺序 | IntegerField | findex | ✓ |  |
| appid | 应用ID | TextField | fappid | ✓ |  |
| number | 实体编码 | TextField | fentitytypeid | ✓ |  |

