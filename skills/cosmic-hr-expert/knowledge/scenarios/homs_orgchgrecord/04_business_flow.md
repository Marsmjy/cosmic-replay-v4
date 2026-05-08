# 业务流程 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 `AdminOrgChgRecordListPlugin.java`（654 行）+ `AdminOrgChgRecordBUListPlugin.java`（22 行）+ 15 opKey 实证
> **confidence**：verified · 与 `homs_orgbatchchgbill` 配对场景双向工作流
> **审计时间**：2026-04-27

---

## 1. 场景生命周期总览

```
┌────────────────────────────────────────────────────────────────────────┐
│ 写入端（配对场景：homs_orgbatchchgbill）          只读端（本场景）           │
│                                                                          │
│  [新建] save A → submit B → audit C ────►  写 t_homs_orgchgrecord         │
│           │           │           │              + t_homs_orgchgentry    │
│           │           │           │              + t_homs_orgchgdetail   │
│           │           │           ▼                                      │
│           │           │   OrgBatchChgBillEffectOp                        │
│           │           ▼                                                  │
│           │     提交即生效 (submiteffect)                                  │
│           ▼                                                              │
│      暂存（不生效 · 本场景看不到）                                          │
│                                                                          │
│                                          ┌──────────────────────────┐    │
│                                          │ 用户进入"组织变动明细查询" │    │
│                                          │      │                   │    │
│                                          │      ▼                   │    │
│                                          │ AdminOrgChgRecordListPlugin   │
│                                          │   ├─ preOpenForm        │    │
│                                          │   ├─ setFilter          │    │
│                                          │   ├─ beforePackageData  │    │
│                                          │   ├─ packageData        │    │
│                                          │   └─ billListHyperLinkClick   │
│                                          │      │                   │    │
│                                          │      ▼                   │    │
│                                          │ 跳 homs_orgbatchchgbill 详情  │
│                                          └──────────────────────────┘    │
└────────────────────────────────────────────────────────────────────────┘
```

> **重要**：本场景**不参与状态机**（没有 billstatus / enable / status 等状态字段）· 只是配对场景生效后落地数据的查询视图。

---

## 2. 标品 15 opKey 全枚举（按业务分组）

| 分组 | opKey | 名称 | opType | 写入路径 |
|---|---|---|---|---|
| **翻页（4）** | `first` / `previous` / `next` / `last` | 第一/前一/后一/最后 | first/previous/next/last | 无（视图层翻页） |
| **写入（兜底·从不触发）** | `save` | 保存 | save | 模板兜底 · 实际本场景无写入语义 |
| **HIES 导入（2）** | `importdata_hr` / `show_import_record_hr` | 导入数据 / 查看导入记录 | - | HIES 模板（写 t_homs_orgchgrecord 的能力 · 一般用于初次系统初始化） |
| **HIES 导出（4）** | `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr` | 按列表导出 / 按导入模板导出 / 按导出模板导出 / 查看导出记录 | - | 仅读 |
| **平台导出（2）** | `exportlistbyselectfields` / `exportdetails` | 导出数据（按列表）/ 查看导出结果 | exportlist/exportdetails | 仅读 |
| **列表辅助（3）** | `refresh` / `close` | 刷新 / 关闭 | refresh/close | 视图操作 |

> 反编译实证：`AdminOrgChgRecordListPlugin.afterDoOperation` 只对 `refresh` 做了空操作（L182-L184） · 其他 opKey 全部走标品兜底。

---

## 3. 用户使用流程（最高频路径 · 90% 用例）

### 3.1 进入列表 · 查全部变动明细

