# 业务流转 · 数据规则 (hrcs_datarule)

> **状态**：基于反编译 + 42 opKey 实抓 + 8 核心 opKey 富化执行链整合
> **confidence**：real_deploy（核心 opKey 状态机来自 `HRBaseDataStatusOp` + `HRBaseDataEnableOp` 标品 + scene_doc.json status/enable 字段语义）

## 一、双状态机（status + enable）

数据规则有 **两套独立状态机**，互不干扰：

### 1.1 单据状态 `status`（draft / saved / audit / submitted）

```
                   save
   ┌─────────────────────────────────┐
   ↓                                 │
draft (A) ──save──→ saved (B) ──audit──→ audit (C)
   ↑                  ↑                    │
   │                  └──unaudit───────────┘
   │
   └──submit──→ submitted (D) ──unsubmit──→ draft
```

| status | 标品状态码 | 进入 | 退出 | 业务含义 |
|---|---|---|---|---|
| draft | A（推断） | new / unsubmit | save / submit | 草稿 · 不参与权限链 |
| saved | B | save / unaudit | audit / delete | 已保存 · 还未生效 |
| audit | C | audit | unaudit | **已审核 · 进入权限链生效** |
| submitted | D | submit | unsubmit | 已提交（流程态）|

⚠️ **进入权限链的关键节点是 audit · 不是 save**。

### 1.2 启用状态 `enable`（独立于 status · 0/1 二元）

```
enable=1 (默认) ──disable──→ enable=0
        ↑                    │
        └──enable────────────┘
```

| enable | 含义 | 进入 OP | 副作用 |
|---|---|---|---|
| 1 | 启用 | enable | 清空 disabler / disabledate |
| 0 | 禁用 | disable | 写 disabler（当前用户）+ disabledate（now）|

⚠️ **enable=0 的规则在权限链中被跳过 · 等价于软删除**。

### 1.3 双状态联合判定

权限链使用规则的判定（建议 ISV 在 CS 中遵循）：

```
ruleEffective = (status == "audit") AND (enable == "1")
```

不满足任一条件 · 规则不参与权限计算。

## 二、核心操作时序图

### 2.1 save 操作（最复杂 · 7 步）

```
用户点保存
    │
    ▼
[FormPlugin] HRDataRuleEditPlugin#beforeDoOperation
    │  ├─ if operateKey=save → doSave(args)
    │  │   ├─ 取 FilterGrid 当前 FilterCondition
    │  │   ├─ 校验非空 (B_SAVE_2 / V_SAVE_3)
    │  │   ├─ FilterBuilder.buildFilter() 校验字段合法 (V_SAVE_4)
    │  │   ├─ SerializationUtils.toJsonString(fc)
    │  │   └─ getModel().setValue("rule", json)  (B_SAVE_2)
    │  └─ FormOperate.option.tag_of_view = true
    ▼
[OP onAddValidators] HRDataRuleSaveOp#onAddValidators
    │  └─ args.addValidator(new HRDataRuleSaveValidator())  (V_SAVE_1)
    ▼
[OP onAddValidators] HRBaseDataStatusOp · 标品状态校验  (V_SAVE_2)
    ▼
[Validator phase] · 跑所有 Validator · 阻断或继续
    ▼
[OP beforeExecute] HRDataRuleSaveOp#beforeExecuteOperationTransaction
    │  ├─ for each dataEntity:
    │  │   ├─ compareFilterCondition(id, rule)
    │  │   │   ├─ HRBaseServiceHelper.queryOne(id) 查老 rule
    │  │   │   └─ DataRuleLogServiceHelper.compareFilterControls(...)
    │  │   ├─ if changeFlag:
    │  │   │   ├─ option.set("operate_<id>", "1")  (B_SAVE_4)
    │  │   │   ├─ option.set("originalRule_<id>", oldRule)
    │  │   │   └─ option.set("beforeData_<id>", JSON(beforeLogModel))
    │  └─（结束）
    ▼
[OP beforeExecute] 标品 HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp
    ▼
[事务] 主事务执行（DML）
    │
    ▼
[OP afterExecute] HRDataRuleSaveOp#afterExecuteOperationTransaction
    │  ├─ for each dataEntity:
    │  │   ├─ if isChange && oldRule != newRule:
    │  │   │   ├─ PermNotifyService.notifyByDataRule(id)  (B_SAVE_5)
    │  │   │   └─ HRPermCacheMgr.clearCache([
    │  │   │       BS_HR_PERM_DATA_RULE,
    │  │   │       BS_HR_PERM_BD_DATA_RULE
    │  │   │     ])  (B_SAVE_6)
    │  │   └─ if isChange:
    │  │       ├─ getDataRuleLogModel(id, false) 拿当前
    │  │       ├─ setBeforeDataRuleModel(beforeData)
    │  │       └─ DataRuleLogServiceHelper.dataRuleLogInit("modify", model)  (B_SAVE_7)
    ▼
[OP afterExecute] 标品 HRBaseDataLogOp · 写操作日志
    ▼
返回操作结果
```

