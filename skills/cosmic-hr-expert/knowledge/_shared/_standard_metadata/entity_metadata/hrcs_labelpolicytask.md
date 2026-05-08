# hrcs_labelpolicytask — 打标策略执行表

**表单编码**: `hrcs_labelpolicytask`  
**表单ID**: `2Y14/H41TF82`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_labelpolicytask（打标策略执行表） [BaseEntity]

- **数据库表**: `t_hrcs_labelpolicytask`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 执行流水号 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 状态 | BillStatusField | fenable |  |  |
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
| labelpolicy | 打标策略 | BasedataField | flabelpolicyid |  | hrcs_lblstrategy |
| taskmessage | 执行描述 | TextField | ftaskmessage |  |  |
| taskstatus | 执行状态 | ComboField | ftaskstatus |  |  |
| priority | 优先级 | IntegerField | fpriority |  |  |
| totalquantity | 实例总数 | IntegerField | ftotalquantity |  |  |
| processedquantity | 已执行数量 | IntegerField | fprocessedquantity |  |  |
| matchedquantity | 符合规则数量 | IntegerField | fmatchedquantity |  |  |
| esstatus | ES存储状态 | IntegerField | fesstatus |  |  |

