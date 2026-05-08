# 模型设计 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `scene_doc.json` (70+ 字段 · 2 物理表) + 4 级继承链 + 7 类反编译整合
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `getFormMetadata.InheritPath` + `javap -p` 反编译 (2026-04-24)

---

## ⭐ 关键业务事实 · 岗位 vs 标准岗位 · 两个独立元数据 + 同一张物理表

**2026-04-24 用户确认**（审 PositionList.setFilter 时点出 · 并澄清）：

苍穹**岗位管理**应用下 · 两个**独立 formId**挂在不同菜单：

| 菜单 | formNumber | 元数据关系 |
|---|---|---|
| 岗位信息维护 | `hbpm_positionhr` | **本场景主实体** |
| 标准岗位维护 | `hbpm_stposition` | **另一独立元数据 · 非继承关系** |

**底层真相**：
- **两个 formId 元数据层面无继承关系**（不是 extends / _inherits · 是独立定义的两张表单）
- **但物理表同一张** `t_hbpm_position` · 靠 **`isstandardpos`** 字段区分业务数据：
  - `isstandardpos = 0` → 业务岗位（`hbpm_positionhr` 视图显示）
  - `isstandardpos = 1` → 标准岗位（`hbpm_stposition` 视图 + `hbpm_posorgtreelistf7` F7 显示）

**开发含义**（极重要 · Claude 做岗位需求时必知）：

**因为 formId 独立**（不是同实体多视图）：
- ⚠ ISV **加字段要两边都改**（modifyMeta 要对 `hbpm_positionhr` 和 `hbpm_stposition` **分别做** · 不是一次改两边都有）
- ⚠ **插件要两边分别挂**（给 `hbpm_positionhr` 挂 FormPlugin 不会自动作用到 `hbpm_stposition`）
- ⚠ 本场景的 scene_doc 只覆盖 `hbpm_positionhr` · 做标准岗位定制时要**独立建 scene** 或额外 probe `hbpm_stposition`

**因为物理表同一张**：
- ✅ 列表 / F7 过滤：用 `isstandardpos` 区分（如 `PositionList.setFilter` L53-L58 · SF1 实证）
- ✅ 数据查询：不管用哪个 formId 查 · 拿到的 DB 行是同一张表
- ✅ 下游引用：下游存 `hbpm_positionhr.id` 还是 `hbpm_stposition.id` 查出来是同一条（物理表同）· 但**实际语义要看业务** —— 任职关系里的 `job` 字段通常只引业务岗位（isstandardpos=0）

**反模式**：
- ❌ 以为"两个 formId 是同一元数据不同视图"（会漏做 modifyMeta / 漏挂插件）
- ❌ 做加字段 CS 时只写一个 formNumber · 忘记另一个
- ❌ 把"任职关系下游引用"直接套到标准岗位（任职关系只引业务岗位）

---

## 一、核心认知：HR 时序基础资料 + 标准岗位适用组织分录 + 岗位树自引用

苍穹岗位采用**时序基础资料 + 分录子表 + 自引用树**设计：

```
hbpm_positionhr（岗位主实体 · 组织发展云 · 组织管理 · HR基础岗位）
 ├── t_hbpm_position              主物理表（68 字段 · 多语言直接存主表）
 └── t_hbpm_standposentry         标准岗位适用组织分录（applicableorg / iscontainsu / entryboid）

特有：
 ├── parent → hbpm_positionhrf7   自引用（HRPositionField 上级岗位树）
 └── entryentity                  协作关系分录（reporttype + targetpos · 虚拟分录）
```

**2 张物理表关系**（`scene_doc.json` L37 `physicalTable`）：
- `t_hbpm_position` = 权威 1:1 存储 · 多语言字段直接存本表（不走 `_l` 子表 · 与职位域不同）
- `t_hbpm_standposentry` = 标准岗位适用范围 1:N · 每行一个行政组织 + 是否包含下级

