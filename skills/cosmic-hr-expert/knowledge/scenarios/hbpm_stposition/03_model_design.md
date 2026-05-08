# 数据模型设计 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 scene_doc.json 字段实抓 + 反编译实证 + haos_adminorghis 对齐
> **confidence**: verified
> **审计时间**: 2026-04-27
> **物理表**: `t_hbpm_position`（与 hbpm_positionhr 共用）+ `t_hbpm_standposentry` + `t_hbpm_position_l`

---

## 1. 标识与定位

| 项 | 值 | 来源 |
|---|---|---|
| 业务场景中文名 | 标准岗位维护（菜单："岗位维护 → 标准岗位维护"）| `scenario.json menu_paths` |
| formNumber | `hbpm_stposition` | `scene_doc.json._meta.formId` |
| formId 技术码 | `24BOER/9R4/9` | `scene_doc.json._meta.formIdTechnical` |
| 场景模型 | **HisModel 历史时序资料** | 继承链含 `hbp_histimeseqtpl` |
| 物理表（主） | **`t_hbpm_position`**（与 hbpm_positionhr 共用 · 区分键 `isstandardpos`）| `scene_doc.json basicInfo.physicalTable` |
| 物理表（附表） | `t_hbpm_standposentry`（行政组织关联子表）| `scene_doc.json` |
| 多语言表 | `t_hbpm_position_l` | `scene_doc.json` |
| 域 | hbpm（岗位管理）| `_meta.domain` |
| 云 | 组织发展云 | `_meta.cloud` |
| menuId | 1493506395784391680 | `scenario.json` |

---

## 2. 核心架构：共用物理表 · isstandardpos 区分键

本场景与 `hbpm_positionhr`（岗位信息维护）共用 `t_hbpm_position` 物理表，通过 `isstandardpos` 字段区分：

| formNumber | 业务名 | isstandardpos | 说明 |
|---|---|---|---|
| `hbpm_stposition`（本场景）| 标准岗位维护 | `"1"` | 标准岗位（StandardPositionSaveOp 强制写入）|
| `hbpm_positionhr` | 岗位信息维护 | `"0"` | 实际岗位（引用 stposition 的 boid）|

**关键认知**：
- 扩展 hbpm_stposition 字段，实际改的是 `t_hbpm_position` 物理表，会同时影响 hbpm_positionhr
- 部署 ISV 扩展字段后，必须**双场景回归**（stposition + positionhr 都测）
- hbpm_positionhr 的 `stposition` 字段（HisModelBasedataField）引用本场景的 boid

---

## 3. 继承链

```
L0 bos_basetpl（基础资料模板）
  └── L1 hbp_bd_tpl_all（HR基础资料全页面模板）
      └── L2 hbp_histimeseqtpl（HR基础资料全页面历史模板）⭐ 时序核心
          └── L3 hbpm_stposition（标准岗位维护 · 本场景）
```

继承 `hbp_histimeseqtpl` 确认本场景是 **HisModel 时序资料**，拥有完整的版本管理字段集（boid / iscurrentversion / hisversion 等）。

---

## 4. ⭐ 时序核心字段（来自 hbp_histimeseqtpl · L2 · 🔒 不能改）

| 字段 key | 类型 | 物理列 | 时序语义 | PR |
|---|---|---|---|---|
| **`boid`** | BigIntField | `t_hbpm_position.fboid` | **业务对象 ID · 跨所有版本不变**（PR-009）| PR-009 |
| **`iscurrentversion`** | CheckBoxField | `t_hbpm_position.fiscurrentversion` | **当前版本标记 · true=当前最新版本**（PR-008）| PR-008 |
| `hisversion` | TextField | `t_hbpm_position.fhisversion` | 版本号（同 boid 链内递增 · 1, 2, 3...）| - |
| `sourcevid` | BigIntField | `t_hbpm_position.fsourcevid` | 指向上一版本 id（链表结构）| - |
| `firstbsed` | DateField | `t_hbpm_position.ffirstbsed` | 该 boid 最早生效日期（所有版本共享）| - |
| `bsed` | DateField | `t_hbpm_position.fbsed` | 当前版本生效日期（区间起点）| - |
| `bsled` | DateField | `t_hbpm_position.fbsled` | 当前版本失效日期（区间终点）| - |
| `datastatus` | ComboField | `t_hbpm_position.fdatastatus` | 数据版本状态（TEMP/待生效/生效中/过期/废弃）| - |
| `changedescription` | TextField | — | 变更说明 | - |

