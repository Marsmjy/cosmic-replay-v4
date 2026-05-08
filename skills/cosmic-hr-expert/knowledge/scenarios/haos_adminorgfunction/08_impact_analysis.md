# 变更影响面 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于反编译 + scene_doc + haos_adminorg 直接引用关系实证
> **数据源**: refentity_reverse + haos_adminorg 03_model_design 双向印证
> **confidence**: verified

---

## 一、改动本场景的影响维度

### 1.1 物理表层影响

| 改动类型 | 影响表 | 级联 |
|---|---|---|
| 加 ISV 字段（CS-01）| `t_haos_adminorgfunction` 加列 | 无（仅本表）|
| 加 ISV 子表（CS-05）| 新建 `t_tdkw_*` 子表 | 无（仅本表）|
| 修改某条数据 name | `t_haos_adminorgfunction_l` 多语言更新 | 下游 F7 标签自动更新（外键 id 不变 · 显示名跟随）|
| 修改 ctrlstrategy | 主表 + 标品 BdCtrlStrtgy 联动 createorg/useorg 可见性 | 表单层联动 · 不影响落地数据 |
| 禁用某条数据（disable）| `t_haos_adminorgfunction` 改 fenable=0 + fdisablerid + fdisabledate | 下游 F7 不再选到此项 · 但已落地引用仍展示 |
| 删除某条数据（delete）⚠ | `t_haos_adminorgfunction` + 多语言表 全部 DELETE | ⚠ haos_adminorg 孤儿外键风险（详见 §3）|

### 1.2 业务流程层影响

| 改动类型 | 影响业务 |
|---|---|
| 加 ISV 字段 | 仅本表的录入页 + 列表展示 |
| 修改 name / description | haos_adminorg 列表和表单的 adminorgfunction F7 显示标签自动跟随 |
| 修改 ctrlstrategy | 影响数据的可见组织范围（哪些组织能用这条职能）|
| 禁用 / 删除 | 影响 haos_adminorg 维护 · 间接影响组织图谱与汇报体系展示（详见 §3）|

---

## 二、下游影响清单（refentity_reverse 实证）

### 2.1 强引用：`haos_adminorg.adminorgfunction`（直接路径 · 单选）

> 数据源：haos_adminorg 模型设计实证（BasedataField 单选 · 物理外键 `fadminorgfunctionid`）

| 字段 | 类型 | 存储路径 | refEntity |
|---|---|---|---|
| `adminorgfunction` | BasedataField（单选）| `t_haos_adminorg.fadminorgfunctionid` | `haos_adminorgfunction` |

**与 haos_orgchangereason 的核心差异**：
- haos_orgchangereason 被 MulBasedataField 多选引用（通过关系子表 `t_haos_cschangereason.fbasedataid`）
- 本场景被 **BasedataField 单选**引用（直接外键列 `fadminorgfunctionid`）
- 反向引用强度：**每条 haos_adminorg 至多有 1 个 adminorgfunction**（单选关系）

**反向引用强度**：⭐⭐⭐⭐⭐（每条行政组织记录直接引用本表 · 量级等于行政组织总数）

**直接 SQL 反向查询**（CS-02 场景）：
```sql
-- 查 id=12345 被多少行政组织引用（当前生效版本）
SELECT COUNT(*) FROM t_haos_adminorg
WHERE fadminorgfunctionid = 12345
  AND fiscurrentversion = 1;
-- 或 ORM 等价（推荐）
HRBaseServiceHelper("haos_adminorg").count(
  new QFilter("adminorgfunction", "=", 12345L)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
)
```

> ⚠ **HisModel 约束**：admin_org 是时序历史模型 · 必须加 `iscurrentversion=true` 过滤 · 否则历史版本行也会计入 · 导致误拦（跟 haos_orgchangereason CS-02 的关键差异）

### 2.2 间接引用：通过 haos_adminorg → haos_adminorghis

> 引用链：haos_adminorgfunction → t_haos_adminorg.fadminorgfunctionid → haos_adminorghis（admin_org 历史快照）

