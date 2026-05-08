# hbpm_positionrelation — 岗位汇报关系

**表单编码**: `hbpm_positionrelation`  
**表单ID**: `4ST4OBG0Z3J4`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_positionrelation（岗位汇报关系） [BaseEntity]

- **数据库表**: `t_hbpm_positionrelation`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 是否已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 是否当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| role | 岗位 | HRPositionField | fpositionid | ✓ | hbpm_positionhrf7 |
| parent | 协作岗位 | HRPositionField | fparentid | ✓ | hbpm_positionhrf7 |
| reportingtype | 角色协作类型 | BasedataField | freportingtypeid | ✓ | hbpm_reportcoreltype |
| changedescription | 变更说明 | TextField | fchangedescription |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcetype | 来源类型 | BasedataField | fsourceid |  | hbss_rolesourcetype |

