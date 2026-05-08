# hrcs_userfield — 用户角色实体列权限

**表单编码**: `hrcs_userfield`  
**表单ID**: `17N2L=DLJJX+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userfield（用户角色实体列权限） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_userfield` | BaseEntity | 主表 |
| `t_hrcs_userfieldentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_userfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entitytype | 实体编码 | BasedataField | t_hrcs_userfield.fentitytypeid |  | bos_objecttype |
| creator | 创建人 | CreaterField | t_hrcs_userfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_userfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_userfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_userfield.fmodifytime |  |  |
| userrolerealt | 用户角色关联 | BasedataField | t_hrcs_userfield.fuserrolerealtid |  | hrcs_userrolerelat |
| app | 应用id | BasedataField | t_hrcs_userfield.fappid |  | bos_devportal_bizapp |
| entryentity | 单据体 | EntryEntity | → t_hrcs_userfieldentry |  |  |

### 字段列表 — t_hrcs_userfieldentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 列名 | TextField | t_hrcs_userfieldentry.ffieldname |  |  |
| isbanread | 是否禁止查看 | CheckBoxField | t_hrcs_userfieldentry.fisbanread |  |  |
| isbanwrite | 是否禁止编辑 | CheckBoxField | t_hrcs_userfieldentry.fisbanwrite |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_userfield（主表） | 7 |
| t_hrcs_userfieldentry（单据体） | 3 |

