# 扩展点全图 · 协作字段配置

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 所有标品插件清单

📌 详见 [_auto_plugin_registry.md](_auto_plugin_registry.md) —— 每次重跑 probe 会覆盖

**关键统计**：

- 插件总数: **2**（含跨 form 重复）
- 已解出生命周期方法数: **2**

## ✅ verified · 按生命周期方法分组

见 [_auto_plugin_registry.md](_auto_plugin_registry.md) 的 **按生命周期方法分组** 段

执行顺序规则：**同一方法，按插件注册顺序依次调用**

## 🟡 likely · ISV 最常覆盖的扩展点

<TODO> 专家补充 Top 3 最常覆盖：

1. `<classname>` — 覆盖原因: <TODO>
2. `<classname>` — 覆盖原因: <TODO>
3. `<classname>` — 覆盖原因: <TODO>

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin -->

## ISV 扩展指引（基于 PermissionTabPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeShowEmbedForm(kd.bos.form.control.embedform.BeforeShowEmbedFormEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public static public static <T extends kd.bos.metadata.form.ControlAp> T findApByPath(java.lang.String, kd.bos.metadata.dao.MetaCategory, java.lang.String)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PermissionTabPlugin L58
```java
  56           }
  57           for (ControlAp item : items) {
  58 >             boolean hasPerm = PermissionServiceHelper.hasViewPermission((long)RequestContext.get().getCurrUserId(), (String)formShowParameter.getCheckRightAppId(), (String)item.getKey());
  59               if (hasPerm) continue;
  60               this.getView().setVisible(Boolean.valueOf(false), new String[]{item.getKey()});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.coordination.PermissionTabPlugin -->