**关键观察**：
1. FormPlugin 阶段已序列化 rule + 校验非空 + FilterBuilder 校验 · OP 阶段不再校
2. OP beforeExecute 用 OperationOption 暂存"是否变更" · afterExecute 才决定是否清缓存（避免无变更也清缓存）
3. afterExecute 是 BEC 发布的合理时机（PR-010 · 主事务已提交）· 但**实证 hrcs_datarule 标品 0 处发布**

### 2.2 audit 操作（审核 · 让规则进入权限链）

```
用户点审核
    │
    ▼
[FormPlugin] HRDataRuleEditPlugin#beforeDoOperation · 设 tag_of_view
    ▼
[OP onAddValidators] HRBaseDataStatusOp · 状态合法性  (V_AUDIT_1)
[OP onAddValidators] 元数据 · entitynum + rule 必填  (V_AUDIT_2)
    ▼
[OP beforeExecute] HRBaseDataStatusOp · status: B → C  (B_AUDIT_1)
[OP beforeExecute] HRBaseOriginalOp · 出厂数据原始值
    ▼
[事务] 提交
    ▼
[OP afterExecute] HRBaseDataLogOp · 标品日志
[OP afterExecute] （建议 ISV 自建 AuditOp）通知权限链 + 清缓存  (B_AUDIT_4 · recommended)
```

⚠️ **关键**：标品 audit OP 没有 hrcs_datarule 专属插件 · 因此**审核后规则虽然 status=audit · 但 hrcs 权限缓存可能没清 · 下次访问规则才会重新加载**。

ISV 想"审核立即生效" · 需要在 CS-03 自建 AuditOp · 在 afterExecute 调 `HRPermCacheMgr.clearCache + PermNotifyService.notifyByDataRule`。

### 2.3 disable 操作（禁用 · 软删除）

```
用户点禁用
    │
    ▼
[FormPlugin] HRDataRuleEditPlugin#beforeDoOperation · 设 tag_of_view
    ▼
[OP onAddValidators] HRBaseDataEnableOp · 当前 enable=1 才能禁  (V_DIS_1)
    ▼
[OP beforeExecute] HRBaseDataEnableOp · enable: 1 → 0  (B_DIS_1)
              · disabler = 当前用户 (RequestContext.getCurrUserId)
              · disabledate = now
    ▼
[事务] 提交
    ▼
[OP afterExecute] HRBaseDataLogOp · 标品日志
[OP afterExecute] （建议 ISV 自建 DisableOp）通知权限链 + 清缓存  (B_DIS_2 · recommended)
```

### 2.4 delete 操作（删除 · 标品基础资料 delete）

```
用户点删除
    │
    ▼
[OP onAddValidators] HRBaseDataStatusOp · 必须 draft/saved 状态才能删  (V_DELETE_1)
[OP onAddValidators] HRBaseDataStatusOp · issyspreset=true 禁删  (V_DELETE_2)
    ▼
[OP beforeExecute] CodeRuleDeleteOp · 释放编码号段
[OP beforeExecute] BdVersionSaveServicePlugin · 删主表 + 多语言子表
    ▼
[事务] 提交
    ▼
[OP afterExecute] HRBaseDataLogOp · 删除日志
```

⚠️ **标品 delete 不会查下游引用** · 数据规则可能正在被 hrcs_dynascheme / hrcs_role 使用 · 删除后会让权限方案"指空" · 强烈建议 ISV 在 CS-04 自建 DeleteValidator。

## 三、新建数据规则的 UX 流程

```
┌─────────────────────────────────────────────────────┐
│  用户进入"数据规则"列表                              │
│      ↓ 点新增                                        │
│  HRAdminStrictPlugin#preOpenForm                    │
│  ├─ 校验 isAdmin / isCosmic + isHrAdmin             │
│  └─ 通过 → 打开新建表单                              │
│      ↓                                              │
│  HRDataRuleEditPlugin#afterCreateNewData            │
│  ├─ 取 fsp_custom_param_entitynum                   │
│  ├─ 有 → 预填 entitynum + 锁定                       │
│  └─ 无 → entitynum 可编辑                           │
│      ↓                                              │
│  用户选 entitynum (F7 弹窗)                          │
│  ├─ HRDataRuleEditPlugin#beforeF7Select             │
│  ├─ HisModelServiceHelper.isInheritHisModelTemplate │
│  └─ 是时序 → 加 iscurrentversion=1 过滤             │
│      ↓                                              │
│  HRDataRuleEditPlugin#propertyChanged (entitynum)   │
│  ├─ clearDataPermFilterGrid()                       │
│  └─ refreshFilterGrid() · 加载新 entitynum 字段集   │
│      ↓                                              │
│  用户在 FilterGrid 上配规则（FilterCondition）       │
│      ↓                                              │
│  用户点保存 → save 操作时序图（见 2.1）              │
└─────────────────────────────────────────────────────┘
```

## 四、查看 / 修改数据规则的 UX 流程

