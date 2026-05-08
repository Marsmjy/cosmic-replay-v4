# core_hr_contract_renew · 上下游逻辑

> 协议：ADR-009 跨云穿透架构
> 数据源：W1 deep_scan_audit.md C/D/H 段（实体 / 调用链 / 跨云引用）

## 上游（本场景的数据源）

### 直接上游

| 来源云 | form / utility | 用途 | 调用方式 | 调用位置 |
|---|---|---|---|---|
| **核心人力云** core_hr | `hlcm_contractapplyrenew` | 合同续签申请单·主消费 | `BusinessDataServiceHelper.load(...)` 反查 | RenewBatchOpPlugin.java:98,107,283 |
| **核心人力云** core_hr | `hlcm_contractapplyrenew` 标品 4 字段 | billstatus / handlestatus / auditstatus / empnumber 校验 | get / set | RenewBatchOpPlugin.java:113,287 |
| **组织发展云** org_dev | `haos_adminorg` | 行政组织·按 structlongnumber 查子组织 | `QueryServiceHelper.query("haos_adminorg",...)` | AdminOrgHrUtils.java:24-46 |
| **组织发展云** org_dev | `bos_org` | 所属组织 BasedataField 引用 | dym + UI 选择器 | renewbatch.dym `${ISV_FLAG}_affiliationord` |
| **基础数据** base | `bos_listf7` (查找弹窗 form) | 选员工弹窗容器 | `ListShowParameter.setBillFormId(...)` | RenewBatchBillPlugin.java:135 |

### 间接上游（通过标品 hlcm 牵引）

| 来源云 | form | 关系 |
|---|---|---|
| 核心人力云 | `hlcm_contract` | 合同主表·hlcm_contractapplyrenew 关联其·HisModel 时序资料 |
| 核心人力云 | `hlcm_contractfileemp` | 员工合同档案·续签前后跟踪 |
| 核心人力云 | `hlcm_empprotocolnew` | 员工协议·关联引用 |
| 核心人力云 | `hlcm_contractapplyend` | 合同到期申请·续签前置触发 |

⚡ **本资产不直接读这些表**·但 `hlcm_contractapplyrenew` 的 audit 链会自动联动它们（标品自治）。

### 上游对本场景的依赖契约

```
[上游] hlcm_contractapplyrenew                  本场景需要的契约
   │
   ├─ id (Long)                                 ← 反查 + 写关联的 PK
   ├─ billstatus (String, 标品)                 ← R-06 校验"C"才放行
   ├─ empnumber (String, 标品)                  ← 异常信息显示
   ├─ affiliationord (BasedataField, 标品)      ← R-03 选员工 list 过滤
   └─ ${ISV_FLAG}_renewbatch (BasedataField, ISV 加) ← 反向引用本场景主表
```

**契约破坏风险**：
- 标品 hlcm 改 billstatus 状态机（如新加 "D" 半审批） → 本资产 R-06 校验需要重新审视
- 标品 hlcm 删 affiliationord 字段 → 本资产 R-03 选员工逻辑无法工作

---

## 下游（本场景的影响范围）

### 直接下游

| 影响目标 | 影响方式 | 同步性 | 影响位置 |
|---|---|---|---|
| `hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch` | 写入主表 ID·建反向引用 | 同事务·强一致 | RenewBatchOpPlugin.java:284,287 |
| `hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch` | 清空 null·解关联 | 同事务·强一致 | RenewBatchOpPlugin.java:296 |

### 间接下游（通过标品 hlcm 牵引）

```
本场景 (ISV 主表 audit)
   ↓ 标品 hlcm_contractapplyrenew 自动跟随 audit
[标品 hlcm 链]
   ├─ hlcm_contract (HisModel 时序资料)
   │     ↓ 标品自动 (afterAudit)
   │  生成新合同版本·iscurrentversion=1
   │
   ├─ hlcm_contractfileemp (合同员工档案)
   │     ↓ 标品自动
   │  归档续签后状态
   │
   └─ BEC 事件中心 (标品自带)
         ↓ 异步
      hpfs_chgrecord.aftereffect 等通用事件
         ↓ 跨云订阅
      薪酬云 swc / 考勤云 wtc / 福利云 hbjm 跟随更新
```

