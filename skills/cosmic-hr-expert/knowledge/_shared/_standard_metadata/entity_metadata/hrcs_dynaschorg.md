# hrcs_dynaschorg — 动态权限方案角色业务组织

**表单编码**: `hrcs_dynaschorg`  
**表单ID**: `5+61J/5DHI76`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaschorg（动态权限方案角色业务组织） [BaseEntity]

- **数据库表**: `t_hrcs_dynaschorg`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| scheme | 动态权限方案 | BasedataField | fschemeid |  | hrcs_dynascheme |
| role | 角色 | BasedataField | froleid |  | perm_role |
| bucafunc | 业务职能 | BasedataField | fbucafuncid |  | bos_org_biz |
| org | 业务组织 | OrgField | forgid |  | bos_org |
| includesuborg | 是否包含下级组织 | CheckBoxField | fisincludesuborg |  |  |
| roleentryid | 方案角色分录ID | BigIntField | froleentryid |  |  |

