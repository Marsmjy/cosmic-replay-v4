# 模型设计 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于 `scene_doc.json` + `_shared/_standard_metadata/entity_metadata/haos_orgchangereason.md` 标品 metadata + 反编译 2 类实证
> **数据源**: OpenAPI getFormSchema 实抓 + jar 反编译（CFR 0.152）
> **confidence**: verified (基础资料场景 · 模型简单清晰)

---

## 一、场景定位 · 这是一张"基础资料字典"

`haos_orgchangereason` 是**行政组织变动原因**基础资料表 · 定义"为什么要做组织变动"的枚举字典。
它**不存业务流水** · 也**不参与时序版本管理** · 是一张普通 BaseFormModel 的基础资料。

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│   haos_orgchangereason · 字典侧（基础资料）                            │
│   ─────────────────                                                 │
│   • 28 个字段 · 17 业务 opKey + 32 HIES/系统 opKey（共 49）           │
│   • ModelType: BaseFormModel · 非时序                                │
│   • 物理表: t_haos_orgchangereason + t_haos_orgchangereason_l         │
│   • 反编译 2 类（共 50 行 · 都是薄壳）                                  │
│       - ChangeReasonListPlugin: 27 行 · 列表硬编码隐藏 1010L           │
│       - BaseDataBuOp: 23 行 · onAddValidators 注册 CtrlStrategyValidator │
│                                                                     │
│        │                                                            │
│        │ 被引用                                                      │
│        ▼                                                            │
│                                                                     │
│   haos_changescene · 配对场景（基础资料）                              │
│   ─────────────────                                                 │
│   • changereason 字段（MulBasedataField · 多对多）                     │
│   • 多选关系子表 t_haos_cschangereason                                 │
│       - fbasedataid → haos_orgchangereason.id                       │
│                                                                     │
│        │                                                            │
│        │ 间接引用（通过 changescene）                                  │
│        ▼                                                            │
│                                                                     │
│   homs_orgbatchchgbill · 业务侧（调整申请单）                          │
│   ─────────────────                                                 │
│   • 7 entry × changescene 字段间接引用本表                            │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**因此 · 改本场景某条数据（特别是删除/禁用主键）会向下游级联**：
- `haos_changescene.changereason` 多选关系子表 t_haos_cschangereason 出现孤儿外键
- 通过 changescene → homs_orgbatchchgbill 7 entry 间接受影响

---

## 二、继承链（OpenAPI 实测 · `_auto_inherit_chain.md`）

```
（L0 · 顶层根模板）
  └── （L1 · 中间层）
      └── hbp_bd_orgtpl_all (HR带组织基础资料全页面模板)
          └── hbp_bd_orgtpl_dlg (HR基础资料带组织对话框模板)
              └── haos_orgchangereason (行政组织变动原因)
```

**对比配对场景 haos_changescene**：
- haos_changescene 继承自 `hbp_bd_tpl_all` (HR 基础资料全页面模板 · **不带组织**)
- haos_orgchangereason 继承自 `hbp_bd_orgtpl_all` (HR 带组织基础资料全页面模板 · **带组织**)

**差异**：本表带 `createorg` / `org` / `useorg` / `srccreateorg` 等 OrgField + `ctrlstrategy` 控制策略 · 多组织维度更完整。

**注意**：本表**不继承 hbp_histimeseqtpl**（不是时序模板）· 这是它跟 `haos_adminorg` 的关键差别。
**没有 bsed / boid / iscurrentversion / hisversion** 等时序字段。

---

## 三、字段分组（28 字段 · 按业务语义）

> 数据源：`scene_doc.json` 实抓 28 字段 · 全部来自 `(main)` 主实体（无 entry 子表）

### 3.1 标识字段（4 个 · L0/L1 系统 + 业务）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 | 备注 |
|---|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌（标品配 CodeRule 自动生成）| ✅ | `fnumber` | 苍穹平台规则：创建后不可改（PR-007 · 出厂数据严控）|
| `name` | MuliLangTextField | 名称 | ✅（INV-CR-01）| ✅ | `fname` | 多语言表 `t_haos_orgchangereason_l.fname` |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | `fsimplename` | |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | `fdescription` | |

### 3.2 状态字段（5 个 · L0/L1 系统）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 | 雷区 |
|---|---|---|---|---|---|---|
| `status` | BillStatusField | 数据状态 (A=暂存/B=已提交/C=已审核) | ❌ | ✅ | `fstatus` | 🟡 变更级联影响下游 |
| `enable` | BillStatusField | 使用状态 (1=启用/0=禁用) | ❌ | ✅ | `fenable` | 🟡 变更级联影响下游 |
| `disabler` | UserField → bos_user | 禁用人 | ❌ | ❌ | `FDisablerID` | 🔴 系统维护 · 手改破坏一致性 |
| `disabledate` | DateTimeField | 禁用时间 | ❌ | ❌ | `FDisableDate` | 🔴 系统维护 |
| `index` | IntegerField | 排序号 | ❌ | ✅ | `findex` | |

