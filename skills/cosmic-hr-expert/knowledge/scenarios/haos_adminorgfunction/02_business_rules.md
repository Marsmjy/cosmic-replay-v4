# 业务规则 + 可变性 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于反编译 2 类 + scene_doc.json 字段属性 + listRules 实抓 1 条 + platform_rules 11 PR
> **可变性分类**: 🔒 硬编码 / ⚙️ 配置 / 🔌 插件可改 / 📝 元数据可改
> **confidence**: verified

---

## 一、7 条本场景核心不变式（INV）

> 这些约束**直接来自标品反编译 + 标品元数据 + listRules 实抓** · 违反会导致数据不一致或下游异常。

### INV-AF-01 · `name` 必填 🔒

- **规则**：保存时 `name` 不能为空（标品 onAddValidators 校验空名 · 平台 BasedataField 通用行为）
- **可变性**: hardcoded（来自 scene_doc.json `(main).name` MuliLangTextField）
- **实现**：标品 BasedataField 平台层校验 · save 链 `HRBaseDataStatusOp.onAddValidators` 注册校验
- **违反后果**：保存 → 用户看到红色提示 "名称 不能为空"
- **不要扩展**：不要在 ISV 插件里写"自定义必填校验" · 平台已强制

### INV-AF-02 · `ctrlstrategy` 控制可见组织 🔌

- **规则**：用户改 `ctrlstrategy`（控制策略 · ComboField 枚举）→ 标品 `BdCtrlStrtgyShowLogicPlugin` 联动可见组织字段（org / useorg）
- **可变性**: pluggable（标品逻辑 · 但 ISV 可以追加联动）
- **实现**：
  - 表单层：`kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`（标品基础资料统一逻辑）
  - 列表层：`kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicListPlugin`
- **设计意图**：基础资料的"管理组织 vs 使用组织"控制策略 · 决定哪些组织可见、可改
- **不要绕过**：不要重写 ctrlstrategy 的 propertyChanged · 改用并列挂追加（CS-03）

### INV-AF-03 · 出厂数据 (issyspreset=true) 关键字段不可改 🔒

- **规则**：以下字段在 issyspreset=true 的行 · isvCanModify=false：
  - `creator` / `modifier` / `createtime` / `modifytime` / `masterid`
  - `disabler` / `disabledate` / `initdatasource`
  - `orinumber` / `oriname` / `oristatus`
  - `issyspreset`
- **可变性**: hardcoded（scene_doc.json `isvCanModify=false`）
- **业务原因**：这些是平台/标品内置职能字典 · 被 admin_org 大量引用
- **遵循 PR-007**：预置数据编码不可改 · 仅业务自建数据可改
- **违反后果**：在【开发平台】尝试 modifyMeta 改这些字段 → 静默忽略 / 平台升级覆盖

### INV-AF-04 · listRules formRule 拦截预置数据修改 🔒 ⭐ （本场景特有）

- **规则**：listRules 实抓 1 条 formRule（ruleId `21V4EGK80+JT`）：
  - **preCondition**: `issyspreset = true`
  - **行为**: 系统预置数据进入编辑视图时所有字段变只读
- **可变性**: hardcoded（标品 listRules 配置 · OpenAPI listRules 实抓）
- **业务原因**：UI 层硬拦 · 比 INV-AF-03 字段级 isvCanModify=false 拦更早（用户进入编辑就看不到任何可填字段）
- **跟 haos_orgchangereason 的差异** ⭐：
  - haos_orgchangereason listRules 是 0 条 · 仅靠 INV-CR-03 字段级拦（用户能进编辑但保存被拒）
  - 本场景 INV-AF-04 + INV-AF-03 双重保护 · 从 UI 到字段都拦 · 更严
- **不要扩展**：不要尝试 addRule 加新规则覆盖此条 · 业务上无意义
- **如何识别**：通过 OpenAPI listRules(formNumber=haos_adminorgfunction) 查 · 不在反编译产物里

### INV-AF-05 · `BaseDataBuOp` 注册 CtrlStrategyValidator 🔌

- **规则**：保存链上 `BaseDataBuOp.onAddValidators` 自动注册 `CtrlStrategyValidator`（控制策略合规校验）
- **可变性**: pluggable（标品行为 · ISV 不应禁用 · 但可追加自己的 Validator）
- **实现**：`BaseDataBuOp.java:17-23`
  ```java
  public void onAddValidators(AddValidatorsEventArgs args) {
      super.onAddValidators(args);
      args.addValidator((AbstractValidator)new CtrlStrategyValidator());
  }
  ```
