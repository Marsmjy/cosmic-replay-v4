# 模型设计 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于 scene_doc.json 字段表（真实元数据同步 2026-04-23）
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、实体结构概述

`hbpm_positiontpltype` 是 **BaseFormModel**（非时序）。主实体单层结构，无子表实体。字段共 **26 个**（含 L0 系统字段 + L3 组织管控字段）。

**与 hbpm_basedatalist 的关键区别**：
- 含 L3 组织管控字段：`createorg`、`org`、`useorg`、`ctrlstrategy`、`sourcedata`、`bitindex`、`srcindex`、`srccreateorg`
- 这些字段由 hbp_bd_orgtpl_dlg（HR 带组织基础资料模板）在 L3 引入
- 意味着岗位模板类型支持按组织范围管控可见性（控制策略）

**区别于 HisModel 的核心特征**：
- 无 `boid` 字段
- 无 `iscurrentversion` 字段
- 无历史版本表
- 查询时不需要加 `iscurrentversion=true` 过滤

---

## 二、字段清单（完整 · 来自 scene_doc.json）

### 基础字段（L0/L1 层）

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

### 组织管控字段（L3 层 · 由 hbp_bd_orgtpl_dlg 引入）

| 字段 key | 显示名 | 类型 | 层级 | 必填 | ISV可改 | 物理列 | 说明 |
|---|---|---|---|---|---|---|---|
| `createorg` | 创建组织 | OrgField | L3 | N | Y | — | 引用 bos_org |
| `org` | 管理组织 | OrgField | L3 | N | Y | — | 引用 bos_org |
| `useorg` | 使用组织 | OrgField | L3 | N | Y | — | 引用 bos_org |
| `ctrlstrategy` | 控制策略 | ComboField | L3 | N | Y | — | 管控可见性范围 |
| `sourcedata` | 原资料id | BigIntField | L3 | N | Y | — | 来源数据 |
| `bitindex` | 位图 | IntegerField | L3 | N | Y | — | 继承位图 |
| `srcindex` | 原资料位图 | IntegerField | L3 | N | Y | — | 来源位图 |
| `srccreateorg` | 原创建组织 | OrgField | L3 | N | Y | — | 引用 bos_org |

---

## 三、关键字段业务语义

### 3.1 `index`（排序号 · 核心字段）

- **作用**：控制岗位模板类型在下拉列表中的显示顺序
- **自动填充**：`PositionTplTypeEditPlugin.afterBindData` 新建时自动填入 `maxIndex + 10`（INV-TT-02）
- **OP 层兜底**：`PositionTplTypeSaveOp.beginOperationTransaction` 再次保证不为空（INV-TT-05）
- **唯一性约束**：`PositionTplTypeIndexUniqueValidator` 校验同 org 下 index 不重复
- **步长设计**：步长 10 留出了插入空间（如需在两条之间插入，手动改为 15/25）

### 3.2 `enable`（使用状态）

- **"0" = 禁用**：`PositionTplTypeEditPlugin` 检测 `enable="0"` 切换 VIEW 状态并隐藏保存/取消按钮（INV-TT-01）
- 与 hbpm_basedatalist 的 `issyspreset` 保护不同：此处是业务禁用保护（非出厂数据保护）

### 3.3 `ctrlstrategy`（控制策略）

- 由 L3 模板引入，通过 `BdCtrlStrtgyShowLogicPlugin`（#3 bos 平台插件）联动 `createorg`/`org`/`useorg` 字段的可见性
- `CtrlStrategyValidator`（由 PositionTplTypeSaveOp 注册）保证控制策略配置合规

---

## 四、物理表命名规律

```
主表：t_hbpm_positiontpltype
（无子表实体）

对比同域场景：
  hbpm_basedatalist    → t_hbpm_basedatalist（无子表）
  hbpm_reportcoreltype → t_hbpm_reportcoreltype + t_hbpm_orgteamtype（含子表）
```

---

## 五、与 HisModel 场景的对比（知识点）

| 维度 | 本场景（BaseFormModel） | HisModel 场景（如 haos_adminorg） |
|---|---|---|
| 模型类型 | BaseFormModel | HisFormModel |
| boid | **不存在** | 业务对象 ID（跨版本共用）|
| iscurrentversion | **不存在** | true=当前版本 |
| 查询方式 | `QFilter("id","=",id)` | 必须加 `iscurrentversion=true` |
| 历史表 | 无 | 有 `_his` 后缀 |

---

## 六、平台命名规则速查（PR-008/PR-009）

**PR-008 · iscurrentversion（本场景不适用）**
- 本场景 BaseFormModel，无 iscurrentversion
- ISV 查询时直接用 id，不加版本过滤

**PR-009 · boid 业务维度（本场景不适用）**
- 本场景无 boid，id 即是唯一标识
- 下游（hbpm_positiontpl 岗位模板）引用本场景时，直接引用 id

**ISV 字段命名规范**：
- 必须带 ISV 前缀（如 `${ISV_FLAG}_`）
- key 长度 ≤ 24 字符（数据库列名上限）
- 扩展字段加在主实体（本场景无子表）
