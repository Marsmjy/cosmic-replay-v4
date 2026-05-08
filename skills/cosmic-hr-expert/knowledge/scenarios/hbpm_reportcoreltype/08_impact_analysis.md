# 变更影响面 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于场景分析 + 反编译实证
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、改动影响矩阵

| 改什么 | 影响范围 | 级联动作 | 风险等级 |
|---|---|---|---|
| 禁用协作类型（enable=0）| 汇报关系场景中引用本字典的配置 | 下游配置中协作类型字段游离 | 🔴 高 |
| 删除协作类型 | 同上 · 不可恢复 | 游离数据无法自动修复 | 🔴 高 |
| 修改 orgteamtype（所属组织分类）| 使用该协作类型的汇报关系过滤逻辑 | 按组织分类过滤的场景结果变化 | 🟡 中 |
| 修改 index（排序号）| 列表展示顺序（PositionnBaseDataOrderPlugin 双列排序）| 仅 UI 展示顺序变化 | 🟢 低 |
| 修改 name（名称）| 所有下游显示名称 | 历史配置显示名称同步更新 | 🟡 中 |
| 修改 ctrlstrategy（控制策略）| 可见组织范围 | 影响其他 org 用户能否看到此协作类型 | 🟡 中 |
| 修改 createorg（创建组织）| 列表排序（createorg.id asc 第一维）| 列表中该记录的分组位置变化 | 🟢 低 |

---

## 二、子表 orgteamtype 修改的特殊影响

`t_hbpm_orgteamtype` 子表采用**全量替换**模式（DELETE+INSERT）：
- 修改 orgteamtype 字段时，子表会先全量删除再重新插入
- 若下游有直接查子表 `t_hbpm_orgteamtype` 的报表或接口，会在极短时间窗内看到空子表
- ISV 不应直接查子表，应通过主实体 `hbpm_reportcoreltype` 的 orgteamtype 路径查

---

## 三、下游受影响模块

```
hbpm_reportcoreltype（协作类型字典）
    ↓ 被引用（字典选择）
汇报关系场景（按协作类型过滤/分类）
    ↓
hrpi_empjobrel（员工岗位汇报关系）
    ↓
组织汇报层级计算
```

---

## 四、ISV 改动风险评估

### 低风险
- CS-01 加 ISV 扩展字段（主实体）
- CS-02 并列挂删除/禁用前校验
- CS-03 并列挂 ctrlstrategy 字段联动提示

### 中风险
- 修改 orgteamtype 字段（影响下游按组织分类过滤的汇报关系配置）

### 高风险（严禁）
- 继承 `PositionBasedataEdit` / `PositionnBaseDataOrderPlugin` / `BaseDataBuOp`（hbpm 域）/ `AbsOrgBaseOp`（PR-001）
- 手动 DELETE/INSERT `t_hbpm_orgteamtype` 子表（平台框架已处理，手动操作破坏事务一致性）
- 直接 UPDATE 主表 `t_hbpm_reportcoreltype`（绕过 CtrlStrategyValidator 校验）
