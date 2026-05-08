# hrcs_userdatarule — 用户角色数据规则

**表单编码**: `hrcs_userdatarule`  
**表单ID**: `16D6R0L4AHBL`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userdatarule（用户角色数据规则） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_userdatarule` | BaseEntity | 主表 |
| `t_hrcs_userdataruleentry` | EntryEntity | 数据规则分录 |
| `t_hrcs_userbdruleentry` | EntryEntity | 基础资料数据规则分录 |

### 字段列表 — t_hrcs_userdatarule（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| createrfield | 创建人 | CreaterField | t_hrcs_userdatarule.fcreatorid |  | bos_user |
| createdatefield | 创建日期 | CreateDateField | t_hrcs_userdatarule.fcreatetime |  |  |
| modifierfield | 修改人 | ModifierField | t_hrcs_userdatarule.fmodifierid |  | bos_user |
| modifydatefield | 修改日期 | ModifyDateField | t_hrcs_userdatarule.fmodifytime |  |  |
| userrolerelate | 用户角色关联 | BasedataField | t_hrcs_userdatarule.fuserrolerealtid |  | hrcs_userrolerelat |
| app | 应用id | BasedataField | t_hrcs_userdatarule.fappid |  | bos_devportal_bizapp |
| entitytype | 实体类型 | BasedataField | t_hrcs_userdatarule.fentitytypeid |  | bos_objecttype |
| dataruleentry | 数据规则分录 | EntryEntity | → t_hrcs_userdataruleentry |  |  |
| bddataruleentry | 基础资料数据规则分录 | EntryEntity | → t_hrcs_userbdruleentry |  |  |

### 字段列表 — t_hrcs_userdataruleentry（数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| permitem | 权限项 | BasedataField | t_hrcs_userdataruleentry.fpermitemid |  | perm_permitem |
| datarule | 数据规则方案 | BasedataField | t_hrcs_userdataruleentry.fdataruleid |  | hrcs_datarule |

### 字段列表 — t_hrcs_userbdruleentry（基础资料数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 属性 | TextField | t_hrcs_userbdruleentry.fpropkey |  |  |
| propentnum | 属性实体编码 | TextField | t_hrcs_userbdruleentry.fpropentnum |  |  |
| bddatarule | 方案 | BasedataField | t_hrcs_userbdruleentry.fdataruleid |  | hrcs_datarule |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_userdatarule（主表） | 7 |
| t_hrcs_userdataruleentry（数据规则分录） | 2 |
| t_hrcs_userbdruleentry（基础资料数据规则分录） | 3 |

