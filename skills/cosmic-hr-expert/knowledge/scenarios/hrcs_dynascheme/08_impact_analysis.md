# 变更影响面 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + scene_doc.json 56 字段 + 11_upstream_downstream_logic.md
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、字段变更影响面（按字段分级）

### 1.1 主表字段（28 个 · 改前必看）

| 字段 | 改了影响 | 安全等级 |
|---|---|---|
| `boid` | 🔴 时序模型崩 · 跨版本追溯失效 · 下游 hrcs_userrolerelat 全乱 | 红区（不改） |
| `id` | 🔴 主键 · 改了所有 entity 引用全断 | 红区（不改） |
| `iscurrentversion` | 🔴 时序当前版本判定崩 · 一个 boid 可能多行 true → 严重数据错乱 | 红区（不改） |
| `sourcevid` | 🟠 链式追溯断 · changeCompare 失效 | 红区（不改） |
| `condition` | 🟠 规则条件 · 改了下游员工权限突变 · 必须走 confirmchange | 黄区（受控） |
| `authaction` | 🟠 授权动作 · 改了规则被清空（DynaAuthSchemePlugin.propertyChanged 二次确认） | 黄区（受控） |
| `admingroup` | 🟠 归属管理员组 · 改了下游 hrcs_dynaschemerange 等 5 表的 admingroup 范围崩 | 黄区（受控 · 标品 issvCanModify=false） |
| `enable` | 🟡 使用状态 · 改了下游用户角色绑定立即失效（看 hrcs_userrolerelat 的查询时是否带 status=1） | 黄区 |
| `status` | 🟡 数据状态 · 跟工作流 op 关联 | 黄区 |
| `name` / `simplename` / `description` | 🟢 显示名 · 影响 UI 显示 / BI 报表 | 绿区 |
| `assigndays` / `assigndesc` / `canceldesc` | 🟢 业务文案 · 影响最终展示 | 绿区 |
| `index` | 🟢 排序号 · 影响列表排序 | 绿区 |
| `ruledescription` | 🔴 派生 · 不要手改（每次 save 会被覆盖） | 红区（autoComputed） |
| `search_*` 6 字段 | 🔴 派生 · 不要手改（resolveRuleConfigToSearch 灌库） | 红区（autoComputed） |
| `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `disabler` / `disabledate` / `issyspreset` / `initdatasource` / `orinumber` / `oristatus` / `oriname` / `firstbsed` / `bsed` / `bsled` / `hisversion` / `datastatus` / `changedescription` | 🔴 系统派生 / HisModel 内部 | 红区（autoComputed） |

### 1.2 分录字段

| 分录 → 字段 | 改了影响 |
|---|---|
| `assignactionentry` 整行删 | 失去对应变动类型的分配规则 · 该类型变动不再触发分配 |
| `cancelactionentry` 整行删 | 同上 · 取消端 |
| `roleentry` 整行删 | 失去对应角色 · 已分配该角色的员工失权（有定时任务来清） |
| `roleentry.customenable` 改 | 重新评估角色成员范围 · 影响一批员工 |
| `roleentry.role` 改 | 改了绑定的角色 · **role.isvCanModify=false** · 标品锁死 |

---

## 二、操作影响面（按 opKey 分组）

### 2.1 高影响 op（红区）

| opKey | 动作 | 影响范围 | 回滚方式 |
|---|---|---|---|
| `delete` | 删除方案 + 级联清 5 张配置表 | 永久丢方案数据 + hrcs_userrolerelat 残留绑定（需手清） | ❌ 不可回滚（彻底删） |
| `confirmchange` | 业务变更 · 双写 boid + bgVid · resolveSearch 重新灌库 | 影响所有命中规则的员工权限 | 🟡 可创建新版本回滚（再 confirmchange 一次走老规则） |
| `audit` | 审核 · entryboid 重映射 · saveRoleEntry 写 sourceVid 维度 | 立即生效 + 触发权限重算 | 🟡 unaudit 后改 |

### 2.2 中影响 op（黄区）

| opKey | 动作 | 影响范围 |
|---|---|---|
| `disable` | 禁用方案 | 已分配绑定不变 · 但下次重算不再分配新人 |
| `enable` | 启用方案 | 立即纳入下次重算 |
| `submit` | 提交（A → B） | 等待审核 · 不影响生产 |
| `setadminrange` | 设置权限范围 | 影响 5 张配置表写入 |

### 2.3 低影响 op（绿区）

| opKey | 动作 |
|---|---|
| `save` (初始保存草稿) | 仅落 t_hrcs_dynascheme + 反查表 · 不影响下游 |
| `copy` | 创建一个新方案（boid 不同）· 跟旧方案完全独立 |
| `view` | 查看 · 不写 |
| 其他导入导出 / 导航 op | 不写主数据 |

---

## 三、跨场景级联影响

### 3.1 强联动（必须考虑）

```
hrcs_dynascheme.audit
   ↓
