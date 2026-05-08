# haos_orgfullnameconfig — 行政组织全称规则 操作清单
**继承链**: `haos_orgfullnameconfig`  **操作数**: 7
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `5AJ=F4QKLI/0` | save | 保存 | save | `haos_orgfullnameconfig` | 1 | 0 |
| 2 | `5AJBUW73BA7B` | newentry | 新增行政组织类型映射分录 | neworgtypeentry | `haos_orgfullnameconfig` | 0 | 0 |
| 3 | `5AJBXD+H+O87` | deleteentry | 删除行政组织类型映射分录 | deleteorgtypeentry | `haos_orgfullnameconfig` | 0 | 0 |
| 4 | `5AJC=9VQ+RT/` | newentry | 新增管理层级映射分录 | neworglayerentry | `haos_orgfullnameconfig` | 0 | 0 |
| 5 | `5AJCBV3HUOKO` | deleteentry | 删除管理层级映射分录 | deleteorglayerentry | `haos_orgfullnameconfig` | 0 | 0 |
| 6 | `5AJCMYVFJB2U` | delete | 删除 | delete | `haos_orgfullnameconfig` | 0 | 0 |
| 7 | `5LYN8L++TK8S` | refresh | 刷新 | refresh | `haos_orgfullnameconfig` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 1. 保存（save / Key: save / oid: 5AJ=F4QKLI/0）
> 根定义: `haos_orgfullnameconfig`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgfullnameconfig` | `kd.hr.haos.opplugin.web.fullname.FullNameConfigSaveOp` | 长名称配置保存op插件 |