- **设计意图**：保证 ctrlstrategy + createorg/useorg 的多组织合规性
- **不要继承 BaseDataBuOp** · 它是场景特有薄壳 · 改用 `HRDataBaseOp` 并列挂（PR-001）

### INV-AF-06 · 删除/禁用没有反向引用校验 ⚠

- **规则**：标品 delete / disable 链**没有**反向引用前置校验
  - delete 链 4 个插件 · 都不查 `t_haos_adminorg.fadminorgfunctionid`（haos_adminorg 的 adminorgfunction 字段引用）
  - delete 链也不查 haos_adminorghis / haos_adminorgdetail（共物理表 · 同源外键）
  - disable 链 2 个插件 · 同上
- **可变性**: pluggable（标品缺失 · CS-02 通过 ISV 加补）
- **后果**：详见 05 §5 删除路径的数据风险
- **正确做法**：CS-02 加 ISV 校验插件 · 在 onAddValidators 注册 Validator · 走 `t_haos_adminorg.fadminorgfunctionid` 直查反向引用（单选关系 · 比 haos_orgchangereason 多选关系查询更直接）

### INV-AF-07 · 列表默认排序：启用项排前面 🔌

- **规则**：列表查询时硬编码 `setOrderBy("enable desc,number asc")`
- **可变性**: pluggable（ISV 可挂列表插件追加排序 · 但**不应重写 setFilter** · 保留标品默认）
- **实现**：`ListOrderCommonPlugin.java:15-18`
- **业务意图**：用户打开列表立即看到启用中的职能（按编码升序）· 已禁用的沉到底部 · 减少误操作
- **不要重写 setFilter** · ISV 想自定义排序应通过 customParam 或追加 OrderBy（参考 07 §五）

---

## 二、标品 formRule（1 条 · listRules 实抓）⭐ 本场景特有

> 数据源：`_auto_rules.md` · OpenAPI listRules(formNumber=haos_adminorgfunction) 实抓 = **1 条**

| ruleId | enabled | preCondition | 说明 |
|---|---|---|---|
| `21V4EGK80+JT` | ✅ | `issyspreset = true` | 系统预置数据不可修改（INV-AF-04）|

→ **结论**：业务规则**绝大部分内嵌在反编译类**（ListOrderCommonPlugin · BaseDataBuOp）+ 标品 8 插件链的默认行为里。
→ 仅 1 条 formRule 由 OpenAPI 配 · ISV 不要去 OpenAPI listRules / addRule 加新规则覆盖（业务上无意义）。
→ 跟 haos_orgchangereason（0 条 formRule）相比 · 本场景**多 1 条 listRules**：UI 层多了一道"预置数据不可修改"硬拦。

---

## 三、字段可变性矩阵（27 字段）

> ⚙️ 配置 · 📝 元数据 · 🔌 插件 · 🔒 硬编码

### 3.1 业务核心字段（3 个 · L1/L3 业务可改）

| 字段 | 可变性 | 修改方式 | 风险 |
|---|---|---|---|
| `number` | 🔌 部分可改（CodeRule 自动 / 用户手填）· 出厂数据 issyspreset=true 不可改 | 标品 CodeRuleOp + 用户输入 | 低（受 PR-007 + INV-AF-04 双拦）|
| `name` | 📝 多语言可改（INV-AF-01 必填）· 出厂数据被 INV-AF-04 listRules 拦 | `t_haos_adminorgfunction_l.fname` | 低 · 但下游 admin_org F7 标签会跟随更新 |
| `ctrlstrategy` | 🔌 联动可见组织（INV-AF-02）| 用户在表单选 + 标品 BdCtrlStrtgyShowLogicPlugin | 低（标品行为完整） |

> 💡 **跟 haos_orgchangereason 的差异**：
> - haos_orgchangereason 有 4 个业务核心字段（多 1 个 otclassify）
> - 本场景**没有 otclassify**（不带分类字段 · 业务上职能不需要二级分类）
> - 所以业务核心字段更少（3 vs 4）

### 3.2 标识字段（4 个 · 创建后特殊管控）

| 字段 | 可变性 | 修改方式 |
|---|---|---|
| `number` | 🔌 创建后部分可改（但出厂 issyspreset=true 行不可改）| 标品配 CodeRule 自动生成 · 用户可改业务自建数据的编码 |
| `name` | 📝 多语言可改（INV-AF-04 拦预置数据）| t_haos_adminorgfunction_l |
| `simplename` | 📝 多语言可改 | 同上 |
| `description` | 📝 多语言可改 | 同上 |

### 3.3 状态字段（5 个 · 流程驱动）

| 字段 | 可变性 | 修改方式 |
|---|---|---|
| `status` | 🔌 走 submit/audit/unsubmit/unaudit 4 操作 | BaseDataSubmitPlugin/BaseDataAuditPlugin 标品 |
| `enable` | 🔌 走 disable/enable 2 操作 | BaseDataDisablePlugin/BaseDataEnablePlugin 标品 |
| `disabler` | 🔒 系统自动写 | disable 操作 |
| `disabledate` | 🔒 系统自动写 | disable 操作 |
| `index` | 📝 排序号 · 用户可改 | 平台默认 · 跟 ListOrderCommonPlugin 默认排序协同 |

### 3.4 多组织字段（4 个 · 平台多组织默认）

| 字段 | 可变性 | 备注 |
|---|---|---|
| `createorg` / `org` / `useorg` / `srccreateorg` | ⚙️ 平台多组织设置默认 + 受 ctrlstrategy 联动 | OrgField 平台行为 |

### 3.5 系统字段（11 个 · 全部 🔒 硬编码或 🔒 系统维护）

`creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oriname` / `oristatus` 全部 isvCanModify=false · ISV 不可改 · 平台维护。

参考 03 §3.3 / §3.5 详细说明。

### 3.6 出厂位图字段（3 个 · L3 系统）

| 字段 | 可变性 | 备注 |
|---|---|---|
| `sourcedata` | 📝 BigInt 原资料 id | L3 平台维护 |
| `bitindex` / `srcindex` | 📝 IntegerField 位图 | L3 平台维护 |

---

## 四、可变性场景化案例

### 案例 A：可改 ⚙️ 配置 · 不需要写代码

**场景**：业务方说"我们集团希望默认看到自己分公司的职能字典"
**判断**：ctrlstrategy 控制策略 + 多组织字段是平台标准能力
**错误做法**：去改 ListOrderCommonPlugin 源码（标品 jar · ISV 改不动）
**正确做法**：通过【数据控制策略基础资料】配置 · 不写代码

**场景**：业务方说"编码规则要按业务线前缀生成"
**判断**：CodeRuleOp 是平台模板插件（PR-006）
**错误做法**：自定义 OP 写编码逻辑
**正确做法**：在【编码规则基础资料】配置即可 · 不写代码

### 案例 B：📝 元数据可改 · 改 modifyMeta

**场景**：业务方说"在职能上加 '职能分类'（核心/支撑/创新）字段"
**判断**：可以加 ISV 前缀字段 · 出厂数据扩展（issyspreset=true 行只是不能改 number 等关键字段 · ISV 加新字段是 OK 的）
**正确做法**：CS-01 加自定义字段 · 走 modifyMeta(op=add field) + ISV 前缀

### 案例 C：🔌 插件可改 · 必须加 ISV 插件

**场景**：业务方说"删除职能前先校验有没有 admin_org 引用过"
**判断**：标品没有这个校验 · INV-AF-06 描述的缺陷 · 必须 ISV 实现
**正确做法**：CS-02 加禁删校验插件

**场景**：业务方说"我们想加一个'适用组织规模'下拉 · 选完自动校验对应组织数"
**判断**：是字段联动业务规则 · 标品没有 · 必须 ISV
**正确做法**：CS-03 字段联动定制 · 在 ctrlstrategy/相关字段的 propertyChanged 之后挂自己的插件 · super 调用后再追加自己的逻辑

### 案例 D：🔒 硬编码 · 不能改

**场景**：业务方说"我们想把 INV-AF-01 改掉 · name 不必填"
**判断**：name 必填是平台 BasedataField 元数据行为 · 必填校验在标品 onAddValidators 链
**正确做法**：跟业务方解释为什么必填（基础资料无名字会导致下游 F7 显示空） · 不要做这个定制

**场景**：业务方说"标品的'管理'职能名字不好 · 想改成'综合管理'"
**判断**：INV-AF-04 listRules 拦预置数据修改 · UI 层硬拦 · ISV 改不动
**正确做法**：跟业务方沟通 · 让他们 disable 标品预置项 · ISV 自建一条"综合管理"业务自建职能

---

## 五、关键 PR 引用（platform_rules.json）

本场景代码生成时必查的 PR：

| PR | 适用场景 |
|---|---|
| **PR-001** | ISV 扩展 OP 必须并列挂 · 不继承 BaseDataBuOp（场景特有类）/ AbsOrgBaseOp |
| **PR-002** | 多插件 RowKey 顺序 · ISV 校验 RowKey 早于 BaseDataBuOp 才能在标品兜底前拦 |
| **PR-003** | FormPlugin 用 getModel().setValue · OP 用 entity.set（CS-03 联动写法）|
| **PR-004** | propertyChanged 联动用 beginInit/endInit 防死循环（CS-03 ctrlstrategy 联动其他字段时务必用）|
| **PR-005** | 自建分录子表新增行需要 `kd.bos.id.ID.genLongId()` 给行 id（CS-05 实证）· 业务编码用 `genStringId()` |
| **PR-006** | CodeRuleOp 是平台模板插件 · 业务侧配【编码规则基础资料】即可 · 不要写自定义编码 OP |
| **PR-007** | 预置数据 number 不可改 · INV-AF-03 已落实 |
| **PR-010** | OP 13 个生命周期方法顺序 · CS-02 在 onAddValidators 注册 · 不在 afterExecute |
| **PR-011** | 标品没发 BEC（grep 0 命中 · 实证）· 不要订阅本场景的 BEC（CS-05 反指引）|

---

## 六、跟 admin_org 域规则的关系

`haos_adminorgfunction` 在 admin_org 业务规则体系中扮演**字典维护**角色：

| admin_org 业务约束 | 跟 haos_adminorgfunction 的依赖 |
|---|---|
| INV-04 (admin_org)：每个组织可选 0-1 个职能 | 本表是"职能"字典侧 · 单选 BasedataField 关系 |
| `haos_adminorg.adminorgfunction` 单选 | 强引用本表 enable=1 的数据（一对多 · t_haos_adminorg.fadminorgfunctionid 直字段）|
| `haos_adminorghis` 时序版本 | 共物理表 t_haos_adminorg · 历史版本中 fadminorgfunctionid 同源外键 |

**结论**：维护本表的"业务字典纪律"会通过 admin_org → adminorgfunction 关系**直接影响**整个 admin_org 域的核心数据完整性。

---

## 七、删除前置规则（业务铁律）

> **业务铁律**（INV-AF-06 + CS-02）：删除/禁用任何 `haos_adminorgfunction` 数据之前 · 必须执行以下检查：

```
1. issyspreset = true ?     → 直接拒绝（出厂数据不可删 · INV-AF-04 listRules + INV-AF-03 双拦）
2. enable = 1 ?              → 先禁用 · 不要直接删（保留历史外键）
3. 反向引用查询：
   - 直接路径：t_haos_adminorg.fadminorgfunctionid = id
     这是 haos_adminorg.adminorgfunction BasedataField 单选字段
     ⚠ 必须加 enable='1' 跟 iscurrentversion=true 缩到当前生效版本
       （admin_org 是 HisModel 时序场景 · 历史版本会有失效外键 · 不该影响删除决策）
4. 上述任一查到结果 → 拒绝删除 · 提示"已被 N 个行政组织引用"
```

→ CS-02 实现这套校验。

> 💡 **跟 haos_orgchangereason 的查询差异**：
> - haos_orgchangereason 走 MulBasedataField 多选：`new QFilter("changereason.fbasedataid", "=", id)`
> - 本场景走 BasedataField 单选：`new QFilter("adminorgfunction", "=", id)` + `iscurrentversion=true`
> - 本场景查的是 HisModel 时序表 · 多了 iscurrentversion 这个版本维度过滤

---

## 八、跟 haos_adminorg 的"主从字典纪律"

业务上"行政组织 + 职能"是一对多耦合关系 · 改本表的"职能"会影响下游的"组织"：

| 改动类型 | 影响下游 |
|---|---|
| 加新 adminorgfunction 数据 | haos_adminorg 新建/编辑时 · adminorgfunction F7 多了一个选项 |
| 修改 adminorgfunction name | 已有 admin_org 的 fadminorgfunctionid 外键 id 不变 · F7 显示标签自动更新（多语言）|
| 禁用 adminorgfunction | 已有 admin_org 的外键关系保留 · 新建时 F7 不再可选 |
| 删除 adminorgfunction ⚠ | t_haos_adminorg.fadminorgfunctionid = 已删 id 的所有行变孤儿 · admin_org 列表"职能"列展示空 |

→ **业务 SOP**：维护职能字典前要先查 admin_org 已用情况；建议先禁用 · 慎删（CS-02 保护）。
→ 跟 haos_orgchangereason 的"双字典对等协同"不同 · 本场景是"主-从字典"（adminorg=主 / function=从）· 维护从表要看主表数据。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin -->

## chgaction 实证补充（ListOrderCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.BaseDataBuOp -->

## chgaction 实证补充（BaseDataBuOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.BaseDataBuOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.BaseDataBuOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.BaseDataBuOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->
