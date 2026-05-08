# 上下游联动 · 组织变动明细查询(homs_orgchgrecord)

> **状态**: 基于 `AdminOrgChgRecordListPlugin.java`(654 行) + 配对场景 `homs_orgbatchchgbill` 实证
> **confidence**: verified
> **审计时间**: 2026-04-27

---

## 1. 全图

```
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│ 上游(写入端 · 数据来源)                                                │
│ ┌─────────────────────────────────────┐                               │
│ │ homs_orgbatchchgbill (配对场景)      │                               │
│ │ · save / submit / audit / submiteffect ──┐                           │
│ │ · OrgBatchChgBillEffectOp 链路         │                              │
│ │ · 写 4 表 (haos_adminorg + 3 张 homs_orgchg*)                        │
│ └─────────────────────────────────────┘   │                            │
│                                            │ (生效驱动)                │
│                                            ▼                            │
│                                  ┌─────────────────────────┐            │
│                                  │ homs_orgchgrecord(本场景)│            │
│                                  │ 物理表:                   │            │
│                                  │ · t_homs_orgchgrecord    │            │
│                                  │ · t_homs_orgchgentry     │            │
│                                  │ · t_homs_orgchgdetail    │            │
│                                  └─────────────────────────┘            │
│                                            │                            │
│                                            │ (查询溯源)                 │
│                                            ▼                            │
│ 下游(读取端 · 数据消费)                                                │
│ ┌─────────────────────────────────────────┐                            │
│ │ 1. 用户审计查询(本场景列表)                │                            │
│ │ 2. ISV 自建审计报表(直查物理表)             │                            │
│ │ 3. ISV BEC 订阅方(从配对场景 audit 接事件) │                            │
│ │ 4. 外部 ERP/OA 系统同步(通过 BEC)          │                            │
│ └─────────────────────────────────────────┘                            │
│                                                                          │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 2. 上游(写入端 · 数据来源)

### 2.1 主上游 · `homs_orgbatchchgbill` 配对场景

**触发链路**:
```
[homs_orgbatchchgbill] save → submit → audit → submiteffect
   │
   ▼
OrgBatchChgBillEffectOp.beforeExecuteOperationTransaction
   │
   ▼
OrgBatchChgBillEffectOp.beginOperationTransaction
   │
   ├─ 1. 写 t_haos_adminorg(更新 / 新建组织 · 派生新版本)
   │
   ├─ 2. 写 t_homs_orgchgrecord(变动主记录)
   │
   ├─ 3. 写 t_homs_orgchgentry(每个 entry 一行)
   │       字段: fchgbillid, fchangetypeid, fchangesceneid,
   │            fchangereasonid, foperatorid, foperationtime,
   │            fchgeffecttime, fchangedescription
   │
   └─ 4. 写 t_homs_orgchgdetail(每个变动字段一行)
         字段: fdetailid (PR-005 · ID.genLongId), fentryid,
               fchgentitynumber, fchgpageelement,
               fbeforechgentity, fafterchgentity, fcoopreltypeid
