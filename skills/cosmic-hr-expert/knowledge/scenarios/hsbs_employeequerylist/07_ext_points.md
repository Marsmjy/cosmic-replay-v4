# 扩展点全图 · 计薪人员

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 所有标品插件清单

📌 详见 [_auto_plugin_registry.md](_auto_plugin_registry.md) —— 每次重跑 probe 会覆盖

**关键统计**：

- 插件总数: **3**（含跨 form 重复）
- 已解出生命周期方法数: **2**

## ✅ verified · 按生命周期方法分组

见 [_auto_plugin_registry.md](_auto_plugin_registry.md) 的 **按生命周期方法分组** 段

执行顺序规则：**同一方法，按插件注册顺序依次调用**

## 🟡 likely · ISV 最常覆盖的扩展点

<TODO> 专家补充 Top 3 最常覆盖：

1. `<classname>` — 覆盖原因: <TODO>
2. `<classname>` — 覆盖原因: <TODO>
3. `<classname>` — 覆盖原因: <TODO>

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.query.QueryListPlugin -->

## ISV 扩展指引（基于 QueryListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.query.QueryListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.query.QueryListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
- `protected protected kd.hr.hbp.business.service.query.ksql.KsqlConfig setCustomKSqlConfig()`
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.query.QueryListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.query.QueryListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.query.QueryListPlugin -->
