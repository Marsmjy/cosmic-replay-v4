# hrcs_dynaschdimgrp — 动态权限方案维度值组

**表单编码**: `hrcs_dynaschdimgrp`  
**表单ID**: `5+5R7E6AHL=1`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaschdimgrp（动态权限方案维度值组） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaschdimgrp` | BaseEntity | 主表 |
| `t_hrcs_dynaschdimval` | EntryEntity | 方案角色维度值分录 |

### 字段列表 — t_hrcs_dynaschdimgrp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynaschdimgrp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaschdimgrp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynaschdimgrp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaschdimgrp.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaschdimgrp.finitdatasource |  |  |
| scheme | 动态权限方案 | BasedataField | t_hrcs_dynaschdimgrp.fschemeid |  | hrcs_dynascheme |
| role | 角色 | BasedataField | t_hrcs_dynaschdimgrp.froleid |  | perm_role |
| bucafunc | 业务职能 | BasedataField | t_hrcs_dynaschdimgrp.fbucafuncid |  | bos_org_biz |
| index | 排序号 | IntegerField | t_hrcs_dynaschdimgrp.findex |  |  |
| roleentryid | 方案角色分录ID | BigIntField | t_hrcs_dynaschdimgrp.froleentryid |  |  |
| dimvalentry | 方案角色维度值分录 | EntryEntity | → t_hrcs_dynaschdimval |  |  |

### 字段列表 — t_hrcs_dynaschdimval（方案角色维度值分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dimension | 维度 | BasedataField | t_hrcs_dynaschdimval.fdimensionid |  | hrcs_dimension |
| isall | 是否全选 | CheckBoxField | t_hrcs_dynaschdimval.fisall |  |  |
| structproject | 架构方案 | BasedataField | t_hrcs_dynaschdimval.fstructprojectid |  | haos_structproject |
| containssub | 是否包含下级 | CheckBoxField | t_hrcs_dynaschdimval.fcontainssub |  |  |
| dimval | 维度值 | TextField | t_hrcs_dynaschdimval.fdimval |  |  |
| valtype | 值类型 | ComboField | t_hrcs_dynaschdimval.fvaltype |  |  |
| dynacond | 动态条件 | BasedataField | t_hrcs_dynaschdimval.fdynacondid |  | hrcs_dynacond |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaschdimgrp（主表） | 10 |
| t_hrcs_dynaschdimval（方案角色维度值分录） | 7 |