> **禁止修改上述字段**：这 9 个字段由 HisModelOPCommonPlugin 框架管理。ISV 代码永远不能调用 `entity.set("boid", ...)` / `entity.set("iscurrentversion", ...)` 等直接写入时序字段。

---

## 5. 通用基础字段（来自 L0 bos_basetpl · 🔒）

| 字段 key | 类型 | 物理列 | 语义 |
|---|---|---|---|
| `creator` | CreaterField | `t_hbpm_position.fcreatorid` | 创建人 → bos_user（系统自动）|
| `modifier` | ModifierField | `t_hbpm_position.fmodifierid` | 修改人 → bos_user（系统自动）|
| `createtime` | CreateDateField | `t_hbpm_position.fcreatetime` | 创建时间 |
| `modifytime` | ModifyDateField | `t_hbpm_position.fmodifytime` | 修改时间 |
| `masterid` | MasterIdField | `t_hbpm_position.fmasterid` | 主数据内码 |

---

## 6. HR 基础资料通用字段（来自 L1 hbp_bd_tpl_all · 🔒 慎改）

| 字段 key | 类型 | 必填 | 物理列 | 语义 | 雷区 |
|---|---|---|---|---|---|
| `number` | TextField | ✓ | `t_hbpm_position.fnumber` | 标准岗位编码（受编码规则管理）| 黄色 |
| `name` | MuliLangTextField | ✓ | `t_hbpm_position_l.fname` | 标准岗位名称（多语言）| - |
| `status` | BillStatusField | - | `t_hbpm_position.fstatus` | 数据状态（A=初始/B=提交/C=审核）| 黄色 |
| `enable` | BillStatusField | - | `t_hbpm_position.fenable` | 业务状态（0=启用中/1=启用/10=禁用）| 黄色 |
| `simplename` | MuliLangTextField | - | `t_hbpm_position_l.fsimplename` | 简称（多语言）| - |
| `description` | MuliLangTextField | - | `t_hbpm_position_l.fdescription` | 备注（多语言）| - |
| `index` | IntegerField | - | `t_hbpm_position.findex` | 排序号 | - |
| `issyspreset` | CheckBoxField | - | `t_hbpm_position.fissyspreset` | 系统预置（PR-007）| 红色 |
| `disabler` | UserField | - | `t_hbpm_position.fdisablerid` | 禁用人 → bos_user | 红色 |
| `disabledate` | DateTimeField | - | `t_hbpm_position.fdisabledate` | 禁用时间 | 红色 |
| `initdatasource` | ComboField | - | `t_hbpm_position.finitdatasource` | 数据来源 | 红色 |
| `orinumber` | TextField | - | — | 出厂编码 | 红色 |
| `oriname` | MuliLangTextField | - | — | 出厂名称 | 红色 |
| `oristatus` | ComboField | - | — | 出厂数据编辑状态 | 红色 |

---

## 7. 标准岗位业务字段（L3 hbpm_stposition 自身 · ✅ 可扩展区）

