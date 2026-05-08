# haos_othrole — 其他形态组织-角色

**表单编码**: `haos_othrole`  
**表单ID**: `5CNUFA5O=0RN`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_othrole（其他形态组织-角色） [BaseEntity]

- **数据库表**: `t_haos_othrole`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 编码 | TextField | fnumber | ✓ |  |
| index | 排序号 | IntegerField | findex |  |  |
| enddate | 失效日期 | DateField | fenddate |  |  |
| businessstatus | 业务状态 | ComboField | fbusinessstatus |  |  |
| startdate | 生效日期 | DateField | fstartdate |  |  |
| adminorg | 所属组织 | BasedataField | fadminorgid | ✓ | haos_othadminorg |
| name | 名称 | MuliLangTextField | fname | ✓ |  |
| sourcetpl | 来源角色模版 | BasedataField | fsourcetplid |  | haos_othroletpl |

