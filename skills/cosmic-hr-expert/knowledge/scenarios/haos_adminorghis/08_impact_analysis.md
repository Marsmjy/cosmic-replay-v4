# 变更影响面 · 组织历史查询（haos_adminorghis）

> **状态**：基于物理表共享分析 + refentity_reverse 引用图
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. ⚠ 跨场景影响认知（最重要）

本场景跟 admin_org_quick_maintenance **共用物理表 t_haos_adminorg** · 任何字段层改动都是**双场景同时影响**。

```
┌─────────────────────────────────────────────────────────────┐
│                     ISV 改一处 · 双场景受影响                  │
└─────────────────────────────────────────────────────────────┘

ISV 操作 modifyMeta(formId="haos_adminorg", op="add field", key="${ISV_FLAG}_xxx")
              │
   ┌──────────┼──────────┐
   ▼                     ▼
admin_org_quick_       haos_adminorghis
maintenance            （本场景）
（当前版本视图）         （历史版本视图）
   │                     │
   ▼                     ▼
列表/详情页立即可见     列表/详情页立即可见
影响 save 操作         影响 view / his_save
影响 confirmchange     影响 revise
```

---

## 2. 改本场景会影响哪些下游

### 2.1 物理表层影响（共表）

| 改动类型 | 影响 | 严重度 |
|---|---|---|
| 在 haos_adminorg 主层加字段（如 ISV `${ISV_FLAG}_region`）| admin_org_quick_maintenance + 本场景双视图同时可见新字段 | 🟡 中（双场景必须双测）|
| 修改 t_haos_adminorg 列长度 / 类型 / 必填 | 双场景共享 · 物理 DDL 同时影响 | 🔴 高（必须停机部署 · 数据迁移）|
| 增加索引（如 ISV 扩展字段查询用）| 双场景查询性能同时受益 / 受影响 | 🟢 低 |

### 2.2 业务层影响（时序模型）

| 改动类型 | 影响 | 严重度 |
|---|---|---|
| 改 his_save Validator 链 | 历史补录路径改变 · 但本场景查询不变 | 🟢 低 |
| 改本场景 list 排序 / 列定制（CS-02）| 仅本场景可见 · 不影响 admin_org_quick_maintenance | 🟢 低 |
| 改本场景 setFilter 加字段过滤（CS-01）| 仅本场景生效 · 不溢出 | 🟢 低 |
| 改 revise 逻辑（继承 HisModelFormCommonPlugin · ⚠ 不允许）| 不允许 · final 类禁继承 | 🔴 编译过不去 |

### 2.3 时序字段层影响（碰不得）

`boid` / `iscurrentversion` / `hisversion` / `sourcevid` / `firstbsed` / `bsed` / `bsled` / `datastatus` / `changedescription` 是 hbp_histimeseqtpl 模板字段 · 改了影响**整个 HR 时序体系**：

- hbpm（岗位）：hbpm_position / hbpm_positionhr / hbpm_stposition
- hbjm（职位 / 职级）：hbjm_jobhr / hbjm_joblevel / hbjm_jobgrade
- hrpi（员工）：hrpi_empjobrel / hrpi_empposorgrel / hrpi_rotationinfo / hrpi_dispatchinfo
- haos（组织）：haos_adminorg / haos_adminorgdetail / haos_adminorghis（**本场景**）/ haos_structproject / haos_structure

**绝对不要碰**这 9 个时序字段 · 改它就是定时炸弹。

---

## 3. 反向追查：本场景被谁引用？

