# 模型设计 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于 `scene_doc.json` + `_shared/_standard_metadata/entity_metadata/haos_changescene.md` 标品 metadata + 反编译 3 类实证
> **数据源**: OpenAPI getFormSchema 实抓 + jar 反编译（CFR 0.152）
> **confidence**: verified (基础资料场景 · 模型简单清晰)

---

## 一、场景定位 · 这是一张"基础资料字典"

`haos_changescene` 是**行政组织变动场景**基础资料表 · 定义"组织变动有哪些类型"的枚举字典。
它**不存业务流水** · 也**不参与时序版本管理** · 是一张普通 BaseFormModel 的基础资料。

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│   haos_changescene · 字典侧（基础资料）                              │
│   ─────────────────                                                 │
│   • 31 个字段 · 18 业务 opKey + 31 HIES/系统 opKey（共 49）           │
│   • ModelType: BaseFormModel · 非时序                                │
│   • 物理表: t_haos_changescene + t_haos_changescene_l（多语言）       │
│   • 子表 2 张: t_haos_cschangereason / t_haos_cschangeoperat          │
│                                                                     │
│        │                                                            │
│        │ 被引用                                                      │
│        ▼                                                            │
│                                                                     │
│   homs_orgbatchchgbill · 业务侧（调整申请单）                          │
│   ─────────────────                                                 │
│   • 7 个 entry 容器全部引用本表的 changescene 字段                     │
│     - 主体 entry         · changescene                               │
│     - add_entry          · add_changescene                           │
│     - parent_entry       · parent_changescene                        │
│     - info_entry         · info_changescene                          │
│     - disable_entry      · disable_changescene                       │
│     - merge_entry        · merge_changescene                         │
│     - split_entry        · split_changescene                         │
│                                                                     │
│   haos_adminorgdetail · 数据视图（必填字段）                            │
│   ─────────────────                                                 │
│   • changescene 字段 required=true（来自 admin_org form_lifecycle.json）│
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**因此 · 改本场景某条数据（特别是删除/禁用主键）会向下游级联**：
- `homs_orgbatchchgbill` 的 7 entry 历史申请单 · 引用了被删除的 `changescene` → F7 显示空
- `haos_adminorgdetail` 新建/变更 → 必填的 changescene 选不到该项

---

## 二、继承链（OpenAPI 实测）

```
bos_basetpl (基础资料模板)
  └── hbp_bd_tpl_all (HR 基础资料全页面模板)
      └── haos_changescene (行政组织变动场景)
              └── 子表 t_haos_cschangereason · MulBasedataField changereason
              └── 子表 t_haos_cschangeoperat · MulBasedataField changeoperat
```

注意：本表**不继承 hbp_histimeseqtpl**（不是时序模板）· 这是它跟 `haos_adminorg` 的关键差别。
**没有 bsed / boid / iscurrentversion / hisversion** 等时序字段。

---

## 三、字段分组（31 字段 · 按业务语义）

> 数据源：`scene_doc.json` 实抓 31 字段 · 全部来自 `(main)` 主实体（无 entry 子表显式字段 · 子表通过 MulBasedataField 自动维护）

### 3.1 标识字段（4 个 · L0 系统）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 | 备注 |
|---|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌（标品配 CodeRule 自动生成） | ✅ | `fnumber` | 苍穹平台规则：创建后不可改（PR-007 但本表预置数据严控）|
| `name` | MuliLangTextField | 名称 | ❌（标品 onAddValidators 拦校验空名）| ✅ | `fname` | 多语言表 `t_haos_changescene_l.fname` |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | `fsimplename` | |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | `fdescription` | |

### 3.2 状态字段（5 个 · L0 系统）

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

### 3.4 多组织字段（4 个 · L3 业务可改）

| 字段 key | 类型 | 业务含义 | refEntity |
|---|---|---|---|
| `createorg` | OrgField | 创建组织 | bos_org |
| `org` | OrgField | 管理组织 | bos_org |
| `useorg` | OrgField | 使用组织 | bos_org |
| `ctrlstrategy` | ComboField | 控制策略 | - |

### 3.5 出厂数据字段（5 个 · 标品预置专用 · ISV 不可改）

| 字段 key | 类型 | 业务含义 | 物理列 | 雷区 |
|---|---|---|---|---|
| `issyspreset` | CheckBoxField | **系统预置标记** ⭐ | `fissyspreset` | 🔴 业务关键判断点（DS-01 删除前置） |
| `initdatasource` | ComboField | 数据来源 | `finitdatasource` | 🔴 系统维护 |
| `orinumber` | TextField | 出厂编码 | `forinumber` | 🔴 例如 `1100_S` 是子集团变动场景 · ListPlugin 默认过滤掉（实证）|
| `oristatus` | ComboField | 出厂数据编辑状态 | `foristatus` | 🔴 系统维护 |
| `oriname` | MuliLangTextField | 出厂名称 | `foriname` | 🔴 系统维护 |