hrcs_dynascheme.confirmchange
   ↓
DynaSchemeRoleAssignServiceHelper.saveRoleEntry → t_hrcs_dynaschemerole
   ↓
[标品异步] 权限重算任务 / 用户登录时计算
   ↓
t_hrcs_userrolerelat 增/删 · sourcetype=4 标记
   ↓
[每次权限校验] 用 hrcs_userrolerelat 决定权限
   ↓
影响功能：菜单可见性 / 数据范围 / 操作按钮启用
```

### 3.2 弱联动（间接）

| 上游/下游 | 联动 |
|---|---|
| `perm_admingroup` 改 | dynascheme 的 admingroup F7 列表变 |
| `perm_role` 改 | dynascheme 的 roleentry F7 列表变 |
| `hrcs_role` 改 | roleentry.hrcsrole 反查值变 |
| `hrcs_dynaauthobject` 改 | dynascheme assignpersonitem/cancelpersonitem F7 变 |
| `hpfs_chgcategory` 改 | dynascheme assignactype/cancelactype F7 变 |
| 员工岗位变动（hrpi_empjobrel） | dynascheme 规则命中可能变 → 触发权限重算 |

---

## 四、改动 dynascheme 的 4 个常见场景 + 影响分析

### 4.1 场景 A · 客户加自定义字段（CS-01）

| 检查 | 影响 | 安全？ |
|---|---|---|
| modifyMeta add 字段到主表 | 加了 ISV 字段 · t_hrcs_dynascheme 加列 | ✅ |
| HisModel 自动复制字段到新版本 | confirmchange 后新版本带这个字段 | ✅（设计如此） |
| 反查派生表是否带这个字段 | ❌ 不带（resolveRuleConfigToSearch 是固定字段集） | ✅（不影响标品流程） |
| 影响 hrcs_userrolerelat | ❌ 不影响（不在派生链里） | ✅ |

### 4.2 场景 B · 客户改 admingroup 字段值（业务运营误操作）

| 检查 | 影响 |
|---|---|
| 可能性 | scene_doc.json `admingroup.isvCanModify=false` · 标品默认锁死 · 不能改 · 但客户走 ISV 自定义 OP 强改 ⚠️ |
| 改了之后 | resolveRuleConfigToSearch 重灌反查表（不一致）· 已分配的 hrcs_userrolerelat 不立即变（要等下次重算）· admingroup 范围 5 表（hrcs_dynaschemerange 等）需要手清 |
| 安全建议 | 不要走 ISV OP 强改 · 走"copy 方案 + 改 admingroup + 删旧方案"路径 |

### 4.3 场景 C · 客户禁用方案（disable）

| 检查 | 影响 |
|---|---|
| 数据状态变化 | enable: 1 → 0 |
| 已分配绑定 | 不立即清（hrcs_userrolerelat 残留） |
| 下次权限重算 | 该方案不参与重算 → 新员工不再分配此方案的角色 |
| 已禁用方案保留期 | 业务侧决定 · 标品不自动清理 |
| 安全建议 | disable 前查 hrcs_userrolerelat 残留绑定数（CS-04 套路） |

### 4.4 场景 D · 客户删除方案（delete）

| 检查 | 影响 |
|---|---|
| 主表 t_hrcs_dynascheme 删 | 永久丢 |
| 5 张配置表（dynaschemerange 等）级联删 | DynaAuthSchemeListPlugin.afterDoOperation 自动 |
| t_hrcs_dynaschemerole 分录 | BdVersionDeleteOp 标品流程级联删 |
| **t_hrcs_userrolerelat 残留** | ❌ **不自动清** · 残留 sourcetype=4 的孤儿绑定 |
| 安全建议 | delete 前查 hrcs_userrolerelat 残留 · 手清或 ISV 自动清（CS-04） |

---

## 五、HisModel 时序场景特有影响

### 5.1 confirmchange 后的"版本爆炸"

```
方案 V1（status=C, iscurrentversion=true）
  + 5 个 roleentry 行
  ↓ confirmchange
方案 V2（status=C, iscurrentversion=true · sourcevid=V1.id）
  + 5 个 roleentry 行（entryboid 跟 V1 一致）
  ↓ confirmchange
