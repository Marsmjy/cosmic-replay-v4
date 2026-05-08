# 异常诊断 · 组织变动明细查询(homs_orgchgrecord)

> **状态**: 基于反编译类异常处理 + 配对场景 + 共性陷阱整理
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 共性陷阱(来自 cosmic_realworld_traps)

| 陷阱 | 触发场景 | 详见 |
|---|---|---|
| buildMeta 加字段静默走 TextField | ISV 加自定义字段时参数名错(用 dataType 不用 fieldType) | `cosmic_realworld_traps/buildmeta_traps.md` |
| modifyMeta 假成功 | ISV 改字段元数据但实际没生效 | `cosmic_realworld_traps/modifymeta_traps.md` |
| addRule preCondition 不能用 `==''` | ISV 加业务规则时 preCondition 写法 | `cosmic_realworld_traps/addrule_traps.md` |

---

## 2. 本场景特有陷阱(8 条 · 反编译实证)

### 2.1 ❌ 在本场景挂订阅插件等"组织变更事件"

**症状**: 永远收不到事件 · 订阅插件的 `handleEvent` 永远不被调用。

**根因**: 反编译产物 grep 0 处 `triggerEventSubscribe / IEventService / EventServiceHelper` · 本场景标品**根本不发** BEC 事件。

**正确做法**: 去配对场景 `homs_orgbatchchgbill` 的 `audit / submiteffect afterExecuteOperationTransaction` 阶段挂 OP + BEC 发布(CS-06)。

### 2.2 ❌ 继承 AdminOrgChgRecordListPlugin

**症状**: 编译失败 · `cannot inherit from final class`。

**根因**: 反编译实证 L117 `public final class AdminOrgChgRecordListPlugin extends HRDataBaseList`(final 类)。

**正确做法**: 继承 `HRDataBaseList` · 重写需要的方法(setFilter / packageData 等)。

### 2.3 ❌ 想靠 setFilter 改成 `iscurrentversion=true`

**症状**: SQL 异常 · `字段 iscurrentversion 不存在于实体 homs_orgchgrecord`。

**根因**: 本场景**不是 HisModel** · 没有 iscurrentversion / boid / hisversion 字段。继承链是 `hbp_bd_originalmintpl`(最小模板) · 不是 `hbp_histimeseqtpl`(时序模板)。

**正确做法**: 不要套用 haos_adminorghis 的时序模式。本场景就是普通 BaseFormModel 列表。

### 2.4 ❌ 在 setFilter 里 `qFilterList.removeIf(...)` 删除权限过滤

**症状**: 用户能看到不属于自己 BU 的变动记录 · 数据安全事故。

**根因**: `setFilter L159` 调用 `OrgPermHelper.getHrPermFilter` 注入的过滤被 ISV 误删。

**正确做法**: 只 add 不 remove。如果业务真需要"全 BU 看变动" → 走平台数据权限管理员配置(给用户加全 BU 权限) · 不是代码绕过。

### 2.5 ❌ 修改 buildData 私有方法

**症状**: ISV 想修改字段级溯源逻辑 · 但 `buildData` / `buildChangeMap` / `formatTextValue` 等都是 private 方法 · 无法重写。

**根因**: 反编译实证 L364 / L408 / L326 都是 `private` 修饰符。

**正确做法**: CS-04 方案 B · 不重写标品方法 · 而是在 ISV 自己的 `beforePackageData` + `packageData` 里追加逻辑(独立的 tdkwChangeMap)。

### 2.6 ❌ ISV 加 chgentitynumber 新值后本场景不识别

**症状**: 配对场景 ISV 已经能写 `chgentitynumber=${ISV_FLAG}_orgmatrix` 到 t_homs_orgchgdetail · 但本场景 list 的 changefield / beforevalue / aftervalue 列空。

**根因**: 标品 `buildData L379-L406` 只识别 3 大类 chgentitynumber:
- `haos_orgteamcooprel` (协作变动)
- `ADMIN_STRUCT_KEY` = `haos_adminorgstruct` (矩阵变动)
- 其他("haos_adminorgdetail" 等 · 走通用查询)

ISV 自定义实体走"其他"分支 · 但如果该实体的元数据上没设置 displayName 等 · `buildBaseChangeVO L488` 算不出 displayName。

