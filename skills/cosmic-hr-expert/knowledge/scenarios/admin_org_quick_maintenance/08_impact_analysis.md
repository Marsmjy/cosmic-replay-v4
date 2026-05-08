# 变更影响面 · 行政组织快速维护

> **状态**: 🟢 基于 `adminorg_extension_pattern.md` 级联实证 + `anchors.md` 禁区
> **核心发现**: 1 字段 → 8 业务包联动

---

## 一、修改字段的影响

### IF-01 · 修改 haos_adminorg.number（组织编码）

**直接影响**:
- 下游所有引用此字段的插件（实测 **6 个标品插件**）
- 报表、外部系统集成点

**间接影响**:
- **pay 模块**: `pay_salary_archive.cost_center` 联动
- **attendance 模块**: `att_schedule.org_scope` 归属
- **reporting 模块**: 所有组织维度报表

**升级风险**: 升级到新版本时，任何改过 number 类型/长度的定制都会与标品冲突

**推荐做法**: **通过扩展字段实现自定义编码**，不要改标品 `number` 字段

---

### IF-02 · 修改 haos_adminorg.parentorg（上级）

**直接影响**: 组织树整体结构

**级联重算**:
- `level` 自动重算
- `longnumber` 自动重算
- `structlongnumber` 自动重算
- `belongcompany` 自动重算
- 所有后代组织的上述 4 字段也级联重算

**下游反写**（见 `05_data_flow.md`）:
- `hrpi_employee.org_path` (同步)
- `hbpm_position.adminorg` (同步)
- `pay_salary_archive.cost_center` (异步)
- `att_schedule.org_scope` (异步)

**推荐**: 仅通过 `onParentChange` 扩展，不直接修改

---

### IF-03 · 修改 haos_adminorg.status（启用/停用）

**影响**:
- 停用后不能作为新组织的上级
- 停用后不能挂新员工/岗位
- 停用组织下的旧数据保留
- **三态设计**: 使用中 → 待停用 → 已停用

**实现点**: `onStatusChange@haos_adminorg` 扩展点

---

### IF-04 · 修改 haos_adminorg.bsed（业务生效日期）

**影响**:
- 产生新的时间轴版本
- 历史版本自动归入 `haos_adminorghis`
- 下游取数取决于当前时间点的有效版本

**注意**: `bsed` 改成未来日期 → 组织进 `haos_adminorgdetailfuture` 视图（未生效）

---

## 二、加字段的级联影响（1 字段 = 5 份）

**⭐ 组织域核心认知**：给 `haos_adminorg` 加 1 字段，最终需要：

| 位置 | 份数 | 说明 |
|---|---|---|
| haos_adminorg 主表 | 1 | modifyMeta op=add field |
| 调整申请单 - 无前缀分录 | 1 | VQ597FqFoc 容器，字段 key = `<原key>` |
| 调整申请单 - info_ 分录 | 1 | 7auphYEIJr 容器，字段 key = `info_<原key>` |
| 调整申请单 - parent_ 分录 | 1 | 8bosVcKAfQ 容器，字段 key = `parent_<原key>` |
| 调整申请单 - add_ 分录 | 1 | wHBtyCCUik 容器，字段 key = `add_<原key>` |
| **合计** | **5** | - |

如果字段**需要联动下游**（如员工档案同步显示），还要：
- 权限配置 SQL (`T_HRCS_PERMRELAT`)
- 信息组 SQL (`T_HOM_INFOGROUP`)
- 下游业务包（6-8 个）同步扩展

→ **1 字段可能波及 10+ 个包**

---

## 三、加分录的影响

**加分录比加字段复杂**，因为：

- 分录本身不涉及 4 前缀（调整单不显示分录）
- 但分录内的字段如果要展示在员工档案/薪酬档案，**必须配 EmbedFormAp 或重新写前端**

**推荐**: 分录只用于"组织自身的附属信息"，不要把分录里的字段做跨模块联动（复杂度爆炸）

