# hrpi_employeenewf7query — 员工

**表单编码**: `hrpi_employeenewf7query`  
**表单ID**: `4UR=Z37J/ADF`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hrpi_employeenewf7query（员工） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 列表展示列（按序号排列）

| # | 字段路径 | 列标题 | 可见 |
|---|---------|--------|------|
| 1 | `headsculpture` | 头像 | ✓ |
| 2 | `name` | 姓名 | ✓ |
| 3 | `empnumber` | 工号 | ✓ |
| 4 | `hrpi_empposorgrel.company.name` | 所属公司 | ✓ |
| 5 | `hrpi_empposorgrel.adminorg.name` | 行政组织 | ✓ |
| 6 | `hrpi_empposorgrel.position.name` | 岗位 | ✓ |
| 7 | `hrpi_empposorgrel.job.name` | 职位 | ✓ |
| 8 | `hrpi_empentrel.laborreltype.name` | 用工关系类型 | ✓ |
| 9 | `hrpi_empentrel.laborrelstatus.name` | 用工关系状态 | ✓ |
| 10 | `hrpi_assignment.assignmentstatus` | 组织分配状态 | ✓ |
| 11 | `hrpi_empentrel.entrydate` | 入职日期 | ✓ |
| 12 | `hrpi_terminationinfo.departdate` | 离职日期 | （默认隐藏） |
| 13 | `hrpi_assignment.org.name` | HR管理组织 | （默认隐藏） |
| 14 | `hrpi_assignment.adminorg.name` | 管理部门 | （默认隐藏） |
| 15 | `hrpi_assignment.persongroup.name` | 员工组 | （默认隐藏） |
| 16 | `createtime` | 创建时间 | ✓ |
| 17 | `hrpi_assignment.sortcode` | 排序码 | ✓ |
| 999 | `fseq` | 序号 | ✓ |

### 涉及的关联实体（来自展示列）

