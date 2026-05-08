# core_hr_contract_renew · 异常处理

> 数据源：W1 deep_scan_audit.md F 段（异常分支）+ 4 java 类 try/catch 全扫

## 已知异常路径

### EX-01 · 员工合同未审核（OP audit 拦截）

| 项 | 值 |
|---|---|
| 触发位置 | `RenewBatchOpPlugin.beforeExecuteOperationTransaction:113` |
| 抛出类 | `KDBizException` |
| 错误码 | (无·走业务异常)|
| 错误信息 | `员工 <empnumber> 合同未审核·不能加入续签批次` |
| 触发条件 | hlcm_contractapplyrenew.billstatus != "C" |
| 影响 op | save / submit / audit / wfauditing |
| 处理方式 | throw·中断整个 op 事务·hlcm + 主表都不写 |
| HR 看到 | 弹窗"员工 张三 合同未审核·不能加入续签批次" |

```java
// RenewBatchOpPlugin.java:107-128
for (DynamicObject item : items) {
    if (!"C".equals(item.getString("billstatus"))) {
        throw new KDBizException(
            "员工 " + item.getString("empnumber") + " 合同未审核·不能加入续签批次"
        );
    }
}
```

**HR 排错**：
1. 列表过滤 `billstatus != "C"` 的员工·查清单
2. 让员工先走标品 hlcm_contractapplyrenew 自身的 audit 流程
3. audit 完后再回来 save 批次

---

### EX-02 · 合同填写未完成（列表 tblsubmiteffect 拦截）

| 项 | 值 |
|---|---|
| 触发位置 | `ContractOtherExListPlugin.itemClick:110-111` |
| 抛出类 | `KDBizException` |
| 错误信息 | `合同填写未完成·不能提交生效` |
| 触发条件 | `${ISV_FLAG}_fillinstatus != "2"` |
| 影响场景 | 列表"提交生效"按钮 |
| 处理方式 | throw·阻止按钮·不进 op |
| HR 看到 | 弹窗"合同填写未完成·不能提交生效" |

**HR 排错**：进合同详情页·补全合同文本字段·让 fillinstatus 跳到 "2"。

---

### EX-03 · 签署状态不允许提交（列表 tblsubmit 拦截）

| 项 | 值 |
|---|---|
| 触发位置 | `ContractOtherExListPlugin.itemClick:151-162` |
| 抛出类 | `KDBizException` |
| 错误信息 | `合同签署状态 <状态> 不允许提交` |
| 触发条件 | `${ISV_FLAG}_fddsignstatuss != "F"` 且 `${ISV_FLAG}_signwaynew != "A"` |
| 影响场景 | 列表"提交"按钮 |
| 处理方式 | throw·阻止按钮 |
| HR 看到 | 弹窗"合同签署状态 待相对方签署 不允许提交" |

**HR 排错**：
- 电子签署：等相对方签完 → fdd 跳到 F
- 纸质签署：在合同详情改 signway = "A"

---

### EX-04 · ISV 自建按钮签署状态不符（baritemapex1 拦截）

| 项 | 值 |
|---|---|
| 触发位置 | `ContractOtherExListPlugin.click:210-211` |
| 抛出类 | `KDBizException` |
| 错误信息 | `合同签署状态需为 待相对方签署 或 已完成` |
| 触发条件 | `${ISV_FLAG}_fddsignstatuss NOT IN ("E", "F")` |
| 影响场景 | ISV 自建按钮 `${ISV_FLAG}_baritemapex1` |
| 处理方式 | throw·阻止按钮 |

---

### EX-05 · 选员工 list 返回空（closedCallBack 静默处理）

| 项 | 值 |
|---|---|
| 触发位置 | `RenewBatchBillPlugin.closedCallBack:178-184` |
| 抛出类 | (无·静默处理) |
| 触发条件 | 用户在弹窗清空选择并确定 |
| 处理方式 | `personsize=0` / `itemids_tag=""`·**不阻止**·HR 可保存空批次 |
| 风险 | 空批次保存后·OP 不会反查 hlcm·不写关联·主表数据不完整 |

**改进建议**：在主表 OP beforeExecute 加非空校验：
```java
if (StringUtils.isBlank(itemIds)) {
    throw new KDBizException("批次员工不能为空·请先选员工");
}
```

---

### EX-06 · 主表删除时关联未清（dealUnbindBatch 异常）

| 项 | 值 |
|---|---|
| 触发位置 | `RenewBatchOpPlugin.dealUnbindBatch:343-365` |
| 抛出类 | `KDBizException`（间接·SaveServiceHelper.save 失败时）|
| 触发条件 | hlcm_contractapplyrenew 当前正被其他 op 占用（行级锁）|
| 处理方式 | 苍穹自动回滚整个 delete 事务·主表也不删 |
| HR 看到 | 弹窗"删除失败·稍后重试"（苍穹默认） |

**改进建议**：加 try/catch 重试机制·或在前台加"删除前请确认所有相关合同未在审批中"。

---

### EX-07 · BusinessDataServiceHelper.load 反查失败（罕见）

