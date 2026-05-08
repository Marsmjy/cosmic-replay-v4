# hcf_canbankcard — 拟入职人员银行卡

**表单编码**: `hcf_canbankcard`  
**表单ID**: `15YYAXNJ1U7=`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_canbankcard（拟入职人员银行卡） [BaseEntity]

- **数据库表**: `t_hcf_canbankcard`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 备注 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| candidate | 拟入职人员 | BasedataField | fcandidateid |  | hcf_candidate |
| bankdeposit | 收款银行 | BasedataField | fbankdepositid |  | bd_bebank |
| bankcardnum | 银行账号 | TextField | fbankcardnum |  |  |
| accountrelation | 账户关系 | BasedataField | faccountrelationid |  | hbss_payrollacrelation |
| cardpurpose | 银行卡用途 | MulBasedataField | t_hcf_bankcardpurpose（子表） |  |  |
| username | 账户名 | TextField | fusername |  |  |

