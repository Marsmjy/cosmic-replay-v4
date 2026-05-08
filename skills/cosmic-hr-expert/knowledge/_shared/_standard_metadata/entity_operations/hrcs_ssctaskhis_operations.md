# hrcs_ssctaskhis — （废弃）中台已完成共享任务 操作清单
**继承链**: `bos_billtpl` → `hbp_ssctaskhistpl` → `hrcs_ssctaskhis`  **操作数**: 44
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b599405400000cac` | new | 新增 | new | `bos_billtpl` | 0 | 0 |
| 2 | `b599405400000dac` | modify | 修改 | modify | `bos_billtpl` | 0 | 0 |
| 3 | `b599405400000eac` | delete | 删除 | delete | `bos_billtpl` | 1 | 2 |
| 4 | `c91d5125000033ac` | save | 保存 | save | `bos_billtpl` | 1 | 1 |
| 5 | `c91d5125000034ac` | submit | 提交 | submit | `bos_billtpl` | 1 | 3 |
| 6 | `c91d5125000035ac` | audit | 审核 | audit | `bos_billtpl` | 0 | 2 |
| 7 | `c91d5125000036ac` | unaudit | 反审核 | unaudit | `bos_billtpl` | 0 | 3 |
| 8 | `c91d5125000037ac` | close | 关闭 | close | `bos_billtpl` | 0 | 0 |
| 9 | `f8f9e162000000ac` | refresh | 刷新 | refresh | `bos_billtpl` | 0 | 0 |
| 10 | `f8f9e162000001ac` | printpreview | 打印预览 | printpreview | `bos_billtpl` | 0 | 0 |
| 11 | `f8f9e162000002ac` | unsubmit | 撤销 | unsubmit | `bos_billtpl` | 0 | 1 |
| 12 | `f8f9e162000005ac` | copy | 复制 | copy | `bos_billtpl` | 0 | 0 |
| 13 | `c8d218200001cfac` | option | 选项设置 | option | `bos_billtpl` | 0 | 0 |
| 14 | `f2843bab00012dac` | print | 打印 | print | `bos_billtpl` | 0 | 0 |
| 15 | `f31a499a000098ac` | submitandnew | 提交并新增 | submitandnew | `bos_billtpl` | 0 | 2 |
| 16 | `17208d6b000000ac` | view | 查看 | view | `bos_billtpl` | 0 | 0 |
| 17 | `5dbe943a000010ac` | printsetting | 打印设置 | printsetting | `bos_billtpl` | 0 | 0 |
| 18 | `S=XKK8OD98D` | first | 第一 | first | `bos_billtpl` | 0 | 0 |
| 19 | `S=XM2EIP/WL` | previous | 前一 | previous | `bos_billtpl` | 0 | 0 |
| 20 | `S=XM+3GQ635` | next | 后一 | next | `bos_billtpl` | 0 | 0 |
| 21 | `S=XNT/ARF6O` | last | 最后 | last | `bos_billtpl` | 0 | 0 |
| 22 | `VZHXYUICLB8` | importtemplate | 设置模板 | importtemplate | `bos_billtpl` | 0 | 0 |
| 23 | `VZHY/XMF7XK` | importdata | 导入数据 | importdata | `bos_billtpl` | 0 | 0 |
| 24 | `VZH/8A8+2/1` | importdetails | 查看导入结果 | importdetails | `bos_billtpl` | 0 | 0 |
| 25 | `VZH=JJJAKS8` | importtemplatelist | 管理模板 | importtemplatelist | `bos_billtpl` | 0 | 0 |
| 26 | `10JK89100EBT` | returndata | 返回数据 | returndata | `bos_billtpl` | 0 | 0 |
| 27 | `10JPBJUKNGSW` | exportlist | 导出数据（按导入模板） | exportlist | `bos_billtpl` | 0 | 0 |
| 28 | `13NVF6Y2P98N` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_billtpl` | 0 | 0 |
| 29 | `/J6L5ONVGHAO` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_billtpl` | 0 | 0 |
| 30 | `0H1S=3CFEZ8G` | exportdetails | 查看导出结果 | exportdetails | `bos_billtpl` | 0 | 0 |
| 31 | `1U=SWXH=TQ+7` | importexport_userset | 导入导出个性化设置 | importexport_userset | `bos_billtpl` | 0 | 0 |
| 32 | `2MMZ77AATXJL` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_billtpl` | 0 | 0 |
| 33 | `2MMZ7XUBUQ3K` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_billtpl` | 0 | 0 |
| 34 | `47ITBG4PNG78` | newentry | 新增分录 | newentry | `hbp_ssctaskhistpl` | 0 | 0 |
| 35 | `47ITBG4PNGWI` | deleteentry | 删除分录 | deleteentry | `hbp_ssctaskhistpl` | 0 | 0 |
| 36 | `4854RC0BX8QK` | donothing | 下推复核任务 | repairnexttask | `hbp_ssctaskhistpl` | 1 | 0 |
| 37 | `4854UF9JRMOE` | donothing | 影像 | viewphoto | `hbp_ssctaskhistpl` | 0 | 0 |
| 38 | `4854WP6YX4R1` | donothing | 查看流程图 | viewflowchart | `hbp_ssctaskhistpl` | 0 | 0 |
| 39 | `4854YP61QS/N` | donothing | 查看凭证 | checkvoucherbtn | `hbp_ssctaskhistpl` | 0 | 0 |
| 40 | `4855+GU1BH+E` | donothing | 状态跟踪 | statetracker | `hbp_ssctaskhistpl` | 0 | 0 |
| 41 | `48550TNAH/K3` | donothing | 传阅 | donothingcir | `hbp_ssctaskhistpl` | 0 | 0 |
| 42 | `48552XZGFHDI` | donothing | 查看传阅记录 | donothinglog | `hbp_ssctaskhistpl` | 0 | 0 |
| 43 | `48554IMWEDAU` | donothing | 轻分析 | qingview | `hbp_ssctaskhistpl` | 0 | 0 |
| 44 | `4A=1095K+VIP` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_ssctaskhistpl` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 3. 删除（delete / Key: delete / oid: b599405400000eac）
> 根定义: `bos_billtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_billtpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1d11920c000004ac` | 合法性校验 | `bos_billtpl` |
| 2 | ConditionValidation | `1+9OXACHMV3M` | 下游单据存在性校验 | `bos_billtpl` |

