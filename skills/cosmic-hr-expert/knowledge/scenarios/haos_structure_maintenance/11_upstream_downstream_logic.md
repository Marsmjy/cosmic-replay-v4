# 上下游联动 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 7 反编译类 + scene_doc.json refEntity + workbench refentity_reverse 实证
> **confidence**：verified

## 1. 上下游全景图

```
                          ┌────────────────────────────────────────┐
                          │ 上游（影响本场景的数据 / 服务）         │
                          ├────────────────────────────────────────┤
                          │ haos_adminorg / haos_adminorghrf7      │
                          │   · rootorg 字段引用                    │
                          │ haos_structproject                     │
                          │   · relyonstructproject 字段引用        │
                          │ bos_org                                │
                          │   · org 字段（创建组织 BU）             │
                          │ bos_user                               │
                          │   · creator/modifier/disabler 字段      │
                          │ hrcs (跨域服务)                        │
                          │   · IHRCSBizDataPermissionService       │
                          │     .getUserStructProjectsF7            │
                          │ homs 应用参数                           │
                          │   · creatorhaspermission                │
                          │ 编码规则基础资料                        │
                          │   · CodeRuleOp 走的                     │
                          └────────────────────────────────────────┘
                                            │
                                            ▼
                         ┌────────────────────────────────────────┐
                         │  本场景 haos_structure                  │
                         │  · 31 字段 · BaseFormModel              │
                         │  · 物理表 t_haos_structproject          │
                         │  · 5 个 OP 操作链                       │
                         └────────────────────────────────────────┘
                                            │
                                            ▼
                          ┌────────────────────────────────────────┐
                          │ 下游（依赖本场景的数据 / 服务）         │
                          ├────────────────────────────────────────┤
                          │ haos_orgstructlist                      │
                          │   · maintainframework 跳进的列表        │
                          │   · custom_param.struct_project_ids     │
                          │ haos_structorgdetail                    │
                          │   · 矩阵下挂组织详情                    │
                          │ haos_othorgstruct                       │
                          │   · 其它组织视图（OPM 异步算）          │
                          │ haos_adminorgstruct                     │
                          │   · 行政组织树（OrgPermHelper 反查）    │
                          │ homs_orgchart_new                       │
                          │   · 新版组织图表（StructProjectRepo     │
                          │     L105 跨域查）                       │
                          └────────────────────────────────────────┘
```

## 2. 上游详解 · 影响本场景的依赖

### 2.1 haos_adminorg（行政组织主表）· 强依赖

| 字段引用 | 关系类型 | 影响 |
|---|---|---|
| `haos_structure.rootorg` → `haos_adminorghrf7` | HRAdminOrgField | 本场景必填 · 没行政组织就建不了矩阵 |

**变更影响**：
- admin_org 重命名 → 本场景 `rootname` 反规范化字段不自动同步（需 ISV 监听 admin_org 变更补任务）
- admin_org 禁用 → F7 不显示 · 已选定的不影响读
- admin_org 删除 → 阻断（本场景 rootorg 引用 · admin_org 的 hrbddeletevalidator 兜底）

**ISV 注意事项**：
- admin_org 是 HisModel · 查时必须 `iscurrentversion=true`（PR-008）
- admin_org 引用按 boid（PR-009）

### 2.2 haos_structproject（结构化方案母本）· 强依赖

| 字段引用 | 关系类型 | 影响 |
|---|---|---|
| `haos_structure.relyonstructproject` → `haos_structproject` | BasedataField | 矩阵实例挂靠的母本 |
| 同物理表共用 t_haos_structproject | 共享物理列 | otclassify 区分实例（1010L）vs 母本（≠ 1010）|

**变更影响**：
- 母本字段加 / 改：物理列同步 · ISV 加字段如何隔离见 CS-06
- 母本启用 / 禁用：实例的 relyonstructproject F7 是否能选到
- 母本删除：阻断（hrbddeletevalidator）

**ISV 注意事项**：
- 母本是 BaseFormModel · 不是时序 · 不需要 PR-008
- 物理表共享是平台机制 · 不要假设"完全隔离"

### 2.3 hrcs（跨域权限服务）· 关键依赖

| 服务接口 | 调用位置 | 用途 |
|---|---|---|
| `IHRCSBizDataPermissionService.getUserStructProjectsF7` | `StructureListPlugin.java:122` + `StructProjectRepository.java:88, 105` | 拿用户授权可见的 structproject id 集合 |

**调用参数**（实证 `StructureListPlugin.java:122`）：
```java
HRMServiceHelper.invokeHRMPService(
    "hrcs",                                  // 模块号
    "IHRCSBizDataPermissionService",         // 接口名
    "getUserStructProjectsF7",               // 方法名
    new Object[]{
        currUserId,                          // 当前用户 id
        appId,                               // 应用 id
        "haos_structure",                    // 实体编码（注意：这里传的是本场景）
        "47150e89000000ac",                  // 权限项 id（view 权限）
        "rootorg",                           // 关联字段
        null                                 // 参数 map（可选 · 全领域开关在 StructProjectRepository 是 needToAllAreasStructProject）
    }
)
```

