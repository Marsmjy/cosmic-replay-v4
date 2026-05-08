# hrptmc_dimmap — 维度映射

**表单编码**: `hrptmc_dimmap`  
**表单ID**: `2Y4VSGLRMENO`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_dimmap（维度映射） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_dimmap` | BaseEntity | 主表 |
| `t_hrptmc_dimmapentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_dimmap（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| preindex | 预置指标 | BasedataField | t_hrptmc_dimmap.fpreindexid |  | hrptmc_preindex |
| preindexnumber | 预置指标编码 | TextField | t_hrptmc_dimmap.fpreindexnumber |  |  |
| report | 报表 | BasedataField | t_hrptmc_dimmap.freportid |  | hrptmc_reportmanage |
| workrpt | 工作表 | BasedataField | t_hrptmc_dimmap.fworkrptid |  | hrptmc_workreport |
| entryentity | 单据体 | EntryEntity | → t_hrptmc_dimmapentry |  |  |

### 字段列表 — t_hrptmc_dimmapentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dim | 报表维度 | MuliLangTextField | t_hrptmc_dimmapentry.fdim |  |  |
| dimtype | 维度类型 | ComboField | t_hrptmc_dimmapentry.fdimtype |  |  |
| dimfrom | 维度来源 | ComboField | t_hrptmc_dimmapentry.fdimfrom |  |  |
| paramrule | 入参规则 | ComboField | t_hrptmc_dimmapentry.fparamrule |  |  |
| preindexparam | 对应预置参数 | ComboField | t_hrptmc_dimmapentry.fpreindexparam |  |  |
| dimnumber | 维度编码 | TextField | t_hrptmc_dimmapentry.fdimnumber |  |  |
| dimrealfrom | 维度实际来源 | ComboField | t_hrptmc_dimmapentry.fdimrealfrom |  |  |
| paramtype | 参数类型 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_dimmap（主表） | 4 |
| t_hrptmc_dimmapentry（单据体） | 8 |
| 无数据库列 | 1 |

