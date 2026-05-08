# hrcs_dynaschexdimgrp — 动态权限方案例外维度值组

**表单编码**: `hrcs_dynaschexdimgrp`  
**表单ID**: `5+5RKV75IW+U`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaschexdimgrp（动态权限方案例外维度值组） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaschexdimgrp` | BaseEntity | 主表 |
| `t_hrcs_dynaschexdimval` | EntryEntity | 方案角色维度值分录 |

### 字段列表 — t_hrcs_dynaschexdimgrp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynaschexdimgrp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaschexdimgrp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynaschexdimgrp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaschexdimgrp.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaschexdimgrp.finitdatasource |  |  |
| scheme | 动态权限方案 | BasedataField | t_hrcs_dynaschexdimgrp.fschemeid |  | hrcs_dynascheme |
| role | 角色 | BasedataField | t_hrcs_dynaschexdimgrp.froleid |  | perm_role |
| bucafunc | 业务职能 | BasedataField | t_hrcs_dynaschexdimgrp.fbucafuncid |  | bos_org_biz |
| index | 排序号 | IntegerField | t_hrcs_dynaschexdimgrp.findex |  |  |
| roleentryid | 方案角色分录ID | BigIntField | t_hrcs_dynaschexdimgrp.froleentryid |  |  |
| dimvalentry | 方案角色维度值分录 | EntryEntity | → t_hrcs_dynaschexdimval |  |  |

### 字段列表 — t_hrcs_dynaschexdimval（方案角色维度值分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dimension | 维度 | BasedataField | t_hrcs_dynaschexdimval.fdimensionid |  | hrcs_dimension |
| structproject | 架构方案 | BasedataField | t_hrcs_dynaschexdimval.fstructprojectid |  | haos_structproject |
| containssub | 是否包含下级 | CheckBoxField | t_hrcs_dynaschexdimval.fcontainssub |  |  |
| dimval | 维度值 | TextField | t_hrcs_dynaschexdimval.fdimval |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaschexdimgrp（主表） | 10 |
| t_hrcs_dynaschexdimval（方案角色维度值分录） | 4 |

