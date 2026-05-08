# hrpi_empposf7query — 任职经历

**表单编码**: `hrpi_empposf7query`  
**表单ID**: `4S5P7B1T8H42`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hrpi_empposf7query（任职经历） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 列表展示列（按序号排列）

| # | 字段路径 | 列标题 | 可见 |
|---|---------|--------|------|
| 1 | `employee.headsculpture` | 头像 | ✓ |
| 2 | `employee.name` | 姓名 | ✓ |
| 3 | `employee.empnumber` | 工号 | ✓ |
| 4 | `hrpi_assignment.org.name` | HR管理组织 | （默认隐藏） |
| 5 | `hrpi_assignment.adminorg.name` | 管理部门 | （默认隐藏） |
| 6 | `hrpi_assignment.persongroup.name` | 员工组 | （默认隐藏） |
| 7 | `hrpi_assignment.country.name` | 国家/地区 | （默认隐藏） |
| 8 | `hrpi_assignment.number` | 组织分配号 | （默认隐藏） |
| 9 | `hrpi_assignment.businesstype.name` | 业务类型 | （默认隐藏） |
| 11 | `hrpi_assignment.primaryassignment.number` | 主组织分配号 | （默认隐藏） |
| 12 | `hrpi_assignment.assignmentstatus` | 组织分配状态 | ✓ |
| 13 | `number` | 任职编号 | ✓ |
| 14 | `company.name` | 所属公司 | ✓ |
| 15 | `adminorg.name` | 行政组织 | ✓ |
| 16 | `position.name` | 岗位 | ✓ |
| 17 | `job.name` | 职位 | ✓ |
| 19 | `postype.name` | 任职类型 | ✓ |
| 20 | `posstatus.name` | 任职状态 | ✓ |
| 21 | `startdate` | 开始日期 | ✓ |
| 22 | `enddate` | 结束日期 | ✓ |
| 23 | `hrpi_empentrel.laborreltype.name` | 用工关系类型 | ✓ |
| 24 | `hrpi_empentrel.laborrelstatus.name` | 用工关系状态 | ✓ |
| 25 | `hrpi_empentrel.entrydate` | 入职日期 | ✓ |
| 26 | `hrpi_terminationinfo.departdate` | 离职日期 | （默认隐藏） |
| 999 | `fseq` | 序号 | ✓ |

### 涉及的关联实体（来自展示列）

