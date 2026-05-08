# 模型设计 · 组织调整申请单

> **状态**: 🟢 基于 OpenAPI 实抓元数据 + jar 反编译实证整合
> **confidence**: verified
> **数据源**: `_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md` + `scene_doc.json` + `_auto_inherit_chain.md` + 反编译 `OrgBatchChgBillEffectOp.java` 等 8 类

---

## 一、核心定位（看清这个再读字段）

`homs_orgbatchchgbill` 是 **`BillFormModel` 类型的单据**，**不是基础资料**，跟 `haos_adminorg`（HisModel 时序基础资料）的差异巨大：

| 维度 | `homs_orgbatchchgbill`（本场景） | `haos_adminorg`（标杆对比） |
|---|---|---|
| ModelType | `BillFormModel` | `HisModel`（时序） |
| 表单基模板 | `hbp_hrbillorgtpl`（HR 带主组织单据基模板） | `hr_basedataorgtpl` |
| 是否多版本 | ❌ **不是**，无 `boid` / `iscurrentversion` / `hisversion` | ✅ 是，多版本共存 |
| 状态字段 | `billstatus`（A/B/C/F/G）+ `auditstatus` | `enable` + `status` |
| 写入对象 | 不直接写 `haos_adminorg` · 通过单据**生效**驱动 | 自身就是真表 |
| 物理表数 | 4 张（主表 + 分录表 + 2 多语言表） | 多张（含 _e/_l/_h 派生） |

⚠️ **关键认知**：本表里**没有** `boid` / `iscurrentversion` / `bsed`（生效起始日）/ `bsled`（生效终止日）这些时序字段。**`effdt`**（组织调整生效日期）只是单据字段，控制申请生效后下游 `haos_adminorg` 用哪个日期落版本号 · **它本身不让本单变成时序记录**。

来源：[`homs_orgbatchchgbill.md`](../../_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md) header `model_type: BillFormModel` + `_auto_inherit_chain.md` L17 实证 `hbp_hrbillorgtpl`。

---

## 二、4 张物理表关系图

```
┌─────────────────────────────────────────────────────────────────┐
│  申请单维度（一对多分录）                                          │
│                                                                 │
│  t_homs_orgchgbill (主表 22 列)                                  │
│   PK: fid                                                        │
│   核心列:                                                         │
│     fbillno         单据编号                                      │
│     fbillstatus     单据状态 (A/B/C/F/G)                          │
│     fauditstatus    审批状态                                      │
│     fauditorid      审核人                                        │
│     fauditdate      审核日期                                      │
│     forgid          组织体系管理组织 (BU)                          │
│     feffdt          组织调整生效日期                              │
│     fdispatchnumber 发文编号                                      │
│     fistodiagram    是否查看组织架构图                            │
│                                                                 │
│   ⤵️ 1:N                                                          │
│                                                                 │
│  t_homs_orgchgbillentry (分录表 20 列)                           │
│   PK: fentryid                                                   │
│   FK: fid → t_homs_orgchgbill.fid                                │
│   逻辑分录归属字段: fchangetypeid → haos_orgchangetype（区分新增/  │
│     上级/信息/停用/合并/拆分 6 大类操作 + 详情）                    │
│   核心列:                                                         │
│     fadminorgid     被操作的行政组织                               │
│     fchangetypeid   变动类型（区分 6 entry）                       │
│     fchangesceneid  变动场景                                      │
│     fchangereasonid 变动原因                                      │
│     fnumber         调整后 / 新增 编码                             │
│     foriparentorgid 原上级（仅 parent entry 用）                   │
│     fparentorgid    新上级（仅 parent / add entry 用）             │
│                                                                 │
│  t_homs_orgchgbill_l (主表多语言 3 列)                            │
│   FK: fid → t_homs_orgchgbill.fid                                │
│   fdescription / fdispatchname                                   │
│                                                                 │
│  t_homs_orgchgbillentry_l (分录多语言 2 列)                       │
│   FK: fentryid → t_homs_orgchgbillentry.fentryid                 │
│   fname / fsimplename                                            │
└─────────────────────────────────────────────────────────────────┘
```

**关键发现 1 · 7 个逻辑 entryentity 共用同一张物理分录表**

`scene_doc.json` 列出 `entryentity_add` / `entryentity_parent` / `entryentity_info` / `entryentity_disable` / `entryentity_merge` / `entryentity_split` / `entryentity_all` 7 个逻辑分录 · 但 `_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md` 显示**物理表只有 1 张** `t_homs_orgchgbillentry`。

