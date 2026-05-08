# haos_adminorg_t_haos_adminorg 系列 — 操作清单（合并去重）
**涵盖实体数**: 18  **去重后操作数**: 99
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── hbp_bd_tpl_all (HR基础资料全页面模板)        └── hbp_histimeseqtpl (HR基础资料全页面历史模板)            ├── haos_adminorg (组织基本信息（主）)            │   ├── haos_adminorgdetail (组织快速维护)            │   │   ├── haos_adminorgbatch (上级行政组织-批量调整)            │   │   ├── haos_adminorgdetailf7 (组织最新版本F7（禁使用）)            │   │   ├── haos_adminorgdetailfuture (未来生效组织)            │   │   ├── haos_adminorgdetailinit (行政组织初始化)            │   │   ├── haos_adminorgf7 (组织历史版本F7（禁使用）)            │   │   ├── haos_adminorghis (组织历史查询)            │   │   ├── haos_adminorghishr (行政组织F7(历史数据))            │   │   ├── haos_structorgdetail (矩阵组织详情)            │   │   └── haos_virtualorg_f7 (虚拟组织-上级F7)            │   ├── haos_adminorghrf7 (HR组织)            │   │   └── hrptmc_adminorghr ()            │   ├── haos_adminorgteam (组织团队F7(控权))            │   ├── haos_adminorgteamnoperm (组织团队F7(不控权))            │   └── haos_othadminorg (其他形态组织)            ├── haos_orgdetail (行政组织（单据使用）)            └── haos_virtualorgdetail (虚拟组织详情)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 13 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 14 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 15 |
| 4 | `b599405400001aac` | save | 保存 | save | 8 | 1 | 4 | 13 |
| 5 | `b599405400001bac` | delete | 删除 | delete | 3 | 3 | 1 | 15 |
| 6 | `b599405400001cac` | audit | 审核 | audit | 2 | 3 | 2 | 15 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | 1 | 3 | 1 | 15 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | 1 | 1 | 1 | 15 |
| 9 | `b599405400001fac` | enable | 启用 | enable | 1 | 1 | 1 | 15 |
| 10 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 15 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 15 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | 6 | 6 | 1 | 15 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 1 | 2 | 1 | 15 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 13 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 16 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 15 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 14 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 18 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 14 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 14 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 18 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 14 |
| 23 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 14 |
| 24 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 14 |
| 25 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 13 |
| 26 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 15 |
| 27 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 18 |
| 28 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 18 |
| 29 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 15 |
| 30 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 14 |
| 31 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 15 |
| 32 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 18 |
| 33 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 15 |
| 34 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 18 |
| 35 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 18 |
| 36 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 14 |
| 37 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 14 |
| 38 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 14 |
| 39 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 18 |
| 40 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 18 |
| 41 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 15 |
| 42 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 15 |
| 43 | `20L86E0+8SH6` | donothing | 变更 | change | 0 | 0 | 1 | 13 |
| 44 | `20L88J=0T27O` | donothing | 历史版本信息 | hisversioninfo | 0 | 0 | 1 | 13 |
| 45 | `20L8N=DTT64=` | donothing | 复制 | hiscopy | 0 | 0 | 1 | 13 |
| 46 | `2BL9MSK5=KD0` | donothing | 新增数据版本 | newhisversion | 0 | 0 | 1 | 14 |
| 47 | `2NXYUXE2FUK1` | donothing | 修订 | revise | 0 | 0 | 1 | 14 |
| 48 | `2NXZ7QU+WWL3` | donothing | 版本修订历史 | reviserecord | 0 | 0 | 1 | 14 |
| 49 | `2P7PDHRSE6+7` | donothing | 版本对比 | versionchangecompare | 0 | 0 | 1 | 14 |
| 50 | `2XZ49B86ARJH` | donothing | 查看所有版本 | showallversion | 0 | 0 | 1 | 14 |
| 51 | `4UXPMBA2=U8J` | save | 确认变更 | confirmchange | 3 | 1 | 3 | 16 |
| 52 | `2HKUESDNMBVG` | donothing | 空操作 | donothing_viewhis | 0 | 0 | 1 | 4 |
| 53 | `501D=E92W8Y+` | donothing | 历史补录 | his_save | 1 | 0 | 1 | 15 |
| 54 | `4SSY4=EVAMB2` | donothing | 调整上级 | parentchg | 0 | 0 | 1 | 8 |
| 55 | `4SSY68KRM70V` | donothing | 变更信息 | infochg | 0 | 0 | 1 | 8 |
| 56 | `4SWIFFAU2Y9G` | newentry | 增行 | newentry | 0 | 0 | 1 | 8 |
| 57 | `4SWIGT1X00A2` | deleteentry | 删行 | deleteentry | 0 | 0 | 1 | 8 |
| 58 | `4T8H83EBU35S` | donothing | 停用组织 | disableorg | 1 | 0 | 1 | 8 |
| 59 | `4T8HBGWFI7EQ` | donothing | 启用组织 | enableorg | 1 | 0 | 1 | 8 |
| 60 | `4TZ7AP=EW=EC` | donothing | 部门负责人设置 | showchargeperson | 0 | 0 | 1 | 8 |
| 61 | `4U02UOYRP6KW` | donothing | 查看部门负责人修改日志 | viewchargepersonlog | 0 | 0 | 1 | 8 |
| 62 | `4U02XRTUF/5L` | importdata | 导入部门负责人 | importchargeperson | 0 | 0 | 1 | 10 |
| 63 | `4U03/80K37N8` | donothing | 查看部门负责人导入结果 | importdetailchargeperson | 0 | 0 | 1 | 10 |
| 64 | `4USCHCQ0UF62` | donothing | 查看组织变动异步处理结果 | operate_steps | 0 | 0 | 1 | 8 |
| 65 | `4VUGNAH8W6YU` | donothing | 查看历史记录 | viewhischange | 0 | 0 | 1 | 8 |
| 66 | `4Z00U5O3ATG+` | donothing | 重置根节点 | rootreset | 1 | 0 | 1 | 8 |
| 67 | `5/M3FMV=4508` | importdata_hr | 导入数据 | chargepersonimpo_hr | 0 | 0 | 1 | 10 |
| 68 | `57IZFW/6CHCK` | donothing | 取消 | cancel | 0 | 0 | 1 | 10 |
| 69 | `5BOKEJ6RJCEN` | new | 新增组织 | addnew | 0 | 0 | 1 | 10 |
| 70 | `5IJ+TIHVCOC+` | donothing | 排序 | sort | 0 | 0 | 1 | 10 |
| 71 | `535R1CY7ME1J` | donothing | 修改 | future_modify | 0 | 0 | 1 | 1 |
| 72 | `53VWOPA9J37P` | newentry | 增行 | newentry | 0 | 0 | 1 | 1 |
| 73 | `53VWQ/WWB6QT` | deleteentry | 删行 | deleteentry | 0 | 0 | 1 | 1 |
| 74 | `53WDI8J3Y35T` | donothing | 保存 | future_save_add | 1 | 0 | 1 | 1 |
| 75 | `53WDJU1BL=25` | donothing | 保存 | future_save_enable | 1 | 0 | 1 | 1 |
| 76 | `53WDLO=S8XM4` | donothing | 保存 | future_save_disable | 1 | 0 | 1 | 1 |
| 77 | `53WDMX+JP3V3` | donothing | 保存 | future_save_change | 1 | 0 | 1 | 1 |
| 78 | `548Q4CTMG3SE` | donothing | 撤销 | future_revoke | 1 | 0 | 1 | 1 |
| 79 | `548Q=IFDDEQG` | donothing | 重试 | future_retry | 1 | 0 | 1 | 1 |
| 80 | `54BE/1S75D34` | donothing | 生效-新增 | future_effect_add | 1 | 0 | 1 | 1 |
| 81 | `54BE2G9RX2FR` | donothing | 生效-变更 | future_effect_change | 1 | 0 | 1 | 1 |
| 82 | `5DKNWP/WSQR/` | donothing | 从行政组织添加 | donothing_ot_addbyorg | 0 | 0 | 1 | 1 |
| 83 | `5DKO87GQP7R1` | donothing | 变更 | donothing_ot_change | 0 | 0 | 1 | 1 |
| 84 | `5GIDKWLTSBKI` | modify | 修改 | changemodify | 0 | 0 | 1 | 1 |
| 85 | `5HARVTT5H3SW` | donothing | 从行政组织表单添加（仅校验） | addnodehr | 0 | 0 | 1 | 1 |
| 86 | `3CZAPNCDDG7B` | donothing | 架构维护 | struct_edit | 0 | 0 | 1 | 1 |
| 87 | `3CZB+JNP2=4P` | donothing | 架构图查看 | orgchart | 0 | 0 | 1 | 1 |
| 88 | `3GLI+P0Y4PGG` | donothing | 导入保存 | import_save | 1 | 1 | 1 | 1 |
| 89 | `3I7DL634QH5+` | donothing | 变更 | modifybtn | 0 | 0 | 1 | 1 |
| 90 | `3I7EOCU=ZZRK` | donothing | 保存 | savebtn | 1 | 0 | 1 | 1 |
| 91 | `3JCFIJ=2OZ=2` | donothing | searchnode | searchnode | 0 | 0 | 1 | 1 |
| 92 | `532X+6ND57YS` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 1 |
| 93 | `532X5SD0WQ06` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 1 |
| 94 | `532X85EHC3I/` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 1 |
| 95 | `532X9JEYPU8+` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 1 |
| 96 | `532XDU1EC2J=` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 1 |
| 97 | `532XFUDVLPHM` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 1 |
| 98 | `3HRIMZNN73V9` | donothing | 变更 | modify | 0 | 0 | 1 | 1 |
| 99 | `3HRIOCQ69DQO` | donothing | 保存 | save | 1 | 1 | 1 | 1 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (13): `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `hrptmc_adminorghr`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_virtualorg_f7`, `haos_adminorgteam`, `haos_othadminorg` | **4 个插件变体**

