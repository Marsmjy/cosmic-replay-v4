# 数据模型设计 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 35 字段 scene_doc.json + main_form.xml + 14 反编译类双向核对
> **confidence**：verified

## 1. 标识与定位

| 项 | 值 | 来源 |
|---|---|---|
| 业务场景中文名 | 矩阵组织设置（也叫"结构化方案"母本）| `scenario.json` menu_paths |
| formNumber | `haos_structproject` | `scene_doc.json._meta.formId` |
| formId 技术码 | `3BPVOPG05AFA` | `scene_doc.json._meta.formIdTechnical` |
| ModelType | **`BaseFormModel`** | scene_doc.json header.modelType |
| 物理表 | **`t_haos_structproject`**（与 haos_structure 共用！）| `scene_doc.json.basicInfo.physicalTable` |
| 多语言表 | `t_haos_structproject_l` | scene_doc.json.basicInfo |
| 应用 | HR基础组织（haos · `bizappId=W11R1282DJK`）| _auto_facts.md |
| 云 | HR基础服务云（HRMP）| _meta.cloud |
| 域 | 组织发展 / 行政组织维护 | menu_paths |
| 父模板 | `2+QE4JA9QV27`（hbp_bd_tpl_all · HR 基础资料全页面模板）| `_auto_inherit_chain.md` |
| 继承路径 | `1942c188000065ac → 2+QE4JA9QV27` | `_auto_inherit_chain.md` |
| Isv | `kingdee` · 标品 | main_form.xml header |

## 2. 关键架构 · 双 form 共用一张物理表（核心 fact）

**结构化方案系列共用 `t_haos_structproject`** —— 这是本场景最核心的架构 fact。类比 hbpm_position 域 `hbpm_positionhr` / `hbpm_stposition` 共用 `t_hbpm_position`（用 isstandardpos 区分）· 类比 admin_org 域 `haos_adminorg` / `haos_adminorgdetail` 共用 `t_haos_adminorg`。

| formNumber | 中文 | 角色 | 物理表 | 业务区分 |
|---|---|---|---|---|
| `haos_structproject`（本场景）| 矩阵组织设置（结构化方案母本）| 母本 / 元数据级方案 | `t_haos_structproject` | `roottype` ComboField + 业务语义"母本"（issyspreset 预置 / 用户自建）|
| `haos_structure` | 矩阵组织维护 | 矩阵组织实例 | `t_haos_structproject`（同表！）| 实例 `otclassify=1010L`（业务硬编码）+ relyonstructproject 引母本 |

> **关键区分键**（main_form.xml 实证）：
> - `roottype` ComboField · 取值 1（实组织）/ 2（虚拟根组织）· `StructProjectEditPlugin.afterBindData` L144 + `propertyChanged` L253 处反复使用
> - `iscustomorg` CheckBoxField · 区分系统派生视图 vs 用户自建矩阵 · `StructProjectListPlugin.setFilter` L100 默认过滤 `iscustomorg='0'`
> - `issyspreset` CheckBoxField · 区分预置 vs 用户自建 · `StructProjectListPlugin.setFilter` L98-99 排预置但保留 STRUCT_PROJECT_MANAGE
> - `relyonstructproject` BasedataField · 反指引 haos_structproject 自身 · 是 haos_structure 实例引用本方案的桥
>
> **`otclassify=1010L` 硬编码语义**（来自配套场景 haos_structure_maintenance/03 + StructProjectSaveOp L103 实证）：
> - StructProjectSaveOp 在 roottype=2（虚拟根）时给虚拟组织自动设 `otclassify=1010L`
> - haos_structure 实例数据全部 `otclassify=1010L`
> - haos_structproject 母本数据 `otclassify` 取业务方案分类（≠ 1010 通常）

> **数据依赖反向引用**：`relyonstructproject`（依赖架构方案 · BasedataField → `haos_structproject`）
> - 自引用：本场景的 `relyonstructproject` 字段也指向 `haos_structproject` 自己 · 业务上是"方案级链式依赖"
> - haos_structure 实例的 `relyonstructproject` 引本方案 · 这是母-子关系的核心桥梁

## 3. 35 字段按业务语义分组

> 数据源：`scene_doc.json.fields`（35 个字段 · 单实体 · 全部 `physicalTable=t_haos_structproject`）
> 注：本表无 EntryEntity（虽 opKey 有 newentry/deleteentry · 但那是基础资料模板兜底 · main_form.xml 没真正的子分录）

