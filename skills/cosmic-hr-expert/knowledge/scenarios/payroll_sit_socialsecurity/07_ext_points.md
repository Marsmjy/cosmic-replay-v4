# payroll_sit_socialsecurity · 扩展点

> **数据源**：deep_scan_class_tree.md

## 扩展点清单

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 `SinsurfileHelper` 行为 | SinsurfileHelper.java (15 方法) | ⭐⭐ | (按需) |
| EP-03 | 改 `SocialSecurityFundCommon` 行为 | SocialSecurityFundCommon.java (1 方法) | ⭐⭐ | (按需) |
| EP-04 | 改 `SocialSecurityFundFormPlugin` 行为 | SocialSecurityFundFormPlugin.java (12 方法) | ⭐⭐ | (按需) |
| EP-05 | 改 `SocialSecurityFundListPlugin` 行为 | SocialSecurityFundListPlugin.java (1 方法) | ⭐⭐ | (按需) |
| EP-06 | 改 `SocialSecurityPaybillListPlugin` 行为 | SocialSecurityPaybillListPlugin.java (1 方法) | ⭐⭐ | (按需) |
| EP-07 | 改 `SocialSecurityPayClickNumberPlugin` 行为 | SocialSecurityPayClickNumberPlugin.java (5 方法) | ⭐⭐ | (按需) |
| EP-08 | 改 `SocialSecurityPayFormPlugin` 行为 | SocialSecurityPayFormPlugin.java (15 方法) | ⭐⭐ | (按需) |
| EP-09 | 改 `SocialSecurityPayOperationPlugin` 行为 | SocialSecurityPayOperationPlugin.java (3 方法) | ⭐⭐ | (按需) |
| EP-10 | 改 `SocialSecurityPayValidator` 行为 | SocialSecurityPayValidator.java (4 方法) | ⭐⭐ | (按需) |
| EP-11 | 改 `SocialSecurityPersonFilterFormPlugin` 行为 | SocialSecurityPersonFilterFormPlugin.java (5 方法) | ⭐⭐ | (按需) |
| EP-12 | 改 `OperateResultCheck` 行为 | OperateResultCheck.java (1 方法) | ⭐⭐ | (按需) |
| EP-13 | 改 `PersonOrgUtil` 行为 | PersonOrgUtil.java (1 方法) | ⭐⭐ | (按需) |
| EP-14 | 改 `StringHelper` 行为 | StringHelper.java (11 方法) | ⭐⭐ | (按需) |
| EP-15 | 改 `TypeConvertUtil` 行为 | TypeConvertUtil.java (13 方法) | ⭐⭐ | (按需) |

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
