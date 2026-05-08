# hrcs_roleexdimgrp — 角色例外维度组

**表单编码**: `hrcs_roleexdimgrp`  
**表单ID**: `4HH24PTI4ADT`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleexdimgrp（角色例外维度组） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_roleexdimgrp` | BaseEntity | 主表 |
| `t_hrcs_roleexdimval` | EntryEntity | 维度值 |

### 字段列表 — t_hrcs_roleexdimgrp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_roleexdimgrp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_roleexdimgrp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_roleexdimgrp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_roleexdimgrp.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_roleexdimgrp.finitdatasource |  |  |
| role | 角色 | BasedataField | t_hrcs_roleexdimgrp.froleid |  | perm_role |
| bucafunc | 业务职能 | BasedataField | t_hrcs_roleexdimgrp.fbucafuncid |  | bos_org_biz |
| index | 排序号 | IntegerField | t_hrcs_roleexdimgrp.findex |  |  |
| entry | 维度值 | EntryEntity | → t_hrcs_roleexdimval |  |  |

### 字段列表 — t_hrcs_roleexdimval（维度值·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dimension | 维度 | BasedataField | t_hrcs_roleexdimval.fdimensionid |  | hrcs_dimension |
| structproject | 架构方案 | BasedataField | t_hrcs_roleexdimval.fstructprojectid |  | haos_structproject |
| containssub | 是否包含下级 | CheckBoxField | t_hrcs_roleexdimval.fcontainssub |  |  |
| dimval | 维度值 | TextField | t_hrcs_roleexdimval.fdimval |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_roleexdimgrp（主表） | 8 |
| t_hrcs_roleexdimval（维度值） | 4 |

