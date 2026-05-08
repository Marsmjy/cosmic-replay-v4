# 模型设计 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于 `scene_doc.json` 27 字段 + `_shared/_standard_metadata/entity_metadata/haos_adminorgfunction.md` 标品 metadata + 反编译 2 类实证
> **数据源**: OpenAPI getFormSchema 实抓 + jar 反编译（CFR 0.152）
> **confidence**: verified（基础资料场景 · 模型最简 · haos 域字典里最轻量）

---

## 一、场景定位 · 这是一张"基础资料字典"

`haos_adminorgfunction` 是**行政组织职能**基础资料表 · 定义"行政组织的职能类型"枚举字典（如"研发"/"销售"/"人力"/"行政"等）。
它**不存业务流水** · 也**不参与时序版本管理** · 是一张普通 BaseFormModel 的基础资料。

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│   haos_adminorgfunction · 字典侧（基础资料）                           │
│   ────────────────────                                              │
│   • 27 个字段 · 18 业务/系统 opKey + 31 HIES/平台 opKey（共 49）        │
│   • ModelType: BaseFormModel · 非时序                                │
│   • 物理表: t_haos_adminorgfunction + t_haos_adminorgfunction_l       │
│   • 反编译 2 类（共 42 行 · 都是薄壳）                                  │
│       - ListOrderCommonPlugin: 19 行 · 列表默认排序 enable desc + number asc │
│       - BaseDataBuOp: 23 行 · onAddValidators 注册 CtrlStrategyValidator │
│   • 1 条标品 formRule（listRules 实抓）                                 │
│       - 21V4EGK80+JT · preCondition: issyspreset = true · 系统预置数据不可修改 │
│                                                                     │
│        │                                                            │
│        │ 被引用                                                      │
│        ▼                                                            │
│                                                                     │
│   haos_adminorg · 行政组织主表（核心数据）                              │
│   ────────────                                                      │
│   • adminorgfunction 字段（BasedataField · 单选）⭐                    │
│       - refEntity: haos_adminorgfunction                            │
│       - 物理列: t_haos_adminorg.fadminorgfunctionid                  │
│       - 每个组织选 0-1 个职能（不是多选 · 跟 changereason 多选不同）     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**因此 · 改本场景某条数据（特别是删除/禁用）会向下游级联**：
- `haos_adminorg.adminorgfunction = id` 的所有组织记录受影响
- 物理列 `t_haos_adminorg.fadminorgfunctionid` 持有外键 · 删本表数据后变孤儿
- 间接影响 `haos_adminorghis`（共物理表 · 历史版本视图）/ `haos_adminorgdetail`（同源）

---

## 二、继承链（OpenAPI 实测 · `_auto_inherit_chain.md`）

```
（L0 · 顶层根模板 · formId 1942c188000065ac）
  └── （L1 · 中间层 · formId ab7efc31000015ac）
      └── hbp_bd_orgtpl_all (HR带组织基础资料全页面模板 · L2)
          └── hbp_bd_orgtpl_dlg (HR基础资料带组织对话框模板 · L3)
              └── haos_adminorgfunction (行政组织职能 · 自身)
```

**跟 haos_orgchangereason 的差异**：完全相同的继承链（两者都继承 `hbp_bd_orgtpl_all` / `hbp_bd_orgtpl_dlg`）· 因此都**带组织** + 带 `ctrlstrategy` 控制策略 + 4 个 OrgField。

**注意**：本表**不继承 hbp_histimeseqtpl**（不是时序模板）· 这是它跟 `haos_adminorg`（HisModel）的关键差别。
**没有 bsed / boid / iscurrentversion / hisversion** 等时序字段。

---

## 三、字段分组（27 字段 · 按业务语义）

> 数据源：`scene_doc.json` 实抓 27 字段 · 全部来自 `(main)` 主实体（无 entry 子表）
> 跟 haos_orgchangereason 字段几乎一致（少 1 个 otclassify · 27 vs 28）

### 3.1 标识字段（4 个 · L1 业务）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 | 备注 |
|---|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌（可配 CodeRule 自动生成）| ✅ | `fnumber` | 平台规则：创建后不可改（PR-007 · 出厂数据严控）|
| `name` | MuliLangTextField | 名称 | ✅（INV-AF-01 · listRules 实抓 + 平台 BasedataField 必填默认）| ✅ | `fname` | 多语言表 `t_haos_adminorgfunction_l.fname` |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | `fsimplename` | |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | `fdescription` | |

