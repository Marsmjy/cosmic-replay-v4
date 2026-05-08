# 数据流转 · 数据规则 (hrcs_datarule)

> **状态**：基于反编译 + scene_doc.json 21 字段实抓 + HRDataRuleSaveOp / HRDataRuleEditPlugin 数据读写实证整合
> **confidence**：real_deploy（所有 read/write 字段均来自 _auto_plugin_semantics.md 反编译实抓）

## 一、物理存储

### 1.1 主表

| 表名 | 角色 | 字段数 | 关键字段 |
|---|---|---|---|
| `t_hrcs_datarule` | 数据规则主表 | 21 | id / number / fname / fstatus / fenable / fentitynum / **frule** |

⚠️ **没有独立的 `_l` 多语言子表**：MuliLangTextField 字段（`fname` / `fsimplename` / `fdescription` / `foriname`）都在主表承载。这是当前实抓结论 · 不是 HR 默认行为。ISV 加 MuliLangTextField 时不要假设会自动建 `_l` 表。

### 1.2 不存在的辅助表

数据规则**不是 HisModel 时序基础资料** · 因此：
- ❌ 没有 `t_hrcs_datarule_h` 历史快照表
- ❌ 没有 boid / sourcevid / iscurrentversion 字段
- ❌ 不参与"业务对象时间维度"的版本管理

数据规则**通过 `entitynum` 引用** HisModel 时序基础资料 · 但本规则自己一行就是一个版本。

### 1.3 列名映射（21 字段实抓）

| 业务 key | 物理列 | 类型 | 说明 |
|---|---|---|---|
| number | fnumber | TextField | 规则编码 |
| name | fname | MuliLangTextField | 规则名称（多语言）|
| simplename | fsimplename | MuliLangTextField | 简称 |
| description | fdescription | MuliLangTextField | 描述 |
| status | fstatus | BillStatusField | 单据状态 |
| enable | fenable | BillStatusField | 启用状态 |
| index | findex | IntegerField | 排序号 |
| issyspreset | fissyspreset | CheckBoxField | 系统预置 |
| disabler | FDisablerID | UserField | 禁用人 |
| disabledate | FDisableDate | DateTimeField | 禁用时间 |
| initdatasource | finitdatasource | ComboField | 数据来源 |
| orinumber | forinumber | TextField | 出厂编码 |
| oristatus | foristatus | ComboField | 出厂数据编辑状态 |
| oriname | foriname | MuliLangTextField | 出厂名称 |
| **entitynum** | **fentitynum** | BasedataField → bos_entityobject | **业务对象** |
| **rule** | **frule** | TextField | **规则**（FilterCondition JSON）|
| creator | fcreatorid | CreaterField | BOS L0 |
| modifier | fmodifierid | ModifierField | BOS L0 |
| createtime | fcreatetime | CreateDateField | BOS L0 |
| modifytime | fmodifytime | ModifyDateField | BOS L0 |
| masterid | fmasterid | MasterIdField | BOS L0 |

## 二、读路径

### 2.1 表单装载（HRDataRuleEditPlugin#afterBindData）

```
用户打开规则
    │
    ▼
[平台] 加载 hrcs_datarule 主表行 → DynamicObject
    │   读字段：number, name, status, enable, entitynum, rule, ...
    ▼
HRDataRuleEditPlugin#afterBindData
    │
    ▼
refreshFilterGrid()
    │
    ├─ DynamicObject entityObj = dataEntity.getDynamicObject("entitynum")
    │     ↓ pkValue = 业务对象 number
    │
    ├─ String ruleStr = dataEntity.getString("rule")
    │
    ├─ FilterGrid filterGrid = (FilterGrid)this.getControl("filtergridap")
    │
    ├─ filterGrid.setEntityNumber(entityNum)
    │
    ├─ MainEntityType entityType = EntityMetadataCache.getDataEntityType(entityNum)
    │   ↓ 加载业务对象的元数据（字段集）
    │
    ├─ List filterColumns = EntityTypeUtil.createFilterColumns(
    │     new GetFilterFieldsParameter(entityType).setNeedMulBasedataField(true))
    │
    ├─ filterGrid.setFilterColumns(filterColumns)
    │
    └─ if (HRStringUtils.isNotEmpty(ruleStr))
        ├─ FilterCondition fc = SerializationUtils.fromJsonString(ruleStr, FilterCondition.class)
        └─ filterGrid.SetValue(fc)
```

读取的字段：`entitynum.number`（pkValue）+ `rule`。

### 2.2 OP beforeExecute 时的 read（HRDataRuleSaveOp）

```java
// HRDataRuleSaveOp.java#beforeExecuteOperationTransaction
// 读字段：id, rule
long dataRuleId = item.getLong("id");
String currentRule = item.getString("rule");

// 通过 HRBaseServiceHelper 反查老值（独立查询）
HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_datarule");
DynamicObject old = helper.queryOne(dataRuleId);
String oldEntityNum = old.getString("entitynum.number");
String oldRule = old.getString("rule");
```

读字段汇总：`id`, `rule`, `entitynum.number`（外键级联）。

### 2.3 权限链消费（业务侧消费数据规则的查询）

```
某 form 列表/表单做权限计算时（hrcs_datarule 的"客户"）
    │
    ├─ 当前用户 + 当前 form (entitynum)
    │
    ▼
查询 hrcs_datarule WHERE
    fstatus = 'C' (audit)
    AND fenable = '1'
    AND fentitynum.fnumber = '<当前 form>'
    │
    ▼
拿到 rule 字符串 → SerializationUtils.fromJsonString(rule, FilterCondition.class)
    │
    ▼
new FilterBuilder(targetEntityType, fc).buildFilter()
    │
    ▼
得到 SQL where 子句 · 加到当前查询的 QFilter 列表
```

## 三、写路径

### 3.1 save 写路径（最复杂）

```
[FormPlugin#beforeDoOperation]
    │
    ├─ FilterGrid.getFilterGridState().getFilterCondition()  (read FilterGrid)
    ├─ SerializationUtils.toJsonString(fc)                   (in-memory)
    └─ getModel().setValue("rule", json)                     (write IDataModel)
    ▼
[OP beforeExecute] HRDataRuleSaveOp
    │
    ├─ 读：id, rule  (item.getLong / item.getString)
    ├─ 反查老值（HRBaseServiceHelper.queryOne）：entitynum.number, rule  (read DB)
    ├─ 比对 (DataRuleLogServiceHelper.compareFilterControls)
    └─ 写 OperationOption variables（不写 DB）
        ├─ operate_<id> = "1"
        ├─ originalRule_<id> = oldRule
        └─ beforeData_<id> = JSON(beforeLogModel)
    ▼
[OP beforeExecute] 标品 OP 链
    ├─ HRBaseDataStatusOp · 校验 + 改 status（如有需要）
    ├─ HRBaseDataLogOp · 准备日志
    ├─ HRBaseDataEnableOp · 校验
    └─ HRBaseOriginalOp · 出厂数据原始值
    ▼
[事务] DML：UPDATE t_hrcs_datarule SET frule=?, fname=?, fstatus=?, ... WHERE fid=?
    │
    ▼
[OP afterExecute] HRDataRuleSaveOp（事务已提交 · 只读 + 异步操作）
    │
    ├─ if isChange:
    │   ├─ PermNotifyService.notifyByDataRule(id)  (远程调用)
    │   ├─ HRPermCacheMgr.clearCache(2 个 key)     (清缓存)
    │   └─ DataRuleLogServiceHelper.dataRuleLogInit("modify", model)  (写日志表 · DB)
    ▼
[OP afterExecute] 标品 HRBaseDataLogOp · 写标品日志表
```

### 3.2 audit / unaudit 写路径（短）

```
[OP beforeExecute] HRBaseDataStatusOp
    └─ item.set("status", "C" | "B")  (in-memory · 标品代码)
    ▼
[事务] UPDATE t_hrcs_datarule SET fstatus = ? WHERE fid = ?
    ▼
[OP afterExecute] 标品 HRBaseDataLogOp 写标品日志
    ▼（建议 ISV 加）
ISV AuditOp / UnauditOp · 调权限缓存 + 通知
```

### 3.3 disable / enable 写路径

```
[OP beforeExecute] HRBaseDataEnableOp
    ├─ item.set("enable", "0" | "1")
    ├─ disable: item.set("disabler", currUserId); item.set("disabledate", now)
    └─ enable: item.set("disabler", null); item.set("disabledate", null)
    ▼
[事务] UPDATE t_hrcs_datarule SET fenable=?, FDisablerID=?, FDisableDate=? WHERE fid=?
    ▼
[OP afterExecute] HRBaseDataLogOp 标品日志
```

### 3.4 delete 写路径

```
[OP beforeExecute] CodeRuleDeleteOp · 释放编码号段（如有）
[OP beforeExecute] BdVersionSaveServicePlugin · 标品基础资料 delete
    └─ DELETE FROM t_hrcs_datarule WHERE fid = ?
    ▼
[事务] 提交
    ▼
[OP afterExecute] HRBaseDataLogOp 标品日志
```

⚠️ **不会**级联删除 hrcs_dynascheme 中引用本规则的方案 · 但方案中的 rule 引用会失效（"指空"）。

## 四、事务边界

| 阶段 | 是否在事务内 | 说明 |
|---|---|---|
| FormPlugin#beforeDoOperation | ❌ 否 | 客户端事件 · 不在 DB 事务 |
| OP onAddValidators | ❌ 否 | 注册 Validator · 不操作 DB |
| Validator#validate | ❌ 否 | 校验阶段 · 失败抛 KDBizException |
| OP beforeExecute | ✅ 是 | 进事务 · 抛异常会回滚 |
| 事务主体（DML） | ✅ 是 | INSERT/UPDATE/DELETE 主表 |
| OP endOperation | ✅ 是 | 事务即将提交 |
| OP afterExecute | ❌ 否（已提交）| **此时调外部服务/BEC 安全** |

⚠️ **`HRDataRuleSaveOp.afterExecute` 调 `PermNotifyService.notifyByDataRule` + `DataRuleLogServiceHelper.dataRuleLogInit` · 都在事务外** · 如果通知失败不会回滚已 save 的规则 · 是合理设计（PR-010）。

但反过来 · `dataRuleLogInit` 写日志表是新事务 · 它失败也不影响主事务 · 但日志会丢。

## 五、失败回滚策略

### 5.1 FormPlugin 阶段失败

```java
// HRDataRuleEditPlugin#doSave
if (!canSave) {
    args.setCancel(true);
    args.setCancelMessage("请配置规则。");
    this.getView().showErrorNotification(...);
}
```

直接 setCancel · 不进 OP · 不进事务 · 用户看到错误提示。

### 5.2 OP onAddValidators / Validator 阶段失败

Validator 抛 KDBizException · 操作整体取消 · 不进事务 · 不写 DB。

### 5.3 OP beforeExecute 阶段失败

抛 KDBizException 或 args.addErrorMessage · **整个事务回滚** · DB 无变更。

### 5.4 OP afterExecute 阶段失败

事务已提交 · **DB 已落库** · afterExecute 抛异常**不会回滚**：
- `PermNotifyService.notifyByDataRule` 失败 → 缓存没清 → 下次访问规则时重新加载（最终一致）
- `DataRuleLogServiceHelper.dataRuleLogInit` 失败 → 日志丢失 · 业务数据无影响

⚠️ ISV 加 BEC 发布（CS-05）建议放 afterExecute · 但要做幂等处理。

## 六、数据流的关键观察

### 6.1 rule 字段的"半结构化"特性

- 物理列 `frule` · 类型 TextField · DB 上是字符串
- 业务上是 FilterCondition JSON · 真实结构化数据
- ⚠️ 直接 SQL 改 frule 列绕过校验 → 规则可能引用不存在的字段
- ⚠️ QueryServiceHelper.queryDataSet("hrcs_datarule") + 用 LIKE '%xxx%' 查 rule 内容 · 性能差

### 6.2 entitynum 跨业务对象的耦合

`entitynum` 引用 `bos_entityobject` · 数据规则跟它保护的业务 form 强耦合：

