# haos_adminorg_msgdetail — 组织消息明细

**表单编码**: `haos_adminorg_msgdetail`  
**表单ID**: `5+27+=YVXO2Z`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: haos_adminorg_msgdetail（组织消息明细） [BaseEntity]

- **数据库表**: `t_haos_adminorg_msgdetail`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bo | 组织当前版本数据 | HRAdminOrgField | fboid |  | haos_adminorghrf7 |
| beforeversion | 变更前版本 | HisModelBasedataField | fbeforeversionid |  | haos_adminorghrf7 |
| afterversion | 变更后版本 | HisModelBasedataField | fafterversionid |  | haos_adminorghrf7 |
| traceid | traceid | TextField | ftraceid |  |  |
| changeoperate | 变动操作 | BasedataField | fchangeoperateid |  | haos_changeoperat |
| changescene | 变动场景 | BasedataField | fchangesceneid |  | haos_changescene |
| sendstate | 发送状态 | ComboField | fsendstate |  |  |
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| isbelongcompanychange | 所属公司是否变化 | CheckBoxField | fisbelongcompanychange |  |  |
| index | 顺序号(同批次) | IntegerField | findex |  |  |