**容错机制**：三重判空兜底
```java
if (permResult != null && !permResult.isHasAllStruct() && !permResult.getAuthorizedStructs().isEmpty()) {
    structIds.addAll(permResult.getAuthorizedStructs());
}
```

### 2.4 PermissionServiceHelper · 平台权限服务

| 调用 | 位置 | 用途 |
|---|---|---|
| `getAllPermOrgs(userId, "21", "homs", "haos_adminorgdetail", "47150e89000000ac", false)` | `OrgPermHelper.java:43` | 拿用户的 HR 组织权限（HR 组织视图 viewType=21）|
| `checkPermission(userId, appId, "haos_structure", "3F/95X2VSZ=1")` | `StructureListPlugin.java:141, 203` | 检查"维护架构"权限项 |

**关键事实**：`OrgPermHelper.HOMS_APP = "homs"` —— 本场景虽然 form 在 haos 域 · 但权限校验绑的是 homs 应用。

### 2.5 应用参数（homs 应用）· 软依赖

| 参数键 | 影响 | 改后效果 |
|---|---|---|
| `creatorhaspermission` | R-14 列表过滤 · 决定 BU 内 HR 是否能看到自己创建的方案 | 缓存 TTL 内不实时 |

### 2.6 编码规则基础资料 · 软依赖

`CodeRuleOp` 在 save / submit 链 RowKey=- 位置（无显式 RowKey · 是平台模板挂的）· 业务侧在【编码规则基础资料】配。

## 3. 下游详解 · 依赖本场景的数据

### 3.1 haos_orgstructlist（矩阵下挂组织列表）· 直接下游

| 引用方式 | 来源 |
|---|---|
| `customParam.struct_project_ids` | `StructureListPlugin.showStructListPage L170` 传入 |
| `customParam.custom_parent_f7_prop = "boid"` | 同上 L169 |

**协作流程**：
```
本场景列表点 name → showStructListPage → 跳到 haos_orgstructlist
   · pageId = "haos_orgstructlist_" + structureId + "_" + mainPageId
   · billFormId = haos_structorgdetail
   · caption = StructProjectRepository.queryByPk("id,name").name
```

### 3.2 haos_structorgdetail（矩阵下挂组织详情）· 间接下游

通过 haos_orgstructlist 访问 · 本场景不直接调。

### 3.3 haos_othorgstruct / haos_adminorgstruct · 视图计算结果

| 下游 form | 关系 | 触发 |
|---|---|---|
| `haos_othorgstruct` | 其它组织视图 / 矩阵组织视图 | 标品 OPM 异步任务（细节标品未暴露）|
| `haos_adminorgstruct` | 行政组织树 | `OrgPermHelper.resetPermOrgResultWithSubWithDate L58` 按 structProjectId 反查 · 拿 structlongnumber 长编码 |

**ISV 注意**：
- ISV 不应直接写这两个表 · 是异步任务的产物
- 矩阵组织变更后 · OPM 异步任务会重算 · 但**没观测到** BEC 事件触发（标品没发）· 触发机制可能是定时 / push job · 不在反编译范围
- 如果业务方期望"实时重算" · 走 CS-05 自建 BEC 发布 · 然后让重算服务订阅

### 3.4 homs_orgchart_new · 跨域引用

`StructProjectRepository.createUserStructProjectFilterNewChart L105` 跨域调 hrcs 时绑的实体名是 `homs_orgchart_new` · 表示新版组织图表也按矩阵组织过滤显示。

### 3.5 报表 / BI 系统 · 隐式下游

| 下游 | 关系 |
|---|---|
| 组织相关报表 | 按 t_haos_structproject 直接查（注意区分 otclassify=1010 实例 vs 母本）|
| BI 数据集 | 同上 |

**ISV 注意**：
- 报表 SQL 必须按 `otclassify=1010L` 过滤 · 否则会带上母本数据
- 报表 join `haos_adminorg` 时按 boid 不按 id（PR-009）

## 4. 跨模块联动矩阵

| 下游模块 | 联动点 | 同步/异步 | 失败策略 | 实证来源 |
|---|---|---|---|---|
| hrcs（HR 业务数据权限）| `IHRCSBizDataPermissionService.getUserStructProjectsF7` | 同步 | 三重判空兜底 | StructureListPlugin L122 |
| OPM（视图计算）| haos_othorgstruct / haos_adminorgstruct 重算 | 异步 | 标品没发 BEC · 触发机制不明 | OrgPermHelper L58（仅查 · 不写）|
| 报表 / BI | 直接读 t_haos_structproject | 异步 | 业务接受延迟 | 隐式 |
| 编码规则 | save 时 CodeRuleOp 自动生成 | 同步 | 编码服务挂 → 保存失败 | save 链 RowKey=- |
| BdVersion 历史 | save 时写历史 · audit 时切版本 | 同步 | 事务保证 | save 链 RowKey=1 BdVersionSaveServicePlugin |
| t_haos_structproject_l 多语言子表 | save / submit 时同步写 | 同步 | 事务保证 | 平台机制 |
| 操作日志 | 几乎所有 opKey 都挂 HRBaseDataLogOp | 同步 | 失败抛 KDBizException | _auto_operations.md |

