# core_hr_org_unit_transfer · 业务规则

> 数据源：deep_scan_audit.md (case_001 真扫) + 成建制划转复用说明书.docx §2.2
> 日期：2026-05-06

## R-01 · changeType 状态机（A/B/C）

3 种 changeType 由 3 个独立调度任务类处理·**完全独立**·不可在同一任务内混跑：

| changeType | 任务类 (FQN) | 业务含义 | 过滤变动场景编码 | 数据源 form |
|:---:|---|---|---|---|
| A | `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgParentChgTask` | 组织上级调整（成建制划转） | `Sets.newHashSet("1020_S")` | `homs_orgchgrecord` |
| B | `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgRenameChgTask` | 组织更名 | `Sets.newHashSet("1030_S")` | `homs_orgchgrecord` |
| C | `${ISV_FLAG}.hr.hdm.orgtransfer.business.PostRenameChgTask` | 岗位更名 | `Sets.newHashSet("1020_S","1080_S")` | `hbpm_chgrecordevt` |

**实证锚点**：
- A: OrgParentChgTask.java:30 / :28
- B: OrgRenameChgTask.java:35 / :33
- C: PostRenameChgTask.java:30 / :28

**📖 docx §2.2 关键码值表**（`${ISV_FLAG}_changetype` 字段）：A=组织上级调整 / B=组织更名 / C=岗位更名 — 与代码 100% 一致 ⛓

## R-02 · 执行状态机（中间表 `${ISV_FLAG}_orguntitransfer.${ISV_FLAG}_executestatus`）

3 状态·驱动调度任务消费 + 重试：

| 状态码 | 含义 | 谁写入 | 转换条件 |
|:---:|---|---|---|
| `01` | 待执行 | 上一轮调度落表（OrgTransferHelper.java:354 过滤 `executestatus=01` 取数）| 默认初始 |
| `02` | 执行中 | 文档定义有此状态·**真代码未见赋值**（待 ISV 确认是否手动状态）| — |
| `03` | 执行完成 | 4 处赋值·成功失败都写 | 标品 `submiteffect` 成功后写入 |

**实证锚点**（`set("${ISV_FLAG}_executestatus", "03")`）：
- AbstractCommonTask.java:310 (基类 saveAndEffectBatchBill 通用)
- OrgParentChgTask.java:106 (A 流程·无人员场景)
- OrgRenameChgTask.java:112 (B 流程·无人员场景)
- PostRenameChgTask.java:125 (C 流程·无人员场景)

**📖 docx §2.2**：执行状态 (`${ISV_FLAG}_executestatus`) — 01 待执行 / 02 执行中 / 03 执行完成

## R-03 · 成建制划转 4 步骤主流程

每个 changeType 的 `execute()` 走相同 4 步骤模式（差异仅在数据源 + 过滤集）：

```
Step 1 · 数据源拉变更消息
  A/B → OrgTransferHelper.queryHAOSChangeMsg(taskDays, scenes)  [homs_orgchgrecord]
  C   → OrgTransferHelper.queryPosChangeMsg(taskDays, scenes)   [hbpm_chgrecordevt]

Step 2 · 转待处理列表 → 落 ISV 中间表
  → OrgTransferHelper.getOrgTransferDyList(...)
  → SaveServiceHelper.save(transferDyList) → ${ISV_FLAG}_orguntitransfer

Step 3 · 拉中间表 executestatus=01 的数据·批处理
  → OrgTransferHelper.queryOrgTranArr(changeType)
  → handleOrgTransfer(orgTranList, changeType)

Step 4 · 每条记录走"建调动单 + 提交生效"
  for each orgTran:
    - 拉子组织 (A/B 才走) → 拉任职经历 → 历史版本刷新
    - buildBatchTranBill → 落 hdm_transferbatch + hdm_transferbatchentry
    - saveAndEffectBatchBill → executeOperate(SAVE / SUBMITEFFECT)
    - 成功 → executestatus=03 / 失败 → 写 resultmsg·状态保留 01 等下次重试
```

## R-04 · 调动单生成铁律（核心架构原则）

**绝对铁律**：本场景**不得私改 `hrpi_*` 任职经历底表**·所有底表协同**完全交给标品 `hdm_transferbatch.submiteffect` op**。

**实证锚点**：
- 标品 op 调用：`AbstractCommonTask.java:279` `executeOperate(HRBaseConstants.SAVE, "hdm_transferbatch", ...)`
- 标品 op 调用：`AbstractCommonTask.java:293` `executeOperate(HRBaseConstants.SUBMITEFFECT, "hdm_transferbatch", ...)`

