# 变更影响面 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `scene_doc.json` minefield + `_auto_operations.md` 插件链 + `_auto_plugin_semantics.md` 反编译整合
> **核心发现**: 改职位 1 字段 ≈ 改 2-3 物理表 + 影响所有时序版本

---

## 一、改字段的影响

### IF-01 · 修改 hbjm_jobhr.number（职位编码）

**直接影响**：
- `t_hbjm_job.fnumber` 主表列
- `CodeRuleOp` + `CodeRuleDeleteOp` 编码规则基础资料的序号占用 / 回收

**间接影响**：
- **hbpm_position** 岗位模块：岗位可能按职位编码展示 / 关联（下游推测）
- **hrpi_*** 人员档案：人员的职位历史可能按 number 引用
- **报表**：所有职位维度报表（如"各序列职位数量"）
- **外部系统同步**：如与招聘系统 ATS / 薪酬系统的接口

**升级风险**：升级到新版本时，任何改过 number 类型 / 长度的定制都会与标品冲突

**推荐做法**：**通过扩展字段实现自定义职位编码**（CS-05），不要改标品 `number` 字段定义

### IF-02 · 修改 hbjm_jobhr.jobseq（职位序列）

**直接影响**：职位体系基石变动

**级联重算**：
- 关联 `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` 4 字段的合法性
- `joblevelrang` / `jobgraderang` 2 个区间串需要重算
- `lowjoblevelname` / `highjoblevelname` / `lowjobgradename` / `highjobgradename` 4 个显示名缓存需要重写

**下游反写**（推测，需专家确认）：
- `hbpm_position.job` 岗位关联的职位
- `hrpi_pemployee.job` 人员当前职位

**推荐**：仅通过 save 链的 `JobHrSaveOp` 重写 `onPreparePropertys` 扩展，不直接 SQL 改表

### IF-03 · 修改 hbjm_jobhr.enable（启用 / 禁用）

**影响**：
- 禁用后不能作为新岗位关联目标
- 禁用后不能关联到新员工的职位历史
- 禁用职位下的旧数据保留
- **二态设计**：启用(1) / 禁用(0)

**实现点**：
- `enable` 操作 → `JobHrEnableOp`（含 `JobEnableValidator`）
- `disable` 操作 → `JobHrDisableOp`
- 标品无"禁用前检查在职员工"业务校验，需自定义（CS-04）

**`scene_doc.json` minefield**：🟡 yellow（变更会级联影响下游 · 慎改）

### IF-04 · 修改 hbjm_jobhr.bsed / bsled（生效 / 失效日期）

**影响**：
- 产生新的时间轴版本
- 历史版本自动通过 `iscurrentversion` 字段切换
- 下游取数取决于当前时间点的有效版本

**注意**：
- `bsed` 不允许填写未来（save validation `2M42D=Y0/0XK`）
- 如需未来生效，必须走 `newhisversion` opKey 新增数据版本

**`scene_doc.json` minefield**：🟡 yellow

### IF-05 · 修改 hbjm_jobhr.status（单据状态 A/B/C）

**影响**：
- 单据状态由 `submit` / `audit` / `unsubmit` / `unaudit` 四个 opKey 流转
- 不能跳状态直接赋值（必须走标品 opKey）

**实现点**：`HRBaseDataStatusOp` 维护（save 链第 4 位）

**`scene_doc.json` minefield**：🟡 yellow

---

## 二、加字段的级联影响（本场景 1 字段 = 2-3 表）

**⭐ 职位域核心认知**：给 `hbjm_jobhr` 加 1 字段，需要考虑字段类型决定的表数量：

| 字段类型 | 落地表数 | 备注 |
|---|---|---|
| 普通 TextField / BasedataField / DateField | 1 | `t_hbjm_job` 主表 |
| MuliLangTextField | 2 | 主表 id 引用 + `t_hbjm_job_i` 多语言子表 |
| MulBasedataField | 2 | 主表引用 + `t_hbjm_{key}mul` 隐式多选子表 |
| EntryEntity 子分录 | 2+ | 主表 + 独立分录子表 |

**无 4 前缀分录问题**（区别于行政组织场景）：
- ❌ 职位不走调整申请单，加字段不需要同步到 4 前缀分录
- ✅ 但如果加的字段要跨职位体系方案（jobscm）联动，需考虑多选子表

**如果字段需要联动下游**，还要：
- 下游字段一起加（推测：岗位、人员档案中关联职位的显示字段）
- 如需事件通知，新建类似 `CS-06` 的 `JobHrMsgHandleOp` 变种发布领域事件

→ **1 字段在职位域波及**：1-4 个包（主扩展 + 可选下游）

