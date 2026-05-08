# 异常诊断 · 组织调整申请单

> **状态**: 🟢 基于反编译 ResId 实证 + 跨 CS 共性踩坑整合
> **confidence**: verified
> **数据源**: `AdminOrgBatchBillPlugin.java`（41 处 ResManager.loadKDString）+ `AdminOrgBatchBillListPlugin.java` + 8 反编译类异常

---

## 一、用户级错误码（ResId 实证）

来源：反编译类的 `ResManager.loadKDString(... ResId ...)` · 跨场景定位异常时按 ResId 搜代码：

### `AdminOrgBatchBillPlugin` (FormPlugin · 67 个 ResId)

| ResId | 中文消息 | 出现位置 | 触发场景 |
|---|---|---|---|
| `AdminorgBatchBillPlugin_1` | 检测到您有更改内容，是否不保存直接退出？若直接退出，将丢失这些更改。 | L407 | beforeClosed · 有未保存数据时 |
| `AdminorgBatchBillPlugin_2` | 返回编辑 | L404 | beforeClosed 弹窗 |
| `AdminorgBatchBillPlugin_3` | 直接退出 | L405 | beforeClosed 弹窗 |
| `AdminorgBatchBillPlugin_4` | %s的单据不能删除调整明细。 | L637 | deleteRowsConfirm · billstatus 不允许删除 |
| `AdminorgBatchBillPlugin_6` | 删除选中的数据后将无法恢复（选中条数：%s），确定要删除吗？ | L656 | 标准删除确认 |
| `AdminorgBatchBillPlugin_7` | 请选中需要操作的行。 | L658 | deleteRowsConfirm · 未选行 |
| `AdminorgBatchBillPlugin_9` | 请选中需要删除的行。 | L646 | deleteRowsConfirm · 拿不到 EntryGrid |
| `AdminorgBatchBillPlugin_10` | 签发组织 | L758 | disorg F7 caption |
| `AdminorgBatchBillPlugin_11` | 上级行政组织 | L800 | parent_parentorg F7 caption |
| `AdminorgBatchBillPlugin_12` | 所在城市 | L868 | parent_city / info_city F7 caption |
| `AdminorgBatchBillPlugin_16` | 组织调整申请-%s | L286 | loadCustomControlMetas 标题（带 billno） |
| `AdminorgBatchBillPlugin_38` | 组织调整申请 | L281 | loadCustomControlMetas 默认标题 |
| `AdminorgBatchBillPlugin_39` | %s 删除拆分数据后系统将不会自动删除该条变动数据，需用户手动删除，确定删除拆分数据？ | L700 | 删 split 行二次确认 |
| `AdminorgBatchBillPlugin_41` | %s 删除合并数据后系统将不会自动删除该条变动数据，需用户手动删除，确定删除合并数据？ | L697 | 删 merge 行二次确认 |
| `AdminorgBatchBillPlugin_42` | 行政组织"%1$s"在本单中已经发起"%2$s"操作，不允许同时对组织进行多种变动操作。 | L693 | 跨 entry 互斥（核心规则） |
| `AdminorgBatchBillPlugin_64` | 请先填写组织体系管理组织。 | L440 | import_multientry_hr · 主表 org 未填 |
| `AdminorgBatchBillPlugin_65` | %s的单据不能导入。 | L450 | import · billstatus 不在 A/G |
| `AdminorgBatchBillPlugin_66` | 已存在 | L609 | afterSubmitError · 编码已存在 |

### `AdminOrgBatchBillListPlugin` (ListPlugin)

| ResId | 中文消息 | 出现位置 | 触发场景 |
|---|---|---|---|
| `AdminorgBatchBillPlugin_19` | 删除成功 | L347 | 列表删除成功 |
| `AdminorgBatchBillPlugin_27` | 废弃成功 | L361 | 列表废弃成功 |
| `AdminorgBatchBillPlugin_35` | 您的"组织调整申请列表"的删除权限已发生变更，无法继续操作，请重新打开页面。 | L337 | 删除权限变更 |
| `AdminorgBatchBillPlugin_36` | 您的"组织调整申请列表"的废弃权限已发生变更，无法继续操作，请重新打开页面。 | L353 | 废弃权限变更 |
| `AdminorgBatchBillPlugin_67` | 一次只能操作一条数据。 | L312 | breakup 多选 |

### `AdminorgFastChgPlugin` 重用消息

