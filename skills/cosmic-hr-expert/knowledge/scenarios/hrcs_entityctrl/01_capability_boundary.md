# 能力边界 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + opkeys_index.json 25 opKey + scene_doc.json 17 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、场景定位

`hrcs_entityctrl`（业务对象维度映射）位于**HR 通用服务（hrcs）/ 权限管理 / 业务对象维度映射**菜单 · 是 HR 权限体系的"业务对象 → 字段 → 维度 → 控权范围"映射配置工具。它回答这一个问题：**"业务对象 X 上的字段 Y 在做权限校验时 · 应该按哪个维度（dimension）来限制范围？"**

放进 hrcs 权限三件套上下文：

- **维度配置 → `hrcs_dimension`**（定义有哪些维度 · 如"按行政组织 / 按管理域 / 按职位序列"）
- **业务对象-维度映射 → `hrcs_entityctrl`**（定义"业务对象 X 的字段 Y 用哪个维度"）⭐ **本场景**
- **角色维度配置 → `hrcs_roledimension`**（定义"角色 R 在维度 D 上有哪些范围"·  由本场景 save 联动写入）
- **数据规则 → `hrcs_datarule`**（基于上述配置生成的运行时数据规则）

**主表单**：`hrcs_entityctrl`（formNumber 与 entityNumber 同名）
**ModelType**：BillFormModel（**非 HisModel · 非时序**· 这是关键差异点）
**菜单 ID**：见 `probe_snapshot.json.menu`
**应用**：HR 通用服务（hrcs）

---

## 二、✅ verified（已覆盖能力）

### 2.1 标品支持的核心动作（25 opKey · 详见 opkeys_index.json）

| 类别 | opKey 清单 |
|---|---|
| **基础 CRUD** | `new` / `modify` / `view` / `save` / `delete` / `copy` / `refresh` |
| **保存延伸** | `saveandnew`（保存并新增）/ `option`（选项设置） |
| **分录操作** | `deleteentry`（删行 · 前置拦截 issyspreset） |
| **导航** | `first` / `previous` / `next` / `last` / `close` / `returndata` |
| **导入导出 HIES** | `show_import_record_hr`（查看导入记录） / `export_from_list_hr`（按列表导出） / `show_export_record_hr`（查看导出记录） |
| **导入导出 (legacy)** | `importdata` / `importdetails` / `importtemplatelist` / `exportlist` / `exportlistbyselectfields` / `exportlist1`（异名 exportlist · 列表"导出"按钮） |

⚠️ **本场景 opKey 中没有 `audit` / `unaudit` / `submit` / `unsubmit` / `disable` / `enable` / `confirmchange` / `change`**：因为是非 HisModel 配置基础资料 · 不走工作流 · 也没有版本管理。**Claude 写 CS 时不要搜这些 opKey · 没有就是没有**。

### 2.2 标品自带规则（实证 · 反编译 5 类抽取）

- **HR 域管理员准入闸**（`HRAdminStrictPlugin.preOpenForm` L29-L52）：preOpenForm 拒绝非 HR 管理员（11+ hrcs 表单共用 · `isAdminUser || isCosmicUser` 都不是 → setCancel + 提示"您不是 HR 领域管理员"）
- **业务对象 F7 4 闸过滤**（`EntityCtrlEdit.bulidEntityFilters` L515-L533）：modeltype + istemplate=0 + 排除已配置 + 排除 forbid + HR 域且有控权项
- **维度 F7 6 分支过滤**（`EntityCtrlEdit.beforeF7Select` L197-L237）：按 propkey 类型走 6 个分支决定可选维度集
- **新建分录走子页面**（`EntityCtrlEdit.itemClick + showFieldForm` L315-L460）：点"添加行"按钮跳 `hrcs_choosefield_page` 子页面 · 用户从该业务对象的属性列表挑字段 · 回填后自动设 `propkey + propname + authrange=2`（如果是 noDBProp / QueryEntityProp · 还会 disable authrange 字段）
- **virtualField 业务对象的特殊路径**（`EntityCtrlEdit.putDynaFormCtrlInfo` L368-L388）：虚字段实体（dynamic/virtual）从 `hrcs_dynaformctrl` 拿"虚字段属性配置" · 不存在则 `showFieldForm` L433 错误提示"请在虚字段数据控权配置中添加该业务对象的相关属性信息"
- **save 校验**（`EntityCtrlEdit.beforeDoOperation` L262-L271 + `EntityControlSaveOp.onAddValidators` L81-L83）：分录为空时拦截 + 注册 `EntityControlSaveValidator`
- **save 联动 hrcs_roledimension**（`EntityControlSaveOp.endOperationTransaction` L99-L146）：自动同步 ismust 维度到 hrcs_roledimension · 删除被去掉的分录对应行
- **delete 前置拦截**（`EntityCtrlEdit.beforeDoOperation` deleteentry L272-L289 + `EntityCtrlTreeListPlugin.beforeDoOperation` delete L89-L101）：predataset / issyspreset 拦截 + 二次确认弹框
- **delete 联动 hrcs_roledimension**（`EntityCtrlDelOp.beginOperationTransaction` L47-L70）：从 OperateOption 读 toDelDimRoleRanges · 调 `EntityCtrlServiceHelper.deleteRoleRange` 清角色维度对应行
- **save / delete 后清缓存**（`EntityCtrlEdit.afterDoOperation` L292-L298 + `EntityCtrlTreeListPlugin.afterDoOperation` L103-L110）：双层 `HRPermCacheMgr.clearAllCache()`
- **TreeList setFilter**（`EntityCtrlTreeListPlugin.setFilter` L79-L87）：列表强制 `entitytype.number is not null` · 排除"指向已不存在业务对象"的孤儿记录
- **TreeList 导出补充 propname**（`EntityCtrlTreeListPlugin.afterQueryOfExport` L112-L144）：导出时实时填 propname（缓存到 entityFieldNameMap）

