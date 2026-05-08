# hrcs_admingroupfunc — 职能组织范围

**表单编码**: `hrcs_admingroupfunc`  
**表单ID**: `2AMES8R3+SOE`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_admingroupfunc（职能组织范围） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_admingroupfunc` | BaseEntity | 主表 |
| `t_hrcs_admingroupfuncorg` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_admingroupfunc（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_admingroupfunc.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_admingroupfunc.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_admingroupfunc.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_admingroupfunc.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_admingroupfunc.finitdatasource |  |  |
| admingroup | 管理员组 | BasedataField | t_hrcs_admingroupfunc.fadmingroupid |  | perm_admingroup |
| bucafunc | 职能 | BasedataField | t_hrcs_admingroupfunc.fbucafuncid |  | hbss_hrbucafunc |
| entryentity | 单据体 | EntryEntity | → t_hrcs_admingroupfuncorg |  |  |

### 字段列表 — t_hrcs_admingroupfuncorg（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| org | 业务单元 | OrgField | t_hrcs_admingroupfuncorg.forgid |  | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_admingroupfunc（主表） | 7 |
| t_hrcs_admingroupfuncorg（单据体） | 1 |