---

## 三、加分录的影响

### 典型场景：加"职位关键任务"分录

- 分录名：`${ISV_FLAG}_keytasks`
- 分录字段：`taskname`（任务名）/ `priority`（优先级）/ `owner`（负责人）

### 技术要点

- 加分录**不涉及** 4 前缀（本场景无调整申请单）
- 加分录**不自动同步**到时序版本链；如需版本化，需要在分录上也标 `boid` 并由 `HisModelOPCommonPlugin` 维护
- ⚠️ 时序模型分录**特别注意**：一个职位有多个 bsed 版本，每个版本的分录是独立的还是共享的？需要设计决策

### 推荐

- 分录只用于"职位自身的附属信息"（如关键任务列表）
- 不要把分录字段做跨模块联动（如让岗位直接读分录）—— 复杂度爆炸

---

## 四、修改插件的影响

### IF-10 · 覆盖 JobHrSaveOp

- **影响**：所有新建 / 修改 / 确认变更（save + confirmchange）走你的逻辑
- **风险**：🔴 高
- **标品风险**：
  - 丢失 `onPreparePropertys` 的字段声明（字段校验可能失效）
  - 丢失 `onAddValidators` 注册的内部校验
  - 丢失将来版本新增的职位保存业务
- **推荐**：**不要覆盖**，只注册并列插件（RowKey > 10）

### IF-11 · 覆盖 JobHrEnableOp

- **影响**：所有启用操作走你的逻辑
- **风险**：🔴 高
- **标品风险**：丢失 `JobEnableValidator`（已注册的标品校验器）
- **推荐**：继承 + super 调用，追加逻辑

### IF-12 · 覆盖 JobHrDisableOp

- **影响**：所有禁用操作
- **风险**：中
- **推荐**：继承 + super 调用（CS-04 加禁用前检查的标准做法）

### IF-13 · 覆盖 JobHrMsgHandleOp

- **影响**：所有时序版本变更的消息处理
- **风险**：🔴 极高
- **标品风险**：`sourcevid` 版本链维护错乱（`_auto_plugin_semantics.md` L18 写字段）
- **推荐**：**完全不覆盖**，新增并列插件处理自己的业务事件

### IF-14 · 覆盖 JobBaseBuListPlugin

- **影响**：列表的组织权限过滤
- **风险**：中
- **推荐**：继承 + super.filterContainerBeforeF7Select 调用 + 追加 filter

### IF-15 · 覆盖 BdVersionSaveServicePlugin (苍穹基础)

- **影响**：所有基础资料版本化
- **风险**：🔴 极高（影响全苍穹基础资料）
- **推荐**：**绝对不覆盖**

### IF-16 · 覆盖 HisModelOPCommonPlugin (HR 通用)

- **影响**：所有 HR 时序模型
- **风险**：🔴 极高（影响全 HR 时序基础资料）
- **推荐**：**绝对不覆盖**

---

## 五、改 opKey 配置的影响

### IF-20 · updateOperation(opKey=save, plugins=[...]) 全量替换

- **症状**：调后 save 链 10 标品插件全丢，职位保存走不通
- **风险**：🔴 极高
- **预防**：`先 get 再 append`（EX-21）
- **实现**：
  ```python
  current = client.get_operation(formId="hbjm_jobhr", opKey="save")
  current.plugins.append(my_plugin)
  client.update_operation(plugins=current.plugins)
  ```

### IF-21 · updateOperation(opKey=save, validations=[...]) 全量替换

- **症状**：调后 save 6 标品校验全丢，生效日期不校验、必填不校验
- **预防**：同上

### IF-22 · disable 某条标品 validation

- **场景**：业务想去掉 save 的 "生效日期不能填写未来"（`2M42D=Y0/0XK`）
- **做法**：`updateOperation(validations=[...新数组，把该 id 设 enabled=false])`
- **风险**：低（本来就是业务可选）

---

## 六、升级版本的影响

| 版本 | 破坏性变更（推断） | 适配建议 |
|---|---|---|
| 当前版本 | 10 插件保存链稳定 | - |
| 未来版本 | `JobHrSaveOp` 可能新增字段读 | 插件按接口扩展，不假设字段列表 |
| 未来版本 | `depcytype` 字段可能移除 | 新规则不要引用 |
| 未来版本 | 4 字段冗余名（`lowjoblevelname` 等）可能重构 | 自动由 `JobHrSaveOp` 内部同步，无需担心 |
| 未来版本 | 时序模型可能升级 | 不要覆盖 `HisModelOPCommonPlugin` |

---

## 七、影响面检查清单（做扩展前必过）

