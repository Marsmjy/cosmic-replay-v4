# haos_personchangeevent — 员工变动活动

**表单编码**: `haos_personchangeevent`  
**表单ID**: `2NQSOD1QSC34`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_personchangeevent（员工变动活动） [BaseEntity]

- **数据库表**: `t_haos_perchangeevent`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| staffperson | 占编员工信息 | BasedataField | fid |  | haos_personstaffinfo |
| orgusestaffdetail | 组织占编明细 | BasedataField | fuid |  | haos_orgusestaffdetail |
| eventtype | 活动类型 | BasedataField | feventtypeid |  | haos_staffactivitytype |
| changeevent | 变动活动 | BasedataField | fchangeeventid |  | haos_staffactivity |
| effdt | 生效时间 | DateField | feffdt |  |  |
| perevent | 关联活动 | BigIntField | fpereventid |  |  |

