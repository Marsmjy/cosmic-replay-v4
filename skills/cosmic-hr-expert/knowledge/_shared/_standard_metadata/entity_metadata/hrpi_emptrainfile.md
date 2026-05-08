# hrpi_emptrainfile — 培训经历

**表单编码**: `hrpi_emptrainfile`  
**表单ID**: `15BEKW7O50M/`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_emptrainfile（培训经历） [BaseEntity]

- **数据库表**: `t_hrpi_emptrainfile`  

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
| classhour | 培训课时 | DecimalField | fclasshour |  |  |
| traintype | 培训类别 | BasedataField | ftraintypeid |  | hbss_traintype |
| period | 培训期次 | DecimalField | fperiod |  |  |
| trainmode | 培训方式 | BasedataField | ftrainmodeid |  | hbss_trainmode |
| score | 培训测试得分 | DecimalField | fscore |  |  |
| issign | 签培训协议 | CheckBoxField | fissign |  |  |
| name | 培训课程 | MuliLangTextField | fname |  |  |
| agreement | 协议条款 | MuliLangTextField | fagreement |  |  |
| trainlengthunit | 培训时长单位 | ComboField | ftrainlengthunit |  |  |
| isinnertraining | 企业内培训 | CheckBoxField | fisinnertraining |  |  |
| trainingcertificate | 培训证书 | MuliLangTextField | ftrainingcertificate |  |  |
| certificateno | 证书编号 | TextField | fcertificateno |  |  |
| trainlength | 培训时长 | DecimalField | ftrainlength |  |  |
| startdate | 开始日期 | DateField | fstartdate |  |  |
| enddate | 结束日期 | DateField | fenddate |  |  |
| cost | 培训费用 | AmountField | fcost |  |  |
| currency | 币种 | CurrencyField | fcurrencyid |  | bd_currency |
| organizer | 培训机构 | MuliLangTextField | forganizerid |  |  |

