# 模型设计 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 `scene_doc.json` (17 字段实抓) + `_auto_inherit_chain.md` + `_auto_plugin_semantics.md` (3 反编译类)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_permrelat.md` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit/PermRelateList` + `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` (2026-04-28)

---

## ⭐ 关键业务事实 · 一张主表 + 1 张分录子表 + 多语言表 + 下游 hrcs_permrelatcfg 同步

`hrcs_permrelat` 是 HR 通用服务（hrcs）域里**关联权限项**的配置入口。它用来定义一个"主权限项"（如 hbjm_jobhr 的 view 权限）和若干"关联权限项"（如关联 hrpi_pernontsprop 的 view 权限）的捆绑关系，使得权限分配时一并生效。

它**不是** HisModel 时序基础资料（grep `iscurrentversion|HisModel|boid` 反编译三类无任何命中），是常规 BillFormModel + 1 个分录子表的**双层结构**。

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_hrcs_permrelat` | 主实体 | 关联权限项主体（含主业务对象/主权限项/应用） | `fid` / `fentitytypeid` / `fpermitemid` / `fappid` / `fdescription` / `finitdatasource` |
| `t_hrcs_permrelat_l` | 多语言表 | 主表多语言字段承载（description 多语言文本） | `fpkid` / `fid` / `flocaleid` / `fdescription` |
| `t_hrcs_permrelatentry` | 分录子表 | 权限项关联分录（一个主项 → N 个关联项） | `fid` / `fentryid` / `fseq` / `fentitytypeid` / `fappid` / `fpermitemid` / `fissyspreset` / `fissynrole` |

⚠️ **下游联动表**：
- `hrcs_permrelatcfg` · 关联权限项细粒度配置表 · 关联权限项主表 save/delete 后会通过 `PermRelateServiceHelper.deletePermRelateConfigs / addPermRelateConfigs` 同步增删（**FormPlugin 业务规则 FP_ADO4 实证**）
- `perm_role_perm` · 角色-权限项关联表 · `btnsycrole` 操作通过 `PermRtSyncService.calcRtPermRole` 计算变更后下发 `HRRelatePermTask` 调度任务同步

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"基础资料 + 单据"形态混合（BillFormModel 但无审批工作流）。`hrcs_permrelat` 列表页由**两层独立元数据**组成，Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BillFormModel | `hrcs_permrelat`（主实体 · 菜单直挂） | 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据） | `hrcs_permrelat`（同名 · 因为单一菜单） | 列表 UI 壳 + UI 层动作按钮（auth / btnsycrole / exportscript） |
| **F7 列表模板** | 同主元数据 | `hrcs_permrelat`（lookUp 模式 · HRAdminStrictPlugin 直接放行） | F7 选择时复用主列表 · 但跳过 HR 域管理员校验 |

**插件挂载职责分工**：
- **数据层校验 / 权限 / setFilter** → 挂 `hrcs_permrelat`（数据实体 · 当前 `PermRelateList` 就挂这里）
- **预入口准入闸（HR 域管理员）** → 挂动态表单模板的 preOpenForm（`HRAdminStrictPlugin`）
- **细粒度授权列表 hrcs_permrelatcfg** → 由 `auth` opKey 跳转打开 · 是独立场景

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`） + `tablist`/`treelist` 三层模型。

---

## 二、继承链 · BillFormModel + 分录子表（非 HisModel）

### ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "BillFormModel"`。这与 hrcs_dynascheme/admin_org 的差异是：
- ❌ **不带 HisModel 时序模板**：grep `iscurrentversion / HisModel / boid` 反编译三类（PermRelateEdit/PermRelateList/HRAdminStrictPlugin）+ scene_doc.json 全部为 0 命中
- ❌ **不带审批工作流**：opKey 列表里**没有** submit/audit/unaudit/confirmchange 等典型工作流操作
- ✅ **典型 BillFormModel + entry 子表**：单据形态（save/delete/copy/saveandnew）+ 1 个 entryentity 子表

