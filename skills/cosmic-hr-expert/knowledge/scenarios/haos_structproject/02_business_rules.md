# 业务规则 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 14 反编译类 + 35 字段 + 52 opKey 实证
> **confidence**：verified

## 1. 必填字段（来自 scene_doc.json + main_form.xml + 反编译实证）

| 字段 | 类型 | 必填条件 | 来源 |
|---|---|---|---|
| `number` | TextField | 始终（编码 · 可走 CodeRule 自动生成）| scene_doc.json fields[0].required=true |
| `name` | MuliLangTextField | 始终 | scene_doc.json fields[1].required=true |
| `org` | OrgField | 始终（创建组织 BU）| scene_doc.json fields[19].required=true · `StructProjectEditPlugin.afterCreateNewData` L119-136 自动带值 |
| `rootorg` | HRAdminOrgField | 始终（roottype=1 时）| scene_doc.json fields[20].required=true · `StructProjectEditPlugin.afterBindData` L156 `setMustInput(true, ROOTORG)` |
| `roottype` | ComboField | 始终（根组织类型 · 1=实组织 / 2=虚拟）| scene_doc.json fields[21].required=true |
| `rootnumber` | TextField | 仅 roottype=2（虚拟）必填 | `StructProjectEditPlugin.propertyChanged` L256 切到 2 时 `setMustInput("2".equals(newValue), "rootnumber"...)` |
| `rootname` | MuliLangTextField | 仅 roottype=2 必填 | 同上 |
| `rooteffdt` | DateField | 仅 roottype=2 必填 | 同上 |
| `effdt` | DateField | 始终（生效日期）| scene_doc.json fields[29].required=true |

> **联动逻辑**：`roottype=1`（实组织）时 rootorg 必填、rootnumber/rootname/rooteffdt 隐藏；`roottype=2`（虚拟根）时 rootorg 隐藏、rootnumber/rootname/rooteffdt 必填 · `StructProjectEditPlugin.propertyChanged` L250-289 实证。

## 2. 真实 formRule（listRules 实抓 · 2 条）

| ruleId | 类型 | preCondition | 描述 |
|---|---|---|---|
| `3F9C=FAAUNO1` | formRule | `enable = '1'` | enable = '1'（已启用态显示规则）|
| `3J6Z0ZYXA2CZ` | formRule | `enable='10'` | enable='10'（待启用 · 允许编辑生效日期字段）|

> 这 2 条是平台 formRule 配置 · 不是反编译代码里的硬规则。其余业务约束来自反编译代码（下文 R-01 ~ R-15）。

## 3. 根组织类型规则（核心业务约束）

### R-01 · roottype=1（实组织）

- **行为**：rootorg 必填 · rootnumber/rootname/rootdescription/rooteffdt 字段**隐藏**
- **保存逻辑**（`StructProjectSaveOp.saveStructProjectAndRootOrg` L114-118）：
  - `rootOrg = OrgRepository.loadCurrentVersionOrgDy(rootOrg.getLong("id"))` 加载现有行政组织
  - 设 `rootOrg.set("index", 1) + rootOrg.set("bsed", effDate)` · 不创建新组织
- **业务语义**：方案挂载到一个**已存在**的行政组织上 · 那个组织成为这个方案的根

### R-02 · roottype=2（虚拟根组织 · 标品自建）

- **行为**：rootorg 隐藏 · rootnumber/rootname/rooteffdt 必填 · 用户填的是虚拟组织信息
- **保存逻辑**（`StructProjectSaveOp.saveStructProjectAndRootOrg` L92-113）：
  - 如果 `rootOrgId=0`（新建虚拟组织）：
    - 调 `ORM.create().genLongId("haos_adminorgdetail")` 生成新 ID（PR-005）
    - 调 `OrgRepository.genEmptyDy()` 生成空白 DynamicObject
    - 设字段：`number/name/description/isvirtualorg=1/bsed=effDate/establishmentdate=effDate/otclassify=1010L/index=1`
    - 把虚拟组织 set 进 `structProject.rootorg` · 一并保存（级联）
  - 如果 `rootOrgId≠0`（修改已有虚拟组织）：
    - 加载现有虚拟组织 · 更新 number/name/description/bsed/establishmentdate