### 3.3 创建/修改审计字段（5 个 · L0 系统 · ISV 全部不可改）

| 字段 key | 类型 | 业务含义 | 物理列 |
|---|---|---|---|
| `creator` | CreaterField → bos_user | 创建人 | `fcreatorid` |
| `modifier` | ModifierField → bos_user | 修改人 | `fmodifierid` |
| `createtime` | CreateDateField | 创建时间 | `fcreatetime` |
| `modifytime` | ModifyDateField | 修改时间 | `fmodifytime` |
| `masterid` | MasterIdField | 主数据内码 | `fmasterid` |

### 3.4 多组织字段（5 个 · L3 业务可改 · 跟 haos_changescene 主要差异）

| 字段 key | 类型 | 业务含义 | refEntity | 备注 |
|---|---|---|---|---|
| `createorg` | OrgField | 创建组织 | bos_org | 平台默认 · 跟 ctrlstrategy 联动 |
| `org` | OrgField | 管理组织 | bos_org | 同上 |
| `useorg` | OrgField | 使用组织 | bos_org | 同上 |
| `srccreateorg` | OrgField | 原创建组织 | bos_org | 平台分配传递场景 |
| `ctrlstrategy` | ComboField | **控制策略** ⭐ | - | 灵魂字段 · INV-CR-02 联动可见组织 |

> ⭐ **跟 haos_changescene 的关键差异**：本表**有 ctrlstrategy 控制策略**（INV-CR-02）· haos_changescene 也有但通常不强调。本表继承 `hbp_bd_orgtpl_all`（带组织）模板 · 多组织能力更完整。

### 3.5 出厂数据字段（5 个 · 标品预置专用 · ISV 不可改）

| 字段 key | 类型 | 业务含义 | 物理列 | 雷区 |
|---|---|---|---|---|
| `issyspreset` | CheckBoxField | **系统预置标记** ⭐ | `fissyspreset` | 🔴 业务关键判断点（DS-01 删除前置） |
| `initdatasource` | ComboField | 数据来源 | `finitdatasource` | 🔴 系统维护 |
| `orinumber` | TextField | 出厂编码 | `forinumber` | 🔴 标品默认行（如 1010L）的标记 |
| `oristatus` | ComboField | 出厂数据编辑状态 | `foristatus` | 🔴 系统维护 |
| `oriname` | MuliLangTextField | 出厂名称 | `foriname` | 🔴 系统维护 |

### 3.6 出厂位图字段（4 个 · L3 系统）

| 字段 key | 类型 | 业务含义 |
|---|---|---|
| `sourcedata` | BigIntField | 原资料 id |
| `bitindex` | IntegerField | 位图 |
| `srcindex` | IntegerField | 原资料位图 |
| `srccreateorg` | OrgField → bos_org | 原创建组织 |

### 3.7 业务分类字段（1 个 · L3 业务可改）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 / refEntity | 备注 |
|---|---|---|---|---|---|---|
| `otclassify` | BasedataField | 组织团队分类 | ❌ | ✅ | `fotclassify` → `haos_otclassify` | 跟 haos_changescene 的 otclassify 字段同源 · 但本场景列表层**没有按 otclassify 默认过滤**（跟 ChangeSceneListPlugin 区别）|

> ⚠ **物理表布局重点**（标品 metadata 实证 `_shared/_standard_metadata/entity_metadata/haos_orgchangereason.md`）：
> - 主表 `t_haos_orgchangereason` 存所有单值字段（25 个）
> - 多语言表 `t_haos_orgchangereason_l` 存 `name/simplename/description/oriname`
> - **没有任何 entry 子表**（跟 haos_changescene 的 2 个 MulBasedata 子表区别）
> - 因此本表是 haos 域里**最简单的字典模型**

---

## 四、`ctrlstrategy` 控制策略联动机制（实证 · 灵魂业务规则）

> **数据源**：
> - `BaseDataBuOp.java:19-22` 注册 CtrlStrategyValidator
> - 标品 `BdCtrlStrtgyShowLogicPlugin` 表单层 + `BdCtrlStrtgyShowLogicListPlugin` 列表层 · 这两个是 bos 平台插件 · 不在本场景反编译产物里（`_auto_plugin_registry.md` 实证只列了类名）

### 4.1 调用链（双层校验）

```
[ 表单交互 ]                                [ 后端写入 ]
BdCtrlStrtgyShowLogicPlugin                BaseDataBuOp.onAddValidators
propertyChanged()                          (注册 CtrlStrategyValidator)
  ↓ 用户改 ctrlstrategy                       ↓ save 链上跑 Validator.validate
按枚举值（5/6/7 等控制级别）                  ↓
联动 createorg/org/useorg 可见性               校验 ctrlstrategy + createorg/useorg 组合合规性
                                            （如选了"分配范围控制"但 useorg 没填会报错）
```

