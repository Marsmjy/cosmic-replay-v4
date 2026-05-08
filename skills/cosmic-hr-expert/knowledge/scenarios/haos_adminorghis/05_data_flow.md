# 数据流转 · 组织历史查询（haos_adminorghis）

> **状态**：基于 7 反编译类（HisModelF7ListPlugin / HisModelListCommonPlugin / HisModelFormCommonPlugin 等）QFilter 链路实证
> **confidence**：verified
> **审计时间**：2026-04-27

---

## 1. 数据流总图

```
┌─────────────────────────────────────────────────────────────┐
│            读路径（90% 用例 · 查询方向）                       │
└─────────────────────────────────────────────────────────────┘

UI 列表请求
  │
  ▼
[setFilter] HisModelListCommonPlugin.setFilter L167-L181
  │
  ├─ 标品强制注入 QFilter("iscurrentversion", "=", FALSE)
  ├─ 移除 ctrlstrategy 过滤
  └─ ISV 可追加 (但不能覆盖 iscurrentversion=false)
  │
  ▼
ORM 查询 t_haos_adminorg + t_haos_adminorg_l
  │
  ▼
[packageData] HisModelListCommonPlugin.packageData L142-L165
  │
  ├─ 按 datastatus 决定 hisoperation 列按钮可见性
  └─ 渲染列表
  │
  ▼
list view 展示历史版本

═════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────┐
│            写路径（罕见 · his_save 历史补录）                  │
└─────────────────────────────────────────────────────────────┘

UI 触发 his_save
  │
  ▼
AdminOrgInitSaveOp（唯一执行链插件）
  │
  ├─ onAddValidators → 注册 10 个 OrgHis*Validator
  │   ↓ 校验失败 → addErrorMessage → 整事务终止
  │
  ├─ beforeExecuteOperationTransaction → 预处理
  │
  ├─ beginOperationTransaction
  │   ↓ INSERT 或 UPDATE t_haos_adminorg（视情况：补新版本 or 修历史）
  │   ↓ 同步 t_haos_adminorg_l 多语言行
  │
  └─ endOperationTransaction → 读 bsed 字段 · 收尾
  │
  ▼
事务 commit · 数据落库（不发 BEC · 反编译实证 0 处）
```

---

## 2. 标品 final 方法 setFilter 的核心逻辑（最关键的实证）

`HisModelListCommonPlugin.setFilter L167-L181`：

```java
public void setFilter(SetFilterEvent event) {
    super.setFilter(event);  // ← 父类 HRDataBaseList 跑数据权限 + BU 闸
    LOGGER.info("HisModelListCommonPlugin setFilter start, ...");

    if (this.listProcessor.isF7()) {
        return;  // F7 模式由 HisModelF7ListPlugin 接管
    }

    if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
        // 当前版本视图（admin_org_quick_maintenance 走这里）
        QFilter currentDataFilter = new QFilter("iscurrentversion", "=", Boolean.TRUE);
        event.getQFilters().add(currentDataFilter);
    } else {
        // 历史版本视图（**本场景走这里 · VERSION_LIST_PAGE**）
        QFilter currentDataFilter = new QFilter("iscurrentversion", "=", Boolean.FALSE);
        event.getQFilters().add(currentDataFilter);
        event.getQFilters().removeIf(filter ->
            filter != null && HRStringUtils.equals(filter.getProperty(), "ctrlstrategy"));
    }
}
```

**ISV 关键认知**：
- 这是一个 `final` 类的方法 · 不能继承重写
- 但 ISV 自己挂的 ListPlugin 也可以 add QFilter · 跟标品并列累加（QFilter add 是 AND 关系）
- **不能去掉** 标品 add 的 `iscurrentversion=false` · 因为它在标品 final 方法里强制 add · ISV 即便后跑也只能再 add 不能 remove
- **可以追加额外条件**：如 `(iscurrentversion=false AND boid IN <ISV 收窄集>)`

---

## 3. F7 历史版本选择的多层 QFilter 构造

`HisModelF7ListPlugin.setFilter L87-L119` 实证 5 层 QFilter 叠加：

| 层 | QFilter 来源 | 业务作用 |
|---|---|---|
| 1 | super.setFilter (HRDataBaseList) | 默认数据权限 + BU 闸 |
| 2 | `buF7Processor.changeHisListBdQFilter` | 标准 BU 闸（非 BoF7 时） |
| 3 | `showCurrentNumAndName=true` 时的子查询 OR | 让历史版本能按"当前编号/名称"被搜到 · 临时拿当前版本 boid · OR 拼回原过滤 |
| 4 | `iscurrentversion` 区分 | ONLY_ONE_EFFECT_VERSION → true · BoF7 → true · 否则 handleSetVersionF7QFilters（按 effectdate 区间筛） |
| 5 | enable + status 收窄 | `handleEnableAndStatusQFilters` |

**5 层叠加的 SQL 等价**（伪代码）：
```sql
SELECT * FROM t_haos_adminorg
WHERE
  -- 层 1：数据权限
  fid IN (<用户授权范围>)
  -- 层 2：BU 闸
  AND fcreatorbu_id IN (<BU 列表>)
  -- 层 3：当前编号 OR 拼接
  AND (
    fnumber LIKE '%xxx%' OR fname LIKE '%xxx%'  -- 原过滤
    OR fboid IN (
      -- 子查询：找匹配编号的当前版本 boid
      SELECT fid FROM t_haos_adminorg
      WHERE (fnumber LIKE '%xxx%' OR fname LIKE '%xxx%')
        AND fiscurrentversion = true
    )
  )
  -- 层 4：版本过滤
  AND (
    fiscurrentversion = true  -- ONLY_ONE_EFFECT_VERSION 或 BoF7 模式
    -- 或：
    AND (fbsed <= '<effdate>' AND (fbsled IS NULL OR fbsled >= '<effdate>'))  -- 一般模式
  )
  -- 层 5：状态收窄
  AND fenable IN ('1', '0')
  AND fstatus IN ('A', 'B', 'C')
ORDER BY fhisversion DESC
```

---

## 4. propertyChanged 触发列表 refresh 的事件链

`HisModelF7ListPlugin.propertyChanged L147-L187`：5 个字段任一变更触发列表刷新：

```
effectdate / effdatestart / effdateend / f7effdatestart / f7effdateend
   │
   ▼
this.f7Processor.getChangeDateMap().put(<key>, <newValue>)
   │
   ▼
listView.refresh() + listView.clearSelection()
   │
   ▼
框架触发新一轮 setFilter → ORM 重新查 t_haos_adminorg → 列表重渲染
```

**关键**：每个字段变更都立即重查 · 不批量等待用户多选完再查 · 这是**用户体验设计** · ISV 想做"批量改完才查"必须重写这块（CS-02 中可以做）。

---

## 5. 历史版本对比（versionchangecompare）的数据流

`HisModelListCommonPlugin.afterDoOperation L232-L238`：

```
用户多选 ≥2 行 + 点对比
   │
   ▼
HisModelListCommonPlugin.afterDoOperation case "versionchangecompare":
   │
   ├─ getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue) ← 拿 id 集合
   │  注：这里拿的是 id（版本维度）· 不是 boid · 因为对比是版本快照
   │
   └─ HisModelListShowFormProcessor.showVersionChangeCompareList(selectedRowIds)
      │
      ▼
      跳到对比页（标品 list 页 · 通常是 hbp_versioncomparelist 或类似）
      内部按 id 集合查 t_haos_adminorg · 渲染字段差异表
```

---

## 6. 跨 form 共用同表的事务边界

本场景写入路径（his_save · chargepersonimpo_hr）跟 admin_org_quick_maintenance 的写入路径（save · confirmchange · disable · enable）**操作同一物理表 t_haos_adminorg** · 但**事务独立**。

| 写入路径 | OP 类 | 事务边界 | 行级锁影响 |
|---|---|---|---|
| admin_org_quick_maintenance.save | AdminOrgFastSaveOp | 单 OP 事务 | 锁 boid 当前版本行（iscurrentversion=true）|
| admin_org_quick_maintenance.confirmchange | AdminOrgFastParentChangeOp / AdminOrgFastInfoChangeOp | 单 OP 事务 + 异步发 BEC | 锁 boid 当前版本行 + 派生新版本行（iscurrentversion=true 切换）|
| haos_adminorghis.his_save | AdminOrgInitSaveOp | 单 OP 事务 · **不发 BEC** | 锁 boid 历史版本行 · 不动当前版本 |
| haos_adminorghis.chargepersonimpo_hr | ImportPlugin → batch his_save | 多事务 + 失败重试 | 按 boid 串行锁历史版本 |

> **并发场景**：用户 A 在 admin_org_quick_maintenance 里 confirmchange 同时用户 B 在本场景 his_save 同一 boid · 两边事务独立 · 数据库行级锁会决定谁先 commit。**ISV 写校验时不要假设双场景互斥** · 用乐观锁 / 数据库唯一约束兜底。

---

## 7. AdminOrgInitSaveOp 的 readFields=[bsed] 含义

`opkeys/his_save.json B_AdminOrgInitSaveOp_4 endOperationTransaction sourceLine=L76-L84 readFields=[bsed]`：

**业务意图**：在 endOperationTransaction 阶段读 bsed 字段 · 大概率是给"已生效后兜底"用：
- 写入完成后 · bsed 已敲定 · 用于决定后续平台标准链路（比如要不要把 datastatus 从 TEMP 切到 EFFECTING）
- 也可能是用来更新 firstbsed 一致性兜底

**ISV 关心**：扩展 his_save 不要再读改 bsed · 平台已经处理。

---

## 8. attachmentPanelLogInfo 跨 lifecycle 传递机制

反编译实证：`HisModelFormCommonPlugin.afterBindData L131-L134` + `beforeDoOperation L143`：

