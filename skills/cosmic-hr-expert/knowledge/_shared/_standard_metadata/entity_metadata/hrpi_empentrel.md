# hrpi_empentrel — 雇佣信息

**表单编码**: `hrpi_empentrel`  
**表单ID**: `15BEKB7/1N+G`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_empentrel（雇佣信息） [BaseEntity]

- **数据库表**: `t_hrpi_empentrel`  

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
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| enterprise | 用人单位 | BasedataField | fenterpriseid |  | hbss_enterprise |
| laborreltype | 用工关系类型 | BasedataField | flaborreltypeid | ✓ | hbss_laborreltype |
| laborrelstatus | 用工关系状态 | BasedataField | flaborrelstatusid | ✓ | hbss_laborrelstatus |
| onboardsource | 入职来源 | BasedataField | fonboardsourceid |  | hbss_onboardsource |
| candidate | 候选人id | BasedataField | fcandidateid |  | hcf_candidate |
| entrydate | 入职日期 | DateField | fentrydate | ✓ |  |
| ishired | 在职 | CheckBoxField | fishired |  |  |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| empstagelatestrecord | 雇佣阶段最新记录 | CheckBoxField | fempstagelatestrecord |  |  |
| istrial | 试用 | CheckBoxField | fistrial |  |  |

