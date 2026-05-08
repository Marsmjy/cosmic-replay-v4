# hrcs_dynaschdatarule — 动态权限方案数据规则

**表单编码**: `hrcs_dynaschdatarule`  
**表单ID**: `5+5ON49SFPM0`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynaschdatarule（动态权限方案数据规则） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_dynaschdatarule` | BaseEntity | 主表 |
| `t_hrcs_dynaschdrentry` | EntryEntity | 角色数据规则分录 |
| `t_hrcs_dynaschbdentry` | EntryEntity | 角色基础资料数据规则分录 |

### 字段列表 — t_hrcs_dynaschdatarule（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrcs_dynaschdatarule.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrcs_dynaschdatarule.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrcs_dynaschdatarule.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_dynaschdatarule.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_dynaschdatarule.finitdatasource |  |  |
| scheme | 动态权限方案 | BasedataField | t_hrcs_dynaschdatarule.fschemeid |  | hrcs_dynascheme |
| role | 角色 | BasedataField | t_hrcs_dynaschdatarule.froleid |  | perm_role |
| app | 应用id | TextField | t_hrcs_dynaschdatarule.fappid |  |  |
| entitytype | 实体编码 | TextField | t_hrcs_dynaschdatarule.fentitytypeid |  |  |
| bucafunc | 业务职能 | BasedataField | t_hrcs_dynaschdatarule.fbucafuncid |  | bos_org_biz |
| roleentryid | 方案角色分录ID | BigIntField | t_hrcs_dynaschdatarule.froleentryid |  |  |
| dataruleentry | 角色数据规则分录 | EntryEntity | → t_hrcs_dynaschdrentry |  |  |
| rolebdruleentry | 角色基础资料数据规则分录 | EntryEntity | → t_hrcs_dynaschbdentry |  |  |

### 字段列表 — t_hrcs_dynaschdrentry（角色数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| permitem | 权限项 | BasedataField | t_hrcs_dynaschdrentry.fpermitemid |  | perm_permitem |
| datarule | 数据规则方案 | BasedataField | t_hrcs_dynaschdrentry.fdataruleid |  | hrcs_datarule |

### 字段列表 — t_hrcs_dynaschbdentry（角色基础资料数据规则分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| propkey | 属性 | TextField | t_hrcs_dynaschbdentry.fpropkey |  |  |
| propentnum | 属性实体编码 | TextField | t_hrcs_dynaschbdentry.fpropentnum |  |  |
| bddatarule | 方案 | BasedataField | t_hrcs_dynaschbdentry.fbddatarule |  | hrcs_datarule |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_dynaschdatarule（主表） | 11 |
| t_hrcs_dynaschdrentry（角色数据规则分录） | 2 |
| t_hrcs_dynaschbdentry（角色基础资料数据规则分录） | 3 |

