# core_hr_contract_renew · 扩展点

> 6 个标准扩展点·锚点来自 W1 deep_scan_audit.md D 段调用链
> 详细方案在 [`06_customization_solutions.md`](06_customization_solutions.md)

## 扩展点清单

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改"选员工"过滤维度 | `RenewBatchBillPlugin.itemClick:122-165` | ⭐⭐ | CS-02 |
| EP-02 | 加 ISV 主表自定义字段 | dym + BillPlugin + OpPlugin.onPreparePropertys | ⭐ | CS-03 |
| EP-03 | 改"按子组织递归"逻辑 | `AdminOrgHrUtils.getLowerOrgIds:24-46` | ⭐⭐⭐ | (未列 CS·见下) |
| EP-04 | 接入其他签署系统 | `ContractOtherExListPlugin:51-220` + ext2 dym | ⭐⭐⭐⭐ | CS-04 |
| EP-05 | 改 7 op 行为 | `RenewBatchOpPlugin.beforeExecute:107-128` | ⭐⭐ | CS-06 |
| EP-06 | 加批次审批工作流 | `RenewBatchOpPlugin.afterExecute` + 苍穹 wf 配置 | ⭐⭐⭐ | CS-05 |

## EP-01 · 改"选员工"过滤维度

**入口**：[`RenewBatchBillPlugin.itemClick`](../../_assets/contract_renew_batch/code/${ISV_FLAG}_hlcm_ext/src/main/java/${ISV_FLAG}/hr/hlcmext/plugin/RenewBatchBillPlugin.java) line 122-165

**默认行为**：按 `${ISV_FLAG}_affiliationord` 取所属组织 + 子组织·过滤 `hlcm_contractapplyrenew.affiliationord IN orgIds`。

**扩展方向**：
- 加职级过滤（`joblevel.level >= X`）
- 加雇佣类型过滤（`laborreltype.code = "外包"`）
- 加合同到期日过滤（`contractenddate <= 2026-12-31`）
- 加自定义状态字段过滤

**SDK 调用范式**（白名单合规）：
```java
// EP-01 标准模式 · 走 QFilter 复合
QFilter affiliationFilter = new QFilter("affiliationord", QCP.in, orgIds);
QFilter customFilter = new QFilter("<your_field>", QCP.equals, "<your_value>");
listShowParameter.getFilters().addAll(affiliationFilter, customFilter);
```

**风险**：复合过滤性能·大组织 (500+ 子组织 × 1000 员工) 慢·建议加索引。

---

## EP-02 · 加 ISV 主表自定义字段

**入口**：3 处联动·任何一处漏改 = 字段读不到。

| 修改位置 | 文件 | 内容 |
|---|---|---|
| dym 字段定义 | `${ISV_FLAG}_hlcm_renewbatch.dym` | `<TextField id="${ISV_FLAG}_<your_field>" length="500"/>` |
| BillPlugin 读写 | `RenewBatchBillPlugin.java` | `this.getModel().getValue("${ISV_FLAG}_<your_field>")` |
| OpPlugin 字段声明 | `RenewBatchOpPlugin.onPreparePropertys` | `fieldKeys.add("${ISV_FLAG}_<your_field>")` |

**风险**：onPreparePropertys 漏改 → OP 时 `getString("${ISV_FLAG}_<your_field>")` 返 null。

---

## EP-03 · 改"按子组织递归"逻辑

**入口**：[`AdminOrgHrUtils.getLowerOrgIds`](../../_assets/contract_renew_batch/code/${ISV_FLAG}_hlcm_ext/src/main/java/${ISV_FLAG}/hr/hlcmext/plugin/AdminOrgHrUtils.java) line 24-46

**默认行为**：`structlongnumber LIKE %xxx%` 模糊匹配·返该组织 + 全部子组织。

**扩展方向**：
- 限定层数（如最多 3 层下钻·而非全部子组织）
- 跳过虚拟组织（`virtualorg=true` 的不算）
- 加 enable / datastatus 复合过滤

**改造模式**：
```java
// EP-03 模式 · 改 QueryServiceHelper 查询条件
public static List<Long> getLowerOrgIdsLimitDepth(List<String> structNumberList, int maxDepth) {
    QFilter qFilter = new QFilter("enable", "=", "1")
        .and("datastatus", "=", "1")
        .and("iscurrentversion", "=", true);
    // 加深度限制
    qFilter.and("structdepth", QCP.less_equals, maxDepth);
    // 其他不变
}
```

**风险**：
- ⚠ 此 utils 多场景共用·改造时建议**新增方法**而非改原方法（避免污染其他场景）
- ⚠ structdepth 是 haos_adminorg 的字段·改前确认现场版本支持

---

## EP-04 · 接入其他签署系统（替代法大大）

**入口**：[`ContractOtherExListPlugin`](../../_assets/contract_renew_batch/code/${ISV_FLAG}_hlcm_ext/src/main/java/${ISV_FLAG}/hr/hlcmext/plugin/ContractOtherExListPlugin.java) line 51-220

**默认行为**：基于 `${ISV_FLAG}_fddsignstatuss / _signwaynew / _fillinstatus` 三字段拦截 3 按钮（详见 [02_business_rules.md R-05](02_business_rules.md)）。

**扩展模式**（PR-001 不直接改原插件·并列挂多个）：

```
hlcm_contractapplyrenew 列表
    ├── ContractOtherExListPlugin（标品·法大大判定）
    └── ${ISV_FLAG}_OtherSignListPlugin（ISV 自建·新签署系统判定）
        ↓ 主从配置驱动
    哪个签署系统为主 → 谁的拦截优先
```