### 3.1 主键 / 编码 / 名称（基础资料标准 6 字段）

| key | type | 必填 | ISV 可改 | 物理列 | 说明 |
|---|---|---|---|---|---|
| `id`（隐式 PK · 不在 scene_doc 显式列）| LongField | - | 否 | `fid` | 平台主键 · `kd.bos.id.ID.genLongId()`（PR-005）|
| `number` | TextField | **是** | 否 | `t_haos_structproject.fnumber` | 编码 · 可走 CodeRule 自动生成 |
| `name` | MuliLangTextField | **是** | 否 | `t_haos_structproject_l.fname` | 名称 · 多语言 · 落 `_l` 子表 |
| `simplename` | MuliLangTextField | 否 | 是 | `t_haos_structproject_l.fsimplename` | 简称 |
| `description` | MuliLangTextField | 否 | 是 | `t_haos_structproject_l.fdescription` | 描述 |
| `index` | IntegerField | 否 | 是 | `t_haos_structproject.findex` | 排序号 |

### 3.2 系统字段（autoComputed · ISV 不可改 · 红色雷区 · 8 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `creator` | CreaterField | `fcreatorid` | 创建人 → bos_user · `RequestContext.get().getCurrUserId()` |
| `modifier` | ModifierField | `fmodifierid` | 修改人 → bos_user |
| `createtime` | CreateDateField | `fcreatetime` | 创建时间 |
| `modifytime` | ModifyDateField | `fmodifytime` | 修改时间 |
| `masterid` | MasterIdField | `fmasterid` | 主数据内码（基础资料标准）|
| `disabler` | UserField | `fdisablerid` | 禁用人 · 由 disable opKey（HRBaseDataLogOp）回填 |
| `disabledate` | DateTimeField | `fdisabledate` | 禁用时间 |
| `issyspreset` | CheckBoxField | `fissyspreset` | 系统预置 · `StructProjectListPlugin.setFilter` L98 默认过滤 `='0'` |

### 3.3 状态字段（黄色雷区 · 改了级联下游 · 2 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `status` | BillStatusField | `fstatus` | 数据状态 · A/B/C → audit/submit/unaudit 流转 |
| `enable` | BillStatusField | `fenable` | 使用状态 · 0=禁用/1=启用/10=启用中（中间态）· `StructProjectDeleteOP.beginOperationTransaction` L50 仅 enable=10 行参与删除 |

### 3.4 出厂数据字段（autoComputed · 红色雷区 · 4 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `initdatasource` | ComboField | — | 数据来源 |
| `orinumber` | TextField | — | 出厂编码 |
| `oriname` | MuliLangTextField | — | 出厂名称 |
| `oristatus` | ComboField | — | 出厂数据编辑状态 |

### 3.5 矩阵组织业务字段（核心 15 字段 · ISV 主要扩展点）

| key | type | 必填 | refEntity | 物理列 | 业务语义 |
|---|---|---|---|---|---|
| `org` | OrgField | **是** | bos_org | `forgid` | **创建组织 BU** · `StructProjectEditPlugin.afterCreateNewData` L119-136 自动赋默认值 |
| `rootorg` | HRAdminOrgField | **是**（roottype=1）| haos_adminorghrf7 | `frootorgid` | **根组织** · 矩阵架构起点 · roottype=1 时必填 |
| `roottype` | ComboField | **是** | - | `froottype` | **根组织类型** · 1=实组织 / 2=虚拟根 · 切换值会改写下挂组织（R-03）|
| `isincludevirtualorg` | CheckBoxField | 否 | - | `fisincludevirtualorg` | 允许设置虚拟组织 · `StructProjectEditPlugin.afterBindData` L150 已有派生虚拟组织时 setEnable(false) |
| `iscustomorg` | CheckBoxField | 否 | - | `fiscustomorg` | 是否自定义组织 · `StructProjectListPlugin.setFilter` L100 默认过滤 `='0'` |
| `relyonstructproject` | BasedataField | 否 | **haos_structproject**（自引用）| `frelyonstructprojectid` | **依赖架构方案** · 方案级链式依赖（haos_structure 实例引本方案的字段也叫这个）|
| `rootnumber` | TextField | 否（roottype=2 必填）| - | — | 根组织编码 · 仅 roottype=2（虚拟根）填 · `propertyChanged` L256 |
| `rootname` | MuliLangTextField | 否（roottype=2 必填）| - | — | 根组织名称 · 仅 roottype=2 填 |
| `rooteffdt` | DateField | 否（roottype=2 必填）| - | — | 成立日期 · 仅 roottype=2 填 |
| `rootdescription` | MuliLangTextField | 否 | - | — | 根组织描述（roottype=2 时使用）|
| `otclassify` | BasedataField | 否 | haos_otclassify | `fotclassifyid` | **组织团队分类** · 虚拟根组织硬编码 1010L（StructProjectSaveOp L103）|
| `effdt` | DateField | **是** | - | `feffdt` | **生效日期** · 决定下挂组织迁移是否触发（R-03）|
| `orgorg` | OrgField | 否 | bos_org | — | 组织体系管理组织 · roottype=2 时显示 |
| `istoallareas` | CheckBoxField | 否 | - | `fistoallareas` | 是否应用全领域 · 业务上控制方案的可见范围 |
| `isprimary` | CheckBoxField | 否 | - | `fisprimary` | 是否主架构方案 · 业务标记 |
| `issyncorg` | CheckBoxField | 否 | - | `fissyncorg` | 维护行政组织时需同步维护该矩阵组织信息 |

