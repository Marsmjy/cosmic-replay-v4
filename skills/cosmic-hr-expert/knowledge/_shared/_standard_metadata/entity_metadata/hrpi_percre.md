# hrpi_percre — 证件信息

**表单编码**: `hrpi_percre`  
**表单ID**: `15BJS5+H711/`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_percre（证件信息） [BaseEntity]

- **数据库表**: `t_hrpi_percre`  

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
| credentialstype | 证件类型 | BasedataField | fcredentialstypeid | ✓ | hbss_credentialstype |
| number | 证件号码 | TextField | fnumber | ✓ |  |
| expirationdate | 证件有效期至 | DateField | fexpirationdate |  |  |
| countrycode | 国家码 | BasedataField | fcountrycodeid |  | bd_country |
| issuingauthor | 签发机关 | MuliLangTextField | fissuingauthor |  |  |
| issuedate | 证件签发日期 | DateField | fissuedate |  |  |
| isnofixedterm | 长期有效 | CheckBoxField | fisnofixedterm |  |  |
| birthplace | 出生地 | AdminDivisionField | fbirthplace |  |  |
| birthday | 出生日期 | DateField | fbirthday |  |  |
| lastnameen | 姓（拼音/英文） | TextField | flastnameen |  |  |
| firstnameen | 名（拼音/英文） | TextField | ffirstnameen |  |  |
| lastnamecn | 姓（中文） | TextField | flastnamecn |  |  |
| firstnamecn | 名（中文） | TextField | ffirstnamecn |  |  |
| nationality | 国籍 | BasedataField | fnationalityid |  | hbss_nationality |
| gender | 性别 | BasedataField | fgenderid |  | hbss_sex |
| issuingplace | 签发地点 | MuliLangTextField | fissuingplace |  |  |
| folk | 民族 | BasedataField | ffolkid |  | hbss_flok |
| faceimage | 证件正面 | PictureField | ffaceimage |  |  |
| reverseimage | 证件反面 | PictureField | freverseimage |  |  |
| percardname | 证件姓名 | TextField | fpercardname |  |  |
| ismajor | 主证件 | CheckBoxField | fismajor |  |  |
| isidentity | 用于身份认证 | CheckBoxField | fisidentity |  |  |
| idcardaddress | 证件地址 | TextField | fidcardaddress |  |  |

