# 批量劳动合同续签 (contract_renew_batch) · 扩展点（Customization Points）

> 6 个标准扩展点·按客户需求定制·**不改资产骨架**
> 数据源：W1 deep_scan_audit.md + 11md 场景知识

## 扩展点总览

| EP ID | 名称 | 修改位置 | 难度 | 工时估算 | CS 关联 |
|---|---|---|---|---|---|
| EP-01 | 改"选员工"过滤维度 | RenewBatchBillPlugin.itemClick:122-165 | ⭐⭐ | 2-3 天 | CS-02 |
| EP-02 | 加 ISV 主表自定义字段 | dym + BillPlugin + OpPlugin 三处 | ⭐ | 0.5-1 天 | CS-03 |
| EP-03 | 改"按子组织递归"逻辑 | AdminOrgHrUtils.getLowerOrgIds:24-46 | ⭐⭐⭐ | 3-5 天 | (07_ext_points.md) |
| EP-04 | 接入其他签署系统（替代法大大）| ContractOtherExListPlugin:51-220 + ext2 dym | ⭐⭐⭐⭐ | 1-2 周 | CS-04 |
| EP-05 | 改 7 op 行为 | RenewBatchOpPlugin.beforeExecute:107-128 | ⭐⭐ | 2-3 天 | CS-06 |
| EP-06 | 加批次审批工作流 | RenewBatchOpPlugin.afterExecute + 苍穹 wf 配置 | ⭐⭐⭐ | 5-7 天 | CS-05 |

---

## EP-01 · 改"选员工"过滤维度

### 业务场景

默认按 `${ISV_FLAG}_affiliationord` 过滤·客户可能想：
- 按职级过滤（"职级 ≥ XX 的员工"）
- 按雇佣类型过滤（"只选外包员工"）
- 按合同到期日过滤（"只选 6 个月内到期的"）
- 多条件复合

### 改造方案

修改 `RenewBatchBillPlugin.itemClick`（line 122-165）·在打开 list 弹窗前加 QFilter：

```java
// 默认过滤
QFilter affiliationFilter = new QFilter("affiliationord", QCP.in, orgIds);

// EP-01 加客户自定义过滤
QFilter joblevelFilter = new QFilter("joblevel.level", QCP.large_equals, customLevel);
QFilter laborrelFilter = new QFilter("laborreltype.code", QCP.equals, "外包");
QFilter contractEndFilter = new QFilter("contractenddate", QCP.less_equals,
    DateUtils.addMonths(new Date(), 6));

// 复合
listShowParameter.getFilters().addAll(
    affiliationFilter, joblevelFilter, laborrelFilter, contractEndFilter
);
```

### 风险与建议

- ⚠ 复合过滤性能·对大组织 (500+ 子组织 × 1000 员工) 可能很慢·建议加复合索引 `(affiliationord, joblevel, laborreltype)`
- ⚠ joblevel / laborreltype 字段不一定所有客户都有·改造前先 dym diff 确认
- ✅ 推荐用配置驱动·把过滤条件写到 ISV 配置表·不要硬编码

---

## EP-02 · 加 ISV 主表自定义字段

### 业务场景

客户业务方要求在 `${ISV_FLAG}_hlcm_renewbatch` 加自定义字段，例如：
- 续签批次说明（`${ISV_FLAG}_batchremark` · TextField）
- 批次预算（`${ISV_FLAG}_batchbudget` · DecimalField）
- 批次类型（`${ISV_FLAG}_batchtype` · ComboField）

### 改造方案（3 处联动）

#### 1. dym 加字段定义

`datamodel/.../${ISV_FLAG}_hlcm_renewbatch.dym`：

```xml
<TextField id="${ISV_FLAG}_batchremark" length="500" required="false">
    <Name lcid="zh_CN" value="续签批次说明"/>
</TextField>
```

#### 2. BillPlugin 按需读写

`RenewBatchBillPlugin`（任意需要的方法）：

```java
String remark = (String) this.getModel().getValue("${ISV_FLAG}_batchremark");
this.getModel().setValue("${ISV_FLAG}_batchremark", "默认值");
```

#### 3. OpPlugin onPreparePropertys 声明

`RenewBatchOpPlugin`：

```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs e) {
    super.onPreparePropertys(e);
    List<String> fieldKeys = e.getFieldKeys();
    fieldKeys.add("${ISV_FLAG}_batchremark");
}
```

### 风险

- ⚠ **必踩坑**：onPreparePropertys 漏改 → OP 时 `getString("${ISV_FLAG}_batchremark")` 返 null
- ✅ 三处联动一处都不能少

---

## EP-03 · 改"按子组织递归"逻辑

### 业务场景

默认 `structlongnumber LIKE %xxx%` 全部子组织·客户场景：
- 集团级组织太大·下钻 5+ 层会查到几千子组织·性能爆炸
- 需要限定"只下钻 N 层"
- 需要跳过虚拟组织（`virtualorg=true`）

