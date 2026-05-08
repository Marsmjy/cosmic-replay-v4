# 成建制划转 · 扩展点（Customization Points）

> 协议：docx §3.2 + dcs_clean v1.5 实测代码 deep_scan_audit.md
> 适用：客户在标准资产基础上做定制·不改 5 类业务代码骨架的前提下·扩展业务范围

## EP-01 · 改"变动场景编码集"

**业务诉求**：客户业务侧定义新的变动场景（如 "ZZCJ06 行政区划撤销"）·想让成建制划转任务也消费此场景。

**修改位置**：3 个子任务类的常量定义

| 子任务类 | 常量名 | 默认值 | 修改示例 |
|---|---|---|---|
| `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgParentChgTask` | `CHANGE_SCENE_ORG_PARENT_NUMBER` | `Sets.newHashSet("1020_S")` | `Sets.newHashSet("1020_S", "ZZCJ06")` |
| `${ISV_FLAG}.hr.hdm.orgtransfer.business.OrgRenameChgTask` | `CHANGE_SCENE_ORG_RENAME_NUMBER` | `Sets.newHashSet("1030_S")` | `Sets.newHashSet("1030_S", "ZZCJ04", "ZZCJ05")` |
| `${ISV_FLAG}.hr.hdm.orgtransfer.business.PostRenameChgTask` | `CHANGE_SCENE_POS_NUMBER_SET` | `Sets.newHashSet("1020_S", "1080_S")` | `Sets.newHashSet("1020_S", "1080_S", "POS-CUSTOM")` |

**docx §3.2 原文**：
> "在其他组织变动场景或者岗位变动场景时进行成建制划转业务·实现方式：修改对应调度作业插件中的变动场景集合即可"

**注意**：
- 编码必须在标品 `haos_changescene` 表里真实存在·否则调度跑不到
- 跑前用 cosmic-mcp 查 haos_changescene 真编码列表

## EP-02 · 改调度执行天数

**业务诉求**：客户想让任务一次跑过去 7 天的变更（默认 1 天）·避免漏单。

**修改位置**：`AbstractCommonTask.java:48`

```java
private static final int EXECUTE_DAYS = 1;
// 改为
private static final int EXECUTE_DAYS = 7;
```

**注意**：
- 该参数影响 `getTaskDays()` 计算的查询区间
- 改大值会增加查询消息表数据量·建议 ≤ 30
- 部署后调度任务跑批历史数据·可能要清掉之前已处理但漏的数据

## EP-03 · 加 ISV 中间表自定义字段

**业务诉求**：客户业务方要求在中间表 `${ISV_FLAG}_orguntitransfer` 加一个自定义字段（如 "审批意见 ${ISV_FLAG}_approval_note"）。

**修改 3 处**（联动）：

### a) datamodel/.../${ISV_FLAG}_orguntitransfer.dym

加字段定义：
```xml
<TextField id="${ISV_FLAG}_approval_note" length="500" required="false">
    <Name lcid="zh_CN" value="审批意见"/>
</TextField>
```

### b) OrgTransferHelper.java:357 BusinessDataServiceHelper.load 字段列表

```java
return BusinessDataServiceHelper.load("${ISV_FLAG}_orguntitransfer",
    "${ISV_FLAG}_takeeffectdate,${ISV_FLAG}_orgid,${ISV_FLAG}_orgnumber,${ISV_FLAG}_orgname,${ISV_FLAG}_executestatus,${ISV_FLAG}_resultmsg,${ISV_FLAG}_resultmsg_tag,${ISV_FLAG}_approval_note",  // 加在这里
    new QFilter[]{qFilter}, "${ISV_FLAG}_takeeffectdate asc,${ISV_FLAG}_modifydate asc");
```

### c) 业务代码读写新字段（按需）

```java
// 在 saveAndEffectBatchBill 等方法里·按需读写
String note = orgTran.getString("${ISV_FLAG}_approval_note");
```

## EP-04 · 添加新 changeType（D/E/...）

**业务诉求**：客户想加一个新的变动类型（如 D=部门撤销）·走类似的批量处理。

**修改步骤**：

1. **新建 java 子类** `${ISV_FLAG}.hr.hdm.orgtransfer.business.DeptRevokeChgTask`·继承 `AbstractCommonTask`
2. **常量定义**：
   ```java
   private static final String DEPT_REVOKE_CHG_TYPE = "D";
   private static final Set<String> CHANGE_SCENE_DEPT_REVOKE_NUMBER = Sets.newHashSet("ZZCJ-DEPT-DEL");
   ```
3. **覆盖 `execute()` + `handleOrgTransfer()`**·参考 OrgParentChgTask（A）的骨架
4. **加 schdata 调度配置**·新加一个 `<TaskClassName>${ISV_FLAG}.hr.hdm.orgtransfer.business.DeptRevokeChgTask</TaskClassName>` 任务
5. **加 docx 关键码值表**·在 `${ISV_FLAG}_changetype` 字典里加"D=部门撤销"

## EP-05 · 改调动单类型描述

**业务诉求**：客户想让生成的标品调动单 `hdm_transferbatch` 的类型名更具体（如把"组织变更上级"改成"成建制上调"）。

**修改位置**：3 个子任务类的 `saveAndEffectBatchBill` 调用最后一个参数

| 子任务 | 行号 | 当前值 | 改为 |
|---|---|---|---|
| OrgParentChgTask | 116 | `"组织变更上级"` | `"成建制上调"` |
| OrgRenameChgTask | 122 | `"组织更名"` | `"组织名称调整"` |
| PostRenameChgTask | 137 | `"岗位更名"` | `"岗位名称调整"` |

注意：这个值会写进调动单的 `currentTypeName` 字段·影响 UI 展示。

## EP-06 · 改数据源（如自建消息表）

**业务诉求**：客户有自己的"组织变更通知"实体·不想用标品 `homs_orgchgrecord`·想让任务消费自己的实体。

**修改位置**：`OrgTransferHelper.java:32-65 queryHAOSChangeMsg` + `:67-90 queryPosChangeMsg`

```java
// 替换 form 名
HRBaseServiceHelper helper = new HRBaseServiceHelper("${ISV_FLAG}_my_org_change");
QFilter changeSceneFilter = new QFilter("orgchgentry.changescene.number", QCP.in, changeSceneSet);
return helper.query("...", new QFilter[]{...});
```

**注意**：
- 自建消息表必须有跟 `homs_orgchgrecord` 相同的字段结构（`orgchgentry.chgeffecttime` 等）
- 否则 `getOrgTransferDyList` 转换逻辑会失败

## 不应该做的扩展（反模式）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 私改 `hrpi_empposorgrel` 等任职经历底表 | 旧 hbis 客户版做法·已被本资产架构淘汰·改用标品 `submiteffect` |
| ❌ 跨服务自发 MQ 通知下游 | 旧客户版的 `ChgRecordUtil.packageMsg` 反模式·标品 op 已自带协同 |
| ❌ 在子任务里写跨云调用 SendEmailService | 应交给标品 op 处理·或加独立 BEC 订阅·不在调度任务里 |
| ❌ 类名带具体客户标识（如 `BjssOrgParentChgTask`）| 包前缀已带 `${ISV_FLAG}.`·类名不该再重复 |

## 协议引用

- docx §3.2 常见扩展点
- deep_scan_audit.md（dcs-case-run case_001 真扫的代码锚点）
- deep_scan_diff.md（A vs B vs C 三任务的代码差异对照）
