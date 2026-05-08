# hrcs_coordbizfield — 业务协作字段

**表单编码**: `hrcs_coordbizfield`  
**表单ID**: `4YF3E/MNA9NU`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordbizfield（业务协作字段） [BaseEntity]

- **数据库表**: `t_hrcs_coordbizfield`  

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
| group | 字段分组 | GroupField | — |  | hrcs_coordbizfieldgrp |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| fieldtype | 字段类型 | ComboField | ffieldtype |  |  |
| ismustinput | 是否必录 | CheckBoxField | fismustinput |  |  |
| iskeepold | 支持保持旧值 | CheckBoxField | fiskeepold |  |  |
| mustinputgroup | 必填分组 | TextField | fmustinputgroup |  |  |
| targets | 可选指标 | MulBasedataField | t_hrcs_coordbizfieldtars（子表） |  |  |
| tips | 规则配置说明 | MuliLangTextField | ftips |  |  |
| isfieldgroup | 是否字段组 | CheckBoxField | fisfieldgroup |  |  |
| realfields | 具体字段编码 | TextField | frealfields |  |  |
| fieldalias | 字段别名 | TextField | ffieldalias |  |  |