#### 变体 1（5 个实体）
> 实体: `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 6 | 6 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 7 | 7 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 3 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |


#### 变体 2（6 个实体）
> 实体: `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 5 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 6 | 6 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 7 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 8 | 8 | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp` | kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |


#### 变体 3（1 个实体）
> 实体: `haos_adminorgteam`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 6 | 6 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 7 | 7 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 8 | 8 | `haos_adminorgteam` | `kd.hr.haos.opplugin.web.orgfast.CusOrgSaveOp` | kd.hr.haos.opplugin.web.orgfast.CusOrgSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 3 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |


#### 变体 4（1 个实体）
> 实体: `haos_othadminorg`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 5 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 6 | 6 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 7 | 7 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 8 | 8 | `haos_othadminorg` | `kd.hr.haos.opplugin.web.otherstruct.othorg.OtherOrgSaveOp` | 其他形态组织保存 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 3 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 3 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `hbp_bd_tpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `hrptmc_adminorghr`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_virtualorg_f7` | **2 个插件变体**

#### 变体 1（7 个实体）
> 实体: `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |


#### 变体 2（8 个实体）
> 实体: `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 2 | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp` | kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
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
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
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

---
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailfuture`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
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
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl` | 涉及实体 (14): `haos_adminorg`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_othadminorg`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 51. 确认变更（save / Key: confirmchange / oid: 4UXPMBA2=U8J）
> 根定义: `hbp_histimeseqtpl` | 涉及实体 (16): `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_virtualorgdetail`, `hrptmc_adminorghr`, `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_structorgdetail`, `haos_virtualorg_f7`, `haos_othadminorg` | **3 个插件变体**

#### 变体 1（7 个实体）
> 实体: `haos_adminorg`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_orgdetail`, `haos_virtualorgdetail`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UXPTMTC=NCR` | 字段值合规性校验 | `hbp_histimeseqtpl` |
| 2 | GrpfieldsuniqueValidation | `4UXPUVV/1NPV` | 编码唯一性校验 | `hbp_histimeseqtpl` |
| 3 | GrpfieldsuniqueValidation | `4UXPW=XL18TM` | 名称唯一性校验 | `hbp_histimeseqtpl` |


#### 变体 2（8 个实体）
> 实体: `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_structorgdetail`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 3 | 2 | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp` | kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UXPTMTC=NCR` | 字段值合规性校验 | `hbp_histimeseqtpl` |


#### 变体 3（1 个实体）
> 实体: `haos_othadminorg`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_histimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验 |
| 3 | 2 | `haos_othadminorg` | `kd.hr.haos.opplugin.web.otherstruct.othorg.OtherOrgConfirmChangeNewOp` | 组织变更 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4UXPTMTC=NCR` | 字段值合规性校验 | `hbp_histimeseqtpl` |
| 2 | GrpfieldsuniqueValidation | `4UXPUVV/1NPV` | 编码唯一性校验 | `hbp_histimeseqtpl` |
| 3 | GrpfieldsuniqueValidation | `4UXPW=XL18TM` | 名称唯一性校验 | `hbp_histimeseqtpl` |

