# 扩展点全图 · haos_adminorgstruct

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `haos_adminorgstruct` 标品接入 **28** 个插件。

### FormPlugin（8）
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `TemplateTreeBaseDataPlugin` ← `kd.bos.form.plugin.TemplateTreeBaseDataPlugin`
- `TemplateBaseGroupEdit` ← `kd.bos.form.plugin.TemplateBaseGroupEdit`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HisLineTimeTplFormEdit` ← `kd.hr.hbp.formplugin.web.newhismodel.HisLineTimeTplFormEdit`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`

### ListPlugin（4）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `TimelineTplListPlugin` ← `kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
- `TimelineLogList` ← `kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList`
- `HisLineMobileListPlugin` ← `kd.hr.hbp.formplugin.web.newhismodel.mobile.HisLineMobileListPlugin`

### OpPlugin（11）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `BaseTreeOp` ← `kd.bos.business.plugin.BaseTreeOp`
- `BaseTreeLongNumberOp` ← `kd.bos.business.plugin.BaseTreeLongNumberOp`
- `HisHRBaseDataOp` ← `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HisLineTimeTplOp` ← `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `HisUniqueValidateOp` ← `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`

### 其他（5）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `BaseTreeDeleteValidate` ← `kd.bos.business.plugin.BaseTreeDeleteValidate`
- `BaseTreeEnableValidate` ← `kd.bos.business.plugin.BaseTreeEnableValidate`
- `TimelineF7FastFilter` ← `kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter`
- `HisBaseDataF7FastFilter` ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`

## ✅ verified · 反编译实抓真处理类（13 个 · ISV 可继承 8 / 禁继承 5）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（8 个 · 详细画像）

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

#### `HRBaseDataLogOp` · OP_PLUGIN @ `OPERATION` · opKey=`unsubmit`

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

### ❌ 禁继承类（5 个 · @SdkInternal 内部类 · 详细说明）

以下类是**苍穹平台内部实现**·绝对不能继承（继承后标品升级会破插件）：

#### `HRBaseDataImportEdit` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承链**: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `TimelineTplListPlugin` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `TimelineLogList` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `TimelineF7FastFilter` · OTHER · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter`
- **继承链**: `kd.bos.base.AbstractBasedataController`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

#### `HisBaseDataF7FastFilter` · OTHER · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
- **继承链**: `kd.bos.base.AbstractBasedataController`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## ✅ verified · opKey 扩展切入点详解（16 个有执行链的 opKey）

ISV 想在某个 opKey 加扩展逻辑·按下表选切入位置：

### opKey `save` · 保存

**执行链**（11 个 plugin 按 order 顺序跑）：

- order **1**: `CodeRuleOp` · 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator
  - 完整 FQN: `kd.bos.business.plugin.CodeRuleOp`
- order **2**: `BaseTreeOp` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeOp`
- order **3**: `BdVersionSaveServicePlugin` · 基础资料版本管理 · 主表 name + 版本子表 name 的写历史
  - 完整 FQN: `kd.bos.base.bdversion.BdVersionSaveServicePlugin`
- order **4**: `BaseTreeLongNumberOp` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeLongNumberOp`
- order **5**: `HisHRBaseDataOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp`
- order **6**: `HRBaseDataStatusOp` · HR 基础资料单据状态（draft/audit/disable）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **7**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **8**: `HRBaseDataEnableOp` · HR 基础资料启用态管理 · 控制 enable 字段
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **9**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`
- order **10**: `HRBaseOriginalOp` · HR 基础资料原始值记录（对比变更前后）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- ...（共 11 · 详见 `rules_chain_all.json`）

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）

### opKey `submit` · 提交

**执行链**（8 个 plugin 按 order 顺序跑）：

- order **1**: `CodeRuleOp` · 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator
  - 完整 FQN: `kd.bos.business.plugin.CodeRuleOp`
- order **2**: `BaseTreeOp` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeOp`
- order **3**: `BaseTreeLongNumberOp` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeLongNumberOp`
- order **4**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`
- order **5**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **6**: `HRBaseDataEnableOp` · HR 基础资料启用态管理 · 控制 enable 字段
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- order **7**: `HisUniqueValidateOp` · 时态模型唯一性校验（同一 boid 同一时间段唯一）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
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

### opKey `delete` · 删除

**执行链**（7 个 plugin 按 order 顺序跑）：

- order **1**: `BaseTreeOp` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeOp`
- order **2**: `BaseTreeDeleteValidate` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeDeleteValidate`
- order **3**: `CodeRuleDeleteOp` · enabled=True
  - 完整 FQN: `kd.bos.coderule.CodeRuleDeleteOp`
- order **4**: `HRBaseDataStatusOp` · HR 基础资料单据状态（draft/audit/disable）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- order **5**: `HisHRBaseDataOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp`
- order **6**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **7**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `audit` · 审核

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `unaudit` · 反审核

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `enable` · 启用

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `BaseTreeEnableValidate` · enabled=True
  - 完整 FQN: `kd.bos.business.plugin.BaseTreeEnableValidate`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `confirmchange` · 确认变更

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `HisUniqueValidateOp` · 时态模型唯一性校验（同一 boid 同一时间段唯一）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `confirmchangenoaudit` · 确认变更

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `HisUniqueValidateOp` · 时态模型唯一性校验（同一 boid 同一时间段唯一）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `his_import_save` · 保存

**执行链**（2 个 plugin 按 order 顺序跑）：

- order **1**: `HisUniqueValidateOp` · 时态模型唯一性校验（同一 boid 同一时间段唯一）
  - 完整 FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- order **2**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `disable` · 禁用

**执行链**（1 个 plugin 按 order 顺序跑）：

- order **1**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `unsubmit` · 撤销

**执行链**（1 个 plugin 按 order 顺序跑）：

- order **1**: `HRBaseDataLogOp` · HR 基础资料操作日志
  - 完整 FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`

**ISV 切入点选择**：
- 想在标品规则**之前**做事 → 在 order=1 之前注入 ISV plugin（用 BeforeXxx 类型 plugin）
- 想在标品规则**之后**做事 → 注入 ISV plugin · order > 上面最大值
- 想**改写**标品规则 → 找上面「可继承类」段中对应类继承重写

**🚨 此 opKey 易踩雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `his_copy` · 复制

**执行链**（1 个 plugin 按 order 顺序跑）：

- order **1**: `HisLineTimeTplOp` · enabled=False
  - 完整 FQN: `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp`

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
