# hrcs_filterscene — 过滤场景

**表单编码**: `hrcs_filterscene`  
**表单ID**: `2VTVMS+SQK5T`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_filterscene（过滤场景） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_filterscene` | BaseEntity | 主表 |
| `t_hrcs_filterparam` | EntryEntity | 输入参数 |

### 字段列表 — t_hrcs_filterscene（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 场景编码 | TextField | t_hrcs_filterscene.fnumber |  |  |
| name | 场景名称 | MuliLangTextField | t_hrcs_filterscene.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_filterscene.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_filterscene.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_filterscene.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_filterscene.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_filterscene.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_filterscene.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_filterscene.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_filterscene.fsimplename |  |  |
| description | 场景描述 | MuliLangTextField | t_hrcs_filterscene.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_filterscene.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_filterscene.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_filterscene.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_filterscene.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_filterscene.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_filterscene.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_filterscene.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_filterscene.foriname |  |  |
| bizappid | 所属应用 | BasedataField | t_hrcs_filterscene.fbizappid | ✓ | hbp_devportal_bizapp |
| sceneinputparams | 输入参数 | EntryEntity | → t_hrcs_filterparam |  |  |

### 字段列表 — t_hrcs_filterparam（输入参数·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| inputnumber | 参数标识 | TextField | t_hrcs_filterparam.fnumber |  |  |
| inputparamstype | 参数类型 | ComboField | t_hrcs_filterparam.fparamstype |  |  |
| inputobject | 业务对象/基础资料 | BasedataField | t_hrcs_filterparam.fparamsobject |  | bos_entityobject |
| inputname | 参数名称 | MuliLangTextField | t_hrcs_filterparam.fname |  |  |
| inputcombo | 下拉项 | TextField | t_hrcs_filterparam.fcombofield |  |  |
| inputmultiple | 允许多选 | ComboField | t_hrcs_filterparam.fmultiple |  |  |
| inputdateformat | 掩码 | ComboField | t_hrcs_filterparam.fdateformat |  |  |
| inputdynprop | 字段 | TextField | t_hrcs_filterparam.fdynprop |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_filterscene（主表） | 20 |
| t_hrcs_filterparam（输入参数） | 8 |