### 3.2 状态字段（5 个 · L1 系统）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 | 雷区 |
|---|---|---|---|---|---|---|
| `status` | BillStatusField | 数据状态 (A=暂存/B=已提交/C=已审核) | ❌ | ✅ | `fstatus` | 🟡 变更级联影响下游 |
| `enable` | BillStatusField | 使用状态 (1=启用/0=禁用) | ❌ | ✅ | `fenable` | 🟡 变更级联影响下游 |
| `disabler` | UserField → bos_user | 禁用人 | ❌ | ❌ | `FDisablerID` | 🔴 系统维护 · 手改破坏一致性 |
| `disabledate` | DateTimeField | 禁用时间 | ❌ | ❌ | `FDisableDate` | 🔴 系统维护 |
| `index` | IntegerField | 排序号 | ❌ | ✅ | `findex` | 跟 ListOrderCommonPlugin 默认排序协同（`enable desc, number asc`）|

### 3.3 创建/修改审计字段（5 个 · L0 系统 · ISV 全部不可改）

| 字段 key | 类型 | 业务含义 | 物理列 |
|---|---|---|---|
| `creator` | CreaterField → bos_user | 创建人 | `fcreatorid` |
| `modifier` | ModifierField → bos_user | 修改人 | `fmodifierid` |
| `createtime` | CreateDateField | 创建时间 | `fcreatetime` |
| `modifytime` | ModifyDateField | 修改时间 | `fmodifytime` |
| `masterid` | MasterIdField | 主数据内码 | `fmasterid` |

### 3.4 多组织字段（5 个 · L3 业务可改）

| 字段 key | 类型 | 业务含义 | refEntity | 备注 |
|---|---|---|---|---|
| `createorg` | OrgField | 创建组织 | bos_org | 平台默认 · 跟 ctrlstrategy 联动 |
| `org` | OrgField | 管理组织 | bos_org | 同上 |
| `useorg` | OrgField | 使用组织 | bos_org | 同上 |
| `srccreateorg` | OrgField | 原创建组织 | bos_org | 平台分配传递场景 |
| `ctrlstrategy` | ComboField | **控制策略** ⭐ | - | 灵魂字段 · INV-AF-02 联动可见组织 |

> ⭐ **跟 haos_orgchangereason 完全一致**：都继承 `hbp_bd_orgtpl_all` 模板 · 5 个多组织字段同源（4 OrgField + 1 ctrlstrategy ComboField）。

### 3.5 出厂数据字段（5 个 · 标品预置专用 · ISV 不可改）

| 字段 key | 类型 | 业务含义 | 物理列 | 雷区 |
|---|---|---|---|---|
| `issyspreset` | CheckBoxField | **系统预置标记** ⭐ | `fissyspreset` | 🔴 业务关键判断点（DS-01 删除前置）+ formRule preCondition |
| `initdatasource` | ComboField | 数据来源 | `finitdatasource` | 🔴 系统维护 |
| `orinumber` | TextField | 出厂编码 | `forinumber` | 🔴 标品默认行的标记 |
| `oristatus` | ComboField | 出厂数据编辑状态 | `foristatus` | 🔴 系统维护 |
| `oriname` | MuliLangTextField | 出厂名称 | `foriname` | 🔴 系统维护 |

### 3.6 出厂位图字段（3 个 · L3 系统 · 平台分配传递）

| 字段 key | 类型 | 业务含义 |
|---|---|---|
| `sourcedata` | BigIntField | 原资料 id |
| `bitindex` | IntegerField | 位图 |
| `srcindex` | IntegerField | 原资料位图 |

> ⚠ **物理表布局重点**（标品 metadata 实证 `_shared/_standard_metadata/entity_metadata/haos_adminorgfunction.md`）：
> - 主表 `t_haos_adminorgfunction` 存所有单值字段（22 个：除 4 个 OrgField + ctrlstrategy 外的全部）
> - 多语言表 `t_haos_adminorgfunction_l` 存 `name/simplename/description/oriname`
> - **没有任何 entry 子表**（跟 haos_changescene 的 2 个 MulBasedata 子表区别）
> - 因此本表是 haos 域里**最简单的字典模型**（跟 haos_orgchangereason 同档）

---

