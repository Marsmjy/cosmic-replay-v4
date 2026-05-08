# core_hr_contract_renew · 数据流

## 数据流向（save 流程）

```
[HR 进入主表 ${ISV_FLAG}_hlcm_renewbatch 新增页]
   ↓
afterCreateNewData [RenewBatchBillPlugin.java:68]
   → 默认值
   → ${ISV_FLAG}_invalid = false (隐藏 tdkw_invalid 工具栏 line 83)
   ↓
[HR 选 affiliationord (BasedataField → bos_org)]
   ↓
itemClick [RenewBatchBillPlugin.java:122]
   ↓ 取参
   orgId = dataEntity.getLong("${ISV_FLAG}_affiliationord.id")     [line 149]
   structNumber = dataEntity.getString("${ISV_FLAG}_affiliationord.structnumber") [line 155]
   ↓ 调 AdminOrgHrUtils
   List<Long> orgIds = AdminOrgHrUtils.getLowerOrgIds([structNumber])
   → query haos_adminorg WHERE structlongnumber LIKE %structNumber%
                              AND enable = '1'
                              AND datastatus = '1'
                              AND iscurrentversion = true
   ↓ 打开 list 弹窗
   ListShowParameter param = ListShowParameter
       .setBillFormId("hlcm_contractapplyrenew")                   [line 135]
       .setFilter(QFilter("affiliationord", "in", orgIds))
       .setCustomParams("selectedIds", existingItemIds)             [line 218-225·F7 默认勾选]
   ↓ 用户多选员工返回
closedCallBack [RenewBatchBillPlugin.java:167]
   ↓
${ISV_FLAG}_hlcm_renewbatch.${ISV_FLAG}_personsize = pkIds.size()  [line 184]
${ISV_FLAG}_hlcm_renewbatch.${ISV_FLAG}_itemids_tag = StringUtils.join(pkIds, ",") [line 185]
   ↓ HR 保存 (save op)
RenewBatchOpPlugin.beforeExecuteOperationTransaction [line 46]
   ↓ 拆 itemids_tag → Long[] itemIds
   itemIds = parse(${ISV_FLAG}_itemids_tag)
   ↓ 反查标品记录
   load(hlcm_contractapplyrenew, "billstatus,handlestatus,auditstatus,${ISV_FLAG}_renewbatch",
        QFilter("id", "in", itemIds))                              [line 98, 107]
   ↓ 校验
   if (item.billstatus != "C") throw KDBizException
   ↓ 写关联
   item.${ISV_FLAG}_renewbatch = ${ISV_FLAG}_hlcm_renewbatch.id    [line 284, 287]
   SaveServiceHelper.save(items)
   ↓ op 通过 → 主表保存成功
   ↓
${ISV_FLAG}_hlcm_renewbatch.billstatus = "A" (草稿态)
hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch = <主表 ID>          [双向关联建立]
```

## 反向解绑流程（wfunaudit / invalid / delete）

```
[HR 反审批 / 失效 / 删除 主表记录]
   ↓
RenewBatchOpPlugin.afterExecuteOperationTransaction [line 138]
   ↓ 守门
   if (operationKey IN ("wfunaudit", "invalid")) {
      ↓ 反查所有关联记录
      pkId = dataEntity.getLong("id")
      bindItems = load("hlcm_contractapplyrenew",
                       "id,${ISV_FLAG}_renewbatch",
                       QFilter("${ISV_FLAG}_renewbatch.id", "=", pkId))
      ↓ 解绑
      for (item : bindItems) {
          item.${ISV_FLAG}_renewbatch = null                       [line 296]
      }
      SaveServiceHelper.save(bindItems)
   }

   ↓ delete op 走 dealUnbindBatch (line 343)
   pkIds = dataEntity 的所有 id 集
   load(hlcm_contractapplyrenew, ..., QFilter("${ISV_FLAG}_renewbatch.id", "in", pkIds))
   for each item:
       item.${ISV_FLAG}_renewbatch = null
   SaveServiceHelper.save
```

## 数据状态机

### `${ISV_FLAG}_hlcm_renewbatch.billstatus`（主表状态·标品标准）
- `A` 草稿
- `B` 提交中
- `C` 已审核
- 内部 wf 流转

### `hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch`（关联状态）
- `null` 未关联
- `<batch_id>` 已关联（属于某个 batch）

转换规则：
- save / submit / audit / wfauditing → null → batch_id
- wfunaudit / invalid → batch_id → null
- delete (主表) → batch_id → null（dealUnbindBatch 强解绑）

### `${ISV_FLAG}_hlcm_contractap_ext2` 的 ISV 字段状态机

| 字段 | 取值 | 业务含义 |
|---|---|---|
| `${ISV_FLAG}_fddsignstatuss` | `A`/`E`/`F` | A=待签署 / E=待相对方签署 / F=已完成 |
| `${ISV_FLAG}_signwaynew` | `A`/`B` | A=纸质签署 / B=电子签署（默认）|
| `${ISV_FLAG}_fillinstatus` | `1`/`2` | 1=填写中 / 2=已完成 |

## 数据隔离与归属

| 表 | 归属 | 谁写 | 谁读 |
|---|---|---|---|
| `${ISV_FLAG}_hlcm_renewbatch` | ISV 自建 | RenewBatchBillPlugin（用户）+ RenewBatchOpPlugin（OP）| 本资产 + UI |
| `hlcm_contractapplyrenew` 标品字段 | 平台标品 | hlcm 标品 op | 本资产 read-only |
| `hlcm_contractapplyrenew.${ISV_FLAG}_*` 4 ISV 字段 | ISV 扩展 (ext2) | RenewBatchOpPlugin / 法大大集成插件 | 本资产 + 列表过滤 |
| `haos_adminorg` | 平台标品 | haos 标品 | 本资产 read-only |
| `bos_listf7` (查找弹窗 form) | 平台标品 | 平台 | RenewBatchBillPlugin 调 |

## 数据延迟与一致性

- **同步执行**：所有 op 都是同步事务（before/after 在同一事务）
- **强一致**：主表保存 = 标品 ${ISV_FLAG}_renewbatch 字段写完成（同事务·要么都成要么都回滚）
- **幂等**：dealUnbindBatch 用 SQL`renewbatch=null` 覆盖·多次执行无副作用
- **无跨云延迟**：本资产**不发 BEC 事件**·不联动跨云·变更只在 hlcm 内闭环

## 跨云数据流（ADR-009 跨云穿透）

```
[本场景] core_hr_contract_renew
   ↓ 不主动发跨云事件
   (本资产不直接通知 swc/wtc/hbjm)

[标品 hlcm_contractapplyrenew] 自身的 op 链
   ↓ 标品自动 (afterAudit 等)
hlcm_contract / hlcm_contractfileemp 等关联档案
   ↓ 标品自动 (BEC 事件如 hpfs_chgrecord.aftereffect)
跨云薪资 / 考勤 / 福利系统跟随更新

[本场景作为消费方]
   ← 上游
hlcm_contractapplyrenew (标品产生·本资产消费)
haos_adminorg (标品)
```

⚡ **关键**：本资产**只联动 hlcm 内部**·跨云事件靠**标品 hlcm 自动派发**·ISV 不重写。