- **关键 hardcode**：虚拟组织的 `otclassify=1010L`（与 haos_structure 实例的硬编码同源）
- **派生 OP**：调 `BatchAdminOrgNewOpService(new DynamicObject[]{rootOrg}, true, operateOption, structProjectId)` · 这会触发 admin_org 域的派单链
- **`OP_ADD_ORG_TEMP_TAG=true`** 标记（L122）：让 `BatchAdminOrgNewOpService.execute` L83 判断 `isAddTempTag` · 暂态标记跳过 saveAdminOrgMsgDetail（避免重复派单）

### R-03 · roottype 切换规则（变更影响下游 · 关键）

- **从 1 切到 2**：弹出虚拟根字段 · 清空 rootorg · `propertyChanged` L259-264 调 `AdminOrgCodeRuleServiceHelper.setOrgNumber(hrDy, "rootnumber")` 自动赋编码
- **从 2 切到 1**：隐藏虚拟根字段 · 清空 rootorg · 重新走 rootorg 必填
- **dbRootType ≠ newRootType + dbEffDate ≠ effDate**（`StructProjectSaveOp.saveStructProjectAndRootOrg` L131-180）：
  - 走 `OtherStructService.saveOtherStruct()` · 整批迁移现有 OrgStruct（重新挂层级 · 旧根 → 新根）
  - 用 `otherStructEntity.setDeleteRoot(true)` 删除旧根
  - **业务后果**：roottype 切换会改写整个方案下挂的组织树
- **dbRootType = newRootType + dbEffDate = effDate**：
  - 仅更新 root 信息 · 不动下挂组织
- **`virtualAndNotChange`**（L132）：dbRootType=2 + newRootType=2 时 · 走 `HisModelAPIService.createDataVersions/reviseVersions` 历史版本路径

## 4. 启用规则（StructProjectEnableOp）

### R-04 · enable opKey

- **执行链**（`opkeys/enable.json`）：
  1. `HRBaseDataLogOp` 写日志
  2. `StructProjectEnableOp.onAddValidators` L24-27 注册 `StructProjectValidator`（共用 Validator · 内部按 opKey 分支）
- **`onPreparePropertys`** L29-33：声明 `org` 字段（让 OP 链拿到 org 字段值）
- **`beginOperationTransaction`** L35-36：**空方法**（实际状态切换由 HRBaseDataEnableOp / HRBaseDataStatusOp 处理）
- **状态变化**：enable 0 → 10 → 1（中间态由 BdVersion 控制）
- **业务前置**（StructProjectValidator 内部 · 反编译没追到具体 validate 逻辑）：推测含"必填字段是否齐全"等基础校验

## 5. 禁用规则（StructProjectDisableOp）

### R-05 · disable opKey

- **执行链**（`opkeys/disable.json`）：
  1. `HRBaseDataLogOp` 写日志
  2. `StructProjectDisableOp.onAddValidators` L21-24 注册 `StructProjectValidator`
- **`beginOperationTransaction`** L26：**空方法**
- **业务后果**：方案 enable=1 → 0
  - 已派生的 haos_structure 实例**不会自动禁用**（标品没级联禁用 · ISV 真要级联走 CS-05 BEC 通知或 CS-04 加禁用前置校验"先禁用所有派生实例"）

## 6. 删除规则（StructProjectDeleteOP）

### R-06 · delete / delete_project opKey

- **执行链**（`opkeys/delete_project.json`）：
  1. `StructProjectDeleteOP.onAddValidators` L35-38 注册 `StructProjectDeleteValidator`（独立 Validator · 不与其它共用）
- **`onPreparePropertys`** L40-44：声明 `enable + rootorg` 字段
- **`beginOperationTransaction`** L46-55：
  - 仅过滤 `enable='10'` 的行参与删除（`if (!"10".equals(dataEntity.getString("enable"))) continue`）
  - 调 `structProjectApplication.doWithDelete(dyToDel)`
- **业务约束**：
  - **R-06.1** · enable='1'（已启用）的方案：被跳过 · 不删除
  - **R-06.2** · enable='0'（已禁用）的方案：被跳过 · 不删除
  - **R-06.3** · enable='10'（启用中）的方案：参与删除
  - 这意味着删除一个**已启用的方案**走标品时会"看似成功但实际跳过" · ISV 必须加 CS-04 校验
