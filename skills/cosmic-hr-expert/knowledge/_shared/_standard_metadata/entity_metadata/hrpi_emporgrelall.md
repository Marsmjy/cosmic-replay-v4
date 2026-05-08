# hrpi_emporgrelall — 任职经历总表

**表单编码**: `hrpi_emporgrelall`  
**表单ID**: `15D688PCFF12`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_emporgrelall（任职经历总表） [BaseEntity]

- **数据库表**: `t_hrpi_emporgrelall`  

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
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| servicelength | 任职时长 | IntegerField | fservicelength |  |  |
| isprimary | 主任职 | CheckBoxField | fisprimary |  |  |
| isexistprobation | 有考察期 | CheckBoxField | fisexistprobation |  |  |
| isinsystem | 系统内任职 | CheckBoxField | fisinsystem |  |  |
| adminorg | 行政组织 | MuliLangTextField | fadminorg |  |  |
| position | 岗位 | MuliLangTextField | fposition |  |  |
| job | 职位 | MuliLangTextField | fjob |  |  |
| startprobation | 考察开始日期 | DateField | fstartprobation |  |  |
| endprobation | 考察结束日期 | DateField | fendprobation |  |  |
| company | 所属公司 | MuliLangTextField | fcompany |  |  |
| postype | 任职类型 | MuliLangTextField | fpostype |  |  |
| variationtype | 变动类型 | MuliLangTextField | fvariationtype |  |  |
| number | 任职编号 | TextField | fnumber |  |  |
| chgaction | 变动操作 | MuliLangTextField | fchgaction |  |  |
| workplace | 常驻工作地 | MuliLangTextField | fworkplace |  |  |
| contractworkplace | 协议工作地 | MuliLangTextField | fcontractworkplace |  |  |