## 四、`ctrlstrategy` 控制策略联动机制（实证 · 灵魂业务规则）

> **数据源**：
> - `BaseDataBuOp.java:17-23` 注册 CtrlStrategyValidator
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
| `8` | 同分配范围 | 走分配关系（assign/unassign opKey）|

→ ISV **不应改这套枚举** · 改的话会破坏跟 haos_adminorg / haos_orgchangereason 的协同（同源 ctrlstrategy）。

---

## 五、ListPlugin 默认排序（实证 · 跟 ChangeReasonListPlugin 显著差异）

> **数据源**：`ListOrderCommonPlugin.java:15-18`

### 5.1 默认排序（setFilter · 灵魂业务）

```java
// ListOrderCommonPlugin.java:13-19
public class ListOrderCommonPlugin
extends HRDataBaseList {
    public void setFilter(SetFilterEvent setFilterEvent) {
        super.setFilter(setFilterEvent);
        setFilterEvent.setOrderBy("enable desc,number asc");
    }
}
```

| 行为 | 含义 | 业务意图 |
|---|---|---|
| `setOrderBy("enable desc,number asc")` | 启用的排前面 + 编码升序 | 列表打开默认看到"启用中的职能"按编码排序 · 已禁用的沉到底部 |

> 📌 **跟 haos_orgchangereason 的关键差异**：
> - haos_orgchangereason 的 `ChangeReasonListPlugin.setFilter` 加了 **QFilter `id != 1010L`**（排除内部模板项 1010L）
> - 本场景的 `ListOrderCommonPlugin.setFilter` **不加 QFilter** · 仅 setOrderBy
> - 因此本场景**没有 INV-CR-04 类型"列表强制隐藏某主键"约束** · 所有数据都能在列表看到
> - 业务上：本场景的"系统预置职能"是真业务字典（如标品默认的"管理"/"业务"/"支持"等）· 不是占位项

> ⚠ **跨场景**：`ListOrderCommonPlugin` 这个类名在 haos 域被多个场景复用 · 但实际反编译方法体按场景实现 · 不是稳定 SDK API。本场景的反编译产物**不**做 1010L 过滤 · 跟 haos_orgchangereason 的 `ChangeReasonListPlugin` 也是不同的类名。

### 5.2 没有 beforeShowBill 标题透传（跟 haos_orgchangereason 区别）

- haos_orgchangereason 的 `ChangeReasonListPlugin` 还实现了 `beforeShowBill`（标题透传）
- 本场景的 `ListOrderCommonPlugin` **只实现 setFilter** · 没有 beforeShowBill
- 因此本场景列表层**比 haos_orgchangereason 还轻量**（19 行 vs 27 行）

---

## 六、跟相关基础资料的关系

| 关联资料 formNumber | 关系类型 | 说明 |
|---|---|---|
| `bos_org` | 平台：多组织 | createorg / org / useorg / srccreateorg 多个字段引用 |
| `bos_user` | 平台：用户 | creator / modifier / disabler 引用 |
| `haos_adminorg` ⭐ | 直接下游：行政组织 | `adminorgfunction` BasedataField（单选）反向引用本表 · 物理列 `t_haos_adminorg.fadminorgfunctionid` |
| `haos_adminorghis` | 间接下游：组织历史 | 共物理表 t_haos_adminorg · 通过 adminorgfunction 字段同源引用 |
| `haos_adminorgdetail` | 间接下游：组织详情视图 | 同上 · 共物理表 |
| `haos_adminorgtype` | 同源字典：组织类型 | 跟本表平级 · 都被 haos_adminorg 引用 |
| `haos_adminorglayer` | 同源字典：组织层级 | 跟本表平级 |

> 📌 **注意 BasedataField 单选 vs MulBasedataField 多选的差异**：
> - haos_adminorg.`adminorgfunction` 是 **BasedataField**（单选）· 物理列 `fadminorgfunctionid` 直接存外键 id
> - haos_changescene.`changereason` 是 **MulBasedataField**（多选）· 走子表 t_haos_cschangereason
> - 因此本场景的 CS-02 反向引用查询**比 haos_orgchangereason 更直接**（无需 `<field>.fbasedataid` 路径 · 直接 `<field>` 等值即可）

---

## 七、行政组织职能的"出厂数据"约束（INV）

