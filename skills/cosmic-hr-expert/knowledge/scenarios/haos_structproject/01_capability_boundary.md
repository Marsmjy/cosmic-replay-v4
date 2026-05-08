# 能力边界 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 52 opKey + 14 反编译类（StructProjectEditPlugin 378 行 / StructProjectListPlugin 225 行 / StructProjectSaveOp 183 行 / StructProjectDeleteOP 56 行 / StructProjectDisableOp 28 行 / StructProjectEnableOp 37 行 / StructOrgPermSaveOp 31 行 / OrgChangeDetailService 591 行 / BatchAdminOrgNewOpService 207 行 / AdminChangeMsgService 124 行 / AdminOrgChangeLogService 226 行 / BatchOrgDeleteOpService 89 行 / BatchOrgExecuteOpService 42 行 / StructProjectBUListPlugin 20 行）+ main_form.xml + scene_doc.json 35 字段实证
> **confidence**：verified

## 1. 场景定位

**菜单路径**：组织管理 / 行政组织维护 / 行政组织维护 / 矩阵组织设置
**form 中文**：矩阵组织设置（也叫"结构化方案"母本）
**formNumber**：`haos_structproject`
**formId 技术码**：`3BPVOPG05AFA`
**菜单 menuId**：`1723408518565984256`
**入口应用**：HR基础组织（haos · `bizappId=W11R1282DJK`）

> 这是**结构化方案的母本设计画面** · 不是矩阵组织实例的运营画面（实例运营在 `haos_structure`）。两个 form 共用 `t_haos_structproject` 物理表（35 字段同一个实体定义 · `roottype` ComboField + 业务硬编码 1010L 区分母本 vs 实例）。
> 业务流向：先在本场景建方案 → 用户在 haos_structure 派生具体矩阵组织实例 → 在 `haos_orgstructlist + haos_structorgdetail` 维护下挂组织树。

## 2. 能干啥（已覆盖能力 · 52 opKey 全景）

### 2.1 方案 CRUD（核心 · 25 opKey）

| 能力 | opKey | 标品 OP 数 | 反编译实证 |
|---|---|---|---|
| 新建方案 · 含选根组织 / 选根组织类型 / 设虚拟组织开关 | `new` + `save` | 7 | `StructProjectEditPlugin.afterCreateNewData` L119-136 默认带 org · `StructProjectSaveOp.beginOperationTransaction` L77-82 写主表 + 派生根组织 |
| 修改已有方案 · 走 `modify` 互斥锁 | `modify` + `save` | 7 | `StructProjectEditPlugin.beforeDoOperation` L217-232 走 `MutexHelper.require` 抢 `edit_struct` 互斥锁 |
| 复制方案 | `copy` + `save` | 7 | 走平台基础资料模板 |
| 删除方案 · 仅 enable=10（启用中）行参与删除 | `delete` / `delete_project` | 1 | `StructProjectDeleteOP.beginOperationTransaction` L46-55 过滤 `enable=10` |
| 查看方案详情 | `view` | 0 | `StructProjectEditPlugin.preOpenForm` L201-206 默认 status=VIEW |
| 关闭表单 | `close` | 0 | - |

### 2.2 状态流转（6 opKey）

| 能力 | opKey | 状态变化 | 反编译实证 |
|---|---|---|---|
| 提交方案 | `submit` | status A → B | 标品 HR 基础资料模板 |
| 提交并新增 | `submitandnew` | A → B + new | 标品 |
| 撤销提交 | `unsubmit` | B → A | 标品 |
| 审核方案 | `audit` | B → C | 仅挂 `HRBaseDataLogOp`（写日志）· 没有真业务 OP |
| 反审核 | `unaudit` | C → A | 同 audit |
| 启用方案 | `enable` | enable 0 → 10 → 1 | `StructProjectEnableOp.onAddValidators` L24-27 注册 StructProjectValidator · `onPreparePropertys` L29-33 仅声明 org 字段 · `beginOperationTransaction` 是空方法 |
| 禁用方案 | `disable` | enable 1 → 0 | `StructProjectDisableOp.onAddValidators` L21-24 注册 StructProjectValidator · `beginOperationTransaction` L26 空方法 |

> 状态字段：`status` BillStatusField + `enable` BillStatusField · enable=10 是 BdVersion 启用过程的中间态（参考 haos_structure FP_StructureListPlugin_setFilter_002）

### 2.3 数据访问能力（基础资料标准 · HIES 6 opKey）

