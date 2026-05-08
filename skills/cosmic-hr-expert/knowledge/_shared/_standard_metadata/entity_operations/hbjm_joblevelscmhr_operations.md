# hbjm_joblevelscmhr — 职级方案 操作清单
**继承链**: `bos_basetpl` → `bos_baseorgtpl` → `hbp_bd_orgtpl_all` → `hbp_bd_orgtpl_dlg` → `hbjm_bd_orgtpl_dlg` → `hbjm_joblevelscmhr`  **操作数**: 67
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `bos_basetpl` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `bos_basetpl` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `bos_basetpl` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `bos_basetpl` | 11 | 13 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `bos_basetpl` | 5 | 3 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `bos_basetpl` | 1 | 3 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `bos_basetpl` | 1 | 3 |
| 8 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 9 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `bos_basetpl` | 0 | 0 |
| 10 | `f381c03f00002cac` | submit | 提交 | submit | `bos_basetpl` | 5 | 5 |
| 11 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `bos_basetpl` | 2 | 2 |
| 12 | `f381c03f000033ac` | copy | 复制 | copy | `bos_basetpl` | 0 | 0 |
| 13 | `f381c03f000034ac` | refresh | 刷新 | refresh | `bos_basetpl` | 0 | 0 |
| 14 | `c8d21820000086ac` | option | 选项设置 | option | `bos_basetpl` | 0 | 0 |
| 15 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `bos_basetpl` | 0 | 2 |
| 16 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 1 | 0 |
| 17 | `S=VTOGXYQBY` | first | 第一 | first | `bos_basetpl` | 0 | 0 |
| 18 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `bos_basetpl` | 0 | 0 |
| 19 | `S=VVFX/B38V` | next | 后一 | next | `bos_basetpl` | 0 | 0 |
| 20 | `S=VW0K6WD7R` | last | 最后 | last | `bos_basetpl` | 0 | 0 |
| 21 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | `bos_basetpl` | 0 | 0 |
| 22 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | `bos_basetpl` | 0 | 0 |
| 23 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | `bos_basetpl` | 0 | 0 |
| 24 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | `bos_basetpl` | 0 | 0 |
| 25 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_basetpl` | 0 | 0 |
| 26 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_basetpl` | 0 | 0 |
| 27 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | `bos_basetpl` | 0 | 0 |
| 28 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | `bos_basetpl` | 0 | 0 |
| 29 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_basetpl` | 0 | 0 |
| 30 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_basetpl` | 0 | 0 |
| 31 | `2PVU302I5+FH` | donothing | 改名 | namehistory | `bos_basetpl` | 0 | 0 |
| 32 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | `bos_basetpl` | 0 | 0 |
| 33 | `QQJ9VPEIWE2` | assign | 分配 | assign | `bos_baseorgtpl` | 0 | 0 |
| 34 | `QQJATA099TH` | unassign | 取消分配 | unassign | `bos_baseorgtpl` | 0 | 0 |
| 35 | `/IRN4Y=FI53J` | donothing | 变更控制策略 | bdctrlchange | `bos_baseorgtpl` | 0 | 0 |
| 36 | `0DACD6GQT/MR` | donothing | 空操作 | individuation | `bos_baseorgtpl` | 0 | 0 |
| 37 | `329L6QTC2T7I` | donothing | 分配 | assign_new | `bos_baseorgtpl` | 0 | 0 |
| 38 | `3A=7K395P2/8` | donothing | 自动分配管理 | auto_assign | `bos_baseorgtpl` | 0 | 0 |
| 39 | `3ACT23RWB6N/` | donothing | Excel导入分配关系 | tbl_assign_import | `bos_baseorgtpl` | 0 | 0 |
| 40 | `21A5G1REGO=D` | donothing | 查看全部日志 | logview | `hbp_bd_orgtpl_all` | 0 | 0 |
| 41 | `2CM=+M12M1OM` | donothing | 查看日志 | viewonelog | `hbp_bd_orgtpl_all` | 0 | 0 |
| 42 | `36NMATCD+40R` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 43 | `36NMCH7J7K17` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 44 | `36NMEU7ZJ64A` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 45 | `36NMFZKH+JK4` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 46 | `36NMHG0I3HKP` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 47 | `36NMIYKCRQOF` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_orgtpl_all` | 0 | 0 |
| 48 | `20KPN/W=M4GG` | donothing | 历史版本信息 | hisversioninfo | `hbjm_joblevelscmhr` | 0 | 0 |
| 49 | `20KPPRMXGF/V` | donothing | 变更 | change | `hbjm_joblevelscmhr` | 0 | 0 |
| 50 | `20KQI22R5KE2` | donothing | 复制 | hiscopy | `hbjm_joblevelscmhr` | 0 | 0 |
| 51 | `23LKXRJB52SD` | donothing | 管理权转让 | orgpermchange | `hbjm_joblevelscmhr` | 0 | 0 |
| 52 | `2BL9A7LJ+MLD` | donothing | 新增数据版本 | newhisversion | `hbjm_joblevelscmhr` | 0 | 1 |
| 53 | `2NY+GRV0I75K` | donothing | 修订 | revise | `hbjm_joblevelscmhr` | 0 | 0 |
| 54 | `2NY+IA24TJAH` | donothing | 版本修订历史 | reviserecord | `hbjm_joblevelscmhr` | 0 | 0 |
| 55 | `2P7P1YQ616DW` | donothing | 版本对比 | versionchangecompare | `hbjm_joblevelscmhr` | 0 | 0 |
| 56 | `2XZ45+=QLS0M` | donothing | 查看所有版本 | showallversion | `hbjm_joblevelscmhr` | 0 | 0 |
| 57 | `4V/7ENQAUB31` | save | 确认变更 | confirmchange | `hbjm_joblevelscmhr` | 2 | 3 |
| 58 | `1PLCVMU+W/EQ` | newentry | 新增分录 | newentry | `hbjm_joblevelscmhr` | 0 | 0 |
| 59 | `1VTZME/NS9KA` | disable | 禁用 | disable | `hbjm_joblevelscmhr` | 2 | 2 |
| 60 | `1VTZV5FJWLZG` | enable | 启用 | enable | `hbjm_joblevelscmhr` | 3 | 1 |
| 61 | `22=M/SVBT1UK` | donothing | 管理权转让 | transfermanage | `hbjm_joblevelscmhr` | 0 | 0 |
| 62 | `22=M43ITKZCU` | donothing | 变更控制策略 | changecontrolstrategy | `hbjm_joblevelscmhr` | 0 | 0 |
| 63 | `22D39LZ1ZLJA` | deleteentry | 删除分录 | deleteentry | `hbjm_joblevelscmhr` | 0 | 0 |
| 64 | `22D3AIYI2PHS` | insertentry | 插入分录 | insertentry | `hbjm_joblevelscmhr` | 0 | 0 |
| 65 | `22D3C4TUACPQ` | moveentryup | 上移 | moveentryup | `hbjm_joblevelscmhr` | 0 | 0 |
| 66 | `22D3DJFBS3R7` | moveentrydown | 下移 | moveentrydown | `hbjm_joblevelscmhr` | 0 | 0 |
| 67 | `4YYWRJS/4PAL` | new | 新增 | new1 | `hbjm_joblevelscmhr` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` |  |
| 3 | 2 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 5 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 6 | 6 | `hbjm_bd_orgtpl_dlg` | `kd.hrmp.hbjm.opplugin.web.HBJMDataBaseOp` | 使用状态和初始化状态校验 |
| 7 | 7 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 8 | 8 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验插件 |
| 9 | 9 | `hbjm_joblevelscmhr` | `kd.hrmp.hbjm.opplugin.web.JobLevelScmSaveOp` | 保存插件 |
| 10 | 10 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 11 | 11 | `hbjm_joblevelscmhr` | `kd.hrmp.hbjm.opplugin.web.HbjmBaseNewDataOp` | hbjm基础新增保存op |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `0+/SL/MZ=VJB` | 创建组织和编码组合字段唯一性校验 | `bos_baseorgtpl` |
| 4 | GrpfieldsuniqueValidation | `3D3G/MZ2++JS` | 名称唯一性校验 | `hbp_bd_orgtpl_all` |
| 5 | GrpfieldsuniqueValidation | `2=KA79HUWMHM` | 编码唯一性校验 | `hbjm_bd_orgtpl_dlg` |
| 6 | GrpfieldsuniqueValidation | `23IKDAZ19TRP` | 创建组织和名称组合字段唯一性校验 | `hbjm_bd_orgtpl_dlg` |
| 7 | GrpfieldsuniqueValidation | `2=K9URZCEUUS` | 编码唯一性校验 | `hbjm_joblevelscmhr` |
| 8 | GrpfieldsuniqueValidation | `23ILP6JS0TBC` | 创建组织和名称组合字段唯一性校验 | `hbjm_joblevelscmhr` |
| 9 | ConditionValidation | `2M42D=Y0/0XK` | 生效日期不能填写未来 | `hbjm_joblevelscmhr` |
| 10 | GrpfieldsuniqueValidation | `23XAO/5+MFO7` | 职级顺序码唯一性校验 | `hbjm_joblevelscmhr` |
| 11 | GrpfieldsuniqueValidation | `244PQPY/C390` | 职级编码唯一性校验 | `hbjm_joblevelscmhr` |
| 12 | GrpfieldsuniqueValidation | `244PTCK3+Z38` | 职级名称唯一性校验 | `hbjm_joblevelscmhr` |
| 13 | ConditionValidation | `2G+NNWP8L2DH` | 职级顺序码取值范围 | `hbjm_joblevelscmhr` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 2 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 4 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 4 | 5 | `hbjm_joblevelscmhr` | `kd.hrmp.hbjm.opplugin.web.JobLevelScmDeleteOp` | JobLevelScmDeleteOp |
| 5 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+U=J7R7IEF/` | HR基础资料删除校验 | `hbp_bd_orgtpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 10. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 1 | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |
| 4 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 5 | 4 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `createorg.id` | 创建组织和编码组合字段唯一性校验 | `bos_baseorgtpl` |
| 5 | GrpfieldsuniqueValidation | `2+U=UD1KBODU` | 名称唯一性校验 | `hbp_bd_orgtpl_all` |

