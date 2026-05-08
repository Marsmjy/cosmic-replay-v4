# 业务规则 + 可变性 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `_auto_operations.md` 61 opKey 校验规则 + `rules_chain_all.json` 反编译业务规则 + `scene_doc.json` 64 字段 minefield
> **可变性分类**: 🔒 硬编码（平台/时序模板级）/ ⚙️ 配置（UI 可切）/ 🔌 插件可改 / 📝 元数据可改
> **confidence**: verified（类名 / opKey 全部来自 jar 反编译 + OpenAPI 实抓）

---

## 一、8 条核心不变式（时序模型 + 基础资料双重约束）

这些是**职位域的系统级约束**，违反会导致时序数据错乱或下游引用失效。

### INV-01 · 职位序列 jobseq 必填 🔒

- **规则**：新增 / 修改职位时 `jobseq` 必须有值（`scene_doc.json` L664 `required: true`）
- **可变性**：`hardcoded`（由元数据 required=true 保证，save 的 MustInput 校验 `6096194600001fac` 执行强制校验）
- **业务意义**：职位序列是所有后续职等 / 职级派生的源头，无序列则整条职位体系链断
- **实现点**：`kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp#onAddValidators`（`rules_chain_all.json` L392-L402）

### INV-02 · 时序模型 boid 不可改 🔒

- **规则**：`boid`（业务 ID）一经生成绑定数据版本组，不可修改
- **可变性**：`hardcoded`
- **违反后果**：时序表 `t_hbjm_job` 各版本间关联断裂，`iscurrentversion` 判断失效
- **实现点**：时序模板 `hbp_histimeseqtpl` 级约束；`HisUniqueValidateOp` 校验（见 `_auto_operations.md` save 链 L110）
- **`scene_doc.json` 证据**：L386 `minefield: red`，`autoComputed: true`

### INV-03 · 同一 boid 同一时间段唯一 🔒

- **规则**：同一业务 ID 在 `[bsed, bsled]` 时间段不允许两条并发生效数据
- **可变性**：`hardcoded`（由 `HisUniqueValidateOp` 强制校验，`_auto_operations.md` L110 save 链第 8 位）
- **实现点**：`kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`（时序模型唯一性校验器）
- **关联字段**：`boid` + `bsed` + `bsled` + `iscurrentversion`

### INV-04 · 系统预置职位禁止删除 🔒

- **规则**：`issyspreset=true` 的职位走 `delete` opKey 时被 `hrbddeletevalidator` 阻断
- **可变性**：`hardcoded`
- **证据**：`_auto_operations.md` L148 delete 校验 `2+U=J7R7IEF/` = `hrbddeletevalidator`
- **`scene_doc.json`**：L278-L289 `issyspreset` 字段 `minefield: red` + `autoComputed: true`

### INV-05 · 出厂数据字段禁止手改 🔒

- **规则**：`orinumber` / `oriname` / `oristatus` 三字段由标品初始化时写入，不可手动修改
- **可变性**：`hardcoded`
- **违反后果**：出厂数据比对（`HRBaseOriginalOp`）逻辑错误，升级补丁失败
- **实现点**：`kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`（save 链第 7 位，`_auto_operations.md` L109）
- **`scene_doc.json`**：L334-L374 三字段均 `minefield: red` + `isvCanModify: false`

### INV-06 · 生效日期不能填写未来 🔒

- **规则**：`bsed` 不允许填写未来日期（save 的 FormValidate `2M42D=Y0/0XK`）
- **可变性**：`hardcoded`（OPS: 严格校验规则）
- **证据**：`_auto_operations.md` L122 save validations 最后一行 `生效日期不能填写未来`
- **业务意义**：职位生效必须立即或者回溯；未来生效必须走 `newhisversion` 新增数据版本流程

### INV-07 · 编码全局唯一 🔒

- **规则**：默认 `number` 在系统内全局唯一
- **可变性**：`pluggable`（可按创建组织隔离）
- **证据**：save 校验 `2=K9URZCEUUS`（编码唯一性，enabled=True，`_auto_operations.md` L121）
- **可选切换**：`0+/SL/MZ=VJB`（创建组织 + 编码组合唯一，默认 disabled）

### INV-08 · 编码创建后不可修改 🔒

- **规则**：`number` 一经保存，后续 modify 不允许改
- **可变性**：`hardcoded`（下游岗位 `hbpm_position` / 人员档案 `hrpi_*` 都引用 number）
- **违反后果**：下游引用断裂

---

## 二、列表展示规则

