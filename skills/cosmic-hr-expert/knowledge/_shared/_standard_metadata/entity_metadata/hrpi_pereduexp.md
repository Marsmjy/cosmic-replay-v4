# hrpi_pereduexp — 教育经历

**表单编码**: `hrpi_pereduexp`  
**表单ID**: `15B5/4HZBDXR`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_pereduexp（教育经历） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrpi_pereduexp` | BaseEntity | 主表 |
| `t_hrpi_eduexpcert` | EntryEntity | 教育证件 |
| `t_hrpi_eduexpcertatt` | MulEmployeeField子表 | 附件 |

### 字段列表 — t_hrpi_pereduexp（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrpi_pereduexp.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrpi_pereduexp.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrpi_pereduexp.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrpi_pereduexp.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrpi_pereduexp.finitdatasource |  |  |
| description | 描述 | MuliLangTextField | t_hrpi_pereduexp.fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_hrpi_pereduexp.fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | t_hrpi_pereduexp.femployeeid | ✓ | hrpi_employeenewf7query |
| issingle | 单行显示 | CheckBoxField | t_hrpi_pereduexp.fissingle |  |  |
| admissiondate | 入学日期 | DateField | t_hrpi_pereduexp.fadmissiondate |  |  |
| gradutiondate | 毕业日期 | DateField | t_hrpi_pereduexp.fgradutiondate |  |  |
| schoolsystem | 学制（年） | DecimalField | t_hrpi_pereduexp.fschoolsystem |  |  |
| education | 学历 | BasedataField | t_hrpi_pereduexp.feducationid |  | hbss_diploma |
| edunature | 学历性质 | BasedataField | t_hrpi_pereduexp.fedunatureid |  | hbss_diplomatype |
| degree | 学位 | BasedataField | t_hrpi_pereduexp.fdegreeid |  | hbss_degree |
| authedegree | 学位授予国家/地区 | BasedataField | t_hrpi_pereduexp.fauthedegreeid |  | bd_country |
| major | 第一专业 | MuliLangTextField | t_hrpi_pereduexp.fmajor |  |  |
| schoolrecord | 其他院校 | MuliLangTextField | t_hrpi_pereduexp.fschoolrecord |  |  |
| collegedepartment | 所在院系 | MuliLangTextField | t_hrpi_pereduexp.fcollegedepartment |  |  |
| collegecountry | 院校所在国家/地区 | BasedataField | t_hrpi_pereduexp.fcollegecountryid |  | bd_country |
| awardeddate | 授予日期 | DateField | t_hrpi_pereduexp.fawardeddate |  |  |
| secondmajor | 第二专业 | MuliLangTextField | t_hrpi_pereduexp.fsecondmajor |  |  |
| isoverseas | 海外教育经历 | CheckBoxField | t_hrpi_pereduexp.fisoverseas |  |  |
| graduateschool | 毕业院校 | HisModelBasedataField | t_hrpi_pereduexp.fgraduateschoolid |  | hbss_college |
| isfulltime | 全日制 | CheckBoxField | t_hrpi_pereduexp.fisfulltime |  |  |
| ishighestdegree | 最高学历 | CheckBoxField | t_hrpi_pereduexp.fishighestdegree |  |  |
| iscadrehighestdegree | 干部最高学历（全日制） | CheckBoxField | t_hrpi_pereduexp.fiscadrehighestdegree |  |  |
| expcert | 教育证件 | EntryEntity | → t_hrpi_eduexpcert |  |  |

### 字段列表 — t_hrpi_eduexpcert（教育证件·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| certnumber | 证书编号 | TextField | t_hrpi_eduexpcert.fcertnumber |  |  |
| isauthenticated | 认证证书 | CheckBoxField | t_hrpi_eduexpcert.fisauthenticated |  |  |
| authcertnumber | 证书认证号 | TextField | t_hrpi_eduexpcert.fauthcertnumber |  |  |
| certtype | 证书类型 | BasedataField | t_hrpi_eduexpcert.fcerttypeid |  | hbss_educerttype |
| attachmentfield | 附件 | AttachmentField | t_hrpi_eduexpcertatt（子表） |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrpi_pereduexp（主表） | 28 |
| t_hrpi_eduexpcert（教育证件） | 5 |
| t_hrpi_eduexpcertatt（MulEmployeeField子表） | 1 |
| 无数据库列 | 1 |