> 💡 **设计意图**：基础资料的"管理组织 vs 使用组织"控制策略 · 决定本条数据被哪些组织看到、修改、使用。
> ctrlstrategy 是平台级抽象 · ISV 通常不动 · 跟 admin_org 域共享同一套语义。

### 4.2 业务语义（标品控制策略枚举）

| ctrlstrategy 值 | 含义 | 影响 |
|---|---|---|
| `5` | 同自由控制 | 所有组织都能看到 |
| `6` | 同管控范围 | 受 createorg + 管控范围限制 |
| `7` | 私有控制 | 仅 createorg 看到 |
| `8` | 同分配范围 | 走分配关系（assign opKey）|

→ ISV **不应改这套枚举** · 改的话会破坏跟 admin_org / homs 的协同。

---

## 五、ListPlugin 物理过滤（实证 · 灵魂业务规则）

> **数据源**：`ChangeReasonListPlugin.java:21`

### 5.1 默认过滤条件（1 条 · 硬编码）

```java
// ChangeReasonListPlugin.java:19-22 · setFilter()
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    setFilterEvent.getQFilters().add(new QFilter("id", "!=", (Object)1010L));
}
```

| 过滤条件 | 含义 | 业务意图 |
|---|---|---|
| `id != 1010L` | 排除主键 1010 那一条 | 标品保留 1010 主键作为某种"内部模板项 / 默认占位项" · 不让用户在列表点开 |

> 📌 **跟 haos_changescene 的对比**：
> - haos_changescene 列表过滤 3 条（otclassify 默认 1010 + id != 1070 + orinumber != "1100_S"）
> - haos_orgchangereason 列表过滤**仅 1 条**（id != 1010）
> - 本场景列表层**没有按 otclassify 默认过滤** · 因此用户在列表能看到所有分类的变动原因

> ⚠ **跨环境提示**：1010L 是标品出厂数据主键 · 多数环境一致 · 但 ISV 做下游引用时仍应通过 OpenAPI 查询确认（参考 `feedback_har_values_not_authoritative.md` · HAR 跨环境主键不可硬假设）。

### 5.2 标题透传（beforeShowBill · `ChangeReasonListPlugin.java:24-26`）

```java
public void beforeShowBill(BeforeShowBillFormEvent e) {
    e.getParameter().setCaption(this.getView().getFormShowParameter().getCaption());
}
```

→ 双击列表行打开详情 · 标题继承列表传过来的 caption（多场景同表单复用时支持自定义标题）。

跟 ChangeSceneListPlugin.beforeShowBill 是相同模式（haos 域同源）。

---

## 六、跟相关基础资料的关系

| 关联资料 formNumber | 关系类型 | 说明 |
|---|---|---|
| `haos_otclassify` | 上游：组织团队分类 | `otclassify` 字段引用（基础资料）|
| `bos_org` | 平台：多组织 | createorg / org / useorg / srccreateorg 多个字段引用 |
| `bos_user` | 平台：用户 | creator / modifier / disabler 引用 |
| `haos_changescene` | 配对：变动场景字典 ⭐ | changereason MulBasedataField 反向引用本表（多对多）· 物理子表 t_haos_cschangereason |
| `homs_orgbatchchgbill` | 间接下游：调整申请单 | 通过 changescene → changereason 链路引用 |
| `haos_adminorgdetail` | 间接下游：组织详情视图 | 通过 changescene 链路引用 |

---

## 七、变动原因的"出厂数据"约束（INV）

| INV 编号 | 约束 | 来源 | 影响 |
|---|---|---|---|
| INV-CR-01 | `name` 必填（MuliLangTextField · 业务关键）| 标品 onAddValidators 链 | save 链拒绝空名 |
| INV-CR-02 | `ctrlstrategy` 联动 createorg/org/useorg | 平台 BdCtrlStrtgyShowLogicPlugin | 改 ctrlstrategy 自动调整组织字段可见性 |
| INV-CR-03 | `issyspreset=true` 的数据**关键字段不可改**（11 个系统字段）| 标品 `_auto_field_props` · 字段 isvCanModify=false | 平台升级会刷新 · 改了被覆盖 |
| INV-CR-04 | `id != 1010L` 在列表层被强制隐藏 | `ChangeReasonListPlugin.java:21` | 该项在列表查不到 · 但通过 F7 仍能选 |
| INV-CR-05 | `BaseDataBuOp` save 链注册 CtrlStrategyValidator | `BaseDataBuOp.java:19-22` | 控制策略合规校验 · ISV 不应禁用 |
| INV-CR-06 | 删除/禁用没有反向引用前置校验 ⚠ | 标品 delete/disable 链插件清单 | CS-02 必须 ISV 加 |

