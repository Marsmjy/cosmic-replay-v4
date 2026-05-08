# hrcs_filterparam — 过滤场景参数

**表单编码**: `hrcs_filterparam`  
**表单ID**: `2VTVPMR65PD=`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_filterparam（过滤场景参数） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_filterparam` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 属性字段 |
| `（虚拟分录）` | EntryEntity | 下拉项 |

### 字段列表 — t_hrcs_filterparam（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 参数标识 | TextField | t_hrcs_filterparam.fnumber |  |  |
| name | 参数名称 | MuliLangTextField | t_hrcs_filterparam.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_filterparam.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_filterparam.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_filterparam.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_filterparam.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_filterparam.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_filterparam.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_filterparam.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_filterparam.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_filterparam.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_filterparam.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_filterparam.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_filterparam.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_filterparam.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_filterparam.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_filterparam.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_filterparam.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_filterparam.foriname |  |  |
| booleanfield | 布尔类型绑定字段（勿删） | ComboField | — |  |  |
| paramstype | 参数类型 | ComboField | t_hrcs_filterparam.fparamstype | ✓ |  |
| paramsobject | 业务对象 | BasedataField | t_hrcs_filterparam.fparamsobject |  | hbp_entityobject |
| basedatafield | 基础资料 | BasedataField | t_hrcs_filterparam.modeltype |  | hbp_entityobject |
| combofield | 下拉项 | TextField | t_hrcs_filterparam.fcombofield |  |  |
| multiple | 支持多选 | CheckBoxField | t_hrcs_filterparam.fmultiple |  |  |
| dateformat | 掩码 | ComboField | t_hrcs_filterparam.fdateformat |  |  |
| dynprop | 属性字段 | TextField | t_hrcs_filterparam.fdynprop |  |  |
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
| t_hrcs_filterparam（主表） | 27 |
| （属性字段） | 5 |
| （下拉项） | 5 |
| 无数据库列 | 6 |

