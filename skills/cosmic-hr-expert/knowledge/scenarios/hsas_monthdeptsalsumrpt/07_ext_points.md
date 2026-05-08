# 扩展点全图 · 组织薪资月汇总表

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 所有标品插件清单

📌 详见 [_auto_plugin_registry.md](_auto_plugin_registry.md) —— 每次重跑 probe 会覆盖

**关键统计**：

- 插件总数: **3**（含跨 form 重复）
- 已解出生命周期方法数: **1**

## ✅ verified · 按生命周期方法分组

见 [_auto_plugin_registry.md](_auto_plugin_registry.md) 的 **按生命周期方法分组** 段

执行顺序规则：**同一方法，按插件注册顺序依次调用**

## 🟡 likely · ISV 最常覆盖的扩展点

<TODO> 专家补充 Top 3 最常覆盖：

1. `<classname>` — 覆盖原因: <TODO>
2. `<classname>` — 覆盖原因: <TODO>
3. `<classname>` — 覆盖原因: <TODO>

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit -->

## ISV 扩展指引（基于 HRBUCAApplicationEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HRBUCAApplicationEdit L83
```java
  81       private static DynamicObject getHrBuCaDyByAppNumber(String appNumber) {
  82           HRBaseServiceHelper hbssAppBusinessType = new HRBaseServiceHelper("hbss_appbusinesstype");
  83 >         QFilter appQFliter = new QFilter("app.number", "=", (Object)appNumber);
  84           QFilter typeEnableQFliter = new QFilter("enable", "=", (Object)"1");
  85           DynamicObject dynamicObject = hbssAppBusinessType.queryOne("businesstype", new QFilter[]{appQFliter.and(typeEnableQFliter)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit -->
