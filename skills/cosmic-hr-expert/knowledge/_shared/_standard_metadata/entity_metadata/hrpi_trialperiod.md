# hrpi_trialperiod — 试用期

**表单编码**: `hrpi_trialperiod`  
**表单ID**: `1HDE8EY//M0A`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_trialperiod（试用期） [BaseEntity]

- **数据库表**: `t_hrpi_trialperiod`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| probation | 试用期长度 | IntegerField | fprobation | ✓ |  |
| probationunit | 试用期单位 | ComboField | fprobationunit | ✓ |  |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| trialstatus | 状态(已废弃) | ComboField | ftrialstatus |  |  |
| preenddate | 预计转正日期 | DateField | fpreenddate | ✓ |  |
| realendate | 实际转正日期 | DateField | frealendate |  |  |
| trialstartdate | 试用开始日期 | DateField | ftrialstartdate | ✓ |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |

