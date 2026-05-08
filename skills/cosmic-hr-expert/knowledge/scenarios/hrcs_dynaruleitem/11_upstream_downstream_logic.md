# 上下游联动 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + scene_doc.json + opkeys_index.json 44 opKey
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、上游依赖全景

`hrcs_dynaruleitem` 作为"规则字典"配置表，上游依赖 6 个外部实体 + 1 个自引用：

```
                    ┌──────────────────────────┐
                    │   hrcs_dynaruleitem      │
                    │   (规则参数项 · 本场景)    │
                    └──────┬───────────────────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
   entitytype        sourceentitytype   relatruleparam
   (基础资料类型)      (值来源实体)       (主规则参数项)
          │                │                │
          ▼                ▼                ▼
   bos_entityobject  bos_entityobject  hrcs_dynaruleitem
   (元数据实体)        (元数据实体)       (自引用)
          │
          ▼
   mserviceapp → bos_devportal_bizapp (微服务应用)
   relatpropkey / sourcepropkey → hrcs_choosefield_page (属性选择子页面)
   datatype=org entitytype → haos_adminorghrf7 (HR行政组织F7)
   creator / modifier / disabler → bos_user (平台用户)
```

### 1.1 entitytype（基础资料类型）→ bos_entityobject

| 属性 | 值 |
|---|---|
| **关联方向** | 上游 F7 引用 |
| **refEntity** | `bos_entityobject`（苍穹元数据实体 · 所有元数据形式的主表） |
| **过滤规则** | DynaRuleItemEdit.beforeF7Select 限定 `modeltype = "BaseFormModel"`（只能选基础资料） |
| **联动机制** | datatype=org 时强制写入 `haos_adminorghrf7`（DynaRuleItemEdit.propertyChanged）· datatype!=org 时清空 |
| **ISV 注意** | entitytype 字段 `isvCanModify=false` —— 不能改元数据属性 · ISV 想支持更多组织实体需并列 propertyChanged 覆盖 |

### 1.2 sourceentitytype（值来源实体）→ bos_entityobject

| 属性 | 值 |
|---|---|
| **关联方向** | 上游 F7 引用 |
| **refEntity** | `bos_entityobject` |
| **过滤规则** | DynaRuleItemEdit.beforeF7Select 限定 `modeltype in ("BaseFormModel", "BillFormModel")`（比 entitytype 宽 · 可基础资料 + 单据） |
| **生效条件** | valsourcetype=1（实体取值）时显示 · formRule 4ZRHJC9QK0/A 控制显隐 |
| **联动** | 选了 sourceentitytype 后 · sourcepropname 点击 → 弹 hrcs_choosefield_page 选该实体的属性 |

### 1.3 relatruleparam（主规则参数项）→ hrcs_dynaruleitem（自引用）

| 属性 | 值 |
|---|---|
| **关联方向** | 自引用 F7（本表引用本表） |
| **refEntity** | `hrcs_dynaruleitem`（自己） |
| **过滤规则** | DynaRuleItemEdit.beforeF7Select **双闸过滤**：(1) `isrelatparam = false`（排除已关联的 · 防止链式关联 A→B→C）(2) `datatype in ("bd", "org")`（只允许基础资料和组织类型当主规则参数 · 枚举型不能当主参数） |
| **生效条件** | isrelatparam=true 时显示 · formRule 4ZR5YQFF=KLX 控制 |
| **联动** | 选了 relatruleparam 后 · relatpropname 点击 → 弹 hrcs_choosefield_page 选该参数项 entitytype 的属性（propKey → propName 反查） |

### 1.4 mserviceapp（微服务所在应用）→ bos_devportal_bizapp

| 属性 | 值 |
|---|---|
| **关联方向** | 上游 F7 引用 |
| **refEntity** | `bos_devportal_bizapp`（苍穹开发平台应用注册表） |
| **生效条件** | valsourcetype=2（微服务取值）时显示 · formRule 4ZRHJC9QK/B1 控制 |
| **联动** | 选了 mserviceapp + 填了 mserviceclass 后 · 规则引擎运行时反射调微服务方法取值 |
| **ISV 注意** | mserviceapp 字段 `isvCanModify=false` —— ISV 不能改 F7 过滤逻辑 · 但可以新增微服务应用 |

### 1.5 hrcs_choosefield_page（属性选择子页面）

