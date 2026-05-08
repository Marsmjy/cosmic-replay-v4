# core_hr_contract_renew · 业务规则

> 数据源：deep_scan_audit.md (W1 真扫·锚点 100% file:line)
> 日期：2026-05-06

## R-01 · 7 op 状态机（OpPlugin 核心）

`RenewBatchOpPlugin` 覆盖 7 个 op·分两组语义：

### 组 A · "建关联" 语义（4 op）
| op | 时机 | 行为 |
|---|---|---|
| `save` | 用户保存 | beforeExecute 时·按 `${ISV_FLAG}_itemids_tag` 反查 hlcm_contractapplyrenew·写 `${ISV_FLAG}_renewbatch=主表 ID` |
| `submit` | 提交 | 同上 |
| `audit` | 审核 | beforeExecute + afterExecute 双确认·校验 `billstatus="C"`·写关联 |
| `wfauditing` | 流程审批中 | beforeExecute 写 + afterExecute 二次确认 |

实证锚点：`RenewBatchOpPlugin.java:46-130` (before) + `:138-210` (after)

### 组 B · "解关联" 语义（3 op）
| op | 时机 | 行为 |
|---|---|---|
| `wfunaudit` | 反审批 | afterExecute 反查所有 `${ISV_FLAG}_renewbatch=本主表 ID` 的 hlcm·清 null |
| `invalid` | 失效 | 同上 |
| `delete` | 删除 | 走 `dealUnbindBatch`·全清 (`RenewBatchOpPlugin.java:343-365`) |

实证锚点：`RenewBatchOpPlugin.java:235-310` (unbindRenewBatch) + `:343-365` (dealUnbindBatch)

## R-02 · ISV 中间表 ↔ 标品反向引用机制

```
${ISV_FLAG}_hlcm_renewbatch (1)                hlcm_contractapplyrenew (N)
   │                                                │
   │ ${ISV_FLAG}_itemids_tag                        │ ${ISV_FLAG}_renewbatch
   │ "id1,id2,id3,..." (字符串)        ←-反向→        BasedataField (单选)
   │                                                │
   └────────────────────────────────────────────────┘
            主动写关联 (RenewBatchOpPlugin.before)
            自动解关联 (RenewBatchOpPlugin.after)
```

**字段双向同步**：
- 写入：`RenewBatchOpPlugin.java:284 item.set("${ISV_FLAG}_renewbatch", pkId)`
- 解绑：`RenewBatchOpPlugin.java:296 item.set("${ISV_FLAG}_renewbatch", null)`

## R-03 · 选员工流程（"按所属组织 + 子组织"）

`RenewBatchBillPlugin.itemClick` 处理"选员工"按钮：

```java
// RenewBatchBillPlugin.java:122-165
1. 取 dataEntity 的 ${ISV_FLAG}_affiliationord.id           [line 149]
2. 取 ${ISV_FLAG}_affiliationord.structnumber               [line 155]
3. 调 AdminOrgHrUtils.getLowerOrgIds([structnumber])        // 拿组织 + 全部子组织 ID
4. 打开 list 弹窗 (formId=hlcm_contractapplyrenew)
   设过滤 affiliationord IN <orgIds>
   把已选 itemIds 反填 (默认勾选)
```

**子组织递归实现**（AdminOrgHrUtils.java:24-46）：
```java
QFilter qFilter = new QFilter("enable", "=", "1")
    .and("datastatus", "=", "1")
    .and("iscurrentversion", "=", true);
QFilter structFilter = null;
for (String num : structNumberList) {
    if (structFilter == null) {
        structFilter = new QFilter("structlongnumber", QCP.like, "%" + num + "%");
    } else {
        structFilter.or("structlongnumber", QCP.like, "%" + num + "%");
    }
}
DynamicObjectCollection coll = QueryServiceHelper.query("haos_adminorg", "id", qFilter.toArray());
```

⚡ **关键**：用 `structlongnumber LIKE %xxx%` 做组织树前缀模糊匹配·返该组织 + 所有子组织 ID。

## R-04 · 选员工后回填规则（closedCallBack）

`RenewBatchBillPlugin.closedCallBack:167-190`：

