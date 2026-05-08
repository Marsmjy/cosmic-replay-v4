# 数据模型设计 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 32 字段 scene_doc.json + main_form.xml + AdminOrgChgRecordListPlugin.java（654 行）实证
> **confidence**：verified · 跟 `homs_orgbatchchgbill` 配对（写入端 → 本场景只读端）
> **审计时间**：2026-04-27

---

## 1. 标识与定位

| 项 | 值 | 来源 |
|---|---|---|
| 业务场景中文名 | 组织变动明细查询（菜单："行政组织维护 → 行政组织维护 → 组织变动明细查询"） | `probe_snapshot.json menu.path` |
| formNumber | `homs_orgchgrecord` | `scene_doc.json._meta.formId` |
| formId 技术码 | `2POTG6QTABRL` | `scene_doc.json._meta.formIdTechnical` |
| ModelType | **`BaseFormModel`**（基础资料 · 但实际**只读查询**形态） | `_shared/_standard_metadata/entity_metadata/homs_orgchgrecord.md` |
| 主物理表 | **`t_homs_orgchgrecord`**（5 实质列 · 主表）| `scene_doc.json basicInfo.physicalTable` |
| 分录物理表 | **`t_homs_orgchgentry`**（11 列 · 组织变动记录分录）| schemaText |
| 子分录物理表 | **`t_homs_orgchgdetail`**（10 列 · 组织变动明细子分录）| schemaText |
| 应用 | 组织管理（homs） | header.appNumber |
| 云 | 组织发展云（ODC） | header.cloudNumber |
| 域 | 组织发展 / 行政组织维护 | menu_paths |
| 标品 ISV | `kingdee` · 标品 | main_form.xml |

---

## 2. ⭐ 核心架构：跟 `homs_orgbatchchgbill` 配对（最易踩坑）

本场景**只读** · 不直接写入 `t_homs_orgchgrecord` · 数据来源是**配对场景** `homs_orgbatchchgbill`（组织调整申请单）生效后由标品 OP 写入。

| 维度 | `homs_orgbatchchgbill`（配对·写入端） | `homs_orgchgrecord`（本场景·只读端） |
|---|---|---|
| 中文名 | 组织调整申请单 | 组织变动明细查询 |
| ModelType | `BillFormModel`（单据） | `BaseFormModel`（基础资料形态 · 实际只读列表） |
| 角色 | 申请发起 · 走审批 · 生效驱动 | 历史变动溯源 · 看哪些组织哪天变了哪些字段 |
| 写入触发 | save / submit / audit / submiteffect | **无写入 opKey**（数据靠配对场景生效写入） |
| 是否多版本 | ❌ 单据 | ❌ 不是 HisModel · 没有 boid/iscurrentversion |
| 状态字段 | `billstatus`（A/B/C/F/G）+ `auditstatus` | 无（只是变动记录） |
| 主插件 | `OrgBatchBillSaveOp` / `OrgBatchChgBillEffectOp` 等 8 类 | `AdminOrgChgRecordListPlugin`（654 行 · 唯一） |
| 业务高频度 | 中（按申请发起） | 中（变动溯源 · 审计 · 查询导出） |

> **关键认知 1**：本场景**没有"审批/保存/审核"语义** · 标品 15 opKey 中**没有 1 个写入 opKey**。`save` 这个 opKey 是 BaseFormModel 模板兜底注册 · 在本场景**不会被触发**（用户操作不到）。

> **关键认知 2**：写入路径走配对场景。当 `homs_orgbatchchgbill` 进入 C 状态（已生效）· `OrgBatchChgBillEffectOp` 链路会落地变动记录到本表的 3 张物理表（详见 [05_data_flow.md §3](05_data_flow.md)）。**ISV 想监听变动 → 不在本场景挂插件 · 去 homs_orgbatchchgbill 的 audit/submiteffect afterExecute 阶段挂**。

---

## 3. 继承链（极简 · 仅 1 级 · 反编译实证）

