# hrcs_coordrulesch — 协作规则方案

**表单编码**: `hrcs_coordrulesch`  
**表单ID**: `4YYODD+Y7ZHQ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordrulesch（协作规则方案） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_coordrulesch` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity |  |
| `（虚拟分录）` | EntryEntity |  |

### 字段列表 — t_hrcs_coordrulesch（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_coordrulesch.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_coordrulesch.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_coordrulesch.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_coordrulesch.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_coordrulesch.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_coordrulesch.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_coordrulesch.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_coordrulesch.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_coordrulesch.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hrcs_coordrulesch.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_coordrulesch.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_coordrulesch.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_coordrulesch.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_coordrulesch.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_coordrulesch.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_coordrulesch.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_coordrulesch.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_coordrulesch.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_coordrulesch.foriname |  |  |
| coordbizobject | 业务对象 | BasedataField | t_hrcs_coordrulesch.fcoordbizobjectid | ✓ | hrcs_coordbizobject |
| scene | 所属场景 | BasedataField | — |  | brm_scene |
| isusedefvalue | 默认值设置 | CheckBoxField | — |  |  |
| isuserule | 规则设置 | CheckBoxField | — |  |  |
| rulepolicytype | 策略分类 | ComboField | — |  |  |
| filterconditionstr | 条件 | TextField | — |  |  |
| filtercondition | 条件JSON | TextField | — |  |  |
| filterresultstr | 结果 | TextField | — |  |  |
| filterresult | 结果JSON | TextField | — |  |  |
| rulenumber | 规则编码 | TextField | — |  |  |
| rulename | 规则名称 | TextField | — |  |  |
| isuseold | 已有档案时保持原值设置 | CheckBoxField | — |  |  |
| useoldtype | 保持原值 | ComboField | — |  |  |
| useoldpolicytype | 策略分类 | ComboField | — |  |  |
| oldrulenumber | 规则编码 | TextField | — |  |  |
| oldrulename | 规则名称 | TextField | — |  |  |
| oldfilterconditionstr | 条件 | TextField | — |  |  |
| oldfiltercondition | 条件JSON | TextField | — |  |  |
| oldfilterresultstr | 结果 | TextField | — |  |  |
| oldfilterresult | 结果JSON | TextField | — |  |  |
| ruledate | 日期 | DateField | — |  |  |
| defaultresultjson | 默认结果 | TextField | — |  |  |
| tips | 多行文本 | TextAreaField | — |  |  |
| tablehead | 决策表表头 | TextField | — |  |  |
| tablebody | 决策表表体 | TextField | — |  |  |
|  |  | EntryEntity | → （虚拟分录） |  |  |
|  |  | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

### 字段列表 — （·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_coordrulesch（主表） | 52 |
| 无数据库列 | 32 |

