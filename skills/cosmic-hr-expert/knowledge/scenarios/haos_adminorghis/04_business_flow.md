# 业务流转 · 组织历史查询（haos_adminorghis）

> **状态**：基于 7 反编译类 + 36 opKey 实证 · 完整查询/补录调用链
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. 整体流程拓扑

```
┌────────────────────────────────────────────────────────────────────┐
│                      业务用户进入路径                                 │
├────────────────────────────────────────────────────────────────────┤
│  路径 A：菜单"行政组织维护 → 行政组织维护 → 组织历史查询"             │
│  路径 B：从 admin_org_quick_maintenance 列表点行 → "查看历史"         │
│  路径 C：F7 选历史版本（其他场景作为引用源）                          │
└────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                  ┌──────────────────────┐
                  │ haos_adminorghis 列表 │ ← HisModelListCommonPlugin（标品）
                  │   (HisPageEnum =     │   setFilter 强制注入
                  │   VERSION_LIST_PAGE) │   iscurrentversion=false
                  └──────────────────────┘
                       │           │
              ┌────────┘           └──────────┐
              ▼                                ▼
       [选行 → view]                   [选 ≥2 行 → versionchangecompare]
              │                                │
              ▼                                ▼
      ┌──────────────────┐              ┌─────────────────────────┐
      │ adminorghis 详情 │              │ 历史版本对比页（标品 list）│
      │ (VIEW 模式 readonly│              │  按 id 集合显示字段差异 │
      │ AdminOrgDetailEdit│              │ HisModelListShowFormProc│
      │ Plugin.beforeBind │              │ essor.show... L237      │
      │ Data L186-L191)   │              └─────────────────────────┘
      └──────────────────┘
              │
   ┌──────────┼──────────┬──────────┐
   ▼          ▼          ▼          ▼
 [revise] [reviserecord] [hisversioninfo] [关闭]
   │          │              │
   ▼          ▼              ▼
 派生新版    显示修订记录    显示版本列表
 (afterDo:   (showSlideBill (showHisVersionListPage)
  showHisVer  · hbp_revise
  sionEditPa  logpage)
  ge L201-L
  205)
```

---

## 2. 列表加载完整调用链

反编译实证：**列表加载阶段** 多个 hook 按顺序触发：

```
1. preOpenForm(args)
   ↓ AdminOrgHisListPlugin.preOpenForm（行政组织历史列表 · 23 号插件）
   ↓ 设 customParam (rootPageId / 数据时间)

2. filterContainerInit(args)
   ↓ HisModelListCommonPlugin.filterContainerInit L89-L96
     · 检查 listProcessor.getHisPageEnum() != NOT_HIS_PAGE
     · 不是 F7 模式
     · 不跳过 → listProcessor.initFilterContainer(args)
     · 用作历史模式专属的过滤面板初始化

3. beforeCreateListColumns(args)
   ↓ HisModelListCommonPlugin.beforeCreateListColumns L107-L139
     · ONLY_ONE_EFFECT_VERSION 模式 → 移除 bsed/bsled/firstbsed 三列
     · NOT_HIS_PAGE 模式 → 移除 hisversion/changedescription/datastatus 等
     · VERSION_LIST_PAGE 模式（本场景）：
       a) NO_INTERRUPTION_NO_OVERLAP 时 bsed DESC + modifytime DESC
       b) 否则 hisversion DESC
       c) 移除 status/firstbsed/issyspreset
       d) 在 hisoperation 列前插入 modifier.name + modifytime（自定义操作人列）

   ↓ OrgDetailList.beforeCreateListColumns L69-L93
     · 看 structproject.id 是否 = ADMINORG_STRUCT 决定列可见性
     · isincludevirtualorg 决定虚拟组织列展示
     ⚠ 注意：本场景模型不一定真挂 OrgDetailList（OrgDetailList 主要给 admin_org_quick）

4. setFilter(event)
   ↓ HisModelListCommonPlugin.setFilter L167-L181
     · NOT_HIS_PAGE 时 → iscurrentversion=true
     · 历史模式 → iscurrentversion=false（**本场景**） + 移除 ctrlstrategy
   ↓ OrgDetailList.filterContainerBeforeF7Select L182-L188（仅 F7 时）

5. beforeBindData(event)
   ↓ HisModelListCommonPlugin.beforeBindData L98-L105
     · 不是 F7 时 → listProcessor.handleBtnVisible() · 隐藏不该有的按钮

6. packageData(event)
   ↓ HisModelListCommonPlugin.packageData L142-L165
     · 处理 hisoperation 列的行级按钮可见性（按 datastatus）
       - TEMP → modify/hiscopy/confirmchange
       - DISCARDED → 仅 hiscopy（不可 revise）
       - 其他 → hiscopy/revise

7. beforeShowBill(e)
   ↓ HisModelListCommonPlugin.beforeShowBill L183-L191
     · 强制 customParam("hisPage") = VERSION_PAGE
     · 注册 closePageCloseCallBack
```

> ⚠ **关键认知**：本场景 list 加载 **8 个 lifecycle 触发点都被标品 final 类绑死** · ISV 通过并列挂插件**无法改 setFilter 的 iscurrentversion=false**（PR-002 RowKey · 标品最后一个 add 兜底）· 但可以**追加自己的 QFilter**（如租户内可见性收窄）。

---

## 3. F7 历史版本选择调用链（外部场景作为消费方）

反编译实证：`HisModelF7ListPlugin` 211 行核心逻辑。

```
1. beforeBindData(e) [L189-L210]
   · isF7() && isUseHisF7Tpl()
   · 不是 BoF7 · 不是 ONLY_ONE_EFFECT_VERSION 模式
   · 是首次打开 F7 页
   · 拿 HisModelF7PageParam · 看 effDateFieldType 类型
   · 类型 = effDate · effectDate 没传 → setValue("effectdate", today)
   · 类型 = effDateRange · 起止都没传 → 起=today, 止=getMaxEffEndDate()

2. beforeCreateListColumns(args) [L78-L85]
   · 创建 versionF7Column（版本列）
   · 调 setFirstOpenF7Page() 标记首次打开

3. setFilter(evt) [L87-L119]
   · handelControlVisible() 控件可见性
   · isHisFieldF7() 直接 return（特殊模式）
   · 非 BoF7：buF7Processor.changeHisListBdQFilter（标准 BU 闸）
   · showCurrentNumAndName=true 时：
     a) 临时构造 (iscurrentversion=true) 子查询拿 boid 集
     b) 把 boid in (...) OR 拼到原过滤
     c) 让"当前编号/名称"也能匹配历史版本
   · ONLY_ONE_EFFECT_VERSION：直接强制 iscurrentversion=true
   · 否则：BoF7 → iscurrentversion=true · 非 BoF7 → handleSetVersionF7QFilters
   · handleEnableAndStatusQFilters 加 enable/status 收窄

4. beforePackageData(event) [L121-L132]
   · showCurrentNumAndName=true → queryCurrentNumberAndName(event)
     缓存当前版本数据 → currentDataMap

5. packageData(e) [L134-L145]
   · setCurrentNumberAndCurrentName(e, currentDataMap)
     把当前版本的 number/name 拼到本行展示

6. propertyChanged(args) [L147-L187]
   · 监听 effectdate / effdatestart / effdateend / f7effdatestart / f7effdateend 5 个字段
   · 任一变 → 更新 changeDateMap
   · listView.refresh() + listView.clearSelection()
```

---

## 4. 详情页（VIEW 模式 · 标准查询路径）

反编译实证：`AdminOrgDetailEditPlugin` + `NewAdminorgDetailEditPlugin` + `HisModelFormCommonPlugin` 三层并行注册：

