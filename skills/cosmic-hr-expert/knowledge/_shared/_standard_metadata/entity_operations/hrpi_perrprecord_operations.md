# hrpi_perrprecord — 奖惩记录 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hrpi_employeetpl` → `hrpi_perrprecord`  **操作数**: 19
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 3 | 3 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `4UHGMDG1N296` | delete | 删除 | delete | `hrpi_employeetpl` | 3 | 0 |
| 13 | `4UNZ9N3LMMMT` | donothing | 放弃 | abandon | `hrpi_employeetpl` | 1 | 0 |
| 14 | `4V2N2XCHXIQI` | donothing | 修改 | modifyatt | `hrpi_employeetpl` | 0 | 0 |
| 15 | `4WQJ/1PZZP9O` | new | 新增 | new | `hrpi_employeetpl` | 0 | 0 |
| 16 | `4WQJ0KLJHLJE` | modify | 修改 | modify | `hrpi_employeetpl` | 0 | 0 |
| 17 | `541Q+WMP0BVS` | save | 保存 | saveatt | `hrpi_employeetpl` | 1 | 0 |
| 18 | `541X2E5/=NRE` | delete | 删除 | deleteatt | `hrpi_employeetpl` | 2 | 0 |
| 19 | `23/AQ3FTXY0B` | close | 退出 | close | `hrpi_perrprecord` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.log.OperateLogOp` | 员工信息日志OP日志插件 |
| 2 | 2 | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp` | 标品字段必填Op |
| 3 | — | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 事务变动消息更新插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4Z3SO=HJZ5D0` | 字段值合规性校验 | `hrpi_employeetpl` |
| 2 | DynamicValidation | `50=VS0/MV0VR` | 单行表校验唯一 | `hrpi_employeetpl` |
| 3 | DynamicValidation | `5CS931+WQQ2C` | 员工附表同一组织分配校验 | `hrpi_employeetpl` |

---
### 12. 删除（delete / Key: delete / oid: 4UHGMDG1N296）
> 根定义: `hrpi_employeetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新事务变动记录 |
| 2 | 1 | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.log.OperateLogOp` | 员工信息日志OP日志插件 |
| 3 | 2 | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp` | 附表删除忽略基础资料引用OP |

---
### 13. 放弃（donothing / Key: abandon / oid: 4UNZ9N3LMMMT）
> 根定义: `hrpi_employeetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_employeetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 员工审核通用op插件 |

---
### 17. 保存（save / Key: saveatt / oid: 541Q+WMP0BVS）
> 根定义: `hrpi_employeetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_employeetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审核OP |

---
### 18. 删除（delete / Key: deleteatt / oid: 541X2E5/=NRE）
> 根定义: `hrpi_employeetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_employeetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审批OP |
| 2 | 1 | `hrpi_employeetpl` | `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp` | 附表删除忽略基础资料引用OP |

