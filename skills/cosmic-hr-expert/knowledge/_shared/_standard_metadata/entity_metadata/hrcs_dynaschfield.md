# hrcs_dynaschfield — 动态权限方案字段权限

**表单编码**: `hrcs_dynaschfield`  
**表单ID**: `5+5RXCSA/9I8`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaschfield（动态权限方案字段权限） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaschfield` | BaseEntity | 主表 |
| `t_hrcs_dynaschfldentry` | EntryEntity | 方案角色字段权限分录 |

### 字段列表 — t_hrcs_dynaschfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynaschfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaschfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynaschfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaschfield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaschfield.finitdatasource |  |  |
| scheme | 动态权限方案 | BasedataField | t_hrcs_dynaschfield.fschemeid |  | hrcs_dynascheme |
| role | 角色 | BasedataField | t_hrcs_dynaschfield.froleid |  | hrcs_role |
| app | 应用id | BasedataField | t_hrcs_dynaschfield.fappid |  | bos_devportal_bizapp |
| entitytype | 实体编码 | BasedataField | t_hrcs_dynaschfield.fentitytypeid |  | bos_entityobject |
| roleentryid | 方案角色分录ID | BigIntField | t_hrcs_dynaschfield.froleentryid |  |  |
| fieldpermentry | 方案角色字段权限分录 | EntryEntity | → t_hrcs_dynaschfldentry |  |  |

### 字段列表 — t_hrcs_dynaschfldentry（方案角色字段权限分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 列名 | TextField | t_hrcs_dynaschfldentry.ffieldname |  |  |
| isbanread | 是否禁止查看 | CheckBoxField | t_hrcs_dynaschfldentry.fisbanread |  |  |
| isbanwrite | 是否禁止编辑 | CheckBoxField | t_hrcs_dynaschfldentry.fisbanwrite |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaschfield（主表） | 10 |
| t_hrcs_dynaschfldentry（方案角色字段权限分录） | 3 |

