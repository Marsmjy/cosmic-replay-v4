# hrcs_checkuserrolesyn — 检查用户角色同步结果

**表单编码**: `hrcs_checkuserrolesyn`  
**表单ID**: `46RM2FZ23U+1`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_checkuserrolesyn（检查用户角色同步结果） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_checkuserrolesyn` | BaseEntity | 主表 |
| `t_hrcs_checkonlyinsys` | EntryEntity | 仅在平台分录 |
| `t_hrcs_checkonlyinhmp` | EntryEntity | 仅在中台分录 |

### 字段列表 — t_hrcs_checkuserrolesyn（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_checkuserrolesyn.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_checkuserrolesyn.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_checkuserrolesyn.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_checkuserrolesyn.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_checkuserrolesyn.finitdatasource |  |  |
| onlyinsyssize | 仅在平台条数 | IntegerField | t_hrcs_checkuserrolesyn.fonlyinsyssize |  |  |
| onlyinhmpsize | 仅在中台条数 | IntegerField | t_hrcs_checkuserrolesyn.fonlyinhmpsize |  |  |
| onlyinsysentry | 仅在平台分录 | EntryEntity | → t_hrcs_checkonlyinsys |  |  |
| onlyinhmpentry | 仅在中台分录 | EntryEntity | → t_hrcs_checkonlyinhmp |  |  |

### 字段列表 — t_hrcs_checkonlyinsys（仅在平台分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sys_role | 角色 | BasedataField | t_hrcs_checkonlyinsys.froleid |  | perm_role |
| sys_user | 用户 | UserField | t_hrcs_checkonlyinsys.fuserid |  | bos_user |
| sys_org | 组织 | OrgField | t_hrcs_checkonlyinsys.forgid |  | bos_org |
| sys_isincludesuborg | 包含下级组织 | CheckBoxField | t_hrcs_checkonlyinsys.fisincludesuborg |  |  |
| sysid | ID | TextField | t_hrcs_checkonlyinsys.finsysid |  |  |

### 字段列表 — t_hrcs_checkonlyinhmp（仅在中台分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| hmp_sysid | 系统ID | TextField | t_hrcs_checkonlyinhmp.finsysid |  |  |
| hmp_role | 角色 | BasedataField | t_hrcs_checkonlyinhmp.froleid |  | perm_role |
| hmp_user | 用户 | UserField | t_hrcs_checkonlyinhmp.fuserid |  | bos_user |
| hmp_org | 组织 | OrgField | t_hrcs_checkonlyinhmp.forgid |  | bos_org |
| hmp_isincludesuborg | 包含下级组织 | CheckBoxField | t_hrcs_checkonlyinhmp.fisincludesuborg |  |  |
| hmpid | ID | BigIntField | t_hrcs_checkonlyinhmp.fhmpid |  |  |
| hmp_relatid | 关联ID | BigIntField | t_hrcs_checkonlyinhmp.frelatid |  |  |
| hmp_bucafunc | 业务职能 | BasedataField | t_hrcs_checkonlyinhmp.fbucafuncid |  | bos_org_biz |
| hmp_createdate | 创建日期 | CreateDateField | t_hrcs_checkonlyinhmp.fcreatedate |  |  |
| hmp_creator | 创建人 | UserField | t_hrcs_checkonlyinhmp.fcreatorid |  | bos_user |
| hmp_customenable | 是否自定义属性 | ComboField | t_hrcs_checkonlyinhmp.fcustomenable |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_checkuserrolesyn（主表） | 7 |
| t_hrcs_checkonlyinsys（仅在平台分录） | 5 |
| t_hrcs_checkonlyinhmp（仅在中台分录） | 11 |

