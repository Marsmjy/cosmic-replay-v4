# 数据流转 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 `AdminOrgChgRecordListPlugin.java`（654 行）+ 配对场景 `homs_orgbatchchgbill` 写入链反向推导
> **confidence**：verified（查询路径）+ inferred-from-paired（写入路径来自配对场景实证）
> **审计时间**：2026-04-27

---

## 1. 数据流总览

```
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│   ┌──────────────────────┐                                           │
│   │ 写入方                │                                           │
│   │ homs_orgbatchchgbill │ ── audit/submiteffect ──┐                 │
│   │ (配对场景)             │                          │                 │
│   └──────────────────────┘                          ▼                 │
│                                            ┌──────────────────────┐  │
│   ┌──────────────────────┐                  │ OrgBatchChgBillEffectOp │
│   │ 标品 HIES 导入         │                  │ + AdminChangeMsgService│
│   │ importdata_hr        │ ── 标品 ImportPlugin ──►─┐                 │
│   └──────────────────────┘                          │                 │
│                                                     ▼                 │
│                                          ┌──────────────────────────┐ │
│                                          │ 物理表写入                 │ │
│                                          │ t_homs_orgchgrecord       │ │
│                                          │ t_homs_orgchgentry        │ │
│                                          │ t_homs_orgchgdetail       │ │
│                                          └──────────────────────────┘ │
│                                                     ▲                 │
│                                                     │ 查询 (只读)      │
│                                                     │                 │
│                                          ┌──────────────────────────┐ │
│                                          │ AdminOrgChgRecordListPlugin│
│                                          │ (本场景)                   │ │
│                                          │  · setFilter (权限+映射)   │ │
│                                          │  · beforePackageData (批量)│ │
│                                          │  · packageData (单行渲染)  │ │
│                                          │  · billListHyperLinkClick  │ │
│                                          │     (跳配对场景详情)        │ │
│                                          └──────────────────────────┘ │
│                                                     ▲                 │
│                                                     │ 用户进入         │
│                                                     │                 │
│                                          ┌──────────────────────────┐ │
│                                          │ 用户                       │ │
│                                          │ 菜单/跨场景跳转            │ │
│                                          └──────────────────────────┘ │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 2. 写入路径（关键认知 · 不在本场景）

### 2.1 标品写入入口 1 · 来自配对场景生效（90% 数据来源）

> ⭐ **本场景没有任何写入 OP** · 所有数据由配对场景 `homs_orgbatchchgbill` 在生效阶段写入。

调用链（基于 `homs_orgbatchchgbill` 反编译类 `OrgBatchChgBillEffectOp.java` 推导 · 详见 `homs_orgbatchchgbill_maintenance/04_business_flow.md` 第三节）：

```
[homs_orgbatchchgbill 单据从 B 状态到 C 状态]
  │
  ▼
[OP · OrgBatchChgBillEffectOp]
  │
  ├─ beforeExecuteOperationTransaction
  │     │ 拿 7 entry 数据（add/parent/info/disable/merge/split + entryentity_all）
  │     │
  │     └─ 解析每条 entry 的实际变动：
  │           · entryentity_add → 新组织
  │           · entryentity_parent → 上级变更
  │           · entryentity_info → 字段变更
  │           · entryentity_disable → 停用
  │           · entryentity_merge → 合并
  │           · entryentity_split → 拆分
  │
  ├─ beginOperationTransaction（事务开始）
  │     │
  │     ├─ 1. 落 t_haos_adminorg 主表（haos_adminorg 时序新版本）
  │     │
  │     ├─ 2. 落 t_homs_orgchgrecord（变动主记录 · 一申请单一行）
  │     │     INSERT INTO t_homs_orgchgrecord (fid, fcreatorid, fcreatetime, finitdatasource, fadminorgid)
  │     │     VALUES (新ID, 当前用户, NOW(), 'BILL_EFFECT', 受影响组织boid)
  │     │
  │     ├─ 3. 落 t_homs_orgchgentry（每个 entry 一行）
  │     │     · fchgbillid = 申请单 ID
  │     │     · fchgeffecttime = 申请单 effdt 字段
  │     │     · foperationtime = NOW()
  │     │     · foperatorid = 当前用户
  │     │     · fchangetypeid / fchangesceneid / fchangereasonid = 申请单填的对应字段
  │     │     · fchangedescription = 申请单的变动说明
  │     │
  │     └─ 4. 落 t_homs_orgchgdetail（字段级 · 每个变更字段一行）
  │           · fchgentitynumber = 'haos_adminorgdetail' / 'haos_orgteamcooprel' 等
  │           · fchgpageelement = 字段 key（如 'name' / 'parentorg' / 'belongcompany'）
  │           · fbeforechgentity / fafterchgentity = 变更前后实体 ID
  │           · ⭐ 主键 fdetailid 由平台 ID.genLongId() 生成（PR-005）
  │
  ├─ endOperationTransaction（事务提交前）
  │     │
  │     └─ AdminChangeMsgService（如配置 BEC 则发组织变更事件）
  │
  └─ afterExecuteOperationTransaction（事务已提交）
        │
        └─ （可选）发 BEC 事件到外部订阅者（标品本场景查询端不订阅 · 详见 02_business_rules.md §4）
