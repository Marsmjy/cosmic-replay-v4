# hbpm_positionhr_t_hbpm_position 系列 — 操作清单（合并去重）
**涵盖实体数**: 9  **去重后操作数**: 95
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── hbp_bd_tpl_all (HR基础资料全页面模板)        └── hbp_histimeseqtpl (HR基础资料全页面历史模板)            ├── hbpm_positionhr (岗位信息维护)            │   ├── hbpm_orgpositionlist (组织下岗位清单)            │   ├── hbpm_position_future (待生效岗位)            │   ├── hbpm_positiondetailrevise (岗位修订详情)            │   ├── hbpm_positionhis (岗位历史查询)            │   ├── hbpm_positionhrf7 (HR岗位)            │   ├── hbpm_positionlist (职位下岗位清单)            │   └── hbpm_subposition (下属岗位)            └── hbpm_stposition (标准岗位)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 5 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 5 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 6 |
| 4 | `b599405400001aac` | save | 保存 | save | 8 | 6 | 2 | 5 |
| 5 | `b599405400001eac` | disable | 禁用 | disable | 2 | 0 | 2 | 5 |
| 6 | `b599405400001fac` | enable | 启用 | enable | 2 | 0 | 2 | 5 |
| 7 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 6 |
| 8 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 6 |
| 9 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 5 |
| 10 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 6 |
| 11 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 6 |
| 12 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 6 |
| 13 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 1 | 0 | 2 | 9 |
| 14 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 6 |
| 15 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 6 |
| 16 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 9 |
| 17 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 6 |
| 18 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 5 |
| 19 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 5 |
| 20 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 5 |
| 21 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 6 |
| 22 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 9 |
| 23 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 9 |
| 24 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 6 |
| 25 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 6 |
| 26 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 6 |
| 27 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 9 |
| 28 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 6 |
| 29 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 9 |
| 30 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 9 |
| 31 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 6 |
| 32 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 4 |
| 33 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 4 |
| 34 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 9 |
| 35 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 9 |
| 36 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 5 |
| 37 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 5 |
| 38 | `20L8N=DTT64=` | donothing | 复制 | hiscopy | 0 | 0 | 1 | 5 |
| 39 | `2BL9MSK5=KD0` | donothing | 新增数据版本 | newhisversion | 0 | 0 | 1 | 5 |
| 40 | `2NXYUXE2FUK1` | donothing | 修订 | revise | 0 | 0 | 1 | 4 |
| 41 | `2NXZ7QU+WWL3` | donothing | 版本修订历史 | reviserecord | 0 | 0 | 1 | 4 |
| 42 | `2P7PDHRSE6+7` | donothing | 版本对比 | versionchangecompare | 0 | 0 | 1 | 5 |
| 43 | `2XZ49B86ARJH` | donothing | 查看所有版本 | showallversion | 0 | 0 | 1 | 5 |
| 44 | `4UXPMBA2=U8J` | save | 确认变更 | confirmchange | 3 | 1 | 2 | 8 |
| 45 | `4SPVH4FU6/K4` | donothing | 查看历史记录 | hisversion_view | 0 | 0 | 1 | 8 |
| 46 | `4SPZP/GC1VZW` | importdata | 协作关系导入 | relationimport | 0 | 0 | 1 | 8 |
| 47 | `4SQ+/Z3B8G7Z` | importdata | 修订导入数据 | reviseimport | 0 | 0 | 1 | 8 |
| 48 | `4SQERIPKZE9U` | donothing | 协作关系变更 | reportchange | 0 | 0 | 1 | 4 |
| 49 | `4SQMYM6KYP81` | newentry | 新增分录 | newentry | 0 | 0 | 1 | 4 |
| 50 | `4SQN4G3BFFQ7` | deleteentry | 删除分录 | deleteentry | 0 | 0 | 1 | 4 |
| 51 | `4T7SFFXU9XDN` | donothing | 汇报关系确认变更 | dochangerelation | 1 | 1 | 1 | 5 |
| 52 | `4V41MHP86Y1X` | donothing | 变更 | change | 0 | 0 | 1 | 7 |
| 53 | `4WEQ42SU2232` | donothing | 协作关系变更退出 | do_close | 0 | 0 | 1 | 7 |
| 54 | `4ZH6HWHKM=DS` | importdata_hr | 批量修订属性信息 | import_positiondetailrevise | 0 | 0 | 1 | 8 |
| 55 | `5/2GRHBL33SR` | donothing | 历史保存 | his_save | 1 | 0 | 1 | 8 |
| 56 | `56=7H1EBIKUY` | selecttplprint | 打印岗位说明书 | selecttplprint | 0 | 0 | 1 | 8 |
| 57 | `5J/UVCM2BGR8` | donothing | 排序 | sort | 0 | 0 | 1 | 8 |
| 58 | `4SWQJD6QUQRT` | donothing | 撤销 | revoke | 1 | 0 | 1 | 1 |
| 59 | `4SWQPS/Y5CAO` | donothing | 重试 | retry | 1 | 0 | 1 | 1 |
| 60 | `4SWR6FRRG9GY` | donothing | 保存 | future_save | 1 | 0 | 1 | 1 |
| 61 | `4SWR8XVKRT+G` | donothing | 修改 | future_modify | 0 | 0 | 1 | 1 |
| 62 | `526J00TM/PWP` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 1 |
| 63 | `53PJC1QMIS9/` | donothing | 生效 | effect | 1 | 0 | 1 | 1 |
| 64 | `53T7PMBDZ30I` | newentry | 新增分录 | newentry | 0 | 0 | 1 | 1 |
| 65 | `53T7SDATMUCH` | deleteentry | 删除分录 | deleteentry | 0 | 0 | 1 | 1 |
| 66 | `4SWFR=UF+GIG` | donothing | 保存 | revisesave | 1 | 1 | 1 | 1 |
| 67 | `4SWFVVIB4KF+` | donothing | 取消 | revisecancel | 0 | 0 | 1 | 1 |
| 68 | `4SWFY+SDODA=` | donothing | 修订 | reviseedit | 0 | 0 | 1 | 1 |
| 69 | `4SWFZCJQMVJY` | donothing | 查看修订日志 | reviselog | 0 | 0 | 1 | 1 |
| 70 | `4ZK/G/OM69RA` | donothing | 岗位修订导入操作 | reviseimportsave | 1 | 0 | 1 | 1 |
| 71 | `5/+U9M=YDCKR` | save | 保存 | save | 0 | 0 | 1 | 1 |
| 72 | `b599405400001bac` | delete | 删除 | delete | 3 | 4 | 1 | 1 |
| 73 | `b599405400001cac` | audit | 审核 | audit | 2 | 3 | 1 | 1 |
| 74 | `b599405400001dac` | unaudit | 反审核 | unaudit | 1 | 3 | 1 | 1 |
| 75 | `f381c03f00002cac` | submit | 提交 | submit | 6 | 7 | 1 | 1 |
| 76 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 1 | 2 | 1 | 1 |
| 77 | `20L86E0+8SH6` | donothing | 变更 | change | 0 | 0 | 1 | 1 |
| 78 | `20L88J=0T27O` | donothing | 历史版本信息 | hisversioninfo | 0 | 0 | 1 | 1 |
| 79 | `0DACD6GQT/MR` | donothing | 空操作 | individuation | 0 | 0 | 1 | 1 |
| 80 | `36NMATCD+40R` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 1 |
| 81 | `36NMCH7J7K17` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 1 |
| 82 | `36NMEU7ZJ64A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 1 |
| 83 | `36NMFZKH+JK4` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 1 |
| 84 | `36NMHG0I3HKP` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 1 |
| 85 | `36NMIYKCRQOF` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 1 |
| 86 | `2NY+GRV0I75K` | donothing | 修订 | revise | 1 | 0 | 1 | 1 |
| 87 | `2NY+IA24TJAH` | donothing | 版本修订历史 | reviserecord | 0 | 0 | 1 | 1 |
| 88 | `2P7P1YQ616DW` | donothing | 版本对比 | versionchangecompare | 0 | 0 | 1 | 1 |
| 89 | `2XZ45+=QLS0M` | donothing | 查看所有版本 | showallversion | 0 | 0 | 1 | 1 |
| 90 | `24EUGEIZ4SF+` | newentry | 新增分录 | newentry | 0 | 0 | 1 | 1 |
| 91 | `24EUJ0KNRTCH` | deleteentry | 减行 | deleteentry | 0 | 0 | 1 | 1 |
| 92 | `4T4I5+HQGVPF` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 1 |
| 93 | `4T4I9RWEYFU8` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 1 |
| 94 | `5D/Q5J0CX7+V` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 1 |
| 95 | `5D/Q7GW=F8IQ` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 1 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (5): `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_stposition` | **2 个插件变体**

#### 变体 1（4 个实体）
> 实体: `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 2 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 4 | 6 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 5 | 7 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 6 | 7 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 7 | 9 | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp` | 岗位保存 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |


#### 变体 2（1 个实体）
> 实体: `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 5 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 6 | 6 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 7 | 7 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 8 | 8 | `hbpm_stposition` | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp` | 标准岗位保存 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 3 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `0+/SL/MZ=VJB` | 创建组织和编码组合字段唯一性校验 | `hbpm_stposition` |
| 5 | GrpfieldsuniqueValidation | `3D3G/MZ2++JS` | 名称唯一性校验 | `hbpm_stposition` |
| 6 | GrpfieldsuniqueValidation | `24EX6EQ+GDLF` | 单据体唯一性校验 | `hbpm_stposition` |

---
### 5. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (5): `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_stposition` | **2 个插件变体**

#### 变体 1（4 个实体）
> 实体: `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp` | 岗位禁用 |
| 2 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |


#### 变体 2（1 个实体）
> 实体: `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbpm_stposition` | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp` | StandardPositionDisableOp |
| 2 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |

---
### 6. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (5): `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_stposition` | **2 个插件变体**

#### 变体 1（4 个实体）
> 实体: `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp` | 岗位启用 |
| 2 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |


#### 变体 2（1 个实体）
> 实体: `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbpm_stposition` | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionEnableOp` | 标准岗位启用 |
| 2 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `25H6VX0YNAW=` | 启用状态校验 | `hbpm_stposition` |

---
### 12. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl` | 涉及实体 (6): `hbpm_orgpositionlist`, `hbpm_positionhis`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_stposition`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 13. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl` | 涉及实体 (9): `hbpm_orgpositionlist`, `hbpm_position_future`, `hbpm_positiondetailrevise`, `hbpm_positionhis`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`, `hbpm_stposition` | **2 个插件变体**

#### 变体 1（8 个实体）
> 实体: `hbpm_orgpositionlist`, `hbpm_position_future`, `hbpm_positiondetailrevise`, `hbpm_positionhis`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`

#### 变体 2（1 个实体）
> 实体: `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_stposition` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

---
### 44. 确认变更（save / Key: confirmchange / oid: 4UXPMBA2=U8J）
> 根定义: `hbp_histimeseqtpl` | 涉及实体 (8): `hbpm_orgpositionlist`, `hbpm_position_future`, `hbpm_positiondetailrevise`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`, `hbpm_stposition` | **2 个插件变体**

#### 变体 1（7 个实体）
> 实体: `hbpm_orgpositionlist`, `hbpm_position_future`, `hbpm_positiondetailrevise`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 3 | 2 | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp` | 岗位变更 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UXPTMTC=NCR` | 字段值合规性校验 | `hbp_histimeseqtpl` |


#### 变体 2（1 个实体）
> 实体: `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 3 | 2 | `hbpm_stposition` | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp` | 岗位确认变更操作 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UXPTMTC=NCR` | 字段值合规性校验 | `hbp_histimeseqtpl` |
| 2 | GrpfieldsuniqueValidation | `4UXPUVV/1NPV` | 编码唯一性校验 | `hbp_histimeseqtpl` |
| 3 | GrpfieldsuniqueValidation | `4UXPW=XL18TM` | 名称唯一性校验 | `hbp_histimeseqtpl` |

