# 数据流转 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 3 反编译类 + form_lifecycle_rules.json + rules_chain_all.json
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin (2026-04-28)

---

## 一、数据落地总览

```
                   ┌─────────────────────────────┐
                   │   hrcs_permrelat (主表单)    │
                   │   PermRelateEdit FormPlugin  │
                   └──────┬─────────────┬─────────┘
                          │ save        │ delete (列表)
                          ▼             ▼
                   ┌──────────────┐  ┌────────────────┐
                   │  HRDataBaseOp │  │  HRDataBaseOp  │
                   └──────┬───────┘  └──────┬─────────┘
                          │ 同事务         │ 同事务
                          ▼                ▼
       ┌───────────────────────────────────────────────────┐
       │  事务范围内: t_hrcs_permrelat (主表)              │
       │             + t_hrcs_permrelat_l (多语言)         │
       │             + t_hrcs_permrelatentry (分录)        │
       └─────────────────────┬─────────────────────────────┘
                             │
                             ▼ afterDoOperation / afterSaveProcessing
       ┌─────────────────────────────────────────┐
       │  事务后: 调 PermRelateServiceHelper       │
       │  ├── add/deletePermRelateConfigs         │
       │  │     → t_hrcs_permrelatcfg (下游)     │
       │  └── (calcRtPermRole 仅算 · 不直写库)    │
       └─────────────────────────────────────────┘
                             │
                             ▼ 用户在弹窗确认
       ┌─────────────────────────────────────────┐
       │  hrcs_syncrolesel/syncroleperm 子页面   │
       │  → t_perm_role / t_perm_role_perm        │
       └─────────────────────────────────────────┘
                             │
                             ▼ 全量同步路径
       ┌─────────────────────────────────────────┐
       │  HRRelatePermTask (sch_task 调度)        │
       │  → t_perm_role_perm 批量更新             │
       └─────────────────────────────────────────┘
```

---

## 二、save 操作的数据落地链路（主入口）

### 2.1 时序

| 时刻 | 阶段 | 操作 | 数据变化 |
|---|---|---|---|
| t0 | 用户点【保存】 | UI 触发 | model 进入 save 流 |
| t1 | beforeDoOperation(save) | FormPlugin 拦截 | entry.removeIf 剔空行 + BU 校验 |
| t2 | onPreparePropertys | OP | （标品 HRDataBaseOp 默认） |
| t3 | onAddValidators | OP | （标品 HRDataBaseOp 默认） |
| t4 | beforeExecuteOperationTransaction | OP | （标品 HRDataBaseOp 默认） |
| t5 | beginOperationTransaction | OP | 开事务 |
| t6 | （事务内）SaveServiceHelper | 平台 | INSERT/UPDATE t_hrcs_permrelat + _l + entry |
| t7 | endOperationTransaction | OP | 事务提交前最后机会 |
| t8 | （事务提交） | 平台 | 数据落库 |
| t9 | afterExecuteOperationTransaction | OP | （标品 HRDataBaseOp 默认 · 不发 BEC · grep 实证） |
| t10 | afterDoOperation(save) | FormPlugin | returnData + afterSaveProcessing + calcRtPermRole |
| t11 | PermRelateServiceHelper.add/delete | （新事务） | INSERT/DELETE t_hrcs_permrelatcfg |

### 2.2 主表写入字段（save 路径）

| 字段 | t_hrcs_permrelat | t_hrcs_permrelat_l | 写入时机 |
|---|---|---|---|
| `id` | fid | fid | save 时平台分配（ORM.genLongId） |
| `creator` | fcreatorid | — | t6 平台自动 |
| `createtime` | fcreatetime | — | t6 平台自动 |
| `modifier` | fmodifierid | — | t6 平台自动 |
| `modifytime` | fmodifytime | — | t6 平台自动 |
| `description` | — | fdescription | t6 多语言写 _l 表（每语言一行 fpkid 分布式 id） |
| `initdatasource` | finitdatasource | — | t6（系统设默认值 · 用户自建一般 = "user"） |
| `entitytype` (id) | fentitytypeid | — | t6 |
| `bizapp` (id) | fappid | — | t6 |
| `mainpermitem` | fpermitemid | — | t6 |

### 2.3 分录子表写入字段

