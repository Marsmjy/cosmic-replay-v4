# hcf_personalarea — 拟入职人员区域信息

**表单编码**: `hcf_personalarea`  
**表单ID**: `1OP/BXZXSCZ3`  
**归属**: HR基础服务云 / 拟入职人员  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hcf_personalarea（拟入职人员区域信息） [BaseEntity]

- **数据库表**: `t_hcf_personalarea`  

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
| politicalstatus | 政治面貌 | BasedataField | fpoliticalstatusid |  | hbss_politicalstatus |
| joinpartydate | 入党日期 | DateField | fjoinpartydate |  |  |
| birthplace | 出生地 | MuliLangTextField | fbirthplace |  |  |
| regresidencenature | 户口性质 | BasedataField | fregresidencenatureid |  | hbss_category |
| religion | 宗教 | BasedataField | freligionid |  | hbss_religion |
| nativeplace | 籍贯 | MuliLangTextField | fnativeplace |  |  |
| party | 所属党派 | BasedataField | fpartyid |  | hbss_party |