**⚠️ 注意**：`entryentity` 是协作关系分录 · scene_doc.json 标的 `physicalTable="t_hbpm_position"` 实际是虚拟分录 · 具体物理表在 `_shared/_standard_metadata/entity_metadata/hbpm_positionhr.md` 中体现（运行时映射到 `t_hbpm_positionrelation` 或独立协作关系子表）

---

## 二、继承链（4 级 · `scene_doc.json.inheritance`）

```
L0  bos_basetpl          → 基础资料根模板（9 字段：id / creator / modifier / createtime / modifytime / masterid / status 等）
L1  hbp_bd_tpl_all       → HR 基础资料模板（10 字段：number / name / enable / org / simplename / description / index / issyspreset / disabler / disabledate / initdatasource / orinumber / oriname / oristatus / sourcesyskey / isdeleted 等）
L2  hbp_histimeseqtpl    → HR 历史时序模板（9 字段：boid / iscurrentversion / datastatus / sourcevid / firstbsed / bsed / bsled / hisversion · 部分 changedescription）⭐ 时态核心
L3  hbpm_positionhr      → 自身字段（28 字段：parent / targetpos / adminorg / job / positiontype / positiontpl / changetype / changescene / changeoperate / changedesc / posduty / posstandard / posorientation / establishmentdate / isleader / isstandardpos / applicableorgentity / entryentity / lowjoblevel / highjoblevel / lowjobgrade / highjobgrade / jobgradescm / joblevelscm / jobscm / joblevelrange / jobgraderange / countryregion / workplace / city / orgdesignbu 等）
```

### ⭐ 字段来源分类

| 层级 | 字段类型 | 典型字段 | 能否改 |
|---|---|---|---|
| **L0 bos_basetpl** | 基础资料通用 | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `status` | 🔒 不改（破坏全系统） |
| **L1 hbp_bd_tpl_all** | HR 基础资料通用 | `number` / `name` / `enable` / `org` / `simplename` / `description` / `issyspreset` / `disabler` / `disabledate` / `enabler` / `enabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` / `sourcesyskey` / `isdeleted` / `index` | 🔒 不改（破坏 HR 全线） |
| **L2 hbp_histimeseqtpl** ⭐ | HR 时序核心 | `boid` / `iscurrentversion` / `datastatus` / `sourcevid` / `firstbsed` / `bsed` / `bsled` / `hisversion` | 🔒 不改（遵循 PR-008 / PR-009） |
| **L3 hbpm_positionhr 自身** | 岗位业务字段 | `parent` / `adminorg` / `job` / `positiontype` / `positiontpl` / `changetype` / `changescene` / `changeoperate` / `changedesc` / `changedescription` / `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` / `jobgradescm` / `joblevelscm` / `jobscm` / `joblevelrange` / `jobgraderange` / `posduty` / `posstandard` / `posorientation` / `knowledgereq` / `skillreq` / `abilityreq` / `experiencereq` / `agereq` / `diplomareq` / `countryregion` / `workplace` / `city` / `orgdesignbu` / `establishmentdate` / `isleader` / `isstandardpos` / `applicableorgentity` / `entryentity` | ⚠️ 谨慎改（参考 08 影响面） |

**关键认知**：
- ⚠️ 共 70+ 字段，其中**约 30+ 属于 hbpm_positionhr 自身**（L3），其余继承自父模板
- 🔒 时序字段语义在 `hbp_histimeseqtpl` 就固定了：所有 HR 时序基础资料共用此字段组
- ✅ ISV 扩展应加新字段到 `hbpm_positionhr`（带 ISV 前缀 · PR-001/2），不要想"改继承字段"

---

## 三、完整字段表（OpenAPI `scene_doc.json` 实抓）

### 3.1 业务核心字段（岗位域特有）

