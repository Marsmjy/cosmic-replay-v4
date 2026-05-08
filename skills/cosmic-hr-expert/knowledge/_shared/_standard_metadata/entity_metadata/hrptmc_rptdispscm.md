# hrptmc_rptdispscm — 显示方案配置

**表单编码**: `hrptmc_rptdispscm`  
**表单ID**: `36K7IFZ2X8H4`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_rptdispscm（显示方案配置） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_rptdispscm` | BaseEntity | 主表 |
| `t_hrptmc_rptdispscmety` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_rptdispscm（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| rptmanage | 报表管理 | BasedataField | t_hrptmc_rptdispscm.frptmanageid |  | hrptmc_reportmanage |
| workrpt | 工作表 | BasedataField | t_hrptmc_rptdispscm.fworkrptid |  | hrptmc_workreport |
| enable1 | 启用用户显示方案 | RadioOptGroupField | — | ✓ |  |
| enable | 用户可自定义报表显示方案 | CheckBoxField | t_hrptmc_rptdispscm.fenable |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrptmc_rptdispscmety |  |  |

### 字段列表 — t_hrptmc_rptdispscmety（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| name | 名称 | MuliLangTextField | t_hrptmc_rptdispscmety.fname |  |  |
| description | 描述 | MuliLangTextField | t_hrptmc_rptdispscmety.fdescription |  |  |
| default | 默认方案 | CheckBoxField | t_hrptmc_rptdispscmety.fdefault |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_rptdispscm（主表） | 4 |
| t_hrptmc_rptdispscmety（单据体） | 3 |
| 无数据库列 | 1 |

