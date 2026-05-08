# hrcs_perminitrecord — 权限初始化任务

**表单编码**: `hrcs_perminitrecord`  
**表单ID**: `31QV=UQX91PY`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_perminitrecord（权限初始化任务） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_pinitrecord` | BaseEntity | 主表 |
| `t_hrcs_pinituserdim` | EntryEntity | 用户维度值分录 |
| `t_hrcs_pinituserdr` | EntryEntity | 用户数据规则分录 |
| `t_hrcs_pinituserbd` | EntryEntity | 用户基础资料范围分录 |
| `t_hrcs_pinituserfield` | EntryEntity | 用户字段权限分录 |
| `t_hrcs_pinitrolebase` | EntryEntity | 角色基本信息分录 |
| `t_hrcs_pinitrolefield` | EntryEntity | 角色字段权限分录 |
| `t_hrcs_pinitrolefunc` | EntryEntity | 角色功能权限分录 |
| `t_hrcs_pinitroledim` | EntryEntity | 角色维度分录 |
| `t_hrcs_pinitroledata` | EntryEntity | 角色数据范围分录 |
| `t_hrcs_pinitrolefunccol` | EntryEntity | 角色功能权限列错误分录 |
| `t_hrcs_pinitrolefuncrow` | EntryEntity | 角色功能权限行错误分录 |
| `t_hrcs_pinitroledimrow` | EntryEntity | 角色维度行错误分录 |
| `t_hrcs_pinitroledatarow` | EntryEntity | 角色数据行错误分录 |
| `t_hrcs_pinituserdimval` | SubEntryEntity | 用户维度值子分录 |
| `t_hrcs_pinitroledataval` | SubEntryEntity | 角色数据范围维度值分录 |
| `t_hrcs_pinituserorg` | MulEmployeeField子表 | 组织范围 |
| `t_hrcs_pinitdrpermitem` | MulEmployeeField子表 | 权限项 |
| `t_hrcs_pinitscopeview` | MulEmployeeField子表 | 角色公开范围（查看） |
| `t_hrcs_pinitscopeedit` | MulEmployeeField子表 | 角色公开范围（编辑） |
| `t_hrcs_pinitroleorg` | MulEmployeeField子表 | 组织范围 |

### 字段列表 — t_hrcs_pinitrecord（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_pinitrecord.fnumber |  |  |
| name | 任务名称 | MuliLangTextField | t_hrcs_pinitrecord.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_pinitrecord.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_pinitrecord.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_pinitrecord.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_pinitrecord.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_pinitrecord.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_pinitrecord.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_pinitrecord.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_pinitrecord.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_pinitrecord.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_pinitrecord.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_pinitrecord.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_pinitrecord.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_pinitrecord.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_pinitrecord.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_pinitrecord.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_pinitrecord.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_pinitrecord.foriname |  |  |
| inittype | 类型 | ComboField | t_hrcs_pinitrecord.finittype |  |  |
| initnumber | 初始化数量 | IntegerField | t_hrcs_pinitrecord.finitnumber |  |  |
| includesub | 数据范围策略 | ComboField | t_hrcs_pinitrecord.fincludesub | ✓ |  |
| dealstatus | 状态 | ComboField | t_hrcs_pinitrecord.fdealstatus |  |  |
| userdimentry | 用户维度值分录 | EntryEntity | → t_hrcs_pinituserdim |  |  |
| userdataruleentry | 用户数据规则分录 | EntryEntity | → t_hrcs_pinituserdr |  |  |
| userbdentry | 用户基础资料范围分录 | EntryEntity | → t_hrcs_pinituserbd |  |  |
| userfieldentry | 用户字段权限分录 | EntryEntity | → t_hrcs_pinituserfield |  |  |
| rolebaseentry | 角色基本信息分录 | EntryEntity | → t_hrcs_pinitrolebase |  |  |
| rolefieldentry | 角色字段权限分录 | EntryEntity | → t_hrcs_pinitrolefield |  |  |
| rolefuncentry | 角色功能权限分录 | EntryEntity | → t_hrcs_pinitrolefunc |  |  |
| roledimentry | 角色维度分录 | EntryEntity | → t_hrcs_pinitroledim |  |  |
| roledataentry | 角色数据范围分录 | EntryEntity | → t_hrcs_pinitroledata |  |  |
| rolefunccolerrorentry | 角色功能权限列错误分录 | EntryEntity | → t_hrcs_pinitrolefunccol |  |  |
| rolefuncrowerrorentry | 角色功能权限行错误分录 | EntryEntity | → t_hrcs_pinitrolefuncrow |  |  |
| roledimrowerrorentry | 角色维度行错误分录 | EntryEntity | → t_hrcs_pinitroledimrow |  |  |
| roledatarowerrorentry | 角色数据行错误分录 | EntryEntity | → t_hrcs_pinitroledatarow |  |  |

### 字段列表 — t_hrcs_pinituserdim（用户维度值分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dim_user | 用户 | BasedataField | t_hrcs_pinituserdim.fuserid |  | bos_user |
| dim_role | 角色 | BasedataField | t_hrcs_pinituserdim.froleid |  | perm_role |
| dim_customenable | 成员范围属性 | ComboField | t_hrcs_pinituserdim.fcustomenable |  |  |
| dim_bucafunc | 职能类型 | BasedataField | t_hrcs_pinituserdim.fbucafuncid |  | bos_org_biz |
| dim_orgrange | 组织范围 | MulBasedataField | t_hrcs_pinituserorg（子表） |  |  |
| dim_validstart | 有效开始时间 | DateField | t_hrcs_pinituserdim.fvalidstart |  |  |
| dim_validend | 有效结束时间 | DateField | t_hrcs_pinituserdim.fvalidend |  |  |
| dim_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinituserdim.ferrormsg |  |  |
| dim_fileorg | HR管理组织 | BasedataField | t_hrcs_pinituserdim.ffileorg |  | bos_org |
| dim_orgcontainssubstr | 组织包含下级字符串 | TextField | t_hrcs_pinituserdim.forgcontainssubstr |  |  |
| userdimvalueentry | 用户维度值子分录 | SubEntryEntity | → t_hrcs_pinituserdimval |  |  |

### 字段列表 — t_hrcs_pinituserdr（用户数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dr_role | 角色 | BasedataField | t_hrcs_pinituserdr.froleid |  | perm_role |
| dr_app | 应用 | BasedataField | t_hrcs_pinituserdr.fappid |  | bos_devportal_bizapp |
| dr_entitytype | 业务对象 | BasedataField | t_hrcs_pinituserdr.fentitytypeid |  | bos_entityobject |
| dr_permitemmulti | 权限项 | MulBasedataField | t_hrcs_pinitdrpermitem（子表） |  |  |
| dr_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinituserdr.ferrormsg |  |  |
| dr_datarule | 数据规则方案 | BasedataField | t_hrcs_pinituserdr.fdataruleid |  | hrcs_datarule |
| dr_user | 用户 | BasedataField | t_hrcs_pinituserdr.fuserid |  | bos_user |
| dr_fileorg | HR管理组织 | BasedataField | t_hrcs_pinituserdr.ffileorg |  | bos_org |

### 字段列表 — t_hrcs_pinituserbd（用户基础资料范围分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bd_user | 用户 | BasedataField | t_hrcs_pinituserbd.fuserid |  | bos_user |
| bd_fileorg | HR管理组织 | BasedataField | t_hrcs_pinituserbd.ffileorg |  | bos_org |
| bd_role | 角色 | BasedataField | t_hrcs_pinituserbd.froleid |  | perm_role |
| bd_app | 应用 | BasedataField | t_hrcs_pinituserbd.fappid |  | bos_devportal_bizapp |
| bd_entitytype | 业务对象 | BasedataField | t_hrcs_pinituserbd.fentitytypeid |  | bos_entityobject |
| bd_datarule | 数据规则方案 | BasedataField | t_hrcs_pinituserbd.fdataruleid |  | hrcs_datarule |
| bd_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinituserbd.ferrormsg |  |  |
| bd_propkey | 属性 | TextField | t_hrcs_pinituserbd.fpropkey |  |  |
| bd_propentnum | 属性实体编码 | TextField | t_hrcs_pinituserbd.fpropentnum |  |  |
| bd_propname | 属性 | TextField | — |  |  |

### 字段列表 — t_hrcs_pinituserfield（用户字段权限分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| field_user | 用户 | BasedataField | t_hrcs_pinituserfield.fuserid |  | bos_user |
| field_fileorg | HR管理组织 | BasedataField | t_hrcs_pinituserfield.ffileorg |  | bos_org |
| field_role | 角色 | BasedataField | t_hrcs_pinituserfield.froleid |  | perm_role |
| field_app | 应用 | BasedataField | t_hrcs_pinituserfield.fappid |  | bos_devportal_bizapp |
| field_entitytype | 业务对象 | BasedataField | t_hrcs_pinituserfield.fentitytypeid |  | bos_entityobject |
| field_propkey | 字段 | TextField | t_hrcs_pinituserfield.fpropkey |  |  |
| field_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinituserfield.ferrormsg |  |  |
| field_propname | 字段名 | TextField | — |  |  |
| field_canread | 查看 | ComboField | t_hrcs_pinituserfield.fcanread |  |  |
| field_canwrite | 编辑 | ComboField | t_hrcs_pinituserfield.fcanwrite |  |  |

### 字段列表 — t_hrcs_pinitrolebase（角色基本信息分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rbase_number | 编码 | TextField | t_hrcs_pinitrolebase.fnumber |  |  |
| rbase_name | 名称 | MuliLangTextField | t_hrcs_pinitrolebase.fname |  |  |
| rbase_group | 角色组 | BasedataField | t_hrcs_pinitrolebase.fgroupid |  | perm_rolegroup |
| rbase_property | 角色成员范围属性 | ComboField | t_hrcs_pinitrolebase.fproperty |  |  |
| rbase_isintersection | 自定义范围是否受限于角色范围 | ComboField | t_hrcs_pinitrolebase.fisintersection |  |  |
| rbase_createadmingrp | HR管理组织 | BasedataField | t_hrcs_pinitrolebase.fcreateadmingrp |  | perm_admingroup |
| rbase_usescope | 公开状态 | ComboField | t_hrcs_pinitrolebase.fusescope |  |  |
| rbase_openscopeview | 角色公开范围（查看） | MulBasedataField | t_hrcs_pinitscopeview（子表） |  |  |
| rbase_openscopeedit | 角色公开范围（编辑） | MulBasedataField | t_hrcs_pinitscopeedit（子表） |  |  |
| rbase_remark | 角色描述 | MuliLangTextField | t_hrcs_pinitrolebase.fremark |  |  |
| rbase_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitrolebase.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitrolefield（角色字段权限分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rfield_rolenumber | 角色编码 | TextField | t_hrcs_pinitrolefield.frolenumber |  |  |
| rfield_app | 应用 | BasedataField | t_hrcs_pinitrolefield.fappid |  | bos_devportal_bizapp |
| rfield_entitytype | 业务对象 | BasedataField | t_hrcs_pinitrolefield.fentitytypeid |  | bos_entityobject |
| rfield_propkey | 字段 | TextField | t_hrcs_pinitrolefield.fpropkey |  |  |
| rfield_canread | 查看 | ComboField | t_hrcs_pinitrolefield.fcanread |  |  |
| rfield_canwrite | 编辑 | ComboField | t_hrcs_pinitrolefield.fcanwrite |  |  |
| rfield_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitrolefield.ferrormsg |  |  |
| rfield_rolename | 角色 | TextField | — |  |  |
| rfield_propname | 字段 | TextField | — |  |  |

### 字段列表 — t_hrcs_pinitrolefunc（角色功能权限分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rfunc_rolenumber | 角色编码 | TextField | t_hrcs_pinitrolefunc.frolenumber |  |  |
| rfunc_app | 应用 | BasedataField | t_hrcs_pinitrolefunc.fappid |  | bos_devportal_bizapp |
| rfunc_entitytype | 业务对象 | BasedataField | t_hrcs_pinitrolefunc.fentitytypeid |  | bos_entityobject |
| rfunc_permitem | 权限项 | BasedataField | t_hrcs_pinitrolefunc.fpermitemid |  | perm_permitem |
| rfunc_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitrolefunc.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitroledim（角色维度分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rdim_rolenumber | 角色编码 | TextField | t_hrcs_pinitroledim.frolenumber |  |  |
| rdim_bucafunc | 职能类型 | BasedataField | t_hrcs_pinitroledim.fbucafuncid |  | bos_org_biz |
| rdim_dimension | 维度 | BasedataField | t_hrcs_pinitroledim.fdimensionid |  | hrcs_dimension |
| rdim_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitroledim.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitroledata（角色数据范围分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rdata_rolenumber | 角色编码 | TextField | t_hrcs_pinitroledata.frolenumber |  |  |
| rdata_bucafunc | 职能类型 | BasedataField | t_hrcs_pinitroledata.fbucafuncid |  | bos_org_biz |
| rdata_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitroledata.ferrormsg |  |  |
| rdata_orgrange | 组织范围 | MulBasedataField | t_hrcs_pinitroleorg（子表） |  |  |
| rdata_orgcontainssubstr | 组织包含下级字符串 | TextField | t_hrcs_pinitroledata.forgcontainssubstr |  |  |
| roledatavalentry | 角色数据范围维度值分录 | SubEntryEntity | → t_hrcs_pinitroledataval |  |  |

### 字段列表 — t_hrcs_pinitrolefunccol（角色功能权限列错误分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rfunccol_rolenumber | 角色编码 | TextField | t_hrcs_pinitrolefunccol.frolenumber |  |  |
| rfunccol_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitrolefunccol.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitrolefuncrow（角色功能权限行错误分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rfuncrow_app | 应用 | BasedataField | t_hrcs_pinitrolefuncrow.fappid |  | bos_devportal_bizapp |
| rfuncrow_entitytype | 业务对象 | BasedataField | t_hrcs_pinitrolefuncrow.fentitytypeid |  | bos_entityobject |
| rfuncrow_permitem | 权限项 | BasedataField | t_hrcs_pinitrolefuncrow.fpermitemid |  | perm_permitem |
| rfuncrow_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitrolefuncrow.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitroledimrow（角色维度行错误分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rdimrow_rolenumber | 角色编码 | TextField | t_hrcs_pinitroledimrow.frolenumber |  |  |
| rdimrow_bucafunc | 职能类型 | BasedataField | t_hrcs_pinitroledimrow.fbucafuncid |  | bos_org_biz |
| rdimrow_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitroledimrow.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinitroledatarow（角色数据行错误分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rdatarow_rolenumber | 角色编码 | TextField | t_hrcs_pinitroledatarow.frolenumber |  |  |
| rdatarow_bucafunc | 职能类型 | BasedataField | t_hrcs_pinitroledatarow.fbucafuncid |  | bos_org_biz |
| rdatarow_errormsg | 错误提示 | MuliLangTextField | t_hrcs_pinitroledatarow.ferrormsg |  |  |

### 字段列表 — t_hrcs_pinituserdimval（用户维度值子分录·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dim_dimension | 维度 | BasedataField | t_hrcs_pinituserdimval.fdimensionid |  | hrcs_dimension |
| dim_dimval | 维度值 | TextField | t_hrcs_pinituserdimval.fdimval |  |  |
| dim_otclassify | 团队分类来源 | BasedataField | t_hrcs_pinituserdimval.fotclassid |  | haos_otclassify |
| dim_isall | 是否全选 | CheckBoxField | t_hrcs_pinituserdimval.fisall |  |  |
| dim_structproject | 构架方案 | BasedataField | t_hrcs_pinituserdimval.fstructprojectid |  | haos_structproject |
| dim_containssub | 是否包含下级 | CheckBoxField | t_hrcs_pinituserdimval.fcontainssub |  |  |
| dim_valtype | 值类型 | ComboField | t_hrcs_pinituserdimval.fvaltype |  |  |
| dim_dynacond | 动态条件 | BasedataField | t_hrcs_pinituserdimval.fdynacondid |  | hrcs_dynacond |

### 字段列表 — t_hrcs_pinitroledataval（角色数据范围维度值分录·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rdata_structproject | 构架方案 | BasedataField | t_hrcs_pinitroledataval.fstructprojectid |  | haos_structproject |
| rdata_dimension | 维度 | BasedataField | t_hrcs_pinitroledataval.fdimensionid |  | hrcs_dimension |
| rdata_dimval | 维度值 | TextField | t_hrcs_pinitroledataval.fdimval |  |  |
| rdata_isall | 是否全选 | CheckBoxField | t_hrcs_pinitroledataval.fisall |  |  |
| rdata_containssub | 是否包含下级 | CheckBoxField | t_hrcs_pinitroledataval.fcontainssub |  |  |
| rdata_valtype | 值类型 | ComboField | t_hrcs_pinitroledataval.fvaltype |  |  |
| rdata_dynacond | 动态条件 | BasedataField | t_hrcs_pinitroledataval.fdynacondid |  | hrcs_dynacond |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_pinitrecord（主表） | 23 |
| t_hrcs_pinituserdim（用户维度值分录） | 10 |
| t_hrcs_pinituserdr（用户数据规则分录） | 8 |
| t_hrcs_pinituserbd（用户基础资料范围分录） | 10 |
| t_hrcs_pinituserfield（用户字段权限分录） | 10 |
| t_hrcs_pinitrolebase（角色基本信息分录） | 11 |
| t_hrcs_pinitrolefield（角色字段权限分录） | 9 |
| t_hrcs_pinitrolefunc（角色功能权限分录） | 5 |
| t_hrcs_pinitroledim（角色维度分录） | 4 |
| t_hrcs_pinitroledata（角色数据范围分录） | 5 |
| t_hrcs_pinitrolefunccol（角色功能权限列错误分录） | 2 |
| t_hrcs_pinitrolefuncrow（角色功能权限行错误分录） | 4 |
| t_hrcs_pinitroledimrow（角色维度行错误分录） | 3 |
| t_hrcs_pinitroledatarow（角色数据行错误分录） | 3 |
| t_hrcs_pinituserdimval（用户维度值子分录） | 8 |
| t_hrcs_pinitroledataval（角色数据范围维度值分录） | 7 |
| t_hrcs_pinituserorg（MulEmployeeField子表） | 1 |
| t_hrcs_pinitdrpermitem（MulEmployeeField子表） | 1 |
| t_hrcs_pinitscopeview（MulEmployeeField子表） | 1 |
| t_hrcs_pinitscopeedit（MulEmployeeField子表） | 1 |
| t_hrcs_pinitroleorg（MulEmployeeField子表） | 1 |
| 无数据库列 | 4 |

