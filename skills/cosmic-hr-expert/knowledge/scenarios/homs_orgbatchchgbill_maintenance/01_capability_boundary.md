# 能力边界 · 组织调整申请单

> **状态**: 🟢 基于 73 opKey + 8 反编译类 + 标品 24 插件 全量梳理
> **confidence**: verified
> **数据源**: `rules_chain_all.json` + `_auto_plugin_registry.md` + 8 反编译 Java + 标品菜单实探

---

## 一、定位（一句话说清楚）

`homs_orgbatchchgbill` = **行政组织变动批量申请单** · 通过审批流批量调整 / 合并 / 拆分 / 停用组织 · 生效后落地到 `haos_adminorg` 主数据。

⚠️ **不是基础资料** · 是 **`BillFormModel` 单据** · 跟 `haos_adminorg`（HisModel 时序基础资料）模式截然不同。详见 [03_model_design.md](03_model_design.md) §一。

---

## 二、10 条平台级硬规则（必记 · 跨场景适用）

| # | 规则 | 影响 |
|---|---|---|
| 1 | `number` 字段创建后**不可修改** | 下游所有引用都绑定 number |
| 2 | 字段 key ≤ **24** 字符 | 数据库列名上限 |
| 3 | 实体 key ≤ **36** 字符 | 数据库表名上限 |
| 4 | 表名 ≤ **25** 字符 | 数据库硬上限 |
| 5 | 系统计算字段禁止手改 | `creator` / `createtime` / `modifier` / `modifytime` / `id` |
| 6 | `errorCode="0"` ≠ 成功 | 必须二次验证 `getFormSchema` / `list_rules` |
| 7 | `op` 枚举只 4 值 | `add` / `modify` / `remove` / `move` |
| 8 | `actionType` / `fieldKey` 必须 PascalCase | 小写静默忽略 |
| 9 | `updateOperation.plugins` 是全量替换 | 必须先 get 再 append |
| 10 | `registerPlugin.targetType` 必须大写枚举 | `BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT` |

---

## 三、✅ 标品原生支持（按 7 entry 分能力）

### 3.1 列表展示

- ✅ 单据列表（按 BU 过滤 + 创建时间 + 单据编号倒序 · `setFilter` L116）
- ✅ 7 大变动类型聚合显示（addcount / parentcount / infocount / disablecount / mergecount / splitcount + entryentity_all all-count）
- ✅ 点击聚合数 → 展开"该单 X 类组织详情"浮层（弹 `homs_orgbatchbilltips` / merge tip / split tip 表单）
- ✅ 列表批量废弃（`discard_row` 可选多条）
- ✅ 列表终止（`breakup` 一次只能 1 条）
- ✅ 列表删除（仅 A/G 状态）
- ✅ 列表 7 大 HIES 操作（导入数据 / 查看导入记录 / 按列表导出 / 按导入模板导出 / 按导出模板导出 / 查看导出记录 / 导入多分录数据）
- ✅ 数据权限过滤（按 BU 限制可见组织）

### 3.2 entryentity_add · 新增组织 ⭐

- ✅ 一次性新增多个组织（25 字段）· 可不填编码（提交时按法人编码规则自动生成）
- ✅ 上级组织选择（HRAdminOrgField · 排除虚拟组织 + 排除根 + 数据权限过滤）
- ✅ 行政组织类型 / 法律实体 / 管理层级 / 行政职能 / 国家 / 城市 / 工作地 / 行业类别 / 详细地址 / 描述 全字段维护
- ✅ 同申请单内 add 组织可被 parent / merge_target / split_target 引用（生效时 boid → vid 自动替换）
- ❌ 不支持自动复制现有组织作为模板新建（每条都要手填）

### 3.3 entryentity_parent · 调整上级 ⭐

- ✅ 可同时改 `parent_number`（编码）· 跟 admin_org_quick 单纯调上级不同
- ✅ 上级变更带"待停用标记"联动（`parent_tobedisableflag`）
- ✅ 自动算 `parent_oriparentorg`（原上级）· UI 展示历史溯源
- ✅ 上级 F7 自动排除根组织 + 自身 boid + 虚拟组织
- ❌ 不支持"调上级 + 跨 BU 移动"（admin_org 不允许跨租户）

### 3.4 entryentity_info · 信息变更

- ✅ 27 字段全量维护（除 number/parentorg 外全可改）
- ✅ 必填：`info_adminorg` / `info_name` / `info_number` / `info_changescene` / `info_changetype` / `info_adminorgtype` / `info_org`
- ⚠️ 注意 `info_number` **必填** · 这是 admin_org 直改主数据**不允许**改 number 的反例（申请单允许）

