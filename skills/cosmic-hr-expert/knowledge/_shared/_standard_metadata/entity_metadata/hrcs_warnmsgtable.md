# hrcs_warnmsgtable — 预警消息表格

**表单编码**: `hrcs_warnmsgtable`  
**表单ID**: `441TZ6+6DESK`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnmsgtable（预警消息表格） [BaseEntity]

- **数据库表**: `t_hrcs_warnmsgtable`  

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
| titleline | 标题行 | TextField | ftitleline |  |  |
| dataline | 数据行 | TextField | fdataline |  |  |
| seqrule | 排序规则 | ComboField | fseqrule |  |  |
| fieldseq | 字段顺序 | IntegerField | fseq |  |  |
| sourcetype | 来源类型 | TextField | fsourcetype |  |  |