```
┌─────────────────────────────────────────────────────┐
│  HRAdminStrictPlugin#preOpenForm · 准入校验          │
│      ↓                                              │
│  HRDataRuleEditPlugin#afterLoadData                 │
│  ├─ setEnable(false, "entitynum")  · entitynum 永久锁│
│  └─ 视图态 → 隐藏 FilterGrid 添加按钮                │
│      ↓                                              │
│  HRDataRuleEditPlugin#afterBindData                 │
│  └─ refreshFilterGrid()                             │
│      ├─ FilterGrid.setEntityNumber(entitynum)       │
│      ├─ EntityTypeUtil.createFilterColumns(...)     │
│      └─ 反序列化 rule (JSON → FilterCondition)      │
│         FilterGrid.SetValue(fc)                     │
│      ↓                                              │
│  用户编辑规则 (FilterGrid 改 FilterCondition)        │
│      ↓                                              │
│  用户点保存 → 同 2.1 时序                             │
└─────────────────────────────────────────────────────┘
```

## 五、规则真正"生效"的判定（下游消费）

⚠️ 这是数据规则的最终业务价值 · ISV 改逻辑前要懂：

```
权限链查询时（kd.hr.hrcs.bussiness.service.perm.* 内部）
    │
    ├─ 拿到当前用户 + 业务对象 entitynum
    │
    ├─ 查 hrcs_datarule WHERE
    │     status = "audit" (C)
    │     AND enable = "1"
    │     AND entitynum.number = <当前 form>
    │
    ├─ 反序列化 rule (JSON → FilterCondition)
    │
    ├─ FilterBuilder.buildFilter() → SQL where 子句
    │
    └─ 加到当前查询的 QFilter 上
```

⚠️ **改 rule 字段后**（save）· `HRDataRuleSaveOp.afterExecute` 会调 `PermNotifyService.notifyByDataRule(id)` + 清 2 个缓存 key（`BS_HR_PERM_DATA_RULE` / `BS_HR_PERM_BD_DATA_RULE`）· 让权限链下次查询重新加载规则。

但 **audit / disable / enable / delete OP 都不调** · 这是标品的潜在问题 · ISV 必要时自建 OP 补全。

## 六、并发场景 · 同一规则同时被多人改

数据规则不是 HisModel 时序基础资料 · 没有版本控制 · 因此并发改会以**最后写为准**（last-writer-wins）。

实证：`HRDataRuleSaveOp.compareFilterCondition` 用 `HRBaseServiceHelper.queryOne(id)` 查老值 · 但**不加锁** · 如果两个并发请求都过了 compareFilterCondition · 后者的写会覆盖前者。

⚠️ ISV 真要解决并发冲突 · 需要在 CS-03 加 Validator · 用 `version` 字段或 `modifytime` 字段做乐观锁。但目前 hrcs_datarule **没有 version 字段** · 标品也没加。

## 七、其他 34 opKey 流程概览

详见 `rules_chain_all.json` 各 opKey · 这里只列业务流转：

| opKey 分组 | 业务含义 | 用户感知 |
|---|---|---|
| new / modify / view | 新建 / 修改 / 查看（FormPlugin afterCreateNewData / afterLoadData 处理）| 打开表单 |
| copy / refresh / close | 复制 / 刷新 / 关闭（标品基础资料能力）| 列表/表单按钮 |
| first / previous / next / last | 翻页（标品基础资料能力）| 列表翻页 |
| importdata_hr 系列 6 个 | HR 导入数据 + 模板管理（标品 HRBaseDataImportEdit）| 导入按钮 |
| importdata 系列 6 个 + exportlist 系列 4 个 | 导入导出（标品 HRBaseDataCommonList）| 导入导出按钮 |
| submitandnew / saveandnew | 提交并新增 / 保存并新增（标品基础资料）| 表单按钮 |
| mobtoolbarselect / mobtoolbarcancel | 移动端选择 / 取消（标品 HRBaseDataCommonMobList）| 移动端 |
| namehistory / namehistoryview / viewonelog / logview | 改名 / 名称历史 / 查看日志 · 4 个 donothing 路由（标品 HRBasedataLogList）| 列表按钮 |

## 八、流程相关的扩展边界（导引到 06）

| 流程节点 | 适合的扩展模式 | CS 方案 |
|---|---|---|
| save 前 · FormPlugin 校验 | 写 ISV FormPlugin · 挂 beforeDoOperation | CS-02 / CS-03 |
| save 时 · OP 校验 | 写 ISV Validator · 挂 onAddValidators | CS-03 |
| save 时 · 字段联动 | 写 ISV FormPlugin · 挂 propertyChanged | CS-02 |
| save 后 · 通知下游 | 写 ISV OP · 挂 afterExecuteOperationTransaction · 调 BEC | CS-05 |
| delete 前 · 查下游引用 | 写 ISV Validator · 挂 onAddValidators | CS-04 |
| 数据规则参数项扩展（行 ID）| ID 用 kd.bos.id.ID | CS-06 |
| 列表过滤（按 entitynum 范围）| 写 ISV ListPlugin · 挂 setFilter | CS-07 |
| 加自定义业务字段 | modifyMeta add field | CS-01 |

详见 `06_customization_solutions.md`。
