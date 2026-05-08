# core_hr_org_unit_transfer · 数据流

## 数据流向（按变更类型）

### A 流程·组织上级调整

```
[HR 改组织父级]
   ↓
homs_orgchgrecord                                # 标品记录 1 行
   .orgchgentry.changescene.number = "1020_S"
   .adminorg.id = <被调整组织>
   .orgchgentry.chgeffecttime = <生效时间>
   ↓
[每日调度 OrgParentChgTask]
   queryHAOSChangeMsg(taskDays, {"1020_S"})
   ↓
${ISV_FLAG}_orguntitransfer                      # 落 ISV 中间表
   .${ISV_FLAG}_changetype = "A"
   .${ISV_FLAG}_orgid = <被调整组织 ID>
   .${ISV_FLAG}_orgnumber/orgname = <编码/名称>
   .${ISV_FLAG}_takeeffectdate = <生效日>
   .${ISV_FLAG}_executestatus = "01"            # 待执行
   ↓
[拉子组织 ID + 历史版本刷新 changeEmpPosOrgRelHisDataByOrg]
   修改 hrpi_empposorgrel.adminorgvid          # 把生效日后任职指向历史组织 vid
   ↓
[拉生效日内任职 getInEffectDateEmpPosOrgRelArrByOrgIdSet]
   ↓
hdm_transferbatch (批量调动单)                   # 标品 form
   .billstatus = "A" (草稿)
   .auditstatus = "A"
   .org = bos_org (根组织)
   .affiliationord_id = orgId
   .creator_id = currUserId
hdm_transferbatchentry (按人员一行)
   .seq = 1, 2, ...
   .ebillno = <CodeRule 批量取号>
   .bb_e_enterprise / _entrydate / _laborrelstatus / ...
   .bb_a_org / _manageadminorg
   .bb_po_orgrelseq
   .b_effectivedate = changeDate
   .entryvalidateresult = "A"
   ↓
[SAVE op]   → 调动单落库 (草稿态)
[SUBMITEFFECT op] → 标品自动协同
   ↓
hrpi_empposorgrel (任职经历) 自动更新           # 标品自己改·ISV 不参与
hpfs_chgrecord (变动记录) 自动写入
跨云事件 (BEC) 自动触发                         # 薪酬考勤跟随
   ↓
${ISV_FLAG}_orguntitransfer.${ISV_FLAG}_executestatus = "03"  # 完成
```

### B 流程·组织更名

跟 A 同·只是数据来源 `changescene.number = "1030_S"`·调动单"业务名" = "组织更名"。

### C 流程·岗位更名

```
[HR 改岗位]
   ↓
hbpm_chgrecordevt (主表) + hbpm_chgrecorddetail (子表·更名前后)
   .changescene.number = "1020_S" 或 "1080_S"
   ↓
[每日调度 PostRenameChgTask]
   queryPosChangeMsg(taskDays, {"1020_S", "1080_S"})
   ↓
${ISV_FLAG}_orguntitransfer
   .${ISV_FLAG}_changetype = "C"
   .${ISV_FLAG}_orgid = <岗位 ID>           # 字段名同·实际是岗位 ID
   ↓
[预处理 queryPosInfo → 建岗位→组织映射 positionOrgMap]
[历史版本刷新 changeEmpPosOrgRelHisDataByPos]
   修改 hrpi_empposorgrel.positionvid       # 历史岗位 vid
   ↓
[拉岗位下任职 getEmpPosOrgRelArrByPosId]
   ↓
hdm_transferbatch (调动单·业务名"岗位更名")
   ↓
SUBMITEFFECT 标品协同
   ↓
状态 03
```

## 数据状态机

```
01 待执行 ──┐
            ├──> 03 执行完成（成功 / 无人员）
            └──> 01 待执行（失败保留·下次重试）

(02 执行中状态 docx 定义有·代码未见赋值·待 ISV 确认)
```

## 数据隔离与归属

| 表 | 归属 | 谁写 | 谁读 |
|---|---|---|---|
| `${ISV_FLAG}_orguntitransfer` | ISV 自建 | 本资产 SaveServiceHelper.save | 本资产·UI 列表 |
| `homs_orgchgrecord` | 平台标品 | 苍穹其他模块（组织变更操作）| 本资产 query |
| `hbpm_chgrecordevt/_detail` | 平台标品 | 苍穹其他模块（岗位变更操作） | 本资产 query |
| `haos_adminorgdetail` / `hbpm_positionhr` | 平台标品 | 苍穹平台（HisModel 自动维护） | 本资产 query |
| `hdm_transferbatch/_entry` | 平台标品 | 本资产 SAVE / 标品自己 SUBMITEFFECT | 标品自身 + 多端 UI |
| `hrpi_*` 任职经历底表 | 平台标品（核心人力）| **标品自动**（SUBMITEFFECT 触发·ISV 不写）| 苍穹下游全 |

## 跨云数据流（ADR-009 跨云穿透）

```
[本场景 core_hr_org_unit_transfer]
   ↓ 写
hdm_transferbatch.SUBMITEFFECT
   ↓ 标品自动事件
hpfs_chgrecord.aftereffect (BEC 事件)
   ↓ 跨云订阅
   ├──> 薪酬云 (swc/hsas) ──> 自动重算工资
   ├──> 考勤云 (wtc/wtbd) ──> 自动重算考勤档案
   └──> 福利云 (hbjm)  ──> 自动调整福利计算

[本场景作为消费方]
   ← 上游写
homs_orgchgrecord ← 组织运营服务云 (haos_adminorg 单据)
hbpm_chgrecordevt ← HR 业务平台 (hbpm_position 单据)
```

## 数据延迟与一致性

- **调度延迟**：默认每日 1 次·按 `EXECUTE_DAYS=1` 处理过去 1 天的变更·**最长延迟 24 小时**
- **失败重试**：状态保留 01·下次调度自动重试·无次数限制
- **幂等**：同一条 `${ISV_FLAG}_orguntitransfer` 记录只会被处理 1 次（status=01 → 03）·调度任务调到第 N 次·已 03 的记录跳过
- **跨任务隔离**：A/B/C 三任务**独立调度**·互不干扰·同一变更不会被多任务消费（编码集互斥设计）
