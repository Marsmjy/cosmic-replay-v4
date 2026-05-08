# 模型设计 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于 `scene_doc.json` (34 字段实抓) + `_auto_inherit_chain.md` + `_auto_plugin_semantics.md` (3 反编译类)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_dynaruleitem.md` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit` / `kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp` / `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` (2026-04-28)

---

## ⭐ 关键业务事实 · 一张主表 + 一张枚举子表的"动态权限规则字典"

`hrcs_dynaruleitem`（规则参数项）是 HR 通用服务（hrcs）域里 `hrcs_dynascheme`（动态授权方案）的**子配置项 / 规则字典**。它不是单据，而是**基础资料 + 单分录子表**的常规结构。所有 `hrcs_dynascheme.condition` JSON 里的 `paramId` 都指向本表的某行；所有 `condition.conditionList[].value` 在 datatype=enum 时都对应本表 `entryentity` 子表的某 `value`。

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_hrcs_dynaruleitem` | 主实体（基础资料 BillFormModel） | 规则参数项主体（含编码/名称/数据类型/关联标记/值来源类型/微服务路径） | `fid` / `fnumber` / `fname` / `fdatatype` / `fentitytype` / `fisrelatparam` / `frelatruleparamid` / `frelatpropkey` / `fvalsourcetype` / `fsourceentitytype` / `fmserviceappid` / `fmserviceclass` |
| `t_hrcs_dynaruleitemenum` | 分录子表 EntryEntity | 枚举值清单（datatype=enum 时存）· 每行 1 个枚举 (value, displayvalue) | `fentryid` / `fvalue` / `fdisplayvalue` |

⚠️ **本场景非 HisModel · 重要实证**：grep `iscurrentversion|HisModel|boid|sourcevid` 在 scene_doc.json + 3 反编译类中 **0 命中**（2026-04-28 实证）。下游 `hrcs_dynascheme` 是 HisModel 时序基础资料 · 但 `hrcs_dynaruleitem` 自己是普通 `BillFormModel` 基础资料（仅 status / enable / 编码 / 多语言名 + entry 子表）。**因此 PR-008（iscurrentversion 过滤）/ PR-009（boid 业务维度）在本场景不适用**。

⚠️ **dynaruleitem ↔ dynascheme 强耦合关系**：标品 `DynaRuleItemEdit.beforeDoOperation` 在 `deleteentry` 子操作前调 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)`（实证 L220）反查所有引用本规则参数项的方案 → 解析每个方案的 `condition` JSON `conditionList[].value` → 命中选中行的 `value` 则 `setCancel(true)` 阻断删除。这个引用阻断是 dynaruleitem 唯一的"业务校验"。

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"基础资料类列表"形态。`hrcs_dynaruleitem` 列表页由两到三层独立元数据组成：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BillFormModel | `hrcs_dynaruleitem`（主实体 · 菜单直挂） | 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据） | ⚠ 待探针确认（推测沿用平台默认 base_list） | 列表 UI 壳 |
| **F7 列表模板** | 动态表单 | `bos_listf7`（推测平台默认） | F7 列表壳（dynascheme 反向引用本表） |

**插件挂载职责分工**：
- **数据层过滤 / 权限 / setFilter** → 挂 `hrcs_dynaruleitem`（数据实体）
- **HR 域准入闸** → 挂 `HRAdminStrictPlugin`（hrcs 11 表单共用 · 不要继承）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）。

---

## 二、继承链 · BillFormModel + 单分录子表（非 HisModel）

### ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "BillFormModel"` + `entryentity` 是 EntryEntity 类型字段（实证 scene_doc.json L420-L429）。

**与 hrcs_dynascheme 的差异**：dynascheme 是 BillFormModel + HisModel + 6 个分录子表 · dynaruleitem 是 BillFormModel + 1 个分录子表（无 HisModel）。两者都用 BillFormModel · 但时序模型不一样。

### 字段层级分类

