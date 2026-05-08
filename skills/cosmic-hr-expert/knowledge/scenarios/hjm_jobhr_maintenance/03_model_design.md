# 模型设计 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `scene_doc.json` (64 字段实抓) + `_auto_inherit_chain.md` (5 级继承) + `_auto_plugin_semantics.md` 反编译整合
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `getFormMetadata.InheritPath` + `javap -p` 反编译 (2026-04-24)

---

## ⭐ 关键业务事实 · 苍穹列表三层模型（参考管道坑 14.1）

本场景列表页由**三层独立元数据**组成 · Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BaseFormModel | `hbjm_jobhr`（主实体 · 菜单直挂）| 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据）| ⚠ 待探针确认 · 一般 `hbjm_joblist*` 类 | 列表 UI 壳 + UI 层业务逻辑 |
| **F7 列表模板** | 动态表单（独立元数据）| ⚠ 待探针确认 · 一般 `hbjm_jobf7list` 类 | F7 选择时的列表壳 |

**⚠ hjm_jobhr 特殊**：菜单直接挂数据实体 `hbjm_jobhr`（不像 admin_org 菜单挂 `haos_adminorgdetail` 这种子视图）· 参考 `probe_snapshot.json.menu.formNumber`。

**插件挂载职责分工**（两边都能挂 · 不要混）：
- **数据层过滤 / 权限 / setFilter** → 挂 `hbjm_jobhr`（数据实体）
- **列表外壳 / 动作按钮布局 / UI 逻辑** → 挂列表表单模板（待探）
- **F7 选择框定制** → 挂 F7 列表模板（待探）

**重要**：列表表单模板 / F7 模板**不在** `knowledge/_shared/_standard_metadata/entity_metadata/*.md`（该目录只抓 BaseFormModel 类数据实体）· `form_catalog.json` 查不到是正常的（TODO：待建 `dynamic_form_template_index.json`）。

参考：
- 管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）的详细约束
- 对称场景：position 的 `hbpm_positionorgtreelist`（列表模板）/ `hbpm_posorgtreelistf7`（F7 模板）· admin_org 的 `haos_adminorgtablist` / `haos_orgtreelistf7`

---

## 一、核心认知：HR 时序基础资料 + 多语言子表

苍穹职位采用**时序基础资料 + 多语言子表**设计：

```
hbjm_jobhr（职位主实体，组织发展云 · 职位管理）
 ├── t_hbjm_job           主物理表（64 字段中的非多语言字段）
 ├── t_hbjm_job_i         多语言子表（12 个 MuliLangTextField）
 └── t_hbjm_jobscmmul     MulBasedataField 子表（jobscm 职位体系方案）
```

**3 张物理表关系**（`scene_doc.json` L26 `physicalTable`）：
- `t_hbjm_job` = 权威 1:1 存储
- `t_hbjm_job_i` = 多语言 1:N（每语言一行）
- `t_hbjm_jobscmmul` = jobscm 多选基础资料子表（MulBasedataField 隐式子表）

---

## 二、继承链（OpenAPI 实测 · 5 级）

### InheritPath 原始 ID 序列（`_auto_inherit_chain.md` L12-L16）

```
L0  1942c188000065ac   → bos_basetpl（基础资料模板，9 字段）
L1  ab7efc31000015ac   → hr_base_* (HR 基础资料父模板)
L2  2+QPVWEP=LYN       → hbp_bd_tpl_all（HR 基础资料全页面）
L3  2/TYVXZRP5/F       → hbp_histimeseqtpl（HR 基础资料时序模板）⭐ 时态核心
L4  21VC3A4THFOS       → hbjm_jobhr（自身 · 约 35 业务字段）
```

### ⭐ 字段来源分类

