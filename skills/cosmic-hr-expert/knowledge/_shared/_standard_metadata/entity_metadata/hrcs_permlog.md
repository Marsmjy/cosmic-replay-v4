---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15NPDX/GJFOO
app_number: hrcs
app_name: HR通用服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrcs_permlog — HR权限日志

**表单编码**: `hrcs_permlog`  
**表单ID**: `4/S7EHFPX+IS`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrcs_permlog（HR权限日志） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrcs_permlog` | 主表 · 25 列 |
| `t_hrcs_permlogbaseinfo` | 分录表 · 4 列 |
| `t_hrcs_permlogrolefunc` | 分录表 · 4 列 |
| `t_hrcs_permlogroledim` | 分录表 · 9 列 |
| `t_hrcs_permlograngefield` | 分录表 · 7 列 |
| `t_hrcs_permlogroleopen` | 分录表 · 6 列 |
| `t_hrcs_permloginfluuser` | 分录表 · 20 列 |
| `t_hrcs_permlograngedr` | 分录表 · 8 列 |
| `t_hrcs_permlograngebddr` | 分录表 · 9 列 |
| `t_hrcs_permloginflurole` | 分录表 · 12 列 |
| `t_hrcs_permlograngeorg` | 分录表 · 4 列 |
| `t_hrcs_permlograngebiz` | 分录表 · 4 列 |
| `t_hrcs_permlog_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_permloginfluuser.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_permloginfluuser.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_permlog.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_permlog.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_permlog_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 编码 | TextField | t_hrcs_permlog.fnumber |  |  |
| logtype | 日志类型 | BasedataField | t_hrcs_permlog.flogtypeid |  | hrcs_permlogtype |
| operator | 操作人 | UserField | t_hrcs_permlog.foperatorid |  | bos_user |
| operationtime | 操作时间 | DateTimeField | t_hrcs_permlog.foperationtime |  |  |
| influusernumber | 影响用户数 | IntegerField | t_hrcs_permlog.finfluusernumber |  |  |
| clienttype | 客户端类型 | ComboField | t_hrcs_permlog.fclienttype |  |  |
| mservicename | 微服务接口名 | TextField | t_hrcs_permlog.fmservicename |  |  |
| opentitytype | 操作业务对象 | BasedataField | t_hrcs_permlog.fopentitytypeid | ✓ | bos_entityobject |
| opbtnname | 操作名称 | TextField | t_hrcs_permlog.fopbtnname |  |  |
| beforeopdata | 操作前数据 | TextField | t_hrcs_permlog.fbeforeopdata |  |  |
| afteropdata | 操作后数据 | TextField | t_hrcs_permlog.fafteropdata |  |  |

## 实体: baseinfoentry（基本信息变动分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| baseinfo_changefield | 字段 | TextField | — |  |  |
| baseinfo_beforedata | 变动前 | TextField | — |  |  |
| baseinfo_afterdata | 变动后 | TextField | — |  |  |
| baseinfo_description | 操作描述 | TextField | — |  |  |
| hashandle | 是否已处理 | CheckBoxField | t_hrcs_permlog.fhashandle |  |  |

## 实体: rolefuncentry（角色功能权限分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rolefunc_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| rolefunc_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| rolefunc_permitem | 权限项 | BasedataField | — |  | perm_permitem |
| rolefunc_description | 操作描述 | TextField | — |  |  |

## 实体: roledimentry（角色维度分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| roledim_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| roledim_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| roledim_dimension | 维度 | BasedataField | — |  | hrcs_dimension |
| roledim_description | 操作描述 | TextField | — |  |  |
| roledim_bucafunc | 职能类型 | BasedataField | — |  | bos_org_biz |
| roledim_propkey | 字段 | TextField | — |  |  |
| roledim_propname | 字段名 | TextField | — |  |  |
| roledim_beforedata | 变动前 | TextField | — |  |  |
| roledim_afterdata | 变动后 | TextField | — |  |  |

## 实体: rangefieldentry（字段权限分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rolefield_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| rolefield_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| rolefield_propkey | 字段 | TextField | — |  |  |
| rolefield_propname | 字段名 | TextField | — |  |  |
| rolefield_beforedata | 变动前 | TextField | — |  |  |
| rolefield_afterdata | 变动后 | TextField | — |  |  |
| rolefield_description | 操作描述 | TextField | — |  |  |

## 实体: roleopenentry（角色公开范围分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| roleopen_admingroup | 管理员分组 | BasedataField | — |  | perm_admingroup |
| roleopen_beforedata | 变动前 | TextField | — |  |  |
| roleopen_afterdata | 变动后 | TextField | — |  |  |
| roleopen_description | 操作描述 | TextField | — |  |  |
| roleopen_influadmin | 影响管理员 | MulBasedataField | — |  |  |
| roleopen_admingroupnumber | 管理员分组编码 | TextField | — |  |  |
| roleopen_admingroupname | 管理员分组名称 | TextField | — |  |  |

