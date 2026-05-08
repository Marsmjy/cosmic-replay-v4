# core_hr_downermanfileword · 扩展点

> **数据源**：deep_scan_class_tree.md

## 扩展点清单

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 `CustomFileHeadHelper` 行为 | CustomFileHeadHelper.java (5 方法) | ⭐⭐ | (按需) |
| EP-02 | 改 `CustomPersonModelUtil` 行为 | CustomPersonModelUtil.java (4 方法) | ⭐⭐ | (按需) |
| EP-03 | 改 `CustomValueConvertHelper` 行为 | CustomValueConvertHelper.java (1 方法) | ⭐⭐ | (按需) |
| EP-04 | 改 `ErmanFileReusePlugin` 行为 | ErmanFileReusePlugin.java (5 方法) | ⭐⭐ | (按需) |
| EP-05 | 改 `PersonFileDownloadFormPlugin` 行为 | PersonFileDownloadFormPlugin.java (14 方法) | ⭐⭐ | (按需) |
| EP-06 | 改 `PersonFileDownloadRptPlugin` 行为 | PersonFileDownloadRptPlugin.java (5 方法) | ⭐⭐ | (按需) |
| EP-07 | 改 `WordPrintSelectTemplatePlugin` 行为 | WordPrintSelectTemplatePlugin.java (3 方法) | ⭐⭐ | (按需) |
| EP-08 | 改 `FileConversionImpl` 行为 | FileConversionImpl.java (15 方法) | ⭐⭐ | (按需) |

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
