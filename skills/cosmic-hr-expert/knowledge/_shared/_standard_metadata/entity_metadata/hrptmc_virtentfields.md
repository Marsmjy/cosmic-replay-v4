# hrptmc_virtentfields — 虚拟对象字段

**表单编码**: `hrptmc_virtentfields`  
**表单ID**: `3DO6N+1Y4NP+`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_virtentfields（虚拟对象字段） [BaseEntity]

- **数据库表**: `t_hrptmc_virtentfields`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| fieldname | 名称 | MuliLangTextField | ffieldname | ✓ |  |
| fieldpath | 属性全路径 | TextField | ffieldpath |  |  |
| fieldvaluetype | 字段值类型 | ComboField | ffieldvaluetype |  |  |
| controltype | 控件类型 | ComboField | fcontroltype |  |  |
| complextype | 复杂类型 | ComboField | fcomplextype |  |  |
| iscommonfield | 是否可解析 | CheckBoxField | fiscommonfield |  |  |
| group | 字段分组 | BasedataField | fgroup |  | hrptmc_virtualfieldgroup |
| fieldnumber | 编码 | TextField | ffieldnumber | ✓ |  |

