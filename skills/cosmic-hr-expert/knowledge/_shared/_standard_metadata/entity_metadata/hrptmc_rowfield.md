# hrptmc_rowfield — 行字段

**表单编码**: `hrptmc_rowfield`  
**表单ID**: `2VY68ZWZYE2T`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_rowfield（行字段） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_rowfield` | BaseEntity | 主表 |
| `t_hrptmc_dataformat` | EntryEntity | 数据格式 |

### 字段列表 — t_hrptmc_rowfield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_rowfield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_rowfield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_rowfield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_rowfield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_rowfield.finitdatasource |  |  |
| displayname | 显示名称 | MuliLangTextField | t_hrptmc_rowfield.fdisplayname |  |  |
| numberalias | 编码别名 | TextField | t_hrptmc_rowfield.fnumberalias |  |  |
| type | 类型 | ComboField | t_hrptmc_rowfield.ftype |  |  |
| algorithm | 汇总方式 | ComboField | t_hrptmc_rowfield.falgorithm |  |  |
| dataformat | 数据格式 | BasedataField | — |  | hrptmc_rowfield |
| displaymode | 显示方式 | ComboField | t_hrptmc_rowfield.fdisplaymode |  |  |
| parentid | 父字段 | BasedataField | t_hrptmc_rowfield.fparentid |  | hrptmc_rowfield |
| rptmanage | 报表管理 | BasedataField | t_hrptmc_rowfield.frptmanageid |  | hrptmc_reportmanage |
| mergetype | 合并方式 | ComboField | t_hrptmc_rowfield.fmergetype |  |  |
| bizindex | 序号 | IntegerField | t_hrptmc_rowfield.fbizindex |  |  |
| anobjfield | 分析对象字段 | BasedataField | t_hrptmc_rowfield.fanobjfieldid |  | hrptmc_anobjqueryfield |
| calcidxfield | 计算指标字段 | BasedataField | t_hrptmc_rowfield.fcalcidxfieldid |  | hrptmc_calculatefield |
| preidxfield | 预置指标字段 | BasedataField | t_hrptmc_rowfield.fpreidxfieldid |  | hrptmc_preindex |
| sort | 排序方式 | ComboField | t_hrptmc_rowfield.fsort |  |  |
| showemptycol | 显示空值列 | CheckBoxField | t_hrptmc_rowfield.fshowemptycol |  |  |
| orgversiondate | 组织切片时间 | TextField | t_hrptmc_rowfield.forgversiondate |  |  |
| workrpt | 工作表 | BasedataField | t_hrptmc_rowfield.fworkrptid |  | hrptmc_workreport |
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
| t_hrptmc_rowfield（主表） | 22 |
| t_hrptmc_dataformat（数据格式） | 4 |
| 无数据库列 | 1 |