→ 7 个逻辑分录在 XML 元数据层是**视图分录**（基于 `changetype` 字段过滤同一物理表），跟 admin_org 实证 `entryentity_*_org` 的"4 前缀分录共用一张表"模式相同（参考 admin_org `_metadata_rules_form.json`）。

**关键发现 2 · 真正存数据的分录代码层叫 `homs_batchorgentity`**

反编译 `OrgBatchBillSaveOp.java` L57：
```java
HRBaseServiceHelper batchOrgEntityHelper = new HRBaseServiceHelper("homs_batchorgentity");
QFilter adminOrgIdFilter = new QFilter("creator", "=", (Object)0);
QFilter billIdFilter = new QFilter("billid", "=", (Object)billId);
batchOrgEntityHelper.deleteByFilter(adminOrgIdFilter.toArray());
```

→ 后端 OP 直接通过 `homs_batchorgentity` 实体名访问分录数据，不用 `entryentity_xxx`。这是**数据物理映射层**，UI 操作走的是 `entryentity_add` 等 7 个逻辑视图。

---

## 三、152 字段按 entry 分组（核心摘要）

> 完整 152 字段见 [`scene_doc.json`](scene_doc.json) 与 [`_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md`](../../_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md)

### 主表（21 字段 · 单据级）

| 字段 key | 类型 | 物理列 | 必填 | 业务含义 | ISV 可改 |
|---|---|---|---|---|---|
| `billno` | BillNoField | t_homs_orgchgbill.fbillno | ✓ | 单据编号 · 由编码规则插件生成 | ❌ 系统 |
| `billstatus` | BillStatusField | t_homs_orgchgbill.fbillstatus | ✓ | 单据状态（A/B/C/F/G 状态机 · 见 02） | ❌ 系统 |
| `auditstatus` | BillStatusField | t_homs_orgchgbill.fauditstatus | ✓ | 审批状态 | ❌ 系统 |
| `auditor` | UserField | t_homs_orgchgbill.fauditorid |  | 审核人 | ✅ 业务 |
| `auditdate` | DateTimeField | t_homs_orgchgbill.fauditdate |  | 审核日期 | ✅ 业务 |
| `creator` | CreaterField | t_homs_orgchgbill.fcreatorid |  | 创建人 | ❌ 平台 |
| `modifier` | ModifierField | t_homs_orgchgbill.fmodifierid |  | 修改人 | ❌ 平台 |
| `createtime` | CreateDateField | t_homs_orgchgbill.fcreatetime |  | 创建时间 | ❌ 平台 |
| `modifytime` | ModifyDateField | t_homs_orgchgbill.fmodifytime |  | 修改时间 | ❌ 平台 |
| `org` | OrgField | t_homs_orgchgbill.forgid | ✓ | 组织体系管理组织（BU） · 申请单挂在哪个 BU 下 | ❌ 系统 |
| `effdt` | DateField | t_homs_orgchgbill.feffdt | ✓ | 组织调整生效日期（驱动下游 `haos_adminorg` 版本日期） | ✅ 业务 |
| `dispatchnumber` | TextField | t_homs_orgchgbill.fdispatchnumber |  | 发文编号 | ✅ 业务 |
| `dispatchname` | MuliLangTextField | t_homs_orgchgbill_l.fdispatchname |  | 发文名称 | ✅ 业务 |
| `description` | MuliLangTextField | t_homs_orgchgbill_l.fdescription |  | 描述 | ✅ 业务 |
| `disorg` | HRMulAdminOrgField | （多选字段） |  | 签发组织 | ✅ 业务 |
| `istodiagram` | CheckBoxField | t_homs_orgchgbill.fistodiagram |  | 是否查看组织架构图 | ✅ 业务 |
| `isexistsworkflow` | CheckBoxField | t_homs_orgchgbill.fisexistsworkflow |  | 是否存在工作流 | ❌ 系统 |
| `barcode` | TextField | — |  | 条形码（HIES 注入） | ✅ 业务 |
| `inputdevicetype` | TextField | — |  | 输入设备（HIES 注入） | ✅ 业务 |
| `eventeffectdate` | DateTimeField | t_homs_orgchgbill.feventeffectdate |  | 事务生效日期（**已废弃** · 不要用） | ❌ 废弃 |
| `issubmit` | CheckBoxField | t_homs_orgchgbill.fissubmit |  | 是否进行过提交（**已废弃** · 不要用） | ❌ 废弃 |

### `entryentity_add` 新增组织分录（25 字段 · 前缀 `add_`）

