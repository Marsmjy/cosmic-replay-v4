# hrcs_warnmonthevent — 每月最后一天冲突事务表

**表单编码**: `hrcs_warnmonthevent`  
**表单ID**: `4/9NNLS3BCG=`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_warnmonthevent（每月最后一天冲突事务表） [BaseEntity]

- **数据库表**: `t_hrcs_warnmonthevent`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| schemeid | 预警方案id | BigIntField | fschemeid |  |  |
| executedate | 执行日期 | DateField | fexecutedate |  |  |
| executed | 是否已执行 | CheckBoxField | fexecuted |  |  |

