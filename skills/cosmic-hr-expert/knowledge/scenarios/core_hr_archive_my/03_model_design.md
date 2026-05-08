# core_hr_archive_my · 模型设计

> **form**：`hspm_myermanfilereform` · 我的档案
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**3**
- 必填字段：**0**
- 引用基础资料字段：**2**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `fileview` | 多视图方案 | `BasedataField` | `hspm_fileviewconfig` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `MyFileReformHomePlugin` | `HRDataBaseEdit` | Edit/Form |