| 字段 | t_hrcs_permrelatentry | 写入时机 | 备注 |
|---|---|---|---|
| `id` | fid | t6 | 主表 fid（分录共享主表 id） |
| `entryid` | fentryid | t6 | 平台分配（ORM.genLongIds("hrcs_permrelat.entryentity", n)） |
| `seq` | fseq | t6 | 平台自动按行号 |
| `entitytypeid` | fentitytypeid | t6 |   |
| `app` | fappid | t6 |   |
| `permitemid` | fpermitemid | t6 | 逗号分隔多权限项 ID |
| `issyspreset` | fissyspreset | t6 | 默认 false · 用户自建 |
| `issynrole` | fissynrole | t6 | 默认 true · 用户可改 |

### 2.4 下游 hrcs_permrelatcfg 同步（t11）

`afterSaveProcessing()` 调用 `Sets.difference` 算出 deleteRows / addRows · 然后：

```java
PermRelateServiceHelper.deletePermRelateConfigs(deleteRows);  // 物理删 hrcs_permrelatcfg
PermRelateServiceHelper.addPermRelateConfigs(addRows);         // 插入 hrcs_permrelatcfg
```

`hrcs_permrelatcfg` 是细粒度授权配置表 · 一条 hrcs_permrelat 主+分录组合在 hrcs_permrelatcfg 里展开为 N 条记录（笛卡尔积 entryentity × permitemid_split）。

⚠ **事务边界**：`PermRelateServiceHelper.deletePermRelateConfigs/addPermRelateConfigs` 是**独立 OP** · 跟主 save 不在同一事务 · 因此存在主 save 成功 + 下游同步失败的可能（标品没有补偿机制 · ISV 应在自建 OP 加监控）。

---

## 三、delete 操作的数据流（列表路径）

### 3.1 时序

| 时刻 | 阶段 | 操作 | 数据变化 |
|---|---|---|---|
| t0 | 用户列表选行点【删除】 | UI | List 触发 delete |
| t1 | beforeDoOperation(delete) | List FormPlugin | QFilter id in pkIds → queryPermRelates → PageCache.deleteRows |
| t2-t8 | OP 13 阶段（标品） | OP | （事务内）DELETE t_hrcs_permrelat + _l + entry |
| t9 | afterDoOperation(delete) | List FormPlugin | PermRelateServiceHelper.deletePermRelateConfigs(deleteRows) |

### 3.2 物理表删除范围

```
DELETE FROM t_hrcs_permrelat       WHERE fid IN (?...)    -- 主表
DELETE FROM t_hrcs_permrelat_l     WHERE fid IN (?...)    -- 多语言
DELETE FROM t_hrcs_permrelatentry  WHERE fid IN (?...)    -- 分录（cascade by fid）
DELETE FROM t_hrcs_permrelatcfg    WHERE relateid IN (?...) -- 下游（独立调）
```

⚠ **物理删 vs 逻辑删**：本场景是**物理删**（不像 HisModel 把 enable 改成 10）· 因为没有版本控制 · 删了就没了 · ISV 不要假设可以"撤销删除"。

---

## 四、btnsycrole 的数据流（角色同步链路）

### 4.1 实时路径（选 1-10 行）

```
 User: select 1-10 rows → click btnsycrole
   ↓
 PermRelateList.beforeDoOperation
   ├── HRBaseServiceHelper.query("hrcs_permrelat", 6 fields, "id in pkIdSet")
   ├── PermRtSyncService.getRelatePermInfoPair(permRelates)
   │     → Pair<List<RelatePermInfo>, List<RelatePermInfo>>  (relate, main)
   ├── PermRtSyncService.calcRtPermRole(relate, main)
   │     ├── 查 perm_role_perm 当前角色权限映射
   │     ├── 比对 应该有的关联权限项 vs 实际有的
   │     └── 返回 LinkedHashMap<roleId, Set<permId>>  (need-to-add 列表)
   ├── 空 → showConfirm("角色已包含·无需同步")
   └── 非空 → showForm("hrcs_syncroleperm", customParam={roleInfo, roleCount, permCount})
   ↓ 用户在子页面确认
 hrcs_syncroleperm 子页面落库 t_perm_role_perm  (子页面独立事务)
```

### 4.2 全量路径（未选行）