### 字段层级分类

苍穹元数据字段分 4 层（与 admin_org/dynascheme 一致 · scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | bos_basetpl + HR 父模板 | `description`（多语言）/ `initdatasource`（数据来源）/ `issyspreset`（系统预置） | 🔒 / ⚠️ 多数不改 |
| **L2** 时序模型 | hbp_histimeseqtpl 父模板 | （**本场景无** · 非 HisModel） | — |
| **L3** 业务字段 | hrcs_permrelat 自身 | `entitytype` / `bizapp` / `appcombo` / `mainpermitem` / `entryentity.entitytypeid` / `entryentity.app` / `entryentity.permitemid` / `entryentity.permitem` / `entryentity.issynrole` | ⚠️ 谨慎改（涉及业务规则） |

### 关键认知

- **本场景没有 HisModel 概念**：没有 `boid / id / iscurrentversion / sourcevid` 这套，所以查询时**不需要带 `iscurrentversion=true` 过滤**（PR-008 不适用）
- **唯一性约束在分录子表**：同一主对象+主应用+主权限项下，分录里同一业务对象+应用+权限项不能重复（PermRelateEdit.beforeF7Select FP_BF7_2 实证）
- **预置数据保护**：`issyspreset=true` 的分录行不允许删除/修改 `entitytypeid` / `app` / `permitem` 三字段（PR-007 实证）
- **数据来源 `initdatasource`**：标记数据是"系统预置"还是"用户自建" · 系统级维护字段 · ISV 不要 setValue

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 17 个字段）

### 3.1 主表系统字段（L0 · 平台维护 · 红区）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `creator` | CreaterField | 创建人 | ❌ | 🔒 | 自动写入 · `t_hrcs_permrelat.fcreatorid` → `bos_user` |
| `createtime` | CreateDateField | 创建时间 | ❌ | 🔒 | 自动写入 · `t_hrcs_permrelat.fcreatetime` |
| `modifier` | ModifierField | 修改人 | ❌ | 🔒 | 自动写入 · `t_hrcs_permrelat.fmodifierid` → `bos_user` |
| `modifytime` | ModifyDateField | 修改时间 | ❌ | 🔒 | 自动写入 · `t_hrcs_permrelat.fmodifytime` |

### 3.2 主表业务字段（L1/L3）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `description` | MuliLangTextField | 描述（多语言） | ❌ | ✅ | 主表 `_l` 表承载（`t_hrcs_permrelat_l.fdescription`） |
| `initdatasource` | ComboField | 数据来源（系统预置/用户自建） | ❌ | 🔒 红区 | 系统/平台维护 · 手改破坏数据一致性 |
| **`entitytype`** | **BasedataField** | **主业务对象** ⭐ | **✅** | 🔒（isvCanModify=false） | → `bos_entityobject` · F7 走 `EntityCtrlServiceHelper.buildFilterForF7(false) + filterNoPermEntity` |
| `bizapp` | BasedataField | 应用（持有真实 ID） | ❌ | ✅ | → `hbp_devportal_bizapp` · 用户填的是 `appcombo` ComboField · propertyChanged 时自动同步到 `bizapp` |
| **`appcombo`** | **ComboField** | **应用** ⭐ UI 可见 | **✅** | 🔒 | 由 `setAppComboList` 动态生成下拉项 · 选完通过 propertyChanged 写到 `bizapp` |
| **`mainpermitem`** | **ComboField** | **主权限项** ⭐ | **✅** | 🔒 | 由 `setMainPermItems` 动态生成下拉项 · 来源是 `EntityCtrlServiceHelper.queryEntityPermItemIdNum(entity)` 减去已用过的 |
| `entryentity` | EntryEntity | 权限项关联分录 | ❌ | ✅ | → `t_hrcs_permrelatentry` |