最关键 12 字段（标 ✓ 必填）：

| 字段 key | 类型 | 必填 | 引用 | 业务含义 |
|---|---|---|---|---|
| `add_adminorgtype` | BasedataField | ✓ | haos_adminorgtype | 行政组织类型 |
| `add_changescene` | BasedataField | ✓ | haos_changescene | 变动场景（关联 changetype 过滤） |
| `add_number` | TextField |  |  | 编码（**为空时系统按编码规则自动生成** · 见 OrgBatchBillSubmitAndEffectiveOp L75） |
| `add_name` | MuliLangTextField | ✓ |  | 行政组织名称 |
| `add_parentorg` | HRAdminOrgField | ✓ | haos_adminorghrf7 | 上级行政组织 |
| `add_org` | OrgField | ✓ | bos_org | 组织体系管理组织 |
| `add_corporateorg` | BasedataField |  | hbss_lawentity | 法律实体 |
| `add_adminorglayer` | BasedataField |  | haos_adminorglayer | 管理层级 |
| `add_adminorgfunction` | BasedataField |  | haos_adminorgfunction | 行政组织职能 |
| `add_changetype` | BasedataField |  | haos_orgchangetype | 变动类型（区分 6 大 entry） |
| `add_changereason` | BasedataField |  | haos_orgchangereason | 变动原因 |
| `add_adminorg` | BigIntField |  |  | 反向引用（生效后才有值，写到 haos_adminorg.id） |

### `entryentity_parent` 调整上级分录（28 字段 · 前缀 `parent_`）

特有 7 字段：

| 字段 key | 类型 | 必填 | 业务含义 |
|---|---|---|---|
| `parent_adminorg` | HRAdminOrgField | ✓ | 被调整的组织 |
| `parent_oriparentorg` | HRAdminOrgField |  | 原上级行政组织 |
| `parent_oriparentorg_name` | TextAreaField |  | 原上级行政组织全称（脱机展示用） |
| `parent_parentorg` | BasedataField | ✓ | 调整后上级行政组织 |
| `parent_parentorg_name` | MuliLangTextField |  | 调整后上级行政组织全称 |
| `parent_number` | TextField | ✓ | 调整后行政组织编码（**调上级可能改编码**） |
| `parent_tobedisableflag` | CheckBoxField |  | 待停用标记（联动子级处理） |

### `entryentity_info` 信息变更分录（27 字段 · 前缀 `info_`）

特有：`info_adminorg`（被调整的组织 · ✓）+ `info_name`/`info_simplename`/`info_number`/`info_adminorgtype` 等所有可变更字段 · 等同于"复制 add 分录字段，但 adminorg 必填指向已有组织"。

### `entryentity_disable` 停用组织分录（10 字段 · 前缀 `disable_`）

| 字段 key | 类型 | 必填 | 业务含义 |
|---|---|---|---|
| `disable_adminorg` | HRAdminOrgField | ✓ | 要停用的组织 |
| `disable_changescene` | BasedataField | ✓ | 变动场景 |
| `disable_changetype` | BasedataField | ✓ | 变动类型 |
| `disable_changereason` | BasedataField |  | 变动原因 |
| `disable_changedescription` | TextField |  | 变动说明 |
| `disable_tobedisableflag` | CheckBoxField |  | 已待停用 |
| `disable_org` | OrgField | ✓ | 组织体系管理组织 |
| `disable_parentorg` | BasedataField |  | 上级行政组织（脱机展示） |
| `disable_oriparentorg_name` | TextAreaField |  | 原上级全称 |
| `disable_adminorgtype` | BasedataField |  | 行政组织类型 |

### `entryentity_merge` 组织合并分录（特殊 · 多字段子分录）

`entryentity_merge` 自身约 8 字段 + 其下嵌套 `to_merge_org` 多基础资料分录（被合并的组织列表 · 多对一）+ `merge_target_org`（合并目标组织 · 一对一）。

合并的"反向追溯"字段：
- `aftermergeorgid`（合并后组织 id · 用于列表 mergecount 聚合 · 见 `AdminOrgBatchBillListPlugin.java` L124）
- `mergecount` 列表显示用聚合字段（按 `aftermergeorgid distinct count` · L168）

### `entryentity_split` 组织拆分分录（特殊 · 镜像 merge）

`entryentity_split` 自身字段 + `split_target_org`（拆分后多组织 · 多对一）+ `to_split_org`（被拆分的源组织 · 一对一）。

拆分追溯：`beforesplitorgid` + `splitcount`（同样按 `beforesplitorgid distinct count` · L170）。

