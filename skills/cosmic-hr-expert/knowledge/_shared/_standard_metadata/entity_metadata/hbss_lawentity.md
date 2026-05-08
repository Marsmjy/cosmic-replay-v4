# hbss_lawentity — 法律实体

**表单编码**: `hbss_lawentity`  
**表单ID**: `2BHUREU0=RYL`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_lawentity（法律实体） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_lawentity` | BaseEntity | 主表 |
| `t_hbss_lawentityvrinf` | EntryEntity | 单据体 |

### 字段列表 — t_hbss_lawentity（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_lawentity.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_lawentity.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_lawentity.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_lawentity.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbss_lawentity.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_lawentity.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_lawentity.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbss_lawentity.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_lawentity.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbss_lawentity.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_lawentity.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_lawentity.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_lawentity.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_lawentity.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_lawentity.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_lawentity.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_lawentity.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_lawentity.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_lawentity.foriname |  |  |
| effectdate | 变更日期 | DateField | t_hbss_lawentity.feffectdate |  |  |
| type | 变更类型 | MulComboField | t_hbss_lawentity.ftype |  |  |
| desc | 变更摘要说明 | TextField | t_hbss_lawentity.fdesc |  |  |
| enterprise | 用人单位 | CheckBoxField | t_hbss_lawentity.fenterprise |  |  |
| signcompany | 聘用单位 | CheckBoxField | t_hbss_lawentity.fsigncompany |  |  |
| taxunit | 纳税单位 | CheckBoxField | t_hbss_lawentity.ftaxunit |  |  |
| welfarepayer | 参保单位 | CheckBoxField | t_hbss_lawentity.fwelfarepayer |  |  |
| paysubject | 支付主体 | CheckBoxField | t_hbss_lawentity.fpaysubject |  |  |
| adminorg | 关联法人 | OrgField | t_hbss_lawentity.fadminorgid |  | bos_org |
| uniformsocialcreditcode | 统一社会信用代码 | TextField | t_hbss_lawentity.funiformsocialcreditcode |  |  |
| firmname | 公司名称 | TextField | t_hbss_lawentity.ffirmname |  |  |
| representative | 法定代表人 | TextField | t_hbss_lawentity.frepresentative |  |  |
| establishmentdate | 成立日期 | DateField | t_hbss_lawentity.festablishmentdate |  |  |
| businessterm | 营业期限 | DateRangeField | t_hbss_lawentity.fbusnissterm1 |  |  |
| phone | 联系电话 | TextField | t_hbss_lawentity.fphone |  |  |
| address | 住所 | TextField | t_hbss_lawentity.faddress |  |  |
| entitytype | 实体类型 | ComboField | t_hbss_lawentity.fentitytype | ✓ |  |
| propctl | 属性控制 | MulComboField | t_hbss_lawentity.fpropctl |  |  |
| oprsts | 经营状态 | ComboField | t_hbss_lawentity.foprsts |  |  |
| syncaddsigncomp | 同步新增聘用单位 | CheckBoxField | t_hbss_lawentity.fsyncaddsigncomp |  |  |
| entryentity | 单据体 | EntryEntity | → t_hbss_lawentityvrinf |  |  |

### 字段列表 — t_hbss_lawentityvrinf（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| vernum | 版本编号 | TextField | t_hbss_lawentityvrinf.fvernum |  |  |
| chgeffectdate | 变更日期 | DateField | t_hbss_lawentityvrinf.fchgeffectdate |  |  |
| chgtype | 变更类型 | MulComboField | t_hbss_lawentityvrinf.fchgtype |  |  |
| chgdesc | 变更摘要说明 | TextField | t_hbss_lawentityvrinf.fchgdesc |  |  |
| chgentitytype | 实体类型 | ComboField | t_hbss_lawentityvrinf.fentitytype |  |  |
| chgpropctl | 属性控制 | MulComboField | t_hbss_lawentityvrinf.fpropctl |  |  |
| chgcreator1 | 变更创建人 | CreaterField | t_hbss_lawentityvrinf.fchgcreatorid |  | bos_user |
| chgcreatetime1 | 变更创建时间 | CreateDateField | t_hbss_lawentityvrinf.fchgcreatetime |  |  |
| chgindex | 排序号 | IntegerField | t_hbss_lawentityvrinf.findex |  |  |
| chgcreator | 创建人 | CreaterField | t_hbss_lawentityvrinf.fcreatorid |  | bos_user |
| chgcreatetime | 创建时间 | CreateDateField | t_hbss_lawentityvrinf.fcreatetime |  |  |
| chgmodifier | 修改人 | ModifierField | t_hbss_lawentityvrinf.fmodifierid |  | bos_user |
| chgmodifytime | 修改时间 | ModifyDateField | t_hbss_lawentityvrinf.fmodifytime |  |  |
| chgdisabler | 禁用人 | UserField | t_hbss_lawentityvrinf.fdisablerid |  | bos_user |
| chgdisabledate | 禁用时间 | DateTimeField | t_hbss_lawentityvrinf.fdisabledate |  |  |
| chgstatus | 数据状态 | BillStatusField | t_hbss_lawentityvrinf.fstatus |  |  |
| chgenable | 使用状态 | BillStatusField | t_hbss_lawentityvrinf.fenable |  |  |
| chgissyspreset | 是否系统预置 | CheckBoxField | t_hbss_lawentityvrinf.fissyspreset |  |  |
| chgmasterid | 主数据内码 | MasterIdField | t_hbss_lawentityvrinf.fmasterid |  |  |
| chgnumber | 编码 | TextField | t_hbss_lawentityvrinf.fnumber |  |  |
| chgcreateorg | 创建组织 | OrgField | t_hbss_lawentityvrinf.fcreateorgid |  | bos_org |
| chgctrlstrategy | 控制策略 | ComboField | t_hbss_lawentityvrinf.fctrlstrategy |  |  |
| chgname | 名称 | MuliLangTextField | t_hbss_lawentityvrinf.fname |  |  |
| chgsimplename | 简称 | MuliLangTextField | t_hbss_lawentityvrinf.fsimplename |  |  |
| chgdescription | 描述 | MuliLangTextField | t_hbss_lawentityvrinf.fdescription |  |  |
| chgoprsts | 经营状态 | ComboField | t_hbss_lawentityvrinf.foprsts |  |  |
| chgenterprise | 用人单位 | CheckBoxField | t_hbss_lawentityvrinf.fenterprise |  |  |
| chgsigncompany | 聘用单位 | CheckBoxField | t_hbss_lawentityvrinf.fsigncompany |  |  |
| chgtaxunit | 纳税单位 | CheckBoxField | t_hbss_lawentityvrinf.ftaxunit |  |  |
| chgwelfarepayer | 参保单位 | CheckBoxField | t_hbss_lawentityvrinf.fwelfarepayer |  |  |
| chgpaysubject | 支付主体 | CheckBoxField | t_hbss_lawentityvrinf.fpaysubject |  |  |
| chgadminorg | 关联法人 | OrgField | t_hbss_lawentityvrinf.fadminorgid |  | bos_org |
| chguniformsocialcreditcod | 统一社会信用代码 | TextField | t_hbss_lawentityvrinf.funiformsocialcreditcode |  |  |
| chgfirmname | 公司名称 | TextField | t_hbss_lawentityvrinf.ffirmname |  |  |
| chgrepresentative | 法定代表人 | TextField | t_hbss_lawentityvrinf.frepresentative |  |  |
| chgestablishmentdate | 成立日期 | DateField | t_hbss_lawentityvrinf.festablishmentdate |  |  |
| chgbusinessterm | 营业期限 | DateRangeField | t_hbss_lawentityvrinf.fchgbusinessterm |  |  |
| chgphone | 联系电话 | TextField | t_hbss_lawentityvrinf.fphone |  |  |
| chgaddress | 住所 | TextField | t_hbss_lawentityvrinf.faddress |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_lawentity（主表） | 47 |
| t_hbss_lawentityvrinf（单据体） | 39 |
| 无数据库列 | 8 |