### 3.3 分录子表字段（entryentity）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `entryentity.entitytypeid` | BasedataField | 业务对象编码 | ✅ | 🔒 | → `bos_entityobject` · F7 排除已选项（QFilter `number not in ids`） |
| `entryentity.app` | BasedataField | 应用 | ✅ | 🔒 | → `bos_devportal_bizapp` · F7 必须先选 entitytypeid · 走 `getEntityRelatedApps` 过滤 |
| `entryentity.permitemid` | TextField | 权限项 ID（逗号分隔多值） | ❌ | ✅ | 由 `hrcs_choose_permitem` 子页面回填 · 格式 "id1,id2,id3" |
| `entryentity.permitem` | TextField | 权限项（显示文本 · 逗号分隔多值） | ✅ | 🔒 | 由 `hrcs_choose_permitem` 子页面回填 · 与 `permitemid` 同步 · 用户点击触发弹窗 |
| `entryentity.issyspreset` | CheckBoxField | 系统预置 | ❌ | 🔒 红区 | 系统/平台维护 · `afterBindData` 实证 issyspreset=true 时三字段被锁 |
| `entryentity.issynrole` | CheckBoxField | 是否同步角色 | ❌ | ✅ | 业务字段 · 影响 `btnsycrole` 是否把此分录纳入计算 |

---

## 四、字段联动关系图（FormPlugin 实证）

```
entitytype (主) ─改─→ 触发 confirmCallBack(mainEntityChangeConfirm)
              └─Yes─→ afterEntityChange()
                        ├─ setValue(mainpermitem, "")          // 清主权限项
                        ├─ deleteEntryData("entryentity")      // 清空分录
                        ├─ setAppComboList(entity)             // 重建 appcombo 下拉
                        └─ setMainPermItems(entity, app, true) // 重建 mainpermitem 下拉
              └─Cancel─→ beginInit/endInit + setValue(entitytype, oldValue)  // 回滚（PR-004）
              └─无确认条件─→ afterEntityChange()                // 直接走流程

appcombo ─改─→ propertyChanged
            ├─ setValue(bizapp, appId)                         // 同步真实 ID
            └─ setMainPermItems(entity, app, true)              // 重算主权限项

entryentity.entitytypeid ─改─→ propertyChanged
            ├─ setValue(app, null)                              // 清应用
            ├─ getEntityRelatedApps(newValue) → entityRelatedApps
            ├─ removeForBidApp(...)                             // 排除被禁
            └─ if size==1: setValue(app, single) + setEnable(false)  // 唯一应用自动带出 + 锁

entryentity.app ─改─→ propertyChanged
            └─ if BU(app+entity) != mainBu:
                 showTipNotification("职能类型不一致")
                 setValue(entitytypeid, "")                     // 强制重选

entryentity.permitem ─清空─→ propertyChanged
            └─ setValue(permitemid, "")                         // 联动清 ID

entryentity.permitem ─点击─→ click(TextEdit)
            └─ showForm("hrcs_choose_permitem", Modal)
                  └─ closedCallBack(returnData)
                        ├─ 解析 pk="permId||permName"
                        └─ setValue(permitemid, ids) + setValue(permitem, names)
```

---

## 五、PageCache 缓存键清单（FormPlugin 实证）

