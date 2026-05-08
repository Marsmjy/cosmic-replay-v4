# 业务规则 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 `AdminOrgChgRecordListPlugin.java`（654 行）+ `AdminOrgChgRecordBUListPlugin.java`（22 行）实证
> **confidence**：verified · 反编译类规则全提取 + 配对场景双向引用
> **审计时间**：2026-04-27

---

## 1. 业务规则总览

本场景的"业务规则"主要分 4 大类：
1. **数据可见性规则**（用户能看到什么） · 由 `setFilter` 实证
2. **数据来源约束**（数据从哪来） · 由配对场景 `homs_orgbatchchgbill` 实证
3. **变动记录字段语义规则**（字段含义） · 由 03_model_design.md 实证
4. **BEC 模式判定**（是否发业务事件） · grep 实证

> 标品 `formRule / bizRule = 0` （`_auto_rules_deep.md` 实证）· 所有规则全在反编译插件代码里。

---

## 2. 数据可见性规则（5 条 · 反编译实证）

### 2.1 行政组织数据权限过滤（最强约束）

**规则**：用户只能看到自己有"组织数据权限"的行政组织发生的变动记录。

**实证**：`AdminOrgChgRecordListPlugin.setFilter L159`：
```
qFilterList.add(OrgPermHelper.getHrPermFilter(billFormId, "adminorg.org"));
```

- `OrgPermHelper` 来自 `kd.hr.haos.business.util.OrgPermHelper` · 标品 haos 域工具类（**禁继承** · `cosmic_hr_sdk_whitelist_audit.md`）· 仅可调用静态方法
- 过滤字段是 `adminorg.org`（行政组织所属 BU）· 按用户的"BU 数据权限"映射出可见 BU 集合 · 落到 SQL 的 `adminorg.org IN (...)` 条件
- ISV 想绕过这条权限做"跨 BU 全局看变动"是反模式（违反平台数据安全设计）

### 2.2 行政组织分类约束（otclassify=1010）

**规则**：列表只显示**行政组织分类**的变动 · 不显示其他类型组织（虚拟/项目/矩阵 等）的变动。

**实证**：`AdminOrgChgRecordListPlugin.setFilter L169`：
```
qFilterList.add(new QFilter("adminorg.otclassify.id", "=", 1010L));
```

- `otclassify=1010` 是"行政组织分类"的预置 ID
- ISV 自建场景（如让本场景也能看"项目组织"变动）→ 必须改 setFilter · 但要注意会跨 admin_org_quick_maintenance 边界

### 2.3 默认排序规则

**规则**：列表按 `组织编码 ASC + 变动生效日期 DESC + 操作日期 DESC + 子分录序号 ASC` 排序。

**实证**：`AdminOrgChgRecordListPlugin.setFilter L170`：
```
setFilterEvent.setOrderBy("adminorg.number,orgchgentry.chgeffecttime desc,orgchgentry.operationtime desc,orgchgentry.subentryentity.seq");
```

- 三级排序：先按组织聚合 · 再按变动时间倒序 · 最后按操作时间倒序 · 同申请单内按 entry 顺序展示
- ISV 想改排序 → CS-02 重写 `beforeCreateListColumns`（不要在 setFilter 里覆盖 setOrderBy · 标品兜底 · 易冲突）

### 2.4 跨场景跳转触发的过滤切换

**规则**：如果调用方传 `customParam.needshowdetail` 或 `customParam.needshowsingle` · 列表会清掉默认过滤 · 改为按申请单 ID（+ entry ID）过滤。

**实证**：`AdminOrgChgRecordListPlugin.setFilter L160-L168`：
```
if (... needshowdetail ...) {
    qFilterList.remove(0);
    qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", customParam.billidfilter));
}
if (... needshowsingle ...) {
    qFilterList.remove(0);
    qFilterList.add(new QFilter("orgchgentry.chgbill.id", "=", customParam.billidfilter));
    qFilterList.add(new QFilter("orgchgentry.orgentry", "=", customParam.entryid));
}
```

