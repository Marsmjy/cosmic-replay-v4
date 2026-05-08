# hbss_hrbuviewquery — HR业务管理视图

**表单编码**: `hbss_hrbuviewquery`  
**表单ID**: `0HL=5H+U=WFI`  
**归属**: HR基础服务云 / HR基础服务  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## hbss_hrbuviewquery（HR业务管理视图） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `number` | 编码 |
| `name` | 名称 |
| `org` | 组织 |
| `org.number` | 组织.编码 |
| `org.name` | 组织.名称 |
| `org.fcomment` | 组织.描述 |
| `parent` | 上级组织 |
| `parent.number` | 上级组织.编码 |
| `parent.fcomment` | 上级组织.描述 |
| `view` | 组织视图 |
| `view.number` | 组织视图.编码 |
| `view.name` | 组织视图.名称 |
| `view.status` | 组织视图.数据状态 |
| `view.isdefault` | 组织视图.默认 |
| `bos_org.number` | 业务单元.编码 |
| `bos_org.name` | 业务单元.名称 |
| `bos_org.status` | 业务单元.数据状态 |
| `bos_org.enable` | 业务单元.使用状态 |
| `bos_org.fishr` | 业务单元.HR组织 |
| `bos_org.simplename` | 业务单元.简称 |
| `bos_org.parent.number` | 业务单元.上级业务单元.编码 |
| `bos_org.parent.name` | 业务单元.上级业务单元.名称 |
| `isfreeze` | 封存 |
| `bos_org.country.name` | 业务单元.国家/地区.名称 |

---

> **统计**: 展示列 0 个，可查询字段 24 个