| 缓存键 | 内容 | 何时灌入 | 何时使用 | 实证 |
|---|---|---|---|---|
| `allBuInfo` | `Map<String,String>` BU id→name | afterBindData · `putAllBuInfoToCache` | beforeDoOperation/propertyChanged 显示职能类型名 | L716-L723 |
| `entityPerm` | `Map<entityId, List<permId>>` | afterBindData · `EntityCtrlServiceHelper.queryExistedForBidInfo` | filterMainPermItem / showForm 排除被禁权限项 | L165 + L582 |
| `appEntityPerm` | `Map<appId, Map<entityId, List<permId>>>` | 同上 | 同上 | L165 + L585 |
| `forBidAppStr` | `Set<String>` 不允许授权的 appId | 首次调用 `getForBidApp()` | propertyChanged / beforeF7Select 排除 | L702-L713 |
| `forBidAppEntity` | `Map<appId, List<entityId>>` | 首次调用 `getForBidAppEntity()` | removeForBidApp | L689-L700 |
| `permIdNumberMap` | `Map<permId, permNumber>` | 首次调用 `getEntryInfo()` | save 后构造 originEntryInfo / newEntryInfo | L726-L737 |
| `ids` | `List<Long>` 旧分录 id 列表 | afterBindData 末尾 | afterDoOperation save 后算新增数 | L196-L198 |
| `originEntryInfo` | `Set<String>` 旧分录唯一标识集合 | afterBindData 末尾 | afterSaveProcessing 算 deleteRows/addRows | L199-L200 + L532-L540 |
| `oldEntity` | `String` 切换前的 entitytype.id | propertyChanged(entitytype) 弹确认前 | confirmCallBack 用户取消时回滚 | L298 + L376 |
| `deleteRows` | `Set<...>` 待删的 hrcs_permrelatcfg 配置 | List.beforeDoOperation(delete) | List.afterDoOperation(delete) 同步删 | L146-L149 + L211-L216 |

---

## 六、子页面/子操作链路（不在 opKey 注册表里的关键 UI 跳转）

| 子页面 formId | 用途 | 触发点 | 回调 actionId | 数据契约 |
|---|---|---|---|---|
| `hrcs_choose_permitem` | 权限项多选 | `entryentity.permitem` Click | `hrcs_choose_permitem` | pk = `permId||permName` 双竖杠分隔 · returnData 是 ListSelectedRowCollection |
| `hrcs_permrelatcfg` | 细粒度授权列表 | `auth` opKey | （MainNewTabPage · 不回调） | pageId = `hrcs_permrelatcfg@<parentPageId>` |
| `hrcs_syncroleperm` | 同步角色预览 | `btnsycrole` 选 1-10 行 | （Modal · 不回调） | customParam: roleInfo / roleCount / permCount |
| `hrcs_syncrolesel` | 同步角色选择 | List.closedCallBack(incPermTips · syncRole=1) | （Modal · 不回调） | customParam: roleInfo (resultRolePermMap JSON) |
| `bos_listf7` | 通用 F7 列表壳 | beforeF7Select · setFormId | （F7 弹窗回 BasedataEdit） | QFilter |

---

## 七、调度任务（btnsycrole 全量路径）

| 任务类 | 触发 | 参数 | 类型 | 超时 | 可终止 |
|---|---|---|---|---|---|
| `kd.hr.hrcs.bussiness.service.perm.dimension.HRRelatePermTask` | List.startJob() | `params={syncAll: 1}` · `appId=hrcs` · 任务名（多语言）"同步角色关联权限项" | `JobType.REALTIME` | 1200 秒 | `false`（不可终止 · 防脏） |

实证：`PermRelateList.startJob()` L243-L262。该任务在用户对全量记录点【同步角色】时下发 · 后台批处理刷 `perm_role_perm`。

---

## 八、平台命名规则速查（PR 红区 + 反模式）

> ⚠ **本节是质量门禁强制项**

### 8.1 多语言表 _l 结尾（实证）

- `t_hrcs_permrelat_l` · 主表 `description` MuliLangTextField 承载
- 字段名前缀仍是 `f` · 表名加 `_l` 后缀
- ISV 加 MuliLangTextField 时 · 平台**自动**建 `<table>_l` 表 · ISV **不要**手建
- exportscript 操作必须连带导出 `_l` 表（`PermRelateList.generateSql` L319-L325 实证）

### 8.2 反模式 · 继承场景专属类（PermRelateEdit / PermRelateList 不要继承）