- 这两个分支是为了从配对场景 `homs_orgbatchchgbill` 跳转过来时显示"该申请单的所有 entry 变动"
- ⚠ `qFilterList.remove(0)` 会清掉数组**第 1 个**过滤条件 · 这条标品代码假设第 0 个就是数据权限过滤 · 如果 ISV 在前面还插了过滤会被误清

### 2.5 BU 左树字段集（控制左树联动哪些查询字段）

**规则**：左树 `AdminOrgChgRecordBUListPlugin.getCtrlBUFieldSet` 返回 11 个字段集 · 决定左树某节点点击时哪些查询字段被自动加上。

**实证**：`AdminOrgChgRecordBUListPlugin.java:17`：
```
"adminorg", "adminorg.org", "searchchangescene",
"adminorg.adminorgtype", "adminorg.parentorg",
"adminorg.corporateorg", "adminorg.belongcompany",
"adminorg.adminorglayer", "adminorg.adminorgfunction",
"changescene", "changereason", "chgbill.disorg.fbasedataid"
```

- 通过继承 `AbstractBUListPlugin`（标品 haos 域 · **禁继承** · `cosmic_hr_sdk_whitelist_audit.md` 不在白名单）· 重写本方法实现 BU 联动
- ISV 想改字段集 → 必须用并列挂插件实现等效效果（不能继承 `AbstractBUListPlugin`）· 但 BU 标品功能跟左树深度绑定 · 实际很难做（所以 CS 列表里没列出）

---

## 3. 数据来源约束（5 条 · 配对场景关联）

### 3.1 数据 100% 来自 `homs_orgbatchchgbill` 生效（核心约束）

**规则**：本场景的 3 张物理表（`t_homs_orgchgrecord` + `t_homs_orgchgentry` + `t_homs_orgchgdetail`）的**唯一标品写入路径**是配对场景 `homs_orgbatchchgbill` 的生效流程。

**实证**：
- 本场景 15 opKey 中**没有 1 个写入 OP**（`save` 是 BaseFormModel 模板兜底 · 用户操作不到）
- 配对场景 `homs_orgbatchchgbill` 的 `OrgBatchChgBillEffectOp` 在 `beginOperationTransaction` 阶段写本场景 3 张表（参考 [05_data_flow.md §2.1](05_data_flow.md)）

**ISV 推论**：
- ISV 想改本场景列表显示哪些变动 · 不在本场景挂插件 · 应该改配对场景 `homs_orgbatchchgbill` 的写入逻辑
- ISV 想加自定义字段的变动溯源 → 必须先在 `homs_orgbatchchgbill` 写入端把新字段的 `chgentitynumber + chgpageelement` 写进 t_homs_orgchgdetail · 否则本场景看不到

### 3.2 写入是事务性的（一致性保证）

**规则**：配对场景生效时 · 写 haos_adminorg + 3 张 homs_orgchg* 是同一事务 · 任一失败一起回滚 · 不会出现"组织变了但变动记录没写"或"变动记录写了但组织没变"。

**实证**：`OrgBatchChgBillEffectOp.beginOperationTransaction` 阶段（参考 `homs_orgbatchchgbill_maintenance/04_business_flow.md` 第三节）。

### 3.3 HIES 导入是补充入口（罕见 · 系统初始化用）

**规则**：opKey `importdata_hr` 通过标品 ImportPlugin 直接写本场景 3 张表 · 用于系统初始化导入历史变动数据 · **不走配对场景**。

**实证**：opKey `importdata_hr` 的 operationType 是 `importdata_hr`（操作清单 L60）· 走 HIES 模板逻辑。

**ISV 注意**：HIES 导入可能跳过部分业务校验（如组织是否存在）· 不建议用 HIES 做日常变动补录。

### 3.4 ISV 直接写入是反模式

**规则**：ISV **不要**直接 INSERT 到 `t_homs_orgchgrecord` 物理表（会破坏 haos_adminorg 的时序版本一致性）。

**正确做法**：
- 走配对场景的标准流程（创建申请单 · 走审批生效）
- 或在配对场景的 OP 链路扩展并列挂插件 · 让标品 OP 自然写入

