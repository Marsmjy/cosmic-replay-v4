# hrptmc_colfield — 列字段

**表单编码**: `hrptmc_colfield`  
**表单ID**: `2W/O98P0RADW`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_colfield（列字段） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_colfield` | BaseEntity | 主表 |
| `t_hrptmc_dataformat` | EntryEntity | 数据格式 |

### 字段列表 — t_hrptmc_colfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_colfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_colfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_colfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_colfield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_colfield.finitdatasource |  |  |
| displayname | 显示名称 | MuliLangTextField | t_hrptmc_colfield.fdisplayname |  |  |
| numberalias | 编码别名 | TextField | t_hrptmc_colfield.fnumberalias |  |  |
| bizindex | 序号 | IntegerField | t_hrptmc_colfield.fbizindex |  |  |
| rptmanage | 报表管理 | BasedataField | t_hrptmc_colfield.frptmanageid |  | hrptmc_reportmanage |
| anobjfield | 分析对象字段 | BasedataField | t_hrptmc_colfield.fanobjfieldid |  | hrptmc_anobjqueryfield |
| calcidxfield | 计算指标字段 | BasedataField | t_hrptmc_colfield.fcalcidxfieldid |  | hrptmc_calculatefield |
| sort | 排序方式 | ComboField | t_hrptmc_colfield.fsort |  |  |
| showemptycol | 显示空值列 | CheckBoxField | t_hrptmc_colfield.fshowemptycol |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_colfield.fworkrptid |  | hrptmc_workreport |
| entryentity | 数据格式 | EntryEntity | → t_hrptmc_dataformat |  |  |

### 字段列表 — t_hrptmc_dataformat（数据格式·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| datedisplaymode | 显示方式 | TextField | t_hrptmc_dataformat.fdisplaymode |  |  |
| nullrule | 空值规则 | TextField | t_hrptmc_dataformat.fnullrule |  |  |
| decimaldigits | 小数位数 | IntegerField | t_hrptmc_dataformat.fdecimaldigits |  |  |
| roundmethod | 取整方式 | TextField | t_hrptmc_dataformat.froundmethod |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_colfield（主表） | 14 |
| t_hrptmc_dataformat（数据格式） | 4 |

