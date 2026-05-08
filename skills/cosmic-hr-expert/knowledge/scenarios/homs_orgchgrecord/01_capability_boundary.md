# 能力边界 · 组织变动明细查询（homs_orgchgrecord）

> **状态**：基于 2 反编译类（AdminOrgChgRecordListPlugin 654 行 + AdminOrgChgRecordBUListPlugin 22 行）+ 15 opKey 实证
> **confidence**：verified · 配对场景 `homs_orgbatchchgbill` 双向边界已划清
> **审计时间**：2026-04-27

---

## 1. 场景一句话

按 `chgbill` 维度（关联 `homs_orgbatchchgbill` 申请单）查询 `t_homs_orgchgrecord + t_homs_orgchgentry + t_homs_orgchgdetail` 三张表里**已生效的组织变动明细** · 提供 BU 左树筛选 / 跨语言变动溯源 / 跳转申请单详情 / HIES 导入导出 等只读查询能力 · **本场景没有任何写入 OP** · 数据全靠配对场景 `homs_orgbatchchgbill` 生效写入。

---

## 2. ✅ 能干啥（实证支撑）

### 2.1 列表查询（核心 · 95% 用例）

| 能力 | 反编译实证 | opKey |
|---|---|---|
| 按行政组织看变动列表 | `AdminOrgChgRecordListPlugin.setFilter L155-L171` | （隐式 list 加载） |
| 按变动生效日期范围筛 | `setFilter` 走 SEARCH_MAP `searchdate → orgchgentry.chgeffecttime` (L144) | （搜索面板字段） |
| 按变动场景筛 | `searchchangescene.id → orgchgentry.changescene.id` (L145) | （搜索面板字段） |
| 按申请单编号筛 | `searchbillno → orgchgentry.chgbill.billno` (L146) | （搜索面板字段） |
| 按发文编号 / 发文名称筛 | `searchdispatchno / searchdispatchname → orgchgentry.chgbill.dispatchnumber / dispatchname` (L147-L148) | （搜索面板字段） |
| 按组织简称筛 | `seatchsimple → adminorg.simplename` (L149) | （搜索面板字段） |
| 按 BU 左树点击筛 | `AdminOrgChgRecordBUListPlugin.getCtrlBUFieldSet` 11 字段集 | （左树联动） |
| 自定义排序 | `setFilterEvent.setOrderBy("adminorg.number, orgchgentry.chgeffecttime desc, ...")` (L170) | sort（默认） |
| 翻页 | first / previous / next / last 4 个 opKey · 标品兜底 | first/previous/next/last |
| 刷新 | `afterDoOperation L182-L184` 拦截 refresh（实际是空操作 · 走标品默认）| refresh |
| 列宽固定 | `beforeCreateListColumns L224-L229` 把 adminorg.number/name 设为固定列 | （隐式） |

### 2.2 跨场景跳转

| 能力 | 反编译实证 | 跳转目标 |
|---|---|---|
| 点列表行 → 跳申请单详情 | `billListHyperLinkClick L200-L218` | `homs_orgbatchchgbill`（配对场景）VIEW 状态 |
| 配对场景反向跳本场景（聚合）| `setFilter` customParam.needshowdetail (L160-L163) | 触发"按申请单聚合视图" |
| 配对场景反向跳本场景（单 entry）| `setFilter` customParam.needshowsingle (L164-L168) | 触发"单 entry 视图" |

### 2.3 字段级变动溯源（最复杂能力 · 内存计算）

