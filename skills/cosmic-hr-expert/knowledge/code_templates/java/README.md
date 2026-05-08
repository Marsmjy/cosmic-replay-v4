# Java 扩展代码模板 · 从 dcs 真实项目抽取

> **来源**：22 个真实交付 ISV 项目（`D:/myproject/dcs/`）
>
> **原则**：**不合成 · 只摘录** · 每个模板带 sourceProject / sourceFile 可回溯
>
> **用途**：Claude Code 收到需求 → 查 `intent_decision_tree.json` 命中 intent → 读本目录对应模板 → 填替换变量（`{{XXX}}`）→ 产出代码

## 模板 × intent 映射

| 模板文件 | 对应 intent(s) | 源项目 | 行数 |
|---|---|---|---|
| `op_before_execute_validate.java` | `before_save_validation` · `before_save_field_supplement` | 编制管理 · SavePlanOp | ~65 行 |
| `op_before_execute_batch_process.java` | `before_save_field_supplement` · `generic_new_bill_template` | 合同续签 · RenewBatchOpPlugin | ~80 行 |
| `validator_custom.java` | `before_save_validation` · `field_uniqueness_validation` | 编制管理 · PositionValidator | ~40 行 |
| `schedule_task.java` | `schedule_task_batch_process` · `org_transfer_mass` | hr-hdm-orgtransfer · OrgParentChgTask | ~40 行 |
| `list_plugin_filter_action.java` | `list_data_permission` · `list_row_action_dispatch` | 外部招聘 · JobListPlugin | ~60 行 |

## 模板格式约定

每个模板文件有 4 个注释区块：

```java
/*
 * ═════ SOURCE ═════
 *   来源项目 / 源文件路径 / Git commit (可选)
 *
 * ═════ INTENT ═════
 *   对应 intent_decision_tree.json 的哪些 intent
 *
 * ═════ VARIABLES ═════
 *   模板里的 {{XXX}} 替换槽位 · 含义 · 例子
 *
 * ═════ CAUTION ═════
 *   用本模板前要注意的坑（从 intent.minefields 来）
 */
```

## 使用流程（Claude Code 角度）

1. 读用户需求 · 查 `intent_decision_tree.json` 匹配 keywords
2. 命中 intent · 读 `implPath` 确认"是 Java 不是 Python"
3. 查本目录找对应 intent 的模板
4. 读模板源码 + 替换变量
5. 查 `minefields` 核对不要踩坑
6. 如有 `csRef` · 进 admin_org 06 方案看类似案例
7. 如有 `dcsRef` · 进 dcs 项目看完整实现

## 版本

- v1 · 2026-04-24 · 首版 · 5 个模板
- 下次标品升级 · dcs 新增项目时可重扫（`index_dcs_projects.py`）· 更多模板候选
