# 数据模型设计 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 31 字段 scene_doc.json + main_form.xml + 7 个反编译类实证
> **confidence**：verified · 数据来自 OpenAPI 探针 + jar 反编译双向核对

## 1. 标识与定位

| 项 | 值 | 来源 |
|---|---|---|
| 业务场景中文名 | 矩阵组织维护（菜单中文："结构化组织维护"）| `scenario.json` menu_paths |
| formNumber | `haos_structure` | `scene_doc.json._meta.formId` |
| formId 技术码 | `3C5IN45RNAP4` | `main_form.xml:6 <Id>` |
| ModelType | **`BaseFormModel`** | `main_form.xml:8 <ModelType>` |
| 物理表 | **`t_haos_structproject`**（注意：不是 t_haos_structure！）| `scene_doc.json.metaTables` |
| 多语言表 | `t_haos_structproject_l` | 平台规则推断 |
| 应用 | HR基础组织（haos · `bizappId=W11R1282DJK`）| `main_form.xml:13 <BizappId>` |
| 云 | HR基础服务云（HRMP）| `_meta.cloud` |
| 域 | 组织发展 / 行政组织维护 | menu_paths |
| 父模板 | `2+QE4JA9QV27`（HR 基础资料模板）| `main_form.xml:11 <ParentId>` |
| 继承路径 | `1942c188000065ac → 2+QE4JA9QV27` | `main_form.xml:18 <InheritPath>` |
| Isv | `kingdee` · 标品 | `main_form.xml:5` |

## 2. 关键架构 · 双 form 共用一张物理表

**结构化方案系列共用 `t_haos_structproject`** —— 这是本场景最核心的架构 fact，类比 hbpm_position 域 `hbpm_positionhr`/`hbpm_stposition` 共用 `t_hbpm_position`。

| formNumber | 中文 | 角色 | 物理表 | otclassify 取值（业务区分键）|
|---|---|---|---|---|
| `haos_structproject` | 结构化组织方案 | 母本 / 元数据级方案 | `t_haos_structproject` | ≠ 1010 · 通用方案 |
| `haos_structure`（本场景）| 矩阵组织维护 | 矩阵组织实例 | `t_haos_structproject`（同表！）| **`= 1010L`** · 来自源码 `StructProjectRepository.java:92`：`new QFilter("otclassify", "=", (Object)1010L)` |

> **业务区分字段**：`otclassify`（组织团队分类 · BigIntField · `physicalColumn=fotclassifyid`）
> · `haos_structure` 数据 `otclassify = 1010L`（矩阵组织专用编码）
> · `haos_structproject` 数据 `otclassify` 取业务方案分类
> · 实证来源：源码 `StructProjectRepository.java:92, :110`（`createUserStructProjectFilter` / `createUserStructProjectFilterNewChart`）

> **数据依赖字段**：`relyonstructproject`（依赖架构方案 · BasedataField → `haos_structproject`）
> · 矩阵组织实例选择一个 structproject 母本作为方案蓝图
> · 实证来源：`scene_doc.json fields[25].refEntity = haos_structproject`

> **预置数据 ID**：`StructProjectConstants.STRUCT_PROJECT_MANAGE`（实际值在 jar 常量类中 · 业务上是 1010L 系列预置母本）
> · 源码 `StructureListPlugin.java:99`：`noPreSetFilter.or("id", "=", (Object)StructProjectConstants.STRUCT_PROJECT_MANAGE)`
> · 含义：列表 setFilter 默认过滤 `issyspreset='0'`（非预置）· 但保留 `STRUCT_PROJECT_MANAGE` 这一条预置母本可见
> · 1010L 这条预置数据在源码 `StructProjectRepository.java:71-76` 被强制提到列表首位

## 3. 31 字段按业务语义分组

> 数据源：`scene_doc.json.fields`（31 个字段 · 单实体 · 全部 `physicalTable=(main)`）
> 注：本表无 EntryEntity（"分录"在 _auto_operations.md 的 `newentry/deleteentry` opKey 是基础资料模板兜底操作 · 实际 main_form.xml 没有真正的子分录）

### 3.1 主键 / 编码 / 名称（基础资料标准 6 字段）

| key | type | 必填 | ISV 可改 | 物理列 | 说明 |
|---|---|---|---|---|---|
| `id` | LongField（PK）| - | 否 | `fid` | 平台主键 · `kd.bos.id.ID.genLongId()` 生成（PR-005）|
| `number` | TextField | 否 | 是 | `fnumber` | 编码 · 可走 CodeRule 自动生成（CodeRuleOp 在 save 链 RowKey=- 位置）|
| `name` | MuliLangTextField | 否 | 是 | `fname` | 名称 · 多语言 · 在 `t_haos_structproject_l` 落库 |
| `simplename` | MuliLangTextField | 否 | 是 | `fsimplename` | 简称 |
| `description` | MuliLangTextField | 否 | 是 | `fdescription` | 描述 |
| `index` | IntegerField | 否 | 是 | `findex` | 排序号 |