苍穹元数据字段分 4 层（与 admin_org/hjm 一致 · scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | bos_basetpl + HR 父模板 | `number` / `name` / `simplename` / `description` / `status` / `enable` / `index` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` | 🔒 / ⚠️ 多数不改 · 业务自建可改 |
| **L3** 业务字段（dynaruleitem 自身） | hrcs_dynaruleitem | `datatype` / `entitytype` / `isrelatparam` / `relatruleparam` / `relatpropkey` / `relatpropname` / `valsourcetype` / `sourceentitytype` / `sourcepropkey` / `sourcepropname` / `mserviceapp` / `mserviceclass` / `entryentity` | ⚠️ 谨慎改 |

**L2 时序层缺失** —— 这是与 dynascheme 最大的差异。本表完全没有 `boid / iscurrentversion / sourcevid / firstbsed / bsed / bsled / hisversion` 这些 HisModel 字段。

### 关键认知

- 主键是 `id`（不是 boid）· 下游 dynascheme.condition.JSON 引用本表用 `paramId = item.id`
- entryentity 子表的 `value` 是被 dynascheme.condition 引用的核心 key
- `issyspreset = true` 标记的预置参数项不能改任何业务字段（PR-007）+ FormPlugin 强制 VIEW（FP_ABD3）

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 34 个字段）

### 3.1 主表业务核心字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌ | ✅ | CodeRuleOp 自动生成（PR-006） |
| `name` | MuliLangTextField | 名称 | ❌ | ✅ | 多语言 · BdVersionSaveServicePlugin 写版本子表 _l |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | - |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | - |
| `status` | BillStatusField | 数据状态 | ❌ | ⚠️ 黄区 | HRBaseDataStatusOp 维护 |
| `enable` | BillStatusField | 使用状态 | ❌ | ⚠️ 黄区 | HRBaseDataEnableOp 维护 |
| `index` | IntegerField | 排序号 | ❌ | ✅ | - |
| **`datatype`** | **ComboField** | **数据类型** ⭐ | **✅** | 🔒（isvCanModify=false） | 取值：`bd`=基础资料 / `org`=组织 / `enum`=枚举 · 联动 entitytype + entryentity 显隐（formRule 4ZR9JXP/TBN= + 4ZRHZU5CYXJR） |

### 3.2 关联参数项 / 实体取值字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| **`entitytype`** | **BasedataField** | **基础资料类型** ⭐ | **✅** | 🔒 | refEntity=bos_entityobject · 仅 datatype=bd/org 时生效 · datatype=org 时强制 haos_adminorghrf7（FP_PC1） |
| `isrelatparam` | CheckBoxField | 关联参数项标记 | ❌ | ✅ | true 时显示 relatruleparam 控件（formRule 4ZR5YQFF=KLX） |
| **`relatruleparam`** | **BasedataField** | **主规则参数项** ⭐ | ✅（条件） | 🔒 | refEntity=hrcs_dynaruleitem（自引用）· F7 双闸过滤：isrelatparam=false + datatype in (bd, org)（FP_BF7S2） |
| `relatpropkey` | TextField | 主规则参数项的属性 key | ❌ | ✅ | 由 hrcs_choosefield_page 子页面回填（FP_CCB1） |
| `relatpropname` | TextField | 主规则参数项的属性显示名 | ✅（条件） | 🔒 | UI 显示用 · afterBindData 反查 RolePermLogServiceHelper.getEntityFieldMap 灌（FP_ABD1） |
| **`valsourcetype`** | **ComboField** | **值来源类型** ⭐ | **✅** | 🔒 | 取值：`1`=实体取值 / `2`=微服务 · 联动 sourceentitytype/sourcepropkey vs mserviceapp/mserviceclass 显隐（formRule 4ZRHJC9QK0/A + 4ZRHJC9QK/B1） |
| `sourceentitytype` | BasedataField | 值来源实体 | ✅（条件） | 🔒 | refEntity=bos_entityobject · F7 限定 modeltype in (BaseFormModel, BillFormModel)（FP_BF7S3） |
| `sourcepropkey` | TextField | 值来源属性 key | ❌ | ✅ | 由 hrcs_choosefield_page 子页面回填（FP_CCB2） |
| `sourcepropname` | TextField | 值来源属性显示名 | ✅（条件） | 🔒 | UI 显示用 · afterBindData 反查灌（FP_ABD2） |
| `mserviceapp` | BasedataField | 微服务所在应用 | ✅（条件） | 🔒 | refEntity=bos_devportal_bizapp · valsourcetype=2 时必填 |
| `mserviceclass` | TextField | 微服务类全限定名 | ✅（条件） | 🔒 | valsourcetype=2 时必填 · 后端运行时反射调 |

### 3.3 枚举子表字段（datatype=enum 时生效）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 |
|---|---|---|---|---|
| `entryentity` | EntryEntity | 枚举值清单容器（→ t_hrcs_dynaruleitemenum） | ❌ | ✅ |
| `entryentity.value` | TextField | 枚举值（被 dynascheme.condition 引用的核心 key） | ✅ | 🔒 已存行（FP_ABD4 锁列） |
| `entryentity.displayvalue` | MuliLangTextField | 枚举显示名 | ✅ | ✅ |

### 3.4 平台维护字段（ISV 不要 setValue）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 平台 | 系统级 · 不可改 |
| `disabler` / `disabledate` | 平台 | disable 时记录 · HRBaseDataEnableOp 维护 |
| `initdatasource` / `orinumber` / `oristatus` / `oriname` | 平台 | HRBaseOriginalOp 原始值记录（变更前后对比） |
| `issyspreset` | 平台 | 系统预置标记 · true 时强制 VIEW（FP_ABD3）+ 不允许编辑（PR-007） |

---

## 四、表间关系（dynaruleitem ↔ dynascheme 强耦合）

```
hrcs_dynaruleitem (本场景)
   ├─ entryentity[] (枚举值清单)
   │     └─ value, displayvalue
   │
   ├─ entitytype → bos_entityobject (基础资料 F7)
   ├─ relatruleparam → hrcs_dynaruleitem (自引用 · 主规则参数项)
   ├─ sourceentitytype → bos_entityobject (值来源实体)
   └─ mserviceapp → bos_devportal_bizapp (微服务应用)

       ↑↑↑ 被 dynascheme 引用 ↑↑↑

