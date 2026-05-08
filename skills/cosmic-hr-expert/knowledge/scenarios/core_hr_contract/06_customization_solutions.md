# core_hr_contract · 定制方案（ISV 扩展真实模板）

> **场景**：劳动合同 - 新签/续签/变更（hlcm · 4 单据）（5 个子实体）
> **生成时间**：2026-04-30

## CS-01 · 给子实体加 ISV 自定义字段

**适用**：`hlcm_contract`, `hlcm_contractapply`, `hlcm_empprotocol`, `hlcm_otheragreements`, `hlcm_renewinquery`（任一子实体）

**步骤**：

```yaml
# 1. 在开发平台进入对应基础资料表单（设计器中）
# 2. 添加 ISV 字段（前缀 _ext 或 _isv 标识 ISV 私域字段）
# 3. 字段类型避免用 Required = true（标品字典设为非必填，避免破坏数据迁入）
# 4. 保存元数据 · 走标品 HRBaseDataTplEdit 默认渲染
```

**铁律**（ADR-009）：
- ❌ 禁改标品字段定义（字段名 / fieldType / refEntity）—— 见 11_*.md 跨云下游消费者
- ✅ 加 ISV 字段安全 · 标品和下游不感知
- ⚠️ 若 ISV 字段需要参与 save 链，挂 ISV OP 插件（见 CS-02）

## CS-02 · 给子实体加 ISV save 链业务规则

**适用**：所有 12 子实体的 save / delete / enable / disable 等标准 opKey

**步骤**：

```java
// 1. 写 ISV OP 插件（继承 HRDataBaseOp · @SdkPlugin 注解 · PR-001 并列挂）
@SdkPlugin
public class MyXxxIsvSaveOp extends kd.hr.hbp.opplugin.web.HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        super.onAddValidators(e);
        e.addValidator(new MyXxxIsvValidator());
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        // ISV 业务逻辑 · 数据已提交
        for (DynamicObject obj : e.getDataEntities()) {
            String number = obj.getString("number");
            // ... 走 ISV 私域服务
        }
    }
}
```

**对应 opKey**：在开发平台给目标 form 的 `save` opKey 注册本插件，RowKey > 100（PR-002 · ISV 在标品后执行）

## CS-03 · 给子实体加 ISV Validator（save 前校验）

```java
@SdkPlugin
public class MyXxxIsvValidator extends kd.hr.hbp.opplugin.web.HRDataBaseValidator {
    @Override
    public void validate() {
        for (ExtendedDataEntity row : this.dataEntities) {
            DynamicObject obj = row.getDataEntity();
            String number = obj.getString("number");
            if (HRStringUtils.isEmpty(number)) {
                this.addErrorMessage(row, "编码不能为空");
            }
        }
    }
}
```

## CS-04 · 给子实体加 ISV FormPlugin（UI 联动）

```java
@SdkPlugin
public class MyXxxIsvFormPlugin extends kd.hr.hbp.formplugin.web.HRDataBaseEdit {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        // ISV UI 联动
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);
        // 字段变化时联动
    }
}
```

**对应 form**：在目标基础资料 form 上**并列挂**本插件 · 不要继承标品 HRBaseDataTplEdit（PR-001）

## CS-05 · 跨云数据集成（基于本场景实体的下游）

> 本场景实体被多个云引用，详见 `11_upstream_downstream_logic.md` 自动注入的下游消费者段。
> 当 ISV 在 hr_hrmp 改实体时，必须确认对下游云的影响。

**真发证据 / 反编译示例（如有）**：
- `ContractBaseSaveOp.java` (12259 bytes)
- `ContractBaseSubmitEffectOp.java` (6282 bytes)

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 模板来源：admin_org_quick_maintenance（金标准）+ HRBaseDataTplEdit 标品共性
- ADR-009 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段

---

## CS-06 · 来自 case 反哺

> 来源 case: `case_002_contract_renew` · 反哺日期: 2026-05-02 · 工具: _kb_inject.py

## 业务场景

合同到期前的批量续签处理·扩展苍穹标品 `hlcm_contractapplyrenew`（合同续签申请单）·新建 ISV 表 `${ISV_FLAG}_hlcm_renewbatch`（批量续签记账）+ `${ISV_FLAG}_hlcm_contractap_ext2`（合同申请扩展 2）·覆盖完整的 op 生命周期：审批（audit）/ 保存 / 提交 / 流程审批（wfauditing）/ 反审批 / 失效 / 删除·联动 ext 表清理。

> **来源**：tdkw 同道伟业 ISV 真实战代码（D:/myproject/dcs/合同续签）·v3 ZERO_OMISSION case_002 反哺·已经过 `_dcs_compare.py v0.4` 评分 37/40·0 AP 命中

## 涉及的标品 form / ISV 表