### 3.5 变动数据不可手动修改

**规则**：本场景列表是**只读** · 没有任何字段可在前端编辑（标品 ListPlugin 没注册任何 propertyChanged 事件 · 也没注册 save OP）。

**实证**：`AdminOrgChgRecordListPlugin` 类没有 `afterPropertyChanged` / `beforeDoOperation` save 拦截 · `afterDoOperation` 只对 refresh 做了空操作（L182-L184）。

**ISV 推论**：想"修正历史变动记录"是反模式（破坏审计完整性）· 应该走配对场景"再发一张反向调整单"的方式表达"修正"语义。

---

## 4. ⛔ BEC 模式判定（核心 · 实证 0 命中）

### 4.1 grep 实证结果

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
     knowledge/_sdk_audit/_decompiled/scenarios/homs_orgchgrecord/

# 结果：0 处命中
```

实证文件覆盖：
- `AdminOrgChgRecordListPlugin.java`（654 行）· 0 处
- `AdminOrgChgRecordBUListPlugin.java`（22 行）· 0 处

### 4.2 判定结论

> **本场景标品不发任何 BEC 业务事件**。

理由：
1. 本场景是**只读查询场景** · 没有写入 OP · 自然没有"业务动作"可触发事件
2. 即使 `import_data_hr` 走 HIES 导入路径 · 标品也没在本场景做事件发布
3. 业务上 · "查询变动记录"是低价值动作 · 不需要发事件

### 4.3 ISV 不要套用 hjm/homs_orgbatchchgbill 的 BEC 模式

⚠️ **铁律**：CS-05 BEC 章节砍掉 · 不为凑数硬写发布方。

如果业务确实需要"组织变更事件"订阅 →
- ✅ 去 `homs_orgbatchchgbill` 配对场景的 audit / submiteffect afterTransDoOp 阶段挂订阅 OP
- ❌ 不要在本场景挂"自建发布方" · 标品没有发布的语义 · ISV 自建发布是错误位置

详见 [06_customization_solutions.md](06_customization_solutions.md) CS-06 末尾的 BEC 注释。

---

## 5. 变动记录字段语义规则（10 条 · 业务约束）

### 5.1 chgeffecttime（变动生效日期）≠ operationtime（操作日期）

**规则**：
- `chgeffecttime` = **业务时间轴**（业务何时生效 · 由用户在申请单填）
- `operationtime` = **系统时间轴**（系统何时执行 · NOW() 自动赋值）

**业务含义**：
- 用户 4/27 操作 · 变动 5/1 生效 → operationtime=4/27 14:30, chgeffecttime=5/1
- 排序时主键用 chgeffecttime · 因为业务关心的是"变动什么时候开始生效" · 不是"什么时候录入"

### 5.2 changescene（变动场景）vs changetype（变动类型）vs changereason（变动原因）

**3 字段语义层次**：

| 字段 | 粒度 | 取值示例 |
|---|---|---|
| `changescene` | 大类 | 组织调整 / 组织合并 / 组织拆分 / 组织停用 |
| `changetype` | 细分 | 上级变更 / 信息变更 / 编码变更 / 名称变更 |
| `changereason` | 业务原因 | 业务调整 / 合规要求 / 战略升级 / 部门重组 |

> 反编译实证：`AdminOrgChgRecordListPlugin.SEARCH_MAP` 把 `searchchangescene.id` 映射到 `orgchgentry.changescene.id`（L145）· 说明搜索面板按 changescene 大类筛 · 不是 changetype 细分。

### 5.3 chgentitynumber 取值约束（3 大类）

**规则**：`subentryentity.chgentitynumber` 字段值有 3 大已知类别（反编译 L384-L398 实证）：

| chgentitynumber | 含义 | 处理逻辑 |
|---|---|---|
| `haos_orgteamcooprel` | 协作组织变动 | 走 `buildCoolChangeVO` · 拿协作类型名 |
| `ADMIN_STRUCT_KEY`（值 = `haos_adminorgstruct` · 来自 `AdminOrgHisDynKey.ADMIN_STRUCT_KEY.getDynKey()`） | 矩阵组织变动 | 走 `buildStructProjectChangeVO` · 拿结构方案名 |
| `haos_adminorgdetail` 等 | 普通行政组织字段变动 | 走 `buildBaseChangeVO` · 直接读字段值 |

**ISV 注意**：如果 ISV 在 `homs_orgbatchchgbill` 写入端加了新的 `chgentitynumber` 取值 · 本场景的字段级溯源**渲染会失败**（changeMap 没有对应记录）· 必须同时在本场景重写 `buildData` 添加新分支 → CS-04。

### 5.4 mergesplitflag 取值约束

**规则**：`mergesplitflag` 字段（在 t_homs_orgchgentry · 反编译 L264 / L274 实证）取值：
- `"1"` = 合并
- `"2"` = 拆分
- 其他 = 普通变动（不走 buildSplitMerge 分支）

### 5.5 fbasedataid（多基础资料字段）的反序列化

**规则**：`mulbasedatafield`（多基础资料）的子集合 · 每条记录的 `fbasedataid` 指向真正的基础资料引用对象。

**实证**：`buildSplitMerge L266-L271`：
```
mulbasedatafield = dy.getDynamicObjectCollection(MUL_BASE_DATA_FIELD);
for (DynamicObject dynamicObject : mulbasedatafield) {
    orgVersionDy = dynamicObject.getDynamicObject(F_BASE_DATA_ID);
    ...
}
```

> 苍穹平台多基础资料字段的标准结构 · ISV 扩展自定义多基础资料字段时必须按此模式遍历。

### 5.6 boid 与 vid 的双层映射（haos_adminorg 时序）

**规则**：合并/拆分场景反编译实证（L286-L291）：

```java
DynamicObjectCollection query = QueryServiceHelper.query("haos_adminorgdetail", "id,boid", new QFilter[]{...});
query.forEach(dyn -> {
    needQueryAdminBoIds.add(dyn.getLong("boid"));
    vidToBoId.putIfAbsent(dyn.getLong("id"), dyn.getLong("boid"));
});
```

> ⭐ **PR-009 实证**：标品在变动溯源时维护 `vid (=id 版本维度) → boid (业务对象维度)` 映射 · 是因为同一个组织有多个历史版本（boid 相同但 id 不同）· 显示时要回溯到同一业务对象。

### 5.7 多语言（ILocaleString）的 diff 算法

**规则**：当字段值类型是 `ILocaleString` · 必须按"启用语言列表"逐语言对比变化（反编译 `buildChangeVO L499-L527` 实证）。

> ISV 扩展时 · 多语言字段的 before/after 对比必须用同样模式（按 IInteService.getEnabledLang() 拿启用语言列表）· 不能直接 `before.toString()` 比较。

### 5.8 ComboField 的取值映射

**规则**：ComboProp 字段（如 `enable` / `mergesplitflag` / `initdatasource`）展示时**取 itemByName** 不是字段值（反编译 `formatValue L606-L609 / L648-L651` 实证）。

> ISV 想加自己的 ComboField 字段溯源 · 必须保证字段元数据上有 ComboItem 配置 · 否则 `comboProp.getItemByName(value)` 返回 null。

### 5.9 BasedataEntityType 的 nameProperty

**规则**：BasedataField 字段的"显示名"取的是元数据上声明的 `nameProperty`（不是固定 `"name"`）（反编译 `formatValue L640-L646` 实证）。

```java
String namePropTmp = ((BasedataEntityType)type).getNameProperty();
String nameProp = StringUtils.isEmpty(namePropTmp) ? "name" : namePropTmp;
return cellValue.getString(nameProp);
```

### 5.10 行政组织的 number 显示规则（去掉前缀）

**规则**：`adminorg` 字段的 number 显示时去掉 `_` 前的部分（反编译 `appendValue L591-L593` 实证）。

```java
if ("adminorg".equals(property) && (index = number.indexOf("_")) != -1) {
    number = number.substring(index + 1);
}
```

> 这是因为 haos_adminorg 的 number 默认带 BU 前缀（如 `BU001_DEPT_HR`）· 显示时只展示业务编码 `DEPT_HR`。ISV 加的自定义 BasedataField 没有这个 case · 不需要套用。

---

## 6. 跨模块约束（5 条）

### 6.1 与 `homs_orgbatchchgbill` 配对（最重要）

| 维度 | 约束 |
|---|---|
| 写入触发 | 仅当配对场景单据进入 C 状态（已生效）· 才写入本场景 3 张表 |
| 数据一致性 | 申请单 + 变动记录在同事务 · 一致性由 `OrgBatchChgBillEffectOp` 保证 |
| 跨场景跳转 | hyperLinkClick 实证：`setFormId("homs_orgbatchchgbill")`（L215） |
| ISV 扩展位置 | ISV 想监听变动事件 → **不**在本场景挂 · 去配对场景 audit/submiteffect afterTransDoOp 挂 |

### 6.2 与 `haos_adminorg` 时序数据

**规则**：本场景 `adminorg` 字段值是组织的 boid · 跟 `t_haos_adminorg` 时序表的 `boid` 列对应。任何想"按组织看变动历史"的查询都用 boid 维度（PR-009）。

**ISV CS-03 / CS-06 都依赖此规则**：跨场景跳转传 boid · 不是 id。

### 6.3 与 `haos_adminorgdetail` / `haos_orgteamcooprel` / `haos_structproject`

**规则**：字段级变动溯源（subentryentity）会查这 3 张真实数据表 · 拿 before/after 字段值（反编译 L379-L406 实证）。

**ISV 注意**：如果 ISV 改了这 3 张表的字段元数据（加字段 / 改类型）· **本场景的字段级溯源会自动适配**（标品按 propertySet 动态 select · 不是写死字段名）· 但要保证 propertySet 里的字段在新元数据上还存在。

### 6.4 与 HRMP 平台 OrgPermHelper（数据权限）

**规则**：本场景的数据可见性完全由 `OrgPermHelper.getHrPermFilter` 决定（参考 §2.1）· 跟标品 HR 数据权限模型一致。

**ISV 注意**：不要在 `setFilter` 里 `qFilterList.removeIf(...)` 删除权限过滤（违反平台数据安全设计）。

### 6.5 与多语言服务（IInteService）

**规则**：变动溯源的 ILocaleString 处理依赖 `kd.bos.inte.api.IInteService` 拿启用语言列表（反编译 L494 实证）。

**ISV 自建场景**：使用 ILocaleString 字段时必须遵循同样的"按启用语言遍历"模式 · 不要假设单语言。

---

## 7. 用户操作权限速查表

| 操作 | 普通用户 | 业务管理员 | 系统管理员 |
|---|---|---|---|
| 查列表 | ✅（按 BU 数据权限） | ✅ | ✅ |
| 翻页（first/previous/next/last） | ✅ | ✅ | ✅ |
| 刷新（refresh） | ✅ | ✅ | ✅ |
| 关闭（close） | ✅ | ✅ | ✅ |
| 跳转申请单（hyperLinkClick） | ✅ | ✅ | ✅ |
| 导出列表（export_from_list_hr） | ✅（受 permissionItemId 控制） | ✅ | ✅ |
| HIES 导入（importdata_hr） | ❌ | ✅（看权限项配置） | ✅ |
| 查看导入/导出记录 | ✅ | ✅ | ✅ |

---

## 8. 关联文档

- [`01_capability_boundary.md`](01_capability_boundary.md) · 能力边界（能干啥/不能干啥）
- [`03_model_design.md`](03_model_design.md) · 字段元数据 + 物理表
- [`04_business_flow.md`](04_business_flow.md) · 业务流程 + 跨场景跳转
- [`05_data_flow.md`](05_data_flow.md) · 写入路径详解
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/02_business_rules.md`](../homs_orgbatchchgbill_maintenance/02_business_rules.md) · ⭐ 配对场景业务规则（必对照读）
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-008 / PR-009 / PR-010 / PR-011
