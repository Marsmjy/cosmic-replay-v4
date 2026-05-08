# hcf_candidate — 拟入职人员 操作清单
**继承链**: `hbp_bd_originaltpl` → `hcf_candidate`  **操作数**: 16
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originaltpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originaltpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originaltpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originaltpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originaltpl` | 1 | 1 |
| 6 | `36XNX2J0IYJ/` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 7 | `36XNYXBDN8US` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 8 | `36XO+0YA=NQ1` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 9 | `36XO/49Y36PX` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 10 | `36XO07P0TYXD` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 11 | `36XO16LFA2/E` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 12 | `23+KT3/P+0KQ` | saveandnew | 保存并新增 | saveandnew | `hcf_candidate` | 0 | 0 |
| 13 | `23/AQ3FTXY0B` | close | 退出 | close | `hcf_candidate` | 0 | 0 |
| 14 | `18+U3TZXAEQL` | new | 新增 | new | `hcf_candidate` | 0 | 0 |
| 15 | `18+U5FF9TM8Y` | delete | 删除 | delete | `hcf_candidate` | 0 | 0 |
| 16 | `4TPDNZF4R93F` | modify | 修改 | modify | `hcf_candidate` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originaltpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hcf_candidate` | `kd.bos.business.plugin.CodeRuleOp` | kd.bos.business.plugin.CodeRuleOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4Y=M7H9J/V=U` | 字段值合规性校验 | `hcf_candidate` |