| 层级 | 字段类型 | 典型字段 | 能否改 |
|---|---|---|---|
| **L0 bos_basetpl** | 基础资料通用 | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `status` / `enable` | 🔒 不改（破坏全系统） |
| **L1 / L2 hbp_bd_tpl_all** | HR 基础资料通用 | `createorg` / `org` / `useorg` / `ctrlstrategy` / `srccreateorg` / `sourcedata` / `bitindex` / `srcindex` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` / `issyspreset` / `index` / `simplename` / `description` / `sourcesyskey` | 🔒 不改（破坏 HR 全线） |
| **L3 hbp_histimeseqtpl** ⭐ | HR 时序核心 | `boid` / `iscurrentversion` / `datastatus` / `sourcevid` / `firstbsed` / `bsed` / `bsled` / `changedescription` / `hisversion` | 🔒 不改（时序模型依赖） |
| **L4 hbjm_jobhr 自身** | 职位业务字段 | `jobseq` / `jobfamily` / `jobclass` / `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` / `jobtype` / `jobgradescm` / `joblevelscm` / `jobscm` / `depcytype` / `diplomareq` / `agereq` / `knowledgereq` / `skillreq` / `abilityreq` / `experiencereq` / `joborientation` / `jobduty` / `jobstandard` / `joblevelrang` / `jobgraderang` / `highjoblevelname` / `lowjoblevelname` / `lowjobgradename` / `highjobgradename` | ⚠️ 谨慎改（继承方案字段等） |

**关键认知**：
- ⚠️ `scene_doc.json` 共 64 字段，其中**大约 35 个属于 hbjm_jobhr 自身**，其余继承自父模板
- 🔒 时序字段语义在 `hbp_histimeseqtpl` 就固定了：所有 HR 时序基础资料共用此字段组
- ✅ ISV 扩展应加新字段到 `hbjm_jobhr`（带 ISV 前缀），不要想"改继承字段"

---

## 三、完整字段表（OpenAPI `scene_doc.json` 实抓 · 共 64 个字段）

### 3.1 业务核心字段（职位域特有）

| Field Key | 类型 | 业务含义 | 必填 | 扩展性 | 引用实体 |
|---|---|---|---|---|---|
| `number` | TextField | 职位编码 | ❌ | ⚠️ 可改（创建后锁） | - |
| `name` | MuliLangTextField | 职位名称 | ❌ | ⚠️ 可改 | - |
| `simplename` | MuliLangTextField | 职位简称 | ❌ | ⚠️ 可改 | - |
| `description` | MuliLangTextField | 描述 | ❌ | ⚠️ 可改 | - |
| **`jobseq`** | **HisModelBasedataField** | **职位序列** ⭐ 唯一必填 | **✅** | 🔒 系统（`isvCanModify: false`） | → `hbjm_jobseqhr` |
| `jobfamily` | HisModelBasedataField | 职位族 | ❌ | ⚠️ 可改 | → `hbjm_jobfamilyhr` |
| `jobclass` | HisModelBasedataField | 职位类 | ❌ | ⚠️ 可改 | → `hbjm_jobclasshr` |
| `jobtype` | BasedataField | 职位类别 | ❌ | ⚠️ 可改 | → `hbjm_jobtype` |

### 3.2 职级 / 职等方案字段（双维度）

| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `jobgradescm` | BasedataField | 职等方案 | → `hbjm_jobgradescmhr` |
| `joblevelscm` | BasedataField | 职级方案 | → `hbjm_joblevelscmhr` |
| `jobscm` | MulBasedataField | 职位体系方案（多选，子表 `t_hbjm_jobscmmul`） | - |
| `lowjoblevel` | BasedataField | 最低职级 | → `hbjm_joblevelhr` |
| `highjoblevel` | BasedataField | 最高职级 | → `hbjm_joblevelhr` |
| `lowjobgrade` | BasedataField | 最低职等 | → `hbjm_jobgradehr` |
| `highjobgrade` | BasedataField | 最高职等 | → `hbjm_jobgradehr` |
| `lowjoblevelname` | TextField | 最低职级（显示名缓存） | - |
| `highjoblevelname` | TextField | 最高职级（显示名缓存） | - |
| `lowjobgradename` | TextField | 最低职等（显示名缓存） | - |
| `highjobgradename` | TextField | 最高职等（显示名缓存） | - |
| `joblevelrang` | TextField | 职级范围（拼接串） | - |
| `jobgraderang` | TextField | 职等范围（拼接串） | - |

⚠️ **注意**：`*levelname` / `*gradename` 是为列表显示而冗余存储的字段，修改 `lowjoblevel` / `highjoblevel` 时需要同步更新这些派生字段（通常由 `JobHrSaveOp` 内部处理）。

### 3.3 招聘要求字段（6 个 MuliLangTextField）

| Field Key | 业务含义 |
|---|---|
| `diplomareq` (BasedataField→`hbss_diploma`) | 学历要求 |
| `agereq` (MuliLangTextField) | 年龄要求 |
| `knowledgereq` | 知识要求 |
| `skillreq` | 技能要求 |
| `abilityreq` | 能力要求 |
| `experiencereq` | 经验要求 |

### 3.4 职责字段（4 个 MuliLangTextField）

| Field Key | 业务含义 |
|---|---|
| `joborientation` | 定位 |
| `jobduty` | 主要职责 |
| `jobstandard` | 衡量标准 |

### 3.5 ⚠️ 废弃字段

| Field Key | 状态 |
|---|---|
| `depcytype` (BasedataField→`hbss_depcytype`) | **已废弃**（`scene_doc.json` L566 `displayName: "属地员工类别(废弃)"`） |

### 3.6 时态字段（L3 hbp_histimeseqtpl 继承，禁改）

| Field Key | 类型 | 业务含义 | minefield |
|---|---|---|---|
| `boid` | BigIntField | 业务 ID ⭐ | 🔴 red |
| `iscurrentversion` | CheckBoxField | 是否当前生效数据 | 🔴 red |
| `datastatus` | ComboField | 数据版本状态 | 🔴 red |
| `sourcevid` | BigIntField | 关联历史版本 | 🔴 red |
| `firstbsed` | DateField | 最早生效日期 | 🔴 red |
| **`bsed`** | **DateField** | **生效日期** ⭐ | 🟡 yellow |
| **`bsled`** | **DateField** | **失效日期** ⭐ | 🟡 yellow |
| `changedescription` | TextField | 变更说明 | - |
| `hisversion` | TextField | 版本号 | 🔴 red |

### 3.7 组织 / 控制字段（L2 hbp_bd_tpl_all 继承）

| Field Key | 类型 | 业务含义 | 引用实体 |
|---|---|---|---|
| `createorg` | OrgField | 创建组织 | → `bos_org` |
| `org` | OrgField | 管理组织 | → `bos_org` |
| `useorg` | OrgField | 使用组织 | → `bos_org` |
| `srccreateorg` | OrgField | 原创建组织 | → `bos_org` |
| `ctrlstrategy` | ComboField | 控制策略 | - |

### 3.8 状态 / 系统字段（L0 + L2 继承）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `status` | BillStatusField | 数据状态（A/B/C 暂存/提交/审核） |
| `enable` | BillStatusField | 业务状态（启用 / 禁用） |
| `creator` | CreaterField→`bos_user` | 创建人 |
| `modifier` | ModifierField→`bos_user` | 修改人 |
| `createtime` | CreateDateField | 创建时间 |
| `modifytime` | ModifyDateField | 修改时间 |
| `masterid` | MasterIdField | 主数据内码 |
| `index` | IntegerField | 排序号 |
| `disabler` | UserField→`bos_user` | 禁用人 |
| `disabledate` | DateTimeField | 禁用时间 |
| `issyspreset` | CheckBoxField | 系统预置 |
| `initdatasource` | ComboField | 数据来源 |
| `sourcesyskey` | TextField | 来源系统唯一标识 |

### 3.9 出厂数据字段（禁改）

| Field Key | 类型 | 业务含义 | minefield |
|---|---|---|---|
| `orinumber` | TextField | 出厂编码 | 🔴 red |
| `oriname` | MuliLangTextField | 出厂名称 | 🔴 red |
| `oristatus` | ComboField | 出厂数据编辑状态 | 🔴 red |

### 3.10 其他位图 / 索引字段（HR 基础资料模板）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `sourcedata` | BigIntField | 原资料 id |
| `bitindex` | IntegerField | 位图 |
| `srcindex` | IntegerField | 原资料位图 |

---

## 四、实体关系图

```
┌─────────────────────────────────────┐
│  hbjm_jobhr (主实体 · 64 字段)       │
│  formId: hbjm_jobhr                  │
│  InheritPath: 5 级                   │
├─────────────────────────────────────┤
│  id (PK)                             │
│  number (唯一, 默认全局)              │
│  name (多语言 · t_hbjm_job_i)         │
│  jobseq ──┐ (⭐ 必填)                 │
│  jobfamily ── hbjm_jobfamilyhr       │
│  jobclass  ── hbjm_jobclasshr        │
│  boid (时序 BO ID)                    │
│  bsed / bsled (时间轴)                │
└───┬─────────────────────────────────┘
    │
    ├──► hbjm_jobseqhr (职位序列 · 时序 F7) ⭐ 必填
    ├──► hbjm_jobfamilyhr (职位族)
    ├──► hbjm_jobclasshr (职位类)
    ├──► hbjm_jobtype (职位类别)
    ├──► hbjm_jobgradescmhr (职等方案)
    ├──► hbjm_joblevelscmhr (职级方案)
    ├──► hbjm_joblevelhr (最低 / 最高职级 × 2)
    ├──► hbjm_jobgradehr (最低 / 最高职等 × 2)
    ├──► hbss_diploma (学历要求)
    └──► hbss_depcytype (⚠️ 废弃)

