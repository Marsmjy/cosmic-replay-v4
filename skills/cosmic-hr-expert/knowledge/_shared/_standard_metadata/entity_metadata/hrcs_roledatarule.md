# hrcs_roledatarule — 角色数据规则

**表单编码**: `hrcs_roledatarule`  
**表单ID**: `16D6JCMHAFKU`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roledatarule（角色数据规则） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_roledatarule` | BaseEntity | 主表 |
| `t_hrcs_roledataruleentry` | EntryEntity | 角色数据规则分录 |
| `t_hrcs_rolebdruleentry` | EntryEntity | 角色基础资料数据范围分录 |

### 字段列表 — t_hrcs_roledatarule（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| role | 角色 | BasedataField | t_hrcs_roledatarule.froleid |  | hrcs_role |
| creator | 创建人 | CreaterField | t_hrcs_roledatarule.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_roledatarule.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_roledatarule.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_roledatarule.fmodifytime |  |  |
| entitytype | 实体编码 | TextField | t_hrcs_roledatarule.fentitytypeid |  |  |
| app | 应用id | TextField | t_hrcs_roledatarule.fappid |  |  |
| roledataruleentry | 角色数据规则分录 | EntryEntity | → t_hrcs_roledataruleentry |  |  |
| hrcs_rolebdruleentry | 角色基础资料数据范围分录 | EntryEntity | → t_hrcs_rolebdruleentry |  |  |

### 字段列表 — t_hrcs_roledataruleentry（角色数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| permitem | 权限项 | BasedataField | t_hrcs_roledataruleentry.fpermitemid |  | perm_permitem |
| datarule | 方案 | BasedataField | t_hrcs_roledataruleentry.fdataruleid |  | hrcs_datarule |

### 字段列表 — t_hrcs_rolebdruleentry（角色基础资料数据范围分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 属性 | TextField | t_hrcs_rolebdruleentry.fpropkey |  |  |
| propentnum | 属性实体编码 | TextField | t_hrcs_rolebdruleentry.fpropentnum |  |  |
| bddatarule | 方案 | BasedataField | t_hrcs_rolebdruleentry.fdataruleid |  | hrcs_datarule |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_roledatarule（主表） | 7 |
| t_hrcs_roledataruleentry（角色数据规则分录） | 2 |
| t_hrcs_rolebdruleentry（角色基础资料数据范围分录） | 3 |