> 注：scene_doc.json 列了 35 字段 · 表中前 6 + 8 + 2 + 4 + 15 = 35（其中 isprimary / issyncorg 等也归属业务字段组）。

## 4. refEntity 引用关系图

```
haos_structproject（本场景 · 母本）
  ├─ rootorg (HRAdminOrgField) ───→ haos_adminorghrf7 (行政组织 F7)
  │                                  │
  │                                  └─ 物理 boid 落到 frootorgid
  │
  ├─ otclassify (BasedataField) ───→ haos_otclassify (组织团队分类)
  │                                  │
  │                                  └─ 虚拟根组织硬编码 1010L
  │
  ├─ relyonstructproject (BasedataField) ───→ haos_structproject (自引用 · 方案级链式依赖)
  │                                            │
  │                                            └─ haos_structure 实例的同名字段也指向本表
  │
  ├─ org (OrgField) ───→ bos_org (基础资料 BU · 创建组织)
  │
  ├─ orgorg (OrgField) ───→ bos_org (组织体系管理组织 · roottype=2 显示)
  │
  ├─ creator/modifier (CreaterField/ModifierField) ───→ bos_user
  │
  └─ disabler (UserField) ───→ bos_user

反向引用（被谁引）：
  haos_structproject  ←─ haos_structure.relyonstructproject（实例引用本方案）
  haos_structproject  ←─ haos_structproject.relyonstructproject（方案链式依赖）
```

## 5. 物理表关键事实

| 项 | 值 |
|---|---|
| 主表名 | `t_haos_structproject`（两个 form 共用）|
| 多语言子表 | `t_haos_structproject_l`（含 `name` / `simplename` / `description` / `oriname` / `rootname` / `rootdescription` 6 个 MuliLangTextField）|
| 是否 HisModel | 否（BaseFormModel · 没 boid/iscurrentversion/hisversion · 不需要 PR-008 时序处理）|
| 是否走 BdVersion | 是（save 链含 `BdVersionSaveServicePlugin` RowKey=1 · 基础资料版本管理 · 主表 name + 版本子表 name 写历史）|
| 表内行数级别 | 业务方案级别 · 通常 < 200 行（含母本+haos_structure 实例）|

## 6. 字段可修改性矩阵（ISV 视角）

| 类别 | 数量 | 字段 | ISV 操作 |
|---|---|---|---|
| L0 自动计算 | 8 | creator/modifier/createtime/modifytime/masterid/disabler/disabledate/issyspreset | 严禁手改 · 平台维护 |
| L1 出厂数据 | 4 | initdatasource/orinumber/oriname/oristatus | 严禁手改 · 标品出厂规则用 |
| L1 标准字段 | 6 | number/name/simplename/description/index/status/enable | 业务可改（编码可走 CodeRule 自动生成 · PR-006）|
| L3 业务字段 | 15 | org/rootorg/roottype/isincludevirtualorg/iscustomorg/relyonstructproject/rootnumber/rootname/rooteffdt/rootdescription/otclassify/effdt/orgorg/istoallareas/isprimary/issyncorg | ISV 可扩展 + 加联动校验 |
| L3 系统约束 | 3 | org（必填 BU）/ rootorg（必填）/ otclassify（业务硬编码 1010L 不应手改）| 改要谨慎 |

## 7. 数据 / UI 双层结构图

