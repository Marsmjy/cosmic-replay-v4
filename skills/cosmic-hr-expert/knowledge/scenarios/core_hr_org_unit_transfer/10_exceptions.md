# core_hr_org_unit_transfer · 异常处理

## 已知异常路径

### EX-01 · 当前组织/岗位下无人员

**触发**：拉到的 `empPosOrgRelArr` 为空。

**实证锚点**：
- A: `OrgParentChgTask.java:104-110`
- B: `OrgRenameChgTask.java:110-117`
- C: `PostRenameChgTask.java:122-128`

**处理**：
```java
if (ObjectUtils.isEmpty(empPosOrgRelArr)) {
    String resultMsg = "成建制划转 当前组织下没有对应人员,组织编码 : " + currentNumber;
    LOGGER.info(resultMsg);
    orgTran.set("${ISV_FLAG}_executestatus", "03");      // 直接标完成
    orgTran.set("${ISV_FLAG}_resultmsg", canInfo);
    orgTran.set("${ISV_FLAG}_resultmsg_tag", resultMsg);
    continue;
}
```

**业务含义**：组织/岗位下无人员·业务上不需要生成调动单·标 03 跳过。

---

### EX-02 · save 调动单失败

**触发**：`OperationServiceHelper.executeOperate(SAVE, ...)` 返回 `!isSuccess()`。

**实证锚点**：`AbstractCommonTask.java:281-289`

**处理**：
```java
if (!operationSaveResult.isSuccess()) {
    String msg = currentTypeName + "，当前编码：：" + currentName + "，保存批量调动单失败, 原因：" + operationSaveResult;
    resultMsg.append(msg).append("\n");
    operationSaveResult.getAllErrorOrValidateInfo().forEach(validData -> {
        resultMsg.append(validData.getMessage()).append("\n");
    });
    success = false;
}
```

**业务含义**：调动单 save 失败·状态保留 `01`·下次调度自动重试。错误细节写入 `resultmsg_tag`。

---

### EX-03 · submiteffect 失败

**触发**：save 成功·但 `executeOperate(SUBMITEFFECT, ...)` 失败。

**实证锚点**：`AbstractCommonTask.java:292-300`

**处理**：同 EX-02·标失败 + 状态保留 01。

**业务含义**：标品 op 内部业务校验未过（如调动数据冲突 / 任职经历有重叠 / 人员状态非法）。

---

### EX-04 · 任职经历刷新失败

**触发**：`changeEmpPosOrgRelHisDataByOrg/ByPos` 执行 update 异常。

**实证锚点**：`AbstractCommonTask.java:368-378`（异常被吞·只 log）

**处理**：异常被吞到日志·**继续往下走 buildBatchTranBill**·后续标品 op 自己处理。

**业务含义**：刷新历史版本是"补丁"动作·失败不阻塞主流程。

---

## 容错策略

| 容错点 | 策略 |
|---|---|
| 单条记录失败 | 不阻塞后续记录·全部跑完后批量保存 |
| 任务失败 | A/B/C 三任务独立·一个失败不影响其他 |
| 数据源查询失败 | 整个调度任务失败·等下次 cron |
| 标品 op 升级行为变化 | 必须 case 类回归·dcs-case-run K2 验证 |

## 调试日志

LOGGER 命名空间：
- `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgParentChgTask`
- `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgRenameChgTask`
- `${ISV_FLAG}.hr.hdm.orgtransfer.business.PostRenameChgTask`
- `${ISV_FLAG}.hr.hdm.orgtransfer.business.AbstractCommonTask`

关键日志：
- "成建制划转(组织上级调整)作业，开始执行..." / "执行结束."
- "保存批量调动单失败" / "生效批量调动单失败"
- "成建制划转，写入调动单成功/失败"

## 业务侧排错指南

`${ISV_FLAG}_orguntitransfer` 列表查 `${ISV_FLAG}_executestatus` + `${ISV_FLAG}_resultmsg_tag`：

| 状态 | 含义 | 可能原因 |
|---|---|---|
| `01` 长期不变 | 重试中 | 检查标品 op 是否健康·查 `_resultmsg_tag` |
| `03` + resultmsg "无对应人员" | 正常·跳过 | 业务侧确认是否真的无人 |
| `03` + resultmsg 异常信息 | 失败已记录 | 看异常详情 |

## 跨任务幂等

- 同一 `${ISV_FLAG}_orguntitransfer` 记录只跑 1 次（status 01 → 03）
- 重复触发 changescene 不会重复落 `${ISV_FLAG}_orguntitransfer` 记录（依赖 `getOrgTransferDyList` 内部去重逻辑·见 OrgTransferHelper.java:155）
