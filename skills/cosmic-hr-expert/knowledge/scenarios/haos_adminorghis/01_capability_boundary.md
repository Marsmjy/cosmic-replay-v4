# 能力边界 · 组织历史查询（haos_adminorghis）

> **状态**：基于 7 个反编译类（HisModelF7ListPlugin · HisModelFormCommonPlugin · HisModelListCommonPlugin · AdminOrgDetailEditPlugin · NewAdminorgDetailEditPlugin · OrgDetailList · AdminOrgPageRightDynamicPlugin）+ 36 opKey 实证
> **confidence**：verified · 物理表与 admin_org_quick_maintenance 共用
> **审计时间**：2026-04-27

---

## 1. 场景一句话

按 `boid` 维度查 `t_haos_adminorg` 物理表里 `iscurrentversion=false` 的行政组织历史版本，外加历史补录（`his_save`）和 HIES 导入（`chargepersonimpo_hr`）写入入口；菜单"组织历史查询"挂的入口表单是 `haos_adminorghis`，但**主物理表是 `t_haos_adminorg`**（与 admin_org_quick_maintenance 共用）。

---

## 2. ✅ 能干啥（实证支撑）

### 2.1 历史版本查询（核心 · 90% 用例）

| 能力 | 反编译实证 | opKey |
|---|---|---|
| 按 boid 看一个组织全部历史版本 | `HisModelListCommonPlugin.setFilter L173-L181`：`HisPageEnum != NOT_HIS_PAGE` 时强制追加 `iscurrentversion=false` | （隐式 list 加载）|
| 历史版本按 hisversion 倒序排 | `HisModelListCommonPlugin.beforeCreateListColumns L118-L127`：`VERSION_LIST_PAGE` 模式下 `hisversion` setOrder DESC | sort |
| 间断时序模式按 bsed/modifytime DESC | 同上 L121-L123：`HisModelTypeEnum.NO_INTERRUPTION_NO_OVERLAP` 时按 bsed DESC + modifytime DESC | sort |
| 看单个历史版本详情 | `AdminOrgDetailEditPlugin.beforeBindData L184-L246`：`adminorg_operation` = null 走 VIEW 分支 · `bar_close` 显示 | view |
| 按生效日期范围筛历史 | `HisModelF7ListPlugin.beforeBindData L189-L210`：`f7effdatestart` / `f7effdateend` 字段 | （F7 模式）|
| 按 effectdate 单点筛 | `HisModelF7ListPlugin.propertyChanged L153-L157`：监听 effectdate 字段变更 → `listView.refresh()` | （F7 模式）|
| 翻页 / 跳第一最后 | first / previous / next / last 4 个 opKey · 标品 BdVersionListPlugin 处理 | first/previous/next/last |
| 名称改名链查询 | `namehistoryview` opKey · 平台 `BaseDataNameVersionListPlugin` | namehistoryview |

### 2.2 历史补录（写入唯一入口 · `his_save`）

**触发路径**：从外部导入向导（HIES）或租户初始化时挂的"历史数据补录" UI · 不是用户日常用。

| 能力 | 反编译实证 | extension point |
|---|---|---|
| 注册 10 个 Validator | `AdminOrgInitSaveOp.onAddValidators` · `rules_chain_all.json his_save.validators[V_AdminOrgInitSaveOp_1..10]` | onAddValidators |
| 进事务前预处理 | `beforeExecuteOperationTransaction L70-L71` | beforeExecuteOperationTransaction |
| 主业务写入 | `beginOperationTransaction L73-L74` | beginOperationTransaction |
| 收尾读 bsed 字段 | `endOperationTransaction L76-L84 readFields=[bsed]` | endOperationTransaction |

10 个 Validator 涵盖：归属公司（OrgHisBelongCompanyValidator）/ 生效日期连续性（OrgHisEffDateContinuityValidator）/ 生效日期合法性（OrgHisEffDateLegitimacyValidator）/ 失效日期范围（OrgHisEndDateRangeValidator）/ 失效日期（OrgHisEndDateValidator）/ 首次生效日期一致性（OrgHisFirstEffDateConsistencyValidator）/ 已迁移（OrgHisMigratedValidator）/ 当前版本父组织（OrgHisOrgCurrVerParentValidator）/ 组织错误（OrgHisOrgErrValidator）/ 父组织（OrgHisOrgParentValidator）。

### 2.3 导出（HIES 体系 · 6 个 opKey）

按列表导出（export_from_list_hr）/ 按导入模板导出（export_from_impttpl_hr）/ 按导出模板导出（export_from_expttpl_hr）/ 查看导出记录（show_export_record_hr）/ 导出数据按列表（exportlistbyselectfields）/ 导出数据按模板（exportlist / exportlist_expt）/ 查看导出结果（exportdetails）/ 导入导出个性化设置（importexport_userset）。

