# 业务流转 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 `_auto_plugin_semantics.md` (3 反编译类) + `rules_chain_all.json` (31 opKey) + `form_lifecycle_rules.json`
> **confidence**: verified
> **数据源**: CFR 反编译 PermRelateEdit/PermRelateList/HRAdminStrictPlugin (2026-04-28)

---

## 一、关联权限项不是"工作流单据" · 是"配置型基础资料"

`hrcs_permrelat` 没有 submit/audit/unaudit/confirmchange 等审批工作流操作（opkeys_index.json 实证 31 opKey 全无审批类）。它是一个**配置型基础资料**：

- ✅ 草稿态 → save → 持久化态（一步到位 · 无审批）
- ✅ 持久化态 → modify → 持久化态（直接改 · 无版本管理 · 非 HisModel）
- ✅ 持久化态 → delete → 删除态（物理删 · 同步删下游 hrcs_permrelatcfg）
- ✅ 行级别 issyspreset 标记预置 vs 用户自建 · 预置不可删/不可改 entitytypeid+app+permitem

**对比维度**：

| 场景 | 是否有审批工作流 | 是否有 HisModel 时序 | 是否有 BEC 发布 |
|---|---|---|---|
| `hrcs_permrelat`（本场景） | ❌ 否 | ❌ 否 | ❌ 否（grep 实证） |
| `hrcs_dynascheme` | ✅ submit/audit/confirmchange | ✅ 是 | ❌ 否 |
| `hrpi_pernontsprop`（员工档案） | ✅ submit/audit | ✅ 是 | ✅ 是（saveOp afterExecute 发） |
| `haos_adminorg` | ✅ effect/disable | ✅ 是 | ❌ 否（标品没发） |

⚠ **CS 设计警示**：不要套用 hjm_jobhr 3 层异步发 BEC 的模式给本场景 ·  本场景不发 BEC（PR-011 ISV 自建路径 CS-05 实证）。

---

## 二、入口流程图（菜单 → 列表 → 详情）

```
HR通用服务 / 权限管理 / 关联权限项
                ↓
  preOpenForm
  └── HRAdminStrictPlugin（HR 域准入闸 · F7 lookUp 直接放行）
       ├── 第一闸: PermissionServiceHelper.isAdminUser(uid)
       │       || PermCommonUtil.isCosmicUser(uid)
       └── 第二闸: HRAdminService.isHrAdmin()
                ↓ 通过双闸
  ListView (hrcs_permrelat 列表 · BillFormModel)
  └── beforeCreateListDataProvider
       └── 替换为 PermItemProvider（permRelateList 内部类）
  └── beforeCreateListColumns
       └── isNotShowFilter customParam 时取消 entitytype.number 超链接
                ↓ 用户操作
   ┌─────────┬─────────┬──────────┬──────────┬───────────┐
 [新建]    [修改]    [删除]      [auth]    [btnsycrole]  [exportscript]
   ↓         ↓         ↓          ↓             ↓             ↓
new      modify    delete      跳转          同步角色      生成 SQL
                                hrcs_permrelatcfg  (实时/全量)   导 INSERT
                                                                  脚本
```

---

## 三、新建/编辑详情流（PermRelateEdit · 主表单）

