# hies_taskinfo · 模型设计

> **form**：`hies_taskinfo` · 导入导出记录
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**47**
- 必填字段：**5**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `inttor` | 中断人 | `CreaterField` | `bos_user` |
| `businessfield` | 业务领域 | `BasedataField` | `hbss_cloud` |
| `app` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `usetpl` | 使用模板 | `BasedataField` | `hies_diaetplconf` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `TaskInfoListPlugin` | `HRDataBaseList` | List |
