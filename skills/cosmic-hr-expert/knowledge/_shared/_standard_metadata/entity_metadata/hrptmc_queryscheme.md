# hrptmc_queryscheme — 报表高级查询方案

**表单编码**: `hrptmc_queryscheme`  
**表单ID**: `3OOTZ11QRDC9`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_queryscheme（报表高级查询方案） [BaseEntity]

- **数据库表**: `t_hrptmc_queryscheme`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| sharescheme | 是否分享方案 | CheckBoxField | fsharescheme |  |  |
| rptmanage | 方案所属的报表 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| name | 名称 | MuliLangTextField | fname |  |  |
| content | 方案内容 | TextField | fcontent |  |  |
| defaultscheme | 设为默认方案 | CheckBoxField | fdefaultscheme |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| publishtype | 发布类型 | ComboField | fpublishtype |  |  |