| 属性 | 值 |
|---|---|
| **关联方向** | 子页面调用（Modal 弹窗） |
| **formNumber** | `hrcs_choosefield_page` |
| **触发方式** | 用户点击 relatpropname / sourcepropname 控件（TextField · 不是 F7 控件） |
| **传参** | `paramEntityName` = 目标实体 number + `param_ifShowForDynaRule` = `"true"`（标记 dynarule 场景 · 字段范围可能受限） |
| **回调** | `closedCallBack` → 按 actionId（`selRelEntityProp` / `selSourceEntityProp`）区分 · 双写 propKey 和 propName 回模型 |
| **数据格式** | `getPrimaryKeyValue()` 返回 `"propKey||propName"` 管道分隔 |

### 1.6 haos_adminorghrf7（HR 行政组织 F7 · 仅 datatype=org 时）

| 属性 | 值 |
|---|---|
| **关联方向** | 硬编码写入（非 F7 选择） |
| **写入时机** | DynaRuleItemEdit.propertyChanged 检测 `datatype="org"` → 自动 `setValue("entitytype", "haos_adminorghrf7")` |
| **实体性质** | haos_adminorghrf7 是 HR 行政组织的 F7 视图实体（HisModel 时序基础资料） |
| **ISV 限制** | 硬编码不可配——ISV 不能通过配置改成其他组织实体 · 必须在 propertyChanged 里并列覆盖 |

### 1.7 bos_user（平台用户 · 创建人/修改人/禁用人）

| 属性 | 值 |
|---|---|
| **关联方向** | 平台自动注入（非用户选择） |
| **字段** | `creator` / `modifier` / `disabler` |
| **注入时机** | 平台在 INSERT/UPDATE 时自动从 `RequestContext.get().getCurrUserId()` 写入 |
| **ISV 注意** | L0 系统级字段 · **不可 setValue** · 手改会破坏审计链 |

---

## 二、下游影响全景

### 2.1 hrcs_dynascheme（动态授权方案）← 核心下游 · 强耦合

| 属性 | 值 |
|---|---|
| **耦合方式** | dynascheme.condition（LargeTextField · DecisionSet JSON）中的 `paramId` 指向本表行 |
| **引用字段** | `condition.conditionList[].paramId` → `hrcs_dynaruleitem.id` |
| **枚举值引用** | datatype=enum 时 `condition.conditionList[].value` → `entryentity.value`（通过 JSON 字符串 · 非外键约束） |
| **加载方式** | dynascheme 打开方案编辑器时 · PermFilter 控件从本表实时 F7 加载可选参数列表（无缓存） |
| **删除保护** | `DynaItemDelValidator` 调 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` 反查 · 被引用则阻断 |
| **deleteentry 保护** | `DynaRuleItemEdit.beforeDoOperation` 解析所有方案 condition JSON · 检查 `conditionList[].value` 命中选中行 |
| **同步/异步** | **同步**（F7 实时查询 · 无 BEC 推送 · 无缓存） |
| **失败策略** | 引用不存在时 condition 评估结果为空——规则静默失效（无报错 · 无日志） |

#### JSON 引用结构（实证 DynaRuleItemEdit.checkRelSchemeVal L253-L270）

```json
{
  "conditionList": [
    {
      "paramId": "1234567890123456789",
      "value": "SALES"
    },
    {
      "paramId": "1234567890123456790",
      "value": "CN"
    }
  ]
}
```

- `paramId` = `hrcs_dynaruleitem.id`（Long 转 String）
- `value` = `entryentity.value`（当 datatype=enum 时）或实体属性的实际值

### 2.2 hbp_dataeditlog（操作日志表）

| 属性 | 值 |
|---|---|
| **耦合方式** | `HRBaseDataLogOp` （beforeExecute 埋点 + afterExecute 落库）—— 所有 opKey 都走 |
| **记录内容** | 操作类型（save/delete/submit/audit/enable/disable）+ 数据 id + 操作人 + 时间 |
| **同步/异步** | **同步**（同事务 · afterExecute 落库） |

### 2.3 t_hrcs_dynaruleitem_n_h（名称历史子表）

| 属性 | 值 |
|---|---|
| **耦合方式** | `BdVersionSaveServicePlugin` —— 只有 name 字段变更时写入 |
| **记录内容** | 变更前后的 name 值 + 版本号 |
| **同步/异步** | **同步**（同事务） |
| **注意** | 这不是 HisModel 时序版本——是平台基础资料通用的"名称多语言版本控制" |

---

## 三、跨模块联动矩阵

| 下游模块 | 联动点 | 同步/异步 | 失败策略 | 是否有 BEC |
|---|---|---|---|---|
| `hrcs_dynascheme`（动态授权方案）| 方案 condition JSON 用 paramId 引用本表 · PermFilter F7 实时加载 | 同步（F7 查询）| 引用不存在 → condition 评估返回空 → 规则静默失效 | **否**（0 处发布） |
| 平台操作日志 | `HRBaseDataLogOp` 写 `hbp_dataeditlog` | 同步（同事务）| 日志写失败不影响主事务 | 否 |
| 名称历史 | `BdVersionSaveServicePlugin` 写 `_n_h` 表 | 同步 | 失败回滚 | 否 |
| 下游 BEC 订阅方 | **无**（标品 0 处发布 BEC · grep 已 cleared）| N/A | N/A | N/A |
| ISV 自建表（如审批配置表）| ISV 自建表 FK 引用 `paramId` | 取决于 ISV 实现 | ISV 自定（推荐 CS-04 Validator 阻断）| ISV 可选（CS-05） |

---

## 四、关键联动路径详解

### 4.1 新增参数项 → 方案引用

```
HR 管理员新建 dynaruleitem (datatype=bd, entitytype=haos_adminorg, isrelatparam=true)
  → 保存 (save)
  → BdVersionSaveServicePlugin 写名称版本历史
  → HRBaseDataLogOp 写操作日志
  → id 生成