| ResId | 中文消息 | 触发场景 |
|---|---|---|
| `AdminorgFastChgPlugin_9` | 行政组织 | merge/split target F7 caption |
| `AdminorgFastChgPlugin_31` | 请先选择组织体系管理组织。 | F7 选择前 BU 未选 |

---

## 二、典型异常场景速查

### 异常 1 · "请先填写组织体系管理组织"

| 维度 | 内容 |
|---|---|
| ResId | `AdminorgBatchBillPlugin_64` |
| 触发 | 用户在主表未选 `org`（BU）就点【导入多分录数据】 |
| 实证 | `AdminOrgBatchBillPlugin.java:440` |
| 解决 | 用户先在主表选 BU 再导入 |
| ISV 扩展时 | 自定义按钮要在 beforeDoOperation 同样校验 org · 防绕过 |

### 异常 2 · "%s 的单据不能导入"

| 维度 | 内容 |
|---|---|
| ResId | `AdminorgBatchBillPlugin_65` |
| 触发 | billstatus 不在 A/G · 即只有暂存或终止状态才能导入数据 |
| 实证 | `AdminOrgBatchBillPlugin.java:450` |
| 解决 | 等审批完或终止后再导入 |

### 异常 3 · 跨 entry 互斥（核心规则违反）

| 维度 | 内容 |
|---|---|
| ResId | `AdminorgBatchBillPlugin_42` |
| 触发 | 同 1 个组织在 2 个 entry（如 add + parent）有变动 |
| 实证 | `AdminOrgBatchBillPlugin.java:691-693` |
| 解决 | 拆成 2 张申请单 · 或先撤销其中一个 entry 的变动 |
| ISV 扩展时 | **不要扩展放过这条** · 平台铁律 |

### 异常 4 · "%s 的单据不能删除调整明细"

| 维度 | 内容 |
|---|---|
| ResId | `AdminorgBatchBillPlugin_4` |
| 触发 | 单据状态非 A/G 时尝试删 entry 行 · 或非 wftask 审批人在 B 状态尝试删 |
| 实证 | `AdminOrgBatchBillPlugin.java:633-637` |
| 解决 | 先 unsubmit 撤回到 A 状态 · 或让审批人在 wftask 改 |

### 异常 5 · 终止只能选 1 条

| 维度 | 内容 |
|---|---|
| ResId | `AdminorgBatchBillPlugin_67` |
| 触发 | 列表多选 + 点击【终止】 |
| 实证 | `AdminOrgBatchBillListPlugin.java:312` |
| 解决 | 单选 1 条再【终止】 · 多张需要循环操作 |
| ISV 扩展时 | CS-07 加校验时不要破坏这个规则 |

### 异常 6 · 权限变更冲击

| ResId | 触发 |
|---|---|
| `AdminorgBatchBillPlugin_35` | 删除权限变更 |
| `AdminorgBatchBillPlugin_36` | 废弃权限变更 |

| 维度 | 内容 |
|---|---|
| 触发 | 用户已经打开列表 · 此期间管理员调整了用户权限 · 用户点删除/废弃时权限码 `4715e1f1000000ac`（删除）/ `0=KX5+R7YTRT`（废弃） 检测失败 |
| 解决 | 关闭列表重新打开（重新加载权限） |
| ISV 扩展时 | ISV 自定义按钮也要类似检 PermissionServiceHelper.checkPermission |

---

## 三、CS 实施时常见报错（按 CS 分类）

### CS-01 加 entry 字段

| 错误 | 原因 | 解决 |
|---|---|---|
| `field already exists` | 字段 key 冲突（与主表或其他 entry 重名） | 字段 key 必须带 entry 前缀（如 `add_tdkw_x`） |
| 字段加成功但物理表无列 | PDM 未同步 | 走苍穹开发平台 → PDM 同步 |
| `column name too long` | fieldName 超过 25 字符 | 改短字段 key（数据库列名 = `f` + key.toLower）|
| 字段加在 entryentity_all 但其他 6 entry 收不到 | entryentity_all 是只读视图 | 改加在 4 个写入 entry 之一 |

### CS-02 / CS-04 加 Validator

| 错误 | 原因 | 解决 |
|---|---|---|
| `KDBizException: ...` 全单阻断 | Validator 抛 KDBizException | 改用 `addErrorMessage(ext, msg)` 逐行高亮 |
| 校验在 OP beforeExec 而非 onAddValidators | 校验时机不对 | 移到 onAddValidators · 统一注册 Validator |
| Validator 死循环 | validate() 里 setValue 触发 propertyChanged | Validator 不能 setValue · 只能 addErrorMessage |
| 跨租户编码冲突 | `haos_adminorg` 没带 BU 维度 | 加 `org_id = X` 过滤 |
| 误报历史版本 | 没带 `iscurrentversion=1` | 加上（PR-008） |

