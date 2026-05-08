# hrpi_perpractqual — 执业资格

**表单编码**: `hrpi_perpractqual`  
**表单ID**: `15BJUD1W351B`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perpractqual（执业资格） [BaseEntity]

- **数据库表**: `t_hrpi_perpractqual`  

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
| qualification | 执业资格 | BasedataField | fqualificationid | ✓ | hbss_operationqual |
| qualevel | 执业资格等级 | BasedataField | fqualevelid |  | hbss_ocpquallevel |
| gettime | 获取日期 | DateField | fgettime |  |  |
| registratedate | 聘任或注册日期 | DateField | fregistratedate |  |  |
| ismajor | 主要执业资格 | CheckBoxField | fismajor |  |  |
| grantunit | 授予单位 | MuliLangTextField | fgrantunit |  |  |
| registrationunit | 聘任或注册单位 | MuliLangTextField | fregistrationunit |  |  |
| certiicateid | 证书编号 | MuliLangTextField | fcertiicateid |  |  |
| issuednation | 颁发国家/地区 | BasedataField | fissuednationid |  | bd_country |
| enddate | 聘任终止日期 | DateField | fenddate |  |  |

