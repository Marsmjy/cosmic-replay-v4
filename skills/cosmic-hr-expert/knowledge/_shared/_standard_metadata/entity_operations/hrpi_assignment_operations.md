# hrpi_assignment — 组织分配 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hrpi_assignment`  **操作数**: 30
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 5 | 2 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `4UHEMMVPOWZ=` | delete | 删除 | delete | `hrpi_assignment` | 4 | 0 |
| 13 | `4V3CKS2QSJ/W` | printsetting | 打印设置 | printsetting | `hrpi_assignment` | 0 | 0 |
| 14 | `4V3CMRBSI9J=` | printpreview | 打印预览 | printpreview | `hrpi_assignment` | 0 | 0 |
| 15 | `4V3CP//RKSLT` | print | 打印 | print | `hrpi_assignment` | 0 | 0 |
| 16 | `4V3CT/TXUIFG` | selecttplprint | 选择模板打印 | selecttplprint | `hrpi_assignment` | 0 | 0 |
| 17 | `4V3DCWLL=E5C` | donothing | 打印 | selecttplprintext | `hrpi_assignment` | 0 | 0 |
| 18 | `4WQCFKSJ2KL8` | modify | 修改 | modify | `hrpi_assignment` | 0 | 0 |
| 19 | `4ZQ42LIM24DA` | importexport_userset | 导入导出个性化设置 | importexport_userset | `hrpi_assignment` | 0 | 0 |
| 20 | `541QVAX=SK1C` | save | 保存 | saveatt | `hrpi_assignment` | 1 | 1 |
| 21 | `541VN=B2D03Y` | delete | 删除 | deleteatt | `hrpi_assignment` | 1 | 0 |
| 22 | `581GZYW+L2+A` | donothing | 发起信息采集任务（要求勾选数据） | start_infocollect | `hrpi_assignment` | 1 | 0 |
| 23 | `581H3373D/7T` | donothing | 信息采集任务跟踪 | infocollect_list | `hrpi_assignment` | 0 | 0 |
| 24 | `581H5+QUOAKH` | donothing | 员工完善档案记录 | infocollect_records | `hrpi_assignment` | 0 | 0 |
| 25 | `5=QAYENTMW7+` | donothing | 发起信息采集任务 | start_infocollect_donothing | `hrpi_assignment` | 0 | 0 |
| 26 | `5=R5Q7O=5=KC` | donothing | 批量导入 | batchimport | `hrpi_assignment` | 0 | 0 |
| 27 | `5=R5V5JF5PU3` | donothing | 批量导出 | batchexport | `hrpi_assignment` | 0 | 0 |
| 28 | `5=R5XX4XRLRD` | donothing | 导入导出记录查看 | record_view | `hrpi_assignment` | 0 | 0 |
| 29 | `5=R6+3GFNKWO` | donothing | 人员附件查看 | person_attachview | `hrpi_assignment` | 0 | 0 |
| 30 | `5HYGE92H8TA0` | donothing | 历史记录 | historyrecord | `hrpi_assignment` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp` | kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp |
| 2 | 2 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 保存成功后更新变动事务记录 |
| 3 | 3 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.log.OperateLogOp` | 员工信息日志OP日志插件 |
| 4 | 4 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp` | 标品字段必填Op |
| 5 | — | `hrpi_assignment` | `kd.bos.business.plugin.CodeRuleOp` | 编码规则op |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4T+7JOX4H0CC` | 字段值合规性校验 | `hrpi_assignment` |
| 2 | GrpfieldsuniqueValidation | `4T1JRTFS10YF` | 组合字段唯一性校验 | `hrpi_assignment` |

---
### 12. 删除（delete / Key: delete / oid: 4UHEMMVPOWZ=）
> 根定义: `hrpi_assignment`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assignment` | `kd.bos.coderule.CodeRuleDeleteOp` | kd.bos.coderule.CodeRuleDeleteOp |
| 2 | 1 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新变动事务记录 |
| 3 | 2 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.log.OperateLogOp` | 员工信息日志OP日志插件 |
| 4 | 3 | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp` | 逻辑删除 |

---
### 20. 保存（save / Key: saveatt / oid: 541QVAX=SK1C）
> 根定义: `hrpi_assignment`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assignment` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审核OP |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `541QTM8TL34E` | 字段值合规性校验 | `hrpi_assignment` |

---
### 21. 删除（delete / Key: deleteatt / oid: 541VN=B2D03Y）
> 根定义: `hrpi_assignment`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assignment` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审核OP |

---
### 22. 发起信息采集任务（要求勾选数据）（donothing / Key: start_infocollect / oid: 581GZYW+L2+A）
> 根定义: `hrpi_assignment`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assignment` | `kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp` | 发起信息采集任务插件 |

