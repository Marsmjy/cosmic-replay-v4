# hrptmc_userdispscm — 显示方案配置

**表单编码**: `hrptmc_userdispscm`  
**表单ID**: `372NZTKTEMK0`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_userdispscm（显示方案配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_userdispscm` | BaseEntity | 主表 |
| `t_hrptmc_userdisprow` | EntryEntity | 行维度分录 |
| `t_hrptmc_userdispcol` | EntryEntity | 列维度分录 |
| `t_hrptmc_userdispinx` | EntryEntity | 指标分录 |

### 字段列表 — t_hrptmc_userdispscm（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| defaultscheme | 设为默认方案 | CheckBoxField | t_hrptmc_userdispscm.fdefaultscm |  |  |
| schemedesc | 方案描述 | MuliLangTextField | t_hrptmc_userdispscm.fdescription | ✓ |  |
| modifier | 修改人 | ModifierField | t_hrptmc_userdispscm.fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_userdispscm.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_userdispscm.fmodifytime |  |  |
| number | 编码 | TextField | t_hrptmc_userdispscm.fnumber |  |  |
| creator | 创建人 | CreaterField | t_hrptmc_userdispscm.fcreatorid |  | bos_user |
| name | 方案名称 | MuliLangTextField | t_hrptmc_userdispscm.fname | ✓ |  |
| user | 用户 | UserField | t_hrptmc_userdispscm.fuser |  | bos_user |
| rptmanage | 方案所属的报表 | BasedataField | t_hrptmc_userdispscm.frptmanageid |  | hrptmc_reportmanage |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_userdispscm.fissyspreset |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_userdispscm.fworkrptid |  | hrptmc_workreport |
| publishtype | 发布类型 | ComboField | t_hrptmc_userdispscm.fpublishtype |  |  |
| rowentryentity | 行维度分录 | EntryEntity | → t_hrptmc_userdisprow |  |  |
| colentryentity | 列维度分录 | EntryEntity | → t_hrptmc_userdispcol |  |  |
| indexentryentity | 指标分录 | EntryEntity | → t_hrptmc_userdispinx |  |  |

### 字段列表 — t_hrptmc_userdisprow（行维度分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rowfreeze | 列冻结 | CheckBoxField | t_hrptmc_userdisprow.ffreeze |  |  |
| rowenable | 允许用户编辑 | CheckBoxField | — |  |  |
| rowfield | 行维度 | BasedataField | t_hrptmc_userdisprow.frowfieldid |  | hrptmc_rowfield |
| userfield | 用户 | UserField | t_hrptmc_userdisprow.fuserid |  | bos_user |
| rowsecondaryheader | 二级表头 | MuliLangTextField | t_hrptmc_userdisprow.fsecondaryheader |  |  |
| rowhide | 隐藏列 | CheckBoxField | t_hrptmc_userdisprow.fhide |  |  |
| rowdisplayname | 显示名称 | MuliLangTextField | t_hrptmc_userdisprow.fdisplayname |  |  |
| treeid | 树形ID | TextField | t_hrptmc_userdisprow.ftreeid |  |  |
| parenttreeid | 树形父ID | TextField | t_hrptmc_userdisprow.fparenttreeid |  |  |

### 字段列表 — t_hrptmc_userdispcol（列维度分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| colfield | 列维度 | BasedataField | t_hrptmc_userdispcol.fcolfieldid |  | hrptmc_colfield |
| colhide | 隐藏列 | CheckBoxField | t_hrptmc_userdispcol.fhide |  |  |
| colenable | 允许用户编辑 | CheckBoxField | — |  |  |

### 字段列表 — t_hrptmc_userdispinx（指标分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| indexfield | 指标 | BasedataField | t_hrptmc_userdispinx.findexfieldid |  | hrptmc_rowfield |
| indexhide | 隐藏列 | CheckBoxField | t_hrptmc_userdispinx.fhide |  |  |
| indexenable | 允许用户编辑 | CheckBoxField | — |  |  |
| indexdisplayname | 显示名称 | MuliLangTextField | t_hrptmc_userdispinx.fdisplayname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_userdispscm（主表） | 13 |
| t_hrptmc_userdisprow（行维度分录） | 9 |
| t_hrptmc_userdispcol（列维度分录） | 3 |
| t_hrptmc_userdispinx（指标分录） | 4 |
| 无数据库列 | 3 |

