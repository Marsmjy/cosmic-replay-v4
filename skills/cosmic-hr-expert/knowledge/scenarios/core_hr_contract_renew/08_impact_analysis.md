# core_hr_contract_renew · 影响分析

> 数据源：W1 deep_scan_audit.md K 段（影响面）+ deep_scan_inventory.md（资产清单）

## 引入资产对客户环境的影响

### 元数据层（datamodel/）

| 影响项 | 数量 | 说明 | 兼容性风险 |
|---|---|---|---|
| 新增 ISV 自建 form | 1 | `${ISV_FLAG}_hlcm_renewbatch` | 无（全 ISV 命名空间）|
| 新增 ext2 扩展元数据 | 1 | `${ISV_FLAG}_hlcm_contractap_ext2` 在 hlcm_contractapplyrenew 上加 4 字段 | ⚠ 客户已有 ext2 → 必须合并 |
| 新增 dym 文件 | 7 | renewbatch + ext2 + 5 关联 | 无 |
| 预置数据 SQL | 15 | hlcm 系列 form 的 preinsdata | ⚠ 客户已有 → 重复 insert 风险 |

### 代码层（code/）

| 影响项 | 数量 | 说明 |
|---|---|---|
| 新增 java 类 | 5 | AdminOrgHrUtils + 3 plugin + 1 op | 全 ISV 包路径·无冲突 |
| 反编译 jar 引用 | 0 | 不依赖客户现场任何 jar·全走苍穹白名单 SDK |

### 国际化（i18n/）

| 影响项 | 数量 | 说明 |
|---|---|---|
| 新增 i18n key | 2 | zh_CN + en_US | 客户已有同名 key 时报"重复"·部署前要 grep |

---

## 性能影响评估

### 关键性能点

| 操作 | 复杂度 | 数据规模 | 性能预估 |
|---|---|---|---|
| 选员工弹窗（itemClick）| O(N×M) | N=组织数·M=员工数 | 500 组织 × 1000 员工 → ~3-5s |
| AdminOrgHrUtils.getLowerOrgIds | O(N) | N=子组织数 | 1000 子组织·~500ms·**LIKE 全表扫**·无索引 |
| beforeExecute 反查 hlcm | O(K) | K=批次员工数 | 100 员工·~200ms |
| afterExecute 反向解绑 | O(K) | K=已关联员工 | 100 员工·~300ms (含 SaveServiceHelper.save) |
| dealUnbindBatch（delete）| O(K) | K=批次主表数 | 主表 50 × 平均 100 员工 → ~5s |

### 性能优化建议

1. **AdminOrgHrUtils LIKE 慢查**：
   - haos_adminorg.structlongnumber 加索引（如已有则跳过）
   - 或改 EQ + IN 查询（如客户允许限定层数）

2. **大批次 save 慢**：
   - 单批次 > 500 员工时·beforeExecute 校验耗时显著
   - 建议加批次大小预警（如 personsize > 500 时弹窗确认）

3. **List F7 弹窗大数据集**：
   - hlcm_contractapplyrenew 全量 > 10W 行时·首次加载慢
   - 建议加索引：`(affiliationord, billstatus, ${ISV_FLAG}_renewbatch)` 复合索引

---

## 跨云影响（ADR-009 跨云穿透）

### 上游依赖（本资产消费的）

| 来源云 | form / 数据 | 调用方式 | 失败影响 |
|---|---|---|---|
| 核心人力云（core_hr） | `hlcm_contractapplyrenew` 标品 | 反查 + 写 ${ISV_FLAG}_renewbatch | 客户未启用 hlcm 模块 → 整个资产无法运行 |
| 组织发展云（org_dev） | `haos_adminorg` | structlongnumber LIKE 查询 | 客户 haos 数据缺失 → 选员工弹窗为空 |

### 下游影响（本资产改了什么·谁可能受影响）

