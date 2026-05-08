# hrptmc_unitestentity01 — 报表单元测试实体01

**表单编码**: `hrptmc_unitestentity01`  
**表单ID**: `3=NFH7CPV2SX`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_unitestentity01（报表单元测试实体01） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrptmc_unitestentity01` | BaseEntity | 主表 |
| `t_hrptmc_unitentry01` | EntryEntity | 单据体 |

### 字段列表 — t_hrptmc_unitestentity01（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrptmc_unitestentity01.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrptmc_unitestentity01.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrptmc_unitestentity01.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrptmc_unitestentity01.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrptmc_unitestentity01.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrptmc_unitestentity01.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrptmc_unitestentity01.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrptmc_unitestentity01.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrptmc_unitestentity01.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrptmc_unitestentity01.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrptmc_unitestentity01.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrptmc_unitestentity01.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrptmc_unitestentity01.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrptmc_unitestentity01.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrptmc_unitestentity01.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrptmc_unitestentity01.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrptmc_unitestentity01.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrptmc_unitestentity01.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrptmc_unitestentity01.foriname |  |  |
| integerfield | 整数 | IntegerField | t_hrptmc_unitestentity01.fintegerfield |  |  |
| decimalfield | 小数 | DecimalField | t_hrptmc_unitestentity01.fdecimalfield |  |  |
| bigintfield | 长整数 | BigIntField | t_hrptmc_unitestentity01.fbigintfield |  |  |
| datefield | 日期 | DateField | t_hrptmc_unitestentity01.fdatefield |  |  |
| checkboxfield | 复选框 | CheckBoxField | t_hrptmc_unitestentity01.fcheckboxfield |  |  |
| combofield | 下拉列表 | ComboField | t_hrptmc_unitestentity01.fcombofield |  |  |
| mulcombofield | 多选下拉列表 | MulComboField | t_hrptmc_unitestentity01.fmulcombofield |  |  |
| basedatafield | 基础资料 | BasedataField | t_hrptmc_unitestentity01.fbasedatafield |  | hbss_diploma |
| adminorg | 行政组织 | HRAdminOrgField | t_hrptmc_unitestentity01.fadminorg |  | haos_adminorghrf7 |
| hisbasedata | 时序历史基础资料 | HisModelBasedataField | t_hrptmc_unitestentity01.fhisbasedata |  | hbss_college |
| amountfield | 金额 | AmountField | t_hrptmc_unitestentity01.famountfield |  |  |
| currencyfield | 币别 | CurrencyField | t_hrptmc_unitestentity01.fcurrencyfield |  | bd_currency |
| entryentity | 单据体 | EntryEntity | → t_hrptmc_unitentry01 |  |  |

### 字段列表 — t_hrptmc_unitentry01（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| textfield | 分录文本 | TextField | t_hrptmc_unitentry01.ftextfield |  |  |
| entryint | 分录整数 | IntegerField | t_hrptmc_unitentry01.fentryint |  |  |
| entrydate | 分录日期 | DateField | t_hrptmc_unitentry01.fentrydate |  |  |
| entrybasedata | 分录基础资料 | BasedataField | t_hrptmc_unitentry01.fentrybasedata |  | hbss_cloud_app |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrptmc_unitestentity01（主表） | 31 |
| t_hrptmc_unitentry01（单据体） | 4 |