| 能力 | 反编译实证 | 底层方法 |
|---|---|---|
| 显示变动字段中文名（changefield 列）| `packageData L341 → buildBaseChangeVO L489` | `setDisplayName` |
| 显示变动前值（beforevalue 列）| `packageData L346 → formatValue L601` | `getBeforeValue` |
| 显示变动后值（aftervalue 列）| `packageData L350 → formatValue L601` | `getAfterValue` |
| 显示带编码的"名称(编码)"（before/afternamenumber 列）| `packageData L353-L358 → appendValue L576-L595` | `getBefore / getAfter` |
| 多语言字段 diff（按启用语言对比）| `buildChangeVO L499-L527` | `IInteService.getEnabledLang` 实证 |
| 协作组织变动（haos_orgteamcooprel）| `buildData L384-L389 → buildCoolChangeVO L459` | 查 haos_teamcoopreltype 拿协作类型名 |
| 矩阵组织变动（haos_adminorgstruct）| `buildData L390-L395 → buildStructProjectChangeVO L463` | 查 haos_structproject 拿结构方案名 |
| 普通字段变动 | `buildData L396-L399 → buildBaseChangeVO L467` | 直接查 chgentitynumber 实体 |
| 合并/拆分概览（mergesplitview 列）| `buildSplitMerge L254-L302 → packageData L313-L316` | StringBuilder 拼装"合并前组织 / 合并后组织" |
| 父组织长名称（parentlongname 列）| `OrgFullNameServiceWrapper.getBatchOrgFullName L237-L243` | 批量查父组织完整路径名 |

### 2.4 数据导入/导出（HIES + 平台）

**HIES 体系（6 个 opKey）**：
- `importdata_hr` 导入数据（系统初始化用 · 罕见）
- `show_import_record_hr` 查看导入记录
- `export_from_list_hr` 按列表导出
- `export_from_impttpl_hr` 按导入模板导出
- `export_from_expttpl_hr` 按导出模板导出
- `show_export_record_hr` 查看导出记录

**平台体系（2 个 opKey）**：
- `exportlistbyselectfields` 导出数据（按列表）· 受 `permissionItemId=4730fc9f000004ae` 权限项控制
- `exportdetails` 查看导出结果 · 同样权限项

### 2.5 列表外壳辅助操作

`refresh`（刷新）/ `close`（关闭）。

### 2.6 标准基础资料生命周期 opKey（**本场景不主用 · 仅模板兜底**）

`save` 这 1 个是 BaseFormModel 模板兜底注册 · 在本场景**走的概率为 0**（详见 §3.1）。

---

## 3. ❌ 不能干啥（关键边界）

### 3.1 不能在本场景修改 / 删除变动记录

- **理由**：这是**只读历史溯源场景** · 变动记录是审计数据 · 修改它会破坏数据完整性
- **反编译实证**：`AdminOrgChgRecordListPlugin` 类没有任何 `propertyChanged` / `beforeDoOperation save` 拦截 · `afterDoOperation L182-L184` 只对 refresh 做了空操作
- **想表达"修正"的语义** → 不在本场景做 · 走配对场景 `homs_orgbatchchgbill` 再发一张反向调整单（保留正反向审计完整性）

### 3.2 不能从本场景发起新的组织变动申请

- **理由**：本场景是**只读端** · 写入端是 `homs_orgbatchchgbill`
- **正确路径**：菜单 → 行政组织维护 → 行政组织维护 → **组织调整**（配对场景）→ 新建申请单 → 走审批生效 → 数据自动落到本场景
- ISV 想加"快速发起新申请"按钮 → 在本场景列表加 toolbar 按钮跳转配对场景（参考 [06_customization_solutions.md CS-03](06_customization_solutions.md)）

### 3.3 不能在本场景上发起 BEC 业务事件

- **反编译产物 grep 结果：0 处** `triggerEventSubscribe / IEventService / EventServiceHelper`
- 标品没有在本场景 OP 链发 BEC（变动事件由 `homs_orgbatchchgbill` 的 `audit / submiteffect` 链路负责发布）
- ISV 想发"变动记录被查询过"事件 → 自建发布方但**强烈不推荐**：查询是高频低价值动作 · 别把 BEC 队列灌爆

### 3.4 不能挂操作 OP 拦截 `save`

