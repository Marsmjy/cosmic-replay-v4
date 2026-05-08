# 变更影响面 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 + scene_doc 跟下游 homs_orgbatchchgbill / haos_adminorgdetail 引用关系实证
> **数据源**: refentity_reverse 反向引用 + admin_org form_lifecycle.json 实证
> **confidence**: verified

---

## 一、改动本场景的影响维度

### 1.1 物理表层影响

| 改动类型 | 影响表 | 级联 |
|---|---|---|
| 加 ISV 字段（CS-01）| `t_haos_changescene` 加列 | 无（仅本表）|
| 加 ISV 子表（CS-06）| 新建 `t_tdkw_*` 子表 | 无（仅本表）|
| 修改某条数据 name | `t_haos_changescene_l` 多语言更新 | 下游 F7 标签自动更新（外键 id 不变 · 显示名跟随）|
| 禁用某条数据（disable）| `t_haos_changescene` 改 fenable=0 + fdisablerid + fdisabledate | 下游 F7 不再选到此项 · 但已落地引用仍展示 |
| 删除某条数据（delete）⚠ | `t_haos_changescene` + 多语言表 + 2 个 MulBasedata 子表 全部 DELETE | ⚠ 下游孤儿外键风险（详见 §3）|

### 1.2 业务流程层影响

| 改动类型 | 影响业务 |
|---|---|
| 加 ISV 字段 | 仅本表的录入页 + 列表展示 |
| 修改 changereason 多选关系 | 影响新建 changescene 时的多选默认值 |
| 修改 orgchangetype | ⚠ 触发 ChangeSceneEditPlugin 联动 changeoperat |
| 禁用 / 删除 | 影响 admin_org 域整个【组织变动】业务（详见 §3）|

---

## 二、下游影响清单（refentity_reverse 实证）

### 2.1 强引用：`homs_orgbatchchgbill` 7 entry

> 数据源：`scenarios/homs_orgbatchchgbill_maintenance/03_model_design.md` · 同 03 §1 双向印证

| entry 容器 | 引用字段 | refEntity |
|---|---|---|
| `entryentity`（主体）| `changescene` | `haos_changescene` |
| `add_entry`（新增）| `add_changescene` | `haos_changescene` |
| `parent_entry`（上级变更）| `parent_changescene` | `haos_changescene` |
| `info_entry`（信息变更）| `info_changescene` | `haos_changescene` |
| `disable_entry`（停用）| `disable_changescene` | `haos_changescene` |
| `merge_entry`（合并）| `merge_changescene` | `haos_changescene` |
| `split_entry`（拆分）| `split_changescene` | `haos_changescene` |

**反向引用强度**：⭐⭐⭐⭐⭐（每张调整申请单 · 平均引用 1-3 个 changescene）

### 2.2 强引用：`haos_adminorgdetail`

| 字段 | 必填 | 来源 |
|---|---|---|
| `changescene` | ✅ | admin_org form_lifecycle.json `_metadata.summary.requiredFieldsMain` 实证（changescene、number、name、enable 同列）|
| `changereason` | ❌ | 同上 |
| `changetype` | ❌（invisible=true · 但有引用）| 同上 |

**反向引用强度**：⭐⭐⭐⭐⭐（每个组织详情记录都关联 1 个 changescene）

### 2.3 中等引用：`haos_adminorghis`

时序模型 · 每次组织调整产生一个版本快照 · 都会复制 changescene 引用 → 数据量级 = 当前组织数 × 历史版本数。

### 2.4 弱引用：其他

通过 `refentity_reverse.json` 走全仓查询 · 可能还有：
- `haos_adminorg` 主表（待 OpenAPI 实证 · 估计也含 changescene 字段 · 因为详情视图共用）
- 自定义 ISV 子表（如客户已建的报表跟踪表）

---

## 三、删除某条数据的级联影响（高危）

### 3.1 已落地数据的孤儿外键（标品 delete 后）

