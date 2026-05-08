# hrcs_esigncosealmgr · 模型设计

> **form**：`hrcs_esigncosealmgr` · 企业印章管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**3**
- 必填字段：**0**
- 引用基础资料字段：**1**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `esignsp` | 电子签服务商 | `BasedataField` | `hrcs_esignspmgr` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ESignCOSealMgrPlugin` | `HRDynamicFormBasePlugin` | Edit/Form |
