# hrcs_warnobjtpl — 预警对象模板(废弃)

**表单编码**: `hrcs_warnobjtpl`  
**表单ID**: `43+SLC3=E3OG`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnobjtpl（预警对象模板(废弃)） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_warnobjecttpl` | BaseEntity | 主表 |
| `t_hrcs_warndataperm` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_warnobjecttpl（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 对象模板编码 | TextField | t_hrcs_warnobjecttpl.fnumber |  |  |
| name | 对象模板名称 | MuliLangTextField | t_hrcs_warnobjecttpl.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_warnobjecttpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_warnobjecttpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_warnobjecttpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_warnobjecttpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_warnobjecttpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_warnobjecttpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_warnobjecttpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_warnobjecttpl.fsimplename |  |  |
| description | 对象模板描述 | MuliLangTextField | t_hrcs_warnobjecttpl.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_warnobjecttpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_warnobjecttpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_warnobjecttpl.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_warnobjecttpl.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_warnobjecttpl.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_warnobjecttpl.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_warnobjecttpl.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_warnobjecttpl.foriname |  |  |
| fromentity | 来源业务功能 | BasedataField | t_hrcs_warnobjecttpl.ffromentiyid |  | hbp_funcprementity |
| bizapp | 所属应用 | BasedataField | t_hrcs_warnobjecttpl.fbizappid |  | hbp_devportal_bizapp |
| ruledate | 触发日期操作的控件（勿删） | DateField | — |  |  |
| datafilter | 数据过滤规则 | TextField | t_hrcs_warnobjecttpl.fdatafilter |  |  |
| adconditionviewexpr | 高级条件公式 | TextField | — |  |  |
| warnpermentryentity | 单据体 | EntryEntity | → t_hrcs_warndataperm |  |  |

### 字段列表 — t_hrcs_warndataperm（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| warnfieldname | 映射的预警对象控权字段 | TextField | t_hrcs_warndataperm.ffieldname | ✓ |  |
| warnfieldalias | 字段别名 | TextField | t_hrcs_warndataperm.ffieldalias |  |  |
| warnfieldpath | 字段全路径编码 | TextField | t_hrcs_warndataperm.ffieldpath |  |  |
| warnvaluetype | 字段值类型 | TextField | t_hrcs_warndataperm.fvaluetype |  |  |
| warnentitynumber | 所属实体编码 | TextField | t_hrcs_warndataperm.fentitynumber |  |  |
| warnpermtype | 控权类型 | TextField | t_hrcs_warndataperm.fwarnpermtype |  |  |
| warndataperm | 控权维度 | BigIntField | t_hrcs_warndataperm.fwarndataperm |  |  |
| datapermname | 控权维度 | MuliLangTextField | t_hrcs_warndataperm.fdatapermname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_warnobjecttpl（主表） | 24 |
| t_hrcs_warndataperm（单据体） | 8 |
| 无数据库列 | 2 |

