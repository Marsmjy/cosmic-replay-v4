# hcf_cancontactinfo — 拟入职人员联系方式

**表单编码**: `hcf_cancontactinfo`  
**表单ID**: `15Y4W1S1FGGU`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_cancontactinfo（拟入职人员联系方式） [BaseEntity]

- **数据库表**: `t_hcf_cancontactinfo`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 拟入职人员 | BasedataField | fcandidateid |  | hcf_candidate |
| phone | 手机号码 | TelephoneField | fphone |  |  |
| peremail | 个人电子邮箱 | EmailField | fperemail |  |  |
| workphone | 办公电话 | TextField | fworkphone |  |  |
| telephone | 家庭电话 | TextField | ftelephone |  |  |
| postcode | 邮政编码 | TextField | fpostcode |  |  |
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
| otherphone | 其他手机号码 | TelephoneField | fotherphone |  |  |
| busemail | 公司电子邮箱 | EmailField | fbusemail |  |  |
| sysname | 系统用户名 | MuliLangTextField | fsysname |  |  |

