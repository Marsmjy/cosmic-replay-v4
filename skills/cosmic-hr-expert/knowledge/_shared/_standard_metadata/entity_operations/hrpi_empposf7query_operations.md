# hrpi_empposf7query — 任职经历 操作清单
**继承链**: `hrpi_empposf7query`  **操作数**: 12
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hrpi_empposf7query` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hrpi_empposf7query` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hrpi_empposf7query` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hrpi_empposf7query` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hrpi_empposf7query` | 3 | 2 |
| 6 | `4R39DC6NFRP7` | modify | 修订 | modify | `hrpi_empposf7query` | 0 | 0 |
| 7 | `4RIR/ALATGNY` | delete | 删除 | delete | `hrpi_empposf7query` | 2 | 0 |
| 8 | `4RJA//W+TN+N` | new | 新增 | new | `hrpi_empposf7query` | 0 | 0 |
| 9 | `22=8CU2XYVBU` | delete | 删除 | delete | `hrpi_empposf7query` | 1 | 0 |
| 10 | `2FG5+5T3IMOU` | exportlist | 引出数据（按引入模板） | exportlist | `hrpi_empposf7query` | 0 | 0 |
| 11 | `4S9HVYE=QJ9V` | close | 关闭 | close | `hrpi_empposf7query` | 0 | 0 |
| 12 | `4S9HWOT7JTV3` | returndata | 返回数据 | returndata | `hrpi_empposf7query` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hrpi_empposf7query`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板OP插件 |
| 2 | — | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 3 | 1 | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4R38557==XM/` | 字段值合规性校验 | `hrpi_empposf7query` |
| 2 | MustInputValidation | `22GYV=PCW6A/` | 字段值合规性校验 | `hrpi_empposf7query` |

---
### 7. 删除（delete / Key: delete / oid: 4RIR/ALATGNY）
> 根定义: `hrpi_empposf7query`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板op插件 |
| 2 | 1 | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |

---
### 9. 删除（delete / Key: delete / oid: 22=8CU2XYVBU）
> 根定义: `hrpi_empposf7query`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_empposf7query` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