### 2.3 标品自带集成点

- **下游灌库**：save 时自动写 `hrcs_roledimension`（角色维度配置 · 实证 `EntityControlSaveOp.syncMustDimToRoleDim`）
- **删除联动**：delete 时自动从 `hrcs_roledimension` 清角色行（实证 `EntityCtrlDelOp` + `EntityCtrlServiceHelper.deleteRoleRange`）
- **跨模块取虚字段配置**：`hrcs_dynaformctrl` 表 `entitytype = entityType.number` 关联（实证 `EntityCtrlEdit.putDynaFormCtrlInfo`）
- **跨模块查 HAOS 结构项目**：`HRMServiceHelper.invokeHRMPService("haos", "IHAOSStructProjectService", "queryStructProConfig", ...)`（实证 `EntityCtrlEdit.queryEntityPropOtclassifyIds` L240-L255）

---

## 三、🟡 likely（标品支持 · 待业务确认）

- **导入导出**：HIES 导入和老 importdata 都注册了 · 业务实际用哪种 · 待业务侧确认（HIES 是新版 · 推荐用 HIES · `show_import_record_hr` / `export_from_list_hr`）
- **复制方案**：`copy` opKey 标品已注册 · 但反编译里没看到针对 copy 的特殊处理 · 推测复制走通用 BillFormModel.copy 行为 · 字段全复制（issyspreset 也被复制 · 但 t_hrcs_entityctrl 主表没有 issyspreset · 仅子表有）
- **`option`（选项设置）opKey**：标品注册 · 推测是表单视图选项 · 与业务无关
- **导出列**：实证 `afterQueryOfExport` 重新计算 propname · 但其他列（dimension.name / authrange / ismust）走标品 · 看不到自定义导出列的入口

---

## 四、⚠️ unverified（需专家确认）

- [ ] 同一业务对象的同一字段配两次（如 `hrpi_person.org` 配两条 entryentity）会怎样？反编译没看到唯一性校验 · 推测靠 `EntityCtrlEdit.bulidEntityFilters` L518 排除已配置实现"业务对象级别" 唯一 · 但**字段级别没看到拦截** —— 待业务侧手动加场景验证
- [ ] `entryentity.needhisver = true` 后 · 该字段值变更是否真有"维度变化历史"快照表？反编译没看到落库逻辑 · 可能是平台级别另有 OP / 后台任务在跑
- [ ] `EntityControlSaveValidator` 实际校验什么？反编译没拿到此类的源码（在 hrcs-opplugin 外层）· 待 javap 实证类内部规则
- [ ] `t_hrcs_entitydimentry.fauthrange` ComboField 取值列表？反编译里只看到 "1" / "2" 两个值（L491 默认 "2"）· 但 metadata 应有完整 comboOptions · 待 OpenAPI 抓元数据补
- [ ] 一个用户没有任何"HR 域管理员组" 时 · `HRAdminStrictPlugin` 直接拒 · 但这个用户能用 hrcs 哪些功能？（影响 ISV 自建 form 时是否要复用准入闸）
- [ ] HIES 导入 / 老 importdata 触发的是同一个 OP 链吗？还是 HIES 走 HISE 专用 OP？

---

## 五、不覆盖（已知限制）

### 5.1 业务限制

