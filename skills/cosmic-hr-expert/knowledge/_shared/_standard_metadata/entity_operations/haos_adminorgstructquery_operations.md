# haos_adminorgstructquery — 行政组织结构 操作清单
**继承链**: `haos_adminorgstructquery`  **操作数**: 22
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `haos_adminorgstructquery` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `haos_adminorgstructquery` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `haos_adminorgstructquery` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `haos_adminorgstructquery` | 9 | 6 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `haos_adminorgstructquery` | 7 | 3 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `haos_adminorgstructquery` | 2 | 2 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `haos_adminorgstructquery` | 2 | 2 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | `haos_adminorgstructquery` | 1 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | `haos_adminorgstructquery` | 2 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | `haos_adminorgstructquery` | 0 | 0 |
| 11 | `f381c03f00002cac` | submit | 提交 | submit | `haos_adminorgstructquery` | 7 | 5 |
| 12 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `haos_adminorgstructquery` | 1 | 2 |
| 13 | `f381c03f000033ac` | copy | 复制 | copy | `haos_adminorgstructquery` | 0 | 0 |
| 14 | `f381c03f000034ac` | refresh | 刷新 | refresh | `haos_adminorgstructquery` | 0 | 0 |
| 15 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `haos_adminorgstructquery` | 0 | 2 |
| 16 | `S=VTOGXYQBY` | first | 第一 | first | `haos_adminorgstructquery` | 0 | 0 |
| 17 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `haos_adminorgstructquery` | 0 | 0 |
| 18 | `S=VVFX/B38V` | next | 后一 | next | `haos_adminorgstructquery` | 0 | 0 |
| 19 | `S=VW0K6WD7R` | last | 最后 | last | `haos_adminorgstructquery` | 0 | 0 |
| 20 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | `haos_adminorgstructquery` | 0 | 0 |
| 21 | `10MEU32VZDCP` | exportlist | 引出数据（按引入模板） | exportlist | `haos_adminorgstructquery` | 0 | 0 |
| 22 | `13NUAIBA368/` | exportlist | 引出数据（按列表） | exportlistbyselectfields | `haos_adminorgstructquery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 3 | 2 | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 4 | 3 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 3 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 6 | 4 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 7 | 5 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 8 | 6 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 9 | 7 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.hismodel.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `haos_adminorgstructquery` |
| 3 | RegexValidation | `/=D33OTQL=12` | 长编码界定符检查 | `haos_adminorgstructquery` |
| 4 | GrpfieldsuniqueValidation | `/D4C17LPW9F+` | 组合字段唯一性校验 | `haos_adminorgstructquery` |
| 5 | GrpfieldsuniqueValidation | `2+UBQR52MZPL` | 编码唯一性校验 | `haos_adminorgstructquery` |
| 6 | GrpfieldsuniqueValidation | `2+UBRNZ73MPD` | 名称唯一性校验 | `haos_adminorgstructquery` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 2 | 1 | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeDeleteValidate` |  |
| 3 | 2 | `haos_adminorgstructquery` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 4 | 3 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 3 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 6 | 4 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 7 | 5 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `haos_adminorgstructquery` |
| 3 | CustValidation | `2+UBV409MZA0` | HR基础资料删除校验 | `haos_adminorgstructquery` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 1 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `haos_adminorgstructquery` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 1 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `haos_adminorgstructquery` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `haos_adminorgstructquery` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeEnableValidate` |  |
| 2 | 1 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `haos_adminorgstructquery` |

---
### 11. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 3 | 2 | `haos_adminorgstructquery` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 4 | 3 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 5 | 4 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 6 | 4 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 7 | 5 | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.hismodel.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.hismodel.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 编码唯一性校验 | `haos_adminorgstructquery` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `haos_adminorgstructquery` |
| 4 | GrpfieldsuniqueValidation | `2DDGVBA4NNWC` | 名称唯一性校验 | `haos_adminorgstructquery` |
| 5 | GrpfieldsuniqueValidation | `28IWMI434CF8` | 名称唯一性校验 | `haos_adminorgstructquery` |

---
### 12. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `haos_adminorgstructquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstructquery` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `haos_adminorgstructquery` |

---
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `haos_adminorgstructquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `haos_adminorgstructquery` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `haos_adminorgstructquery` |

