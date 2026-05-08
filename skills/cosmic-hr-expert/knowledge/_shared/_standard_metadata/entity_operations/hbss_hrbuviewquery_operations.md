# hbss_hrbuviewquery — HR业务管理视图 操作清单
**继承链**: `hbss_hrbuviewquery`  **操作数**: 24
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `hbss_hrbuviewquery` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `hbss_hrbuviewquery` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `hbss_hrbuviewquery` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `hbss_hrbuviewquery` | 1 | 1 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `hbss_hrbuviewquery` | 0 | 2 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `hbss_hrbuviewquery` | 0 | 2 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `hbss_hrbuviewquery` | 0 | 2 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | `hbss_hrbuviewquery` | 0 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | `hbss_hrbuviewquery` | 0 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | `hbss_hrbuviewquery` | 0 | 0 |
| 11 | `f381c03f00002cac` | submit | 提交 | submit | `hbss_hrbuviewquery` | 1 | 3 |
| 12 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `hbss_hrbuviewquery` | 0 | 2 |
| 13 | `f381c03f000033ac` | copy | 复制 | copy | `hbss_hrbuviewquery` | 0 | 0 |
| 14 | `f381c03f000034ac` | refresh | 刷新 | refresh | `hbss_hrbuviewquery` | 0 | 0 |
| 15 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `hbss_hrbuviewquery` | 0 | 2 |
| 16 | `S=VTOGXYQBY` | first | 第一 | first | `hbss_hrbuviewquery` | 0 | 0 |
| 17 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `hbss_hrbuviewquery` | 0 | 0 |
| 18 | `S=VVFX/B38V` | next | 后一 | next | `hbss_hrbuviewquery` | 0 | 0 |
| 19 | `S=VW0K6WD7R` | last | 最后 | last | `hbss_hrbuviewquery` | 0 | 0 |
| 20 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | `hbss_hrbuviewquery` | 0 | 0 |
| 21 | `10MEU32VZDCP` | exportlist | 引出数据 | exportlist | `hbss_hrbuviewquery` | 0 | 0 |
| 22 | `13NUAIBA368/` | exportlist | 引出-按列表字段引出 | exportlistbyselectfields | `hbss_hrbuviewquery` | 0 | 0 |
| 23 | `0HOMH546+ZDQ` | saveandnew | 保存并新增 | saveandnew | `hbss_hrbuviewquery` | 0 | 0 |
| 24 | `3F8T8K7I1A5/` | donothing | 空操作 | donothing | `hbss_hrbuviewquery` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `hbss_hrbuviewquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbss_hrbuviewquery` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 必录校验 | `hbss_hrbuviewquery` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `f789ca66000000ac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 11. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `hbss_hrbuviewquery`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbss_hrbuviewquery` | `kd.bos.business.plugin.CodeRuleOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 组合字段唯一性校验 | `hbss_hrbuviewquery` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 必录校验 | `hbss_hrbuviewquery` |

---
### 12. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `hbss_hrbuviewquery` |

---
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `hbss_hrbuviewquery`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `hbss_hrbuviewquery` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `hbss_hrbuviewquery` |