| 实体 | 引用的字段路径 |
|------|--------------|
| `（主实体）` | `headsculpture`，`name`，`empnumber`，`createtime`，`fseq` |
| `hrpi_empposorgrel` | `hrpi_empposorgrel.company.name`，`hrpi_empposorgrel.adminorg.name`，`hrpi_empposorgrel.position.name`，`hrpi_empposorgrel.job.name` |
| `hrpi_empentrel` | `hrpi_empentrel.laborreltype.name`，`hrpi_empentrel.laborrelstatus.name`，`hrpi_empentrel.entrydate` |
| `hrpi_assignment` | `hrpi_assignment.assignmentstatus`，`hrpi_assignment.org.name`，`hrpi_assignment.adminorg.name`，`hrpi_assignment.persongroup.name`，`hrpi_assignment.sortcode` |
| `hrpi_terminationinfo` | `hrpi_terminationinfo.departdate` |

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `empnumber` | 工号 |
| `name` | 姓名 |
| `headsculpture` | 头像 |
| `hrpi_empposorgrel.company` | 任职经历.所属公司 |
| `hrpi_empposorgrel.company.number` | 任职经历.所属公司.行政组织编码 |
| `hrpi_empposorgrel.company.name` | 任职经历.所属公司.行政组织名称 |
| `hrpi_empposorgrel.company.boid` | 任职经历.所属公司.业务ID |
| `hrpi_empposorgrel.company.isvirtualorg` | 任职经历.所属公司.是否虚拟组织 |
| `hrpi_empposorgrel.position` | 任职经历.岗位 |
| `hrpi_empposorgrel.position.number` | 任职经历.岗位.岗位编码 |
| `hrpi_empposorgrel.position.name` | 任职经历.岗位.岗位名称 |
| `hrpi_empposorgrel.position.boid` | 任职经历.岗位.业务ID |
| `hrpi_empposorgrel.position.adminorg` | 任职经历.岗位.行政组织 |
| `hrpi_empposorgrel.position.adminorg.number` | 任职经历.岗位.行政组织.行政组织编码 |
| `hrpi_empposorgrel.position.adminorg.name` | 任职经历.岗位.行政组织.行政组织名称 |
| `hrpi_empposorgrel.position.isleader` | 任职经历.岗位.是否主负责岗 |
| `hrpi_empposorgrel.adminorg` | 任职经历.行政组织 |
| `hrpi_empposorgrel.adminorg.number` | 任职经历.行政组织.行政组织编码 |
| `hrpi_empposorgrel.adminorg.name` | 任职经历.行政组织.行政组织名称 |
| `hrpi_empposorgrel.adminorg.boid` | 任职经历.行政组织.业务ID |
| `hrpi_empposorgrel.adminorg.isvirtualorg` | 任职经历.行政组织.是否虚拟组织 |
| `hrpi_empentrel.startdate` | 雇佣信息.开始日期 |
| `hrpi_empentrel.enddate` | 雇佣信息.结束日期 |
| `hrpi_empentrel.laborreltype` | 雇佣信息.用工关系类型 |
| `hrpi_empentrel.laborreltype.number` | 雇佣信息.用工关系类型.编码 |
| `hrpi_empentrel.laborreltype.name` | 雇佣信息.用工关系类型.名称 |
| `hrpi_empentrel.laborreltype.laborreltypecls` | 雇佣信息.用工关系类型.用工关系类型分类 |
| `hrpi_empentrel.laborreltype.laborreltypecls.number` | 雇佣信息.用工关系类型.用工关系类型分类.编码 |
| `hrpi_empentrel.laborreltype.laborreltypecls.name` | 雇佣信息.用工关系类型.用工关系类型分类.名称 |
| `hrpi_empentrel.laborrelstatus` | 雇佣信息.用工关系状态 |
| `hrpi_empentrel.laborrelstatus.number` | 雇佣信息.用工关系状态.编码 |
| `hrpi_empentrel.laborrelstatus.name` | 雇佣信息.用工关系状态.名称 |
| `hrpi_assignment.org` | 组织分配.人事管理组织 |
| `hrpi_assignment.org.number` | 组织分配.人事管理组织.编码 |
| `hrpi_assignment.org.name` | 组织分配.人事管理组织.名称 |
| `hrpi_assignment.adminorg` | 组织分配.管理部门 |
| `hrpi_assignment.adminorg.number` | 组织分配.管理部门.行政组织编码 |
| `hrpi_assignment.adminorg.name` | 组织分配.管理部门.行政组织名称 |
| `hrpi_assignment.adminorg.boid` | 组织分配.管理部门.业务ID |
| `hrpi_assignment.persongroup` | 组织分配.员工组 |
| `hrpi_assignment.persongroup.number` | 组织分配.员工组.编码 |
| `hrpi_assignment.persongroup.name` | 组织分配.员工组.名称 |
| `creator` | 创建人 |
| `creator.number` | 创建人.工号 |
| `creator.name` | 创建人.姓名 |
| `creator.picturefield` | 创建人.人员头像 |
| `createtime` | 创建时间 |
| `modifier` | 修改人 |
| `modifier.number` | 修改人.工号 |
| `modifier.name` | 修改人.姓名 |
| `modifier.picturefield` | 修改人.人员头像 |
| `modifytime` | 修改时间 |
| `initdatasource` | 数据来源 |
| `boid` | 业务ID |
| `iscurrentversion` | 是否当前版本 |
| `datastatus` | 数据版本状态 |
| `sourcevid` | 关联历史版本 |
| `firstbsed` | 最早生效日期 |
| `bsed` | 生效日期 |
| `bsled` | 失效日期 |
| `hisversion` | 版本号 |
| `globalperson` | 全球员工 |
| `globalperson.number` | 全球员工.全球员工唯一标识 |
| `oldempnumber` | 前工号 |
| `number` | 员工系统ID |
| `isprimary` | 主员工标识 |
| `primaryemployee` | 主员工 |
| `primaryemployee.empnumber` | 主员工.工号 |
| `primaryemployee.name` | 主员工.姓名 |
| `procreatstatus` | 生育状况 |
| `procreatstatus.number` | 生育状况.编码 |
| `procreatstatus.name` | 生育状况.名称 |
| `marriagestatus` | 婚姻状况 |
| `marriagestatus.number` | 婚姻状况.编码 |
| `marriagestatus.name` | 婚姻状况.名称 |
| `healthstatus` | 健康状况 |
| `healthstatus.number` | 健康状况.编码 |
| `healthstatus.name` | 健康状况.名称 |
| `childrennumber` | 子女数 |
| `nameen` | 拼音名 |
| `enname` | 英文名 |
| `displayname` | 显示名 |
| `title` | 头衔 |
| `nativelngname` | 本地语言姓名 |
| `formername` | 曾用名 |
| `gender` | 性别 |
| `gender.number` | 性别.编码 |
| `gender.name` | 性别.名称 |
| `nationality` | 国籍 |
| `nationality.number` | 国籍.编码 |
| `nationality.name` | 国籍.名称 |
| `folk` | 民族 |
| `folk.number` | 民族.编码 |
| `folk.name` | 民族.名称 |
| `symbolicanimals` | 生肖 |
| `symbolicanimals.number` | 生肖.编码 |
| `symbolicanimals.name` | 生肖.名称 |
| `constellation` | 星座 |
| `constellation.number` | 星座.编码 |
| `constellation.name` | 星座.名称 |
| `nbloodtype` | 血型 |
| `nbloodtype.number` | 血型.编码 |
| `nbloodtype.name` | 血型.名称 |
| `height` | 身高 |
| `age` | 年龄 |
| `deathdate` | 身故日期 |
| `birthday` | 出生日期 |
| `julianbirthday` | 公历生日 |
| `lunarcalendarbirthday` | 农历生日 |
| `marriageregistdate` | 结婚登记日期 |
| `birthdaytype` | 生日类型 |
| `description` | 描述 |
| `oldemployee` | 前员工 |
| `oldemployee.empnumber` | 前员工.工号 |
| `oldemployee.name` | 前员工.姓名 |
| `sourcesyskey` | 来源系统唯一标识 |
| `hrpi_empposorgrel.startdate` | 任职经历.开始日期 |
| `hrpi_empposorgrel.enddate` | 任职经历.结束日期 |
| `hrpi_empentrel.ishired` | 雇佣信息.是否在职 |
| `hrpi_terminationinfo.departdate` | 离职信息.离职日期 |
| `hrpi_empentrel.entrydate` | 雇佣信息.入职日期 |
| `hrpi_empposorgrel.job` | 任职经历.职位 |
| `hrpi_empposorgrel.job.number` | 任职经历.职位.职位编码 |
| `hrpi_empposorgrel.job.name` | 任职经历.职位.职位名称 |
| `hrpi_empposorgrel.job.boid` | 任职经历.职位.业务ID |
| `hrpi_empposorgrel.job.firstbsed` | 任职经历.职位.最早生效日期 |
| `hrpi_assignment.adminorg.firstbsed` | 组织分配.管理部门.最早生效日期 |
| `isdeleted` | 已删除 |
| `hrpi_empposorgrel.isdeleted` | 任职经历.已删除 |
| `hrpi_empposorgrel.isseqlatestrecord` | 任职经历.最新任职记录 |
| `hrpi_assignment.assignmentstatus` | 组织分配.组织分配状态 |
| `assignment` | 组织分配 |
| `assignment.number` | 组织分配.组织分配号 |
| `assignment.org` | 组织分配.人事管理组织 |
| `assignment.org.number` | 组织分配.人事管理组织.编码 |
| `assignment.org.name` | 组织分配.人事管理组织.名称 |
| `assignment.org.simplename` | 组织分配.人事管理组织.简称 |
| `assignment.adminorg` | 组织分配.管理部门 |
| `assignment.adminorg.number` | 组织分配.管理部门.组织编码 |
| `assignment.adminorg.name` | 组织分配.管理部门.组织名称 |
| `assignment.persongroup` | 组织分配.员工组 |
| `assignment.persongroup.number` | 组织分配.员工组.编码 |
| `assignment.persongroup.name` | 组织分配.员工组.名称 |
| `hrpi_empentrel.istrial` | 雇佣信息.试用 |
| `hrpi_assignment.sortcode` | 组织分配.排序码 |

---

> **统计**: 展示列 18 个，可查询字段 145 个

