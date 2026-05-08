# hbss_contracttypes — 合同类型

**表单编码**: `hbss_contracttypes`  
**表单ID**: `25L2WU/HHPLT`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_contracttypes（合同类型） [BaseEntity]

- **数据库表**: `t_hbss_contracttypes`  

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
| group | 合同大类 | GroupField | — |  | hbss_contracttypecat |
| org | 管理组织 | OrgField | forgid |  | bos_org |
| createorg | 创建组织 | OrgField | fcreateorgid | ✓ | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | fctrlstrategy |  |  |
| sourcedata | 原资料id | BigIntField | fsourcedataid |  |  |
| bitindex | 位图 | IntegerField | fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | fsourcebitindex |  |  |
| srccreateorg | 原创建组织 | OrgField | fsrccreateorgid |  | bos_org |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |

