# haos_othroletpl — 其他形态组织-角色库 操作清单
**继承链**: `hbp_bd_originaltpl` → `hbp_bd_originaldlgtpl` → `haos_othroletpl`  **操作数**: 32
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originaltpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originaltpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originaltpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originaltpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originaltpl` | 0 | 0 |
| 6 | `36XNX2J0IYJ/` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 7 | `36XNYXBDN8US` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 8 | `36XO+0YA=NQ1` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 9 | `36XO/49Y36PX` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 10 | `36XO07P0TYXD` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 11 | `36XO16LFA2/E` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 12 | `b5994054000016ac` | new | 新增 | new | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 13 | `b5994054000017ac` | modify | 修改 | modify | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 14 | `b5994054000018ac` | view | 查看 | view | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 15 | `b599405400001aac` | save | 保存 | save | `hbp_bd_originaldlgtpl` | 0 | 2 |
| 16 | `b599405400001bac` | delete | 删除 | delete | `hbp_bd_originaldlgtpl` | 1 | 1 |
| 17 | `b5994054000021ac` | close | 关闭 | close | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 18 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 19 | `f381c03f000033ac` | copy | 复制 | copy | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 20 | `f381c03f000034ac` | refresh | 刷新 | refresh | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 21 | `c8d21820000086ac` | option | 选项设置 | option | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 22 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 23 | `S=VTOGXYQBY` | first | 第一 | first | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 24 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 25 | `S=VVFX/B38V` | next | 后一 | next | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 26 | `S=VW0K6WD7R` | last | 最后 | last | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 27 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 28 | `VZIWYQT9HE3` | importdetails | 引入详情 | importdetails | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 29 | `VZIX=AIFOYM` | importtemplatelist | 引入模板列表 | importtemplatelist | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 30 | `10MEU32VZDCP` | exportlist | 引出数据 | exportlist | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 31 | `13NUAIBA368/` | exportlist | 引出-按列表字段引出 | exportlistbyselectfields | `hbp_bd_originaldlgtpl` | 0 | 0 |
| 32 | `5CVU8YH1Q7IT` | donothing | 保存 | btlsave | `haos_othroletpl` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 15. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `hbp_bd_originaldlgtpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 必录校验 | `hbp_bd_originaldlgtpl` |
| 2 | ConditionValidation | `1a51dc0a0000a1ac` | 合法性校验 | `hbp_bd_originaldlgtpl` |

---
### 16. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `hbp_bd_originaldlgtpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_originaldlgtpl` | `kd.hr.hbp.opplugin.web.HRDataBaseOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f789ca66000000ac` | 合法性校验 | `hbp_bd_originaldlgtpl` |