- 删本场景某条 → t_haos_adminorg 所有版本行（含历史）的 fadminorgfunctionid 孤儿
- haos_adminorghis 里保存的历史快照同样受影响
- 历史报表"按组织职能统计"出错

### 2.3 间接引用：通过 haos_adminorg → 员工任职关系

> 引用链：haos_adminorgfunction → haos_adminorg → hrpi_empjobrel.adminorg

- 员工任职关系 (hrpi_empjobrel) 通过 adminorg boid 关联 admin_org
- admin_org 上的 adminorgfunction 只是分类属性 · 不直接影响 hrpi_empjobrel 的数据完整性
- **间接影响**：BI 报表"按职能统计员工数"可能出错

### 2.4 弱引用：数据权限规则 / 业务报表

- ISV 自建报表字段（如按职能统计 admin_org 数量 · 直接 JOIN `t_haos_adminorgfunction`）
- 数据权限规则（按 adminorgfunction 筛选 admin_org 数据 · 极少但存在）

---

## 三、删除某条数据的级联影响（高危）

### 3.1 已落地数据的孤儿外键（标品 delete 后）

```
删除 haos_adminorgfunction id=12345 · "运营支撑"
   ↓
[t_haos_adminorgfunction] DELETE WHERE fid=12345           ✅ 主表删除
[t_haos_adminorgfunction_l] DELETE WHERE fid=12345         ✅ 多语言删除
   ↓
但下游引用 ↓ 标品没处理 · 变成孤儿外键
[t_haos_adminorg (当前生效版本)]
   有 N 行 · fadminorgfunctionid=12345 · 这些行的 adminorgfunction 单选字段 → 展示为空
[t_haos_adminorg (历史版本)]
   同样有 fadminorgfunctionid=12345 的历史行 → 历史快照里"职能"列展示为空
[haos_adminorg 列表展示]
   adminorgfunction 列展示"运营支撑"的位置 · 现在变空白
[组织职能汇总报表]
   "按职能统计" → "运营支撑"那行消失 · 那批 admin_org 无职能标签
```

### 3.2 防护：CS-02 加反向引用前置校验

CS-02 拦下"被引用过的不可删" · 走逻辑：

```
delete 操作 → onAddValidators 阶段
   ↓
AdminOrgFunctionRefCheckOp 注册 AdminOrgFunctionDeleteValidator
   ↓
Validator.validate() 遍历 dataEntities：
  ↓ 对每条 dataEntity (id=12345)
  if (issyspreset) → 拒绝（INV-AF-03/04 双保护）
  HRBaseServiceHelper("haos_adminorg").isExists(
    new QFilter("adminorgfunction", "=", 12345)
      .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
  )
  → 命中 · addErrorMessage("行政组织职能[运营支撑]已被行政组织当前版本引用·不可删除·建议改用禁用")
```

→ **结论**：本场景**禁止 delete** · 必须先走 CS-02 校验 · 多数情况只能走 disable。

---

## 四、改动字段值的影响（修改 vs 删除）

### 4.1 修改 name（多语言）— 安全

```
修改 haos_adminorgfunction.name "运营支撑" → "运营保障"
   ↓
[t_haos_adminorgfunction_l] UPDATE fname WHERE fid=12345
   ↓
所有下游 haos_adminorg 表单展示自动更新（外键 id 不变 · 标签跟随）
```

✅ **无下游事故风险** · 多语言字段是显示用 · 不是业务逻辑用。

### 4.2 修改 ctrlstrategy — 中危

```
修改 haos_adminorgfunction.ctrlstrategy = "5" (自由控制) → "7" (私有控制)
   ↓
[BdCtrlStrtgyShowLogicPlugin.propertyChanged] 联动 createorg/org/useorg
   ↓
保存：t_haos_adminorgfunction.ctrlstrategy 更新 + 多组织字段调整
   ↓
影响：
  - 业务上"该职能变成只在 createorg 那个组织能用"
  - 已经在其他组织"使用"该职能的 haos_adminorg 数据 · F7 选择列表里看不到了
  - 但已落地的 fadminorgfunctionid 外键保留
```

⚠ **业务影响**：从"宽控制 → 严控制"会缩小 adminorgfunction 可见范围 · 业务方应明确通知。

