# 变更影响面 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 + scene_doc 跟下游 haos_changescene 引用关系实证
> **数据源**: refentity_reverse 反向引用 + haos_changescene 03_model_design 实证
> **confidence**: verified

---

## 一、改动本场景的影响维度

### 1.1 物理表层影响

| 改动类型 | 影响表 | 级联 |
|---|---|---|
| 加 ISV 字段（CS-01）| `t_haos_orgchangereason` 加列 | 无（仅本表）|
| 加 ISV 子表（CS-05）| 新建 `t_tdkw_*` 子表 | 无（仅本表）|
| 修改某条数据 name | `t_haos_orgchangereason_l` 多语言更新 | 下游 F7 标签自动更新（外键 id 不变 · 显示名跟随）|
| 修改 ctrlstrategy | 主表 + 标品 BdCtrlStrtgy 联动 createorg/useorg 可见性 | 表单层联动 · 不影响落地数据 |
| 禁用某条数据（disable）| `t_haos_orgchangereason` 改 fenable=0 + fdisablerid + fdisabledate | 下游 F7 不再选到此项 · 但已落地引用仍展示 |
| 删除某条数据（delete）⚠ | `t_haos_orgchangereason` + 多语言表 全部 DELETE | ⚠ 下游孤儿外键风险（详见 §3）|

### 1.2 业务流程层影响

| 改动类型 | 影响业务 |
|---|---|
| 加 ISV 字段 | 仅本表的录入页 + 列表展示 |
| 修改 name / description | haos_changescene 的 changereason F7 显示标签自动跟随 |
| 修改 ctrlstrategy | 影响数据的可见组织范围（业务上：哪些组织能用这条原因）|
| 禁用 / 删除 | 影响 haos_changescene 维护 + 间接影响 admin_org 域整个【组织变动】业务（详见 §3）|

---

## 二、下游影响清单（refentity_reverse 实证）

### 2.1 强引用：`haos_changescene.changereason`（直接路径）

> 数据源：`scenarios/haos_changescene/03_model_design.md` §3.7 双向印证

| 字段 | 类型 | 关系子表 | refEntity |
|---|---|---|---|
| `changereason` | MulBasedataField | `t_haos_cschangereason` | `haos_orgchangereason` |

**反向引用强度**：⭐⭐⭐⭐（每条 changescene 通常引用 1-3 个 changereason）

**直接 SQL 反向查询**：
```sql
-- 查 id=12345 被多少 changescene 引用
SELECT COUNT(*) FROM t_haos_cschangereason WHERE fbasedataid = 12345;
-- 或 ORM 等价（推荐）
HRBaseServiceHelper("haos_changescene").count(
  new QFilter("changereason.fbasedataid", "=", 12345L)
)
```

### 2.2 间接引用：通过 changescene → homs_orgbatchchgbill

> 引用链：haos_orgchangereason → t_haos_cschangereason.fbasedataid → t_haos_cschangereason.fid (= changescene.id) → 7 个 *_changescene 字段值

**这条引用链**：
- 删本场景某条 → t_haos_cschangereason 出现孤儿 fbasedataid
- 该 t_haos_cschangereason 关联的 changescene 仍正常 · 但 changescene 的 changereason 多选展示空
- 间接的 homs_orgbatchchgbill 7 entry 通过 changescene 引用 · 不直接受本场景删除影响

→ **CS-02 校验路径选择**：直查 `t_haos_cschangereason.fbasedataid` 即可 · 不需要"穿透 3 层"查 homs（性能 + 可解释性更好）。

### 2.3 间接引用：通过 changescene → haos_adminorgdetail

类似上述路径 · `haos_adminorgdetail.changescene` 字段间接通过 changescene → changereason 链路引用本表。
直接孤儿不发生在 adminorgdetail · 而是发生在 t_haos_cschangereason。

### 2.4 弱引用：其他

通过 `refentity_reverse.json` 走全仓查询 · 可能还有：
- ISV 自建报表字段（如客户跟踪表 · 直接查询 t_haos_orgchangereason.id）
- 数据权限规则（按 changereason 筛选数据 · 极少）

---

## 三、删除某条数据的级联影响（高危）

### 3.1 已落地数据的孤儿外键（标品 delete 后）

```
删除 haos_orgchangereason id=12345 · "战略调整"
   ↓
[t_haos_orgchangereason] DELETE WHERE fid=12345           ✅ 主表删除
[t_haos_orgchangereason_l] DELETE WHERE fid=12345         ✅ 多语言删除
   ↓
但下游引用 ↓ 标品没动 · 变成孤儿外键
[t_haos_cschangereason 多选关系子表]
   有 N 行 · fbasedataid=12345 · fid 指向多个 changescene
   → 这些行变孤儿 · F7 显示空
[haos_changescene 列表展示]
   changereason 列展示"战略调整"的位置 · 现在变空白
[haos_adminorgdetail / haos_adminorghis]
   通过 changescene 间接引用 · 业务报表"按变动原因统计"出错
```

