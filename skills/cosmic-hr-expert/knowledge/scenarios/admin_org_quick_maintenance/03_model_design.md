# 模型设计 · 行政组织快速维护

> **状态**: 🟢 基于 `knowledge/domain/org/ontology.md` + `adminorg_extension_pattern.md` 整合
> **数据源**: 元数据导出 + jar 反编译 + 实测 DYM
> **confidence**: verified (v1.0, 2026-04-18)

---

## ⭐ 关键业务事实 · 苍穹列表三层模型（参考管道坑 14.1）

本场景的"列表页"由**三层独立元数据**组成 · Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BaseFormModel | `haos_adminorgdetail`（主实体继承链 L3）| 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据）| `haos_adminorgtablist`（带页签行政组织树列表）| 列表 UI 壳 + 左树结构 + UI 层业务逻辑 |
| **F7 列表模板** | 动态表单（独立元数据）| `haos_orgtreelistf7`（F7 选择用）| F7 选择时的列表壳 |

**插件挂载职责分工**（两边都能挂 · 不要混）：
- **数据层过滤 / 权限 / setFilter** → 挂 `haos_adminorgdetail`（数据实体）
- **列表外壳 / 左树构造 / 动作按钮布局 / UI 逻辑** → 挂 `haos_adminorgtablist`（列表模板）
- **F7 选择框定制** → 挂 `haos_orgtreelistf7`

**重要**：列表表单模板 / F7 模板**不在** `knowledge/_shared/_standard_metadata/entity_metadata/*.md`（该目录只抓 BaseFormModel 类数据实体）· `form_catalog.json` 查不到是正常的（TODO：待建 `dynamic_form_template_index.json`）。

参考：
- `CS-06 · 列表按数据权限动态隐藏` 就是挂数据实体的典型例子
- 管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）的详细约束

---

## 一、核心认知：两层应用模型

苍穹行政组织采用**两层应用**设计：

```
haos_*  (数据层)      homs_*  (服务层)
 ├── haos_adminorg        ├── orgchgbill (变动单)
 ├── haos_adminorgdetail  ├── orgbatchchgbill (批量变动)
 ├── haos_adminorghis     ├── orgchgentry (变动分录)
 ├── haos_adminorgtype    └── orgchart_new (组织结构图)
 └── haos_adminorglayer
```

- **haos_\*** = **数据权威**，存储实体和历史
- **homs_\*** = **业务服务层**，调整单、审批流、批量处理

**⚠️ 关键事实**：`haos_adminorg` / `haos_adminorgdetail` / `haos_adminorghis` **三者共用同一张物理表**（元数据继承关系，不是数据关系）。

---

## 二、继承链（OpenAPI 实测 · 4 级）

```
bos_basetpl (基础资料模板) [9 字段, 无实体独有]
  └── hbp_bd_tpl_all (HR基础资料全页面模板) [19 字段, 无实体独有]
      └── hbp_histimeseqtpl (HR基础资料全页面历史模板) [28 字段, ⭐ 时序核心]
          └── haos_adminorg (组织基本信息-主) [自身 41 字段]  ← 4 级继承
              ├── haos_adminorgdetail  (组织快速维护视图, 共 83-85 字段)
              └── haos_adminorghis     (组织历史查询视图, 共 83 字段)
```

**总字段数**: 69 个（28 继承 + 41 自身）

**InheritPath (实抓)**: `1942c188000065ac,2+QE4JA9QV27,2/TYOWOSM0TE`
- `1942c188000065ac` = bos_basetpl
- `2+QE4JA9QV27` = hbp_bd_tpl_all
- `2/TYOWOSM0TE` = hbp_histimeseqtpl

### ⭐ 字段来源分类（定制开发视角非常重要）

| 层级 | 字段类型 | 数量 | 典型字段 | 能否改 |
|---|---|---|---|---|
| **bos_basetpl** | 基础资料通用字段 | 9 | - | 🔒 不改（破坏全系统） |
| **hbp_bd_tpl_all** | HR 基础资料通用 | 19 | - | 🔒 不改（破坏 HR 全线） |
| **hbp_histimeseqtpl** | HR 时序核心 ⭐ | 28 | `bsed`, `boid`, `iscurrentversion`, `hisversion`, `firstbsed`, `datastatus`, `sourcevid`, `disabler` | 🔒 不改（时序模型依赖） |
| **haos_adminorg 自身** | 行政组织业务字段 | 41 | `parentorg`, `adminorgtype`, `belongcompany`, `adminorglayer`, `level`, `longnumber`, `corporateorg` | ⚠️ 谨慎改 |