```

**事务边界**: 4 张表写入是同一事务(原子操作) · 任一失败一起回滚。

**写入触发的 7 个 entry 类型**(配对场景 entryentity_*):
- `add` → 新增组织
- `parent` → 调整上级
- `info` → 信息变更
- `disable` → 停用组织
- `merge` → 合并(N:1)
- `split` → 拆分(1:N)
- `entryentity_all` → 全部组织汇总(只读 list 用 · 不直接写)

每种 entry 写入时 chgentitynumber 默认是 `haos_adminorgdetail`(普通变动) · 但合并/拆分场景会写 `haos_orgteamcooprel` 或 `haos_adminorgstruct`。

### 2.2 次上游 · HIES 导入(罕见)

**触发**: opKey `importdata_hr` · 通过标品 ImportPlugin 走 HIES 导入向导。

**用途**:
- 系统初始化时把外部历史变动数据导入(数据迁移)
- 一次最多 5000 条(HIES 标品限制)

**与配对场景的差异**: HIES 导入**不走** OrgBatchChgBillEffectOp · 直接写 t_homs_orgchgrecord 等 3 表。**优点**: 快。**缺点**: 跳过部分业务校验。

### 2.3 上游字段映射(从配对场景到本场景)

| 配对场景字段 | 本场景字段 | 转换逻辑 |
|---|---|---|
| `homs_orgbatchchgbill.id` | `homs_orgchgrecord.orgchgentry.chgbill` | 直接 ID 映射 |
| `homs_orgbatchchgbill.effdt` | `homs_orgchgentry.chgeffecttime` | 直接日期映射 |
| (audit 时间) | `homs_orgchgentry.operationtime` | NOW() 自动赋值 |
| (audit 操作人) | `homs_orgchgentry.operator` | 当前用户 |
| `entryentity_*.changescene` | `homs_orgchgentry.changescene` | 跨 entry 映射(每条 entry 写一行) |
| `entryentity_*.changetype` | `homs_orgchgentry.changetype` | 同上 |
| `entryentity_*.changereason` | `homs_orgchgentry.changereason` | 同上 |
| `entryentity_info` 字段差异 | `homs_orgchgdetail.chgentitynumber + chgpageelement + before/afterchgentity` | 字段级 diff(标品自动) |

---

## 3. 下游(读取端 · 数据消费)

### 3.1 本场景列表查询(主要消费方 · 95% 用例)

**消费方式**: 用户进入"组织变动明细查询"菜单 · 看列表 · 跨场景跳转看申请单详情。

**查询路径**: 详见 [05_data_flow.md §3](05_data_flow.md)。

### 3.2 跨场景跳转 → 配对场景详情

**触发**: 用户点列表行的 `chgbill` 列(或 ISV 扩展按钮)。

**实证**: `billListHyperLinkClick L200-L218`:
```java
showParameter.setFormId("homs_orgbatchchgbill");
showParameter.setPkId(dy.getLong("orgchgentry.chgbill"));
showParameter.setBillStatus(BillOperationStatus.VIEW);
```

**目标**: 让审计员从变动记录直接看到原始申请单内容(发文编号 / 发文名称 / 完整 entry / 审批历史等)。

### 3.3 跨场景跳转 → 行政组织详情(ISV CS-03)

**触发**: ISV 在 list 加扩展按钮"看组织当前版本"。

**实证**: CS-03 代码框架。

**关键 PR**: PR-008 + PR-009(用 boid + iscurrentversion=true 查当前版本)。

### 3.4 ISV BEC 订阅方(下游消费 · CS-06)

**注意**: 本场景**不发** BEC · 订阅方在配对场景挂(详见 CS-06)。

**消费方式**:
```java
@Override
public void handleEvent(KDBizEvent event) {
    if ("${ISV_FLAG}_org_chg_record_synced".equals(event.getEventNumber())) {
        // 收到事件 · 调外部 ERP / OA / 自定义系统
    }
}
```

### 3.5 ISV 自建审计报表(直查物理表)

**消费方式**: ISV 在自建动态表单 / 报表 form 直接 query t_homs_orgchgrecord 等 3 表 · 做合规审计 / 月度报表 / 变动统计。

**实例**: 案例 2 的"近 30 天关键变动审计报表"。

**关键认知**: 直查物理表是**只读** · ISV 不要 INSERT/UPDATE/DELETE。

### 3.6 平台数据归档(平台级)

**消费方式**: 平台提供"基础资料归档"功能 · 把 N 年前的变动记录迁到归档库 · 当前库释放空间。

**ISV 注意**: 不要自己写脚本 DELETE FROM 物理表(详见 08_impact_analysis.md §7.2)。

---

## 4. 跨域影响(HR 子域联动)

### 4.1 hrpi(人事档案域)

**联动点**: 当组织变动(如组织合并 / 拆分)时 · 该组织下的员工 hrpi_empjobrel 的 adminorg 字段需要级联更新。

**触发方**: 配对场景 `homs_orgbatchchgbill` 的 OrgBatchChgBillEffectOp(标品)在 `endOperationTransaction` 阶段调 `AdminChangeMsgService` 通知 hrpi 反写。

**与本场景关系**: 本场景**不参与** hrpi 反写 · 仅记录"变动事件" · 反写动作由配对场景驱动。

**ISV 注意**: 想监听"员工归属调整"事件 → 在 hrpi 域订阅 BEC · 不在本场景挂。

### 4.2 pay(薪酬域)

**联动点**: 组织变动可能影响薪酬归集(如组织合并 → 薪酬中心也要合并)。

**触发方**: 配对场景生效 → BEC 发"组织变更"事件 → 薪酬域订阅 → 调 pay 域接口反写。

**与本场景关系**: 同 4.1 · 本场景仅记录 · 不联动。

### 4.3 attendance(考勤域)

**联动点**: 组织停用 → 考勤规则需要调整。

**与本场景关系**: 同 4.1。

### 4.4 performance(绩效域)

**联动点**: 组织合并/拆分 → 绩效目标 / 评分权重需调整。

**与本场景关系**: 同 4.1。

### 4.5 cost-center(成本中心域)

**联动点**: 组织变动 → 成本中心引用更新。

**与本场景关系**: 同 4.1。

> ⭐ **核心认知**: 本场景**不发起** HR 子域联动 · 仅记录"变动已发生" · 联动由配对场景驱动 · 跨域订阅在配对场景的 BEC 链路。

---

## 5. 跨场景跳转矩阵(完整版)

| 跳转源 | 跳转目标 | 触发 | 实证 |
|---|---|---|---|
| 本场景 → `homs_orgbatchchgbill` | 申请单详情 VIEW | hyperLinkClick 默认行为 | L215 setFormId |
| 本场景 → `haos_adminorgdetail` | 组织当前版本详情 | ISV CS-03 自定义跳转 | CS-03 代码 |
| `homs_orgbatchchgbill` 详情页 → 本场景 | needshowdetail 模式列表 | ISV 加 toolbar 按钮 | L160-L163 接收 |
| `homs_orgbatchchgbill` 详情页 → 本场景 | needshowsingle 模式列表 | ISV 加 toolbar 按钮 + entry id | L164-L168 接收 |
| `admin_org_quick_maintenance` → 本场景 | 看该组织全部变动历史 | ISV 加扩展按钮 | (CS-03 反向) |
| `haos_adminorghis` → 本场景 | 历史版本对比时跳变动溯源 | ISV 扩展 | 同上 |

---

## 6. 数据生命周期

```
[T0]                       [T1]                      [T2]                     [T3]                      [T4]
申请单创建            提交审批                     生效                       归档                      永久删除
                                                                          (N 年后 · 平台级)
