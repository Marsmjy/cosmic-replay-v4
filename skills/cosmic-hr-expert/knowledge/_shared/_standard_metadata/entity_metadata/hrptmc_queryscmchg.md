# hrptmc_queryscmchg — 高级查询方案变更通知

**表单编码**: `hrptmc_queryscmchg`  
**表单ID**: `3R=AWTW81Y9W`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_queryscmchg（高级查询方案变更通知） [BaseEntity]

- **数据库表**: `t_hrptmc_queryscmchg`  

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
| scheme | 方案id | BasedataField | fschemeid | ✓ | hrptmc_queryscheme |
| user | 用户 | BasedataField | fuserid |  | bos_user |

