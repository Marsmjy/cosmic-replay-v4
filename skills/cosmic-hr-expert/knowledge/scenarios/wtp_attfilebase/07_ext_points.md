# 扩展点全图 · wtp_attfilebase

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `wtp_attfilebase` 标品接入 **44** 个插件。

### FormPlugin（13）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `HisModelFormCommonPlugin` ← `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`
- `WTCPresetEdit` ← `kd.wtc.wtbs.formplugin.web.WTCPresetEdit`
- `AttFileBaseCoreDataEdit` ← `kd.wtc.wtp.formplugin.web.attfile.AttFileBaseCoreDataEdit`
- `HisModelListCommonPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
- `AttFileImportPlugin` ← `kd.wtc.wtp.formplugin.web.attfile.AttFileImportPlugin`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`
- `HisModelOPCommonPlugin` ← `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- ...（共 13，详见 `_auto_plugin_registry.md`）

### ListPlugin（15）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `HisModelF7ListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
- `HisModelFilterPanelListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- `HisModelFilterPanelF7ListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- `WTCPresetDeleteForbidList` ← `kd.wtc.wtbs.formplugin.web.model.WTCPresetDeleteForbidList`
- `WTCHisNewList` ← `kd.wtc.wtbs.formplugin.web.history.WTCHisNewList`
- `AttFileBaseList` ← `kd.wtc.wtp.formplugin.web.attfile.AttFileBaseList`
- `WTCBaseCommonList` ← `kd.wtc.wtbs.formplugin.web.WTCBaseCommonList`
- `AttFileBasef7List` ← `kd.wtc.wtp.formplugin.web.attfile.AttFileBasef7List`
- ...（共 15，详见 `_auto_plugin_registry.md`）

### OpPlugin（13）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `AttFileBaseSaveOp` ← `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- `HisUniqueValidateOp` ← `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `AttFileBaseModifyOp` ← `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- `AttFileBaseDiscardOp` ← `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- `AttFileResignRollBackOp` ← `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- `AttFileTransferOp` ← `kd.wtc.wtp.opplugin.web.transfer.AttFileTransferOp`
- ...（共 13，详见 `_auto_plugin_registry.md`）

### 其他（3）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `HisBaseDataF7FastFilter` ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
- `AttFileBaseF7FastFilter` ← `kd.wtc.wtp.formplugin.web.attfile.AttFileBaseF7FastFilter`

## ✅ verified · ISV 可继承的 SDK 入口（curated_sdk 9 桶）

### FormPlugin · 表单生命周期（3 个）
- `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`

### OpPlugin · 操作生命周期（6 个）
- `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`

## ✅ verified · 反编译实抓真处理类（16 个 · ISV 可继承 9 / 禁继承 7）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（9 个 · 详细画像）

