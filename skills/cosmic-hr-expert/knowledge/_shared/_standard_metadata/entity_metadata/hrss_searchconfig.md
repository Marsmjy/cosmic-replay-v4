# hrss_searchconfig — 业务搜索页面注册表

**表单编码**: `hrss_searchconfig`  
**表单ID**: `3UA6PZ6J5UTC`  
**归属**: HR基础服务云 / HR智能搜索  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrss_searchconfig（业务搜索页面注册表） [BaseEntity]

- **数据库表**: `t_hrss_searchconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| basedatafield | 业务对象 | BasedataField | fbasedatafield |  | hbp_entityobject |
| number | 编码 | TextField | fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | fname | ✓ |  |