**关键认知**：
- ⚠️ 你看到的字段**只有 41 个真正属于 haos_adminorg**，另 28 个是从 HR 时序模板继承
- 🔒 继承字段**不能在 haos_adminorg 扩展里改**，只能修改 `hbp_histimeseqtpl`（代价极高）
- 🔒 如 `bsed` 的语义在 `hbp_histimeseqtpl` 就固定了：所有 HR 时序实体共用此字段
- ✅ 自己 ISV 扩展**加新字段到 haos_adminorg**，不要想"改继承字段"

**扩展哲学**：
- ✅ 扩展 **haos_adminorg**（主），下游 `detail` / `his` 自动获得
- ✅ 加新字段（ISV 前缀） = 安全，下游 detail/his 继承后自动可见
- ❌ 不要扩展 `haos_adminorgdetail` 或 `haos_adminorghis`（下游视图，扩展会引起继承混乱）
- ❌ 不要想"改继承字段"（如改 bsed 类型）—— 全 HR 全炸

**自动生成**（含完整继承字段分组）:
```bash
python scripts/gen_model_design_with_inheritance.py --form haos_adminorg
```
产出见 [`knowledge/_data/standard_forms/_inherit_haos_adminorg.md`](../../_data/standard_forms/_inherit_haos_adminorg.md)

---

## 三、核心实体清单

### 3.1 `haos_adminorg` · 行政组织主实体

- **Form ID**: `4S6B8I6U+EBN`
- **类型**: 基础资料 (basedata) + 历史时序
- **ISV**: `kingdee` (标品)
- **可扩展**: ✅ 推荐扩展方式 = 扩展字段 / 扩展分录

#### 字段表（OpenAPI 实抓 · 共 68 个字段）

> **数据源**: `/kapi/v2/devportal/ai-meta/getFormSchema?number=haos_adminorg`
> **表单名**: **组织基本信息（主）**
> **Form ID**: `4S6B8I6U+EBN`
> **自动同步**: `python scripts/gen_model_design_openapi.py --form haos_adminorg`

##### 核心业务字段

| 字段 Key | 类型 | 业务含义 | 必填 | 扩展性 | 引用 |
|---|---|---|---|---|---|
| `number` | TextField | 组织编码 | ✅ | ⚠️ 可改 | |
| `name` | MuliLangTextField | 组织名称 | ✅ | ⚠️ 可改 | |
| `enable` | BillStatusField | 业务状态 | ✅ | 🔒 系统 | |
| `establishmentdate` | DateField | **成立日期** | ✅ | ⚠️ 可改 | |
| `parentorg` | HRAdminOrgField | 上级行政组织 | ❌ | ⚠️ 可改 | →`haos_adminorghrf7` |
| `adminorgtype` | BasedataField | 行政组织类型 | ❌ | ⚠️ 可改 | →`haos_adminorgtype` |
| `adminorgfunction` | BasedataField | 行政组织职能 | ❌ | ⚠️ 可改 | →`haos_adminorgfunction` |
| `adminorglayer` | BasedataField | 管理层级 | ❌ | ⚠️ 可改 | →`haos_adminorglayer` |
| `corporateorg` | BasedataField | 法律实体 | ❌ | ⚠️ 可改 | →`hbss_lawentity` |
| `belongdept` | HRAdminOrgField | 所属部门 | ❌ | ⚠️ 可改 | →`haos_adminorghrf7` |
| `belongcompany` | HRAdminOrgField | 所属公司（**系统计算**） | ❌ | 🔒 系统 | →`haos_adminorghrf7` |
| `belongadminorg` | HRAdminOrgField | 所属行政组织 | ❌ | ⚠️ 可改 | →`haos_adminorghrf7` |
| `simplename` | MuliLangTextField | 简称 | ❌ | ⚠️ 可改 | |
| `fullname` | MuliLangTextField | 行政组织全称 | ❌ | ⚠️ 可改 | |
| `description` | MuliLangTextField | 描述 | ❌ | ⚠️ 可改 | |
| `index` | IntegerField | 排序号 | ❌ | ⚠️ 可改 | |