---
### 53. 历史补录（donothing / Key: his_save / oid: 501D=E92W8Y+）
> 根定义: `haos_adminorg` | 涉及实体 (15): `haos_adminorg`, `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghis`, `haos_adminorghishr`, `haos_adminorghrf7`, `haos_adminorgteam`, `haos_adminorgteamnoperm`, `haos_othadminorg`, `haos_structorgdetail`, `haos_virtualorg_f7`, `hrptmc_adminorghr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorg` | `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp` | 组织历史补录op |

---
### 58. 停用组织（donothing / Key: disableorg / oid: 4T8H83EBU35S）
> 根定义: `haos_adminorgdetail` | 涉及实体 (8): `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_structorgdetail`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp` | 停用组织op插件 |

---
### 59. 启用组织（donothing / Key: enableorg / oid: 4T8HBGWFI7EQ）
> 根定义: `haos_adminorgdetail` | 涉及实体 (8): `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_structorgdetail`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp` | 启用组织 |

---
### 66. 重置根节点（donothing / Key: rootreset / oid: 4Z00U5O3ATG+）
> 根定义: `haos_adminorgdetail` | 涉及实体 (8): `haos_adminorgbatch`, `haos_adminorgdetail`, `haos_adminorgdetailf7`, `haos_adminorgdetailinit`, `haos_adminorgf7`, `haos_adminorghishr`, `haos_structorgdetail`, `haos_virtualorg_f7`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetail` | `kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp` | 行政组织重置根节点op插件 |

---
### 74. 保存（donothing / Key: future_save_add / oid: 53WDI8J3Y35T）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.AdminOrgFutureModifyAddOp` | 新增数据修改 |