### CS-03 字段联动

| 错误 | 原因 | 解决 |
|---|---|---|
| 死循环 / 浏览器卡死 | setValue 没包 beginInit/endInit（PR-004） | 严格 `beginInit() → setValue → endInit() → updateView` |
| 字段值不更新 | 缺 `getView().updateView(key, rowIndex)` | 加上 |
| 主表字段 setValue 写到 entry 行 | 没传 rowIndex | setValue 第三参传 rowIndex |
| propertyChanged 同时触发标品和 ISV | 标品 / ISV 各自处理 | 两者并列 · 互不影响（PR-001） |

### CS-05 BEC 订阅方

| 错误 | 原因 | 解决 |
|---|---|---|
| `eventNumber not found` | eventNumber 错或环境未注册 | 走【业务事件中心】查准确编号 |
| 订阅方不触发 | 订阅未部署或停用 | 检查【事件订阅】列表 + 部署状态 |
| 重复处理 | 没做幂等 | 按 `evt.getEventId()` 去重（PR-011） |
| 查 hrpi_empjobrel 漏数据 | 没带 boid + iscurrentversion | 加上（PR-008 + PR-009） |
| 订阅方拖死队列 | handleEvent 里同步处理慢 | 转后台任务 / 切批量处理 |
| `KDBizEvent.source` 解析报错 | 没识别 JSON 字符串结构 | 标品发布的格式：`[{data: [...]}]` |

### CS-06 同步 ERP

| 错误 | 原因 | 解决 |
|---|---|---|
| 用户操作假死 30s+ | 外系无响应 + 没设 timeout | 必须设 timeout（如 5000ms） |
| 红错框 + 数据已生效 | afterExec 抛异常 · 但事务已提交 | catch + 写补偿表 + 不抛 |
| 重复推送外系 | 重试时没幂等 | 用 billno 当幂等 key |
| 大批量卡 audit | 一次审批 100+ entry · 逐个调外系 | 改 CS-05 异步 |

### CS-07 终止校验

| 错误 | 原因 | 解决 |
|---|---|---|
| 阈值硬编不能改 | 把 5 写死 | 改系统参数（如 `${ISV_FLAG}.homs.breakup.max_org`） |
| 提示给用户暴露 boid | 误用 boid 显示给用户 | 改用 number / name |
| 阻断管理员 | 自助 + 管理员都用同一插件 | 按当前用户角色判断 · 管理员跳过 |

---

## 四、状态机异常（不要踩）

### 不要直接 setValue billstatus

```java
// ❌ 反模式 · 越过状态机
bill.set("billstatus", "C");

// ✅ 正确：走 invokeOperation
this.getView().invokeOperation("submiteffect");
```

如果 ISV 在自定义 OP 直接 set billstatus · 会出现：
- billstatus 已改 · 但 haos_adminorg 没生效 · 数据假死
- wf 审批节点跟不上 · 永远卡在审批中

### A → C 必须经 B 或 submiteffect

| 起始状态 | 目标状态 | 合法路径 |
|---|---|---|
| A | C | submiteffect 直接 / submit + audit 走审批 |
| A | F | discard_row 列表废弃 |
| A | G | breakup |
| B | C | audit |
| B | A | unsubmit |
| B | G | breakup |
| C | G | breakup（高风险 · 反向回退） |
| F | * | 不可逆 |
| G | * | 不可逆 |

ISV 扩展时不要尝试跳过中间状态。

---

## 五、跨场景共性陷阱（来自 cosmic_realworld_traps）

### `buildmeta_traps.md` 相关

| 陷阱 | 应用本场景 |
|---|---|
| FieldType 74 值枚举 · `EmployeeField` / `MultiLangTextField` 不支持 | 加 entry 字段时改用 `BasedataField` + `MuliLangTextField`（一个 t） |
| ENTITY_PARENT_ID 必须显式 | buildMeta 不传 parentId 兜底到 bos 基础资料 · 但本场景一般不 buildMeta · 走 modifyMeta |
| key + columnName 总长 ≤ 25 字符 | 加自定义字段时算字数（`f` 前缀 + key.toLower） |
| 命名规则 | `{entry_prefix}_tdkw_{业务名}` |