##### 时间轴字段

| 字段 Key | 类型 | 业务含义 |
|---|---|---|
| `bsed` | DateField | 业务生效日期 ⭐ |
| `bsled` | DateField | 业务预计失效日期 |
| `firstbsed` | DateField | 最早生效日期 |
| `hisversion` | TextField | 版本号 |
| `sourcevid` | BigIntField | 关联历史版本 |
| `iscurrentversion` | CheckBoxField | 是否当前生效数据 |
| `datastatus` | ComboField | 数据版本状态 |

##### 地域字段

| 字段 Key | 类型 | 业务含义 | 引用 |
|---|---|---|---|
| `companyarea` | BasedataField | 国家/地区 | →`bd_country` |
| `city` | BasedataField | 所在城市 | →`bd_admindivision` |
| `workplace` | BasedataField | 工作地 | →`hbss_workplace` |
| `detailaddress` | MuliLangTextField | 详细地址 | |

##### 结构字段（系统计算）

| 字段 Key | 类型 | 业务含义 | 扩展性 |
|---|---|---|---|
| `structlongnumber` | TextField | 组织上下级结构长编码 | 🔒 系统 |
| `structnumber` | TextField | 组织结构编码 | ⚠️ 可改 |
| `structfullname` | MuliLangTextField | 结构长名称 | ⚠️ 可改 |
| `level` | IntegerField | 物理层级 | 🔒 系统 |
| `isleaf` | CheckBoxField | 是否叶子 | ⚠️ 可改 |
| `deptlongname` | TextField | 部门长名称 | ⚠️ 可改 |
| `orglongname` | TextField | 所属长名称 | ⚠️ 可改 |
| `sortcode` | TextField | 排序码 | ⚠️ 可改 |

##### 状态 + 变动字段

| 字段 Key | 类型 | 业务含义 |
|---|---|---|
| `tobedisableflag` | CheckBoxField | 待停用 |
| `tobedisabledate` | DateField | 待停用日期 |
| `disabler` | UserField | 禁用人 |
| `disabledate` | DateTimeField | 停用日期 |
| `disbanddate` | DateField | 解散时间 |
| `fdiscardcourse` | TextField | 解散原因 |
| `changedescription` | TextField | 变更说明 |
| `companychangetype` | TextField | 所属公司变化类型 |
| `isvirtualorg` | CheckBoxField | 是否虚拟组织 |
| `otclassify` | BasedataField→`haos_otclassify` | 组织分类 |
| `industrytype` | BasedataField→`hbss_industrytype` | 行业类别 |
| `positioning` | MuliLangTextField | 定位 |
| `mainduty` | MuliLangTextField | 主要职责 |

##### 出厂字段

| 字段 Key | 类型 | 业务含义 |
|---|---|---|
| `orinumber` | TextField | 出厂编码 |
| `oriname` | MuliLangTextField | 出厂名称 |
| `oristatus` | ComboField | 出厂数据编辑状态 |
| `issyspreset` | CheckBoxField | 系统预置 |
| `initdatasource` | ComboField | 数据来源 |

##### 项目字段（项目团队专用）

| 字段 Key | 类型 | 业务含义 |
|---|---|---|
| `project` | BasedataField→`bd_project` | 所属项目 |
| `projectname` | MuliLangTextField | 项目名称 |
| `projectnumber` | TextField | 项目编号 |
| `projectidentify` | TextField | 项目名称标识码 |
| `startdate` | DateField | 项目团队生效日期 |

##### 系统字段（全部禁止手改）

| 字段 Key | 类型 | 业务含义 |
|---|---|---|
| `creator` | CreaterField→`bos_user` | 创建人 |
| `modifier` | ModifierField→`bos_user` | 修改人 |
| `createtime` | CreateDateField | 创建时间 |
| `modifytime` | ModifyDateField | 修改时间 |
| `masterid` | MasterIdField | 主数据内码 |
| `boid` | BigIntField | 业务ID |
| `status` | BillStatusField | 数据状态 |
| `org` | OrgField→`bos_org` | 组织体系管理组织 |
| `isdeleted` | CheckBoxField | 是否已删除 |
| `sourcesyskey` | TextField | 来源系统唯一标识 |

