# 扩展点全图 · wtam_busitripbill

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `wtam_busitripbill` 标品接入 **35** 个插件。

### FormPlugin（13）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRTemplateBillEdit` ← `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
- `HRBUCAApplicationEdit` ← `kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit`
- `HRPermCommonEdit` ← `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`
- `WTCBaseBillPlugin` ← `kd.wtc.wtbs.formplugin.web.WTCBaseBillPlugin`
- `WTCApplyTypeOtherEdit` ← `kd.wtc.wtbs.formplugin.web.WTCApplyTypeOtherEdit`
- `AttachmentLimitPlugin` ← `kd.wtc.wtbs.formplugin.web.AttachmentLimitPlugin`
- `WTCAttFileShowDetailPlugin` ← `kd.wtc.wtbs.formplugin.web.WTCAttFileShowDetailPlugin`
- ...（共 13，详见 `_auto_plugin_registry.md`）

### ListPlugin（5）
- `HRTemplateBillList` ← `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
- `HRPermCommonList` ← `kd.hr.hbp.formplugin.web.HRPermCommonList`
- `WTCApplyTypeOtherList` ← `kd.wtc.wtbs.formplugin.web.WTCApplyTypeOtherList`
- `BusiTripBillList` ← `kd.wtc.wtam.formplugin.web.busitripbill.BusiTripBillList`
- `BusiTripOtherList` ← `kd.wtc.wtam.formplugin.web.busitripbill.BusiTripOtherList`

### OpPlugin（17）
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `WTCBillSelfApplyOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- `WTCBillAttFileOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillAttFileOp`
- `WTCBillOtherApplyOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- `WTCBillChangeDeleteOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeDeleteOp`
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `WTCBillVerifyStatusOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillVerifyStatusOp`
- `BusiTripBillOp` ← `kd.wtc.wtam.opplugin.web.busitripbill.BusiTripBillOp`
- `BillTplOp` ← `kd.wtc.wtbs.opplugin.web.bill.BillTplOp`
- `TpArchiveControlCheckOp` ← `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`
- `WTCBillUpdateAttfileOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillUpdateAttfileOp`
- `WTCBillReplenishOriginalOp` ← `kd.wtc.wtbs.opplugin.web.bill.WTCBillReplenishOriginalOp`
- ...（共 17，详见 `_auto_plugin_registry.md`）

## ✅ verified · 反编译实抓真处理类（8 个 · ISV 可继承 4 / 禁继承 4）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（4 个 · 详细画像）

#### `HRPermCommonEdit` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
- **完整继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.HRPermCommonEdit;
  
  public class IsvCustomHRPermCommonEdit extends HRPermCommonEdit {
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md)

#### `HRBaseUeEdit` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- **完整继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **重写生命周期方法（1）**:
  - `preOpenForm` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 4 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.template.HRBaseUeEdit;
  
  public class IsvCustomHRBaseUeEdit extends HRBaseUeEdit {
      @Override
      public void preOpenForm(... args) {
          super.preOpenForm(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md)

#### `HRHiesButtonSwitchPlugin` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- **完整继承链**: `kd.bos.form.plugin.AbstractFormPlugin`
- **重写生命周期方法（1）**:
  - `afterBindData` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 6 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin;
  
  public class IsvCustomHRHiesButtonSwitchPlugin extends HRHiesButtonSwitchPlugin {
      @Override
      public void afterBindData(... args) {
          super.afterBindData(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md)

#### `HRCodeRuleOp` · OP_PLUGIN @ `OPERATION` · opKey=`submiteffect`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
- **完整继承链**: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- **重写生命周期方法（1）**:
  - `onAddValidators` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 2 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.HRCodeRuleOp;
  
  public class IsvCustomHRCodeRuleOp extends HRCodeRuleOp {
      @Override
      public void onAddValidators(... args) {
          super.onAddValidators(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md)

### ❌ 禁继承类（4 个 · @SdkInternal 内部类 · 详细说明）

以下类是**苍穹平台内部实现**·绝对不能继承（继承后标品升级会破插件）：

#### `HRTemplateBillEdit` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HRBaseDataImportEdit` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HRTemplateBillList` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HRPermCommonList` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.HRPermCommonList`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## ✅ verified · opKey 扩展切入点详解（10 个有执行链的 opKey）

ISV 想在某个 opKey 加扩展逻辑·按下表选切入位置：

### opKey `submiteffect` · 提交并生效

**执行链**（10 个 plugin 按 order 顺序跑）：

- order **1**: `HRCodeRuleOp` · 重写 1 个方法：onAddValidators（注册校验器）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
- order **2**: `WTCBillSelfApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- order **3**: `BillTplOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.BillTplOp`
- order **4**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`
- order **5**: `BusiTripBillOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.BusiTripBillOp`
- order **6**: `WTCBillUpdateAttfileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillUpdateAttfileOp`
- order **7**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- order **8**: `WTCBillChangeShowOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeShowOp`
- order **9**: `WTCBillChangeStatusOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeStatusOp`
- order **10**: `WTCBillReplenishOriginalOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillReplenishOriginalOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `submit` · 提交