```
1. preOpenForm(args)
   ↓ AdminOrgDetailEditPlugin.preOpenForm L147-L154
     · 看 customParam.adminorg_operation
     · != null → setCaption(showParameter.getCustomParam("caption"))
   ↓ AdminOrgPageRightDynamicPlugin.preOpenForm
     · 设 hbss_entitytype_id = 2030（关联信息分类 ID）

2. beforeBindData(e)
   ↓ HisModelFormCommonPlugin.beforeBindData L104-L108
     · LOG 记录 entityNumber + hisPage
     · copyProcessor.setBoIdForNewHisVersionPage()
   ↓ AdminOrgDetailEditPlugin.beforeBindData L181-L246
     · setValue("effectdate", model.getValue("bsed"))
     · operation == null → VIEW 模式 · setVisible(false) 全部保存按钮
     · 否则按 infochg/addnew/parentchg 分支
   ↓ AdminOrgPageRightDynamicPlugin.beforeBindData L42-L57
     · 拿 customPKFilter · put boid
     · setQueryTime() 解析 searchdate
     · 拼 caption "关联信息-{nameLocal}"
     · cacheFormShowParameter()

3. afterCreateNewData(event)
   ↓ HisModelFormCommonPlugin.afterCreateNewData L110-L114
     · copyProcessor.copyData() · 从原版本复制字段（revise/hiscopy 用）

4. afterLoadData(e)
   ↓ HisModelFormCommonPlugin.afterLoadData L116-L121
     · formProcessor.setHisPageAfterLoadData()
     · formProcessor.handlePageStatusForEdit()
   ↓ AdminOrgDetailEditPlugin.afterLoadData L297-L299
     · super 调（无业务）

5. afterBindData(e)
   ↓ HisModelFormCommonPlugin.afterBindData L123-L135
     · formProcessor.handleBtnOrFieldVisible()
     · formProcessor.handlePageStatusForEdit()
     · formProcessor.handlePageStatusForNew()
     · codeRuleProcessor.setNumberField()
     · commonProcessor.setEnable()
     · 缓存 attachmentPanelLogInfo
   ↓ NewAdminorgDetailEditPlugin.afterBindData L98-L130
     · isFromFuture / operation != null → 隐藏 topinfo/rightflexpanelap 等
     · OperationStatus.VIEW → 隐藏 orginfo/change_info
     · setEnable(false, "attachmeflex")
     · 拼 fullname (走 OrgFullNameServiceWrapper)
     · showStructEntry / showTips / showCooprel
   ↓ AdminOrgDetailEditPlugin.afterBindData L257-L295
     · parentorg edit · setMustInput(true/false) 按业务规则
     · 拿 cooprel 数据填 entry
     · enable=0 时隐藏 info_chg/parent_chg
     · parentorg 为空 → setVisible(FALSE, "parent_chg")

6. registerListener(event)
   ↓ AdminOrgDetailEditPlugin.registerListener L301-L322
     · 注册 9 个 BeforeF7SelectListener
     · addItemClickListeners(["change_save"])
```

---

## 5. revise（修订）触发新版本派生流程

反编译实证：`HisModelFormCommonPlugin.afterDoOperation L188-L266`。

```
触发：用户在历史版本详情页点 revise 按钮
   │
   ▼
beforeDoOperation case "save"
   │ → 检查 hisPage = REVISE_VERSION_PAGE
   │ → 算 maxEffEndDate · 检查 bsled 没早于它
   │ → 不通过：showErrorNotification("失效日期不能早于...")
   │ → 通过：设 hisModelOPParam.reviseSave=true
   ▼
op execute（OP 链 · 走 HisModelOPCommonPlugin + 标品 OP）
   │ → 实际写入 t_haos_adminorg
   │ → 派生新版本（hisversion+1 · sourcevid=老版本.id）
   │ → 老版本的 bsled 自动收缩到新版本 bsed-1
   ▼
afterDoOperation case "revise" L198-L209
   │ → operationResult.isSuccess()
   │ → if (id == boid)：当前版本 → showHisVersionEditPage(sourcevid, true)
   │ → else：历史版本 → showHisVersionEditPage(id, false)
   │ → 跳到新派生版本的编辑页
```

---

## 6. his_save 历史补录（写入唯一入口）调用链

反编译实证：`AdminOrgInitSaveOp` 是 `his_save` 唯一执行链插件。

```
1. onAddValidators
   注册 10 个 OrgHis*Validator（详见 02_business_rules.md §5）
   - OrgHisBelongCompanyValidator
   - OrgHisEffDateContinuityValidator
   - OrgHisEffDateLegitimacyValidator
   - OrgHisEndDateRangeValidator
   - OrgHisEndDateValidator
   - OrgHisFirstEffDateConsistencyValidator
   - OrgHisMigratedValidator
   - OrgHisOrgCurrVerParentValidator
   - OrgHisOrgErrValidator
   - OrgHisOrgParentValidator

2. beforeExecuteOperationTransaction L70-L71
   预处理（业务重写）

3. beginOperationTransaction L73-L74
   主业务写入

4. endOperationTransaction L76-L84
   readFields=[bsed]
   收尾
```

**整个流程不发 BEC** · 反编译产物 grep 0 处 `triggerEventSubscribe`。

**ISV 想拦截**：在该 OP 链上**并列挂新 OP**（继承 `HRDataBaseOp` · 不继承 `AdminOrgInitSaveOp`）· 在 `onAddValidators` 加自己的 Validator（PR-001 / PR-010）。详见 CS-04。

---

## 7. 列表 → 详情 → revise → 列表 自动闭环

反编译实证：`HisModelListCommonPlugin.closedCallBack L243-L254` + `HisModelFormCommonPlugin.afterDoOperation case "save" L240-L249`。

```
列表 listView
   │
   ▼ [双击行 → beforeShowBill]
   │ HisModelListCommonPlugin.beforeShowBill L189
   │   设 customParam.hisPage = VERSION_PAGE
   │   注册 closePageCloseCallBack
   ▼
详情 view（VIEW 模式）
   │
   ▼ [revise]
   │ HisModelFormCommonPlugin.afterDoOperation case "revise"
   ▼
新版本编辑页 view
   │
   ▼ [save]
   │ HisModelFormCommonPlugin.afterDoOperation case "save" L240-L249
   │   if hisPage == CHANGE_PAGE: getView().invokeOperation("refresh")
   │   commonProcessor.resetHisPageForHisOp()
   ▼
回到列表 closedCallBack
   │ HisModelListCommonPlugin.closedCallBack
   │   actionId == "closeForCopyHisVersion" / "closeForNewHisVersion"
   │     → getView().invokeOperation("refresh")
   ▼
列表数据自动刷新（看到新版本）
```

---

## 8. 历史补录的 双触发路径

| 触发 | 入口 | 调用链 | 业务场景 |
|---|---|---|---|
| **路径 A：UI 点 his_save 按钮** | haos_adminorghis 列表 → "历史补录" | his_save opKey → AdminOrgInitSaveOp | 业务运维人员手动补录某个组织丢失的历史 |
| **路径 B：HIES 批量导入** | chargepersonimpo_hr opKey | importdata_hr → ImportPlugin → 调内部 his_save 服务 | 数据迁移 / 大批量补录 |

**路径 A** 单条：触发 his_save → 跳详情页 → 用户填字段 → save 按钮 → AdminOrgInitSaveOp 执行链。

**路径 B** 批量：用户走 HIES 向导 → 上传 Excel → 标品分批读 → 内部按 boid 调 `AdminOrgInitSaveOp.executeOperate(...)` 模拟 OP 链。

---

## 9. namehistoryview（名称历史查询）特殊路径

opType = `donothing` · 但平台 `BaseDataNameVersionListPlugin`（11/12 号插件）接管：
- 解析 `t_haos_adminorg_l.fname` 的多语言 name 历史变更轨迹
- 显示一个独立的"改名链"对话框 · 与 hisversion 维度独立
- 改名链的字段维度仅 name 字段 · 不显示 hisversion 全字段

---

## 10. 详情页 attachmentPanelLogInfo 缓存机制

反编译实证：`HisModelFormCommonPlugin.afterBindData L131-L134`：
```
if (model.getDataEntity().getDataEntityState().getFromDatabase()) {
    List attachmentPanelLogInfo = LogHandlerUtil.getAttachmentLogInfo(...);
    pageCache.put("attachmentPanelLogInfo", SerializationUtils.toJsonString(attachmentPanelLogInfo));
}
```

**业务意图**：详情页加载时拿当前实体的附件操作日志 · 缓存到 pageCache · 后续走 save 操作时通过 op.getOption().setVariableValue("attachmentPanelLogInfo", ...) 传到 OP（L143） → 落到 OP 的 LogHandler。

**ISV 关心**：附件操作日志缓存机制是标品自动跑的 · CS 扩展不要清这个 cache key（清了会让附件审计日志缺失）。

---

## 11. 流程总结：关键 lifecycle 触发点速查

| 流程 | 关键插件 | 关键 lifecycle |
|---|---|---|
| 列表加载 | HisModelListCommonPlugin | filterContainerInit / setFilter / beforeCreateListColumns / packageData |
| F7 选历史 | HisModelF7ListPlugin | setFilter / beforePackageData / packageData / propertyChanged |
| 详情页 VIEW | AdminOrgDetailEditPlugin + NewAdminorgDetailEditPlugin + HisModelFormCommonPlugin | preOpenForm → beforeBindData → afterCreateNewData → afterLoadData → afterBindData → registerListener |
| revise 派生 | HisModelFormCommonPlugin | beforeDoOperation case "save" / afterDoOperation case "revise" |
| his_save 补录 | AdminOrgInitSaveOp | onAddValidators → beforeExecute → begin → end |
| versionchangecompare 对比 | HisModelListCommonPlugin | afterDoOperation case "versionchangecompare" |
| 关闭回调刷新 | HisModelListCommonPlugin | closedCallBack |

---

## 12. 关联文档

- `05_data_flow.md` · 完整 QFilter 构造路径与事务边界
- `02_business_rules.md` · §5 his_save 10 Validator 规则
- `06_customization_solutions.md` · CS-04 拦截 his_save · CS-06 跨场景跳转
- `knowledge/scenarios/admin_org_quick_maintenance/04_business_flow.md` · 当前版本流程对照