### 3.2 系统字段（autoComputed · ISV 不可改 · 红色雷区 · 8 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `creator` | CreaterField | `fcreatorid` | 创建人 → bos_user · `RequestContext.get().getCurrUserId()` |
| `modifier` | ModifierField | `fmodifierid` | 修改人 → bos_user |
| `createtime` | CreateDateField | `fcreatetime` | 创建时间 |
| `modifytime` | ModifyDateField | `fmodifytime` | 修改时间 |
| `masterid` | MasterIdField | `fmasterid` | 主数据内码（基础资料标准）|
| `disabler` | UserField | `FDisablerID` | 禁用人 · 由 disable opKey（HRBaseDataLogOp）回填 |
| `disabledate` | DateTimeField | `FDisableDate` | 禁用时间 |
| `org` | OrgField → bos_org | `forgid` | **创建组织**（基础资料 BU）· **必填** · 默认走 `OrgPermHelper.getHRPermOrg().getHasPermOrgs()` 首选 |

### 3.3 状态字段（黄色雷区 · 改了级联下游 · 4 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `status` | BillStatusField | `fstatus` | 数据状态 · A/B/C → audit/submit/unaudit 流转 · `parameter.StatusFieldId=AkId5S4yTs`（save/audit）|
| `enable` | BillStatusField | `fenable` | 使用状态 · 0=禁用/1=启用/10=正在启用 · `parameter.StatusFieldId=ac5Y5Dax1q`（disable/enable）· 源码 `StructProjectRepository.java:53`：`new QFilter("enable", "=", (Object)"10")` 用于 `queryEnablingByIds` |
| `issyspreset` | CheckBoxField | `fissyspreset` | 系统预置 · 源码 `StructureListPlugin.java:98` 默认过滤 `issyspreset='0'`（非预置）|
| `initdatasource` | ComboField | `finitdatasource` | 数据来源 |

### 3.4 出厂数据字段（autoComputed · 红色雷区 · 3 字段）

| key | type | 物理列 | 说明 |
|---|---|---|---|
| `orinumber` | TextField | `forinumber` | 出厂编码 |
| `oriname` | MuliLangTextField | `foriname` | 出厂名称 |
| `oristatus` | ComboField | `foristatus` | 出厂数据编辑状态 |

### 3.5 矩阵组织业务字段（核心 11 字段 · ISV 主要扩展点）

| key | type | 必填 | refEntity / 备注 | 物理列 | 业务语义 |
|---|---|---|---|---|---|
| `rootorg` | HRAdminOrgField | **是** | → `haos_adminorghrf7` | `frootorgid` | **根组织** · 矩阵架构的起点 · 没填则源码 `StructureListPlugin.java:148` 抛"无根组织"错（ResId=`StructureListPlugin_1`）|
| `roottype` | ComboField | 否 | - | `froottype` | 根组织类型 · 用于过滤可选 rootorg |
| `otclassify` | BigIntField | 否 | 业务硬编码 | `fotclassifyid` | **组织团队分类** · 矩阵组织实例 = 1010L（区分母本/实例的关键）|
| `relyonstructproject` | BasedataField | 否 | → `haos_structproject` | `frelyonstructprojectid` | **依赖架构方案** · 矩阵组织挂靠的母本 structproject id |
| `isincludevirtualorg` | CheckBoxField | 否 | - | `fisincludevirtualorg` | 是否包含虚拟组织 · 决定查 adminorg 时是否带虚拟组织 |
| `iscustomorg` | CheckBoxField | 否 | - | `fiscustomorg` | 是否自定义组织 · 区分系统派生 vs 用户自建矩阵 |
| `istoallareas` | CheckBoxField | 否 | - | `fistoallareas` | 是否应用全领域 · 源码 `StructProjectRepository.java:97` 用此字段过滤："是" 才允许跨领域可见 |
| `rootnumber` | TextField | 否 | - | `frootnumber` | 根组织编码（冗余 · 来自 rootorg.number）|
| `rootname` | MuliLangTextField | 否 | - | `frootname` | 根组织名称（冗余）|
| `rooteffdt` | DateField | 否 | - | `frooteffdt` | 根组织生效日期 |
| `rootdescription` | MuliLangTextField | 否 | - | `frootdescription` | 根组织描述（冗余）|