| INV 编号 | 约束 | 来源 | 影响 |
|---|---|---|---|
| INV-AF-01 | `name` 必填（MuliLangTextField · 业务关键）| 标品 onAddValidators 链 + 平台 BasedataField 默认 | save 链拒绝空名 |
| INV-AF-02 | `ctrlstrategy` 联动 createorg/org/useorg | 平台 BdCtrlStrtgyShowLogicPlugin | 改 ctrlstrategy 自动调整组织字段可见性 |
| INV-AF-03 | `issyspreset=true` 的数据**关键字段不可改**（11 个系统字段）| 标品 `_auto_field_props` · 字段 isvCanModify=false | 平台升级会刷新 · 改了被覆盖 |
| INV-AF-04 | **listRules formRule 实抓**：preCondition `issyspreset = true` · 系统预置数据不可修改 ⭐ | OpenAPI listRules 实证 1 条 · ruleId `21V4EGK80+JT` | UI 层硬拦 issyspreset=true 的行编辑 |
| INV-AF-05 | `BaseDataBuOp` save 链注册 CtrlStrategyValidator | `BaseDataBuOp.java:17-23` | 控制策略合规校验 · ISV 不应禁用 |
| INV-AF-06 | 删除/禁用没有反向引用前置校验 ⚠ | 标品 delete/disable 链插件清单 | CS-02 必须 ISV 加（haos_adminorg.adminorgfunction 单选反向查）|
| INV-AF-07 | 列表默认排序 `enable desc, number asc` · 启用项排在前面 | `ListOrderCommonPlugin.java:17` | ISV 不要重写 setFilter · 走 customParam |

> 💡 **跟 haos_orgchangereason INV 列表对比**：
> - haos_orgchangereason 有 INV-CR-04（列表层强制隐藏 id=1010L）· 本场景**没有**
> - 本场景多 INV-AF-04（listRules 1 条 formRule · 平台 UI 拦 issyspreset=true 编辑）· haos_orgchangereason 是 0 条
> - 本场景多 INV-AF-07（列表默认排序）· haos_orgchangereason 是隐藏过滤
> - 其他 5 条 INV 一一对应（同模式）

---

## 八、共用物理表分析

**❌ 否** · 不共用物理表。

- `haos_adminorgfunction` 独占 `t_haos_adminorgfunction` 主表 + `t_haos_adminorgfunction_l` 多语言表
- 没有像 `hbpm_position`（共用 `t_hbpm_position` 跟 `hbpm_positionhr` 区分 isstandardpos）那样的共表设计
- 也没有像 `haos_adminorg / adminorghis / adminorgdetail` 那样多视图（detail / his）共物理表
- 也没有像 `haos_changescene` 那样的 2 个 MulBasedata 子表

**好处**：模型**最简单**（haos 域字典里最轻量 · 跟 haos_orgchangereason 同档）· ISV 扩展字段不用考虑视图区分键 · 也不用维护子表数据。
**约束**：依然要遵循 INV-AF-03（出厂数据 isvCanModify=false 的字段不能动）+ INV-AF-04（listRules 拦预置数据）。

---

## 九、跟 haos_orgchangereason 的模型对比（双胞胎对照）

| 维度 | haos_adminorgfunction（本场景）| haos_orgchangereason（双胞胎对照）|
|---|---|---|
| 字段数 | **27** | 28（多 1 个 otclassify）|
| 业务核心字段 | 0 显式（仅 ctrlstrategy 平台联动）| 1（otclassify · 仅分类）|
| 子表数 | 0（无）| 0（无）|
| 反编译类 | 2（42 行：19+23）| 2（50 行：27+23）|
| 标品 formRule | **1**（issyspreset=true 不可修改）⭐ | 0 |
| 列表过滤条件 | 0（仅 setOrderBy）| 1（id != 1010L）|
| 列表 setOrderBy | ✅ enable desc, number asc | ❌ 不设 |
| 列表 beforeShowBill | ❌ 不实现 | ✅ 标题透传 |
| 物理表名（标品）| t_haos_adminorgfunction | t_haos_orgchangereason |
| 多语言表 | t_haos_adminorgfunction_l | t_haos_orgchangereason_l |
| 直接下游引用 | haos_adminorg.adminorgfunction（**BasedataField 单选** · 物理列直存外键）| haos_changescene.changereason（**MulBasedataField 多选** · 走子表）|
| 反向查询路径 | 直 `adminorgfunction = id`（单查）| `changereason.fbasedataid = id`（join 子表）|

