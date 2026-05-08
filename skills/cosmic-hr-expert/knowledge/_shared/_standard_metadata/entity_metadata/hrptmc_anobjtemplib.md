# hrptmc_anobjtemplib — 分析对象模板库

**表单编码**: `hrptmc_anobjtemplib`  
**表单ID**: `4+THSN0YF0/3`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_anobjtemplib（分析对象模板库） [BaseEntity]

- **数据库表**: `t_hrptmc_analysisobject`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 对象描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 对象编码 | TextField | fnumber | ✓ |  |
| name | 对象名称 | MuliLangTextField | fname | ✓ |  |
| datafilter | 数据过滤规则 | TextField | fdatafilter |  |  |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| storeentity | 落地实体 | TextField | fstoreentity |  |  |
| isv | 开发商标识 | TextField | fisv |  |  |
| version | 版本号 | IntegerField | fversion |  |  |
| enable | 状态 | BillStatusField | fenable |  |  |
| queryscheme | 取数方案 | ComboField | fqueryscheme |  |  |
| ruledate | 触发日期操作的控件（勿删） | DateField | — |  |  |
| isvirtualentity | 是否虚实体对象 | CheckBoxField | fisvirtualentity |  |  |
| pivotdimval | 转置维度值 | TextField | fpivotdimval |  |  |
| pivotindex | 被转置的指标 | TextField | fpivotindex |  |  |
| sidebar | 侧边栏顺序 | TextField | fsidebar |  |  |
| pivotdim | 需要转置的列 | ComboField | fpivotdim |  |  |
| objecttype | 对象类型 | ComboField | fobjecttype |  |  |

