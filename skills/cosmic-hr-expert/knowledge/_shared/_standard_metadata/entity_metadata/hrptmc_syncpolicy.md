# hrptmc_syncpolicy — 同步策略

**表单编码**: `hrptmc_syncpolicy`  
**表单ID**: `2X4X6AHE71R=`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_syncpolicy（同步策略） [BaseEntity]

- **数据库表**: `t_hrptmc_syncpolicy`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isstore | 数据是否落地 | ComboField | fisstore | ✓ |  |
| syncstyle | 数据同步方式 | ComboField | fsyncstyle |  |  |
| frequency | 数据同步频率 | ComboField | ffrequency |  |  |
| synctime | 数据同步时间 | ComboField | fsynctime |  |  |
| dependfield | 依赖字段 | TextField | fdependfield |  |  |
| anobj | 分析对象 | BasedataField | fanobjid |  | hrptmc_analyseobject |

