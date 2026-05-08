# hrcs_querydynsourcelist — HR多实体查询配置列表

**表单编码**: `hrcs_querydynsourcelist`  
**表单ID**: `294M5PJ4+20K`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_querydynsourcelist（HR多实体查询配置列表） [BaseEntity]

- **数据库表**: `t_meta_entitydesign`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | fnumber |  |  |
| name | 名称 | MuliLangTextField | FName |  |  |
| modeltype | 类型 | ComboField | fmodeltype |  |  |
| bizappid | 应用 | BasedataField | FBIZAPPID |  | bos_devportal_bizapp |
| isextended | 是否扩展 | CheckBoxField | — |  |  |