| Field Key | 类型 | 业务含义 | 必填 | 扩展性 | 引用实体 |
|---|---|---|---|---|---|
| `number` | TextField | 岗位编码 | ❌ | ⚠️ 可改（创建后业务不应改 · INV-06） | - |
| `name` | MuliLangTextField | 岗位名称 | ❌ | ⚠️ 可改 | - |
| `simplename` | MuliLangTextField | 简称 | ❌ | ⚠️ 可改 | - |
| `description` | MuliLangTextField | 备注 | ❌ | ⚠️ 可改 | - |
| **`adminorg`** | **HRAdminOrgField** | **行政组织** ⭐ 唯一 required=true | **✅** | 🔒 系统（`isvCanModify: false`） | → `haos_adminorghrf7` |
| `parent` | HRPositionField | 上级岗位（自引用） | ❌ | ⚠️ 可改（成环禁区 · INV-02） | → `hbpm_positionhrf7` |
| `job` | BasedataField | 职位 | ❌ | ⚠️ 可改（受 `PosMustRelateJob` 参数控制） | → `hbjm_jobhr` |
| `positiontype` | BasedataField | 岗位类型 | ❌ | ⚠️ 可改 | → `hbpm_positiontype` |
| `positiontpl` | BasedataField | 岗位模板 | ❌ | ⚠️ 可改 | → `hbpm_positiontpl` |
| `isleader` | CheckBoxField | 主负责岗 | ❌ | ⚠️ 可改 | - |
| `isstandardpos` | ComboField | 是否标准岗位 | ❌ | ⚠️ 可改（1=标准 / 0=业务） | - |
| `establishmentdate` | DateField | 设立日期 | ❌ | ⚠️ 可改（首次 save 自动带 bsed） | - |

### 3.2 职级 / 职等方案字段（双维度 · 含冗余区间文本）

| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `jobgradescm` | BasedataField | 职等方案 | → `hbjm_jobgradescmhr` |
| `joblevelscm` | BasedataField | 职级方案 | → `hbjm_joblevelscmhr` |
| `jobscm` | BasedataField | 职位体系方案 | → `hbjm_jobscmhr` |
| `lowjobgrade` | BasedataField | 最低职等 | → `hbjm_jobgradehr` |
| `highjobgrade` | BasedataField | 最高职等 | → `hbjm_jobgradehr` |
| `lowjoblevel` | BasedataField | 最低职级 | → `hbjm_joblevelhr` |
| `highjoblevel` | BasedataField | 最高职级 | → `hbjm_joblevelhr` |
| `jobgraderange` | TextField | 职等范围（冗余文本） | - |
| `joblevelrange` | TextField | 职级范围（冗余文本） | - |

⚠️ **注意**：`jobgraderange` / `joblevelrange` 是列表展示用的冗余字段，修改 `lowjobgrade/highjobgrade/lowjoblevel/highjoblevel` 时需要同步更新（通常由前端 `JobLevelGradeRangeUtil.setFieldRange` 处理 · `PositionEdit.changePositionTpl` L546-L547）。

### 3.3 变动溯源字段（岗位特有 · 由 Op 自动填）

| Field Key | 类型 | 业务含义 | 引用实体 | 预置编码 |
|---|---|---|---|---|
| `changetype` | BasedataField | 变动类型 | → `hbpm_changetype` | 1010 新建 / 1020 变更 / 1030 汇报关系 / 1040 禁用 / 1070 启用 |
| `changeoperate` | BasedataField | 变动操作 | → `hbpm_changeoperate` | 1010 / 1020 / 1030 / 1070 |
| `changescene` | BasedataField | 变动场景 | → `hbpm_changescene` | 1010 / 1020 / 1030 / 1040 / 1070 |
| `changedesc` | BasedataField | 变动原因 | → `hbpm_changereason` | - |
| `changedescription` | TextField | 变动说明（用户填） | - | - |

