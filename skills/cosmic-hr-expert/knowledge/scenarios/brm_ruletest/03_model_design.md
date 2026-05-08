# brm_ruletest · 模型设计

> **form**：`brm_ruletest` · 规则测试
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**10**
- 必填字段：**3**
- 引用基础资料字段：**3**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `bizapp` | 应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `scene` | 场景 | `BasedataField` | `brm_scene` |
| `bu` | 组织 | `OrgField` | `bos_org` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `RuleTestPlugin` | `HRDataBaseEdit` | Edit/Form |
