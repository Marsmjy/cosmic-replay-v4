# 扩展点全图 · wtte_calresult

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `wtte_calresult` 标品接入 **7** 个插件。

### FormPlugin（5）
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WTCTipsPlugin` ← `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

### ListPlugin（2）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `CalculateResultList` ← `kd.wtc.wtte.formplugin.web.attcalculate.CalculateResultList`

## ✅ verified · ISV 可继承的 SDK 入口（curated_sdk 9 桶）

### FormPlugin · 表单生命周期（3 个）
- `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`

### ListPlugin · 列表生命周期（1 个）
- `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`

## ✅ verified · 反编译实抓真处理类（5 个 · ISV 可继承 4 / 禁继承 1）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（4 个 · 详细画像）

#### `HRCertCheckEdit` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- **完整继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- **重写生命周期方法（1）**:
  - `preOpenForm` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit;
  
  public class IsvCustomHRCertCheckEdit extends HRCertCheckEdit {
      @Override
      public void preOpenForm(... args) {
          super.preOpenForm(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md)

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

#### `HRCertCheckList` · LIST_PLUGIN @ `LIST_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- **完整继承链**: `kd.bos.list.plugin.AbstractListPlugin`
- **重写生命周期方法（1）**:
  - `preOpenForm` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.cert.HRCertCheckList;
  
  public class IsvCustomHRCertCheckList extends HRCertCheckList {
      @Override
      public void preOpenForm(... args) {
          super.preOpenForm(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md)

### ❌ 禁继承类（1 个 · @SdkInternal 内部类 · 详细说明）

以下类是**苍穹平台内部实现**·绝对不能继承（继承后标品升级会破插件）：

#### `HRBaseDataImportEdit` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## ✅ verified · opKey 扩展切入点详解（21 个有执行链的 opKey）

ISV 想在某个 opKey 加扩展逻辑·按下表选切入位置：

### opKey `importdata_hr` · 导入数据

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_import_record_hr` · 查看导入记录

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_list_hr` · 按列表导出

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_impttpl_hr` · 按导入模板导出

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `export_from_expttpl_hr` · 按导出模板导出

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `show_export_record_hr` · 查看导出记录

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `first` · 第一

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `previous` · 前一

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `next` · 后一

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `last` · 最后

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `save` · 保存

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）

### opKey `edit` · 编辑

**执行链**（5 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataImportEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- order **2**: `HRCertCheckEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- order **3**: `HRBaseUeEdit` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- order **4**: `HRHiesButtonSwitchPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- order **5**: `WTCTipsPlugin` · plugins.json 实证 · 标品 · extendable
  - 完整 FQN: `kd.wtc.wtbs.formplugin.web.prompt.WTCTipsPlugin`

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
