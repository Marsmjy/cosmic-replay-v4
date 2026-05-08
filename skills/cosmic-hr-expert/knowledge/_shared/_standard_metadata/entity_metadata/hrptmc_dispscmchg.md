# hrptmc_dispscmchg — 显示方案变更通知

**表单编码**: `hrptmc_dispscmchg`  
**表单ID**: `3584CQX1Y8U/`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_dispscmchg（显示方案变更通知） [BaseEntity]

- **数据库表**: `t_hrptmc_dispscmchg`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| rptmanage | 报表ID | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| known | 用户已知 | CheckBoxField | fknown |  |  |
| state | 方案状态 | ComboField | fstate |  |  |
| workrpt | 工作表 | BasedataField | fworkrptid |  | hrptmc_workreport |

