# attendance_wtc_marriageverify · 扩展点

> **数据源**：deep_scan_class_tree.md

## 扩展点清单

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 `MarriageVerifyPlugin` 行为 | MarriageVerifyPlugin.java (6 方法) | ⭐⭐ | (按需) |
| EP-02 | 改 `MobilleMarriageVerifyPlugin` 行为 | MobilleMarriageVerifyPlugin.java (5 方法) | ⭐⭐ | (按需) |
| EP-03 | 改 `BatchMarriageDateValidator` 行为 | BatchMarriageDateValidator.java (1 方法) | ⭐⭐ | (按需) |
| EP-04 | 改 `MarriageDateValidator` 行为 | MarriageDateValidator.java (1 方法) | ⭐⭐ | (按需) |
| EP-05 | 改 `MarriageDateDealUtil` 行为 | MarriageDateDealUtil.java (6 方法) | ⭐⭐ | (按需) |

## SDK 白名单合规扩展模式

本资产所有类继承平台白名单基类（`Abstract*Plugin`）·扩展遵循：

- ✅ 继承白名单基类（`AbstractMobBillPlugIn` / `AbstractOperationServicePlugIn`）
- ✅ 用 `@Override` 重写苍穹生命周期方法
- ✅ ISV 字段必带 `${ISV_FLAG}_` 前缀
- ✅ 走 ext 扩展元数据·不直接 modifyMeta 标品 form
- ❌ 不继承 ISV 自己的插件（双继承死循环·PR-001）
- ❌ 不直接调内部 API（无 @SDKPublic 注解的）

## 关联 PR

- **PR-001** ISV 隔离 / **PR-008/009** 时序资料 / **PR-010** OP 13 生命周期 / **PR-011** BEC 事件中心

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)