> **冗余字段说明**：`rootnumber/rootname/rooteffdt/rootdescription` 是 `rootorg`（HRAdminOrgField）的副本字段。新增矩阵组织时，按 `rootorg` 反查 `haos_adminorg` 把 number/name/bsed/description 拷贝过来。这是反规范化做法 · 让列表渲染不用每次 join。
> ISV 扩展时不要单独修改这 4 个冗余字段 · 应改 rootorg 触发联动（如果业务方真要改 · 走 `propertyChanged` 联动而不是直接 set）。

## 4. refEntity 引用关系图

```
haos_structure (本场景实例 · otclassify=1010)
  ├─ rootorg (HRAdminOrgField) ───→ haos_adminorghrf7 (行政组织 F7)
  │                                   │
  │                                   └─ 物理 boid 落库到 frootorgid
  │
  ├─ relyonstructproject (BasedataField) ───→ haos_structproject (结构化方案母本)
  │                                            │
  │                                            └─ 共用同一物理表 t_haos_structproject
  │                                            └─ 区分键 otclassify ≠ 1010
  │
  ├─ org (OrgField) ───→ bos_org (基础资料 BU · 创建组织)
  │
  ├─ creator/modifier (CreaterField/ModifierField) ───→ bos_user
  │
  └─ disabler (UserField) ───→ bos_user
```

## 5. 物理表关键事实

| 项 | 值 |
|---|---|
| 主表名 | `t_haos_structproject`（两个 form 共用）|
| 多语言子表 | `t_haos_structproject_l`（含 `name` / `simplename` / `description` / `oriname` / `rootname` / `rootdescription` 6 个 MuliLangTextField）|
| 是否 HisModel | 否（是 BaseFormModel · 没 boid/iscurrentversion/hisversion · 不需要 PR-008/PR-009 时序处理）|
| 是否走 BdVersion | 是（save 链 RowKey=1 是 `BdVersionSaveServicePlugin` · 基础资料版本管理 · 主表 name + 版本子表 name 写历史）|
| 表内行数级别 | 业务方案级别 · 通常 < 100 条 · 远小于 hrpi_* 员工域百万级表 |

## 6. 字段可修改性矩阵（ISV 视角）

| 类别 | 数量 | 字段 | ISV 操作 |
|---|---|---|---|
| L0 自动计算 | 8 | creator/modifier/createtime/modifytime/masterid/disabler/disabledate/issyspreset | 严禁手改 · 平台维护 |
| L1 出厂数据 | 3 | orinumber/oriname/oristatus/initdatasource | 严禁手改 · 标品出厂规则用 |
| L1 标准字段 | 6 | number/name/simplename/description/index/status/enable | 业务可改（编码可走 CodeRule 自动生成 · PR-006）|
| L3 业务字段 | 11 | rootorg/roottype/otclassify/relyonstructproject/isincludevirtualorg/iscustomorg/istoallareas/rootnumber/rootname/rooteffdt/rootdescription | ISV 可扩展 + 加联动校验 |
| L3 系统约束 | 3 | org（必填 BU）/ rootorg（必填）/ otclassify（业务硬编码 1010L 不应手改）| 改要谨慎 |

> 数据来源：`scene_doc.json fields[].layer + isvCanModify`（扫码统计）

## 7. 数据 / UI 双层结构图

```
┌─────────────────────────────────────────────────────────────┐
│ UI 层 · main_form.xml (haos_structure)                      │
│  ┌─────────────────┐  ┌──────────────────────────────────┐  │
│  │ 左树 BUListPanel │  │ 右表 BasedataFormAp              │  │
│  │ StructProjectBU- │  │ FieldAp x N · 含 rootorg /       │  │
│  │ ListPlugin       │  │ relyonstructproject /            │  │
│  │ (org 字段控制 BU)│  │ otclassify / iscustomorg ...     │  │
│  └─────────────────┘  └──────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                                │
                                │ formId=haos_structure 落库
                                ▼
┌─────────────────────────────────────────────────────────────┐
│ 数据层 · t_haos_structproject                                │
│  · otclassify=1010L 行 = haos_structure 实例                 │
│  · otclassify≠1010 行 = haos_structproject 母本              │
│  · t_haos_structproject_l 多语言子表（6 个 MuliLang 字段）   │
└─────────────────────────────────────────────────────────────┘
                                │
                                │ 业务下游引用
                                ▼
┌─────────────────────────────────────────────────────────────┐
│ 下游 · 组织视图计算结果（标品 OPM 异步任务）                 │
│  · haos_othorgstruct（其它组织视图 / 矩阵组织视图）          │
│  · haos_adminorgstruct（行政组织树 · 在 OrgPermHelper 中查询）│
└─────────────────────────────────────────────────────────────┘
```

