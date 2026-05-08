# 业务规则 · 组织调整申请单

> **状态**: 🟢 基于 8 类反编译实证 + rules_chain 73 opKey + Validator 链全量梳理
> **confidence**: verified
> **数据源**: 反编译 OP 注册的 Validator + opKey 元数据 + AdminOrgBatchBillPlugin 拦截逻辑

---

## 一、单据状态机（核心规则）

```
┌─────────┐                                                        ┌─────────┐
│  EDIT   │ ── save ──► ┌────────┐ ── submit ────►┌─────────┐ ── audit ──► │ 已生效 C │
│ (新建)   │             │ 暂存 A │                │ 待审 B  │              └─────────┘
└─────────┘             └────────┘                └─────────┘                  ▲
                              │                        │                       │
                              │ submiteffect           │ unsubmit              │
                              └─────────┐              └─────────┐             │
                                        ▼                        │             │
                                   ┌─────────┐                   ▼             │
                                   │ 已生效 C ├──── breakup ────►┌─────────┐    │
                                   │  (跳审) │                  │ 终止 G  │    │
                                   └─────────┘                  └─────────┘    │
                                                                                │
                                   discard_row（列表）                           │
                                        │                                       │
                                        ▼                                       │
                                   ┌─────────┐                                  │
                                   │ 废弃 F  │                                  │
                                   └─────────┘                                  │
```

### 状态约束规则（实证来源 · 不脑补）

| 规则 ID | 规则 | 实证 |
|---|---|---|
| BR-STATE-01 | A/G 状态才允许 import_multientry_hr 导入分录数据 | `AdminOrgBatchBillPlugin.java:448` |
| BR-STATE-02 | 删除 entry 行：A/G 状态可删；wftask 审批人 + B 状态可删；其他状态拒绝 | `AdminOrgBatchBillPlugin.java:633-637` |
| BR-STATE-03 | breakup 一次只能选 1 条单据（`AdminOrgBatchBillBreakupStatusValidator` 拦截非 A/B/C 状态） | `AdminOrgBatchBillListPlugin.java:312` |
| BR-STATE-04 | discard_row 只改 billstatus = F · 不撤销已生效到 haos_adminorg 的版本（不可逆） | `AdminOrgBatchBillListPlugin.java:357-360` |
| BR-STATE-05 | submit 时强制先调 save_no_log（silent save · 防"提交但未保存"） | `AdminOrgBatchBillPlugin.java:454-462` |

---

## 二、必填规则（来自 entry XML 元数据 + Validator 链）

### 主表必填

```
billno         必填 · BillNoField · CodeRuleOp 自动生成
billstatus     必填 · BillStatusField · 系统维护
auditstatus    必填 · BillStatusField · 系统维护
org            必填 · OrgField · 用户必选 BU
effdt          必填 · DateField · 用户必填生效日
```

### 7 entry 必填（取自 `_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md`）

| Entry | 必填字段 | 实证 |
|---|---|---|
| `entryentity_add` | `add_adminorgtype` ✓ / `add_changescene` ✓ / `add_name` ✓ / `add_parentorg` ✓ / `add_org` ✓ | OpenAPI 元数据 mustInput=true |
| `entryentity_parent` | `parent_adminorg` ✓ / `parent_parentorg` ✓ / `parent_name` ✓ / `parent_changescene` ✓ / `parent_number` ✓ / `parent_adminorgtype` ✓ / `parent_org` ✓ | 同上 |
| `entryentity_info` | `info_adminorg` ✓ / `info_name` ✓ / `info_number` ✓ / `info_changescene` ✓ / `info_changetype` ✓ / `info_adminorgtype` ✓ / `info_org` ✓ | 同上 |
| `entryentity_disable` | `disable_adminorg` ✓ / `disable_changescene` ✓ / `disable_changetype` ✓ / `disable_org` ✓ | 同上 |
| `entryentity_merge` | （拼模型 · 详见反编译 `AdminOrgBatchBillPlugin.java:805-832` F7 联动） | UI 强制选 |
| `entryentity_split` | （拼模型 · 同上 split） | UI 强制选 |

⚠️ **add_number 不必填**：用户可不填编码 · `OrgBatchBillSubmitAndEffectiveOp.setAddOrgNumber` 在生效时按法人维度的编码规则自动补全（实证 L75-110）。

---

## 三、Validator 注册清单（onAddValidators 实证）

来自 `_auto_plugin_registry.md` "onAddValidators" 段 + 反编译实证：

