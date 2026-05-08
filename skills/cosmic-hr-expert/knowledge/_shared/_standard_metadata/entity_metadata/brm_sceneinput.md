# brm_sceneinput — 输入参数

**表单编码**: `brm_sceneinput`  
**表单ID**: `1IMW5O=NE+T1`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_sceneinput（输入参数） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_brm_sceneinput` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 属性字段 |
| `（虚拟分录）` | EntryEntity | 下拉项 |

### 字段列表 — t_brm_sceneinput（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 参数标识 | TextField | t_brm_sceneinput.fnumber |  |  |
| name | 参数名称 | MuliLangTextField | t_brm_sceneinput.fname |  |  |
| status | 数据状态 | BillStatusField | t_brm_sceneinput.fstatus |  |  |
| creator | 创建人 | CreaterField | t_brm_sceneinput.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_brm_sceneinput.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_brm_sceneinput.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_brm_sceneinput.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_brm_sceneinput.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_brm_sceneinput.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_brm_sceneinput.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_brm_sceneinput.fdescription |  |  |
| index | 排序号 | IntegerField | t_brm_sceneinput.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_brm_sceneinput.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_brm_sceneinput.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_brm_sceneinput.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_brm_sceneinput.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_brm_sceneinput.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_brm_sceneinput.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_brm_sceneinput.foriname |  |  |
| booleanfield | 布尔类型绑定字段（勿删） | ComboField | — |  |  |
| paramstype | 参数类型 | ComboField | t_brm_sceneinput.fparamstype | ✓ |  |
| paramsobject | 业务对象 | BasedataField | t_brm_sceneinput.fparamsobject |  | hbp_entityobject |
| basedatafield | 基础资料 | BasedataField | t_brm_sceneinput.modeltype |  | hbp_entityobject |
| combofield | 下拉项 | TextField | t_brm_sceneinput.fcombofield |  |  |
| multiple | 支持多选 | CheckBoxField | t_brm_sceneinput.fmultiple |  |  |
| dateformat | 掩码 | ComboField | t_brm_sceneinput.fdateformat | ✓ |  |
| dynprop | 属性字段 | TextField | t_brm_sceneinput.fdynprop |  |  |
| presetisedit | 预置是否已更新 | CheckBoxField | t_brm_sceneinput.fpresetisedit |  |  |
| treelv | 属性字段层级 | IntegerField | t_brm_sceneinput.ftreelv |  |  |
| entryentity | 属性字段 | EntryEntity | → （虚拟分录） |  |  |
| entryentity1 | 下拉项 | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （属性字段·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （下拉项·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_brm_sceneinput（主表） | 29 |
| （属性字段） | 6 |
| （下拉项） | 6 |
| 无数据库列 | 7 |

