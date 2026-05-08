# 考勤周期封存提交校验 (wtc_roster) · 扩展点（Customization Points）

> 8 个标准扩展点·按客户需求定制·**不改资产骨架**
> 数据源：deep_scan_class_tree.md（每类 → 1 EP）+ _asset_meta.yaml

## 扩展点总览

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 Constant 行为 | Constant.java | ⭐⭐ | (按需) |
| EP-02 | 改 DateScope 行为 | DateScope.java | ⭐⭐ | (按需) |
| EP-03 | 改 Constant 行为 | Constant.java | ⭐⭐ | (按需) |
| EP-04 | 改 CommonPropertiesQueryUtil 行为 | CommonPropertiesQueryUtil.java | ⭐⭐ | (按需) |
| EP-05 | 改 BatchBusiTripBillSubmitPlugin 行为 | BatchBusiTripBillSubmitPlugin.java | ⭐⭐ | (按需) |
| EP-06 | 改 BusiTripBillSubmitPlugin 行为 | BusiTripBillSubmitPlugin.java | ⭐⭐ | (按需) |
| EP-07 | 改 RosterValidatorStorage 行为 | RosterValidatorStorage.java | ⭐⭐ | (按需) |
| EP-08 | 改 ArchivalInformationServiceImpl 行为 | ArchivalInformationServiceImpl.java | ⭐⭐ | (按需) |

---

## EP-01 · 改 Constant 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`Constant.java`

### 改造方案

Constant 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-02 · 改 DateScope 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`DateScope.java`

### 改造方案

DateScope 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-03 · 改 Constant 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`Constant.java`

### 改造方案

Constant 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-04 · 改 CommonPropertiesQueryUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CommonPropertiesQueryUtil.java`

### 改造方案

CommonPropertiesQueryUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-05 · 改 BatchBusiTripBillSubmitPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BatchBusiTripBillSubmitPlugin.java`

### 改造方案

BatchBusiTripBillSubmitPlugin 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-06 · 改 BusiTripBillSubmitPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BusiTripBillSubmitPlugin.java`

### 改造方案

BusiTripBillSubmitPlugin 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-07 · 改 RosterValidatorStorage 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`RosterValidatorStorage.java`

### 改造方案

RosterValidatorStorage 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-08 · 改 ArchivalInformationServiceImpl 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`ArchivalInformationServiceImpl.java`

### 改造方案

ArchivalInformationServiceImpl 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)


---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 form 加字段 | PR-001·必须走 ext 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin before/after·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品自带 BEC·ISV 不重发·避免重复事件 |
| ❌ 改标品 form 的 billstatus / auditstatus | 标品状态机·ISV 不能动·只能加 `${ISV_FLAG}_*` 自定义字段 |
| ❌ 继承 ISV 自己的插件加 if/else 分支 | 双继承死循环（PR-001）·必须并列挂多 plugin |

详见 [`_antipatterns.json`](../_antipatterns.json)。

## 关联 PR 红线

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext 扩展元数据 ✅
- **PR-008/009** 时序资料·HisModel 查询要带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法
- **PR-011** BEC 事件中心·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)

## 关联文档

- 部署 SOP：[deploy_sop.md](deploy_sop.md)
- 业务规则：[02_business_rules.md](../../scenarios/<scene>/02_business_rules.md)
- 扩展点全景：[07_ext_points.md](../../scenarios/<scene>/07_ext_points.md)
- 定制方案：[06_customization_solutions.md](../../scenarios/<scene>/06_customization_solutions.md)
- W1 标杆：[contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)
