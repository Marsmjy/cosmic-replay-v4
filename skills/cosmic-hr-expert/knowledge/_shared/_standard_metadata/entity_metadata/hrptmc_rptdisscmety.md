# hrptmc_rptdisscmety — 显示方案配置

**表单编码**: `hrptmc_rptdisscmety`  
**表单ID**: `59ALJ=6+1QPQ`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_rptdisscmety（显示方案配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_rptdispscmety` | BaseEntity | 主表 |
| `t_hrptmc_rptdispscmrow` | EntryEntity | 行维度分录 |
| `t_hrptmc_rptdispscmcol` | EntryEntity | 列维度分录 |
| `t_hrptmc_rptdispscmidx` | EntryEntity | 指标分录 |

### 字段列表 — t_hrptmc_rptdispscmety（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| name | 名称 | MuliLangTextField | t_hrptmc_rptdispscmety.fname | ✓ |  |
| description | 描述 | MuliLangTextField | t_hrptmc_rptdispscmety.fdescription |  |  |
| default1 | 默认方案 | CheckBoxField | — |  |  |
| default | 设为默认 | RadioOptGroupField | t_hrptmc_rptdispscmety.fdefault | ✓ |  |
| parent | 显示方案 | BasedataField | t_hrptmc_rptdispscmety.fid |  | hrptmc_rptdispscm |
| index | 整数 | IntegerField | t_hrptmc_rptdispscmety.fseq |  |  |
| rowentryentity | 行维度分录 | EntryEntity | → t_hrptmc_rptdispscmrow |  |  |
| colentryentity | 列维度分录 | EntryEntity | → t_hrptmc_rptdispscmcol |  |  |
| indexentryentity | 指标分录 | EntryEntity | → t_hrptmc_rptdispscmidx |  |  |

### 字段列表 — t_hrptmc_rptdispscmrow（行维度分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rowfield | 行维度 | BasedataField | t_hrptmc_rptdispscmrow.frowfieldid |  | hrptmc_rowfield |
| rowsecondaryheader | 二级表头 | MuliLangTextField | t_hrptmc_rptdispscmrow.ftableheadlv2 |  |  |
| rowhide | 隐藏列 | CheckBoxField | t_hrptmc_rptdispscmrow.fhide |  |  |
| rowfreeze | 列冻结 | CheckBoxField | t_hrptmc_rptdispscmrow.ffreeze |  |  |
| rowenable | 允许用户编辑 | CheckBoxField | t_hrptmc_rptdispscmrow.fenable |  |  |
| rowdisplayname | 字段名称 | MuliLangTextField | t_hrptmc_rptdispscmrow.fdisplayname |  |  |
| rowfieldname | 字段名称 | MuliLangTextField | — |  |  |
| rowfieldalias | 字段别名 | TextField | — |  |  |
| parentfield | 父行维度 | BasedataField | — |  | hrptmc_rowfield |
| treeid | 树形ID | TextField | t_hrptmc_rptdispscmrow.ftreeid |  |  |
| parenttreeid | 树形父ID | TextField | t_hrptmc_rptdispscmrow.fparenttreeid |  |  |

### 字段列表 — t_hrptmc_rptdispscmcol（列维度分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| colfield | 列维度 | BasedataField | t_hrptmc_rptdispscmcol.fcolfieldid |  | hrptmc_colfield |
| colhide | 隐藏列 | CheckBoxField | t_hrptmc_rptdispscmcol.fhide |  |  |
| colenable | 允许用户编辑 | CheckBoxField | t_hrptmc_rptdispscmcol.fenable |  |  |
| colfieldname | 字段名称 | MuliLangTextField | — |  |  |
| colfieldalias | 字段别名 | TextField | — |  |  |

### 字段列表 — t_hrptmc_rptdispscmidx（指标分录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| indexfield | 指标 | BasedataField | t_hrptmc_rptdispscmidx.frowfieldid |  | hrptmc_rowfield |
| indexhide | 隐藏列 | CheckBoxField | t_hrptmc_rptdispscmidx.fhide |  |  |
| indexenable | 允许用户编辑 | CheckBoxField | t_hrptmc_rptdispscmidx.fenable |  |  |
| indexdisplayname | 字段名称 | MuliLangTextField | t_hrptmc_rptdispscmidx.fdisplayname |  |  |
| indexfieldname | 字段名称 | MuliLangTextField | — |  |  |
| indexfieldalias | 字段别名 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_rptdispscmety（主表） | 6 |
| t_hrptmc_rptdispscmrow（行维度分录） | 11 |
| t_hrptmc_rptdispscmcol（列维度分录） | 5 |
| t_hrptmc_rptdispscmidx（指标分录） | 6 |
| 无数据库列 | 8 |

