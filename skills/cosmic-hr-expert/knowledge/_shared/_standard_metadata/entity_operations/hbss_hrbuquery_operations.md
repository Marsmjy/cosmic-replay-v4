# hbss_hrbuquery — HR管理组织 操作清单
**继承链**: `hbss_hrbuquery`  **操作数**: 25
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `hbss_hrbuquery` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `hbss_hrbuquery` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `hbss_hrbuquery` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `hbss_hrbuquery` | 3 | 4 |
| 5 | `b599405400001cac` | audit | 审核 | audit | `hbss_hrbuquery` | 0 | 2 |
| 6 | `b599405400001dac` | unaudit | 反审核 | unaudit | `hbss_hrbuquery` | 0 | 2 |
| 7 | `b599405400001eac` | disable | 禁用 | disable | `hbss_hrbuquery` | 2 | 2 |
| 8 | `b599405400001fac` | enable | 启用 | enable | `hbss_hrbuquery` | 2 | 1 |
| 9 | `b5994054000021ac` | close | 关闭 | close | `hbss_hrbuquery` | 0 | 0 |
| 10 | `f381c03f00002cac` | submit | 提交 | submit | `hbss_hrbuquery` | 1 | 3 |
| 11 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `hbss_hrbuquery` | 0 | 2 |
| 12 | `f381c03f000033ac` | copy | 复制 | copy | `hbss_hrbuquery` | 0 | 0 |
| 13 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `hbss_hrbuquery` | 0 | 2 |
| 14 | `S=VTOGXYQBY` | first | 第一 | first | `hbss_hrbuquery` | 0 | 0 |
| 15 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `hbss_hrbuquery` | 0 | 0 |
| 16 | `S=VVFX/B38V` | next | 后一 | next | `hbss_hrbuquery` | 0 | 0 |
| 17 | `S=VW0K6WD7R` | last | 最后 | last | `hbss_hrbuquery` | 0 | 0 |
| 18 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | `hbss_hrbuquery` | 0 | 0 |
| 19 | `10MEU32VZDCP` | exportlist | 引出数据 | exportlist | `hbss_hrbuquery` | 0 | 0 |
| 20 | `13NUAIBA368/` | exportlist | 引出-按列表字段引出 | exportlistbyselectfields | `hbss_hrbuquery` | 0 | 0 |
| 21 | `ac00c0bd000000ac` | refresh | 刷新 | refresh | `hbss_hrbuquery` | 0 | 0 |
| 22 | `0H4T5XIIP5K9` | saveandnew | 保存并新增 | saveandnew | `hbss_hrbuquery` | 0 | 0 |
| 23 | `0H80=ZM+5WEV` | delete | 删除 | delete | `hbss_hrbuquery` | 0 | 0 |
| 24 | `3F8SQXWYCVRD` | donothing | 空操作 | donothing | `hbss_hrbuquery` | 0 | 0 |
| 25 | `3FI8=OG81MMG` | returndata | 返回数据 | returndata | `hbss_hrbuquery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `hbss_hrbuquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbss_hrbuquery` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | — | `hbss_hrbuquery` | `kd.bos.org.OrgBillOperPlugin` |  |
| 3 | — | `hbss_hrbuquery` | `kd.bos.form.plugin.bdctrl.OrgSavePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 必录校验 | `hbss_hrbuquery` |
| 2 | MustInputValidation | `6073a58000004cac` | 必录校验 | `hbss_hrbuquery` |
| 3 | GrpfieldsuniqueValidation | `402ead400000b4ac` | 编码字段唯一性校验 | `hbss_hrbuquery` |
| 4 | ConditionValidation | `RT=+X1FO4TA` | 合法性校验 | `hbss_hrbuquery` |

---
### 5. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `hbss_hrbuquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `hbss_hrbuquery` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `hbss_hrbuquery` |

---
### 6. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `hbss_hrbuquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `hbss_hrbuquery` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `hbss_hrbuquery` |

---
### 7. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `hbss_hrbuquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbss_hrbuquery` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` |  |
| 2 | 2 | `hbss_hrbuquery` | `kd.bos.org.OrgDisableOperationPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `hbss_hrbuquery` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `hbss_hrbuquery` |

---
### 8. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `hbss_hrbuquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbss_hrbuquery` | `kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin` |  |
| 2 | — | `hbss_hrbuquery` | `kd.bos.org.OrgBillOperPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `hbss_hrbuquery` |

---
### 10. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `hbss_hrbuquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbss_hrbuquery` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `hbss_hrbuquery` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 组合字段唯一性校验 | `hbss_hrbuquery` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 必录校验 | `hbss_hrbuquery` |

---
### 11. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `hbss_hrbuquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `hbss_hrbuquery` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `hbss_hrbuquery` |

---
### 13. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `hbss_hrbuquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `hbss_hrbuquery` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `hbss_hrbuquery` |

