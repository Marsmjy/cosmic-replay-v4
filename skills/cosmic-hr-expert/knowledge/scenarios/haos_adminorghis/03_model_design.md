# 数据模型设计 · 组织历史查询（haos_adminorghis）

> **状态**：基于 85 字段 scene_doc.json + main_form.xml + 7 反编译类实证
> **confidence**：verified · 共用物理表 t_haos_adminorg
> **审计时间**：2026-04-27

---

## 1. 标识与定位

| 项 | 值 | 来源 |
|---|---|---|
| 业务场景中文名 | 组织历史查询（菜单："行政组织维护 → 行政组织维护 → 组织历史查询"）| `scenario.json menu_paths` |
| formNumber | `haos_adminorghis` | `scene_doc.json._meta.formId` |
| formId 技术码 | `4U7/YJXB29D9` | `scene_doc.json._meta.formIdTechnical` |
| ModelType | **`BaseFormModel`**（基础资料 · 但只读历史模式）| `_shared/_standard_metadata/entity_metadata/haos_adminorghis.md` |
| 物理表 | **`t_haos_adminorg`**（**与 admin_org / haos_adminorgdetail 共用 · 共表不共视图**）| `scene_doc.json basicInfo.physicalTable` |
| 多语言表 | `t_haos_adminorg_l` | 同上 |
| 应用 | HR基础组织（haos · `bizappId=W11R1282DJK`）| `_shared/_standard_metadata/entity_metadata/haos_adminorghis.md` |
| 云 | HR基础服务云（HRMP）| 同上 |
| 域 | 组织发展 / 行政组织维护 | menu_paths |
| 标品 ISV | `kingdee` · 标品 | main_form.xml |

---

## 2. ⭐ 核心架构：物理表共用 · 视图分流（最易踩坑）

本场景与 `admin_org_quick_maintenance` 共用 `t_haos_adminorg` 物理表 · 走的是**视图分流模式**——同一张表上挂三个 BaseFormModel 视图 · 通过 `iscurrentversion` 和外部 customParam 区分用途。

| formNumber | 中文 | 角色 | 物理表 | 关键时序过滤 | 谁用 |
|---|---|---|---|---|---|
| `haos_adminorg` | 组织基本信息（主）| 父模板 / 元数据 L3 | `t_haos_adminorg` | （无） | 标品继承基类 |
| `haos_adminorgdetail` | 组织快速维护 | 当前版本视图 | `t_haos_adminorg`（同表）| `iscurrentversion=true`（标品 ListPlugin 注入）| `admin_org_quick_maintenance` 场景 |
| `haos_adminorghis`（**本场景**）| 组织历史查询 | 历史版本视图 | `t_haos_adminorg`（**同表！**）| **`iscurrentversion=false`**（HisModelListCommonPlugin.setFilter L177-L178）| `haos_adminorghis` 场景 |

> **共表不共视图的本质**（与 hbpm / haos_structure 同性质）：
> - hbpm 域：`hbpm_positionhr` + `hbpm_stposition` 共用 `t_hbpm_position` · 区分键 `isstandardpos`
> - haos 域：`haos_structure` + `haos_structproject` 共用 `t_haos_structproject` · 区分键 `otclassify`
> - **本场景**：`haos_adminorghis` + `haos_adminorgdetail` 共用 `t_haos_adminorg` · **区分键不是新加字段** · 而是**已有字段 `iscurrentversion`** + **标品 final 方法写死过滤**

> **关键认知**：本场景**没有自己独立的物理表 t_haos_adminorghis** · 任何"在 haos_adminorghis 上扩展字段"实际是改 `t_haos_adminorg` 物理表 · 跟 admin_org_quick_maintenance 双场景共担。

---

## 3. 继承链（5 级 · OpenAPI 实抓）

