# hcf_canprojectexp — 拟入职人员项目经历

**表单编码**: `hcf_canprojectexp`  
**表单ID**: `15YYKVZT/EGV`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canprojectexp（拟入职人员项目经历） [BaseEntity]

- **数据库表**: `t_hcf_canprojectexp`  

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
| role | 担任角色 | MuliLangTextField | frole |  |  |
| name | 项目名称 | MuliLangTextField | fname |  |  |
| projectlenth | 项目时长 | DecimalField | fprojectlenth |  |  |
| area | 项目地区 | BasedataField | fareaid |  | bd_country |
| companynature | 企业性质 | BasedataField | fcompanynatureid |  | hbss_empnature |
| industry | 所属行业 | BasedataField | findustryid |  | hbss_industrytype |
| projectstatus | 项目状态 | ComboField | fprojectstatus |  |  |
| isenterprisepro | 企业内项目 | CheckBoxField | fisenterprisepro |  |  |
| witnessphone | 证明人电话 | TextField | fwitnessphone |  |  |
| clientname | 客户名称 | MuliLangTextField | fclientname |  |  |
| certifier | 证明人 | MuliLangTextField | fcertifier |  |  |
| belongscompany | 所属企业/单位 | MuliLangTextField | fbelongscompany |  |  |
| duty | 项目职责 | MuliLangTextField | fduty |  |  |
| projectperf | 项目业绩 | MuliLangTextField | fprojectperf |  |  |
| projecturl | 项目检索网址 | TextField | fprojecturl |  |  |
| projectlenunit | 项目时长单位 | ComboField | fprojectlenunit |  |  |
| projecttype | 项目类型 | BasedataField | fprojecttypeid |  | hbss_projecttype |
| workingcompany | 任职企业/单位 | MuliLangTextField | fworkingcompany |  |  |

