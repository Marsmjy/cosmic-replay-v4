# haos_chargeperson_t_haos_chargeperson 系列 — 操作清单（合并去重）
**涵盖实体数**: 2  **去重后操作数**: 42
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```hbp_bd_originalmintpl (HR原生基础资料最小模板)    └── haos_chargeperson (部门负责人)        └── haos_chargeperson_add ()```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | 0 | 0 | 1 | 2 |
| 6 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 7 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 8 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 9 | `36XNX2J0IYJ/` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 2 |
| 10 | `36XNYXBDN8US` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 11 | `36XO+0YA=NQ1` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 12 | `36XO/49Y36PX` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 13 | `36XO07P0TYXD` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 2 |
| 14 | `36XO16LFA2/E` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 2 |
| 15 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 2 |
| 16 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 2 |
| 17 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 2 |
| 18 | `b599405400001bac` | delete | 删除 | delete | 1 | 1 | 1 | 2 |
| 19 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 2 |
| 20 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 2 |
| 21 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 2 |
| 22 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 2 |
| 23 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 2 |
| 24 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 2 |
| 25 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 26 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 27 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 28 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 29 | `VZIV=BOH9KA` | importdata | 引入数据 | importdata | 0 | 0 | 1 | 2 |
| 30 | `VZIWYQT9HE3` | importdetails | 引入详情 | importdetails | 0 | 0 | 1 | 2 |
| 31 | `VZIX=AIFOYM` | importtemplatelist | 引入模板列表 | importtemplatelist | 0 | 0 | 1 | 2 |
| 32 | `10MEU32VZDCP` | exportlist | 导出数据 | exportlist | 0 | 0 | 1 | 2 |
| 33 | `13NUAIBA368/` | exportlist | 导出-按列表字段导出 | exportlistbyselectfields | 0 | 0 | 1 | 2 |
| 34 | `34+SZTPWP6XC` | save | 保存 | save | 1 | 0 | 1 | 2 |
| 35 | `36K3MGO+Z125` | donothing | 取消负责人 | donothing_removecharge | 0 | 0 | 1 | 2 |
| 36 | `36K8EOXYC8FZ` | donothing | 取消负责人 | donothing_removeconfirm | 1 | 0 | 1 | 2 |
| 37 | `37Q+9JPCB6E0` | donothing | 添加负责人 | donothing_new | 0 | 0 | 1 | 2 |
| 38 | `37QQ4EW8GLZ9` | donothing | 保存 | donothing_savenew | 1 | 0 | 1 | 2 |
| 39 | `39R7HWX35716` | donothing | 查看日志 | viewchargepersonlog | 0 | 0 | 1 | 2 |
| 40 | `3AJCM2PKUEBC` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 2 |
| 41 | `5/EZ3STHD7HZ` | donothing | 作废部门负责人 | donothing_discard | 1 | 0 | 1 | 2 |
| 42 | `5DCFL56L0WGS` | donothing | 删除部门负责人 | donothing_delete | 1 | 0 | 1 | 2 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 18. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.hbp.opplugin.web.HRDataBaseOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f789ca66000000ac` | 合法性校验 | `haos_chargeperson` |

---
### 34. 保存（save / Key: save / oid: 34+SZTPWP6XC）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.haos.opplugin.web.ChargePersonSaveOp` | kd.hr.haos.opplugin.web.ChargePersonSaveOp |

---
### 36. 取消负责人（donothing / Key: donothing_removeconfirm / oid: 36K8EOXYC8FZ）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.haos.opplugin.web.ChargePersonRemoveOp` | 部门负责人取消op |

---
### 38. 保存（donothing / Key: donothing_savenew / oid: 37QQ4EW8GLZ9）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.haos.opplugin.web.ChargePersonSaveOp` | kd.hr.haos.opplugin.web.ChargePersonSaveOp |

---
### 41. 作废部门负责人（donothing / Key: donothing_discard / oid: 5/EZ3STHD7HZ）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.haos.opplugin.web.ChargePersonDiscardOp` | 作废部门负责人 |

---
### 42. 删除部门负责人（donothing / Key: donothing_delete / oid: 5DCFL56L0WGS）
> 根定义: `haos_chargeperson` | 涉及实体 (2): `haos_chargeperson`, `haos_chargeperson_add`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_chargeperson` | `kd.hr.haos.opplugin.web.ChargePersonDeleteOp` | 删除部门负责人op |

