# hrcs_workingplanquery — 公共日历 操作清单
**继承链**: `hrcs_workingplanquery`  **操作数**: 12
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `1JXF1MW6C3S3` | save | 保存 | save | `hrcs_workingplanquery` | 0 | 1 |
| 2 | `1JXF6L3YNQYM` | close | 退出 | close | `hrcs_workingplanquery` | 0 | 0 |
| 3 | `1K+OJ3F=XM7P` | new | 新增 | new | `hrcs_workingplanquery` | 0 | 0 |
| 4 | `1K89QL7C+DOI` | delete | 删除 | delete | `hrcs_workingplanquery` | 0 | 0 |
| 5 | `1K89TGR1HXCF` | refresh | 刷新 | refresh | `hrcs_workingplanquery` | 0 | 0 |
| 6 | `1K8=/ZAQY97+` | disable | 禁用 | disable | `hrcs_workingplanquery` | 1 | 2 |
| 7 | `1K=GWW635SVN` | enable | 启用 | enable | `hrcs_workingplanquery` | 0 | 1 |
| 8 | `1PLB80CEMPB8` | copy | 复制 | copy | `hrcs_workingplanquery` | 0 | 0 |
| 9 | `23PX+/XF3QYY` | modify | 修改 | modify | `hrcs_workingplanquery` | 0 | 0 |
| 10 | `25DWMG22QQC5` | returndata | 返回数据 | returndata | `hrcs_workingplanquery` | 0 | 0 |
| 11 | `2N2KH455YK03` | donothing | 预览 | preview | `hrcs_workingplanquery` | 0 | 0 |
| 12 | `2N31N3S1UQ3J` | donothing | 查看日志 | viewlog | `hrcs_workingplanquery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 1. 保存（save / Key: save / oid: 1JXF1MW6C3S3）
> 根定义: `hrcs_workingplanquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `1KE88S6CRU/A` | 缂栫爜瀛楁鍞竴鎬ф牎楠� | `hrcs_workingplanquery` |

---
### 6. 禁用（disable / Key: disable / oid: 1K8=/ZAQY97+）
> 根定义: `hrcs_workingplanquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_workingplanquery` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` | 绂佺敤 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1T1KTWN7WRXR` | 鍚堟硶鎬ф牎楠� | `hrcs_workingplanquery` |
| 2 | ConditionValidation | `1T1KX1OMN+L=` | 鍚堟硶鎬ф牎楠� | `hrcs_workingplanquery` |

---
### 7. 启用（enable / Key: enable / oid: 1K=GWW635SVN）
> 根定义: `hrcs_workingplanquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1T1L3GP9FMHJ` | 鍚堟硶鎬ф牎楠� | `hrcs_workingplanquery` |