| 实体 | 引用的字段路径 |
|------|--------------|
| `employee` | `employee.headsculpture`，`employee.name`，`employee.empnumber` |
| `hrpi_assignment` | `hrpi_assignment.org.name`，`hrpi_assignment.adminorg.name`，`hrpi_assignment.persongroup.name`，`hrpi_assignment.country.name`，`hrpi_assignment.number`，`hrpi_assignment.businesstype.name`，`hrpi_assignment.primaryassignment.number`，`hrpi_assignment.assignmentstatus` |
| `（主实体）` | `number`，`startdate`，`enddate`，`fseq` |
| `company` | `company.name` |
| `adminorg` | `adminorg.name` |
| `position` | `position.name` |
| `job` | `job.name` |
| `postype` | `postype.name` |
| `posstatus` | `posstatus.name` |
| `hrpi_empentrel` | `hrpi_empentrel.laborreltype.name`，`hrpi_empentrel.laborrelstatus.name`，`hrpi_empentrel.entrydate` |
| `hrpi_terminationinfo` | `hrpi_terminationinfo.departdate` |

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `employee.name` | 员工.姓名 |
| `employee.empnumber` | 员工.工号 |
| `company` | 所属公司 |
| `company.number` | 所属公司.行政组织编码 |
| `company.name` | 所属公司.行政组织名称 |
| `company.boid` | 所属公司.业务ID |
| `company.isvirtualorg` | 所属公司.是否虚拟组织 |
| `position` | 岗位 |
| `position.number` | 岗位.岗位编码 |
| `position.name` | 岗位.岗位名称 |
| `position.boid` | 岗位.业务ID |
| `position.adminorg` | 岗位.行政组织 |
| `position.adminorg.number` | 岗位.行政组织.行政组织编码 |
| `position.adminorg.name` | 岗位.行政组织.行政组织名称 |
| `position.isleader` | 岗位.是否主负责岗 |
| `adminorg` | 行政组织 |
| `adminorg.number` | 行政组织.行政组织编码 |
| `adminorg.name` | 行政组织.行政组织名称 |
| `adminorg.boid` | 行政组织.业务ID |
| `adminorg.isvirtualorg` | 行政组织.是否虚拟组织 |
| `job` | 职位 |
| `job.number` | 职位.职位编码 |
| `job.name` | 职位.职位名称 |
| `job.boid` | 职位.业务ID |
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
| `hrpi_empentrel.startdate` | 雇佣信息.开始日期 |
| `hrpi_empentrel.enddate` | 雇佣信息.结束日期 |
| `number` | 任职编号 |
| `hrpi_empentrel.employee` | 雇佣信息.员工 |
| `hrpi_empentrel.employee.empnumber` | 雇佣信息.员工.工号 |
| `hrpi_empentrel.employee.name` | 雇佣信息.员工.姓名 |
| `employee.headsculpture` | 员工.头像 |
| `employee` | 员工 |
| `employee.globalperson` | 员工.全球员工 |
| `employee.globalperson.number` | 员工.全球员工.全球员工唯一标识 |
| `assignment.orgtype` | 组织分配.组织分类 |
| `assignment.orgtype.number` | 组织分配.组织分类.编码 |
| `assignment.orgtype.name` | 组织分配.组织分类.名称 |
| `startdate` | 开始日期 |
| `enddate` | 结束日期 |
| `hrpi_empentrel.ishired` | 雇佣信息.是否在职 |
| `hrpi_empentrel.laborrelstatus.ishired` | 雇佣信息.用工关系状态.是否在职 |
| `hrpi_empentrel.entrydate` | 雇佣信息.入职日期 |
| `hrpi_assignment.number` | 组织分配.组织分配号 |
| `hrpi_assignment.org` | 组织分配.人事管理组织 |
| `hrpi_assignment.org.number` | 组织分配.人事管理组织.编码 |
| `hrpi_assignment.org.name` | 组织分配.人事管理组织.名称 |
| `hrpi_assignment.adminorg` | 组织分配.管理部门 |
| `hrpi_assignment.adminorg.number` | 组织分配.管理部门.行政组织编码 |
| `hrpi_assignment.adminorg.name` | 组织分配.管理部门.行政组织名称 |
| `hrpi_assignment.adminorg.boid` | 组织分配.管理部门.业务ID |
| `hrpi_assignment.adminorg.firstbsed` | 组织分配.管理部门.最早生效日期 |
| `hrpi_assignment.persongroup` | 组织分配.员工组 |
| `hrpi_assignment.persongroup.number` | 组织分配.员工组.编码 |
| `hrpi_assignment.persongroup.name` | 组织分配.员工组.名称 |
| `hrpi_assignment.country` | 组织分配.国家/地区 |
| `hrpi_assignment.country.number` | 组织分配.国家/地区.编码 |
| `hrpi_assignment.country.name` | 组织分配.国家/地区.名称 |
| `hrpi_assignment.country.fullname` | 组织分配.国家/地区.全称 |
| `hrpi_assignment.primaryassignment` | 组织分配.主组织分配 |
| `hrpi_assignment.primaryassignment.number` | 组织分配.主组织分配.组织分配号 |
| `hrpi_assignment.primaryassignment.isprimary` | 组织分配.主组织分配.是否主组织分配 |
| `hrpi_assignment.isprimary` | 组织分配.是否主组织分配 |
| `hrpi_terminationinfo.departdate` | 离职信息.离职日期 |
| `company.establishmentdate` | 所属公司.成立日期 |
| `company.companyarea` | 所属公司.国家/地区 |
| `company.companyarea.number` | 所属公司.国家/地区.编码 |
| `company.companyarea.name` | 所属公司.国家/地区.名称 |
| `company.companyarea.fullname` | 所属公司.国家/地区.全称 |
| `company.structnumber` | 所属公司.组织上下级关系编码 |
| `company.org` | 所属公司.组织体系管理组织 |
| `company.org.number` | 所属公司.组织体系管理组织.编码 |
| `company.org.name` | 所属公司.组织体系管理组织.名称 |
| `company.structlongnumber` | 所属公司.组织上下级结构长编码 |
| `company.company` | 所属公司.所属公司 |
| `company.company.number` | 所属公司.所属公司.行政组织编码 |
| `company.company.name` | 所属公司.所属公司.行政组织名称 |
| `position.firstbsed` | 岗位.最早生效日期 |
| `position.job` | 岗位.职位 |
| `position.job.number` | 岗位.职位.职位编码 |
| `position.job.name` | 岗位.职位.职位名称 |
| `postype` | 任职类型 |
| `postype.number` | 任职类型.编码 |
| `postype.name` | 任职类型.名称 |
| `postype.postcategory` | 任职类型.任职类型分类 |
| `postype.postcategory.number` | 任职类型.任职类型分类.编码 |
| `postype.postcategory.name` | 任职类型.任职类型分类.名称 |
| `posstatus` | 任职状态 |
| `posstatus.number` | 任职状态.编码 |
| `posstatus.name` | 任职状态.名称 |
| `posstatus.poststatecls` | 任职状态.任职状态分类 |
| `posstatus.poststatecls.number` | 任职状态.任职状态分类.编码 |
| `posstatus.poststatecls.name` | 任职状态.任职状态分类.名称 |
| `adminorg.establishmentdate` | 行政组织.成立日期 |
| `adminorg.companyarea` | 行政组织.国家/地区 |
| `adminorg.companyarea.number` | 行政组织.国家/地区.编码 |
| `adminorg.companyarea.name` | 行政组织.国家/地区.名称 |
| `adminorg.companyarea.fullname` | 行政组织.国家/地区.全称 |
| `adminorg.structnumber` | 行政组织.组织上下级关系编码 |
| `adminorg.org` | 行政组织.组织体系管理组织 |
| `adminorg.org.number` | 行政组织.组织体系管理组织.编码 |
| `adminorg.org.name` | 行政组织.组织体系管理组织.名称 |
| `adminorg.structlongnumber` | 行政组织.组织上下级结构长编码 |
| `adminorg.company` | 行政组织.所属公司 |
| `adminorg.company.number` | 行政组织.所属公司.行政组织编码 |
| `adminorg.company.name` | 行政组织.所属公司.行政组织名称 |
| `job.firstbsed` | 职位.最早生效日期 |
| `hrpi_assignment.businesstype` | 组织分配.业务类型 |
| `hrpi_assignment.businesstype.number` | 组织分配.业务类型.编码 |
| `hrpi_assignment.businesstype.name` | 组织分配.业务类型.名称 |
| `isprimary` | 是否主任职 |
| `isdeleted` | 已删除 |
| `hrpi_assignment.isdeleted` | 组织分配.已删除 |
| `hrpi_assignment.assignmentstatus` | 组织分配.组织分配状态 |
| `workplace.number` | 常驻工作地.编码 |
| `workplace.name` | 常驻工作地.名称 |
| `contractworkplace.number` | 协议工作地.编码 |
| `contractworkplace.name` | 协议工作地.名称 |
| `islatestrecord` | 退出组织分配前最新记录 |
| `isquitassignment` | 退出组织分配后记录 |
| `employee.bsed` | 员工.生效日期 |
| `isseqlatestrecord` | 最新任职记录 |
| `employee.assignment` | 员工.组织分配 |
| `employee.assignment.number` | 员工.组织分配.组织分配号 |
| `sortcode` | 排序码 |
| `empstage` | 雇佣阶段 |
| `hrpi_assignment.empstage` | 组织分配.雇佣阶段 |

---

> **统计**: 展示列 25 个，可查询字段 147 个

