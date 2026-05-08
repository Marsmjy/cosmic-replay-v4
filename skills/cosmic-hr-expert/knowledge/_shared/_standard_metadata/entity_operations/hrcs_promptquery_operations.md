# hrcs_promptquery — 提示语配置列表 操作清单
**继承链**: `hrcs_promptquery`  **操作数**: 33
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `hrcs_promptquery` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `hrcs_promptquery` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `hrcs_promptquery` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `hrcs_promptquery` | 2 | 5 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `hrcs_promptquery` | 1 | 2 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `hrcs_promptquery` | 0 | 2 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `hrcs_promptquery` | 0 | 2 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | `hrcs_promptquery` | 0 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | `hrcs_promptquery` | 0 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | `hrcs_promptquery` | 0 | 0 |
| 11 | `f381c03f00002cac` | submit | 提交 | submit | `hrcs_promptquery` | 1 | 3 |
| 12 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `hrcs_promptquery` | 0 | 2 |
| 13 | `f381c03f000033ac` | copy | 复制 | copy | `hrcs_promptquery` | 0 | 0 |
| 14 | `f381c03f000034ac` | refresh | 刷新 | refresh | `hrcs_promptquery` | 0 | 0 |
| 15 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `hrcs_promptquery` | 0 | 2 |
| 16 | `S=VTOGXYQBY` | first | 第一 | first | `hrcs_promptquery` | 0 | 0 |
| 17 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `hrcs_promptquery` | 0 | 0 |
| 18 | `S=VVFX/B38V` | next | 后一 | next | `hrcs_promptquery` | 0 | 0 |
| 19 | `S=VW0K6WD7R` | last | 最后 | last | `hrcs_promptquery` | 0 | 0 |
| 20 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | `hrcs_promptquery` | 0 | 0 |
| 21 | `10MEU32VZDCP` | exportlist | 引出数据（按引入模板） | exportlist | `hrcs_promptquery` | 0 | 0 |
| 22 | `13NUAIBA368/` | exportlist | 引出数据（按列表） | exportlistbyselectfields | `hrcs_promptquery` | 0 | 0 |
| 23 | `1H7=1YCHHI51` | close | 关闭 | close | `hrcs_promptquery` | 0 | 0 |
| 24 | `1I/P1VW6XUVH` | donothing | 选择变量 | selectfield | `hrcs_promptquery` | 0 | 0 |
| 25 | `1I/P3Q/XJ0EI` | donothing | 查看规则 | viewruledetail | `hrcs_promptquery` | 0 | 0 |
| 26 | `1MKKG120EU2T` | donothing | 空操作 | opentips | `hrcs_promptquery` | 0 | 0 |
| 27 | `1MKPS70P1GZK` | returndata | 返回数据 | returndata | `hrcs_promptquery` | 0 | 0 |
| 28 | `1MUY/6AARL/B` | exportlist | 引出提示语及内容 | export_prompt | `hrcs_promptquery` | 0 | 0 |
| 29 | `2+JLDUN9ADR1` | donothing | 自定义弹窗 | customwindow | `hrcs_promptquery` | 0 | 0 |
| 30 | `2+JLFNX6R74/` | donothing | 自定义变量 | customvar | `hrcs_promptquery` | 0 | 0 |
| 31 | `3W347S=0V4K6` | importdata_hr | 引入数据 | importdata_hr | `hrcs_promptquery` | 0 | 0 |
| 32 | `3W6=TFZMUPL4` | export_from_impttpl_hr | 按引入模板引出 | export_from_impttpl_hr | `hrcs_promptquery` | 0 | 0 |
| 33 | `3W6HKMD7CAEB` | export_from_expttpl_hr | 引出 | export_from_expttpl_hr | `hrcs_promptquery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `hrcs_promptquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_promptquery` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `hrcs_promptquery` | `kd.hr.hbss.opplugin.web.PromptSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `1a51dc0a0000a1ac` | 合法性校验 | `hrcs_promptquery` |
| 3 | CustValidation | `0R3DU5Z42SN0` | HR基础资料保存校验 | `hrcs_promptquery` |
| 4 | GrpfieldsuniqueValidation | `1B9SI1HB3NKW` | 编码唯一性校验 | `hrcs_promptquery` |
| 5 | GrpfieldsuniqueValidation | `1B9SIU+IP6+=` | 名称唯一性校验 | `hrcs_promptquery` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `hrcs_promptquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_promptquery` | `kd.bos.coderule.CodeRuleDeleteOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f789ca66000000ac` | 数据已经启用，不能删除 | `hrcs_promptquery` |
| 2 | CustValidation | `0R6U8WEZK8/W` | HR基础资料删除校验 | `hrcs_promptquery` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `hrcs_promptquery` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `hrcs_promptquery` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `hrcs_promptquery` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `hrcs_promptquery` |

---
### 11. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `hrcs_promptquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_promptquery` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `hrcs_promptquery` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 组合字段唯一性校验 | `hrcs_promptquery` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `hrcs_promptquery` |

---
### 12. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `hrcs_promptquery` |

---
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `hrcs_promptquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `hrcs_promptquery` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `hrcs_promptquery` |

