# 变更影响面 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于 PositionTplTypeSaveOp + 场景分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、改动影响矩阵

| 改什么 | 影响范围 | 级联动作 | 风险等级 |
|---|---|---|---|
| 禁用类型（enable=0）| hbpm_positiontpl 岗位模板 | 下游岗位模板类型字段游离 | 🔴 高 |
| 删除类型 | 同上 · 不可恢复 | 游离数据无法自动修复 | 🔴 高 |
| 修改 index（排序号）| 下拉列表显示顺序 | 仅 UI 展示影响 | 🟢 低 |
| 修改 name | 下拉显示名称 | 历史数据显示变化 | 🟡 中 |
| 修改 ctrlstrategy | 可见范围 | 影响其他 org 用户能否看到此类型 | 🟡 中 |
| 重复 index | PositionTplTypeIndexUniqueValidator 拦截 | 保存失败，高亮 index 字段 | 🟢 低（自动拦截）|

---

## 二、下游受影响模块

```
hbpm_positiontpltype（岗位模板类型）
    ↓ 被引用
hbpm_positiontpl（岗位模板）
    · type 字段引用本场景
    ↓ 被引用
hbjm_jobhr（岗位主数据）
    · 岗位可能选择岗位模板
```

---

## 三、ISV 改动风险评估

### 低风险
- CS-01 加 ISV 扩展字段
- CS-02 并列挂删除/禁用前校验

### 中风险
- 修改 index 排序逻辑（影响 PositionTplTypeIndexUniqueValidator 的校验范围判断）

### 高风险（严禁）
- 继承 `PositionTplTypeEditPlugin` / `PositionTplTypeSaveOp` / `AbsOrgBaseOp`（PR-001）
- 修改 PositionTplTypeSaveOp 注册的 3 个 Validator 之一（会破坏唯一性约束）
