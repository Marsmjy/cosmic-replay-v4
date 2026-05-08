# 上下游联动 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于 scene_doc.json + rules_chain_all.json 44 opKey + form_lifecycle_rules.json 24 rules + curated_sdk.json + CFR 反编译 4 类
> **confidence**: verified（上游 F7 关系来自 scene_doc.json refEntity 实证 · 下游引用链来自 DimensionNewEdit.afterBindData 反查 hrcs_entityctrl 实证 + DimensionDeleteValidator 实证）

---

## 一、上下游全景图

```
                         ┌──────────────────────────────────────┐
                         │  上游基础资料 / 业务对象                │
                         │  ────────────────────                 │
                         │  bos_entityobject （业务对象定义）      │
                         │  每个 entitynum 指向一个 formId        │
                         │  提供字段 schema（EntityMetadataCache） │
                         │                                      │
                         │  hbss_hrbucafunc  （职能类型）         │
                         │  haos_adminorgdetail （行政组织）       │
                         │  bos_org （组织基础资料）               │
                         └─────────────┬────────────────────────┘
                                       │ entitytype/hrbu F7 引用
                                       │ BasedataField · refEntity 实证
                                       ↓
                         ┌──────────────────────────────────────┐
                         │  hrcs_dimension （主场景 · 本文档）     │
                         │                                      │
                         │  主表 t_hrcs_dimension               │
                         │  子表 t_hrcs_dimensionenum (entry)    │
                         │  中间表 t_hrcs_dimorgclass            │
                         │                                      │
                         │  datasource → 4 种模式开关            │
                         │  entitytype → 引用上游业务对象         │
                         │  hrbu → 引用职能类型                  │
                         │  org_classify → 多选组织分类          │
                         └─────────────┬────────────────────────┘
                                       │
           ┌───────────────────────────┼──────────────────────────┐
           │                           │                          │
           ↓ 反查灌 ctrlentry         ↓ 被引用                    ↓ 维度枚举值被消费
           │ (afterBindData)          │ (delete 拦截)             │
           │                           │                          │
  ┌────────────────────┐    ┌──────────────────────┐    ┌────────────────────────────┐
  │ hrcs_entityctrl     │    │ 动态授权方案 + 数据规则 │    │ 角色维度授权               │
  │ ────────────────    │    │ ────────────────      │    │ ──────────────             │
  │ entryentity.dimen-  │    │ hrcs_dynascheme       │    │ hrcs_roledimension         │
  │  sion 引用 dimens-  │    │  .condition JSON      │    │  .dimension 引用维度 id     │
  │  ionId              │    │  内嵌 dimensionId     │    │                             │
  │                     │    │                       │    │ EntityCtrlServiceHelper     │
  │ EDIT 时反查灌到     │    │ hrcs_datarule         │    │  .getRoles(dimensionId)     │
  │ ctrlentry 虚拟分录  │    │  间接引用（通过        │    │  → 锁定 entry.value         │
  │                     │    │  entitynum 关联）     │    │                             │
  └────────────────────┘    └──────────────────────┘    └────────────────────────────┘
```

---

## 二、上游依赖清单（hrcs_dimension 依赖谁）

### 2.1 直接上游（F7 / 引用关系 · scene_doc.json 实抓）

| 上游实体 | hrcs_dimension 字段 | 关系 | 失败影响 |
|---|---|---|---|
| `bos_entityobject` | `entitytype` (BasedataField · refEntity=bos_entityobject) | F7 引用 · 条件必填（datasource=basedata/orgteam 时必填） | 选不到业务对象 → 无法创建 basedata/orgteam 维度 |
| `hbss_hrbucafunc` | `hrbu` (BasedataField · refEntity=hbss_hrbucafunc) | F7 引用 · 条件必填（datasource=hrbu/orgteam 时必填） | 选不到职能类型 → 无法创建 hrbu/orgteam 维度 |
| `bos_org` | `entitytype`（标品强制 · save 时 datasource=hrbu → `setValue("entitytype", "bos_org")`） | 隐式引用 · 非 F7 可选 | save 失败 |
| `haos_adminorgdetail` | `entitytype`（标品强制 · datasource=orgteam → `setValue("entitytype", "haos_adminorgdetail")`） | 隐式引用 · 非 F7 可选 · DimensionNewEdit.showEnumCtrl L195-L198 | 无法创建组织团队维度 |
| `bos_user` | `creator` / `modifier` / `disabler` (CreaterField / ModifierField / UserField · BOS L0/L1 系统字段) | 创建人/修改人/禁用人引用 | 显示为空 · 不影响核心功能 |