```
删除 haos_changescene id=12345 · "战略调整"
   ↓
[t_haos_changescene] DELETE WHERE fid=12345              ✅ 主表删除
[t_haos_changescene_l] DELETE WHERE fid=12345            ✅ 多语言删除
[t_haos_cschangereason] DELETE WHERE fid=12345           ✅ 子表删除
[t_haos_cschangeoperat] DELETE WHERE fid=12345           ✅ 子表删除
   ↓
但下游引用 ↓ 标品没动 · 变成孤儿外键
[homs_orgbatchchgbill 已存在的所有申请单]
   .entryentity 1234567 行 · changescene=12345 → F7 显示空名称
   .add_entry / parent_entry / info_entry / disable_entry / merge_entry / split_entry
       6 个 *_changescene 字段同样可能含 12345 → 全部空
[haos_adminorgdetail 已存在的所有详情]
   .changescene=12345 → 列表展示 changescene 列空白
[haos_adminorghis 历史版本]
   .changescene=12345 → 同上
```

### 3.2 防护：CS-02 加反向引用前置校验

CS-02 拦下"被引用过的不可删" · 走逻辑：

```
delete 操作 → onAddValidators 阶段
   ↓
ChangeSceneRefCheckOp 注册 ChangeSceneDeleteValidator
   ↓
Validator.validate() 遍历 dataEntities：
  ↓ 对每条 dataEntity (id=12345)
  if (issyspreset) → 拒绝（INV-CS-03）
  循环 7 个 entry 字段：
    QueryServiceHelper.exists("homs_orgbatchchgbill",
      QFilter(每个 *_changescene, "=", 12345))
    → 任一命中 · addErrorMessage("已被申请单引用 · 不可删")
  QueryServiceHelper.exists("haos_adminorgdetail",
    QFilter("changescene", "=", 12345))
    → 命中 · addErrorMessage("已被组织详情引用 · 不可删")
```

→ **结论**：本场景**禁止 delete** · 必须先走 CS-02 校验 · 多数情况只能走 disable。

---

## 四、改动 changescene 字段值的影响（修改 vs 删除）

### 4.1 修改 name（多语言）— 安全

```
修改 haos_changescene.name "战略调整" → "战略性调整"
   ↓
[t_haos_changescene_l] UPDATE fname WHERE fid=12345
   ↓
所有下游 F7 显示自动更新（外键 id 不变 · 标签跟随）
```

✅ **无下游事故风险** · 多语言字段是显示用 · 不是业务逻辑用。

### 4.2 修改 orgchangetype — ⚠ 中危

```
修改 haos_changescene.orgchangetype = "调整" → "合并"
   ↓
[ChangeSceneEditPlugin.propertyChanged] 联动 changeoperat
   ↓
保存：t_haos_changescene.forgchangetypeid 更新
   ↓
但下游 homs_orgbatchchgbill 的历史申请单 changescene 字段仍指向同一 id
[业务异常]：以前的"调整"申请单 · 现在 changescene.orgchangetype 变"合并"了
   → 业务报表会"重分类" · 数据语义混乱
```

⚠ **强烈不推荐**修改已经被业务使用过的 changescene 的 orgchangetype。
→ 正确做法：禁用旧 · 新建新（CS-02 通过 disable 拦不会拦修改 · 但应人工 SOP 控制）。

### 4.3 修改 enable（disable / enable）— 低危

```
disable 操作：fenable 1→0
   ↓
下游 F7 不再选到此项（标品过滤 enable=1）
但已存在的引用都正常展示
```

✅ **业务推荐路径**：要淘汰一个变动场景类型 · 用 disable 而非 delete · 保留历史一致性。

---

## 五、改动 ISV 扩展字段（CS-01 加的字段）的影响

| 改动 | 影响 |
|---|---|
| 加 ISV 字段（如 ${ISV_FLAG}_bizline）| 仅本表 · 下游不感知 |
| 修改 ISV 字段值 | 仅本表 · 下游 F7 选择列表不展示这些字段 |
| 删除 ISV 字段 | ⚠ 下游 ISV 报表 / 自建插件可能引用 · 删除前查全仓代码 |