```
L0 1942c188000065ac (bos_basetpl · 基础资料模板)
  └── L1 2+QE4JA9QV27 (hbp_bd_tpl_all · HR基础资料全页面模板)
      └── L2 2/TYOWOSM0TE (hbp_histimeseqtpl · HR基础资料全页面历史模板) ⭐ 时序核心
          └── L3 4S6B8I6U+EBN (haos_adminorg · 组织基本信息-主)
              ├── L4 21=MGSD53K0/ (haos_adminorgdetail · 组织快速维护)
              └── L4 4U7/YJXB29D9 (haos_adminorghis · 组织历史查询) ← 本场景
```

InheritPath 实抓：`1942c188000065ac,2+QE4JA9QV27,2/TYOWOSM0TE,4S6B8I6U+EBN,21=MGSD53K0/`（**注意**：本场景 main_form.xml 显示 InheritPath 末位是 `21=MGSD53K0/` = haos_adminorgdetail · 说明 haos_adminorghis 父模板**也是**走 detail 视图链路 · 这是 BaseFormModel 多视图共用的典型架构）

### 3.1 字段层级分布（85 字段总数）

| 层级 | 字段类型 | 数量估计 | ISV 能改吗 |
|---|---|---|---|
| **L0 bos_basetpl** | 基础资料通用字段 | ~9 | 🔒 不改 |
| **L1 hbp_bd_tpl_all** | HR 基础资料通用 | ~19 | 🔒 不改 |
| **L2 hbp_histimeseqtpl** ⭐ | **HR 时序核心** | **~28** | 🔒 不改（破坏全 HR 时序）|
| **L3 haos_adminorg 自身** | 行政组织业务字段 | ~29 | ⚠ 谨慎改（共用 `t_haos_adminorg`）|

**核心认知**：本场景看到的 85 字段中 · ~28 个来自 `hbp_histimeseqtpl` · 改不动；~29 个真正属于 haos_adminorg · 但改了会双场景影响。

---

## 4. 85 字段按时序语义分组

> 数据源：`scene_doc.json.fields`（85 个字段）+ `_shared/_standard_metadata/entity_metadata/haos_adminorghis.md`

### 4.1 时序核心 28 字段（来自 hbp_histimeseqtpl · 🔒 不能改）

| 字段 key | 类型 | 必填 | 物理列 | 时序语义 |
|---|---|---|---|---|
| `boid` | BigIntField | - | `t_haos_adminorg.fboid` | **业务对象 ID** · 跨版本一致（**PR-009**）|
| `iscurrentversion` | CheckBoxField | - | `t_haos_adminorg.fiscurrentversion` | **当前版本标记** · true=当前 · false=历史（**PR-008**）|
| `hisversion` | TextField | - | `t_haos_adminorg.fhisversion` | 版本号 · 递增 |
| `sourcevid` | BigIntField | - | `t_haos_adminorg.fsourcevid` | 链向上一版本 ID（链表）|
| `firstbsed` | DateField | - | `t_haos_adminorg.ffirstbsed` | 该 boid 最早生效日期 |
| `bsed` | DateField | - | `t_haos_adminorg.fbsed` | 当前版本生效日期（区间起）|
| `bsled` | DateField | - | `t_haos_adminorg.fbsled` | 当前版本失效日期（区间止）|
| `datastatus` | ComboField | - | `t_haos_adminorg.fdatastatus` | 数据版本状态（TEMP / 待生效 / 生效中 / 过期 / 废弃）|
| `changedescription` | TextField | - | `t_haos_adminorg.fchangedescription` | 变动说明 |
| `creator` | CreaterField | - | `t_haos_adminorg.fcreatorid` | 创建人 → bos_user（系统）|
| `modifier` | ModifierField | - | `t_haos_adminorg.fmodifierid` | 修改人 → bos_user（系统）|
| `createtime` | CreateDateField | - | `t_haos_adminorg.fcreatetime` | 创建时间 |
| `modifytime` | ModifyDateField | - | `t_haos_adminorg.fmodifytime` | 修改时间（每次 revise 更新）|
| `masterid` | MasterIdField | - | `t_haos_adminorg.fmasterid` | 主数据内码 |
| `disabler` | UserField | - | `t_haos_adminorg.fdisablerid` | 禁用人 |
| `disabledate` | DateTimeField | - | `t_haos_adminorg.fdisabledate` | 停用日期 |
| `tobedisableflag` | CheckBoxField | - | `t_haos_adminorg.ftobedisableflag` | 待停用 |
| `tobedisabledate` | DateField | - | `t_haos_adminorg.ftobedisabledate` | 待停用日期 |
| `enable` | BillStatusField | ✓ | `t_haos_adminorg.fenable` | 业务状态 0/1/10 |
| `status` | BillStatusField | - | `t_haos_adminorg.fstatus` | 数据状态 A/B/C |
| `issyspreset` | CheckBoxField | - | `t_haos_adminorg.fissyspreset` | 系统预置 |
| `initdatasource` | ComboField | - | `t_haos_adminorg.finitdatasource` | 数据来源 |
| `orinumber` | TextField | - | -（出厂数据 · 拆分表）| 出厂编码 |
| `oriname` | MuliLangTextField | - | -（拆分表）| 出厂名称 |
| `oristatus` | ComboField | - | -（拆分表）| 出厂数据编辑状态 |
| `index` | IntegerField | - | `t_haos_adminorg.findex` | 排序号 |
| `simplename` | MuliLangTextField | - | `t_haos_adminorg_l.fsimplename` | 简称（多语言）|
| `description` | MuliLangTextField | - | `t_haos_adminorg_l.fdescription` | 描述（多语言）|

