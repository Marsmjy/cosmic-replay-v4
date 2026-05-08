# 模型设计 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证 + 物理表推断 + 继承链分析
> **confidence**: verified

---

## 一、实体基本信息

| 属性 | 值 |
|---|---|
| **formNumber** | `hbpm_positiontpl` |
| **主实体名** | `hbpm_positiontpl` |
| **物理主表** | `t_hbpm_positiontpl`（推断）|
| **物理多语言表** | `t_hbpm_positiontpl_l`（推断）|
| **ModelType** | **BaseFormModel**（基础资料 · 非时序 · 见重要说明）|
| **所属包** | `hrmp-hbpm-formplugin-1.0.jar` / `hrmp-hbpm-opplugin-1.0.jar` |

> ⚠ **重要**：虽继承 `hbp_histimeseqtpl`（历史时序模板），但本场景**实际为 BaseFormModel**。反编译代码中无 `boid`/`iscurrentversion` 字段操作，`PositionTplSaveOp` 的 `beforeExecuteOperationTransaction` 中通过 `dynamicObject.getLong("id") != 0L` 直接按 id 区分新旧记录（HisModel 场景会用 boid），没有时序版本切换逻辑。模板继承仅带来 UI 框架，不代表数据模型是 HisModel。

---

## 二、字段清单

### 2.1 L0 层字段（平台系统字段 · isvCanModify=false · 禁止手改）

| 字段 key | 显示名 | 类型 | 物理列 | minefield | 说明 |
|---|---|---|---|---|---|
| `creator` | 创建人 | CreaterField | `fcreatorid` | 🔴 red | 系统维护 · 自动计算 |
| `modifier` | 修改人 | ModifierField | `fmodifierid` | 🔴 red | 系统维护 · 自动计算 |
| `createtime` | 创建时间 | CreateDateField | `fcreatetime` | 🔴 red | 系统维护 · 自动计算 |
| `modifytime` | 修改时间 | ModifyDateField | `fmodifytime` | 🔴 red | 系统维护 · 自动计算 |
| `masterid` | 主数据内码 | MasterIdField | `fmasterid` | 🔴 red | 系统维护 · 自动计算 |

---

### 2.2 L1 层字段（基础资料核心字段）

| 字段 key | 显示名 | 类型 | 物理列 | isvCanModify | minefield | 说明 |
|---|---|---|---|---|---|---|
| `number` | 编码 | TextField | `fnumber` | ✅ | - | 唯一编码 · 创建后不可改（PR-007）|
| `name` | 名称 | MuliLangTextField | `fname` | ✅ | - | 多语言 · 名称唯一性校验（nameError R-4）|
| `status` | 数据状态 | BillStatusField | `fstatus` | ✅ | 🟡 yellow | A=已审核 · 驱动 UI 三态 |
| `enable` | 使用状态 | BillStatusField | `fenable` | ✅ | 🟡 yellow | 禁用时不可设适用范围（R-2）|
| `index` | 排序号 | IntegerField | `findex` | ✅ | - | 唯一性校验（indexError R-4）|
| `simplename` | 简称 | MuliLangTextField | `fsimplename` | ✅ | - | 多语言简称 |
| `description` | 描述 | MuliLangTextField | `fdescription` | ✅ | - | 多语言描述 |
| `issyspreset` | 系统预置 | CheckBoxField | `fissyspreset` | ❌ | 🔴 red | 系统维护 |
| `disabler` | 禁用人 | UserField | `FDisablerID` | ❌ | 🔴 red | 系统维护 |
| `disabledate` | 禁用时间 | DateTimeField | `FDisableDate` | ❌ | 🔴 red | 系统维护 |

---

### 2.3 L2/场景专属字段

| 字段 key | 显示名 | 类型 | 物理列 | isvCanModify | 说明 |
|---|---|---|---|---|---|
| `createorg` | 创建组织 | OrgField | — | ✅ | 多组织字段 |
| **`org`** | **管理组织** | **OrgField** | — | ✅ | ⭐ 核心字段 · 与 BU 挂钩 · 新建时权限自动填 |
| `useorg` | 使用组织 | OrgField | — | ✅ | 多组织字段 |
| `ctrlstrategy` | 控制策略 | ComboField | — | ✅ | 基础资料控制策略 |
| **`posttpltype`** | **岗位模板类型** | **BasedataField** | `fposttpltype` | ✅ | 引用 `hbpm_positiontpltype`（上游）|
| **`ablemodifyfield`** | **允许变更字段** | **CheckBoxField** | `fablemodifyfield` | ✅ | true 时 fieldrange 可见 |
| **`fieldrange`** | **字段范围** | **BasedataField** | — | ✅ | 允许变更的字段范围 · ablemodifyfield=true 时可见 |
| **`modifystrategy`** | **变更策略** | **分组/面板** | — | ✅ | BU openpositiontpl=true 时方可见 |

---

## 三、关键字段详解

### 3.1 org（管理组织）⭐