**数据填充规律**（`PositionChangeEventQueryRepository.queryChangeType/Operation/Scene` 调用点）：
- save (新建)：`changetype=1010` / `changeoperate=1010` / `changescene=1010`
- confirmchange：`changetype=1020` / `changeoperate=1020` / `changescene=1020`
- dochangerelation：`changetype=1030` / `changeoperate=1020` / `changescene=1030`
- disable：`changetype=1040` / `changeoperate=1030` / `changescene=1040`
- enable：`changetype=1070` / `changeoperate=1070` / `changescene=1070`

### 3.4 岗位说明书字段（多语言 · 职责类）

| Field Key | 业务含义 |
|---|---|
| `posorientation` (MuliLangTextField) | 岗位定位 |
| `posduty` (MuliLangTextField) | 岗位职责 |
| `posstandard` (MuliLangTextField) | 岗位衡量标准 |

### 3.5 任职要求字段（6 个 MuliLangTextField + 1 BasedataField）

| Field Key | 业务含义 |
|---|---|
| `diplomareq` (BasedataField→`hbss_diploma`) | 最低学历要求 |
| `agereq` (MuliLangTextField) | 年龄要求 |
| `knowledgereq` | 知识要求 |
| `skillreq` | 技能要求 |
| `abilityreq` | 能力要求 |
| `experiencereq` | 经验要求 |

### 3.6 工作地字段

| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `countryregion` | BasedataField | 国家地区 | → `bd_country` |
| `workplace` | BasedataField | 工作地 | → `hbss_workplace` |
| `city` | BasedataField | 所在城市 | → `bd_admindivision` |

**联动规律**（`PositionEdit.propertyChanged`）：
- adminorg → 带出 countryregion / city / workplace / org
- city 变化 → 若 countryregion 空 · 自动填 city.country · 并按 (country, city) 查默认 workplace
- countryregion 变化 → city 置 null

### 3.7 时态字段（L2 hbp_histimeseqtpl 继承，禁改）

| Field Key | 类型 | 业务含义 | minefield |
|---|---|---|---|
| `boid` | BigIntField | 业务对象 ID ⭐ | 🔴 red |
| `iscurrentversion` | CheckBoxField | 是否当前生效数据 | 🔴 red |
| `datastatus` | ComboField | 数据版本状态 | 🔴 red |
| `sourcevid` | BigIntField | 关联上一版本 id | 🔴 red |
| `firstbsed` | DateField | 最早生效日期 | 🔴 red |
| **`bsed`** | **DateField** | **生效日期** ⭐ | 🟡 yellow |
| **`bsled`** | **DateField** | **失效日期** ⭐ | 🟡 yellow |
| `changedescription` | TextField | 变动说明 | - |
| `hisversion` | TextField | 历史版本号 | 🔴 red |

### 3.8 组织 / 控制字段（L1 hbp_bd_tpl_all 继承）

| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `org` | OrgField | 组织体系管理组织（从 adminorg.org 自动派生） | → `bos_org` |
| `orgdesignbu` | OrgField | 职位体系管理组织 | → `bos_org` |

### 3.9 状态 / 系统字段（L0 + L1 继承）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `status` | BillStatusField | 数据状态 |
| `enable` | BillStatusField | 业务状态（启用 / 禁用） |
| `creator` / `modifier` / `createtime` / `modifytime` | 平台维护 | 创建人 / 修改人 / 时间 |
| `masterid` | MasterIdField | 主数据内码 |
| `index` | IntegerField | 排序号 |
| `disabler` / `disabledate` | 平台维护 | 禁用人 / 禁用时间 |
| `enabler` / `enabledate` | 平台维护 | 启用人 / 启用时间 |
| `issyspreset` | CheckBoxField | 系统预置（不能删除） |
| `isdeleted` | CheckBoxField | 是否已删除（软删除标记） |
| `initdatasource` | ComboField | 数据来源 |
| `sourcesyskey` | TextField | 来源系统唯一标识 |

