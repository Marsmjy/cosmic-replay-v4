# hrcs_userroledimgrp — 用户角色维度组

**表单编码**: `hrcs_userroledimgrp`  
**表单ID**: `2+6ZWDDVDAM1`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_userroledimgrp（用户角色维度组） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_userroledimgrp` | BaseEntity | 主表 |
| `t_hrcs_userroledimval` | EntryEntity | 维度值 |

### 字段列表 — t_hrcs_userroledimgrp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_userroledimgrp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_userroledimgrp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_userroledimgrp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_userroledimgrp.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_userroledimgrp.finitdatasource |  |  |
| userrolerelat | 用户角色关联 | BasedataField | t_hrcs_userroledimgrp.fuserrolerelatid |  | hrcs_userrolerelat |
| bucafunc | 业务职能 | BasedataField | t_hrcs_userroledimgrp.fbucafuncid |  | bos_org_biz |
| index | 排序号 | IntegerField | t_hrcs_userroledimgrp.findex |  |  |
| entry | 维度值 | EntryEntity | → t_hrcs_userroledimval |  |  |

### 字段列表 — t_hrcs_userroledimval（维度值·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dimension | 维度 | BasedataField | t_hrcs_userroledimval.fdimensionid |  | hrcs_dimension |
| isall | 是否全选 | CheckBoxField | t_hrcs_userroledimval.fisall |  |  |
| containssub | 是否包含下级 | CheckBoxField | t_hrcs_userroledimval.fcontainssub |  |  |
| dimval | 维度值 | TextField | t_hrcs_userroledimval.fdimval |  |  |
| structproject | 架构方案 | BasedataField | t_hrcs_userroledimval.fstructprojectid |  | haos_structproject |
| valtype | 值类型 | ComboField | t_hrcs_userroledimval.fvaltype |  |  |
| dynacond | 动态条件 | BasedataField | t_hrcs_userroledimval.fdynacondid |  | hrcs_dynacond |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_userroledimgrp（主表） | 8 |
| t_hrcs_userroledimval（维度值） | 7 |