### 4.2 行政组织业务字段 ~29 字段（L3 自身）

| 字段 key | 类型 | 必填 | refEntity / 备注 | 物理列 | 业务语义 |
|---|---|---|---|---|---|
| `number` | TextField | ✓ | - | `t_haos_adminorg.fnumber` | 行政组织编码 |
| `name` | MuliLangTextField | ✓ | - | `t_haos_adminorg_l.fname` | 行政组织名称 |
| `parentorg` | HRAdminOrgField | - | → haos_adminorghrf7 | -（拆分表）| 上级行政组织 · boid 维度 |
| `adminorgtype` | BasedataField | ✓ | → haos_adminorgtype | `fadminorgtypeid` | 行政组织类型 |
| `adminorglayer` | BasedataField | - | → haos_adminorglayer | `fadminorglayerid` | 管理层级 |
| `adminorgfunction` | BasedataField | - | → haos_adminorgfunction | `fadminorgfunctionid` | 行政组织职能 |
| `corporateorg` | BasedataField | - | → hbss_lawentity | `fcorporateorgid` | 法律实体 |
| `belongdept` | HRAdminOrgField | - | → haos_adminorghrf7 | -（拆分表）| 所属部门 · boid |
| `belongcompany` | HRAdminOrgField | - | → haos_adminorghrf7 | -（拆分表）| 所属公司 · boid |
| `companyarea` | BasedataField | - | → bd_country | `fcompanyareaid` | 国家/地区 |
| `city` | BasedataField | - | → bd_admindivision | `fcityid` | 所在城市 |
| `workplace` | BasedataField | - | → hbss_workplace | `fworkplaceid` | 工作地 |
| `industrytype` | BasedataField | - | → hbss_industrytype | `findustrytypeid` | 行业类别 |
| `establishmentdate` | DateField | - | - | `t_haos_adminorg.festablishmentdate` | 成立日期 |
| `detailaddress` | MuliLangTextField | - | -（拆分表）| 详细地址 |
| `structnumber` | TextField | - | - | `fstructnumber` | 组织结构编码 |
| `positioning` | MuliLangTextField | - | `t_haos_adminorg_l.fpositioning` | 定位 |
| `mainduty` | MuliLangTextField | - | `t_haos_adminorg_l.fmainduty` | 主要职责 |
| `org` | OrgField | ✓ | → bos_org | `forgid` | 创建组织（基础资料 BU）|
| `isvirtualorg` | CheckBoxField | - | - | `fisvirtualorg` | 是否虚拟组织 |
| `isroot` | CheckBoxField | - | - | `fisroot` | 是否根组织 |
| `isleaf` | CheckBoxField | - | - | `fisleaf` | 是否叶子节点 |
| `level` | IntegerField | - | - | `flevel` | 层级 |
| `longnumber` | TextField | - | - | `flongnumber` | 长编码（含层级路径）|
| `fullname` | MuliLangTextField | - | - | `t_haos_adminorg_l.ffullname` | 全名（拼接 longnumber 来）|
| `entryboid_coop` | BigIntField | - | - | - | 协作信息分录 boid |
| `entryboid_struct` | BigIntField | - | - | - | 矩阵组织分录 boid |
| `cooprelentryentity` | EntryEntity | - | - | （子表 t_haos_adminorg_coop）| 协作组织分录 |
| `structentryentity` | EntryEntity | - | - | （子表 t_haos_adminorg_struct）| 矩阵组织分录 |