```
打开详情页 (BillFormModel · BillStatusField=null)
  ↓
registerListener
  ├── toolbarap.ItemClick (this)
  ├── entitytype.BeforeF7Select (this)
  ├── entryentity.entitytypeid.BeforeF7Select (this)
  ├── entryentity.app.BeforeF7Select (this)
  ├── entryentity.permitem.Click (this)
  └── btnsave.Click (this · 条件性)
  ↓
afterBindData
  ├── EntityCtrlServiceHelper.queryExistedForBidInfo(view)
  │     → PageCache: entityPerm + appEntityPerm
  ├── putAllBuInfoToCache
  │     → PageCache: allBuInfo (BU id→name)
  ├── 已有数据时：
  │     ├── setAppComboList(entity)
  │     │     ├── 查 EntityCtrlServiceHelper.getAppComboForPerm(entity, forBidAppEntity, forBidApps)
  │     │     ├── 0 项 → 锁 appcombo + showErrorNotification
  │     │     └── ≤1 项 → 锁 appcombo (无可选)
  │     ├── setValue(appcombo, app.id)
  │     ├── setMainPermItems(entity, app, isAdd=false)
  │     │     └── EntityCtrlServiceHelper.queryEntityPermItemIdNum(entity)
  │     │         → ComboEdit.setComboItems
  │     └── 分录行：
  │           ├── permitemid 翻译为 permitem 显示文本
  │           └── issyspreset=true → setEnable(false, [entitytypeid, app, permitem])
  ├── PageCache.put("ids", JSON.stringify([Long]))      // 旧分录 id
  ├── PageCache.put("originEntryInfo", JSON.stringify(Set<String>))
  └── setDataChanged(false)
  ↓
用户编辑（5 大联动）
  ├── 改 entitytype (主) → propertyChanged
  │     └── 已有主权限项或分录 → showConfirm("确定要清除主权限项和关联信息吗？", OKCancel)
  │           ├── confirmCallBack(Yes) → afterEntityChange()
  │           │     ├── setValue(mainpermitem, "")
  │           │     ├── deleteEntryData("entryentity")
  │           │     ├── setAppComboList(entity)
  │           │     └── setMainPermItems(entity, app, isAdd=true)
  │           └── confirmCallBack(Cancel) → beginInit/setValue(entitytype, oldValue)/endInit/updateView (PR-004)
  ├── 改 appcombo → propertyChanged
  │     ├── setValue(bizapp, appId)
  │     └── setMainPermItems(entity, app, isAdd=true)
  ├── 改 entryentity.entitytypeid → propertyChanged
  │     ├── setValue(app, null, rowIndex)
  │     ├── 计算 entityRelatedApps - removeForBidApp
  │     ├── 0 项 → 锁 + 报错
  │     └── 1 项 → setValue(app, single, rowIndex) + setEnable(false)
  ├── 改 entryentity.app → propertyChanged
  │     └── BU 不一致 → showTipNotification + setValue(entitytypeid, "")
  ├── 改 entryentity.permitem → propertyChanged
  │     └── newValue isEmpty → setValue(permitemid, "")
  └── 点 entryentity.permitem (TextEdit) → click()
        └── showForm("hrcs_choose_permitem", Modal, customParam)
              └── closedCallBack(actionId="hrcs_choose_permitem")
                    └── 解析 pk="permId||permName" 回填 permitemid + permitem
  ↓
增行/删行
  ├── 点 toolbar.addrows → beforeItemClick
  │     └── entitytype == null → showTipNotification + setCancel
  └── 点 deleteentry → beforeDoOperation
        └── issyspreset=true → showErrorNotification("预置数据无法删除") + setCancel
  ↓
点保存 (save)
  ├── beforeDoOperation(save)
  │     ├── entry.removeIf(entitytypeid == null)              // 剔空行
  │     └── BU 一致性校验：所有分录 BU == 主 BU                // 兜底（已实时拦截）
  ├── HRDataBaseOp.beforeExecuteOperationTransaction         // 标品 OP 默认行为
  ├── （事务内）落库 t_hrcs_permrelat + t_hrcs_permrelatentry + t_hrcs_permrelat_l
  ├── HRDataBaseOp.afterExecuteOperationTransaction          // 标品 OP 默认行为
  └── afterDoOperation(save)
        ├── 算新增分录数 (count vs PageCache.ids)
        │     └── > 0 → returnDataToParent({entityNum, permNum})
        ├── returnDataToParent({changed: "changed"})
        ├── afterSaveProcessing()
        │     ├── diff originEntryInfo vs newEntryInfo
        │     ├── PermRelateServiceHelper.deletePermRelateConfigs(deleteRows)  ← 同步 hrcs_permrelatcfg
        │     └── PermRelateServiceHelper.addPermRelateConfigs(addRows)        ← 同步 hrcs_permrelatcfg
        └── 计算受影响角色（calcRtPermRole）
              ├── 构造 List<RelatePermInfo>
              ├── 排除 alone 权限项 (查 hrcs_permrelatcfg.isassign=1)
              ├── 排除 permInfoMap.get(it.getPermId()) == null 的脏权限
              ├── PermRtSyncService.calcRtPermRole(list, mainList)
              ├── LOG.info cost time
              └── !isEmpty → returnDataToParent({syncRole: 1, resultRolePermMap})
```

