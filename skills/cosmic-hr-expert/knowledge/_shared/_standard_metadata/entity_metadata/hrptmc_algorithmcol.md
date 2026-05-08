# hrptmc_algorithmcol — 汇总列

**表单编码**: `hrptmc_algorithmcol`  
**表单ID**: `3F8R2+8H/GSB`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_algorithmcol（汇总列） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_algorithmcol` | BaseEntity | 主表 |
| `t_hrptmc_colalgorithmtype` | EntryEntity | 列汇总方式 |

### 字段列表 — t_hrptmc_algorithmcol（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_algorithmcol.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_algorithmcol.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_algorithmcol.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_algorithmcol.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_algorithmcol.finitdatasource |  |  |
| showlocation | 展示位置 | ComboField | t_hrptmc_algorithmcol.fshowlocation |  |  |
| total | 显示汇总列（总计列） | CheckBoxField | t_hrptmc_algorithmcol.ftotal |  |  |
| totalname | 总计列显示名 | MuliLangTextField | t_hrptmc_algorithmcol.ftotalname |  |  |
| subtotal | 显示分类汇总列（小计列） | CheckBoxField | t_hrptmc_algorithmcol.fsubtotal |  |  |
| subtotalname | 小计列显示名 | MuliLangTextField | t_hrptmc_algorithmcol.fsubtotalname |  |  |
| dimfield | 小计维度 | TextField | t_hrptmc_algorithmcol.fdimfield |  |  |
| rptconfig | 报表配置 | BasedataField | t_hrptmc_algorithmcol.frptconfigid |  | hrptmc_reportconfig |
| entryentity | 列汇总方式 | EntryEntity | → t_hrptmc_colalgorithmtype |  |  |

### 字段列表 — t_hrptmc_colalgorithmtype（列汇总方式·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| numberalias | 指标别名 | TextField | t_hrptmc_colalgorithmtype.fnumberalias |  |  |
| algorithm | 计算方式 | TextField | t_hrptmc_colalgorithmtype.falgorithm |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_algorithmcol（主表） | 12 |
| t_hrptmc_colalgorithmtype（列汇总方式） | 2 |