| OP | Validator | 用途 | 实证 |
|---|---|---|---|
| `OrgBatchBillSaveOp` | `OrgBatchBillSaveAndSubmitValidator` | save / submit 通用业务校验 | OrgBatchBillSaveOp.java:42 |
| `OrgBatchBillSaveOp` | `AdminOrgBillSaveValidator` | 行政组织域规则校验 | OrgBatchBillSaveOp.java:43 |
| `OrgBatchBillSubmitAndEffectiveOp` | （仅 super.onAddValidators · 复用 save 的 Validator） | submiteffect 复用 save 链 | OrgBatchBillSubmitAndEffectiveOp.java:58-60 |
| `OrgBatchChgBillEffectOp` | （仅 super.onAddValidators） | audit 链复用 | OrgBatchChgBillEffectOp.java:62-64 |
| `OrgBatchBillAuditSaveOp` | （未反编译 · 标品内部 · 不可继承） | audit 时同步保存 | _auto_plugin_registry.md L100 |
| `AdminOrgBatchBreakupOp` | `AdminOrgBatchBillBreakupStatusValidator` | 终止状态校验 | AdminOrgBatchBreakupOp.java:26 |
| `HRCodeRuleOp` | （平台编码规则 Validator） | 编码格式 + 唯一性 | _auto_plugin_registry.md L97 |

⚠️ **关键点**：
- `OrgBatchBillSaveAndSubmitValidator` / `AdminOrgBillSaveValidator` 等都是**标品内部类** · ISV **不可直接继承**（PR-001）· 要扩展只能挂**新 Validator** 跟标品并列跑
- `OrgBatchBillSubmitAndEffectiveOp` / `OrgBatchChgBillEffectOp` 的 `onAddValidators` 都仅 super · 等于复用 save 的 Validator 链 · ISV 加 Validator 在哪个 OP 注册都生效（建议挂在 save · 一处生效全链）

---

## 四、跨 entry 互斥规则（防同 org 多操作）

来源：`AdminOrgBatchBillPlugin.java:662-704`（`showMessageForMergeAndSplit`）

**规则**：同一行政组织在一张申请单内**不能同时被多种类型的 entry 操作**。

**冲突矩阵**（X = 不允许）：

| | add | parent | info | disable | merge | split |
|---|---|---|---|---|---|---|
| add | ✅ 同名重复检查 | X | X | X | X | X |
| parent | X | ✅ | X | X | X | X |
| info | X | X | ✅ | X | X | X |
| disable | X | X | X | ✅ | X | X |
| merge | X | X | X | X | ✅ | X |
| split | X | X | X | X | X | ✅ |

**实证（删除 merge/split 行时检测）**：

```java
// AdminOrgBatchBillPlugin.java:691-694
showMsg.append(String.format(
    "行政组织"%1$s"在本单中已经发起"%2$s"操作，不允许同时对组织进行多种变动操作。",
    entry.getValue(),
    BillEntryHelperEnum.getEntryDescriptionByChangeType(orgIdToChangeType.get(entry.getKey()))
));
```

---

## 五、数据权限规则（多处分散实证）

### 5.1 BU 数据权限过滤（贯穿全流程）

`AdminOrgBatchBillPlugin.java:943-946` 实证：所有 F7 字段必须按当前用户的 BU 过滤可见组织 · 走 `BaseDataHelper.getAdminOrgBaseDataFilter("haos_adminorgdetail", [bu_id])`。

```java
private void setFilterByOrg(BeforeF7SelectEvent beforeF7SelectEvent) {
    QFilter baseDataFilter = BaseDataHelper.getAdminOrgBaseDataFilter(
        "haos_adminorgdetail",
        Collections.singletonList(((DynamicObject)this.getModel().getValue("org")).getLong("id"))
    );
    beforeF7SelectEvent.getCustomQFilters().add(baseDataFilter);
}
```

### 5.2 列表权限组织（`disorg` 签发组织）

`AdminOrgBatchBillListPlugin.java:268-289`（beforeShowBill）：列表打开新建表单时 · 默认设置 `customParam.businessUnit` 为当前用户首选 BU · 用户在列表过滤时也写到 PageCache。

### 5.3 操作级权限（删除/废弃必查权限位）

`AdminOrgBatchBillListPlugin.java:336-340`（confirmCallBack del_row）：
```java
if (!this.checkUsePermission("4715e1f1000000ac")) {
    showMessage("您的"组织调整申请列表"的删除权限已发生变更...");
    return;
}
```

权限码：
- 删除：`4715e1f1000000ac`
- 废弃：`0=KX5+R7YTRT`

→ ISV 自定义按钮要遵循同样的权限码校验 · 不要绕过 PermissionServiceHelper 直接执行。

---

## 六、F7 字段联动业务规则（30 字段 · 见 04_business_flow §四）

### 6.1 changetype → changescene 链（强约束）

```
当 entry.changetype 改变 → 重置 entry.changescene + entry.changereason
当 entry.changescene 改变 → 重置 entry.changereason
```