下游引用（被哪些实体引用，推断）:
  ├─► hbpm_position.job           (岗位归属职位)
  ├─► hrpi_pemployee.job          (人员当前职位)
  ├─► hrpi_empjobresume.job       (人员职业经历)
  └─► pay_* 薪酬档案可能按职位定薪
```

⚠️ **下游引用属推断**：hbjm 场景 probe 未扫描 downstream，scene_doc.json L824 `downstream: []` 为空，具体下游需专家补充。

---

## 五、时间轴模型（时序基础资料）

本实体继承 `hbp_histimeseqtpl`，因此是**带时间轴的版本链**。

```
同一职位的多版本 (同一 boid)：
  ├── 初始版本 (datastatus='initial')
  ├── 当前生效版本 (iscurrentversion=true)
  ├── 过去版本 (iscurrentversion=false, bsled < 今天)
  └── 未来版本 (bsed > 今天，通过 newhisversion 产生)
```

### 关键时序字段

- **`boid`**：业务 ID，同一职位的多版本共用同一 boid
- **`bsed` / `bsled`**：当前版本的生效 / 失效日期
- **`firstbsed`**：最早生效日期（永远不变）
- **`iscurrentversion`**：由标品计算，自动维护
- **`datastatus`**：数据版本状态（初始 / 当前 / 历史）
- **`sourcevid`**：关联的源版本 ID（`JobHrMsgHandleOp` 会写此字段）
- **`hisversion`**：版本号（标品自动递增）

### 相关 opKey

- `newhisversion` - 新增数据版本
- `change` - 变更
- `revise` - 修订
- `confirmchange` - 确认变更（触发 `JobHrSaveOp` 复用保存链）
- `reviserecord` / `versionchangecompare` / `showallversion` - 3 个只读查询

---

## 六、扩展规范

### 6.1 扩展入口

| 场景 | 扩展对象 | 方式 |
|---|---|---|
| 加字段 | **hbjm_jobhr** (主) | `modifyMeta op=add field` |
| 加子分录 | **hbjm_jobhr** | `modifyMeta op=add EntryEntity` |
| 加校验 | hbjm_jobhr | `updateOperation.validations`（save / audit / enable） |
| 加插件 | hbjm_jobhr | `registerPlugin targetType=OPERATION` |
| 前端字段联动 | hbjm_jobhr | `registerPlugin targetType=BILL_FORM` |

### 6.2 字段扩展禁区 ⛔

- ❌ 修改 `number` 的类型 / 长度（下游岗位 / 人员引用）
- ❌ 修改 `jobseq` 的关联目标（职位体系核心）
- ❌ 修改时序字段（`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `sourcevid` / `hisversion`）
- ❌ 修改出厂数据字段（`orinumber` / `oriname` / `oristatus`）
- ❌ 修改 `issyspreset`（破坏系统预置保护）
- ❌ 直接改 `t_hbjm_job` 表（绕过 `JobHrSaveOp` 会丢版本）
- ❌ 在新规则引用废弃字段 `depcytype`