**正确做法**: CS-04 方案 B 实现自定义溯源逻辑。

### 2.7 ❌ 在 packageData 里查数据库

**症状**: 大数据量场景(>1000 行)列表渲染卡顿 / 超时。

**根因**: `packageData` 是单行回调 · 调用次数 = 行数。每行都查数据库 = O(N) 性能。

**正确做法**: 把所有数据库查询挪到 `beforePackageData` 阶段 · 一次性批量查 · 缓存到内存。`packageData` 只读内存。参考标品反编译 L237-L243 (OrgFullNameServiceWrapper.getBatchOrgFullName 一次批量) + L364 (buildData 一次批量)。

### 2.8 ❌ 跨场景跳转时拿组织字段当 id 跳

**症状**: 跳到 haos_adminorgdetail 后显示"未找到"或显示错误版本。

**根因**: 本场景 `adminorg` 字段值是组织 **boid**(业务对象维度) · 不是 id(版本维度)。直接 `setPkId(adminorg)` 错误地把 boid 当成主键。

**正确做法**: 先用 `HRBaseServiceHelper("haos_adminorgdetail").queryOne` + `boid + iscurrentversion=true` 查到当前版本 id · 再 setPkId(PR-008 + PR-009)。参考 CS-03 代码框架。

---

## 3. 数据完整性异常

### 3.1 配对场景生效但本场景看不到

**症状**: `homs_orgbatchchgbill` 申请单状态显示 C(已生效) · 但本场景 list 找不到对应变动记录。

**可能根因**:
- 配对场景生效失败 · billstatus 错误标成 C(查 t_homs_orgchgbill.fbillstatus 实际值)
- 用户没有该组织的 BU 数据权限(setFilter L159 过滤掉了)
- 用户没有时间范围内的查询权限(searchdate 过滤)

**排查命令**:
```sql
-- 1. 看申请单状态是否真的生效
SELECT fid, fbillstatus, fauditstatus FROM t_homs_orgchgbill WHERE fbillno = 'OBC0001';

-- 2. 看变动主记录是否真写入
SELECT fid, fadminorgid, fcreatetime FROM t_homs_orgchgrecord
  WHERE fid IN (SELECT fid FROM t_homs_orgchgentry WHERE fchgbillid = <申请单id>);

-- 3. 看变动 entry 是否写入
SELECT fentryid, fchgbillid, fchgeffecttime FROM t_homs_orgchgentry
  WHERE fchgbillid = <申请单id>;

-- 4. 看变动 detail 是否写入
SELECT fdetailid, fentryid, fchgentitynumber, fchgpageelement
  FROM t_homs_orgchgdetail WHERE fentryid IN (...);
```

### 3.2 字段级溯源缺失

**症状**: list 上有变动行 · changefield/beforevalue/aftervalue 列全空。

**可能根因**:
- 配对场景写入时没写 t_homs_orgchgdetail 子分录(只写了主表 + entry · 漏 detail)
- chgentitynumber 取值不在标品识别的 3 大类(2.6)
- `before/afterchgentity` 指向已被删除的实体记录

**排查**:
```sql
-- 看 detail 子分录是否完整
SELECT * FROM t_homs_orgchgdetail WHERE fentryid = <entry id>;

-- 看 before/afterchgentity 关联的实体是否存在
SELECT * FROM t_haos_adminorg WHERE fid IN (<beforechgentity>, <afterchgentity>);
```

### 3.3 跨场景跳转 chgbill 不存在

**症状**: 用户点 hyperLinkClick 跳转 · 静默失败(L207 `if (dy == null) return;`) · 不弹错误。

**根因**: 反编译 L206-L209:
```java
DynamicObject dy = helper.queryOriginalOne(CHGBILL, idFilter);
if (dy == null) {
    return;  // 静默不跳
}
```

**用户体验改进**(ISV 扩展):
```java
// 在 ISV 扩展插件 billListHyperLinkClick 里加提示
if (dy == null) {
    getView().showTipNotification("申请单已被废弃 · 无法查看");
    return;
}
```

### 3.4 多语言显示乱码

**症状**: 用户切换语言后 · 变动详情显示的 changefield / beforevalue 仍是中文(没切换)。

