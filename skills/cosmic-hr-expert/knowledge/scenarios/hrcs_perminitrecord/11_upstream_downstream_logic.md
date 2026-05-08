# 上下游联动 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于场景反编译实证 + BEC 分析）
> **数据源**: `form_lifecycle_rules.json` + `scene_doc.json` + `curated_sdk.json`

## 🟢 场景定位

权限初始化（hrcs_perminitrecord）是 HR 权限体系中的 **批量授权入口**，位于 HR 基础服务云的 HR 通用服务（hrcs）应用下。它不是一个独立运行的模块，而是权限中心的"配置工作台"——通过它生成的数据最终落地到权限中心真正的权限表中。

## 🟢 上游：谁在调用权限初始化

| 上游模块 | 联动点 | 触发方式 | 数据传递 |
|---|---|---|---|
| 权限中心（perm） | labelpolicy 策略标签→跳转到本场景的 initrole/inituserrole 路径 | ListPlugin.beforeShowBill 设 customParam inittype | 通过 BillShowParameter 传递 inittype=role/userrole |
| HR 管理员工作台 | 在工作台菜单点"权限初始化" | 菜单直达 | 无特殊参数，默认 userrole 模式 |
| SessionManager 跨表单缓存 | 导出模板异步任务完成→通知列表页刷新下载链接 | FP_LBDO1：taskInfo 回调时 SessionManager.getCurrent().put(key, pageId) 激活 tab | pageId + download URL |
| 子表单嵌入（hrcs_dynadim 等） | 主表单内嵌 4 个子表单（hrcs_dynadim/hrcs_permroleinitfunc/hrcs_permroleinitdim/hrcs_permroleinitdr） | FormPlugin.beforeBindData 动态加载子表单 | 通过 FormShowParameter 传递 recordId |

## 🟢 下游：权限初始化会写入哪些模块

### finishinit 核心落库链（userrole 模式）

```
PermInitRecordEdit.afterDoOperation(finishinit)
  → UserPermInitConvertService.convertRecord(recordId)
    → hrcs_userrolerelat 表（用户-角色关联）
    → hrcs_userdimval 表（用户维度值）
    → hrcs_userdatarule 表（用户数据规则）
    → hrcs_userbdscope 表（用户基础资料范围）
    → hrcs_userfieldperm 表（用户字段权限）
```

### finishinit 核心落库链（role 模式）

```
PermInitRecordEdit.afterDoOperation(finishinit)
  → PermRoleInitService.getInstance().initRole(recordId)
    → perm_role 表（角色基本信息）
    → perm_rolefunc 表（角色功能权限）
    → perm_roledim 表（角色维度）
    → perm_roledata 表（角色数据范围）
    → perm_rolefield 表（角色字段权限）
```

### 下游受影响的 HR 子模块

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **权限中心（perm）** | finishinit 落库角色功能权限/维度/数据范围/字段权限 | 同步（同一事务） | 失败弹检查结果页（hrcs_perminitcheckresult / hrcs_roleinitcheckresult），用户修正后重试 |
| **HR 基础服务（hbp）** | HRBaseDataLogOp 写入操作日志；HRBaseDataStatusOp 更新状态 | 同步 | OP 插件链内任一失败整条操作回滚 |
| **sch_task 调度中心** | 异步导出任务 | 异步 | taskInfo.isTaskEnd() 回调通知；失败不阻塞主流程 |
| **BEC 业务事件中心** | 本场景标品不发布 BEC（CS-05 实证 0 处发布） | N/A | ISV 如需事件发布需自行在 ext OP afterExecute 调 IEventService |

## 🟢 跨模块数据依赖

### 读取依赖（本场景需要从其他模块读数据）

| 依赖模块 | 读取内容 | 调用方式 |
|---|---|---|
| 实体元数据缓存 (EntityMetadataCache) | paintErrMarkCol/initRoleFdEntry 回填字段中文名时查实体字段 DisplayName | `EntityMetadataCache.getDataEntityType(entityNumber)` |
| 用户中心 (bos_user) | userdimentry.dim_user 引用 bos_user 基础资料 | F7 选择器（BasedataField refEntity=bos_user） |
| 角色 (perm_role) | userdimentry.dim_role 引用 perm_role 基础资料 | F7 选择器（BasedataField refEntity=perm_role） |
| 组织架构 (bos_org_biz) | userdimentry.dim_bucafunc 引用职能类型 | F7 选择器（BasedataField refEntity=bos_org_biz） |
| PageCache 跨表单 | 字段中文名缓存（entity → propKey → propName 字典） | this.getPageCache().put("entityFields", map) / .get("entityFields") |
| SessionManager | 导出模板下载 URL 跨表单传递 | SessionManager.getCurrent().put(key, pageId) |