### `entryentity_all` 所有分录视图（**只读视图** · 不要写）

`entryentity_all` 是只读视图（看 `_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md` L177-200 列出的字段无前缀 · 物理列指向 `t_homs_orgchgbillentry` 同一物理表）。

⚠️ ISV 扩展时**不要往 `entryentity_all` 加字段**，加了主物理表也不会跟"6 个写入分录"自动同步 · 真正写入分录是 `entryentity_add` / `entryentity_parent` / `entryentity_info` / `entryentity_disable` / `entryentity_merge` / `entryentity_split`。

---

## 四、字段联动关系（来自 `AdminOrgBatchBillPlugin.java` 反编译）

### F7 过滤联动（`beforeF7Select` 30 字段 · 源码 L749-L884）

**注册的 F7 字段清单**（源码 L749-L750 实证 · 30 个）：
```
info_org, parent_org, to_merge_org, merge_target_org, to_split_org, split_target_org,
parent_adminorg, parent_parentorg, parent_adminorgtype, parent_changescene,
parent_changereason, parent_city, info_adminorg, info_adminorgtype, info_changescene,
info_changereason, info_city, disable_adminorg, disable_changescene, disable_changereason,
parent_corporateorg, info_corporateorg, disorg, parent_adminorglayer, parent_adminorgfunction,
info_adminorglayer, info_adminorgfunction, merge_changescene, merge_changereason,
split_changescene, split_changereason
```

**典型 F7 过滤逻辑**：

1. **`parent_adminorg` 上级行政组织 F7**（源码 L777-L784）
   - 过滤 `boid != 平台根组织`（不能选根）
   - 注入 `searchdate` = 当前日期（按当前时态查可见组织）
   - 走数据权限过滤 `BaseDataHelper.getAdminOrgBaseDataFilter("haos_adminorgdetail", [bu_id])`
   - 排除虚拟组织 `isvirtualorg != '1'`

2. **`{prefix}_changescene` 变动场景 F7**（源码 L833-L841）
   - 按 `orgchangetype.id = changetype.id`（变动类型决定可选场景）
   - 排除 "启用" 场景 `id != OrgBatchChgBillConstants.CHANGE_SCENE_ENABLE`

3. **`{prefix}_changereason` 变动原因 F7**（源码 L851-L857）
   - 走 `AdminOrgBatchChgHelper.setChangeReasonF7Filter(...)` · 按 changescene 过滤可选原因

4. **`{prefix}_city` 城市 F7**（源码 L859-L869）
   - 按 `country = companyarea.id` 过滤（必须先选国家）

5. **`disorg` 签发组织 F7**（源码 L757-L760）
   - 走 BU 数据权限过滤

### 字段值变更联动（`propertyChanged` 委托 · 源码 L356-L360）

`AdminOrgBatchBillPlugin.propertyChanged` **不直接处理** · 委托给 `AdminOrgBatchBillPropertyChangedService` 业务类（见 `_auto_plugin_semantics.md` 引用清单）。具体联动规则在该业务类里 · 标准模式：
- 选 `*_org`（BU）变更 → 触发清空 `*_adminorg` / `*_parentorg`
- 选 `*_changetype` 变更 → 触发清空 `*_changescene` / `*_changereason`
- 选 `*_changescene` 变更 → 触发回填 `*_changereason` 默认值

---

## 五、继承链（机读实证）

来源 `_auto_inherit_chain.md` + getFormMetadata 实抓：

```
L0  00305e8b000006ac        （bos 基础单据模板）
L1  ab7efc31000010ac        （HR 单据基模板 · hrmp-hbp）
L2  hbp_hrbillorgtpl        （HR 带主组织单据基模板）
L3  homs_orgbatchchgbill   （组织调整申请单 · 当前表）
```

⚠️ **ISV 扩展继承层选择**：
- 加业务字段（如"申请来源系统"）→ 加在 **L3 自身**（不要往 L2 推 · 那是平台基模板 · ISV 改不动 · `feedback_isv_ownership_redline`）
- 加 entry 字段 → 必须挂在 7 个 entry 之一的 `parentScope` 下 · 主物理表只有一张（参考关键发现 1）
- 加跟时态相关的字段 → ⚠️ 慎重，单据本身不是时序，加 bsed/bsled 没用

---

## 六、字段安全分级