⚡ **关键铁律**（PR-011）：本资产**不发任何 BEC 事件**·跨云联动靠**标品 hlcm 自动派发**·ISV 不重写。

### 跨云穿透矩阵

| 跨云目标 | 触发链路 | 延迟 | 一致性 |
|---|---|---|---|
| 薪酬云 swc | 标品 hlcm audit → BEC `hpfs_chgrecord.effect` → swc 订阅 | 秒级（异步） | 最终一致 |
| 考勤云 wtc | 同上 → wtc 订阅 | 秒级 | 最终一致 |
| 福利云 hbjm | 同上 → hbjm 订阅 | 秒级 | 最终一致 |
| 报表云 hrptmc | 标品 hlcm 数据快照·定时同步 | 分钟级 | 弱一致 |

**ISV 跨云改造点**：
- 客户希望"批次审核完成立即通知薪酬云"·**不要**在本资产 OpPlugin 加 BEC 发布·应该让标品 hlcm 自动派发·或加客户独立的 ISV 跨云通知插件
- 详见 [`knowledge/_shared/platform_rules.json` PR-011 BEC 事件中心](../../_shared/platform_rules.json)

---

## 跨云引用反向报告

### 本场景被跨云引用情况

```bash
# 查 ${ISV_FLAG}_hlcm_renewbatch 在跨云被引用情况（应该是 0·因为是 ISV 自建）
python scripts/probe_cross_cloud_refs.py --form '${ISV_FLAG}_hlcm_renewbatch'
# → 0 跨云引用·符合 ISV 隔离设计
```

### 本场景跨云引用情况

```bash
# 查本场景 5 java 类引用了哪些跨云 form
python scripts/probe_cross_cloud_refs.py --asset contract_renew_batch
# 输出：
#   core_hr / hlcm_contractapplyrenew (主消费)
#   core_hr / hlcm_contract (HisModel·间接)
#   org_dev / haos_adminorg (只读)
#   org_dev / bos_org (BasedataField)
```

---

## ISV 扩展跨云联动建议

如果客户需要"批次审核完成 → 立即触发自定义跨云逻辑"：

### 推荐模式（PR-001 + PR-011）

```
${ISV_FLAG}_hlcm_renewbatch (ISV 主表)
  ↓ audit
RenewBatchOpPlugin.afterExecute (本资产·**不发 BEC**)
  ↓
[ISV 自建独立插件]
${ISV_FLAG}_RenewBatchEventPublisher.java (新建)
  ↓ 调 EventService 发自定义 ISV 事件
event: ${ISV_FLAG}_renewbatch.audited
  ↓ 客户自配订阅
跨云接收方（薪酬 / 考勤 / 自定义系统）
```

### 反指引

- ❌ 不要在本资产 RenewBatchOpPlugin 直接加 EventService.publish·会污染共用插件
- ❌ 不要重发标品 hlcm 自带的 BEC（重复事件）
- ❌ 不要直接调跨云 RPC（违反 PR-011 BEC 中心化）

---

## 关联资产

- 资产复刻：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/)
- 部署 SOP：[`_assets/contract_renew_batch/deploy_sop.md`](../../_assets/contract_renew_batch/deploy_sop.md)
- 扩展点：[`_assets/contract_renew_batch/customization_points.md`](../../_assets/contract_renew_batch/customization_points.md)

## 关联文档

- [05_data_flow.md 跨云数据流](05_data_flow.md)
- [08_impact_analysis.md 跨云影响](08_impact_analysis.md)
- [ADR-009 跨云穿透架构](../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md)
- [`knowledge/_shared/platform_rules.json` PR-011 BEC 事件中心](../../_shared/platform_rules.json)
