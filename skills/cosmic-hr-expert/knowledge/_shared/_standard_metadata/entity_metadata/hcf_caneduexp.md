# hcf_caneduexp — 拟入职人员教育经历

**表单编码**: `hcf_caneduexp`  
**表单ID**: `1606B53J7208`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_caneduexp（拟入职人员教育经历） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hcf_caneduexp` | BaseEntity | 主表 |
| `t_hcf_eduexpcert` | EntryEntity | 教育证件 |
| `t_hcf_eduexpcertatt` | MulEmployeeField子表 | 附件 |

### 字段列表 — t_hcf_caneduexp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcf_caneduexp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcf_caneduexp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcf_caneduexp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcf_caneduexp.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hcf_caneduexp.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | t_hcf_caneduexp.finitdatasource |  |  |
| candidate | 拟入职人员 | BasedataField | t_hcf_caneduexp.fcandidateid |  | hcf_candidate |
| education | 学历 | BasedataField | t_hcf_caneduexp.feducationid |  | hbss_diploma |
| degree | 学位 | BasedataField | t_hcf_caneduexp.fdegreeid |  | hbss_degree |
| graduateschool | 毕业院校 | BasedataField | t_hcf_caneduexp.fgraduateschoolid |  | hbss_college |
| collegecountry | 院校所在国家/地区 | BasedataField | t_hcf_caneduexp.fcollegecountryid |  | bd_country |
| admissiondate | 入学日期 | DateField | t_hcf_caneduexp.fadmissiondate |  |  |
| gradutiondate | 毕业日期 | DateField | t_hcf_caneduexp.fgradutiondate |  |  |
| ishighestdegree | 最高学历 | CheckBoxField | t_hcf_caneduexp.fishighestdegree |  |  |
| isfulltime | 全日制 | CheckBoxField | t_hcf_caneduexp.fisfulltime |  |  |
| schoolsystem | 学制（年） | DecimalField | t_hcf_caneduexp.fschoolsystem |  |  |
| edunature | 学历性质 | BasedataField | t_hcf_caneduexp.fedunatureid |  | hbss_diplomatype |
| authedegree | 学位授予国家/地区 | BasedataField | t_hcf_caneduexp.fauthedegreeid |  | bd_country |
| isoverseas | 海外教育经历 | CheckBoxField | t_hcf_caneduexp.fisoverseas |  |  |
| schoolrecord | 其他院校 | MuliLangTextField | t_hcf_caneduexp.fschoolrecord |  |  |
| major | 第一专业 | MuliLangTextField | t_hcf_caneduexp.fmajor |  |  |
| secondmajor | 第二专业 | MuliLangTextField | t_hcf_caneduexp.fsecondmajor |  |  |
| collegedepartment | 所在院系 | MuliLangTextField | t_hcf_caneduexp.fcollegedepartment |  |  |
| awardeddate | 授予日期 | DateField | t_hcf_caneduexp.fawardeddate |  |  |
| iscadrehighestdegree | 干部最高学历（全日制） | CheckBoxField | t_hcf_caneduexp.fiscadrehighestdegree |  |  |
| expcert | 教育证件 | EntryEntity | → t_hcf_eduexpcert |  |  |

### 字段列表 — t_hcf_eduexpcert（教育证件·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| certtype | 证书类型 | BasedataField | t_hcf_eduexpcert.fcerttypeid |  | hbss_educerttype |
| certnumber | 证书编号 | TextField | t_hcf_eduexpcert.fcertnumber |  |  |
| isauthenticated | 认证证书 | CheckBoxField | t_hcf_eduexpcert.fisauthenticated |  |  |
| authcertnumber | 证书认证号 | TextField | t_hcf_eduexpcert.fauthcertnumber |  |  |
| attachmentfield | 附件 | AttachmentField | t_hcf_eduexpcertatt（子表） |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcf_caneduexp（主表） | 25 |
| t_hcf_eduexpcert（教育证件） | 5 |
| t_hcf_eduexpcertatt（MulEmployeeField子表） | 1 |

