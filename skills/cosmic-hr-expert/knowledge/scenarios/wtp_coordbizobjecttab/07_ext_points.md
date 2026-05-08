# 扩展点全图 · wtp_coordbizobjecttab

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `wtp_coordbizobjecttab` 标品接入 **2** 个插件。

### FormPlugin（2）
- `PermissionTabPlugin` ← `kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin`
- `ForbidUrlOpenPlugin` ← `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`

## ✅ verified · 反编译实抓真处理类（2 个 · ISV 可继承 1 / 禁继承 1）

> 数据源：`_deep_resolve_index.json` · 由 cosmic-class-resolver 反编译产出

### 🟢 ISV 可继承类（1 个 · 详细画像）

#### `PermissionTabPlugin` · FORM_PLUGIN @ `BILL_FORM` · opKey=`save`

- **完整 FQN**: `kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin`
- **完整继承链**: `kd.bos.form.plugin.AbstractFormPlugin`
- **重写生命周期方法（2）**:
  - `beforeBindData` — ISV 子类可在此切入扩展逻辑
  - `closedCallBack` — ISV 子类可在此切入扩展逻辑
- **静态指标**: 写表 0 · 发事件 0 · 抛异常 0 · 调核心 Service 0 · trace 访问 1 类
- **ISV 扩展套路**：
  ```java
  package <你的 ISV 包>;
  
  import kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin;
  
  public class IsvCustomPermissionTabPlugin extends PermissionTabPlugin {
      @Override
      public void beforeBindData(... args) {
          super.beforeBindData(args);  // 必调 super · 保留标品行为
          // 你的 ISV 业务逻辑 ...
      }
  }
  ```
- **详细 ISV 扩展指南**: [`<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin/extension_guide.md`](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin/extension_guide.md)

### ❌ 禁继承类（1 个 · @SdkInternal 内部类 · 详细说明）

以下类是**苍穹平台内部实现**·绝对不能继承（继承后标品升级会破插件）：

#### `ForbidUrlOpenPlugin` · FORM_PLUGIN · opKey=`save`

- **FQN**: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
- **继承链**: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- **为什么禁**: 标识为内部实现 · ISV 继承会破升级兼容
- **该用什么**: 上一节「可继承类」中找对应 opKey 的类

**继承雷区铁律**：上述类带 `@SdkInternal` / `@SdkPlugin(role=internal)` / 历史模型内部 · ISV 不得继承。详见 `cosmic_sdk_annotation_whitelist.md`。

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）