```

> ⭐ **关键认知 1**：写入是**多表跨实体事务** · 涉及 4 张表（haos_adminorg + 3 张 homs_orgchg*）· 任何一张写失败整事务回滚 · 不会出现"半截"变动记录。

> ⭐ **关键认知 2**：写入由配对场景驱动 · 本场景不参与任何写入。**ISV 想监听变动事件 · 不在本场景挂插件 · 去 `homs_orgbatchchgbill` 的 `audit / submiteffect` afterExecuteOperationTransaction 阶段挂 OP 监听**（参考 [06_customization_solutions.md CS-04 + CS-06](06_customization_solutions.md)）。

### 2.2 标品写入入口 2 · HIES 导入（罕见 · 系统初始化用）

opKey `importdata_hr` 触发标品 ImportPlugin 走 HIES 导入。**这条路径**是为了系统初始化时把外部历史变动数据导入用 · 日常用户不会用。

写入路径不走 OP · 走标品的 `kd.bos.entity.plugin.IImportPlugin` 链路 · 一次最多 5000 条（HIES 标品限制）。

### 2.3 ISV 写入路径（如需）

> ❌ **ISV 不应在本场景直接写入** · 应该走配对场景的扩展点。

如果业务确实需要"在本场景手工补录变动记录"（极罕见 · 通常是数据迁移场景）：
1. 走配对场景 `homs_orgbatchchgbill` 创建一张"补录单据" · 走审批流生效
2. 让标品 OP 写入本表（保证数据一致性）

直接 INSERT 到 `t_homs_orgchgrecord` 物理表是**强烈反模式**（破坏 haos_adminorg 时序版本一致性）。

---

## 3. 查询路径（本场景核心 · 反编译实证）

### 3.1 用户进入列表的 QFilter 链

```
用户点菜单"组织变动明细查询"
  │
  ▼
[HRDataBaseList.setFilter] super 调用
  │
  ├─ 1) 平台默认数据权限过滤（数据权限模型自动注入）
  │
  ▼
[AdminOrgChgRecordListPlugin.setFilter L155-L171]
  │
  ├─ 2) replaceProperty(qFilterList) (L158)
  │     │
  │     └─ 把搜索面板的 search* 代理字段映射到真实字段路径
  │        · "searchdate" → "orgchgentry.chgeffecttime"
  │        · "searchchangescene.id" → "orgchgentry.changescene.id"
  │        · "searchbillno" → "orgchgentry.chgbill.billno"
  │        · "searchdispatchno" → "orgchgentry.chgbill.dispatchnumber"
  │        · "searchdispatchname" → "orgchgentry.chgbill.dispatchname"
  │        · "seatchsimple" → "adminorg.simplename"
  │
  ├─ 3) qFilterList.add(OrgPermHelper.getHrPermFilter(billFormId, "adminorg.org")) (L159)
  │     │
  │     └─ 加 HR 权限过滤 · 按 adminorg.org 字段映射数据权限到当前用户的可见 BU
  │
  ├─ 4) [条件分支] 如果有 customParam.needshowdetail
  │     │
  │     └─ qFilterList.remove(0); 清掉默认过滤
  │        qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", customParam.billidfilter))
  │
  ├─ 5) [条件分支] 如果有 customParam.needshowsingle
  │     │
  │     └─ qFilterList.remove(0); 清掉默认过滤
  │        qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", billidfilter))
  │        qFilterList.add(new QFilter("orgchgentry.orgentry", "=", entryid))
  │
  ├─ 6) qFilterList.add(new QFilter("adminorg.otclassify.id", "=", 1010L)) (L169)
  │     │
  │     └─ 强制限定 otclassify=1010（行政组织分类）· 排除非行政类组织
  │
  └─ 7) setFilterEvent.setOrderBy("adminorg.number, orgchgentry.chgeffecttime desc, ...") (L170)
        │
        └─ 默认排序：组织编码 + 变动生效日期 DESC + 操作时间 DESC + 子分录序号
  │
  ▼