### 修改字段前
- [ ] 此字段 minefield 是什么颜色？（`scene_doc.json` 红 / 黄 / 无）
- [ ] `isvCanModify` 是 true 还是 false？
- [ ] 是否是时序字段（`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `sourcevid` / `hisversion`）？
- [ ] 是否是出厂字段（`orinumber` / `oriname` / `oristatus`）？
- [ ] 是否是已废弃字段（`depcytype`）？
- [ ] 此字段改名会破坏下游依赖吗？

### 加字段前
- [ ] 字段 key 有 ISV 前缀吗？（`${ISV_FLAG}_*` / `kingdee_*`）
- [ ] 字段 key ≤ 24 字符？
- [ ] 字段类型在 74 值枚举里吗？（`EmployeeField` 不在！）
- [ ] 如果是 Combo，`comboOptions` 准备了吗？
- [ ] 多语言字段会自动挂 `t_hbjm_job_i`，但要确认是否需要
- [ ] 是否影响招聘 / 岗位 / 人员档案下游？

### 加插件前
- [ ] 是覆盖标品还是新增？（优先新增）
- [ ] 是否调用 super？（所有 JobHr*Op 类都要 super）
- [ ] RowKey 顺序（CodeRuleOp 必须第 1 位，JobHrSaveOp 在最后）
- [ ] 异常处理用 `addErrorMessage` 而非 `throw`

### 改操作配置前
- [ ] `plugins` / `validations` 是全量替换吗？（答案：是）
- [ ] 是否先 get 再 append？（IF-20 / IF-21）

---

## 八、"最小变更"原则（强推）

做职位域定制时，按**影响最小**的顺序尝试：

```
优先级 1: 改编码规则基础资料配置（不动代码不动元数据）
   ↓ 不够用
优先级 2: 改 updateOperation.validations 的 enabled（UI 可切换）
   ↓ 不够用
优先级 3: 加扩展字段（只动自己的 ISV 空间）
   ↓ 不够用
优先级 4: 前端 propertyChanged 字段联动（纯 UI 逻辑）
   ↓ 不够用
优先级 5: 新增 Validator 并列注册（如 JobEnableValidator 模式）
   ↓ 不够用
优先级 6: 继承 JobHr*Op 重写特定方法（super 调用）
   ↓ 不够用
优先级 7: 覆盖 JobHr*Op（风险高，充分测试）
   ↓ 不够用
优先级 8: 覆盖 HR 通用插件（全 HR 炸）
   ↓ 极少用
优先级 9: 修改继承父模板 hbp_histimeseqtpl（⛔ 禁区）
```

---

## 九、影响面可视化

```
改 hbjm_jobhr.number
    ↓
┌─ t_hbjm_job.fnumber
├─ CodeRuleOp / CodeRuleDeleteOp 编码规则占用表
├─ 10 标品保存插件执行链
└─ 下游（推测）
    │
    v              v              v
  hbpm_position   hrpi_pemployee   报表 / BI

级联总数: 1 改 → 3 插件系统 + N 下游
```

```
禁用某个时序版本的职位
    ↓
JobHrDisableOp（无业务校验）
    ↓
业务风险: 下游仍在用 → 脏数据
    ↓
建议: 加 CS-04 禁用前检查
```

---

## 十、本场景与行政组织的影响面对比

| 维度 | hbjm_jobhr（本场景） | haos_adminorg（行政组织） |
|---|---|---|
| 1 字段扩展 | 1-2 个表（主 + 可选多语言） | 8 个包（主 + 4 前缀 + 信息组 + 权限） |
| 系统计算字段 | 仅时序字段（6 个） | 4 个（level/longnumber/structlongnumber/belongcompany） |
| 下游标品反写 | 默认 0 | 5 下游（hrpi/hbpm/pay/att/perf） |
| 业务插件数 | 5 个 JobHr*Op + JobHisBasedata* | 14 个 AdminOrg* |
| 4 前缀分录 | 无 | 有（VQ597FqFoc/7auphYEIJr/8bosVcKAfQ/wHBtyCCUik） |
| 事件订阅 | 推荐自建（CS-06） | 标品 `org_change_event` |

---

**📌 来源追溯**：
- minefield 颜色分类：`scene_doc.json` L31-L821 `minefield` 字段
- 10 插件链影响：`_auto_operations.md` L99-L113
- JobHrMsgHandleOp 读写：`_auto_plugin_semantics.md` L18-L20
- 全量替换陷阱：`knowledge/kb_cosmic_modifymeta_traps.md` EX-21
- 废弃字段：`scene_doc.json` L566 depcytype displayName 标注
