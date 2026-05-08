# hrcs_esigncoseal — 企业印章

**表单编码**: `hrcs_esigncoseal`  
**表单ID**: `3F5C65E6JBX7`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esigncoseal（企业印章） [BaseEntity]

- **数据库表**: `t_hrcs_esigncoseal`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| status | 数据状态 | BillStatusField | fstatus |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | fenable |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | fsimplename |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| disabler | 禁用人 | UserField | FDisablerID |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | FDisableDate |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | foriname |  |  |
| sealsortradiogrp | 印章分类单选按钮组 | RadioGroupField | fsealsort | ✓ |  |
| coseal | 企业印章 | RadioField | — |  |  |
| larseal | 法定代表人名章 | RadioField | — |  |  |
| esignsp | 电子签服务商 | BasedataField | fesignsp | ✓ | hrcs_esignspmgr |
| sealtype | 印章类型 | BasedataField | fsealtype |  | hrcs_esignsealtype |
| sealpic | 印章图片 | PictureField | fsealpic | ✓ |  |
| sealid | 印章ID | TextField | fsealid |  |  |
| corporate | 企业 | BasedataField | fcorporate | ✓ | hbss_lawentity |
| sealsource | 印章来源 | ComboField | fsealsource |  |  |
| authnum | 授权数量 | IntegerField | fauthnum |  |  |
| esignapp | 应用 | BasedataField | fesignapp |  | hrcs_esignappcfg |
| thirdauditstatus | 第三方审核状态 | ComboField | fthirdauditstatus |  |  |