| 场景 | 回填行为 |
|---|---|
| 用户选了 N 个新员工 | `${ISV_FLAG}_personsize=N` / `${ISV_FLAG}_itemids_tag=逗号分隔 ID` |
| 用户清空选择 | `${ISV_FLAG}_personsize=0` / `${ISV_FLAG}_itemids_tag=""` |
| 用户取消选择 | 保留原 itemIds·不动 |

实证锚点：`RenewBatchBillPlugin.java:178-190`

## R-05 · 工具栏按钮拦截规则（ContractOtherExListPlugin）

扩展 `hlcm_contractapplyrenew` 列表页·拦截 3 按钮（`ContractOtherExListPlugin.java:47`）·按法大大签署状态决定放行：

| 按钮 | 校验字段 | 放行条件 | 实证锚点 |
|---|---|---|---|
| `tblsubmiteffect` (提交生效) | `${ISV_FLAG}_fillinstatus` | `"2"`（已完成）| line 110-111 |
| `tblsubmit` (提交) | `${ISV_FLAG}_fddsignstatuss` + `${ISV_FLAG}_signwaynew` | `fdd="F/已完成"` 或 `signway="A/纸质签署"` | line 151-162 |
| `${ISV_FLAG}_baritemapex1` (ISV 自建) | `${ISV_FLAG}_fddsignstatuss` | `"E/待相对方签署"` 或 `"F/已完成"` | line 210-211 |

不满足放行条件 → throw KDBizException 阻止提交。

## R-06 · 校验已审核合同（beforeExecute）

OP 审核 audit 时·必须确认所有关联的 hlcm_contractapplyrenew 都 `billstatus="C"`：

```java
// RenewBatchOpPlugin.java:107-128
DynamicObject[] allitems = BusinessDataServiceHelper.load("hlcm_contractapplyrenew",
    "billstatus,handlestatus,auditstatus,${ISV_FLAG}_renewbatch",
    new QFilter("id", QCP.in, parseItemIds(itemIds)).toArray());

for (DynamicObject item : allitems) {
    if (!"C".equals(item.getString("billstatus"))) {
        throw new KDBizException("员工 " + item.getString("empnumber") + " 合同未审核·不能加入续签批次");
    }
}
```

⚡ **铁律**：批次主表审核 = 所有员工的合同申请单**预先**已审核·否则中断 OP。

## R-07 · 字段命名规则（ISV 隔离铁律 PR-001）

ISV 加在 hlcm_contractapplyrenew 上的所有字段·必须带 `${ISV_FLAG}_` 前缀（PR-001）：

| 字段 | 类型 | 用途 |
|---|---|---|
| `${ISV_FLAG}_renewbatch` | BasedataField | 反向引用主表 |
| `${ISV_FLAG}_fddsignstatuss` | TextField | 法大大签署状态（A/E/F）|
| `${ISV_FLAG}_signwaynew` | TextField | 签署方式（A/B）|
| `${ISV_FLAG}_fillinstatus` | TextField | 合同填写状态（1/2）|

**走 ext2 扩展元数据**（`tdkw_hlcm_contractap_ext2.dym`）·**不直接 modifyMeta 标品 hlcm_contractapplyrenew**。

## R-08 · 解关联幂等（dealUnbindBatch）

delete op 调 `dealUnbindBatch:343-365`·**确保即使主表已删·标品记录也能解绑**：

```java
DynamicObject[] bindItems = BusinessDataServiceHelper.load(
    "hlcm_contractapplyrenew",
    "billstatus,handlestatus,auditstatus,${ISV_FLAG}_renewbatch",
    new QFilter("${ISV_FLAG}_renewbatch.id", QCP.in, pkIds.toArray()).toArray()
);
for (DynamicObject item : bindItems) {
    item.set("${ISV_FLAG}_renewbatch", null);
}
SaveServiceHelper.save(bindItems);
```

**幂等性**：多次 delete 不会重复出错·因为已 null 的不再被 query 命中。

## 与 v3 知识库关联

- 资产复刻：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/)
- 业务事实表：[deep_scan_audit.md](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_audit.md)（60+ 业务断言）
- 双侧对照：[deep_scan_diff.md](../../../dcs_regression/passed/case_002_contract_renew/deep_scan_diff.md)（4 大对照）