### 3.2 防护：CS-02 加反向引用前置校验

CS-02 拦下"被引用过的不可删" · 走逻辑：

```
delete 操作 → onAddValidators 阶段
   ↓
ChangeReasonRefCheckOp 注册 ChangeReasonDeleteValidator
   ↓
Validator.validate() 遍历 dataEntities：
  ↓ 对每条 dataEntity (id=12345)
  if (issyspreset) → 拒绝（INV-CR-03）
  if (id == 1010L) → 拒绝（INV-CR-04 平台保留）
  HRBaseServiceHelper("haos_changescene").isExists(
    new QFilter("changereason.fbasedataid", "=", 12345)
  )
  → 命中 · addErrorMessage("已被变动场景引用 · 不可删")
```

→ **结论**：本场景**禁止 delete** · 必须先走 CS-02 校验 · 多数情况只能走 disable。

---

## 四、改动 changereason 字段值的影响（修改 vs 删除）

### 4.1 修改 name（多语言）— 安全

```
修改 haos_orgchangereason.name "战略调整" → "战略性调整"
   ↓
[t_haos_orgchangereason_l] UPDATE fname WHERE fid=12345
   ↓
所有下游 F7 显示自动更新（外键 id 不变 · 标签跟随）
```

✅ **无下游事故风险** · 多语言字段是显示用 · 不是业务逻辑用。

### 4.2 修改 ctrlstrategy — 中危

```
修改 haos_orgchangereason.ctrlstrategy = "5" (自由控制) → "7" (私有控制)
   ↓
[BdCtrlStrtgyShowLogicPlugin.propertyChanged] 联动 createorg/org/useorg
   ↓
保存：t_haos_orgchangereason.ctrlstrategy 更新 + 多组织字段调整
   ↓
影响：
  - 业务上"该原因变成只在 createorg 那个组织能用"
  - 已经在其他组织"用"该原因的 changescene 数据 · F7 选择列表里看不到了
  - 但已落地的引用关系（t_haos_cschangereason）保留
```

⚠ **业务影响**：从"宽控制 → 严控制"会缩小可见范围 · 业务方应明确知道。

### 4.3 修改 enable（disable / enable）— 低危

```
disable 操作：fenable 1→0
   ↓
下游 F7 不再选到此项（标品过滤 enable=1）
但已存在的引用都正常展示
```

✅ **业务推荐路径**：要淘汰一个变动原因 · 用 disable 而非 delete · 保留历史一致性。

### 4.4 修改 otclassify — 低危

```
修改 otclassify "1010 行政" → "1020 子集团"
   ↓
仅本表数据更新 · 下游不感知（因为下游通过 id 引用 · 不查 otclassify）
   ↓
影响：业务层级分类显示变化 · 但功能链路不受影响
```

✅ 多数情况安全 · 但业务上"重分类"可能让历史报表困惑（"为什么这条原因从行政变子集团了"）。

---

## 五、改动 ISV 扩展字段（CS-01 加的字段）的影响

| 改动 | 影响 |
|---|---|
| 加 ISV 字段（如 ${ISV_FLAG}_bizimpact）| 仅本表 · 下游不感知 |
| 修改 ISV 字段值 | 仅本表 · 下游 F7 选择列表不展示这些字段 |
| 删除 ISV 字段 | ⚠ 下游 ISV 报表 / 自建插件可能引用 · 删除前查全仓代码 |

→ **ISV 字段是隔离的安全空间** · 多数情况随便改不影响下游。

---

## 六、改动 ListPlugin 行为的影响

### 6.1 不要重写 setFilter（反指引）

```
ISV 重写 setFilter@haos_orgchangereason · 自定义 QFilter
   ↓
[标品 ChangeReasonListPlugin.setFilter 不再执行]
   → INV-CR-04 失效（id != 1010L 强制隐藏丢）
   → 用户能看到本不该看到的"内部模板项 1010" · 误操作风险
```

→ **后果**：业务用户能看到本不该看到的"模板项 1010" · 点击可能崩溃 / 影响数据一致性。

✅ **正确做法**：通过 customParam 或自建过滤控件 · 不重写 setFilter · 让标品继续跑。

---

## 七、跟 admin_org 域的整体影响图