### 改造方案

修改 `AdminOrgHrUtils.getLowerOrgIds`（line 24-46）：

```java
public static List<Long> getLowerOrgIdsLimitDepth(List<String> structNumberList, int maxDepth) {
    QFilter qFilter = new QFilter("enable", "=", "1")
        .and("datastatus", "=", "1")
        .and("iscurrentversion", "=", true);

    // EP-03 加深度限制
    qFilter.and("structdepth", QCP.less_equals, maxDepth);

    // EP-03 跳过虚拟组织
    qFilter.and("virtualorg", "=", false);

    // 其他 LIKE 逻辑不变
    QFilter structFilter = null;
    for (String num : structNumberList) {
        if (structFilter == null) {
            structFilter = new QFilter("structlongnumber", QCP.like, "%" + num + "%");
        } else {
            structFilter.or("structlongnumber", QCP.like, "%" + num + "%");
        }
    }
    qFilter.and(structFilter);

    return QueryServiceHelper.queryPrimaryKeys("haos_adminorg",
        qFilter.toArray(), null, 10000);
}
```

### 风险与建议

- ⚠ AdminOrgHrUtils 多场景共用·改造时建议**新增方法**而非改原方法（避免污染其他场景）
- ⚠ structdepth 是 haos_adminorg 的字段·改前确认现场版本支持
- ⚠ virtualorg 同理·客户 hbis 早期版本可能没有此字段
- ✅ 推荐用配置驱动·把 maxDepth / 是否跳过虚拟组织写到 ISV 配置

---

## EP-04 · 接入其他签署系统（替代法大大）

### 业务场景

客户用其他电子签署服务（如 e签宝 / 上上签 / 自建签署系统）·不用法大大。

### 改造方案（PR-001 不直接改原插件·并列挂多个）

**5 处改造**：

#### 1. ext2 dym 加新签署系统字段

`${ISV_FLAG}_hlcm_contractap_ext2.dym` 加：
```xml
<TextField id="${ISV_FLAG}_qysignstatus" length="20">
    <Name lcid="zh_CN" value="e签宝签署状态"/>
</TextField>
```

#### 2. 新建并列 ListPlugin（**不要继承 ContractOtherExListPlugin**）

```java
package ${ISV_FLAG}.hr.hlcmext.plugin;

import kd.bos.list.plugin.AbstractListPlugin;

public class OtherSignListPlugin extends AbstractListPlugin {
    @Override
    public void itemClick(ItemClickEvent e) {
        String itemKey = e.getItemKey();
        if ("tblsubmit".equals(itemKey)) {
            // 按 ${ISV_FLAG}_qysignstatus 拦截·替代 fdd 判断
            ListSelectedRowCollection selectedRows = e.getSource().getSelectedRows();
            for (ListSelectedRow row : selectedRows) {
                DynamicObject obj = BusinessDataServiceHelper.loadSingle(
                    row.getPrimaryKeyValue(), "hlcm_contractapplyrenew",
                    "${ISV_FLAG}_qysignstatus"
                );
                if (!"完成".equals(obj.getString("${ISV_FLAG}_qysignstatus"))) {
                    throw new KDBizException("e签宝未完成·不能提交");
                }
            }
        }
    }
}
```

#### 3. 在 hlcm_contractapplyrenew dym 上挂新插件（并列于 ContractOtherExListPlugin）

```xml
<Plugin ListPlugin>${ISV_FLAG}.hr.hlcmext.plugin.OtherSignListPlugin</Plugin>
```

#### 4. 加签署回调插件

处理 e签宝 webhook·写 `${ISV_FLAG}_qysignstatus` 字段。

#### 5. deploy_sop 加 e签宝环境变量配置

```properties
# 客户 cosmic.properties 加
qysign.api.url=https://api.qiyue.com/v2
qysign.api.key=xxx
```

### 风险与反指引

- ❌ **绝对不要**直接在 ContractOtherExListPlugin 加 if/else 分支（PR-001 双继承死循环）
- ❌ 不要继承 ContractOtherExListPlugin·并列挂
- ⚠ 多签署系统并存时·要明确"主签署系统"是谁·防多个 plugin 都 throw
- ✅ 推荐配置驱动 ISV 主从开关·只让"当前主"的拦截生效

---

## EP-05 · 改 7 op 行为

### 业务场景

客户业务流不严格要求 audit 时所有 hlcm 的 billstatus="C"·想放宽：
- 允许 "D=待审批" 状态
- 加自定义状态校验（如要求 `${ISV_FLAG}_fillinstatus="2"`）
- 切换 op 语义（如 submit 时不写关联·只 audit 时写）

### 改造方案

修改 `RenewBatchOpPlugin.beforeExecuteOperationTransaction`（line 107-128）：

