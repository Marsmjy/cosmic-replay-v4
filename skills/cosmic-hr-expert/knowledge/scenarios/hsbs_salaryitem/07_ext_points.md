# 扩展点全图 · hsbs_salaryitem

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hsbs_salaryitem` 标品接入 **40** 个插件。

### FormPlugin（19）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `BdCtrlStrtgyShowLogicPlugin` ← `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`
- `BaseDataCreateOrgPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataCreateOrgPlugin`
- `BaseDataFormPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataFormPlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `SalaryItemEdit` ← `kd.swc.hsbs.formplugin.web.basedata.salaryitem.SalaryItemEdit`
- `BaseDataNewPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataNewPlugin`
- `BaseDataSavePlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`
- `BaseDataDeletePlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin`
- ...（共 19，详见 `_auto_plugin_registry.md`）

### ListPlugin（9）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `BdCtrlStrtgyShowLogicListPlugin` ← `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicListPlugin`
- `BaseDataListPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `SalaryItemList` ← `kd.swc.hsbs.formplugin.web.basedata.salaryitem.SalaryItemList`
- `HSBSFixedFieldListPlugin` ← `kd.swc.hsbs.formplugin.web.HSBSFixedFieldListPlugin`
- `BaseDataMobListPlugin` ← `kd.bos.form.plugin.bdctrl.BaseDataMobListPlugin`

### OpPlugin（10）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `SalaryItemSaveOp` ← `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `SalaryItemDeleteOp` ← `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- `SalaryItemAuditOp` ← `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- `SalaryItemUnAuditOp` ← `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

### 其他（2）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `BasedataFilterController` ← `kd.bos.form.plugin.bdctrl.BasedataFilterController`

## ✅ verified · ISV 可继承的 SDK 入口（curated_sdk 9 桶）

### FormPlugin · 表单生命周期（2 个）
- `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`

### OpPlugin · 操作生命周期（4 个）
- `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`

## ✅ verified · 反编译实抓真处理类（9 个 · ISV 可继承 6 / 禁继承 3）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（6 个 · 详细画像）

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

### ❌ 禁继承类（3 个 · @SdkInternal 内部类 · 详细说明）

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

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## ✅ verified · opKey 扩展切入点详解（51 个有执行链的 opKey）

ISV 想在某个 opKey 加扩展逻辑·按下表选切入位置：

### opKey `importdata_hr` · 导入数据

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_import_record_hr` · 查看导入记录

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_list_hr` · 按列表导出

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_impttpl_hr` · 按导入模板导出

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_expttpl_hr` · 按导出模板导出

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_export_record_hr` · 查看导出记录

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `new` · 新增

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `modify` · 修改

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `view` · 查看

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

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
- order **2**: `SalaryItemSaveOp` · enabled=True
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **3**: `BaseDataSavePlugin` · enabled=True
  - 完整 FQN: `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin`
- order **4**: `BdVersionSaveServicePlugin` · 基础资料版本管理 · 主表 name + 版本子表 name 的写历史
  - 完整 FQN: `kd.bos.base.bdversion.BdVersionSaveServicePlugin`
- order **5**: `HRBaseDataStatusOp` · HR 基础资料单据状态（draft/audit/disable）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **6**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **7**: `HRBaseDataEnableOp` · HR 基础资料启用态管理 · 控制 enable 字段
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **8**: `HRBaseOriginalOp` · HR 基础资料原始值记录（对比变更前后）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）

### opKey `close` · 关闭

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `returndata` · 返回数据

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `SalaryItemSaveOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemSaveOp`
- order **2**: `HRBaseDataStatusOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **3**: `HRBaseDataLogOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **4**: `HRBaseDataEnableOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **5**: `HRBaseOriginalOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- order **6**: `SalaryItemDeleteOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemDeleteOp`
- order **7**: `SalaryItemAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemAuditOp`
- order **8**: `SalaryItemUnAuditOp` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.swc.hsbs.opplugin.web.basedata.SalaryItemUnAuditOp`

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