### 写入依赖（本场景写入后影响其他模块）

| 目标模块 | 写入时机 | 写入内容 | 影响 |
|---|---|---|---|
| 权限中心 | finishinit 完成后 | 用户角色绑定/角色功能权限/维度授权/数据范围/字段权限 | 用户登录后即刻有权访问对应菜单/查看数据；权限变更立即生效（无审批流程） |
| 操作日志 | 每次操作 | HRBaseDataLogOp 写操作日志记录 | 审计追溯 |

## 🟢 权限初始化与其他 HR 权限配置场景的关系

| 场景 | 关系 | 差异 |
|---|---|---|
| 权限中心-角色管理 | 下游（role 模式 finishinit → 落到 perm_role） | 角色管理是单个角色配置；权限初始化是批量导入 |
| 权限中心-用户授权 | 下游（userrole 模式 finishinit → 落到用户角色关联） | 用户授权是单个用户逐个配；权限初始化是 Excel 批量导入 |
| 权限中心-功能权限分配 | 下游 | 功能权限在 role 模式中按角色批量分配 |
| HR 管理员工具 | 上游 | 管理员工具通过 labelpolicy 跳转到 initrole/inituserrole |

## 🟢 BEC 事件发布现状与 ISV 扩展建议

**标品现状**：本场景 **不发布 BEC 事件**。finishinit 成功后直接调用内部 Service 落地权限表，不通过 BEC 总线通知下游。

**ISV 扩展建议**：如果 ISV 需要在权限初始化完成后触发其他模块的动作（如发送通知、同步到第三方系统），应在 ext Op plugin 的 `afterExecuteOperationTransaction` 方法中判断 `operateKey == "finishinit"` 且 `dealstatus == 1` 后，自行发布 BEC 或调外部接口：

```java
// ext PermInitRecordDeleteOp.afterExecuteOperationTransaction
if ("finishinit".equals(args.getOperateKey())) {
    for (DynamicObject obj : args.getDataEntities()) {
        String dealstatus = obj.getString("dealstatus");
        if ("1".equals(dealstatus)) {
            // 发布自定义 BEC 事件 或 调 HRMServiceHelper 发通知
            // IEventService svc = EventServiceFactory.getService();
            // svc.triggerEventSubscribeJobs("hrcs", "perm_init_finished", "权限初始化完成", vars);
        }
    }
}
```

## 🟢 数据流向总图

```
┌─────────────────────────────────────────────────┐
│ 上游输入                                          │
│  菜单直达 │ labelpolicy 跳转 │ SessionManager 激活  │
├─────────────────────────────────────────────────┤
│ 权限初始化主表单 (hrcs_perminitrecord)             │
│   inittype=userrole         inittype=role         │
│   ┌──────────┐              ┌──────────┐         │
│   │ 4子分录   │              │ 5子分录   │         │
│   │ userdim  │              │ rolebase │         │
│   │ userdr   │              │ rolefunc │         │
│   │ userbd   │              │ roledim  │         │
│   │ userfield│              │ roledata │         │
│   └────┬─────┘              │ rolefield│         │
│        │                    └────┬─────┘         │
│   finishinit                finishinit            │
│   UserPermInitConvertSvc    PermRoleInitSvc       │
│        │                         │                │
├────────┼─────────────────────────┼────────────────┤
│ 下游输出│                         │                │
│   ┌────▼─────────────────────────▼──┐            │
│   │  权限中心 (perm) 真正的权限表      │            │
│   │  · 用户角色关联                   │            │
│   │  · 角色功能权限                   │            │
│   │  · 用户/角色维度授权              │            │
│   │  · 用户/角色数据范围              │            │
│   │  · 用户/角色字段权限              │            │
│   └──────────────────────────────────┘            │
│   ┌──────────────────────────────────┐            │
│   │  操作日志 (HRBaseDataLogOp)        │            │
│   └──────────────────────────────────┘            │
└─────────────────────────────────────────────────┘
```

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`hrcs_perminitrecord`，所属 HR 基础服务云）引用了其他云的 **3** 个底座实体：

### ⬆️ 组织发展云（`org_dev`）3 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `dim_otclassify` | 团队分类来源 | BasedataField | `haos_otclassify` | — |
| `dim_structproject` | 构架方案 | BasedataField | `haos_structproject` | [haos_structproject](../haos_structproject/) |
| `rdata_structproject` | 构架方案 | BasedataField | `haos_structproject` | [haos_structproject](../haos_structproject/) |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/hr_hrmp_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
