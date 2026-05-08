# hrcs_keywordmapping — 模板变量取值关系配置 操作清单
**继承链**: `hrcs_keywordmapping`  **操作数**: 5
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `2AWM+/+7AXIQ` | save | 保存 | save | `hrcs_keywordmapping` | 1 | 2 |
| 2 | `2AWM0=GAACRE` | view | 查看 | view | `hrcs_keywordmapping` | 0 | 0 |
| 3 | `2AXKSIC28YDB` | donothing | 自定义操作 | showkeywordf7 | `hrcs_keywordmapping` | 0 | 0 |
| 4 | `2CB2/J1/23AD` | close | 关闭 | close | `hrcs_keywordmapping` | 0 | 0 |
| 5 | `3FF=K4=XBC+U` | donothing | 自定义 | customize | `hrcs_keywordmapping` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 1. 保存（save / Key: save / oid: 2AWM+/+7AXIQ）
> 根定义: `hrcs_keywordmapping`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_keywordmapping` | `kd.hr.hrcs.opplugin.web.KeywordMappingOp` | KeywordMappingOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2CB5MJX7KDDE` | 字段值合规性校验 | `hrcs_keywordmapping` |
| 2 | GrpfieldsuniqueValidation | `2E3DLSADYWSC` | 组合字段唯一性校验 | `hrcs_keywordmapping` |

