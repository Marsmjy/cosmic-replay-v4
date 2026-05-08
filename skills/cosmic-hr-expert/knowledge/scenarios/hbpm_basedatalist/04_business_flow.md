# 业务流转 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于 18 插件注册表 + 反编译实证
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景业务流转概述

`hbpm_basedatalist` 是岗位基础资料的**分组入口聚合页**，本身不涉及审批流程。其核心业务流转分为两段：

1. **列表层**：分组展示所有基础资料类别 → 用户点击进入具体子列表
2. **编辑层**：具体基础资料的新增/修改/启用/禁用操作（由各子页面承载）

---

## 二、业务状态机

```
[新增]
   ↓
[草稿 · status=Draft]
   ↓ save
[已审核 · status=Audited]（基础资料无审批 · 直接保存即生效）
   ↓ enable/disable
[启用 enable=1] ←→ [禁用 enable=0]

说明：
- hbpm 基础资料不走审批单（无 SubmitOp / AuditOp）
- 保存即进入可用状态（HRBaseDataStatusOp 管控状态流转）
- 启用/禁用通过 HRBaseDataEnableOp 执行
```

---

## 三、操作链（OP 层）

### 保存操作（save）

```
用户点保存
  ↓
CodeRulePlugin          #1  · 生成/校验编码
  ↓
HRBaseDataStatusOp      #14 · onAddValidators · 注册状态校验
                             · beforeExecuteOperationTransaction · 状态流转检查
  ↓
HRBaseDataLogOp         #15 · beforeExecuteOperationTransaction · 日志记录前置
                             · afterExecuteOperationTransaction · 日志记录后置
  ↓
HRBaseDataEnableOp      #16 · beforeExecuteOperationTransaction · 启用状态检查
  ↓
HRBaseOriginalOp        #17 · beforeExecuteOperationTransaction · 出厂数据保护
  ↓
BdVersionSaveServicePlugin #13 · 版本管理
  ↓
[落库]
```

### 删除操作（delete）

```
用户点删除
  ↓
CodeRuleDeleteOp        #18 · 释放编码池
  ↓
HRBaseDataStatusOp      #14 · onAddValidators · 状态合法性
  ↓
[落库删除]
```

---

## 四、表单层生命周期

```
[用户打开列表页]
  ↓
HRBaseDataTplList.beforeBindData  · 列表数据绑定前处理
HRBasedataLogList.beforeBindData  · 日志列表初始化
  ↓
[列表显示 · 按 pagekey 分组]（HRBDGroupList 管控行为）
  ↓
[用户点击超链接/双击行]
  ↓
HRBDGroupList.billListHyperLinkClick / listRowDoubleClick
  → getPageKeyById(rowId) → showForm(ListShowParameter)
  ↓
[跳转到子列表页面]
  ↓
[用户打开编辑弹窗]
  ↓
HRBaseDataTplEdit.afterBindData
HRHiesButtonSwitchPlugin.afterBindData
PositionBasedataEdit.afterBindData  · ⭐ issyspreset 检查 → setBillStatus(VIEW)
  ↓
[页面显示（预置数据为只读）]
```

---

## 五、启用/禁用流程

```
[用户点禁用]
  ↓
HRBaseDataStatusOp.onAddValidators   · 注册状态合法性 Validator
HRBaseDataStatusOp.beforeExecuteOperationTransaction · 禁用前状态检查
HRBaseDataEnableOp.beforeExecuteOperationTransaction · 禁用执行
HRBaseDataLogOp.afterExecuteOperationTransaction     · 记录操作日志
  ↓
disabler / disabledate 字段自动写入
  ↓
enable 从 1 → 0
```

---

## 六、与审批单场景的对比

| 维度 | 本场景（直接基础资料）| 有审批单场景（如 homs_orgbatchchgbill）|
|---|---|---|
| 生效方式 | 保存即生效 | 审批通过后生效 |
| 状态流 | Draft→Audited（保存直跳）| Draft→Submit→Audit→Effect |
| 回滚 | HRBaseDataStatusOp 阻断 | 审批拒绝/撤回 |
| BEC 发布 | 无（标品 grep 0）| 有（如 OrgBatchChgBillEffectOp 发 BEC）|