HR 管理员打开 dynascheme 编辑方案
  → PermFilter 控件 F7 加载规则参数项列表
  → 实时 SQL: SELECT * FROM t_hrcs_dynaruleitem WHERE ...（无缓存）
  → 用户选刚才创建的参数项
  → 用户填 condition value
  → 保存方案 → condition JSON 写 paramId = 新参数项.id
```

### 4.2 修改枚举行 → 方案引用评估

```
HR 管理员修改 dynaruleitem 的枚举行（新增"华南"= "SOUTH"）
  → save 通过 checkEnumEntry 三连校验
  → 新枚举行 INSERT t_hrcs_dynaruleitemenum

某个方案的 condition.conditionList[0].value = "SOUTH"
  → 规则引擎评估时 用 value "SOUTH" 去匹配员工属性
  → 匹配成功 → 员工命中该动态权限方案

HR 管理员删除枚举行"SOUTH"
  → deleteentry → DynaRuleItemEdit.beforeDoOperation 反查
  → checkRelSchemeVal 解析所有方案 condition JSON
  → 命中 value="SOUTH" → setCancel(true)
  → 不允许删
```

### 4.3 删除参数项 → 阻断链

```
管理员在列表点【删除】
  → delete OP 链
  → CodeRuleDeleteOp (编码清理)
  → HRBaseDataStatusOp (状态校验)
  → HRBaseDataLogOp (日志埋点)
  → DynaItemDeleteOp.onAddValidators
      → DynaItemDelValidator.validate()
          → DynaSchemeServiceHelper.queryRelDynaScheme(itemId)
          → 返回 [scheme1, scheme2, ...] (2 个方案引用了此参数项)
          → addErrorMessage("规则参数项 [XXX] 被 2 个动态授权方案引用，请先解除引用再删除。")
      → setCancel(true) · DB 不删
```

---

## 五、BEC 事件地图

| 事件 | 发布方 | 标品发吗 | ISV 能加吗 | 推荐方案 |
|---|---|---|---|---|
| 规则参数项新增/修改 | 本场景 | **否**（grep 0 命中）| 能（CS-05）| afterExecute + IEventService |
| 规则参数项启用/禁用 | 本场景 | **否** | 能（CS-05）| 同上 |
| 规则参数项删除 | 本场景 | **否** | 能（CS-05）| 同上 |
| 枚举行新增/修改/删除 | 本场景 | **否** | 能（FormPlugin afterDoOperation）| 注意：deleteentry 不走 OP 链 |

**核心结论**：hrcs_dynaruleitem 是 **BEC 沉默场景**——标品不发任何事件。所有跨模块通知需求都需 ISV 自建发布方（CS-05）。

---

## 六、上下游数据一致性检查清单

| 检查项 | 检查方法 | 修复方式 |
|---|---|---|
| dynascheme.condition 中的 paramId 在 dynaruleitem 中是否存在 | `SELECT * FROM hrcs_dynascheme WHERE condition LIKE '%"paramId":"X"%'` vs `SELECT id FROM t_hrcs_dynaruleitem WHERE fid = X` | 如果不存在 → 方案 condition 需要清理或补建参数项 |
| entryentity.value 被方案引用 | `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` 解析 condition JSON | 如果脱钩 → 看 08_impact_analysis.md 处理 |
| entitytype 指向的实体是否存在 | `SELECT * FROM bos_entityobject WHERE fid = ?` | 如果实体被删 → 参数项的 entitytype 需改成有效实体 |
| issyspreset=true 的参数项数据完整性 | OpenAPI list 过滤 `issyspreset = true` | 缺失则需要走 DB 脚本恢复 |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/hr_hrmp_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