```
L0  0NKB12K4VH2O  (hbp_bd_originalmintpl · HR原生基础资料最小模板) ← 本场景的唯一父
       └── homs_orgchgrecord（formId=2POTG6QTABRL · 直接挂 L0 极简模板）
```

InheritPath 实抓：`0NKB12K4VH2O`（仅 1 个 ID）。

**继承链分析**：
- 本场景**没有走 hbp_bd_tpl_all / hbp_histimeseqtpl** · 直接挂 `hbp_bd_originalmintpl`（最小基础资料模板）· 说明**业务不是真的"基础资料"**（没有 enable / status / number 全套生命周期）· 而是**借基础资料外壳做查询列表**。
- 因为是最小模板 · 所以本场景**没有时序字段**（boid/iscurrentversion/hisversion/sourcevid 全无）· 这是跟 `haos_adminorghis` 的本质差别（haos_adminorghis 走 hbp_histimeseqtpl 时序模板）。

---

## 4. 32 字段按业务维度分组

> 数据源：`scene_doc.json.fields`（32 个字段）+ `probe_snapshot.json schema_text`（3 实体）+ `_shared/_standard_metadata/entity_metadata/homs_orgchgrecord.md`

### 4.1 主表 `homs_orgchgrecord` · 系统字段（5 个 · 🔒 红雷区）

| 字段 key | 类型 | 必填 | 物理列（t_homs_orgchgrecord）| 业务语义 | minefield |
|---|---|---|---|---|---|
| `creator` | CreaterField | - | `fcreatorid` → bos_user | 创建人（系统自动） | 🔴 红 |
| `createtime` | CreateDateField | - | `fcreatetime` | 创建时间（系统自动） | 🔴 红 |
| `modifier` | ModifierField | - | `fmodifierid` → bos_user | 修改人 | 🔴 红 |
| `modifytime` | ModifyDateField | - | `fmodifytime` | 修改时间 | 🔴 红 |
| `initdatasource` | ComboField | - | `finitdatasource` | 数据来源（标品/手工/导入）| 🔴 红 |

> **autoComputed 标志**：上述 5 个字段在 scene_doc.json 中 `autoComputed=true` · ISV 不要手填 / 改名 / 改类型 · 改了会破坏全平台基础资料一致性。

### 4.2 主表 `homs_orgchgrecord` · 查询代理字段（6 个 · 用于 BU 列表查询面板）

> ⭐ **设计模式**：左树 `AdminOrgChgRecordBUListPlugin` 反编译实证 · `searchXxx` 字段是**搜索代理字段**（在 SQL 上不真存数据 · 由 `AdminOrgChgRecordListPlugin` 的 `replaceProperty` 把它们映射到 `orgchgentry.*` 真实字段查询路径）。

| 字段 key | 类型 | 物理列 | 真正映射的查询路径 | 业务语义 |
|---|---|---|---|---|
| `searchdate` | DateField | —（代理）| `orgchgentry.chgeffecttime` | 生效日期（按变动生效日期搜） |
| `searchchangescene` | BasedataField → haos_changescene | —（代理）| `orgchgentry.changescene.id` | 变动场景（按场景搜） |
| `searchbillno` | TextField | —（代理）| `orgchgentry.chgbill.billno` | 单据编号（按申请单编号搜） |
| `searchdispatchno` | TextField | —（代理）| `orgchgentry.chgbill.dispatchnumber` | 发文编号 |
| `searchdispatchname` | TextField | —（代理）| `orgchgentry.chgbill.dispatchname` | 发文名称 |
| `seatchsimple` | TextField | —（代理）| `adminorg.simplename` | 简称（按组织简称搜 · 注意 key 拼写错 `seatch` 是标品历史问题） |

反编译实证（`AdminOrgChgRecordListPlugin.java:144-149`）：
```
this.SEARCH_MAP.put("searchdate", "orgchgentry.chgeffecttime");
this.SEARCH_MAP.put("searchchangescene.id", "orgchgentry.changescene.id");
this.SEARCH_MAP.put("searchbillno", "orgchgentry.chgbill.billno");
this.SEARCH_MAP.put("searchdispatchno", "orgchgentry.chgbill.dispatchnumber");
this.SEARCH_MAP.put("searchdispatchname", "orgchgentry.chgbill.dispatchname");
this.SEARCH_MAP.put("seatchsimple", "adminorg.simplename");
```

