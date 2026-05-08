# hrcs_userrole — 用户角色业务单元范围

**表单编码**: `hrcs_userrole`  
**表单ID**: `0T4WTM1PSFVV`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userrole（用户角色业务单元范围） [BaseEntity]

- **数据库表**: `t_hbss_userrole`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| includesuborg | 是否包含下级组织 | CheckBoxField | fisincludesuborg |  |  |
| org | 组织 | OrgField | forgid |  | bos_org |
| userrolerealt | 用户角色关联关系 | BasedataField | fuserrolerealtid |  | hrcs_userrolerelat |
| index | 排序号 | IntegerField | findex |  |  |
| userrolepf | 平台用户角色关系 | BasedataField | fuserrolepfid |  | perm_userrole |
| bucafunc | 业务职能id | BigIntField | fbucafuncid |  |  |

