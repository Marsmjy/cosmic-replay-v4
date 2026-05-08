# hrcs_warnmsgconf — 预警消息

**表单编码**: `hrcs_warnmsgconf`  
**表单ID**: `441RN5I4V17K`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnmsgconf（预警消息） [BaseEntity]

- **数据库表**: `t_hrcs_warncontext`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| sourceid | 引用来源ID | BigIntField | fsourceid |  |  |
| localeid | 语言 | TextField | flocaleid |  |  |
| channel | 渠道类型 | TextField | fchannel |  |  |
| msgtitle | 消息标题 | TextField | fmsgtitle |  |  |
| msgmain | 消息正文 | TextField | fmsgmain |  |  |
| msgconclusion | 消息结束语 | TextField | fmsgconclusion |  |  |
| msgtabletitle | 消息表格标题 | TextField | fmsgtabletitle |  |  |
| sourcetype | 来源类型 | TextField | fsourcetype |  |  |

