# hrcs_permfilequery — 用户授权 操作清单
**继承链**: `hrcs_permfilequery`  **操作数**: 23
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hrcs_permfilequery` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hrcs_permfilequery` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hrcs_permfilequery` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hrcs_permfilequery` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hrcs_permfilequery` | 1 | 0 |
| 6 | `08OXTO/K399W` | donothing | 生效 | enable | `hrcs_permfilequery` | 1 | 1 |
| 7 | `08OYLI/7K=HT` | donothing | 失效 | disable | `hrcs_permfilequery` | 0 | 0 |
| 8 | `08PL3MTA0124` | delete | 删除 | delete | `hrcs_permfilequery` | 1 | 1 |
| 9 | `08SDQ5AQGQKF` | new | 新增 | new | `hrcs_permfilequery` | 0 | 0 |
| 10 | `0===1LN4JH9F` | disable | 禁用 | disable_new | `hrcs_permfilequery` | 1 | 1 |
| 11 | `0=N=4GD93+4S` | refresh | 刷新 | refresh | `hrcs_permfilequery` | 0 | 0 |
| 12 | `0RPN4=VMMT60` | close | 关闭 | close | `hrcs_permfilequery` | 0 | 0 |
| 13 | `0SFTK/AZ34MN` | modify | 修改 | modify | `hrcs_permfilequery` | 0 | 0 |
| 14 | `0T8BQDZNVFC4` | view | 查看 | view | `hrcs_permfilequery` | 0 | 0 |
| 15 | `0W=F71Z/3YKW` | donothing | 空操作 | donothing | `hrcs_permfilequery` | 0 | 0 |
| 16 | `191OSN1U2N7V` | importdata | 引入数据 | importdata | `hrcs_permfilequery` | 0 | 0 |
| 17 | `2=KMI18KC3I8` | donothing | 分配角色 | assignrole | `hrcs_permfilequery` | 0 | 0 |
| 18 | `2NTTLRNAQAG2` | exportlist | 引出数据（按引入模板） | exportlist | `hrcs_permfilequery` | 0 | 0 |
| 19 | `2OOVG2RZMN2H` | donothing | 按人事档案生成 | syncperm | `hrcs_permfilequery` | 0 | 0 |
| 20 | `34PPA7J1T2TV` | donothing | 初始化用户权限 | inituserperm | `hrcs_permfilequery` | 0 | 0 |
| 21 | `36A79G3GEIMD` | donothing | 引出用户权限 | exportuserperm | `hrcs_permfilequery` | 0 | 0 |
| 22 | `3NIP9UMFFK5S` | donothing | 复制权限 | copyuserperm | `hrcs_permfilequery` | 0 | 0 |
| 23 | `5=U=FWBCKYXP` | donothing | 批量分组 | batchgroup | `hrcs_permfilequery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hrcs_permfilequery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_permfilequery` | `kd.hr.hbss.opplugin.web.PermFilesSaveOp` |  |

---
### 6. 生效（donothing / Key: enable / oid: 08OXTO/K399W）
> 根定义: `hrcs_permfilequery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_permfilequery` | `kd.hr.hbss.opplugin.web.PermFilesSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `08VSPFHHJ4PU` | 合法性校验 | `hrcs_permfilequery` |

---
### 8. 删除（delete / Key: delete / oid: 08PL3MTA0124）
> 根定义: `hrcs_permfilequery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_permfilequery` | `kd.hr.hbss.opplugin.web.PermFilesSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `08VTK+YML3MU` | 合法性校验 | `hrcs_permfilequery` |

---
### 10. 禁用（disable / Key: disable_new / oid: 0===1LN4JH9F）
> 根定义: `hrcs_permfilequery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_permfilequery` | `kd.hr.hbss.opplugin.web.PermFilesSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `0===1HWWTAJR` | 合法性校验 | `hrcs_permfilequery` |