```
afterBindData：
  if model.getDataEntity().getDataEntityState().getFromDatabase()
  → list = LogHandlerUtil.getAttachmentLogInfo(...)
  → pageCache.put("attachmentPanelLogInfo", json)

beforeDoOperation case "save"：
  op.getOption().setVariableValue("attachmentPanelLogInfo",
      pageCache.get("attachmentPanelLogInfo"))
  → 传到 OP 链 OperateOption · OP 端用 LogHandler 落地附件审计
```

**FormPlugin → OP 跨层传值的标准模式**（PR-003 + PR-010 涉及）：用 `OperateOption.setVariableValue` 不是直接 set 实体字段。

---

## 9. revise 派生新版本的物理写入

revise 触发 `HisModelOPCommonPlugin`（28 号插件 · 标品 OP 父类） + `BdVersionSaveServicePlugin`（31 号插件 · 平台版本服务）的执行链。物理操作：

```
revise 操作 OP 链：
  ├─ HisModelOPCommonPlugin.beforeExecuteOperationTransaction
  │   ↓ 校验当前版本能否派生（如未审核版本不行）
  │
  ├─ BdVersionSaveServicePlugin.beginOperationTransaction
  │   ↓ INSERT 新行 to t_haos_adminorg
  │     - boid = 老版本.boid
  │     - hisversion = 老版本.hisversion + 1
  │     - sourcevid = 老版本.id
  │     - bsed = 用户填的新生效日期
  │     - bsled = TimeLineServiceUtil.getMaxEffEndDate()
  │     - iscurrentversion = false（暂为待生效）
  │     - datastatus = TEMP
  │   ↓ UPDATE 老版本.bsled = 新版本.bsed - 1
  │   ↓ INSERT t_haos_adminorg_l 多语言行
  │
  └─ HisModelOPCommonPlugin.afterExecuteOperationTransaction
      ↓ （不发 BEC · 实证）
```

> 注：confirmchange 走的链不一样（在 admin_org_quick_maintenance 域 · 走 AdminOrgFastParentChangeOp / AdminOrgFastInfoChangeOp · 那条链发 BEC）。

---

## 10. 列表 packageData 行级按钮可见性数据流

反编译实证：`HisModelListCommonPlugin.packageData L142-L165`：

```
event.getColKey() == "hisoperation"
   │
   ▼
event.getRowData().getString("datastatus")
   │
   ├─ "TEMP" (HisModelDataStatusEnum.TEMP)
   │   └─ operationColItems.iterator()
   │       opItem.setVisible(
   │           "modify".equals(opKey) ||
   │           "hiscopy".equals(opKey) ||
   │           "confirmchange".equals(opKey)
   │       )
   │
   └─ 其他 (DISCARDED / EFFECTING / EXPIRED / ...)
       └─ operationColItems.iterator()
           opItem.setVisible(
               "hiscopy".equals(opKey) ||
               "revise".equals(opKey) && !DISCARDED.equals(dataStatus)
           )
```

**关键认知**：行级按钮可见性是**每行独立计算**的 · datastatus 是行数据字段 · 不是页面 customParam。ISV 想加自己的行级按钮（如"导出此版本"），要在 packageData 里追加判断逻辑（CS-05 行级样式扩展）。

---

## 11. 物理表写入时序（his_save 串行）

his_save 是单条事务 · 串行执行。如果一次提交多条历史补录数据 · OP 接收 `args.getDataEntities()` 数组 · OP 方法体内 for 循环每个 entity 走一次完整 lifecycle（onAddValidators 注册的 Validator 在每个 entity 上都跑一遍）。

**ISV 边界**：批量 his_save 操作不要在 OP 方法体内手动开新事务 · 让平台的 OperationTransaction 兜底。

---

## 12. 数据查询性能特征

| 查询模式 | 等价 SQL | 索引利用 |
|---|---|---|
| 列表默认（VERSION_LIST_PAGE）| `SELECT ... FROM t_haos_adminorg WHERE iscurrentversion=false ORDER BY fhisversion DESC LIMIT 50` | 走 `(fiscurrentversion, fhisversion)` 联合索引（标品建的）|
| 按 boid 查所有版本 | `WHERE fboid = ? ORDER BY fhisversion DESC` | 走 `fboid` 索引 |
| 按 effdate 范围筛 | `WHERE fboid = ? AND fbsed <= ? AND fbsled >= ?` | 走 `fboid + fbsed` 索引 |
| F7 显示当前编号搜索 | 见 §3 层 3 的子查询 OR · 涉及两次 t_haos_adminorg 查询 | 性能略差 · 但 boid 索引兜底 |

**ISV 关心**：自加扩展过滤字段（如 ISV 字段 `${ISV_FLAG}_region`）需要 DBA 配合建索引 · 否则全表扫超慢。

---

## 13. 关联文档

- `04_business_flow.md` · 完整调用链
- `02_business_rules.md` · §1.1 7 大时序字段
- `06_customization_solutions.md` · CS-01 加自定义筛选字段（QFilter 扩展）
- `knowledge/scenarios/admin_org_quick_maintenance/05_data_flow.md` · 当前版本写入路径