```
 User: 不选行 → click btnsycrole
   ↓
 PermRelateList.beforeDoOperation
   └── ConfirmTypes.Delete YesNo "数据量大·请谨慎操作·确认是否同步？"
   ↓ confirmCallBack(syncAllRtPermRole, Yes)
 PermRelateList.startJob
   ├── new JobInfo
   ├── setTaskClassname("kd.hr.hrcs.bussiness.service.perm.dimension.HRRelatePermTask")
   ├── setParams({syncAll: 1})
   ├── setJobType(REALTIME)
   ├── setTimeout(1200)
   └── JobForm.dispatch → sch_task 调度
                            └── HRRelatePermTask.execute
                                  └── 全量扫 hrcs_permrelat
                                        → calcRtPermRole 批处理
                                        → 批量 update t_perm_role_perm
```

### 4.3 数据流接触表

| 表 | 读/写 | 触发路径 |
|---|---|---|
| `hrcs_permrelat` (主) | 读 | calcRtPermRole 输入 |
| `hrcs_permrelatcfg` (下游) | 读 | calcRtPermRole 算 alone 排除 |
| `perm_permitem` | 读 | getPermInfo · 校验权限项有效性 |
| `perm_role` | 读 | calcRtPermRole 算受影响角色清单 |
| `perm_role_perm` | 读+写 | calcRtPermRole 输入 + 子页面/Job 落库写入 |

---

## 五、exportscript 的数据流（导出路径）

```
 User: select rows → click exportscript
   ↓
 PermRelateList.afterDoOperation(exportscript) · isSuccess
   └── generateSql(pkIds)
         ├── try (TXHandle tx = TX.requiresNew())   ← 起新事务防污染
         │   try
         │     ├── HRBaseServiceHelper.query(6 fields, "id in pkIds")
         │     ├── (缓存原值 oldSynValueMap)
         │     ├── for row: row.set("issyspreset",'1'); row.set("isSynRole",'0')
         │     ├── serviceHelper.save(query)        ← 第一次 save (导出前态)
         │     │     → UPDATE t_hrcs_permrelatentry SET fissyspreset='1', fissynrole='0'
         │     ├── for id:
         │     │     ├── execScript("T_HRCS_PERMRELAT", fields, "fid="+id)
         │     │     ├── execScript("T_HRCS_PERMRELAT_L", fields, "fid="+id+" and flocaleid='zh_CN'")
         │     │     └── execScript("T_HRCS_PERMRELATENTRY", fields, "fid="+id)
         │     ├── exportFile(sqlList join "\n")
         │     │     └── CacheFactory.tempFileCache.saveAsUrl
         │     │           → view.openUrl (浏览器下载)
         │     ├── for row: row.set("isSynRole", oldValueMap[row.id])  ← 还原
         │     └── serviceHelper.save(query)        ← 第二次 save (恢复)
         │   catch Exception
         │     └── tx.markRollback() → throw new KDBizException
```

⚠ **平台 SDK 用法**：
- `kd.bos.db.tx.TX.requiresNew()` · 起独立事务（与外部事务隔离）
- `kd.bos.sqlscript.PreInsDataScriptBuilder.genInsertSQLScript` · 平台脚本生成（DBRoute.of("hr")）
- `kd.bos.cache.CacheFactory.tempFileCache.saveAsUrl` · 临时文件缓存（5000 秒 TTL）

---

## 六、auth 跳转的数据流（无落库）

```
 User: select row → click auth
   ↓
 PermRelateList.beforeDoOperation(auth)
   └── ListShowParameter
         ├── setBillFormId("hrcs_permrelatcfg")
         ├── setFormId("bos_list")
         ├── setPageId("hrcs_permrelatcfg@" + parentPageId)
         └── ShowType.MainNewTabPage
       → view.showForm
   ↓ 不调 HRDataBaseOp.execute（auth 是 donothing 类型）
   ↓
 用户进入 hrcs_permrelatcfg 列表（独立场景）
```

**结论**：auth 不读不写本场景物理表 · 仅 UI 跳转。

---

## 七、PageCache 数据流（FormPlugin 关键）

| 阶段 | PageCache 写入 | PageCache 读取 |
|---|---|---|
| afterBindData | allBuInfo / entityPerm / appEntityPerm / forBidAppStr / forBidAppEntity / permIdNumberMap / ids / originEntryInfo | — |
| beforeF7Select | — | forBidAppStr / forBidAppEntity / entityPerm / appEntityPerm |
| propertyChanged(entitytype 改) | oldEntity (取消时回滚用) | — |
| confirmCallBack(Cancel) | — | oldEntity |
| beforeDoOperation(save) | — | allBuInfo (BU 名展示) |
| afterDoOperation(save) | — | ids / originEntryInfo |
| afterSaveProcessing | — | originEntryInfo / permIdNumberMap |
| beforeDoOperation(delete · List) | deleteRows | — |
| afterDoOperation(delete · List) | — | deleteRows |

