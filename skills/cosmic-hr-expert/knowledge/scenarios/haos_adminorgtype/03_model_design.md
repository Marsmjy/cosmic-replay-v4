# 模型设计 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于 scene_doc.json（31 字段实证）+ 物理表推断 + 继承链分析
> **confidence**: verified
> **schema 字段数**: 31

---

## 一、实体基本信息

| 属性 | 值 |
|---|---|
| **formNumber** | `haos_adminorgtype` |
| **主实体名** | `haos_adminorgtype` |
| **物理主表** | `t_haos_adminorgtype` |
| **物理多语言表** | `t_haos_adminorgtype_l` |
| **ModelType** | BaseFormModel（基础资料 · 非时序） |
| **所属包** | `hrmp-haos-formplugin-1.0.jar` / `hrmp-haos-opplugin-1.0.jar` |

---

## 二、字段完整清单（31 字段）

### 2.1 L0 层字段（平台系统字段 · isvCanModify=false · 禁止手改）

| 字段 key | 显示名 | 类型 | 物理列 | 引用实体 | minefield | 说明 |
|---|---|---|---|---|---|---|
| `creator` | 创建人 | CreaterField | `fcreatorid` | `bos_user` | 🔴 red | 系统维护 · 自动计算 |
| `modifier` | 修改人 | ModifierField | `fmodifierid` | `bos_user` | 🔴 red | 系统维护 · 自动计算 |
| `createtime` | 创建时间 | CreateDateField | `fcreatetime` | - | 🔴 red | 系统维护 · 自动计算 |
| `modifytime` | 修改时间 | ModifyDateField | `fmodifytime` | - | 🔴 red | 系统维护 · 自动计算 |
| `masterid` | 主数据内码 | MasterIdField | `fmasterid` | - | 🔴 red | 系统维护 · 自动计算 |

> 🔴 red minefield：手改破坏数据一致性 · 严禁在 OP 层直接 set

---

### 2.2 L1 层字段（基础资料核心字段 · 部分 isvCanModify=false）

| 字段 key | 显示名 | 类型 | 物理列 | isvCanModify | minefield | 说明 |
|---|---|---|---|---|---|---|
| `number` | 编码 | TextField | `fnumber` | ✅ | - | 基础资料编码 · 创建后不可改（PR-007）|
| `name` | 名称 | MuliLangTextField | `fname` | ✅ | - | 多语言 · 同步写 _l 表 |
| `status` | 数据状态 | BillStatusField | `fstatus` | ✅ | 🟡 yellow | 影响下游 · 慎改 |
| `enable` | 使用状态 | BillStatusField | `fenable` | ✅ | 🟡 yellow | 影响下游 · 慎改 |
| `index` | 排序号 | IntegerField | `findex` | ✅ | - | 手动排序号 |
| `simplename` | 简称 | MuliLangTextField | `fsimplename` | ✅ | - | 多语言简称 |
| `description` | 描述 | MuliLangTextField | `fdescription` | ✅ | - | 多语言描述 |
| `issyspreset` | 系统预置 | CheckBoxField | `fissyspreset` | ❌ | 🔴 red | 系统/平台维护 · 手改破坏一致性 |
| `disabler` | 禁用人 | UserField | `FDisablerID` | ❌ | 🔴 red | 系统维护 · 自动计算 |
| `disabledate` | 禁用时间 | DateTimeField | `FDisableDate` | ❌ | 🔴 red | 系统维护 · 自动计算 |
| `initdatasource` | 数据来源 | ComboField | `finitdatasource` | ❌ | 🔴 red | 系统维护 · 自动计算 |
| `orinumber` | 出厂编码 | TextField | `forinumber` | ❌ | 🔴 red | 系统维护 · 自动计算 |
| `oristatus` | 出厂数据编辑状态 | ComboField | `foristatus` | ❌ | 🔴 red | 系统维护 · 自动计算 |
| `oriname` | 出厂名称 | MuliLangTextField | `foriname` | ❌ | 🔴 red | 系统维护 · 自动计算 |

