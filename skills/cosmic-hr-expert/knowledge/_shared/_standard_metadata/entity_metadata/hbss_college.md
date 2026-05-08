# hbss_college — 高等院校

**表单编码**: `hbss_college`  
**表单ID**: `08B2NE63B//H`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_college（高等院校） [BaseEntity]

- **数据库表**: `t_hbss_college`  

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
| collegecharactlbl | 院校特性 | TextField | — |  |  |
| identicode | 学校标识码 | TextField | fidenticode |  |  |
| locationcity | 所在地 | CityField | flocationcityid |  | bd_admindivision |
| authedegree | 学位授予国家（地区） | BasedataField | fauthedegreeid | ✓ | bd_country |
| edulevel | 办学层次 | ComboField | fedulevel |  |  |
| collegecharact | 院校特性 | MulBasedataField | t_hbss_collegcateg（子表） |  |  |
| runnature | 办学性质 | ComboField | frunnature |  |  |