```
┌─────────────────────────────────────────────────────────────┐
│ UI 层 · main_form.xml (haos_structproject)                  │
│  ┌─────────────────┐  ┌──────────────────────────────────┐  │
│  │ 左树 BUListPanel │  │ 主表 BasedataFormAp              │  │
│  │ StructProject-   │  │ FieldAp x N · roottype 切换      │  │
│  │ BUListPlugin     │  │ rootorg/rootnumber/rootname/     │  │
│  │ (org BU 控制)    │  │ rooteffdt/rootdescription 显隐  │  │
│  └─────────────────┘  └──────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                                │
                                │ formId=haos_structproject 落库
                                ▼
┌─────────────────────────────────────────────────────────────┐
│ 数据层 · t_haos_structproject                                │
│  · roottype=1 行 · 实组织根的方案                            │
│  · roottype=2 行 · 虚拟根的方案 · 派生 haos_adminorgdetail   │
│  · 同表也存放 haos_structure 实例数据（otclassify=1010L）    │
│  · t_haos_structproject_l 多语言子表（6 个 MuliLang 字段）   │
└─────────────────────────────────────────────────────────────┘
                                │
                                │ 业务派生 · 跨表写
                                ▼
┌─────────────────────────────────────────────────────────────┐
│ 派生 · 行政组织（roottype=2 时）                              │
│  · haos_adminorgdetail（虚拟根组织数据）                     │
│  · haos_orgteamcooprel（组织团队协作关系 · BatchAdminOrgNew  │
│    OpService.execute L93-94 写）                             │
│  · haos_adminorg_msgdetail（变更消息 · sch_task 派单）       │
└─────────────────────────────────────────────────────────────┘
                                │
                                │ 反向引用（业务消费）
                                ▼
┌─────────────────────────────────────────────────────────────┐
│ 反向引用 · haos_structure 实例                               │
│  · haos_structure.relyonstructproject = 本方案 id            │
│  · 业务流：用户在 haos_structure 选本方案 → 派生具体矩阵实例 │
└─────────────────────────────────────────────────────────────┘
```

## 8. 平台规则适用性

| PR | 是否适用 | 说明 |
|---|---|---|
| PR-001 ISV 并列挂不继承 | 强适用 | 标品全 final（StructProject* 7 个类）· 不能继承 |
| PR-002 RowKey 顺序 | 适用 | save 链：CodeRuleOp(无 RowKey) → BdVersionSaveServicePlugin(1) → HRBaseDataStatusOp(2) → HRBaseDataLogOp(3) → HRBaseDataEnableOp(4) → HRBaseOriginalOp(5) → StructProjectSaveOp(7) |
| PR-003 数据 API 分层 | 适用 | StructProjectEditPlugin 用 getModel().setValue()（L122 / 167 / 256 ...）· StructProjectSaveOp 用 entity.set()（L96-119）|
| PR-004 setValue 死循环防护 | 强适用 | propertyChanged L250-289 · ISV 加联动必须包 beginInit/endInit |
| PR-005 ID.genLongId | 适用 | StructProjectSaveOp L94 `ORM.create().genLongId("haos_adminorgdetail")` |
| PR-006 CodeRuleOp 业务侧配置 | 适用 | save 链含 CodeRuleOp · `AdminOrgCodeRuleServiceHelper.setOrgNumber` 实证 |
| PR-007 预置数据 number 不可改 | 强适用 | `STRUCT_PROJECT_MANAGE` 预置母本 + `issyspreset` 字段 |
| PR-008 iscurrentversion 时序过滤 | 弱适用 | 主表是 BaseFormModel（非 HisModel）· 但 rootorg 引 haos_adminorghrf7（HisModel · 跨表 join 时下游用）|
| PR-009 boid 业务维度 | 弱适用 | 本场景没 boid · 派生的 haos_adminorg 走 admin_org 域有 boid（StructProjectSaveOp L131 `oldRootOrgId = dbRootOrg.getLong("id")` 实证）|
| PR-010 OP 13 方法 | 强适用 | save / delete / disable / enable 全部走 OP 链 |
| PR-011 BEC | **关键** | 标品**没**走标准 BEC（`triggerEventSubscribe` 14 类 0 命中）· 走 sch_task + JobClient.dispatch |

## 9. 与同族场景对照