### R-L01 · 按创建组织数据权限过滤 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.formplugin.web.basedata.JobBaseBuListPlugin`（`_auto_plugin_semantics.md` L59-L70）
- **关键方法**：`filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args)` + `getPermOrgResult(String entityName)`
- **扩展方式**：继承 + super 调用 + 追加 filter

### R-L02 · 列表支持超链跳转详情 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin.billListHyperLinkClick`（`_auto_plugin_semantics.md` L75-L81）
- **扩展方式**：继承 + 追加跳转逻辑

### R-L03 · 基础资料通用列表过滤 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.formplugin.web.common.JobBaseDataListPlugin.setFilter`（`_auto_plugin_semantics.md` L47-L55）
- **父类**：`HRDataBaseList`

### R-L04 · 启用 / 禁用状态过滤 ⚙️

- **可变性**：`config`（标品控制策略 UI 可切启停用过滤）
- **关联字段**：`enable`（BillStatusField，`scene_doc.json` L99）

---

## 三、新增 / 修改职位规则

### R-C01 · 职位序列必填 🔒

- 见 INV-01

### R-C02 · 编码唯一性（默认全局）🔌

- 见 INV-07
- **可变性**：pluggable（可切"创建组织隔离"，类比 CS-05 按租户隔离）

### R-C03 · 职级方案 joblevelscm / 职等方案 jobgradescm 引用有效 🔌

- **可变性**：`pluggable`（可加自定义校验，如方案必须启用）
- **实现位置**：`JobHrSaveOp.onAddValidators` 并列注册 Validator

### R-C04 · 保存合法性校验 `FormValidate 1VRALXJOVNKD` ⚙️

- **可变性**：`config`（默认 disabled，可通过 updateOperation 改 enabled）
- **证据**：`_auto_operations.md` L119

### R-C05 · 保存时 CodeRuleOp 自动生成编码 🔌

- **可变性**：`config`（规则由编码规则基础资料配置）+ `pluggable`（可继承 `CodeRuleOp`）
- **证据**：save 链第 1 位 `kd.bos.business.plugin.CodeRuleOp`（`_auto_operations.md` L104）

---

## 四、审核 / 反审核规则

### R-A01 · 审核前合法性校验 ⚙️

- **可变性**：`config`（默认 enabled）
- **证据**：`_auto_operations.md` L169-L172 audit 3 条校验
  - `1cc0054f000018ac` 合法性校验（enabled）
  - `f2843bab0000bfac` 合法性校验（enabled）
  - `InProcess 2W/BRWU+MXP7` 基础资料在流程中校验（默认 disabled）

### R-A02 · 审核动作由 JobHrAuditOp 处理 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp`（`_auto_operations.md` L164，audit 链第 4 位）
- **扩展方式**：继承 + super 调用

### R-A03 · 反审核校验同审核 ⚙️

- **证据**：`_auto_operations.md` L191-L196 unaudit 3 条校验对称

---

## 五、启用 / 禁用规则

### R-E01 · 启用操作 JobEnableValidator 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp.onAddValidators` 注册 `JobEnableValidator`（`rules_chain_all.json` L678-L688）
- **业务语义**：启用前的业务校验（具体逻辑由专家补充）

### R-E02 · 启用前预处理读 boid / enable 🔌

- **可变性**：`pluggable`
- **实现**：`JobHrEnableOp.beforeExecuteOperationTransaction`（`rules_chain_all.json` L724-L740）
- **读字段**：`boid` / `enable`（`rules_chain_all.json` L733-L735）

### R-E03 · 禁用操作由 JobHrDisableOp 处理 🔌

- **可变性**：`pluggable`
- **实现**：`kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp`（`_auto_operations.md` L210 disable 链第 3 位）
- **扩展方式**：继承 + 追加"禁用前检查在职员工"

### R-E04 · 禁用时合法性校验 ⚙️

- **可变性**：`config`
- **证据**：disable validations `f2843bab0000baac` enabled（`_auto_operations.md` L216）

---

## 六、删除规则

### R-D01 · HR 基础资料删除校验 🔒

- **规则**：标品统一走 `hrbddeletevalidator`（默认 enabled）
- **证据**：`_auto_operations.md` L148 delete validation `2+U=J7R7IEF/`
- **可变性**：`hardcoded`（不建议覆盖，避免误删系统预置）

### R-D02 · 禁用状态下不能删除 ⚙️

- **可变性**：`config`（默认 disabled，可启用）
- **证据**：`_auto_operations.md` L146 delete validation `f789ca66000000ac`（`数据已经禁用，不能删除`）

