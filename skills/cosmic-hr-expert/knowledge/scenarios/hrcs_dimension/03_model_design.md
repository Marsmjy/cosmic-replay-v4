# 模型设计 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于 `scene_doc.json` (33 字段实抓) + `_auto_inherit_chain.md` + `_auto_plugin_semantics.md` (4 反编译类: DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_dimension.md` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.dimension.*` + `kd.hr.hrcs.opplugin.web.perm.*` (2026-04-28)

---

## ⭐ 关键业务事实 · 一张主表 + 2 张子表 + 1 个虚拟分录

`hrcs_dimension`（维度管理）是 HR 通用服务（hrcs）权限管理域的**基础配置主数据**。一条维度行表示"角色权限分配时可以按照哪个维度（如组织/枚举/基础资料/职能类型）来切分授权"。它是**`hrcs_dynascheme` / `hrcs_datarule` / `hrcs_dynaschemerole` 等下游 4-5 个权限引擎实体的强依赖上游基础资料** —— ISV 改它要谨慎得多。

`hrcs_dimension` 不是单表 + 单分录的常规基础资料 · 而是**1 张主表 + 2 个分录子表 + 1 个虚拟分录（不落库）**：

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_hrcs_dimension` | 主实体 | 维度主体（编码/名称/类型/控权类型/业务对象/职能类型） | `fid` / `fnumber` / `fname` / `fdatasource` / `fauthtype` / `fentitytypeid` / `fbucafuncid` / `fshowtype` |
| `t_hrcs_dimensionenum` | 分录子表 | 枚举值清单（datasource=enum 时的枚举值/枚举名称/顺序/备注） | `fentryid` / `fvalue` / `fdisplayvalue` / `findex` / `fdescription` |
| `t_hrcs_dimorgclass` | 分录子表 | 组织分类配置（datasource=orgteam 时挂哪些组织分类 · MulBasedataField 中间表） | `fpkid` / 关联 `forg_classifyid` |

**虚拟分录 `ctrlentry` · 不落库（重要）**：`DimensionNewEdit.afterBindData` (L113-L138) 显示：`ctrlentry`（控权业务对象分录）数据**不落 t_hrcs_dimension 自身**，而是从**`t_hrcs_entityctrl.entryentity`** 反查关联，按 `entryentity.dimension = id` 过滤后，**手动 createNewEntryRow 灌进当前页面的 ctrlentry**。这是 dimension 跟 entityctrl（业务对象维度映射）的"交叉显示"机制。ISV 不要假设 ctrlentry 是 dimension 自身的真实数据子表。

⚠️ **`org_classify` 字段是 MulBasedataField**：`scene_doc.json` 显示其 physicalColumn 为 `t_hrcs_dimorgclass（子表）` —— 苍穹平台 MulBasedataField 默认建独立中间表存多对多关系（不在 `t_hrcs_dimension` 主表）。

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"基础资料类列表"形态（与 dynascheme 同 BillFormModel 形态）。`hrcs_dimension` 列表页由**两到三层独立元数据**组成，Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BillFormModel | `hrcs_dimension`（主实体 · 菜单直挂） | 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据） | ⚠ 待探针确认（推测 `hrcs_dimensionlist` 类） | 列表 UI 壳 + UI 层动作按钮（tbldisable 二次确认 / refrole 跳转角色清单） |
| **F7 列表模板** | 动态表单（独立元数据） | ⚠ 待探针确认 · dimension 作为基础资料常被 dynascheme 等引用 | F7 选择时的列表壳（如有） |

**插件挂载职责分工**：
- **数据层过滤 / 权限 / setFilter** → 挂 `hrcs_dimension`（数据实体 · 当前 `DimensionList` 就挂这里）
- **列表外壳 / tbldisable 二次确认按钮 UI** → 挂列表表单模板（`DimensionList.beforeItemClick` 实证此层）
- **F7 列表过滤** → 在调用方 FormPlugin 的 `beforeF7Select` 写过滤条件（`DimensionNewEdit.beforeF7Select` 已实证：F7 entitytype 时强制 `modeltype = "BaseFormModel"` 过滤）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`） + `tablist`/`treelist` 三层模型。

---

## 二、继承链 · BillFormModel 单据形态（无 HisModel）

