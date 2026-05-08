# hrpi_perlgability — 语言技能

**表单编码**: `hrpi_perlgability`  
**表单ID**: `4SMSPSZUN01B`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perlgability（语言技能） [BaseEntity]

- **数据库表**: `t_hrpi_perlgability`  

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
| grade | 分数等级 | TextField | fgrade |  |  |
| certissuedate | 证书签发日期 | DateField | fcertissuedate |  |  |
| writescore | 写作分数 | TextField | fwritescore |  |  |
| readscore | 阅读分数 | TextField | freadscore |  |  |
| listenscore | 听力分数 | TextField | flistenscore |  |  |
| speakscore | 口语分数 | TextField | fspeakscore |  |  |
| name | 证书名称 | MuliLangTextField | fname |  |  |
| language | 语言种类 | BasedataField | flanguageid | ✓ | hbss_languagetype |
| languagecert | 语言证书 | BasedataField | flanguagecertid |  | hbss_languagecert |
| otherlanguagecert | 其他语言证书 | TextField | fotherlanguagecert |  |  |
| iswork | 可作为工作语言 | CheckBoxField | fiswork |  |  |
| listen | 听 | BasedataField | flisten |  | hbss_familiarity |
| speak | 说 | BasedataField | fspeak |  | hbss_familiarity |
| read | 读 | BasedataField | fread |  | hbss_familiarity |
| write | 写 | BasedataField | fwrite |  | hbss_familiarity |

