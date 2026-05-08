# hrcs_roleinitdemo_fp — 角色初始化字段权限样例

**表单编码**: `hrcs_roleinitdemo_fp`  
**表单ID**: `3G+6O5PIVXOJ`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_roleinitdemo_fp（角色初始化字段权限样例） [BaseEntity]

- **数据库表**: `t_hrcs_roleinitdemo_fp`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sceneid | 场景ID | IntegerField | fsceneid |  |  |
| scenename | 场景名 | MuliLangTextField | fscenename |  |  |
| scenedesc | 场景描述 | MuliLangTextField | fscenedesc |  |  |
| index | 排序号 | IntegerField | findex |  |  |
| rolenumber | 角色编码 | TextField | frolenumber |  |  |
| rolename | 角色名称 | MuliLangTextField | frolename |  |  |
| appnumber | 应用编码 | TextField | fappnumber |  |  |
| appname | 应用名称 | MuliLangTextField | fappname |  |  |
| entitynumber | 业务对象编码 | TextField | fentitynumber |  |  |
| entityname | 业务对象名称 | MuliLangTextField | fentityname |  |  |
| propkey | 字段属性 | TextField | fpropkey |  |  |
| propname | 字段名称 | MuliLangTextField | fpropname |  |  |
| canread | 查看 | MuliLangTextField | fcanread |  |  |
| canwrite | 编辑 | MuliLangTextField | fcanwrite |  |  |