**`{prefix}_changescene` F7 过滤实证**（AdminOrgBatchBillPlugin.java:833-841）：
```java
QFilter changeTyeFilter = new QFilter("orgchangetype.id", "=",
    focusEntryDyn.getLong(prefix + "_changetype.id"))
    .and("id", "!=", OrgBatchChgBillConstants.CHANGE_SCENE_ENABLE);
```

→ 变动场景必须匹配当前变动类型 · 且**排除"启用"场景**（"启用"是 admin_org_quick 走的 · 申请单里不允许）。

### 6.2 companyarea → city 链

```
当 entry.companyarea(国家) 改变 → 重置 entry.city(城市)
city F7 过滤：country = companyarea.id
```

实证 `AdminOrgBatchBillPlugin.java:859-869`。

### 6.3 parent_adminorg 业务规则

```
parent_adminorg F7：
  - 排除根组织（boid != 平台 root）
  - 注入 searchdate = 当前日期（按当前时态查可见组织）
  - 排除虚拟组织 (isvirtualorg != '1')
  - 走 BU 数据权限过滤
```

实证 `AdminOrgBatchBillPlugin.java:777-784`。

### 6.4 merge / split target 联动

```
merge_target_org / split_target_org F7：
  - 排除已停用 entry 中的组织
  - 包含 add entry 中的新组织（允许把新建的作为目标）
  - 跟 'iscurrentversion = 0' 或 'id ∈ add 列表' 取并集
```

实证 `AdminOrgBatchBillPlugin.java:803-832`。

---

## 七、编码规则约束

### 7.1 add 编码生成（OrgBatchBillSubmitAndEffectiveOp.setAddOrgNumber）

来源 `OrgBatchBillSubmitAndEffectiveOp.java:75-110`：

```
对每个 add entry · 若 number 为空：
  1. 找该 entry 的 belongcompany（法律实体）
  2. 复制 entry 字段成临时 haos_adminorgdetail 对象（带 parent/orgtype/company 上下文）
  3. 查该法人是否配了 haos_adminorgdetail 编码规则（CodeRuleServiceHelper.isExist）
  4. 若有 · 调 OrgBatchBillSaveHelper.setOrgNumber(addDys, adminorgHrDys, numberList)
     按规则生成编码并回写到 entry.number
```

⚠️ **业务约束**：
- 必须先选 `add_corporateorg`（法律实体）才能生成编码（编码规则按法人维度配置）
- 法人没配编码规则 → entry.number 保持空 → 用户必须手填（否则保存失败）
- ISV 自定义编码规则必须按 `haos_adminorgdetail` 维度配（不是 `homs_orgbatchchgbill`）

### 7.2 单据编码生成

`AdminOrgBatchBillPlugin.afterCreateNewData`（L236-260）实证：
- 按 `homs_orgbatchchgbill` + 当前 `org`（BU）配置编码规则
- 若有规则 · CodeRuleOp 自动生成 billno
- 若无 · 用户必须手填

---

## 八、状态字段联动业务（隐藏依赖）

### 8.1 待停用标记联动（`tobedisableflag`）

只有 parent / info / disable 这 3 类 entry 有 `*_tobedisableflag` 字段。业务规则：
- `tobedisableflag = true` 表示组织变更后**待停用**（业务过渡期标记）
- 实际停用时机由 `bsled`（生效终止日）控制 · 不是立即生效

### 8.2 organizationchart 联动（`istodiagram` · 主表）

主表的 `istodiagram = true` 时 · 提交后会自动打开组织架构图视图 · 见 `AdminOrgBatchBillPlugin.afterDoOperation` 各 entry 的 `AdminOrgBatchChartBaseHelper.openMerge*Page` / `openSplit` 跳转逻辑。

---

## 九、ISV 扩展约束（哪些规则不能改）

### 🔴 红线（违反平台铁律）

| 规则 | 原因 |
|---|---|
| ❌ 不要往 `entryentity_all` 加字段 | 它是只读视图 · 不写真分录 |
| ❌ 不要扩展继承 `OrgBatchBillSaveOp` / `OrgBatchBillSubmitOp` 等标品 OP | 都是 final 类 + 标品专属 · 违反 PR-001 |
| ❌ 不要继承 `OrgBatchBillSaveAndSubmitValidator` / `AdminOrgBillSaveValidator` | 标品内部 Validator · 不在 SDK 白名单 |
| ❌ 不要直接 set `billstatus` | 状态机由标品 OP 控制 · 越过去会破坏状态一致性 |
| ❌ 不要让"同 org 多 entry"通过 | 平台跨 entry 互斥规则不能绕（参考 §四） |
| ❌ 不要扩展 entry 间转换 | 比如把 add 数据"转"成 parent 数据 · 平台无此 API |

