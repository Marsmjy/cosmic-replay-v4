# hrptmc_selparam — 参数选择

**表单编码**: `hrptmc_selparam`  
**表单ID**: `2WMUWTGRQ47M`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_selparam（参数选择） [BaseEntity]

- **数据库表**: `t_hrptmc_busisrvparam`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 参数编码 | TextField | fnumber |  |  |
| name | 参数名称 | MuliLangTextField | fname |  |  |
| type | 参数类型 | ComboField | ftype |  |  |
| isrequired | 是否必传 | CheckBoxField | fisrequired |  |  |
| serviceid | 服务id | BigIntField | fid |  |  |

