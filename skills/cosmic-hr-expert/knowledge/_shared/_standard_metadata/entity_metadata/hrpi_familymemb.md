# hrpi_familymemb — 家庭成员

**表单编码**: `hrpi_familymemb`  
**表单ID**: `160IL+K0WSQ4`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_familymemb（家庭成员） [BaseEntity]

- **数据库表**: `t_hrpi_familymemb`  

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
| familymembship | 家庭成员关系 | BasedataField | ffamilymembshipid |  | hbss_familymemberrel |
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| workunit | 工作单位 | TextField | fworkunit |  |  |
| name | 家庭成员姓名 | TextField | fname |  |  |
| politicalstatus | 政治面貌 | BasedataField | fpoliticalstatusid |  | hbss_politicalstatus |
| age | 年龄 | IntegerField | fage |  |  |
| birthday | 出生日期 | DateField | fbirthday |  |  |
| certnumber | 身份证号 | TextField | fcertnumber |  |  |
| job | 职务 | MuliLangTextField | fjob |  |  |
| mobilephone | 家庭成员联系电话 | TelephoneField | fmobilephone |  |  |
| contactaddr | 联系地址 | TextField | fcontactaddr |  |  |

