# hrpi_terminationinfo — 离职信息

**表单编码**: `hrpi_terminationinfo`  
**表单ID**: `4SELNRISIN49`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_terminationinfo（离职信息） [BaseEntity]

- **数据库表**: `t_hrpi_terminationinfo`  

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
| lastworkdate | 最后工作日 | DateField | flastworkdate |  |  |
| departdate | 离职日期 | DateField | fdepartdate | ✓ |  |
| termreasondetail | 离职详情 | TextField | ftermreasondetail |  |  |
| termdestination | 离职去向 | TextField | ftermdestination |  |  |
| termphone | 离职后手机号码 | TelephoneField | ftermphone |  |  |
| termemail | 离职后邮箱 | EmailField | ftermemail |  |  |
| termtype | 离职类型 | BasedataField | ftermtypeid |  | hrpi_quittype |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |

