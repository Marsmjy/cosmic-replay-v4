# haos_orgteamcooprel — 组织协作关系 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hbp_bd_timelinemintpl` → `haos_orgteamcooprel`  **操作数**: 61
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
| 12 | `4R/HR6LQCLZ1` | donothing | 复制 | copy | `hbp_bd_timelinemintpl` | 0 | 0 |
| 13 | `4R/HUQ+ARVEU` | donothing | 查看日志 | viewlog | `hbp_bd_timelinemintpl` | 0 | 0 |
| 14 | `4RIR/ALATGNY` | delete | 删除 | delete | `hbp_bd_timelinemintpl` | 2 | 0 |
| 15 | `4RJA//W+TN+N` | new | 新增 | new | `hbp_bd_timelinemintpl` | 0 | 0 |
| 16 | `2BLCQTNZ8SU=` | donothing | 新增数据版本 | newhisversion | `haos_orgteamcooprel` | 0 | 0 |
| 17 | `2BT8O/+Q8X4Q` | donothing | 保存 | his_import_save | `haos_orgteamcooprel` | 2 | 1 |
| 18 | `2NY0FWM1A005` | donothing | 修订 | revise | `haos_orgteamcooprel` | 1 | 0 |
| 19 | `2NY0HHSF0RQW` | donothing | 版本修订历史 | reviserecord | `haos_orgteamcooprel` | 0 | 0 |
| 20 | `2P7PUMG92898` | donothing | 版本对比 | versionchangecompare | `haos_orgteamcooprel` | 0 | 0 |
| 21 | `2XZ5+K3BD8CH` | donothing | 查看所有版本 | showallversion | `haos_orgteamcooprel` | 0 | 0 |
| 22 | `b5994054000016ac` | new | 新增 | new | `haos_orgteamcooprel` | 0 | 0 |
| 23 | `b5994054000017ac` | modify | 修改 | modify | `haos_orgteamcooprel` | 0 | 0 |
| 24 | `b5994054000018ac` | view | 查看 | view | `haos_orgteamcooprel` | 0 | 0 |
| 25 | `b599405400001aac` | save | 保存 | save | `haos_orgteamcooprel` | 6 | 4 |
| 26 | `b599405400001bac` | delete | 删除 | delete | `haos_orgteamcooprel` | 4 | 3 |
| 27 | `b599405400001cac` | audit | 审核 | audit | `haos_orgteamcooprel` | 2 | 2 |
| 28 | `b599405400001dac` | unaudit | 反审核 | unaudit | `haos_orgteamcooprel` | 1 | 2 |
| 29 | `b599405400001eac` | disable | 禁用 | disable | `haos_orgteamcooprel` | 1 | 2 |
| 30 | `b599405400001fac` | enable | 启用 | enable | `haos_orgteamcooprel` | 1 | 1 |
| 31 | `b5994054000021ac` | close | 关闭 | close | `haos_orgteamcooprel` | 0 | 0 |
| 32 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `haos_orgteamcooprel` | 0 | 0 |
| 33 | `f381c03f00002cac` | submit | 提交 | submit | `haos_orgteamcooprel` | 2 | 3 |
| 34 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `haos_orgteamcooprel` | 0 | 2 |
| 35 | `f381c03f000033ac` | copy | 复制 | copy | `haos_orgteamcooprel` | 0 | 0 |
| 36 | `f381c03f000034ac` | refresh | 刷新 | refresh | `haos_orgteamcooprel` | 0 | 0 |
| 37 | `c8d21820000086ac` | option | 选项设置 | option | `haos_orgteamcooprel` | 0 | 0 |
| 38 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `haos_orgteamcooprel` | 0 | 2 |
| 39 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `haos_orgteamcooprel` | 0 | 0 |
| 40 | `S=VTOGXYQBY` | first | 第一 | first | `haos_orgteamcooprel` | 0 | 0 |
| 41 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `haos_orgteamcooprel` | 0 | 0 |
| 42 | `S=VVFX/B38V` | next | 后一 | next | `haos_orgteamcooprel` | 0 | 0 |
| 43 | `S=VW0K6WD7R` | last | 最后 | last | `haos_orgteamcooprel` | 0 | 0 |
| 44 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | `haos_orgteamcooprel` | 0 | 0 |
| 45 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | `haos_orgteamcooprel` | 0 | 0 |
| 46 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | `haos_orgteamcooprel` | 0 | 0 |
| 47 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | `haos_orgteamcooprel` | 0 | 0 |
| 48 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `haos_orgteamcooprel` | 0 | 0 |
| 49 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `haos_orgteamcooprel` | 0 | 0 |
| 50 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | `haos_orgteamcooprel` | 0 | 0 |
| 51 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | `haos_orgteamcooprel` | 0 | 0 |
| 52 | `21A5O/3PV7IL` | donothing | 查看日志 | logview | `haos_orgteamcooprel` | 0 | 0 |
| 53 | `20L86E0+8SH6` | donothing | 变更 | insertdata_his | `haos_orgteamcooprel` | 0 | 0 |
| 54 | `20L88J=0T27O` | donothing | 历史版本信息 | showhisversion | `haos_orgteamcooprel` | 0 | 0 |
| 55 | `20L8CM6COJUQ` | donothing | 确认变更 | confirmchange | `haos_orgteamcooprel` | 2 | 3 |
| 56 | `20L8K8O/0IDH` | donothing | 修改 | his_modify | `haos_orgteamcooprel` | 0 | 0 |
| 57 | `20L8N=DTT64=` | donothing | 复制 | his_copy | `haos_orgteamcooprel` | 1 | 0 |
| 58 | `20L8P5A+RVVL` | donothing | 变更生效日期 | changebed | `haos_orgteamcooprel` | 1 | 0 |
| 59 | `20L8QSD/ZIY1` | donothing | 取消变更 | cancelchange | `haos_orgteamcooprel` | 1 | 0 |
| 60 | `21CWKJEV422O` | donothing | 删除历史版本 | his_delete | `haos_orgteamcooprel` | 1 | 0 |
| 61 | `21RCLIHUDPU1` | donothing | 确认变更 | confirmchangenoaudit | `haos_orgteamcooprel` | 2 | 3 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |
| 2 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板OP插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4R38557==XM/` | 字段值合规性校验 | `hbp_bd_timelinemintpl` |

---
### 14. 删除（delete / Key: delete / oid: 4RIR/ALATGNY）
> 根定义: `hbp_bd_timelinemintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板op插件 |
| 2 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |

---
### 17. 保存（donothing / Key: his_import_save / oid: 2BT8O/+Q8X4Q）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2BT8MTQMRGKG` | 字段值合规性校验 | `haos_orgteamcooprel` |

---
### 18. 修订（donothing / Key: revise / oid: 2NY0FWM1A005）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 25. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 3 | 2 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 3 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 4 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 6 | 5 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `haos_orgteamcooprel` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `haos_orgteamcooprel` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `haos_orgteamcooprel` |

---
### 26. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 3 | 2 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 3 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `haos_orgteamcooprel` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `haos_orgteamcooprel` |

---
### 27. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 28. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 29. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 30. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 33. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 组合字段唯一性校验 | `haos_orgteamcooprel` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `haos_orgteamcooprel` |

---
### 34. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `haos_orgteamcooprel`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 38. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `haos_orgteamcooprel`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `haos_orgteamcooprel` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `haos_orgteamcooprel` |

---
### 55. 确认变更（donothing / Key: confirmchange / oid: 20L8CM6COJUQ）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `22GPJMYIWRS6` | 字段值合规性校验 | `haos_orgteamcooprel` |
| 2 | GrpfieldsuniqueValidation | `254FQ5Z7=/BY` | 编码唯一性校验 | `haos_orgteamcooprel` |
| 3 | GrpfieldsuniqueValidation | `254FQWXF2Z=S` | 名称唯一性校验 | `haos_orgteamcooprel` |

---
### 57. 复制（donothing / Key: his_copy / oid: 20L8N=DTT64=）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 58. 变更生效日期（donothing / Key: changebed / oid: 20L8P5A+RVVL）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 59. 取消变更（donothing / Key: cancelchange / oid: 20L8QSD/ZIY1）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 60. 删除历史版本（donothing / Key: his_delete / oid: 21CWKJEV422O）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 61. 确认变更（donothing / Key: confirmchangenoaudit / oid: 21RCLIHUDPU1）
> 根定义: `haos_orgteamcooprel`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 2 | 1 | `haos_orgteamcooprel` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `22GPKHG4BABB` | 字段值合规性校验 | `haos_orgteamcooprel` |
| 2 | GrpfieldsuniqueValidation | `254G378OLWPX` | 编码唯一性校验 | `haos_orgteamcooprel` |
| 3 | GrpfieldsuniqueValidation | `254G3W+MHTPU` | 名称唯一性校验 | `haos_orgteamcooprel` |