---
### 51. 汇报关系确认变更（donothing / Key: dochangerelation / oid: 4T7SFFXU9XDN）
> 根定义: `hbpm_positionhr` | 涉及实体 (5): `hbpm_orgpositionlist`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp` | 汇报关系变更插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4T7SDY=13RY6` | 字段值合规性校验 | `hbpm_positionhr` |

---
### 55. 历史保存（donothing / Key: his_save / oid: 5/2GRHBL33SR）
> 根定义: `hbpm_positionhr` | 涉及实体 (8): `hbpm_orgpositionlist`, `hbpm_position_future`, `hbpm_positiondetailrevise`, `hbpm_positionhis`, `hbpm_positionhr`, `hbpm_positionhrf7`, `hbpm_positionlist`, `hbpm_subposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_positionhr` | `kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp` | 岗位历史迁移OP |

---
### 58. 撤销（donothing / Key: revoke / oid: 4SWQJD6QUQRT）
> 根定义: `hbpm_position_future` | 涉及实体 (1): `hbpm_position_future`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_position_future` | `kd.hrmp.hbpm.opplugin.web.position.PositionFutureRevokeOp` | 岗位未来生效撤销插件 |

---
### 59. 重试（donothing / Key: retry / oid: 4SWQPS/Y5CAO）
> 根定义: `hbpm_position_future` | 涉及实体 (1): `hbpm_position_future`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_position_future` | `kd.hrmp.hbpm.opplugin.web.position.PositionFutureEffectOp` | 岗位未来生效插件 |

