# 业务流转 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于 35 插件注册表 + 反编译实证（PositionTplTypeEditPlugin + PositionTplTypeSaveOp）
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景业务流转概述

`hbpm_positiontpltype` 是岗位模板的上游分类字典，业务流转是典型的基础资料直接生效模式（无审批）。核心特色是保存前的 3 层 Validator 校验 + 保存失败时的字段级高亮错误提示。

---

## 二、业务状态机

```
[新增]
   ↓
[草稿 · status=Draft]
   ↓ save（经过 3 个 Validator 校验）
   ↓ 名称唯一 + index 唯一 + 控制策略合规
[已审核 · status=Audited]（直接保存即生效，无审批）
   ↓ enable/disable
[启用 enable=1] ←→ [禁用 enable=0（编辑页切 VIEW + 隐藏按钮）]
```

---

## 三、表单层生命周期（带 index 自动填充）

```
[用户打开新建/编辑弹窗]
  ↓
HRBaseDataTplEdit.afterBindData         #6  · L1 通用模板
HRHiesButtonSwitchPlugin.afterBindData  #8  · HIES 按钮开关
PositionTplTypeEditPlugin.afterBindData #9  · ⭐ 核心专属插件
    → enable="0"? → VIEW 状态 + 隐藏按钮（INV-TT-01）
    → index=0/空? → 查 maxIndex → 填 maxIndex+10 → setDataChanged(false)（INV-TT-02）
  ↓
[页面展示（禁用态为只读 · 新建时 index 已自动填值）]

[用户填写数据，点保存]
  ↓
HRBaseDataTplEdit.beforeDoOperation     · UI 层前置检查
  ↓
[进入 OP 链]

[操作结果返回]
  ↓
HRBaseDataTplEdit.afterDoOperation
PositionTplTypeEditPlugin.afterDoOperation #9 · ⭐ 核心专属插件
    → 操作失败? → 检查 nameError → 高亮 name 字段
    → 操作失败? → 检查 indexError → 高亮 index 字段
```

---

## 四、操作链（OP 层 · 保存流程）

```
用户点保存
  ↓
CodeRuleOp #18           · 生成/校验 number 编码
BaseDataSavePlugin #19   · bos 平台基础资料保存前置
  ↓
HRBaseDataStatusOp #21 · onAddValidators · 注册状态 Validator
  ↓
PositionTplTypeSaveOp #25 · onAddValidators · 注册 3 个业务 Validator：
    1. PositionTplCommonValidator    → 名称唯一性校验（失败 → nameError 变量）
    2. PositionTplTypeIndexUniqueValidator → index 唯一性（失败 → indexError 变量）
    3. CtrlStrategyValidator         → 控制策略合规
  ↓ [Validator 执行]
  ↓ 任一 Validator 失败 → OperationResult.isSuccess=false
  ↓ → 回到 afterDoOperation 高亮错误字段
  ↓ 全部通过
HRBaseDataStatusOp #21 · beforeExecuteOperationTransaction · 状态流转
HRBaseDataLogOp #22 · beforeExecuteOperationTransaction · 日志前置
HRBaseDataEnableOp #23 · beforeExecuteOperationTransaction · 启用状态
HRBaseOriginalOp #24 · beforeExecuteOperationTransaction · 出厂数据保护
PositionTplTypeSaveOp #25 · beginOperationTransaction · index 兜底填充
BdVersionSaveServicePlugin #20 · 版本管理
  ↓
[落库 t_hbpm_positiontpltype]
  ↓
HRBaseDataLogOp #22 · afterExecuteOperationTransaction · 日志后置
```

---

## 五、启用/禁用流程

```
[用户点禁用]
  ↓
HRBaseDataStatusOp.onAddValidators   · 状态合法性
BaseDataDisablePlugin #30            · bos 平台禁用操作
HRBaseDataEnableOp.beforeExecuteOperationTransaction
  ↓
enable 从 1 → 0
disabler / disabledate 自动写入
  ↓
[下次打开编辑页] → PositionTplTypeEditPlugin.afterBindData 检测 enable="0" → VIEW 模式
```

---

## 六、操作完整清单（35 插件覆盖）

本场景有 35 个插件，覆盖保存/删除/审核/反审/提交/撤提/禁用/启用等完整操作，比 hbpm_basedatalist（18 插件）多了 bos 平台的 BaseDataAuditPlugin/BaseDataDeletePlugin 等操作插件。这是 L3 带组织基础资料模板带来的更完整操作集。
