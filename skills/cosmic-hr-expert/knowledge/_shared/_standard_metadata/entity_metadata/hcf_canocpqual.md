# hcf_canocpqual — 拟入职人员职业资格

**表单编码**: `hcf_canocpqual`  
**表单ID**: `15Z+2SF1B5UB`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canocpqual（拟入职人员职业资格） [BaseEntity]

- **数据库表**: `t_hcf_canocpqual`  

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
| certiicateid | 证书编号 | TextField | fcertiicateid |  |  |
| expirationdate | 证书有效期 | DateField | fexpirationdate |  |  |
| gettime | 证书获得日期 | DateField | fgettime |  |  |
| issuednation | 颁发国家/地区 | BasedataField | fissuednationid |  | bd_country |
| grantunit | 发证机构 | TextField | fgrantunit |  |  |
| qualification | 职业资格 | BasedataField | fqualificationid |  | hbss_ocpqual |
| qualevel | 职业资格等级 | BasedataField | fqualevelid |  | hbss_ocpquallevel |
| registratedate | 聘任或注册日期 | DateField | fregistratedate |  |  |
| ismajor | 主要职业资格 | CheckBoxField | fismajor |  |  |
| enddate | 聘任终止日期 | DateField | fenddate |  |  |
| isnofixedterm | 长期有效 | CheckBoxField | fisnofixedterm |  |  |
| registrationunit | 聘任或注册单位 | MuliLangTextField | fregistrationunit |  |  |
| name | 证书名称 | MuliLangTextField | fname |  |  |