- 标品 `save` opKey 在本场景**根本不会被触发**（用户没有"新建/编辑"入口 · 只有列表）
- ISV 想挂 save OP 做校验 → 不要在本场景挂 · 去配对场景 `homs_orgbatchchgbill` 的 save / submit / audit / submiteffect OP 挂

### 3.5 不能跨语言修改字段值

- 反编译实证：所有字段值是只读列表展示 · `formatValue` 是格式化函数 · 不能写回
- 想做"变动记录翻译" / "变动说明替换" → 走 ResManager 多语言（按启用语言展示原值）· 不要在 packageData 里硬塞翻译

### 3.6 不能直接看到不属于自己 BU 的组织变动

- 反编译实证：`setFilter L159` 加 `OrgPermHelper.getHrPermFilter` 强制按用户的"组织数据权限"过滤
- ISV 不能 `qFilterList.removeIf(filter -> filter.getProperty().contains("hrperm"))` 绕过权限（违反平台数据安全设计）
- 想做"全局看变动" → 平台数据权限管理员给用户分配"全 BU 权限"（不是代码绕过）

### 3.7 字段级溯源不能查"自定义实体的变动"（除非 ISV 在配对场景写入）

- 反编译实证：`buildData L379-L406` 只识别 3 类 chgentitynumber：`haos_orgteamcooprel` / `ADMIN_STRUCT_KEY` / 其他通用
- ISV 加自定义字段（如 `${ISV_FLAG}_costcenter`）的变动 → 必须先在 `homs_orgbatchchgbill` 的写入端把 `chgentitynumber=haos_adminorgdetail, chgpageelement=${ISV_FLAG}_costcenter` 写到 t_homs_orgchgdetail · 否则本场景看不到溯源

### 3.8 不能用 `searchchangescene` 同时筛多个变动场景

- 反编译实证：`SEARCH_MAP` 只映射 `searchchangescene.id → orgchgentry.changescene.id`（L145）· 单值映射 · 不支持 in
- ISV 想加"多选变动场景筛选" → CS-01 自定义筛选字段 + 重写 setFilter（用 in 替代 equals）

---

## 4. 边界条件矩阵

| 场景 | 入口 form | 写入路径 | 是谁的事 |
|---|---|---|---|
| 发起新组织变动申请 | `homs_orgbatchchgbill`（配对场景）| save → submit → audit → submiteffect | `homs_orgbatchchgbill` |
| 查看已生效申请单的变动明细 | `homs_orgchgrecord`（本场景） | （只读 · 数据来自配对场景生效）| `homs_orgchgrecord` |
| 字段级变动溯源 | `homs_orgchgrecord`（本场景）| （只读 · 内存计算）| `homs_orgchgrecord` |
| 当前版本组织维护 | `haos_adminorgdetail`（admin_org_quick_maintenance）| save / confirmchange | `admin_org_quick_maintenance` |
| 历史版本查询（区分维度）| `haos_adminorghis` | his_save（罕见）+ HIES 导入 | `haos_adminorghis` |
| 跨场景跳到申请单 | hyperLinkClick → `homs_orgbatchchgbill` | 反编译 L215 实证 | 跨场景 |

---

## 5. 三大常见错觉澄清

### 错觉 1：本场景能修改变动记录

- ❌ 实际：只读列表 · 没有任何 save / edit / delete OP
- ✅ 正确认知：变动记录是审计数据 · 想表达修正走配对场景"再发一张反向调整单"

### 错觉 2：本场景能监听 BEC 事件订阅"组织变更"

- ❌ 实际：本场景标品**不发**任何 BEC（grep 0 处）· 监听本场景的事件等于永远收不到
- ✅ 正确认知：去配对场景 `homs_orgbatchchgbill` 的 audit / submiteffect afterTransDoOp 阶段挂订阅 OP（参考配对场景的 06_customization_solutions.md）

### 错觉 3：可以靠 setFilter 改成 `iscurrentversion=true` 让本场景查"当前组织"