| 项 | 值 |
|---|---|
| 触发位置 | `RenewBatchOpPlugin.beforeExecuteOperationTransaction:98-107` |
| 抛出类 | (苍穹平台层抛 RuntimeException) |
| 触发条件 | hlcm_contractapplyrenew 表结构变化（字段被删除）·或 OpenAPI 限流 |
| 处理方式 | 抛到 OP 框架·op 失败·HR 收到 500 |
| HR 看到 | 弹窗"系统异常·请联系管理员" |

**ISV 改进建议**（可选）：加 try/catch 包 load·失败时记日志·降级为"不校验·直接通过"。但**风险**：可能放行未审核的合同·业务上一般不可接受。

---

## 容错策略

### 事务一致性保证

| 操作 | 事务边界 | 失败回滚 |
|---|---|---|
| save / audit | 苍穹 OP 单事务（before + commit + after）| 任一步失败·主表 + hlcm 全回滚 |
| delete + dealUnbindBatch | 单事务 | 解绑失败·主表也不删 |
| 选员工弹窗 itemClick | 无事务（前端交互）| 失败弹错·不影响数据 |

### 幂等性保证

| 场景 | 幂等性 | 实现 |
|---|---|---|
| dealUnbindBatch 多次 delete | ✅ 幂等 | SQL `renewbatch=null` 覆盖·已 null 的不再被 query 命中 |
| save 重复保存 | ✅ 幂等 | 写关联用 `set("renewbatch", pkId)`·重复写无副作用 |
| afterExecute 二次写入 | ✅ 幂等 | beforeExecute 已写·afterExecute 重复写同值 |

---

## 调试日志

### 关键日志点（建议加 Logger）

| 类 | 方法 | 日志级别 | 内容 |
|---|---|---|---|
| RenewBatchBillPlugin | itemClick | INFO | `选员工 orgId=X structnumber=Y → 子组织 N 个` |
| RenewBatchBillPlugin | closedCallBack | INFO | `回填 personsize=N itemids=...` |
| RenewBatchOpPlugin | beforeExecute | INFO | `op=X 批次 pkId=Y 校验员工 N 个` |
| RenewBatchOpPlugin | beforeExecute | WARN | `员工 X 合同状态 Y 拦截` |
| RenewBatchOpPlugin | afterExecute | INFO | `op=X 反向解绑 N 条` |
| AdminOrgHrUtils | getLowerOrgIds | DEBUG | `查询 structlongnumber LIKE %X% → N 子组织` |

**当前现状**：本资产**未加 Logger**·依赖苍穹 OP 框架默认日志。改进项：加 `kd.bos.logging.LogFactory.getLog`。

---

## 业务侧排错指南（HR / 实施）

### 场景 1：HR 保存批次失败·提示"员工 X 合同未审核"

**步骤**：
1. 列表过滤 `billstatus != "C"` 的员工
2. 联系合同负责人·让其先走 hlcm_contractapplyrenew 审核
3. 审核通过后·HR 重新进批次单·重新选员工·保存

### 场景 2：选员工弹窗显示空

**可能原因**：
- 所属组织 affiliationord 为空 → 先填
- 子组织无员工 → 检查 haos_adminorg 数据完整性
- hlcm_contractapplyrenew 全部已关联其他批次 → 反审批旧批次·或选其他员工

### 场景 3：批次审批后部分员工的 hlcm 没写关联字段

**可能原因**：
- 部分员工被 EX-01 拦截·主表事务整体回滚 → 但日志显示"成功"·实际没写

**排查**：
```sql
SELECT COUNT(*) FROM t_hr_hlcm_contractapplyrenew_ext2
WHERE renewbatch = <主表 ID>;
-- 应等于 ${ISV_FLAG}_personsize
```

### 场景 4：删除主表后·hlcm 上的关联字段还在

**可能原因**：dealUnbindBatch 失败·但事务未正确回滚（罕见·苍穹平台 bug）

**排查**：
```sql
SELECT id, fid, renewbatch FROM t_hr_hlcm_contractapplyrenew_ext2
WHERE renewbatch IN (<已删主表 ID 集>);
-- 应该 0 条
-- 若有·人工 SQL 清理
UPDATE t_hr_hlcm_contractapplyrenew_ext2 SET renewbatch = NULL WHERE renewbatch IN (...);
```

### 场景 5：HR 反映"提交按钮置灰"

**排查清单**：
1. 查 `${ISV_FLAG}_fddsignstatuss` 当前值（A/E/F）→ 不是 F
2. 查 `${ISV_FLAG}_signwaynew` 当前值（A/B）→ 不是 A
3. 同时不满足 → 拦截（EX-03）
4. 解：让相对方签 → fdd=F·或改为纸质 signway=A

## 关联文档

- [04_business_flow.md 异常路径表](04_business_flow.md)
- [02_business_rules.md R-05/R-06 拦截规则](02_business_rules.md)
- [`_assets/contract_renew_batch/deploy_sop.md` 烟测步骤](../../_assets/contract_renew_batch/deploy_sop.md)
