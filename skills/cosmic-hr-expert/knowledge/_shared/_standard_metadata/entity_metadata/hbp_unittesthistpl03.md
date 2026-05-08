# hbp_unittesthistpl03 — 全页面时序单测（需审核）

**表单编码**: `hbp_unittesthistpl03`  
**表单ID**: `50S=BBWQ=RB0`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_unittesthistpl03（全页面时序单测（需审核）） [BaseEntity]

- **数据库表**: `t_hbp_unittesthistpl03`  

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
| boid | 业务ID | BigIntField | fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | ffirstbsed |  |  |
| bsed | 生效日期 | DateField | fbsed |  |  |
| bsled | 失效日期 | DateField | fbsled |  |  |
| changedescription | 变更说明 | TextField | fchangedescription |  |  |
| hisversion | 版本号 | TextField | fhisversion |  |  |
| bof7 | boF7 | HisModelBasedataField | fbof7 |  | hbp_unittesthistpl01 |
| versionf7 | versionF7 | HisModelBasedataField | fversionf7 |  | hbp_unittesthistpl01 |