- 标品：`hlcm_contractapplyrenew`（合同续签申请单·hlcm 模块）
- 标品：`haos_adminorghr`（行政组织 HR·HR 域 form）
- ISV 主表：`${ISV_FLAG}_hlcm_renewbatch`（批量续签记账·新建）
- ISV 扩展：`${ISV_FLAG}_hlcm_contractap_ext2`（合同申请单第 2 个 ISV 扩展）

## 实现要点

### 1. OP 插件（RenewBatchOpPlugin · 199 LOC）

**继承**：`kd.bos.entity.plugin.AbstractOperationServicePlugIn`

**重写 lifecycle 方法**：
- `onPreparePropertys`·扩展加载 ISV 字段
- `beforeExecuteOperationTransaction`·分支处理 opKey
- `afterExecuteOperationTransaction`·分支处理 opKey

**示例·onPreparePropertys 加 ISV 字段加载**：
```java
@Override
public void onPreparePropertys(PreparePropertysEventArgs args) {
    List<String> fieldKeys = args.getFieldKeys();
    fieldKeys.add("${ISV_FLAG}_itemids_tag");
    fieldKeys.add("${ISV_FLAG}_affiliationord");
}
```

**示例·beforeExecuteOperationTransaction 分支 opKey**：
```java
@Override
public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {
    super.beforeExecuteOperationTransaction(e);
    String operationKey = e.getOperationKey();
    DynamicObject[] dynamicObjects = e.getDataEntities();
    if ("audit".equals(operationKey)) {
        auditItems(dynamicObjects, e);
    } else if ("save".equals(operationKey) || "submit".equals(operationKey)) {
        checkDocumentOrg(dynamicObjects, e);
    }
}
```

### 2. Form 插件（RenewBatchBillPlugin）

**继承**：`kd.bos.form.plugin.AbstractFormPlugin`
**实现接口**：`HyperLinkClickListener`, `BeforeF7SelectListener`

负责 UI 联动·批量记账单据界面交互。

### 3. List 插件（ContractOtherExListPlugin）

**继承**：`kd.bos.list.plugin.AbstractListPlugin`
**实现接口**：`ClickListener`

处理列表多选 + 联动跳转·示例·列表点击加载单据：
```java
DynamicObject obj = BusinessDataServiceHelper.loadSingle(id, "hlcm_contractapplyrenew");
```

### 4. 联动 ext 表清理

失效（invalid）/ 删除（delete）opKey 时同步清 `${ISV_FLAG}_hlcm_renewbatch` 关联记录·避免脏数据。

## 注意事项 / 红线

- **ISV 前缀**：必须用 `${ISV_FLAG}_*` (ISV 开发商标识占位符·部署时全局替换)或 `${ISV_FLAG}.*` (ISV Java 包前缀·部署时全局替换)·不可用裸 `kd.*`·遵守 ISV 隔离铁律
- **op 切入点**：业务校验放 `beforeExecuteOperationTransaction`·不要塞 `addValidator`（AP-002 反模式）
- **事务保护**：用 `kd.bos.db.tx.TX` / `TXHandle`·**不要**自己 commit / rollback
- **AP 命中**：本实现 0 AP·可作为 v3 codegen 模板的"标准 op 插件"参考

## 测试 / 验证

- DCS 项目 build.gradle 提供完整可跑环境
- 反哺后跑 `_dcs_compare.py --case D:/myproject/dcs/合同续签 --verify-only` 验证 v3 recommend 命中"合同续签批量处理"

## 引用 / 来源

- DCS 项目：`D:/myproject/dcs/合同续签/`
- 主插件：
  - `tdkw/hr/hlcmext/plugin/RenewBatchOpPlugin.java`（199 LOC）
  - `tdkw/hr/hlcmext/plugin/RenewBatchBillPlugin.java`
  - `tdkw/hr/hlcmext/plugin/ContractOtherExListPlugin.java`
- 自动评分：标品复用 10 / ISV 隔离 10 / 跨云一致 7 / 代码质量 10 = 37/40
- v3 ZERO_OMISSION case_002·DCS-driven L2 反哺·2026-05-02

---

## 被引用·批量劳动合同续签（业务流型聚合）

> ⚡ 本场景作为下游被引用方·在 `core_hr_contract_renew` 业务流中扮演角色。

### 引用方
- **业务流场景**：[`core_hr_contract_renew`](../core_hr_contract_renew/) (核心人力 / 合同管理)
- **完整可复刻资产**：[`_assets/contract_renew_batch/`](../../_assets/contract_renew_batch/)

### 引用关系
被资产 _assets/contract_renew_batch/ 消费·详见那边的资产文档

### 跟本场景 ISV 扩展的关系
- ✅ 客户在本场景做 ISV 扩展（如加字段·改排序）·**不影响**资产消费
- ⚠️ 客户在本场景**改字段命名规范**·**必须**同步检查 `core_hr_contract_renew` 是否引用·避免破坏跨场景集成
