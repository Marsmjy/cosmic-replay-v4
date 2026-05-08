# hrcs_dynacond — 动态数据范围配置

**表单编码**: `hrcs_dynacond`  
**表单ID**: `3WCX+Q3WNN4+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_dynacond（动态数据范围配置） [BaseEntity]

- **数据库表**: `t_hrcs_dynacond`  

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
| applyscope | 适用范围 | ComboField | fapplyscope | ✓ |  |
| applytype | 结果值适配类型 | ComboField | fapplytype |  |  |
| applybdtype | 基础资料类型 | MulBasedataField | t_hrcs_dynacondapplybd（子表） |  |  |
| applyteamcls | 团队分类 | MulBasedataField | t_hrcs_dynacondapplycls（子表） |  |  |
| parsertype | 解析调用类型 | ComboField | fparsertype | ✓ |  |
| parserclass | 解析类 | TextField | fparserclass |  |  |
| mserviceapp | 微服务所在应用 | BasedataField | fmserviceappid |  | bos_devportal_bizapp |
| mserviceclass | 微服务 | TextField | fmserviceclass |  |  |
| mservicemethod | 方法 | TextField | — |  |  |

