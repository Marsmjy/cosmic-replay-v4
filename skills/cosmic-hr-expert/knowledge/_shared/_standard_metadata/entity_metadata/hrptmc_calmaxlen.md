# hrptmc_calmaxlen — 计算字段最大长度配置表

**表单编码**: `hrptmc_calmaxlen`  
**表单ID**: `45A5QS4PVR70`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_calmaxlen（计算字段最大长度配置表） [BaseEntity]

- **数据库表**: `t_hrptmc_calmaxlen`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| anobj | 分析对象 | BasedataField | fanobjid | ✓ | hrptmc_analyseobject |
| calfield | 计算字段 | BasedataField | fcalfieldid | ✓ | hrptmc_calculatefield |
| maxlen | 文本最大长度 | IntegerField | fmaxlen | ✓ |  |

