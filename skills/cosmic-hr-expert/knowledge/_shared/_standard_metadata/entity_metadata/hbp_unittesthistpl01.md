# hbp_unittesthistpl01 — 全页面时序历史单测

**表单编码**: `hbp_unittesthistpl01`  
**表单ID**: `50R7T9BPJA93`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_unittesthistpl01（全页面时序历史单测） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbp_unittesthistpl01` | BaseEntity | 主表 |
| `t_hbp_thisentry01` | EntryEntity | 单据体 |
| `t_hbp_thisusbentry` | SubEntryEntity | 子单据体 |
| `t_hbp_unittestatt01` | MulEmployeeField子表 | 附件 |

### 字段列表 — t_hbp_unittesthistpl01（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hbp_unittesthistpl01.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hbp_unittesthistpl01.fname |  |  |
| status | 数据状态 | BillStatusField | t_hbp_unittesthistpl01.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hbp_unittesthistpl01.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hbp_unittesthistpl01.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hbp_unittesthistpl01.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hbp_unittesthistpl01.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hbp_unittesthistpl01.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hbp_unittesthistpl01.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hbp_unittesthistpl01.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hbp_unittesthistpl01.fdescription |  |  |
| index | 排序号 | IntegerField | t_hbp_unittesthistpl01.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hbp_unittesthistpl01.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hbp_unittesthistpl01.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hbp_unittesthistpl01.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hbp_unittesthistpl01.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hbp_unittesthistpl01.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hbp_unittesthistpl01.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hbp_unittesthistpl01.foriname |  |  |
| boid | 业务ID | BigIntField | t_hbp_unittesthistpl01.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hbp_unittesthistpl01.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hbp_unittesthistpl01.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hbp_unittesthistpl01.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hbp_unittesthistpl01.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hbp_unittesthistpl01.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hbp_unittesthistpl01.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hbp_unittesthistpl01.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hbp_unittesthistpl01.fhisversion |  |  |
| attachmentfield | 附件 | AttachmentField | t_hbp_unittestatt01（子表） |  |  |
| hisbasedata01 | 版本F7 | HisModelBasedataField | t_hbp_unittesthistpl01.fhisbasedata01 |  | hbp_unittesthistpl01 |
| entryentity | 单据体 | EntryEntity | → t_hbp_thisentry01 |  |  |

### 字段列表 — t_hbp_thisentry01（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entryboid | entryboid | BigIntField | t_hbp_thisentry01.fentryboid |  |  |
| textfield | 文本4 | TextField | t_hbp_thisentry01.ftextfield |  |  |
| subentryentity | 子单据体 | SubEntryEntity | → t_hbp_thisusbentry |  |  |

### 字段列表 — t_hbp_thisusbentry（子单据体·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| integerfield | 整数1 | IntegerField | t_hbp_thisusbentry.fintegerfield |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbp_unittesthistpl01（主表） | 30 |
| t_hbp_thisentry01（单据体） | 2 |
| t_hbp_thisusbentry（子单据体） | 1 |
| t_hbp_unittestatt01（MulEmployeeField子表） | 1 |