## 实体: influuserentry（影响分配记录分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| influuser_permfile | 权限档案 | BasedataField | — |  | hrcs_userpermfile |
| influuser_validstart | 有效开始日期 | DateField | — |  |  |
| influuser_validend | 有效结束日期 | DateField | — |  |  |
| influuser_role | 角色 | BasedataField | — |  | perm_role |
| influuser_dataproperty | 成员范围属性 | ComboField | — |  |  |
| influuser_creator | 管理员 | CreaterField | — |  | bos_user |
| influuser_createtime | 分配时间 | CreateDateField | — |  |  |
| influuser_permitem | 权限项 | BasedataField | — |  | perm_permitem |
| influuser_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| influuser_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| influuser_propkey | 字段 | TextField | — |  |  |
| influuser_propname | 字段名 | TextField | — |  |  |
| influuser_rolenumber | 角色编码 | TextField | — |  |  |
| influuser_rolename | 角色名称 | TextField | — |  |  |
| influuser_isuserforbidden | 用户禁用 | CheckBoxField | — |  |  |
| influuser_schemenumber | 动态授权方案编码 | TextField | — |  |  |
| influuser_schemename | 动态授权方案名称 | TextField | — |  |  |
| influuser_fileorgnumber | 权限档案组织编码 | TextField | — |  |  |
| influuser_fileorgname | 权限档案组织名称 | TextField | — |  |  |
| influuser_intersection | 受角色范围限制 | ComboField | — |  |  |

## 实体: rangedrentry（数据规则分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| roledr_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| roledr_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| roledr_permitem | 权限项 | BasedataField | — |  | perm_permitem |
| roledr_description | 操作描述 | TextField | — |  |  |
| roledr_beforedr | 变动前数据规则 | BasedataField | — |  | hrcs_datarule |
| roledr_afterdr | 变动后数据规则 | BasedataField | — |  | hrcs_datarule |
| roledr_beforedrdesc | 变动前数据规则描述 | TextField | — |  |  |
| roledr_afterdrdesc | 变动后数据规则描述 | TextField | — |  |  |

## 实体: rangebddrentry（基础资料范围分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rolebddr_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| rolebddr_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| rolebddr_beforedr | 变动前数据规则 | BasedataField | — |  | hrcs_datarule |
| rolebddr_beforedrdesc | 变动前数据规则描述 | TextField | — |  |  |
| rolebddr_afterdr | 变动后数据规则 | BasedataField | — |  | hrcs_datarule |
| rolebddr_afterdrdesc | 变动后数据规则描述 | TextField | — |  |  |
| rolebddr_description | 操作描述 | TextField | — |  |  |
| rolebddr_propkey | 字段 | TextField | — |  |  |
| rolebddr_propname | 字段名 | TextField | — |  |  |

## 实体: influroleentry（影响角色分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| influrole_bizapp | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| influrole_entitytype | 业务对象 | BasedataField | — |  | bos_entityobject |
| influrole_permitem | 权限项 | BasedataField | — |  | perm_permitem |
| influrole_role | 角色 | BasedataField | — |  | perm_role |
| influrole_influtype | 影响类型 | ComboField | — |  |  |
| influrole_propkey | 字段 | TextField | — |  |  |
| influrole_propname | 字段名 | TextField | — |  |  |
| influrole_bucafunc | 职能类型 | BasedataField | — |  | bos_org_biz |
| influrole_dimension | 维度 | BasedataField | — |  | hrcs_dimension |
| influrole_description | 操作描述 | TextField | — |  |  |
| influrole_rolenumber | 角色编码 | TextField | — |  |  |
| influrole_rolename | 角色名称 | TextField | — |  |  |
| role | 角色 | BasedataField | t_hrcs_permloginflurole.froleid |  | perm_role |
| entitytype | 业务对象 | BasedataField | t_hrcs_permloginflurole.fentitytypeid |  | bos_entityobject |
| datarule | 数据规则方案 | BasedataField | t_hrcs_permlog.fdataruleid |  | hrcs_datarule |

## 实体: rangeorgentry（组织范围分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rangeorg_bucafunc | 职能类型 | BasedataField | — |  | bos_org_biz |
| rangeorg_beforeorgs | 变动前业务组织 | TextField | — |  |  |
| rangeorg_afterorgs | 变动后业务组织 | TextField | — |  |  |
| rangeorg_description | 操作描述 | TextField | — |  |  |

## 实体: rangebizentry（业务数据范围分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rangebiz_bucafunc | 职能类型 | BasedataField | — |  | bos_org_biz |
| rangebiz_beforebizdata | 变动前业务数据范围 | TextField | — |  |  |
| rangebiz_afterbizdata | 变动后业务数据范围 | TextField | — |  |  |
| rangebiz_description | 操作描述 | TextField | — |  |  |
| permfile | 权限档案 | BasedataField | t_hrcs_permloginfluuser.fpermfileid |  | hrcs_userpermfile |
| datarulenumber | 数据规则方案编码 | TextField | t_hrcs_permlog.fdatarulenumber |  |  |
| datarulename | 数据规则方案名称 | TextField | t_hrcs_permlog.fdatarulename |  |  |
| rolenumber | 角色编码 | TextField | t_hrcs_permloginflurole.frolenumber |  |  |
| rolename | 角色名称 | TextField | t_hrcs_permloginflurole.frolename |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_permlog（主表） | 17 |
| t_hrcs_permlog_l | 1 |
| t_hrcs_permloginflurole | 4 |
| t_hrcs_permloginfluuser | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 89 |
