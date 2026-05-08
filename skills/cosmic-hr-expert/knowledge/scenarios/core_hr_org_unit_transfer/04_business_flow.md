# core_hr_org_unit_transfer · 业务流程

> 数据源：deep_scan_audit.md H 段调用链全图 / deep_scan_diff.md 三任务对照 / docx §1.3 业务方案描述

## 主流程时序图

```
┌─────────┐  ┌────────────┐  ┌────────────┐  ┌─────────────────┐  ┌─────────────────┐
│ HR 业务  │  │ 标品消息表 │  │ ISV 中间表 │  │ 任务 (A/B/C)     │  │ 标品调动单       │
│ 操作员    │  │  homs/hbpm │  │            │  │  调度执行        │  │ hdm_transferbatch│
└────┬────┘  └─────┬──────┘  └─────┬──────┘  └────────┬────────┘  └────────┬────────┘
     │              │                │                  │                     │
     │ 改组织/岗位   │                │                  │                     │
     │─────────────>│                │                  │                     │
     │              │                │                  │                     │
     │              │  (变更明细记录) │                  │                     │
     │              │                │                  │                     │
     │              │                │                  │ 每日定时调度        │
     │              │                │                  │ getTaskDays()       │
     │              │ <────拉变更─── │                  │ queryHAOSChangeMsg  │
     │              │                │                  │ / queryPosChangeMsg │
     │              │                │                  │                     │
     │              │                │ <─SaveServiceHelper.save              │
     │              │                │   getOrgTransferDyList()              │
     │              │                │                  │                     │
     │              │                │ ──────拉 01 待处理─>│                  │
     │              │                │   queryOrgTranArr  │                  │
     │              │                │                  │                     │
     │              │                │                  │ for each orgTran:   │
     │              │                │                  │   拉子组织 (A/B)    │
     │              │                │                  │   拉历史 ergel      │
     │              │                │                  │   buildBatchTranBill│
     │              │                │                  │ ─────SAVE──────────>│
     │              │                │                  │ ─────SUBMITEFFECT──>│
     │              │                │                  │                     │
     │              │                │ <─状态写 03─────  │                     │
     │              │                │   或失败保留 01   │                     │
     │              │                │                  │                     │
     │              │                │                  │   标品自动协同      │
     │              │                │                  │   hrpi_empposorgrel │
     │              │                │                  │   hpfs_chgrecord    │
     │              │                │                  │   跨云事件 (BEC)    │
```

## 调用链全图（changeType=A 完整路径）

详见 [deep_scan_audit.md H 段](../../../dcs_regression/pending/case_001_org_unit_transfer/deep_scan_audit.md)·此处简化：

```
[调度计划 hdm_orgtransfer_plan_SKDP_S 触发]
  ↓
OrgParentChgTask.execute(requestContext, map)              [OrgParentChgTask.java:33]
  ↓ Step 1 · 拉变更消息
  AbstractCommonTask.getTaskDays(map)                       [AbstractCommonTask.java:438]
  OrgTransferHelper.queryHAOSChangeMsg(taskDays, {"1020_S"}) [OrgTransferHelper.java:32]
    └→ HRBaseServiceHelper("homs_orgchgrecord").query(...)
  ↓ Step 2 · 转待处理列表 → ISV 中间表
  OrgTransferHelper.getOrgTransferDyList(changeMsgs, "A", scenes) [OrgTransferHelper.java:155]
    └→ TransferEntity (model 转换)
    └→ DynamicObject 数组 (${ISV_FLAG}_orguntitransfer)
  SaveServiceHelper.save(transferDyList)
  ↓ Step 3 · 拉中间表待处理
  OrgTransferHelper.queryOrgTranArr("A")                    [OrgTransferHelper.java:354]
    └→ HRBaseServiceHelper("${ISV_FLAG}_orguntitransfer")
       .load(executestatus="01" AND changetype="A")
  ↓ Step 4 · 批处理
  handleOrgTransfer(orgTranList, "A")                       [OrgParentChgTask.java:76]
    for each orgTran:
      - effectDate = orgTran.getDate("${ISV_FLAG}_takeeffectdate")
      - 拉子组织 (HRMServiceHelper.invokeHRMPService)
      - changeEmpPosOrgRelHisDataByOrg(orgSet, effectDate)  [AbstractCommonTask.java:336]
        └→ 刷新生效日之后任职经历的 adminorgvid 字段
      - 拉任职经历 OrgTransferHelper.getInEffectDateEmpPosOrgRelArrByOrgIdSet  [:245]
      - buildBatchTranBill(empPosOrgRelArr, orgId, effectDate, "A") [AbstractCommonTask.java:61]
        └→ 落 hdm_transferbatch + entryentity 子表
      - saveAndEffectBatchBill(orgTran, batchTranBill, orgName, "组织变更上级") [AbstractCommonTask.java:270]
        ├→ executeOperate(SAVE, "hdm_transferbatch", ...)   [:279]
        ├→ executeOperate(SUBMITEFFECT, "hdm_transferbatch", ...) [:293]
        ├─ 成功 → orgTran.set("${ISV_FLAG}_executestatus", "03")
        └─ 失败 → 写 ${ISV_FLAG}_resultmsg / _resultmsg_tag·状态保留 "01"
  SaveServiceHelper.save(orgTranList)
```

## 三流程对比（A vs B vs C）

详见 [deep_scan_diff.md](../../../dcs_regression/pending/case_001_org_unit_transfer/deep_scan_diff.md) 对照 1。关键差异：

| 步骤 | A | B | C |
|---|---|---|---|
| 数据源 | `homs_orgchgrecord` | `homs_orgchgrecord` | `hbpm_chgrecordevt` |
| 是否拉子组织 | ✅ | ✅ | ❌（岗位无层级）|
| 历史版本刷新 | ByOrg | ByOrg | ByPos |
| 任职 API | ByOrgIdSet | ByOrgIdSet | ByPosId |
| 预处理 | 无 | 无 | ✅ queryPosInfo 建岗位→组织映射 |
| 调动单"业务名" | "组织变更上级" | "组织更名" | "岗位更名" |

## 异常路径

详见 10_exceptions.md·主要 3 类：
- 当前组织/岗位下无人员 → 状态 03 + resultmsg "无对应人员"·跳过
- save 调动单失败 → 状态保留 01 + 详细错误·下次重试
- submiteffect 失败 → 同上