**执行链**（9 个 plugin 按 order 顺序跑）：

- order **1**: `CodeRuleOp` · 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator
  - 完整 FQN: `kd.bos.business.plugin.CodeRuleOp`
- order **2**: `WTCBillSelfApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- order **3**: `BillTplOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.BillTplOp`
- order **4**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`
- order **5**: `BusiTripBillOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.BusiTripBillOp`
- order **6**: `WTCBillUpdateAttfileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillUpdateAttfileOp`
- order **7**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- order **8**: `WTCBillChangeShowOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeShowOp`
- order **9**: `WTCBillReplenishOriginalOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillReplenishOriginalOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `save` · 保存

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `CodeRuleOp` · 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator
  - 完整 FQN: `kd.bos.business.plugin.CodeRuleOp`
- order **2**: `WTCBillVerifyStatusOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillVerifyStatusOp`
- order **3**: `BusiTripBillOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.BusiTripBillOp`
- order **4**: `BillTplOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.BillTplOp`
- order **5**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`
- order **6**: `WTCBillUpdateAttfileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillUpdateAttfileOp`
- order **7**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- order **8**: `WTCBillReplenishOriginalOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillReplenishOriginalOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）

### opKey `discard` · 废弃

**执行链**（6 个 plugin 按 order 顺序跑）：

- order **1**: `WTCBillSelfApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- order **2**: `WTCBillAttFileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillAttFileOp`
- order **3**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- order **4**: `WTCBillVerifyStatusOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillVerifyStatusOp`
- order **5**: `WTCBillChangeDeleteOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeDeleteOp`
- order **6**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `delete` · 删除

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `CodeRuleDeleteOp` · enabled=True
  - 完整 FQN: `kd.bos.coderule.CodeRuleDeleteOp`
- order **2**: `WTCBillSelfApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- order **3**: `WTCBillAttFileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillAttFileOp`
- order **4**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`
- order **5**: `WTCBillChangeDeleteOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeDeleteOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `unsubmit` · 撤销

**执行链**（3 个 plugin 按 order 顺序跑）：

- order **1**: `WTCBillAttFileOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillAttFileOp`
- order **2**: `WTCBillSelfApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillSelfApplyOp`
- order **3**: `WTCBillOtherApplyOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillOtherApplyOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `audit` · 审批通过

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`
- order **2**: `WTCBillChangeStatusOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeStatusOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `wtcreaudit` · 恢复核算

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `WTCBillChangeStatusOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillChangeStatusOp`
- order **2**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `change` · 变更

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `WTCBillApplyTypeOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtbs.opplugin.web.bill.WTCBillApplyTypeOp`
- order **2**: `TpApplyChangeOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpApplyChangeOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `archivectrlcheck` · 封存管控校验

**执行链**（1 个 plugin 按 order 顺序跑）：

- order **1**: `TpArchiveControlCheckOp` · enabled=True
  - 完整 FQN: `kd.wtc.wtam.opplugin.web.busitripbill.TpArchiveControlCheckOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）
