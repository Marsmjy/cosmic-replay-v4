# hrptmc_preindex — 预置指标

**表单编码**: `hrptmc_preindex`  
**表单ID**: `2W/WJWOZYV9I`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_preindex（预置指标） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_preindex` | BaseEntity | 主表 |
| `t_hrptmc_preindexparam` | EntryEntity | 服务参数 |
| `t_hrptmc_preindexcol` | EntryEntity | 分析对象字段 |

### 字段列表 — t_hrptmc_preindex（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrptmc_preindex.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrptmc_preindex.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrptmc_preindex.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrptmc_preindex.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrptmc_preindex.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrptmc_preindex.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrptmc_preindex.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_preindex.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrptmc_preindex.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrptmc_preindex.fsimplename |  |  |
| description | 指标释义 | MuliLangTextField | t_hrptmc_preindex.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrptmc_preindex.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_preindex.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrptmc_preindex.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrptmc_preindex.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_preindex.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrptmc_preindex.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrptmc_preindex.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrptmc_preindex.foriname |  |  |
| getway | 获取方式 | ComboField | t_hrptmc_preindex.fgetway | ✓ |  |
| calmethod | 计算方式 | ComboField | t_hrptmc_preindex.fcalmethod |  |  |
| service | 服务 | BasedataField | t_hrptmc_preindex.fserviceid |  | hrptmc_busiservice |
| anobj | 分析对象 | BasedataField | t_hrptmc_preindex.fanobjid |  | hrptmc_analyseobject |
| targetfrom | 指标来源 | ComboField | t_hrptmc_preindex.ftargetfrom |  |  |
| target | 指标 | ComboField | t_hrptmc_preindex.ftarget |  |  |
| entryentity_param | 服务参数 | EntryEntity | → t_hrptmc_preindexparam |  |  |
| entryentity_col | 分析对象字段 | EntryEntity | → t_hrptmc_preindexcol |  |  |

### 字段列表 — t_hrptmc_preindexparam（服务参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| selparam | 参数选择 | BasedataField | t_hrptmc_preindexparam.fbusisrvparamid |  | hrptmc_selparam |
| paramname | 参数名称 | MuliLangTextField | t_hrptmc_preindexparam.fname |  |  |
| isrequired | 是否必传 | CheckBoxField | t_hrptmc_preindexparam.fisrequired |  |  |

### 字段列表 — t_hrptmc_preindexcol（分析对象字段·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| colfrom | 字段来源 | ComboField | t_hrptmc_preindexcol.fcolfrom |  |  |
| colnumber | 字段编码 | TextField | t_hrptmc_preindexcol.fnumber |  |  |
| colname | 字段名称 | MuliLangTextField | t_hrptmc_preindexcol.fname |  |  |
| coltype | 字段类型 | ComboField | t_hrptmc_preindexcol.fcoltype |  |  |
| isrequired1 | 是否必传 | CheckBoxField | t_hrptmc_preindexcol.fisrequired |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_preindex（主表） | 25 |
| t_hrptmc_preindexparam（服务参数） | 3 |
| t_hrptmc_preindexcol（分析对象字段） | 5 |