```
菜单 → 行政组织维护 → 行政组织维护 → 组织变动明细查询
   │
   ▼
[FormPlugin · AdminOrgChgRecordListPlugin]
   │
   ├─ AdminOrgChgRecordListPlugin() 构造
   │     │
   │     ├─ 注册 6 个 SEARCH_MAP 字段映射 (L144-L149)
   │     └─ 加载 haos_adminorgdetail 字段元数据到 this.properties (L150-L152)
   │
   ▼
preOpenForm(e) (L173-L178)
   │
   ├─ 检查 customParam.needshowdetail 或 needshowsingle
   │     │
   │     └─ 是 → setSelectedEntity("subentryentity") （子分录视图）
   │
   ▼
[BU 左树 · AdminOrgChgRecordBUListPlugin]
   │
   ├─ 重写 getCtrlBUFieldSet() (L19) → 返回 11 个 BU 控制字段集合
   │     · adminorg / adminorg.org / searchchangescene / adminorg.adminorgtype 等
   │     · 控制左树点击时 · 哪些字段会被自动加到查询条件
   │
   ▼
setFilter(setFilterEvent) (L155-L171)
   │
   ├─ super.setFilter(e) （HRDataBaseList 数据权限）
   │
   ├─ replaceProperty(qFilterList) (L155 → L187-L198)
   │     │
   │     └─ 把用户在搜索面板填的 "searchdate" 等代理字段
   │        映射到真实查询路径 "orgchgentry.chgeffecttime"
   │
   ├─ 加 OrgPermHelper.getHrPermFilter (L159)
   │     │
   │     └─ 按当前用户的"组织数据权限"过滤可见行
   │
   ├─ 加 adminorg.otclassify.id = 1010 过滤 (L169)
   │     │
   │     └─ otclassify=1010 是"行政组织分类" · 排除非行政类组织
   │
   └─ setOrderBy("adminorg.number, orgchgentry.chgeffecttime desc, ...") (L170)
   │
   ▼
beforePackageData(event) (L232-L252)
   │
   ├─ 拿当前页 pageData 的所有 parentorg.id (L237)
   │
   ├─ OrgFullNameServiceWrapper.getBatchOrgFullName(parentOrgList, today) (L239)
   │     │
   │     └─ ⭐ 批量拿父组织全名（一次 SQL · 不是 N+1）
   │
   ├─ buildSplitMerge(entryIds) (L250)
   │     │
   │     └─ 处理"组织合并/拆分"概览（L254-L302）
   │
   └─ buildData(detailIdList) (L251)
        │
        └─ 处理字段级变动溯源（L364-L406）
   │
   ▼
packageData(event) [PER ROW] (L304-L324)
   │
   ├─ 处理 mergesplitview 列 → setFormatValue (L313-L316)
   ├─ 处理 parentlongname 列 → setFormatValue (L317-L320)
   └─ 处理字段溯源 4 列 (changefield/before/after/before/afternamenumber) (L326-L362)
        ├─ key=changefield → vo.getDisplayName()
        ├─ key=beforevalue → vo.getBeforeValue()
        ├─ key=aftervalue → vo.getAfterValue()
        ├─ key=beforenamenumber → vo.getBefore()
        └─ key=afternamenumber → vo.getAfter()
   │
   ▼
渲染列表（用户看到结果）
```

### 3.2 用户点行 → 跳到申请单详情

```
[用户点列表行的超链接（如某行的 chgbill 列）]
   │
   ▼
billListHyperLinkClick(args) (L200-L218)
   │
   ├─ args.setCancel(true)  （阻止默认跳转 · 自定义跳转逻辑）
   │
   ├─ 拿 entryPkId = args.getCurrentRow().getEntryPrimaryKeyValue() (L202-L203)
   │
   ├─ HRBaseServiceHelper helper = new HRBaseServiceHelper("homs_orgchgrecord") (L204)
   │
   ├─ helper.queryOriginalOne("orgchgentry.chgbill", new QFilter("orgchgentry.id","=",entryPkId)) (L206)
   │     │
   │     └─ 查得当前 entry 的 chgbill（申请单 ID）
   │
   ├─ new BillShowParameter() (L210)
   │     ├─ setShowType(MainNewTabPage)
   │     ├─ setPkId(dy.getLong("orgchgentry.chgbill"))  ← ⭐ 申请单的 ID
   │     ├─ setBillStatus(BillOperationStatus.VIEW)
   │     ├─ setStatus(OperationStatus.VIEW)
   │     ├─ setFormId("homs_orgbatchchgbill")  ← ⭐ 跳转目标 form！配对场景
   │     └─ setPageId(entryPkId + "_" + getView().getPageId())
   │
   └─ getView().showForm(showParameter)
   │
   ▼
打开 homs_orgbatchchgbill 详情页（VIEW 状态 · 只读看申请单全貌）
```

