# 业务流转 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于反编译 5 类 + opkeys_index.json 25 opKey 全量梳理
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## ⭐ 关键事实 · 本场景没有状态机

`hrcs_entityctrl` 是 **BillFormModel 配置型基础资料** · **不是工作流单据** · 不走 audit/submit/disable/confirmchange · 没有 `billstatus` / `enable` / `datastatus` 这种状态字段（实证 `scene_doc.json` 17 字段中没有任何状态字段）。

**实证**：opKey 列表中 **0 个** audit/unaudit/submit/unsubmit/enable/disable/confirmchange/change opKey（`opkeys_index.json` 25 个 opKey 全部是 CRUD + 导航 + 导入导出）。

→ 因此本文档不画"草稿 → 已提交 → 已审核"状态机 · 而是画**业务对象映射的生命周期**。

---

## 一、业务对象映射的生命周期

```
[1] 用户进入菜单
    ↓ HRAdminStrictPlugin.preOpenForm 准入闸（非 HR 管理员直接拒）
    ↓
[2] TreeList 列表
    ↓ EntityCtrlTreeListPlugin.setFilter（强制 entitytype.number is not null + 排除孤儿记录）
    ↓
[3] 用户点"新增"  ──→ 进入 BillFormModel 表单
    ↓ EntityCtrlEdit.beforeBindData / afterBindData / registerListener
    ↓
[4] 用户选"业务对象"
    ↓ EntityCtrlEdit.beforeF7Select(entitytype) · 4 闸过滤
    ↓ 用户选完  ──→ propertyChanged(entitytype)
    ↓ 自动带 bizapp + 装载 bdPropInfos / orgInfos / noDBProps 到 PageCache
    ↓
[5] 用户点"添加行"按钮（toolbar.addrows）
    ↓ EntityCtrlEdit.itemClick · 校验 bos_objecttype 中该实体有属性
    ↓ 跳 hrcs_choosefield_page 子页面
    ↓ 用户选字段后回填 (closedCallBack actionId="perm_choosefieldpage")
    ↓ createNewEntryRow + setValue propkey/propname [+ 默认 authrange=2 if noDBProp]
    ↓
[6] 用户配每行的"维度 + 控权范围 + 是否必选"
    ↓ EntityCtrlEdit.beforeF7Select(dimension) · 6 分支过滤
    ↓ 用户选完  ──→ closedCallBack actionId="dimensionCallBack" · 回填 dimensionId
    ↓
[7] 用户点保存
    ↓ EntityCtrlEdit.beforeDoOperation(save)
    ↓ · 校验分录非空
    ↓ · setVariableValue propDimInfo / originPropDimInfo / tag_of_view
    ↓
    ↓ 进入 OP 链
    ↓
    ↓ HRBaseDataLogOp.beforeExecute (HR 基础资料操作日志)
    ↓ EntityControlSaveOp.onAddValidators · 注册 EntityControlSaveValidator
    ↓ · Validator 校验
    ↓ EntityControlSaveOp.beginOperationTransaction · 自动带 bizapp
    ↓ · INSERT/UPDATE t_hrcs_entityctrl + t_hrcs_entitydimentry
    ↓ EntityControlSaveOp.endOperationTransaction（事务中收尾）
    ↓ · 收集 ismust=true 的分录
    ↓ · assembleEntityRoleDim → 查 t_perm_rolepermdetial 拿涉及角色
    ↓ · syncMustDimToRoleDim → INSERT/UPDATE hrcs_roledimension（独立事务 TX.requiresNew）
    ↓ · deleteRowsPostProcessing → 清理被删 propkey 对应的角色范围
    ↓ · EntityCtrlLogService.resolveLog 落操作日志
    ↓ · HRPermCacheMgr.clearAllCache
    ↓ HRBaseDataLogOp.afterExecute
    ↓
[8] EntityCtrlEdit.afterDoOperation(save) · operationResult.isSuccess
    ↓ HRPermCacheMgr.clearAllCache（双重保险）
    ↓
[9] 列表刷新（标品行为）

【后续维护】
[A] 用户改某行 ismust（编辑态）
    ↓ EntityCtrlEdit.propertyChanged(ismust) · 更新 PageCache.changedMustDim
    ↓ 用户保存 → 重走 [7] 流程

[B] 用户删某行（编辑态）
    ↓ EntityCtrlEdit.beforeDoOperation(deleteentry)
    ↓ · 检查 issyspreset · 命中拒绝
    ↓ · 弹"删除后影响角色控权"二次确认（confirmCallBack="delete_confirm"）
    ↓ 用户点 Yes → confirmCallBack 执行 deleteEntryRows
    ↓ 用户保存 → 重走 [7] 流程

[C] 用户从列表删整条主记录
    ↓ EntityCtrlTreeListPlugin.beforeDoOperation(delete)
    ↓ · EntityCtrlServiceHelper.beforeDelOp 校验（issyspreset 等）
    ↓ EntityCtrlDelOp.beginOperationTransaction
    ↓ · 读 OperateOption.toDelDimRoleRanges
    ↓ · EntityCtrlServiceHelper.deleteRoleRange 清 hrcs_roledimension
    ↓ · EntityCtrlLogService.resolveLog 落删除日志
    ↓ HRBaseDataLogOp.afterExecute
    ↓ EntityCtrlTreeListPlugin.afterDoOperation · HRPermCacheMgr.clearAllCache
    ↓ 列表刷新
```

