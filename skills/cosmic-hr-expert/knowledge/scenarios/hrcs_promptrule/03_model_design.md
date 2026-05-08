# hrcs_promptrule · 模型设计

> **form**：`hrcs_promptrule` · 提示语映射
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**29**
- 必填字段：**2**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `businessobject` | 页面 | `BasedataField` | `hbp_entityobject` |
| `entity` | 业务对象 | `BasedataField` | `bos_entitymeta` |
| `entryprompt` | 提示语 | `BasedataField` | `hrcs_prompt` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `PromptRuleBatchImportSaveOp` | `HRDataBaseOp` | OP |
| `PromptRuleEditPlugin` | `AbstractFormPlugin` | Edit/Form |
| `PromptRuleInitPlugin` | `HRBaseDataCommonEdit` | Edit/Form |
| `PromptRuleListPlugin` | `AbstractListPlugin` | List |