---

## 七、时态版本规则

### R-V01 · 新增数据版本必须校验 🔒

- **可变性**：`hardcoded`
- **证据**：`newhisversion` validations `2K+JH30V7OJV` enabled（`_auto_operations.md` L555-L556）

### R-V02 · 确认变更走完整保存链 🔌

- **可变性**：`pluggable`
- **证据**：`confirmchange` 3 插件链（`HisModelOPCommonPlugin` + `HisUniqueValidateOp` + `JobHrSaveOp`，`_auto_operations.md` L594-L597）+ 3 校验（MustInput + 2 GroupFieldUnique，`_auto_operations.md` L601-L605）

### R-V03 · 变更由 JobHrMsgHandleOp 监听 🔌

- **实现**：`kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp`（`_auto_plugin_semantics.md` L13-L26）
- **读字段**：`boid` / `iscurrentversion` / `sourcevid`
- **写字段**：`sourcevid`
- **生命周期方法**：`beforeExecuteOperationTransaction` / `endOperationTransaction` / `afterExecuteOperationTransaction`
- **父类**：`HRDataBaseOp`
- **用途**：监听数据版本链变更（典型场景：告诉下游"此职位有新版本"）

---

## 八、提交 / 撤销规则

### R-S01 · 提交校验 ⚙️

- **证据**：submit 2 validations（`_auto_operations.md` L271-L274）
  - `1cc0054f000017ac` 合法性 enabled
  - `RS=E9QE25UN` 字段合规性（MustInput）enabled

### R-S02 · 撤销后状态回 A ⚙️

- **证据**：unsubmit parameter `Value: "A"`（`_auto_operations.md` L281）

### R-S03 · 提交并新增有独立组合校验 ⚙️

- **证据**：submitandnew 2 validations（`_auto_operations.md` L322-L325）
  - `2f5773ca0001b5ac` GroupFieldUnique
  - `2f5773ca0001b6ac` FormValidate

---

## 九、字段可变性速查（基于 scene_doc.json 64 字段 minefield）

| Minefield | 数量 | 典型字段 | 规则 |
|---|---|---|---|
| 🔴 red（系统 / 平台维护） | 25 | `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` / `sourcevid` / `sourcesyskey` | `isvCanModify: false`，禁止手改 |
| 🟡 yellow（变更级联下游） | 4 | `status` / `enable` / `bsed` / `bsled` | 改前必测下游引用 |
| ⚪ 无标注（可改） | 35 | 业务字段 | 可按 pattern 扩展 |

---

## 十、规则速查表

| 规则类型 | 数量 | 典型可变性 |
|---|---|---|
| 系统不变式（INV） | 8 | 🔒 hardcoded |
| 列表（R-L） | 4 | 多为 🔌 pluggable |
| 新增 / 修改（R-C） | 5 | 混合 |
| 审核（R-A） | 3 | ⚙️ + 🔌 |
| 启停用（R-E） | 4 | 🔌 pluggable |
| 删除（R-D） | 2 | 🔒 + ⚙️ |
| 时态版本（R-V） | 3 | 🔌 pluggable |
| 提交撤销（R-S） | 3 | ⚙️ config |

---

**📌 来源追溯**：
- INV 1-8：`scene_doc.json` minefield + `rules_chain_all.json` businessRules
- R-L / R-C 清单：`_auto_operations.md` 各 opKey validations 段
- 插件类名：`_auto_plugin_semantics.md` 5 类反编译
- JobHrEnableOp 的 3 个业务规则：`rules_chain_all.json` L690-L741
- JobHrSaveOp 的 2 个业务规则：`rules_chain_all.json` L368-L402

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

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

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin -->

## chgaction 实证补充（HisModelBuFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit -->

## chgaction 实证补充（JobHisBasedataEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit -->

## chgaction 实证补充（JobHisBasedataFiledChangeEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## chgaction 实证补充（HRRelatePageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit -->

## chgaction 实证补充（JobBasedataEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin -->

## chgaction 实证补充（HisModelBuListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin -->

## chgaction 实证补充（JobInitFilterCommonPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.job.JobListPlugin -->

## chgaction 实证补充（JobListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.formplugin.web.job.JobListPlugin`
> 跨类追踪: 13 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.formplugin.web.job.JobListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp -->

## chgaction 实证补充（JobHrSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp -->

## chgaction 实证补充（JobHrAuditOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp -->

## chgaction 实证补充（JobHrDisableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp -->

## chgaction 实证补充（JobHrEnableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
