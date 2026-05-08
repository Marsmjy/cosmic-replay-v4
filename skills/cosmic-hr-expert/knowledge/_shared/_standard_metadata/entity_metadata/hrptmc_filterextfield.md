# hrptmc_filterextfield — 筛选器二开插件字段

**表单编码**: `hrptmc_filterextfield`  
**表单ID**: `41=XN+JVEM8W`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_filterextfield（筛选器二开插件字段） [BaseEntity]

- **数据库表**: `t_hrptmc_filterextfield`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 字段编码 | TextField | fnumber |  |  |
| report | 报表 | BasedataField | freportid |  | hrptmc_reportmanage |
| issyspreset | 系统预置 | CheckBoxField | fissyspreset |  |  |
| ismust | 是否必填 | CheckBoxField | fismust |  |  |

