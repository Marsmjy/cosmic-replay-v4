# hrcs_coordstrategy — 协作处理策略

**表单编码**: `hrcs_coordstrategy`  
**表单ID**: `4Z0LH=7A/6IH`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_coordstrategy（协作处理策略） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrcs_coordstrategy` | BaseEntity | 主表 |
| `（虚拟分录）` | EntryEntity |  |

### 字段列表 — t_hrcs_coordstrategy（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrcs_coordstrategy.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_hrcs_coordstrategy.fname |  |  |
| status | 数据状态 | BillStatusField | t_hrcs_coordstrategy.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrcs_coordstrategy.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrcs_coordstrategy.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrcs_coordstrategy.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrcs_coordstrategy.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrcs_coordstrategy.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrcs_coordstrategy.fmasterid |  |  |
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | t_hrcs_coordstrategy.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hrcs_coordstrategy.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrcs_coordstrategy.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrcs_coordstrategy.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrcs_coordstrategy.FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrcs_coordstrategy.FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | t_hrcs_coordstrategy.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_hrcs_coordstrategy.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrcs_coordstrategy.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrcs_coordstrategy.foriname |  |  |
| coordbizobject | 业务对象 | BasedataField | t_hrcs_coordstrategy.fcoordbizobjectid | ✓ | hrcs_coordbizobject |
| policy | 处理策略（隐藏） | BasedataField | t_hrcs_coordstrategy.fpolicyid |  | brm_policy_edit |
| isuserule | 策略规则 | CheckBoxField | t_hrcs_coordstrategy.fisuserule |  |  |
| rulepolicytype | 策略分类 | ComboField | t_hrcs_coordstrategy.fpolicytype |  |  |
| rulenumber | 规则编码 | TextField | — |  |  |
| rulename | 规则名称 | TextField | — |  |  |
| filterconditionstr | 条件 | TextField | — |  |  |
| filtercondition | 条件JSON | TextField | — |  |  |
| filterresultstr | 结果 | TextField | — |  |  |
| filterresult | 结果JSON | TextField | — |  |  |
| type | 协作处理策略 | ComboField | t_hrcs_coordstrategy.ftype |  |  |
| currentlog | 当前日志 | BasedataField | t_hrcs_coordstrategy.fcurrentlogid |  | hrcs_coordstrategylog |
| scene | 所属场景 | BasedataField | — |  | brm_scene |
| tablehead | 决策表表头 | TextField | — |  |  |
| tablebody | 决策表表体 | TextField | — |  |  |
| ruledate | 日期 | DateField | — |  |  |
| isusedefault | 默认值 | CheckBoxField | — |  |  |
|  |  | EntryEntity | → （虚拟分录） |  |  |

### 字段列表 — （·虚拟分录）

> 此分录为虚拟分录，无独立物理表，无专属字段。

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrcs_coordstrategy（主表） | 44 |
| 无数据库列 | 19 |