### 2.1 ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "BillFormModel"`。这与 hjm/admin_org 的 BaseFormModel 不同 —— 维度管理是**单据**形态（含 `submit/audit/unaudit/disable/enable` 工作流），但**无 HisModel 时序**（`grep -E "iscurrentversion|HisModel|boid"` 在反编译/scene_doc **0 处命中**）。

### 2.2 InheritPath 实证

`_auto_inherit_chain.md` 实抓（数据源：`getFormMetadata.data.InheritPath`）：

```
L0 · 1942c188000065ac          （顶层 bos 模板 · 平台层）
L1 · hbp_bd_tpl_all (2+QE4JA9QV27)  HR基础资料全页面模板
L2 · hrcs_dimension（自身）
```

**对比 hrcs_dynascheme 的 4 层（含 hbp_histimeseqtpl 时序父）**：dimension **没有**继承时序模板 · 因此**不带 boid / iscurrentversion / sourcevid / firstbsed 等时序字段**。

### 2.3 字段层级分类

苍穹元数据字段分 4 层（与 admin_org/hjm 一致 · scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | bos_basetpl + HR 父模板 | `number` / `name` / `simplename` / `description` / `status` / `enable` / `index` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` | 🔒 / ⚠️ 多数不改 · 业务自建可改 |
| **L3** 业务字段 | hrcs_dimension 自身 | `datasource` / `entitytype` / `authtype` / `isadminorg` / `isorg` / `showtype` / `hrbu` / `org_classify` / `entry` / `ctrlentry` | ⚠️ 谨慎改（涉及核心权限引擎语义） |

⚠️ **关键认知**（与 dynascheme 不同）：
- dimension **没有 L2 时序层** · 所有改动**直接落库** · 没有版本回溯
- dimension 改 `datasource` 字段会**触发 5+ 联动规则**（DimensionNewEdit.showEnumCtrl + propertyChanged · 见下文）· 比改 dynascheme.condition 影响更大
- `entitytype`、`hrbu`、`showtype` 等字段值有**对 entitytype 的硬约束**（限定 modeltype=BaseFormModel · 必须有 number/name 字段 · 树形显示需继承 hbp_bd_treetpl_all）

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 33 个字段）

### 3.1 主表业务核心字段（27 个 · 标品锁死的基础字段先标 ⚠）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌ | ✅ | CodeRuleOp 自动生成（PR-006） |
| `name` | MuliLangTextField | 名称 | ❌ | ✅ | 多语言 · 主表承载（无独立 _l 表） |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | - |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | - |
| `status` | BillStatusField | 数据状态（A 暂存 / B 已提交 / C 已审核） | ❌ | ⚠️ 黄区 | 工作流驱动 |
| `enable` | BillStatusField | 使用状态（0 已禁用 / 1 已启用 / 10 启用中） | ❌ | ⚠️ 黄区 | enable / disable 操作管理 |
| `index` | IntegerField | 排序号 | ❌ | ✅ | - |
| **`datasource`** | **ComboField** | **类型** ⭐⭐⭐ | **✅** | 🔒（isvCanModify=false） | **核心字段** · 取值 `enum` / `basedata` / `hrbu` / `orgteam` |
| `entitytype` | BasedataField | 业务对象 | ⚠️ 动态 | ✅ | → `bos_entityobject`（datasource=basedata/orgteam 时必填） |
| `authtype` | ComboField | 控权类型 | ❌ | ✅ | - |
| `isadminorg` | CheckBoxField | 是否行政组织维度 | ❌ | ✅ | - |
| `isorg` | CheckBoxField | 是否组织维度 | ❌ | ✅ | - |
| **`showtype`** | **ComboField** | **显示类型** ⭐ | **✅** | 🔒（isvCanModify=false） | 取值 `list` / `tree` / `checkbox` 等 |
| `hrbu` | BasedataField | 职能类型 | ⚠️ 动态 | ✅ | → `hbss_hrbucafunc`（datasource=hrbu 时必填） |
| `org_classify` | MulBasedataField | 多选组织分类 | ⚠️ 动态 | ✅ | datasource=orgteam 时必填 · 落 `t_hrcs_dimorgclass` 中间表 |

### 3.2 系统派生字段（红区 · ISV 不改）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `creator` | CreaterField | 创建人 |
| `modifier` | ModifierField | 修改人 |
| `createtime` | CreateDateField | 创建时间 |
| `modifytime` | ModifyDateField | 修改时间 · ⚠️ DimensionNewEdit.beforeDoOperation L277 在 save 时主动重置为 `new Date()` |
| `masterid` | MasterIdField | 主数据内码 |
| `issyspreset` | CheckBoxField | 系统预置标记 |
| `disabler` / `disabledate` | UserField / DateTimeField | 禁用人 / 禁用时间 |
| `initdatasource` | ComboField | 数据来源（标品/出厂/手建） |
| `orinumber` / `oristatus` / `oriname` | TextField / ComboField / MuliLangTextField | 出厂值族（用于 issyspreset=true 行的还原） |

### 3.3 分录子表 · 枚举值（datasource=enum 时启用）

| 字段全名 | 类型 | 业务含义 | 必填 |
|---|---|---|---|
| `entry` | EntryEntity | 枚举值清单容器 | - |
| `entry.value` | TextField | 枚举值（如 "P5" / "Beijing"） | ✅（DimensionNewEdit.showEnumCtrl L169-L170 · datasource=enum 时强制必填） |
| `entry.displayvalue` | MuliLangTextField | 枚举名称 | ✅（同上） |
| `entry.entryindex` | IntegerField | 顺序 | ❌ |
| `entry.enumdescription` | MuliLangTextField | 备注 | ❌ |

### 3.4 虚拟分录 · 控权业务对象（不落库 · 反查显示）

| 字段全名 | 类型 | 业务含义 |
|---|---|---|
| `ctrlentry` | EntryEntity | 控权业务对象分录（**虚拟分录** · 不在 t_hrcs_dimension） |
| `ctrlentry.entity` | TextField | 业务对象 entityNumber（来源 entitytype.number） |
| `ctrlentry.propkey` | TextField | 字段 key |
| `ctrlentry.propname` | MuliLangTextField | 字段名 |
| `ctrlentry.authrange` | TextField | 控权范围 |
| `ctrlentry.issyspreset1` | CheckBoxField | 是否预置 |
| `ctrlentry.ismust` | CheckBoxField | 是否必须 |
| `ctrlentry.desc` | MuliLangTextField | 备注 |

⚠️ ctrlentry 数据来源：`DimensionNewEdit.afterBindData` 反查 `hrcs_entityctrl.entryentity` 表（`QFilter("entryentity.dimension", "=", dimensionId)`）后手动 createNewEntryRow 填入。**ISV 不要 setValue ctrlentry · 不会落 dimension 表**。

---

## 四、`datasource` 字段：5 路联动 · dimension 全模型核心

`datasource` 是 dimension 整张表最重要的"模式开关"字段。`DimensionNewEdit.showEnumCtrl` (L145-L203) + `propertyChanged` (L217-L234) 实证：

### 4.1 datasource 取值 4 种 · UI 切换矩阵

| datasource 取值 | dimensionenum 显示 | entitytype 必填 | hrbu 必填 | org_classify 显示+必填 | showtype enable | 自动 setValue |
|---|---|---|---|---|---|---|
| `enum` 枚举 | ✅ 显示 entry 分录 | ❌ 不必填 | ❌ 不必填 | ❌ 不显示 | - | hrbu 清空 · entry value/displayvalue 必填 |
| `basedata` 基础资料 | ❌ 隐藏 + 清 entry | ✅ 必填 | ❌ | ❌ 不显示 | ❌ 强制 disable | hrbu 清空 |
| `hrbu` 职能类型 | ❌ 隐藏 + 清 entry | ❌ | ✅ 必填 | ❌ 不显示 | ✅ enable | entitytype 设 `bos_org` (L279) |
| `orgteam` 组织团队 | ❌ 隐藏 + 清 entry | ✅ 必填 | ✅ 必填 | ✅ 显示+必填 | - | entitytype 强制 `haos_adminorgdetail` (L196) |

### 4.2 联动顺序（`DimensionNewEdit.propertyChanged`）

```
用户改 datasource → propertyChanged → showEnumCtrl(newValue) → 上表 UI/必填重置
用户改 entitytype（datasource=basedata 时）→ propertyChanged
                                     → limitBasedataType (L236-L257)：检查业务对象有没有 number 和 name 字段 · 没有则清空
                                     → handleShowTypeForEntityType (L205-L215)：检查是否继承 hbp_bd_treetpl_all · 不继承则强制 showtype=list 且 disable
```

### 4.3 enum 枚举值已被引用时的保护

`DimensionNewEdit.showEnumCtrl` L160-L168（datasource=enum 路径）：

```java
DataSet roles = EntityCtrlServiceHelper.getRoles(dimensionId);    // 反查角色绑定该维度的清单
int count = roles.count("id", true);
if (count > 0) {
    // 已被角色引用 · entry value 字段全部 setEnable(false) · 不允许改值（避免下游脏数据）
    for (int index = 0; index < entrySize; ++index) {
        this.getView().setEnable(Boolean.FALSE, index, new String[]{"value"});
    }
}
```

⚠️ **ISV 修改 dimension 表时必须考虑**：枚举值若被角色引用 · 改其 value 会让下游 hrcs_dynascheme.condition 里的"枚举值参数引用"失效。

### 4.4 save 时的二次保护（DimensionNewEdit.checkEnumChange）

`DimensionNewEdit.checkEnumChange` (L308-L324)：在 EDIT 状态 + datasource=enum 时 · 比对当前 model.entry 跟 DB.entry · 如有 displayvalue 对应的 value 改了 · 弹"调整枚举值会影响角色维度·确定修改吗？"二次确认（保留 hadConfirm 标记跳过下次）。

⚠️ **ISV 自建 OP 替代默认 save 时 · 必须保留 checkEnumChange 类逻辑** · 否则会破坏角色维度数据一致性。

---

## 五、共用物理表分析（区分键）

`hrcs_dimension` **不与其他 form 共用主物理表 `t_hrcs_dimension`**（标品里没看到平行 form 共表写入此表的场景）。但它**通过 id 跟下列下游表强耦合**：

| 下游表 | 关系 | 区分键 |
|---|---|---|
| `hrcs_entityctrl.entryentity` | 业务对象-维度映射分录（"entityctrl 为业务对象 X 配的维度 Y" 的关系） | `entryentity.dimension = dimensionId` |
| `hrcs_dynascheme.condition` | 动态授权方案的规则条件（DecisionSet JSON 中引用 dimension 的 id 作为参数） | DecisionSet JSON 内嵌 dimensionId |
| `hrcs_dynaschemerole` | 动态方案角色清单（角色权限维度配置） | 间接引用（先到 dynascheme · 再链式到 dimension） |
| `hrcs_datarule` | 数据规则（按维度切分数据访问范围） | 直接 dimension F7 引用 |
| `perm_role` 角色清单 | 角色配置带"角色维度"概念 · 间接关联 dimension | 通过 entityctrl + dimension 链路 |

⚠️ **dimension 的禁用/删除前提**（`DimensionList.beforeItemClick` + `DimensionDeleteValidator`）：
- `disable`：列表点禁用 · 弹"禁用维度后·不允许在'业务对象维度映射'中使用·已有的角色维度的数据权限不受影响"二次确认（`DimensionList.beforeItemClick` L31-L34）· 用户确认后才走 disable opKey
- `delete`：DimensionDeleteOp 注册 DimensionDeleteValidator（具体校验内容反编译只看到注册 · 推断为反查 entityctrl/dynascheme 等下游引用 · 详见 04 业务流转）

---

## 六、Plugin 链概览（17 标品 + ISV 未挂）

完整 20 plugin 清单见 `_auto_plugin_registry.md`。这里只列对 ISV 扩展最关键的层次：

```
preOpenForm  (3 个)
  3. HRBaseDataTplEdit         (HRDataBaseEdit · 标品)
  6. HRAdminStrictPlugin       ⭐ HR 领域管理员准入闸 · 非 HR 管理员直接拒
  10. HRBaseDataTplList

beforeBindData (2 个)
  10. HRBaseDataTplList
  11. HRBasedataLogList

afterBindData (3 个)
  3. HRBaseDataTplEdit
  5. HRHiesButtonSwitchPlugin  · HIES 导入导出按钮切换
  7. DimensionNewEdit           ⭐ 主 FormPlugin · ctrlentry 反查 + showEnumCtrl 切换

beforeDoOperation (3 个)
  3. HRBaseDataTplEdit
  7. DimensionNewEdit            ⭐ save 时 modifytime 重置 + checkEntitytype + checkEnumChange
  11. HRBasedataLogList

beforeItemClick (2 个)
  7. DimensionNewEdit            ⭐ refrole 跳转 hrcs_refdetails 子页面
  12. DimensionList              ⭐ 列表 tbldisable 二次确认

propertyChanged (1 个)
  7. DimensionNewEdit            ⭐ datasource/entitytype 变更联动（showEnumCtrl/handleShowTypeForEntityType/limitBasedataType）

registerListener (1 个)
  7. DimensionNewEdit            ⭐ 注册 entitytype BeforeF7Select 监听

beforeExecuteOperationTransaction (4 个)
  15. HRBaseDataStatusOp
  16. HRBaseDataLogOp
  17. HRBaseDataEnableOp
  18. HRBaseOriginalOp

afterExecuteOperationTransaction (1 个)
  16. HRBaseDataLogOp           · 操作日志落库

onAddValidators (2 个)
  15. HRBaseDataStatusOp        · 标品单据状态校验
  20. DimensionDeleteOp          ⭐ 注册 DimensionDeleteValidator
```

---

## 七、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/hjm/hbpm/dynascheme）保持一致

### 7.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。`hrcs_dimension` 的 MuliLangTextField 字段（`name`/`simplename`/`description`/`oriname` 主表 · `entry.displayvalue`/`entry.enumdescription` 子表）目前在主表 + 子表自身承载多语言（无独立 `_l` 表 · 这是标品配置 · 不是默认行为）。**ISV 扩展 MuliLangTextField 字段时不要假设会自动有 _l 表**。

### 7.2 反模式 · 继承场景专属类（**全部不要继承**）

| 场景专属类（**禁继承**） | 为什么不能继承 | 推荐做法 |
|---|---|---|
| `DimensionNewEdit` | hrcs 维度场景专属 FormPlugin · ISV 改 dimension 形态会引发 datasource 联动逻辑全部错位 · 标品升级方法签名一动 ISV 直接挂 | 并列挂新 FormPlugin · 继承 `HRDataBaseEdit` · 不 super 直接独立业务 |
| `DimensionList` | hrcs 维度列表场景专属 · 含 tbldisable 二次确认 + disable_conform callBackId 私有 | 并列挂新 ListPlugin · 继承 `HRDataBaseList` |
| `DimensionDeleteOp` | hrcs 维度删除 OP 专属 · 注册了 DimensionDeleteValidator（标品反查下游引用规则） | 并列挂新 OP · 继承 `HRDataBaseOp` · 自建 Validator 也走 onAddValidators 注册 |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸（grep 反编译实证 · 复用面广） | 配置即可 · 直接复用 · ISV 不要继承（继承 = 改了所有 hrcs 表单的准入闸） |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs · 不在 HR SDK 白名单） | 用 `HRDataBaseOp` 代替 |

参考 `_shared/platform_rules.json` PR-001 · `cosmic_hr_sdk_whitelist_audit.md` · `cosmic_sdk_annotation_whitelist.md`。

### 7.3 列表三层模型（tablist|treelist）

`hrcs_dimension` 是 BillFormModel 单据 · 列表挂在数据实体上（不是 tablist/treelist 层）。Claude 做列表类 CS（如 setFilter / 自定义按钮）时 · 看反编译 `DimensionList extends HRDataBaseList` · 这是数据层。如果要做 UI 外壳定制 · 需要找列表表单模板（待探针确认 form 是否独立）。

### 7.4 HisModel 时序场景 · **本场景不适用**

> grep `iscurrentversion|HisModel|boid` 在 `scene_doc.json` + `_decompiled/scenarios/hrcs_dimension/*.java` 全 0 命中

`hrcs_dimension` 是**纯单据形态**（status/enable 字段控制工作流和使用态）· **没有 HisModel 时序**：
- ❌ 没有 `boid` / `iscurrentversion` / `sourcevid` / `firstbsed` / `bsed` 等时序字段
- ❌ 没有 `confirmchange` opKey · 没有"审核-变更-双写"机制
- ❌ 没有时序基础资料的"过去版本保留"

→ **ISV 改 dimension 是直接改 · 没有版本回溯**：写 dimension 时不要套用 dynascheme/admin_org/hjm 的 HisModel 套路。这是和 dynascheme 最大的区别 · 反而像 admin_org_quick_maintenance 那种轻量基础资料 · 但带 status/enable 工作流。

PR-008 / PR-009 在本场景**不适用**。

### 7.5 PR 引用速查

本场景定制必引：
- **PR-001** 并列挂不继承 · 是核心铁律（`DimensionNewEdit` / `DimensionList` / `DimensionDeleteOp` 全部不继承）
- **PR-002** 插件 RowKey 顺序（同一 opKey 下父级模板插件先 → 子实体 ISV 插件后；ISV 扩展元数据可插在标品之前）
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环（`DimensionNewEdit.afterBindData` L117 / L137 已用此模式）
- **PR-005** ID 生成用 kd.bos.id.ID（自建分录子表行 id 时强制 · 不要 UUID）
- **PR-006** CodeRuleOp 是平台模板插件 · 业务自助配（dimension save 已挂 CodeRuleOp · ISV 不要再写 number 生成）
- **PR-007** 预置数据（issyspreset）不可改 · 业务自建可改
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验

**不引**：PR-008（iscurrentversion 过滤）/ PR-009（boid 不用 id）/ PR-011（BEC · 标品 0 处发布 · 详见 06 CS-05）。

---

## 八、模型层对外 API（ServiceHelper 反编译实证）

| ServiceHelper | 关键方法 | 用途 |
|---|---|---|
| `EntityCtrlServiceHelper` | `getEntityFieldMap(DynamicObject entitytype)` / `getRoles(long dimensionId)` | 反查业务对象的字段映射 + 反查使用该维度的角色清单 · DimensionNewEdit 强依赖 |
| `HRBaseServiceHelper` | `query("entitytype,entryentity.dimension,...", new QFilter[]{filter})` / `queryOne(...)` | HR 基础资料服务 · 首选（DimensionNewEdit 用此查 hrcs_entityctrl + 自查 hrcs_dimension） |
| `EntityMetadataCache` | `getDataEntityType(formId).getInheritPath()` / `.getProperties()` | 苍穹元数据缓存 · 用于检查业务对象继承路径（限制 showtype=tree 必须继承 hbp_bd_treetpl_all） |
| `HRStringUtils` | `equals(a, b)` / `isEmpty(s)` / `isNotEmpty(s)` | HR 字符串工具（DimensionNewEdit 大量用） |
| `HRAdminService` | `isHrAdmin()` | HR 领域管理员判定（HRAdminStrictPlugin 调用） |

⚠️ ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。

---

## 九、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加自定义业务字段（如"维度备注分类"） | modifyMeta add field 到主实体 hrcs_dimension · ISV 前缀 | 低 |
| 加分录字段（在 entry 加"枚举值标签"） | modifyMeta add field 到子实体 entry · ISV 前缀 | 中（要对齐 datasource=enum 模式） |
| 监听 disable 后做下游清理 | ISV 自建 OP 挂 disable 的 afterExecuteOperationTransaction（PR-010） | 中（要确认下游清不清） |
| save 前业务校验（如"维度名称不能跟 dynascheme 重名"） | ISV 自建 Validator 挂 onAddValidators | 低 |
| 列表 setFilter 增加权限过滤 | ISV 自建 ListPlugin 挂 hrcs_dimension 列表 | 低 |
| 共用 HRAdminStrictPlugin 准入闸 | 配置即可 · 不要修改 | 0 |
| 改 datasource 联动逻辑（如"加 datasource=position 选项"） | ❌ 不推荐 · 改了 DimensionNewEdit 的 5 路联动逻辑 · 标品升级直接挂 | 高 |
| 改 ctrlentry 反查机制 | ❌ 不推荐 · ctrlentry 不落库 · 改了等于改 hrcs_entityctrl 体系 | 高 |
| 替换 EntityCtrlServiceHelper.getRoles 反查逻辑 | ❌ 不推荐 · 标品依赖 · 改了影响 dimensions 全场景 | 高 |

详见 `06_customization_solutions.md`。
