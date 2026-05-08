# hbpm_chgrecord — 岗位变动明细

**表单编码**: `hbpm_chgrecord`  
**表单ID**: `2W+KC/N25T6Q`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_chgrecord（岗位变动明细） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hbpm_chgrecord` | BaseEntity | 主表 |
| `t_hbpm_chgrecordevt` | EntryEntity | 岗位变动记录 |
| `t_hbpm_chgrecorddetail` | SubEntryEntity | 岗位变动明细 |

### 字段列表 — t_hbpm_chgrecord（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hbpm_chgrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hbpm_chgrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hbpm_chgrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hbpm_chgrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hbpm_chgrecord.finitdatasource |  |  |
| position | 岗位 | HRPositionField | t_hbpm_chgrecord.fpositionid |  | hbpm_positionhrf7 |
| searchchangedate | 变动生效日期 | DateField | — |  |  |
| searchchangereason | 变动原因 | BasedataField | — |  | hbpm_changereason |
| org | 主管责任单位 | OrgField | t_hbpm_chgrecord.forgid |  | bos_org |
| evententry | 岗位变动记录 | EntryEntity | → t_hbpm_chgrecordevt |  |  |

### 字段列表 — t_hbpm_chgrecordevt（岗位变动记录·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| event | 事务ID | BigIntField | t_hbpm_chgrecordevt.feventid |  |  |
| targetposition | 目标岗位 | HRPositionField | t_hbpm_chgrecordevt.ftargetpositionid |  | hbpm_positionhrf7 |
| sourceposition | 关联岗位 | HRPositionField | t_hbpm_chgrecordevt.fsourcepositionid |  | hbpm_positionhrf7 |
| changetype | 变动类型 | BasedataField | t_hbpm_chgrecordevt.fchangetypeid |  | hbpm_changetype |
| changescene | 变动场景 | BasedataField | t_hbpm_chgrecordevt.fchangesceneid |  | hbpm_changescene |
| changereason | 变动原因 | BasedataField | t_hbpm_chgrecordevt.fchangereasonid |  | hbpm_changereason |
| changedate | 变动日期 | DateField | t_hbpm_chgrecordevt.fchangedate |  |  |
| operator | 操作人 | UserField | t_hbpm_chgrecordevt.foperatorid |  | bos_user |
| hisposition | 岗位历史版本ID | HRPositionField | t_hbpm_chgrecordevt.fhispositionid |  | hbpm_positionhrf7 |
| operatetime | 操作时间 | DateTimeField | t_hbpm_chgrecordevt.foperatetime |  |  |
| changedescription | 变动说明 | TextField | t_hbpm_chgrecordevt.fchangedescription |  |  |
| detailentry | 岗位变动明细 | SubEntryEntity | → t_hbpm_chgrecorddetail |  |  |

### 字段列表 — t_hbpm_chgrecorddetail（岗位变动明细·SubEntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| changentity | 变动实体标识 | TextField | t_hbpm_chgrecorddetail.fchangeentity |  |  |
| propkey | 变动实体字段标识 | TextField | t_hbpm_chgrecorddetail.fchangekey |  |  |
| beforevalue | 变动前 | TextField | t_hbpm_chgrecorddetail.fbeforechange |  |  |
| aftervalue | 变动后 | TextField | t_hbpm_chgrecorddetail.fafterchange |  |  |
| changeoperate | 变动操作 | BasedataField | t_hbpm_chgrecorddetail.fchangeoperateid |  | hbpm_changeoperate |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hbpm_chgrecord（主表） | 9 |
| t_hbpm_chgrecordevt（岗位变动记录） | 11 |
| t_hbpm_chgrecorddetail（岗位变动明细） | 5 |
| 无数据库列 | 2 |