---

## 四、列表流（PermRelateList · 列表表单）

### 4.1 delete 流

```
用户选择若干行 → 点 delete
  ↓
beforeDoOperation(delete)
  └── QFilter id in primaryKeyValues
      → PermRelateServiceHelper.queryPermRelates(filter) → Set permRelateCfgs
      → PageCache.put("deleteRows", JSON.stringify(permRelateCfgs))
  ↓
HRDataBaseOp.beforeExecuteOperationTransaction
  ↓ （事务内）
delete t_hrcs_permrelat + t_hrcs_permrelatentry + t_hrcs_permrelat_l
  ↓
afterDoOperation(delete) · operationResult.isSuccess
  └── PageCache.deleteRows 取出
      → PermRelateServiceHelper.deletePermRelateConfigs(deleteRows)   ← 下游同步删
```

### 4.2 auth 跳转流

```
用户选择行 → 点 auth
  ↓
beforeDoOperation(auth)
  └── ListShowParameter
        ├── setBillFormId("hrcs_permrelatcfg")
        ├── setFormId("bos_list")
        ├── setPageId("hrcs_permrelatcfg@" + parentPageId)
        └── ShowType.MainNewTabPage
      → view.showForm
  ↓
（不会调用 HRDataBaseOp.execute · 因 auth 是 donothing 类型）
```

### 4.3 btnsycrole 同步角色流（双路径）

```
用户点 btnsycrole
  ↓
beforeDoOperation(btnsycrole)
  ├── selectedRows == 0 → 全量路径
  │     ├── ConfirmTypes.Delete YesNo "数据量大·请谨慎操作·确认是否同步？"
  │     ├── writeOpLog(false, ...)
  │     └── confirmCallBack(syncAllRtPermRole, Yes)
  │           └── startJob()
  │                 ├── JobInfo.setTaskClassname("HRRelatePermTask")
  │                 ├── setJobType(REALTIME)
  │                 ├── setTimeout(1200)
  │                 └── JobForm.dispatch
  ├── selectedRows > 10 → showTipNotification("不能超出10行")
  │     └── writeOpLog(false, ...)
  ├── selectedRows in [1,10] → 实时路径
  │     ├── HRBaseServiceHelper.query("hrcs_permrelat", fields, qf)
  │     ├── PermRtSyncService.getRelatePermInfoPair(permRelates)
  │     ├── relatePermInfoList isEmpty
  │     │     → showConfirm("当前权限项为允许独立授权·不允许同步角色")
  │     │     → writeOpLog(false, ...)
  │     ├── PermRtSyncService.calcRtPermRole(relatePermInfoList, mainPermInfoList)
  │     │     LOG cost time
  │     ├── resultRolePermMap isEmpty
  │     │     → showConfirm("角色已包含关联权限项·无需同步")
  │     │     → writeOpLog(false, ...)
  │     └── 否则 → showForm("hrcs_syncroleperm", Modal, customParam)
```

### 4.4 exportscript 导出 SQL 流