---
### 60. 保存（donothing / Key: future_save / oid: 4SWR6FRRG9GY）
> 根定义: `hbpm_position_future` | 涉及实体 (1): `hbpm_position_future`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_position_future` | `kd.hrmp.hbpm.opplugin.web.position.PositionFutureModifyOp` | 待生效版本修改 |

---
### 63. 生效（donothing / Key: effect / oid: 53PJC1QMIS9/）
> 根定义: `hbpm_position_future` | 涉及实体 (1): `hbpm_position_future`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_position_future` | `kd.hrmp.hbpm.opplugin.web.position.PositionFutureEffectOp` | 岗位未来生效op |

---
### 66. 保存（donothing / Key: revisesave / oid: 4SWFR=UF+GIG）
> 根定义: `hbpm_positiondetailrevise` | 涉及实体 (1): `hbpm_positiondetailrevise`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_positiondetailrevise` | `kd.hrmp.hbpm.opplugin.web.position.PositionReviseOp` | 岗位修订 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4SWFR3OHVCCF` | 字段值合规性校验 | `hbpm_positiondetailrevise` |

---
### 70. 岗位修订导入操作（donothing / Key: reviseimportsave / oid: 4ZK/G/OM69RA）
> 根定义: `hbpm_positiondetailrevise` | 涉及实体 (1): `hbpm_positiondetailrevise`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_positiondetailrevise` | `kd.hrmp.hbpm.opplugin.web.position.PositionReviseImportOp` | 岗位修订导入op |

---
### 72. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `hbp_bd_tpl_all` |
| 4 | CustValidation | `2+U=J7R7IEF/` | HR基础资料删除校验 | `hbpm_stposition` |

---
### 73. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 2 | `hbpm_stposition` | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionAuditOp` | 审核 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 74. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 75. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 4 | 3 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验器 |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 6 | 5 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `2DDGGGIVWM1U` | 名称唯一性校验 | `hbp_bd_tpl_all` |
| 5 | GrpfieldsuniqueValidation | `424b895300015bac` | 编码唯一性校验 | `hbp_histimeseqtpl` |
| 6 | GrpfieldsuniqueValidation | `28IW773QJDZ6` | 名称唯一性校验 | `hbp_histimeseqtpl` |
| 7 | GrpfieldsuniqueValidation | `createorg.id` |  | `hbpm_stposition` |

---
### 76. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 86. 修订（donothing / Key: revise / oid: 2NY+GRV0I75K）
> 根定义: `hbpm_stposition` | 涉及实体 (1): `hbpm_stposition`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbpm_stposition` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