### 4.3 修改 enable（disable / enable）— 低危

```
disable 操作：fenable 1→0
   ↓
下游 haos_adminorg 新建时 F7 不再选到此项（标品过滤 enable=1）
但已存在的 fadminorgfunctionid 引用都正常展示
```

✅ **业务推荐路径**：要淘汰一个职能 · 用 disable 而非 delete · 保留历史数据一致性。

### 4.4 修改 ISV 字段（如 ${ISV_FLAG}_funccategory）— 安全

```
修改 haos_adminorgfunction.${ISV_FLAG}_funccategory "核心" → "创新"
   ↓
仅本表数据更新 · 下游 haos_adminorg 不感知（外键只引用 id · 不读 ISV 字段）
   ↓
影响：ISV 自建报表"按职能分类统计"展示改变 · 功能链路不受影响
```

✅ 多数情况安全 · 但业务上"重分类"可能让历史报表困惑。

---

## 五、改动 ISV 扩展字段的影响

| 改动 | 影响 |
|---|---|
| 加 ISV 字段（如 ${ISV_FLAG}_funccategory）| 仅本表 · 下游 haos_adminorg 不感知 |
| 修改 ISV 字段值 | 仅本表 · 下游 F7 单选列表不展示这些字段 |
| 删除 ISV 字段 | ⚠ 下游 ISV 报表 / 自建插件可能引用 · 删除前查全仓代码 |

→ **ISV 字段是隔离的安全空间** · 多数情况随便改不影响下游。

---

## 六、改动 ListPlugin 行为的影响

### 6.1 不要重写 setOrderBy（反指引）

```
ISV 重写 ListOrderCommonPlugin 或直接覆盖 setFilter 重排序
   ↓
[标品 ListOrderCommonPlugin 的 setOrderBy 失效]
   → 列表默认按主键或插入顺序展示（无序）
   → 业务方反馈"职能列表乱序" · 用户体验差
```

→ **后果**：列表无序 · 业务用户难以找到对应职能 · 容易误操作。
✅ **正确做法**：通过 customParam 或自建过滤控件 · 不重写 setFilter · 让标品继续跑排序逻辑。

### 6.2 本场景无"保留主键过滤"（与 haos_orgchangereason 的核心差异）

- haos_orgchangereason 列表有 `id != 1010L` 的强制隐藏 · 重写 setFilter 会泄漏内部模板项
- 本场景 **ListOrderCommonPlugin 只做 setOrderBy · 不做 setFilter** · 重写不存在"泄漏内部 id"风险
- 但重写仍会丢失排序效果 · 故同样不推荐

---

## 七、跟 haos_adminorg 域的整体影响图

```
[改 haos_adminorgfunction]
   │
   ├─ disable / delete (本场景)
   │   ↓
   │   ↓ 下游 F7 单选 · 不再展示
   │   ↓
   │   └─→ haos_adminorg (新建/编辑)：
   │       adminorgfunction 单选 F7 选不到 → 业务方填不上职能 → 组织分类缺失
   │
   └─ delete (本场景 · 已被引用过) ⚠
       ↓
       ↓ 历史孤儿外键（单选 · 直接外键列）
       ↓
       ├─→ t_haos_adminorg.fadminorgfunctionid 值有效但主表数据已删
       ├─→ haos_adminorg 列表 adminorgfunction 列展示空
       ├─→ haos_adminorghis 历史快照 adminorgfunction 展示空
       └─→ 间接：组织职能汇总报表"按职能统计"列空
```

---

## 八、生产事故规避清单

| # | 风险动作 | 防 |
|---|---|---|
| 1 | 直接 delete 一条 adminorgfunction | 走 CS-02 拦 |
| 2 | 改 ctrlstrategy 缩小范围（5→7）影响下游可见 | 业务侧 SOP 提醒 + ISV 加 propertyChanged 警告 |
| 3 | 重写 ListOrderCommonPlugin.setOrderBy 或覆盖 setFilter | 反指引 · 改用 customParam · 不动 setOrderBy |
| 4 | 在 ISV 插件里 set("issyspreset", ...) 改预置标记 | 靠 INV-AF-03/04 + 平台 isvCanModify=false 拦 · ISV 写也无效 |
| 5 | 反向引用查询没有加 iscurrentversion=true | admin_org HisModel 特性 · 历史版本误拦删除 · 必须加 |
| 6 | 用 `.fbasedataid` 路径反查（MulBasedata 多选的查法）| 错误路径 · 本场景是单选直字段查 |
| 7 | ISV 自建 BEC 发布方 | CS-05 反指引 · 字典层不发 BEC |

