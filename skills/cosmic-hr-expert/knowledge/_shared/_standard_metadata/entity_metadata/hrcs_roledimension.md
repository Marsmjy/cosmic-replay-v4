# hrcs_roledimension — 角色维度关系

**表单编码**: `hrcs_roledimension`  
**表单ID**: `1=6WY3J64ISV`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roledimension（角色维度关系） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_roledimension` | BaseEntity | 主表 |
| `t_hrcs_dimensionprop` | EntryEntity | 枚举值 |

### 字段列表 — t_hrcs_roledimension（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_roledimension.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_roledimension.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_roledimension.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_roledimension.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_roledimension.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_roledimension.finitdatasource |  |  |
| role | 角色 | BasedataField | t_hrcs_roledimension.froleid | ✓ | perm_role |
| dimension | 维度 | BasedataField | t_hrcs_roledimension.fdimensionid | ✓ | hrcs_dimension |
| bucafunc | 业务职能 | BasedataField | t_hrcs_roledimension.fbucafuncid |  | bos_org_biz |
| entry | 枚举值 | EntryEntity | → t_hrcs_dimensionprop |  |  |

### 字段列表 — t_hrcs_dimensionprop（枚举值·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 属性 | TextField | t_hrcs_dimensionprop.fpropkey | ✓ |  |
| entitytype | 实体编码 | BasedataField | t_hrcs_dimensionprop.fentitytypeid | ✓ | bos_entityobject |
| app | 应用 | BasedataField | t_hrcs_dimensionprop.fappid | ✓ | bos_devportal_bizapp |
| enable | 使用状态 | ComboField | t_hrcs_dimensionprop.fenable |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_roledimension（主表） | 9 |
| t_hrcs_dimensionprop（枚举值） | 4 |

