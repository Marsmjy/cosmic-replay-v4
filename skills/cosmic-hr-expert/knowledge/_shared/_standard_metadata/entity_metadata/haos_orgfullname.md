# haos_orgfullname — 组织全称

**表单编码**: `haos_orgfullname`  
**表单ID**: `5AJ8D6ROVMGQ`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_orgfullname（组织全称） [BaseEntity]

- **数据库表**: `t_haos_orgfullname`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 是否已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 是否当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| structlongnumber | 结构长编码 | TextField | fstructlongnumber |  |  |
| adminorg | 组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| structfullname | 结构长名称 | MuliLangTextField | fstructfullname |  |  |
| fullname | 长名称 | MuliLangTextField | ffullname |  |  |
| levelorg1 | 一级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg2 | 二级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg3 | 三级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg4 | 四级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg5 | 五级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg6 | 六级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg7 | 七级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg8 | 八级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg9 | 九级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg10 | 十级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg11 | 十一级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg12 | 十二级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg13 | 十三级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg14 | 十四级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg15 | 十五级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg16 | 十六级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg17 | 十七级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg18 | 十八级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg19 | 十九级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg20 | 二十级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg21 | 二十一级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg22 | 二十二级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg23 | 二十三级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg24 | 二十四级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg25 | 二十五级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg26 | 二十六级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg27 | 二十七级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg28 | 二十八级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg29 | 二十九级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| levelorg30 | 三十级组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |

