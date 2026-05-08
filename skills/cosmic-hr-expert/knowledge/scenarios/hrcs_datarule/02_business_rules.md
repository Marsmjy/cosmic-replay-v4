# 业务规则 · 数据规则 (hrcs_datarule)

> **状态**：基于 3 类反编译 + 42 opKey 富化 + form_lifecycle_rules.json metadataRules 整合
> **confidence**：real_deploy（所有规则 V_/B_/FP_/MV_ id 都可在 rules_chain_all.json + form_lifecycle_rules.json 找到 sourceLine）

## 一、规则索引（按触发点分类）

| 触发点 | 规则数 | 文件 |
|---|---|---|
| FormPlugin#preOpenForm | 3 | form_lifecycle_rules.json#FP_HR1-3 |
| FormPlugin#registerListener | 1 | form_lifecycle_rules.json#FP_RL1 |
| FormPlugin#beforeF7Select | 1 | form_lifecycle_rules.json#FP_BF1 |
| FormPlugin#afterLoadData | 2 | form_lifecycle_rules.json#FP_AL1-2 |
| FormPlugin#afterCreateNewData | 1 | form_lifecycle_rules.json#FP_AC1 |
| FormPlugin#afterBindData | 1 | form_lifecycle_rules.json#FP_AB1 |
| FormPlugin#propertyChanged | 1 | form_lifecycle_rules.json#FP_PC1 |
| FormPlugin#beforeDoOperation | 5 | form_lifecycle_rules.json#FP_BD1-5 |
| OP onAddValidators · save | 4 | rules_chain_all.json#opKeys.save.validators |
| OP beforeExecute · save | 7 | rules_chain_all.json#opKeys.save.businessRules |
| OP onAddValidators · 其他 7 核心 opKey | 21 | rules_chain_all.json#opKeys.<op>.validators |
| OP businessRules · 其他 7 核心 opKey | 28 | rules_chain_all.json#opKeys.<op>.businessRules |
| 元数据校验 | 4 | form_lifecycle_rules.json#metadataRules.validations |

## 二、HR 领域准入规则（HRAdminStrictPlugin · 11 表单共用）

打开 hrcs_datarule 表单前 · 必须通过 HR 领域准入校验。

```java
// HRAdminStrictPlugin.java#showMesIfUserIsNotAdmin · L41-L52
boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);
boolean isCosmic = PermCommonUtil.isCosmicUser(userId);
if (!isAdmin & !isCosmic) {
    e.setCancel(true);
    e.setCancelMessage("您无法访问该功能 · 因为您不是 HR 领域管理员。");
    return;
}
if (!HRAdminService.isHrAdmin()) {
    e.setCancel(true);
    e.setCancelMessage("您无法访问该功能 · 因为您不是 HR 领域管理员。");
}
```

| 规则 ID | 触发 | 条件 | 阻断消息 |
|---|---|---|---|
| FP_HR1 | preOpenForm · ListShowParameter.isLookUp() | lookUp 列表态 | 直接放行 |
| FP_HR2 | preOpenForm | 非 isAdmin AND 非 isCosmic | 您无法访问该功能 · 因为您不是 HR 领域管理员。 |
| FP_HR3 | preOpenForm | 非 isHrAdmin | 您无法访问该功能 · 因为您不是 HR 领域管理员。 |

⚠️ **lookUp 列表（F7 弹窗选基础资料）放行** · 否则普通用户做 F7 会被拦。

⚠️ **HR 域三道闸**：
1. `PermissionServiceHelper.isAdminUser` · 平台 Admin 判断
2. `PermCommonUtil.isCosmicUser` · Cosmic 用户判断（平台超管）
3. `HRAdminService.isHrAdmin` · HR 域管理员判断（hrcs 业务侧定义）

任一通过都不能完全开放：必须 (1 OR 2) AND 3。

## 三、entitynum 字段规则

### 3.1 创建后只读（`FP_AL1`）

```java
// HRDataRuleEditPlugin.java#afterLoadData L88-L89
this.getView().setEnable(Boolean.FALSE, new String[]{"entitynum"});
```

数据规则一旦创建（无论 status 是什么），entitynum **永久不能改**。

业务原因：rule 字段里序列化的 FilterCondition 引用的字段属于 entitynum 业务对象 · 切换业务对象后 · 老规则全部失效。

### 3.2 新建态可预填 + 锁定（`FP_AC1`）

```java
// HRDataRuleEditPlugin.java#afterCreateNewData L99-L106
String prevFocusEntNum = (String)this.getView().getFormShowParameter()
        .getCustomParam("fsp_custom_param_entitynum");
if (HRStringUtils.isNotEmpty(prevFocusEntNum)) {
    this.getModel().setValue("entitynum", prevFocusEntNum);
    this.getView().setEnable(Boolean.FALSE, new String[]{"entitynum"});
} else {
    this.getView().setEnable(Boolean.TRUE, new String[]{"entitynum"});
}
```

如果父页面通过 `customParam fsp_custom_param_entitynum` 传业务对象号 · 直接预填 + 锁定。否则用户可自由选。

### 3.3 切换会清空 rule（`FP_PC1`）

```java
// HRDataRuleEditPlugin.java#propertyChanged L114-L121
if (HRStringUtils.equals("entitynum", propName)) {
    this.clearDataPermFilterGrid();  // 清 FilterGrid + rule 字段
    this.refreshFilterGrid();         // 重建 FilterGrid 列
}
```

切换 entitynum → `clearDataPermFilterGrid` 清掉 FilterGrid 状态 + 把 rule 字段 setValue(null) → `refreshFilterGrid` 重新加载新业务对象的字段集。

### 3.4 F7 弹窗时序基础资料过滤（`FP_BF1`）

```java
// HRDataRuleEditPlugin.java#beforeF7Select L77-L85
String entityNumber = evt.getRefEntityId();
if (HisModelServiceHelper.isInheritHisModelTemplate(entityNumber)) {
    evt.addCustomQFilter(new QFilter("iscurrentversion", "=", "1"));
}
```

如果业务对象是 HisModel 时序基础资料（如 `haos_adminorg` / `hbjm_job`）· F7 弹窗只展示 iscurrentversion=1 当前版本（PR-008）。

⚠️ ISV 自建 BasedataField 字段做 F7 时 · 也应该用 `HisModelServiceHelper.isInheritHisModelTemplate` 做同样的判断（这是 PR-008 在 F7 场景的体现）。

## 四、rule 字段规则

### 4.1 save 前 · FilterCondition 必须非空（`FP_BD2` / `V_SAVE_3` / `MV_V1`）

```java
// HRDataRuleEditPlugin.java#doSave L138-L153
boolean canSave = false;
if (fc != null && !CollectionUtils.isEmpty(fc.getFilterRow()) && HRStringUtils.isNotEmpty(ruleStr)) {
    // ... 处理
    canSave = true;
}
if (!canSave) {
    args.setCancel(true);
    args.setCancelMessage("请配置规则。");
    this.getView().showErrorNotification(...);
}
```

**业务原因**：空规则 = "授权所有数据" · 在权限场景下极其危险 · 直接阻断在 FormPlugin 阶段。

### 4.2 save 前 · FilterBuilder 字段合法性（`FP_BD3` / `V_SAVE_4`）

```java
// HRDataRuleEditPlugin.java#doSave L142-L145
MainEntityType dataEntityType = EntityMetadataCache.getDataEntityType(entityNum);
FilterBuilder filterBuilder = new FilterBuilder(dataEntityType, fc);
filterBuilder.buildFilter();
```

**业务原因**：FilterBuilder 内部会校验：
- FilterCondition 引用的字段必须在 entitynum 业务对象 schema 内
- 字段类型跟比较符兼容（如 String 字段不能用 `>` 比较）

不合法时 buildFilter 抛异常 · 走 KDBizException 回归用户。

### 4.3 save · FilterCondition → JSON 字符串（`FP_BD4` / `B_SAVE_2`）

```java
// HRDataRuleEditPlugin.java#doSave L138-L146
String ruleStr = SerializationUtils.toJsonString(fc);
this.getModel().setValue("rule", ruleStr);
```

**业务原因**：FilterGrid 控件状态是临时 UI 状态 · 不直接绑定字段 · 必须在 save 时显式序列化进 rule 字段 · 才能持久化。

⚠️ **submit / submitandnew / saveandnew 操作不走 doSave**（实证 `if (operateKey == "save")`）· 因此提交前必须先保存。

## 五、save OP 规则（HRDataRuleSaveOp · 7 条业务规则 · 4 条校验）

### 5.1 V_SAVE_1 · HRDataRuleSaveValidator 业务校验

```java
// HRDataRuleSaveOp.java#onAddValidators L48
args.addValidator(new HRDataRuleSaveValidator());
```

⚠️ 详细校验逻辑待 `HRDataRuleSaveValidator` 反编译（hrmp-hrcs-opplugin 包内 · 当前未在 _decompiled 路径中）。

### 5.2 B_SAVE_3 · 抓 rule 字段变更

```java
// HRDataRuleSaveOp.java#beforeExecuteOperationTransaction L51-L65
for (each dataEntity) {
    long dataRuleId = item.getLong("id");
    if (0L == dataRuleId) continue;  // 新建跳过

    Tuple<Boolean, String> compareResult = compareFilterCondition(dataRuleId, item.getString("rule"));
    boolean changeFlag = (Boolean)compareResult.item1;
    String beforeRuleStr = (String)compareResult.item2;

    if (!changeFlag) continue;

    // 在 OperationOption 暂存
    this.getOption().setVariableValue("operate_" + dataRuleId, "1");
    this.getOption().setVariableValue("originalRule_" + dataRuleId, beforeRuleStr);
    DataRuleLogModel before = DataRuleLogServiceHelper.getDataRuleLogModel(dataRuleId, true);
    this.getOption().setVariableValue("beforeData_" + dataRuleId, SerializationUtils.toJsonString(before));
}
```

`compareFilterCondition` 用 `DataRuleLogServiceHelper.compareFilterControls(beforeEntityNumber, beforeRule, ruleStr)` 做语义比较 · 不只是字符串相等。

### 5.3 B_SAVE_5/6/7 · afterExecute 清缓存 + 通知 + 写日志

详见 `04_business_flow.md#二、save 操作`。

⚠️ **关键**：只有 isChange=true 的规则才走 afterExecute 的"清缓存 + 通知 + 写日志"分支 · 没变更的不会触发。

## 六、status 状态机规则（标品 HRBaseDataStatusOp）

### 6.1 audit 状态转换

| 当前 status | 操作 | 允许 | 错误消息（如不允许） |
|---|---|---|---|
| draft (A) | audit | ✅ | – |
| saved (B) | audit | ✅ | – |
| audit (C) | audit | ❌ | 已审核单据不能再次审核 |
| submitted (D) | audit | ❌（推断）| 已提交单据请先撤销 |

### 6.2 unaudit 状态转换

| 当前 status | 操作 | 允许 | 错误消息 |
|---|---|---|---|
| audit (C) | unaudit | ✅ | – |
| 其他 | unaudit | ❌ | 非已审核单据不能反审核 |

### 6.3 submit / unsubmit

| 当前 status | submit | unsubmit |
|---|---|---|
| draft (A) | → D | – |
| saved (B) | → D | – |
| submitted (D) | – | → A |

### 6.4 issyspreset 限制（PR-007）

```
issyspreset = true →
    禁用 audit / unaudit / submit / unsubmit / disable / delete
    (具体由 HRBaseDataStatusOp 标品逻辑限定)
```

## 七、enable 状态机规则（标品 HRBaseDataEnableOp）

| 当前 enable | 操作 | 允许 |
|---|---|---|
| 1 | disable | ✅ → 0 + 写 disabler/disabledate |
| 0 | disable | ❌ 已禁用单据不能再次禁用 |
| 0 | enable | ✅ → 1 + 清 disabler/disabledate |
| 1 | enable | ❌ 已启用单据不能再次启用 |

## 八、删除规则（标品 + ISV 建议）

### 8.1 标品规则

```
DELETE 允许条件 = (status != audit) AND (status != enable) AND (issyspreset != true)
```

### 8.2 ISV 建议补充（CS-04）

```
ISV DeleteValidator:
1. 查 hrcs_dynascheme.permfilter 是否引用本规则 ID
2. 查 hrcs_role 是否引用本规则
3. 任一引用 → 阻断 + 列出引用方
```

## 九、save 跟 submit 的协作规则

### 9.1 submit 不会重新序列化 rule

```java
// HRDataRuleEditPlugin.java#beforeDoOperation L126
if (HRStringUtils.equals("save", operateKey)) {
    this.doSave(args);
}
```

**只有 operateKey = "save"** 才进 doSave · submit 不走 · 因此 submit 时 FilterGrid 的当前状态**不会**写进 rule 字段。

### 9.2 推荐 UX 流程

```
用户配规则 → 点保存 → save 成功 → 点提交 → submit 成功
            ↑                       ↑
            序列化 rule              不序列化 · 用上次 save 的 rule
```

⚠️ ISV 想"提交时也序列化最新规则" · 在 CS-02 中给 FormPlugin 加 `else if (operateKey == "submit") doSave(args)` 分支。

## 十、规则的可观测性

### 10.1 标品日志（HRBaseDataLogOp · 所有 OP 都有）

记录字段：操作类型、操作人、时间。

### 10.2 数据规则日志（DataRuleLog · 仅 save modify）

实证：`HRDataRuleSaveOp.afterExecute` 调 `DataRuleLogServiceHelper.dataRuleLogInit("modify", model)` · 记录 rule 字段变更前后 + 业务对象。

### 10.3 缺口

audit / unaudit / disable / enable / delete / submit / unsubmit · 都没写 DataRuleLog · 这是合规风险 · 建议 ISV CS-03 补齐。

## 十一、跨模块约束（hrcs_datarule 跟权限链的契约）

### 11.1 hrcs_datarule 是"配置"· 权限链是"消费方"

```
hrcs_datarule (配置规则)
        ↓
        ↓ (PermNotifyService.notifyByDataRule + clearCache)
        ↓
hrcs 权限链运行时（kd.hr.hrcs.bussiness.service.perm.*）
        ↓
        ↓ 反序列化 rule → FilterBuilder.buildFilter → SQL where
        ↓
某个 form 列表/表单查询时拼上规则 SQL
```

⚠️ ISV 想在 hrcs_datarule 之外另搞一套权限规则 · 必须接入这个 SPI · 否则规则不被消费。

### 11.2 hrcs_dynascheme 引用 hrcs_datarule 的规则

详见 `11_upstream_downstream_logic.md` · 删除规则前必查方案引用。

## 十二、PR 引用速查（本场景规则相关）

| PR | 适用规则 |
|---|---|
| PR-001 | 全场景 · 不继承 HRDataRuleEditPlugin / HRDataRuleSaveOp / HRDataRuleSaveValidator / HRAdminStrictPlugin |
| PR-003 | FP_BD4 · FormPlugin 用 setValue · 不能在 OP 用 |
| PR-004 | CS-02 · propertyChanged 联动用 beginInit/endInit |
| PR-005 | CS-06 · 数据规则参数项行 ID 用 kd.bos.id.ID |
| PR-007 | issyspreset 限制（10+ 规则共用）|
| PR-008 | FP_BF1 · F7 时序基础资料 iscurrentversion=1 |
| PR-009 | 消费 HisModel 时下游用 boid |
| PR-010 | save OP 7 条 BR 全部 · onAddValidators / beforeExecute / afterExecute 标准流程 |
| PR-011 | CS-05 · BEC 走平台事件中心 · 标品没发 |

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin -->

## chgaction 实证补充（HRDataRuleEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.TemplateBillEdit -->

## chgaction 实证补充（TemplateBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.TemplateBillEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.TemplateBillEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.TemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit -->

## chgaction 实证补充（HRBaseDataCommonEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonList -->

## chgaction 实证补充（HRBaseDataCommonList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

## chgaction 实证补充（HRBaseDataCommonMobList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp -->

## chgaction 实证补充（HRDataRuleSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->
