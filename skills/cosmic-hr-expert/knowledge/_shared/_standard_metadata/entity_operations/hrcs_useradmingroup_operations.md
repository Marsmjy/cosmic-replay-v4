# hrcs_useradmingroup — HR管理员 操作清单
**继承链**: `hrcs_useradmingroup`  **操作数**: 10
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `2BFKQSVFD4MF` | donothing | 删除用户 | donothing_remove_user | `hrcs_useradmingroup` | 0 | 0 |
| 2 | `2Y+4RL1G0DOP` | donothing | 添加用户 | donothing_add_user | `hrcs_useradmingroup` | 0 | 0 |
| 3 | `49OFRBIDU1/L` | donothing | 修改HR管理员分组 | donothing_modify_group | `hrcs_useradmingroup` | 0 | 0 |
| 4 | `49OGPDCYX95/` | donothing | 删除HR管理员分组 | donothing_remove_group | `hrcs_useradmingroup` | 0 | 0 |
| 5 | `49OGQW0C7+01` | donothing | 批量授权HR管理员分组 | donothing_batch_perm | `hrcs_useradmingroup` | 0 | 0 |
| 6 | `4=NCHMH3X6EX` | donothing | 新增管理员分组 | donothing_add_group | `hrcs_useradmingroup` | 0 | 0 |
| 7 | `4AMY/WMI8AH6` | refresh | 刷新 | refresh | `hrcs_useradmingroup` | 0 | 0 |
| 8 | `5J/=9KPADFGN` | donothing | 删除HR管理员分组 | do_remove_group | `hrcs_useradmingroup` | 1 | 0 |
| 9 | `5J/=B71/U3QB` | donothing | 添加用户 | do_add_user | `hrcs_useradmingroup` | 1 | 0 |
| 10 | `5J/=DI7X/8LI` | donothing | 删除用户 | do_remove_user | `hrcs_useradmingroup` | 1 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 8. 删除HR管理员分组（donothing / Key: do_remove_group / oid: 5J/=9KPADFGN）
> 根定义: `hrcs_useradmingroup`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_useradmingroup` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp` | kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp |

---
### 9. 添加用户（donothing / Key: do_add_user / oid: 5J/=B71/U3QB）
> 根定义: `hrcs_useradmingroup`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_useradmingroup` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp` | kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp |

---
### 10. 删除用户（donothing / Key: do_remove_user / oid: 5J/=DI7X/8LI）
> 根定义: `hrcs_useradmingroup`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_useradmingroup` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp` | kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp |

