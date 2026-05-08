# 数据流转 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + scene_doc.json + opKey 链路实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、数据落地表清单（写入方向）

| 物理表 | 业务含义 | 写入触发 | 写入方 |
|---|---|---|---|
| `t_hrcs_entityctrl` | 主实体 · 业务对象-应用映射头 | save 主流程 | 苍穹 ORM 框架（基础资料 SaveOp 通用机制 · 不需 ISV 写 SQL） |
| `t_hrcs_entitydimentry` | 分录子表 · 字段-维度-控权范围 | save 主流程（随主表级联 INSERT/UPDATE/DELETE） | 苍穹 ORM 框架 |
| `t_hrcs_entityctrl_l` | 主表多语言子表（如有） | save 主流程 | 苍穹 ORM 框架 · 自动维护 |
| `t_hrcs_entitydimentry_l` | 子表多语言子表（如有） | save 主流程 | 苍穹 ORM 框架 · 自动维护 |
| `hrcs_roledimension`（角色维度配置） | 下游联动表 · 落 ismust 维度的角色-范围 | save 的 endOperationTransaction（**独立事务**） + delete 的 beginOperationTransaction（同主事务） | `EntityControlSaveOp.syncMustDimToRoleDim` / `EntityCtrlServiceHelper.deleteRoleRange` |
| 操作日志表（hrcs 自建） | save / delete 操作日志 | save / delete 的 endOperationTransaction / beginOperationTransaction | `EntityCtrlLogService.resolveLog` |

⚠ **没有时序历史表**（无 `t_hrcs_entityctrl_his` / 无 `_history` 后缀表）· 因为本场景**不是 HisModel** · 数据改了直接覆盖 · 没有版本快照。

---

## 二、save 数据流（最复杂的链路）

```
[FormPlugin 层] 用户表单数据
    │
    ↓ EntityCtrlEdit.beforeDoOperation(save)
    │
    │  从 model.getDataEntity 拿 DynamicObject
    │  ├ propDimInfo = entryentity.collect { propkey → dimension.id }
    │  ├ originPropDimInfo = PageCache.get("originPropDimInfo")（保存前的快照 · afterBindData 时填）
    │  └ 这两个 map 都序列化到 formOperate.getOption().setVariableValue(...)
    │     → 让 OP 端在 endOperationTransaction 反序列化对比差集
    │
    ↓ 进入 OP 链
    │
    ↓ EntityControlSaveOp.beginOperationTransaction
    │
    │  for entity in dataEntities:
    │    if importtype != "override" && entity.bizapp == null:
    │      entity.set("bizapp", entitytype.bizappid)              ← 自动带应用
    │
    ↓ 苍穹 ORM 主流程
    │
    │  INSERT/UPDATE t_hrcs_entityctrl                            ← 主表 4 业务字段 + L0 系统字段
    │  INSERT/UPDATE t_hrcs_entitydimentry (per row)              ← 分录 · 8 业务字段
    │  INSERT/UPDATE t_hrcs_entityctrl_l (per locale)             ← 多语言（description）
    │  INSERT/UPDATE t_hrcs_entitydimentry_l (per row, per locale)← 多语言（desc）
    │
    ↓ EntityControlSaveOp.endOperationTransaction
    │
    │  // 1. 收集 ismust=true 的分录（要同步到 hrcs_roledimension）
    │  for entity in dataEntities:
    │    for entryRow in entity.entryentity:
    │      if entryRow.ismust:
    │        syncMustDims[dimId][entityId#appId] += [propKey]
    │
    │  // 2. 装载现有 roleDim
    │  Map<String, Set<DynamicObject>> entityRoleDims = assembleEntityRoleDim(...)
    │  ↓ SELECT fentitytypeid, froleid, fbizappid FROM t_perm_rolepermdetial
    │    WHERE fentitytypeid IN (entityIds)
    │  ↓ 查 HRBuCaServiceHelper.getBuCaFuncFromSpec 算 bucafunc
    │  ↓ load hrcs_roledimension WHERE role IN (roleIds)
    │
    │  // 3. 处理被删的 propkey（独立事务前）
    │  for entity in dataEntities:
    │    deleteRowsPostProcessing(entity, propDimInfo, originPropDimInfo, ...)
    │    ↓ 找出 propkey 已被改 / 删的差集
    │    ↓ 在内存的 roleDim.entry 里 removeIf 匹配 (entityNum, propKey)
    │    ↓ 加 DimRoleInfoModel("delete") 到 effectDimRoleList
    │
    │  // 4. 同步 ismust 维度（独立事务）
    │  syncMustDimToRoleDim(syncMustDims, entityRoleDims, entityRolesFuncMap, ...)
    │    ↓ for each toSync:
    │    ↓   找匹配 dimId 的 roleDim · 移除"已 enable=false 的对应行" (modify)
    │    ↓   找未匹配的 (role, buCaFunc) · 新建 roleDim 行 (add)
    │    ↓ TX.requiresNew():                                      ⚠ 独立事务！
    │      serviceHelper.save(toUpdateRoleDimList)
    │      ↓ INSERT/UPDATE hrcs_roledimension
    │      catch Exception: tx.markRollback()
    │
    │  // 5. 落操作日志
    │  EntityCtrlLogService.resolveLog(entityCtrlLogInfos)        ← 落操作日志（同主事务）
    │
    │  // 6. 清权限缓存
    │  HRPermCacheMgr.clearAllCache()
    │
    ↓ 主事务 commit
    │
    ↓ EntityCtrlEdit.afterDoOperation(save)
    │  HRPermCacheMgr.clearAllCache()                             ← 双重保险
```

**写入分布**：
- **主事务**：t_hrcs_entityctrl + t_hrcs_entitydimentry + 操作日志
- **独立事务**：hrcs_roledimension（解耦 · 失败不影响主事务）
- **非事务**：HRPermCacheMgr 缓存清空

---

## 三、delete 数据流

```
[列表层] 用户选中要删的主记录
    │
    ↓ EntityCtrlTreeListPlugin.beforeDoOperation(delete)
    │
    │  调 EntityCtrlServiceHelper.beforeDelOp(ids, entryIds, OperateOption)
    │  ↓ 内部：if 选中了 issyspreset = true 的记录 → return false
    │  ↓ 否则可能内部已经把"要删的角色范围"塞进 OperateOption.toDelDimRoleRanges
    │
    ↓ 进入 OP 链
    │
    ↓ EntityCtrlDelOp.beginOperationTransaction
    │
    │  toDelDimRoleRangesStr = OperateOption.getVariableValue("toDelDimRoleRanges", "")
    │  if 为空 → log "Del_IS_EMPTY" + 不做角色范围联动
    │
    │  for dataEntity in dataEntities:
    │    idList += dataEntity.id
    │    toDelDimRoleRanges += toDelDimRoleRangeMap.get(dataEntity.id)
    │
    │  for encoded in toDelDimRoleRanges:
    │    split('|') → roleId, entityId, propKey, dimId
    │    EntityCtrlServiceHelper.deleteRoleRange(roleDimHelper, dimId, entityId, propKey, roleId, [])
    │    ↓ 在 hrcs_roledimension 找匹配行
    │    ↓ entry.removeIf 匹配 (entitytype.id, propkey, dimension.id)
    │
    │  resolvePermLog(idList, logInfos)
    │    ↓ JSON 反序列化 EntityCtrlLogInfoMap
    │    ↓ EntityCtrlLogService.resolveLog
    │
    ↓ 苍穹 ORM 主流程
    │
    │  DELETE t_hrcs_entityctrl WHERE id IN (idList)
    │  DELETE t_hrcs_entitydimentry WHERE fid IN (idList)        ← 级联
    │  DELETE *_l (多语言子表)                                    ← 级联
    │
    ↓ 主事务 commit
    │
    ↓ EntityCtrlTreeListPlugin.afterDoOperation(delete)
    │  HRPermCacheMgr.clearAllCache()
```

