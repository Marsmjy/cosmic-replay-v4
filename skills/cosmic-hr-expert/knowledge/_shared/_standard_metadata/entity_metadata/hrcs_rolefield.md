# hrcs_rolefield — 角色实体列权限

**表单编码**: `hrcs_rolefield`  
**表单ID**: `17N7A9LV2Z1P`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_rolefield（角色实体列权限） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_rolefield` | BaseEntity | 主表 |
| `t_hrcs_rolefieldentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_rolefield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| role | 角色 | BasedataField | t_hrcs_rolefield.froleid |  | hrcs_role |
| entitytype | 实体编码 | BasedataField | t_hrcs_rolefield.fentitytypeid |  | bos_entityobject |
| creator | 创建人 | CreaterField | t_hrcs_rolefield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_rolefield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_rolefield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_rolefield.fmodifytime |  |  |
| app | 应用id | TextField | t_hrcs_rolefield.fappid |  |  |
| fieldpermentry | 单据体 | EntryEntity | → t_hrcs_rolefieldentry |  |  |

### 字段列表 — t_hrcs_rolefieldentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 列名 | TextField | t_hrcs_rolefieldentry.ffieldname |  |  |
| isbanread | 是否禁止查看 | CheckBoxField | t_hrcs_rolefieldentry.fisbanread |  |  |
| isbanwrite | 是否禁止编辑 | CheckBoxField | t_hrcs_rolefieldentry.fisbanwrite |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_rolefield（主表） | 7 |
| t_hrcs_rolefieldentry（单据体） | 3 |

