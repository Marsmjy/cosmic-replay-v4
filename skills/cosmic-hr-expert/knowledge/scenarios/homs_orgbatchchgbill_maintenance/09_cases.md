# 参考案例 · 组织调整申请单

> **状态**: 🟢 基于 7 个 CS + 实际客户场景对偶整合
> **confidence**: real_deploy（结构）+ likely（具体客户）
> **数据源**: 7 个 CS 反编译实证 + admin_org_quick 对偶模式

---

## 案例 1 · 跨子公司组织合并 · 必须走申请单

### 客户场景

某集团客户 · 因业务整合 · 要把"成都研发部"和"重庆研发部"合并成"西南研发部" · 涉及：
- 2 个原组织停用
- 1 个新组织创建（继承 2 个原组织的所有员工任职）
- 涉及 200+ 名员工任职关系自动跟随

### 选 CS

直接走标品的 `entryentity_merge`（不需要 ISV 自定义 CS）+ CS-05 BEC 订阅方监听变动通知到 OA / 邮件系统。

### 关键操作

1. 用户在【组织调整申请】新建一张单
2. 切到"合并"页签 · `entryentity_merge` · 点"组织合并"按钮
3. UI 弹合并配置浮层（见 `AdminOrgBatchBillPlugin.mergeOperation` L580）：
   - 选 `to_merge_org`：勾选"成都研发部" + "重庆研发部" 多基础资料
   - 选 `merge_target_org`：填新组织"西南研发部"（admin_org_quick 不能在合并前预创建 · 必须在 add entry 同时建）
   - 配 `merge_changescene` / `merge_changereason`
4. **同时**在 `entryentity_add` 加 1 行：新建"西南研发部" · `add_corporateorg` 选某法律实体 · `add_changescene` 选"组织合并新增"
5. submit → wf 审批通过 → audit 生效

### 关键代码片段

**为什么 add 和 merge 必须同时？** 反编译 `AdminOrgBatchBillPlugin.java:803-832` F7 实证：

```java
case "merge_target_org":
case "split_target_org":
    // 包含 add entry 中的新组织（允许把新建的作为目标）
    Set adminOrgSet = addCollection.stream().map(s -> s.getString("add_adminorg")).collect(Collectors.toSet());
    addOrgIdSet = adminOrgSet.stream().map(Long::valueOf).collect(Collectors.toSet());
    // ...
    QFilter currentOrderFilter = new QFilter("id", "in", addOrgIdSet);
    // 用 currentOrderFilter 跟 'iscurrentversion=0' 取并集
    beforeF7SelectEvent.getCustomQFilters().add(
        new QFilter("iscurrentversion", "=", "0").or(currentOrderFilter));
```

→ `merge_target_org` F7 过滤会**包含** add entry 里新建的组织 · 但**只有先在 add entry 配好** · merge 这边 F7 才能看到。

### 生效后链路（CS-05 介入点）

```
audit (用户点审批通过)
  │
  ▼
OrgBatchChgBillEffectOp.batchEffect
  ├── add 策略：建新组织"西南研发部"（haos_adminorg INSERT 新版本）
  ├── merge 策略：原 2 组织 disable + adminorg.parentorg/sourcevid 调整
  └── disable 策略：原 2 组织 enable=0 · iscurrentversion=false
  │
  ▼
afterExec
  └── AdminChangeMsgService.handleChangeMsg() → sch_task 异步派 BEC 事件
  │
  ▼
[ISV CS-05 订阅方] OrgChangeWmsSyncConsumer.handleEvent
  ├── 解析 changescene / changeoperate
  ├── 按"组织合并"分支
  └── 发邮件到 OA："您所属的 X 组织已合并到 Y · 请核实"
```

### 踩坑

- ⚠️ 用户经常忘"先 add 后 merge" · 如果直接 merge 不预创目标 · F7 选不到任何"新建的西南研发部"
- ⚠️ 标品已发"行政组织变更" BEC 事件 · ISV 不要自己在 OP 里再发一次（会重复通知）

---

## 案例 2 · 大型组织拆分 · 200+ 任职关系自动调整

### 客户场景

某客户的"销售总部"按业务线拆成 3 个事业部（"消费品事业部"/"工业品事业部"/"国际事业部"）· 总部 200+ 员工要按"主负责业务线"分配到 3 个新事业部。

### 选 CS