---

## 二、save opKey 完整执行链（最复杂的 opKey）

实证：`rules_chain_all.json` save opKey · 反编译 5 类整合：

```
order=1 · HRBaseDataLogOp                        【父类模板 · HR 基础资料操作日志】
order=2 · EntityControlSaveOp                    【主 OP · ⭐ 业务核心】
            │
            ├ 阶段 4: onAddValidators
            │   └ args.addValidator(new EntityControlSaveValidator())
            │
            ├ 阶段 5: beforeExecuteOperationTransaction
            │   └ (默认实现 · 校验阶段触发)
            │
            ├ 阶段 6: beginOperationTransaction
            │   └ if (importtype != "override") {
            │     for entity in dataEntities:
            │       if (entity.bizapp == null && entitytype.bizappid != null)
            │         entity.set("bizapp", entitytype.bizappid)
            │   }
            │
            ├ 阶段 8: endOperationTransaction
            │   └ if (importtype != "override") {
            │     收集 ismust=true 分录 → syncMustDims map
            │     entityRoleDims = assembleEntityRoleDim(entityIds, entityRolesFuncMap)
            │     ↓ 查 t_perm_rolepermdetial · 拿到所有相关 roleDim
            │     ↓ 调 HRBuCaServiceHelper.getBuCaFuncFromSpec 算 buCaFunc id
            │     ↓ load hrcs_roledimension 数组
            │
            │     if propDimInfoStr 非空：
            │       deleteRowsPostProcessing(...)        【处理被删的 propkey · 从 roleDim 移除】
            │
            │     syncMustDimToRoleDim(...)              【ismust 维度同步到 roleDim】
            │       ↓ for each toSync (dimId, entity, propKeys):
            │       ↓   for each existing roleDim (matching dimId):
            │       ↓     从 entry collection 中移除 enable=false 的对应行 (modify)
            │       ↓   for each new role + buCaFunc:
            │       ↓     INSERT 一行 roleDim (add)
            │       ↓ TX.requiresNew(): serviceHelper.save(toUpdateRoleDimList)   ⚠ 独立事务！
            │
            │     EntityCtrlLogService.resolveLog(entityCtrlLogInfos)
            │     HRPermCacheMgr.clearAllCache()
            │   }
            │
            └ 其余阶段（阶段 7/9/10/11/12）使用父类默认实现
```

**关键事实**：
- `EntityControlSaveOp` 重写了 **3 个生命周期方法**：`onAddValidators` / `beginOperationTransaction` / `endOperationTransaction`
- save 走完后 · `t_hrcs_entityctrl` 主表 + `t_hrcs_entitydimentry` 分录子表 + `hrcs_roledimension`（多个表多行）+ HR 操作日志 全部更新
- ⚠ `TX.requiresNew()` 让 hrcs_roledimension 写入跟主事务**解耦** · 主事务回滚不会撤销 hrcs_roledimension · 这是标品的设计选择

---

## 三、delete opKey 完整执行链

```
order=1 · HRDataBaseOp                            【父类模板 · 默认实现】
order=2 · HRBaseDataLogOp                         【父类模板 · HR 基础资料操作日志】
order=3 · EntityCtrlDelOp                         【主 OP · ⭐ 业务核心】
            │
            └ 阶段 6: beginOperationTransaction
                ├ 读 OperateOption.toDelDimRoleRanges
                ├ idList = [dataEntity.id, ...]
                ├ toDelDimRoleRanges = [encoded "roleId|entityId|propKey|dimId", ...]
                │
                ├ for each encoded range:
                │   split('|') → roleId, entityId, propKey, dimId
                │   EntityCtrlServiceHelper.deleteRoleRange(...)
                │   ↓ 在 hrcs_roledimension 里
                │   ↓ entry.removeIf 匹配 (entityNum, propKey, dimId)
                │
                └ resolvePermLog(idList, logInfoStr)
                  ↓ JSON 反序列化 EntityCtrlLogInfoMap
                  ↓ EntityCtrlLogService.resolveLog
```

**关键事实**：
- `EntityCtrlDelOp` 只重写 **1 个**生命周期方法（`beginOperationTransaction`）· 其他都用父类
- 列表批量 delete 必经过 `EntityCtrlTreeListPlugin.beforeDoOperation` 的 issyspreset 拦截 + `afterDoOperation` 的 `HRPermCacheMgr.clearAllCache`

---

## 四、deleteentry opKey（删行 · 不是删主表）

```
order=1 · HRBaseDataLogOp                         【操作日志】
order=2 · EntityControlSaveOp                     【绑在 deleteentry 但只用 onAddValidators 注册 Validator · 实际删行业务在 FormPlugin 层】
order=3 · HRDataBaseOp                            【父类默认实现】
order=4 · EntityCtrlDelOp                         【绑在 deleteentry 但实际只在主表 delete 触发】
```

