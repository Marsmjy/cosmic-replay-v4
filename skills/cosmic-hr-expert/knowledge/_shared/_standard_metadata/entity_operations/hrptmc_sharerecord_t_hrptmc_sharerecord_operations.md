# hrptmc_sharerecord_t_hrptmc_sharerecord 系列 — 操作清单（合并去重）
**涵盖实体数**: 3  **去重后操作数**: 12
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```hbp_bd_originalmintpl (HR原生基础资料最小模板)    └── hrptmc_sharerecord (分享记录)        ├── hrptmc_sharerecord_layout (分享给我)        └── hrptmc_sharerecord_log (分享执行记录)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | 0 | 0 | 1 | 3 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | 0 | 0 | 1 | 3 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | 0 | 0 | 1 | 3 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | 0 | 0 | 1 | 3 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | 0 | 0 | 1 | 3 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 3 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 3 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 3 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 3 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 3 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 3 |
| 12 | `48+FR4JFD31P` | donothing | 取消分享 | cancelshare | 1 | 0 | 1 | 3 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 12. 取消分享（donothing / Key: cancelshare / oid: 48+FR4JFD31P）
> 根定义: `hrptmc_sharerecord` | 涉及实体 (3): `hrptmc_sharerecord`, `hrptmc_sharerecord_layout`, `hrptmc_sharerecord_log`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_sharerecord` | `kd.hr.hrptc.opplugin.web.share.ReportShareOp` | kd.hr.hrptc.opplugin.web.share.ReportShareOp |

