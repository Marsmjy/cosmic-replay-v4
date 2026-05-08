# hrpi_assignment — 组织分配

**表单编码**: `hrpi_assignment`  
**表单ID**: `4S28Q83PUQOP`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_assignment（组织分配） [BaseEntity]

- **数据库表**: `t_hrpi_assignment`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| number | 组织分配号 | TextField | fnumber | ✓ |  |
| org | 人事管理组织 | OrgField | forgid | ✓ | bos_org |
| orgtype | 组织分类 | BasedataField | forgtypeid | ✓ | haos_otclassify |
| businesstype | 业务类型 | BasedataField | fbusinesstypeid | ✓ | hbss_bussinessfield |
| adminorg | 管理部门 | HRAdminOrgField | fadminorgid | ✓ | haos_adminorghrf7 |
| persongroup | 员工组 | BasedataField | fpersongroupid |  | hbss_employeegroup |
| employee | 员工 | BasedataField | femployeeid | ✓ | hrpi_employee |
| primaryassignment | 主组织分配 | BasedataField | fprimaryassignmentid |  | hrpi_assignment |
| isprimary | 主组织分配 | CheckBoxField | fisprimary |  |  |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| assignmentstatus | 组织分配状态 | ComboField | fassignmentstatus |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| isdeleted | 已删除 | CheckBoxField | fisdeleted |  |  |
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enable | 状态 | BillStatusField | fenable |  |  |
| country | 国家/地区 | BasedataField | fcountryid |  | bd_country |
| sortcode | 排序码 | TextField | fsortcode |  |  |