#### `HRBaseDataTplEdit` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- **完整继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **重写生命周期方法（6）**:
  - `afterBindData` — ISV 子类可在此切入扩展逻辑
  - `afterLoadData` — ISV 子类可在此切入扩展逻辑
  - `beforeDoOperation` — ISV 子类可在此切入扩展逻辑
  - `afterDoOperation` — ISV 子类可在此切入扩展逻辑
  - `preOpenForm` — ISV 子类可在此切入扩展逻辑
  - `beforeClosed` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 7 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit;
  
  public class IsvCustomHRBaseDataTplEdit extends HRBaseDataTplEdit {
      @Override
      public void afterBindData(... args) {
          super.afterBindData(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md)

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

#### `HisModelFormCommonPlugin` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
- **完整继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **重写生命周期方法（7）**:
  - `beforeBindData` — ISV 子类可在此切入扩展逻辑
  - `afterCreateNewData` — ISV 子类可在此切入扩展逻辑
  - `afterLoadData` — ISV 子类可在此切入扩展逻辑
  - `afterBindData` — ISV 子类可在此切入扩展逻辑
  - `beforeDoOperation` — ISV 子类可在此切入扩展逻辑
  - `afterDoOperation` — ISV 子类可在此切入扩展逻辑
  - `closedCallBack` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 13 · 调核心 Service 2 · trace 访问 15 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin;
  
  public class IsvCustomHisModelFormCommonPlugin extends HisModelFormCommonPlugin {
      @Override
      public void beforeBindData(... args) {
          super.beforeBindData(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md)

#### `HRBaseDataStatusOp` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- **完整继承链**: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- **重写生命周期方法（3）**:
  - `onPreparePropertys` — ISV 子类可在此切入扩展逻辑
  - `onAddValidators` — ISV 子类可在此切入扩展逻辑
  - `beforeExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 3 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp;
  
  public class IsvCustomHRBaseDataStatusOp extends HRBaseDataStatusOp {
      @Override
      public void onPreparePropertys(... args) {
          super.onPreparePropertys(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md)

#### `HRBaseDataLogOp` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- **完整继承链**: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- **重写生命周期方法（3）**:
  - `beforeExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
  - `beginOperationTransaction` — ISV 子类可在此切入扩展逻辑
  - `afterExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 3 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp;
  
  public class IsvCustomHRBaseDataLogOp extends HRBaseDataLogOp {
      @Override
      public void beforeExecuteOperationTransaction(... args) {
          super.beforeExecuteOperationTransaction(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md)

#### `HRBaseDataEnableOp` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- **完整继承链**: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- **重写生命周期方法（1）**:
  - `beforeExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 4 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp;
  
  public class IsvCustomHRBaseDataEnableOp extends HRBaseDataEnableOp {
      @Override
      public void beforeExecuteOperationTransaction(... args) {
          super.beforeExecuteOperationTransaction(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md)

#### `HRBaseOriginalOp` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- **完整继承链**: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- **重写生命周期方法（2）**:
  - `onPreparePropertys` — ISV 子类可在此切入扩展逻辑
  - `beforeExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp;
  
  public class IsvCustomHRBaseOriginalOp extends HRBaseOriginalOp {
      @Override
      public void onPreparePropertys(... args) {
          super.onPreparePropertys(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md)

#### `HisModelOPCommonPlugin` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- **完整继承链**: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- **重写生命周期方法（5）**:
  - `onPreparePropertys` — ISV 子类可在此切入扩展逻辑
  - `onAddValidators` — ISV 子类可在此切入扩展逻辑
  - `beforeExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
  - `beginOperationTransaction` — ISV 子类可在此切入扩展逻辑
  - `afterExecuteOperationTransaction` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 15 · 调核心 Service 2 · trace 访问 17 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin;
  
  public class IsvCustomHisModelOPCommonPlugin extends HisModelOPCommonPlugin {
      @Override
      public void onPreparePropertys(... args) {
          super.onPreparePropertys(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md)

#### `HisUniqueValidateOp` · OP_PLUGIN @ `OPERATION` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- **完整继承链**: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- **重写生命周期方法（2）**:
  - `onPreparePropertys` — ISV 子类可在此切入扩展逻辑
  - `onAddValidators` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp;
  
  public class IsvCustomHisUniqueValidateOp extends HisUniqueValidateOp {
      @Override
      public void onPreparePropertys(... args) {
          super.onPreparePropertys(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md)

### ❌ 禁继承类（7 个 · @SdkInternal 内部类 · 详细说明）

以下类是**苍穹平台内部实现**·绝对不能继承（继承后标品升级会破插件）：

#### `HRBaseDataImportEdit` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HRBaseDataTplList` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HRBasedataLogList` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HisModelFilterPanelListPlugin` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HisModelFilterPanelF7ListPlugin` · OP_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HisModelBatchImportPlugin` · OTHER · opKey=`multisheetimport`

- **FQN**: `kd.hr.hbp.formplugin.web.history.impt.HisModelBatchImportPlugin`
- **继承链**: `kd.bos.form.plugin.impt.BatchImportPlugin`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HisBaseDataF7FastFilter` · OTHER · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
- **继承链**: `kd.bos.base.AbstractBasedataController`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## ✅ verified · opKey 扩展切入点详解（69 个有执行链的 opKey）

ISV 想在某个 opKey 加扩展逻辑·按下表选切入位置：

### opKey `importdata_hr` · 导入数据

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_import_record_hr` · 查看导入记录

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_list_hr` · 按列表导出

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_impttpl_hr` · 按导入模板导出

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_expttpl_hr` · 按导出模板导出

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_export_record_hr` · 查看导出记录

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `new` · 新增

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `modify` · 修改

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `view` · 查看

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `close` · 关闭

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `returndata` · 返回数据

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `copy` · 复制

**执行链**（12 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **2**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **3**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **4**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **5**: `HisModelOPCommonPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
- order **6**: `AttFileBaseSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseSaveOp`
- order **7**: `HisUniqueValidateOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **8**: `AttFileBaseModifyOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseModifyOp`
- order **9**: `AttFileBaseDiscardOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileBaseDiscardOp`
- order **10**: `AttFileResignRollBackOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtp.opplugin.web.attfile.AttFileResignRollBackOp`
- ...（共 12 · 详见 `rules_chain_all.json`）

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
