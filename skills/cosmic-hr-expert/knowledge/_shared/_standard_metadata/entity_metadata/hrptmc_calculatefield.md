# hrptmc_calculatefield — 计算字段

**表单编码**: `hrptmc_calculatefield`  
**表单ID**: `2VFS8JTKXAR5`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_calculatefield（计算字段） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_calculatefield` | BaseEntity | 主表 |
| `t_hrptmc_calreffield` | EntryEntity | 引用字段 |

### 字段列表 — t_hrptmc_calculatefield（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrptmc_calculatefield.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrptmc_calculatefield.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrptmc_calculatefield.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_calculatefield.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_calculatefield.finitdatasource |  |  |
| number | 编码 | TextField | t_hrptmc_calculatefield.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrptmc_calculatefield.fname |  |  |
| valuetype | 字段值类型 | ComboField | t_hrptmc_calculatefield.fvaluetype |  |  |
| controltype | 控件类型 | ComboField | t_hrptmc_calculatefield.fcontroltype |  |  |
| expr | 表达式（用于执行） | TextField | t_hrptmc_calculatefield.fexpr |  |  |
| anobj | 分析对象 | BasedataField | t_hrptmc_calculatefield.fanobjid |  | hrptmc_analyseobject |
| type | 类型 | ComboField | t_hrptmc_calculatefield.ftype |  |  |
| source | 字段来源 | ComboField | t_hrptmc_calculatefield.fsource |  |  |
| order | 执行优先级 | IntegerField | t_hrptmc_calculatefield.forder |  |  |
| isselected | 是否选中 | CheckBoxField | t_hrptmc_calculatefield.fisselected |  |  |
| index | 排序号 | IntegerField | t_hrptmc_calculatefield.findex |  |  |
| report | 报表 | BasedataField | t_hrptmc_calculatefield.freportid |  | hrptmc_reportmanage |
| calmethod | 计算方式 | ComboField | t_hrptmc_calculatefield.fcalmethod |  |  |
| viewexpr | 表达式（用于展示） | TextField | t_hrptmc_calculatefield.fviewexpr |  |  |
| hidefield | 是否隐藏 | CheckBoxField | t_hrptmc_calculatefield.fhidefield |  |  |
| reffieldentry | 引用字段 | EntryEntity | → t_hrptmc_calreffield |  |  |

### 字段列表 — t_hrptmc_calreffield（引用字段·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| reffieldalias | 引用实体字段别名 | TextField | t_hrptmc_calreffield.freffieldalias |  |  |
| refcalfield | 引用计算字段 | BasedataField | t_hrptmc_calreffield.frefcalfieldid |  | hrptmc_calculatefield |
| reftype | 引用字段类型 | ComboField | t_hrptmc_calreffield.freftype |  |  |
| refpreindex | 引用预置指标 | BasedataField | t_hrptmc_calreffield.fpreindexid |  | hrptmc_preindex |
| aggregation | 聚合方式 | ComboField | t_hrptmc_calreffield.faggregation |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_calculatefield（主表） | 20 |
| t_hrptmc_calreffield（引用字段） | 5 |

