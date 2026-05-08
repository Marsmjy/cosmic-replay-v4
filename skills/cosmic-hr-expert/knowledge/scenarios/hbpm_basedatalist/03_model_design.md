# 模型设计 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于 scene_doc.json 字段表（真实元数据同步）
> **confidence**: verified（字段来自 lastMetadataSyncedAt=2026-04-23 实抓）
> **最后更新**: 2026-04-27

---

## 一、实体结构概述

`hbpm_basedatalist` 是 **BaseFormModel**（非时序）。主实体仅有单层结构，无子表实体。字段共 **20 个**（含 L0 系统字段）。

**区别于 HisModel 的核心特征**：
- 无 `boid` 字段（业务对象 ID）
- 无 `iscurrentversion` 字段（当前版本标记）
- 无历史版本表（无 `_his` 后缀物理表）
- 查询时不需要加 `iscurrentversion=true` 过滤（与 haos_adminorg 等 HisModel 场景区别）

> 参见 PR-009：boid 是业务维度 · id 是版本维度（本场景为 BaseFormModel · 无此区分 · id 即唯一标识）

---

## 二、字段清单（完整 · 来自 scene_doc.json）

| 字段 key | 显示名 | 类型 | 层级 | 必填 | ISV可改 | 物理列 | 雷区等级 |
|---|---|---|---|---|---|---|---|
| `number` | 编码 | TextField | L1 | N | Y | fnumber | - |
| `name` | 名称 | MuliLangTextField | L1 | N | Y | fname | - |
| `simplename` | 简称 | MuliLangTextField | L1 | N | Y | fsimplename | - |
| `description` | 描述 | MuliLangTextField | L1 | N | Y | fdescription | - |
| `index` | 排序号 | IntegerField | L1 | N | Y | findex | - |
| `status` | 数据状态 | BillStatusField | L1 | N | Y | fstatus | 🟡 慎改 |
| `enable` | 使用状态 | BillStatusField | L1 | N | Y | fenable | 🟡 慎改 |
| `issyspreset` | 系统预置 | CheckBoxField | L1 | N | **N** | fissyspreset | 🔴 系统维护 |
| `pagekey` | 页面标识 | TextField | **L3** | Y | **N** | fpagekey | - |
| `creator` | 创建人 | CreaterField | L0 | N | N | fcreatorid | 🔴 系统维护 |
| `modifier` | 修改人 | ModifierField | L0 | N | N | fmodifierid | 🔴 系统维护 |
| `createtime` | 创建时间 | CreateDateField | L0 | N | N | fcreatetime | 🔴 系统维护 |
| `modifytime` | 修改时间 | ModifyDateField | L0 | N | N | fmodifytime | 🔴 系统维护 |
| `masterid` | 主数据内码 | MasterIdField | L0 | N | N | fmasterid | 🔴 系统维护 |
| `disabler` | 禁用人 | UserField | L1 | N | N | FDisablerID | 🔴 系统维护 |
| `disabledate` | 禁用时间 | DateTimeField | L1 | N | N | FDisableDate | 🔴 系统维护 |
| `initdatasource` | 数据来源 | ComboField | L1 | N | N | finitdatasource | 🔴 系统维护 |
| `orinumber` | 出厂编码 | TextField | L1 | N | N | forinumber | 🔴 系统维护 |
| `oristatus` | 出厂数据编辑状态 | ComboField | L1 | N | N | foristatus | 🔴 系统维护 |
| `oriname` | 出厂名称 | MuliLangTextField | L1 | N | N | foriname | 🔴 系统维护 |

---

## 三、关键字段业务语义

### 3.1 `pagekey`（L3 · 岗位模板对话框层引入）

- **作用**：存储当前基础资料分类行对应的列表页 formId
- **触发**：`HRBDGroupList` 在用户点击超链接或双击行时，用 `pagekey` 值打开对应子列表
- **约束**：isvCanModify=false，不允许 ISV 修改；`required=true`（必填）
- **物理列**：`fpagekey`（VARCHAR 类型）
- **查询模式**：先按行 id 查 `pagekey` 字段值，再用该值作为 formId 构造 `ListShowParameter`

### 3.2 `issyspreset`（L1 · 系统预置标记）

- **作用**：标记当前数据是否为系统出厂预置
- **触发逻辑**：`PositionBasedataEdit.afterBindData` 读取此字段 → 为 true 时切换页面为 VIEW 状态
- **写入方**：标品导入/升级包在安装时写入，ISV 无法修改
- **雷区**：不能手工写 `true`（会导致业务数据被锁死为只读）

### 3.3 `status` 与 `enable`（双状态字段）

- `status`（fstatus）：数据状态，BillStatusField，受标品 HRBaseDataStatusOp 管控
- `enable`（fenable）：使用状态，控制该基础资料是否对业务可用
- **雷区**：两个字段都标记为 🟡 慎改，直接修改会影响下游引用此字典的所有业务数据

---

## 四、物理表命名规律

hbpm 岗位域基础资料的物理表命名遵循以下规律：

```
主表：t_hbpm_{场景简称}
例：t_hbpm_basedatalist（岗位基础资料主表）
子表：t_hbpm_{场景简称}_{子实体名}（本场景无子表）
```

**对比其他场景**：
- `hbpm_positiontpltype` → `t_hbpm_positiontpltype`
- `hbpm_reportcoreltype` → `t_hbpm_reportcoreltype`（含 `t_hbpm_orgteamtype` 子表用于 orgteamtype 多选字段）

---

## 五、与 HisModel 场景的对比（知识点）

| 维度 | 本场景（BaseFormModel） | HisModel 场景（如 haos_adminorg） |
|---|---|---|
| 模型类型 | BaseFormModel | HisFormModel |
| id 语义 | 数据唯一主键 | 版本唯一 ID |
| boid | **不存在** | 业务对象 ID（跨版本共用） |
| iscurrentversion | **不存在** | true=当前版本 · false=历史版本 |
| 查当前数据 | `QFilter("id","=",id)` 直接查 | 必须加 `.and("iscurrentversion","=",Boolean.TRUE)` |
| 历史表 | 无 | 有 `_his` 后缀物理表 |
| 下游引用 | 存 id 直接引用 | 存 boid 引用业务对象（PR-009） |

> ISV 开发岗位基础资料扩展时，查询和引用均用 `id`，无需处理 boid/iscurrentversion。

---

## 六、平台命名规则速查（PR-008/PR-009）

以下两条 PR 铁律在本场景**不适用**（BaseFormModel 无时序），但作为知识点必须了解：

**PR-008 · 时序资料查当前版本用 iscurrentversion=true**
- 适用场景：HisModel（haos_adminorg、hbjm_jobhr、hrpi_empjobrel 等）
- 本场景：BaseFormModel → 无 iscurrentversion → 直接用 id 查

**PR-009 · boid 是业务维度 · id 是版本维度**
- 适用场景：HisModel 下游引用时必须引用 boid，而非某个 id
- 本场景：BaseFormModel → 无 boid → id 即是业务对象唯一标识
- ISV 在 haos_adminorg 中查"属于某岗位基础资料的组织"时，直接 `QFilter("position_basedata","=", basedataId)` 即可，无需 boid 转换

**字段前缀识别规则**：
- `f` 前缀：物理列（如 fnumber、fname、findex）→ 标品字段的数据库存储列
- ISV 扩展字段的 key 必须带 ISV 前缀（如 `${ISV_FLAG}_`），物理列自动生成为 `f{key}`（如 `ftdkw_category`）
- key 长度上限：24 字符（数据库列名约束），超限平台静默截断
