# hcf_canfamily — 拟入职人员家庭成员

**表单编码**: `hcf_canfamily`  
**表单ID**: `15YTZ9Y2O21C`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canfamily（拟入职人员家庭成员） [BaseEntity]

- **数据库表**: `t_hcf_canfamily`  

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
| mobilephone | 家庭成员联系电话 | TelephoneField | fmobilephone |  |  |
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| familymembship | 家庭成员关系 | BasedataField | ffamilymembshipid |  | hbss_familymemberrel |
| name | 家庭成员姓名 | TextField | fname |  |  |
| contactaddr | 联系地址 | TextField | fcontactaddr |  |  |
| workunit | 工作单位 | TextField | fworkunit |  |  |
| birthday | 出生日期 | DateField | fbirthday |  |  |
| age | 年龄 | IntegerField | fage |  |  |
| politicalstatus | 政治面貌 | BasedataField | fpoliticalstatusid |  | hbss_politicalstatus |
| certnumber | 身份证号 | TextField | fcertnumber |  |  |
| job | 职务 | MuliLangTextField | fjob |  |  |

