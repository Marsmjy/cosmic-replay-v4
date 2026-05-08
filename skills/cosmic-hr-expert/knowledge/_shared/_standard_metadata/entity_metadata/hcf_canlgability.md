# hcf_canlgability — 拟入职人员语言技能

**表单编码**: `hcf_canlgability`  
**表单ID**: `15YT7RJ4QO2F`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canlgability（拟入职人员语言技能） [BaseEntity]

- **数据库表**: `t_hcf_canlgability`  

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
| language | 语言种类 | BasedataField | flanguageid |  | hbss_languagetype |
| languagecert | 语言证书 | BasedataField | flanguagecertid |  | hbss_languagecert |
| iswork | 可作为工作语言 | CheckBoxField | fiswork |  |  |
| certissuedate | 证书签发日期 | DateField | fcertissuedate |  |  |
| grade | 分数等级 | TextField | fgrade |  |  |
| readscore | 阅读分数 | TextField | freadscore |  |  |
| speakscore | 口语分数 | TextField | fspeakscore |  |  |
| listenscore | 听力分数 | TextField | flistenscore |  |  |
| writescore | 写作分数 | TextField | fwritescore |  |  |
| listen | 听 | BasedataField | flisten |  | hbss_familiarity |
| speak | 说 | BasedataField | fspeak |  | hbss_familiarity |
| read | 读 | BasedataField | fread |  | hbss_familiarity |
| write | 写 | BasedataField | fwrite |  | hbss_familiarity |
| otherlanguagecert | 其他语言证书 | TextField | fotherlanguagecert |  |  |
| name | 证书名称 | MuliLangTextField | fname |  |  |