```
字段 key:    org
显示名:      管理组织
类型:        OrgField
物理列:      —（OrgField 走平台 OrgCtrl 表）
层级:        L2/L3
isvCanModify: true
```

**业务意义**：
- 新建时通过 `PermissionServiceHelper.getAllPermOrgs()` 权限查询自动填入
- 通过 `orgId` customParam 可从列表页传入预设值
- `org` 变化时触发 `propertyChanged` → `setModifyStrategy()` 重新计算 modifystrategy 可见性
- `setModifyStrategy()` 通过 `SystemParamHelper.getBatchParameter(orgId)` 查询 BU 参数决定 UI 展示

### 3.2 posttpltype（岗位模板类型）

```
字段 key:    posttpltype
显示名:      岗位模板类型
类型:        BasedataField（单选）
引用实体:    hbpm_positiontpltype
层级:        L3
isvCanModify: true
```

**F7 过滤**：`PositionTplEditPlugin.registerListener` 注册了 `posttpltype` 字段的 BeforeF7SelectListener，配合 `JobLevelGradeRangeUtil` 处理岗位层级范围过滤。

### 3.3 status 驱动的 UI 三态

| 状态 | bar_save 可见 | bar_modify 可见 | 说明 |
|---|---|---|---|
| VIEW（isAudit=true · status=A）| 隐藏 | 显示 | 审核通过 · 可变更 |
| VIEW（isAudit=true · status≠A）| 隐藏 | 隐藏 | 未审核 · 不可变更 |
| VIEW（isAudit=false）| 隐藏 | 显示 | 未启用审核流 · 直接可变更 |
| EDIT（modify 后）| 显示 | 隐藏 | 编辑中 · 可保存 |

---

## 四、多语言表结构（_l 表）

主表 `t_hbpm_positiontpl` 的多语言字段存在 `t_hbpm_positiontpl_l` 表中：
- `name`（fname）→ `t_hbpm_positiontpl_l.fname`
- `simplename`（fsimplename）→ `t_hbpm_positiontpl_l.fsimplename`
- `description`（fdescription）→ `t_hbpm_positiontpl_l.fdescription`

---

## 五、物理表命名规律

| 规律 | 示例 |
|---|---|
| 主表：`t_{formNumber}` | `t_hbpm_positiontpl` |
| 多语言表：`t_{formNumber}_l` | `t_hbpm_positiontpl_l` |
| 字段物理列：`f{fieldKey}` | `fnumber` / `fname` / `fstatus` |
| 引用字段物理列：`f{fieldKey}id`（推断）| `fposttpltype` → `fposttpltype_id` |

---

## 六、BaseFormModel vs HisModel 对比说明

本场景继承 `hbp_histimeseqtpl` 但实为 BaseFormModel，以下是关键区别：

| 维度 | 本场景（BaseFormModel）| HisModel（如 haos_adminorghis）|
|---|---|---|
| boid 字段 | ❌ 不存在 | ✅ 存在（业务对象 id）|
| iscurrentversion | ❌ 不存在 | ✅ 存在（版本过滤必用）|
| 查询过滤 | 直接 QFilter("id", "=", id) | 必须加 iscurrentversion=true（PR-009）|
| 版本切换 | ❌ 无 | ✅ 有生效日期驱动版本 |
| PositionTplSaveOp 实证 | `id != 0L` 区分新旧 | boid + effdt 驱动 |

**ISV 查询本场景**：直接按 `id` 查询，无需 `iscurrentversion` 过滤，与普通基础资料相同。

---

## 七、字段可扩展性分析

| 字段分类 | ISV 可操作性 | 说明 |
|---|---|---|
| L0 系统字段（5 个）| ❌ 不可改 | autoComputed=true |
| L1 保护字段（issyspreset 等）| ❌ 不可改 | isvCanModify=false |
| L1 业务字段（number/name/enable 等）| ✅ 可读取 | enable 禁用有下游影响 |
| L3 核心字段（posttpltype/org）| 读✅ · modifyMeta 属性改❌ | 上下游关联复杂 |
| **ISV 新加字段** | ✅ 可加 | modifyMeta op=add · 前缀必须带 ISV 标识 |

---

## 八、平台命名规则速查（铁律 14）

| 元素 | 上限 | 示例 |
|---|---|---|
| ISV 字段 key | ≤ 24 字符 | `${ISV_FLAG}_positiontplcate`（21 字符 ✅）|
| ISV 实体 key | ≤ 36 字符 | `${ISV_FLAG}_positiontpl_entry`（23 字符 ✅）|
| 物理表名 | ≤ 25 字符 | `t_hbpm_positiontpl`（19 字符 ✅）|
| 字段前缀 | 必须带前缀 | `${ISV_FLAG}_*` 防标品升级覆盖（PR-001）|
| 禁止继承 | 场景专属类 | PositionTplEditPlugin / PositionTplListPlugin / PositionTplBuListPlugin / PositionTplSaveOp |
