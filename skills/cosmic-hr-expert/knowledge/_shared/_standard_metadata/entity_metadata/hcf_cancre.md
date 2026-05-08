# hcf_cancre — 拟入职人员证件信息

**表单编码**: `hcf_cancre`  
**表单ID**: `15Y9/5AHTCJ+`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_cancre（拟入职人员证件信息） [BaseEntity]

- **数据库表**: `t_hcf_cancre`  

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
| credentialstype | 证件类型 | BasedataField | fcredentialstypeid |  | hbss_credentialstype |
| number | 证件号码 | TextField | fnumber |  |  |
| issuedate | 证件签发日期 | DateField | fissuedate |  |  |
| expirationdate | 证件有效期至 | DateField | fexpirationdate |  |  |
| ismajor | 主证件 | CheckBoxField | fismajor |  |  |
| isidentity | 用于身份认证 | CheckBoxField | fisidentity |  |  |
| countrycode | 国家码 | BasedataField | fcountrycodeid |  | bd_country |
| birthday | 出生日期 | DateField | fbirthday |  |  |
| nationality | 国籍 | BasedataField | fnationalityid |  | hbss_nationality |
| lastnamecn | 姓（中文） | TextField | flastnamecn |  |  |
| firstnamecn | 名（中文） | TextField | ffirstnamecn |  |  |
| lastnameen | 姓（拼音/英文） | TextField | flastnameen |  |  |
| firstnameen | 名（拼音/英文） | TextField | ffirstnameen |  |  |
| percardname | 证件姓名 | TextField | fpercardname |  |  |
| idcardaddress | 证件地址 | TextField | fidcardaddress |  |  |
| faceimage | 证件正面 | PictureField | ffaceimage |  |  |
| reverseimage | 证件反面 | PictureField | freverseimage |  |  |
| gender | 性别 | BasedataField | fgenderid |  | hbss_sex |
| folk | 民族 | BasedataField | ffolkid |  | hbss_flok |
| isnofixedterm | 长期有效 | CheckBoxField | fisnofixedterm |  |  |
| issuingplace | 签发地点 | MuliLangTextField | fissuingplace |  |  |
| issuingauthor | 签发机关 | MuliLangTextField | fissuingauthor |  |  |
| birthplace | 出生地 | AdminDivisionField | fbirthplace |  |  |