### 6.3 扩展关键特征

- 扩展 1 字段 = 主表 1 字段 + `t_hbjm_job_i` 自动处理多语言
- 不需要 4 前缀分录（区别于行政组织调整申请单）
- 不需要额外建历史表（时序模板自动处理）
- 时序基础资料扩展字段**必须考虑版本回溯**：加了新字段后历史版本该字段为 null，需要在 `JobHrSaveOp` 或数据迁移中补默认值

---

## 七、字段命名规范

- **字段 key**：`{ISV 前缀}_{语义}` 如 `${ISV_FLAG}_jobzt`（职位状态）
- **字段 key 长度**：≤ 24 字符
- **列名**：`fk_{key}`（ISV 扩展）或 `f{key}`（标品，如 `fjobseqid`）
- **MulBasedataField 子表命名**：`t_hbjm_{key}mul`（参考 `t_hbjm_jobscmmul`）
- **多语言字段自动挂 `_i` 子表**：无需额外建表

---

## 八、本场景独有的模型点

### 8.1 HisModelBasedataField × 3

职位实体有 3 个 `HisModelBasedataField` 字段（时序基础资料引用），这是**本场景的特色**：

- `jobseq` → `hbjm_jobseqhr`（职位序列）
- `jobfamily` → `hbjm_jobfamilyhr`（职位族）
- `jobclass` → `hbjm_jobclasshr`（职位类）