```
用户选择若干行 → 点 exportscript
  ↓
afterDoOperation(exportscript) · operationResult.isSuccess
  └── selectedRows.getPrimaryKeyValues()
      → generateSql(pkIds)
            └── try (TXHandle tx = TX.requiresNew())
                  ├── HRBaseServiceHelper.query("hrcs_permrelat", "id,entryentity.issyspreset,entryentity.issynrole", filter)
                  ├── 临时改 (oldSynValueMap 缓存原值)
                  │     └── for row: row.set("issyspreset", '1'); row.set("isSynRole", '0')
                  ├── serviceHelper.save(query)        ← 第一次保存（导出前态）
                  ├── for id in ids:
                  │     ├── execScript("T_HRCS_PERMRELAT", fields, "fid = " + id)
                  │     ├── execScript("T_HRCS_PERMRELAT_L", fields, "fid = " + id + " and flocaleid = 'zh_CN'")
                  │     └── execScript("T_HRCS_PERMRELATENTRY", fields, "fid = " + id)
                  ├── exportFile(sqlList join "\n")
                  │     └── CacheFactory.getCommonCacheFactory().getTempFileCache().saveAsUrl(sqlFileName, ins, 5000)
                  │     → view.openUrl
                  ├── 改回原值
                  │     └── for row: row.set("isSynRole", oldValueMap.get(row.id))
                  └── serviceHelper.save(query)        ← 第二次保存（恢复原态）
```

⚠ **exportscript 用 TX.requiresNew 起新事务**：因为标品需要在同一逻辑里"改→读→改回"，必须独立事务避免污染外部事务上下文。

---

## 五、状态机（行级 · BillStatusField 不存在）

本场景**没有** BillStatusField · 没有 status 字段。"状态"概念在分录行级别用 `issyspreset` (是否预置) + `issynrole` (是否同步角色) 两个布尔字段表示：

| 行状态组合 | 业务含义 | UI 行为 |
|---|---|---|
| `issyspreset=true, issynrole=*` | 系统预置行 | entitytypeid/app/permitem 锁定不可改 · 不可删 |
| `issyspreset=false, issynrole=true` | 业务自建 + 参与角色同步 | 全字段可改 · 可删 · btnsycrole 时纳入计算 |
| `issyspreset=false, issynrole=false` | 业务自建 + 不参与角色同步 | 全字段可改 · 可删 · btnsycrole 时跳过 |

### 主表"状态"映射

主表也没有显式 status · 但有隐式生命周期：

```
不存在 ──new──→ 草稿态(in-memory) ──save──→ 持久化态
                                              │
                                              ├──modify──→ 持久化态(version 不变 · 非 HisModel)
                                              ├──delete──→ 删除态(物理删 + cascade hrcs_permrelatcfg)
                                              └──copy────→ 草稿态(新单 · 不带 id)
```

---

## 六、与上下游交互（数据流接口）

| 接口 | 方向 | 触发 | 同步/异步 |
|---|---|---|---|
| `hrcs_permrelat` ↔ `hrcs_permrelatcfg` | save 后增删同步 | afterSaveProcessing / afterDoOperation(delete) | ✅ 同事务后立即调（同库微服务） |
| `hrcs_permrelat` → `perm_role_perm` | calcRtPermRole 算变更 → 用户在弹窗里确认 → 角色更新 | save 后 / btnsycrole 操作 | ⚠ 半同步（计算实时 · 落库交给 hrcs_syncrolesel/syncroleperm 子页面流） |
| `hrcs_permrelat` → `sch_task` | btnsycrole 全量 → HRRelatePermTask | btnsycrole 未选行 + 用户确认 | ✅ 异步（JobForm.dispatch） |
| `hrcs_permrelat` ← `bos_entityobject` | F7 引用 | beforeF7Select | 同步查询 |
| `hrcs_permrelat` ← `hbp_devportal_bizapp` / `bos_devportal_bizapp` | F7 引用 | beforeF7Select | 同步查询 |
| `hrcs_permrelat` ← `perm_permitem` | showForm 子页面权限项选择 | click(permitem) | 同步查询 + Modal 子页面 |
| `hrcs_permrelat` ← `hbss_hrbucafunc` | BU 字典 | afterBindData putAllBuInfoToCache | 同步查询（缓存到 PageCache） |

