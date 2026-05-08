# hrpi_perocpqual — 职业资格

**表单编码**: `hrpi_perocpqual`  
**表单ID**: `15BJUKF95J5Q`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perocpqual（职业资格） [BaseEntity]

- **数据库表**: `t_hrpi_perocpqual`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| qualification | 职业资格 | BasedataField | fqualificationid |  | hbss_ocpqual |
| qualevel | 职业资格等级 | BasedataField | fqualevelid |  | hbss_ocpquallevel |
| gettime | 证书获得日期 | DateField | fgettime |  |  |
| registratedate | 聘任或注册日期 | DateField | fregistratedate |  |  |
| ismajor | 主要职业资格 | CheckBoxField | fismajor |  |  |
| name | 证书名称 | MuliLangTextField | fname |  |  |
| issuednation | 颁发国家/地区 | BasedataField | fissuednationid |  | bd_country |
| enddate | 聘任终止日期 | DateField | fenddate |  |  |
| registrationunit | 聘任或注册单位 | MuliLangTextField | fregistrationunit |  |  |
| isnofixedterm | 长期有效 | CheckBoxField | fisnofixedterm |  |  |
| expirationdate | 证书有效期 | DateField | fexpirationdate |  |  |
| certiicateid | 证书编号 | TextField | fcertiicateid |  |  |
| grantunit | 发证机构 | TextField | fgrantunit |  |  |