---
### 75. 保存（donothing / Key: future_save_enable / oid: 53WDJU1BL=25）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.AdminOrgFutureModifyEnableOp` | 启用数据修改 |

---
### 76. 保存（donothing / Key: future_save_disable / oid: 53WDLO=S8XM4）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.AdminOrgFutureModifyDisableOp` | 禁用数据修改 |

---
### 77. 保存（donothing / Key: future_save_change / oid: 53WDMX+JP3V3）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.AdminOrgFutureModifyChangeOp` | 变更数据修改 |

---
### 78. 撤销（donothing / Key: future_revoke / oid: 548Q4CTMG3SE）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.revoke.AdminOrgFutureRevokeOp` | 待生效组织撤销 |

---
### 79. 重试（donothing / Key: future_retry / oid: 548Q=IFDDEQG）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.retry.AdminOrgFutureRetryOp` | 待生效组织重试 |

---
### 80. 生效-新增（donothing / Key: future_effect_add / oid: 54BE/1S75D34）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.effect.AdminOrgFutureAddEffectOp` | 新增数据生效 |

---
### 81. 生效-变更（donothing / Key: future_effect_change / oid: 54BE2G9RX2FR）
> 根定义: `haos_adminorgdetailfuture` | 涉及实体 (1): `haos_adminorgdetailfuture`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgdetailfuture` | `kd.hr.haos.opplugin.web.future.effect.AdminOrgFutureChangeEffectOp` | 生效-变更 |

---
### 88. 导入保存（donothing / Key: import_save / oid: 3GLI+P0Y4PGG）
> 根定义: `haos_structorgdetail` | 涉及实体 (1): `haos_structorgdetail`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_structorgdetail` | `kd.hr.haos.opplugin.web.structproject.OtherStructProjectOrgImportSaveOp` | kd.hr.haos.opplugin.web.structproject.OtherStructProjectOrgImportSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `3HAMS71B8WED` | 字段值合规性校验 | `haos_structorgdetail` |

---
### 90. 保存（donothing / Key: savebtn / oid: 3I7EOCU=ZZRK）
> 根定义: `haos_structorgdetail` | 涉及实体 (1): `haos_structorgdetail`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_structorgdetail` | `kd.hr.haos.opplugin.web.structproject.StructOrgDetailSaveOp` | kd.hr.haos.opplugin.web.structproject.StructOrgDetailSaveOp |

---
### 99. 保存（donothing / Key: save / oid: 3HRIOCQ69DQO）
> 根定义: `haos_virtualorgdetail` | 涉及实体 (1): `haos_virtualorgdetail`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_virtualorgdetail` | `kd.hr.haos.opplugin.web.structproject.VirtualOrgDetailSaveOp` | kd.hr.haos.opplugin.web.structproject.VirtualOrgDetailSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `3I38GXIVZVMF` | 字段值合规性校验 | `haos_virtualorgdetail` |