## 5. 数据归属链

```
   【租户】
       │
       ▼
   【应用 homs · BU 树】
       │
       ▼
   【homs.creatorhaspermission 应用参数】
       │
       ▼
   【bos_user 创建人】
       │
       ▼
   【bos_org 创建组织 BU · org 字段】
       │
       ▼
   【haos_structure 实例 · otclassify=1010L】
       │
       ├── rootorg → haos_adminorghrf7 → 行政组织树
       ├── relyonstructproject → haos_structproject 母本
       │
       ▼
   【t_haos_structproject 物理表 · 共用】
       │
       ▼
   【haos_orgstructlist 下挂组织列表 / haos_structorgdetail 详情】
       │
       ▼
   【haos_othorgstruct 视图计算】
       │
       ▼
   【报表 / BI / 标品下游业务模块】
```

## 6. ISV 扩展时的上下游影响检查

### 加字段（CS-01）

- [ ] 上游：admin_org / structproject 是否有同名字段冲突
- [ ] 下游：报表 / BI 是否需要新字段
- [ ] 物理列：t_haos_structproject 多列 · 母本侧报表可能多列
- [ ] 多语言：是否需要 `_l` 子表（取决于字段类型）

### 加 save 校验（CS-02 / CS-04）

- [ ] 上游：admin_org 是否启用 / 禁用影响校验语义
- [ ] 下游：影响 save 时间（多一次跨域调要测性能）

### 字段联动（CS-03）

- [ ] 上游：被联动字段（如 relyonstructproject）的 F7 是否会变（如母本启停影响 F7 数据）
- [ ] 下游：联动后字段是否被 BdVersion 写历史（取决于联动字段是否在版本管理范围）

### BEC 发布（CS-05）

- [ ] 上游：业务事件中心 eventNumber 是否预配
- [ ] 下游：订阅方是否能处理这个 eventNumber
- [ ] 主事务：是否在 afterExecuteOperationTransaction（不要在 endOperationTransaction）
- [ ] 重算服务：如果是为了触发 OPM 重算 · 那 OPM 是否真的订阅了

### 列表过滤（CS-08）

- [ ] 上游：标品 setFilter 已有逻辑（双 BU 闸 + hrcs 授权）· 不要清空
- [ ] 下游：影响列表加载性能

### 左树扩展（CS-07）

- [ ] 上游：bos_org BU 树查询性能
- [ ] 下游：左树渲染速度

## 7. 给业务方的依赖矩阵（决策用）

| 业务问 | 答 |
|---|---|
| 矩阵组织依赖什么 | rootorg（行政组织）+ relyonstructproject（母本）+ org（创建 BU）+ creator（用户）+ hrcs 授权 |
| 谁依赖矩阵组织 | haos_orgstructlist 下挂组织 · 报表 · OPM 视图计算 · 跨域 homs_orgchart_new |
| 改了矩阵组织谁会变 | 1) 下挂组织列表（caption / 过滤）· 2) 报表汇总数 · 3) OPM 视图（异步） |
| 删了矩阵组织谁会断 | 1) 下挂组织列表（hrbddeletevalidator 阻断）· 2) 标品依赖触发阻断 |
| 矩阵组织上下文需要什么权限 | 1) view（普通可见）· 2) 3F/95X2VSZ=1（维护架构）· 3) hrcs 授权（看别人的）|

## 8. 跨场景知识引用

- 上游 admin_org 详情：`scenarios/admin_org_quick_maintenance/03_model_design.md`
- 上游 structproject 母本：`scenarios/haos_structproject_xxx/...`（如有）
- 跨域 hrcs：`memory/cosmic_hr_scenario_atlas_v2.md` 看 hrcs 域场景
- BEC 模式：`memory/kb_cosmic_java_api_authoritative.md` + `_scenarios_t0/G2_business_event_message.json`
- 行政组织 boid 维度：`memory/feedback_har_values_not_authoritative.md`

## 9. 来源追溯

- refEntity 引用：`scene_doc.json fields[].refEntity`
- 反编译：`_sdk_audit/_decompiled/scenarios/haos_structure/*.java`
- 跨域调用：`StructureListPlugin.java:122` + `StructProjectRepository.java:88, 105`
- 权限服务：`OrgPermHelper.java:43-47` + `StructureListPlugin.java:141, 203`
- 反向引用：`knowledge/workbench/_indexes/refentity_reverse.json`（haos_structproject 段）

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