> 🟡 yellow minefield：变更会级联影响下游 · 慎改（尤其 enable/status 直接影响 haos_adminorg 数据可用性）

---

### 2.3 L3 层字段（模板/组织/业务字段）

| 字段 key | 显示名 | 类型 | 物理列 | 引用实体 | isvCanModify | 说明 |
|---|---|---|---|---|---|---|
| `createorg` | 创建组织 | OrgField | — | `bos_org` | ✅ | 多组织字段 |
| `org` | 管理组织 | OrgField | — | `bos_org` | ✅ | 多组织字段 |
| `useorg` | 使用组织 | OrgField | — | `bos_org` | ✅ | 多组织字段 |
| `ctrlstrategy` | 控制策略 | ComboField | — | - | ✅ | 基础资料控制策略 |
| `sourcedata` | 原资料id | BigIntField | — | - | ✅ | 同步控制 |
| `bitindex` | 位图 | IntegerField | — | - | ✅ | 控制位图 |
| `srcindex` | 原资料位图 | IntegerField | — | - | ✅ | 控制位图 |
| `srccreateorg` | 原创建组织 | OrgField | — | `bos_org` | ✅ | 原创建组织 |
| **`adminorgtypestd`** | **类型归属** | **BasedataField** | `fadminorgtypestdid` | **`haos_adminorgtypestd`** | ❌ | ⭐ 场景核心字段 · 被引用后不可改（R-2）|
| **`orgpattern`** | **形态** | **BasedataField** | `forgpatternid` | **`bos_org_pattern`** | ✅ | ⭐ 联动字段 · adminorgtypestd 变化时自动填（R-3）|

---

## 三、关键字段详解

### 3.1 adminorgtypestd（类型归属）⭐

```
字段 key:    adminorgtypestd
显示名:      类型归属
类型:        BasedataField（单选）
物理列:      fadminorgtypestdid（外键 · 存 haos_adminorgtypestd 记录的 id）
引用实体:    haos_adminorgtypestd
层级:        L3
isvCanModify: false（ISV 不可通过 modifyMeta 修改此字段属性）
required:     true（必填）
minefield:   无显式标记 · 但业务上极其敏感
```

**业务敏感性**：
- 被引用后字段灰化（R-2 · AdminorgtypeEditPlugin.beforeBindData 实证）
- 是 orgpattern 的联动源头（R-3 · propertyChanged 实证）
- HIES 导入时按此字段修正 orgpattern（R-4 · AdminOrgTypeSaveOp 实证）
- haos_adminorgtypestd 是本字段的上游字典（refentity_reverse 实证）

### 3.2 orgpattern（形态）⭐

```
字段 key:    orgpattern
显示名:      形态
类型:        BasedataField（单选）
物理列:      forgpatternid（外键 · 存 bos_org_pattern 记录的 id）
引用实体:    bos_org_pattern
层级:        L3
isvCanModify: true
required:     false
```

**联动关系**：adminorgtypestd 变化时，通过 `AdminOrgTypeStdEnum.getOrgPatternIdById` 枚举查出对应 orgPatternId 自动填入。用户可在自动填充后手工覆盖。

### 3.3 issyspreset（系统预置）

```
字段 key:    issyspreset
显示名:      系统预置
类型:        CheckBoxField
物理列:      fissyspreset
层级:        L1
isvCanModify: false
autoComputed: true
minefield:   red
```

**保护作用**：issyspreset=true 的预置数据受 R-1 业务规则保护（即使 listRules 规则已禁用，字段本身 isvCanModify=false 仍有效）。

---

## 四、多语言表结构（_l 表）

主表 `t_haos_adminorgtype` 的多语言字段存在 `t_haos_adminorgtype_l` 表中：
- `name`（fname）→ 同步写 t_haos_adminorgtype_l.fname
- `simplename`（fsimplename）→ 同步写 t_haos_adminorgtype_l.fsimplename
- `description`（fdescription）→ 同步写 t_haos_adminorgtype_l.fdescription
- `oriname`（foriname）→ 同步写 t_haos_adminorgtype_l.foriname