方案 V3...
```

→ 每次 confirmchange 主表 + 分录全量复制 · **历史版本永久保留**（无自动清理）

→ 影响：
- DB 占用线性增长 · 一年 12 次 confirmchange × 100 方案 = 1200+ 历史行
- 列表查询带 iscurrentversion=true 过滤 · 性能不受影响
- 但 DataSet 跨版本查询 · 需要带 boid 过滤

### 5.2 entryboid 跨版本一致性

`DynaAuthSchemeAuditOp.endOperationTransaction` 调 `genVersionRoleEntryColl(sourceVid, ...)`（实证 L29-L41）：
- 每行 entry 在新版本里复用旧的 entryboid
- 这意味着"V1.entry1 = V2.entry1"在业务上 · 即使他们的 fentryid 不同
- 下游写日志若要"跟踪某行 entry 的演化" · 必须用 entryboid 而非 fentryid

---

## 六、生产事故规避指南

### 6.1 误操作类（高频）

| 事故 | 规避 |
|---|---|
| 客户禁用一个生产方案 → 一批员工失权 | CS-04 加事前阻断 + 提示残留绑定数 |
| 客户改 condition 后忘记 confirmchange · 直接 save → 数据态错乱 | 标品 PermRuleValidateUtil 已校验 · ISV 不要绕过 |
| 客户复制方案后忘记改 number → 编码冲突 | 标品 CodeRuleOp 自动生成新 number · ISV 不要手填 |
| 客户在列表批量 confirmchange → 抛 not support | 标品已抛 KDBizException · ISV 在 Validator 阶段拦更友好 |

### 6.2 数据一致性类

| 事故 | 规避 |
|---|---|
| ISV 自定义 OP 在 endOperationTransaction 阶段抛异常 → 主事务回滚 → ISV 自建表写了一半 | 必须把"ISV 自建表写"放同事务内（用 HRBaseServiceHelper · 自动同事务） |
| ISV 在 afterExecuteOperationTransaction 阶段又抛异常 | 该阶段事务已提交 · 抛异常不会回滚 · 只 log |
| ISV 写 BEC 不预配 eventNumber → 静默丢消息 | CS-05 必须先在【开发平台】配 eventNumber |

### 6.3 性能类

| 事故 | 规避 |
|---|---|
| ISV 在 beforeDoOperation 同步查 N 张大表 → 用户感知 RT 拉长 | 把"查询" 推到 OP 端 onAddValidators · 用 queryDataSet count |
| ISV 在 endOperationTransaction 同步调外部系统 → 主事务超时回滚 | 外部调用全部走 afterExecuteOperationTransaction |
| ISV 加字段后 confirmchange · 字段值复制慢 · 卡住 confirmchange | 不可控（HisModel 标品行为）· 控制 ISV 字段数量 |

---

## 七、影响面分析工具

### 7.1 自动化扫描（推荐）

```bash
# 1. ISV 字段引用扫描
grep -r "${ISV_FLAG}_" knowledge/scenarios/hrcs_dynascheme/

# 2. 反编译类引用扫描
grep -r "DynaAuthScheme" knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dynascheme/

# 3. 影响 hrcs_userrolerelat 的 OP/插件
grep -r "hrcs_userrolerelat" knowledge/_sdk_audit/_decompiled/

# 4. 涉及 boid / sourcevid 的代码（HisModel 高危点）
grep -E "boid|sourcevid" knowledge/_sdk_audit/_decompiled/scenarios/hrcs_dynascheme/*.java
```

### 7.2 ISV 改动 checklist

每次 ISV 提交改动前 · 跑：
```
[ ] 读 03_model_design.md 确认改的字段在哪一层（L0/L1/L2/L3）
[ ] 改 L0/L1/L2 字段 → 红区 · 必须高优先级 review
[ ] 改 L3 业务字段 → 看 isvCanModify 标记
[ ] 加新 OP/Plugin → 看 PR-001 是否并列挂 · 没继承场景专属类
[ ] 涉及 confirmchange → 看 PR-008/PR-009 是否带 iscurrentversion + boid
[ ] 跨表写 → 看 PR-010 选对 endOperationTransaction vs afterExecute
[ ] 发外部事件 → 看 PR-011 BEC 流程
```

---

## 八、Q&A · 客户最常问的影响面问题

### Q1：禁用方案后已分配的员工权限会立即丢吗？

→ 不会。`enable=0` 后 · 已写入 `hrcs_userrolerelat` 的 sourcetype=4 绑定保留 · 等下次权限重算（标品异步任务 / 用户登录时）才会清。

### Q2：删除方案后 hrcs_userrolerelat 残留怎么办？

→ DynaAuthSchemeListPlugin.afterDoOperation 只清 5 张配置表（dynaschemerange / dynaschorg / dynaschdimgrp / dynaschdatarule / dynaschfield）· **不清 hrcs_userrolerelat**。需要业务侧手清或 ISV 自动清（CS-04 + 自定义清理 OP）。

### Q3：confirmchange 多少次会有性能问题？

→ 单方案版本无上限 · 但跨版本查询性能随版本数线性。建议：
- 单方案 confirmchange < 50 次/年 · 不会有问题
- > 100 次 · 考虑业务上分裂方案（一个方案做一两次 · 多种业务用多个方案）

### Q4：ISV 加字段会被 audit/confirmchange 自动维护版本吗？

→ 是。HisModel 时序模型默认对所有字段做版本复制。ISV 加的字段也会被复制到新版本。
