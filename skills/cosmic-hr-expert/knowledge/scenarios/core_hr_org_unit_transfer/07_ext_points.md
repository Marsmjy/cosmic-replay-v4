# core_hr_org_unit_transfer · 扩展点

> 6 个标准扩展点·全部在 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) 详细描述

## 扩展点清单

| EP ID | 名称 | 修改位置 | CS 关联 |
|---|---|---|---|
| EP-01 | 改变动场景编码集 | 3 子类的 `CHANGE_SCENE_*_NUMBER` 常量 | CS-02 |
| EP-02 | 改调度执行天数 | `AbstractCommonTask.java:48 EXECUTE_DAYS` | CS-06 |
| EP-03 | 加 ISV 中间表自定义字段 | dym + OrgTransferHelper.java:357 + 业务代码 | CS-03 |
| EP-04 | 添加新 changeType | 新建子类 + schdata + docx 关键码值表 | CS-04 |
| EP-05 | 改调动单类型描述 | 3 子类 saveAndEffectBatchBill 调用最后一个参数 | — |
| EP-06 | 改数据源 | OrgTransferHelper.queryHAOSChangeMsg / queryPosChangeMsg | CS-05 |

## SDK 白名单合规扩展模式

ISV 扩展时·**禁止**继承 dcs_clean v1.5 实现的 5 类（标品已合规）·**允许**：

- ✅ 继承 `AbstractCommonTask` 加新 changeType（如 EP-04）
- ✅ 调用 `OrgTransferHelper` 的 19 个 public static 方法
- ❌ 改 5 类内部 private 方法签名（升级会冲突）

## 标品继承根

`kd.bos.schedule.executor.AbstractTask` (SDKPlugin 白名单)·本资产 5 类全部 SDK 白名单合规·**0 内部 API 引用**。

详见 [02_business_rules.md](02_business_rules.md) R-04 + [03_model_design.md](03_model_design.md) ISV 元数据归属表。
