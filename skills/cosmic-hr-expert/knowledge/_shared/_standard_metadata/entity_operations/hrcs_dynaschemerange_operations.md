# hrcs_dynaschemerange — 授权方案管理员范围 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hbp_orimintimeseqtpl` → `hrcs_dynaschemerange`  **操作数**: 22
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 2 | 1 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `20LC95XJY6DN` | donothing | 变更 | change | `hbp_orimintimeseqtpl` | 0 | 0 |
| 13 | `20LC=THDC==V` | donothing | 历史版本信息 | hisversioninfo | `hbp_orimintimeseqtpl` | 0 | 0 |
| 14 | `20LCIMZ/09K8` | donothing | 复制 | hiscopy | `hbp_orimintimeseqtpl` | 0 | 0 |
| 15 | `22=8CU2XYVBU` | delete | 删除 | delete | `hbp_orimintimeseqtpl` | 1 | 0 |
| 16 | `2BLCQTNZ8SU=` | donothing | 新增数据版本 | newhisversion | `hbp_orimintimeseqtpl` | 0 | 0 |
| 17 | `2NY0FWM1A005` | donothing | 修订 | revise | `hbp_orimintimeseqtpl` | 0 | 0 |
| 18 | `2P7PUMG92898` | donothing | 版本对比 | versionchangecompare | `hbp_orimintimeseqtpl` | 0 | 0 |
| 19 | `2XZ5+K3BD8CH` | donothing | 查看所有版本 | showallversion | `hbp_orimintimeseqtpl` | 0 | 0 |
| 20 | `4URC9IUO7/CK` | modify | 修改 | modify | `hbp_orimintimeseqtpl` | 0 | 0 |
| 21 | `4UUDEW9U95EO` | donothing | 修订日志 | reviserecord | `hbp_orimintimeseqtpl` | 0 | 0 |
| 22 | `4UUWBPGI1O9Z` | save | 确认变更 | confirmchange | `hbp_orimintimeseqtpl` | 2 | 1 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_orimintimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | — | `hbp_orimintimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型-组合字段唯一性校验OP插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `22GYV=PCW6A/` | 字段值合规性校验 | `hbp_orimintimeseqtpl` |

---
### 15. 删除（delete / Key: delete / oid: 22=8CU2XYVBU）
> 根定义: `hbp_orimintimeseqtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_orimintimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

---
### 22. 确认变更（save / Key: confirmchange / oid: 4UUWBPGI1O9Z）
> 根定义: `hbp_orimintimeseqtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_orimintimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验器 |
| 2 | 1 | `hbp_orimintimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UUWAVUZKVAG` | 字段值合规性校验 | `hbp_orimintimeseqtpl` |