| 能力 | opKey | 来源 |
|---|---|---|
| 列表查看（含 BU 闸 + 默认排预置 + 排自定义组织）| - | `StructProjectListPlugin.setFilter` L91-108 三层过滤 |
| 列表导入 / 导出 | `importdata_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_import_record_hr` / `show_export_record_hr` | `HRBaseDataImportEdit` + `HRHiesButtonSwitchPlugin` 提供 HIES 按钮 |
| 操作日志查看 | `logview` / `viewonelog` | `HRBaseDataLogOp` 写日志 + `HRBasedataLogList` 显示 |
| 名称历史 | `namehistory` / `namehistoryview` | `BaseDataNameVersionListPlugin` |

### 2.4 维护方案下挂的组织树（核心业务能力）

| 能力 | opKey | 跳转路径 | 反编译实证 |
|---|---|---|---|
| 维护方案下挂的组织 · 主表单跳转入口 | `maintain_struct` | 跳到 `haos_orgstructlist` 列表 + `haos_structorgdetail` 详情 | `StructProjectEditPlugin.openOperationPage` L292-296 + `showMaintainFrameworkForm` L318-337 |
| 维护方案下挂的组织 · 列表跳转入口 | `maintainframework` | 同上 + 处理 1040 全领域标记 | `StructProjectListPlugin.openOperationPage` L124-127 + `showMaintainFrameworkForm` L151-177（id=1040 时设 `struct_project_is_to_all_areas=true`）|

### 2.5 列表新增（带 BU 上下文）

| 能力 | opKey | 反编译实证 |
|---|---|---|
| 列表新增"我的方案" · 带当前 BU 过滤 | `addnew_org` | `StructProjectListPlugin.openOperationPage` L120-122 + `showStructProjectForm` L134-149 拿 ControlFilter `org.id` 当 customParam 传给新表单 |

### 2.6 互斥编辑控制

| 能力 | opKey | 反编译实证 |
|---|---|---|
| 编辑架构互斥锁（防多页面同时编辑同一方案）| `edit_struct` | `StructProjectEditPlugin.beforeDoOperation` L217-232 + `MutexHelper.require/release` · 锁释放在 `openOperationPage` L298-303 |

### 2.7 编码规则

| 能力 | 来源 |
|---|---|
| number 走业务侧编码规则自动生成 | save / submit 链 RowKey=- 是 `CodeRuleOp`（业务侧在【编码规则基础资料】配 · PR-006）· 反编译实证 `StructProjectEditPlugin.propertyChanged` L262 切换到 roottype=2 时调 `AdminOrgCodeRuleServiceHelper.create(...).setOrgNumber(hrDy, "rootnumber")` |

### 2.8 列表分页 / 移动端 / 分录占位（剩余 11 opKey）

| 能力 | opKey | 说明 |
|---|---|---|
| 移动端工具栏 | `mobtoolbarselect` / `mobtoolbarcancel` | 移动端列表选择 |
| 列表分页 | `first` / `previous` / `next` / `last` | 平台兜底 |
| 分录占位 | `newentry` / `deleteentry` / `previousentry` / `nextentry` | 占位（本场景主表无 EntryEntity · scene_doc.json 35 字段全在主实体）|
| 选项 / 刷新 / 返回 | `option` / `refresh` / `returndata` | 平台兜底 |
| 个性化设置 | `importexport_userset` | HIES 配置 |
| 模板管理 | `importtemplatelist` | HIES 模板 |
| 导出查看 | `exportdetails` / `importdetails` / `exportlist` / `exportlist_expt` / `exportlistbyselectfields` | HIES 兜底 |
| 空操作 | `donothing` | 平台占位 |

## 3. 不能干啥（已知限制）

### 3.1 ⚠ 标品没实现的能力

| 功能 | 为什么不能 | 解决路径 |
|---|---|---|
| **方案被派生为 haos_structure 实例后改根字段类型** | `StructProjectSaveOp.saveStructProjectAndRootOrg` L84-181 在 roottype 变化时调 `BatchOrgDeleteOpService` + `BatchAdminOrgNewOpService` · 这意味着改 roottype 会**删旧根组织 + 建新根组织** · 业务上影响所有依赖该方案的下游 | 业务方面要求 ISV 加 `onAddValidators@save` 校验"已有派生 haos_structure 实例时禁改 roottype"（CS-02）|
| **方案删除时检查派生实例** | `StructProjectDeleteOP` L46-55 仅按 `enable=10` 过滤 · **没有**校验该方案是否被 haos_structure 实例 `relyonstructproject` 引用 | ISV 加 `onAddValidators@delete` 反向引用校验（CS-04）|
| **审核挂业务 OP** | audit/unaudit 仅挂 `HRBaseDataLogOp` · 没有真业务 OP（基础资料标准做法） | 业务方真要走审批 · 用 OPM 审批中心独立配置 |
| **业务事件 BEC 标准发布** | 反编译 grep 全场景 14 类**没有**`triggerEventSubscribe` / `IEventService` / `EventServiceHelper` · BEC 不走标准事件总线 · 而是 `AdminChangeMsgService.handleChangeMsg` L113-123 用 `JobClient.dispatch + sch_task` 派单（标品 `haos_adminorg_msgdetail` 实体落地）· 这是 admin_org 共用的派单机制 · 跟标准 BEC 不是一回事 | ISV 真要发标准 BEC 必须自建发布方（CS-05）· 别套 hjm 那种 `triggerEventSubscribeJobs` 模式 |