---

## 四、修改插件的影响

### IF-10 · 覆盖 OrgCodeValidatePlugin

- **影响**: 所有新建/修改组织走你的逻辑
- **风险**: 丢失编码格式标准校验（导致脏编码）
- **推荐**: 继承而非 implements，super 调用保留标品

### IF-11 · 覆盖 OrgPathRecalcPlugin

- **影响**: 所有 parentorg 变更走你的路径重算逻辑
- **风险**: 🔴 极高 - 算错会导致整棵树路径错乱
- **推荐**: **不要覆盖**，只继承后追加逻辑

### IF-12 · 改 OrgListStatusFilterPlugin

- **影响**: 列表默认过滤逻辑
- **风险**: 低 - 只影响显示，不改数据
- **推荐**: 继承 + 修改过滤条件

---

## 五、升级版本的影响

| 版本 | 破坏性变更 | 适配建议 |
|---|---|---|
| 2022R1 → 2023 | `OrgCodeValidatePlugin` 新增 | 检查覆盖是否丢失编码校验 |
| 2023 → 2024 | `SaveContext.getSource()` 新增 | 利用此字段区分 UI/批量来源 |
| 2024 → 2024R1 | `haos_adminorg` 新增 `cost_center` 字段 | 自定义列表需加此列 |
| 2024R1 → 2025 | `onParentChange` 新增 `batchMode` 参数 | 反射调用此方法的插件需适配 |
| 2025 | 异步 beforeSave 支持 | 性能优化可选升级 |

---

## 六、影响面检查清单（做扩展前必过）

### 修改字段前
- [ ] 此字段是否是系统计算字段？（level / longnumber / belongcompany / structlongnumber）
- [ ] 此字段是否被 `hrpi_employee` / `hbpm_position` / `pay_salary_archive` 引用？
- [ ] 此字段改名会破坏下游依赖吗？
- [ ] 此字段类型变化（如 String → Long）数据库迁移怎么做？

### 加字段前
- [ ] 字段 key 有 ISV 前缀吗？
- [ ] 字段 key ≤ 24 字符？
- [ ] 字段类型在 74 值枚举里吗？
- [ ] 如果是 Combo，comboOptions 准备了吗？
- [ ] 是否需要同步到调整申请单 4 前缀？
- [ ] 是否需要跨模块反写？

### 加插件前
- [ ] 是覆盖标品还是新增？
- [ ] 是否调用 super？
- [ ] order 是否合理（跟标品插件的顺序关系）？
- [ ] 异常处理是否完整？

### 改操作配置前
- [ ] `plugins` / `validations` 是全量替换吗？（答案：是）
- [ ] 是否先 get 再 append？

---

## 七、"最小变更"原则（强推）

做组织域定制时，按**影响最小**的顺序尝试：

```
优先级 1: 改配置（不动代码不动元数据）
   ↓ 不够用
优先级 2: 加扩展字段（只动自己的 ISV 空间）
   ↓ 不够用
优先级 3: 扩展运行时插件（beforeSave/afterSave/onXxx）
   ↓ 不够用
优先级 4: 覆盖标品插件（风险高，充分测试）
   ↓ 不够用
优先级 5: 修改标品字段（极高风险，升级兼容性差）
   ↓ 极少用
优先级 6: 修改系统计算字段（⛔ 禁区）
```

---

## 八、影响面可视化

```
    改 haos_adminorg.number
        ↓
    ┌─ 6 标品插件 ─┐
    │                │
    v                v
  薪酬模块      考勤模块      报表模块
    │                │              │
    v                v              v
员工成本中心     考勤组归属    组织维度报表

级联总数: 1 改 → 6+6+N 影响点
```

---

**📌 来源追溯**：
- 1 字段 → 8 包: `adminorg_extension_pattern.md`
- 禁区清单: `anchors.md`
- 版本变更: `openapi_capability_map.md` + `buildmeta_traps.md`
