# hrpi_assignmentf7query — 组织分配

**表单编码**: `hrpi_assignmentf7query`  
**表单ID**: `4S57FXQUOYX5`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hrpi_assignmentf7query（组织分配） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 列表展示列（按序号排列）

| # | 字段路径 | 列标题 | 可见 |
|---|---------|--------|------|
| 1 | `employee.headsculpture` | 头像 | ✓ |
| 2 | `employee.name` | 姓名 | ✓ |
| 3 | `employee.empnumber` | 工号 | ✓ |
| 4 | `org.name` | HR管理组织 | （默认隐藏） |
| 5 | `adminorg.name` | 管理部门 | （默认隐藏） |
| 6 | `persongroup.name` | 员工组 | （默认隐藏） |
| 7 | `country.name` | 国家/地区 | （默认隐藏） |
| 8 | `number` | 组织分配号 | （默认隐藏） |
| 9 | `businesstype.name` | 业务类型 | （默认隐藏） |
| 11 | `primaryassignment.number` | 主组织分配号 | （默认隐藏） |
| 12 | `assignmentstatus` | 组织分配状态 | ✓ |
| 13 | `hrpi_empposorgrel.company.name` | 所属公司 | ✓ |
| 14 | `hrpi_empposorgrel.adminorg.name` | 行政组织 | ✓ |
| 15 | `hrpi_empposorgrel.position.name` | 岗位 | ✓ |
| 16 | `hrpi_empposorgrel.job.name` | 职位 | ✓ |
| 17 | `hrpi_empposorgrel.startdate` | 开始日期 | （默认隐藏） |
| 18 | `hrpi_empposorgrel.enddate` | 结束日期 | （默认隐藏） |
| 19 | `hrpi_empentrel.laborreltype.name` | 用工关系类型 | ✓ |
| 20 | `hrpi_empentrel.laborrelstatus.name` | 用工关系状态 | ✓ |
| 21 | `hrpi_empentrel.entrydate` | 入职日期 | ✓ |
| 22 | `hrpi_terminationinfo.departdate` | 离职日期 | （默认隐藏） |
| 999 | `fseq` | 序号 | ✓ |

### 涉及的关联实体（来自展示列）