**反例（旧 hbis 客户版做法·已废弃）**：
```java
// ❌ 反模式：私自 MQ 通知改 8 实体（hrpi_personrolerel 等）
ChgRecordUtil.packageMsg(chgRecordMap);
MQFactory.get().createSimplePublisher("hr", "hpfs_chgexternalrecord_queue");
```

→ 详见 [`_antipatterns.json`](../../_antipatterns.json) AP-01x: "ISV 不应私改 hrpi 底表"

## R-05 · A/B vs C 数据维度差异

| 维度 | A/B (组织维度) | C (岗位维度) |
|---|---|---|
| 主键字段 | `${ISV_FLAG}_orgid` (组织 ID) | `${ISV_FLAG}_orgid` (实际是岗位 ID·字段名同) |
| 是否拉子集 | ✅ 拉子组织 (`IHAOSBatchAdminOrgInfoQueryService.querySubOrgToList(List<Long> orgIdList, Date date, Integer level)` · level=null 表示所有层级) | ❌ 不拉（岗位无层级） |
| 历史版本表 | `haos_adminorgdetail` (`OrgTransferHelper.java:98`) | `hbpm_positionhr` (`OrgTransferHelper.java:114, 128`) |
| 任职经历刷新方法 | `changeEmpPosOrgRelHisDataByOrg(orgSet, effectDate)` (AbstractCommonTask.java:336) | `changeEmpPosOrgRelHisDataByPos(posSet, effectDate)` (AbstractCommonTask.java:387) |
| 任职经历查询 API | `getInEffectDateEmpPosOrgRelArrByOrgIdSet` (`OrgTransferHelper.java:245`) | `getEmpPosOrgRelArrByPosId` (`OrgTransferHelper.java:289`) |
| 调动单"业务名" | "组织变更上级" (A) / "组织更名" (B) | "岗位更名" |

详细代码片段双侧对照：[deep_scan_diff.md](../../../dcs_regression/pending/case_001_org_unit_transfer/deep_scan_diff.md) 对照 1。

## R-06 · 时态字段处理（HisModel）

**iscurrentversion 过滤铁律**：查历史组织 / 历史岗位时·**必须** `qFilter.and("iscurrentversion", QCP.not_equals, "1")`·只取过去/未来版本（不包含当前版本）。

**实证锚点**：
- 历史组织：OrgTransferHelper.java:94
- 历史岗位：OrgTransferHelper.java:110

**为什么**：当前版本 `iscurrentversion=1` 是**业务上的"当前活跃版本"**·不属于"历史版本"·成建制划转只刷"任职经历指向的历史 vid"·不动当前版本。

## R-07 · 错误处理 + 幂等

每条 orgTran 处理失败·状态**保留 `01`**（不写 `03`）·下次调度自动重试。
错误信息写入：
- `${ISV_FLAG}_resultmsg` (≤255 字符·UI 列展示)
- `${ISV_FLAG}_resultmsg_tag` (完整异常·tag 字段无长度限制)

**实证锚点**：AbstractCommonTask.java:312-313 / OrgParentChgTask.java:108 / OrgRenameChgTask.java:114 / PostRenameChgTask.java:127

## R-08 · 默认调度参数

| 参数 | 默认值 | 修改方式 |
|---|---|---|
| `EXECUTE_DAYS` (任务执行天数) | 1 | 改 AbstractCommonTask.java:48 |
| 调度周期 | 由 schdata 决定·建议每日 cron | 改 schedule_hdm_orgtransfer_plan_SKDP_S.schdata |
| 调度计划编码 | `hdm_orgtransfer_plan_SKDP_S` | 不建议改·跟 docx §2.2 一致 |

## 与 v3 知识库的关联

- **资产复刻**：本规则对应的可执行代码模板在 [`_assets/org_unit_transfer/code_templates/`](../../_assets/org_unit_transfer/code_templates/)
- **扩展点**：详见 [`_assets/org_unit_transfer/customization_points.md`](../../_assets/org_unit_transfer/customization_points.md) EP-01 ~ EP-06
- **意图路由**：本场景由 `_intent_routing.json` 命中"成建制划转 / 批量调动 / 组织上级调整"等意图
- **跨场景关系**：详见 `_scene_relations.json` 中本场景的 upstream / downstream 双向引用
