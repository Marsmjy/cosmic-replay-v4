# hrcs_activityins — 活动任务实例 操作清单
**继承链**: `hrcs_activityins`  **操作数**: 15
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `2+U=89OKH32S` | new | 新增 | new | `hrcs_activityins` | 0 | 0 |
| 2 | `2+U==YNW3ION` | delete | 删除 | delete | `hrcs_activityins` | 0 | 0 |
| 3 | `2+U=E5264Y1O` | save | 保存 | save | `hrcs_activityins` | 1 | 0 |
| 4 | `2+U=H4ZRGTDF` | close | 关闭 | close | `hrcs_activityins` | 0 | 0 |
| 5 | `2/5AAE0RB=SJ` | donothing | 分配处理人 | assignto | `hrcs_activityins` | 1 | 0 |
| 6 | `2/6GP7XG2YL1` | donothing | 分配历史 | viewrec | `hrcs_activityins` | 0 | 0 |
| 7 | `2/6H0MEGMA1I` | refresh | 刷新 | refresh | `hrcs_activityins` | 0 | 0 |
| 8 | `2/6HMO3W54P=` | export_from_impttpl_hr | 引出 | export_from_impttpl_hr | `hrcs_activityins` | 0 | 0 |
| 9 | `2/BX1PFA8YMK` | donothing | testmservice | testmservice | `hrcs_activityins` | 0 | 0 |
| 10 | `200MU+CZXW+Z` | viewflowchart | 查看流程图 | viewflowchart | `hrcs_activityins` | 0 | 0 |
| 11 | `200YQ1W/GBG1` | donothing | 查看任务流程图 | viewtaskflowchart | `hrcs_activityins` | 0 | 0 |
| 12 | `226LOJE6URM8` | donothing | 同意工作流 | consentwf | `hrcs_activityins` | 0 | 0 |
| 13 | `226LQL/28BGM` | donothing | 驳回工作流 | rejectwf | `hrcs_activityins` | 0 | 0 |
| 14 | `226LU5WKV2Y/` | donothing | 终止工作流 | terminatewf | `hrcs_activityins` | 0 | 0 |
| 15 | `2XVHAQ0GG680` | donothing | 查看日志 | showlog | `hrcs_activityins` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 3. 保存（save / Key: save / oid: 2+U=E5264Y1O）
> 根定义: `hrcs_activityins`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_activityins` | `kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp` | kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp |

---
### 5. 分配处理人（donothing / Key: assignto / oid: 2/5AAE0RB=SJ）
> 根定义: `hrcs_activityins`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_activityins` | `kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp` | 分配处理人操作插件 |