| 业务对象动作 | 对数据规则的影响 |
|---|---|
| 业务对象删除 | rule 引用消失字段 → FilterBuilder 报错 |
| 业务对象重命名 | rule 字段名不变 · 但下游不识别 |
| 业务对象加字段 | 不影响（向后兼容）|
| 业务对象类型变 | rule 可能字段类型不匹配 → FilterBuilder 报错 |

ISV 在 CS-03 加 audit / enable 二次 FilterBuilder 校验 · 是处理这类 drift 的关键。

### 6.3 状态字段相互独立

`status`（draft/saved/audit/submitted）和 `enable`（0/1）是两套独立状态机：
- 状态字段更新只触发各自的 OP（HRBaseDataStatusOp / HRBaseDataEnableOp）
- 不存在"状态联动"自动化（如 audit 自动 enable=1）
- 业务上"规则生效"是 `status=audit AND enable=1` 联合判断

## 七、缓存层

### 7.1 hrcs 内置 2 个数据规则缓存 key（实证）

```java
// HRDataRuleSaveOp.java L76-L78
String[] dataRuleCacheKeyArr = new String[]{
    HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_DATA_RULE"),
    HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_BD_DATA_RULE")
};
HRPermCacheMgr.clearCache((String[])dataRuleCacheKeyArr);
```

| Cache Key Prefix | 用途 |
|---|---|
| `BS_HR_PERM_DATA_RULE` | 普通业务对象的数据规则缓存 |
| `BS_HR_PERM_BD_DATA_RULE` | 基础资料类业务对象的数据规则缓存 |

⚠️ ISV 不要 `clearCache` 同样的 key prefix · 会跟标品争 · 自己的 ISV 缓存用独立 prefix（如 `${ISV_FLAG}_DATA_RULE_EXT`）。

### 7.2 缓存清理时机

| 时机 | 是否清缓存 | 实证 |
|---|---|---|
| save（rule 变更） | ✅ 清 | HRDataRuleSaveOp.afterExecute |
| save（rule 未变） | ❌ 不清 | isChange 判断 |
| audit / unaudit | ❌ 不清（标品潜在问题）| 标品 OP 没清 · CS-03 建议补 |
| disable / enable | ❌ 不清（标品潜在问题）| 标品 OP 没清 · CS-03 建议补 |
| delete | ❌ 不清（标品潜在问题）| 标品 OP 没清 |

## 八、日志层（DataRuleLog）

### 8.1 日志触发时机

实证：仅 `save · modify` 模式有标品日志（HRDataRuleSaveOp.afterExecute）：

```java
DataRuleLogModel beforeData = SerializationUtils.fromJsonString(beforeDataStr, DataRuleLogModel.class);
DataRuleLogModel afterData = DataRuleLogServiceHelper.getDataRuleLogModel(dataRuleId, false);
afterData.setBeforeDataRuleModel(beforeData);
DataRuleLogServiceHelper.dataRuleLogInit("modify", afterData);
```

### 8.2 日志缺口

audit / unaudit / disable / enable / delete / submit / unsubmit 标品 OP **都不写 DataRuleLog** · 这是潜在合规风险。

ISV 在 CS-03 自建 OP 时 · 建议复用 `DataRuleLogServiceHelper.dataRuleLogInit(modeStr, model)` 补齐。

| 操作 | 标品日志 | DataRuleLog | 建议 |
|---|---|---|---|
| save (modify) | ✅ HRBaseDataLogOp | ✅ 实证 | 已齐 |
| save (new) | ✅ HRBaseDataLogOp | ❌ 没（HRDataRuleSaveOp 只处理 dataRuleId !=0 的 modify）| ISV CS-03 补 |
| delete | ✅ HRBaseDataLogOp | ❌ | ISV CS-03 补 |
| audit / unaudit | ✅ HRBaseDataLogOp | ❌ | ISV CS-03 补 |
| disable / enable | ✅ HRBaseDataLogOp | ❌ | ISV CS-03 补 |
| submit / unsubmit | ✅ HRBaseDataLogOp | ❌ | ISV CS-03 补 |