### 2.4 HIES 数据导入（chargepersonimpo_hr）

opType = `importdata_hr`，触发标品导入向导。**导入也是写入路径**——但走 ImportPlugin 不走 OP 直接写。

### 2.5 移动端筛选（mobtoolbar）

mobtoolbarselect / mobtoolbarcancel · 移动端 H5 端筛选确认 / 取消。

### 2.6 列表外壳辅助操作

refresh（刷新）/ option（选项设置）/ close（关闭）/ returndata（返回数据 · F7 选完返回）/ cancel（取消）/ addnew（新增组织 · opType=new · ⚠ 注意见 §3.5 边界）。

### 2.7 标准基础资料生命周期 opKey（**本场景不主用 · 仅模板兜底**）

submit / unsubmit / audit / unaudit / disable / enable / delete / saveandnew / submitandnew · 这 9 个是 HRBaseDataTplEdit / HRBaseDataTplList 父模板兜底注册的 · 在历史查询表单上**走的概率为 0**（详见 §3.1 不能干啥）。

---

## 3. ❌ 不能干啥（关键边界）

### 3.1 不能在历史版本上 save / audit / disable / enable

- **理由**：这是**只读历史查询场景** · 历史版本（`iscurrentversion=false`）数据已经定型 · 修改它会破坏时序链表
- **想改组织 → 走 admin_org_quick_maintenance**（同物理表 · 但筛 `iscurrentversion=true`）· 通过 `confirmchange` 走变更 · 平台自动派发新版本
- 反编译实证：`HisModelListCommonPlugin.packageData L142-L165` · `dataStatus = TEMP` 才显示 modify/hiscopy/confirmchange 按钮 · 其他状态只显示 hiscopy/revise · **save 按钮根本不在 actionPanel**

### 3.2 不能修改某条历史版本的字段值

- 历史版本上 `AdminOrgDetailEditPlugin.beforeBindData L186-L191` 走 VIEW 分支：所有保存类按钮（new_save / change_save / parent_save / newentry / deleteentry / cancel）默认 `setVisible(false)`
- 想"补正历史" → 用 `revise`（修订）走 `HisModelFormCommonPlugin.afterDoOperation case "revise" L198-L209` · 平台会**复制一份新版本** · 把当前版本的 sourcevid 链上去 · 不是原地改

### 3.3 不能在历史版本上发起 BEC 业务事件

- 反编译产物 grep 结果：**0 处** `triggerEventSubscribe / IEventService / EventServiceHelper`
- 标品没有在本场景 OP 链发 BEC（变更事件由 `admin_org_quick_maintenance` 的 `confirmchange` 链发 · 见 admin_org_quick_maintenance CS-04）
- ISV 想发"历史版本被查询过"事件可以自建发布方 · 但建议不要 · 查询是高频低价值动作 · 别把 BEC 队列灌爆

### 3.4 不能挂 list 的 sort 操作做"自定义历史排序逻辑"

- sort 是 `donothing` opType · 实际排序在 `HisModelListCommonPlugin.beforeCreateListColumns L118-L139` 由 ListColumn.setOrder 写死
- 想换排序顺序 → CS-02 重写 `beforeCreateListColumns` 改 ListColumn 的 SortType · 不是挂 sort opKey

### 3.5 addnew (opType=new) 在本场景实际不会触发

- 历史查询页不应该新增组织 · "新增组织"应该去 admin_org_quick_maintenance 入口
- 反编译 `OrgDetailList.beforeDoOperation` 没拦 addnew · 但 `HisModelListCommonPlugin.beforeShowBill L189` 强制把弹出表单参数 `hisPage = VERSION_PAGE` · addnew 在历史模式下底层 OP（`AdminOrgFastSaveOp` · 标品禁继承）要求当前版本不存在才能创建 · 这条路在历史查询场景实际跑不通
- 业务上：标品按钮在 `HisModelListCommonPlugin.beforeBindData L98-L105` 里被 `listProcessor.handleBtnVisible()` 隐藏 · 用户根本看不到此按钮（除非二开把它拉出来）

### 3.6 不能跨 boid 批量写历史

- `his_save` 走 `AdminOrgInitSaveOp` · OP 单条事务 · OrgHisBelongCompanyValidator / OrgHisEndDateRangeValidator 都是单实体校验 · 没有"跨 boid 批量补录"语义
- 真要批量补录历史 → 走标品 HIES 导入向导 `chargepersonimpo_hr` · 一次最多 5000 条（HIES 标品限制）

