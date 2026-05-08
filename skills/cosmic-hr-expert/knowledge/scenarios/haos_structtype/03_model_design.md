# 模型设计 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证 + 物理表推断 + 继承链分析
> **confidence**: verified

---

## 一、实体基本信息

| 属性 | 值 |
|---|---|
| **formNumber** | `haos_structtype` |
| **主实体名** | `haos_structtype` |
| **物理主表** | `t_haos_structtype` |
| **物理多语言表** | `t_haos_structtype_l` |
| **ModelType** | BaseFormModel（基础资料 · 非时序）|
| **所属包** | `hrmp-haos-formplugin-1.0.jar` / `hrmp-haos-opplugin-1.0.jar` |

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
| `number` | 编码 | TextField | `fnumber` | ✅ | - | 基础资料编码 · 创建后不可改（PR-007）|
| `name` | 名称 | MuliLangTextField | `fname` | ✅ | - | 多语言 · 改名走 ChgNameOp |
| `status` | 数据状态 | BillStatusField | `fstatus` | ✅ | 🟡 yellow | 影响下游 · 慎改 |
| `enable` | 使用状态 | BillStatusField | `fenable` | ✅ | 🟡 yellow | 级联禁用下游（R-6）|
| `index` | 排序号 | IntegerField | `findex` | ✅ | - | 手动排序号 |
| `simplename` | 简称 | MuliLangTextField | `fsimplename` | ✅ | - | 多语言简称 |
| `description` | 描述 | MuliLangTextField | `fdescription` | ✅ | - | 多语言描述 |
| `issyspreset` | 系统预置 | CheckBoxField | `fissyspreset` | ❌ | 🔴 red | 系统维护 |
| `disabler` | 禁用人 | UserField | `FDisablerID` | ❌ | 🔴 red | 系统维护 |
| `disabledate` | 禁用时间 | DateTimeField | `FDisableDate` | ❌ | 🔴 red | 系统维护 |
| `initdatasource` | 数据来源 | ComboField | `finitdatasource` | ❌ | 🔴 red | 系统维护 |
| `orinumber` | 出厂编码 | TextField | `forinumber` | ❌ | 🔴 red | 系统维护 |
| `oristatus` | 出厂数据编辑状态 | ComboField | `foristatus` | ❌ | 🔴 red | 系统维护 |
| `oriname` | 出厂名称 | MuliLangTextField | `foriname` | ❌ | 🔴 red | 系统维护 |

---

### 2.3 L2/L3 层字段（模板/场景专属字段）

| 字段 key | 显示名 | 类型 | 物理列 | isvCanModify | 说明 |
|---|---|---|---|---|---|
| `createorg` | 创建组织 | OrgField | — | ✅ | 多组织字段 |
| `org` | 管理组织 | OrgField | — | ✅ | 多组织字段 · 新建时权限自动填 |
| `useorg` | 使用组织 | OrgField | — | ✅ | 多组织字段 |
| `ctrlstrategy` | 控制策略 | ComboField | — | ✅ | 基础资料控制策略 |
| `sourcedata` | 原资料id | BigIntField | — | ✅ | 同步控制 |
| `bitindex` | 位图 | IntegerField | — | ✅ | 控制位图 |
| `srcindex` | 原资料位图 | IntegerField | — | ✅ | 控制位图 |
| `srccreateorg` | 原创建组织 | OrgField | — | ✅ | 原创建组织 |
| **`effdt`** | **生效日期** | **DateField** | `feffdt` | ✅ | ⭐ 仅启用状态可编辑（R-1）|
| **`metanumsuffix`** | **元数据编码后缀** | **TextField** | `fmetanumsuffix` | ✅ | ⭐ 核心字段 · 唯一命名空间标识 · 不可改 |

---

## 三、关键字段详解

### 3.1 metanumsuffix（元数据编码后缀）⭐

```
字段 key:    metanumsuffix
显示名:      元数据编码后缀
类型:        TextField
物理列:      fmetanumsuffix
层级:        L3（场景专属）
isvCanModify: true（理论上可改 · 但业务上极其敏感禁止改）
minefield:   实际为红色 · 改变此字段会导致所有关联元数据/菜单/规则全部失效
```

**业务作用**：`metanumsuffix` 是本架构类型所有动态创建资源的命名空间后缀。
- 创建元数据时：`metaNumber = BASE_META_PAGE_PREFIX + "_" + metanumsuffix`
- 创建菜单时：菜单 id 关联 metanumsuffix
- 删除时：按 metanumsuffix 查找并清理所有关联资源