### 3.5 entryentity_disable · 停用组织

- ✅ 批量停用多组织
- ✅ disable_changescene 必选 · 走"停用"变动场景
- ✅ 停用前 `AdminOrgBillSaveValidator` 自动校验"是否有下级组织"

### 3.6 entryentity_merge · 组织合并 ⭐⭐

- ✅ N:1 合并 · 多个被合并组织 → 1 个目标组织
- ✅ 跨子分录配置（merge_detail 详情子分录）
- ✅ 列表显示 `mergecount` = `aftermergeorgid distinct count`（按目标组织数算 · `AdminOrgBatchBillListPlugin.java:168`）
- ✅ admin_org_quick **不支持**合并 · 必须用本场景

### 3.7 entryentity_split · 组织拆分 ⭐⭐

- ✅ 1:N 拆分 · 1 个源组织 → 多个目标组织
- ✅ 跨子分录配置（split_detail 详情子分录）
- ✅ 列表显示 `splitcount` = `beforesplitorgid distinct count`（按源组织数算）
- ✅ admin_org_quick **不支持**拆分 · 必须用本场景

### 3.8 状态机能力

- ✅ 5 状态完整状态机（A 暂存 / B 待审 / C 已生效 / F 废弃 / G 终止）
- ✅ submit 走审批 / submiteffect 跳过审批直接生效
- ✅ 审批驳回回到 A · 由发起人重提
- ✅ 审批人在 wftask 工作中心可继续编辑（B 状态）
- ✅ breakup 终止可逆向回退已生效到 haos_adminorg 的版本
- ✅ unsubmit 撤回（B → A）

### 3.9 异步事件能力

- ✅ 生效后通过 `sch_task` 异步派 BEC 业务事件
- ✅ 业务事件携带变动场景（`changescene`）+ 变动操作（`changeoperate`）+ 是否法人变化（`isbelongcompanychange`）
- ✅ 订阅方独立事务消费（不影响主事务）

---

## 四、❌ 标品不支持（需定制）

| 不支持的能力 | 替代方案 |
|---|---|
| 单条 entry 跨多 changetype（同 org 多操作） | 平台铁律 · 拆成多条申请单 |
| 申请单跨 BU 引用组织 | 不允许 · 一张单只能挂 1 个 BU（`org` 字段） |
| 回溯生效（effdt 设过去日期） | 不支持 · 必须 ≥ 当前日期（admin_org 也是） |
| 自动复制旧申请单作模板 | 用户必须新建 · 不支持复制 |
| 多张单据一次性 breakup | 一次只能 1 条（`AdminOrgBatchBillListPlugin.java:312` 实证） |
| 已废弃单据恢复 | F 状态不可逆 · 用户须新建 |
| 跨场景共用 entry（比如 add + parent 同时操作 1 个组织） | 跨 entry 互斥规则禁止（`AdminOrgBatchBillPlugin.java:691`） |
| 自定义审批流（标品流程外） | 必须走 wf 工作流配置 · ISV 不要在 OP 里写自定义审批 |
| 调单期间冻结目标组织 | 平台无锁 · 高并发下两单都对同一组织操作时取后到 |

---

## 五、🔧 可通过配置实现（无需代码）

| 业务需求 | 配置入口 |
|---|---|
| 单据编码规则（前缀/序号步长） | 编码规则基础资料（按 `homs_orgbatchchgbill` + 当前 BU 配） |
| 新增组织编码规则（自动生成） | 编码规则基础资料（按 `haos_adminorgdetail` + 法人维度配） |
| 变动场景 / 变动原因维护 | `haos_changescene` / `haos_orgchangereason` 基础资料 |
| 组织类型字典 | `haos_adminorgtype` 基础资料 |
| 管理层级字典 | `haos_adminorglayer` 基础资料 |
| 审批流模板 | 苍穹工作流模块 |
| 列表默认列 / 排序 | 列表配置界面 |
| 列表权限 | 权限管理（角色 + 数据权限） |

---

## 六、💻 必须通过插件扩展