---

## 七、不发 BEC 实证

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_permrelat/

(no results)
```

**结论**：本场景标品 0 处发 BEC 事件。
- 与 `hjm_jobhr` 3 层异步发 BEC 不同（参考 `feedback_bec_3layer_async_publish.md`）
- 与 `haos_structure` 标品没发同模式（参考 `feedback_bec_mode_per_scene_verify.md`）
- ISV 若需要发 BEC（CS-05） · **必须自建** · 走 `kd.bos.bec.api.IEventService.triggerEventSubscribeJobs` API · 在自建 OP 的 `afterExecuteOperationTransaction` 阶段调（PR-010 + PR-011）

⚠ **铁律 5/6 实证**：不套用 hjm 3 层异步模式 · 同应用不同 form 的 BEC 行为差异巨大 · 必须每场景 grep 实证。

---

## 八、关键超时/阈值（实证）

| 操作 | 阈值 | 失败行为 | 实证 |
|---|---|---|---|
| btnsycrole 实时同步 | ≤ 10 行 | > 10 行 → "不能超出10行·请修改。" | PermRelateList L170-L173 |
| HRRelatePermTask 后台同步 | 1200 秒超时 + 不可终止 | 超时 → 任务失败（CanStop=false 不能用户中断） | PermRelateList L255-L256 |
| exportscript 临时文件缓存 | 5000 秒 | 用户没下载 → 缓存清掉（需要重新点导出） | PermRelateList L359 |
| HR 域准入闸 (HRAdminStrictPlugin) | F7 lookUp 直接放行 · 否则双闸 | 双闸不通过 → setCancel + cancelMessage | HRAdminStrictPlugin L33-L53 |

---

## 九、开发动作触发地图（哪个 opKey/动作 → 改哪段代码）

| 用户动作 | opKey/事件 | 触发插件 | 主要业务规则节点 |
|---|---|---|---|
| 进入列表页 | preOpenForm + beforeCreateListDataProvider | HRAdminStrictPlugin / PermRelateList | FP_HAS1-3 + FP_LBCDP1 |
| 新建详情 | new + 进入 PermRelateEdit | PermRelateEdit | FP_RL1-6 + FP_ABD1-5 |
| 选主业务对象 | beforeF7Select(entitytype) | PermRelateEdit | FP_BF7_1 |
| 改主业务对象 | propertyChanged(entitytype) + confirmCallBack | PermRelateEdit | FP_PC1 + FP_CFB1-2 |
| 选应用 | propertyChanged(appcombo) | PermRelateEdit | FP_PC2 |
| 增分录行 | beforeItemClick(addrows) | PermRelateEdit | FP_BIC1 |
| 选分录业务对象 | beforeF7Select(entitytypeid) + propertyChanged | PermRelateEdit | FP_BF7_2 + FP_PC3 |
| 选分录应用 | beforeF7Select(app) + propertyChanged | PermRelateEdit | FP_BF7_3-5 + FP_PC5 |
| 选分录权限项 | click(permitem) + closedCallBack | PermRelateEdit | FP_CLK1 + FP_CCB1 |
| 删分录行 | beforeDoOperation(deleteentry) | PermRelateEdit | FP_BDO3 |
| 保存 | beforeDoOperation(save) + afterDoOperation(save) | PermRelateEdit | FP_BDO1-2 + FP_ADO1-4 |
| 列表删除 | beforeDoOperation(delete) + afterDoOperation(delete) | PermRelateList | FP_LBDO1 + FP_LADO1 |
| 列表跳授权 | beforeDoOperation(auth) | PermRelateList | FP_LBDO2 |
| 列表同步角色 | beforeDoOperation(btnsycrole) + confirmCallBack | PermRelateList | FP_LBDO3-5 + FP_LCFB1 |
| 列表导出脚本 | afterDoOperation(exportscript) | PermRelateList | FP_LADO2 |

→ 详细规则见 `form_lifecycle_rules.json`。
