# hrcs_formmeta — 维度控制

**表单编码**: `hrcs_formmeta`  
**表单ID**: `2EQNYPOFJPZ+`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_formmeta（维度控制） [BaseEntity]

- **数据库表**: ``  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | fname |  |  |
| modeltype | 模型类型 | ComboField | — |  |  |
| inheritpath | 继承路径 | TextField | — |  |  |
| basedatafield | 实体元数据 | BasedataField | — |  | bos_entitymeta |
| parentid | 父对象 | BasedataField | — |  | bos_formmeta |
| type | 表单类型 | ComboField | — |  |  |
| createdate | 创建日期 | DateField | — |  |  |
| modifydate | 修改日期 | DateField | — |  |  |
| modifierid | 修改人id | TextField | — |  |  |
| masterid | 原页面id | TextField | fmasterid |  |  |
| bizappid | 应用id | TextField | — |  |  |
| istemplate | 模板 | TextField | — |  |  |
| isv | 开发商标识 | TextField | — |  |  |
| industry | 行业 | BasedataField | — |  | bos_devp_industry |
| isextended | 报表是否允许扩展 | CheckBoxField | — |  |  |
| enabled | 是否启用 | CheckBoxField | — |  |  |
| version | 版本 | BigIntField | — |  |  |
| isinherit | 是否允许继承 | CheckBoxField | — |  |  |
| subsysid | subsysid | IntegerField | — |  |  |