```java
// 默认严格 C
if (!"C".equals(item.getString("billstatus"))) {
    throw new KDBizException(...);
}

// EP-05 改为放宽（C 或 D）
if (!Arrays.asList("C", "D").contains(item.getString("billstatus"))) {
    throw new KDBizException(...);
}

// 或加自定义状态
if (!"2".equals(item.getString("${ISV_FLAG}_fillinstatus"))) {
    throw new KDBizException("员工 " + item.getString("empnumber") + " 合同填写未完成");
}
```

### 风险

- ⚠ 放宽校验后·D 状态合同的"半审批"语义对下游（薪资/福利）影响要确认
- ⚠ 客户 IT 必须明确"半审批合同进入批次"的合规风险
- ✅ 建议加 ISV 配置表·把规则放进配置·不硬编码

---

## EP-06 · 加批次审批工作流

### 业务场景

默认批次主表 audit 是单步·HR 自审。客户想加多级审批：
- HR → 部门长 → CFO 三级审批
- 不同审批人写不同状态字段
- 审批进度可视化

### 改造方案（3 步）

#### 1. 苍穹工作流配置（无代码）

苍穹后台 → 工作流管理 → 定义 `${ISV_FLAG}_hlcm_renewbatch` 的审批流：
- 节点 1：HR 节点 (currentStep=HR)
- 节点 2：部门长节点 (currentStep=DeptHead)
- 节点 3：CFO 节点 (currentStep=CFO)

#### 2. RenewBatchOpPlugin afterExecute 扩展 wfauditing

```java
@Override
public void afterExecuteOperationTransaction(AfterOperationArgs e) {
    String opKey = e.getOperationKey();
    if ("wfauditing".equals(opKey)) {
        String currentStep = e.getOperationVariableValue("currentStep");
        for (DynamicObject n : e.getDataEntities()) {
            if ("HR".equals(currentStep)) {
                n.set("${ISV_FLAG}_hr_approved", true);
                n.set("${ISV_FLAG}_hr_approve_date", new Date());
            } else if ("DeptHead".equals(currentStep)) {
                n.set("${ISV_FLAG}_depthead_approved", true);
            } else if ("CFO".equals(currentStep)) {
                n.set("${ISV_FLAG}_cfo_approved", true);
            }
        }
        SaveServiceHelper.save(e.getDataEntities());
    }
}
```

#### 3. 加 ISV 字段标审批进度

`${ISV_FLAG}_hlcm_renewbatch.dym`：

```xml
<BoolField id="${ISV_FLAG}_hr_approved"/>
<BoolField id="${ISV_FLAG}_depthead_approved"/>
<BoolField id="${ISV_FLAG}_cfo_approved"/>
<DateTimeField id="${ISV_FLAG}_hr_approve_date"/>
<DateTimeField id="${ISV_FLAG}_depthead_approve_date"/>
<DateTimeField id="${ISV_FLAG}_cfo_approve_date"/>
```

### 风险

- ⚠ 工作流变量 `currentStep` 要在苍穹流程引擎里预先定义·不能脑补
- ⚠ 多步审批时·每步审批人要有读写 `${ISV_FLAG}_hlcm_renewbatch` 的权限
- ⚠ 审批超时 / 驳回路径要单独处理（默认仅处理通过）
- ✅ 推荐审批进度做成布尔字段 + 日期字段·便于报表统计

---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 hlcm_contractapplyrenew 加字段 | PR-001 铁律·必须走 ext2 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin 的 before/after 走·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品 hlcm 自带 BEC·ISV 不重发·避免重复事件 |
| ❌ 改 hlcm_contractapplyrenew 的 billstatus / auditstatus | 这是标品状态机字段·ISV 不能动·只能写 ${ISV_FLAG}_renewbatch 反向引用 |
| ❌ 继承 ContractOtherExListPlugin 加 if/else 分支 | 会触发双继承死循环（PR-001）·必须并列挂多 plugin |
| ❌ AdminOrgHrUtils 改原方法影响其他场景 | 多场景共用·改造时新增方法不改原方法 |

详见 [`_antipatterns.json`](../_antipatterns.json)。

## 关联 PR 红线

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext2 扩展元数据 ✅
- **PR-008/009** 时序资料·hlcm_contract 是 HisModel·查询要带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法·本资产用 `beforeExecute/afterExecute` 两个 ✅
- **PR-011** BEC 事件中心·**本资产不发 BEC**·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)

## 关联文档

- 部署 SOP：[deploy_sop.md](deploy_sop.md)
- 业务规则：[02_business_rules.md](../../scenarios/core_hr_contract_renew/02_business_rules.md)
- 扩展点全景：[07_ext_points.md](../../scenarios/core_hr_contract_renew/07_ext_points.md)
- 定制方案：[06_customization_solutions.md](../../scenarios/core_hr_contract_renew/06_customization_solutions.md)
- W1 真扫：[`dcs_regression/passed/case_002_contract_renew/`](../../../dcs_regression/passed/case_002_contract_renew/)