5 处改造：
1. **ext2 dym** 加新签署系统字段（如 `${ISV_FLAG}_qysignstatus`）
2. **新建 ListPlugin** 并列挂在 hlcm_contractapplyrenew·**不要继承 ContractOtherExListPlugin**（PR-001 ISV 隔离）
3. **加签署回调插件** 处理新系统的 webhook 写状态字段
4. **deploy_sop** 加新系统环境变量
5. **配置表** 存"主签署系统"开关·驱动多签署系统并存逻辑

**风险**：
- ⚠ 双继承死循环（PR-001）：不要在 ContractOtherExListPlugin 上加 if/else 分支·并列挂
- ⚠ 多系统共存时·要明确拦截优先级（哪个 throw 先生效）

---

## EP-05 · 改 7 op 行为

**入口**：[`RenewBatchOpPlugin.beforeExecuteOperationTransaction`](../../_assets/contract_renew_batch/code/${ISV_FLAG}_hlcm_ext/src/main/java/${ISV_FLAG}/hr/hlcmext/plugin/RenewBatchOpPlugin.java) line 107-128

**默认行为**：audit/save/submit/wfauditing 4 op 严格校验 hlcm_contractapplyrenew.billstatus="C"。

**扩展方向**：
- 放宽校验（允许 D=待审批·走"半审批"流）
- 加自定义状态校验（如要求 `${ISV_FLAG}_fillinstatus="2"`）
- 切换 op 的语义（如 submit 时不写关联·只在 audit 时写）

**改造范式**：
```java
// EP-05 模式 · 在 beforeExecute 加自定义校验
@Override
public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
    String opKey = e.getOperationKey();
    if (!Arrays.asList("audit","save","submit","wfauditing").contains(opKey)) return;

    // 默认严格 C
    if (!"C".equals(item.getString("billstatus"))) {
        // 改为放宽 C 或 D
        if (!Arrays.asList("C","D").contains(item.getString("billstatus"))) {
            throw new KDBizException(...);
        }
    }
}
```

**风险**：
- ⚠ 放宽校验后·D 状态合同的"半审批"语义对下游（薪资/福利）影响要确认
- ⚠ 建议加 ISV 配置表存放规则·而非硬编码

---

## EP-06 · 加批次审批工作流

**入口**：[`RenewBatchOpPlugin.afterExecuteOperationTransaction`](../../_assets/contract_renew_batch/code/${ISV_FLAG}_hlcm_ext/src/main/java/${ISV_FLAG}/hr/hlcmext/plugin/RenewBatchOpPlugin.java) line 138-210 + 苍穹工作流配置

**默认行为**：批次主表 audit 单步·HR 自审。

**扩展模式**（3 步）：
1. 苍穹工作流配置·定义 `${ISV_FLAG}_hlcm_renewbatch` 的多级审批流（无代码）
2. afterExecute 扩展 wfauditing 行为·按 `currentStep` 写不同状态字段
3. 加 ISV 字段标审批进度（`_hr_approved` / `_depthead_approved` / `_cfo_approved`）

**改造范式**：
```java
if ("wfauditing".equals(opKey)) {
    String currentStep = e.getOperationVariableValue("currentStep");
    if ("HR".equals(currentStep)) {
        dataEntity.set("${ISV_FLAG}_hr_approved", true);
    } else if ("DeptHead".equals(currentStep)) {
        dataEntity.set("${ISV_FLAG}_depthead_approved", true);
    }
}
```

**风险**：
- ⚠ 工作流变量 `currentStep` 要在苍穹流程引擎里预先定义
- ⚠ 多步审批时·要确认每步审批人有权限读到 `${ISV_FLAG}_hlcm_renewbatch` 的字段

---

## SDK 白名单合规扩展模式

本资产 4 个核心类全部继承平台白名单基类（详见 [03_model_design.md SDK 白名单](03_model_design.md)）：

| 扩展类 | 父类（白名单） | 出处 |
|---|---|---|
| RenewBatchBillPlugin | `AbstractFormPlugin` | kd.bos.form |
| RenewBatchOpPlugin | `AbstractOperationServicePlugIn` | kd.bos.entity.operate |
| ContractOtherExListPlugin | `AbstractListPlugin` | kd.bos.list.plugin |
| AdminOrgHrUtils | (utility class·无父类) | - |

**扩展铁律**（PR-001/PR-010）：
- ✅ 继承白名单基类（`Abstract*Plugin`）
- ✅ 用 `@Override` 重写 13 个生命周期方法（详见 PR-010）
- ✅ ISV 字段必带 `${ISV_FLAG}_` 前缀
- ✅ 走 ext2 扩展元数据·不直接 modifyMeta 标品 form
- ❌ 不继承 ISV 自己的插件（双继承死循环）
- ❌ 不直接调内部 API（`kd.bos.servicehelper.permission.PermissionServiceHelper` 等无 @SDKPublic 注解的）

## 关联 PR 红线

- **PR-001** ISV 隔离·所有扩展走 ext + 字段加 `${ISV_FLAG}_` 前缀
- **PR-008/009** 时序资料·hlcm_contract 是 HisModel·查询带 `iscurrentversion=1`
- **PR-010** OP 13 生命周期·本资产用 beforeExecute / afterExecute 两个
- **PR-011** BEC 事件中心·**本资产不发 BEC**·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)
