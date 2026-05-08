# hcf_cancontact — 拟入职人员紧急联系人

**表单编码**: `hcf_cancontact`  
**表单ID**: `16/NGLLKABY8`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_cancontact（拟入职人员紧急联系人） [BaseEntity]

- **数据库表**: `t_hcf_cancontact`  

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
| emrgphone | 紧急联系人联系电话 | TelephoneField | femrgphone |  |  |
| emergcontactype | 紧急联系人类型 | BasedataField | femergcontactypeid |  | hbss_emergcontactype |
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| emrgname | 紧急联系人姓名 | TextField | femrgname |  |  |
| emrgaddress | 联系地址 | TextField | femrgaddress |  |  |

