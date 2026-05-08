# hrpi_assignmentmag — 组织分配管理主体

**表单编码**: `hrpi_assignmentmag`  
**表单ID**: `4S6F5D1M7PD1`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_assignmentmag（组织分配管理主体） [BaseEntity]

- **数据库表**: `t_hrpi_assignmentmag`  

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
| adminorg | 管理部门 | HRAdminOrgField | fadminorgid | ✓ | haos_adminorghrf7 |
| org | 人事管理组织 | OrgField | forgid | ✓ | bos_org |
| persongroup | 员工组 | BasedataField | fpersongroupid |  | hbss_employeegroup |
| primaryassignment | 主组织分配 | BasedataField | fprimaryassignmentid |  | hrpi_assignment |
| isprimary | 主组织分配 | CheckBoxField | fisprimary |  |  |
| orgtype | 组织分类 | BasedataField | forgtypeid | ✓ | haos_otclassify |
| assignmentstatus | 组织分配状态 | ComboField | fassignmentstatus |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| sortcode | 排序码 | TextField | fsortcode |  |  |
| number | 组织分配号 | TextField | fnumber | ✓ |  |
| businesstype | 业务类型 | BasedataField | fbusinesstype | ✓ | hbss_bussinessfield |

