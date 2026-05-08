# hrcs_lblstrategy — 打标策略

**表单编码**: `hrcs_lblstrategy`  
**表单ID**: `2WG/DV8/1EQU`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_lblstrategy（打标策略） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_labelpolicy` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity | 单据体 |
| `t_hrcs_lblstrategyentry` | EntryEntity | 单据体 |

### 字段列表 — t_hrcs_labelpolicy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_labelpolicy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_labelpolicy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_labelpolicy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_labelpolicy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_labelpolicy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_labelpolicy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_labelpolicy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_labelpolicy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_labelpolicy.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_labelpolicy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_labelpolicy.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrcs_labelpolicy.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_labelpolicy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_labelpolicy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_labelpolicy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_labelpolicy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_labelpolicy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_labelpolicy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_labelpolicy.foriname |  |  |
| label | 标签 | BasedataField | t_hrcs_labelpolicy.flabelid | ✓ | hrcs_label |
| labelobject | 打标对象 | BasedataField | t_hrcs_labelpolicy.flabelobjectid | ✓ | hrcs_labelobject |
| worktype | 打标方式 | ComboField | t_hrcs_labelpolicy.fworktype | ✓ |  |
| daterangefield | 策略有效期 | DateRangeField | — | ✓ |  |
| ruledate | 日期 | DateField | — |  |  |
| lasttasktime | 上次执行时间 | DateTimeField | t_hrcs_labelpolicy.flasttasktime |  |  |
| nexttasktime | 下次执行时间 | DateTimeField | t_hrcs_labelpolicy.fnexttasktime |  |  |
| frequency | 打标频率 | TextField | t_hrcs_labelpolicy.ffrequency |  |  |
| lasttasknumber | 上次执行成功流水号 | TextField | t_hrcs_labelpolicy.flasttasknumbernew |  |  |
| brmpolicyid | 规则策略ID | BigIntField | t_hrcs_labelpolicy.fbrmpolicyid |  |  |
| easy | 简单模式 | RadioField | — |  |  |
| configtype | 配置模式 | RadioGroupField | t_hrcs_labelpolicy.fconfigtype |  |  |
| plugin | 插件模式 | RadioField | — |  |  |
| entryentitydisplay | 单据体 | EntryEntity | → （虚拟分录） |  |  |
| entryentity | 单据体 | EntryEntity | → t_hrcs_lblstrategyentry |  |  |

### 字段列表 — （单据体·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — t_hrcs_lblstrategyentry（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| labelvalue | 标签值 | BasedataField | t_hrcs_lblstrategyentry.flabelvalueid |  | hrcs_labelvalue |
| service | 打标规则服务类配置 | TextField | t_hrcs_lblstrategyentry.fservice |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_labelpolicy（主表） | 32 |
| （单据体） | 2 |
| t_hrcs_lblstrategyentry（单据体） | 2 |
| 无数据库列 | 6 |

