# hbpm_position_msgdetail — 岗位消息明细

**表单编码**: `hbpm_position_msgdetail`  
**表单ID**: `5+25ENEXGHNU`  
**归属**: HR基础服务云 / HR基础岗位  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hbpm_position_msgdetail（岗位消息明细） [BaseEntity]

- **数据库表**: `t_hbpm_position_msgdetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| changescene | 变动场景 | BasedataField | fchangesceneid |  | hbpm_changescene |
| bo | 岗位bo | HRPositionField | fboid |  | hbpm_positionhrf7 |
| traceid | traceid | TextField | ftraceid |  |  |
| changeoperate | 变动操作 | BasedataField | fchangeoperateid |  | hbpm_changeoperate |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| sendstate | 发送状态 | ComboField | fsendstate |  |  |
| index | 顺序号(同批次) | IntegerField | findex |  |  |
| isstandardpos | 是否标准岗位 | ComboField | fisstandardpos |  |  |
| beforeversion | 变更前版本 | HRPositionField | fbeforeversionid |  | hbpm_positionhrf7 |
| afterversion | 变更后版本 | HRPositionField | fafterversionid |  | hbpm_positionhrf7 |