### 3.10 出厂数据字段（禁改 · PR-007）

| Field Key | 类型 | 业务含义 | minefield |
|---|---|---|---|
| `orinumber` | TextField | 出厂编码 | 🔴 red |
| `oriname` | MuliLangTextField | 出厂名称 | 🔴 red |
| `oristatus` | ComboField | 出厂数据编辑状态 | 🔴 red |

### 3.11 分录字段

#### 3.11.1 applicableorgentity（标准岗位适用组织 · 物理表 `t_hbpm_standposentry`）

| Field Key | 类型 | 业务含义 | 必填 | 引用实体 |
|---|---|---|---|---|
| `applicableorg` | HRAdminOrgField | 行政组织名称 | **✅** | → `haos_adminorghrf7` |
| `iscontainsu` | CheckBoxField | 是否包含下级 | ❌ | - |
| `entryboid` | BigIntField | 分录 boid | ❌ | - |

#### 3.11.2 entryentity（协作关系分录 · 运行时绑定）

根据 `PositionEdit.insertEntryEntity` L706-L716 实现 · 字段包含：
| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `id` | BigIntField | 关系行 id | - |
| `reporttype` | BasedataField | 汇报类型 | → `hbpm_reportcoreltype` |
| `targetpos` | HRPositionField | 协作目标岗位 | → `hbpm_positionhrf7` |

---

## 四、实体关系图

```
┌──────────────────────────────────────────┐
│  hbpm_positionhr (主实体 · 70+ 字段)      │
│  formId: hbpm_positionhr                  │
│  InheritPath: 4 级                        │
├──────────────────────────────────────────┤
│  id (PK)                                  │
│  number (唯一, 默认全局)                  │
│  name (MuliLangTextField 直存主表)        │
│  adminorg ──┐ (⭐ 必填)                   │
│  parent ──self (自引用 成环禁区 INV-02)   │
│  boid (时序 BO ID)                        │
│  bsed / bsled (时间轴)                    │
│  changetype/changeoperate/changescene     │
│  (三分类 · Op 自动填)                      │
└───┬──────────────────────────────────────┘
    │
    ├──► haos_adminorghrf7 (行政组织 · 必填)
    ├──► hbpm_positionhrf7 (父岗位 · 自引用)
    ├──► hbjm_jobhr (职位)
    ├──► hbpm_positiontype (岗位类型)
    ├──► hbpm_positiontpl (岗位模板 · 批量回填 8 字段)
    ├──► hbpm_changetype/changeoperate/changescene (变动三字典)
    ├──► hbjm_jobgradescmhr / jobgradehr (职等方案/职等)
    ├──► hbjm_joblevelscmhr / joblevelhr (职级方案/职级)
    ├──► hbjm_jobscmhr (职位体系方案)
    ├──► hbss_diploma / workplace (学历 / 工作地)
    ├──► bd_country / bd_admindivision (国家 / 城市)
    └──► applicableorgentity[] → t_hbpm_standposentry
                    ├── applicableorg → haos_adminorghrf7
                    └── iscontainsu / entryboid

下游引用 hbpm_positionhrf7 (27 处 · refentity_reverse.json 实证):
  ├─► 任职关系类 (hrpi_empposorgrel.position / hrpi_empjobrel.position / hrpi_rotationinfo.position / hrpi_dispatchinfo.position / hrpi_appointremoverel.position / hrpi_blacklist.position)
  ├─► 组织关系类 (haos_chargeperson.position / haos_orgpersonstaffinfo.position / haos_dimstaffreport.position / haos_muldimendetail.position / haos_muldimendetailhis.position)
  ├─► 岗位内部 (hbpm_positionhr.parent 自引用 / hbpm_positionrelation.role/parent / hbpm_chgrecord.position/targetposition/sourceposition/hisposition / hbpm_chgrecordevt.targetposition/sourceposition/hisposition / hbpm_position_msgdetail.bo/beforeversion/afterversion)
  └─► 预警 (hrcs_warnscheme.position)
```