⚠ 实际语义：
- `deleteentry` 是**前端层**的"删行"按钮 · 走 FormPlugin `EntityCtrlEdit.beforeDoOperation(deleteentry)` 拦截（issyspreset 校验 + 二次确认）
- 用户点 Yes 后才调 `deleteEntryRows("entryentity", selectRows)` 删除内存中的分录行（不入库）
- **真正落库**要等用户后续点 save · 重走 save opKey 的 OP 链

→ 因此 deleteentry opKey 的 OP 链**不直接影响数据库** · 只是审计 + Validator 注册形式上的链

---

## 五、并发与事务边界

| 操作 | 主事务 | 独立事务 | 备注 |
|---|---|---|---|
| `save` 主表 + entryentity 落库 | ✅ 主 | - | INSERT/UPDATE t_hrcs_entityctrl + t_hrcs_entitydimentry |
| `save` 同步 hrcs_roledimension | - | ✅ TX.requiresNew | 实证 `EntityControlSaveOp.syncMustDimToRoleDim` L251-L258 |
| `save` 落 EntityCtrlLog | ✅ 主 | - | 实证 L143 |
| `save` 清缓存 HRPermCacheMgr | （非事务） | - | 实证 L144 |
| `delete` 主表删除 | ✅ 主 | - | DELETE t_hrcs_entityctrl + 级联子表 |
| `delete` 清 hrcs_roledimension | ✅ 主 | - | 实证 `EntityCtrlDelOp.beginOperationTransaction` 在主事务里 |
| `delete` 落删除日志 | ✅ 主 | - | 实证 L69 |

⚠ 事务陷阱：save 时 `TX.requiresNew()` 让 hrcs_roledimension 跟主事务解耦 · 如果主事务因为 Validator 失败回滚 · 但**外层 endOperationTransaction 已经跑完**（先跑 endOperationTransaction · 后失败回滚是不可能的 · endOperationTransaction 是事务提交前最后一阶段）。这个设计通常是为了：
- 避免大量 hrcs_roledimension 更新影响主事务 commit 性能
- 同步 ismust 维度本质上是"跨表后置同步" · 不强一致

---

## 六、并发场景

| 场景 | 风险 | 标品处理 |
|---|---|---|
| 两个用户同时给同一业务对象配映射 | F7 自带"排除已配置"过滤 · 第二个用户根本看不到该业务对象 · 自然防冲突 | F7 过滤兜底 |
| 一个用户保存的同时另一个用户在删 | 主事务并发会有 Lock 等待 · `t_hrcs_entityctrl` 行级锁 | 数据库锁 |
| save 时其他用户在改 hrcs_roledimension | TX.requiresNew 拿到的是新事务 · 与其他事务并行 · 行锁 | 数据库锁 |
| 缓存与数据库不一致 | 两个用户分别 save · 都 clearAllCache · 谁后清谁赢（最终一致） | 缓存最终一致 |

---

## 七、错误回滚策略

| 失败点 | 回滚行为 | 备注 |
|---|---|---|
| `EntityCtrlEdit.beforeDoOperation(save)` 校验失败 | `args.setCancel(true)` · 主事务都没开始 | 前端拦截 · 用户看到错误提示 |
| `EntityControlSaveValidator.validate()` 失败 | 主事务不开 · `addErrorMessage` | onAddValidators 阶段 4 |
| `beginOperationTransaction` 抛异常 | 主事务回滚 · t_hrcs_entityctrl 不入库 | 阶段 6 |
| `endOperationTransaction` 抛异常（独立事务里 catch + markRollback） | 独立事务回滚 hrcs_roledimension · **主事务正常 commit** | 实证 L255-L257 |
| 主事务 commit 后 `clearAllCache` 失败 | 缓存清不干净 · 但下次 save 会再清 | `HRPermCacheMgr.clearAllCache` 是非事务的 |

⚠ **失败提示都通过 ResManager.loadKDString 国际化** · ISV 的错误提示也应走同样模式（参考 PR-001 / `EntityCtrlEdit.java` L264 / L280 / L286 / L321 / L330 / L434）

---

## 八、ISV 扩展时的流程切入点

| 想做的事 | 推荐切入点 | 详见 |
|---|---|---|
| 校验 propkey + dimension 不重复（save 前） | 自建 Validator 挂 onAddValidators | CS-03 |
| save 后通知下游系统 | 自建 OP 挂 afterExecuteOperationTransaction（PR-010 阶段 9） | CS-05 |
| save 时记录映射变化日志（自建表） | 自建 OP 挂 endOperationTransaction（与主事务同事务） | CS-06 |
| 删除前查 hrcs_datarule 引用 | 自建 Validator 挂 delete 的 onAddValidators | CS-04 |
| 业务对象 → 维度 联动（选了 X 自动带 Y） | 自建 FormPlugin 挂 propertyChanged | CS-02 |
| 加业务字段（如"映射备注"）| modifyMeta add field（非代码扩展） | CS-01 |
