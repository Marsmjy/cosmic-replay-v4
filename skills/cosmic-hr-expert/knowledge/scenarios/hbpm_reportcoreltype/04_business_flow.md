# 业务流转 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于 37 插件注册表 + 反编译实证
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景业务流转概述

`hbpm_reportcoreltype` 是协作类型字典，业务流转是标准的基础资料直接生效模式（无审批）。核心特色在于：列表层有 `PositionnBaseDataOrderPlugin` 双列排序，OP 层有 `BaseDataBuOp`（hbpm 域）注册控制策略校验。

---

## 二、业务状态机

```
[新增]
   ↓ 填写 name / number / orgteamtype（必填）/ ctrlstrategy 等
[草稿]
   ↓ save（经过 CtrlStrategyValidator 校验）
[已审核]（直接保存即生效，无审批）
   ↓ enable/disable
[启用 enable=1] ←→ [禁用 enable=0]
```

---

## 三、表单层生命周期

```
[用户访问协作类型列表]
  ↓
HRBaseDataTplList.beforeBindData      · L1 列表模板
HRBasedataLogList.beforeBindData      · 日志列表
  ↓
PositionnBaseDataOrderPlugin.setFilter  · ⭐ 专属插件
  → setOrderBy("createorg.id asc,index asc")
  → 列表按创建组织+排序号双列排序
  ↓
[列表展示（已按 createorg.id + index 双列排序）]

[用户打开编辑弹窗]
  ↓
HRBaseDataTplEdit.afterBindData
HRHiesButtonSwitchPlugin.afterBindData
PositionBasedataEdit.afterBindData    · ⭐ 专属插件（与 hbpm_basedatalist 共用）
  → issyspreset=true? → setBillStatus(VIEW) → 只读
  ↓
[页面展示（预置数据为只读）]
```

---

## 四、操作链（OP 层 · 保存流程）

```
用户点保存
  ↓
CodeRuleOp #20                         · 生成/校验 number
HRBaseDataStatusOp #21 · onAddValidators · 状态校验
  ↓
BaseDataBuOp（hbpm 域）#26 · onAddValidators · ⭐ 注册 CtrlStrategyValidator
    → CtrlStrategyValidator 校验控制策略配置合规
  ↓ 通过
HRBaseDataStatusOp.beforeExecuteOperationTransaction · 状态流转
HRBaseDataLogOp.beforeExecuteOperationTransaction    · 日志前置
HRBaseDataEnableOp.beforeExecuteOperationTransaction · 启用状态检查
HRBaseOriginalOp.beforeExecuteOperationTransaction   · 出厂数据保护
BdVersionSaveServicePlugin                           · 版本管理
  ↓
[落库 t_hbpm_reportcoreltype 主表 + t_hbpm_orgteamtype 子表]
  ↓
HRBaseDataLogOp.afterExecuteOperationTransaction · 写日志
```

---

## 五、orgteamtype 多选字段的保存处理

```
用户在编辑页选择 orgteamtype（多选，至少 1 个，required=true）
  ↓
保存时：
  主表行 → t_hbpm_reportcoreltype
  每个选中的 orgteamtype 值 → t_hbpm_orgteamtype 子表（一行一个）
  子表行 id 由平台自动生成（无需 ISV 处理）
```

---

## 六、与 hbpm_basedatalist / hbpm_positiontpltype 的流程差异对比

| 维度 | hbpm_basedatalist | hbpm_positiontpltype | hbpm_reportcoreltype |
|---|---|---|---|
| 列表排序 | 无专属排序 | 无专属排序 | createorg+index 双列排序 |
| 编辑保护 | issyspreset 只读 | enable='0' 只读 | issyspreset 只读 |
| OP 层专属 Validator | 无 | 3 个（名称+index+策略）| 1 个（CtrlStrategy）|
| 子表数据 | 无子表 | 无子表 | orgteamtype 子表 |
| index 自动填充 | 无 | 有（步长 10）| 无 |
