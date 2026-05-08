# hrpi_preworkexp — 前工作经历

**表单编码**: `hrpi_preworkexp`  
**表单ID**: `15VL/M36S033`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_preworkexp（前工作经历） [BaseEntity]

- **数据库表**: `t_hrpi_preworkexp`  

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
| unitname | 前雇主名称 | TextField | funitname |  |  |
| startdate | 开始日期 | DateField | fstartdate |  |  |
| enddate | 结束日期 | DateField | fenddate |  |  |
| duty | 担任职务 | TextField | fduty |  |  |
| empcountry | 前雇主所属国家/地区 | BasedataField | fempcountryid |  | bd_country |
| jobdesc | 工作职责 | MuliLangTextField | fjobdesc |  |  |
| jobcityid | 工作城市 | CityField | fjobcityid |  | bd_admindivision |
| tenuretime | 任职时长 | DecimalField | ftenuretime |  |  |
| trade | 所属行业 | BasedataField | ftradeid |  | hbss_industrytype |
| businesstypeid | 企业性质 | BasedataField | fbusinesstypeid |  | hbss_empnature |
| subordinates | 下属人数 | IntegerField | fsubordinates |  |  |
| isabroadbackground | 海外工作背景 | CheckBoxField | fisabroadbackground |  |  |
| witnessphone | 证明人电话 | TextField | fwitnessphone |  |  |
| reportposition | 汇报职位 | MuliLangTextField | freportposition |  |  |
| position | 任职职位 | MuliLangTextField | fposition |  |  |
| department | 任职部门 | MuliLangTextField | fdepartment |  |  |
| positiontype | 职位类别 | MuliLangTextField | fpositiontype |  |  |
| isuntilnow | 至今 | CheckBoxField | fisuntilnow |  |  |
| companyscale | 公司规模 | BasedataField | fcompanyscaleid |  | hbss_companyscale |
| quitreason | 离职原因 | TextField | fquitreason |  |  |
| exitdate | 离职日期 | DateField | fexitdate |  |  |
| tenuretimeunit | 任职时长单位 | ComboField | ftenuretimeunit |  |  |
| empaddress | 前雇主地址 | MuliLangTextField | fempaddress |  |  |
| witness | 证明人 | MuliLangTextField | fwitness |  |  |