| 实体 | 引用的字段路径 |
|------|--------------|
| `employee` | `employee.headsculpture`，`employee.name`，`employee.empnumber` |
| `org` | `org.name` |
| `adminorg` | `adminorg.name` |
| `persongroup` | `persongroup.name` |
| `country` | `country.name` |
| `（主实体）` | `number`，`assignmentstatus`，`fseq` |
| `businesstype` | `businesstype.name` |
| `primaryassignment` | `primaryassignment.number` |
| `hrpi_empposorgrel` | `hrpi_empposorgrel.company.name`，`hrpi_empposorgrel.adminorg.name`，`hrpi_empposorgrel.position.name`，`hrpi_empposorgrel.job.name`，`hrpi_empposorgrel.startdate`，`hrpi_empposorgrel.enddate` |
| `hrpi_empentrel` | `hrpi_empentrel.laborreltype.name`，`hrpi_empentrel.laborrelstatus.name`，`hrpi_empentrel.entrydate` |
| `hrpi_terminationinfo` | `hrpi_terminationinfo.departdate` |

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `org` | 人事管理组织 |
| `org.number` | 人事管理组织.编码 |
| `org.name` | 人事管理组织.名称 |
| `businesstype` | 业务类型 |
| `businesstype.number` | 业务类型.编码 |
| `businesstype.name` | 业务类型.名称 |
| `adminorg` | 管理部门（挂靠行政组织） |
| `adminorg.number` | 管理部门（挂靠行政组织）.行政组织编码 |
| `adminorg.name` | 管理部门（挂靠行政组织）.行政组织名称 |
| `adminorg.boid` | 管理部门（挂靠行政组织）.业务ID |
| `persongroup` | 人事人员组 |
| `persongroup.number` | 人事人员组.编码 |
| `persongroup.name` | 人事人员组.名称 |
| `enable` | 状态 |
| `employee` | 员工 |
| `employee.oldempnumber` | 员工.前工号 |
| `hrpi_empposorgrel.company` | 任职经历基础页面.所属公司 |
| `hrpi_empposorgrel.company.number` | 任职经历基础页面.所属公司.行政组织编码 |
| `hrpi_empposorgrel.company.name` | 任职经历基础页面.所属公司.行政组织名称 |
| `hrpi_empposorgrel.company.boid` | 任职经历基础页面.所属公司.业务ID |
| `hrpi_empposorgrel.company.isvirtualorg` | 任职经历基础页面.所属公司.是否虚拟组织 |
| `hrpi_empposorgrel.position` | 任职经历基础页面.岗位 |
| `hrpi_empposorgrel.position.number` | 任职经历基础页面.岗位.岗位编码 |
| `hrpi_empposorgrel.position.name` | 任职经历基础页面.岗位.岗位名称 |
| `hrpi_empposorgrel.position.boid` | 任职经历基础页面.岗位.业务ID |
| `hrpi_empposorgrel.position.adminorg` | 任职经历基础页面.岗位.行政组织 |
| `hrpi_empposorgrel.position.adminorg.number` | 任职经历基础页面.岗位.行政组织.行政组织编码 |
| `hrpi_empposorgrel.position.adminorg.name` | 任职经历基础页面.岗位.行政组织.行政组织名称 |
| `hrpi_empposorgrel.position.isleader` | 任职经历基础页面.岗位.是否主负责岗 |
| `hrpi_empposorgrel.adminorg` | 任职经历基础页面.行政组织 |
| `hrpi_empposorgrel.adminorg.number` | 任职经历基础页面.行政组织.行政组织编码 |
| `hrpi_empposorgrel.adminorg.name` | 任职经历基础页面.行政组织.行政组织名称 |
| `hrpi_empposorgrel.adminorg.boid` | 任职经历基础页面.行政组织.业务ID |
| `hrpi_empposorgrel.adminorg.isvirtualorg` | 任职经历基础页面.行政组织.是否虚拟组织 |
| `hrpi_empposorgrel.job` | 任职经历基础页面.职位 |
| `hrpi_empposorgrel.job.number` | 任职经历基础页面.职位.职位编码 |
| `hrpi_empposorgrel.job.name` | 任职经历基础页面.职位.职位名称 |
| `hrpi_empposorgrel.job.boid` | 任职经历基础页面.职位.业务ID |
| `hrpi_empentrel.laborreltype` | 雇佣信息.用工关系类型 |
| `hrpi_empentrel.laborreltype.number` | 雇佣信息.用工关系类型.编码 |
| `hrpi_empentrel.laborreltype.name` | 雇佣信息.用工关系类型.名称 |
| `hrpi_empentrel.laborreltype.laborreltypecls` | 雇佣信息.用工关系类型.用工关系类型分类 |
| `hrpi_empentrel.laborreltype.laborreltypecls.number` | 雇佣信息.用工关系类型.用工关系类型分类.编码 |
| `hrpi_empentrel.laborreltype.laborreltypecls.name` | 雇佣信息.用工关系类型.用工关系类型分类.名称 |
| `hrpi_empentrel.laborrelstatus` | 雇佣信息.用工关系状态 |
| `hrpi_empentrel.laborrelstatus.number` | 雇佣信息.用工关系状态.编码 |
| `hrpi_empentrel.laborrelstatus.name` | 雇佣信息.用工关系状态.名称 |
| `hrpi_empentrel.laborrelstatus.labrelstatuscls` | 雇佣信息.用工关系状态.用工状态分类 |
| `hrpi_empentrel.laborrelstatus.labrelstatuscls.number` | 雇佣信息.用工关系状态.用工状态分类.编码 |
| `hrpi_empentrel.laborrelstatus.labrelstatuscls.name` | 雇佣信息.用工关系状态.用工状态分类.名称 |
| `hrpi_empentrel.laborrelstatus.labrelstatusprd` | 雇佣信息.用工关系状态.用工关系阶段 |
| `hrpi_empentrel.laborrelstatus.labrelstatusprd.number` | 雇佣信息.用工关系状态.用工关系阶段.编码 |
| `hrpi_empentrel.laborrelstatus.labrelstatusprd.name` | 雇佣信息.用工关系状态.用工关系阶段.名称 |
| `hrpi_empentrel.laborrelstatus.laborreltype` | 雇佣信息.用工关系状态.用工关系类型 |
| `hrpi_empentrel.laborrelstatus.laborreltype.number` | 雇佣信息.用工关系状态.用工关系类型.编码 |
| `hrpi_empentrel.laborrelstatus.laborreltype.name` | 雇佣信息.用工关系状态.用工关系类型.名称 |
| `hrpi_empentrel.laborrelstatus.laborreltypecls` | 雇佣信息.用工关系状态.用工关系类型分类 |
| `hrpi_empentrel.laborrelstatus.laborreltypecls.number` | 雇佣信息.用工关系状态.用工关系类型分类.编码 |
| `hrpi_empentrel.laborrelstatus.laborreltypecls.name` | 雇佣信息.用工关系状态.用工关系类型分类.名称 |
| `employee.empnumber` | 员工.工号 |
| `employee.name` | 员工.姓名 |
| `employee.headsculpture` | 员工.头像 |
| `hrpi_empentrel.ishired` | 雇佣信息.是否在职 |
| `adminorg.firstbsed` | 管理部门.最早生效日期 |
| `country` | 国家/地区 |
| `country.number` | 国家/地区.编码 |
| `country.name` | 国家/地区.名称 |
| `country.fullname` | 国家/地区.全称 |
| `primaryassignment` | 主组织分配 |
| `primaryassignment.number` | 主组织分配.组织分配号 |
| `primaryassignment.isprimary` | 主组织分配.是否主组织分配 |
| `hrpi_empposorgrel.startdate` | 任职经历基础页面.开始日期 |
| `hrpi_empposorgrel.enddate` | 任职经历基础页面.结束日期 |
| `hrpi_empposorgrel.company.establishmentdate` | 任职经历基础页面.所属公司.成立日期 |
| `hrpi_empposorgrel.company.companyarea` | 任职经历基础页面.所属公司.国家/地区 |
| `hrpi_empposorgrel.company.companyarea.number` | 任职经历基础页面.所属公司.国家/地区.编码 |
| `hrpi_empposorgrel.company.companyarea.name` | 任职经历基础页面.所属公司.国家/地区.名称 |
| `hrpi_empposorgrel.company.companyarea.fullname` | 任职经历基础页面.所属公司.国家/地区.全称 |
| `hrpi_empposorgrel.company.structnumber` | 任职经历基础页面.所属公司.组织上下级关系编码 |
| `hrpi_empposorgrel.company.org` | 任职经历基础页面.所属公司.组织体系管理组织 |
| `hrpi_empposorgrel.company.org.number` | 任职经历基础页面.所属公司.组织体系管理组织.编码 |
| `hrpi_empposorgrel.company.org.name` | 任职经历基础页面.所属公司.组织体系管理组织.名称 |
| `hrpi_empposorgrel.company.structlongnumber` | 任职经历基础页面.所属公司.组织上下级结构长编码 |
| `hrpi_empposorgrel.company.company` | 任职经历基础页面.所属公司.所属公司 |
| `hrpi_empposorgrel.company.company.number` | 任职经历基础页面.所属公司.所属公司.行政组织编码 |
| `hrpi_empposorgrel.company.company.name` | 任职经历基础页面.所属公司.所属公司.行政组织名称 |
| `hrpi_empposorgrel.position.firstbsed` | 任职经历基础页面.岗位.最早生效日期 |
| `hrpi_empposorgrel.position.job` | 任职经历基础页面.岗位.职位 |
| `hrpi_empposorgrel.position.job.number` | 任职经历基础页面.岗位.职位.职位编码 |
| `hrpi_empposorgrel.position.job.name` | 任职经历基础页面.岗位.职位.职位名称 |
| `hrpi_empposorgrel.adminorg.establishmentdate` | 任职经历基础页面.行政组织.成立日期 |
| `hrpi_empposorgrel.adminorg.companyarea` | 任职经历基础页面.行政组织.国家/地区 |
| `hrpi_empposorgrel.adminorg.companyarea.number` | 任职经历基础页面.行政组织.国家/地区.编码 |
| `hrpi_empposorgrel.adminorg.companyarea.name` | 任职经历基础页面.行政组织.国家/地区.名称 |
| `hrpi_empposorgrel.adminorg.companyarea.fullname` | 任职经历基础页面.行政组织.国家/地区.全称 |
| `hrpi_empposorgrel.adminorg.structnumber` | 任职经历基础页面.行政组织.组织上下级关系编码 |
| `hrpi_empposorgrel.adminorg.org` | 任职经历基础页面.行政组织.组织体系管理组织 |
| `hrpi_empposorgrel.adminorg.org.number` | 任职经历基础页面.行政组织.组织体系管理组织.编码 |
| `hrpi_empposorgrel.adminorg.org.name` | 任职经历基础页面.行政组织.组织体系管理组织.名称 |
| `hrpi_empposorgrel.adminorg.structlongnumber` | 任职经历基础页面.行政组织.组织上下级结构长编码 |
| `hrpi_empposorgrel.adminorg.company` | 任职经历基础页面.行政组织.所属公司 |
| `hrpi_empposorgrel.adminorg.company.number` | 任职经历基础页面.行政组织.所属公司.行政组织编码 |
| `hrpi_empposorgrel.adminorg.company.name` | 任职经历基础页面.行政组织.所属公司.行政组织名称 |
| `hrpi_empposorgrel.job.firstbsed` | 任职经历基础页面.职位.最早生效日期 |
| `hrpi_empentrel.laborrelstatus.ishired` | 雇佣信息.用工关系状态.是否在职 |
| `hrpi_empentrel.entrydate` | 雇佣信息.入职日期 |
| `hrpi_terminationinfo.departdate` | 离职信息.离职日期 |
| `number` | 组织分配号 |
| `isprimary` | 是否主组织分配 |
| `isdeleted` | 已删除 |
| `hrpi_empposorgrel.isdeleted` | 任职经历基础页面.已删除 |
| `hrpi_empposorgrel.isseqlatestrecord` | 任职经历基础页面.最新任职记录 |
| `assignmentstatus` | 组织分配状态 |
| `hrpi_empposorgrel.islatestrecord` | 任职经历基础页面.退出组织分配前最新记录 |
| `hrpi_empposorgrel.isquitassignment` | 任职经历基础页面.退出组织分配后记录 |
| `employee.bsed` | 员工.生效日期 |
| `hrpi_empposorgrel.isprimary` | 任职经历基础页面.主任职 |
| `employee.assignment` | 员工.组织分配 |
| `employee.assignment.number` | 员工.组织分配.组织分配号 |
| `sortcode` | 排序码 |

---

> **统计**: 展示列 22 个，可查询字段 120 个

