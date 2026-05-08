# 业务规则 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 `_auto_rules.md` (formRule 0 条) + `_auto_plugin_semantics.md` (3 反编译类) + `form_lifecycle_rules.json` + `rules_chain_all.json` 31 opKey
> **confidence**: verified
> **数据源**: PermRelateEdit/PermRelateList/HRAdminStrictPlugin (2026-04-28)

---

## 一、规则来源全景

```
hrcs_permrelat 业务规则
    ├── 元数据 formRule (0 条)                          ← _auto_rules.md
    ├── Java FormPlugin 规则 (~28 条 · 11 lifecycleMethods)  ← form_lifecycle_rules.json
    │   ├── PermRelateEdit (10 lifecycleMethods · 21 rules)
    │   ├── PermRelateList (7 lifecycleMethods · 13 rules)
    │   └── HRAdminStrictPlugin (1 lifecycleMethod · 3 rules)
    ├── opKey OP 链规则 (31 opKey · 7 关键 opKey 富化)        ← rules_chain_all.json
    │   ├── save (3 V + 6 B)
    │   ├── delete (3 V + 5 B)
    │   ├── auth (1 V + 1 B)
    │   ├── btnsycrole (1 V + 4 B)
    │   ├── exportscript (1 V + 2 B)
    │   ├── newentry (0 V + 1 B)
    │   └── deleteentry (0 V + 1 B)
    └── 平台规则 PR (引用 _shared/platform_rules.json 11 条)
```

---

## 二、核心业务规则（按"业务关键性"排）

### 2.1 BU 一致性规则（最关键）

**规则**：分录里的"业务对象 + 应用"组合的 BU（业务单元/职能类型），必须等于主"业务对象 + 应用"组合的 BU。

**实证位置**：
- 实时拦截：`PermRelateEdit.propertyChanged` L336-L353（FP_PC5）
- save 兜底：`PermRelateEdit.beforeDoOperation` L214-L225（FP_BDO2）

**计算方法**：`HRBuCaServiceHelper.getBuCaFuncFromSpec(entityId, appId)` 查 `hbss_hrbucafunc` 表得到 BU id

**业务含义**：HR BU = 招聘 / 人事 / 薪酬 / 绩效 等职能类型 · 不允许跨 BU 关联权限（业务上"招聘业务对象"和"薪酬业务对象"不应该共用一个权限项）

**违反时**：showTipNotification + setValue(entitytypeid, "") 强制重选

**ISV 提示**：
- ❌ 不要把这条规则改成警告（PR-001 反对修改业务规则）
- ✅ 可以基于此扩展：增加 BU 别名/合并规则 · 走 ISV OP 加 Validator

### 2.2 预置数据保护规则（PR-007）

**规则**：分录行 `issyspreset = true` 时：
- 不可删除（deleteentry 阻止）
- entitytypeid / app / permitem 三字段锁定不可改

**实证位置**：
- afterBindData 锁字段：FP_ABD4 · L191-L193
- deleteentry 阻止：FP_BDO3 · L228-L234

**违反时**：showErrorNotification("预置数据无法删除。")

**ISV 提示**：
- ✅ ISV 可改 `issynrole` 业务字段（不在锁定范围）
- ❌ ISV 不要把 `issyspreset` 改成 false 来绕过保护（破坏一致性）

### 2.3 主业务对象切换确认规则

**规则**：当主 entitytype 改变时，如果已有主权限项或分录数据，必须弹确认对话框，用户取消时必须回滚。

**实证位置**：
- propertyChanged 触发确认：FP_PC1 · L294-L302
- confirmCallBack Yes 走清空：FP_CFB1 · L368-L382
- Cancel 回滚必须 beginInit/endInit：FP_CFB2（PR-004 实证）

**ISV 提示**：
- ✅ 这是 PR-004 的样本：`getModel().beginInit(); setValue; endInit(); updateView` 防死循环
- ✅ ISV 加联动逻辑必须沿用这个模式

### 2.4 分录唯一性规则

**规则**：同一关联权限项主记录下，分录子表的"业务对象"不能重复。

**实证位置**：
- F7 阶段排除：FP_BF7_2 · `QFilter("number", "not in", ids)` L250-L257

**违反时**：F7 阶段就不显示已选项（无法选到重复值）

**ISV 提示**：
- ✅ 唯一性约束在 F7 阶段实现 · 不在 OP Validator
- ⚠ ISV 加新分录字段时不要破坏这个去重逻辑（不要直接 setValue 绕过 F7）

### 2.5 应用受限规则（不允许授权应用）

**规则**：标记为"不允许授权"的应用不能出现在 appcombo / 分录 app 字段中。

**实证位置**：
- 主 appcombo：FP_ABD2 · `setAppComboList` L666-L686
- 分录 app F7：FP_BF7_3-5 · `removeForBidApp` L283-L290 + L259-L277

**数据源**：`forBidAppStr` + `forBidAppEntity` PageCache（来自 `EntityCtrlServiceHelper.queryEntityForBidInfo`）

**违反时**：
- 主对象关联应用全被禁 → 锁 appcombo + showErrorNotification "主业务对象的应用已设置为不允许授权·不允许设置关联权限·请修改。"
- 分录关联应用全被禁 → 锁该行 app + showErrorNotification "当前业务对象的应用已设置为不允许授权·不允许设置关联权限·请修改。"

### 2.6 权限项排除规则（被禁权限项）

**规则**：标记为"被禁"的权限项不能选到主 mainpermitem 或分录 permitemid。

**实证位置**：`PermRelateEdit.filterMainPermItem` L626-L665（用 entityPerm + appEntityPerm 过滤）

**数据源**：`entityPerm` + `appEntityPerm` PageCache（来自 `EntityCtrlServiceHelper.queryExistedForBidInfo`）

**ISV 提示**：
- ✅ 子页面 `hrcs_choose_permitem` customParam 传 `forbidPermNums` · 子页面会用此排除（实证 L580-L593）

---

## 三、HR 域准入规则（HRAdminStrictPlugin）

### 3.1 双闸校验

| 闸 | 条件 | 失败行为 | 实证 |
|---|---|---|---|
| 第一闸 | `PermissionServiceHelper.isAdminUser(uid) || PermCommonUtil.isCosmicUser(uid)` | setCancel + cancelMessage "您无法访问该功能·因为您不是HR领域管理员。" | FP_HAS2 · L42-L47 |
| 第二闸 | `HRAdminService.isHrAdmin()` | 同上 | FP_HAS3 · L48-L53 |

### 3.2 F7 lookUp 模式直接放行

| 条件 | 行为 | 实证 |
|---|---|---|
| `fsp instanceof ListShowParameter && lsp.isLookUp()` | return（不调 showMesIfUserIsNotAdmin · 跳过双闸） | FP_HAS1 · L33-L36 |

**业务含义**：作为 F7 基础资料被引用时，普通业务用户也能选 · 不强制 HR 管理员身份

**ISV 提示**：
- ⚠ 多个 hrcs 表单（包括本场景）共用 HRAdminStrictPlugin · 不要 ISV 私自改它
- ✅ ISV 想加额外准入条件 · 走自建 PreOpenForm 插件 · 排在 HRAdminStrictPlugin **之后**

---

## 四、增删行规则

| 规则 | opKey | 触发 | 实证 |
|---|---|---|---|
| 增行前必须先选主业务对象 | newentry / addrows | FP_BIC1 · entitytype == null → setCancel | L434-L440 |
| 删行禁删预置 | deleteentry | FP_BDO3 · issyspreset=true → setCancel | L228-L234 |

---

## 五、save 后置业务规则

| 规则 | 实证 | 影响范围 |
|---|---|---|
| 同步增删 hrcs_permrelatcfg | FP_ADO4 · `afterSaveProcessing` | t_hrcs_permrelatcfg |
| 计算受影响角色（calcRtPermRole） | FP_ADO3 · L472-L523 | 弹 hrcs_syncrolesel 子页面 → t_perm_role_perm |
| 通知父表单刷新（changed 标志） | FP_ADO2 · L463-L471 | 列表页自动 refresh |
| 通知父 entityNum/permNum | FP_ADO1 · L446-L460 | 业务联动（如父表单刷权限缓存） |

---

## 六、列表页业务规则

### 6.1 delete 缓存 + 同步删

```java
// PermRelateList.beforeDoOperation
QFilter filter = new QFilter("id", "in", primaryKeyValues);
Set permRelateCfgs = PermRelateServiceHelper.queryPermRelates(filter);
this.getPageCache().put("deleteRows", SerializationUtils.toJsonString(permRelateCfgs));

// PermRelateList.afterDoOperation (delete success)
Set deleteRows = SerializationUtils.fromJsonString(deleteRowsStr, Set.class);
PermRelateServiceHelper.deletePermRelateConfigs(deleteRows);
```

### 6.2 auth 跳转规则

- 跳转目标：`hrcs_permrelatcfg` 列表（独立场景）
- pageId：`hrcs_permrelatcfg@<parentPageId>`（保持父子关联）
- ShowType：`MainNewTabPage`（不阻塞父）

### 6.3 btnsycrole 双路径规则

| 选行数 | 路径 | 行为 |
|---|---|---|
| 0 | 全量 | 红色 ConfirmTypes.Delete + YesNo 确认 → startJob 下发 HRRelatePermTask |
| 1-10 | 实时 | 实时 calcRtPermRole + 弹 hrcs_syncroleperm |
| > 10 | 拒绝 | "不能超出10行·请修改。" |

### 6.4 exportscript 三表脚本规则

- 临时改 issyspreset=1 + isSynRole=0 · save · 生成 SQL · 改回原值 · save
- 三表全导：`T_HRCS_PERMRELAT` + `T_HRCS_PERMRELAT_L`（zh_CN）+ `T_HRCS_PERMRELATENTRY`
- 用 `TX.requiresNew()` 起新事务避免污染外部上下文
- Exception → tx.markRollback()

---

## 七、平台规则引用（PR · `_shared/platform_rules.json`）

| PR ID | 名称 | 本场景应用点 |
|---|---|---|
| PR-001 | ISV 并列挂插件 · 不继承场景类 | 06 CS 全部 · §03 §08.2 反模式 |
| PR-002 | RowKey 执行顺序 | 06 CS-03 (排在标品前) |
| PR-003 | FormPlugin getModel().setValue · OP entity.set | 06 CS-02 (FormPlugin 联动) |
| PR-004 | beginInit/endInit 死循环防护 | §2.3 主对象切换回滚 + 06 CS-02 |
| PR-005 | ID 生成 用 kd.bos.id.ID | 06 CS-06 (扩展分录) |
| PR-006 | CodeRuleOp 业务自助配 | （本场景无 number 字段 · 不适用） |
| PR-007 | 预置数据 issyspreset 保护 | §2.2 + 06 CS-03 |
| PR-008 | 时序 iscurrentversion 过滤 | （本场景非 HisModel · 不适用 · 但 §03 §二关键认知显式说明） |
| PR-009 | boid 业务维度 · id 版本维度 | （本场景非 HisModel · 不适用） |
| PR-010 | OP 13 生命周期 | 06 CS-03 / CS-04 / CS-05 / CS-06 |
| PR-011 | BEC 业务事件中心 | 06 CS-05（自建 · 标品 0 处） |

**PR 引用合计**（场景内文档统计）：
- PR-001：≥ 8 处（CS-01~CS-07 + §08.2）
- PR-007：≥ 6 处
- PR-010：≥ 5 处
- PR-011：≥ 5 处
- 其他：散见各章

→ 详细规则见 `_shared/platform_rules.json`。

---

## 八、不要做的事（反模式速查）

| 反模式 | 错在哪 | 正确做法 |
|---|---|---|
| `extends PermRelateEdit` | PR-001 违反 | extends HRDataBaseEdit · 并列挂 |
| `extends PermRelateList` | PR-001 违反 | extends HRDataBaseList · 并列挂 |
| `extends HisModelOPCommonPlugin` | @SdkInternal · 本场景非 HisModel 也不需要 | 不需要任何 HisModel 类 |
| 继承 AbsOrgBaseOp（禁止） | 非 HR 通用推荐 | extends HRDataBaseOp |
| 在 propertyChanged 里直接 `getModel().setValue` 不包 beginInit | PR-004 违反 · 死循环 | beginInit/setValue/endInit/updateView |
| 在 OP `beforeExecuteOperationTransaction` 用 `getModel()` | PR-003 违反 · model=null NPE | 用 `args.getDataEntities()[i]` |
| 在 `afterExecuteOperationTransaction` 抛 KDBizException 期望回滚 | 事务已提交 · 抛异常无效果 | 用 `addErrorMessage` 或前移到 beforeExecute |
| save 时直接 `setValue("issyspreset", false)` 改预置标志 | PR-007 违反 · 破坏一致性 | 不改 · 让平台 finitdatasource 维护 |
| 删主记录前不查 hrcs_permrelatcfg 引用 | 留下孤儿数据 | 06 CS-04 加 DownstreamReferenceCheckValidator |
| 假设本场景发 BEC | grep 实证 0 处 · 假设错误 | 走 06 CS-05 ISV 自建 |
| 假设本场景有 HisModel | 非 HisModel · `iscurrentversion / boid` 都不存在 | scene_doc.json + grep 实证 |
| 套用 hjm_jobhr 3 层异步发 BEC | 同应用不同 form 行为差异巨大 | 每场景 grep + 反编译实证 |
| 在 hrcs_choose_permitem 子页面用单竖杠 \| 而不是双 \|\| | 标品私有协议 · 单竖杠会被 split 误解析 | 用 `permId\|\|permName` 双竖杠 |
| pageId 不带 `@parentPageId` | 父子关联丢失 · 子页面无法回 | `<formId>@<parentPageId>` |

---

## 九、调用 PR 完整列表（脚本可校验）

```
PR-001  §一总纲 + §08.2 + 06_CS-01,02,03,04,05,06,07
PR-002  06_CS-03 (RowKey 排序)
PR-003  §03 §一关键事实 + 06_CS-02 (FormPlugin)
PR-004  §2.3 + 06_CS-02 (beginInit/endInit)
PR-005  06_CS-06 (ID 生成)
PR-006  ✗ 不适用（无 number 字段）
PR-007  §2.2 + §3.3 + 06_CS-03
PR-008  §03 §二（不适用 · 显式说明）
PR-009  ✗ 不适用（非 HisModel）
PR-010  06_CS-03,04,05,06 (OP lifecycle)
PR-011  06_CS-05 (BEC 自建) + §05 §九.1 (grep 实证)
```

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## chgaction 实证补充（HRBaseUeEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit -->

## chgaction 实证补充（PermRelateEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit`
> 跨类追踪: 10 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_roledimension` | saveOne | HRBaseServiceHelper | kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncServic |
| `perm_roleperm` | saveOne | HRBaseServiceHelper | kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncServic |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList -->

## chgaction 实证补充（PermRelateList 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_roledimension` | saveOne | HRBaseServiceHelper | kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncServic |
| `perm_roleperm` | saveOne | HRBaseServiceHelper | kd.hr.hrcs.bussiness.service.perm.dimension.PermRtSyncServic |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## chgaction 实证补充（HRDataBaseOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRDataBaseOp_0` | 数据量超过限制阈值%1$s，当前记录数：%2$s。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->
