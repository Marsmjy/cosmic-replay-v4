# admin_org_erp_sync · 定制方案（ISV 扩展真实模板）

> **场景**：ERP 组织同步（1 个子实体）
> **生成时间**：2026-04-30

## CS-01 · 给子实体加 ISV 自定义字段

**适用**：`homs_orgdifftemp`（任一子实体）

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
- 本场景无 hr_hrmp 自有反编译类（标品 HRBaseDataTplEdit 通用）

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 模板来源：admin_org_quick_maintenance（金标准）+ HRBaseDataTplEdit 标品共性
- ADR-009 跨云穿透：见 `11_upstream_downstream_logic.md` 自动注入段