| 字段 key | 类型 | 必填 | 物理列 | refEntity | 语义 | ISV 可改 |
|---|---|---|---|---|---|---|
| `org` | OrgField | ✓ | `t_hbpm_position.forgid` | bos_org | 组织体系管理组织（BU）| ❌ |
| `adminorg` | HRAdminOrgField | ✓ | `t_hbpm_standposentry.fadminorgid` | haos_adminorghrf7 | 行政组织（按 boid 引用）| ❌ |
| `applicableorg` | HRAdminOrgField | ✓ | — | haos_adminorghrf7 | 适用行政组织 | ❌ |
| `iscontainsu` | CheckBoxField | - | — | - | 包含下级 | ✅ |
| `job` | HisModelBasedataField | - | — | hbjm_jobhr | 职位（时序型字段 · 引用 boid）| ✅ |
| `jobscm` | BasedataField | - | `t_hbpm_position.fjobscmid` | hbjm_jobscmhr | 职位体系方案 | ✅ |
| `joblevelscm` | HisModelBasedataField | - | — | hbjm_joblevelscmhr | 职级方案（时序型）| ✅ |
| `jobgradescm` | HisModelBasedataField | - | — | hbjm_jobgradescmhr | 职等方案（时序型）| ✅ |
| `positiontype` | BasedataField | - | — | hbpm_positiontype | 岗位类型 | ✅ |
| `jobgraderange` | TextField | - | — | - | 职等范围（展示用）| ✅ |
| `joblevelrange` | TextField | - | — | - | 职级范围（展示用）| ✅ |
| `lowjoblevel` | BasedataField | - | `t_hbpm_position.flowjoblevelid` | hbjm_joblevelhr | 最低职级 | ✅ |
| `highjoblevel` | BasedataField | - | `t_hbpm_position.fhighjoblevelid` | hbjm_joblevelhr | 最高职级 | ✅ |
| `lowjobgrade` | BasedataField | - | `t_hbpm_position.flowjobgradeid` | hbjm_jobgradehr | 最低职等 | ✅ |
| `highjobgrade` | BasedataField | - | `t_hbpm_position.fhighjobgradeid` | hbjm_jobgradehr | 最高职等 | ✅ |
| `jobseq` | BasedataPropField | - | — | - | 职位序列 | ✅ |
| `jobfamily` | BasedataPropField | - | — | - | 职位族 | ✅ |
| `jobclass` | BasedataPropField | - | — | - | 职位类 | ✅ |
| `orgdesignbu` | OrgField | - | — | bos_org | 职位体系管理组织 | ✅ |
| `isstandardpos` | ComboField | - | `t_hbpm_position.fisstandardpos` | - | 是否标准岗位（框架强制 "1"）| ✅ |
| `entryboid` | BigIntField | - | `t_hbpm_standposentry.fentryboid` | - | 子表行 boid | ✅ |
| `basedatapropfield` | BasedataPropField | - | — | - | 行政组织编码（衍生展示）| ✅ |

---

## 8. 多语言表（`t_hbpm_position_l`）

| 多语言列 | 字段 key | 备注 |
|---|---|---|
| `fname` | name | 标准岗位名称（必填）|
| `fsimplename` | simplename | 简称 |
| `fdescription` | description | 备注 |

> **扩展多语言字段**：走 MuliLangTextField 类型，平台自动落 `_l` 表，不要写成 `_i`（垂直分表）。

---

## 9. 子分录（t_hbpm_standposentry）

| 字段 | 类型 | 物理列 | 含义 |
|---|---|---|---|
| `adminorg` | HRAdminOrgField | `t_hbpm_standposentry.fadminorgid` | 行政组织（boid 维度 · PR-009）|
| `entryboid` | BigIntField | `t_hbpm_standposentry.fentryboid` | 子表行 boid |

---

## 10. 雷区字段汇总

🔴 **红色（系统/平台维护 · 手改破坏一致性）**：
`creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oriname` / `oristatus` / **`boid`** / **`iscurrentversion`** / `hisversion` / `sourcevid` / `firstbsed`

🟡 **黄色（变更级联下游）**：
`number` / `status` / `enable` / `bsed` / `bsled` / `datastatus` / `adminorg` / `isstandardpos`