> 苍穹平台 MuliLangTextField 自动双写主表和多语言表，无需 ISV 手动处理。

---

## 五、物理表命名规律

| 规律 | 示例 |
|---|---|
| 主表：`t_{formNumber}` | `t_haos_adminorgtype` |
| 多语言表：`t_{formNumber}_l` | `t_haos_adminorgtype_l` |
| 字段物理列：`f{fieldKey}` | `fnumber` / `fname` / `fissyspreset` |
| 引用字段物理列：`f{fieldKey}id` | `fadminorgtypestdid` / `forgpatternid` |
| 多组织字段：无固定物理列（OrgField · 走平台 OrgCtrl 表）| `createorg`（—）|

---

## 六、字段层级说明

| 层级 | 来源 | 说明 |
|---|---|---|
| L0 | 苍穹平台基类 | 5 个系统计算字段（creator/modifier/createtime/modifytime/masterid） |
| L1 | 基础资料模板（BaseFormModel）| 14 个字段（number/name/status/enable 等）· 包括保护性字段 |
| L3 | hbp_bd_orgtpl_all / 本场景专属 | 10 个字段（多组织 + ctrlstrategy + adminorgtypestd + orgpattern）|

> **注意**：L2 层无字段（当前 schema 未见 L2 独有字段）。

---

## 七、字段可扩展性分析

| 字段分类 | ISV 可操作性 | 说明 |
|---|---|---|
| L0 系统字段（5 个）| ❌ 不可改 | autoComputed=true · minefield=red |
| L1 保护字段（issyspreset/disabler/disabledate 等）| ❌ 不可改 | isvCanModify=false |
| L1 业务字段（number/name/enable 等）| ✅ 可读取 · 谨慎修改 | enable/status 慎改（minefield=yellow）|
| L3 组织字段（createorg/org/useorg）| ✅ 可读取 | 多组织控制 · 走平台 OrgField |
| L3 核心业务字段（adminorgtypestd/orgpattern）| 读✅ · modifyMeta❌ | isvCanModify=false（adminorgtypestd）|
| **ISV 新加字段** | ✅ 可加 | 走 modifyMeta op=add · 前缀必须带 ISV 标识（如 ${ISV_FLAG}_）|

---

## 八、平台命名规则速查（铁律 14）

| 元素 | 上限 | 示例 |
|---|---|---|
| ISV 字段 key | ≤ 24 字符 | `${ISV_FLAG}_orgtypecategory`（20 字符 ✅）|
| ISV 实体 key | ≤ 36 字符 | `${ISV_FLAG}_adminorgtype_entry`（23 字符 ✅）|
| 物理表名 | ≤ 25 字符 | `t_haos_adminorgtype`（20 字符 ✅）|
| 字段前缀 | 必须带前缀 | `${ISV_FLAG}_*` 防标品升级覆盖（PR-001）|
| 禁止继承 | 场景专属类 | AdminorgtypeEditPlugin / AdminorgtypeListPlugin / AdminOrgTypeSaveOp / BaseDataBuOp / AbsOrgBaseOp / CtrlStrategyValidator |

---

## 九、与三胞胎字段对比

| 字段 | haos_adminorgtype | haos_adminorgfunction | haos_adminorglayer | haos_orgchangereason |
|---|---|---|---|---|
| `adminorgtypestd` | ✅（L3 · 场景核心）| ❌ | ❌ | ❌ |
| `orgpattern` | ✅（L3 · 联动字段）| ❌ | ❌ | ❌ |
| `issyspreset` | ✅ L1 | ✅ L1 | ✅ L1 | ❌ |
| `otclassify` 分类 | ❌ | ❌ | ❌ | ✅（haos_orgchangereason 独有）|
| 总字段数 | **31** | 27 | 约 27 | 28 |

**本场景字段数最多（31）**，主要因为增加了 `adminorgtypestd`（类型归属）和 `orgpattern`（形态）两个核心字段，以及相关的 L3 层字段。
