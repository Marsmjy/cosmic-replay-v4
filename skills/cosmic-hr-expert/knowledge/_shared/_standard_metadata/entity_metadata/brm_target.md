# brm_target — 指标

**表单编码**: `brm_target`  
**表单ID**: `1IMW8=N1XBOF`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: brm_target（指标） [BaseEntity]

- **数据库表**: `t_brm_target`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 指标编码 | TextField | fnumber |  |  |
| name | 指标名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 指标描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| bu | 创建组织 | OrgField | fbuid | ✓ | bos_org |
| bizapp | 所属应用 | BasedataField | fbizappid | ✓ | hbp_devportal_bizapp |
| scene | 所属场景 | BasedataField | fsceneid | ✓ | brm_scene |
| returntype | 返回类型 | ComboField | freturntype | ✓ |  |
| resultdate | 日期（勿删） | DateField | — |  |  |
| elsedate | 日期（勿删） | DateField | — |  |  |
| funcdescription | 1.函数描述： | TextAreaField | — |  |  |
| format | 2.函数格式： | TextAreaField | — |  |  |
| param | 3.函数参数： | TextAreaField | — |  |  |
| example | 4.举例： | TextAreaField | — |  |  |
| applicationscope | 适用范围 | ComboField | fapplicationscope |  |  |
| ruledate | 用来触发日期操作的控件（勿删） | DateField | — |  |  |
| returnparamfield | 对象字段 | ComboField | freturnparamfield |  |  |
| targettypegroup | 指标类型 | ComboField | ftargettype | ✓ |  |
| targetenable | 使用状态 | ComboField | — |  |  |
| templatenumber | 模板编码 | TextField | ftemplatenumber |  |  |
| displayfunctiontext | 表达式 | TextAreaField | fdisplayfunctext |  |  |
| conditions | 条件 | TextField | fconditions |  |  |
| results | 结果 | TextField | fresults |  |  |
| elses | 否则 | TextField | felses |  |  |
| dateformat | 掩码 | ComboField | fdateformat |  |  |