---
### 4. 保存（save / Key: save / oid: c91d5125000033ac）
> 根定义: `bos_billtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_billtpl` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `RS=8GBXGZY4` | 字段值合规性校验 | `bos_billtpl` |

---
### 5. 提交（submit / Key: submit / oid: c91d5125000034ac）
> 根定义: `bos_billtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_billtpl` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `194cc69300000aac` | 合法性校验 | `bos_billtpl` |
| 2 | GrpfieldsuniqueValidation | `3c48f4980000efac` | 组合字段唯一性校验 | `bos_billtpl` |
| 3 | MustInputValidation | `RS=AANUYST7` | 字段值合规性校验 | `bos_billtpl` |

---
### 6. 审核（audit / Key: audit / oid: c91d5125000035ac）
> 根定义: `bos_billtpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `194cc693000015ac` | 合法性校验 | `bos_billtpl` |
| 2 | InProcessValidation | `=K8MV9VWN3Z` | 单据在流程中校验 | `bos_billtpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: c91d5125000036ac）
> 根定义: `bos_billtpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1d11920c000005ac` | 合法性校验 | `bos_billtpl` |
| 2 | InProcessValidation | `=K8PAT6QHU3` | 单据在流程中校验 | `bos_billtpl` |
| 3 | ConditionValidation | `2W/B/278HX77` | 下游单据存在性校验 | `bos_billtpl` |

---
### 11. 撤销（unsubmit / Key: unsubmit / oid: f8f9e162000002ac）
> 根定义: `bos_billtpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba447000113ac` | 合法性校验 | `bos_billtpl` |

---
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a000098ac）
> 根定义: `bos_billtpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001d0ac` | 组合字段唯一性校验 | `bos_billtpl` |
| 2 | ConditionValidation | `2f5773ca0001d1ac` | 合法性校验 | `bos_billtpl` |

---
### 36. 下推复核任务（donothing / Key: repairnexttask / oid: 4854RC0BX8QK）
> 根定义: `hbp_ssctaskhistpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_ssctaskhistpl` | `kd.ssc.task.opplugin.CheckTaskRepairPlugin` | 初审通过后，下推复核任务 |