- ❌ `extends PermRelateEdit` · `extends PermRelateList` · `extends PermRelateOp(若有)`
- ❌ `super.beforeDoOperation(evt)` · `super.afterDoOperation(args)` 调用链耦合标品
- ✅ 走"并列挂插件"模式（PR-001）· ISV 父类只能是 `HRDataBaseEdit` / `HRDataBaseList` / `HRDataBaseOp` / `AbstractValidator`
- ✅ ISV 扩展元数据 · 把自己插件排在标品插件之前（PR-002）

### 8.3 列表三层模型（实证）

- 数据实体（BillFormModel） · 列表表单模板（动态表单） · F7 列表模板（动态表单 · 走 lookUp 模式）
- 三层独立 · ISV 挂插件时**必须区分挂哪层**：
  - 数据校验/权限 → 数据实体 · `PermRelateList` 就挂这里
  - 准入闸/UI 壳 → 列表表单模板 · `HRAdminStrictPlugin.preOpenForm`
- F7 模式 `HRAdminStrictPlugin` 直接放行（FP_HAS1 实证 L33-L36）· 让普通业务用户也能选关联权限项作为基础资料
- 参考管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）

### 8.4 其他命名（提示）

- 主表物理表 = `t_<formId>` · `t_hrcs_permrelat`
- 分录物理表 = `t_<formId><entryName>` · `t_hrcs_permrelatentry`
- 字段物理列名 = `f` + key.lowercase() · 不要手填 fieldName 前缀（kb_cosmic_buildmeta_traps.md 实证）

### 8.5 反模式 · 跨 BU 关联（业务规则）

- ❌ 主业务对象的职能类型（BU）= 人事 · 分录里关联薪酬业务对象
- ✅ 同 BU 才能关联 · `PermRelateEdit.beforeDoOperation` save 阶段强制校验
- ✅ propertyChanged 阶段已实时拦截（FP_PC5 · L336-L353） · save 是兜底（FP_BDO2 · L214-L225）

---

## 九、与其他 hrcs 场景对比

| 维度 | hrcs_permrelat | hrcs_dynascheme | hrcs_permrelatcfg(下游) |
|---|---|---|---|
| ModelType | BillFormModel + 1 entry | BillFormModel + 6 entry + 4 隐式 | BillFormModel |
| HisModel | ❌ 否 | ✅ 是 | ❌ 否 |
| 工作流 | ❌ 无（只 save/delete/copy） | ✅ submit/audit/confirmchange | ❌ 无 |
| 主插件类 | PermRelateEdit/List | DynaAuthSchemePlugin/List | （另查） |
| 准入闸 | HRAdminStrictPlugin | HRAdminStrictPlugin | HRAdminStrictPlugin |
| BEC 发布 | ❌ 0 处（grep 实证） | ❌ 0 处 | （未查） |
| 子页面交互 | hrcs_choose_permitem 多选 | addrole + setadminrange 子表单 | (无) |
| 调度任务 | HRRelatePermTask | （无） | （无） |

---

## 十、引用 PR 速查

| PR | 章节 | 引用条数 |
|---|---|---|
| PR-001 ISV 并列挂插件 | §8.2 反模式 | 1 |
| PR-002 RowKey 顺序 | §8.2 | 1 |
| PR-003 FormPlugin setValue | §一关键事实 + §四联动 | 1 |
| PR-004 setValue 死循环 | §四联动 confirmCallBack 回滚 | 1 |
| PR-005 ID 生成 | §一关键事实（自建分录行 id 提示） | 1 |
| PR-007 预置数据保护 | §3.3 issyspreset + §二关键认知 | 2 |
| PR-008 时序 iscurrentversion | §二关键认知（不适用 · 显式说明） | 1 |
| PR-010 OP 13 生命周期 | §06 CS 引用 | (跨章) |
| PR-011 BEC | §九对比表 + §06 CS-05 | (跨章) |

→ 详细规则见 `_shared/platform_rules.json`。