### 4.3 主表 `homs_orgchgrecord` · 行政组织维度（1 个）

| 字段 key | 类型 | 物理列 | refEntity | 业务语义 |
|---|---|---|---|---|
| `adminorg` | HRAdminOrgField | —（拆分表）| `haos_adminorghrf7` | 行政组织（变动主体）· 字段值是组织 boid（**PR-009**） |

⚠️ 雷区：HRAdminOrgField 字段值是**组织的 boid 维度** · 不是 id 维度。跨场景跳转传组织时只能传 boid（参考 [11_upstream_downstream_logic.md §3](11_upstream_downstream_logic.md)）。

### 4.4 分录 `orgchgentry` · 变动维度核心字段（11 个 · 物理表 t_homs_orgchgentry）

> 这是本场景最重要的数据 · 一条分录 = 一次组织变动事件。

| 字段 key | 类型 | 物理列（t_homs_orgchgentry）| refEntity | 业务语义 |
|---|---|---|---|---|
| `chgeffecttime` | DateField | `fchgeffecttime` | - | **变动生效日期**（业务时间轴）|
| `changescene` | BasedataField | `fchangesceneid` | `haos_changescene` | 变动场景（如"组织调整"/"组织合并"/"组织拆分" 等）|
| `changetype` | BasedataField | `fchangetypeid` | `haos_orgchangetype` | 变动类型（细分类 · 如"上级变更"/"信息变更"/"停用"等）|
| `changereason` | BasedataField | `fchangereasonid` | `haos_orgchangereason` | 变动原因（用户或预置 · 如"业务调整"/"合规要求"等）|
| `operator` | UserField | `foperatorid` → bos_user | - | 变动人（执行变动的操作人） |
| `operationtime` | DateTimeField | `foperationtime` | - | 操作日期（实际操作时间 · 跟 chgeffecttime 不同：操作时间是真发生 · 生效日期是业务起算）|
| `chgbill` | BasedataField | `fchgbillid` | `homs_orgchgbill` | 单据（关联申请单 boid · ⭐ 跟 `homs_orgbatchchgbill` 直接挂钩）|
| `orgentry` | BigIntField | —（运行时）| - | 申请单分录 ID（跨场景定位单据某条 entry） |
| `afterchgorg` | BigIntField | —（运行时）| - | 变更后组织（合并/拆分场景用）|
| `mergesplitview` | TextField | —（运行时计算）| - | 合并/拆分概览（由 `buildSplitMerge` 拼装）|
| `changedescription` | TextField | `fchangedescription` | - | 变动说明（用户填写或自动生成）|

⚠️ **chgbill 引用 homs_orgchgbill**（不是 homs_orgbatchchgbill）：标品在数据层**用的是 `homs_orgchgbill`**（基础资料层 · 不带审批） · 但实际跳转用的是 `homs_orgbatchchgbill`（带审批的单据视图）· 见 hyperLinkClick L215 实证。

### 4.5 子分录 `subentryentity` · 字段级变动明细（10 个 · 物理表 t_homs_orgchgdetail）

> 这是字段级变动溯源的核心 · 每条记录 = "某字段从 X 变成 Y"。

| 字段 key | 类型 | 物理列（t_homs_orgchgdetail）| 业务语义 |
|---|---|---|---|
| `chgentitynumber` | TextField | `fchgentitynumber` | 变动实体（如 "haos_adminorgdetail" / "haos_orgteamcooprel" / "haos_adminorgstruct"）|
| `chgpageelement` | TextField | `fchgpageelement` | 变动实体页面标识（在变动实体上的字段 key）|
| `beforechgentity` | BigIntField | —（运行时定位）| 变更前实体 ID（指向具体记录）|
| `coopreltype` | BigIntField | —（运行时）| 协作类型（"haos_orgteamcooprel" / 矩阵组织时用）|
| `afterchgentity` | BigIntField | —（运行时定位）| 变更后实体 ID |
| `beforevalue` | TextField | —（运行时计算）| 变动前的字段值（由 `formatValue` 拼装）|
| `aftervalue` | TextField | —（运行时计算）| 变动后的字段值 |
| `changefield` | TextField | —（运行时计算）| 变动字段（中文显示名 · 由 `buildBaseChangeVO` 算出 displayName）|
| `beforenamenumber` | TextField | —（运行时）| 变动前 名称(编码) |
| `afternamenumber` | TextField | —（运行时）| 变动后 名称(编码) |