### 3.7 不能从历史版本反向影响当前版本字段

- 时序模型铁律：`boid` 链表只能往前推（用 `revise` 派生新版本）· 不能回写
- 想让历史版本的某个字段值"传到当前版本" → 错误模式 · 应该重新走 admin_org_quick_maintenance 的 `confirmchange` 改当前版本 · 自动生成新历史快照

---

## 4. 边界条件矩阵

| 场景 | 入口 form | 物理表过滤 | 写入路径 | 谁的事 |
|---|---|---|---|---|
| 当前版本日常维护 | `haos_adminorgdetail` | `iscurrentversion=true` | save / confirmchange / disable / enable | admin_org_quick_maintenance |
| 历史版本查询（本场景）| `haos_adminorghis` | `iscurrentversion=false` | **无写入** | haos_adminorghis |
| 历史版本补录（罕见）| `haos_adminorghis` | （写入 t_haos_adminorg）| `his_save` opKey · 10 Validator | haos_adminorghis · 见 §2.2 |
| HIES 导入历史 | （ImportPlugin）| 写 t_haos_adminorg + bo_chg 关联表 | `chargepersonimpo_hr` | haos_adminorghis 协同 HIES |
| 单点穿透看历史 | `haos_adminorghis` 详情页 | `id=<某历史 id>` 单条 | 只读 | haos_adminorghis |

---

## 5. 三大常见错觉澄清

### 错觉 1："历史查询"页修改字段会改历史

- ❌ 实际反编译：详情页 VIEW 模式 · `setEnable(false)` 全部主表字段（`AdminOrgDetailEditPlugin.beforeBindData L186-L191`）· 用户根本编辑不了
- ✅ 正确认知：历史版本是只读的 · 只能 view / revise（派生新版本）/ 看 reviserecord（修订记录）

### 错觉 2：本场景有自己独立的 t_haos_adminorghis 物理表

- ❌ 实际：scene_doc.json `physicalTable = t_haos_adminorg + t_haos_adminorg_l` · **没有** `t_haos_adminorghis`
- ✅ 正确认知：`haos_adminorghis` 是 BaseFormModel 视图 · 共用 admin_org / adminorgdetail 主物理表 · 通过 `iscurrentversion` 区分历史与当前

### 错觉 3：可以靠 setFilter 改成 `iscurrentversion=true` 让本场景查当前数据

- ❌ 实际不能：`HisModelListCommonPlugin.setFilter L177-L178` 是 `final` 类的 `final` 方法 · 强制写死 `iscurrentversion=false` · ISV 重写了也跑在标品后面（PR-002 RowKey 顺序）· 标品最后一个 add 会覆盖
- ✅ 正确认知：要查当前版本 → 用 admin_org_quick_maintenance 入口 · 别在本场景里硬掰

---

## 6. 与 admin_org_quick_maintenance 的对偶清单

| 维度 | admin_org_quick_maintenance | haos_adminorghis（本场景）|
|---|---|---|
| 入口表单 | `haos_adminorgdetail`（list）+ `haos_adminorgtablist`（带页签）| `haos_adminorghis` |
| 主物理表 | `t_haos_adminorg` + `_l` | `t_haos_adminorg` + `_l`（**同**）|
| 时序过滤 | `iscurrentversion=true` | `iscurrentversion=false`（HisModelListCommonPlugin.setFilter L177-L178）|
| 写入操作 | save / confirmchange / disable / enable / 14 opKey | his_save（罕用）+ chargepersonimpo_hr（HIES）|
| 主插件 | `OrgDetailList` / `AdminOrgDetailListPlugin` / `AdminOrgFastSaveOp`(等) | `HisModelF7ListPlugin` / `HisModelFormCommonPlugin` / `HisModelListCommonPlugin` |
| BEC 发布 | confirmchange 链 afterTransDoOp 发组织变更事件 | **不发**（grep 0 处）|
| 业务高频度 | 高（日常维护）| 低（按需查史）|
| ISV 扩展频度 | 高（CS-01..CS-08 高频改）| 中低（主要是 list 列定制 + 历史版本对比 + 历史补录前校验）|

---

## 7. 关联文档

- `02_business_rules.md` · HisModel 时序规则全集（boid / iscurrentversion / hisversion / sourcevid 语义）
- `03_model_design.md` · 共用物理表 + 85 字段时序分组
- `09_cases.md` · "我的数据丢了" → 实际是历史版本，引导用本场景查
- `knowledge/scenarios/admin_org_quick_maintenance/` · 同应用同物理表的对偶场景（**强烈建议对照读**）
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009（时序铁律）