| 安全级 | 字段族 | 处理原则 |
|---|---|---|
| 🔴 红线（写了崩） | `creator` / `createtime` / `modifier` / `modifytime` / `id` / `auditstatus` 写后改 | 平台维护 · 任何插件都禁止写 |
| 🟠 高敏感 | `billstatus` / `billno` / `org`（创建后） | 只能通过 OP 在生命周期方法里改 · 不要直接 set · 见 `OrgBatchBillSubmitOp.java:35-37` 实证只有 OP 可改 billstatus |
| 🟡 业务可改 | `effdt` / `auditor` / `auditdate` / `description` / `dispatchnumber` 等 | FormPlugin 可以联动 setValue · 但要 beginInit/endInit 防死循环（PR-004） |
| 🟢 ISV 自定义 | 加自定义字段 `${ISV_FLAG}_*`（带 ISV 前缀）| 可加在 7 entry 任一 · 走 modifyMeta · 服从 PR-001/PR-002 |

---

## 七、与 admin_org 的对偶关系（关键决策图）

```
┌──────────────────────┐                  ┌──────────────────────┐
│ haos_adminorg        │ ◄──────────────  │ homs_orgbatchchgbill │
│ (HisModel 时序主数据)  │      生效写入      │ (BillFormModel 申请单) │
│                      │                  │                      │
│ 4 时态 / 多版本共存    │                  │ 5 状态 / 一次生效     │
│ 直改主数据             │                  │ 走单据走审批          │
└──────────────────────┘                  └──────────────────────┘
        ▲                                          │
        │                                          │ effdt 控制版本日期
        │ 直接 add/update                           ▼
        │ admin_org_quick_maintenance         OrgBatchChgBillEffectOp
        │ + AdminOrgFastSaveOp                  + OrgBillBatchEffectService
        │                                       + AdminChangeMsgService
        │
┌──────────────────────┐
│ admin_org_quick      │  ← 单个 / 即时调整 / 不走审批
│ (CS-01..CS-08 适用)   │
└──────────────────────┘
```

**何时走哪条路**：

| 场景 | 推荐路径 | 理由 |
|---|---|---|
| 单个组织即时建/改 | admin_org_quick + AdminOrgFastSaveOp | 无审批 · 即时生效 · 简单 |
| 批量建组织（>3 个） | homs_orgbatchchgbill `entryentity_add` | UI 支持批量 · 一次审批 |
| 组织合并 / 拆分 | **必须** homs_orgbatchchgbill | admin_org_quick **不支持**合并拆分 |
| 调整上级 + 改编码 | homs_orgbatchchgbill `entryentity_parent` | 申请单允许 `parent_number` 同步改编码 · admin_org_quick 不允许 |
| 需要发文 / 走审批 | 必须 homs_orgbatchchgbill | 主表 `dispatchnumber` / `dispatchname` 字段；审批走 wftask |
| 异步生效（指定未来 effdt） | homs_orgbatchchgbill | 单据 `effdt` 可设未来日期 |

---

## 八、平台命名规则速查

- **多语言表 `_l` 结尾**：`t_homs_orgchgbill_l`（主表多语言）+ `t_homs_orgchgbillentry_l`（分录多语言）。苍穹平台规则：**所有 `MuliLangTextField` 字段都落 `_l` 结尾的物理表**（不是 `_i`，`_i` 是基础资料拆分表）。`add_name` / `add_simplename` / `info_name` 等字段值都在 `_l` 表。
- **反模式 · 继承场景专属类**：`OrgBatchBillSaveOp` / `OrgBatchChgBillEffectOp` / `AdminOrgBatchBreakupOp` 都是场景专属类 · ISV 不要继承（违反 PR-001 · 跟 admin_org 的 `AdminOrgFastSaveOp` / hjm 的 `JobHrSaveOp` 同性质）。扩展要走"并列挂" `HRDataBaseOp`（白名单 SDK 父类）+ `onAddValidators` 注册 ISV Validator。

---

**📌 来源追溯**：

- 物理表与字段元数据：`_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md`（OpenAPI 实抓）
- 7 entry 共用一张物理表：`AdminOrgBatchBillListPlugin.java:122-124` + `AdminOrgBatchChgBillEffectOp.java:92-99`（HRBaseServiceHelper("homs_batchorgentity") 操作同一表）
- 继承链：`_auto_inherit_chain.md` + `_auto_plugin_registry.md`
- F7 联动：`AdminOrgBatchBillPlugin.java:749-884`（30 字段 F7 注册 + 过滤实证）
- 状态机字段：`OrgBatchBillSubmitOp.java:35-37`（writes billstatus="B"）+ `AdminOrgBatchBillListPlugin.java:357-359`（writes billstatus="F"）