**区别于普通 BasedataField**：HisModelBasedataField 会自动跟随被引用实体的时序版本，所以选择一个职位序列时实际选择的是某个 bsed 下的特定版本。

### 8.2 MulBasedataField jobscm

`jobscm`（职位体系方案）是**唯一的多选基础资料**，自动生成隐式子表 `t_hbjm_jobscmmul`，用于支持一个职位归属多个方案。

### 8.3 级联字段 lowjoblevelname × 4

列表展示用的冗余字段（`lowjoblevelname` / `highjoblevelname` / `lowjobgradename` / `highjobgradename`）在保存时必须和引用字段（`lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade`）同步，否则列表显示会和 F7 选择不一致。此逻辑通常已在 `JobHrSaveOp` 内实现（专家可反编译确认）。

---

**📌 来源追溯**：
- 64 字段详情：`scene_doc.json` L31-L821
- 5 级继承链：`_auto_inherit_chain.md` L12-L16
- 物理表 3 张：`scene_doc.json` L27 `physicalTable` + L805 `jobscm.physicalColumn`
- HisModelBasedataField：`scene_doc.json` L662-L695 jobseq / jobfamily / jobclass
- 必填规则：`scene_doc.json` L664 `jobseq.required: true`
- 废弃字段：`scene_doc.json` L566 depcytype displayName
- 字段类型规则：`knowledge/cosmic_realworld_traps/buildmeta_traps.md`
