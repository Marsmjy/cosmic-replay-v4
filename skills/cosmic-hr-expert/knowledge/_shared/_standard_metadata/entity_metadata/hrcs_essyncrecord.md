# hrcs_essyncrecord — 同步记录

**表单编码**: `hrcs_essyncrecord`  
**表单ID**: `2LKA/46VRT52`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_essyncrecord（同步记录） [BaseEntity]

- **数据库表**: `t_hrcs_essyncrecord`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| syncmode | 全量同步方式 | ComboField | fsyncmode | ✓ |  |
| plansynctime | 计划同步时间 | DateTimeField | fplansynctime | ✓ |  |
| status | 同步状态 | ComboField | fstatus |  |  |
| finishedsynctime | 完成同步时间 | DateField | ffinishedsynctime |  |  |
| esscheme | ES同步方案 | BasedataField | fesschemeid |  | hrcs_essyncschemecfig |
| syncdatarange | 同步数据范围 | DateRangeField | — |  |  |
| essynctime | ES同步开始时间 | DateField | fessynctime |  |  |