---

## 九、生产事故案例参考（HR 域同类历史教训）

### 案例 A · 类比 admin_org 删除孤儿外键事故

某客户 HR 助理意外删了"科研创新"这条职能 · 标品没拦 · 结果：
- t_haos_adminorg 当前版本有 23 行 fadminorgfunctionid 指向已删 id
- haos_adminorg 列表 adminorgfunction 列出现大量空白行
- 组织架构报表"按职能统计组织数量" · "科研创新"那列消失
- 业务方发现后紧急回滚（DBA 手工 INSERT 恢复主表 + 多语言表）

→ 修复方案：手工恢复主表数据 + 检查 t_haos_adminorg 全量
→ **对应到本场景**：CS-02 必装 · 加 iscurrentversion=true 过滤才准确

### 案例 B · 改 ctrlstrategy 误缩范围

某客户 HR 助理为"权限收紧"把"数字运营"职能的 ctrlstrategy 从"5 自由控制"改成"7 私有控制" · 结果：
- 子公司管理员在 haos_adminorg 维护时 adminorgfunction F7 选不到"数字运营"
- 业务方反馈"系统坏了" · 排查发现是控制策略变更
- 修复方案：改回 5 · 沟通业务方 ctrlstrategy 的影响范围

**业务建议**：ctrlstrategy 是权限边界 · 改前要 review 已用情况 · 下游 admin_org 有无跨组织使用该职能。

### 案例 C · 反向引用查询没加 iscurrentversion 误拦

某 ISV 开发参考 haos_orgchangereason 的 CS-02 · 直接抄了查询代码没加 iscurrentversion · 结果：
- 某条职能历史版本有引用 · 当前版本已不再引用（业务上已经迁移）
- CS-02 仍然拦下了 delete · 业务方说"这条职能明明没有组织用了还删不掉"
- 排查发现是历史版本数据干扰 · 修复：加 iscurrentversion=true 条件

→ **本场景比 haos_orgchangereason 多一个 HisModel 约束** · 移植时务必注意差异。

---

## 十、跟 haos_orgchangereason 影响面对比

| 维度 | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| 直接下游 | 1 个（haos_adminorg.adminorgfunction 单选）| 1 个（haos_changescene.changereason 多选）|
| 外键存储方式 | 直接外键列 `fadminorgfunctionid` | 关系子表 `t_haos_cschangereason.fbasedataid` |
| 删除影响 | t_haos_adminorg 行直接游离（无子表）| t_haos_cschangereason 子表行游离 |
| HisModel 约束 | **有**（admin_org 时序 · 反向查需 iscurrentversion）| 无（haos_changescene 不是时序）|
| 修改 name 安全度 | ⭐⭐⭐⭐⭐ 安全 | ⭐⭐⭐⭐⭐ 安全 |
| 修改 ctrlstrategy 危险度 | ⭐⭐ 中 | ⭐⭐ 中 |
| CS-02 复杂度 | 单查 1 字段 · 需加 iscurrentversion | 单查 1 字段（.fbasedataid 路径）|
| 下游引用量级 | 等于组织总数（每个组织 1 个职能）| 等于变动场景 × 原因映射数 |

→ **本场景影响面更广**（下游是 admin_org 主数据 · 而非 changescene 字典 · 量级更大） · 业务铁律一致：禁删 / 慎改 ctrlstrategy / 用 disable 替代 delete。

---

## 参考

- PR-001 · PR-006 · PR-007 · PR-010
- `06_customization_solutions.md` — CS-02 详细代码（含 iscurrentversion 实证）
- `07_ext_points.md` — 扩展点清单
