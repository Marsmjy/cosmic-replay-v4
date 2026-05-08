# hrpi_empposorgrelquery — 行政组织下人员清单

**表单编码**: `hrpi_empposorgrelquery`  
**表单ID**: `26NWCKAUHC0E`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hrpi_empposorgrelquery（行政组织下人员清单） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `position` | 岗位 |
| `position.number` | 岗位.岗位编码 |
| `position.name` | 岗位.岗位名称 |
| `isprimary` | 是否主任职 |
| `job` | 职位 |
| `job.number` | 职位.职位编码 |
| `job.name` | 职位.职位名称 |
| `hrpi_empjobrel.joblevel` | 职级职等基础页面.职级 |
| `hrpi_empjobrel.joblevel.number` | 职级职等基础页面.职级.编码 |
| `hrpi_empjobrel.joblevel.name` | 职级职等基础页面.职级.名称 |
| `hrpi_empjobrel.joblevel.joblevelseq` | 职级职等基础页面.职级.职级顺序号 |
| `hrpi_empjobrel.jobgrade` | 职级职等基础页面.职等 |
| `hrpi_empjobrel.jobgrade.number` | 职级职等基础页面.职等.编码 |
| `hrpi_empjobrel.jobgrade.jobgradeseq` | 职级职等基础页面.职等.职等顺序码 |
| `hrpi_empjobrel.jobgrade.name` | 职级职等基础页面.职等.名称 |
| `hrpi_empentrel.laborreltype` | 职业信息基础页面.用工关系类型 |
| `hrpi_empentrel.laborreltype.number` | 职业信息基础页面.用工关系类型.编码 |
| `hrpi_empentrel.laborreltype.name` | 职业信息基础页面.用工关系类型.名称 |
| `hrpi_empentrel.laborreltype.laborreltypecls` | 职业信息基础页面.用工关系类型.用工关系类型分类 |
| `hrpi_empentrel.laborreltype.laborreltypecls.number` | 职业信息基础页面.用工关系类型.用工关系类型分类.编码 |
| `hrpi_empentrel.laborreltype.laborreltypecls.name` | 职业信息基础页面.用工关系类型.用工关系类型分类.名称 |
| `hrpi_empentrel.laborrelstatus` | 职业信息基础页面.用工关系状态 |
| `hrpi_empentrel.laborrelstatus.number` | 职业信息基础页面.用工关系状态.编码 |
| `hrpi_empentrel.laborrelstatus.name` | 职业信息基础页面.用工关系状态.名称 |
| `hrpi_empentrel.startdate` | 职业信息基础页面.用工开始日期 |
| `postype` | 任职类型 |
| `postype.number` | 任职类型.编码 |
| `postype.name` | 任职类型.名称 |
| `posstatus` | 任职状态 |
| `posstatus.number` | 任职状态.编码 |
| `posstatus.name` | 任职状态.名称 |
| `hrpi_employee.oldempnumber` | 员工.前工号 |
| `hrpi_employee.empnumber` | 员工.工号 |
| `hrpi_employee.isprimary` | 员工.主员工标识 |
| `hrpi_employee.name` | 员工.姓名 |
| `hrpi_employee.headsculpture` | 员工.头像 |
| `hrpi_employee.nameen` | 员工.拼音名 |
| `hrpi_employee.gender` | 员工.性别 |
| `hrpi_employee.gender.number` | 员工.性别.编码 |
| `hrpi_employee.gender.name` | 员工.性别.名称 |

---

> **统计**: 展示列 0 个，可查询字段 40 个

