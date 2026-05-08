# hrcs_entitysyncconfig — 实体同步配置

**表单编码**: `hrcs_entitysyncconfig`  
**表单ID**: `1ZKF89/6=FFB`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrcs_entitysyncconfig（实体同步配置） [BaseEntity]

- **数据库表**: `t_hbss_entitysyncconfig`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| entity | 实体 | BasedataField | fentity | ✓ | bos_entityobject |
| entityname | 实体名称 | TextField | — |  |  |
| servicename | 服务名 | TextField | fservicename | ✓ |  |
| methodname | 服务方法名 | TextField | fmethodname | ✓ |  |
| app | 应用 | BasedataField | fapp | ✓ | bos_devportal_bizapp |
| status | 同步状态 | ComboField | fstatus |  |  |
| fixedimportdatatime | 定时任务时间 | DateField | ffixedimportdatatime |  |  |