### 2.2 entitytype F7 过滤上游

entitytype F7 仅展示 `modeltype = BaseFormModel` 的业务对象（`DimensionNewEdit.beforeF7Select` L364-L372）：

```java
// DimensionNewEdit.java L364-L372 (FP_DNE11)
QFilter filter = new QFilter("modeltype", "=", "BaseFormModel");
lsp.getListFilterParameter().setFilter(filter);
lsp.setFormId("bos_listf7");
```

**为什么**：维度只允许挂在"基础资料形态"业务对象上（BillFormModel / DynamicFormModel 不允许）。这与 hrcs_dynascheme 不同 —— dynascheme 无此限制。

### 2.3 选 entitytype 时的二次校验上游

`DimensionNewEdit.limitBasedataType` (L236-L257) + `handleShowTypeForEntityType` (L205-L215)：

| 校验项 | 依赖 | 失败后果 |
|---|---|---|
| 所选业务对象必须有 `number` + `name` 字段 | 业务对象元数据 EntityMetadataCache | 提示"当前业务对象无编码或名称字段，不允许配置维度"，清空 entitytype |
| showtype=tree 时 entitytype 必须继承 `hbp_bd_treetpl_all` | 平台基础资料模板 | 强制 setValue("showtype", "list") + setEnable(showtype, false) |

---

## 三、下游消费清单（谁依赖 hrcs_dimension）

### 3.1 hrcs_entityctrl · 业务对象维度映射（核心下游 · 反查灌 ctrlentry）

**消费方**：`hrcs_entityctrl` 表单（`entryentity` 分录引用 dimension.id）

**触发时机**：`DimensionNewEdit.afterBindData` (L108-L141) · EDIT 模式进入维度表单时：

```java
// DimensionNewEdit.java L108-L141 (FP_DNE02)
// 反查 hrcs_entityctrl WHERE entryentity.dimension = dimensionId
// 对每行创建 ctrlentry 虚拟分录行并填入 entity/propkey/authrange/propname/issyspreset/ismust/desc
```

**同步/异步**：**同步**（afterBindData 内直查）· 是只读反查显示。

**失败策略**：
- hrcs_entityctrl 查询无结果 → 隐藏 entityctrl 面板（FP_DNE03 · L139-L141）
- hrcs_entityctrl 表不可用 → ctrlentry 为空 · 不影响维度主表保存

### 3.2 hrcs_dynascheme · 动态授权方案（引用维度 ID）

**消费方**：`hrcs_dynascheme.condition` 字段（DecisionSet JSON 内嵌 dimensionId）

**引用方式**：JSON 字符串内嵌 · 非结构化外键。查询只能 LIKE 文本匹配：

```sql
-- 05_data_flow.md 第 7.2 节
SELECT fid, fnumber, fname, fcondition
FROM t_hrcs_dynascheme
WHERE fcondition LIKE CONCAT('%', ?, '%')
  AND fiscurrentversion = '1'
```

**同步/异步**：权限方案运行时**同步**解析 condition JSON → 提取 dimensionId → 加载维度枚举值 → 构建权限条件。

**失败策略**：
- 维度被删 → condition JSON 中的 dimensionId 失效 → 权限方案解析失败 → 可能拒绝所有用户访问
- DecisionSet 无结构化反查 helper（截至 8.0 反编译实证）→ 只能 LIKE 文本匹配找引用

### 3.3 hrcs_datarule · 数据规则（间接引用）

hrcs_datarule 通过 `entitynum` 引用业务对象 · 该业务对象如果也是 dimension 的 entitytype → 形成间接引用链。如果 dimension 限制了该 entitytype 的枚举值 · datarule 的 FilterGrid 条件可能用到这些枚举值。