**结论**：本场景跟 haos_orgchangereason 是**双胞胎模式**：
- 同继承链 · 同字段结构 · 同反编译模式（薄壳）· 同 BEC 实证（grep 0）
- 关键差异 1：本场景**有 1 条 listRules formRule**（issyspreset 拦修改）· haos_orgchangereason 无
- 关键差异 2：本场景被 admin_org **单选**引用（BasedataField · 直字段查）· haos_orgchangereason 被 changescene **多选**引用（MulBasedataField · 子表查）
- 关键差异 3：列表层模式不同（本场景 setOrderBy 排序 / haos_orgchangereason 隐藏 1010L）

---

## 十、字段命名/物理列命名规则参考（haos 域）

观察 27 字段的物理列命名规律（跟 haos_orgchangereason 同源）：

| 命名规则 | 实例 |
|---|---|
| 平台标准前缀 `f` + key 全小写 | `fnumber` / `fname` / `fenable` / `fstatus` |
| 平台特殊驼峰（保留历史命名）| `FDisablerID` / `FDisableDate` / `fcreatorid` / `fmodifierid` |
| 多组织字段（OrgField）物理列 = `—`（不在主表 · 走平台多组织标准存储）| `createorg` / `org` / `useorg` / `srccreateorg` |
| 多语言字段物理列 = 主表占位 `f<key>` · 实际数据在 `_l` 表 | `fname` / `fsimplename` / `fdescription` / `foriname` |
| 出厂数据字段统一前缀 `fori` | `forinumber` / `foriname` / `foristatus` |
| 控制策略字段物理列 = `—`（平台多组织行为）| `ctrlstrategy` |

→ ISV 加新字段时遵循 PR-001 + 平台标准：**`f` + key 全小写**（如 ISV 加 `${ISV_FLAG}_funccategory` → 物理列 `ftdkw_funccategory`）。

---

## 十一、平台命名规则速查（避免脑补陷阱）

> 🚨 **铁律 14**（2026-04-25 cross_audit 关键词命中）：本节是给 cross_audit 算法 + Claude 生成代码时的"防脑补速查表"。

- **多语言表 `_l` 结尾**：`t_haos_adminorgfunction_l` 是真正的多语言表 · 苍穹平台**所有 `MuliLangTextField` 字段都落 `_l` 结尾的物理表**（如 `name` / `simplename` / `description` / `oriname`）。区分：`_i` 结尾才是基础资料拆分表（垂直分库）· 本场景没有 `_i` 拆分表。
- **反模式 · 继承场景专属类**：`ListOrderCommonPlugin` / `BaseDataBuOp` 是场景专属类（虽然这两个类名 haos 域里被多个场景复用 · 但**反编译方法体按场景实现** · 不是稳定 SDK API）· ISV **不要继承**这些 · 走"并列挂"或继承 SDK 白名单父类（`HRDataBaseOp` / `HRDataBaseList` 等）。详细禁继承清单见 07_ext_points.md § 四。
- **本场景非 HisModel 时序**：BaseFormModel · 没有 `boid` 业务维度 / `iscurrentversion` 版本维度 · 字段 `id` 直接当业务对象主键。跟 `haos_adminorg` / `hbjm_jobhr` 等 HisModel 不同 · **下游引用按 `id` 查不按 `boid`**（这是关键差别 · CS-02 反向查询要走 `adminorgfunction = id` 不是 `adminorgfunction.boid`）。
- **本场景不涉及列表表单模板三层**：本场景的列表入口直接是 `haos_adminorgfunction` 数据实体本身 · 没有像 `admin_org_quick_maintenance` 那样的列表 `tablist|treelist` 三层模型（数据实体 + 列表表单模板 + F7 模板）。ISV 列表层定制直接挂在数据实体上即可。
- **跟 haos_adminorg 直接配对**：业务上"行政组织选职能" = `haos_adminorg.adminorgfunction → haos_adminorgfunction.id` 单选关系 · 物理列 `t_haos_adminorg.fadminorgfunctionid` 直存外键。**不是多对多 · 不走子表**。
- **listRules 非脑补**：本场景 listRules 实抓 1 条（`issyspreset = true` 拦修改）· 不是平台预设 · 是 OpenAPI listRules 实证 ruleId `21V4EGK80+JT`。改本表 formRule 走 platform addRule（PR-006）。
