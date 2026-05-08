# hrcs_userrolerelat — 用户角色关联关系

**表单编码**: `hrcs_userrolerelat`  
**表单ID**: `16RU630BQSU4`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userrolerelat（用户角色关联关系） [BaseEntity]

- **数据库表**: `t_hrcs_userrolerelat`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| validend | 有效结束日期 | DateTimeField | fvalidend |  |  |
| validstart | 有效开始日期 | DateTimeField | fvalidstart |  |  |
| user | 用户 | UserField | fuserid |  | bos_user |
| permfile | 权限档案 | BasedataField | fpermfileid |  | hrcs_userpermfile |
| role | 角色 | BasedataField | froleid |  | perm_role |
| customenable | 是否自定义属性 | MulComboField | fcustomenable |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| createtime | 创建日期 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改日期 | ModifyDateField | fmodifytime |  |  |
| sourcetype | 来源类型 | ComboField | fsourcetype |  |  |
| initrecord | 初始化记录 | BasedataField | finitrecordid |  | hrcs_perminitrecord |
| scheme | 授权方案 | BasedataField | fschemeid |  | hrcs_dynascheme |
| assigntype | 分配类型 | ComboField | fassigntype |  |  |

