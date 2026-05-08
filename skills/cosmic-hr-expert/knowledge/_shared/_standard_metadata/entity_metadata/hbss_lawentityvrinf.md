# hbss_lawentityvrinf — 法律实体版本详情

**表单编码**: `hbss_lawentityvrinf`  
**表单ID**: `2BZWWI09CV/X`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_lawentityvrinf（法律实体版本详情） [BaseEntity]

- **数据库表**: `t_hbss_lawentityvrinf`  

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
| enterprise | 用人单位 | CheckBoxField | fenterprise |  |  |
| signcompany | 聘用单位 | CheckBoxField | fsigncompany |  |  |
| taxunit | 纳税单位 | CheckBoxField | ftaxunit |  |  |
| welfarepayer | 参保单位 | CheckBoxField | fwelfarepayer |  |  |
| paysubject | 支付主体 | CheckBoxField | fpaysubject |  |  |
| uniformsocialcreditcode | 统一社会信用代码 | TextField | funiformsocialcreditcode |  |  |
| firmname | 公司名称 | TextField | ffirmname |  |  |
| representative | 法定代表人 | TextField | frepresentative |  |  |
| establishmentdate | 成立日期 | DateField | festablishmentdate |  |  |
| busnissterm | 营业期限 | DateRangeField | fbusnissterm1 |  |  |
| phone | 联系电话 | TextField | fphone |  |  |
| address | 住所 | TextAreaField | faddress |  |  |
| adminorg | 关联法人 | OrgField | fadminorgid |  | bos_org |
| entitytype | 实体类型 | ComboField | fentitytype | ✓ |  |
| propctl | 属性控制 | MulComboField | fpropctl |  |  |
| oprsts | 经营状态 | ComboField | foprsts |  |  |
| vernum | 版本号 | TextField | fvernum |  |  |
| createorg | 创建组织 | OrgField | fcreateorgid | ✓ | bos_org |
| ctrlstrategy | 控制策略 | ComboField | fctrlstrategy | ✓ |  |
| syncaddsigncomp | 同步新增聘用单位 | CheckBoxField | fsyncaddsigncomp |  |  |

