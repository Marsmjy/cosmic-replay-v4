# hbp_entityobject — HR主实体对象

**表单编码**: `hbp_entityobject`  
**表单ID**: `29E=9E3S658E`  
**归属**: HR基础服务云 / HR基础平台  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbp_entityobject（HR主实体对象） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| modeltype | 类型 | ComboField | — |  |  |
| bizappid | 应用 | BasedataField | — |  | bos_devportal_bizapp |
| dentityid | 实体 | TextField | — |  |  |
| model | 模板判断 | TextField | — |  |  |
| istemplate | 是否模板 | CheckBoxField | — |  |  |
| codenumber | 支持编码规则 | CheckBoxField | — |  |  |
| billtype | 是否单据类型 | CheckBoxField | — |  |  |
| workflow | 是否工作流 | CheckBoxField | — |  |  |
| botp | 单据转换 | CheckBoxField | — |  |  |
| isqinganalysis | 支持轻分析 | CheckBoxField | — |  |  |
| voucher | 是否凭证 | CheckBoxField | — |  |  |
| enableimport | 允许导入导出 | CheckBoxField | — |  |  |
| isprint | 支持打印 | CheckBoxField | — |  |  |
| tablename | 主表格 | TextField | — |  |  |
| pkfieldname | 主键字段名 | TextField | — |  |  |
| pkfieldtype | 主键字段类型 | IntegerField | — |  |  |
| enablenameversion | 支持名称版本化 | CheckBoxField | — |  |  |

