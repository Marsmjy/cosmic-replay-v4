# hrcs_entityreleaseinfo — 查询实体上线信息

**表单编码**: `hrcs_entityreleaseinfo`  
**表单ID**: `1ZKF4F9=0IWY`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entityreleaseinfo（查询实体上线信息） [BaseEntity]

- **数据库表**: `t_hbss_entityreleaseinfo`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| queryentityname | 查询实体名称 | TextField | fqueryentityname |  |  |
| datasourcetype | 数据源类别 | TextField | fdatasourcetype |  |  |
| ksqlquerytype | KSQL查询方式 | TextField | fksqlquerytype |  |  |
| ksqluseunion | KSQL是否启用Union | CheckBoxField | fksqluseunion |  |  |
| bizapplytype | 业务应用类型 | ComboField | fbizapplytype | ✓ |  |

