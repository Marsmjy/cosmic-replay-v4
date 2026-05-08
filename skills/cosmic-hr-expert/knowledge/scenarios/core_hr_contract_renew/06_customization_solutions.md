# core_hr_contract_renew · 推荐定制方案

> **状态**：🟢 基于 dcs_clean v1.5 真代码 + W1 真扫 + 6 EP 扩展点
> **资产复刻**：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/)·一键 assemble 出 37 文件
> **代码示例**：本节用 `${ISV_FLAG}` 占位符代表客户开发商标识·部署时全局替换为客户实值（如 bjss / cds0）

---

## CS-01 · 完整资产复刻（最高频路径 ⭐）

### 业务背景

ISV 实施工程师拿到客户需求"我要做一套批量合同续签"·客户提供：
- 开发商标识（如 `bjss`）
- ISV 应用编码（如 `bjss_hlcm_ext`）

**预期产出**：完整工程包（5 java + 7 datamodel + 15 SQL + 2 i18n + 6 工程文件 + 部署 SOP）。

### 推荐方案

```bash
python scripts/assemble_asset.py \
    --asset contract_renew_batch \
    --isv-flag bjss \
    --biz-app bjss_hlcm_ext \
    --output D:/myproject/contract_renew_batch-bjss/
```

产物 37 文件：
- `code/bjss_hlcm_ext/src/main/java/bjss/hr/hlcmext/plugin/{AdminOrgHrUtils, ContractOtherExListPlugin, RenewBatchBillPlugin, RenewBatchOpPlugin}.java`
- `datamodel/hr/1.5.0/main/bjss_hlcm_ext/{*.app, *.appx, *.xml, metadata/{bjss_hlcm_renewbatch.dym, bjss_hlcm_contractap_ext2.dym, hr.cld, ...}, preinsdata/{15 SQL}}`
- `build.gradle / settings.gradle / cosmic.json` 等 6 工程文件
- `deploy_sop.md / customization_points.md`

### 部署 SOP

详见 [`_assets/contract_renew_batch/deploy_sop.md`](../../_assets/contract_renew_batch/deploy_sop.md)·6 步法。

### 踩坑

1. ⚠️ **15 SQL 预置数据**：包含 hlcm 系列 form 的初始数据·**部署前要先确认现场环境是否已经存在**·避免重复 insert
2. ⚠️ **ext2 扩展元数据**：客户现场如果已经有 hlcm_contractapplyrenew_ext / _ext2·要避免与本资产冲突·或合并
3. ⚠️ **法大大集成**：本资产引用 `${ISV_FLAG}_fddsignstatuss / _signwaynew` 字段·假设客户已有法大大集成·若没有要先实施法大大插件

### 关联 PR

- PR-001 ISV 隔离·走 ext2 扩展元数据
- PR-008/009 时序资料 (hlcm_contract 是 HisModel)

---

## CS-02 · 改"选员工"过滤维度

### 业务背景

默认按 `affiliationord` 过滤·客户可能想按"职级 ≥ XX" / "雇佣类型=外包"等过滤。

### 扩展点

修改 `RenewBatchBillPlugin.itemClick` (line 122-165)：

```java
// 默认过滤
QFilter affiliationFilter = new QFilter("affiliationord", QCP.in, orgIds);

// 加客户过滤
QFilter joblevelFilter = new QFilter("joblevel.level", QCP.large_equals, customLevel);
QFilter laborrelFilter = new QFilter("laborreltype.code", QCP.equals, "外包");

// 复合
listShowParameter.getFilters().addAll(affiliationFilter, joblevelFilter, laborrelFilter);
```

### 踩坑

- ⚠️ 复合过滤性能·对大组织 (500+ 子组织 × 1000 员工) 可能很慢·建议加索引

### 关联 PR

- PR-005 用 QFilter·不要用 `OrderBy.大字段 LIKE` 慢查询

---

## CS-03 · 加 ISV 主表自定义字段

### 业务背景

客户业务方要求在 `${ISV_FLAG}_hlcm_renewbatch` 加自定义字段（如"续签批次说明 ${ISV_FLAG}_batchremark"）。

### 扩展点

3 处联动：

#### a) datamodel/.../${ISV_FLAG}_hlcm_renewbatch.dym