- **不发 BEC 事件**：grep `triggerEventSubscribe / IEventService / EventServiceHelper` 在反编译 0 处命中（详见 06_customization_solutions.md CS-05）。如客户需要"映射变更通知下游系统" · 只能 ISV 自建 BEC 发布方
- **issyspreset 标记的预置分录不能删**（`EntityCtrlEdit.beforeDoOperation` L278-L283 + `EntityCtrlTreeListPlugin.beforeDoOperation` L97-L99）
- **业务对象只能配一次**：F7 自带"排除已配置"过滤（`bulidEntityFilters` L518）· 一个业务对象在 hrcs_entityctrl 中只能存一条主记录
- **`bos_org / hbss_hrbu` 业务对象硬排除**（`bulidEntityFilters` L519-L520）：组织本身和 HR 业务单元做控权配置不被允许
- **`role` / `propkey` / `dimension` / `entitytype` / `authrange` 字段 isvCanModify=false**：标品保护关键业务键 · ISV 不能直接 modifyMeta 改这些字段
- **不支持 audit / submit / disable / confirmchange**：非 HisModel · 没有工作流 · 没有版本管理

### 5.2 平台限制

- **OpenAPI 不支持注册 ISV 扩展元数据 add field**（`PLATFORM_OPENAPI_GAPS.md` P0-1 · 平台需补齐）· ISV 加字段必须走开发平台 IDEA 插件或 Web UI
- **modifyMeta add field 不支持 ISV 归属判定**（`PLATFORM_OPENAPI_GAPS.md` P0-2）· 跨 ISV 归属的字段会被相互覆盖
- **HRMulPositionField / HRMulAdminOrgField / MulHisModelBasedataField 不能通过 OpenAPI buildMeta 创建**（`kb_cosmic_buildmeta_traps.md`）· OpenAPI 视作 BasedataField 兜底
- **EmployeeField 不支持**：标品 hrcs_entityctrl 不用 EmployeeField · 但 ISV 想加员工字段必须走 hrpi_person + Java 插件而非 EmployeeField

---

## 六、扩展能力总结

| 扩展场景 | 是否支持 | 详见 |
|---|---|---|
| 加自定义业务字段（如"映射备注"） | ✅ 完全支持 | CS-01 |
| 字段联动（业务对象 → 维度选择联动） | ✅ 完全支持（FormPlugin propertyChanged） | CS-02 |
| save / delete 前置业务校验（如"映射不能重复"） | ✅ 完全支持（自建 Validator + onAddValidators） | CS-03 |
| 删除前查下游引用（hrcs_roledimension / hrcs_datarule 等） | ✅ 完全支持（自建 Validator + 反查） | CS-04 |
| 变更后通知下游（BEC 发布方） | ⚠ 需 ISV 自建发布方（标品没发） | CS-05 |
| 分录子表 entryentity 操作扩展（如增行时强制走 ID 生成） | ✅ 完全支持（自建 OP 挂 save 的 beforeExecuteOperationTransaction · PR-005） | CS-06 |
| 列表过滤定制（按部门/标签过滤） | ✅ 完全支持（继承 `AbstractTreeListPlugin` · 不继承场景专属类） | CS-07 |
| 修改 propkey/dimension/entitytype 字段类型 | ❌ 不允许（关键业务键 · 改了直接挂） | - |
| 替换 ChoiceFieldPageCustomQueryService（自定义字段选择子页面） | ❌ 不推荐 · hrcs 业务核心 helper · 改了风险大 | - |

---

## 七、版本兼容性

- 反编译版本：HRMP **8.0**（hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar）
- 苍穹平台：8.0+（`AbstractTreeListPlugin` / TreeList API 8.0 已稳定）
- 待验证：2024R1 / 2025 版本是否有 opKey/字段差异（建议每年 Q1 重跑 probe）
- **本场景非 HisModel** · 不依赖 `hbp_histimeseqtpl` 父模板 · 与 hrcs_dynascheme / haos_adminorg / hjm_jobhr 不同

---

## 八、与其他场景的关联

| 关联场景 | 关系 | 备注 |
|---|---|---|
| `hrcs_dimension` | 上游引用（`entryentity.dimension`） | 维度基础资料 |
| `bos_entityobject` | 上游引用（`entitytype`） | 业务对象基础资料 |
| `hbp_devportal_bizapp` | 上游引用（`bizapp`） | 应用基础资料 |
| `hrcs_dynaformctrl` | 上游引用（仅虚字段实体场景） | 虚字段数据控权配置 · `EntityCtrlEdit.putDynaFormCtrlInfo` 实证 |
| `hrcs_roledimension` | 下游写表 | 角色维度配置 · save 联动写 / delete 联动清 |
| `hrcs_datarule` | 下游引用 | 数据规则 · 基于本场景 + hrcs_roledimension 计算运行时规则 |
| `hrcs_choosefield_page` | 子页面 | 添加行按钮跳转 · 选业务对象的字段 |
| `bos_listf7` | F7 列表壳 | 业务对象 F7 · `EntityCtrlEdit.beforeF7Select` L197 实证 |
| `hrcs_dynascheme` | 同应用 | 动态授权方案 · 同样在 hrcs/权限管理目录下 · ModelType 不同（HisModel） |

详见 `11_upstream_downstream_logic.md`。
