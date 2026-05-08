# hcf_cantraining — 拟入职人员培训经历

**表单编码**: `hcf_cantraining`  
**表单ID**: `15YYS8U9CYG2`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_cantraining（拟入职人员培训经历） [BaseEntity]

- **数据库表**: `t_hcf_cantraining`  

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
| enddate | 结束日期 | DateField | fenddate |  |  |
| startdate | 开始日期 | DateField | fstartdate |  |  |
| name | 培训课程 | MuliLangTextField | fname |  |  |
| trainlength | 培训时长 | DecimalField | ftrainlength |  |  |
| trainingcertificate | 培训证书 | MuliLangTextField | ftrainingcertificate |  |  |
| certificateno | 证书编号 | TextField | fcertificateno |  |  |
| trainlengthunit | 培训时长单位 | ComboField | ftrainlengthunit |  |  |
| trainmode | 培训方式 | BasedataField | ftrainmodeid |  | hbss_trainmode |
| organizer | 培训机构 | MuliLangTextField | forganizerid |  |  |
| score | 培训测试得分 | DecimalField | fscore |  |  |
| cost | 培训费用 | DecimalField | fcost |  |  |
| currency | 币种 | CurrencyField | fcurrencyid |  | bd_currency |
| classhour | 培训课时 | DecimalField | fclasshour |  |  |
| period | 培训期次 | DecimalField | fperiod |  |  |
| isinnertraining | 企业内培训 | CheckBoxField | fisinnertraining |  |  |
| issign | 签培训协议 | CheckBoxField | fissign |  |  |
| agreement | 协议条款 | MuliLangTextField | fagreement |  |  |
| traintype | 培训类别 | BasedataField | ftraintypeid |  | hbss_traintype |

