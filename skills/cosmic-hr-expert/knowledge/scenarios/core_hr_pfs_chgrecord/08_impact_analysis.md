# core_hr_pfs_chgrecord · 影响分析

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

1. 每个子实体单独的影响分析章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 影响分析 · ISV 排错 SOP（PPT slide 102-108）

### 排错点 1：单据事务变动信息查询

```
1. 根据单据编号 → 查询 hpfs_chgrecord（按 affrecord）
2. 查 entryentity 的 databefore_tag / dataafter_tag → 解析变动前后数据
3. 查 paramentry 的 paramerrormsg_tag → 看错误详情
4. 查 chgrecord.errormsg / chgrecord.traceid → 定位日志
```

### 排错点 2：下游没收到 aftereffect 消息

```
1. 查 MQ 服务器是否异常
2. 查 mq.debug.queue.tag 上下游是否一致
3. 查日志 "send_msg_success_msgNumber=[X]" → 不存在 = 上游没发
4. 查 t_hrcs_msgcenter 表入库情况 + 处理状态
5. 查下游 hrcs_msgsubscriber 配置
```

### 排错点 3：消息延迟 > 5min

```
1. MQ 消息堆积
2. 中台通用服务发布/重启
3. 下游微服务问题
```

## 影响分析 · 跨云协作铁律

ISV 下游云（薪酬/考勤/绩效）订阅 hpfs_chgrecord 事件时：

| ⛔ 错误 | ✅ 正确 |
|---|---|
| 订阅 `hpfs_chgrecord.effect` | 订阅 `hpfs_chgrecord.aftereffect` |
| 不做幂等 | 对 chgrecord.id 做幂等去重 |
| 假设事件按时序到达 | 按 chgrecord.effecttime 排序处理 |
| TX 内做下游 RPC | aftereffect 在 TX 外触发，安全 |

<!-- END ppt04-injected -->