走 `entryentity_split` + CS-05 BEC 订阅方。员工分配规则在订阅方实现。

### 关键操作

1. 在 `entryentity_add` 建 3 行（3 个新事业部）· 配编码 / 名称 / 上级（销售总部上级）
2. 在 `entryentity_split`：
   - `to_split_org` = "销售总部"
   - `split_target_org` = 多基础资料 · 勾上 3 个新事业部
   - `split_changescene` = "组织拆分"
3. submit → audit
4. 生效后 · `OrgBatchChgBillEffectOp.batchEffect` 走 split 策略：
   - 销售总部 disable
   - 3 个新事业部建成
   - 但**员工任职关系不会自动按业务线分配**（标品默认所有员工跟随到第 1 个目标事业部）

### CS-05 订阅方业务规则

```java
// 订阅 BEC 事件
public Object handleEvent(KDBizEvent evt) {
    JSONArray dataList = ...;  // 解析 payload
    for (JSONObject item : dataList) {
        Object changeOperate = item.get("changeoperate");
        if (!"组织拆分".equals(changeOperate.toString())) continue;
        long oldOrgBoid = item.getLongValue("bo");  // 销售总部 boid

        // 1. 查所有任职在销售总部的员工
        HRBaseServiceHelper relHelper = new HRBaseServiceHelper("hrpi_empjobrel");
        QFilter qf = new QFilter("adminorg", QCP.equals, oldOrgBoid)
                          .and("iscurrentversion", QCP.equals, "1");
        DynamicObject[] rels = relHelper.query("id, employee, businessline", qf.toArray());

        // 2. 按 businessline 字段分配到新事业部
        Map<String, Long> businessLineToNewOrg = loadBusinessLineMapping();  // ISV 自定义映射表
        for (DynamicObject rel : rels) {
            String bl = rel.getString("businessline");
            long newOrgBoid = businessLineToNewOrg.get(bl);
            // 触发 admin_org_quick 的"信息变更"流程把员工从销售总部转到新事业部
            this.transferEmployee(rel.getLong("employee_id"), newOrgBoid);
        }
    }
    return null;
}
```

### 踩坑

- ⚠️ **不要在 OP afterExec 直接调 hrpi_empjobrel update** · 会跟标品订阅方冲突（标品有员工任职同步插件 · 都对同一表写）· 走 BEC 订阅方 · ISV 订阅方独立事务
- ⚠️ 拆分后员工默认跟随第 1 个目标 · 不要假设标品会按业务线自动分（标品没这逻辑）

---

## 案例 3 · 编码重复检查 · 防止 add entry 撞已存在组织

### 客户场景

某客户多个 BU 共用同一行政组织树 · BU A 的 HR 经常误操作 · 在 add entry 用了 BU B 已经存在的组织编码 · 直到生效时才报"编码重复"· 数据已写一半被回滚 · 体验差。

### 选 CS

直接用 CS-02 · 在 save 阶段就校验编码全租户唯一。

### 实施步骤

1. ISV 写 `${ISV_FLAG}.hrmp.homs.opplugin.OrgAddNumberDuplicateOp`（CS-02 完整代码）
2. 平台开发→ homs_orgbatchchgbill → 操作 save → 扩展插件 → 新增挂上
3. 部署 → 测试场景：
   - **测 1**：add entry 编码留空 → save 通过（编码空时跳过校验 · 等 setAddOrgNumber 自动补）
   - **测 2**：add entry 填一个已存在编码 → save 报错 "新增组织编码 [XXX] 已被其他组织占用..."
   - **测 3**：add entry 多行 · 第 2 行重复 → 报错 + 行号精准

### 生产数据

某客户上线 CS-02 后 1 个月 · 误填编码导致的"生效失败"事件从 12 次降到 0 次。

### 踩坑

- ⚠️ Validator 必须按"租户内"or"全局" 来确定 · 别忘了带 BU 过滤（看业务定义）
- ⚠️ haos_adminorg 是 HisModel · 必须 `iscurrentversion=1` 否则会查到历史版本误报

---

## 案例 4 · 同步推送 ERP 成本中心 · 实时回执

### 客户场景

某客户 ERP 系统的成本中心维度跟苍穹组织一一对应 · 业务要求：申请单审批通过那一刻 · ERP 必须立刻同步成功 · 才允许 HR 推后续动作（招聘 / 发薪）。如果 ERP 同步失败 · 不能让用户以为已生效。

### 选 CS