---

## 八、共用物理表分析

**❌ 否** · 不共用物理表。

- `haos_orgchangereason` 独占 `t_haos_orgchangereason` 主表 + `t_haos_orgchangereason_l` 多语言表
- 没有像 `hbpm_position`（共用 `t_hbpm_position` 跟 `hbpm_positionhr` 区分 isstandardpos）那样的共表设计
- 也没有像 `haos_adminorg` 那样多视图（detail / his）共物理表
- 也没有像 `haos_changescene` 那样的 2 个 MulBasedata 子表

**好处**：模型**最简单**（haos 域字典里最轻量）· ISV 扩展字段不用考虑视图区分键 · 也不用维护子表数据。
**约束**：依然要遵循 INV-CR-03（出厂数据 isvCanModify=false 的字段不能动）。

---

## 九、跟 haos_changescene 的模型对比（双字典纪律）

| 维度 | haos_orgchangereason（本场景）| haos_changescene（配对）|
|---|---|---|
| 字段数 | 28 | 31 |
| 业务核心字段 | 1（otclassify · 仅分类）| 4（orgchangetype/otclassify/changereason/changeoperat 联动）|
| 子表数 | 0（无）| 2（t_haos_cschangereason · t_haos_cschangeoperat）|
| 反编译类 | 2（50 行）| 3（139 行）|
| 联动逻辑复杂度 | 仅 ctrlstrategy（标品平台级）| ctrlstrategy + orgchangetype→changeoperat（场景特有）|
| formRule | 0 | 3 |
| 列表过滤条件 | 1（id != 1010）| 3（otclassify + id != 1070 + orinumber != 1100_S）|
| 物理表名（标品）| t_haos_orgchangereason | t_haos_changescene |
| 多语言表 | t_haos_orgchangereason_l | t_haos_changescene_l |

**结论**：本场景比配对的 haos_changescene **更简单**：少了 1 个 MulBasedata + 没有 type→operat 联动 + 没有 formRule。
是 haos 域**最轻量的基础资料场景** · 适合作为 ISV 第一次试水扩展的练习场。

---

## 十、字段命名/物理列命名规则参考（haos 域）

观察 28 字段的物理列命名规律：

| 命名规则 | 实例 |
|---|---|
| 平台标准前缀 `f` + key 全小写 | `fnumber` / `fname` / `fenable` / `fstatus` |
| 平台特殊驼峰（保留历史命名）| `FDisablerID` / `FDisableDate` / `fcreatorid` / `fmodifierid` |
| 多组织字段（OrgField）物理列 = `—`（不在主表 · 走平台多组织标准存储）| `createorg` / `org` / `useorg` / `srccreateorg` |
| 多语言字段物理列 = 主表占位 `f<key>` · 实际数据在 `_l` 表 | `fname` / `fsimplename` / `fdescription` / `foriname` |
| 出厂数据字段统一前缀 `fori` | `forinumber` / `foriname` / `foristatus` |
| 控制策略字段物理列 = `—`（平台多组织行为）| `ctrlstrategy` |

→ ISV 加新字段时遵循 PR-001 + 平台标准：**`f` + key 全小写**（如 ISV 加 `${ISV_FLAG}_bizimpact` → 物理列 `ftdkw_bizimpact`）。

---

## 平台命名规则速查（避免脑补陷阱）

- **多语言表 `_l` 结尾**：`t_haos_orgchangereason_l` 是真正的多语言表 · 苍穹平台**所有 `MuliLangTextField` 字段都落 `_l` 结尾的物理表**（如 `name` / `simplename` / `description` / `oriname`）。区分：`_i` 结尾才是基础资料拆分表（垂直分库）· 本场景没有 `_i` 拆分表。
- **反模式 · 继承场景专属类**：`ChangeReasonListPlugin` / `BaseDataBuOp` 是场景专属类（虽然 BaseDataBuOp 是通用 BU OP · 不是本场景独占 · 但仍属"非 SDK 白名单父类"）· ISV **不要继承**这些 · 走"并列挂"或继承 SDK 白名单父类（`HRDataBaseOp` / `HRDataBaseList` 等）。
- **本场景非 HisModel 时序**：BaseFormModel · 没有 `boid` 业务维度 / `iscurrentversion` 版本维度 · 字段 `id` 直接当业务对象主键。跟 `haos_adminorg` / `hbjm_jobhr` 等 HisModel 不同 · 下游引用按 `id` 查不按 `boid`。
- **跟 haos_changescene 配对**：业务上常一起用（"变动场景=拆分组织" + "变动原因=战略调整"）· 但物理上独立 · 不共用物理表。
