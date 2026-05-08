# core_hr_pfs_chgrecord · 业务流程

> **聚合场景**：core_hr_pfs_chgrecord · 包含 3 个 hbss 字典实体（**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.begin...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.beginOperationTransaction 调 IPersonGenericService.deleteBatch + saveBatch 实际改人员底表 → processPersonFlow 写流入流出 → processChargePerson 写部门负责人 → syncEmployeeLicenseCert 同步许可证。**datastatus 状态机**：0=待生效 / 1=已生效 / 2=未来生效定时（effecttime>now 且 future-effect 配置）。异常时 PerChgStatusEnum.SMALLEST_FAIL 写回单据 + 触发 aftereffect op。5 关键 OP：ChgRecordEffectOp / DiscardOp / AfterEffectOp / ChgRollbackOp / ChgRecordEditPlugin。

## 涉及实体（3 个）

- `hpfs_chgrecord`
- `hpfs_chgrecordentry`
- `hpfs_changerecord`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 业务流转 · ChgRecordEffectOp 完整调用链（反编译实证 · 250 行核心方法）

```java
beginOperationTransaction(BeginOperationTransactionArgs args) {
  1. 收集 chgrecord，按 effecttime 分类：
     - 即时生效 (effecttime ≤ now or future-effect 关) → datastatus=0
     - 未来生效 → datastatus=2，结束（等定时任务触发）

  2. 对即时生效集合：
     a. ChgUtils.buildDeleteBatchParam → IPersonGenericService.deleteBatch  // 批量删
     b. ChgUtils.buildSaveBatchParam → IPersonGenericService.saveBatch       // 批量改/新增
     → 实际改 hrpi_* 底表

  3. processPersonFlow(chgRecords)
     → ChgUtils.savePersonFlow → 写 hpfs_personflow 流入流出

  4. processChargePerson(chgRecords)
     → 找 paramentry 里 paramchgentity == "haos_chargeperson" 的
     → 写部门负责人 (haos_chargeperson save)

  5. syncEmployeeLicenseCert(chgRecords)
     → ILicenseCertApplicationService.syncEmployeeLicenseCert(employeeIds)

  6. 收尾：
     成功 → bill.perchgstatus = SMALLEST_SUCCESS
     失败 → bill.perchgstatus = SMALLEST_FAIL · TXHandle.markRollback
}
```

## 业务流转 · 异常回滚三阶段

```
beginOperationTransaction 抛 KDBizException
  ↓
TXHandle.markRollback
  ↓
rollbackOperation (args 缓存 EXCEPTION_CHG_RECORD_IDS)
  ↓
onReturnOperation (写 errormsg 到 chgrecord.errormsg + 触发 aftereffect op)
  ↓
ChgUtils.executeOperate(billIds, "aftereffect")  → 触发跨云事件
```

## 业务事件 · 4 大 chgrecord 子事件（PPT slide 45）

```
hpfs_chgrecord.effect       生效   (TX 内 · 失败回滚 · 不要订阅)
hpfs_chgrecord.aftereffect  生效后 (TX 外 · 安全 · 跨云协作订阅这个 ⭐)
hpfs_chgrecord.discard      废弃
hpfs_chgrecord.rollback     回滚
hpfs.invokechgrecordeffect  自定义事件 · 主动触发
```

## 11 个标品定时任务（处理 datastatus=2 未来生效）

```
hdm_regeffect_plan_SKDP_S        转正生效   (扫 effecttime ≤ now 触发)
hdm_parteffect_plan_SKDP_S       兼职生效
hdm_transfereffect_plan_SKDP_S   调动生效
htm_effectquit_plan_SKDP_S       离职生效
hpb_batchreg_quit_plan_SKDP_S    批量转正状态更新
hdm_batchpartquit_plan_SKDP_S    批量兼职状态更新
hom_collectmanage_SKDP_S         入职采集管理
hom_countoverdue_SKDP_S          入职逾期未报到
hom_PreOnBrdBillSwitchTask_SKDP_S 预入职模板切换
hom_activityhandlemsg_SKDJ_S     入职协作处理消息
htm_QuitApplySyncResultUpgradeTask_SKDP_S  离职数据升级
```

<!-- END ppt04-injected -->
