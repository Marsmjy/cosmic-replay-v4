# hbp_calresultitem — 计算公式结果参数

**表单编码**: `hbp_calresultitem`  
**表单ID**: `2A=EOZUCB126`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_calresultitem（计算公式结果参数） [BaseEntity]

- **数据库表**: `t_hbp_calresultitem`  

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
| itemcategory | 项目分类 | TextField | fitemcategory |  |  |
| uniquecode | 唯一编码 | TextField | funiquecode |  |  |
| datatype | 数据类型 | TextField | fdatatype |  |  |
| datalength | 数据长度 | IntegerField | fdatalength |  |  |
| scale | 数据精度 | IntegerField | fscale |  |  |
| issubsectionitem | 是否分段项目 | ComboField | fissubsectionitem |  |  |
| entitynumber | 实体编码 | TextField | fentitynumber |  |  |
| propertyfield | 实体字段 | TextField | fpropertyfield |  |  |

