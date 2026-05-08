# 上下游联动 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + scene_doc.json + 跨模块调用实证
> **confidence**: verified
> **数据源**: 反编译 + OpenAPI (2026-04-28)

---

## 一、上下游全景图

```
                    ┌─────────────────────────────────────────────┐
                    │  bos / 苍穹平台                              │
                    │  ─ bos_entityobject (业务对象元数据)          │
                    │  ─ bos_objecttype (业务对象的属性元数据)      │
                    │  ─ hbp_devportal_bizapp (应用基础资料)        │
                    │  ─ bos_listf7 (F7 列表壳)                     │
                    └─────────┬───────────────────────────────────┘
                              │ 上游引用
                              ↓
┌──────────────────────┐    ┌─────────────────────────────────────┐    ┌──────────────────────┐
│  hrcs_dimension      │───→│   hrcs_entityctrl ⭐ 本场景          │←───│  hrcs_dynaformctrl    │
│  (维度基础资料)       │    │                                      │    │  (虚字段数据控权)      │
│  上游                │    │  t_hrcs_entityctrl                  │    │  上游（虚字段实体场景） │
│                      │    │  + t_hrcs_entitydimentry            │    │                       │
└──────────────────────┘    └─────────┬───────────────────────────┘    └──────────────────────┘
                                      │
                                      │  save 联动写
                                      │  delete 联动清
                                      ↓
            ┌─────────────────────────────────────────┐
            │  hrcs_roledimension                      │
            │  (角色维度配置 · 下游)                    │
            │                                          │
            │  按 (role × dimension × bucafunc × propkey × entitytype) 笛卡尔积存数据 │
            └─────────────────────────────────────────┘
                                      │
                                      │  运行时反查
                                      ↓
            ┌─────────────────────────────────────────┐
            │  hrcs_datarule (数据规则 · 下游)         │
            │  ─ 不主动联动 · 运行时按 entityctrl + roledimension 算规则 │
            └─────────────────────────────────────────┘

           ┌─────────────────────────┐
           │ haos / IHAOSStructProjectService │  跨模块 RPC（F7 维度过滤）
           │ ← EntityCtrlEdit.queryEntityPropOtclassifyIds │
           └─────────────────────────┘
```

---

## 二、上游引用关系

| 上游表 | 字段引用 | 引用方向 | 引用方式 |
|---|---|---|---|
| `bos_entityobject` | `entitytype` | hrcs_entityctrl → 引用 | BasedataField · 严过滤（4 闸：modeltype + istemplate + 排除已配置 + HR 域有控权项） |
| `hbp_devportal_bizapp` | `bizapp` | hrcs_entityctrl → 引用 | BasedataField · 自动从 entitytype.bizappid 带 |
| `hrcs_dimension` | `entryentity.dimension` | hrcs_entityctrl → 引用 | BasedataField · 严过滤（6 分支按 propkey 类型） |
| `bos_objecttype` | （隐式） | itemClick 时校验 | `HRBaseServiceHelper("bos_objecttype").isExists(QFilter("number","=",entityNumber))` 实证 L325-L327 |
| `hrcs_dynaformctrl` | （隐式） | 仅虚字段实体场景 | `HRBaseServiceHelper("hrcs_dynaformctrl").queryOne(...)` 实证 L370 |
| `bos_user` | `creator` / `modifier` | 系统级（autoComputed） | 平台维护 |

---

## 三、下游写表关系

### 3.1 hrcs_roledimension（核心下游）

**写入触发**：

| 触发 | 实证位置 | 写入方式 | 事务 |
|---|---|---|---|
| save · ismust=true 维度同步 | `EntityControlSaveOp.syncMustDimToRoleDim` L187-L259 | `serviceHelper.save(toUpdateRoleDimList)` | 独立事务（TX.requiresNew · L251-L258） |
| save · 删除被去掉的 propkey | `EntityControlSaveOp.deleteRowsPostProcessing` L148-L178 | `entry.removeIf` + `roleDimHelper.update` | 主事务 |
| delete · 清角色维度 | `EntityCtrlDelOp.beginOperationTransaction` L47-L70 | `EntityCtrlServiceHelper.deleteRoleRange` | 主事务 |

**写入数据格式**（从反编译 + scene_doc 推断）：
```
hrcs_roledimension {
    role            (BasedataField → perm_role)
    dimension       (BasedataField → hrcs_dimension)
    bucafunc        (BasedataField → 业务单元类型)
    entry [ {                                           ← 嵌套子表
        entitytype   (匹配 entityctrl.entitytype)
        propkey      (匹配 entityctrl.entryentity.propkey)
        enable       (true / false 启用状态)
    } ]
}
```

**业务键**：`(role, dimension, bucafunc)` 三维定位一行 · 其下 entry 数组按 (entitytype, propkey) 二维定位行内子表。

### 3.2 hrcs_datarule（被动重算 · 不联动）

**关系**：`hrcs_datarule` 表的数据规则按 `entitytype` 引用业务对象 · 运行时按 entityctrl + roledimension 计算"该业务对象上的字段在该角色范围下能看哪些数据"。

