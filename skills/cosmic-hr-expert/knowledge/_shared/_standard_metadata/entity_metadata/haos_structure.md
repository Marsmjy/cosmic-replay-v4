# haos_structure — 矩阵组织维护

**表单编码**: `haos_structure`  
**表单ID**: `3C5IN45RNAP4`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_structure（矩阵组织维护） [BaseEntity]

- **数据库表**: `t_haos_structproject`  

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
| org | 创建组织 | OrgField | forgid | ✓ | bos_org |
| rootorg | 根组织 | HRAdminOrgField | frootorgid | ✓ | haos_adminorghrf7 |
| roottype | 根组织类型 | ComboField | froottype |  |  |
| otclassify | 组织团队分类 | BigIntField | fotclassifyid |  |  |
| isincludevirtualorg | 是否包含虚拟组织 | CheckBoxField | fisincludevirtualorg |  |  |
| iscustomorg | 是否是自定义组织 | CheckBoxField | fiscustomorg |  |  |
| relyonstructproject | 依赖架构方案 | BasedataField | frelyonstructprojectid |  | haos_structproject |
| rootnumber | 根组织编码 | TextField | frootnumber |  |  |
| rootname | 根组织名称 | MuliLangTextField | frootname |  |  |
| rooteffdt | 根组织生效日期 | DateField | frooteffdt |  |  |
| rootdescription | 根组织描述 | MuliLangTextField | frootdescription |  |  |
| istoallareas | 是否应用全领域 | CheckBoxField | fistoallareas |  |  |