- **缺失校验**（标品**没有**实现）：
  - **派生实例引用校验**：当前 `StructProjectDeleteOP` 没有反查 `haos_structure.relyonstructproject IN (deleteIds)` 的逻辑 · 删除时不会发现该方案被实例引用 → 业务必须加 CS-04 反向引用校验

## 7. 跟 haos_structure 的协作规则（核心母-子约束）

### R-07 · 母-子双向引用

- **本场景**：方案母本 · 在 haos_structproject 表单中创建 / 维护
- **haos_structure**：实例 · 引用本方案（`relyonstructproject` BasedataField → haos_structproject）
- **物理表共用**：`t_haos_structproject` 同表 · 通过 `roottype + otclassify` 等字段区分（haos_structure 实例 `otclassify=1010L`）
- **业务流向**：
  1. 在本场景建方案 → 启用方案 → 用户在 haos_structure 中选这个方案派生实例 → 用户在 `haos_orgstructlist` 维护实例下挂的组织树

### R-08 · 派生关系约束（业务硬规则 · 标品**未自动校验** · ISV 自补）

- **R-08.1**：方案被派生为 haos_structure 实例后 · 改 roottype（实↔虚切换）会改写整个下挂组织树（StructProjectSaveOp 实证 L139-181）· **必须先停所有派生实例**
- **R-08.2**：方案删除前 · 必须无活跃 haos_structure 实例引用（CS-04）
- **R-08.3**：方案禁用后 · 已派生实例的 haos_structure 行不会自动禁用（业务方期望什么 · ISV 自决）

### R-09 · 列表过滤共用键

- **本场景列表**（`StructProjectListPlugin.setFilter` L91-108）：
  - `issyspreset='0'`（默认排预置 · 与 haos_structure 列表行为一致）
  - `iscustomorg='0'`（默认排自定义组织视图）
  - `org in 用户管辖 BU`（除非有全权限）
- **haos_structure 列表**（详见 haos_structure_maintenance/03_model_design）：
  - 同样 `issyspreset='0'` + `enable in [1, 0]` + `creatorhaspermission` + `org.id in 用户 BU` + 跨域调 hrcs `getUserStructProjectsF7`
- **业务区分**：本场景列表少了"跨域调 hrcs"（母本不需要 hrcs 授权过滤）

## 8. 互斥编辑规则（MutexHelper）

### R-10 · 编辑互斥锁

- **触发**：`modify` opKey · `StructProjectEditPlugin.beforeDoOperation` L223-230
- **锁键**：`MutexHelper.require(view, "haos_structproject", id, "edit_struct", true, sb)`
- **缓存键**：`pageCache.put("edit_struct_clock", String.valueOf(modifyClock))`
- **失败处理**：抛 `OrgStructProjectPermTreeListPlugin_1` ResId · setCancel(true)
- **释放**：保存成功（save opKey）后 `MutexHelper.release("haos_structproject", "edit_struct", id)` L299
- **业务后果**：单租户单方案同时只能一个会话编辑

## 9. 列表过滤规则

### R-11 · 列表查询的三层 QFilter

`StructProjectListPlugin.setFilter` L91-108：

1. **L98**：`QFilter noPreSetFilter = new QFilter("issyspreset", "=", "0").or("id", "=", STRUCT_PROJECT_MANAGE)` · 默认排预置 · 但保留 `STRUCT_PROJECT_MANAGE` 这条预置母本
2. **L100**：`QFilter isCustomorgFilter = new QFilter("iscustomorg", "=", "0")` · 排自定义组织
3. **L103-105**：`OrgPermHelper.getHRPermOrg(false)` · 非全权限时按 `org in 管辖 BU` 收窄
4. **L107**：`event.setOrderBy("enable desc, number asc")` · 启用优先 · 同状态按编码升序

### R-12 · F7 选择控制

`StructProjectEditPlugin.beforeF7Select` L208-215：
- 字段是 `org` 或 `orgorg` 时 · 拿 `OrgPermHelper.getHRPermOrg(false)` · 非全权限按 `id in 管辖 BU` 加 customQFilter

## 10. 变更派单规则（"伪 BEC"派单）

### R-13 · `BatchAdminOrgNewOpService` 派单链（保存方案的派生根组织时触发）