> ⭐ **设计精髓（反编译实证 L364-L406 `buildData`）**：
> - `before/after value` / `changefield` / `before/afternamenumber` **不是数据库列** · 是 `AdminOrgChgRecordListPlugin.beforePackageData` → `buildData` → `buildChangeMap` 在内存里**通过查 t_haos_adminorgdetail 等真实业务表反推**计算出来的。
> - 真存在表里的只有 `chgentitynumber` / `chgpageelement` / `beforechgentity` / `afterchgentity` / `coopreltype` 5 个 ID 类字段。
> - ISV 想加字段级溯源功能 · 必须理解"展示字段是计算出来的 · 不是查出来的" · 修改 setFilter 的 SortType 也对这 5 个计算字段无效。

### 4.6 主表 `homs_orgchgrecord` · 父组织信息字段（1 个 · 运行时计算）

| 字段 key | 类型 | 物理列 | 业务语义 |
|---|---|---|---|
| `parentlongname` | TextField | —（运行时计算）| 父组织长名称（由 `OrgFullNameServiceWrapper.getBatchOrgFullName` 在 `beforePackageData` 阶段拼出）|

反编译实证（`AdminOrgChgRecordListPlugin.java:237-243`）：
```
Set parentOrgList = pageData.stream().map(dy -> dy.getLong("adminorg.parentorg.id")).collect(Collectors.toSet());
OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
Map parentOrgLongName = fullNameServiceWrapper.getBatchOrgFullName(parentOrgList, HRDateTimeUtils.truncateDate(new Date()));
```

> ⭐ **批量优化关键**：`getBatchOrgFullName` 一次性查所有可见行的父组织全名 · O(1) 数据库 IO（不是 N+1）· ISV 扩展时如果加自己的"运行时计算列"也要走批量。

---

## 5. 物理表 schema 速查

### 5.1 t_homs_orgchgrecord（主表 · 5 列实质内容）

```
PK: fid
fcreatorid           BigInt   创建人 → t_sec_user.fid
fcreatetime          DateTime 创建时间
fmodifierid          BigInt   修改人
fmodifytime          DateTime 修改时间
finitdatasource      Combo    数据来源
fadminorgid          BigInt   行政组织 boid
```

### 5.2 t_homs_orgchgentry（分录表 · 11 列）

```
PK: fentryid
FK: fid → t_homs_orgchgrecord.fid
fchgbillid           BigInt   关联申请单 → t_homs_orgchgbill.fid（⭐ 跟 homs_orgbatchchgbill 同 ID 域）
fchangesceneid       BigInt   变动场景 → t_haos_changescene.fid
fchangetypeid        BigInt   变动类型 → t_haos_orgchangetype.fid
fchangereasonid      BigInt   变动原因 → t_haos_orgchangereason.fid
foperatorid          BigInt   操作人 → t_sec_user.fid
foperationtime       DateTime 操作日期
fchgeffecttime       Date     变动生效日期
fchangedescription   String   变动说明（标量·非多语言）
```

### 5.3 t_homs_orgchgdetail（子分录表 · 5 列实质内容）

```
PK: fdetailid（系统生成 · ⚠ ISV 自建子表写入时必须用 ID.genLongId() · PR-005）
FK: fentryid → t_homs_orgchgentry.fentryid
fchgentitynumber     String   变动实体编码
fchgpageelement      String   变动字段 key
fcoopreltypeid       BigInt   协作类型
```