### 3.2 平台限制

| 限制 | 说明 |
|---|---|
| `haos_structproject` 不能再增删主表字段（标品锁定）| `IsvSign` 已签 · ISV 加字段必须走【ISV 扩展元数据】机制（不能直接 modifyMeta 改 haos_structproject 主表）· 详见 isv_ownership_redline.md |
| 物理表 `t_haos_structproject` 不能直接 SQL 改 | 必须走苍穹 `HRBaseServiceHelper.save` · 否则 BdVersion 历史不一致 |
| `issyspreset='1'` 的预置数据 number 不可改 | PR-007 业务硬规则 · `STRUCT_PROJECT_MANAGE` 预置母本受保护 |
| 物理表跟 haos_structure 共用 | 加字段后**两个 form 都会"看到"** · 想隔离要走 form 元数据层挂载（CS-06）· 不能在物理列上单独抹除 |

### 3.3 与同应用其他场景的边界

| 不在本场景内的功能 | 应该去 |
|---|---|
| 派生具体矩阵组织实例并维护 | `haos_structure` 场景（用 relyonstructproject 字段引本方案）|
| 在矩阵下挂具体的行政组织 | `haos_orgstructlist` + `haos_structorgdetail`（通过 maintain_struct / maintainframework 跳进去）|
| 行政组织本身的 CRUD | `admin_org_quick_maintenance` / `homs_orgbatchchgbill` 调整申请单 |
| 行政组织变更日志读取 | `haos_adminorg_msgdetail`（标品 admin_org 域 · 标品已派单写入）|
| 矩阵组织实例的查询 API | `haos_structure` 实体的 OpenAPI · 不是本场景 |

## 4. 边界条件与前置依赖

### 4.1 创建方案前 · 必须存在

1. 至少一个 `haos_adminorg` 行政组织作为根组织（"根组织"字段 `rootorg` 必填）· 或者选 `roottype=2` 走虚拟根组织（自建）路径
2. 当前用户在某个 BU 下有权限（否则 `StructProjectEditPlugin.afterCreateNewData` L131-133 会 LOG.error 找不到 org · 表单仍能打开但 org 字段为空）
3. 当前用户对 `haos_structproject` 有创建权限（受 `OrgPermHelper.getHRPermOrg(true)` 控制 · L124）

### 4.2 修改方案 · 必须满足

1. 互斥锁可拿到（`MutexHelper.require(view, "haos_structproject", id, "edit_struct", true, sb)` L225）· 否则提示 `OrgStructProjectPermTreeListPlugin_1`："当前单据已在其他页签中打开 · 请关闭后重试或重新登录"
2. 关闭表单时锁释放：`MutexHelper.release("haos_structproject", "edit_struct", id)` L299

### 4.3 删除方案前 · 必须满足

1. 数据 enable='10'（启用中状态 · `StructProjectDeleteOP.beginOperationTransaction` L50 过滤）· 注意：`enable=1`（已启用）和 `enable=0`（已禁用）的行**会被跳过删除**（业务方需先 disable 流程）
2. `StructProjectDeleteValidator` 校验通过（L37 注册 · 反编译没追到 validator 内部 · 推测含"是否被引用"校验 · 这是 CS-04 的实证起点）

### 4.4 启用方案前 · 必须满足

1. `StructProjectValidator` 校验通过（L24-27）· 注意此 Validator 跟 save / disable 共用 · 内部按 opKey 分支判断
2. `enable` 字段从 0 → 10 → 1 走 BdVersion 中间态

## 5. 性能边界

| 项 | 限制 |
|---|---|
| 单次列表查询 | 三层 QFilter 过滤（`issyspreset` + `iscustomorg` + `org`）· 物理表 `t_haos_structproject` 数据量小（业务方案级别 < 200）· 无明显瓶颈 |
| 单次保存 | 7 个 OP 顺序执行 · 含 `BdVersionSaveServicePlugin` 写历史 + 派生根组织（`BatchAdminOrgNewOpService` 跨表写）· 单条 100-300ms |
| 维护互斥锁 | 单租户单方案同时只能一个会话编辑 · 锁不释放阻断后续编辑（页面关闭主动 release · LB Crash 场景靠平台 mutex 超时回收）|
| sch_task 派单 | `AdminChangeMsgService.handleChangeMsg` L113-123 派单到 JOB_ID `5+X/4Y=AOZ=O` · 重复派单走 `SCHEDULED` 状态过滤 · 不会风暴 |