### 🟢 安全扩展点（PR-001 + PR-002）

| 扩展点 | 父类 | 走法 |
|---|---|---|
| 加 entry 字段 | （不需要 OP）| modifyMeta op=add elementType=field 挂 entry parentScope |
| 加业务校验 | `AbstractValidator` 独立实现 | 挂在 save 的 onAddValidators · 自动级联到 submit/submiteffect/audit |
| 加事务前阻断 | `HRDataBaseOp` | 重写 beforeExecuteOperationTransaction · 并列挂 |
| 加事务后通知 | `IEventServicePlugin` | BEC 订阅方 · 不挂 OP |
| 加 F7 过滤 | （表单级 · 通过新 FormPlugin） | extends `HRCoreBaseBillEdit` · 自定义 beforeF7Select |

---

## 十、典型业务流程小结

### 流程 A · 标准走审批

```
新建 → 填 7 entry 任意 → save (A) → submit (B) → wf 审批 → audit (C 落 haos_adminorg)
                                                          ↓
                                                  AdminChangeMsgService 派 sch_task
                                                          ↓
                                                  异步发 BEC 通知所有订阅方
```

### 流程 B · 跳过审批快速生效

```
新建 → 填 7 entry → save (A) → submiteffect (直接 C 落 haos_adminorg + 自动补编码)
                                          ↓
                                  AdminChangeMsgService 派 sch_task
                                          ↓
                                  异步发 BEC 通知所有订阅方
```

### 流程 C · 终止已生效单

```
已生效 (C) → breakup → AdminOrgBatchBillBreakupStatusValidator 校验
                              ↓
                              (校验通过)
                              ↓
                      OrgBatchBreakupService.doBreakUpBill
                              ↓
                      反向回退 haos_adminorg 版本（高风险 · 通常需要授权）
                              ↓
                       billstatus = G (终止)
```

---

**📌 来源追溯**：

- 状态机字段：`OrgBatchBillSubmitOp.java:36` + `AdminOrgBatchBillListPlugin.java:358` + `AdminOrgBatchBillPlugin.java:493`
- A/G 导入约束：`AdminOrgBatchBillPlugin.java:448`
- 删除状态约束：`AdminOrgBatchBillPlugin.java:633-637`
- breakup 单条约束：`AdminOrgBatchBillListPlugin.java:312`
- Validator 注册清单：8 个反编译类 onAddValidators 段
- 跨 entry 互斥：`AdminOrgBatchBillPlugin.java:662-704`
- F7 联动 30 字段：`AdminOrgBatchBillPlugin.java:749-884`
- 权限码：`AdminOrgBatchBillListPlugin.java:336-352`
- 编码生成：`OrgBatchBillSubmitAndEffectiveOp.java:75-110`
- 异步派发：`AdminChangeMsgService.java:113-123`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## chgaction 实证补充（HRPermCommonEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## chgaction 实证补充（HRBaseUeEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin -->

## chgaction 实证补充（AdminOrgBatchBillPlugin 跨类追踪聚合）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin -->

## chgaction 实证补充（AdminorgCubeViewPlugin 跨类追踪聚合）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin`
> 跨类追踪: 39 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode(CHART_TREE_NODE_LIST, message), new Object[0] |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin -->

## chgaction 实证补充（AdminOrgBatchBillListPlugin 跨类追踪聚合）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin`
> 跨类追踪: 31 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp -->

## chgaction 实证补充（OrgBatchBillSaveOp 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("compare", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp -->

## chgaction 实证补充（OrgBatchBillSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_orgbatchchgbill` | updateOne | HRBaseServiceHelper | <self> (depth=0) |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("getOrgRelateVersion", message), new Object[0] |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin -->

## chgaction 实证补充（AdminOrgBatchBillDiscardOpPlugin 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp -->

## chgaction 实证补充（OrgBatchChgBillEffectOp 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |

### 调用的核心 Service（Top 10）
- `billBatchEffectService.batchEffect`
- `billBatchEffectService.afterBatchEffect`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp -->

## chgaction 实证补充（OrgBatchBillAuditSaveOp 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("compare", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp -->

## chgaction 实证补充（AdminOrgBatchBreakupOp 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin -->

## chgaction 实证补充（AdminOrgBatchBillDeleteOpPlugin 跨类追踪聚合）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin`
> 跨类追踪: 34 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `homs_orgbatchchgbill` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgstruct` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_orgteamcooprel` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteOne | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `haos_adminorgdetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.batchbill.repository.AdminOrgBatc |
| `homs_batchorgentity` | deleteByFilter | HRBaseServiceHelper | kd.hr.homs.business.domain.orgbatch.service.impl.OrgBatchBil |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin -->
