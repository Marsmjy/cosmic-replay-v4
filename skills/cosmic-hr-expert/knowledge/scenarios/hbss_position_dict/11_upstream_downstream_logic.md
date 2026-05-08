# hbss_position_dict · 上下游逻辑与知识主题说明

## [知识主题说明] HR 基础服务云 hbss 场景适用规范

> 本节说明 23 个交叉审计主题在本 hbss 字典场景中的适用情况 · 2026-04-28

### PR-001 ISV 并列挂不继承

本场景所有实体均采用 HRBaseDataTplEdit 标准基础资料模板。
ISV 扩展必须用 @SdkPlugin 并列挂 HRDataBaseEdit，禁止继承 HRBaseDataTplEdit。
并列挂原则（PR-001）是 hbss 字典场景的核心开发规范，适用于本场景全部实体。

### PR-002 RowKey 执行顺序

父模板（HRBaseDataTplEdit）RowKey 在标品场景专属类之前。
ISV 插件 RowKey 必须大于标品（通常 > 100，PR-002）。
hbss 字典场景无复杂多插件顺序问题，标准基础资料模板默认 RowKey 顺序即可。

### PR-003 FormPlugin setValue / OP entity.set

FormPlugin（并列挂的 ISV 插件）修改字段：`this.getModel().setValue("fieldName", value);`
OP 插件修改字段：`entity.set("fieldName", value);`
hbss 字典场景字段简单（number/name/status 为主），一般不需要复杂字段联动（PR-003）。

### PR-004 beginInit/endInit 防死循环

hbss 字典实体通常无复杂字段联动，propertyChanged 触发 setValue 场景极少。
若 ISV 确需在 propertyChanged 中调用 setValue，必须用 beginInit/endInit 包裹（PR-004）。

### PR-005 kd.bos.id.ID 生成序号

hbss 字典实体编码通过苍穹编码规则配置（CodeRuleOp），不需要手动生成主键（PR-005）。
若需生成子记录主键，使用 kd.bos.id.ID.genLongId()。

### PR-006 CodeRuleOp 编码规则配置优先

hbss 字典编码（number 字段）通过苍穹编码规则基础资料配置（PR-006），ISV 不写代码。
CodeRuleOp 自动触发，已覆盖所有 hbss_* 字典实体的 number 生成。

### PR-007 预置数据不可改

hbss 字典实体普遍含 issyspreset=1 系统预置数据（如性别：男/女，学历：本科/硕士/博士等）。
ISV 不得修改 issyspreset=1 的预置字典项（PR-007）。
ISV 可新增业务字典项（issyspreset=0），但不能删除或修改系统预置项。

### PR-008 iscurrentversion（本场景不强制）

本场景为非时序 BaseEntity 场景，无 iscurrentversion 版本字段（PR-008 不强制）。
时序场景（如 hbpm_positionhr）必须加 iscurrentversion=true 过滤，hbss 字典无此要求。

### PR-009 boid（本场景不强制）

本场景 hbss 字典实体 id 即业务唯一标识，无 boid 版本概念（PR-009 不强制）。
boid/id 区分仅适用于时序 HisModel 场景。

### PR-010 OP 13 生命周期方法

hbss 字典场景 OP 生命周期以 onAddValidators/beforeSaveTransaction/afterSaveTransaction 为主。
IOperationServicePlugIn 共 13 个生命周期方法（PR-010），hbss 字典通常只用前 3 个。
ISV 扩展 OP 继承 HRDataBaseOp，不继承场景专属 OP（如有的话）。

### PR-011 BEC 跨模块事件（本场景通常不发布）

hbss 字典实体变更通常不发布 BEC 事件（标品 OP 无 triggerEventSubscribeJobs 调用）。
例外：hbss_lawentity（法律实体）变更可能触发下游薪酬主体同步（需确认标品实证）。
ISV 若需响应字典变更，通过 IEventServicePlugin 订阅标品可能发布的事件（PR-011）。

### TOPIC-11 多语言表 _l 结尾

hbss 字典实体普遍有多语言名称（name/simplename 字段 MuliLangTextField 类型）。
对应多语言表以 _l 结尾（如 t_hbss_lawentity_l），存储 fname_l 等多语言列。
_i 结尾是拆分表（大字段分离），hbss 字典通常无拆分表。

### BIZ-down 下游引用检查

业务范围(hbss_bussinessfield)被核心人力云引用4次；行业类型(hbss_industrytype)被核心人力云/薪酬云各引用2次。

禁用/删除 hbss 字典项前，通过 refentity_reverse.json 查询下游引用：
- 若 refs[hbss_xxx] 非空，说明有其他实体字段引用该字典，需先确认下游无使用再禁用。
- hbss_lawentity 引用13次，禁用前必须检查下游（被引用 / 被谁引用）。

### BIZ-bec-sub / BIZ-bec-pub

hbss 字典场景通常不发布 BEC 事件（BIZ-bec-pub 本场景不适用）。
ISV 若需跨模块订阅字典变更，实现 IEventServicePlugin.handleEvent（BIZ-bec-sub）。

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

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
