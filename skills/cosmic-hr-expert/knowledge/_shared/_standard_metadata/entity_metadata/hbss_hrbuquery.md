# hbss_hrbuquery — HR管理组织

**表单编码**: `hbss_hrbuquery`  
**表单ID**: `0G/KAHMD8E9X`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hbss_hrbuquery（HR管理组织） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `number` | 编码 |
| `name` | 名称 |
| `status` | 数据状态 |
| `enable` | 使用状态 |
| `fishr` | HR组织 |
| `fcomment` | 描述 |
| `simplename` | 简称 |
| `parent.number` | 上级业务单元.编码 |
| `parent.name` | 上级业务单元.名称 |
| `parent.fishr` | 上级业务单元.HR组织 |
| `bos_org_structure.number` | 组织结构.编码 |
| `bos_org_structure.name` | 组织结构.名称 |
| `bos_org_structure.level` | 组织结构.级次 |
| `bos_org_structure.longnumber` | 组织结构.长编码 |
| `bos_org_structure.fullname` | 组织结构.长名称 |
| `bos_org_structure.org.number` | 组织结构.组织.编码 |
| `bos_org_structure.org.name` | 组织结构.组织.名称 |
| `bos_org_structure.org.fishr` | 组织结构.组织.HR组织 |
| `bos_org_structure.parent.number` | 组织结构.上级组织.编码 |
| `bos_org_structure.parent.name` | 组织结构.上级组织.名称 |
| `bos_org_structure.parent.fishr` | 组织结构.上级组织.HR组织 |
| `bos_org_structure.view` | 组织结构.组织视图 |
| `bos_org_structure.view.number` | 组织结构.组织视图.编码 |
| `bos_org_structure.view.name` | 组织结构.组织视图.名称 |
| `bos_org_structure.ishr` | 组织结构.是否启用HR |
| `bos_org_structure.comment` | 组织结构.描述 |
| `fishrod` | 组织设计业务组织 |
| `fishrop` | 组织绩效业务组织 |
| `fishrbg` | 编制/预算业务组织 |
| `fishrtr` | 人才招聘业务组织 |
| `fishrpa` | 员工人事业务组织 |
| `fishrwt` | 工时考勤业务组织 |
| `fishrcmp` | 薪酬管理业务组织 |
| `fishrbs` | 定调薪业务组织 |
| `fishrpay` | 发薪业务组织 |
| `fishrtax` | 个税业务组织 |
| `fishrsi` | 社保业务组织 |
| `fishrlti` | 长期激励业务组织 |
| `fishrbm` | 奖金业务组织 |
| `fishrtd` | 人才发展业务组织 |
| `fishrtl` | 培训学习业务组织 |
| `fishrip` | 个人绩效业务组织 |
| `fishrssc` | HR共享服务业务组织 |
| `bos_org_structure.enable` | 组织结构.使用状态 |
| `fishrlc` | 人力成本组织 |
| `fishrab` | 假期组织 |
| `bos_org_structure.isfreeze` | 组织结构.封存 |
| `country.name` | 国家/地区.名称 |

---

> **统计**: 展示列 0 个，可查询字段 48 个