> ⚠️ 主键 `fdetailid` ISV 自建子表时**必须用 `kd.bos.id.ID.genLongId()` 生成**（PR-005）· 不要 UUID / System.currentTimeMillis / select max+1。本场景标品本身只读 · 无需关心 · 但 CS 自建分录扩展（如 CS-04）就要用到。

---

## 6. 实体引用关系图

```
homs_orgchgrecord (本场景视图)
  ├─ adminorg (HRAdminOrgField → haos_adminorghrf7)
  │             │
  │             └─ 字段值是组织 boid（PR-009）· 不是 id
  │
  ├─ search* (6 个代理字段 · setFilter 阶段映射到分录 · 不真存)
  │
  ├─ orgchgentry (EntryEntity · t_homs_orgchgentry)
  │     ├─ chgbill (BasedataField → homs_orgchgbill)
  │     │           │
  │     │           └─ ⭐ 这是跟 homs_orgbatchchgbill 配对场景的关联点
  │     │              hyperLinkClick 跳转用的是 homs_orgbatchchgbill 单据视图 (L215)
  │     ├─ changescene (BasedataField → haos_changescene)
  │     ├─ changetype (BasedataField → haos_orgchangetype)
  │     ├─ changereason (BasedataField → haos_orgchangereason)
  │     ├─ operator (UserField → bos_user)
  │     │
  │     └─ subentryentity (SubEntryEntity · t_homs_orgchgdetail)
  │           ├─ chgentitynumber (变动实体编码 · 多取值: haos_adminorgdetail / haos_orgteamcooprel / haos_adminorgstruct)
  │           ├─ chgpageelement (变动字段 key)
  │           ├─ before/afterchgentity (BigIntField · 指向具体记录 ID)
  │           └─ coopreltype (BigIntField · 协作类型 · 仅 haos_orgteamcooprel 用)
  │
  └─ parentlongname (TextField · 运行时由 OrgFullNameServiceWrapper 拼)
```

---

## 7. 关键 EntryEntity（子分录）

### 7.1 orgchgentry（组织变动记录）

| 字段 | 类型 | 含义 |
|---|---|---|
| `chgbill` | BasedataField → homs_orgchgbill | 关联申请单 |
| `changescene` | BasedataField → haos_changescene | 变动场景 |
| `chgeffecttime` | DateField | 变动生效日期（业务时间轴）|
| `operator` | UserField | 变动操作人 |

### 7.2 subentryentity（组织变动明细 · 嵌在 orgchgentry 下）

| 字段 | 类型 | 含义 |
|---|---|---|
| `chgentitynumber` | TextField | 变动实体（决定从哪张表算 before/after）|
| `chgpageelement` | TextField | 字段 key |
| `beforechgentity` / `afterchgentity` | BigIntField | 实体 ID |

> 反编译实证（L367-L370）：
> ```
> SearchVO vo = searchMap.computeIfAbsent(dy2.getString("chgentitynumber"), k -> new SearchVO());
> vo.entityId.add(dy2.getLong("beforechgentity"));
> vo.entityId.add(dy2.getLong("afterchgentity"));
> vo.propertySet.add(dy2.getString("chgpageelement"));
> ```
> 这是字段级变动溯源的"两阶段查询"：先按 chgentitynumber 分组 · 再按 entityId 一次性 query 真表（haos_adminorgdetail / haos_orgteamcooprel / haos_structproject）· 拿到 before/after 数据再渲染。

---

## 8. ⚠ ISV 扩展的关键认知

### 8.1 不要扩展本场景视图字段 · 应扩展 t_haos_adminorgdetail 主表

```
✅ 正确：modifyMeta(formId="haos_adminorgdetail", op="add field", key="${ISV_FLAG}_xxx")
   → 物理表 t_haos_adminorg 增列 ftdkw_xxx
   → admin_org_quick_maintenance 自动可见
   → 本场景的字段级溯源（subentryentity）也能溯到这个新字段（**前提是配对场景 homs_orgbatchchgbill 在生效阶段写入了 chgpageelement="${ISV_FLAG}_xxx"**）

❌ 错误：modifyMeta(formId="homs_orgchgrecord", op="add field")
   → 视图层独立扩展 · 仅在本场景列表显示新列
   → 但配对场景写不进来（写入端不知道这个新字段语义）· 浪费 + 误导
```