---

## 五、时间轴模型（时序基础资料）

本实体继承 `hbp_histimeseqtpl`，因此是**带时间轴的版本链**。

```
同一岗位的多版本 (同一 boid)：
  ├── 初始版本 (datastatus='initial')
  ├── 当前生效版本 (iscurrentversion=true)
  ├── 过去版本 (iscurrentversion=false, bsled < 今天)
  └── 未来版本 (bsed > 今天，通过 newhisversion 产生)
```

### 关键时序字段

- **`boid`**：业务 ID，同一岗位的多版本共用同一 boid（遵循 PR-009 下游引用用 boid）
- **`bsed` / `bsled`**：当前版本的生效 / 失效日期
- **`firstbsed`**：最早生效日期（永远不变）
- **`iscurrentversion`**：由标品计算，自动维护（遵循 PR-008 查询用此过滤）
- **`datastatus`**：数据版本状态（初始 / 当前 / 历史）
- **`sourcevid`**：关联的源版本 ID（`PositionHr*Op.beginOperationTransaction` 自动写）
- **`hisversion`**：版本号（标品自动递增）

### 相关 opKey（9 个）

- `newhisversion` - 新增数据版本
- `change` - 变更
- `revise` - 修订（不产新版 · INV-07）
- `confirmchange` - 确认变更（触发 `PositionHrChangeOp`）
- `hiscopy` - 复制
- `hisversion_view` - 查看历史记录
- `reviserecord` - 版本修订历史
- `versionchangecompare` - 版本对比
- `showallversion` - 查看所有版本
- `his_save` - 历史保存（触发 `PositionHisSaveOp` + `JobLevelGradeRangeImportValidator(true)` + `PositionHisLoopValidator.checkHis()` + `PositionHisValidator`）

---

## 六、扩展规范

### 6.1 扩展入口

| 场景 | 扩展对象 | 方式 |
|---|---|---|
| 加字段 | **hbpm_positionhr** (主) | `modifyMeta op=add field` |
| 加子分录 | **hbpm_positionhr** | `modifyMeta op=add EntryEntity` |
| 改 `applicableorgentity` 分录 | 分录 scope | `modifyMeta op=add field parentScope=applicableorgentity` |
| 加校验 | hbpm_positionhr | `updateOperation.validations`（save / confirmchange / enable / disable / his_save） |
| 加插件 | hbpm_positionhr | `registerPlugin targetType=OPERATION`（并列挂 · PR-001） |
| 前端字段联动 | hbpm_positionhr | `registerPlugin targetType=BILL_FORM` |

### 6.2 字段扩展禁区 ⛔

