# haos_adminorgstructquery — 行政组织结构

**表单编码**: `haos_adminorgstructquery`  
**表单ID**: `2P1WK4+OVXU8`  
**归属**: HR基础服务云 / HR基础组织  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## haos_adminorgstructquery（行政组织结构） [查询列表]

> 此元数据为**查询列表**类型，无独立物理表，通过跨实体关联查询聚合展示数据。

### 全部可查询字段（QuerySelectField）

| 字段路径（Alias） | 显示名称 |
|-----------------|---------|
| `level` | 级次 |
| `haos_adminorgdetail.number` | 行政组织查询.行政组织编码 |
| `haos_adminorgdetail.bsed` | 行政组织查询.生效日期 |
| `haos_adminorgdetail.establishmentdate` | 行政组织查询.成立日期 |
| `haos_adminorgdetail.parentorg` | 行政组织查询.上级行政组织 |
| `haos_adminorgdetail.parentorg.number` | 行政组织查询.上级行政组织.行政组织编码 |
| `haos_adminorgdetail.parentorg.boid` | 行政组织查询.上级行政组织.业务id |
| `haos_adminorgdetail.parentorg.datastatus` | 行政组织查询.上级行政组织.数据版本状态 |
| `haos_adminorgdetail.parentorg.sourcevid` | 行政组织查询.上级行政组织.关联历史版本 |
| `haos_adminorgdetail.parentorg.bsed` | 行政组织查询.上级行政组织.生效日期 |
| `haos_adminorgdetail.parentorg.bsled` | 行政组织查询.上级行政组织.业务预计失效日期 |
| `haos_adminorgdetail.parentorg.adminorgtype` | 行政组织查询.上级行政组织.行政组织类型 |
| `haos_adminorgdetail.parentorg.adminorgtype.number` | 行政组织查询.上级行政组织.行政组织类型.编码 |
| `haos_adminorgdetail.parentorg.adminorgtype.name` | 行政组织查询.上级行政组织.行政组织类型.名称 |
| `haos_adminorgdetail.parentorg.belongcompany` | 行政组织查询.上级行政组织.所属公司 |
| `haos_adminorgdetail.parentorg.belongcompany.number` | 行政组织查询.上级行政组织.所属公司.行政组织编码 |
| `haos_adminorgdetail.parentorg.belongcompany.name` | 行政组织查询.上级行政组织.所属公司.行政组织名称 |
| `haos_adminorgdetail.parentorg.org` | 行政组织查询.上级行政组织.主管责任单位 |
| `haos_adminorgdetail.parentorg.org.number` | 行政组织查询.上级行政组织.主管责任单位.编码 |
| `haos_adminorgdetail.parentorg.org.name` | 行政组织查询.上级行政组织.主管责任单位.名称 |
| `haos_adminorgdetail.parentorg.name` | 行政组织查询.上级行政组织.行政组织名称 |
| `haos_adminorgdetail.parentorg.status` | 行政组织查询.上级行政组织.数据状态 |
| `haos_adminorgdetail.parentorg.enable` | 行政组织查询.上级行政组织.业务状态 |
| `haos_adminorgdetail.name` | 行政组织查询.行政组织名称 |
| `haos_adminorgdetail.enable` | 行政组织查询.业务状态 |
| `haos_adminorgdetail.isvirtualorg` | 行政组织查询.是否虚拟组织 |

---

> **统计**: 展示列 0 个，可查询字段 26 个