⚠ **删除不级联清下游 `hrcs_datarule` / `hrcs_dynaformctrl`**：标品 OP 只清 `hrcs_roledimension` · 其他下游表（数据规则 / 虚字段控权）由其他模块的代码自己反查 entityctrl 是否还存在。**ISV 不要假设删了 entityctrl 就清了所有下游** —— 数据规则可能挂"幽灵引用"。

---

## 四、读取（query）流程

| 用途 | 入口 | 主调用 |
|---|---|---|
| 列表展示 | TreeList | 苍穹列表标准 ORM SELECT + setFilter（强制 entitytype.number is not null） |
| 表单 beforeBindData | `EntityCtrlEdit.beforeBindData` | model 自动加载 + EntityCtrlServiceHelper.getEntityFieldMap 装载 propname |
| 业务对象 F7 | `EntityCtrlEdit.beforeF7Select(entitytype)` | bulidEntityFilters 4 闸过滤 · setFormId("bos_listf7") |
| 维度 F7 | `EntityCtrlEdit.beforeF7Select(dimension)` | 6 分支条件构造 QFilter · setCloseCallBack("dimensionCallBack") |
| 角色维度装载 | `EntityControlSaveOp.assembleEntityRoleDim` | 算法 SQL `select fentitytypeid,froleid,fbizappid from t_perm_rolepermdetial where fentitytypeid in (...)` |
| 导出列填 propname | `EntityCtrlTreeListPlugin.afterQueryOfExport` | EntityMetadataCache + ChoiceFieldPageCustomQueryService.parsePropertySub |
| 跨模块 HAOS 结构项 | `EntityCtrlEdit.queryEntityPropOtclassifyIds` | HRMServiceHelper.invokeHRMPService("haos", "IHAOSStructProjectService", "queryStructProConfig", ...) |

---

## 五、缓存维度

| 缓存 key（PageCache） | 写入时机 | 读取时机 | 内容 |
|---|---|---|---|
| `noDBProps` | beforeBindData / putMainOrgFieldProp | beforeBindData L153 / closedCallBack L490 | List<String> · 不入库的虚字段 propkey · 控制 `authrange` 字段 setEnable=false |
| `bdPropInfos` | putMainOrgFieldProp / putDynaFormCtrlInfo | beforeF7Select(dimension) L202 | Map<String, String> · propkey → 关联的实体 number |
| `orgInfos` | putMainOrgFieldProp / putDynaFormCtrlInfo | beforeF7Select(dimension) L204 | Map<String, Integer> · propkey → buCaFunc id |
| `mainorgfield` | putMainOrgFieldProp（仅非 QueryEntityType） | showFieldForm L455 | String · 主组织字段 propkey · 弹子页面时透传 |
| `originPropDimInfo` | afterBindData | beforeDoOperation(save) → 传给 OP | Map<String, Long> · 加载时的 propkey → dimensionId · 用于 OP 端 endOperationTransaction 算差集 |
| `changedMustDim` | afterBindData / propertyChanged(ismust) | （内部状态 · 仅前端用） | List<Long> · 当前 ismust=true 的分录 id 列表 |
| `existMustDim` | afterBindData | （历史快照 · 仅前端用） | List<Long> · 加载时的 ismust=true 分录 id 列表 |
| `beforeOpData` | afterBindData | （未看到读取 · 推测 ISV 自建审计读取） | JSON List<EntityCtrlEntryRowModel> · 完整快照 |

| 缓存 key（HRAppCache "hrcs"） | 写入时机 | 读取时机 | 内容 |
|---|---|---|---|
| `entityFields` | TreeList.afterQueryOfExport | （跨页面共享 · 提升导出性能） | Map<entityNumber, Set<propKey>> · 字段集 |

| 缓存 key（HRPermCacheMgr 全局） | 写入时机 | 读取时机 | 内容 |
|---|---|---|---|
| 全部清空 | save / delete 后 | 后续权限校验 | HRPermCacheMgr 内部各种缓存（角色范围 / 维度等）· save / delete 后强制重建 |

---

## 六、字段流（ISV 加自定义字段后的数据传递）

ISV 在主实体 `hrcs_entityctrl` 加 `${ISV_FLAG}_remark`（备注）字段：