- **`StructProjectSaveOp.saveStructProjectAndRootOrg`** 派生新组织时调 `BatchAdminOrgNewOpService(...).execute()` （L124）
- **`BatchAdminOrgNewOpService.endDoChangeOp`** L98-104：
  - 非 `isAddTempTag` 时调 `saveAdminOrgMsgDetail` + `saveAdminorgChangeLog`
- **`BatchAdminOrgNewOpService.afterTransDoOp`** L133-175：
  - 跨表写：`saveFullNameData / syncOrgUnit / syncOrgChgUnit`
  - **L172**：`new AdminChangeMsgService().handleChangeMsg()` 派单
- **`AdminChangeMsgService.handleChangeMsg`** L113-123：
  - 查 `sch_task` 是否已有 `JOB_ID="5+X/4Y=AOZ=O"` 处于 `SCHEDULED` 状态 · 已有则跳过（防风暴）
  - 否则 `JobClient.dispatch(jobInfo)` · 派单到调度服务
- **业务语义**：方案保存（派生新根组织）后 · 走 admin_org 域 sch_task 派单 · 通知下游（admin_org 异步消费）
- **跟标准 BEC 的区别**：
  - 标准 BEC：`EventServiceHelper.triggerEventSubscribeJobs(sourceApp, eventNumber, message, variables)` · 走业务事件中心
  - 本场景实证（grep 14 类 0 命中）：**没用** EventServiceHelper · 走 sch_task + JobClient.dispatch
  - 这是 admin_org 共用的派单机制 · ISV 想接订阅可以监听 `haos_adminorg_msgdetail` 表 · 不要套 hjm 的 `triggerEventSubscribeJobs`

## 11. 编码规则

### R-14 · CodeRule 自动生成 number

- save 链 RowKey=- 是 `CodeRuleOp`（标品平台）
- 业务侧在【编码规则基础资料】配 · PR-006
- 反编译实证：`StructProjectEditPlugin.propertyChanged` L262 切换 roottype=2 时 · `AdminOrgCodeRuleServiceHelper.create(view, model).setOrgNumber(hrDy, "rootnumber")` 自动给虚拟根组织赋编码

## 12. 异常码 / ResId 表（来源真实抛错）

| ResId | 中文 | 反编译位置 |
|---|---|---|
| `OrgStructProjectPermTreeListPlugin_1` | 当前单据已在其他页签中打开 · 请关闭后重试或重新登录 | `StructProjectEditPlugin.beforeDoOperation` L228 |
| `StructListPlugin_1` | 删除成功 | `StructProjectListPlugin.afterDeleteOperation` L194 |
| StructProjectValidator 内部 | 校验失败 | 反编译没追到 validator 内部 · 推测含 enable / disable / save 通用校验 |

## 13. 平台规则适用性（PR-001 ~ PR-011）

| PR | 是否适用 | 说明 |
|---|---|---|
| PR-001 ISV 并列挂不继承 | 强适用 | 标品 OP / FormPlugin 全 final · 不能继承（StructProjectEditPlugin / StructProjectListPlugin / StructProjectSaveOp / StructProjectDeleteOP / StructProjectDisableOp / StructProjectEnableOp / StructProjectBUListPlugin 都是 final）|
| PR-002 RowKey 顺序 | 适用 | 已观测：CodeRuleOp(无 RowKey) → BdVersionSaveServicePlugin(1) → HRBaseDataStatusOp(2) → ... |
| PR-003 FormPlugin/OP 数据 API 分层 | 适用 | StructProjectEditPlugin 用 getModel().setValue() · StructProjectSaveOp 用 entity.set() |
| PR-004 setValue 死循环防护 | **强适用** | 标品 propertyChanged L250-289 没用 beginInit/endInit · 但内部 setValue 调用都是"切换显示性"操作 · ISV 加联动必须包 beginInit/endInit |
| PR-005 ID.genLongId | 适用 | `StructProjectSaveOp` L94 走 `ORM.create().genLongId("haos_adminorgdetail")` 生成虚拟根组织 ID |
| PR-006 CodeRuleOp 业务侧配置 | 适用 | save 链含 CodeRuleOp · 业务侧"编码规则基础资料"配 |
| PR-007 预置数据 number 不可改 | 强适用 | `STRUCT_PROJECT_MANAGE` 预置母本 · `issyspreset='1'` |
| PR-008 iscurrentversion 时序过滤 | 弱适用 | BaseFormModel · 主表非 HisModel · 但 rootorg → haos_adminorg 引用是 HisModel · 跨表 join 时下游用 boid + iscurrentversion |
| PR-009 boid 业务维度 | 弱适用 | 本场景没 boid · 但派生 haos_adminorg 走 admin_org 域有 boid |
| PR-010 OP 13 方法 | 强适用 | save/delete/audit/disable/enable 全部走 OP 链 · onAddValidators (3) → beforeExecuteOperationTransaction (5) → beginOperationTransaction (6) → endOperationTransaction (8) → afterExecuteOperationTransaction (9) |
| PR-011 BEC | **关键** | 标品**没**走标准 BEC（`triggerEventSubscribe` 14 类 0 命中）· 走 sch_task + JobClient.dispatch · ISV 自建发布方走标准 BEC（CS-05）|

