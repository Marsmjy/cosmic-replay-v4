# 业务规则 + 可变性 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于反编译 3 类 + scene_doc.json 字段 invariants + listRules 实抓 3 条 + platform_rules 11 PR
> **可变性分类**: 🔒 硬编码 / ⚙️ 配置 / 🔌 插件可改 / 📝 元数据可改
> **confidence**: verified

---

## 一、6 条本场景核心不变式（INV）

> 这些约束**直接来自标品反编译 + 标品 formRule** · 违反会导致数据不一致或下游异常。

### INV-CS-01 · `orgchangetype` 必填 🔒

- **规则**：保存时 `orgchangetype` 不能为空（required=true · BasedataField → haos_orgchangetype）
- **可变性**: hardcoded（来自 scene_doc.json `(main).orgchangetype.required=true`）
- **实现**：标品 BasedataField 平台层校验 · save 链 onAddValidators 通过 numberValidator/标品校验拦
- **违反后果**：保存 → 用户看到红色提示 "变动类型 不能为空"
- **不要扩展**：不要在 ISV 插件里写"自定义必填校验" · 平台已强制

### INV-CS-02 · `changeoperat` 由 `orgchangetype` 联动反填 🔌

- **规则**：用户改 orgchangetype 时 · changeoperat 自动按 ChangeSceneServiceHelper.getChangeOperate 算出
- **可变性**: pluggable（标品逻辑 · 但 ISV 可以覆盖前/后做联动追加）
- **实现**：
  - 表单层：`ChangeSceneEditPlugin.propertyChanged` 实现（见 04 §3.2）
  - OP 层：`ChangeSceneSaveOp.beginOperationTransaction` 仅在 importtype!=null 兜底
- **对应标品 formRule**：`2=L0QS6DW0M4 · 变动类型影响变动操作操作 · enabled=True`（listRules 实抓）
- **设计意图**：业务规定"每种变动类型默认配套一种主操作" · 这是字典间的强语义绑定
- **不要绕过**：不要在 onAddValidators 写"changeoperat 必填"校验 · 它由 type 联动 · 用户根本看不到该字段

### INV-CS-03 · 出厂数据 (issyspreset=true) 关键字段不可改 🔒

- **规则**：以下字段在 issyspreset=true 的行 · isvCanModify=false：
  - `number` / `orgchangetype` / `orinumber` / `oriname` / `oristatus` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `disabler` / `disabledate` / `initdatasource` / `issyspreset`
- **可变性**: hardcoded（scene_doc.json `isvCanModify=false`）
- **对应标品 formRule**：`2=JQG9AHBTT+ · 系统预置数据不可修改 · preCondition=issyspreset = true · enabled=False`（listRules 实抓 · 当前**未启用** · 但平台层依然按 isvCanModify 拦）
- **违反后果**：在【开发平台】尝试 modifyMeta 改这些字段 → 静默忽略 / 平台升级覆盖
- **业务原因**：这些是平台/标品内置字典 · 被很多地方引用（如 1010 ADMINISTRATIVE / 1100_S 子集团 / 1070 隐藏项）
- **遵循 PR-007**：预置数据编码不可改 · 仅业务自建数据可改

### INV-CS-04 · 列表层强制隐藏 id=1070L 🔒

- **规则**：列表查询时硬编码 `id != 1070L`
- **可变性**: hardcoded（`ChangeSceneListPlugin.java:32`）
- **业务意图**：标品保留 1070 主键作为某种"内部模板项" · 不让用户在列表点开
- **影响**：通过列表入口看不到 · 但通过 F7（走平台 bd_basedata 列表）仍能选到（注意：F7 选择不走 ChangeSceneListPlugin）
- **不能改**：覆盖会破坏标品业务约定 · ISV 自建过滤应在自己列表插件里加

### INV-CS-05 · 搜索 orgchangetype.name 时排除 orinumber=1100_S 🔒

- **规则**：filterColumnSetFilter 触发时 · 给 customQFilters 加 `orinumber != "1100_S"`
- **可变性**: hardcoded（`ChangeSceneListPlugin.java:42`）
- **业务意图**：1100_S 是子集团专用变动场景 · 不让普通组织搜索
- **影响**：仅在搜索 orgchangetype 字段时生效 · 其他过滤路径正常

### INV-CS-06 · 删除/禁用没有反向引用校验 ⚠

- **规则**：标品 delete / disable 链**没有**反向引用前置校验
  - delete 链 4 个插件 · 都不查 homs_orgbatchchgbill 是否引用过
  - disable 链 2 个插件 · 同上
- **可变性**: pluggable（标品缺失 · CS-02 通过 ISV 加补）
- **后果**：详见 05 §5 删除路径的数据风险
- **正确做法**：CS-02 加 ISV 校验插件 · 在 onAddValidators 注册 Validator · 走 refentity_reverse 查反向引用

---

## 二、标品 formRule（3 条 · listRules 实抓）

> 数据源：`_auto_rules_deep.md` · OpenAPI listRules(formNumber=haos_changescene) 实抓

| ruleId | enabled | preCondition | description | 解读 |
|---|---|---|---|---|
| `2=JQG9AHBTT+` | ❌ | `issyspreset = true` | 系统预置数据不可修改 | 当前未启用 · 但 isvCanModify=false 配合元数据已经拦了 |
| `2=L0QS6DW0M4` | ✅ | `true` | 变动类型影响变动操作操作 | **本表灵魂规则** · 跟 ChangeSceneEditPlugin/ChangeSceneSaveOp 配合实现 type→operat 联动 |
| `37IJ/XY669PB` | ❌ | `id = 1110L` | 组织修订不可修改名称 | 1110L 是"组织修订"主键 · 该项的 name 字段不可改（当前未启用 · 估计是历史规则 · 但业务上 1110_S 的 name 也是出厂保护的）|

> ⚠ 这 3 条 formRule 其中 2 条 enabled=False · 实际生效逻辑全靠 java 反编译类（ChangeSceneEditPlugin / ChangeSceneListPlugin / ChangeSceneSaveOp）。
> ISV 不要把 formRule 当作"只看这里就够了"的事实源 · 必须配反编译类双向印证。

---

## 三、字段可变性矩阵（31 字段）

> ⚙️ 配置 · 📝 元数据 · 🔌 插件 · 🔒 硬编码

### 3.1 业务字段（4 个 · 可改）

| 字段 | 可变性 | 修改方式 | 风险 |
|---|---|---|---|
| `orgchangetype` | 🔒 必填 + 🔌 联动 changeoperat | 用户在表单选 | 低（受 BasedataField 平台层校验）|
| `otclassify` | 🔌 列表 customParam 默认 1010 | 用户在表单选 / 跳转传参 | 低 |
| `changereason` | 📝 多选 | 用户多选 / API 写子表 | 低 |
| `changeoperat` | 🔌 由 orgchangetype 联动 · 不让用户手填 | ChangeSceneEditPlugin/ChangeSceneSaveOp 自动 | ⚠ 中（绕过联动会语义异常）|

### 3.2 标识字段（4 个 · 创建后特殊管控）

| 字段 | 可变性 | 修改方式 |
|---|---|---|
| `number` | 🔌 创建后部分可改（但出厂 issyspreset=true 行不可改）| 标品配 CodeRule 自动生成 · 用户可改业务自建数据的编码 |
| `name` | 📝 多语言可改 | t_haos_changescene_l |
| `simplename` | 📝 多语言可改 | 同上 |
| `description` | 📝 多语言可改 | 同上 |

### 3.3 状态字段（5 个 · 流程驱动）

| 字段 | 可变性 | 修改方式 |
|---|---|---|
| `status` | 🔌 走 submit/audit/unsubmit/unaudit 4 操作 | BaseDataSubmitPlugin/BaseDataAuditPlugin 标品 |
| `enable` | 🔌 走 disable/enable 2 操作 | BaseDataDisablePlugin/BaseDataEnablePlugin 标品 |
| `disabler` | 🔒 系统自动写 | disable 操作 |
| `disabledate` | 🔒 系统自动写 | disable 操作 |
| `index` | 📝 排序号 · 用户可改 | 平台默认 |

### 3.4 多组织字段（4 个 · 平台多组织默认）

| 字段 | 可变性 | 备注 |
|---|---|---|
| `createorg` / `org` / `useorg` / `srccreateorg` | ⚙️ 平台多组织设置默认 | OrgField 平台行为 |

### 3.5 系统字段（14 个 · 全部 🔒 硬编码或 🔒 系统维护）

参考 03 §3.3 / §3.5 / §3.6 · ISV 不可改 · 平台维护。

---

## 四、可变性场景化案例

### 案例 A：可改 ⚙️ 配置 · 不需要写代码

**场景**：业务方说"我们集团希望默认看到'子集团'的变动场景"
**判断**：otclassify 默认 1010L 是 ListPlugin 硬编码
**错误做法**：去改 ChangeSceneListPlugin 源码（标品 jar · ISV 改不动）
**正确做法**：CS-04 列表过滤定制 · 通过菜单配置传入 customParam.otclassify

**场景**：业务方说"编码规则要按组织前缀生成"
**判断**：CodeRuleOp 是平台模板插件（PR-006）
**错误做法**：自定义 OP 写编码逻辑
**正确做法**：在【编码规则基础资料】配置即可 · 不写代码

### 案例 B：📝 元数据可改 · 改 modifyMeta

**场景**：业务方说"在变动场景上加 '所属业务线' 字段"
**判断**：可以加 ISV 前缀字段 · 出厂数据扩展（issyspreset=true 行只是不能改 number/orgchangetype 等关键字段 · ISV 加新字段是 OK 的）
**正确做法**：CS-01 加自定义字段 · 走 modifyMeta(op=add field) + ISV 前缀

### 案例 C：🔌 插件可改 · 必须加 ISV 插件

**场景**：业务方说"删除变动场景前先校验有没有 homs_orgbatchchgbill 引用过"
**判断**：标品没有这个校验 · INV-CS-06 描述的缺陷 · 必须 ISV 实现
**正确做法**：CS-02 加禁删校验插件

**场景**：业务方说"我们公司有个特殊的'临时调整'类型 · 默认带特殊原因"
**判断**：orgchangetype → changeoperat 联动是 ChangeSceneServiceHelper 内部逻辑 · 不是字典配置
**正确做法**：CS-03 字段联动定制 · 在 ChangeSceneEditPlugin 之后挂自己的 propertyChanged 插件 · super 调用后再追加自己的逻辑

### 案例 D：🔒 硬编码 · 不能改

**场景**：业务方说"我们想把 INV-CS-01 改掉 · orgchangetype 不必填"
**判断**：required=true 是平台 BasedataField 元数据行为 · 必填校验在标品 onAddValidators 链
**正确做法**：跟业务方解释为什么必填（联动 changeoperat 必须有 type） · 不要做这个定制

---

## 五、关键 PR 引用（platform_rules.json）

本场景代码生成时必查的 PR：

| PR | 适用场景 |
|---|---|
| **PR-001** | ISV 扩展 OP 必须并列挂 · 不继承 ChangeSceneSaveOp（场景特有类）/ AbsOrgBaseOp |
| **PR-002** | 多插件 RowKey 顺序 · ISV 校验 RowKey 早于 ChangeSceneSaveOp 才能在标品兜底前拦 |
| **PR-003** | FormPlugin 用 getModel().setValue · OP 用 entity.set（CS-03 联动写法）|
| **PR-004** | propertyChanged 联动用 beginInit/endInit 防死循环（虽然 ChangeSceneEditPlugin 没用 · 因为 changeoperat 跟 orgchangetype 是不同字段不会触发死循环 · 但 CS-03 扩展时若联动同字段必须用）|
| **PR-005** | 自建分录子表新增行需要 `kd.bos.id.ID.genLongId()` 给行 id（changereason / changeoperat 子表新增行时 · 见 CS-06 实证）|
| **PR-006** | CodeRuleOp 是平台模板插件 · 业务侧配【编码规则基础资料】即可 · 不要写自定义编码 OP |
| **PR-007** | 预置数据 number 不可改 · INV-CS-03 已落实 |
| **PR-010** | OP 13 个生命周期方法顺序 · CS-02 在 onAddValidators 注册 · 不在 afterExecute |
| **PR-011** | 标品没发 BEC（grep 0 命中 · 实证）· 不要订阅本场景的 BEC（CS-05 反指引）|

---

## 六、跟 admin_org 域规则的关系

`haos_changescene` 在 admin_org 业务规则体系中扮演**字典维护**角色：

| admin_org 业务约束 | 跟 haos_changescene 的依赖 |
|---|---|
| INV-04 (admin_org)：变动必须双分类（场景 + 原因）| 本表是"变动场景"字典侧 · 跟 haos_orgchangereason 配对 |
| `haos_adminorgdetail.changescene` 必填 | 强引用本表 enable=1 的数据 |
| `homs_orgbatchchgbill` 7 entry 全部含 changescene | 强引用本表 |

**结论**：维护本表的"业务字典纪律"会影响整个 admin_org 域的合规性。

---

## 七、删除前置规则（业务铁律）

> **业务铁律**（INV-CS-06 + CS-02）：删除/禁用任何 `haos_changescene` 数据之前 · 必须执行以下检查：

```
1. issyspreset = true ?     → 直接拒绝（出厂数据不可删）
2. enable = 1 ?              → 先禁用 · 不要直接删（保留历史外键）
3. 反向引用查询：
   - homs_orgbatchchgbill 主体 entry：QFilter("changescene", "=", id)
   - homs_orgbatchchgbill 6 个 *_entry：QFilter("add_changescene"/parent_/info_/disable_/merge_/split_changescene, "=", id)
   - haos_adminorgdetail：QFilter("changescene", "=", id)
   - haos_adminorghis：QFilter("changescene", "=", id)
4. 上述任一查到结果 → 拒绝删除 · 提示"已被 N 张申请单/详情记录引用"
```

→ CS-02 实现这套校验。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeSceneEditPlugin -->

## chgaction 实证补充（ChangeSceneEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.ChangeSceneEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeSceneEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeSceneEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeSceneListPlugin -->

## chgaction 实证补充（ChangeSceneListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.ChangeSceneListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeSceneListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeSceneListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.ChangeSceneSaveOp -->

## chgaction 实证补充（ChangeSceneSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.ChangeSceneSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.ChangeSceneSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.ChangeSceneSaveOp -->