hrcs_dynascheme.condition (LargeTextField · DecisionSet JSON)
   └─ conditionList[].paramId → dynaruleitem.id     (规则参数 paramId 指向本表)
   └─ conditionList[].value   → dynaruleitem.entryentity.value  (datatype=enum 时引用)
```

**JSON 结构示例**（`DynaRuleItemEdit.checkRelSchemeVal` L260-L268 反编译实证）：

```json
{
  "conditionList": [
    {
      "paramId": "<dynaruleitem.id>",
      "value": "<entryentity.value>"
    }
  ]
}
```

---

## 五、字段联动矩阵（formRule + propertyChanged 实证）

| 触发字段 | 联动逻辑 | 触发位置 | 影响字段 |
|---|---|---|---|
| `datatype` 改 = `org` | 自动写入 `entitytype = haos_adminorghrf7` | DynaRuleItemEdit.propertyChanged L144-L150 | entitytype |
| `datatype` 改非 `org` | `entitytype = null` | DynaRuleItemEdit.propertyChanged L148-L150 | entitytype |
| `datatype` = `bd` | 显示 entitytype 控件 + 隐藏 entryentity | formRule 4ZR9JXP/TBN= | entitytype 显隐 |
| `datatype` = `enum` | 显示 entryentity + enumbar 工具栏 | formRule 4ZRHZU5CYXJR | entryentity 显隐 |
| `isrelatparam` = true | 显示 relatruleparam + relatpropkey + relatpropname | formRule 4ZR5YQFF=KLX | relatruleparam 显隐 |
| `valsourcetype` = `1` | 显示 sourceentitytype + sourcepropkey + sourcepropname · 隐藏 mserviceapp/mserviceclass | formRule 4ZRHJC9QK0/A | source* / mservice* 显隐 |
| `valsourcetype` = `2` | 显示 mserviceapp + mserviceclass · 隐藏 source* | formRule 4ZRHJC9QK/B1 | source* / mservice* 显隐 |
| `bar_save / bar_saveandnew` 点击 | datatype=enum → checkEnumEntry 4 连校验 → 通过则 clearUnMustData | DynaRuleItemEdit.beforeItemClick L233-L251 | entryentity / 多个清空 |

---

## 六、平台命名规则速查

### 6.1 多语言表 _l 结尾

苍穹平台对 `MuliLangTextField` 的多语言数据存储有两种模式：
- **方案 A · 主表承载**：name 字段直接落 `t_hrcs_dynaruleitem.fname`（仅当前语种 · 切语言查不到其他语种的）
- **方案 B · 独立 _l 子表**：`t_hrcs_dynaruleitem_l` 表 (fpkid, fid, flocaleid, fname, fsimplename, ...) 存所有语种

`hrcs_dynaruleitem` 标品采用 **方案 B**（推断 · 与 dynascheme 一致）· 由 `BdVersionSaveServicePlugin` (kd.bos.base.bdversion) 在 save/submit 阶段自动写入。`entryentity` 的 `displayvalue`（MuliLangTextField）类似走子表 `t_hrcs_dynaruleitemenum_l`。

⚠️ **ISV 加 MuliLangTextField 时不要假设会自动建独立 _l 表**：苍穹平台的 _l 表是元数据建表时根据多语言字段统计自动生成的 · 加新字段 → 后续重新 buildPDM 才会让新字段进 _l 表。

### 6.2 反模式 · 继承场景专属类（DynaRuleItemEdit / DynaItemDeleteOp 不要继承）

`DynaRuleItemEdit`（hrmp-hrcs-formplugin-1.0.jar）和 `DynaItemDeleteOp`（hrmp-hrcs-opplugin-1.0.jar）都是 **场景专属类**：
- 平台未给它们加 `@SdkPlugin` 注解（参考 `cosmic_sdk_annotation_whitelist.md`）· 视为内部 API
- 继承会让 ISV 代码跟标品强耦合：标品改方法签名 / 改私有方法名 → ISV 编译失败
- **正确做法**（PR-001）：ISV 走"并列挂插件"模式 · 父类只能选 `HRDataBaseEdit / HRDataBaseOp / AbstractValidator / AbstractFormPlugin / HRDataBaseList`

```java
// 反模式
public class TdkwDynaRuleItemEdit extends DynaRuleItemEdit { ... }

// 正确模式（PR-001）
public class TdkwDynaRuleItemEdit extends HRDataBaseEdit { ... }
```

`DynaItemDelValidator`（在 `kd.hr.hrcs.opplugin.validator.perm.dyna` · 引用方 DynaItemDeleteOp.java L18）同样**不要继承** —— 平台未加 `@SdkPlugin/@SdkPublic` 注解 · 走内部 API 隔离。

`HRBaseDataLogOp / HRBaseDataStatusOp / HRBaseDataEnableOp / HRBaseOriginalOp / BdVersionSaveServicePlugin / CodeRuleOp / CodeRuleDeleteOp` 都是平台/HR 框架基础类 —— **可以挂在同一 opKey 链上 · 不要继承**。

### 6.3 时序场景说明（HisModel 在本场景不适用）

⚠️ 重要：本场景**不是 HisModel** · 无 boid/iscurrentversion/sourcevid 字段。

PR-008（iscurrentversion=true 过滤）和 PR-009（boid 业务维度）在本场景**不需要使用**。但要**在跨场景查询时区分**：

```java
// 查 dynaruleitem（普通基础资料 · 用 id）
QFilter[] filters = { new QFilter("id", "=", itemId) };
new HRBaseServiceHelper("hrcs_dynaruleitem").queryOriginalOne(...);

// 查 dynascheme（HisModel · 必须 iscurrentversion=true）
QFilter[] filters = {
    new QFilter("boid", "=", schemeBoid),
    new QFilter("iscurrentversion", "=", Boolean.TRUE)
};
new HRBaseServiceHelper("hrcs_dynascheme").queryOriginalOne(...);
```

如果 ISV 写代码时把 dynaruleitem 当 HisModel 处理（加 iscurrentversion 过滤）· QFilter 会查不到任何数据（字段不存在或 null）· 是高危错误。
