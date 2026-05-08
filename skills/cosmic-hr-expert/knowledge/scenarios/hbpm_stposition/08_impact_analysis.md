# 变更影响面 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 scene_doc.json + 反编译分析 + 物理表共用架构
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. ⭐ 最重要认知：共用物理表双场景影响

**任何对 hbpm_stposition 的元数据修改（加字段/改字段属性）都会同时影响 hbpm_positionhr**，因为两者共用 `t_hbpm_position` 物理表。

| 修改操作 | 影响范围 | 风险级别 |
|---|---|---|
| modifyMeta 加字段 | hbpm_stposition 和 hbpm_positionhr 双场景 | 中等 |
| 改字段必填属性 | 两场景的保存/变更操作都会触发新必填校验 | 高 |
| 改字段长度 | 物理表列长度变更，双场景数据写入都受影响 | 高 |
| 改字段名称（显示名）| 仅影响对应 formId 的视图层，不影响物理表 | 低 |

---

## 2. 下游引用影响（变更/禁用标准岗位时）

### 2.1 hbpm_positionhr（岗位信息维护）

| 字段 | 引用方式 | 影响 |
|---|---|---|
| `stposition` | HisModelBasedataField → hbpm_stposition.boid | 标准岗位变更时 boid 不变，hbpm_positionhr 无需更新 |

**关键认知（PR-009）**：hbpm_positionhr 存的是 stposition 的 **boid**，不是 id。标准岗位发生版本变更（change）时，boid 保持不变，所以 hbpm_positionhr 不需要任何级联更新。

**禁用标准岗位时的影响**：
- 标准岗位被禁用后，hbpm_positionhr 的 stposition 字段仍保留 boid 引用
- 标品默认不会级联禁用关联岗位（需 ISV 通过 CS-02 自行实现前置校验）

### 2.2 hrpi 域（员工档案）

| 场景 | 字段 | 引用方式 |
|---|---|---|
| hrpi_empjobrel | stposition | boid 引用（员工任职记录关联标准岗位）|

员工任职关系（hrpi_empjobrel）中可能直接或间接引用 stposition boid，变更后 boid 不变，所以不需要级联更新。

---

## 3. 物理表变更影响矩阵

| 操作 | t_hbpm_position | t_hbpm_standposentry | t_hbpm_position_l | hbpm_positionhr 数据 |
|---|---|---|---|---|
| 新增标准岗位（save）| 写入（isstandardpos=1）| 写入 adminorg 关联 | 写入多语言 | 不影响 |
| 变更（change）| 新写一行（旧行 iscurrentversion=false）| 新写一行 | 新写多语言 | 不影响（boid 不变）|
| 修改（modify）| 更新当前版本行 | 更新 | 更新 | 不影响 |
| 禁用（disable）| 更新 fenable=10 | - | - | 保留引用 · 但 stposition 变为禁用状态 |
| 删除（delete）| 删所有版本行（boid 链）| 删关联行 | 删多语言 | ⚠ hbpm_positionhr 游离 |

---

## 4. 插件链扩展的影响面

### 4.1 并列挂 FormPlugin 的影响

| 生命周期 | 影响范围 | 风险 |
|---|---|---|
| beforeBindData | 打开详情页时 | 低（不影响保存）|
| afterBindData | 同上 | 低 |
| propertyChanged | 字段变更时触发 | 中（死循环风险 · PR-004）|
| beforeDoOperation | 操作执行前 | 中（可能阻断操作）|
| afterDoOperation | 操作执行后 | 低（已完成 · 只影响 UI）|

### 4.2 并列挂 OP 的影响

| 生命周期 | 影响范围 | 风险 |
|---|---|---|
| onAddValidators | 对应 opKey 的所有操作 | 中（校验失败会阻断）|
| beforeExecuteOperationTransaction | 事务前 | 高（可改数据 · 可阻断）|
| afterExecuteOperationTransaction | 事务后 | 高（BEC 发重了产生幂等风暴）|

---

## 5. HisModel 时序变更的特殊影响

| 场景 | 影响 | 说明 |
|---|---|---|
| 变更生效日期 | 旧版本 iscurrentversion=false | 下游 boid 引用不受影响 |
| 多次变更同一天 | HisUniqueValidateOp 拦截 | 同 boid 同天不能有两个活跃版本 |
| 删除当前版本 | 上一版本自动恢复 iscurrentversion=true | HisModelOPCommonPlugin 处理 |
| 删除历史版本 | 只删那条记录 · 链表修复 | HisModelOPCommonPlugin 处理 |

---

## 6. ISV 扩展的双场景回归检查清单

部署 ISV 扩展到 hbpm_stposition 后，必须回归以下场景：

- [ ] hbpm_stposition 新增标准岗位（save 操作）
- [ ] hbpm_stposition 变更标准岗位（change + confirmchange 操作）
- [ ] hbpm_stposition 禁用 / 启用
- [ ] **hbpm_positionhr 新增岗位信息**（共用物理表 · ISV 字段会出现）
- [ ] **hbpm_positionhr 修改岗位信息**（同上）
- [ ] 从 hbpm_positionhr F7 选标准岗位（验证 F7 仍正常）
- [ ] 查看标准岗位历史版本列表

---

## 7. 关联文档

- `03_model_design.md` · 共用物理表架构说明
- `11_upstream_downstream_logic.md` · hbpm_positionhr 引用关系
- `06_customization_solutions.md` · CS-02 下游引用校验