### 3.4 角色维度授权（entry.value 被锁定）

`EntityCtrlServiceHelper.getRoles(dimensionId)` 返回所有引用本维度的角色：

```java
// DimensionNewEdit.java L160-L168 (FP_DNE05)
// datasource=enum 且 roles.count() > 0 时:
//   对每个 entry 行的 value 字段 setEnable(false)
//   防止破坏已有角色授权
```

**触发时机**：`showEnumCtrl(datasource="enum")` · 数据加载后 + datasource 切换到 enum 时。

**同步/异步**：**同步**（数据加载时直查 EntityCtrlServiceHelper）· 仅 UI 锁定 · 后端不强制。

### 3.5 hrcs_refdetails · 角色引用明细（Modal 跳转）

`DimensionNewEdit.beforeItemClick` L259-L270 (FP_DNE12)：

```java
// refrole 工具栏按钮点击
long dimensionId = this.getModel().getDataEntity().getLong("id");
FormShowParameter fsp = new FormShowParameter();
fsp.setFormId("hrcs_refdetails");
fsp.setCustomParam("dimension.id", dimensionId);
fsp.getOpenStyle().setShowType(ShowType.Modal);
this.getView().showForm(fsp);
```

**只读展示** · 不写数据 · 仅传 dimension.id 给子页面反查哪些角色引用了本维度。

### 3.6 hrcs_rolelist · 角色列表（间接联动）

角色管理中的角色维度配置（`hrcs_roledimension` 表）引用 dimension.id。角色成员的数据权限范围可能受 dimension 枚举值限制。

### 3.7 HR 操作日志（通用 · 所有 opKey）

`HRBaseDataLogOp` 覆盖 hrcs_dimension 的全部 44 个 opKey 的 afterExecute 写操作日志（curated_sdk.json `_usage_note` L374）。

---

## 四、跨模块联动场景

### 4.1 新增维度 → 联动

```
[新建] hrcs_dimension.save
   ↓ datasource=enum → 填 entry 枚举值
   ↓ datasource=basedata → 选 entitytype + showtype
   ↓ datasource=hrbu → entitytype 自动= bos_org
   ↓ datasource=orgteam → entitytype 锁 haos_adminorgdetail
   ↓
[save OP 链] CodeRuleOp → BdVersionSave → HRBaseDataStatusOp
             → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp (6 标品)
   ↓
[落库] t_hrcs_dimension + t_hrcs_dimensionenum (entry) + t_hrcs_dimorgclass (org_classify)
   ↓
[可用] 维度出现在 entitytype F7 候选 · 业务对象维度映射可用 · 角色维度配置可用
```

### 4.2 修改维度枚举值 → 二次确认 + 下游锁定

```
[编辑] datasource=enum 的维度 · 用户改 entry.displayvalue → value 映射
   ↓
[save] beforeDoOperation (FP_DNE17 L292-L294):
   - EDIT 模式 + datasource=enum → checkEnumChange 比对 DB
   - 有变化 → 弹 "调整枚举值会影响角色维度，确定修改吗？"
   ↓
[用户点 Yes] setPageCache("hadConfirm") + invokeOperation("save")
   - 二次进入 beforeDoOperation (FP_DNE15 L281-L285):
   - hadConfirm != null → 移除缓存 → 跳过校验直接放行
   ↓
[下游] 已有角色的维度配置仍引用旧枚举值 → 权限可能偏移
   - ⚠ 标品不做自动同步 · 管理员需手动修角色配置
```

### 4.3 禁用维度 → 二次确认（列表操作）

```
[列表] 点 tbldisable (DimensionList.beforeItemClick L29-L35 · FP_DNL01):
   - showConfirm "禁用维度后，不允许在'业务对象维度映射'中使用。已有的角色维度的数据权限不受影响。"
   - callBackId=disable_conform · evt.setCancel(true)
   ↓
[用户确认] confirmCallBack (FP_DNL02 L37-L44):
   - invokeOperation("disable")
   ↓
[disable OP 链] HRBaseDataLogOp → t_hrcs_dimension.fenable=0
   ↓
[下游] "业务对象维度映射"（hrcs_entityctrl）不能再选此维度
       · 已有角色权限不受影响（标品文案明确）
```

### 4.4 删除维度 → 引用检查拦截

```
[列表] delete 触发
   ↓ DimensionDeleteOp.onAddValidators (FP_DDO01 L19-L21):
   - args.addValidator(new DimensionDeleteValidator())
   ↓
[校验] DimensionDeleteValidator.validate（反编译未读到内部实现 · 推断）:
   - 反查 hrcs_entityctrl.entryentity.dimension
   - 反查 hrcs_dynascheme.condition（LIKE 文本匹配 dimensionId）
   - 反查 hrcs_datarule（间接）
   - 任一引用 → addErrorMessage 阻断
   ↓ 通过校验
[delete OP 链] CodeRuleDeleteOp → HRBaseDataStatusOp → HRBaseDataLogOp
   ↓
[级联删除] t_hrcs_dimension + t_hrcs_dimensionenum + t_hrcs_dimorgclass
```

⚠ **ISV 不要继承 DimensionDeleteOp 来加自己的删除校验** · 走"并列挂新 OP + 自建 Validator + onAddValidators"（PR-001 / PR-010 · 02_business_rules.md 第 7 节）。

### 4.5 entitytype 引用的业务对象被删除 → 维度全部失效

如果 `bos_entityobject` 中被 entitytype 引用的业务对象被删除：
- 维度表单的 entitytype 字段显示空值
- datasource=basedata/orgteam 的维度在 save 时 checkEntitytype (FP_DNE16) 报 "请选择业务对象。"
- hrcs_entityctrl 的 entityctrl 面板无法加载（entitytype 反查失败）
- 使用了该维度的角色 · 权限方案可能解析 condition 失败

### 4.6 跨场景联动总表

| 兄弟场景 | 关系 | 联动点 | 具体行为 |
|---|---|---|---|
| `hrcs_entityctrl` （业务对象维度映射） | 下游**引用** dimension.id | entryentity.dimension 字段 + EDIT 时反查灌 ctrlentry | 维度禁用后不能再在映射中新增使用 · 已有映射不受影响 |
| `hrcs_dynascheme` （动态授权方案） | 下游**引用** dimensionId | condition JSON 内嵌 + DecisionSet 解析 | 维度枚举值变更影响方案权限计算 · 维度删除导致方案失效 |
| `hrcs_datarule` （数据规则） | 间接引用 | entitynum 与 dimension.entitytype 指向同一业务对象时联动 | 维度限制的枚举值可能被数据规则引用 |
| `hrcs_rolelist` （角色管理） | 下游**引用** dimension.id | hrcs_roledimension + EntityCtrlServiceHelper.getRoles | 维度枚举值变更 · 角色维度锁定 UI |
| `bos_entityobject` （业务对象） | 上游**数据源** | entitytype F7 候选 + modeltype=BaseFormModel 过滤 | 业务对象被删 → 维度失效 |

---

## 五、消息与事件机制

### 5.1 BEC 事件

hcs_dimension 作为标品基础资料 · save/delete/disable/enable OP 链中的 `HRBaseDataLogOp` 在 afterExecute 阶段会通过平台层发布 BEC 事件（标品通用行为 · curated_sdk.json L166 `AfterOperationArgs: 事务已提交·发BEC`）。

**ISV 订阅**：需要在 hrcs 应用下创建 ISV OP · 继承 `AbstractOperationServicePlugIn` · 实现 `afterExecuteOperationTransaction` · 监听 dimension 的 save/delete/disable 事件（CS-05）。

### 5.2 版本控制（BdVersionSaveServicePlugin · save/submit 挂载）

维度是标品基础资料 · 有历史版本控制。`BdVersionSaveServicePlugin`（save 链第 2 位）在每次 save/submit 时管理版本号。ISV 不要绕版本控制。

---

## 六、同步 vs 异步总结

| 联动动作 | 同步/异步 | 延迟 | 失败影响 |
|---|---|---|---|
| afterBindData · 反查 hrcs_entityctrl 灌 ctrlentry | 同步 | 0ms | ctrlentry 为空 · 不影响保存 |
| datasource 切换 · showEnumCtrl UI 联动 | 同步（propertyChanged） | 0ms | 字段必填/可见性状态不正确 |
| entitytype F7 · 过滤 BaseFormModel | 同步（beforeF7Select） | 0ms | 错误地显示 BillFormModel |
| save → entitytype= bos_org (hrbu) | 同步（beforeDoOperation） | 0ms | entitytype 未补 → 下游引用失效 |
| save → checkEntitytype 5 项校验 | 同步（beforeDoOperation） | 0ms | 校验失败 → save 被 cancel |
| delete → DimensionDeleteValidator | 同步（onAddValidators 阶段） | 0ms | 引用检查失败 → delete 被 cancel |
| disable · 二次确认 | 同步（beforeItemClick → confirmCallBack → invokeOperation） | 取决于用户确认速度 | 用户取消 → 无操作 |
| edit + enum → checkEnumChange 二次确认 | 同步（beforeDoOperation） | 取决于用户确认速度 | 用户取消 → save 被 cancel |
| save → BdVersionSaveServicePlugin 版本管理 | 同步（OP 链内） | 0ms | 版本号不递增 · 历史版本丢失 |
| BEC 事件发布（ISV 自建扩展） | 异步 | >=100ms | BEC 丢失 · 订阅方收不到 |

---

## 七、ISV 开发注意事项

### 7.1 datasource 是模式开关 · 不可绕过

- `datasource` 字段的 `isvCanModify=false`（scene_doc.json）
- ISV 新建维度时必须选 datasource · 否则 showEnumCtrl 不知道走哪个分支
- 4 种 datasource 模式的字段必填性/可见性矩阵（`02_business_rules.md` 1.3/1.4 节）必须完整复制

### 7.2 扩展维度前先检查下游引用

在 delete 操作中 · `DimensionDeleteValidator` 会拦截有下游引用的维度。但 ISV 新增的下游表（如自建表引用了 dimension.id）不会被标品 Validator 覆盖：

```java
// 建议 ISV 在自定义 Validator 中追加:
// 反查 ISV 自有表是否引用 dimensionId
// HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_isv_table");
// DynamicObject[] refs = helper.query("dimension.id", new QFilter[]{...});
```

### 7.3 entitytype 保存时自动修正 · ISV 不必干预

- datasource=hrbu → save 时自动 `setValue("entitytype", "bos_org")` (FP_DNE14 L278-L280)
- datasource=orgteam → afterBindData 时自动 `setValue("entitytype", "haos_adminorgdetail")` (FP_DNE08 L195-L198)

ISV 不要在 beforeDoOperation 中重复写修正逻辑。

### 7.4 不要继承标品 FormPlugin 类

- **不要**继承 `DimensionNewEdit` / `DimensionList` / `DimensionDeleteOp`（@SdkInternal · ISV 不可继承）
- 扩展方式：**并列挂**新的 `HRDataBaseEdit` / `HRDataBaseList` / `HRDataBaseOp` 子类（PR-001）

### 7.5 枚举值修改的二次确认机制不要在 ISV 代码中绕过

`FP_DNE17` / `FP_DNE18` / `FP_DNE15` 三个规则构成"枚举变更二次确认"闭环：
1. beforeDoOperation: checkEnumChange 检测变更 → pop confirm
2. confirmCallBack: 用户 Yes → setPageCache("hadConfirm") + invokeOperation("save")
3. beforeDoOperation 二次进入: hadConfirm != null → 跳过校验放行

ISV 如果加了 save 前的自定义校验 · 不要用与 `hadConfirm` 冲突的 pageCache key。

### 7.6 ctrlentry 虚拟分录不落库

`ctrlentry`（控权业务对象分录）的数据来自 `t_hrcs_entityctrl.entryentity` 反查 · 是只读的 · **修改 ctrlentry 的值不会保存到数据库**。要修改实体管控配置 · 必须在 hrcs_entityctrl 表单操作。

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

**汇总**：1 个本场景实体 · 共 14 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