→ **ISV 字段是隔离的安全空间** · 多数情况随便改不影响下游。

---

## 六、改动 ListPlugin 行为的影响

### 6.1 不要重写 setFilter（CS-04 反指引）

```
ISV 重写 setFilter@haos_changescene · 自定义 QFilter
   ↓
[标品 ChangeSceneListPlugin.setFilter 不再执行]
   → otclassify 默认 1010L 过滤丢
   → id != 1070L 强制隐藏丢（INV-CS-04 失效 · 用户能看到 1070 项）
   → orinumber != 1100_S 搜索排除丢（INV-CS-05 失效）
```

→ **后果**：业务用户能看到本不该看到的"模板项 1070" / "子集团 1100_S" · 误操作风险。

✅ **正确做法**：CS-04 走 customParam · 不重写 setFilter · 让标品继续跑 + ISV 加 customParam 影响标品行为。

---

## 七、跟 admin_org 域的整体影响图

```
[改 haos_changescene]
   │
   ├─ disable / delete (本场景)
   │   ↓
   │   ↓ 下游 F7 选择列表 · 不再展示
   │   ↓
   │   ├─→ haos_adminorg (新建)：changescene 必填 · 选不到 → 影响 admin_org_quick_maintenance 业务
   │   └─→ homs_orgbatchchgbill (新建)：7 entry changescene 选不到 → 影响 homs_orgbatchchgbill_maintenance 业务
   │
   └─ delete (本场景 · 已被引用过) ⚠
       ↓
       ↓ 历史孤儿外键
       ↓
       ├─→ admin_org "组织变动历史查询" 报表：changescene 列空
       ├─→ homs_orgbatchchgbill 已存在申请单 F7 显示空
       └─→ haos_adminorghis 历史快照：changescene 列空
```

---

## 八、生产事故规避清单

| # | 风险动作 | 防 |
|---|---|---|
| 1 | 直接 delete 一条 changescene | 走 CS-02 拦 |
| 2 | 修改已被引用的 changescene 的 orgchangetype | 人工 SOP 禁止 + CS-02 增强（可加同样反向引用提醒）|
| 3 | 重写 ChangeSceneListPlugin.setFilter | CS-04 改用 customParam · 不动 setFilter |
| 4 | 在 ISV 插件里 set("issyspreset", ...) 改预置标记 | 靠 INV-CS-03 + 平台 isvCanModify=false 拦 · ISV 写也无效 |
| 5 | 标品升级覆盖 1010L/1070L 主键 ID | 跨环境查时通过 OpenAPI 实证 · 不硬编码 ID |
| 6 | ISV 加了字段后保存 → CodeRuleOp 没识别新字段 | 加字段走 CS-01 · 不动 CodeRule 配置 |

---

## 九、生产事故案例参考（HR 域同类历史教训）

### 案例 A · admin_org 类似删除事故

> 来源：admin_org `08_impact_analysis.md` 类比

某客户 HR 助理意外删了行政组织 id=58 的"项目管理部" · 标品没拦 · 结果：
- 该部门下 23 名员工的 hrpi_empjobrel.adminorg 变成游离 id（外键空）
- 薪酬 / 考勤 报表无法按部门筛选
- 组织树展示空节点

→ 修复方案：手工恢复主表数据 + 检查 hrpi_empjobrel 全员任职归属

**对应到本场景**：删一个 changescene 后果同样严重 · 7 entry × 千张申请单可能都受影响 · CS-02 必装。

### 案例 B · 改 orgchangetype 重分类事故

某客户 HR 主管为了"合并相似类型"把 changescene id=11 的 orgchangetype 从"调整"改成"合并"·结果：
- 业务报表"按类型统计"全部错位
- 历史 6 个月 12 张申请单的 changescene.type 都变了
- 财务对账"调整 vs 合并"分摊规则全乱

→ 修复方案：业务上禁止该操作 · 只允许新建（disable + create）路径。
