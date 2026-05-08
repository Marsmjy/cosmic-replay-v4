# hrcs_esignsealauth — 印章授权

**表单编码**: `hrcs_esignsealauth`  
**表单ID**: `3HDW+GB2W5+/`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_esignsealauth（印章授权） [BaseEntity]

- **数据库表**: `t_hrcs_esignsealauth`  

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
| auttype | 授权类型 | RadioGroupField | fauttype |  |  |
| othercompany | 其他企业 | RadioField | — |  |  |
| companymember | 企业内成员 | RadioField | — |  |  |
| authcompany | 授权企业 | BasedataField | fauthcompany | ✓ | hbss_lawentity |
| authrange | 授权范围 | ComboField | fauthrange | ✓ |  |
| validity | 有效期 | DateRangeField | — |  |  |
| seal | 电子签章 | BasedataField | fseal |  | hrcs_esigncoseal |
| authstatus | 授权状态 | ComboField | fauthstatus |  |  |
| esignsp | 电子签服务商 | BasedataField | fesignsp | ✓ | hrcs_esignspmgr |

