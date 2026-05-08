# 模型设计 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于 scene_doc.json 字段表（真实元数据同步 2026-04-23）
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、实体结构概述

`hbpm_reportcoreltype` 是 **BaseFormModel**（非时序）。与 hbpm_positiontpltype 不同，本场景含 **子表实体**（`t_hbpm_orgteamtype`，由 orgteamtype 多选字段驱动）。字段共 **27 个**（含 L4 层 orgteamtype 多选字段）。

**三场景字段数对比**：
- hbpm_basedatalist：20 字段（无 L3 组织管控）
- hbpm_positiontpltype：26 字段（有 L3 组织管控，无子表）
- hbpm_reportcoreltype：27 字段（有 L3 组织管控 + L4 orgteamtype 多选子表）

**区别于 HisModel 的核心特征**：
- 无 `boid` / `iscurrentversion`
- 主表直接 UPDATE，无历史版本表
- 查询时不加 iscurrentversion 过滤

---

## 二、字段清单（完整 · 来自 scene_doc.json）

### 基础字段（L0/L1 层）

| 字段 key | 显示名 | 类型 | 层级 | 必填 | ISV可改 | 物理列 |
|---|---|---|---|---|---|---|
| `number` | 编码 | TextField | L1 | N | Y | fnumber |
| `name` | 名称 | MuliLangTextField | L1 | N | Y | fname |
| `simplename` | 简称 | MuliLangTextField | L1 | N | Y | fsimplename |
| `description` | 描述 | MuliLangTextField | L1 | N | Y | fdescription |
| `index` | 排序号 | IntegerField | L1 | N | Y | findex |
| `status` | 数据状态 | BillStatusField | L1 | N | Y | fstatus |
| `enable` | 使用状态 | BillStatusField | L1 | N | Y | fenable |
| `issyspreset` | 系统预置 | CheckBoxField | L1 | N | **N** | fissyspreset |
| `creator` | 创建人 | CreaterField | L0 | N | N | fcreatorid |
| `modifier` | 修改人 | ModifierField | L0 | N | N | fmodifierid |
| `createtime` | 创建时间 | CreateDateField | L0 | N | N | fcreatetime |
| `modifytime` | 修改时间 | ModifyDateField | L0 | N | N | fmodifytime |
| `masterid` | 主数据内码 | MasterIdField | L0 | N | N | fmasterid |
| `disabler` | 禁用人 | UserField | L1 | N | N | FDisablerID |
| `disabledate` | 禁用时间 | DateTimeField | L1 | N | N | FDisableDate |
| `initdatasource` | 数据来源 | ComboField | L1 | N | N | finitdatasource |
| `orinumber` | 出厂编码 | TextField | L1 | N | N | forinumber |
| `oristatus` | 出厂数据编辑状态 | ComboField | L1 | N | N | foristatus |
| `oriname` | 出厂名称 | MuliLangTextField | L1 | N | N | foriname |

### 组织管控字段（L3 层）

| 字段 key | 显示名 | 类型 | 层级 | 必填 | ISV可改 |
|---|---|---|---|---|---|
| `createorg` | 创建组织 | OrgField | L3 | N | Y |
| `org` | 管理组织 | OrgField | L3 | N | Y |
| `useorg` | 使用组织 | OrgField | L3 | N | Y |
| `ctrlstrategy` | 控制策略 | ComboField | L3 | N | Y |
| `sourcedata` | 原资料id | BigIntField | L3 | N | Y |
| `bitindex` | 位图 | IntegerField | L3 | N | Y |
| `srcindex` | 原资料位图 | IntegerField | L3 | N | Y |
| `srccreateorg` | 原创建组织 | OrgField | L3 | N | Y |

### L4 专有字段（子表类型）

| 字段 key | 显示名 | 类型 | 层级 | 必填 | ISV可改 | 物理表 |
|---|---|---|---|---|---|---|
| `orgteamtype` | 所属组织分类 | **MulBasedataField** | **L3（标注但实为 L4 引入）** | **Y** | **N** | `t_hbpm_orgteamtype`（子表）|

---

## 三、关键字段业务语义

### 3.1 `orgteamtype`（所属组织分类 · 最关键字段）

- **类型**：MulBasedataField（多选基础资料字段）
- **必填**：true（required=true，保存时必须选择至少一个组织分类）
- **物理存储**：子表 `t_hbpm_orgteamtype`（每条协作类型可关联多个组织分类）
- **isvCanModify=false**：ISV 不可修改此字段的元数据（但可以在 FormPlugin/OP 层读取值）
- **注意**：在 OP 层 onPreparePropertys 中需要显式声明 `orgteamtype` 才能读取子表数据（PR-010）

### 3.2 `index`（排序号）

- 与 hbpm_positiontpltype 不同：本场景没有 index 自动递增逻辑（PositionBasedataEdit 不处理 index）
- 排序影响 `PositionnBaseDataOrderPlugin.setFilter` 中的 `index asc` 排序
- 手动填写，无自动计算

### 3.3 排序逻辑驱动的字段组合

`PositionnBaseDataOrderPlugin` 实现 `createorg.id asc, index asc` 双列排序，所以：
- `createorg`（创建组织）+`index`（排序号）联合决定列表展示顺序
- 同一 createorg 下按 index 升序；不同 createorg 按 id 升序

---

## 四、物理表命名规律

```
主表：t_hbpm_reportcoreltype（fnumber, fname, findex, fenable 等）
子表：t_hbpm_orgteamtype（orgteamtype 多选字段的存储表）

子表结构（推断）：
  fid（子表行 id）
  fentryid（主表外键 → t_hbpm_reportcoreltype.fid）
  forgteamtype（关联的组织分类 id）
```

---

## 五、与 HisModel 场景的对比（知识点）

| 维度 | 本场景（BaseFormModel）| HisModel（如 haos_adminorg）|
|---|---|---|
| boid | **不存在** | 业务对象 ID |
| iscurrentversion | **不存在** | true=当前版本 |
| 子表查询 | 直接查 t_hbpm_orgteamtype | 子表也有 iscurrentversion 过滤 |
| 反向引用查询 | 直接 id 查 | 必须加 iscurrentversion=true |

---

## 六、平台命名规则速查（PR-008/PR-009）

**PR-008/PR-009 在本场景不适用（BaseFormModel）**：
- 本场景无 boid，无 iscurrentversion
- 反向引用查询直接用 id，不加版本过滤
- 子表 `t_hbpm_orgteamtype` 同样无 iscurrentversion

**MulBasedataField 查询模式**（ISV 若需反向查"哪些协作类型关联了某组织分类"）：
```java
// MulBasedataField 走子表路径查（不是直字段查）
QFilter qf = new QFilter("orgteamtype.fbasedataid", "=", orgteamTypeId);
// 注意：多选字段通过 .fbasedataid 子表路径查，不是直字段查
```

**ISV 字段命名规范**：
- 扩展字段 key 必须带 ISV 前缀（如 `${ISV_FLAG}_`），长度 ≤ 24 字符
- 主实体字段：加在 `hbpm_reportcoreltype` 主实体
- 子表字段：加在 `t_hbpm_orgteamtype` 子实体（如需扩展子表）