```
[改 haos_orgchangereason]
   │
   ├─ disable / delete (本场景)
   │   ↓
   │   ↓ 下游 F7 选择列表 · 不再展示
   │   ↓
   │   └─→ haos_changescene (新建/编辑)：
   │       changereason 多选 F7 选不到 → 业务方填不到该原因 → 字典维护麻烦
   │
   └─ delete (本场景 · 已被引用过) ⚠
       ↓
       ↓ 历史孤儿外键
       ↓
       ├─→ t_haos_cschangereason 行游离（fbasedataid 指向已删 id）
       ├─→ haos_changescene 列表展示 changereason 列空
       └─→ 间接：admin_org 业务报表"按变动原因统计"列空
```

---

## 八、生产事故规避清单

| # | 风险动作 | 防 |
|---|---|---|
| 1 | 直接 delete 一条 changereason | 走 CS-02 拦 |
| 2 | 改 ctrlstrategy 缩小范围（5→7）影响下游可见 | 业务侧 SOP 提醒 + ISV 加 propertyChanged 警告 |
| 3 | 重写 ChangeReasonListPlugin.setFilter | 反指引 · 改用 customParam · 不动 setFilter |
| 4 | 在 ISV 插件里 set("issyspreset", ...) 改预置标记 | 靠 INV-CR-03 + 平台 isvCanModify=false 拦 · ISV 写也无效 |
| 5 | 标品升级覆盖 1010L 主键 ID | 跨环境查时通过 OpenAPI 实证 · 不硬编码 ID |
| 6 | ISV 加了字段后保存 → CodeRuleOp 没识别新字段 | 加字段走 CS-01 · 不动 CodeRule 配置 |
| 7 | ISV 自建 BEC 发布方 | CS-05 反指引 · 字典层不发 BEC |

---

## 九、生产事故案例参考（HR 域同类历史教训）

### 案例 A · 类比 admin_org 删除事故

> 来源：admin_org `08_impact_analysis.md` 类比 + haos_changescene `09_cases.md` 案例 2

某客户 HR 助理意外删了某条变动原因 · 标品没拦 · 结果：
- t_haos_cschangereason 出现 12 行 fbasedataid=已删 id 的孤儿数据
- 8 条历史 haos_changescene 的 changereason 多选展示出现空白
- 业务报表"按变动原因统计"出错 · 主管发现后紧急回滚（DBA 手工 INSERT 恢复主表 + 多语言表）

→ 修复方案：手工恢复主表数据 + 检查 t_haos_cschangereason 全量

**对应到本场景**：删一个 changereason 后果同样严重 · 8 条 changescene + N 张报表受影响 · CS-02 必装。

### 案例 B · 改 ctrlstrategy 误缩范围事故

某客户 HR 助理为了"权限收紧"把 changereason id=11 的 ctrlstrategy 从"5 自由控制"改成"7 私有控制" · 结果：
- 子公司用户在 changescene 维护时 · changereason 多选 F7 选不到该项
- 业务方反馈"系统坏了" · 排查发现是控制策略变更
- 修复方案：改回 5 · 沟通业务方 ctrlstrategy 的影响范围

**业务建议**：ctrlstrategy 是权限边界 · 改前要 review 已用情况。

### 案例 C · 跟 changescene 双字典不同步事故

某客户加了一条新 changereason "应急临时调整" · 但忘了去 haos_changescene 把对应场景"应急临时调整"的 changereason 多选追加该项 · 结果：
- 业务方在创建变动场景时找不到匹配的 reason
- 反复反馈"系统不全" · 让 ISV 排查
- 实际是双字典维护协作失误

→ **业务 SOP**：双字典维护要协同 · 加 reason 后立即检查关联 changescene 是否需要追加多选关系。

---

## 十、跟 haos_changescene 影响面对比

| 维度 | haos_orgchangereason（本场景）| haos_changescene |
|---|---|---|
| 直接下游 | 1 个（haos_changescene.changereason）| 8+ 个（adminorgdetail / adminorghis / homs 7 entry）|
| 删除影响 | t_haos_cschangereason 子表行游离 | 7 个 entry 字段 + adminorgdetail 全部游离 |
| 修改 name 安全度 | ⭐⭐⭐⭐⭐ 安全 | ⭐⭐⭐⭐⭐ 安全 |
| 修改 ctrlstrategy 危险度 | ⭐⭐ 中（缩可见范围）| 同 · 但 changescene ctrlstrategy 用得少 |
| 修改 otclassify 危险度 | ⭐ 低 | ⭐⭐⭐ 中（影响列表过滤）|
| CS-02 复杂度 | 单查 1 个字段（直路径）| 循环 7 个字段 + 跨表 |
| 需要 SOP 协议 | 是（双字典维护协同）| 是（同上 · 加 type 灵魂字段保护）|

→ **本场景影响面更窄** · 但业务铁律一致：禁删 / 慎改 ctrlstrategy / 用 disable 替代 delete。