| 场景 | formNumber | 物理表 | ModelType | 共用模式 |
|---|---|---|---|---|
| **本场景** | haos_structproject | t_haos_structproject | BaseFormModel | 母本 / 用 roottype + issyspreset 区分 |
| haos_structure（配对实例）| haos_structure | t_haos_structproject | BaseFormModel | 实例 / `otclassify=1010L` 区分 / `relyonstructproject` 引本方案 |
| admin_org_quick_maintenance | haos_adminorg | t_haos_adminorg | HisModel | 时序 · 同应用不同物理表 |
| admin_org（detail）| haos_adminorgdetail | 数据视图 BaseForm | BaseFormModel | 列表 UI 壳 · admin_org 用 |
| hbpm_position_maintenance | hbpm_positionhr / hbpm_stposition | t_hbpm_position | BaseFormModel | 双 form 共用一表（用 isstandardpos 区分）|

> 想理解"双 form 共用一物理表"模式 · 推荐对照看 `hbpm_position_maintenance/03_model_design.md` 的 `isstandardpos` 区分键分析（与本场景的 `roottype + otclassify` 同模式）· 也对照看 [`../haos_structure_maintenance/03_model_design.md`](../haos_structure_maintenance/03_model_design.md)（配对场景 · 同表反向）。

## 10. 平台命名规则速查（避免脑补陷阱）

- **多语言表 `_l` 结尾**：`t_haos_structproject_l` 是真正的多语言表 · 苍穹平台所有 MuliLangTextField 字段都落到 `_l` 结尾的物理表
- **反模式 · 继承场景专属类**：`StructProjectEditPlugin` / `StructProjectListPlugin` / `StructProjectSaveOp` / `StructProjectDeleteOP` / `StructProjectDisableOp` / `StructProjectEnableOp` / `StructOrgPermSaveOp` / `StructProjectBUListPlugin` 全是 final · ISV 不要继承场景专属类（违反 PR-001）· 要扩展走"并列挂"或继承 SDK 白名单父类（HRDataBaseEdit / HRDataBaseList / HRDataBaseOp / AbstractValidator / AbstractBUListPlugin 是抽象 · 可继承）

## 11. 元数据来源追溯

- `_shared/_standard_metadata/entity_metadata/haos_structproject.md` — 平台元数据快照（OpenAPI 实时反推）
- `haos_structproject/main_form.xml` — 完整 FormMetadata XML（2155 行）
- `haos_structproject/rules.json` — 2 条 formRule
- `_auto_plugin_registry.md` — 24 plugin 注册清单（绑定 13 个不同生命周期方法）
- `_auto_plugin_semantics.md` — 14 反编译类语义
- 反编译实证：`_sdk_audit/_decompiled/scenarios/haos_structproject/` 共 14 个 .java 文件

## 12. 与配套场景 haos_structure 的字段重叠分析

| 字段 | haos_structproject | haos_structure | 共用情况 |
|---|---|---|---|
| number / name / simplename / description / index | ✓ | ✓ | 完全共用 · 物理列同 |
| status / enable | ✓ | ✓ | 完全共用 · 状态切换共享同一行（实际上不是同一行 · 而是同表不同行）|
| issyspreset / iscustomorg / istoallareas / isprimary | ✓ | ✓ | 完全共用 · 排预置 / 排自定义 / 全领域共享语义 |
| roottype | ✓ | ✓ | 共用 · 但语义不同（母本设计阶段 vs 实例运行阶段）|
| rootorg / rootnumber / rootname / rooteffdt / rootdescription | ✓ | ✓ | 完全共用 · 物理列同 |
| relyonstructproject | ✓（自引用 · 链式）| ✓（指向母本）| 同字段 · 不同业务语义 |
| isincludevirtualorg | ✓ | - | haos_structproject 独有（实例不需要再开虚拟开关）|
| isprimary / issyncorg | ✓ | - | haos_structproject 独有（方案级开关）|
| effdt / orgorg | ✓ | - | haos_structproject 独有（方案生效时间 + 体系管理组织）|
| otclassify | ✓ | ✓ | 完全共用 · 区分键（实例硬编码 1010L · 母本取业务分类）|

> **共用物理表的扩展隔离**（CS-06 详证）：ISV 给 haos_structproject 加字段时 · 物理列必然同时影响 haos_structure（同表）· 但**元数据层可以隔离**——ISV 扩展元数据只挂在 haos_structproject 上、不挂 haos_structure · 即可让 haos_structure 表单"看不到"该字段（虽然物理列共享）· 这跟 hbpm 的 isstandardpos 模式同源。