homs_orgbatchchgbill   homs_orgbatchchgbill        OrgBatchChgBillEffectOp  归档库迁移                ❌ 平台不允许
billstatus = A         billstatus = B               写 4 表                  当前库 → 归档库
                                                   billstatus = C
                                                   │
                                                   ▼
                                                   homs_orgchgrecord 出现新行
                                                   (本场景列表查得到)
```

> 数据从 T2 开始一直存在 · 直到平台归档 · 不可手动删除(违反审计设计)。

---

## 7. 失败补偿策略

### 7.1 配对场景生效失败

- **现象**: 用户点 audit 报错 · 事务回滚
- **影响本场景**: 本场景没新行(因为同事务回滚)
- **业务后果**: 用户看到错误信息 · 修改申请单后重试

### 7.2 写入部分成功(理论上不可能 · 同事务保证)

- **现象**: 假设标品 BUG 导致写入主表成功但 entry/detail 失败
- **修复**: 平台事务保证 · 如发生立即升级到红色事故 · 不要走 ISV 补偿

### 7.3 BEC 事件投递失败(下游)

- **现象**: ISV 订阅方收不到事件
- **修复**:
  - 平台 BEC 自动重试(默认 3 次)
  - 重试后仍失败 → 死信队列 → 人工介入

---

## 8. 关联文档

- [`02_business_rules.md`](02_business_rules.md) · §3 数据来源约束 / §6 跨模块约束
- [`03_model_design.md`](03_model_design.md) · §9 配对场景对偶清单
- [`05_data_flow.md`](05_data_flow.md) · §2 写入路径详解 / §4 跨表跳转路径
- [`06_customization_solutions.md`](06_customization_solutions.md) · CS-03 跨场景跳转 / CS-06 BEC 订阅
- [`08_impact_analysis.md`](08_impact_analysis.md) · §5 配对场景影响传导
- [`09_cases.md`](09_cases.md) · 案例 1 申请单生效后实时同步 / 案例 4 跨场景跳转
- [`knowledge/scenarios/homs_orgbatchchgbill_maintenance/11_upstream_downstream_logic.md`](../homs_orgbatchchgbill_maintenance/11_upstream_downstream_logic.md) · ⭐ 配对场景上下游(必对照)
- [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json) · PR-008 / PR-009 / PR-010 / PR-011

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
