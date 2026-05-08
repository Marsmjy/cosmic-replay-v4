# hrptmc_rptcomref — 报表通用排序关系

**表单编码**: `hrptmc_rptcomref`  
**表单ID**: `3984N=JX=PES`  
**归属**: HR基础服务云 / HR报表管理中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrptmc_rptcomref（报表通用排序关系） [BaseEntity]

- **数据库表**: `t_hrptmc_rptcomref`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| rptmanage | 报表管理 | BasedataField | frptmanageid |  | hrptmc_reportmanage |
| commonsort | 通用排序 | BasedataField | fcommonsortid |  | hrptmc_commonsort |
| workrpt | 工作表 | BasedataField | fworkrptid |  | hrptmc_workreport |

