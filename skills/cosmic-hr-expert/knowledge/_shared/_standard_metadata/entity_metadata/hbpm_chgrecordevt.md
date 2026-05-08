# hbpm_chgrecordevt — 岗位变动明细事务分录

**表单编码**: `hbpm_chgrecordevt`  
**表单ID**: `2W/54SML==+1`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_chgrecordevt（岗位变动明细事务分录） [BaseEntity]

- **数据库表**: `t_hbpm_chgrecordevt`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| event | 事务ID | BigIntField | feventid |  |  |
| targetposition | 目标岗位 | HRPositionField | ftargetpositionid |  | hbpm_positionhrf7 |
| sourceposition | 关联岗位 | HRPositionField | fsourcepositionid |  | hbpm_positionhrf7 |
| changetype | 变动类型 | BasedataField | fchangetypeid |  | hbpm_changetype |
| changescene | 变动场景 | BasedataField | fchangesceneid |  | hbpm_changescene |
| changereason | 变动原因 | BasedataField | fchangereasonid |  | hbpm_changereason |
| changedate | 变动日期 | DateField | fchangedate |  |  |
| operator | 操作人 | UserField | foperatorid |  | bos_user |
| parent | 父实体ID | BigIntField | fid |  |  |
| hisposition | 岗位历史版本ID | HRPositionField | fhispositionid |  | hbpm_positionhrf7 |
| operatetime | 操作时间 | DateTimeField | foperatetime |  |  |
| changedescription | 变动说明 | TextField | fchangedescription |  |  |