### 8.2 字段级变动溯源不是 SQL 查的 · 是计算的

`changefield` / `beforevalue` / `aftervalue` / `before/afternamenumber` **不在数据库里** · 是 `AdminOrgChgRecordListPlugin.beforePackageData` 内存计算（参考 [05_data_flow.md §4](05_data_flow.md)）· ISV 扩展时不能：
- 不能在 SQL 直接查（这些列不存在）
- 不能在 setFilter 按这些字段做 QFilter（property 无效）
- 想加自定义计算列 → 重写 `packageData` · 在 `event.setFormatValue` 阶段塞值（参考 [06_customization_solutions.md CS-04](06_customization_solutions.md)）

### 8.3 数据来源是配对场景生效驱动 · 不是用户在本场景填写

**写入路径**（反编译标品产物可推 · 详见 05_data_flow.md）：
1. 用户在 `homs_orgbatchchgbill`（**配对场景** · 写入端）填写申请单
2. 走 save → submit → audit 流程进入 C 状态
3. `OrgBatchChgBillEffectOp` 链路触发 → 写 `t_homs_orgchgrecord` + `t_homs_orgchgentry` + `t_homs_orgchgdetail`
4. 用户在本场景**只读查询**这些落库的变动明细

> **PR-005 速查**：本场景 ISV 如果做"自建变动字段补录"扩展（CS-04） · 给 t_homs_orgchgdetail 自建分录新增行 · `id` 必须用 `ID.genLongId()` · 禁用 UUID/System.currentTimeMillis/select max+1。

---

## 9. 与 `homs_orgbatchchgbill` 配对场景的对偶清单（必对照读）

| 维度 | `homs_orgbatchchgbill`（写入端） | `homs_orgchgrecord`（本场景·只读端） |
|---|---|---|
| 入口表单 | `homs_orgbatchchgbill`（list + 详情）| `homs_orgchgrecord`（list 唯一）|
| 主物理表 | `t_homs_orgchgbill`（22 列）| `t_homs_orgchgrecord`（5 实质列）|
| 时序模型 | 否 · BillFormModel | 否 · BaseFormModel（无 boid/iscurrentversion）|
| 写入 opKey | save / submit / audit / submiteffect / discard_row / breakup（73 opKey）| 无（15 opKey 全为只读 / 导出 / 翻页 / 刷新 / 关闭）|
| 主插件 | OrgBatchBillSaveOp / OrgBatchBillSubmitOp / OrgBatchChgBillEffectOp / 等 8 类（73+ 行 OP）| AdminOrgChgRecordListPlugin（654 行 · 唯一深度类）|
| BEC 发布 | submiteffect / audit afterTransDoOp 链路（**待标品配置确认**） | **不发**（grep 0 处 · 详见 02_business_rules.md §4）|
| 业务高频 | 中（按申请发起 · 一天几次到几十次） | 中（变动溯源 · 审计 · 查询导出 · 一天几次到几十次） |
| ISV 扩展频度 | 高（CS 含字段、流程、校验、跳转）| 中（主要是列表筛选定制 + 跳转扩展 + 变动溯源美化） |

---

## 10. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 查询权限 + 数据来源约束 + 变动记录字段语义
- [`05_data_flow.md`](05_data_flow.md) · 数据写入路径（来自 homs_orgbatchchgbill 生效）+ 查询路径（setFilter 实证）
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06 6 个定制方案
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/03_model_design.md`](../homs_orgbatchchgbill_maintenance/03_model_design.md) · ⭐ 配对场景模型（必对照读）
- [`knowledge/scenarios/haos_adminorghis/03_model_design.md`](../haos_adminorghis/03_model_design.md) · 同样只读查询模式参考
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-009
- [`knowledge/_shared/_standard_metadata/entity_metadata/homs_orgchgrecord.md`](../../_shared/_standard_metadata/entity_metadata/homs_orgchgrecord.md) · 标品元数据（OpenAPI 实时反推）
