# hcf_canprework — 拟入职人员工作经历

**表单编码**: `hcf_canprework`  
**表单ID**: `15YYO1G0ZC4V`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canprework（拟入职人员工作经历） [BaseEntity]

- **数据库表**: `t_hcf_canprework`  

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
| startdate | 开始日期 | DateField | fstartdate |  |  |
| enddate | 结束日期 | DateField | fenddate |  |  |
| exitdate | 离职日期 | DateField | fexitdate |  |  |
| quitreason | 离职原因 | TextField | fquitreason |  |  |
| tenuretime | 任职时长 | DecimalField | ftenuretime |  |  |
| trade | 所属行业 | BasedataField | ftradeid |  | hbss_industrytype |
| businesstypeid | 企业性质 | BasedataField | fbusinesstypeid |  | hbss_empnature |
| subordinates | 下属人数 | IntegerField | fsubordinates |  |  |
| isabroadbackground | 海外工作背景 | CheckBoxField | fisabroadbackground |  |  |
| witnessphone | 证明人电话 | TextField | fwitnessphone |  |  |
| empcountry | 雇主所属国家/地区 | BasedataField | fempcountryid |  | bd_country |
| positiontype | 职位类别 | MuliLangTextField | fpositiontype |  |  |
| department | 任职部门 | MuliLangTextField | fdepartment |  |  |
| empaddress | 雇主地址 | MuliLangTextField | fempaddress |  |  |
| position | 任职职位 | MuliLangTextField | fposition |  |  |
| jobcityid | 工作城市 | CityField | fjobcityid |  | bd_admindivision |
| isuntilnow | 至今 | CheckBoxField | fisuntilnow |  |  |
| jobdesc | 工作职责 | MuliLangTextField | fjobdesc |  |  |
| tenuretimeunit | 任职时长单位 | ComboField | ftenuretimeunit |  |  |
| unitname | 雇主名称 | TextField | funitname |  |  |
| companyscale | 公司规模 | BasedataField | fcompanyscaleid |  | hbss_companyscale |
| witness | 证明人 | MuliLangTextField | fwitness |  |  |
| duty | 担任职务 | TextField | fduty |  |  |
| reportposition | 汇报职位 | MuliLangTextField | freportposition |  |  |

