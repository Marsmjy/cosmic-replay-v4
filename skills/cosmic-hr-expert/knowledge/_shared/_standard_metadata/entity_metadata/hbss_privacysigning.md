# hbss_privacysigning — 隐私声明签署记录

**表单编码**: `hbss_privacysigning`  
**表单ID**: `5LRKR/PZXEIU`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbss_privacysigning（隐私声明签署记录） [BaseEntity]

- **数据库表**: `t_hrcs_privacysigning`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| user | 用户id | TextField | fuserid |  |  |
| usertype | 用户类型 | BasedataField | fusertypeid |  | hbss_privacyusertype |
| privacystmt | 隐私声明 | BasedataField | fprivacystmtid |  | privacystatement |
| version | 版本 | TextField | fversion |  |  |
| form | 业务对象 | BasedataField | fformid |  | bos_objecttype |
| locale | 语种 | BasedataField | flocaleid |  | inte_language |
| country | 国家 | BasedataField | fcountryid |  | bd_country |
| client | 客户端 | TextField | fclient |  |  |
| isagree | 签署状态 | ComboField | fisagree |  |  |
| modifytime | 操作时间 | DateTimeField | fmodifytime |  |  |
| org | 行政组织 | OrgField | forgid |  | bos_org |