[平台 ORM] 拼 SQL 查 t_homs_orgchgrecord + JOIN t_homs_orgchgentry + JOIN t_homs_orgchgdetail
```

### 3.2 BU 左树联动查询

`AdminOrgChgRecordBUListPlugin.getCtrlBUFieldSet L19` 返回 11 个字段：

```java
"adminorg", "adminorg.org", "searchchangescene",
"adminorg.adminorgtype", "adminorg.parentorg",
"adminorg.corporateorg", "adminorg.belongcompany",
"adminorg.adminorglayer", "adminorg.adminorgfunction",
"changescene", "changereason", "chgbill.disorg.fbasedataid"
```

含义：左树点击某个 BU / adminorgtype / changescene 等节点时 · 自动把对应字段加到查询条件。这是 `AbstractBUListPlugin` 父类的标准模式（`AbstractBUListPlugin.getCtrlBUFieldSet` 用于决定哪些字段会被左树联动）。

### 3.3 字段级变动溯源的两阶段查询（最核心数据流）

```
beforePackageData 触发
  │
  ▼
buildData(detailIdList) - L364
  │
  ├─ 阶段 1：HRBaseServiceHelper("homs_subentryentity").query(...) (L367)
  │     · 拿当前页所有 detail 行的 5 字段 ID
  │     · 1 SQL · 不是 N+1
  │
  ├─ 阶段 2：按 chgentitynumber 分组 (L368-L375)
  │     · key=chgentitynumber, value=SearchVO{entityId集合, propertySet集合, coopRelTypeId集合}
  │
  ├─ 阶段 3：对每组按 entityNumber 查真表 (L379-L406)
  │     特殊处理 3 路：
  │       │
  │       ├─ a) "haos_orgteamcooprel" → 协作变动
  │       │     · 先查 haos_teamcoopreltype 拿协作类型名
  │       │     · 真实数据查 haos_adminorgdetail（变动主体仍是行政组织）
  │       │
  │       ├─ b) ADMIN_STRUCT_KEY → 矩阵组织变动
  │       │     · 先查 haos_structproject 拿结构方案名
  │       │     · 真实数据查 haos_adminorgdetail
  │       │
  │       └─ c) 其他实体 → 普通字段变动
  │             · 直接按 entityNumber 查 · select propertySet 拼出来的字段
  │
  ▼
[buildChangeMap L408-L430] 内存计算 ChangeDetailVO
  │
  ├─ 对每条 detail 算 before/after 的字段值字符串
  ├─ 处理 3 类特殊场景（合并/拆分 / ILocaleString 多语言 / DynamicObject 引用 / MulBasedataDynamicObjectCollection）
  └─ 存到 this.changeMap
  │
  ▼
[packageData per row L304] 在渲染时 · 通过 event.setFormatValue 把 changeMap 的值塞回列
```

### 3.4 查询时的批量优化（PR-009 实证）

反编译 L237-L243 实证：

```java
Set parentOrgList = pageData.stream().map(dy -> dy.getLong("adminorg.parentorg.id")).collect(Collectors.toSet());
OrgFullNameServiceWrapper fullNameServiceWrapper = new OrgFullNameServiceWrapper();
Map parentOrgLongName = fullNameServiceWrapper.getBatchOrgFullName(parentOrgList, HRDateTimeUtils.truncateDate(new Date()));
```

> ⭐ 一次性查所有可见行的父组织全名 · 1 SQL O(1) · 不是 N+1。
> 注意 `parentOrgList` 用的是 `adminorg.parentorg.id`（id 维度）· 因为 `OrgFullNameServiceWrapper` 内部会再用 boid 反查 · ISV 扩展时不要套用此模式 · 跨场景跳转传值仍要用 boid（PR-009）。

---

## 4. 跨表跳转路径（hyperLinkClick 实证）

### 4.1 路径 A · 从本场景跳到配对场景看申请单详情

```
用户点列表行的 chgbill 列（或其他超链接列）
  │
  ▼
[BillListHyperLinkClickEvent] 触发
  │
  ▼