| 需求类型 | 推荐扩展点 | 推荐父类 | 关联 CS |
|---|---|---|---|
| 加 entry 字段 | `modifyMeta op=add field` 挂 7 entry parentScope | （无需 Java） | CS-01 |
| 加跨 entry 业务校验 | `onAddValidators@save` | `AbstractValidator` | CS-02 / CS-04 |
| 字段联动联动 | `propertyChanged@homs_orgbatchchgbill` 表单插件 | `HRCoreBaseBillEdit` | CS-03 |
| 提交前业务阻断 | `beforeExecuteOperationTransaction@submit` | `HRDataBaseOp`（不继承 OrgBatchBillSubmitOp） | CS-04 |
| 监听组织变动事件 | BEC 订阅方 | `IEventServicePlugin` | CS-05 |
| 自定义生效后业务 | `afterExecuteOperationTransaction@audit` | `HRDataBaseOp` | CS-06 |
| 终止前补充校验 | `onAddValidators@breakup` | `AbstractValidator` | CS-07 |
| 列表自定义按钮 | `beforeDoOperation` / `afterDoOperation` 列表插件 | `HRDataBaseList` | （列表通用） |

---

## 七、📊 跟 admin_org_quick_maintenance 的能力矩阵对比

| 子场景 | admin_org_quick | homs_orgbatchchgbill | 选哪个 |
|---|---|---|---|
| 单建 1 个组织 | ✅ 即时 | ✅ 走单据 | quick 简单 · orgbatch 走审批 |
| 批量建 N 个组织 | ❌ 逐个 | ✅ add entry 多行 | **必须 orgbatch** |
| 改单字段（不改编码） | ✅ infochg | ✅ info entry | quick 即时 · orgbatch 走单 |
| 改字段+改编码 | ❌ number 不可改 | ✅ info_number 必填 | **必须 orgbatch** |
| 调上级 | ✅ confirmchange | ✅ parent entry | quick 即时 · orgbatch 走单 |
| 调上级+改编码 | ❌ | ✅ parent_number 必填 | **必须 orgbatch** |
| 停用 | ✅ disable | ✅ disable entry | quick 即时 · orgbatch 走单 |
| 合并 | ❌ 不支持 | ✅ merge entry | **必须 orgbatch** |
| 拆分 | ❌ 不支持 | ✅ split entry | **必须 orgbatch** |
| 异步生效 | ❌ 即时 | ✅ effdt 可设未来 | **必须 orgbatch** |
| 走审批 | ❌ | ✅ submit | **必须 orgbatch** |
| 跳过审批 | ✅ 默认 | ✅ submiteffect | quick 即时 · orgbatch 也支持 |
| 终止已生效单 | ❌ | ✅ breakup | **必须 orgbatch** |
| 发文记录 | ❌ | ✅ dispatchnumber/name | **必须 orgbatch** |

---

## 八、📊 73 opKey 能力清单（来自 rules_chain_all.json）

### 标品 HIES 注入（7 opKey · 任何 HR 列表通用）

| opKey | 能力 |
|---|---|
| `importdata_hr` | 导入数据 |
| `import_multientry_hr` | 导入多分录数据（限 A/G 状态） |
| `show_import_record_hr` | 查看导入记录 |
| `export_from_list_hr` | 按列表导出 |
| `export_from_impttpl_hr` | 按导入模板导出 |
| `export_from_expttpl_hr` | 按导出模板导出 |
| `show_export_record_hr` | 查看导出记录 |

### bos 原生（6 opKey · 只看不可改）

`save` / `delete` / `audit` / `unaudit` / `refresh` / `viewflowchart`（其中 audit / save 在 OP 链有 ISV 自定义 · 详见 07_ext_points）

### 业务自定义（60+ opKey · 主要业务）

| 类别 | opKey | 能力 |
|---|---|---|
| 单据生命周期 | `save` `submit` `unsubmit` `audit` `unaudit` | 状态流转 |
| 提交即生效 | `submiteffect` | 跳过审批直生效 |
| 终止 | `breakup` | 已生效单回退 |
| 废弃 | `discard_row` `discard_nothing` | 列表批量废弃 |
| save 变体 | `save_no_log` | 不进操作日志的 silent save |
| 编辑入口 | `add` `edit_add` `parent_edit` `edit_info` | 各 entry 编辑器 |
| 新建入口 | `newentry_parent` `newentry_info` `newentry_disable` | 新建空 entry 行 |
| 合并/拆分 | `org_merge` `edit_merge_detail` `org_split` `edit_split_detail` | 合并拆分专属 |
| 删除 | `delete_rows` `delete_nothing` | 列表批量删除 |

完整 73 opKey 清单见 [`rules_chain_all.json`](rules_chain_all.json)。

---

## 九、🚨 标品限制（重要）