#### 🚨 系统计算字段（OpenAPI 实测）

由系统自动计算，**禁止手改**：

- `belongcompany` - 所属公司（沿 parentorg 找公司/集团）
- `structlongnumber` - 组织上下级结构长编码
- `level` - 物理层级
- `orglongname` / `deptlongname` / `structfullname` - 派生的长名称
- 系统字段 `creator/modifier/createtime/modifytime/masterid/status` 等

**手改后果**: 数据不一致、下游薪酬/考勤取数错乱

#### ⭐ 实测纠偏（与经验的偏差）

本次 OpenAPI 实抓发现 v1.0 骨架的偏差：

| 经验以为 | 实测真相 |
|---|---|
| `bsed` 必填 | ❌ 实际非必填 |
| `adminorgtype` 必填 | ❌ 实际非必填 |
| `parentorg` 必填（除根） | ❌ 实际非必填（系统自动处理） |
| `establishmentdate` 非必填 | ✅ **实际必填** |
| `enable` 是 ComboField | ❌ 实际是 `BillStatusField`（不同语义） |
| `corporateorg` 引用 `haos_adminorg` | ❌ 实际引用 `hbss_lawentity`（单独法律实体） |
| `adminorg*` 引用自身 | ❌ 实际引用 `haos_adminorghrf7`（F7 查询视图） |
| 仅 4 个必填字段 | `number / name / enable / establishmentdate` |

⚠️ **教训**: 只凭经验写字段表会出错。**必须通过 getFormSchema 实抓校准**。

#### `belongcompany` 推算算法（标品逻辑）

```
if 自己是"公司"或"集团":
    belongcompany = 自己
else:
    沿 parentorg 向上递归查找，直到找到"公司"或"集团"
```

### 3.2 `haos_adminorgdetail` · 快速维护视图

- **定位**: 组织快速维护的**日常操作入口**
- **共用物理表**: 与 haos_adminorg 同表
- **区别**: UI 视图不同，字段可编辑性差异（如 bsed 可改）
- **Snapshot 字段数**: 46 字段（详情）× 35 字段（列表）

### 3.3 `haos_adminorgtablist` · 列表视图

- **定位**: 列表展示入口（`核心人事 > 组织 > 快速维护`）
- **典型 URL**: `/formId=haos_adminorgtablist`

### 3.4 其他相关实体

| 实体 | 作用 |
|---|---|
| `haos_adminorgtype` | 组织类型字典（公司/部门/事业部） |
| `haos_adminorgtypestd` | 组织类型标准值 |
| `haos_adminorglayer` | 组织层级字典 |
| `haos_adminorgfunction` | 组织职能字典 |
| `haos_adminorgbatch` | 批量建组织 |
| `haos_adminorgdetailfuture` | 未来生效的组织（bsed 未来） |
| `haos_adminorgdetailinit` | 初始化组织 |
| `haos_adminorghrf7` | 行政组织 F7 选择控件 |

### 3.5 变动族（homs_\* 服务层）

| 实体 | 作用 |
|---|---|
| `orgchgbill` | 组织调整申请单（走审批流） |
| `orgbatchchgbill` | 批量组织调整单 |
| `orgchgentry` | 变动分录（含 4 前缀分录容器） |
| `changescene` | 变动场景字典 |
| `changescenesub` | 变动场景子类 |
| `orgchangereason` | 变动原因字典 |
| `orgchangetype` | 变动类型（5 种：新设/调整/停用/启用/修订） |
| `orgchgeffectlog` | 变动生效日志 |

---

## 四、实体关系图

