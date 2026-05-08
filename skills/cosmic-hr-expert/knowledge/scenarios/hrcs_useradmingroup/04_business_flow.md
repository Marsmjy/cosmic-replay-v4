# 业务流转 · hrcs_useradmingroup

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（基于 OP 类名 + opKey 类型推断状态流 · v2）
> **数据源**: `rules_chain_all.json` · 反编译 OP 类
> **生成**: polish_form_scene_v2.py

## ✅ verified · 业务动作链（实抓 opKey）

## ✅ verified · OP 类执行链（反编译实证）

反编译共发现 **3** 个 OP 类，按命名推断生命周期：

| OP 类 | 推断阶段 |
|---|---|
| `AdminGroupAddUserOp` | 通用 |
| `AdminGroupDelOp` | 通用 |
| `AdminGroupDelUserOp` | 通用 |

## 🟡 unverified · 状态字段 + 审批链（待人工补充）

<TODO 人工补>
- 状态字段用的是哪个？（`billstatus` / `enable` / `iscurrentversion` ...）
- 审批链是否固定？是否走 hpfs_chgaction 配置驱动？
- 生效时机（提交即生效 / 审批通过生效 / 指定日期生效）