> ⭐ **跨场景跳转的关键设计**：本场景的 `chgbill` 字段引用的是 `homs_orgchgbill`（基础资料层）· 但 hyperLinkClick **跳的是 `homs_orgbatchchgbill`**（带审批的单据视图）· 因为后者才是用户真实需要的"申请单详情"形态。

---

## 4. 跨场景调用模式：从外部"已生效申请单"跳进本场景查变动

### 4.1 模式 A · 显示某申请单的所有 entry 变动（needshowdetail）

调用方（如 homs_orgbatchchgbill 详情页的"查看变动明细"按钮）：

```java
// 在 homs_orgbatchchgbill 详情页扩展按钮 · 跳到本场景看该申请单的所有 entry 变动
ListShowParameter param = new ListShowParameter();
param.setFormId("homs_orgchgrecord");
param.setCustomParam("needshowdetail", true);  // ⭐ 触发本场景的"按申请单聚合"视图
param.setCustomParam("billidfilter", currentBillId);
this.getView().showForm(param);
```

本场景接收（反编译 `preOpenForm` L173-L178 + `setFilter` L160-L163）：

```java
// preOpenForm
if (e.getFormShowParameter().getCustomParam("needshowdetail") != null
        && (Boolean) e.getFormShowParameter().getCustomParam("needshowdetail")) {
    ((ListShowParameter) e.getFormShowParameter()).setSelectedEntity("subentryentity");  // 选中子分录维度
}

// setFilter
if (... needshowdetail ...) {
    qFilterList.remove(0);  // 清掉默认过滤
    qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", customParam.billidfilter));
}
```

### 4.2 模式 B · 显示某申请单的某条 entry 的变动（needshowsingle）

类似模式 A · 但更细 · 只显示一条 entry 的变动（用于精确溯源单个组织的某次变动）：

```java
ListShowParameter param = new ListShowParameter();
param.setFormId("homs_orgchgrecord");
param.setCustomParam("needshowsingle", true);  // ⭐ 单 entry 视图
param.setCustomParam("billidfilter", billId);
param.setCustomParam("entryid", entryId);
```

本场景接收（`setFilter` L164-L168）：

```java
if (... needshowsingle ...) {
    qFilterList.remove(0);
    qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", customParam.billidfilter));
    qFilterList.add(new QFilter("orgchgentry.orgentry", "=", customParam.entryid));  // 多加一条
}
```

> **2 种模式 vs 直查**：用户从菜单进入是"全量查看"模式（看自己有权限的所有变动）；从配对场景跳入是"局部聚焦"模式（看某申请单的全部 / 某条 entry 的变动）。

---

## 5. 字段级变动溯源工作流（最复杂的部分 · 内存计算）

```
beforePackageData 触发
   │
   ▼
buildData(detailIdList) (L364-L406)
   │
   ├─ 1) 查 t_homs_orgchgdetail · 拿这一页的 subentryentity 子分录数据
   │     QFilter("id","in", detailIdList)
   │     字段：id, chgentitynumber, chgpageelement, beforechgentity, afterchgentity, coopreltype
   │
   ├─ 2) 按 chgentitynumber 分组到 SearchVO（key→entityIds 集合 + propertySet 集合）(L369-L375)
   │     · 例：chgentitynumber="haos_adminorgdetail"
   │           entityId={123, 456, 789}（before/after id 集合）
   │           propertySet={"name", "number", "adminorgtype"}（变动的字段集合）
   │
   ├─ 3) 对每个分组按 entityNumber 查真表（L379-L406）
   │     特殊分支：
   │       a) chgentitynumber == "haos_orgteamcooprel"
   │            → 查 haos_teamcoopreltype（拿协作类型名称）
   │            → 真实数据查 haos_adminorgdetail（变动主体仍是行政组织）
   │       b) chgentitynumber == ADMIN_STRUCT_KEY（矩阵组织）
   │            → 查 haos_structproject（拿结构方案名称）
   │            → 真实数据查 haos_adminorgdetail
   │       c) 其他（如 haos_adminorgdetail 直接变动）
   │            → 查对应表（select properties = propertySet 拼出来）
   │
   ▼
buildChangeMap (L408-L430)
   │
   ├─ 对每条 detail · 把 before/after 的 DynamicObject 取出
   │
   ├─ 调对应 buildChangeVO 方法（3 路）
   │     · buildCoolChangeVO（协作变动）
   │     · buildStructProjectChangeVO（矩阵变动）
   │     · buildBaseChangeVO（普通字段变动）
   │
   ├─ 计算 ChangeDetailVO 实体
   │     · setBeforeValue / setAfterValue（字段值字符串）
   │     · setBefore / setAfter（带编码的"名称(编码)"格式）
   │     · setDisplayName（中文显示名 · 多语言）
   │
   └─ 存到 this.changeMap (L429)
   │
   ▼
packageData [PER ROW] · 渲染时用 changeMap 提取 (L326-L362)
```