**风险**：
- 删除 entityctrl 主记录后 · datarule 留幽儿引用（标品不联动清）→ 静默 skip
- 修改 entityctrl 的 entitytype（或 ismust 切 false）后 · datarule 变化但不立即体现 · 直到下次重算

**规避**：
- 加 CS-04 自建 Validator 阻止有 datarule 引用时删 entityctrl
- 加 CS-05 自建 BEC 通知 datarule 模块订阅刷新

### 3.3 操作日志（hrcs 自建表）

**写入触发**：
- save · `EntityCtrlLogService.resolveLog(entityCtrlLogInfos)` 实证 `EntityControlSaveOp.endOperationTransaction` L143
- delete · `EntityCtrlLogService.resolveLog(entityCtrlLogInfos)` 实证 `EntityCtrlDelOp.resolvePermLog` L72-L83

**写入数据格式**：`EntityCtrlModel` + `DimRoleInfoModel` 序列化 · 含 effectDimRoleList（被影响的角色范围列表）

---

## 四、跨模块联动（同步 / 异步）

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **hrpi (人事)** | 不直接联动 | - | - |
| **pay (薪酬)** | 不直接联动 | - | - |
| **attendance (考勤)** | 不直接联动 | - | - |
| **performance (绩效)** | 不直接联动 | - | - |
| **hrcs · hrcs_roledimension** | save / delete 联动 | save：独立事务（异步语义）<br>delete：同主事务（同步） | save 独立事务失败 → markRollback 回滚 hrcs_roledimension · 主事务正常提交（孤儿主记录） |
| **hrcs · hrcs_datarule** | 运行时算 | 异步（每次访问规则时按当前数据计算） | 不一致时规则失效（静默 skip） |
| **hrcs · hrcs_dynaformctrl** | 上游引用（虚字段实体场景）| 同步（用户进 entityctrl 表单时实时查） | 找不到则报错"请在虚字段数据控权配置中添加" |
| **haos · IHAOSStructProjectService** | F7 维度过滤 | 同步 RPC（每次维度 F7 时跨模块） | 失败时 otclassifyIds 为空 · 维度 F7 走默认路径 |
| **bos · 权限服务（HRPermCacheMgr）** | save / delete 后清空全局缓存 | 同步（非事务） | 清空失败 · 下次 save 再清 |

⚠️ **本场景与 HR 业务子域（hrpi/pay/attendance/performance）无直接联动** · 这些子域通过 `hrcs_datarule` 间接消费 entityctrl 配置 · 不直接读 entityctrl 表。

---

## 五、跨模块 RPC 调用清单

实证：反编译 5 类中的所有跨模块调用：

| 调用 | 实证位置 | 用途 |
|---|---|---|
| `HRMServiceHelper.invokeHRMPService("haos", "IHAOSStructProjectService", "queryStructProConfig", ...)` | `EntityCtrlEdit.queryEntityPropOtclassifyIds` L242 | 查"业务对象组织结构项目配置"· 用于维度 F7 过滤 |
| `HRBaseServiceHelper("bos_objecttype").isExists(QFilter("number","=",number))` | `EntityCtrlEdit.itemClick` L325-L327 | 校验业务对象有属性 |
| `HRBaseServiceHelper("hrcs_dynaformctrl").queryOne(...)` | `EntityCtrlEdit.putDynaFormCtrlInfo` L370 | 拿虚字段属性配置 |
| `HRBaseServiceHelper("hrcs_entityctrl").query("entitytype", null)` | `EntityCtrlEdit.bulidEntityFilters` L516-L517 | 列出已配置的业务对象（F7 排除） |
| `HRBaseServiceHelper("hrcs_roledimension").loadDynamicObjectArray(QFilter("role","in",roleIds))` | `EntityControlSaveOp.assembleEntityRoleDim` L307 | 装载现有角色维度配置 |
| `EntityCtrlServiceHelper.queryEntityForBidInfo(...)` | `EntityCtrlEdit.bulidEntityFilters` L522 | 拿"禁用做权限映射的实体集"（F7 排除） |
| `EntityCtrlServiceHelper.getHRApps(false)` | `EntityCtrlEdit.bulidEntityFilters` L526 | 拿 HR 应用集 |
| `EntityCtrlServiceHelper.getAllHrHasPermItemEntity(view, validApps)` | `EntityCtrlEdit.bulidEntityFilters` L527 | 拿"HR 域且有控权项的实体集"（F7 In 过滤） |
| `EntityCtrlServiceHelper.getAllNoExistEntityCtrlNumbers()` | `EntityCtrlTreeListPlugin.setFilter` L82 | 列出已不存在的业务对象（List 排除孤儿） |
| `EntityCtrlServiceHelper.beforeDelOp(ids, entryIds, OperateOption)` | `EntityCtrlTreeListPlugin.beforeDoOperation` L97 | 删除前置校验（含 issyspreset 等） |
| `EntityCtrlServiceHelper.deleteRoleRange(roleDimHelper, dimId, entityNum, propKey, roleId, [])` | `EntityCtrlDelOp.beginOperationTransaction` L66 | 删除时清角色维度行 |
| `RoleManageService.getNoCtrlPermEntitysFromCache()` | `EntityCtrlEdit.bulidEntityFilters` L524 | 拿"非控权实体集"（F7 排除） |
| `EntityCtrlLogService.resolveLog(entityCtrlLogInfos)` | `EntityControlSaveOp.endOperationTransaction` L143 / `EntityCtrlDelOp.resolvePermLog` L81 | 落操作日志 |
| `HRPermCacheMgr.clearAllCache()` | `EntityCtrlEdit.afterDoOperation` L296 / `EntityControlSaveOp.endOperationTransaction` L144 / `EntityCtrlTreeListPlugin.afterDoOperation` L108 | 清 HR 权限全缓存 |
| `HRBuCaServiceHelper.getBuCaFuncFromSpec(appEntityMap)` | `EntityControlSaveOp.assembleEntityRoleDim` L290 | 按应用查 buCaFunc |
| `ChoiceFieldPageCustomQueryService.parsePropertySub(entityType, _, queryCondition, "1=1", noDBProps)` | `EntityCtrlEdit.putMainOrgFieldProp` L417 / `EntityCtrlTreeListPlugin.afterQueryOfExport` L129 | 解析业务对象的所有字段 |
| `EntityOrgFieldBuQueryService.getPropBuMap(entityNumber)` | `EntityCtrlEdit.putMainOrgFieldProp` L391 | 解析业务对象的组织字段 |