---
### 11. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnSubmitPlugin` | 受控基础资料撤销插件 |
| 2 | — | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 16. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

---
### 52. 新增数据版本（donothing / Key: newhisversion / oid: 2BL9A7LJ+MLD）
> 根定义: `hbjm_joblevelscmhr`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `2K+JH30V7OJV` | 合法性校验 | `hbjm_joblevelscmhr` |

---
### 57. 确认变更（save / Key: confirmchange / oid: 4V/7ENQAUB31）
> 根定义: `hbjm_joblevelscmhr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4V/78ID=HERR` | 字段值合规性校验 | `hbjm_joblevelscmhr` |
| 2 | GrpfieldsuniqueValidation | `4V/7C958CRVD` | 名称唯一性校验 | `hbjm_joblevelscmhr` |
| 3 | GrpfieldsuniqueValidation | `4V/7I6HFRZ3+` | 创建组织字段和编码唯一性校验 | `hbjm_joblevelscmhr` |

---
### 59. 禁用（disable / Key: disable / oid: 1VTZME/NS9KA）
> 根定义: `hbjm_joblevelscmhr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbjm_joblevelscmhr` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` | kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin |
| 2 | 1 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `4Z0D8F=K8IQM` | 合法性校验 | `hbjm_joblevelscmhr` |
| 2 | ConditionValidation | `4Z0D=+XNAQKC` | 合法性校验 | `hbjm_joblevelscmhr` |

---
### 60. 启用（enable / Key: enable / oid: 1VTZV5FJWLZG）
> 根定义: `hbjm_joblevelscmhr`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbjm_joblevelscmhr` | `kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin` | kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin |
| 2 | 1 | `hbjm_joblevelscmhr` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 3 | 2 | `hbjm_joblevelscmhr` | `kd.hrmp.hbjm.opplugin.web.JobLevelScmEnableOp` | 职级方案启用OP |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `4Z0D35XRPESD` | 合法性校验 | `hbjm_joblevelscmhr` |

