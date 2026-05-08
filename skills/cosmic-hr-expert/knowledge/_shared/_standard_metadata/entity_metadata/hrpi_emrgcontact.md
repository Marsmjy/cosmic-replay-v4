# hrpi_emrgcontact — 紧急联系人

**表单编码**: `hrpi_emrgcontact`  
**表单ID**: `15UTU8D62D1W`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_emrgcontact（紧急联系人） [BaseEntity]

- **数据库表**: `t_hrpi_emrgcontact`  

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
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| emrgname | 紧急联系人姓名 | TextField | femrgname |  |  |
| emergcontactype | 紧急联系人类型 | BasedataField | femergcontactypeid |  | hbss_emergcontactype |
| emrgphone | 紧急联系人联系电话 | TelephoneField | femrgphone |  |  |
| emrgaddress | 联系地址 | TextField | femrgaddress |  |  |