[AdminOrgChgRecordListPlugin.billListHyperLinkClick L200-L218]
  │
  ├─ args.setCancel(true)  （阻止默认跳转）
  │
  ├─ 拿 entryPkId（当前 entry 的主键）
  │
  ├─ 用 HRBaseServiceHelper("homs_orgchgrecord").queryOriginalOne(...)
  │     · 注意：用的是 "homs_orgchgrecord" 实体（本场景）
  │     · 字段："orgchgentry.chgbill"（拿当前 entry 关联的申请单 ID）
  │
  ├─ 构造 BillShowParameter
  │     · setFormId("homs_orgbatchchgbill")  ⭐ 跳的是配对场景
  │     · setPkId(申请单 ID)
  │     · setBillStatus(VIEW)
  │     · setShowType(MainNewTabPage)
  │
  └─ getView().showForm(showParameter)
  │
  ▼
[配对场景 homs_orgbatchchgbill 详情页打开 · VIEW 状态]
```

### 4.2 路径 B · 从本场景跳到行政组织详情（如 ISV 扩展）

> ⭐ 标品**不提供**这条跳转 · 但 ISV 经常需要（CS-03）

ISV 扩展插件的实现思路：

```java
// 拿到当前行的 adminorg 字段（值是组织 boid · PR-009）
long boid = currentRow.getLong("adminorg");

// 查 haos_adminorgdetail 拿到当前版本的 id
// PR-008 + PR-009：用 boid + iscurrentversion=true
HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorgdetail");
QFilter qf = new QFilter("boid", "=", boid).and("iscurrentversion", "=", Boolean.TRUE);
DynamicObject dy = helper.queryOne("id,name", new QFilter[]{qf});

if (dy != null) {
    BillShowParameter param = new BillShowParameter();
    param.setFormId("haos_adminorgdetail");
    param.setPkId(dy.getLong("id"));
    param.setBillStatus(BillOperationStatus.VIEW);
    getView().showForm(param);
}
```

参考 [06_customization_solutions.md CS-03](06_customization_solutions.md)。

---

## 5. 数据一致性保证

| 风险点 | 标品保证 | ISV 注意事项 |
|---|---|---|
| 申请单生效但变动记录写失败 | OP 同事务 · `OrgBatchChgBillEffectOp` 事务回滚一起回滚 | ISV 别在 `afterExecuteOperationTransaction` 改本表 · 主事务已提交 · 改了破坏一致性 |
| 字段级变动记录不全 | 标品在生效阶段做"diff" · 把每个变动字段都写一行 detail | ISV 想加新字段的变动溯源 · 必须在 homs_orgbatchchgbill 写入端补 chgpageelement 维度 |
| 父组织 longname 显示错乱 | `OrgFullNameServiceWrapper` 按 today 拿当时的全名（不是历史时点）| 想看"变动当时的"父组织名 → ISV CS-04 重写 packageData · 用 chgeffecttime 替代 today |
| 跨语言展示 | `formatValue` L601-L631 处理 ILocaleString · 按当前用户的启用语言展示 | ISV 加自定义字段建议用 MuliLangTextField 平台兜底 |

---

## 6. 性能特征

| 操作 | 复杂度 | 说明 |
|---|---|---|
| `setFilter` | O(1) | 只是 QFilter 拼装 |
| `beforePackageData.buildSplitMerge` | O(N) · N=本页合并/拆分 entry 数 | 1 SQL 查 detail + 1 SQL 查 adminorgdetail · 批量 |
| `beforePackageData.buildData` | O(K) · K=不同 chgentitynumber 类型数（通常 1-3）| 每类 1 SQL · 批量 |
| `beforePackageData.parentlongname` | O(1) | OrgFullNameServiceWrapper 一次批查 |
| `packageData` 单行 | O(1) | 内存 changeMap 取值 |
| `billListHyperLinkClick` | O(1) | 1 SQL 查 chgbill |

> 大数据量（>10000 行）瓶颈：本场景设计已经做了批量优化 · 但**前端页面渲染 5000+ 行**仍然会卡 · 推荐分页（默认 25/页）。

---

## 7. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 查询权限 + 数据来源约束
- [`03_model_design.md`](03_model_design.md) · 物理表 schema + 字段语义
- [`04_business_flow.md`](04_business_flow.md) · 用户使用流程 + 跨场景跳转
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-04 字段溯源美化 / CS-06 跳转扩展
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/04_business_flow.md`](../homs_orgbatchchgbill_maintenance/04_business_flow.md) · ⭐ 配对场景写入流程实证
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-008 / PR-009 / PR-010
