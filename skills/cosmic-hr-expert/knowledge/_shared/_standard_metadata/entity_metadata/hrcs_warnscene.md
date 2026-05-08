# hrcs_warnscene — 预警场景

**表单编码**: `hrcs_warnscene`  
**表单ID**: `441YCYWTTUWA`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnscene（预警场景） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnscene` | BaseEntity | 主表 |
| `t_hrcs_warnrcperm` | EntryEntity | 消息接收人控权维度单据体 |

### 字段列表 — t_hrcs_warnscene（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 场景编码 | TextField | t_hrcs_warnscene.fnumber |  |  |
| name | 场景名称 | MuliLangTextField | t_hrcs_warnscene.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_warnscene.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_warnscene.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_warnscene.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_warnscene.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnscene.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnscene.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_warnscene.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_warnscene.fsimplename |  |  |
| description | 场景描述 | MuliLangTextField | t_hrcs_warnscene.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_warnscene.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_warnscene.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_warnscene.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_warnscene.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnscene.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_warnscene.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_warnscene.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_warnscene.foriname |  |  |
| bizapp | 所属应用 | BasedataField | t_hrcs_warnscene.fbizappid | ✓ | hbp_devportal_bizapp |
| warnobjtpl | 预警对象(废弃) | BasedataField | t_hrcs_warnscene.fwarnobjtplid |  | hrcs_warnobjtpl |
| ruledate | 触发日期操作的控件（勿删） | DateField | — |  |  |
| commoncondition | 常用条件 | TextField | t_hrcs_warnscene.fcommoncondition |  |  |
| datafilter | 数据过滤规则 | TextField | t_hrcs_warnscene.fdatafilter |  |  |
| permrc | 预警消息按接收人权限过滤 | CheckBoxField | t_hrcs_warnscene.fpermrc |  |  |
| warnfromentiy | 处理预警业务功能 | BasedataField | t_hrcs_warnscene.fwarnfromentiy |  | hbp_funcprementity |
| warnbizappid | 所属应用 | BasedataField | t_hrcs_warnscene.fwarnbizappid |  | hbp_devportal_bizapp |
| adconditionviewexpr | 高级条件公式 | TextField | — |  |  |
| warnrcpermentryentity | 消息接收人控权维度单据体 | EntryEntity | → t_hrcs_warnrcperm |  |  |

### 字段列表 — t_hrcs_warnrcperm（消息接收人控权维度单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| warnentityperm | 控权维度 | BasedataField | t_hrcs_warnrcperm.fwarnentityperm |  | hrcs_dimension |
| fieldname | 映射的预警对象控权字段 | TextField | t_hrcs_warnrcperm.ffieldname | ✓ |  |
| fieldalias | 字段别名 | TextField | t_hrcs_warnrcperm.ffieldalias |  |  |
| fieldpath | 字段全路径编码 | TextField | t_hrcs_warnrcperm.ffieldpath |  |  |
| valuetype | 字段值类型 | TextField | t_hrcs_warnrcperm.fvaluetype |  |  |
| entitynumber | 所属实体编码 | TextField | t_hrcs_warnrcperm.fentitynumber |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnscene（主表） | 28 |
| t_hrcs_warnrcperm（消息接收人控权维度单据体） | 6 |
| 无数据库列 | 2 |

