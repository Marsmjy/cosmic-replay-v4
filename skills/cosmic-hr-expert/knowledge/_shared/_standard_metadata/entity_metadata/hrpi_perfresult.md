# hrpi_perfresult — 绩效结果

**表单编码**: `hrpi_perfresult`  
**表单ID**: `5GZ=S563DH+Y`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_perfresult（绩效结果） [BaseEntity]

- **数据库表**: `t_hrpi_perfresult`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| empposrel | 任职经历 | BasedataField | fempposrelid |  | hrpi_empposorgrel |
| assesslevel | 综合绩效等级 | ComboField | fassesslevel |  |  |
| assessleveltext | 综合绩效等级 | MuliLangTextField | fassessleveltext |  |  |
| score | 综合绩效得分 | DecimalField | fscore |  |  |
| rulescore | 评分分制 | BasedataField | frulescoreid |  | hbss_scoresystem |
| perflevel | 绩效等级 | BasedataField | fperflevelid |  | hbss_perflevel |
| period | 周期 | ComboField | fperiod |  |  |
| datasource | 数据来源 | ComboField | fdatasource |  |  |
| number | 绩效结果编码 | TextField | fnumber |  |  |
| assessperiod | 考核周期 | TextField | fassessperiod |  |  |
| year | 年 | DateField | fyear |  |  |
| fsopisnull | 综合绩效的得分是否为空 | CheckBoxField | ffsopisnull |  |  |