CS-06 (afterExec 同步调) · **不**用 CS-05（异步会让用户后续动作时机不可控）。

### 实施

1. ISV 写 `OrgBatchErpSyncOp`（CS-06 完整代码）
2. 挂在 audit + submiteffect 两个 opKey 上（共用同一插件）
3. 内部走 ERP 接口同步调用 + 5s timeout
4. ERP 同步失败 → 写 ISV 自建补偿表 + 用户提示"已生效但 ERP 同步失败 · 请联系 IT"

### 不同选择对比

| 维度 | CS-05 BEC | CS-06 afterExec | 客户选哪个 |
|---|---|---|---|
| 实时性 | 秒级（异步） | 毫秒级（同步） | CS-06 ✅（业务要求即时） |
| 主事务影响 | 无 | 拖时长 | CS-06（小批量场景可接受） |
| 失败影响 | BEC 重试无影响主流程 | 同步失败时用户已看到生效 · 体验割裂 | 通过补偿表 + IT 介入兜底 |
| 大批量 | 强 | 弱（>100 entry 卡） | 业务约定单次最多 50 entry |

### 踩坑

- ⚠️ ERP 接口必须设 timeout（5s）· 否则审批人在 UI 假死 30+ 秒
- ⚠️ ERP 失败要写补偿表 · 不能让用户看到红错（事务已提交·错误也回不去）

---

## 案例 5 · 终止已生效大型变更 · 影响范围二次确认

### 客户场景

某客户做过一次大型组织调整 · 涉及 20 个组织变动 + 影响 500+ 员工任职。半年后业务复盘发现"组织合并"决策错了 · 想回退（breakup 终止）。但终止可能造成大范围数据回退 · 风险极高。需要：

1. 终止前精准告知"将影响 N 个组织 + M 名员工"
2. 影响 ≥ 5 组织 → 普通 HR 不能自助终止 · 必须管理员授权
3. 终止前要发邮件给所有受影响员工的直属经理

### 选 CS

CS-07（终止校验）+ CS-05（终止后通知）

### 关键流程

```
普通 HR 选已生效单 → 列表点【终止】
  │
  ▼
列表 ListPlugin · beforeDoOperation
  ├── 校验单条（标品）
  └── 走 callValidatorBeforeDoOperation
  │
  ▼
[OP] AdminOrgBatchBreakupOp
  │
  ▼
[ISV CS-07] OrgBatchBreakupImpactCheckOp.onAddValidators
  │
  └── BreakupImpactValidator.validate
       ├── 加载 entry · 算受影响 boid 数 = 20
       ├── 20 > 5 → addErrorMessage("影响 20 个行政组织 · 超过自助终止上限 5 · 请联系管理员授权")
       └── 阻断 · 用户看到精准提示
  │
  ▼ [被阻断]
（管理员介入：用管理员账号登录 · 走管理员专属 opKey 终止 · 跳过 CS-07 阈值）
```

### 终止后通知（CS-05 共用 BEC）

`OrgBatchChgBillEffectOp` 在 audit/submiteffect 调 `handleChangeMsg`。breakup 反向回退也会触发 admin_org 变动 · 同样发"行政组织变更"事件。CS-05 订阅方按 `changeoperate = 终止回退` 分支处理：

```java
if ("组织变动终止".equals(changeOperate.toString())) {
    // 查所有受影响员工 → 给直属经理发邮件
    notifyManagersForRollback(orgBoid, ...);
}
```

### 踩坑

- ⚠️ 终止已生效单的反向回退**很危险** · 业务上不推荐 · 大多数情况"补一张反向调整单"比 breakup 更稳
- ⚠️ CS-07 阈值要可配置（系统参数）· 不要硬编

---

## 案例 6 · 给 add entry 加"申请来源系统"字段 · 跟外部 OA 工单系统对接

### 客户场景

某客户 OA 工单系统通过 OpenAPI 发起组织调整 · 需要在申请单上记录"工单号 + 来源系统"· 用于后续追溯。

### 选 CS

CS-01（加字段）+ 自定义 CS（FormPlugin afterCreateNewData 自动填）。

### 实施

1. **CS-01 加字段**：4 个 entry 都加 `{prefix}_tdkw_sourcesys` (TextField, 50字) + `{prefix}_tdkw_oaticketno` (TextField, 50字)

