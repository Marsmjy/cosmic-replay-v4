# hrptmc_reportmanage — 报表管理

**表单编码**: `hrptmc_reportmanage`  
**表单ID**: `2VY37BL22XAJ`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_reportmanage（报表管理） [BaseEntity]

- **数据库表**: `t_hrptmc_reportmanage`  

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
| cloudid | 归属业务云 | BasedataField | fcloudid |  | bos_devportal_bizcloud |
| anobjid | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |
| row | 行 | TextField | frow |  |  |
| column | 列 | TextField | fcolumn |  |  |
| publishstatus | 发布应用菜单状态 | ComboField | fpublishstatus |  |  |
| rptindex | 指标 | TextField | — |  |  |
| rpttype | 图标类型 | TextField | — |  |  |
| rptfilter | 筛选器 | TextField | — |  |  |
| rptset | 表格设置 | TextField | — |  |  |
| datafilter | 数据过滤条件 | TextField | fdatafilter |  |  |
| createorg | 创建组织 | OrgField | fcreateorgid |  | bos_org |
| drillingdrl | 下钻目录 | TextField | fdrillingdrl |  |  |
| publishrptstatus | 发布报表中心状态 | ComboField | fpublishrptstatus |  |  |
| newpublishstatus | 发布状态 | ComboField | — |  |  |
| publishworkstatus | 发布工作台状态 | ComboField | fpublishworkstatus |  |  |

