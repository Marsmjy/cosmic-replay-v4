# haos_adminorgcompany — 公司信息

**表单编码**: `haos_adminorgcompany`  
**表单ID**: `22003ZY2I4WF`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_adminorgcompany（公司信息） [BaseEntity]

- **数据库表**: `t_haos_company`  

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
| adminorg | 行政组织 | BasedataField | fadminorgid |  | haos_adminorgdetail |
| companyname | 公司名称 | MuliLangTextField | fcompanyname |  |  |
| companytype | 公司类型 | BasedataField | fcompanytypeid |  | haos_companytype |
| industrytype | 行业类别 | BasedataField | findustrytypeid |  | hbss_industrytype |
| isincorporatedcompany | 法人公司 | CheckBoxField | fisincorporatedcompany |  |  |
| registeroffice | 注册机关 | MuliLangTextField | fregisteroffice |  |  |
| uniformsocialcreditcode | 统一社会信用代码 | TextField | funiformsocialcreditcode |  |  |
| legalperson | 法人代表 | MuliLangTextField | flegalperson |  |  |
| contactnumber | 联系电话 | TextField | fcontactphone |  |  |
| registeredcapital | 注册资本 | MuliLangTextField | fregisteredcapital |  |  |
| registeredaddress | 注册地址 | MuliLangTextField | fregisteredaddress |  |  |
| businessscope | 经营范围 | MuliLangTextField | fbusinessscope |  |  |