### 3.6 出厂位图字段（4 个 · L3 系统）

| 字段 key | 类型 | 业务含义 |
|---|---|---|
| `sourcedata` | BigIntField | 原资料 id |
| `bitindex` | IntegerField | 位图 |
| `srcindex` | IntegerField | 原资料位图 |
| `srccreateorg` | OrgField → bos_org | 原创建组织 |

### 3.7 ⭐ 业务核心字段（4 个 · 全场景灵魂）

| 字段 key | 类型 | 业务含义 | 必填 | ISV 可改 | 物理列 / refEntity | 备注 |
|---|---|---|---|---|---|---|
| `orgchangetype` | BasedataField | **变动类型** ⭐ | ✅ | ❌ | `forgchangetypeid` → `haos_orgchangetype` | 灵魂字段 · 决定本场景属于哪种组织变动（新设/调整/合并/拆分/停用…）|
| `otclassify` | BasedataField | 组织团队分类 | ❌ | ✅ | `fotclassifyid` → `haos_otclassify` | ListPlugin 按此过滤（默认 1010=ADMINISTRATIVE）|
| `changereason` | MulBasedataField | **变动原因**（多选）| ❌ | ✅ | 子表 `t_haos_cschangereason` → `haos_orgchangereason` | 跟 `haos_orgchangereason` 配对 |
| `changeoperat` | MulBasedataField | **变动操作**（多选）| ❌ | ✅ | 子表 `t_haos_cschangeoperat` → `haos_orgchangeoperate` | 由 `orgchangetype` 联动反填（实证 ChangeSceneEditPlugin/ChangeSceneSaveOp）|

> ⚠ **物理表布局重点**：
> - 主表 `t_haos_changescene` 存 `orgchangetype` 等单值字段
> - 多语言表 `t_haos_changescene_l` 存 `name/simplename/description/oriname`
> - **`changereason` 和 `changeoperat` 是 MulBasedataField → 各自有独立的关联子表**（这跟普通 entry 不同 · 是平台 MulBasedata 标准模式）

---

## 四、`orgchangetype → changeoperat` 联动机制（实证 · 灵魂业务规则）

> **数据源**：
> - `ChangeSceneEditPlugin.java:21-36` 表单层 propertyChanged
> - `ChangeSceneSaveOp.java:36-56` OP 层 beginOperationTransaction
> - 两端调用同一服务 `ChangeSceneServiceHelper.getChangeOperate(Long typeId)`

### 4.1 调用链（双层兜底）

```
[ 表单交互 ]                                [ 后端写入 ]
ChangeSceneEditPlugin                       ChangeSceneSaveOp
propertyChanged()                           beginOperationTransaction()
  ↓ 用户改 orgchangetype                      ↓ 仅 importtype != null（导入路径）
ChangeSceneServiceHelper.getChangeOperate()  ChangeSceneServiceHelper.getChangeOperate()
  ↓                                            ↓
this.getModel().setValue("changeoperat",     dataEntity.getDynamicObjectCollection("changeoperat")
    new Object[]{changeOperateId})            .clear() → add(fbasedataid=changeOperateId)
```

> 💡 **设计意图**：UI 层只在用户改 `orgchangetype` 时联动 · 但 OP 层为了防"导入时只填了 type 没填 operat"做兜底（仅在 `importtype != null` 时执行 · 不影响普通保存路径）。

### 4.2 业务语义

每种 `orgchangetype`（如"新设"/"调整"/"合并"）→ 对应一组默认 `changeoperat`（如"创建"/"更新"/"合并节点"）。
该映射存储在 `ChangeSceneServiceHelper.getChangeOperate` 内部 · 是**标品硬编码逻辑** · ISV 不能改。

---

## 五、ListPlugin 物理过滤（实证 · 灵魂业务规则）

> **数据源**：`ChangeSceneListPlugin.java:21-43`

### 5.1 默认过滤条件（3 条 · 全部硬编码）

```java
// ChangeSceneListPlugin.java:23-32 · setFilter()
event.getQFilters().add(new QFilter("otclassify.id", "=", otclassifyid));
event.getQFilters().add(new QFilter("id", "!=", 1070L));
```

```java
// ChangeSceneListPlugin.java:41-43 · filterColumnSetFilter()
// 当前用户在搜索框选 orgchangetype.name 时
event.getCustomQFilters().add(new QFilter("orinumber", "!=", "1100_S"));
```

| 过滤条件 | 含义 | 业务意图 |
|---|---|---|
| `otclassify.id = ${customParam.otclassify || 1010L}` | 按组织团队分类（默认 1010=ADMINISTRATIVE 行政组织）| 不同组织类型看到不同变动场景 |
| `id != 1070L` | 排除主键 1070 那一条 | 标品硬编码隐藏（具体业务原因未注释 · 推测是某种"只读模板项"）|
| `orinumber != "1100_S"` | 搜索 orgchangetype 时排除子集团类 | 不让用户在普通搜索看到子集团专用项 |

