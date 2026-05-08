# hrcs_contractest — 电子合同_联调测试

**表单编码**: `hrcs_contractest`  
**表单ID**: `2=O45MDY2GD5`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_contractest（电子合同_联调测试） [BaseEntity]

- **数据库表**: `t_hrcs_contract`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 合同编号 | TextField | fnumber |  |  |
| econttmp | 电子合同模板 | BasedataField | feconttmpid |  | hrcs_econtemplate |
| ishandsign | 是否已上传 | CheckBoxField | fishandsign |  |  |
| signurl | 手签地址 | TextAreaField | fsignurl |  |  |
| attachmentfield | 附件 | AttachmentField | t_hrcs_econtfile（子表） | ✓ |  |
| candidate | 候选人 | BasedataField | fcandidateid |  | hcf_candidate |
| autosign | 自动签 | CheckBoxField | fautosign |  |  |
| signmode | 签署模式 | ComboField | fsignmode |  |  |
| handsignfronturl | 手签成功跳转地址 | TextAreaField | fhandsignfronturl |  |  |
| authfronturl | 实名认证通过跳转地址 | TextAreaField | ftextareafield |  |  |
| name | 姓名 | TextField | fname |  |  |
| mobile | 手机号码 | TextField | fmobile |  |  |
| identity | 证件号 | TextField | fidentity |  |  |
| businessid | 业务唯一ID | TextField | fbusinessid |  |  |
| appnum | 应用编码 | TextField | fappnum |  |  |
| authurl | 实名认证地址 | TextAreaField | fauthurl |  |  |
| authstatus | 实名认证通过 | CheckBoxField | fauthstatus |  |  |
| textfield | 备注 | TextField | ftextfield |  |  |
| sortnum | 排序号 | IntegerField | fsortnum | ✓ |  |
| cloud | 云编码 | TextField | fcloud |  |  |
| org | 合同主体ID | BigIntField | forg | ✓ |  |
| templatetype | 签署类型 | BasedataField | ftemplatetype |  | hrcs_econtemplatetype |
| identitytype | 证件类型 | BasedataField | fidentitytype |  | hbss_credentialstype |
| lawentity | 法律实体 | BasedataField | flawentityid |  | hbss_lawentity |
| person | 自然人 | EmployeeField | fpersonid |  | hrpi_employeenewf7query |