## 14. 跟 haos_structure 业务规则的差异

| 规则 | haos_structproject（本场景）| haos_structure（实例）|
|---|---|---|
| 列表过滤 | issyspreset + iscustomorg + org BU | issyspreset + enable in [1,0] + creatorhaspermission + 跨域调 hrcs · 多一层 |
| roottype 业务硬规则 | 切换会改写下挂组织（StructProjectSaveOp 实证）| 不存在 roottype 切换问题（实例不改根类型）|
| 派单链 | `BatchAdminOrgNewOpService → AdminChangeMsgService.handleChangeMsg → sch_task` | 实例本身 SAVE 不派单（只在派生新根组织时派）|
| 删除前置 | enable=10 才参与删除 + Validator 校验 | hrbddeletevalidator |
| 互斥锁 | 有 `edit_struct` 互斥锁 | 标品没有此锁 |

## 15. 关键反模式（绝对不能踩）

| 反模式 | 后果 | 正确做法 |
|---|---|---|
| 直接 `modifyMeta haos_structproject add field` | IsvSign 已签 · 平台拒绝 | 建 ISV 扩展元数据继承 haos_structproject（CS-01）|
| 在 OP 里 `getModel().setValue()` | model=null · NPE | OP 用 `entity.set(key, value)`（PR-003）|
| propertyChanged 里 setValue 不包 beginInit/endInit | 无限死循环 | 包 `getModel().beginInit() / endInit()`（PR-004）|
| 套用 hjm 的 `triggerEventSubscribeJobs` 模式发 BEC | 标品没发 · ISV 没配 eventNumber 的话静默失败 | 业务事件中心预配 eventNumber + 自建发布方（CS-05）|
| 删除时不校验 haos_structure 引用 | 删除母本后实例 relyonstructproject 引用悬挂 | 加 onAddValidators@delete 反向引用校验（CS-04）|
| 改 otclassify 业务硬编码 | `StructProjectRepository.createUserStructProjectFilter` 过滤掉看不见 | 用常量类引用 · 不硬编码 1010L |

---

## 来源追溯

- 反编译类（14 个）：`knowledge/_sdk_audit/_decompiled/scenarios/haos_structproject/`
- 字段实证：`scene_doc.json` 35 字段 + main_form.xml
- ResId：`StructProjectEditPlugin.beforeDoOperation` L228 + `StructProjectListPlugin.afterDeleteOperation` L194
- BEC 缺失实证：grep 14 类 `triggerEventSubscribe / IEventService / EventServiceHelper` 全部 0 命中
- 派单实证：`AdminChangeMsgService.java` L113-123 走 `sch_task + JobClient.dispatch`
- 配套场景：[../haos_structure_maintenance/](../haos_structure_maintenance/)（实例 · 反向引用 relyonstructproject）

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin -->

## chgaction 实证补充（StructProjectEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

## chgaction 实证补充（StructProjectBUListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectListPlugin -->

## chgaction 实证补充（StructProjectListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectListPlugin`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.structures.StructProjectListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp -->

## chgaction 实证补充（StructProjectSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `TimeLineHelper_0` | 实体{0}保存时间轴数据失败:{1} |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP -->

## chgaction 实证补充（StructProjectDeleteOP 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp -->

## chgaction 实证补充（StructProjectDisableOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp -->

## chgaction 实证补充（StructProjectEnableOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp -->

## chgaction 实证补充（StructOrgPermSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp -->