**OP 层使用**：`StructTypeSaveOp`、`StructTypeEnableOp`、`StructTypeDeleteDonothingOp`、`StructTypeChgNameOp` 都通过 `onPreparePropertys` 声明加载 `metanumsuffix` 字段。

### 3.2 effdt（生效日期）⭐

```
字段 key:    effdt
显示名:      生效日期
类型:        DateField
物理列:      feffdt
层级:        L3
isvCanModify: true
```

**UI 控制**：`StructTypeEditPlugin.beforeBindData` 根据 `enable` 字段状态动态设置 effdt 的可编辑性：`enable="10"` 时可编辑，否则禁用。

### 3.3 enable（使用状态）⭐

| enable 值 | 含义 | 影响 |
|---|---|---|
| `"1"` | 启用 | 正常可用 · chgname 检查要求此值 |
| `"0"` | 禁用 | 级联禁用下游（R-6）|
| `"10"` | 可用（启用中）| effdt 可编辑（R-1）|

---

## 四、多语言表结构（_l 表）

主表 `t_haos_structtype` 的多语言字段存在 `t_haos_structtype_l` 表中：
- `name`（fname）→ `t_haos_structtype_l.fname`
- `simplename`（fsimplename）→ `t_haos_structtype_l.fsimplename`
- `description`（fdescription）→ `t_haos_structtype_l.fdescription`
- `oriname`（foriname）→ `t_haos_structtype_l.foriname`

`StructTypeChgNameOp` 改名时使用 `DynamicObject.getLocaleString("name")` 遍历多语言值，通过 `StructClassHelper.chgMenuName(id, nameValuesForMap)` 同步更新菜单多语言名称。

---

## 五、物理表命名规律

| 规律 | 示例 |
|---|---|
| 主表：`t_{formNumber}` | `t_haos_structtype` |
| 多语言表：`t_{formNumber}_l` | `t_haos_structtype_l` |
| 字段物理列：`f{fieldKey}` | `fnumber` / `fname` / `fenable` |
| 引用字段物理列：`f{fieldKey}id` | `forgid`（org 字段）|
| 多组织字段：无固定物理列（OrgField）| `org`（—）|

---

## 六、字段层级说明

| 层级 | 来源 | 说明 |
|---|---|---|
| L0 | 苍穹平台基类 | 5 个系统计算字段 |
| L1 | 基础资料模板（BaseFormModel）| 14 个字段（number/name/status/enable 等）|
| L2 | `hbp_bd_tpl_all` | 多组织字段（createorg/org/useorg）+ ctrlstrategy |
| L3 | `haos_otclassify` 场景专属 | effdt + metanumsuffix + 其他标识字段 |

---

## 七、字段可扩展性分析

| 字段分类 | ISV 可操作性 | 说明 |
|---|---|---|
| L0 系统字段（5 个）| ❌ 不可改 | autoComputed=true |
| L1 保护字段（issyspreset/disabler 等）| ❌ 不可改 | isvCanModify=false |
| L1 业务字段（number/name/enable 等）| ✅ 可读取 · 谨慎改 | enable 禁用级联影响大 |
| L3 核心字段（metanumsuffix/effdt）| 读✅ · 改❌ | metanumsuffix 改变导致资源失效 |
| **ISV 新加字段** | ✅ 可加 | 走 modifyMeta op=add · 前缀必须带 ISV 标识 |

---

## 八、平台命名规则速查（铁律 14）

| 元素 | 上限 | 示例 |
|---|---|---|
| ISV 字段 key | ≤ 24 字符 | `${ISV_FLAG}_structtypecate`（20 字符 ✅）|
| ISV 实体 key | ≤ 36 字符 | `${ISV_FLAG}_structtype_entry`（22 字符 ✅）|
| 物理表名 | ≤ 25 字符 | `t_haos_structtype`（18 字符 ✅）|
| 字段前缀 | 必须带前缀 | `${ISV_FLAG}_*` 防标品升级覆盖（PR-001）|
| 禁止继承 | 场景专属类 | StructTypeEditPlugin / StructTypeListPlugin / StructTypeSaveOp / StructTypeDisableOp / StructTypeEnableOp / StructTypeDeleteDonothingOp / StructTypeChgNameOp |