✅ **业务自由扩展区**：
`simplename` / `description` / `index` / `jobscm` / `positiontype` / `joblevelscm` / `jobgradescm` / `lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` / `jobseq` / `jobfamily` / `jobclass` / `orgdesignbu`

---

## 11. HisModelBasedataField 类型说明

场景内有 3 个字段使用 `HisModelBasedataField` 类型：
- `job` → hbjm_jobhr（职位，本身也是时序资料）
- `joblevelscm` → hbjm_joblevelscmhr
- `jobgradescm` → hbjm_jobgradescmhr

**HisModelBasedataField 的特殊性**：
- 这种字段引用的是另一个 HisModel 实体，存储的是目标实体的 **boid**（而非 id）
- F7 选值时平台自动返回当前版本的 boid（PR-009）
- ISV **不能用 OpenAPI modifyMeta 添加 HisModelBasedataField 类型**（74 值枚举中不支持）

---

## 12. 平台命名规则速查（PR-008 · PR-009 完整说明）

### PR-008：时序资料当前版本查询必须用 iscurrentversion=true

```java
// ✅ 正确：查询当前有效标准岗位
QFilter filter = new QFilter("iscurrentversion", "=", Boolean.TRUE);
DynamicObjectCollection positions = QueryServiceHelper.query(
    "hbpm_stposition",
    "id,boid,number,name,adminorg,job",
    new QFilter[]{filter}
);

// ✅ 正确：根据 boid 查当前版本（跨场景查询）
QFilter filter = new QFilter("boid", "=", stdPosBoid)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));

// ❌ 错误：不加 iscurrentversion 会查到所有历史版本
QFilter wrongFilter = new QFilter("number", "=", "SP001");
// 可能返回同一岗位的 N 个历史版本！
```

**为什么不能用 bsed 代替**：bsed 是生效日期，但一个 boid 对应的最新版本的 bsed 不一定是今天之前（可能是未来生效的版本），正确的"当前版本"标记是 `iscurrentversion=true`。

### PR-009：boid 是业务维度 · id 是版本维度

```
业务场景示意：
  标准岗位"高级研发工程师" (boid=10001)
    版本1: id=10001, iscurrentversion=false, bsed=2023-01-01, bsled=2023-12-31
    版本2: id=20002, iscurrentversion=false, bsed=2024-01-01, bsled=2024-12-31
    版本3: id=30003, iscurrentversion=true,  bsed=2025-01-01, bsled=9999-12-31  <- 当前版本

  规律：boid=10001 = 当前版本(id=30003)的 boid 值也是 10001
```

**下游引用规范（hbpm_positionhr 为例）**：
- hbpm_positionhr.stposition 存的是 hbpm_stposition 的 **boid**
- 标准岗位变更产生新版本时，hbpm_positionhr 不需要更新（boid 不变）
- 如果存 id（版本 id），变更后旧 id 对应 iscurrentversion=false，数据游离

---

## 13. ISV 扩展字段建议

| 维度 | 建议 |
|---|---|
| 扩展对象 formId | **`hbpm_stposition`**（本场景主层）|
| 字段 key | `<isv前缀>_<name>` · 如 `${ISV_FLAG}_certtype` |
| fieldType | TextField / BasedataField / DateField / ComboField · **避免 HisModelBasedataField / HRAdminOrgField**（OpenAPI 不支持）|
| 物理列命名 | 不传 columnName · 让平台按 `f + key.lower()` 默认生成 |
| 字段长度 | key ≤ 24 字符（数据库列名上限）|
| 扩展同时影响 | hbpm_positionhr（共用物理表）· 必须双场景测试 |

---

## 14. 关联文档

- `02_business_rules.md` · INV-SP-01 ~ 18 规则说明
- `06_customization_solutions.md` · CS-04 时序版本查询扩展
- `11_upstream_downstream_logic.md` · hbpm_positionhr 引用关系
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009 完整原文
- `knowledge/scenarios/haos_adminorghis/03_model_design.md` · 同性质 HisModel 场景对比
