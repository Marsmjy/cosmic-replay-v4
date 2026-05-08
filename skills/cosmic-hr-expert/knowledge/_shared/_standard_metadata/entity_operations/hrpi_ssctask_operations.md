# hrpi_ssctask — HR库处理中共享任务 操作清单
**继承链**: `bos_billtpl` → `hbp_ssctasktpl` → `hrpi_ssctask`  **操作数**: 65
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
| 34 | `47IEG6VJA2Q+` | newentry | 新增分录 | newentry | `hbp_ssctasktpl` | 0 | 0 |
| 35 | `47IEG6VMYIKP` | deleteentry | 删除分录 | deleteentry | `hbp_ssctasktpl` | 0 | 0 |
| 36 | `484QP1YCC14Z` | donothing | 轻分析 | qingview | `hbp_ssctasktpl` | 0 | 0 |
| 37 | `484QTRONM4Z/` | donothing | 查看影像 | viewphoto | `hbp_ssctasktpl` | 0 | 0 |
| 38 | `484QW84WZ0A0` | donothing | 审批通过 | pass | `hbp_ssctasktpl` | 0 | 0 |
| 39 | `484QYQE/3SZU` | donothing | 打回 | admin_repulse | `hbp_ssctasktpl` | 0 | 0 |
| 40 | `484R+WORYC6=` | donothing | 打回（含影像） | repulse_both | `hbp_ssctasktpl` | 0 | 0 |
| 41 | `484R41XGLRRT` | donothing | 查看流程图 | viewflowchart | `hbp_ssctasktpl` | 0 | 0 |
| 42 | `484R68JR24CS` | donothing | 暂挂 | pause | `hbp_ssctasktpl` | 0 | 0 |
| 43 | `484R8H39169V` | donothing | 修改优先级 | modifypriority | `hbp_ssctasktpl` | 0 | 0 |
| 44 | `484RA16TZ6SW` | donothing | 获取任务 | ask | `hbp_ssctasktpl` | 0 | 0 |
| 45 | `484RBOH06D0G` | donothing | 取消暂挂 | cancelpause | `hbp_ssctasktpl` | 0 | 0 |
| 46 | `484RF/HWX62Z` | donothing | 退回重扫 | rescan | `hbp_ssctasktpl` | 0 | 0 |
| 47 | `484RH++92DMF` | donothing | 取消退扫 | cancelrescan | `hbp_ssctasktpl` | 0 | 0 |
| 48 | `484RJWD+QV4T` | donothing | 状态追踪 | statetracker | `hbp_ssctasktpl` | 0 | 0 |
| 49 | `4851YGFYHKUY` | donothing | 审批不通过 | nopass | `hbp_ssctasktpl` | 0 | 0 |
| 50 | `48535AKQXA70` | donothing | 暂挂 | quality_pause | `hbp_ssctasktpl` | 0 | 0 |
| 51 | `48537T0AVAK+` | donothing | 立即分配 | assignimme | `hbp_ssctasktpl` | 0 | 0 |
| 52 | `4853=Q2959M1` | donothing | 废弃 | quality_discard | `hbp_ssctasktpl` | 0 | 0 |
| 53 | `4853C/YZD2Q2` | donothing | 分配人员 | admin_assigntask | `hbp_ssctasktpl` | 0 | 0 |
| 54 | `4853ED694EBC` | donothing | 修改优先级 | admin_modifypriority | `hbp_ssctasktpl` | 0 | 0 |
| 55 | `4853HW=3LIRS` | donothing | 取消回收 | admin_cancleback | `hbp_ssctasktpl` | 0 | 0 |
| 56 | `4853JLKX1YRG` | donothing | 维护 | maintenance | `hbp_ssctasktpl` | 0 | 0 |
| 57 | `4853LHNXQ=W2` | donothing | 重分配 | admin_redistribution | `hbp_ssctasktpl` | 0 | 0 |
| 58 | `4853NY5U2BEL` | donothing | 回收任务 | admin_back | `hbp_ssctasktpl` | 0 | 0 |
| 59 | `4853QLMTHCP3` | donothing | 删除任务 | admin_delete | `hbp_ssctasktpl` | 0 | 0 |
| 60 | `4853SRUHN9X5` | donothing | 打回（不含影像） | repulse | `hbp_ssctasktpl` | 0 | 0 |
| 61 | `4853V9QM7P0D` | donothing | 取消暂挂 | admin_cancelpause | `hbp_ssctasktpl` | 0 | 0 |
| 62 | `4854/82BOBU6` | donothing | 传阅 | donothingcir | `hbp_ssctasktpl` | 0 | 0 |
| 63 | `4854125+KPJH` | donothing | 查询传阅记录 | donothinglog | `hbp_ssctasktpl` | 0 | 0 |
| 64 | `48543F1JK640` | donothing | 查看凭证 | checkvoucherbtn | `hbp_ssctasktpl` | 0 | 0 |
| 65 | `4A9ZW9=RT5KP` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_ssctasktpl` | 0 | 0 |
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