```
[Web 端] 用户填了"${ISV_FLAG}_remark = '初始化映射 · 试运行'"
    │
    ↓ Web → save HTTP 请求
    │
    ↓ 苍穹序列化为 DynamicObject · entity.set("${ISV_FLAG}_remark", ...)
    │
    ↓ EntityControlSaveOp.beginOperationTransaction
    │   不识别此字段 · 不影响 bizapp 自动带逻辑
    │
    ↓ ORM 主流程 INSERT t_hrcs_entityctrl 包含 ${ISV_FLAG}_remark 列
    │
    ↓ EntityControlSaveOp.endOperationTransaction
    │   不读不写 ${ISV_FLAG}_remark
    │   只关心 entityentity.ismust / propkey / dimension.id 等业务字段
    │
    ↓ 主事务 commit · ${ISV_FLAG}_remark 落库
```

→ ISV 字段对 save 主流程**透明**（标品 OP 不读不写 ISV 字段）· 这是好事：标品逻辑不会污染 ISV 字段。

---

## 七、跨模块数据传递

### 7.1 EntityCtrlEdit → HAOS（结构项目）

```
EntityCtrlEdit.queryEntityPropOtclassifyIds(entityNum, propKey)
    ↓
HRMServiceHelper.invokeHRMPService("haos", "IHAOSStructProjectService",
                                    "queryStructProConfig",
                                    new Object[]{entityNum, propKey, null})
    ↓
返回 List<Map> · 取 [0].otclassify (Set<Long>) 或 [0].structproject -> [].otclassify
    ↓
返回 List<Long> otclassifyIds 给 beforeF7Select 用于维度过滤
```

⚠ HAOS 服务返回结构有两种格式（顶层 otclassify 直接给 / 还是 structproject 嵌套里给）· 反编译里都做了兼容（L247-L253）

### 7.2 EntityCtrlEdit → bos_objecttype

```
EntityCtrlEdit.itemClick(addrows)
    ↓ 拿 entityType.number
    ↓ HRBaseServiceHelper("bos_objecttype").isExists(QFilter("number", "=", number))
    ↓
存在 → 走 showFieldForm 跳子页面
不存在 → 错误提示"该实体没有属性 · 请重新选择"
```

### 7.3 EntityCtrlEdit → hrcs_dynaformctrl（虚字段配置）

```
仅当 entityType 是 dynamic/virtual 实体时
    ↓
HRBaseServiceHelper("hrcs_dynaformctrl").queryOne(
    "entryentity.propkey,entryentity.bdtype,entryentity.bucafunc",
    QFilter("entitytype", "=", entityType.number)
)
    ↓
不存在 → 错误"请在虚字段数据控权配置中添加该业务对象的相关属性信息"
存在 → 装载 bdPropInfos / orgInfos 给后续 F7 用
```

---

## 八、失败回滚边界

| 失败场景 | t_hrcs_entityctrl | t_hrcs_entitydimentry | hrcs_roledimension | 操作日志 | HRPermCacheMgr |
|---|---|---|---|---|---|
| Validator 校验失败 | 未写入 | 未写入 | 未写入 | 未写入 | 未清 |
| 主事务异常回滚（在 beginOperationTransaction） | 已 INSERT 回滚 | 已 INSERT 回滚 | 未触发 | 未写入 | 未清 |
| `endOperationTransaction` 主事务部分（落日志）异常 | 已 INSERT 回滚 | 已 INSERT 回滚 | 已写入（独立事务）**残留** | 未写入 | 已清 |
| `TX.requiresNew()` 独立事务异常 | 已 INSERT | 已 INSERT | 已 markRollback 回滚 | 已写入 | 已清 |
| 主事务 commit 后 `clearAllCache` 异常 | 已 INSERT | 已 INSERT | 已写入 | 已写入 | 未清 |

⚠ **数据一致性陷阱**：`endOperationTransaction` 主事务部分异常时 · `hrcs_roledimension` 已经独立提交但 `t_hrcs_entityctrl` 回滚 · 会出现孤儿数据。这是标品的已知设计 · ISV 不要模仿。