> ⚠ **跨环境提示**：1010L / 1070L / "1100_S" 是标品出厂数据主键 · 多数环境一致 · 但 ISV 做下游引用时仍应通过 OpenAPI 查询确认（参考 `feedback_har_values_not_authoritative.md` · HAR 跨环境主键不可硬假设）。

---

## 六、跟相关基础资料的关系

| 关联资料 formNumber | 关系类型 | 说明 |
|---|---|---|
| `haos_orgchangetype` | 上游：变动类型字典 | `orgchangetype` 字段引用 · 决定本场景所属类型 |
| `haos_orgchangereason` | 配对：变动原因字典 | `changereason` 多选字段引用 · 业务上常一起用 |
| `haos_orgchangeoperate` | 配对：变动操作字典 | `changeoperat` 多选字段引用 · 由 type 联动反填 |
| `haos_otclassify` | 上游：组织团队分类 | `otclassify` 字段引用 · ListPlugin 用于物理过滤 |
| `bos_org` | 平台：多组织 | createorg / org / useorg / srccreateorg 多个字段引用 |
| `homs_orgbatchchgbill` | 下游：调整申请单 | 7 entry 全部引用 changescene · 强级联 |
| `haos_adminorgdetail` | 下游：组织详情视图 | changescene 字段必填（admin_org form_lifecycle.json:_metadata 实抓 required=true）|

---

## 七、变动场景的"出厂数据"约束（INV）

| INV 编号 | 约束 | 来源 | 影响 |
|---|---|---|---|
| INV-CS-01 | `issyspreset=true` 的数据**编码 / orgchangetype / orinumber 不可改** | 标品 `_auto_field_props` · 字段 isvCanModify=false | 平台升级会刷新 · 改了被覆盖 |
| INV-CS-02 | `orgchangetype` 必填（required=true · BasedataField）| `scene_doc.json` 实证 | save 链 onAddValidators 校验 |
| INV-CS-03 | `changeoperat` 由 `orgchangetype` 联动反填 | `ChangeSceneServiceHelper.getChangeOperate` | 直接改 changeoperat 而不改 type 是"反向操作" · UI 层不会拦但语义异常 |
| INV-CS-04 | `id != 1070L` 在列表层被强制隐藏 | `ChangeSceneListPlugin.java:32` | 该项在列表查不到 · 但通过 F7 仍能选（注意 F7 用 `bd_basedata` 列表 · 不走本插件）|
| INV-CS-05 | `orinumber = "1100_S"` 在搜索 orgchangetype 时被排除 | `ChangeSceneListPlugin.java:42` | 子集团类型搜索不到 |

---

## 八、共用物理表分析

**❌ 否** · 不共用物理表。
- `haos_changescene` 独占 `t_haos_changescene` 主表 + `t_haos_changescene_l` 多语言表 + 2 个 MulBasedata 子表
- 没有像 `hbpm_position`（共用 `t_hbpm_position` 跟 `hbpm_positionhr` 区分 isstandardpos）那样的共表设计
- 也没有像 `haos_adminorg` 那样多视图（detail / his）共物理表

**好处**：模型简单 · ISV 扩展字段不用考虑视图区分键。
**约束**：依然要遵循 INV-CS-01（出厂数据 isvCanModify=false 的字段不能动）。

---

## 九、平台命名规则速查（避免脑补陷阱）

- **多语言表 `_l` 结尾**：`t_haos_changescene_l` 是真正的多语言表 · 苍穹平台**所有 `MuliLangTextField` 字段都落 `_l` 结尾的物理表**。区分：`_i` 结尾才是基础资料拆分表（垂直分库）· 本场景没有 `_i` 拆分表。
- **反模式 · 继承场景专属类**：`ChangeSceneSaveOp` / `ChangeSceneEditPlugin` / `ChangeSceneListPlugin` 都是场景专属类（跟 hjm 的 `JobHrSaveOp` / hbpm 的 `PositionHisSaveOp` 同性质）· ISV **不要继承**这些 · 走"并列挂"或继承 SDK 白名单父类（`HRDataBaseOp` / `HRDataBaseEdit` 等）。
- **本场景非 HisModel 时序**：BaseFormModel · 没有 `boid` 业务维度 / `iscurrentversion` 版本维度的字段 · 字段 `id` 直接当业务对象主键。跟 `haos_adminorg` / `hbjm_jobhr` 等 HisModel 不同。
- **下游引用按 id 查不按 boid**：因本场景非 HisModel · `homs_orgbatchchgbill.add_changescene = changescene.id`（不是 boid）。