- ❌ 实际：本场景**没有 iscurrentversion 字段**（不是 HisModel · 不走时序）· QFilter 加这条会**报字段不存在错误**
- ✅ 正确认知：要查当前版本组织 → 用 `admin_org_quick_maintenance` 入口 · 别在本场景里硬掰

---

## 6. 与 `homs_orgbatchchgbill` 配对场景的对偶清单（必对照读）

| 维度 | `homs_orgbatchchgbill`（写入端） | `homs_orgchgrecord`（本场景·只读端） |
|---|---|---|
| 入口表单 | `homs_orgbatchchgbill`（list + 详情）| `homs_orgchgrecord`（list 唯一） |
| 主物理表 | `t_homs_orgchgbill`（22 列）| `t_homs_orgchgrecord`（5 实质列） |
| 时序模型 | 否 · BillFormModel | 否 · BaseFormModel（无 boid/iscurrentversion） |
| 写入 opKey | save / submit / audit / submiteffect / discard_row / breakup（73 opKey）| 无（15 opKey 全为只读 / 导出 / 翻页 / 刷新 / 关闭） |
| 主插件 | OrgBatchBillSaveOp / OrgBatchBillSubmitOp / OrgBatchChgBillEffectOp / 等 8 类 | AdminOrgChgRecordListPlugin（654 行 · 唯一深度类） |
| BEC 发布 | submiteffect / audit afterTransDoOp 链路（**待标品配置确认**） | **不发**（grep 0 处） |
| 业务高频 | 中（按申请发起） | 中（变动溯源 · 审计 · 查询导出） |
| ISV 扩展频度 | 高（CS 含字段、流程、校验、跳转）| 中（主要是列表筛选定制 + 跳转扩展 + 变动溯源美化） |
| ISV 监听变动事件位置 | ⭐ 在这里挂 OP afterExecuteOperationTransaction | ❌ 不在这里挂 |

---

## 7. 与 `haos_adminorghis` 的差异（同样只读但模式不同）

| 维度 | `haos_adminorghis` | `homs_orgchgrecord`（本场景） |
|---|---|---|
| ModelType | BaseFormModel | BaseFormModel |
| 是否 HisModel | ✅ 是（boid/iscurrentversion/hisversion）| ❌ 否（无时序字段） |
| 物理表共享 | ✅ 共用 t_haos_adminorg（与 admin_org_quick_maintenance）| ❌ 独立 t_homs_orgchgrecord 等 3 表 |
| 数据来源 | his_save / HIES 导入（写入到本表）| 配对场景生效驱动写入（不在本场景写） |
| 主插件 | HisModelListCommonPlugin / HisModelF7ListPlugin（标品 final）| AdminOrgChgRecordListPlugin（场景专属） |
| 时序过滤 | 强制 iscurrentversion=false | 强制 adminorg.otclassify.id=1010 |
| 跨场景跳转目标 | （场景内自循环） | hyperLinkClick → homs_orgbatchchgbill |

---

## 8. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · 数据可见性 5 条 + 数据来源 5 条 + BEC 判定 + 变动字段语义 10 条
- [`03_model_design.md`](03_model_design.md) · 32 字段 + 3 物理表 + 配对场景对偶
- [`04_business_flow.md`](04_business_flow.md) · 用户使用流程 + 跨场景跳转
- [`07_ext_points.md`](07_ext_points.md) · 15 opKey 扩展点矩阵 + 7 plugin 扩展位置
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/01_capability_boundary.md`](../homs_orgbatchchgbill_maintenance/01_capability_boundary.md) · ⭐ 配对场景能力边界（必对照读）
- [`knowledge/scenarios/haos_adminorghis/01_capability_boundary.md`](../haos_adminorghis/01_capability_boundary.md) · 同应用域只读场景参考
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-001 / PR-005 / PR-008 / PR-009 / PR-011