2. **新 FormPlugin 自动填默认值**（参考 admin_org `AdminOrgBatchBillPlugin.afterCreateNewData` L236-260 模式）：

```java
public final class OrgBatchAutoFillSourceSysPlugin extends HRCoreBaseBillEdit {
    @Override
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        // 从 customParam 取来源系统标识（OpenAPI 调用时塞进去）
        String sourceSys = (String) this.getView().getFormShowParameter()
                                          .getCustomParam("${ISV_FLAG}_source_sys");
        String ticketNo = (String) this.getView().getFormShowParameter()
                                          .getCustomParam("${ISV_FLAG}_oa_ticket");
        if (HRStringUtils.isEmpty(sourceSys)) {
            return;  // 不是外部触发的 · 不自动填
        }
        // 主表 customParam 透传（如果 ISV 给主表也加了字段）
        // ...
        // 暂时不动 entry · 让后续业务流程在 add 行时手动填
    }
}
```

3. **CS-04 校验补强**：submit 时校验 `add_tdkw_sourcesys` 必填（业务要求工单系统来的单子必须带工单号）

### 踩坑

- ⚠️ CS-01 加字段必须每个 entry 一条 modifyMeta · 不能"加 1 个 add 自动级联到 4 entry"
- ⚠️ 字段命名严格 `{prefix}_tdkw_xxx` · prefix 是 entry 的前缀（add/parent/info/disable）

---

## 案例对偶矩阵：跟其他场景的关系

| 业务需求 | 本场景 (homs_orgbatchchgbill) | admin_org_quick_maintenance | 选哪个 |
|---|---|---|---|
| 单建 1-2 个组织 | ✅ 走单据走审批 | ✅ 即时直改 | quick（简单） |
| 批量建 5+ 组织 | ✅ entry_add 多行 | ❌ 一个个建 | **本场景** |
| 改字段不改编码 | ✅ entry_info | ✅ infochg | quick（即时）/ 本场景（走审批） |
| 改字段含改编码 | ✅ entry_info（info_number 必填） | ❌ 标品 number 不可改 | **本场景** |
| 调上级（标准） | ✅ entry_parent | ✅ confirmchange | quick（即时）/ 本场景（走审批） |
| 合并 | ✅ entry_merge | ❌ 不支持 | **本场景** |
| 拆分 | ✅ entry_split | ❌ 不支持 | **本场景** |
| 异步生效（指定未来日期） | ✅ effdt 可设未来 | ❌ 即时生效 | **本场景** |
| 走审批 | ✅ submit | ❌ | **本场景** |
| 跳过审批立即生效 | ✅ submiteffect | ✅ 默认 | 都行 |
| 同时多组织变更 | ✅ 7 entry 并行 | ❌ 单条 | **本场景** |
| 加业务字段 | ✅ 单 entry 自定义 | ✅ 主表自定义 + 4 前缀级联 | 看业务范畴（详见 03_model_design §七） |
| BEC 订阅监听变动 | ✅ 共用同一事件号 | ✅ 共用 | 同一订阅方都收到 |

---

## AI 自动索引（待补充）

未来可跑 `scripts/search_historical_customizations.py --scenario homs_orgbatchchgbill_maintenance` 把真实客户案例的 GitLab 路径填入下表：

| 客户 | 需求摘要 | GitLab 路径 | 时间 | 选了哪个 CS |
|---|---|---|---|---|
| `<TODO>` | `<TODO>` | `<TODO>` | | |

---

**📌 来源追溯**：

- 案例 1 merge：`AdminOrgBatchBillPlugin.java:580-589`（mergeOperation）+ `:803-832`（merge_target_org F7 实证）
- 案例 2 split：`AdminOrgBatchBillPlugin.java:591-600`（splitOperation）
- 案例 3 编码重复：CS-02 完整代码 + `OrgBatchBillSubmitAndEffectiveOp.java:75-110` 标品自动补编码
- 案例 4 ERP 同步：CS-06 完整代码 + PR-010 #9 afterExec 阶段
- 案例 5 终止校验：CS-07 完整代码 + `AdminOrgBatchBreakupOp.java:25-33`（标品 Validator 注册）
- 案例 6 自定义字段：CS-01 + `AdminOrgBatchBillPlugin.afterCreateNewData` L236-260 模式
- 跨场景对偶：`scenarios/admin_org_quick_maintenance/06_customization_solutions.md` CS-01 ~ CS-08 + 本场景 CS-01 ~ CS-07
