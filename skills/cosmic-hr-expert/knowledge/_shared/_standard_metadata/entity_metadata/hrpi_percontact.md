# hrpi_percontact — 联系方式

**表单编码**: `hrpi_percontact`  
**表单ID**: `15BJSAYI2C94`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_percontact（联系方式） [BaseEntity]

- **数据库表**: `t_hrpi_percontact`  

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
| workphone | 办公电话 | TextField | fworkphone |  |  |
| wechat | 微信号 | TextField | fwechat |  |  |
| qq | QQ号 | TextField | fqq |  |  |
| blog | 微博 | TextField | fblog |  |  |
| facebook | Facebook | TextField | ffacebook |  |  |
| whatsapp | WhatsApp | TextField | fwhatsapp |  |  |
| skype | Skype | TextField | fskype |  |  |
| linkin | Linkedin | TextField | flinkin |  |  |
| twitter | X | TextField | ftwitter |  |  |
| google | Google | TextField | fgoogle |  |  |
| yunzhijia | 云之家 | TextField | fyunzhijia |  |  |
| fax | 传真 | TextField | ffax |  |  |
| sysname | 系统用户名 | MuliLangTextField | fsysname |  |  |
| telephone | 家庭电话 | TextField | ftelephone |  |  |
| postcode | 邮政编码 | TextField | fpostcode |  |  |
| phone | 手机号码 | TelephoneField | fphone |  |  |
| otherphone | 其他手机号码 | TelephoneField | fotherphone |  |  |
| peremail | 个人电子邮箱 | EmailField | fperemail |  |  |
| busemail | 公司电子邮箱 | EmailField | fbusemail |  |  |