### `addrule_traps.md` 相关

| 陷阱 | 应用本场景 |
|---|---|
| ActionType 必须 PascalCase | 加规则时严格大写 |
| preCondition 不能用 `==''` | 用 `IS NULL` / `IS NOT NULL` |
| 坏规则清理 | 删除规则要走 `op=remove` |

### `modifymeta_traps.md` 相关

| 陷阱 | 应用本场景 |
|---|---|
| `errorCode="0"` 不算成功 | modifyMeta 后必须 `getFormSchema` 二次验证 |
| `ops` 是数组 · 单条也要包数组 | 标准格式 |
| `treeType` 必填 | `entity` 操作字段 / 实体 |

### `kotlin_sync` / `python_service` 相关

| 陷阱 | 应用本场景 |
|---|---|
| OpenAPI 加字段成功但物理表没列 | PDM 同步是手动步骤 · OpenAPI 没暴露 |
| 加字段但访问报 "field not found" | PDM 同步未完成 |

---

## 六、性能问题（实际生产）

### 大批量 entry 单据卡顿

| 现象 | 原因 | 缓解 |
|---|---|---|
| 50+ entry 时 save 慢 | DELETE+INSERT 模式（OrgBatchBillSaveOp 实证） | 业务侧约定单单 entry ≤ 50 |
| 100+ entry 时 audit 卡 | OrgBillBatchEffectService.batchEffect 顺序处理 | 拆多张申请单 |
| BEC 订阅方延迟 | sch_task JOB_ID 全租户共用 | 队列拥堵时考虑加监控 |
| F7 上级组织选择慢 | 组织树深度 > 8 级 | 业务侧考虑组织分层 |

### `homs_batchorgentity` 表数据膨胀

注意：`homs_batchorgentity` 是反编译实证的物理实体名（`OrgBatchBillSaveOp.java:57`）· 物理表 `t_homs_orgchgbillentry`。每次 save 都 DELETE + INSERT · 累计可能产生大量孤儿数据。标品已在 `endOperationTransaction` 清理 `creator=0` 的脏数据（`OrgBatchBillSaveOp.java:55-62`）。

ISV 扩展时不要重复实现这个清理 · 标品已做。

---

## 七、调试 / 排查流程

### 步骤 1 · 看 billstatus 当前值

```sql
SELECT fid, fbillno, fbillstatus, fauditstatus
FROM t_homs_orgchgbill
WHERE fid = ?;
```

按 02_business_rules §一 状态机判断当前应该走什么操作。

### 步骤 2 · 看 entry 实际数据

```sql
SELECT *
FROM t_homs_orgchgbillentry
WHERE fid = ?;
```

按 changetype 字段分组看 7 entry 各自有几条。

### 步骤 3 · 看 sch_task 是否派发成功

```sql
SELECT *
FROM t_sch_task
WHERE fjobid = '5+X/4Y=AOZ=O'
ORDER BY fcreatetime DESC LIMIT 5;
```

如果生效后 BEC 订阅方没触发 · 看 sch_task 是否有 SCHEDULED 状态的任务。

### 步骤 4 · 看 haos_adminorg_msgdetail

```sql
SELECT * FROM t_haos_adminorg_msgdetail
WHERE ftraceid = ? OR fbo = ?
ORDER BY fcreatetime DESC LIMIT 10;
```

`sendstate = '0'` 表示待派发 · `'1'` 表示已派 · 排查 BEC 链路。

### 步骤 5 · 看反编译关键代码行

按异常 ResId 搜：

```bash
grep -rn "AdminorgBatchBillPlugin_64" knowledge/_sdk_audit/_decompiled/scenarios/homs_orgbatchchgbill/
```

定位标品代码位置 + 上下文看清楚状态校验。

---

**📌 来源追溯**：

- 41 ResId 实证：`AdminOrgBatchBillPlugin.java`（67 处 ResManager）+ `AdminOrgBatchBillListPlugin.java`（17 处 ResManager）
- 状态机异常：02_business_rules.md
- CS 报错对应：CS-01 ~ CS-07 的"踩坑"段
- BEC 排查：`AdminChangeMsgService.java` + `OrgBatchChgBillEffectOp.java:77`
- 性能：`OrgBatchBillSaveOp.java:46-62` DELETE+INSERT 模式实证
- 共性陷阱：`memory/kb_cosmic_buildmeta_traps.md` / `memory/kb_cosmic_addrule_traps.md` / `memory/kb_cosmic_modifymeta_traps.md`
