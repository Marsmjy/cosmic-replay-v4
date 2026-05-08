# hrpi_employeetaxcn — 员工个税信息

**表单编码**: `hrpi_employeetaxcn`  
**表单ID**: `5GHMP8P67X0P`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_employeetaxcn（员工个税信息） [BaseEntity]

- **数据库表**: `t_hrpi_employeetaxcn`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 工号 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| assignment | 组织分配号 | EmployeeField | fassignmentid | ✓ | hrpi_assignmentf7query |
| adminorg | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| taxunit | 纳税单位 | BasedataField | ftaxunitid | ✓ | hbss_taxunit |
| taxpayertype | 纳税人类型 | ComboField | ftaxpayertype | ✓ |  |
| emptype | 任职受雇从业类型 | ComboField | femptype | ✓ |  |
| empdate | 任职受雇日期 | DateField | fempdate |  |  |
| empsituation | 入职年度就业情形 | ComboField | fempsituation |  |  |
| taxfree | 上一年度完整纳税且收入小于6万 | ComboField | ftaxfree |  |  |
| isdeductexps | 是否扣除减除费用 | CheckBoxField | fisdeductexps |  |  |
| taxstatus | 个税状态 | ComboField | ftaxstatus | ✓ |  |
| quitdate | 离职日期 | DateField | fquitdate |  |  |
| disability | 是否残疾 | ComboField | fdisability |  |  |
| disabilitynum | 残疾证号 | TextField | fdisabilitynum |  |  |
| martyrsfamily | 是否烈属 | ComboField | fmartyrsfamily |  |  |
| martyrsfamilynum | 烈属证号 | TextField | fmartyrsfamilynum |  |  |
| oldandloney | 是否孤老 | ComboField | foldandloney |  |  |
| phone | 手机号码 | TextField | fphone |  |  |
| investtotal | 个人投资总额 | DecimalField | finvesttotal |  |  |
| investratio | 个人投资比例（%） | DecimalField | finvestratio |  |  |
| duty | 职务 | ComboField | fduty |  |  |
| chinesename | 中文名 | TextField | fchinesename |  |  |
| taxreason | 涉税事由 | ComboField | ftaxreason |  |  |
| otheridtype | 其他证件类型 | ComboField | fotheridtype |  |  |
| otheridnumber | 其他证件号码 | TextField | fotheridnumber |  |  |
| birthplace | 出生地（税局） | BasedataField | fbirthplaceid |  | hbss_nationality |
| firstentrydate | 首次入境日期 | DateField | ffirstentrydate |  |  |
| departdate | 预计离境日期 | DateField | fdepartdate |  |  |
| name | 姓名 | QueryPropField | fname |  |  |
| credentialstypename | 证件类型 | TextField | — |  |  |
| percrenumber | 证件号码 | TextField | — |  |  |
| nationality | 国籍（地区） | QueryPropField | — |  |  |
| birthday | 出生日期 | QueryPropField | — |  |  |
| gender | 性别 | QueryPropField | — |  |  |
| marriagestatus | 婚姻状况 | QueryPropField | — |  |  |
| education | 学历 | BasedataField | — |  | hbss_diploma |
| taxpayernum | 纳税人识别号 | TextField | ftaxpayernum |  |  |
| credentialstype | 证件类型 | BasedataField | — |  | hbss_credentialstype |
| regpermres | 户籍所在地（省） | BasedataField | fregpermresid |  | bd_admindivision |
| regpermrescity | 户籍所在地（市） | BasedataField | fregpermrescityid |  | bd_admindivision |
| regpermrescounty | 户籍所在地（区县） | BasedataField | fregpermrescountyid |  | bd_admindivision |
| habitres | 经常居住地（省） | BasedataField | fhabitresid |  | bd_admindivision |
| habitrescity | 经常居住地（市） | BasedataField | fhabitrescityid |  | bd_admindivision |
| habitrescounty | 经常居住地（区县） | BasedataField | fhabitrescountyid |  | bd_admindivision |
| address | 联系地址（省） | BasedataField | faddressid |  | bd_admindivision |
| addresscity | 联系地址（市） | BasedataField | faddresscityid |  | bd_admindivision |
| addresscounty | 联系地址（区县） | BasedataField | faddresscountyid |  | bd_admindivision |
| email | 电子邮箱 | EmailField | femail |  |  |
| regpermresinfo | 户籍所在地（详细地址） | TextField | fregpermresinfo |  |  |
| habitresinfo | 经常居住地（详细地址） | TextField | fhabitresinfo |  |  |
| addressinfo | 联系地址（详细地址） | TextField | faddressinfo |  |  |
| remark | 备注 | TextField | fremark |  |  |
| otherdesc | 其他情况说明 | ComboField | — |  |  |
| isseniorexpert | 是否高级专家 | ComboField | — |  |  |
| disabilitycerttype | 残疾证件类型 | ComboField | — |  |  |