> ⭐ 这一段是**本场景最复杂的业务**。所有"字段从 X 变成 Y"的展示都靠这套查询+计算 · 不是 SQL 直接 select。

---

## 6. 反编译实证调用栈（按方法分组）

| 调用栈 | 来源类·方法·行号 | 关键意图 |
|---|---|---|
| 字段映射初始化 | `AdminOrgChgRecordListPlugin.<init> L143-L153` | 注册 6 个 search 代理字段 |
| 数据权限过滤 | `AdminOrgChgRecordListPlugin.setFilter L155-L171` | OrgPermHelper.getHrPermFilter（只看自己有权的组织变动） |
| BU 联动 | `AdminOrgChgRecordBUListPlugin.getCtrlBUFieldSet L19` | 11 字段集 · 控制左树联动哪些查询条件 |
| 父组织全名拼装 | `AdminOrgChgRecordListPlugin.beforePackageData L237-L243` | OrgFullNameServiceWrapper（HR 域专用工具） |
| 合并/拆分概览 | `AdminOrgChgRecordListPlugin.buildSplitMerge L254-L302` | 多语言+多基础资料字段拼合 |
| 字段级变动 | `AdminOrgChgRecordListPlugin.buildData L364-L406` | 两阶段查询 |
| 渲染计算列 | `AdminOrgChgRecordListPlugin.packageData L304-L324` + `formatTextValue L326-L362` | event.setFormatValue |
| 跳申请单详情 | `AdminOrgChgRecordListPlugin.billListHyperLinkClick L200-L218` | showForm("homs_orgbatchchgbill") |

---

## 7. 异常路径

### 7.1 用户没有组织权限

`setFilter L159` 调用 `OrgPermHelper.getHrPermFilter` · 返回的 QFilter 限制了用户可见组织 · 没有权限的组织变动**不显示**（不是报错 · 直接列表为空）。

### 7.2 跨场景跳转时 chgbill 不存在

`billListHyperLinkClick L207-L209`：

```java
DynamicObject dy = helper.queryOriginalOne(CHGBILL, idFilter);
if (dy == null) {
    return;  // 静默不跳 · 不弹错误
}
```

> 这是反模式 · ISV 扩展时建议加 `getView().showTipNotification("申请单已被废弃 · 无法查看")` 提升用户体验。

### 7.3 字段级溯源数据缺失

`packageData L327-L339`：

```java
if (CollectionUtils.isEmpty(this.changeMap)) {
    return;  // 没数据 · 不渲染计算列
}
ChangeDetailVO changeVO = this.changeMap.get(id);
if (changeVO == null) {
    return;
}
```

变动明细行的 `chgentitynumber` 不识别（不在 haos_adminorgdetail / haos_orgteamcooprel / 矩阵几个 case 里）· `changeMap` 不会有对应记录 · 计算列显示空。这种情况通常出现在：
- ISV 扩展了 `homs_orgbatchchgbill` 写入逻辑 · 让它写入了未支持的 chgentitynumber
- 标品升级新增了 chgentitynumber 取值 · 但 ISV 部署的还是旧版

---

## 8. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 查询权限规则 / 变动记录字段语义 / BEC 模式判定
- [`03_model_design.md`](03_model_design.md) · 32 字段分组 + 物理表 schema
- [`05_data_flow.md`](05_data_flow.md) · 数据写入路径详解（来自配对场景）
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06 6 个定制方案
- [`07_ext_points.md`](07_ext_points.md) · 15 opKey 扩展点矩阵
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/04_business_flow.md`](../homs_orgbatchchgbill_maintenance/04_business_flow.md) · 配对场景写入流程（必对照读）
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-009 / PR-010