## 6. 数据规模边界

| 项 | 典型规模 |
|---|---|
| 单租户结构化方案数（含母本+实例）| < 200 行 in `t_haos_structproject` |
| 多语言子表 `t_haos_structproject_l` | 行数 = 方案数 × 业务用语言数 · 6 个 MuliLangTextField（name / simplename / description / oriname / rootname / rootdescription）|
| 派生 haos_structure 实例数 | 通常 1 个母本 → 0~3 个实例（不同管理视角）|
| 关联派单消息表 `haos_adminorg_msgdetail` | 每次 save 派一条 · 累计可达万级 |

## 7. 与版本演进相关

| 版本 | 行为 |
|---|---|
| 8.x（当前抓取版本）| 双 form 共用一表（roottype + 业务硬编码区分）· `StructProjectSaveOp` 派生根组织走 BatchAdminOrgNewOpService · 派单走 sch_task 而非标准 BEC |
| 历史早期 | 可能 haos_structure 是独立表 · 后期重构合并到 t_haos_structproject |

> ISV 扩展时不要假设业务硬编码（如 1010L `otclassify` 值）稳定 · 标品升级可能调整 · 用常量类引用而不硬编码

## 8. 同族场景能力对照

| 场景 | 能干啥 | 不能干啥 |
|---|---|---|
| **本场景**（haos_structproject）| 结构化方案母本 CRUD + 设计字段 + 启停用 + 维护下挂组织 | 不能直接派生矩阵实例（要去 haos_structure）· 不能改行政组织本身 |
| haos_structure | 矩阵组织实例 CRUD + 引用 relyonstructproject 母本 | 不能改母本结构（要回到本场景）· 不能改行政组织本身 |
| admin_org_quick_maintenance | 行政组织 CRUD + 上级变更 + HisModel 历史回溯 | 不能改方案 / 矩阵架构 |
| haos_orgstructlist | 在指定方案下挂组织 / 调层级 | 不能建方案 / 改方案 |

## 9. 给 ISV 扩展的开发坐标

| 能扩展什么 | 在哪扩展 | 推荐 CS |
|---|---|---|
| 加自定义字段（如"应用领域""权限模板"）| ISV 扩展元数据继承 haos_structproject | CS-01 |
| 改 roottype 时校验下游引用 | save 的 onAddValidators 阶段 · 并列挂新 Validator | CS-02 |
| 字段联动（选了"应用领域"自动带出权限模板）| 新 FormPlugin extends HRDataBaseEdit · propertyChanged · PR-004 防死循环 | CS-03 |
| 删除方案前校验"无活跃 haos_structure 实例" | delete / delete_project 的 onAddValidators · 反向引用校验 | CS-04 |
| 通知下游标准 BEC 事件 | afterExecuteOperationTransaction · 自建发布方（标品没发标准 BEC）| CS-05 |
| 共用物理表场景的字段隔离 | ISV 扩展元数据只挂本 form · 不挂 haos_structure | CS-06 |
| 启用方案前置检查 | enable 的 onAddValidators · StructProjectEnableOp 并列挂 | CS-07 |
| 改列表权限过滤 | 新 ListPlugin extends HRDataBaseList · 重写 setFilter · 与标品 StructProjectListPlugin 并列跑 | （参考 haos_structure CS-07/08 · 同模式）|

## 10. 高频场景速查（业务诉求 → CS）

| 业务诉求 | 推荐 CS | 复杂度 |
|---|---|---|
| 给方案加字段（如"应用领域"）| CS-01 | 低 |
| 校验"修改根组织类型时检查派生实例" | CS-02 | 中 |
| 字段联动（"选应用领域自动带权限模板"）| CS-03 | 中 |
| "删除方案前必须无活跃实例"校验 | CS-04 | 中 |
| 变更通知下游（自建 BEC）| CS-05 | 高 |
| 共用物理表的扩展隔离 | CS-06 | 高（需理解 form 元数据隔离机制）|
| 启用方案前置检查 | CS-07 | 低 |

---

## 来源追溯

- 14 反编译类：`knowledge/_sdk_audit/_decompiled/scenarios/haos_structproject/*.java`
- 52 opKey：`probe_out` 已分裂到 `opkeys/<opKey>.json` × 52
- 35 字段：`scene_doc.json.fields`
- main_form.xml：`haos_structproject/main_form.xml`（2155 行）
- BEC 派单实证：`AdminChangeMsgService.java:113-123` 用 sch_task + JobClient.dispatch（非标准 BEC）
- 互斥锁实证：`StructProjectEditPlugin.java:225-230` MutexHelper.require
- 删除前置：`StructProjectDeleteOP.java:46-55` 仅按 enable=10 过滤