来源：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.haos_adminorghis` 段 + refs.haos_adminorghrf7 段（共用 boid 维度）

### 3.1 引用 haos_adminorghrf7（行政组织 F7 · = haos_adminorg 的 F7 视图）

下游约 **17 条 hrpi_*** 引用（参考 admin_org_quick_maintenance 06 CS-04 反向追查）：
- `hrpi_empjobrel.adminorg`（员工任职关系）
- `hrpi_empposorgrel.adminorg`（人岗组关系）
- `hrpi_rotationinfo.adminorg`（轮岗信息）
- `hrpi_dispatchinfo.adminorg`（派遣信息）
- ...（共 17 条）

**业务含义**：组织变化（不论 admin_org_quick 改还是本场景补录历史）都可能产生新版本 · 下游员工任职关系**当前版本 hrpi_empjobrel.adminorg 引用的是 boid**（PR-009）· 所以**通常不需要级联更新**（PR-009 设计意图）。

但有特殊情况：组织被禁用（disable）→ 影响下属员工的 enable 状态。这条逻辑由 admin_org_quick_maintenance 的 disable opKey 链处理 · 与本场景无关。

### 3.2 直接引用 haos_adminorghis 的下游

通常 0 个。`haos_adminorghis` 是**视图层** · 不会被业务实体作为引用目标（业务引用的是 `haos_adminorghrf7` 或 `haos_adminorgdetail`）。

---

## 4. 历史补录（his_save）的影响面

his_save 写入数据后：

| 影响目标 | 实际效果 | 时序 |
|---|---|---|
| t_haos_adminorg 该 boid 的历史版本行 | INSERT 一行（iscurrentversion=false）| 立即 |
| t_haos_adminorg 该 boid 的当前版本行（iscurrentversion=true）| **不动**（OrgHisOrgCurrVerParentValidator 校验合法性 · 但不修改）| - |
| t_haos_adminorg_l 多语言行 | INSERT 多行（一个语言一行）| 同事务 |
| 下游 hrpi_* 引用 | **不动**（下游引用的是 boid · 不是 id · PR-009）| - |
| BEC 业务事件 | **不发**（grep 0 处）| - |

**关键认知**：his_save 是**非常局部的影响** · 只补一条历史版本行 · 不动当前版本 · 不发事件 · 不影响下游。

---

## 5. revise（修订）的影响面

revise 是从历史版本派生新版本（详见 04_business_flow.md §5）：

| 影响目标 | 实际效果 |
|---|---|
| t_haos_adminorg 派生新版本行 | INSERT 一行 · `iscurrentversion=false` · `hisversion = 老 + 1` · `sourcevid = 老 id` · `bsed = 用户填的新生效日期` |
| t_haos_adminorg 老版本行 | UPDATE `bsled = 新版本.bsed - 1` |
| t_haos_adminorg_l 多语言行 | INSERT 新行 |
| 下游 hrpi_* 引用 | **不动**（按 boid 引用）|
| BEC 业务事件 | **不发** |

> 注：如果 revise 操作完成后用户继续走 confirmchange 让派生版本生效 · 这条逻辑就跨到 admin_org_quick_maintenance 域 · 走 AdminOrgFastParentChangeOp / AdminOrgFastInfoChangeOp · 那时**会发 BEC**（admin_org_quick_maintenance CS-04 实证）。但 revise 本身只是派生 · 不发。

---

## 6. ISV 加自定义 Validator（CS-04）的影响面

| 影响目标 | 实际效果 |
|---|---|
| his_save OP 链 | 多了一个 Validator · 跟标品 10 个并列跑 |
| 校验失败时 | OP 整事务终止 · 用户看到 errorMessage |
| 校验通过时 | 跟原来流程一样 · 没有副作用 |
| admin_org_quick_maintenance.save / confirmchange 链 | **不影响**（ISV 自加 Validator 只挂 his_save · 不挂 save）|
| HIES 导入路径 | **可能影响**（如果 HIES 内部调 his_save · 业务方需要确认）|

> ⚠ HIES 导入会跑 OP 链吗？大概率会（标品平台导入向导设计就是模拟 OP 链）· 所以 ISV 加的 Validator 在 HIES 批量导入时**也会触发** · 可能导致大批量失败。预期内就保留 · 否则需要在 Validator 里识别"导入模式"做兼容（OperateOption.getVariableValue 拿到导入标识）。

---

## 7. 改本场景列表 setFilter（CS-01）的影响面

| 影响目标 | 实际效果 |
|---|---|
| haos_adminorghis 列表查询 | 多了 ISV QFilter · 用户看到的列表条数收窄 |
| t_haos_adminorg 物理表 | 不动 · 仅查询行为变 |
| admin_org_quick_maintenance 列表 | **不影响**（不同 form · 不同 setFilter）|
| F7 模式 | **不影响**（HisModelF7ListPlugin.setFilter 是另一条链 · 与列表 setFilter 独立）|
| versionchangecompare 对比页 | 取决于跳转传入的 id 集合 · 跟 setFilter 无直接关系 |

---

## 8. 改本场景 list 排序（CS-02）的影响面

| 影响目标 | 实际效果 |
|---|---|
| haos_adminorghis 列表默认排序 | ISV 覆盖标品 setOrder · 用户看到的列表顺序变化 |
| 用户点列头排序 | **保持有效**（用户操作高于默认）|
| F7 模式 | **不影响** |
| 全历史对比页（CS-03）| **不影响**（自建动态表单独立排序）|
| admin_org_quick_maintenance 列表 | **不影响** |

---

## 9. 共用物理表的特殊事故案例

### 9.1 案例 A：忘记双场景回归（典型）

ISV 加扩展字段 `${ISV_FLAG}_region` 到 haos_adminorg 主层 · 只在 admin_org_quick_maintenance 测过 · 上线后用户在 haos_adminorghis 列表点列头排序 `${ISV_FLAG}_region` 报错（DBA 没建索引 · t_haos_adminorg 50w 行 · 全表扫超时）。

**教训**：扩展字段后**双场景都要回归**（CS-01 踩坑明确说）· list 排序场景必须 DBA 配合建索引。

### 9.2 案例 B：误改 hbp_histimeseqtpl

某 ISV 想把 `bsled` 默认值改 · 直接 modifyMeta 在 hbp_histimeseqtpl 改 · 结果**全 HR 时序体系崩了**（haos / hbpm / hbjm / hrpi 都共用这个模板）· 5 个场景的列表查询全部报错。

**教训**：hbp_histimeseqtpl 是模板基类 · 所有 HR 时序场景共享 · **绝对不要改**。要扩展字段挂在具体场景层（如 haos_adminorg）· 不要挂模板层。

### 9.3 案例 C：在 his_save 加 Validator 拒绝带 `change*` 字段的补录

某 ISV 在 his_save 加自定义 Validator · 拒绝 changescene / changereason 字段为空的补录 · 但 HIES 批量导入时 · 标品 changescene 默认是空（导入数据没带）· 导致全批失败。

**教训**：his_save Validator 在 HIES 导入路径也会跑 · ISV 加校验时要考虑导入兼容性。可以在 Validator 里读 `OperateOption.getVariableValue("isImport")` 做分支兼容（如果业务允许）。

---

## 10. 影响面速查表

| 操作 | 物理表 | 双场景 | 下游 | BEC |
|---|---|---|---|---|
| 加 ISV 字段（haos_adminorg 主层）| 增列 | ✓ 双方可见 | 不影响（无新引用）| 不发 |
| 改本场景列表排序（CS-02）| 不动 | ✗ 仅本场景 | 不影响 | 不发 |
| 改本场景 setFilter（CS-01）| 不动 | ✗ 仅本场景 | 不影响 | 不发 |
| 加 his_save Validator（CS-04）| 不动 | ✗ 仅 his_save 路径 | 不影响 | 不发 |
| 加 revise 后置闭环（不推荐 · final 类禁继承）| 不动 | - | - | 不发 |
| 改本场景行级样式（CS-05）| 不动 | ✗ 仅本场景 | 不影响 | 不发 |
| 加跨场景跳转按钮（CS-06）| 不动 | 双方都加（发起 + 接收）| 不影响 | 不发 |

---

## 11. 关联文档

- `03_model_design.md` · §2 物理表共享架构详解
- `06_customization_solutions.md` · 各 CS 的扩展边界
- `knowledge/scenarios/admin_org_quick_maintenance/06_customization_solutions.md` · CS-01 字段扩展（双场景受影响）
- `knowledge/workbench/_indexes/refentity_reverse.json` · 反向引用图
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009
