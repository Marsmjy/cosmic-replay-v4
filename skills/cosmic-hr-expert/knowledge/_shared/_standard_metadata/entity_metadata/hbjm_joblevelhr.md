# hbjm_joblevelhr — 职级

**表单编码**: `hbjm_joblevelhr`  
**表单ID**: `/IK45U1VF2XK`  
**归属**: HR基础服务云 / HR基础职位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbjm_joblevelhr（职级） [BaseEntity]

- **数据库表**: `t_hbjm_joblevel`  

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
| createorg | 创建组织 | OrgField | — |  | bos_org |
| org | 管理组织 | OrgField | — |  | bos_org |
| useorg | 使用组织 | OrgField | — |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | — |  |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | — |  | bos_org |
| index | 排序号 | IntegerField | findex |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
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
| joblevelseq | 职级顺序码 | IntegerField | fjoblevelseq | ✓ |  |
| entryboid | boid | BigIntField | fentryboid |  |  |
| iscorjoblevel | 集团职级 | CheckBoxField | fiscorjoblevel |  |  |
| joblevelscm | 职级方案 | MulBasedataField | t_hbjm_joblevelscmmul（子表） | ✓ |  |
| corjoblevel | 集团职级 | BasedataField | fcorjoblevelid |  | hbjm_joblevelhr |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |

