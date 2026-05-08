# 业务规则 + 可变性 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于反编译 2 类 + scene_doc.json 字段属性 + listRules 实抓 0 条 + platform_rules 11 PR
> **可变性分类**: 🔒 硬编码 / ⚙️ 配置 / 🔌 插件可改 / 📝 元数据可改
> **confidence**: verified

---

## 一、6 条本场景核心不变式（INV）

> 这些约束**直接来自标品反编译 + 标品元数据** · 违反会导致数据不一致或下游异常。

### INV-CR-01 · `name` 必填 🔒

- **规则**：保存时 `name` 不能为空（标品 onAddValidators 校验空名 · 平台 BasedataField 通用行为）
- **可变性**: hardcoded（来自 scene_doc.json `(main).name` MuliLangTextField）
- **实现**：标品 BasedataField 平台层校验 · save 链 `HRBaseDataStatusOp.onAddValidators` 注册校验
- **违反后果**：保存 → 用户看到红色提示 "名称 不能为空"
- **不要扩展**：不要在 ISV 插件里写"自定义必填校验" · 平台已强制

### INV-CR-02 · `ctrlstrategy` 控制可见组织 🔌

- **规则**：用户改 `ctrlstrategy`（控制策略 · ComboField 枚举）→ 标品 `BdCtrlStrtgyShowLogicPlugin` 联动可见组织字段（org / useorg）
- **可变性**: pluggable（标品逻辑 · 但 ISV 可以追加联动）
- **实现**：
  - 表单层：`kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin`（标品基础资料统一逻辑）
  - 列表层：`kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicListPlugin`
- **设计意图**：基础资料的"管理组织 vs 使用组织"控制策略 · 决定哪些组织可见、可改
- **不要绕过**：不要重写 ctrlstrategy 的 propertyChanged · 改用并列挂追加（CS-03）

### INV-CR-03 · 出厂数据 (issyspreset=true) 关键字段不可改 🔒

- **规则**：以下字段在 issyspreset=true 的行 · isvCanModify=false：
  - `creator` / `modifier` / `createtime` / `modifytime` / `masterid`
  - `disabler` / `disabledate` / `initdatasource`
  - `orinumber` / `oriname` / `oristatus`
  - `issyspreset`
- **可变性**: hardcoded（scene_doc.json `isvCanModify=false`）
- **业务原因**：这些是平台/标品内置字典 · 被很多地方引用（如标品默认 1010L 主键）
- **遵循 PR-007**：预置数据编码不可改 · 仅业务自建数据可改
- **违反后果**：在【开发平台】尝试 modifyMeta 改这些字段 → 静默忽略 / 平台升级覆盖

### INV-CR-04 · 列表层强制隐藏 id=1010L 🔒

- **规则**：列表查询时硬编码 `id != 1010L`
- **可变性**: hardcoded（`ChangeReasonListPlugin.java:21`）
- **业务意图**：标品保留 1010 主键作为某种"内部模板项 / 默认占位项" · 不让用户在列表点开
- **影响**：通过列表入口看不到 · 但通过 F7（走平台 bd_basedata 列表）仍能选到（注意：F7 选择不走 ChangeReasonListPlugin）
- **不能改**：覆盖会破坏标品业务约定 · ISV 自建过滤应在自己列表插件里加

### INV-CR-05 · `BaseDataBuOp` 注册 CtrlStrategyValidator 🔌

- **规则**：保存链上 `BaseDataBuOp.onAddValidators` 自动注册 `CtrlStrategyValidator`（控制策略合规校验）
- **可变性**: pluggable（标品行为 · ISV 不应禁用 · 但可追加自己的 Validator）
- **实现**：`BaseDataBuOp.java:19-22`
  ```java
  public void onAddValidators(AddValidatorsEventArgs args) {
      super.onAddValidators(args);
      args.addValidator((AbstractValidator)new CtrlStrategyValidator());
  }
  ```
- **设计意图**：保证 ctrlstrategy + createorg/useorg 的多组织合规性
- **不要继承 BaseDataBuOp** · 它是场景特有薄壳 · 改用 `HRDataBaseOp` 并列挂（PR-001）

### INV-CR-06 · 删除/禁用没有反向引用校验 ⚠

- **规则**：标品 delete / disable 链**没有**反向引用前置校验
  - delete 链 4 个插件 · 都不查 `t_haos_cschangereason`（haos_changescene 的 changereason 多选关系子表）
  - delete 链也不查 `homs_orgbatchchgbill` 是否间接引用过（通过 changescene 链路）
  - disable 链 2 个插件 · 同上
- **可变性**: pluggable（标品缺失 · CS-02 通过 ISV 加补）
- **后果**：详见 05 §5 删除路径的数据风险
- **正确做法**：CS-02 加 ISV 校验插件 · 在 onAddValidators 注册 Validator · 走 `t_haos_cschangereason` 直查反向引用

---

## 二、标品 formRule（0 条 · listRules 实抓）

> 数据源：`_auto_rules.md` · OpenAPI listRules(formNumber=haos_orgchangereason) 实抓 = **0 条**

| ruleId | enabled | 说明 |
|---|---|---|
| _无_ | _无_ | 本场景没有任何 formRule / bizRule（标品没配） |

→ **结论**：业务规则**全部内嵌在反编译类**（ChangeReasonListPlugin · BaseDataBuOp）+ 标品 8 插件链的默认行为里。
→ ISV 不要去 OpenAPI listRules / addRule 找规则 · 直接看反编译类 + opkeys/save.json 链路。
→ 跟 haos_changescene（3 条 formRule）相比 · 本场景**比配对场景还简单**。

---

## 三、字段可变性矩阵（28 字段）

> ⚙️ 配置 · 📝 元数据 · 🔌 插件 · 🔒 硬编码

### 3.1 业务核心字段（4 个 · L1/L3 业务可改）

| 字段 | 可变性 | 修改方式 | 风险 |
|---|---|---|---|
| `number` | 🔌 部分可改（CodeRule 自动 / 用户手填）· 出厂数据 issyspreset=true 不可改 | 标品 CodeRuleOp + 用户输入 | 低（受 PR-007 拦）|
| `name` | 📝 多语言可改（INV-CR-01 必填）| `t_haos_orgchangereason_l.fname` | 低 · 但下游 F7 标签会跟随更新 |
| `otclassify` | 📝 BasedataField 引用 haos_otclassify | 用户在表单选 / API 写 | 低 |
| `ctrlstrategy` | 🔌 联动可见组织（INV-CR-02）| 用户在表单选 + 标品 BdCtrlStrtgyShowLogicPlugin | 低（标品行为完整） |

### 3.2 标识字段（4 个 · 创建后特殊管控）

| 字段 | 可变性 | 修改方式 |
|---|---|---|
| `number` | 🔌 创建后部分可改（但出厂 issyspreset=true 行不可改）| 标品配 CodeRule 自动生成 · 用户可改业务自建数据的编码 |
| `name` | 📝 多语言可改 | t_haos_orgchangereason_l |
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
| `createorg` / `org` / `useorg` / `srccreateorg` | ⚙️ 平台多组织设置默认 + 受 ctrlstrategy 联动 | OrgField 平台行为 |

### 3.5 系统字段（11 个 · 全部 🔒 硬编码或 🔒 系统维护）

`creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oriname` / `oristatus` 全部 isvCanModify=false · ISV 不可改 · 平台维护。

参考 03 §3.3 / §3.5 详细说明。

### 3.6 出厂位图字段（4 个 · L3 系统）

| 字段 | 可变性 | 备注 |
|---|---|---|
| `sourcedata` | 📝 BigInt 原资料 id | L3 平台维护 |
| `bitindex` / `srcindex` | 📝 IntegerField 位图 | L3 平台维护 |
| `srccreateorg` | 📝 OrgField 原创建组织 | L3 平台维护 |

---

## 四、可变性场景化案例

### 案例 A：可改 ⚙️ 配置 · 不需要写代码

**场景**：业务方说"我们集团希望默认看到自己分公司的变动原因"
**判断**：ctrlstrategy 控制策略 + 多组织字段是平台标准能力
**错误做法**：去改 ChangeReasonListPlugin 源码（标品 jar · ISV 改不动）
**正确做法**：通过【数据控制策略基础资料】配置 · 不写代码

**场景**：业务方说"编码规则要按业务线前缀生成"
**判断**：CodeRuleOp 是平台模板插件（PR-006）
**错误做法**：自定义 OP 写编码逻辑
**正确做法**：在【编码规则基础资料】配置即可 · 不写代码

### 案例 B：📝 元数据可改 · 改 modifyMeta

**场景**：业务方说"在变动原因上加 '业务影响等级'（紧急/重要/一般）字段"
**判断**：可以加 ISV 前缀字段 · 出厂数据扩展（issyspreset=true 行只是不能改 number 等关键字段 · ISV 加新字段是 OK 的）
**正确做法**：CS-01 加自定义字段 · 走 modifyMeta(op=add field) + ISV 前缀

### 案例 C：🔌 插件可改 · 必须加 ISV 插件

**场景**：业务方说"删除变动原因前先校验有没有 changescene 引用过"
**判断**：标品没有这个校验 · INV-CR-06 描述的缺陷 · 必须 ISV 实现
**正确做法**：CS-02 加禁删校验插件

**场景**：业务方说"我们想加一个'连锁影响范围'下拉 · 选完自动校验对应组织数"
**判断**：是字段联动业务规则 · 标品没有 · 必须 ISV
**正确做法**：CS-03 字段联动定制 · 在 ctrlstrategy/相关字段的 propertyChanged 之后挂自己的插件 · super 调用后再追加自己的逻辑

### 案例 D：🔒 硬编码 · 不能改

**场景**：业务方说"我们想把 INV-CR-01 改掉 · name 不必填"
**判断**：name 必填是平台 BasedataField 元数据行为 · 必填校验在标品 onAddValidators 链
**正确做法**：跟业务方解释为什么必填（基础资料无名字会导致下游 F7 显示空） · 不要做这个定制

**场景**：业务方说"我们想让用户在列表里也看到 1010L 那一条"
**判断**：INV-CR-04 是 ChangeReasonListPlugin 硬编码（27 行薄壳里的灵魂行）· ISV 改不动
**正确做法**：跟业务方解释 1010L 是平台预留主键 · 业务上不应该用 · 改用 F7 选择路径

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
| **PR-007** | 预置数据 number 不可改 · INV-CR-03 已落实 |
| **PR-010** | OP 13 个生命周期方法顺序 · CS-02 在 onAddValidators 注册 · 不在 afterExecute |
| **PR-011** | 标品没发 BEC（grep 0 命中 · 实证）· 不要订阅本场景的 BEC（CS-05 反指引）|

---

## 六、跟 admin_org 域规则的关系

`haos_orgchangereason` 在 admin_org 业务规则体系中扮演**字典维护**角色：

| admin_org 业务约束 | 跟 haos_orgchangereason 的依赖 |
|---|---|
| INV-04 (admin_org)：变动必须双分类（场景 + 原因）| 本表是"变动原因"字典侧 · 跟 haos_changescene 配对 |
| `haos_changescene.changereason` 多选 | 强引用本表 enable=1 的数据（多对多 · t_haos_cschangereason）|
| `homs_orgbatchchgbill` 7 entry × changescene → changereason | 间接引用本表（通过 changescene 链路）|

**结论**：维护本表的"业务字典纪律"会通过 changescene 链路影响整个 admin_org 域的合规性。

---

## 七、删除前置规则（业务铁律）

> **业务铁律**（INV-CR-06 + CS-02）：删除/禁用任何 `haos_orgchangereason` 数据之前 · 必须执行以下检查：

```
1. issyspreset = true ?     → 直接拒绝（出厂数据不可删）
2. enable = 1 ?              → 先禁用 · 不要直接删（保留历史外键）
3. 反向引用查询：
   - 直接路径：t_haos_cschangereason.fbasedataid = id
     这是 haos_changescene.changereason MulBasedata 子表
   - 间接路径：通过 changescene → homs_orgbatchchgbill 7 entry
     （CS-02 提供"穿透"查询模板 · 但建议只查直接路径 · 性能/可解释性优先）
4. 上述任一查到结果 → 拒绝删除 · 提示"已被 N 条变动场景引用"
```

→ CS-02 实现这套校验。

---

## 八、跟 haos_changescene 的双字典纪律

业务上"变动场景 + 变动原因"是耦合的双字典 · 改本表的"原因"会影响配对的"场景"：

| 改动类型 | 影响配对字典 |
|---|---|
| 加新 changereason 数据 | haos_changescene 新建/编辑时 · 多选 F7 多了一个选项 |
| 修改 changereason name | 已有 changescene 多选关系外键 id 不变 · F7 显示标签自动更新（多语言）|
| 禁用 changereason | 已有 changescene 多选关系保留 · 新建时多选不到 |
| 删除 changereason ⚠ | t_haos_cschangereason 该行外键变孤儿 · changescene 多选展示空 |

→ **业务 SOP**：双字典维护要协同 · 改本表前先查 changescene 已用情况；建议先禁用 · 慎删。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin -->

## chgaction 实证补充（ChangeReasonListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin -->

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