## 8. 平台规则适用性

| PR | 是否适用 | 说明 |
|---|---|---|
| PR-001 ISV 并列挂不继承 | 强适用 | 标品 `StructureListPlugin / StructureEditPlugin / StructProjectBUListPlugin` 全 final · 不能继承 |
| PR-002 RowKey 顺序 | 适用 | 已观测：CodeRuleOp(无 RowKey) → BdVersionSaveServicePlugin(1) → HRBaseDataStatusOp(2) → ... |
| PR-003 FormPlugin/OP 数据 API 分层 | 适用 | 标品代码：StructureListPlugin 用 `getView()` · OP 用 `args.getDataEntities()` |
| PR-004 setValue 死循环防护 | 弱相关 | 本场景标品没明显字段联动 · ISV 扩展若加联动需注意 |
| PR-005 ID.genLongId | 适用 | 自定义编码生成时用 |
| PR-006 CodeRuleOp 业务侧配置 | 适用 | save 链含 CodeRuleOp · 业务侧"编码规则基础资料"配 |
| PR-007 预置数据 number 不可改 | 强适用 | `STRUCT_PROJECT_MANAGE` 预置数据 + `issyspreset` 字段 · 不要拦业务行 |
| PR-008 iscurrentversion 时序过滤 | 不适用 | BaseFormModel · 非 HisModel |
| PR-009 boid 业务维度 | 弱适用 | 本场景没 boid · 但 `rootorg` 引用的 `haos_adminorghrf7` 是 HisModel · 跨表 join 时下游要用 boid |
| PR-010 OP 13 方法 | 强适用 | save/delete/audit/disable/enable 全部走 OP 链 |
| PR-011 BEC | 弱适用 | 标品没观测到主动发 BEC 事件 · 看 `StructProjectRepository.java` 没有 `EventServiceHelper.triggerEventSubscribeJobs` 调用 · 因此本场景不走 BEC（不要套 hjm 模式 · 见 docs/NEW_SCENARIO_PIPELINE.md 坑 24）|

## 9. 与同族场景对照

| 场景 | formNumber | 物理表 | ModelType | 共用模式 |
|---|---|---|---|---|
| 本场景 | haos_structure | t_haos_structproject | BaseFormModel | 与 haos_structproject 共用同表（otclassify 区分）|
| haos_structproject | haos_structproject | t_haos_structproject | BaseFormModel | 母本 / otclassify ≠ 1010 |
| admin_org_quick_maintenance | haos_adminorg | t_haos_adminorg | HisModel | 时序 · 同应用不同物理表 |
| admin_org（detail）| haos_adminorgdetail | 数据视图 BaseForm | BaseFormModel | 列表 UI 壳 · admin_org 列表用 |
| hbpm_position_maintenance | hbpm_positionhr / hbpm_stposition | t_hbpm_position | BaseFormModel | 与本场景同源：双 form 共用一表（用 isstandardpos 区分）|

> 想理解"双 form 共用一物理表"模式 · 推荐对照看 `hbpm_position_maintenance/03_model_design.md` · 那里有 `isstandardpos` 区分键的详细分析（与本场景的 `otclassify=1010L` 同模式）。

## 9. 平台命名规则速查（避免脑补陷阱）

- **多语言表 `_l` 结尾**：`t_haos_structproject_l` 是真正的多语言表 · 苍穹平台所有 MuliLangTextField 字段都落到 `_l` 结尾的物理表。**注意区分**：`_i` 结尾才是基础资料拆分表（垂直分库）· 本场景没有 `_i` 拆分表。
- **反模式 · 继承场景专属类**：`StructureEditPlugin` / `StructProjectBUListPlugin` 都是 final 薄壳 · ISV 不要继承场景专属类（违反 PR-001 · 跟 hbpm 的 `PositionHisSaveOp` / hjm 的 `JobHrSaveOp` 同性质）。要扩展走"并列挂"或继承 SDK 白名单父类（`HRDataBaseEdit` / `AbstractBUListPlugin` 是 abstract · 可继承）。

## 10. 元数据来源追溯

- `_shared/_standard_metadata/entity_metadata/haos_structure.md` — 平台元数据快照
- `haos_structure/main_form.xml` — 完整 FormMetadata XML（2477 行）
- `haos_structure/rules.json` + `_auto_operations.md` — 30 opKey 注册详情
- `_auto_plugin_registry.md` — 19 plugin 注册清单（绑定 6 个不同生命周期方法）
- 反编译实证：`_sdk_audit/_decompiled/scenarios/haos_structure/` 共 7 个 .java 文件