```
┌───────────────────────────────┐
│  haos_adminorg (主实体)         │
│  ParentId: 4S6B8I6U+EBN        │
├───────────────────────────────┤
│  id (PK)                       │
│  number (UK, 全局唯一)          │
│  name (多语言)                  │
│  parentorg ──┐ (自关联)         │
│  level (计算)                   │
│  longnumber (计算)              │
│  belongcompany (计算)           │
│  bsed (时间轴核心)              │
└───┬────────┬───────────────────┘
    │        │
    │        │ parentorg (形成组织树)
    │        ▼
    │   ┌───────────────┐
    │   │ haos_adminorg │
    │   │   (上级)       │
    │   └───────────────┘
    │
    ├──► haos_adminorgtype      (组织类型)
    ├──► haos_adminorglayer     (组织层次)
    ├──► haos_adminorgfunction  (组织职能)
    ├──► haos_adminorg          (所属公司, 系统计算)
    └──► haos_adminorg          (法人组织)

下游引用 (被哪些实体引用):
  ├─► hrpi_employee.adminorg        (员工归属组织)
  ├─► hbpm_position.adminorg        (岗位归属组织)
  ├─► pay_salary_archive.adminorg   (薪酬成本中心)
  ├─► att_schedule.adminorg         (考勤归属)
  └─► perf_template.adminorg        (绩效范围)
```

---

## 五、时间轴模型（4 时态）

这是组织域的**核心设计**，非常重要：

```
现在时 (adminorg / adminorgdetail)    ← 当前生效版本
   │
   ├─ 过去时 (adminorghis)              ← 历史版本（bsed 已过）
   │
   ├─ 未来时 (adminorgdetailfuture)     ← 未来生效版本（bsed 未来）
   │
   └─ 初始时 (adminorgdetailinit)       ← 系统初始化时的快照
```

**同一组织的多版本，都在同一张物理表里**，通过 `bsed` 区分。

### `bsed` vs `establishmentdate`

- **bsed** (business start effective date): **业务生效日期**，时间轴版本标识
- **establishmentdate**: 组织成立日期（业务属性）

**决策**:
- 变动单生效时间用 **bsed**
- 新设组织的"成立时间"用 **establishmentdate**

---

## 六、扩展规范

### 6.1 扩展入口

| 场景 | 扩展对象 | 方式 |
|---|---|---|
| 加字段 | **haos_adminorg** (主，不是 detail) | `modifyMeta op=add field` |
| 加分录（附表） | **haos_adminorg** | `modifyMeta op=add EntryEntity` |
| 加校验 | haos_adminorg | `addRule` 或 `updateOperation.validations` |
| 加插件 | haos_adminorg | `registerPlugin targetType=BILL_FORM / OPERATION` |

### 6.2 字段扩展禁区 ⛔

- ❌ 修改 `number` 的类型/长度（标品级强依赖）
- ❌ 修改 `parentorg` 的关联目标（破坏组织树）
- ❌ 修改 `level` / `longnumber` / `structlongnumber` / `belongcompany` 的计算逻辑
- ❌ 直接修改 `haos_adminorgdetail` 的字段（下游视图）
- ❌ 直接改 `haos_adminorghis` 表（历史表，标品自动维护）
- ❌ 绕过变动单直接改 `adminorgdetail` 数据（断审计链）

### 6.3 扩展关键特征（实测）

- 扩展 **1 个字段** = **主表 1 个字段 + 调整单 4 前缀 × 1 = 共 5 个字段**
  （详见 `orgbill_4prefix_cascade` Pattern）
- 扩展**不需要派生 _e / _ly / _emly**（区别于员工附表）
- 扩展**不需要 EmbedFormAp**（区别于员工档案附表）
- 扩展**不需要权限预置 SQL**（T_HRCS_ENTITYFORBID）

---

## 七、字段命名规范

- **字段 key**: `{ISV前缀}_{语义}` 如 `${ISV_FLAG}_region_code`
- **字段 key 长度**: ≤24 字符
- **列名**: `fk_{key}` (ISV 扩展) 或 `f{key}` (标品)
- **实体 key**: ≤36 字符
- **表名**: ≤25 字符（数据库硬上限）

---

**📌 来源追溯**：
- 实体清单、继承链、时态模型：`knowledge/domain/org/ontology.md`
- 扩展规范、禁区：`knowledge/domain/org/anchors.md` + `adminorg_extension_pattern.md`
- 字段类型规则：`knowledge/cosmic_realworld_traps/buildmeta_traps.md`