### 4.3 雷区字段（不要轻易改）

🔴 **红色（系统/平台维护 · 手改破坏一致性）**：creator / modifier / boid / iscurrentversion / hisversion / sourcevid

🟡 **黄色（变更级联下游）**：status / enable / disabler / disabledate / firstbsed / bsed / bsled / datastatus

✅ **业务自由扩展区**：number 之外的业务字段都可以走 ISV 扩展（**但要扩展到 haos_adminorg 主层 · 不是 haos_adminorghis 视图**）

---

## 5. ⚠ ISV 扩展的关键认知（共用物理表的影响）

### 5.1 扩展字段必须挂在 haos_adminorg 主层 · 不是 haos_adminorghis

```
✅ 正确：modifyMeta(formId="haos_adminorg", op="add field", key="${ISV_FLAG}_xxx")
   → 物理表 t_haos_adminorg 增列 ftdkw_xxx
   → admin_org_quick_maintenance 自动可见
   → haos_adminorghis 自动可见（同表）

❌ 错误：modifyMeta(formId="haos_adminorghis", op="add field")
   → 视图层独立扩展 · 不会写入物理表 · 浪费且会引起继承混乱
```

### 5.2 修改字段属性会双场景同时受影响

修改 `t_haos_adminorg.fadminorgtypeid` 列长度 / 类型 / 必填 → 立即同时影响：
- admin_org_quick_maintenance 当前版本视图（保存/审核流）
- haos_adminorghis 历史版本视图（查询/补录）

部署前必须**双场景回归** · 别只测一个场景。

### 5.3 时序字段改不得（PR-008 + PR-009）

`boid` / `iscurrentversion` / `hisversion` / `sourcevid` / `firstbsed` / `bsed` / `bsled` 是 hbp_histimeseqtpl 模板字段 · 改了会破坏**整个 HR 时序体系**（hbpm / hbjm / hrpi / haos 全用这套）。绝对不要碰。

---

## 6. 实体引用关系图

```
haos_adminorghis (本场景视图 · iscurrentversion=false)
  ├─ parentorg / belongdept / belongcompany (HRAdminOrgField · 全部 → haos_adminorghrf7)
  │                        │
  │                        └─ 字段值是组织 boid（PR-009）· 不是 id
  │
  ├─ adminorgtype (BasedataField → haos_adminorgtype)
  ├─ adminorglayer (BasedataField → haos_adminorglayer)
  ├─ adminorgfunction (BasedataField → haos_adminorgfunction)
  ├─ corporateorg (BasedataField → hbss_lawentity)
  ├─ companyarea (BasedataField → bd_country)
  ├─ city (BasedataField → bd_admindivision)
  ├─ workplace (BasedataField → hbss_workplace)
  ├─ industrytype (BasedataField → hbss_industrytype)
  ├─ creator / modifier / disabler (UserField → bos_user)
  ├─ org (OrgField → bos_org · 创建 BU)
  │
  ├─ cooprelentryentity (EntryEntity)
  │     ├─ coopreltype (BasedataField → haos_orgteamtype)
  │     ├─ cooporgteam (BasedataField → 协作组织 BU)
  │     └─ entryboid_coop (BigIntField · 子表行 boid)
  │
  └─ structentryentity (EntryEntity)
        └─ entryboid_struct (BigIntField · 子表行 boid)
```