- ❌ 修改 `number` 的类型 / 长度（下游 27 处引用）
- ❌ 修改 `adminorg` 的关联目标（岗位归属核心）
- ❌ 修改时序字段（`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `sourcevid` / `hisversion`）· PR-008/PR-009
- ❌ 修改出厂数据字段（`orinumber` / `oriname` / `oristatus`）
- ❌ 修改 `issyspreset` / `isdeleted`（破坏系统预置保护 / 软删除标记）
- ❌ 修改 `changetype` / `changeoperate` / `changescene`（由 Op 自动填 · 手改会被覆盖）
- ❌ 直接改 `t_hbpm_position` 表（绕过 `PositionHrSaveOp` 会丢版本链 + 协作关系）
- ❌ 自己扩展 `parent` 的成环校验逻辑（复杂时间段判断 · 参考 `PositionHisLoopValidator` 而非重写）

### 6.3 扩展关键特征

- 扩展 1 字段 = 主表 1 字段（多语言字段直接存主表 · 没有 `_l`）
- 分录扩展要考虑时态（新版本分录是独立复制还是共享）
- 时序基础资料扩展字段**必须考虑版本回溯**：加了新字段后历史版本该字段为 null，需要在数据迁移中补默认值
- ⚠️ **不同于职位**：职位有 `_i` 多语言子表；岗位多语言字段直接存主表

---

## 七、字段命名规范

- **字段 key**：`{ISV 前缀}_{语义}` 如 `${ISV_FLAG}_posstatus`（岗位状态）
- **字段 key 长度**：≤ 24 字符
- **列名**：`fk_{key}`（ISV 扩展）或 `f{key}`（标品，如 `fparentid`）
- **多语言字段直接加主表**：无需建 `_l` 子表
- **EntryEntity 命名**：`t_hbpm_{key}entry`（参考 `t_hbpm_standposentry`）

---

## 八、本场景独有的模型点

### 8.1 HRPositionField 自引用 ⭐

岗位实体的 `parent` 字段是**自引用**（指向自己这个实体的 F7 视图 `hbpm_positionhrf7`）· 这是岗位特有的组织树结构：
- F7 时自动排除自己：`beforeF7Select` 加 `QFilter("boid","!=",boid)`（`PositionEdit` L932-L936）
- 成环校验 `PositionHisLoopValidator`：按行政汇报类型 `reportingtype=1010` 检查同时期不成环

### 8.2 HRAdminOrgField × 2

岗位有 2 处 `HRAdminOrgField`：
- 主字段 `adminorg`：唯一必填字段 · 决定岗位的组织归属
- 分录字段 `applicableorgentity.applicableorg`：标准岗位适用范围（每行一个组织，含是否包含下级）

### 8.3 变动双分类（`changetype / changeoperate / changescene`）

岗位场景独有的 3 分类字段 · 由各 Op.beginOperationTransaction 自动写入 · **业务侧不要手动设置**（会被覆盖）。

### 8.4 岗位模板批量联动 8 字段

`positiontpl` 字段是岗位域特有的"模板批量回填"机制：
- 模板存 `job + jobscm + lowjoblevel + highjoblevel + lowjobgrade + highjobgrade + jobgradescm + joblevelscm` 8 字段的默认值
- 选模板时批量回填 + 按模板 `ablemodifyfield` 和 `fieldrange` 控制哪些字段可改

### 8.5 多语言字段不走 `_l` 子表

岗位共 10 个 `MuliLangTextField` 直接存在 `t_hbpm_position` 主表（与职位场景对比：职位走 `t_hbjm_job_i` 拆分表）· ISV 扩展多语言字段时无需考虑子表同步。

### 8.6 软删除而非物理删除

岗位没有 `delete` opKey（不在 57 opKey 列表）· 删除走 `isdeleted=1` 标记 · 列表自动过滤。

### 8.7 消息触发走调度任务

每次 save / enable / disable 都会触发 `ChangeMsgServiceImpl.sendMsg()`：
- 启动一个苍穹调度任务（`sch_task` · jobId `5/2/X9QCCFNS`）
- 调度任务读 `hbpm_position_msgdetail` 批量发消息（详见 05 数据流）

---

**📌 来源追溯**：
- 70+ 字段详情：`scene_doc.json` L65-L993
- 4 级继承链：`scene_doc.json.inheritance` 段 L42-L63
- 物理表 2 张：`scene_doc.json` L37 `physicalTable` + L959 `applicableorgentity.physicalTable`
- HRPositionField 语义：`PositionEdit.java` L183 / L932-L936 + `PositionHisLoopValidator` L91-L220
- HRAdminOrgField × 2：`scene_doc.json` L612-L628 + L960-L971
- 变动 3 分类预置编码：5 类反编译 `PositionHr*Op.beginOperationTransaction`
- 岗位模板字段集：`PositionEdit.java` L183-L184 / L517-L549
- 字段类型规则：`knowledge/cosmic_realworld_traps/buildmeta_traps.md`
