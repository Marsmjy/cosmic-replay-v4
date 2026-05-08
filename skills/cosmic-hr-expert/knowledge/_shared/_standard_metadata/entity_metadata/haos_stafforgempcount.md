# haos_stafforgempcount — 组织人数信息

**表单编码**: `haos_stafforgempcount`  
**表单ID**: `2WQ2VOSWK3HO`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_stafforgempcount（组织人数信息） [BaseEntity]

- **数据库表**: `t_haos_stafforgempcount`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| useorgbo | 行政组织 | HRAdminOrgField | fuseorgboid |  | haos_adminorghrf7 |
| count | 人数 | IntegerField | fcount |  |  |
| containsubcount | 人数(包含下级) | IntegerField | fcontainsubcount |  |  |