加字段：
```xml
<TextField id="${ISV_FLAG}_batchremark" length="500" required="false">
    <Name lcid="zh_CN" value="续签批次说明"/>
</TextField>
```

#### b) RenewBatchBillPlugin·按需读写

```java
String remark = this.getModel().getValue("${ISV_FLAG}_batchremark");
```

#### c) RenewBatchOpPlugin.onPreparePropertys 加字段声明

```java
fieldKeys.add("${ISV_FLAG}_batchremark");
```

### 踩坑

- ⚠️ onPreparePropertys 漏改 → OP 时 `getString("${ISV_FLAG}_batchremark")` 返 null

---

## CS-04 · 接入其他签署系统（替代法大大）

### 业务背景

客户用其他电子签署服务（如 e签宝 / 上上签）·不用法大大。

### 扩展点

5 处改造：

1. **ContractOtherExListPlugin** 改放行规则（line 51-220）·按新字段判定·如 `${ISV_FLAG}_qysignstatus` 替代 `${ISV_FLAG}_fddsignstatuss`
2. **ext2 dym** 改字段定义·加新签署系统字段
3. **加客户自建 sign 集成 plugin**（在 hlcm_contractapplyrenew 上挂另一个 ListPlugin·并列于 ContractOtherExListPlugin）
4. **签署回调插件**：处理新签署系统的 webhook
5. **deploy_sop**：加新系统的环境变量配置

### 踩坑

- ⚠️ 不要直接在 ContractOtherExListPlugin 里加 if/else·用并列挂多个 plugin·防双继承死循环 (PR-001)
- ⚠️ 多签署系统并存时·加判定哪个为主（如配置驱动）

---

## CS-05 · 加批次审批工作流

### 业务背景

默认批次主表 audit 是手工·客户想加多级审批工作流（如 HR → 部门长 → CFO）。

### 扩展点

3 步：

1. **苍穹工作流配置**·定义 `${ISV_FLAG}_hlcm_renewbatch` 的审批流（无代码）
2. **RenewBatchOpPlugin·扩展 wfauditing 行为**：
   ```java
   // 加多级审批·不同审批人写不同状态字段
   if ("wfauditing".equals(opKey)) {
       String currentStep = e.getOperationVariableValue("currentStep");
       if ("HR".equals(currentStep)) {
           dataEntity.set("${ISV_FLAG}_hr_approved", true);
       } else if ("DeptHead".equals(currentStep)) {
           dataEntity.set("${ISV_FLAG}_depthead_approved", true);
       }
   }
   ```
3. **加 ISV 字段** `${ISV_FLAG}_hr_approved` / `_depthead_approved` / `_cfo_approved`·标审批进度

---

## CS-06 · 改 7 op 行为

### 业务背景

客户业务流不严格要求 audit 时所有 hlcm 的 billstatus="C"·想放宽（如允许 D=待审批状态）。

### 扩展点

`RenewBatchOpPlugin.beforeExecuteOperationTransaction:107-128`：

```java
// 默认严格校验
if (!"C".equals(item.getString("billstatus"))) {
    throw new KDBizException(...);
}

// 改为放宽（C 或 D）
if (!Arrays.asList("C", "D").contains(item.getString("billstatus"))) {
    throw new KDBizException(...);
}
```

### 踩坑

- ⚠️ 放宽校验后·要明确 D 状态合同的"半审批"语义对下游 (薪资/福利) 影响
- ⚠️ 建议加 ISV 配置表存放规则·而不是硬编码

---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 hlcm_contractapplyrenew 加字段 | PR-001 铁律·必须走 ext2 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin 的 before/after 走·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品 hlcm 自带 BEC·ISV 不重发·避免重复事件 |
| ❌ 改 hlcm_contractapplyrenew 的 billstatus / auditstatus | 这是标品状态机字段·ISV 不能动·只能写 ${ISV_FLAG}_renewbatch 反向引用 |

详见 [`_antipatterns.json`](../../_antipatterns.json)。

---

## 关联 PR 红线

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext2 扩展元数据 ✅
- **PR-008/009** 时序资料·hlcm_contract 是 HisModel·查询要带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法·本资产用 `beforeExecute/afterExecute` 两个 ✅
- **PR-011** BEC 事件中心·**本资产不发 BEC**·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)