| 影响目标 | 影响方式 | 是否同步 |
|---|---|---|
| `hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch` | OP audit 时写入·OP wfunaudit/invalid/delete 时清空 | 同事务·强一致 |
| 标品 hlcm 内部状态机 | **不动**·只在 ISV 字段层操作 | - |
| 跨云 swc/wtc/hbjm | **本资产不直接通知**·靠标品 hlcm 自动派发 BEC | 异步·走标品自带 BEC |

### 跨云数据一致性保证

```
本资产 (ISV)              标品 hlcm                     跨云
   ↓ 同事务写关联             ↓ 标品 audit op             ↓ BEC
${ISV_FLAG}_renewbatch    hlcm_contractapplyrenew     swc 薪资 / wtc 考勤
                           ↓ 标品 afterAudit             ↓ 跟随更新
                           hlcm_contract                 hpfs_chgrecord
```

⚡ **关键**：本资产**只联动 hlcm 内部**·跨云事件靠**标品 hlcm 自动派发**·ISV 不重写。详见 [05_data_flow.md 跨云数据流](05_data_flow.md)。

---

## 升级风险

### 苍穹版本兼容性

| 苍穹版本 | 兼容性 | 风险点 |
|---|---|---|
| dcs_clean 基线 v1.5 | ✅ 全兼容 | 本资产打底版本 |
| 客户现场 < v1.5 | ⚠ 校验 | hlcm_contractapplyrenew 字段定义可能差异（如 fillinstatus 字段未引入） |
| 客户现场 > v1.5 | ⚠ 校验 | ext2 扩展可能与平台新增字段冲突·部署前 dym diff |

### 关键升级风险点

1. **hlcm_contractapplyrenew 字段变更**：
   - 标品平台增删字段时·ISV 反查可能失败
   - 缓解：beforeExecute 用 try/catch 包 BusinessDataServiceHelper.load·失败降级日志

2. **AdminOrgHrUtils 实现变更**：
   - 苍穹平台 OrgServiceUtil 可能演进·structlongnumber 含义变化
   - 缓解：本资产已 fork 一份 utils·不继承平台·影响隔离

3. **OP 生命周期变化**：
   - PR-010 OP 13 方法在新版苍穹可能新增/废弃
   - 缓解：本资产只用 beforeExecute / afterExecute 两个稳定方法

### 回滚方案

资产部署 = 增量加 7 dym + 5 java + 15 SQL + 2 i18n。回滚步骤：
1. 删 ${ISV_FLAG}_hlcm_renewbatch 整个 form（连数据 truncate）
2. 删 ext2 在 hlcm_contractapplyrenew 上的 4 字段（清 schema）
3. 删 5 java 类的 jar 包
4. 反执行 15 SQL 的 preinsdata（注意：客户已用过的数据要保留）
5. 删 2 i18n key

⚠ **风险**：第 4 步若客户已建批次·`hlcm_contractapplyrenew.${ISV_FLAG}_renewbatch` 字段值需要先清 null·再删字段定义·否则约束冲突。

---

## 监控与告警建议

| 监控项 | 阈值 | 告警等级 |
|---|---|---|
| AdminOrgHrUtils.getLowerOrgIds 耗时 | > 5s | warning |
| RenewBatchOpPlugin.beforeExecute 单批次耗时 | > 30s | error |
| List F7 弹窗加载耗时 | > 10s | warning |
| dealUnbindBatch 单次解绑数 | > 1000 | warning（确认是否合理批量删）|
| KDBizException "员工 X 合同未审核" 频次 | > 50/天 | warning（业务流程问题）|

## 关联文档

- [05_data_flow.md](05_data_flow.md) — 数据流向 + 跨云数据流
- [06_customization_solutions.md](06_customization_solutions.md) — CS-01 部署 SOP
- [`_assets/contract_renew_batch/deploy_sop.md`](../../_assets/contract_renew_batch/deploy_sop.md) — 实际部署步骤