---

## 7. 多语言表（`t_haos_adminorg_l`）

| 多语言列 | 字段 key | 备注 |
|---|---|---|
| `fname` | name | 行政组织名称 |
| `fsimplename` | simplename | 简称 |
| `fdescription` | description | 描述 |
| `ffullname` | fullname | 全名（业务计算 · 由 OrgFullNameServiceWrapper 拼）|
| `fpositioning` | positioning | 定位 |
| `fmainduty` | mainduty | 主要职责 |

> **多语言表规范**：`_l` 后缀是横向多语言扩展表（每条主行对应多种语言行）· **不要写成 `_i`**（垂直拆分表）。

> **拆分表（垂直分表 · 性能优化）**：`detailaddress` / `parentorg` / `belongdept` / `belongcompany` 等字段在 entity_metadata.md 显示物理列为 `—` · 实际落在拆分子表（如 `t_haos_adminorg_split`）。ISV 扩展字段时不要选这种拆分模式 · 让平台默认落主表即可。

---

## 8. 关键 EntryEntity（子分录）

### 8.1 cooprelentryentity（协作组织分录）

| 字段 | 类型 | 含义 |
|---|---|---|
| `coopreltype` | BasedataField → haos_orgteamtype | 协作关系类型 |
| `cooporgteam` | BasedataField | 协作组织（BU）|
| `entryboid_coop` | BigIntField | 子表行 boid（与主表 boid 协同管理）|

### 8.2 structentryentity（矩阵组织分录）

| 字段 | 类型 | 含义 |
|---|---|---|
| `entryboid_struct` | BigIntField | 矩阵组织分录 boid |
| `structproject` | BasedataField → haos_structproject | 关联结构化方案 |

> **历史查询场景的子分录关系**：每条历史版本的 cooprelentryentity / structentryentity 都是**版本快照** · revise 派生新版本时分录会被复制（`HisModelCopyProcessor.copyData()` · 见 `HisModelFormCommonPlugin.afterCreateNewData L110-L114`）

---

## 9. ISV 扩展字段命名建议（落 haos_adminorg 主层）

| 维度 | 建议 |
|---|---|
| 扩展对象 formId | **`haos_adminorg`**（主层 · 不是 haos_adminorghis 视图）|
| 字段 key | `<isv>_<name>` · 如 `${ISV_FLAG}_costcenter` |
| fieldType | TextField / BasedataField / DateField · **避免 HRAdminOrgField**（74 值枚举不支持 · 用 BasedataField + refEntity=haos_adminorghrf7）|
| fieldName 物理列 | 不传 · 让平台按 `f + key.lower()` 默认 → `ftdkw_costcenter` |
| 列长度 | ≤ 25 字符（苍穹平台开发规范）|
| 多语言 | 走 MuliLangTextField · 平台自动落 _l 表 |

---

## 10. 关联文档

- `02_business_rules.md` · 28 个时序字段语义规则
- `06_customization_solutions.md` · CS-01 扩展字段（挂 haos_adminorg 主层 · 双场景共用）
- `knowledge/scenarios/admin_org_quick_maintenance/03_model_design.md` · 当前版本视图设计（必对照）
- `knowledge/scenarios/hbpm_position_maintenance/03_model_design.md` · 同性质共表场景（isstandardpos 区分键）参考
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009
