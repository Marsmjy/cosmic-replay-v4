# hrpi_contractinfo — 合同信息

**表单编码**: `hrpi_contractinfo`  
**表单ID**: `4V=6+PGXWIIJ`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_contractinfo（合同信息） [BaseEntity]

- **数据库表**: `t_hrpi_contractinfo`  

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
| startdate | 合同开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 实际结束日期 | DateField | fenddate | ✓ |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| number | 合同编号 | TextField | fnumber | ✓ |  |
| periodunit | 合同期限单位 | ComboField | fperiodunit | ✓ |  |
| contractid | 劳动合同id | BigIntField | fcontractid |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| signcompany | 聘用单位 | BasedataField | fsigncompanyid | ✓ | hbss_signcompany |
| signway | 签署方式 | ComboField | fsignway |  |  |
| contractstatus | 合同状态 | ComboField | fcontractstatus |  |  |
| contracttype | 合同类型 | BasedataField | fcontracttypeid | ✓ | hbss_contracttypes |
| planenddate | 计划结束日期 | DateField | fplanenddate | ✓ |  |
| signeddate | 签订日期 | DateField | fsigneddate |  |  |
| period | 合同期限 | DecimalField | fperiod |  |  |
| renewcount | 续签次数 | IntegerField | frenewcount |  |  |
| periodtype | 期限类型 | BasedataField | fperiodtypeid | ✓ | hbss_timelimittype |