**根因**: `formatValue L610-L631` 实证按 `IInteService.getEnabledLang()` 拿启用语言列表。如果用户切换的语言**不在启用列表里** · 不会被识别。

**排查**: 检查租户的"启用语言配置" · 确认要切换的语言在启用列表里。

---

## 4. 性能异常

### 4.1 列表渲染卡顿

**症状**: 列表加载 > 10 秒。

**可能根因**:
- 大数据量场景(>10000 行) · 默认 25/页 · 仍然 buildData 阶段查 4-5 张表
- `t_homs_orgchgentry.fchgeffecttime` 没建索引 · 按时间范围筛全表扫
- `t_homs_orgchgdetail.fchgentitynumber + fchgpageelement` 没建复合索引

**优化方案**:
- DBA 加索引(部署前问 DBA)
- ISV 重写 setFilter 时收紧筛选条件(默认加"近 90 天"过滤)

### 4.2 导出超时

**症状**: 标品导出 (`export_from_list_hr`) 大数据量超时。

**根因**: HIES 导出会跑 packageData 计算列 · 大数据量场景 buildData 多次调用占满时间。

**ISV 扩展**: 在 ISV 自定义导出插件里**跳过字段级溯源计算** · 只导原始字段(适合"快导基础数据"场景)。

---

## 5. 运行时常见错误

### 5.1 `field iscurrentversion not found`

**触发**: ISV 在 setFilter 里 `add(new QFilter("iscurrentversion", "=", true))`。

**修复**: 删掉这行。本场景没有这个字段(2.3)。

### 5.2 `class final · cannot extend`

**触发**: ISV 写 `extends AdminOrgChgRecordListPlugin`。

**修复**: 改为 `extends HRDataBaseList`(2.2)。

### 5.3 `NullPointerException at packageData` (event.getRowData())

**触发**: 用户在空列表上做某种刷新操作 · 触发 packageData 但 rowData 为 null。

**修复**: ISV 重写 packageData 时**第一行就空指针检查**:
```java
if (event.getRowData() == null) return;
```

### 5.4 `setOrderBy 字段不存在`

**触发**: ISV setOrderBy 里写了非 ListColumn 字段(如标品 SEARCH_MAP 的代理字段)。

**修复**: 用真实字段路径(如 "orgchgentry.chgeffecttime") · 不用代理字段(如 "searchdate")。

### 5.5 `OrgPermHelper.getHrPermFilter NullPointerException`

**触发**: `((ListView)getView()).getBillFormId()` 返回 null(脱离 ListView 上下文调用)。

**修复**: ISV 在 OP 端**不要**调 OrgPermHelper(它只能在 FormPlugin 端用) · OP 端走 SDK 的 IUserService 拿权限。

---

## 6. 排查工具速查

| 现象 | 工具 | 命令 |
|---|---|---|
| 看申请单是否生效 | DB 直查 | `SELECT fbillstatus FROM t_homs_orgchgbill WHERE fbillno = ?` |
| 看变动记录是否完整(主+entry+detail) | DB 直查 | 见 §3.1 4 条 SQL |
| 看 ISV 插件是否被注册 | OpenAPI | `query_plugins` 接口 · 看是否含 ISV 类名 |
| 看 RowKey 顺序(标品 vs ISV) | 反编译 | `_auto_plugin_registry.md` 按生命周期方法分组 |
| 看 BEC 事件是否预配置 | 开发平台 | 业务事件管理 · 搜索 eventNumber |
| 看物理表实际写入 | DB 直查 | `SELECT * FROM t_homs_orgchgrecord ORDER BY fcreatetime DESC LIMIT 10` |

---

## 7. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 业务规则 + BEC 判定
- [`03_model_design.md`](03_model_design.md) · 字段语义 + 物理表
- [`05_data_flow.md`](05_data_flow.md) · 数据流转
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-01..CS-06 + 各 CS 末尾的踩坑
- [`08_impact_analysis.md`](08_impact_analysis.md) · 改动风险分级
- [`knowledge/cosmic_realworld_traps/`](../../cosmic_realworld_traps/) · 苍穹平台共性坑(buildmeta / addrule / modifymeta)
