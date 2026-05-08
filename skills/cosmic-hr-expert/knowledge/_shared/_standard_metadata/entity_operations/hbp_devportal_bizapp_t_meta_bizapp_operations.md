# hbp_devportal_bizapp_t_meta_bizapp 系列 — 操作清单（合并去重）
**涵盖实体数**: 2  **去重后操作数**: 4
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_devportal_bizapp (业务应用实体)    └── hbp_devportal_bizapp (HR业务应用实体)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `0d903c06000006ac` | save | 保存 | save | 0 | 0 | 1 | 2 |
| 2 | `10Q973PDQXMP` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 2 |
| 3 | `11EKFC5Q53AK` | close | 关闭 | close | 0 | 0 | 1 | 2 |
| 4 | `18X++8MRE4OF` | donothing | 空操作 | donothing | 0 | 1 | 1 | 2 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 空操作（donothing / Key: donothing / oid: 18X++8MRE4OF）
> 根定义: `bos_devportal_bizapp` | 涉及实体 (2): `bos_devportal_bizapp`, `hbp_devportal_bizapp`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | RegexValidation | `18X+YY12K4B0` | 简码必须大写英文或数字且长度不能超过4位 | `bos_devportal_bizapp` |