---

## 八、事务边界一览

| 事务 | 范围 | 边界 | 失败回滚 |
|---|---|---|---|
| 主事务（save / delete） | t_hrcs_permrelat + _l + entry | save/delete 操作 OP 13 阶段 | ✅ 平台自动 |
| 下游同步事务 | t_hrcs_permrelatcfg add/delete | afterSaveProcessing / afterDoOperation(delete) | ❌ 独立调 · 失败不影响主事务 · 但产生不一致 |
| 角色同步子页面事务 | t_perm_role_perm | hrcs_syncroleperm/syncrolesel 子页面 | ✅ 子页面自管 |
| 全量同步任务事务 | t_perm_role_perm 批量 | HRRelatePermTask.execute | 任务自管（CanStop=false 防中断） |
| exportscript 临时事务 | t_hrcs_permrelatentry 临时改 issyspreset/isSynRole | TX.requiresNew | tx.markRollback() on Exception |

---

## 九、不发 BEC · 不写 ext 表

### 9.1 BEC 实证（grep）

```bash
$ grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_permrelat/

(0 results across 3 files)
```

**结论**：本场景标品**不发** BEC 事件。

### 9.2 不写历史表

`scene_doc.json` 全字段 grep `iscurrentversion / boid / sourcevid / firstbsed / hisversion / datastatus` · 全部 0 命中。

**结论**：本场景**不写** `t_hrcs_permrelat_his` 等历史版本表（这种表压根不存在）。

### 9.3 ISV 扩展提示

如果 ISV 需要：
- **发 BEC** · 走 PR-011 · 自建 OP 在 `afterExecuteOperationTransaction` 调 `IEventService.triggerEventSubscribeJobs` · 06_customization_solutions.md CS-05
- **加版本字段** · 不要硬塞 boid/iscurrentversion · 应该建独立的 ISV 扩展表 · CS-01 提示

---

## 十、关键 API 调用图

```
PermRelateEdit (FormPlugin)
  ├── EntityCtrlServiceHelper.queryExistedForBidInfo(view)
  ├── EntityCtrlServiceHelper.queryEntityForBidInfo(?, forBidApps, forBidAppEntity)
  ├── EntityCtrlServiceHelper.buildFilterForF7(false)
  ├── EntityCtrlServiceHelper.filterNoPermEntity(view)
  ├── EntityCtrlServiceHelper.getEntityRelatedApps(entity)
  ├── EntityCtrlServiceHelper.getAppComboForPerm(entity, forBidAppEntity, forBidApps)
  ├── EntityCtrlServiceHelper.queryEntityPermItems(entity)
  ├── EntityCtrlServiceHelper.queryEntityPermItemIdNum(entity)
  ├── HRBuCaServiceHelper.getBuCaFuncFromSpec(entityId, appId)
  ├── PermRelateServiceHelper.queryPermItems()
  ├── PermRelateServiceHelper.getEntryInfo(collection, permIdNumberMap)
  ├── PermRelateServiceHelper.deletePermRelateConfigs(deleteRows)
  ├── PermRelateServiceHelper.addPermRelateConfigs(addRows)
  ├── PermRelateServiceHelper.getPermInfo()
  └── PermRtSyncService.calcRtPermRole(relateList, mainList)

PermRelateList (FormPlugin)
  ├── PermRelateServiceHelper.queryPermRelates(filter)
  ├── PermRelateServiceHelper.deletePermRelateConfigs(deleteRows)
  ├── PermRtSyncService.getRelatePermInfoPair(permRelates)
  ├── PermRtSyncService.calcRtPermRole(relateList, mainList)
  └── PermRtSyncService.writeOpLog(...)

HRAdminStrictPlugin (FormPlugin)
  ├── PermissionServiceHelper.isAdminUser(uid)
  ├── PermCommonUtil.isCosmicUser(uid)
  └── HRAdminService.isHrAdmin()
```

→ 详细 SDK 用法见 `curated_sdk.json`。