⚠️ 这些 ServiceHelper 类大多在 `hrcs` 业务包内 · 默认**不带 `@SdkPublic` 注解** · ISV 调用前必须查白名单（详见 `curated_sdk.json` `hrcs` 分类 + `cosmic_hr_sdk_whitelist_audit.md`）。

---

## 六、ISV 自建表（如有）的下游接入建议

如果 ISV 自建表（如 `${ISV_FLAG}_extentityctrlmeta` 自定义元数据扩展表）需要接入 hrcs_entityctrl 数据流：

| ISV 自建表角色 | 推荐接入方式 |
|---|---|
| 监听变更（被动） | CS-05 BEC 订阅方 · `IEventServicePlugin.handleEvent` 接 `${ISV_FLAG}_entityctrl_changed` 事件 |
| 反查映射数据（主动） | `HRBaseServiceHelper("hrcs_entityctrl").query(...)` + QFilter（注意非 HisModel · 不要加 iscurrentversion） |
| 同事务联动（强一致） | 自建 OP 挂 save / delete 的 `endOperationTransaction`（同主事务 · 失败一起回滚） |
| 异步联动（最终一致） | 自建 OP 挂 save / delete 的 `afterExecuteOperationTransaction`（事务后 · CS-05 模式） |

---

## 七、上游变化对本场景的影响

| 上游变化 | 对本场景影响 | 应对 |
|---|---|---|
| `bos_entityobject` 删除某业务对象 | hrcs_entityctrl 中该 entitytype 的主记录变孤儿 | TreeList setFilter 已加 `entitytype.number is not null` 排除孤儿（实证 L82） |
| `hrcs_dimension` 删除某维度 | hrcs_entityctrl 分录中该 dimension 引用变孤儿 | 标品没自动清 · 需要业务侧先清 entityctrl 再删 dimension |
| `hbp_devportal_bizapp` 删除某应用 | hrcs_entityctrl 中 bizapp 引用变孤儿 | 业务侧少见 · 影响小 |
| `hrcs_dynaformctrl` 删除虚字段配置 | hrcs_entityctrl 编辑该业务对象时 showFieldForm 报错 | 业务 SOP：先建虚字段配置再来 entityctrl |
| `bos_objecttype` 业务对象的属性变化 | hrcs_entityctrl 已配的 propkey 可能引用不存在的属性 | 标品没自动清 · 用户手动删失效行 |
| 新增 hrcs 业务对象（角色配权限项） | hrcs_entityctrl F7 自动可见（4 闸自动通过） | 无需手动 |

---

## 八、下游变化对本场景的影响

| 下游变化 | 反向影响 | 应对 |
|---|---|---|
| `hrcs_roledimension` 被手工 SQL 改 | 本场景 save 不感知 · 下次 save 走 syncMustDimToRoleDim 时按当时状态重新比对 | 业务侧不要手工改 hrcs_roledimension |
| `hrcs_datarule` 大量新增 | 不影响本场景 save / delete · 但 CS-04 反查会变慢 | 加索引 / 缓存 |
| `hrcs_userrolerelat` 变化 | 不影响本场景 | - |
| 删除 hrcs_entityctrl 主记录后下游孤儿 | datarule / dynaformctrl 留幽灵引用 | CS-04 加自建 Validator 防误删 |

---

## 九、参考的反编译实证文件

- `EntityCtrlEdit.java` —— 表单插件
- `EntityCtrlTreeListPlugin.java` —— TreeList 插件
- `EntityControlSaveOp.java` —— save OP
- `EntityCtrlDelOp.java` —— delete OP
- `HRAdminStrictPlugin.java` —— HR 域准入闸

所有源码位于 `knowledge/_sdk_audit/_decompiled/scenarios/hrcs_entityctrl/`。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

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

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
