# hrcs_commonvariable — 常用变量

**表单编码**: `hrcs_commonvariable`  
**表单ID**: `3MUMB2C=ZL6C`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_commonvariable（常用变量） [BaseEntity]

- **数据库表**: `t_hrcs_commonvariable`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| getvalueway | 单选按钮组 | RadioGroupField | fgetvalueway |  |  |
| radiofield | 取业务对象属性 | RadioField | — |  |  |
| radiofield1 | 自定义 | RadioField | — |  |  |
| filtercondition | 过滤条件 | TextField | ffiltercondition |  |  |
| relateentityid | 关联实体ID | BigIntField | frelateentityid |  |  |
| entityfield | 实体字段标识 | TextField | fentityfield |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| entityproperty | 业务对象属性 | TextField | fentityproperty |  |  |
| multilang | 多语言字段标识 | TextField | fmultilang |  |  |
| custkey | 映射Key | TextField | fcustkey |  |  |
| mainentity | 主实体编码 | TextField | fmainentity |  |  |
| variableupgradflag | 常用变量数据升级标识 | TextField | fvariableupgradflag |  |  |
| ruletext | 取值规则 | TextField | fruletext |  |  |
| varmappingscene | 所属场景 | BasedataField | fvarmappingsceneid |  | hrcs_varmappingscene |

