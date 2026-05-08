# hbss_signcompany — 聘用单位

**表单编码**: `hbss_signcompany`  
**表单ID**: `26UOO/JKDR7R`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_signcompany（聘用单位） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbss_signcompany` | BaseEntity | 主表 |
| `t_hbss_signcompanyentity` | EntryEntity | 单据体 |

### 字段列表 — t_hbss_signcompany（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbss_signcompany.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbss_signcompany.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbss_signcompany.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbss_signcompany.fcreatorid |  | bos_user |
| modifier | 变更人 | ModifierField | t_hbss_signcompany.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbss_signcompany.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbss_signcompany.fcreatetime |  |  |
| modifytime | 变更日期 | ModifyDateField | t_hbss_signcompany.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbss_signcompany.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hbss_signcompany.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hbss_signcompany.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbss_signcompany.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbss_signcompany.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbss_signcompany.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbss_signcompany.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbss_signcompany.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbss_signcompany.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbss_signcompany.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbss_signcompany.foriname |  |  |
| lawentity | 法律实体 | BasedataField | t_hbss_signcompany.fcorporateorgid |  | hbss_lawentity |
| changetype | 变更类型 | ComboField | — |  |  |
| changereason | 变更原因 | MuliLangTextField | — |  |  |
| ischangecon | 是否需要改签合同 | CheckBoxField | — |  |  |
| vid | 历史版本id | BigIntField | t_hbss_signcompany.fvid |  |  |
| representative | 法定代表人/主要负责人 | TextField | t_hbss_signcompany.flegalrepresent | ✓ |  |
| unifiedcode | 统一社会信用代码 | TextField | t_hbss_signcompany.funifiedcode |  |  |
| address | 地址 | MuliLangTextField | t_hbss_signcompany.faddress |  |  |
| postalcode | 邮编 | TextField | t_hbss_signcompany.fpostalcode |  |  |
| contactnumber | 联系电话 | TextField | t_hbss_signcompany.fcontactnumber |  |  |
| reorg | 关联法人 | BasedataField | t_hbss_signcompany.freorgid |  | hbss_lawentity |
| busperiod | 营业期限 | DateRangeField | — |  |  |
| entryentity | 单据体 | EntryEntity | → t_hbss_signcompanyentity |  |  |

### 字段列表 — t_hbss_signcompanyentity（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| chchangetype | 变更类型 | ComboField | t_hbss_signcompanyentity.fchangetype |  |  |
| changedate | 变更日期 | DateField | t_hbss_signcompanyentity.fchangedate |  |  |
| hisid | 历史变更主键ID | BigIntField | t_hbss_signcompanyentity.fhisid |  |  |
| chchangereason | 变更原因 | TextField | t_hbss_signcompanyentity.fchangereason |  |  |
| changeperson | 变更人 | UserField | t_hbss_signcompanyentity.fchangepersonid |  | bos_user |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbss_signcompany（主表） | 39 |
| t_hbss_signcompanyentity（单据体） | 5 |
| 无数据库列 | 12 |