| 限制 | 来源 |
|---|---|
| 一张单 7 entry 任意填 · 但同 org 不能跨 entry 操作 | `AdminOrgBatchBillPlugin.java:691` |
| breakup 一次 1 条 · 不可批量 | `AdminOrgBatchBillListPlugin.java:312` |
| effdt 必须 ≥ 当前日期 · 不能回溯生效 | 标品时序模型限制 |
| 编码规则只按 `haos_adminorgdetail` + 法人维度 | `OrgBatchBillSubmitAndEffectiveOp.java:84-93` |
| 单据 number 一旦生成不可改 | 平台规则 #1 |
| 已生效组织 disable 后 · F7 不再可选作合并 / 拆分 / 信息变更目标 | `AdminOrgBatchBillPlugin.java:794` |
| 已审批通过的 entry 不允许再编辑（必须 unsubmit / breakup） | wf 框架约束 |
| billstatus 由系统维护 · ISV 严禁直接 set | 详见 02 §九 |

---

## 十、扩展对象选择决策树

```
我要改什么？
    │
    ├─ 加 entry 字段（如"申请来源系统"）
    │   → modifyMeta + parentScope = entryentity_add (或其他 6 个)
    │   → 不需要 Java 插件
    │
    ├─ 加业务校验（"提交时校验某条件"）
    │   → 新写 AbstractValidator · 挂在 save 的 onAddValidators
    │   → CS-04
    │
    ├─ 加事务前阻断（"submit 时阻止某场景"）
    │   → 新 OP extends HRDataBaseOp · 挂 submit 的 beforeExecute
    │   → CS-04
    │
    ├─ 加生效后通知（"发邮件 / 同步到外系"）
    │   → BEC 订阅方 implements IEventServicePlugin
    │   → CS-05（不挂 OP · 不影响主事务）
    │
    ├─ 改字段联动（"选了 X 自动填 Y"）
    │   → 新 FormPlugin extends HRCoreBaseBillEdit · 重写 propertyChanged
    │   → CS-03
    │
    ├─ 改 F7 过滤（"上级组织 F7 改成只看 XX"）
    │   → 新 FormPlugin · 重写 beforeF7Select
    │   → 跟标品 F7 链并列跑
    │
    └─ 改单据状态机
        → ❌ 不支持（违反平台铁律 · 状态机由标品 OP 控制）
```

---

## 十一、字段类型约束

### 推荐使用

- `TextField` / `MuliLangTextField` - 文本（注意 MuliLangTextField 拼写 · 一个 t）
- `DateField` / `DateTimeField` - 日期
- `BasedataField` - 引用基础资料
- `HRAdminOrgField` - 行政组织专属（**HRAdminOrgField 已存在 · 实证 entry XML L1`add_parentorg`**）
- `OrgField` - BU 专属
- `ComboField` - 枚举
- `IntegerField` / `BigIntField` - 数字
- `CheckBoxField` - 布尔
- `BillNoField` / `BillStatusField` - 单据专用

### ❌ 慎用 / 禁用

- `EmployeeField` - buildMeta 不支持（用 BasedataField + refEntity=hrpi_employeenewf7query 替代）
- `MultiLangTextField` - 拼写错误（正确是 `MuliLangTextField`）

详见 `cosmic_realworld_traps/buildmeta_traps.md`。

---

## 十二、OpenAPI 覆盖度（2026-04-25 实测）

| 操作 | OpenAPI 支持 | 备注 |
|---|---|---|
| getFormSchema | ✅ | 152 字段实抓 |
| modifyMeta（加字段） | ✅ | 7 entry 都可挂 |
| addRule（加规则） | ✅ | 三层规则可挂 |
| registerPlugin | ✅ | targetType=OPERATION 注册到具体 opKey |
| updateOperation | ✅ | 加自定义 OP 到 save / submit / breakup 等 |
| HIES 一键加字段 | ❌ | 必须手动 modifyMeta |
| 编码规则配置 | ⚠️ 走 UI | OpenAPI 没暴露 CodeRule 基础资料的写接口 |

---

**📌 来源追溯**：

- 73 opKey 清单：`rules_chain_all.json`
- 24 标品插件：`_auto_plugin_registry.md`
- 8 反编译类业务能力：`_auto_plugin_semantics.md` + `OrgBatchBillSaveOp.java` 等 8 类
- 跨 entry 互斥：`AdminOrgBatchBillPlugin.java:662-704`
- breakup 单条：`AdminOrgBatchBillListPlugin.java:312`
- 编码生成：`OrgBatchBillSubmitAndEffectiveOp.java:75-110`
- 状态机字段：`OrgBatchBillSubmitOp.java:36` / `AdminOrgBatchBillListPlugin.java:358`
- 跟 admin_org_quick 对比：`scenarios/admin_org_quick_maintenance/` + 标品菜单实探
