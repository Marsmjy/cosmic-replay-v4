# hrcs_permrelat — 关联权限项

**表单编码**: `hrcs_permrelat`  
**表单ID**: `2+3C=1C+OMP6`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_permrelat（关联权限项） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_permrelat` | BaseEntity | 主表 |
| `t_hrcs_permrelatentry` | EntryEntity | 权限项关联分录 |

### 字段列表 — t_hrcs_permrelat（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_permrelat.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_permrelat.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_permrelat.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_permrelat.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_permrelat.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_permrelat.finitdatasource |  |  |
| entitytype | 主业务对象 | BasedataField | t_hrcs_permrelat.fentitytypeid | ✓ | bos_entityobject |
| bizapp | 应用 | BasedataField | t_hrcs_permrelat.fappid |  | hbp_devportal_bizapp |
| appcombo | 应用 | ComboField | — | ✓ |  |
| mainpermitem | 主权限项 | ComboField | t_hrcs_permrelat.fpermitemid | ✓ |  |
| entryentity | 权限项关联分录 | EntryEntity | → t_hrcs_permrelatentry |  |  |

### 字段列表 — t_hrcs_permrelatentry（权限项关联分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entitytypeid | 业务对象编码 | BasedataField | t_hrcs_permrelatentry.fentitytypeid | ✓ | bos_entityobject |
| app | 应用 | BasedataField | t_hrcs_permrelatentry.fappid | ✓ | bos_devportal_bizapp |
| permitemid | 权限项ID | TextField | t_hrcs_permrelatentry.fpermitemid |  |  |
| permitem | 权限项 | TextField | — | ✓ |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_permrelatentry.fissyspreset |  |  |
| issynrole | 是否同步角色 | CheckBoxField | t_hrcs_permrelatentry.fissynrole |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_permrelat（主表） | 10 |
| t_hrcs_permrelatentry（权限项关联分录） | 6 |
| 无数据库列 | 2 |

